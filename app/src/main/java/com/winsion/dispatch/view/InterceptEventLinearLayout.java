package com.winsion.dispatch.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by yalong on 2016/6/30.
 */
public class InterceptEventLinearLayout extends LinearLayout {

    public InterceptEventLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }
}
