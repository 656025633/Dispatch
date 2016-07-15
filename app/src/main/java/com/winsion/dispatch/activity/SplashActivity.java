package com.winsion.dispatch.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.winsion.dispatch.R;
import com.winsion.dispatch.bean.ComConfigBean;
import com.winsion.dispatch.bean.ConfigBean;
import com.winsion.dispatch.bean.LoginBean;
import com.winsion.dispatch.bean.UserBean;
import com.winsion.dispatch.constants.ConfigInfo;
import com.winsion.dispatch.constants.NetConstants;
import com.winsion.dispatch.constants.UserInfo;
import com.winsion.dispatch.utils.JsonUtil;
import com.winsion.dispatch.utils.OkHttpUtil;
import com.winsion.dispatch.utils.ToastUtil;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * Created by yalong on 2016/6/28.
 */
public class SplashActivity extends Activity {

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getWindow();
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        window.setFlags(flag, flag);
        setContentView(R.layout.splash_activity);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences user = getSharedPreferences("userInfo", MODE_PRIVATE);
                boolean loginState = user.getBoolean("loginState", false);
                if (loginState) {
                    String lastLoginUser = user.getString("lastLoginUser", "");
                    Set<String> stringSet = user.getStringSet(lastLoginUser, null);
                    if (stringSet == null) {
                        Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish();
                    } else {
                        String passWord = null;
                        for (String value : stringSet) {
                            if (!(value.length() > 12)) {
                                passWord = value;
                            }
                        }
                        login(lastLoginUser, passWord);
                    }
                } else {
                    Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                }
            }
        }, 3000);
        TextView textView = (TextView) findViewById(R.id.tv_name);
        ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1);
        scaleAnimation.setDuration(2000);
        textView.startAnimation(scaleAnimation);
    }

    private void login(final String userName, final String passWord) {
        RequestBody body = new FormEncodingBuilder()
                .add("userName", userName)
                .add("password", passWord)
                .add("server", "MOBILE")
                .add("api_key", UserInfo.API_KEY)
                .build();
        Request request = new Request.Builder()
                .url(NetConstants.HOST + NetConstants.AUTH + NetConstants.LOGIN)
                .post(body)
                .build();
        OkHttpUtil.enqueue(request, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showToast(getBaseContext(), "网络异常");
                    }
                });
                Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    String result = response.body().string();
                    LoginBean loginBean = JsonUtil.json2Bean(result, LoginBean.class);
                    int code = loginBean.getMeta().getCode();
                    if (code == 200) {
                        UserBean data = loginBean.getData();
                        String id = data.getId();
                        UserInfo.setId(id);
                        UserInfo.setAuthToken(data.getAuthToken());
                        UserInfo.setPassword(passWord);
                        UserInfo.setUserName(userName);
                        saveInfo();
                        Intent intent = new Intent(getBaseContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (code == 400) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showToast(getBaseContext(), "用户信息已更新，请重新登陆");
                            }
                        });
                        Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showToast(getBaseContext(), "服务器异常");
                            }
                        });
                        Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showToast(getBaseContext(), "请检查你的网络");
                        }
                    });
                    Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    /**
     * 存储配置信息
     */
    private void saveInfo() {
        RequestBody body = new FormEncodingBuilder()
                .add("api_key", UserInfo.API_KEY)
                .build();
        Request request = new Request.Builder()
                .url(NetConstants.HOST + NetConstants.COMMON_CONFIG + NetConstants.QUERY_SYS_CONFIG)
                .post(body)
                .build();
        OkHttpUtil.enqueue(request, new Callback() {

            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                String string = response.body().string();
                ComConfigBean comConfigBean = JsonUtil.json2Bean(string, ComConfigBean.class);
                if (comConfigBean.getMeta().getCode() == 200) {
                    List<ConfigBean> data = comConfigBean.getData();
                    for (ConfigBean bean : data) {
                        String sysKey = bean.getSysKey();
                        String value = bean.getValue();
                        switch (sysKey) {
                            // 通信服务器地址
                            case "COMMUNICATIONDADRESS":
                                ConfigInfo.setCommunicationAddress(value);
                                break;
                            // 软交换服务器地址
                            case "SWITCHBOARDADRESS":
                                ConfigInfo.setSwitchBoardAddress(value);
                                break;
                            // 客户端实时消息保留天数
                            case "CLIENTMESSAGEREMAIN":
                                ConfigInfo.setClientMessageRemain(value);
                                break;
                            // 客户端客运命令保留天数
                            case "CLIENTNOTICEREMAIN":
                                ConfigInfo.setClientNoticeRemain(value);
                                break;
                        }
                    }
                }
            }
        });
    }
}
