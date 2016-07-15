package com.winsion.dispatch.activity.contract;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.winsion.dispatch.R;
import com.winsion.dispatch.adapter.ViewHolder;
import com.winsion.dispatch.adapter.abslistview.CommonAdapter;
import com.winsion.dispatch.base.BaseActivity;
import com.winsion.dispatch.bean.RelateUserBean;
import com.winsion.dispatch.constants.NetConstants;
import com.winsion.dispatch.constants.UserInfo;
import com.winsion.dispatch.utils.T;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/***
 * 呼叫中的通讯录
 */

public class ContractActivity extends BaseActivity implements ContractContract.View {
    @InjectView(R.id.contract_lv)
    ListView mContractLv;
    private List<RelateUserBean> contracts;
    private CommonAdapter contractAdapter;
    //
    ContractPresenter mPresenter;


    @Override
    protected void beforeView() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_contract;
    }

    @Override
    protected void obtainIntent() {

    }

    @Override
    protected void initView(Bundle saveInstanceState) {
        ButterKnife.inject(this);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        contracts = new ArrayList<>();
        contractAdapter = new CommonAdapter<RelateUserBean>(this,R.layout.item_friend,contracts) {
            @Override
            public void convert(ViewHolder holder, RelateUserBean o) {
                holder.setText(R.id.friend_name,o.getUserName());
                holder.setText(R.id.friend_friend_title,o.getRoleNames());
                if (TextUtils.isEmpty(o.getAreaName())) {
                    holder.setText(R.id.friend_location,"");
                    Glide.with(ContractActivity.this).load(NetConstants.HOST + NetConstants.FILE + NetConstants.DOWNLOAD_USER_PICTURE+"?userId="+o.getId()+"&api_key="+UserInfo.API_KEY).into((ImageView) holder.getConvertView().findViewById(R.id.friend_image));
                    //头像置灰
                    ColorMatrix matrix = new ColorMatrix();
                    matrix.setSaturation(0);
                    ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                    ( (ImageView) holder.getConvertView().findViewById(R.id.friend_image)).setColorFilter(filter);
                }
                else {
                    ColorMatrix matrix = new ColorMatrix();
                    matrix.setSaturation(1);
                    ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                    ( (ImageView) holder.getConvertView().findViewById(R.id.friend_image)).setColorFilter(filter);
                    holder.setText(R.id.friend_location, o.getAreaName());
                    Glide.with(ContractActivity.this).load(NetConstants.HOST + NetConstants.FILE + NetConstants.DOWNLOAD_USER_PICTURE+"?userId="+o.getId()+"&api_key="+UserInfo.API_KEY).into((ImageView) holder.getConvertView().findViewById(R.id.friend_image));
                }
            }
        };
        mContractLv.setAdapter(contractAdapter);
        mPresenter = new ContractPresenter();
        mPresenter.attachView(this);
        mPresenter.loadData();
    }

    @Override
    public void showContractContent(List<RelateUserBean> friends) {
        if(friends!=null){
            contracts.clear();
            contracts.addAll(friends);
            contractAdapter.notifyDataSetChanged();
        }
        else{
            T.show(this,"暂时没有通讯录好友",1);
        }
    }
}
