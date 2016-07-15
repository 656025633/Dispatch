package com.winsion.dispatch.fragment.CallFragment;


import com.winsion.dispatch.base.BasePresenter;
import com.winsion.dispatch.base.BaseView;
import com.winsion.dispatch.bean.OneKeyCallBean;

import java.util.List;

/**
 * Created by Mr.ZCM on 2016/6/8.
 * QQ:656025633
 * Company:com.winsion
 * Version:1.0
 * explain:
 */
public interface CallContract {
    interface UIView extends BaseView {
       public void showContractView(List<OneKeyCallBean> datas);
        
    }
    interface Presenter extends BasePresenter<UIView> {

    }
}
