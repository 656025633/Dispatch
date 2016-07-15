package com.winsion.dispatch.presenter;

import android.content.Context;

import com.winsion.dispatch.biz.ChangePasswordBiz;
import com.winsion.dispatch.biz.listener.OnChangePasswordListener;
import com.winsion.dispatch.iview.IChangePasswordView;

/**
 * Created by admin on 2016/7/5.
 */
public class ChangePasswordPresenter {

    private IChangePasswordView mView;
    private ChangePasswordBiz mBiz;

    public ChangePasswordPresenter(IChangePasswordView view, Context context) {
        this.mView = view;
        this.mBiz = new ChangePasswordBiz(context);
    }

    public void changePassword() {
        String quondamPassword = mView.getQuondamPassword();
        String newPassword = mView.getNewPassword();
        String confirmPassword = mView.getConfirmPassword();
        mBiz.changePassword(quondamPassword, newPassword, confirmPassword,
                new OnChangePasswordListener() {
                    @Override
                    public void onPrepare() {
                        mView.showProgressBar();
                    }

                    @Override
                    public void onFailed(String msg) {
                        mView.changePasswordFailed(msg);
                    }
                });
    }
}
