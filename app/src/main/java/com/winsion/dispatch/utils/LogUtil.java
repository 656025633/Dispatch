package com.winsion.dispatch.utils;

import android.util.Log;

/**
 * Created by yalong on 2016/6/16.
 */
public class LogUtil {

    private static boolean mToggle = true;

    public static void showLog(String tag, String msg) {
        if (mToggle) {
            Log.d(tag, msg);
        }
    }
}
