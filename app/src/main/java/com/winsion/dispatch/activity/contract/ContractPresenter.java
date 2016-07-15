package com.winsion.dispatch.activity.contract;

import android.content.Context;
import android.util.Log;

import com.winsion.dispatch.bean.BaseModelBean;
import com.winsion.dispatch.bean.OneKeyCallBean;
import com.winsion.dispatch.bean.RelateUserBean;
import com.winsion.dispatch.constants.UserInfo;
import com.winsion.dispatch.utils.RetrofitUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mr.ZCM on 2016/6/13.
 * QQ:656025633
 * Company:winsion
 * Version:1.0
 * explain:
 */
public class ContractPresenter implements ContractContract.Presenter {
    private ContractContract.View mView;
    private List<RelateUserBean> datas;
    private Context mContext;
    @Override
    public void attachView(ContractContract.View view) {
        this.mView = view;
    }

    @Override
    public void detachView() {

    }

    /**
     * 加载网络数据
     * 根据chatid去得文件的名字
     */
    public void loadData(){
        //获取到数据之后
        //todo 获取通讯录数据
        String userId = UserInfo.getId();
        Log.d("random","useId:"+userId);

        RetrofitUtil.getApiService().getRelateUser(userId,UserInfo.API_KEY).enqueue(new Callback<BaseModelBean<List<RelateUserBean>>>() {
            @Override
            public void onResponse(Call<BaseModelBean<List<RelateUserBean>>> call, Response<BaseModelBean<List<RelateUserBean>>> response) {
                List<RelateUserBean> beans = response.body().getData();
                mView.showContractContent(beans);
            }

            @Override
            public void onFailure(Call<BaseModelBean<List<RelateUserBean>>> call, Throwable t) {
                //回调请求失败的界面

            }
        });
        RetrofitUtil.getApiService().getOneKeyCallUser(UserInfo.API_KEY).enqueue(new Callback<BaseModelBean<List<OneKeyCallBean>>>() {
            @Override
            public void onResponse(Call<BaseModelBean<List<OneKeyCallBean>>> call, Response<BaseModelBean<List<OneKeyCallBean>>> response) {
                if(response.body() == null){
                    Log.d("random","null"+"code2"+response.code());
                }
                else{
                    List<OneKeyCallBean> beans = response.body().getData();
                    for(OneKeyCallBean bean:beans){
                        Log.d("random","bean:"+bean.getUserName());
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseModelBean<List<OneKeyCallBean>>> call, Throwable t) {

            }
        });
    }
}
