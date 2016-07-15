package com.winsion.dispatch.iview;

/**
 * Created by admin on 2016/7/5.
 */
public interface IChangePasswordView {
    // 原密码
    String getQuondamPassword();

    // 新密码
    String getNewPassword();

    // 确认密码
    String getConfirmPassword();

    // 显示进度条
    void showProgressBar();

    // 修改密码失败
    void changePasswordFailed(String msg);
}
