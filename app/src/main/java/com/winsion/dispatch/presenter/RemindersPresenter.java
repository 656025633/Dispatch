package com.winsion.dispatch.presenter;

import com.winsion.dispatch.bean.ReminderBean;
import com.winsion.dispatch.biz.listener.OnDeleteCompleteListener;
import com.winsion.dispatch.biz.listener.OnSearchCompleteListener;
import com.winsion.dispatch.biz.RemindersBiz;
import com.winsion.dispatch.db.RemindersDao;
import com.winsion.dispatch.iview.IRemindersView;

import java.util.List;

/**
 * Created by yalong on 2016/6/15.
 */
public class RemindersPresenter {

    private IRemindersView mView;
    private RemindersBiz mBiz;

    public RemindersPresenter(IRemindersView view, RemindersDao dao) {
        this.mView = view;
        this.mBiz = new RemindersBiz(dao);
    }

    /**
     * 查询本地数据库获取所有提醒事项
     *
     * @param isFinished
     */
    public void searchDbForData(boolean isFinished) {
        mBiz.searchDbForData(isFinished, new OnSearchCompleteListener() {
            @Override
            public void onSearchComplete(List<ReminderBean> list) {
                mView.updateUi(list);
            }
        });
    }

    public void delete(final ReminderBean bean) {
        mBiz.delete(bean, new OnDeleteCompleteListener() {
            @Override
            public void deleteSuccess() {
                mView.deleteSuccess(bean);
            }
        });
    }
}
