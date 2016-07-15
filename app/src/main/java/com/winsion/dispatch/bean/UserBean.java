package com.winsion.dispatch.bean;

/**
 * Created by yalong on 2016/6/28.
 */
public class UserBean {
    private String id;
    private String username;
    private String authToken;
    private String lastOperateTime;
    private String ssId;

    public void setLastOperateTime(String lastOperateTime) {
        this.lastOperateTime = lastOperateTime;
    }

    public void setSsId(String ssId) {
        this.ssId = ssId;
    }

    public String getLastOperateTime() {
        return lastOperateTime;
    }

    public String getSsId() {
        return ssId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getId() {
        return id;
    }
    
    public String getUsername() {
        return username;
    }

    public String getAuthToken() {
        return authToken;
    }
}
