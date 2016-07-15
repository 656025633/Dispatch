package com.winsion.dispatch.utils;

import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yalong on 2016/6/15.
 */
public class ConvertUtil {

    /**
     * 从资源ID转换成String
     *
     * @param titles
     * @return
     */
    public static String[] resourcesIdToString(Context context, int[] titles) {
        String[] strings = new String[titles.length];
        for (int i = 0; i < titles.length; i++) {
            strings[i] = context.getResources().getString(titles[i]);
        }
        return strings;
    }

    /**
     * 毫秒值->2016-06-22 16:07
     *
     * @param millis
     * @return
     */
    public static String millisToDate(long millis) {
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formater.format(new Date(millis));
    }

    public static long dateToMillis(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date parse = null;
        try {
            parse = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return parse.getTime();
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
