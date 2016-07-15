package com.winsion.dispatch.fragment.operation;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;

import com.winsion.dispatch.R;
import com.winsion.dispatch.activity.AddReminderActivity;
import com.winsion.dispatch.base.BaseFragment;
import com.winsion.dispatch.base.BaseLvAdapter;
import com.winsion.dispatch.bean.FailedReminderBean;
import com.winsion.dispatch.bean.ReminderBean;
import com.winsion.dispatch.constants.DbConstants;
import com.winsion.dispatch.constants.UserInfo;
import com.winsion.dispatch.db.RemindersDao;
import com.winsion.dispatch.iview.IRemindersView;
import com.winsion.dispatch.presenter.RemindersPresenter;
import com.winsion.dispatch.receiver.ReminderReceiver;
import com.winsion.dispatch.utils.ConvertUtil;
import com.winsion.dispatch.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by yalong on 2016/6/15.
 */
@SuppressLint("ValidFragment")
public class RemindersListFragment extends BaseFragment implements IRemindersView, AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener {

    @InjectView(R.id.lv_reminders_list)
    ListView lvRemindersList;
    @InjectView(R.id.pb_synchronize)
    ProgressBar pbSynchronize;
    @InjectView(R.id.ll_container)
    LinearLayout llContainer;

    private RemindersPresenter mPresenter;
    private List<ReminderBean> mList = new ArrayList<>();
    private BaseLvAdapter<ReminderBean> mAdapter;
    private boolean mIsFinished;
    private AlarmManager alarmManager;
    private BroadcastReceiver broadcastReceiver;
    private RemindersDao mDao;

    @SuppressLint("ValidFragment")
    public RemindersListFragment(boolean isFinished, RemindersDao dao) {
        this.mIsFinished = isFinished;
        this.mDao = dao;
    }

    @Override
    public View setContentView() {
        mPresenter = new RemindersPresenter(this, mDao);
        alarmManager = (AlarmManager) mContext.getSystemService(mContext.ALARM_SERVICE);
        return mLayoutInflater.inflate(R.layout.operation_reminders_list, null);
    }

    /**
     * 点击添加按钮跳转至添加提醒事项界面
     */
    @OnClick(R.id.btn_add)
    public void onClick() {
        Intent intent = new Intent(mContext, AddReminderActivity.class);
        startActivity(intent);
    }

    @Override
    protected void init() {
        initAdapter();
        // 查询数据库获取数据
        mPresenter.searchDbForData(mIsFinished);
        // 注册广播接收者接受数据变化的广播，刷新界面
        registerReceiver();
    }

    private void initAdapter() {
        final long currentTimeMillis = System.currentTimeMillis();
        mAdapter = new BaseLvAdapter<ReminderBean>(mContext, mList, R.layout.reminders_list_item) {
            @Override
            public void convert(ViewHolder viewHolder, final ReminderBean bean) {
                String date = bean.getPlanDate();
                String[] split = date.split(" ");
                String[] split1 = split[0].split("-");
                // 年
                viewHolder.setText(R.id.tv_year, split1[0]);
                // 日期
                viewHolder.setText(R.id.tv_date, split1[1] + "月" + split1[2]);
                // 时间
                viewHolder.setText(R.id.tv_time, split[1].substring(0, 5));
                // 事项描述
                viewHolder.setText(R.id.tv_desc, bean.getContent());
                // 删除按钮点击事件
                viewHolder.setOnClickListener(R.id.btn_delete, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPresenter.delete(bean);
                    }
                });
                viewHolder.setVisibility(R.id.tv_time_out, ConvertUtil.dateToMillis(bean.getPlanDate())
                        < currentTimeMillis && !bean.getFinished() ? true : false);
            }
        };
        lvRemindersList.setAdapter(mAdapter);
        if (!mIsFinished) {
            lvRemindersList.setOnItemLongClickListener(this);
        }
        lvRemindersList.setOnItemClickListener(this);
    }

    /**
     * 注册广播接受者
     */
    private void registerReceiver() {
        // 收到广播刷新界面
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // 收到广播刷新界面
                mPresenter.searchDbForData(mIsFinished);
                pbSynchronize.setVisibility(View.INVISIBLE);
                llContainer.setVisibility(View.VISIBLE);
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction("UPDATE_UI");
        mContext.registerReceiver(broadcastReceiver, filter);
    }

    @Override
    public void updateUi(List<ReminderBean> list) {
        if (list != null) {
            mList.clear();
            ArrayList<ReminderBean> timeOut = new ArrayList<>();
            long currentTimeMillis = System.currentTimeMillis();
            for (int i = 0; i < list.size(); i++) {
                ReminderBean reminder = list.get(i);
                if (ConvertUtil.dateToMillis(reminder.getPlanDate()) < currentTimeMillis && !reminder.getFinished()) {
                    // 提醒时间小于当前时间且状态为未完成，判断为超时
                    timeOut.add(reminder);
                } else {
                    mList.add(reminder);
                }
            }
            Collections.sort(mList);
            Collections.sort(timeOut);
            mList.addAll(0, timeOut);
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    /**
     * 删除一条提醒成功
     */
    @Override
    public void deleteSuccess(final ReminderBean bean) {
        // 删除成功，刷新数据
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mList.remove(bean);
                if (!bean.getFinished()) {
                    cancelClock(bean);
                }
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private void cancelClock(ReminderBean bean) {
        Intent intent = new Intent(mContext, ReminderReceiver.class);
        intent.putExtra("bean", bean);
        long requestCode = ConvertUtil.dateToMillis(bean.getPlanDate());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext,
                (int) requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);
    }

    /**
     * 长按条目可以手动置为已完成
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     * @return
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        View inflate = View.inflate(mContext, R.layout.popup_window, null);
        final PopupWindow popupWindow = new PopupWindow(inflate, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
        Button btnDelete = (Button) inflate.findViewById(R.id.btn_delete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 这里做更改状态操作
                update(mList.get(position));
                popupWindow.dismiss();
            }
        });
        inflate.measure(0, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            popupWindow.showAsDropDown(view, view.getWidth() / 2 - inflate.getMeasuredWidth() / 2,
                    0, Gravity.CENTER);
        } else {
            popupWindow.showAsDropDown(view);
        }
        return true;
    }

    private void update(ReminderBean bean) {
        // 先更新本地数据库
        ContentValues values = new ContentValues();
        values.put("finished", "1");
        mDao.update(DbConstants.REMINDERS_TABLE_NAME, values, "date=?", new String[]{bean.getDate()});
        // 发送广播刷新界面
        Intent intent = new Intent();
        intent.setAction("UPDATE_UI");
        mContext.sendBroadcast(intent);
        // 更新失败库中该条提醒事项的状态为已完成
        int update = mDao.update(DbConstants.FAILED_REMINDERS_TABLE_NAME, values, "date=?", new String[]{bean.getDate()});
        if (update == 0) {
            String noteId = mDao.queryOneNoteId(bean.getDate());
            mDao.insertToFailed(new FailedReminderBean(noteId, UserInfo.getId(), "update",
                    bean.getDate(), true, bean.getContent(), bean.getPlanDate()));
        }
    }

    /**
     * 点击条目可以进行修改
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(mContext, AddReminderActivity.class);
        ReminderBean value = mList.get(position);
        intent.putExtra("bean", value);
        startActivity(intent);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        // 解绑广播接收者
        mContext.unregisterReceiver(broadcastReceiver);
    }
}
