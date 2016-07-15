package com.winsion.dispatch.fragment.CommandFragment;


import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bartoszlipinski.recyclerviewheader.RecyclerViewHeader;
import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;
import com.winsion.dispatch.R;
import com.winsion.dispatch.activity.search.CommandSearchActivity;
import com.winsion.dispatch.adapter.ViewHolder;
import com.winsion.dispatch.adapter.recyclerview.OnItemClickListener;
import com.winsion.dispatch.adapter.recyclerview.support.SectionSupport;
import com.winsion.dispatch.base.BaseFragment;
import com.winsion.dispatch.bean.ContractBean;
import com.winsion.dispatch.fragment.MessageFragment.SectionAdapter;
import com.winsion.dispatch.utils.T;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommandFragment extends BaseFragment implements  CommandContract.View{
    @InjectView(R.id.commmand_rv)
    RecyclerView mRecyclerView;
    @InjectView(R.id.command_publish)
    Button mCommandPublish;
    private CommandPresenter mPresenter;
    private RecyclerViewHeader mHeader;
    private Context mContext;
    private SectionSupport<String> sectionSupport;
    private SectionAdapter<ContractBean> adapter;
    private List<ContractBean> beans;


    @Override
        public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void showComandList(List<ContractBean> beans) {
        this.beans.clear();
        this.beans.addAll(beans);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public View setContentView() {
        return mLayoutInflater.inflate(R.layout.fragment_command,null);
    }

    @Override
    protected void init() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mHeader = RecyclerViewHeader.fromXml(getActivity(),R.layout.lv_head);
        mHeader.attachTo(mRecyclerView);

        RxView.clicks(mCommandPublish).subscribe(v->{
            //  startActivity(new Intent(getActivity(), CommandActivity.class));
        });
        RxView.clicks((RelativeLayout)mHeader.findViewById(R.id.searchBar)).subscribe(v->{
            T.show(mContext,"搜索界面",1);
            Intent intent = new Intent(getActivity(),CommandSearchActivity.class);
            intent.putExtra("contracts",(Serializable) beans);
            startActivity(intent);
        });


        beans = new ArrayList<>();
        mPresenter = new CommandPresenter();
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
        adapter = new SectionAdapter<ContractBean>(getActivity(), R.layout.item_cmd, beans, sectionSupport) {
            @Override
            public void convert(ViewHolder holder, ContractBean s) {
                holder.setText(R.id.cmd_name, s.getName());
                holder.setText(R.id.cmd_date,s.getDatte());
                Glide.with(CommandFragment.this).load(s.getHeadIcon()).placeholder(R.mipmap.ic_launcher).into((ImageView) holder.getConvertView().findViewById(R.id.cmd_image));

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
        mPresenter.attachView(this);
        mPresenter.loadData();
    }
}
