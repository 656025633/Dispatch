package com.winsion.dispatch.iview;

import com.winsion.dispatch.bean.ReminderBean;

import java.util.List;

/**
 * Created by yalong on 2016/6/15.
 */
public interface IRemindersView {
    void updateUi(List<ReminderBean> list);

    void deleteSuccess(ReminderBean bean);
}
