package com.winsion.dispatch.bean;

import java.util.List;

/**
 * Created by admin on 2016/7/4.
 */
public class ComConfigBean {
    private Meta meta;
    private List<ConfigBean> data;

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public void setData(List<ConfigBean> data) {
        this.data = data;
    }

    public Meta getMeta() {
        return meta;
    }

    public List<ConfigBean> getData() {
        return data;
    }
}
