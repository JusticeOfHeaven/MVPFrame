package com.my.mvpframe.rxnet;

import com.my.mvpframe.bean.AppVersionBean;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 *  api 类
 */

public interface RetrofitService {
//    @FormUrlEncoded
    @GET("site/AppVersions")
    Observable<AppVersionBean> getGameList();
}
