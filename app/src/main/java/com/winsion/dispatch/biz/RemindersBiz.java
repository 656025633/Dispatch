package com.winsion.dispatch.biz;

import android.text.TextUtils;

import com.winsion.dispatch.bean.FailedReminderBean;
import com.winsion.dispatch.bean.ReminderBean;
import com.winsion.dispatch.biz.listener.OnDeleteCompleteListener;
import com.winsion.dispatch.biz.listener.OnSearchCompleteListener;
import com.winsion.dispatch.constants.DbConstants;
import com.winsion.dispatch.constants.UserInfo;
import com.winsion.dispatch.db.RemindersDao;

import java.util.List;

/**
 * Created by yalong on 2016/6/15.
 */
public class RemindersBiz {

    private RemindersDao mDao;

    public RemindersBiz(RemindersDao dao) {
        this.mDao = dao;
    }

    /**
     * 查询所有提醒事项
     *
     * @param finished
     * @param listener
     */
    public void searchDbForData(boolean finished, OnSearchCompleteListener listener) {
        List<ReminderBean> list = mDao.queryAll(finished);
        listener.onSearchComplete(list);
    }

    /**
     * 删除一条提醒事项
     */
    public void delete(ReminderBean bean, final OnDeleteCompleteListener listener) {
        String id = bean.getId();
        if (TextUtils.equals("blank", id)) {
            id = mDao.queryOneNoteId(bean.getDate());
        }
        // 先删除本地数据库中的记录
        mDao.delete(DbConstants.REMINDERS_TABLE_NAME, "date=?", new String[]{bean.getDate()});
        // 刷新界面
        listener.deleteSuccess();
        // 查询失败记录中是否有该条提醒，有：删掉，没有：添加删除操作到失败记录
        FailedReminderBean failedReminder = mDao.queryFailedReminder(bean.getDate());
        if (failedReminder != null) {
            mDao.delete(DbConstants.FAILED_REMINDERS_TABLE_NAME, "date=?", new String[]{bean.getDate()});
        } else {
            FailedReminderBean bean1 = new FailedReminderBean(id, UserInfo.getId(), "delete", bean.getDate(),
                    bean.getFinished(), bean.getContent(), bean.getPlanDate());
            mDao.insertToFailed(bean1);
        }
    }
}
