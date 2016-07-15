package com.winsion.dispatch.iview;

import com.winsion.dispatch.bean.TrainInfoBean;
import com.winsion.dispatch.bean.UserStation;

import java.util.List;

/**
 * Created by yalong on 2016/6/14.
 */
public interface IOperationControlView {
    /**
     * 更新数据，运行在子线程
     *
     * @param
     */
    void updateData(List<TrainInfoBean> beanList);

    void dataInitComplete(List<UserStation> data);
}
