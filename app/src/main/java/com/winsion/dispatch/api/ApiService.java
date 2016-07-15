package com.winsion.dispatch.api;

import com.winsion.dispatch.bean.BaseModelBean;
import com.winsion.dispatch.bean.OneKeyCallBean;
import com.winsion.dispatch.bean.RelateUserBean;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by zcm on 2016/3/31.
 * qq:656025633
 * 存放接口
 * 1.http://www.weather.com.cn/data/sk/101010100.html
 */
public interface ApiService {
    /* @GET("/data/sk/101010100.html")
     Observable<WeatherBean> getWeathe();*/
    //https://api.douban.com/v2/movie/top250
  //  @GET("/data/sk/101010100.html")
   /* Call<WeatherBean> getWeathe();

    @GET("api/4/news/latest")
    Observable<ZhihuBean> getLatestNews();

    @Headers("Cache-Control: public, max-age=64 0000, s-maxage=640000 , max-stale=2419200")
    @GET("v2/movie/top250")
    Call<DouBean> getDouNews(@Query("start") int start, @Query("count") int count);*/
    //todo 查询通讯录的好友目录
    @POST("phoneUser/findYourRelateUser")
    Call<BaseModelBean<List<RelateUserBean>>> getRelateUser(@Query("userId") String userId,@Query("api_key") String api_key);
    //todo 获取一键配置的用户的信息
    @POST("phoneUser/findOneKeyCallUser")
    Call<BaseModelBean<List<OneKeyCallBean>>> getOneKeyCallUser(@Query("api_key") String api_key);

}
