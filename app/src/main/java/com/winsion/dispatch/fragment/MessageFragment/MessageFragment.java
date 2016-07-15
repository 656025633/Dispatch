package com.winsion.dispatch.fragment.MessageFragment;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bartoszlipinski.recyclerviewheader.RecyclerViewHeader;
import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;
import com.winsion.dispatch.R;
import com.winsion.dispatch.activity.chat.ChatActivity;
import com.winsion.dispatch.adapter.ViewHolder;
import com.winsion.dispatch.adapter.recyclerview.OnItemClickListener;
import com.winsion.dispatch.adapter.recyclerview.support.SectionSupport;
import com.winsion.dispatch.base.BaseFragment;
import com.winsion.dispatch.bean.ContractBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**

 */
public class MessageFragment extends BaseFragment implements MessageContract.View{
    @InjectView(R.id.rv)
    RecyclerView mRecyclerView;
    private MessagePresenter mPresenter;
    private RecyclerViewHeader header;
    private Context mContext;
    private SectionSupport<String> sectionSupport;
    private SectionAdapter<ContractBean> adapter;
    private List<ContractBean> beans;
    private Intent intent;



    public void initListener() {
       /* RxView.clicks((RelativeLayout)header.findViewById(R.id.searchBar)).subscribe(v->{
            Intent intent = new Intent(getActivity(),MessageSearchActivity.class);
            intent.putExtra("messages",(Serializable) beans);
            startActivity(intent);
        });*/
    }


    public void initData() {
        beans = new ArrayList<>();
        sectionSupport = new SectionSupport<String>() {
            @Override
            public int sectionHeaderLayoutId() {
                return R.layout.header;
            }

            @Override
            public int sectionTitleTextViewId() {
                return R.id.id_header_title;
            }

            @Override
            public String getTitle(String s) {
                return s;
            }
        };
        adapter = new SectionAdapter<ContractBean>(getActivity(), R.layout.item_msg_msg, beans, sectionSupport) {
            @Override
            public void convert(ViewHolder holder, ContractBean s) {
                holder.setText(R.id.msg_name, s.getName());
                holder.setText(R.id.msg_date,s.getDatte());
                Glide.with(MessageFragment.this).load(s.getHeadIcon()).placeholder(R.mipmap.ic_launcher).into((ImageView) holder.getConvertView().findViewById(R.id.msg_image));
                RxView.clicks(holder.getConvertView().findViewById(R.id.msg_enter)).subscribe(v->{
                    //跳转到聊天界面
                    intent = new Intent(mContext, ChatActivity.class);
                    intent.putExtra("chatId",s.getUserId());
                    startActivity(intent);
                });
            }
        };
        adapter.setOnItemClickListener(new OnItemClickListener<ContractBean>() {
            @Override
            public void onItemClick(ViewGroup parent, View view, ContractBean o, int position) {
                Toast.makeText(getActivity(), "Click:" + position + " => " + o.getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, ContractBean o, int position) {
                return false;
            }
        });
        mRecyclerView.setAdapter(adapter);
        mPresenter = new MessagePresenter();
        mPresenter.attachView(this);
        mPresenter.loadData();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void showMessagList(List<ContractBean> mDatas) {
        beans.clear();
        beans.addAll(mDatas);
        adapter.notifyDataSetChanged();
    }

    @Override
    public View setContentView() {
        return mLayoutInflater.inflate(R.layout.fragment_message,null);
    }

    @Override
    protected void init() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        header = RecyclerViewHeader.fromXml(getActivity(),R.layout.lv_head);
        header.attachTo(mRecyclerView);
        initListener();
        initData();
    }
}
