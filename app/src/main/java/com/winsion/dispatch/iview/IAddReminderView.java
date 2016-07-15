package com.winsion.dispatch.iview;

import android.os.Bundle;

import com.winsion.dispatch.db.RemindersDao;

/**
 * Created by admin on 2016/7/5.
 */
public interface IAddReminderView {
    String getDesc();
    String getTime();
    String getDate();
    Bundle getExtras();
    RemindersDao getRemindersDao();
    void onUpdateComplete();
    void onAddComplete();
}
