package com.winsion.dispatch.biz;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.winsion.dispatch.activity.LoginActivity;
import com.winsion.dispatch.bean.UserNoteAddBean;
import com.winsion.dispatch.biz.listener.OnChangePasswordListener;
import com.winsion.dispatch.constants.NetConstants;
import com.winsion.dispatch.constants.UserInfo;
import com.winsion.dispatch.utils.JsonUtil;
import com.winsion.dispatch.utils.OkHttpUtil;
import com.winsion.dispatch.view.SharedPreferencesUtil;

import java.io.IOException;

/**
 * Created by admin on 2016/7/5.
 */
public class ChangePasswordBiz {

    private Context mContext;

    public ChangePasswordBiz(Context context) {
        this.mContext = context;
    }

    public void changePassword(String quondamPassword, String newPassword, String confirmPassword,
                               final OnChangePasswordListener listener) {
        listener.onPrepare();
        String password = UserInfo.getPassword();
        if (!TextUtils.equals(password, quondamPassword)) {
            listener.onFailed("原密码不正确");
            return;
        }
        if (newPassword.length() < 6 || confirmPassword.length() < 6) {
            listener.onFailed("新密码应该为6-12个字符");
            return;
        }
        if (!TextUtils.equals(newPassword, confirmPassword)) {
            listener.onFailed("两次密码输入不一致");
            return;
        }

        RequestBody body = new FormEncodingBuilder()
                .add("userName", UserInfo.getUserName())
                .add("oldPassword", password)
                .add("newPassword", newPassword)
                .add("api_key", UserInfo.API_KEY)
                .build();
        Request request = new Request.Builder()
                .url(NetConstants.HOST + NetConstants.AUTH + NetConstants.CHANGE_PASSWORD)
                .post(body)
                .build();
        OkHttpUtil.enqueue(request, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                listener.onFailed("网络异常");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.code() == 200) {
                    UserNoteAddBean bean = JsonUtil.json2Bean(response.body().string(), UserNoteAddBean.class);
                    int code = bean.getMeta().getCode();
                    if (code == 200) {
                        SharedPreferencesUtil.saveData(mContext, "loginState", false);
                        Intent intent = new Intent(mContext, LoginActivity.class);
                        // 清空activity栈
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                    }
                } else {
                    listener.onFailed("服务器异常");
                }
            }
        });
    }
}
