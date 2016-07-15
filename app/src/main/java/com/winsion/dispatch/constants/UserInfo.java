package com.winsion.dispatch.constants;

import android.content.Context;
import android.os.Environment;

/**
 * Created by yalong on 2016/6/28.
 */
public class UserInfo {
    public static String API_KEY = "myApiKeyXXXX123456789";
    private static String mId = null;
    private static String mUserName = null;
    private static String mPassword = null;
    private static String authToken = null;
    private static String lastOperateTime = null;

    public static void setAuthToken(String authToken) {
        UserInfo.authToken = authToken;
    }

    public static void setLastOperateTime(String lastOperateTime) {
        UserInfo.lastOperateTime = lastOperateTime;
    }

    public static void setId(String id) {
        mId = id;
    }

    public static String getAuthToken() {
        return authToken;
    }

    public static String getLastOperateTime() {
        return lastOperateTime;
    }

    public static String getId() {
        if (mId == null) {
            return "";
        }
        return mId;
    }

    public static void setUserName(String mUserName) {
        UserInfo.mUserName = mUserName;
    }

    public static String getUserName() {
        return mUserName;
    }

    public static void setPassword(String mPassword) {
        UserInfo.mPassword = mPassword;
    }

    public static String getPassword() {
        return mPassword;
    }

    public static String getUserIconPath(String id, Context context) {
        return Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/" + context.getPackageName() + "/" + id + "/userIcon";
    }
}
