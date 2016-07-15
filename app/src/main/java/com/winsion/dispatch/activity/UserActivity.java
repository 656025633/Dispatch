package com.winsion.dispatch.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.winsion.dispatch.R;
import com.winsion.dispatch.constants.NetConstants;
import com.winsion.dispatch.constants.UserInfo;
import com.winsion.dispatch.utils.LogUtil;
import com.winsion.dispatch.utils.OkHttpUtil;
import com.winsion.dispatch.view.SharedPreferencesUtil;

import java.io.File;
import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by yalong on 2016/6/28.
 */
public class UserActivity extends Activity {
    @InjectView(R.id.iv_user_icon)
    ImageView userIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity);
        ButterKnife.inject(this);
        String filePath = UserInfo.getUserIconPath(UserInfo.getId(), getBaseContext());
        if (new File(filePath).exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(filePath);
            userIcon.setImageBitmap(bitmap);
        }
    }

    @OnClick({R.id.ll_change_password, R.id.btn_logout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_change_password:
                // 更改密码
                Intent intent1 = new Intent(getBaseContext(), ChangePasswordActivity.class);
                startActivity(intent1);
                break;
            case R.id.btn_logout:
                // 注销
                SharedPreferencesUtil.saveData(this, "loginState", false);
                Intent intent2 = new Intent(getBaseContext(), LoginActivity.class);
                // 退出信息发送给服务器
                RequestBody body = new FormEncodingBuilder()
                        .add("userId", UserInfo.getId())
                        .build();
                Request request = new Request.Builder()
                        .url(NetConstants.HOST + NetConstants.AUTH + NetConstants.LOGOUT)
                        .post(body)
                        .build();
                OkHttpUtil.enqueue(request);
                // 清空activity栈
                intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent2);
                finish();
                break;
        }
    }
}
