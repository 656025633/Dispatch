package com.winsion.dispatch.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.winsion.dispatch.R;
import com.winsion.dispatch.iview.IChangePasswordView;
import com.winsion.dispatch.presenter.ChangePasswordPresenter;
import com.winsion.dispatch.utils.ToastUtil;
import com.winsion.dispatch.view.InterceptEventLinearLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by yalong on 2016/6/30.
 */
public class ChangePasswordActivity extends Activity implements IChangePasswordView {
    @InjectView(R.id.et_quondam_password)
    EditText etQuondamPassword;
    @InjectView(R.id.et_new_password)
    EditText etNewPassword;
    @InjectView(R.id.et_confirm_password)
    EditText etConfirmPassword;
    @InjectView(R.id.iell_progress_bar)
    InterceptEventLinearLayout progressBar;

    private Context mContext;
    private ChangePasswordPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password_activity);
        mContext = getBaseContext();
        ButterKnife.inject(this);
        mPresenter = new ChangePasswordPresenter(this, mContext);
    }

    @OnClick(R.id.btn_confirm_change)
    public void onClick() {
        // 点击修改密码
        mPresenter.changePassword();
    }

    @Override
    public String getQuondamPassword() {
        return etQuondamPassword.getText().toString().trim();
    }

    @Override
    public String getNewPassword() {
        return etNewPassword.getText().toString().trim();
    }

    @Override
    public String getConfirmPassword() {
        return etConfirmPassword.getText().toString().trim();
    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void changePasswordFailed(String msg) {
        progressBar.setVisibility(View.GONE);
        ToastUtil.showToast(mContext, msg);
    }
}
