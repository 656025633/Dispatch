package com.winsion.dispatch.fragment.MessageFragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.winsion.dispatch.R;
import com.winsion.dispatch.base.BaseActivity;

import butterknife.InjectView;

public class RvWidthHeaderActivity extends BaseActivity {

    @InjectView(R.id.chat_rv)
    RecyclerView mChatRv;

    @Override
    protected void beforeView() {

    }                                                                                                                                                                                                                                                    

    @Override
    protected int getLayoutId() {
        return R.layout.activity_msg_chat;
    }

    @Override
    protected void obtainIntent() {

    }

    @Override
    protected void initView(Bundle saveInstanceState) {
    //    ButterKnife.bind(this);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        mChatRv.setLayoutManager(new LinearLayoutManager(this));
       // mChatRv.setAdapter(new ChatAdapterForRv(this,ChatMessage.MOCK_DATAS));
    }
}
