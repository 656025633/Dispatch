package com.winsion.dispatch.biz.listener;

import com.winsion.dispatch.bean.TrainInfoBean;

import java.util.List;

/**
 * Created by yalong on 2016/6/14.
 */
public interface OnResponseListener {
    /**
     * 运行在子线程
     *
     * @param
     */
    void onSuccess(List<TrainInfoBean> beanList);

    /**
     * 运行在子线程
     */
    void onFailed(Exception e);
}
