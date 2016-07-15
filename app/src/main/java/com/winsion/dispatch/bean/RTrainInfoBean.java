package com.winsion.dispatch.bean;

import java.util.List;

/**
 * Created by admin on 2016/7/6.
 */
public class RTrainInfoBean {
    private Meta meta;

    private List<TrainInfoBean> data;

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public Meta getMeta() {
        return this.meta;
    }

    public void setData(List<TrainInfoBean> data) {
        this.data = data;
    }

    public List<TrainInfoBean> getData() {
        return this.data;
    }
}
