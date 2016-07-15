package com.winsion.dispatch.fragment.dispatch;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.jakewharton.rxbinding.view.RxView;
import com.winsion.dispatch.R;
import com.winsion.dispatch.activity.search.CommandSearchActivity;
import com.winsion.dispatch.base.BaseFragment;
import com.winsion.dispatch.fragment.CallFragment.CallFragment;
import com.winsion.dispatch.fragment.CommandFragment.CommandFragment;
import com.winsion.dispatch.fragment.MessageFragment.MessageFragment;

import butterknife.InjectView;

/**
 */
public class ControlFragment extends BaseFragment {
    @InjectView(R.id.control_message)
    RadioButton mControlMessage;
    @InjectView(R.id.control_command)
    RadioButton mControlCommand;
    @InjectView(R.id.control_call)
    RadioButton mControlCall;
    @InjectView(R.id.control_rg)
    RadioGroup mControlRg;
    @InjectView(R.id.control_content)
    FrameLayout mControlContent;
    @InjectView(R.id.control_search)
    ImageView mControlSearch;
    private CallFragment mCallFragment;
    private CommandFragment mCommandFragment;
    private MessageFragment mMessageFragment;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mTransaction;
    private View view;
    private Intent intent;
    private Context mContext;

    @Override
    public View setContentView() {
        view = mLayoutInflater.inflate(R.layout.fragment_control, null);
        return view;
    }
    @Override
    protected void init() {
        initView();
        initListener();
        mControlRg.check(R.id.control_message);
    }
    public void initView() {
        mCallFragment = new CallFragment();
        mMessageFragment = new MessageFragment();
        mCommandFragment = new CommandFragment();
       // mFriendFragment = new FriendFragment();
        //保存状态
    /*    if (saveInstanceState != null) {
            if (mArriveFragment != null) {
                mArriveFragment = (ArriveFragment) getSupportFragmentManager().findFragmentByTag(mArriveFragment.getClass().getName());
                mcontrolFragment = (ControlFragment) getSupportFragmentManager().findFragmentByTag(mcontrolFragment.getClass().getName());
                mBroadcastFragment = (BroadcastFragment) getSupportFragmentManager().findFragmentByTag(mBroadcastFragment.getClass().getName());
                mRecordFragment = (RecordFragment) getSupportFragmentManager().findFragmentByTag(mRecordFragment.getClass().getName());
                mLocationFragment = (LocationFragment) getSupportFragmentManager().findFragmentByTag(mLocationFragment.getClass().getName());
                getSupportFragmentManager().beginTransaction().hide(mcontrolFragment).hide(mBroadcastFragment).hide(mRecordFragment).hide(mLocationFragment).show(mArriveFragment).commit();
            }
        } else {*/
            //添加fragment到content

            //设置第一次显示的fragment
            mTransaction = getChildFragmentManager().beginTransaction();
            mTransaction
                .add(R.id.control_content, mMessageFragment, mMessageFragment.getClass().getName())
                .add(R.id.control_content, mCommandFragment, mCommandFragment.getClass().getName())
                .add(R.id.control_content, mCallFragment, mCallFragment.getClass().getName())
                .hide(mCallFragment).hide(mCommandFragment).commit();
                mControlRg.check(R.id.control_message);

    }


    public void initListener() {

        mControlRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                mFragmentManager = getChildFragmentManager();
                mTransaction = mFragmentManager.beginTransaction();
                switch (checkedId){
                    case R.id.control_call:

                        if(!mCallFragment.isAdded()){
                            mTransaction.hide(mMessageFragment).hide(mCommandFragment).add(R.id.control_content, mCallFragment).commit();
                        }
                        else{
                            mTransaction.hide(mMessageFragment).hide(mCommandFragment).show(mCallFragment).commit();
                        }
                        break;
                    case R.id.control_command:
                        if(!mCommandFragment.isAdded()){
                            mTransaction.hide(mCallFragment).hide(mMessageFragment).add(R.id.control_content, mCommandFragment).commit();
                        }
                        else{
                            mTransaction.hide(mCallFragment).hide(mMessageFragment).show(mCommandFragment).commit();
                        }
                        break;
                    case R.id.control_message:
                        if(!mMessageFragment.isAdded()){
                            mTransaction.hide(mCallFragment).hide(mCommandFragment).add(R.id.control_content,mMessageFragment).commit();
                        }
                        else{
                            mTransaction.hide(mCallFragment).hide(mCommandFragment).show(mMessageFragment).commit();
                        }
                        break;
                }
            }
        });

        RxView.clicks(mControlSearch).subscribe(v->{
            intent = new Intent(mContext, CommandSearchActivity.class);
            startActivity(intent);
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("random","onDestroyView");
    }
    ////////////////////////////

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = getActivity();
        Log.d("random","onAtach");
    }
}
