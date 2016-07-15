package com.winsion.dispatch.base;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import butterknife.ButterKnife;

/**
 * Created by yalong on 2016/6/13.
 */
public abstract class BaseFragment extends Fragment {

    public LayoutInflater mLayoutInflater;
    private View mContentView;
    public Context mContext;
    public Handler mHandler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mLayoutInflater = inflater;
        mContext = getContext();
        mHandler = new Handler();
        // 缓存Fragment，防止切换造成UI的重绘
        if (mContentView == null) {
            mContentView = setContentView();
            ButterKnife.inject(this, mContentView);
            init();
        }
        ViewGroup parent = (ViewGroup) mContentView.getParent();
        if (parent != null) {
            parent.removeView(mContentView);
        }
        return mContentView;
    }

    /**
     * 设置Fragment的内容View
     *
     * @return
     */
    public abstract View setContentView();

    /**
     * 初始化
     */
    protected abstract void init();

    /**
     * 返回布局View
     */
    public View getContentView() {
        return mContentView;
    }
}
