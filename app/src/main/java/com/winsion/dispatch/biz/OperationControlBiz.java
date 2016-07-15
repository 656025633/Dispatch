package com.winsion.dispatch.biz;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.winsion.dispatch.bean.RTrainInfoBean;
import com.winsion.dispatch.bean.TrainInfoBean;
import com.winsion.dispatch.bean.UserStation;
import com.winsion.dispatch.bean.UserStationBean;
import com.winsion.dispatch.biz.listener.OnInitSpinnerListener;
import com.winsion.dispatch.biz.listener.OnResponseListener;
import com.winsion.dispatch.constants.NetConstants;
import com.winsion.dispatch.constants.UserInfo;
import com.winsion.dispatch.utils.JsonUtil;
import com.winsion.dispatch.utils.OkHttpUtil;

import java.io.IOException;
import java.util.List;

/**
 * Created by yalong on 2016/6/14.
 */
public class OperationControlBiz {

    public void requestInternet(final String id, String type, final OnResponseListener listener) {
        RequestBody body = new FormEncodingBuilder()
                .add("userId", UserInfo.getId())
                .add("stationOrgId", id)
                .add("trainRunType", type)
                .add("api_key", UserInfo.API_KEY)
                .build();
        Request request = new Request.Builder()
                .url(NetConstants.HOST + NetConstants.PHONE_JOB + NetConstants.JOB_CONTROLLER)
                .post(body)
                .build();
        OkHttpUtil.enqueue(request, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    RTrainInfoBean bean = JsonUtil.json2Bean(response.body().string(), RTrainInfoBean.class);
                    if (bean.getMeta().getCode() == 200) {
                        List<TrainInfoBean> data = bean.getData();
                        listener.onSuccess(data);
                    }
                }
            }
        });
    }

    public void initSpinnerData(final OnInitSpinnerListener listener) {
        RequestBody body = new FormEncodingBuilder()
                .add("userId", UserInfo.getId())
                .add("api_key", UserInfo.API_KEY)
                .build();
        Request request = new Request.Builder()
                .url(NetConstants.HOST + NetConstants.PHONE_JOB + NetConstants.SEARCH_USER_STATION_ORG)
                .post(body)
                .build();
        OkHttpUtil.enqueue(request, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    UserStationBean userStationBean = JsonUtil.json2Bean(response.body().string(), UserStationBean.class);
                    if (userStationBean.getMeta().getCode() == 200) {
                        List<UserStation> data = userStationBean.getData();
                        listener.onSuccess(data);
                    }
                }
            }
        });
    }
}
