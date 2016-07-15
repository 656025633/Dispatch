package com.winsion.dispatch.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.winsion.dispatch.R;
import com.winsion.dispatch.adapter.OptionsAdapter;
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
import com.winsion.dispatch.view.InterceptEventLinearLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by yalong on 2016/6/28.
 */
public class LoginActivity extends Activity {
    @InjectView(R.id.iv_head)
    ImageView ivHead;
    @InjectView(R.id.et_username)
    EditText etUsername;
    @InjectView(R.id.et_password)
    EditText etPassword;
    @InjectView(R.id.rl_username)
    RelativeLayout llUserName;
    @InjectView(R.id.btn_select)
    ImageView btnSelect;
    @InjectView(R.id.iell_progress_bar)
    InterceptEventLinearLayout interceptEventLinearLayout;

    // 下拉框选项数据源
    private List<Map<String, Object>> datas;
    // 下拉框依附组件宽度，也将作为下拉框的宽度
    private int pwidth;
    // 展示所有下拉选项的listView
    private ListView listView = null;
    // PopupWindow对象
    private PopupWindow selectPopupWindow = null;
    // 自定义Adapter
    private OptionsAdapter optionsAdapter = null;
    // 是否初始化完成标志
    private boolean flag = false;
    // 账号
    private ArrayList<String> numbers = new ArrayList<>();
    // 头像
    private ArrayList<String> userIconsPath = new ArrayList<>();
    // 密码
    private ArrayList<String> passWord = new ArrayList<>();
    private SharedPreferences userInfo;
    private SharedPreferences.Editor edit;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            Bundle data = message.getData();
            switch (message.what) {
                case 1:
                    // 选择了新的用户名
                    int selIndex = data.getInt("selIndex");
                    Bitmap headImage = BitmapFactory.decodeFile((String) datas.get(selIndex).get("headImage"));
                    ivHead.setImageBitmap(headImage);
                    etUsername.setText((CharSequence) datas.get(selIndex).get("number"));
                    etPassword.setText((String) datas.get(selIndex).get("password"));
                    selectPopupWindow.dismiss();
                    break;
                case 2:
                    // 点击了删除
                    int delIndex = data.getInt("delIndex");
                    String userName = (String) datas.get(delIndex).get("number");
                    edit.remove(userName);
                    edit.commit();
                    datas.remove(delIndex);
                    optionsAdapter.notifyDataSetChanged();
                    if (datas.size() == 0) {
                        selectPopupWindow.dismiss();
                    }
                    // 如果删除的是当前自动填充的用户，则将界面上的信息清除
                    if (TextUtils.equals(userName, etUsername.getText().toString().trim())) {
                        etUsername.setText("");
                        etPassword.setText("");
                        ivHead.setImageResource(R.mipmap.ic_user_icon);
                    }
                    break;
                case 3:
                    interceptEventLinearLayout.setVisibility(View.GONE);
                    ToastUtil.showToast(getBaseContext(), "用户名密码错误");
                    break;
                case 4:
                    interceptEventLinearLayout.setVisibility(View.GONE);
                    ToastUtil.showToast(getBaseContext(), "服务器异常");
                    break;
                case 5:
                    interceptEventLinearLayout.setVisibility(View.GONE);
                    break;
                case 6:
                    interceptEventLinearLayout.setVisibility(View.GONE);
                    ToastUtil.showToast(getBaseContext(), "网络异常");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        ButterKnife.inject(this);
        // 将上一次登陆信息回显
        userInfo = getSharedPreferences("userInfo", MODE_PRIVATE);
        String username = userInfo.getString("lastLoginUser", "");
        edit = userInfo.edit();
        if (!TextUtils.isEmpty(username)) {
            Set<String> stringSet = userInfo.getStringSet(username, null);
            if (stringSet != null) {
                String passWord = null;
                String id = null;
                for (String value : stringSet) {
                    if (value.length() > 12) {
                        id = value;
                    } else {
                        passWord = value;
                    }
                }
                etUsername.setText(username);
                etPassword.setText(passWord);
                Bitmap bitmap = BitmapFactory.decodeFile(UserInfo.getUserIconPath(id, getBaseContext()));
                ivHead.setImageBitmap(bitmap);
            }
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        while (!flag) {
            initView();
            flag = true;
        }
    }

    private void initView() {
        pwidth = llUserName.getWidth();
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag) {
                    selectPopupWindow.showAsDropDown(llUserName, 0, -3);
                }
            }
        });
        initPopupWindow();
    }

    /**
     * 初始化下拉框
     */
    private void initPopupWindow() {
        initNumbers();
        initUserIcons();
        datas = initDatas();
        View loginwindow = this.getLayoutInflater().inflate(
                R.layout.options, null);
        listView = (ListView) loginwindow.findViewById(R.id.list);
        optionsAdapter = new OptionsAdapter(this, handler, datas);
        listView.setAdapter(optionsAdapter);
        selectPopupWindow = new PopupWindow(loginwindow, pwidth,
                LinearLayout.LayoutParams.WRAP_CONTENT, true);
        selectPopupWindow.setOutsideTouchable(true);
        selectPopupWindow.setBackgroundDrawable(new BitmapDrawable());
    }

    private void initNumbers() {
        Map<String, ?> all = userInfo.getAll();
        for (String key : all.keySet()) {
            if (!TextUtils.equals(key, "loginState") && !TextUtils.equals(key, "lastLoginUser")) {
                numbers.add(key);
            }
        }
    }

    private void initUserIcons() {
        for (int i = 0; i < numbers.size(); i++) {
            String userName = numbers.get(i);
            Set<String> stringSet = userInfo.getStringSet(userName, null);
            String id = null;
            String password = null;
            for (String value : stringSet) {
                if (value.length() > 12) {
                    id = value;
                } else {
                    password = value;
                }
            }
            String path = UserInfo.getUserIconPath(id, getBaseContext());
            userIconsPath.add(path);
            passWord.add(password);
        }
    }

    /**
     * 下拉框数据
     *
     * @return
     */
    private List<Map<String, Object>> initDatas() {
        List<Map<String, Object>> listItems = new ArrayList<>();
        for (int i = 0; i < numbers.size(); i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("headImage", userIconsPath.get(i));
            map.put("number", numbers.get(i));
            map.put("delimage", R.mipmap.quit);
            map.put("password",passWord.get(i));
            listItems.add(map);
        }
        return listItems;
    }

    @OnClick(R.id.btn_login)
    public void onClick() {
        /*Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();*/
        String userName = etUsername.getText().toString().trim();
        String passWord = etPassword.getText().toString().trim();
        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(passWord)) {
            ToastUtil.showToast(getBaseContext(), "用户名密码不能为空");
            return;
        }
        // 显示进度条
        interceptEventLinearLayout.setVisibility(View.VISIBLE);
        login(userName, passWord);
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
                        handler.sendEmptyMessage(6);
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        String result = response.body().string();
                        LoginBean loginBean = JsonUtil.json2Bean(result, LoginBean.class);
                        switch (loginBean.getMeta().getCode()) {
                            case 200:
                                // 登录成功
                                handler.sendEmptyMessage(5);
                                UserBean data = loginBean.getData();
                                String id = data.getId();
                                UserInfo.setId(id);
                                UserInfo.setAuthToken(data.getAuthToken());
                                UserInfo.setPassword(passWord);
                                UserInfo.setUserName(userName);
                                // 将账号密码以及userid保存起来
                                HashSet<String> values = new HashSet<>();
                                values.add(passWord);
                                values.add(id);
                                edit.putStringSet(userName, values);
                                edit.putBoolean("loginState", true);
                                edit.putString("lastLoginUser", userName);
                                edit.commit();
                                // 下载用户头像
                                String path = Environment.getExternalStorageDirectory().getAbsolutePath();
                                File file = new File(path + "/" + getPackageName() + "/" + UserInfo.getId());
                                if (!new File(file, "userIcon").exists()) {
                                    downloadUserIcon(file);
                                }
                                // 存储配置信息
                                saveInfo();

                                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
                                break;
                            case 400:
                                handler.sendEmptyMessage(3);
                                break;
                            default:
                                handler.sendEmptyMessage(4);
                                break;
                        }
                    }
                }

        );
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

    private void downloadUserIcon(final File file) {
        RequestBody body = new FormEncodingBuilder()
                .add("userId", UserInfo.getId())
                .add("api_key", UserInfo.API_KEY)
                .build();
        Request request = new Request.Builder()
                .url(NetConstants.HOST + NetConstants.FILE + NetConstants.DOWNLOAD_USER_PICTURE)
                .post(body)
                .build();
        OkHttpUtil.enqueue(request, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful() && response.code() == 200) {
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        if (!file.exists()) {
                            file.mkdirs();
                        }
                        InputStream is = response.body().byteStream();
                        FileOutputStream fos = new FileOutputStream(new File(file, "userIcon"));
                        int len = 0;
                        byte[] buffer = new byte[1024];
                        while ((len = is.read(buffer)) != -1) {
                            fos.write(buffer, 0, len);
                        }
                        is.close();
                        fos.close();
                    }
                }
            }
        });
    }

    /**
     * 再按一次退出程序的开关
     */
    private boolean mToggle = true;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (mToggle) {
                mToggle = false;
                ToastUtil.showToast(this, "再按一次退出");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mToggle = true;
                    }
                }, 1500);
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
