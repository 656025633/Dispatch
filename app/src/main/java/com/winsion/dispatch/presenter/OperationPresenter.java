package com.winsion.dispatch.presenter;

import com.winsion.dispatch.bean.TrainInfoBean;
import com.winsion.dispatch.bean.UserStation;
import com.winsion.dispatch.biz.OperationControlBiz;
import com.winsion.dispatch.biz.listener.OnInitSpinnerListener;
import com.winsion.dispatch.biz.listener.OnResponseListener;
import com.winsion.dispatch.iview.IOperationControlView;

import java.util.List;

/**
 * Created by yalong on 2016/6/14.
 */
public class OperationPresenter {

    private IOperationControlView mView;
    private OperationControlBiz mBiz;

    public OperationPresenter(IOperationControlView view) {
        this.mView = view;
        this.mBiz = new OperationControlBiz();
    }

    /**
     * 请求网络
     */
    public void requestInternet(String id, String type) {
        mBiz.requestInternet(id, type, new OnResponseListener() {
            @Override
            public void onSuccess(List<TrainInfoBean> beanList) {
                mView.updateData(beanList);
            }

            @Override
            public void onFailed(Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 初始化车站spinner数据源
     */
    public void initSpinnerData() {
        mBiz.initSpinnerData(new OnInitSpinnerListener() {
            @Override
            public void onSuccess(List<UserStation> data) {
                mView.dataInitComplete(data);
            }
        });
    }
}
