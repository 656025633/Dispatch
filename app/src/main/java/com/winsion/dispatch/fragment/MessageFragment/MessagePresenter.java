package com.winsion.dispatch.fragment.MessageFragment;

import com.winsion.dispatch.bean.ContractBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr.ZCM on 2016/6/8.
 * QQ:656025633
 * Company:com.winsion
 * Version:1.0
 * explain:
 */
public class MessagePresenter implements MessageContract.Presenter {
    private MessageContract.View view;
    @Override
    public void attachView(MessageContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {

    }
    public void loadData(){
        List<ContractBean> mDatas = new ArrayList<ContractBean>();
        for (int i = 0; i < 10; i++) {
            ContractBean bean = new ContractBean();
            bean.setName("X站长");
            bean.setType("北京西站");
            bean.setDatte("12:00");
            bean.setUserId(i+"");
            bean.setHeadIcon("https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=2244238741,1217077712&fm=5");
            mDatas.add(bean);
        }
        for (int i = 0; i < 10; i++) {
            ContractBean bean = new ContractBean();
            bean.setName("张昌明");
            bean.setType("北京南站");
            bean.setDatte("12:00");
            bean.setUserId(i+""+i);
            bean.setHeadIcon("https://ss0.baidu.com/6ONWsjip0QIZ8tyhnq/it/u=1853832225,307688784&fm=5");
            mDatas.add(bean);
        }
        view.showMessagList(mDatas);
    }
}
