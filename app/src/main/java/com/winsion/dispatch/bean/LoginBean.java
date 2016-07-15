package com.winsion.dispatch.bean;

/**
 * Created by yalong on 2016/6/28.
 */
public class LoginBean {
    private Meta meta;
    private UserBean data;

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public void setData(UserBean data) {
        this.data = data;
    }

    public Meta getMeta() {
        return meta;
    }

    public UserBean getData() {
        return data;
    }
}
