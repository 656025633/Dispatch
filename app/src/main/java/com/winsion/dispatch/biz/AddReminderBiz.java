package com.winsion.dispatch.biz;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.winsion.dispatch.bean.FailedReminderBean;
import com.winsion.dispatch.bean.ReminderBean;
import com.winsion.dispatch.biz.listener.OnAddReminderListener;
import com.winsion.dispatch.constants.DbConstants;
import com.winsion.dispatch.constants.UserInfo;
import com.winsion.dispatch.db.RemindersDao;
import com.winsion.dispatch.receiver.ReminderReceiver;
import com.winsion.dispatch.utils.ConvertUtil;

/**
 * Created by admin on 2016/7/5.
 */
public class AddReminderBiz {

    private Context mContext;
    private AlarmManager alarmManager;

    public AddReminderBiz(Context context) {
        this.mContext = context;
        // 获取闹钟服务
        alarmManager = (AlarmManager) mContext.getSystemService(mContext.ALARM_SERVICE);
    }

    public void addReminder(String desc, String date, String time, Bundle extras, RemindersDao dao,
                            OnAddReminderListener listener) {
        String planDate = date + " " + time + ":00";
        // 添加到本地数据库(包括记录提交失败的数据库)
        final String currentTime = ConvertUtil.millisToDate(System.currentTimeMillis());
        if (extras != null) {
            ReminderBean bean = (ReminderBean) extras.getSerializable("bean");
            // 更新操作
            // 先更新本地数据库
            ContentValues values = new ContentValues();
            values.put("date", currentTime);
            values.put("planDate", planDate);
            values.put("content", desc);
            dao.update(DbConstants.REMINDERS_TABLE_NAME, values, "date=?", new String[]{bean.getDate()});
            // 更新失败库中该条提醒事项的状态
            int update = dao.update(DbConstants.FAILED_REMINDERS_TABLE_NAME, values, "date=?", new String[]{bean.getDate()});
            if (update == 0) {
                FailedReminderBean failedReminderBean = new FailedReminderBean(bean.getId(), UserInfo.getId(),
                        "update", currentTime, bean.getFinished(), desc, planDate);
                dao.insertToFailed(failedReminderBean);
            }
            if (!bean.getFinished()) {
                // 取消之前的闹钟
                cancelClock(bean);
                // 开启新的闹钟
                bean.setDate(currentTime);
                bean.setPlanDate(planDate);
                bean.setContent(desc);
                startReminder(bean);
            }
            // 发送添加成功广播
            Intent intent = new Intent();
            intent.setAction("UPDATE_UI");
            mContext.sendBroadcast(intent);
            listener.onUpdateComplete();
        } else {
            // 添加操作
            final ReminderBean bean = new ReminderBean("blank", desc, false, currentTime, planDate);
            FailedReminderBean failedReminderBean = new FailedReminderBean("blank", UserInfo.getId(),
                    "add", currentTime, false, desc, planDate);
            dao.insertToFailed(failedReminderBean);
            dao.insert(bean);
            // 开启闹钟
            startReminder(bean);
            // 发送添加成功广播
            Intent intent = new Intent();
            intent.setAction("UPDATE_UI");
            mContext.sendBroadcast(intent);
            listener.onAddComplete();
        }
    }

    /**
     * 取消闹钟
     *
     * @param bean
     */
    private void cancelClock(ReminderBean bean) {
        Intent intent = new Intent(mContext, ReminderReceiver.class);
        intent.putExtra("bean", bean);
        long requestCode = ConvertUtil.dateToMillis(bean.getPlanDate());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext,
                (int) requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);
    }

    /**
     * 开启闹钟
     *
     * @param bean
     */
    private void startReminder(ReminderBean bean) {
        Intent intent = new Intent(mContext, ReminderReceiver.class);
        intent.putExtra("bean", bean);
        long requestCode = ConvertUtil.dateToMillis(bean.getPlanDate());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, (int) requestCode,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // API19之前set设置闹钟会精准提醒，API19之后需要用setExact
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, ConvertUtil.dateToMillis(bean.getPlanDate())
                    , pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, ConvertUtil.dateToMillis(bean.getPlanDate()),
                    pendingIntent);
        }
    }
}
