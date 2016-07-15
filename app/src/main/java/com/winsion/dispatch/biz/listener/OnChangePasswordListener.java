package com.winsion.dispatch.biz.listener;

/**
 * Created by admin on 2016/7/5.
 */
public interface OnChangePasswordListener {
    void onPrepare();

    void onFailed(String msg);
}
