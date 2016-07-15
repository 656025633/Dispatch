package com.winsion.dispatch.bean;

/**
 * Created by yalong on 2016/6/22.
 */
public class UserNoteAddBean {
    private Meta meta;

    private ReminderBean data;

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public Meta getMeta() {
        return this.meta;
    }

    public void setData(ReminderBean reminders) {
        this.data = reminders;
    }

    public ReminderBean getData() {
        return this.data;
    }
}
