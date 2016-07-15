package com.winsion.dispatch.activity.chat;


import com.winsion.dispatch.base.BasePresenter;
import com.winsion.dispatch.base.BaseView;
import com.winsion.dispatch.bean.MessageBean;

import java.util.List;

/**
 * Created by Mr.ZCM on 2016/6/13.
 * QQ:656025633
 * Company:winsion
 * Version:1.0
 * explain:
 */
public class ChatContract {
    interface View extends BaseView {
        void showChatContent(List<MessageBean> messages);

    }
    interface Presenter extends BasePresenter<View> {

    }

}
