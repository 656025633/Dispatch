package com.winsion.dispatch.fragment.operation;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.winsion.dispatch.R;
import com.winsion.dispatch.base.BaseFragment;
import com.winsion.dispatch.db.RemindersDao;
import com.winsion.dispatch.utils.ConvertUtil;
import com.winsion.dispatch.view.NoScrollViewPager;

import butterknife.InjectView;

/**
 * Created by yalong on 2016/6/15.
 */
public class RemindersFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener {

    @InjectView(R.id.reminders_indicator)
    RadioGroup remindersIndicator;
    @InjectView(R.id.vp_reminders)
    NoScrollViewPager vpReminders;

    private Fragment[] mFragments = new Fragment[2];
    private int[] mTitles = {R.string.unDone, R.string.done};
    private String[] mStrTitles;
    private MyPagerAdapter mAdapter;
    private RemindersDao mDao;

    @Override
    public View setContentView() {
        // 提交未更新的提醒事项，完成后同步数据
        mDao = RemindersDao.getInstance(mContext);
        mDao.queryAllFailed();
        return mLayoutInflater.inflate(R.layout.operation_reminders_fragment, null);
    }

    @Override
    protected void init() {
        mFragments[0] = new RemindersListFragment(false, mDao);
        mFragments[1] = new RemindersListFragment(true, mDao);
        mStrTitles = ConvertUtil.resourcesIdToString(mContext, mTitles);
        initAdapter();
    }

    private void initAdapter() {
        mAdapter = new MyPagerAdapter(getChildFragmentManager());
        vpReminders.setAdapter(mAdapter);
        remindersIndicator.setOnCheckedChangeListener(this);
        RadioButton childAt = (RadioButton) remindersIndicator.getChildAt(0);
        childAt.setChecked(true);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case 7:
                vpReminders.setCurrentItem(0, false);
                break;
            case 8:
                vpReminders.setCurrentItem(1, false);
                break;
        }
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
            return mStrTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mStrTitles[position];
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDao.close();
    }
}
