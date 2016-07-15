package com.winsion.dispatch.activity.contract;


import com.winsion.dispatch.base.BasePresenter;
import com.winsion.dispatch.base.BaseView;
import com.winsion.dispatch.bean.RelateUserBean;

import java.util.List;

/**
 * Created by Mr.ZCM on 2016/6/13.
 * QQ:656025633
 * Company:winsion
 * Version:1.0
 * explain:
 */
public class ContractContract {
    interface View extends BaseView {
        void showContractContent(List<RelateUserBean> messages);

    }
    interface Presenter extends BasePresenter<View> {

    }
}
