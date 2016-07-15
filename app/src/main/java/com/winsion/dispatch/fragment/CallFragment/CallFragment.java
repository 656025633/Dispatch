package com.winsion.dispatch.fragment.CallFragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;
import com.winsion.dispatch.R;
import com.winsion.dispatch.activity.contract.ContractActivity;
import com.winsion.dispatch.adapter.ViewHolder;
import com.winsion.dispatch.adapter.abslistview.CommonAdapter;
import com.winsion.dispatch.base.BaseFragment;
import com.winsion.dispatch.bean.FriendBean;
import com.winsion.dispatch.bean.OneKeyCallBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
/**

 *
 */
public class CallFragment extends BaseFragment implements CallContract.UIView {
    @InjectView(R.id.call_friends)
    ListView mCallFriends;
    @InjectView(R.id.call_button)
    ImageButton mCallButton;
    @InjectView(R.id.call_key)
    GridView mCallKey;
    @InjectView(R.id.key_layout)
    LinearLayout mKeyLayout;
    @InjectView(R.id.call_key_input)
    EditText mCallKeyInput;
    @InjectView(R.id.sip_friend)
    Button mSipFirend;
    @InjectView(R.id.sip_common_friend)
    Button mSipCommonFriend;
    private CallPresenter mPresenter;
    private static List<Integer> resIds;
    private CommonAdapter adapter;
    private CommonAdapter mCommonAdapter;
    private List<FriendBean> friends;
    PopupWindow pw;
    private View view;
    private View commFriendListView;
    private GridView commonFriendGV;
    private List<FriendBean> commonFriendList;//常用联系人列表
    private List<OneKeyCallBean> calls;//一键配置
    private PopupWindow pw2;
    private Context mContext;
    private Intent intent;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = getActivity();
    }
    static {
        resIds = new ArrayList<>();
        resIds.add(R.drawable.call_button_one_bg);
        resIds.add((R.drawable.call_button_two_bg));
        resIds.add(R.drawable.call_button_three_bg);
        resIds.add(R.drawable.call_button_four_bg);
        resIds.add(R.drawable.call_button_five_bg);
        resIds.add(R.drawable.call_button_six_bg);
        resIds.add(R.drawable.call_button_seven_bg);
        resIds.add(R.drawable.call_button_eight_bg);
        resIds.add(R.drawable.call_button_nine_bg);
    }
    public void initData() {
        //最近联系人
        friends = new ArrayList<>();
        calls = new ArrayList<>();
        adapter = new CommonAdapter<FriendBean>(getActivity(), R.layout.item_latest_friend, friends) {
            @Override
            public void convert(ViewHolder holder, FriendBean o) {
                holder.setText(R.id.latest_friend_name, o.getName());
                holder.setText(R.id.latest_friend_location, o.getLocation());
                Glide.with(CallFragment.this).load(o.getHeadImage()).into((ImageView) holder.getConvertView().findViewById(R.id.latest_friend_image));
            }
        };
        mCallFriends.setAdapter(adapter);
    }

    @Override
    public void showContractView(List<OneKeyCallBean> datas) {
        for (int i = 0; i< 8; i++) {
            FriendBean bean = new FriendBean();
            bean.setName("shdf");
            bean.setHeadImage("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3130633325,2207220440&fm=116&gp=0.jpg");
            bean.setLocation("第一候车室");
            bean.setTitle("国家主席");
            bean.setDate("today");
            commonFriendList.add(bean);

        }
        mCommonAdapter.notifyDataSetChanged();
    }


    @Override
    public View setContentView() {
        return mLayoutInflater.inflate(R.layout.fragment_call,null);
    }

    @Override
    protected void init() {
        initListener();
        initData();
        initkey();
        initChangYong();
        mPresenter = new CallPresenter();
        mPresenter.attachView(this);
        mPresenter.loadData();
    }

    //一键呼叫用户
    private void initChangYong() {
        commFriendListView = LayoutInflater.from(getActivity()).inflate(R.layout.item_common_friend,null);
        commonFriendGV = (GridView)commFriendListView.findViewById(R.id.sip_common_friend_list);//常用联系人中的gridview
        commonFriendList = new ArrayList<>();
        mCommonAdapter = new CommonAdapter<FriendBean>(mContext,R.layout.item_item_common_friend,commonFriendList) {
            @Override
            public void convert(ViewHolder holder, FriendBean friendBean) {
                Glide.with(mContext).load(friendBean.getHeadImage()).into((ImageView)holder.getConvertView().findViewById(R.id.head));
                holder.setText(R.id.nick,friendBean.getName());
                Log.d("random","nemae:"+friendBean.getName());
            }
        };
        commonFriendGV.setAdapter(mCommonAdapter);
    }

    private void initkey() {
        mCallKeyInput.setFocusable(false);
        mCallKeyInput.setFocusableInTouchMode(false);
        mCallKey.setAdapter(new CommonAdapter<Integer>(mContext,R.layout.sip_key_item,resIds) {
            @Override
            public void convert(ViewHolder holder, Integer bean) {
                holder.setImageResource(R.id.key_image, bean);
                holder.setOnClickListener(R.id.key_image, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String editContent = mCallKeyInput.getText().toString().trim()+holder.getItemPosition();//获取item位置
                         mCallKeyInput.setText(editContent);
                    }
                });
            }
        });
    }

    private void initListener() {
        RxView.clicks(mCallButton).subscribe(v->{
            mKeyLayout.setVisibility(View.VISIBLE);
        });
        mCallFriends.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_MOVE){
                    mKeyLayout.setVisibility(View.INVISIBLE);//隐藏
                    mCallKeyInput.setText("");//清空edittext上的内容
                }
                return false;
            }
        });
        mSipFirend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(mContext, ContractActivity.class);
                startActivity(intent);
            }
        });
        mSipCommonFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出一个popwindow
                //常用联系人
                WindowManager.LayoutParams params = getActivity().getWindow().getAttributes();
                params.alpha = 0.7f;
                getActivity().getWindow().setAttributes(params);
                pw2 = new PopupWindow(commFriendListView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                pw2.setBackgroundDrawable(new ColorDrawable(Color.rgb(255, 255, 255)));
                pw2.setOutsideTouchable(true);
                pw2.setFocusable(true);
                pw2.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        WindowManager.LayoutParams params=getActivity().getWindow().getAttributes();
                        params.alpha=1f;
                        getActivity().getWindow().setAttributes(params);
                    }
                });
                pw2.showAsDropDown(mSipCommonFriend, 0, 200);
            }
        });
    }

}
