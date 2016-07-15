package com.winsion.dispatch.db;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.winsion.dispatch.bean.FailedReminderBean;
import com.winsion.dispatch.bean.ReminderBean;
import com.winsion.dispatch.bean.UserNoteAddBean;
import com.winsion.dispatch.bean.UserNoteQueryAllBean;
import com.winsion.dispatch.constants.DbConstants;
import com.winsion.dispatch.constants.NetConstants;
import com.winsion.dispatch.constants.UserInfo;
import com.winsion.dispatch.receiver.ReminderReceiver;
import com.winsion.dispatch.utils.ConvertUtil;
import com.winsion.dispatch.utils.JsonUtil;
import com.winsion.dispatch.utils.OkHttpUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yalong on 2016/5/31.
 */
public class RemindersDao {

    private RemindersDbHelper helper;
    private SQLiteDatabase db;
    private Context mContext;
    private static RemindersDao instance;

    private RemindersDao(Context context) {
        this.mContext = context;
        helper = new RemindersDbHelper(context);
        db = helper.getReadableDatabase();
    }

    public static RemindersDao getInstance(Context context) {
        if (instance == null) {
            synchronized (RemindersDao.class) {
                if (instance == null) {
                    instance = new RemindersDao(context);
                }
            }
        }
        return instance;
    }

