package com.winsion.dispatch.fragment.operation;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.winsion.dispatch.R;
import com.winsion.dispatch.base.BaseFragment;
import com.winsion.dispatch.view.NoScrollViewPager;

import butterknife.InjectView;

/**
 * Created by yalong on 2016/6/13.
 */
public class LocationFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener {

    @InjectView(R.id.vp_content)
    NoScrollViewPager vpContent;
    @InjectView(R.id.rg_display_by)
    RadioGroup rgDisplayBy;

    private Fragment[] mFragments = {new LocationDisplayFragment(true), new LocationDisplayFragment(false)};

    @Override
    public View setContentView() {
        return mLayoutInflater.inflate(R.layout.location_root_fragment, null);
    }

    @Override
    protected void init() {
        vpContent.setAdapter(new MyPagerAdaper(getChildFragmentManager()));
        rgDisplayBy.setOnCheckedChangeListener(this);
        RadioButton childAt = (RadioButton)  rgDisplayBy.getChildAt(0);
        childAt.setChecked(true);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case 5:
                vpContent.setCurrentItem(0,false);
                break;
            case 6:
                vpContent.setCurrentItem(1,false);
                break;
        }
    }

    private class MyPagerAdaper extends FragmentPagerAdapter {

        public MyPagerAdaper(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments[position];
        }

        @Override
        public int getCount() {
            return mFragments.length;
        }
    }
}
