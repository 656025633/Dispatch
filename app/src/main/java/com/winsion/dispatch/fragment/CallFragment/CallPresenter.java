package com.winsion.dispatch.fragment.CallFragment;

import com.winsion.dispatch.bean.FriendBean;
import com.winsion.dispatch.bean.OneKeyCallBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr.ZCM on 2016/6/8.
 * QQ:656025633
 * Company:com.winsion
 * Version:1.0
 * explain:
 */
public class CallPresenter implements CallContract.Presenter{
    private CallContract.UIView view;
    @Override
    public void attachView(CallContract.UIView view) {
        this.view = view;
    }

    @Override
    public void detachView() {

    }
    //从网络获取数据
    public void loadData(){
        //获取到数据之后
        List<OneKeyCallBean> datas = new ArrayList<OneKeyCallBean>();
        for (int i = 0; i <20 ; i++) {

            datas.add(bean);
        }
        view.showContractView(datas);
    }
}