    /**
     * 查询所有提醒事项
     *
     * @param isFinished
     * @return
     */
    public List<ReminderBean> queryAll(boolean isFinished) {
        int state = isFinished ? 1 : 0;
        Cursor cursor = db.query(DbConstants.REMINDERS_TABLE_NAME, null, "finished=? and userid=?",
                new String[]{state + "", UserInfo.getId()}, null, null, null);
        List<ReminderBean> list = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String noteId = cursor.getString(0);
                String content = cursor.getString(1);
                boolean finished = cursor.getInt(2) == 0 ? false : true;
                String date = cursor.getString(3);
                String planDate = cursor.getString(4);
                ReminderBean bean = new ReminderBean(noteId, content, finished, date, planDate);
                list.add(bean);
            }
        }
        return list;
    }

    public String queryOneNoteId(String date) {
        Cursor cursor = db.query(DbConstants.REMINDERS_TABLE_NAME, null, "date=?",
                new String[]{date}, null, null, null);
        String id = null;
        if (cursor != null) {
            while (cursor.moveToNext()) {
                id = cursor.getString(0);
            }
        }
        return id;
    }

    public void queryAllFailed() {
        Cursor cursor = db.query(DbConstants.FAILED_REMINDERS_TABLE_NAME, null, null, null, null, null, null);
        final List<FailedReminderBean> list = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String noteId = cursor.getString(0);
                String content = cursor.getString(1);
                boolean finished = cursor.getInt(2) == 0 ? false : true;
                String date = cursor.getString(3);
                String planDate = cursor.getString(4);
                String userId = cursor.getString(5);
                String state = cursor.getString(6);
                FailedReminderBean bean = new FailedReminderBean(noteId, userId, state, date, finished, content, planDate);
                list.add(bean);
            }
        }
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                RequestBody body = null;
                final FailedReminderBean bean = list.get(i);
                String state = bean.getState();
                String requestState = null;
                switch (state) {
                    case "add":
                        body = new FormEncodingBuilder()
                                .add("userId", bean.getUserid())
                                .add("content", bean.getContent())
                                .add("planDate", bean.getPlanDate())
                                .build();
                        requestState = NetConstants.ADD;
                        break;
                    case "delete":
                        body = new FormEncodingBuilder()
                                .add("userNoteId", bean.getId())
                                .build();
                        requestState = NetConstants.DELETE;
                        break;
                    case "update":
                        body = new FormEncodingBuilder()
                                .add("userNoteId", bean.getId())
                                .add("isFinished", bean.isFinished() + "")
                                .add("content", bean.getContent())
                                .build();
                        requestState = NetConstants.UPDATE;
                        break;
                }
                Request request = new Request.Builder()
                        .url(NetConstants.HOST + NetConstants.USER_NOTE + requestState)
                        .post(body)
                        .build();
                final int finalI = i;
                OkHttpUtil.enqueue(request, new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        if (response.isSuccessful() && response.code() == 200) {
                            // 如果是添加操作，更新本地该提醒的noteId
                            if (TextUtils.equals(bean.getState(), "add")) {
                                String string = response.body().string();
                                UserNoteAddBean addBean = JsonUtil.json2Bean(string, UserNoteAddBean.class);
                                if (addBean.getMeta().getCode() == 200) {
                                    ReminderBean bean1 = addBean.getData();
                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put("noteid", bean1.getId());
                                    db.update(DbConstants.REMINDERS_TABLE_NAME, contentValues, "date=?", new String[]{bean.getDate()});
                                    if (bean.isFinished()) {
                                        ContentValues values = new ContentValues();
                                        values.put("state", "update");
                                        values.put("noteid", bean1.getId());
                                        db.update(DbConstants.FAILED_REMINDERS_TABLE_NAME, values, "date=?", new String[]{bean.getDate()});
                                        RequestBody body = new FormEncodingBuilder()
                                                .add("userNoteId", bean1.getId())
                                                .add("isFinished", bean.isFinished() + "")
                                                .add("content", bean.getContent())
                                                .build();
                                        Request request = new Request.Builder()
                                                .url(NetConstants.HOST + NetConstants.USER_NOTE + NetConstants.UPDATE)
                                                .post(body)
                                                .build();
                                        OkHttpUtil.enqueue(request, new Callback() {
                                            @Override
                                            public void onFailure(Request request, IOException e) {

                                            }

                                            @Override
                                            public void onResponse(Response response) throws IOException {
                                                if (response.isSuccessful() && response.code() == 200) {
                                                    // 更新成功，删除记录
                                                    db.delete(DbConstants.FAILED_REMINDERS_TABLE_NAME, "date=?", new String[]{bean.getDate()});
                                                    // 是提交的最后一个记录则同步服务器数据
                                                    if (finalI == list.size() - 1) {
                                                        synchroData(true);
                                                        synchroData(false);
                                                    }
                                                }
                                            }
                                        });
                                    } else {
                                        // 提交服务器成功，从本地数据库中删除
                                        db.delete(DbConstants.FAILED_REMINDERS_TABLE_NAME, "date=?", new String[]{bean.getDate()});
                                        // 是提交的最后一个记录则同步服务器数据
                                        if (finalI == list.size() - 1) {
                                            synchroData(true);
                                            synchroData(false);
                                        }
                                    }
                                }
                            } else {
                                // 提交服务器成功，从本地数据库中删除
                                db.delete(DbConstants.FAILED_REMINDERS_TABLE_NAME, "date=?", new String[]{bean.getDate()});
                                // 是提交的最后一个记录则同步服务器数据
                                if (finalI == list.size() - 1) {
                                    synchroData(true);
                                    synchroData(false);
                                }
                            }
                        }
                    }
                });
            }
        } else {
            synchroData(true);
            synchroData(false);
        }
    }

    public void synchroData(boolean isFinished) {
        RequestBody body = new FormEncodingBuilder()
                .add("userId", UserInfo.getId())
                .add("isFinished", isFinished + "")
                .add("api_key", UserInfo.API_KEY).build();
        Request request = new Request.Builder()
                .url(NetConstants.HOST + NetConstants.USER_NOTE + NetConstants.QUERY_ALL)
                .post(body)
                .build();
        final Intent intent = new Intent();
        intent.setAction("UPDATE_UI");
        OkHttpUtil.enqueue(request, new Callback() {
            @Override
            public void onResponse(Response response) throws IOException {
                String string = response.body().string();
                UserNoteQueryAllBean allBean = JsonUtil.json2Bean(string, UserNoteQueryAllBean.class);
                int code = allBean.getMeta().getCode();
                if (code == 200) {
                    List<ReminderBean> data = allBean.getData();
                    if (data.size() > 0) {
                        synchroData(data);
                    }
                    // 同步完成刷新数据
                    mContext.sendBroadcast(intent);
                }
            }

            @Override
            public void onFailure(Request request, IOException e) {
            }
        });
    }

    public FailedReminderBean queryFailedReminder(String date) {
        Cursor cursor = db.query(DbConstants.FAILED_REMINDERS_TABLE_NAME, null, "date=?", new String[]{date}, null, null, null);
        FailedReminderBean bean = null;
        if (cursor != null && cursor.moveToNext()) {
            String noteId = cursor.getString(0);
            String content = cursor.getString(1);
            boolean finished = cursor.getInt(2) == 0 ? false : true;
            String planDate = cursor.getString(4);
            String userId = cursor.getString(5);
            String state = cursor.getString(6);
            bean = new FailedReminderBean(noteId, userId, state, date, finished, content, planDate);
        }
        return bean;
    }

    /**
     * 添加一条提醒事项
     *
     * @param reminderBean
     */
    public void insert(ReminderBean reminderBean) {
        String sql = "insert into " + DbConstants.REMINDERS_TABLE_NAME +
                "(noteid,content,finished,date,plandate,userid) values (?,?,?,?,?,?)";
        db.execSQL(sql, new Object[]{reminderBean.getId(), reminderBean.getContent(),
                reminderBean.getFinished() ? 1 : 0, reminderBean.getDate(),
                reminderBean.getPlanDate(), UserInfo.getId()});
    }

    /**
     * 将服务器返回的数据与本地数据做同步
     */
    public void synchroData(List<ReminderBean> list) {
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(mContext.ALARM_SERVICE);
        for (int i = 0; i < list.size(); i++) {
            ReminderBean bean = list.get(i);
            Cursor cursor = db.query(DbConstants.REMINDERS_TABLE_NAME, null, "noteid=?",
                    new String[]{bean.getId()}, null, null, null);
            if (!cursor.moveToNext()) {
                insert(bean);
                long planDate = ConvertUtil.dateToMillis(bean.getPlanDate());
                if (planDate > System.currentTimeMillis()) {
                    Intent intent = new Intent(mContext, ReminderReceiver.class);
                    intent.putExtra("bean", bean);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, planDate, pendingIntent);
                }
            }
        }
    }

    public void insertToFailed(FailedReminderBean bean) {
        String sql = "insert into " + DbConstants.FAILED_REMINDERS_TABLE_NAME +
                "(noteid,content,finished,date,plandate,userid,state) values (?,?,?,?,?,?,?)";
        db.execSQL(sql, new Object[]{bean.getId(), bean.getContent(),
                bean.isFinished() ? 1 : 0, bean.getDate(),
                bean.getPlanDate(), UserInfo.getId(), bean.getState()});
    }

    public int update(String tableName, ContentValues values, String whereClause, String[] whereArgs) {
        int update = db.update(tableName, values, whereClause, whereArgs);
        return update;
    }

    /**
     * 删除一条提醒事项
     *
     * @param table
     * @param whereClause
     * @param whereArgs
     */
    public void delete(String table, String whereClause, String[] whereArgs) {
        db.delete(table, whereClause, whereArgs);
    }

    /**
     * 关闭数据库
     */
    public void close() {
        db.close();
    }
}
