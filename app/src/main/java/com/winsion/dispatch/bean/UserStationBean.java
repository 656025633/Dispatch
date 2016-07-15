package com.winsion.dispatch.bean;

import java.util.List;

/**
 * Created by admin on 2016/7/6.
 */
public class UserStationBean {
    private Meta meta;

    private List<UserStation> data;

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public Meta getMeta() {
        return this.meta;
    }

    public void setData(List<UserStation> data) {
        this.data = data;
    }

    public List<UserStation> getData() {
        return this.data;
    }
}
