package com.winsion.dispatch.biz.listener;

import com.winsion.dispatch.bean.ReminderBean;

import java.util.List;

/**
 * Created by yalong on 2016/6/15.
 */
public interface OnSearchCompleteListener {
    void onSearchComplete(List<ReminderBean> list);
}
