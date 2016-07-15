package com.winsion.dispatch.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.winsion.dispatch.R;
import com.winsion.dispatch.fragment.OperationFragment;
import com.winsion.dispatch.fragment.dispatch.ControlFragment;
import com.winsion.dispatch.fragment.operation.LocationFragment;
import com.winsion.dispatch.fragment.operation.TestFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends FragmentActivity {

    @InjectView(R.id.tabhost)
    FragmentTabHost mFragmentTabHost;

    /**
     * Tab选项卡的文字
     */
    private int[] mTabTitles = {R.string.tab_operation, R.string.tab_dispatch, R.string.tab_control,
            R.string.tab_location, R.string.tab_monitor};
    /**
     * Tab选项卡的图片
     */
    private int[] mTabIcons = {R.drawable.selector_operation_icon, R.drawable.selector_dispatch_icon,
            R.drawable.selector_control_icon, R.drawable.selector_location_icon,
            R.drawable.selector_monitor_icon};
    /**
     * Tab对应的内容Fragment
     */
    private Class[] mTabFragments = {OperationFragment.class, ControlFragment.class,
            TestFragment.class, LocationFragment.class, TestFragment.class};

    /**
     * 再按一次退出程序的开关
     */
    private boolean mToggle = true;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ButterKnife.inject(this);
        initView();
    }

    private void initView() {
        mFragmentTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        int count = mTabFragments.length;
        for (int i = 0; i < count; i++) {
            // 给Tab设置标签和按钮的样式(图片和文字)
            TabHost.TabSpec tabSpec = mFragmentTabHost.newTabSpec(getResources().getString(mTabTitles[i])).setIndicator(getTabItemView(i));
            mFragmentTabHost.addTab(tabSpec, mTabFragments[i], null);
        }
        // 去掉分割线
        mFragmentTabHost.getTabWidget().setDividerDrawable(null);
    }

    private View getTabItemView(int index) {
        View tabView = View.inflate(getApplicationContext(), R.layout.main_tab_view, null);
        ImageView imageView = (ImageView) tabView.findViewById(R.id.iv_icon);
        TextView textView = (TextView) tabView.findViewById(R.id.tv_title);
        imageView.setImageResource(mTabIcons[index]);
        textView.setText(mTabTitles[index]);
        return tabView;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (mToggle) {
                mToggle = false;
                Toast.makeText(MainActivity.this, "再按一次退出", Toast.LENGTH_SHORT).show();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mToggle = true;
                    }
                }, 1500);
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.exit(0);
    }
}
