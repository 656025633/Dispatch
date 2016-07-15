package com.winsion.dispatch.bean;

/**
 * Created by yalong on 2016/5/31.
 */
public class FailedReminderBean {
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
    // 操作状态
    private String state;
    // userId
    private String userid;

    public FailedReminderBean(String id, String userid, String state, String date, boolean finished, String content, String planDate) {
        this.id = id;
        this.userid = userid;
        this.state = state;
        this.date = date;
        this.finished = finished;
        this.content = content;
        this.planDate = planDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPlanDate() {
        return planDate;
    }

    public void setPlanDate(String planDate) {
        this.planDate = planDate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
