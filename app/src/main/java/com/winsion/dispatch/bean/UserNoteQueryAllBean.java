package com.winsion.dispatch.bean;

import java.util.List;

/**
 * Created by yalong on 2016/6/22.
 */
public class UserNoteQueryAllBean {
    private Meta meta;

    private List<ReminderBean> data;

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public Meta getMeta() {
        return this.meta;
    }

    public void setData(List<ReminderBean> reminders) {
        this.data = reminders;
    }

    public List<ReminderBean> getData() {
        return this.data;
    }
}
