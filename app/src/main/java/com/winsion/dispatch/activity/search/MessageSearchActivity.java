package com.winsion.dispatch.activity.search;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;
import com.winsion.dispatch.R;
import com.winsion.dispatch.activity.chat.ChatActivity;
import com.winsion.dispatch.adapter.ViewHolder;
import com.winsion.dispatch.adapter.abslistview.CommonAdapter;
import com.winsion.dispatch.base.BaseActivity;
import com.winsion.dispatch.bean.ContractBean;
import com.winsion.dispatch.utils.T;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MessageSearchActivity extends BaseActivity {

    @InjectView(android.R.id.list)
    ListView mList;
    @InjectView(R.id.search_edit)
    EditText mSearchEdit;
    @InjectView(R.id.search_text)
    TextView mSearchText;
    private CommonAdapter adapter;
    private List<ContractBean> datas;
    private List<ContractBean> selectDatas;
    @Override
    protected void beforeView() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void obtainIntent() {
        Intent intent = getIntent();
        datas = (List<ContractBean>)intent.getSerializableExtra("messages");
        for (int i = 0; i <datas.size() ; i++) {
            Log.d("random",datas.get(i).getName());
        }
    }

    @Override
    protected void initView(Bundle saveInstanceState) {
        ButterKnife.inject(this);
    }

    @Override
    protected void initListener() {
        mSearchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    selectDatas.clear();
                    adapter.notifyDataSetChanged();
                    mSearchText.setText("搜索");
                } else {
                    selectDatas.clear();
                    selectDatas.addAll(select(s.toString()));
                    adapter.notifyDataSetChanged();
                    mSearchText.setText("取消");
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mSearchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSearchText.getText().toString().trim().equals("取消"))
                    mSearchEdit.setText("");
            }
        });
    }

    private List<ContractBean> select(String query) {
        List<ContractBean> newDatas = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            if (datas.get(i).getName().trim().contains(query)) {
                newDatas.add(datas.get(i));
            }
        }
        return newDatas;
    }

    @Override
    protected void initData() {
        if(datas == null){
            datas = new ArrayList<>();
        }
        if(selectDatas == null){
            selectDatas= new ArrayList<>();
        }

        adapter = new CommonAdapter<ContractBean>(this, R.layout.item_msg_msg, selectDatas) {
            @Override
            public void convert(ViewHolder holder, ContractBean s) {
                holder.setText(R.id.msg_name, s.getName());
                holder.setText(R.id.msg_date,s.getDatte());
                Glide.with(MessageSearchActivity.this).load(s.getHeadIcon()).placeholder(R.mipmap.ic_launcher).into((ImageView) holder.getConvertView().findViewById(R.id.msg_image));
                RxView.clicks(holder.getConvertView().findViewById(R.id.msg_enter)).subscribe(v->{
                    //跳转到聊天界面
                    startActivity(new Intent(mContext, ChatActivity.class));

                });
            }
        };
        mList.setAdapter(adapter);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //
                String content = selectDatas.get(position).toString();
                T.show(MessageSearchActivity.this, content, 1);
            }
        });
    }
}
