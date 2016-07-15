package com.winsion.dispatch.presenter;

import android.content.Context;
import android.os.Bundle;

import com.winsion.dispatch.biz.AddReminderBiz;
import com.winsion.dispatch.biz.listener.OnAddReminderListener;
import com.winsion.dispatch.db.RemindersDao;
import com.winsion.dispatch.iview.IAddReminderView;

/**
 * Created by admin on 2016/7/5.
 */
public class AddReminderPresenter {

    private IAddReminderView mView;
    private AddReminderBiz mBiz;

    public AddReminderPresenter(IAddReminderView view, Context context) {
        this.mView = view;
        this.mBiz = new AddReminderBiz(context);
    }

    public void addReminder() {
        String desc = mView.getDesc();
        String date = mView.getDate();
        String time = mView.getTime();
        Bundle extras = mView.getExtras();
        RemindersDao dao = mView.getRemindersDao();
        mBiz.addReminder(desc, date, time, extras, dao, new OnAddReminderListener() {
            @Override
            public void onUpdateComplete() {
                mView.onUpdateComplete();
            }

            @Override
            public void onAddComplete() {
                mView.onAddComplete();
            }
        });
    }
}
