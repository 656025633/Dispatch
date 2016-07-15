package com.winsion.dispatch.fragment.operation;

import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.winsion.dispatch.base.BaseFragment;

/**
 * Created by yalong on 2016/6/15.
 */
public class TestFragment extends BaseFragment {
    @Override
    public View setContentView() {
        TextView textView = new TextView(mContext);
        textView.setGravity(Gravity.CENTER);
        textView.setText("TEST");
        textView.setTextSize(50);
        return textView;
    }

    @Override
    protected void init() {

    }
}
