package com.winsion.dispatch.bean;

import com.winsion.dispatch.utils.ConvertUtil;

import java.io.Serializable;

/**
 * Created by yalong on 2016/5/31.
 */
public class ReminderBean implements Comparable, Serializable {
    // noteId
    private String id;
    // 提醒内容
    private String content;
    // 是否已完成
    private boolean finished;
    // 添加时间
    private String date;
    // 提醒时间
    private String planDate;                                                

    public ReminderBean(String id, String content, boolean finished, String date, String planDate) {
        this.id = id;
        this.content = content;
        this.finished = finished;
        this.date = date;
        this.planDate = planDate;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return this.content;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public boolean getFinished() {
        return this.finished;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return this.date;
    }

    public void setPlanDate(String planDate) {
        this.planDate = planDate;
    }

    public String getPlanDate() {
        return this.planDate;
    }

    @Override
    public int compareTo(Object another) {
        ReminderBean one = (ReminderBean) another;
        int value = ConvertUtil.dateToMillis(this.getPlanDate()) < ConvertUtil.dateToMillis(one.getPlanDate())
                ? -1 : 1;
        return value;
    }
}
