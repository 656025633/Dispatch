package com.winsion.dispatch.bean;

import java.io.Serializable;

/**
 * Created by Mr.ZCM on 2016/6/6.
 * QQ:656025633
 * Company:com.winsion
 * Version:1.0
 * explain:
 */
public class BaseParent implements Serializable{
    boolean isHead;

    public void setHead(boolean head) {
        isHead = head;
    }

    public boolean isHead() {
        return isHead;
    }
}
