package com.winsion.dispatch.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by yalong on 2016/6/22.
 */
public class ToastUtil {
    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
