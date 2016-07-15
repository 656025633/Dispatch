package com.winsion.dispatch.bean;

import java.io.Serializable;

/**
 * Created by Mr.ZCM on 2016/7/13.
 * QQ:656025633
 * Company:winsion
 * Version:1.0
 * explain:
 */
public class OneKeyCallBean implements Serializable {
    private  String id;
    private String sipTellAddress;
    private String userName;

    public String getId() {
        return id;
    }

    public String getSipTellAddress() {
        return sipTellAddress;
    }

    public String getUserName() {
        return userName;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSipTellAddress(String sipTellAddress) {
        this.sipTellAddress = sipTellAddress;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
