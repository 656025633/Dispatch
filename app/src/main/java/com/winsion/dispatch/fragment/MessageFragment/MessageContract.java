package com.winsion.dispatch.fragment.MessageFragment;


import com.winsion.dispatch.base.BasePresenter;
import com.winsion.dispatch.base.BaseView;
import com.winsion.dispatch.bean.ContractBean;

import java.util.List;


/**
 * Created by Mr.ZCM on 2016/6/8.
 * QQ:656025633
 * Company:com.winsion
 * Version:1.0
 * explain:
 */
public interface MessageContract {
    public interface View extends BaseView {
        public void showMessagList(List<ContractBean> beans);
    }
    public interface Presenter extends BasePresenter<View> {

    }
}
