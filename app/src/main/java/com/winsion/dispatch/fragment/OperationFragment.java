package com.winsion.dispatch.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import com.winsion.dispatch.R;
import com.winsion.dispatch.activity.UserActivity;
import com.winsion.dispatch.base.BaseFragment;
import com.winsion.dispatch.fragment.operation.LocationFragment;
import com.winsion.dispatch.fragment.operation.OperationControlFragment;
import com.winsion.dispatch.fragment.operation.RemindersFragment;
import com.winsion.dispatch.fragment.operation.TestFragment;
import com.winsion.dispatch.utils.ConvertUtil;
import com.winsion.dispatch.view.MyIndicator;
import com.winsion.dispatch.view.NoScrollViewPager;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 到发作业页面
 * Created by yalong on 2016/6/13.
 */
public class OperationFragment extends BaseFragment {

    @InjectView(R.id.vp_content)
    NoScrollViewPager vpContent;
    @InjectView(R.id.rg_indicator)
    MyIndicator mIndicator;

    private int[] mTitles = {R.string.tab_operation, R.string.duty_officer, R.string.handover_record,
            R.string.reminders};
    private Fragment[] mFragments = {new OperationControlFragment(), new LocationFragment(),
            new TestFragment(), new RemindersFragment()};
    private String[] mStrTitles;

    @Override
    public View setContentView() {
        return mLayoutInflater.inflate(R.layout.operation_root_fragment, null);
    }

    @Override
    protected void init() {
        mStrTitles = ConvertUtil.resourcesIdToString(mContext, mTitles);
        vpContent.setAdapter(new MyPagerAdapter(getChildFragmentManager()));
        mIndicator.setViewPager(vpContent);
    }

    @OnClick(R.id.ib_user_icon)
    public void onClick() {
        // 跳转到用户界面
        Intent intent = new Intent(mContext, UserActivity.class);
        startActivity(intent);
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments[position];
        }

        @Override
        public int getCount() {
            return mTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mStrTitles[position];
        }
    }
}
