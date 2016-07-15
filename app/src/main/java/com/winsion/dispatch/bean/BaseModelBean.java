package com.winsion.dispatch.bean;

import java.io.Serializable;

/**
 * Created by Mr.ZCM on 2016/7/12.
 * QQ:656025633
 * Company:winsion
 * Version:1.0
 * explain:
 */
public class BaseModelBean<T>  implements Serializable{
    private Meta meta;
    private T data;

    public Meta getMeta() {
        return meta;
    }

    public T getData() {
        return data;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public void setData(T data) {
        this.data = data;
    }
}
