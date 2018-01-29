package com.my.mvpframe.rxnet;

import com.my.mvpframe.utils.PublicKetUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by liuyong
 * Data: 2017/7/31
 * Github:https://github.com/MrAllRight
 */

public class HttpCenter {
    private static final long READ_TIME_OUT = 10000;
    private static final long CONNECT_TIME_OUT = 10000;
    public RetrofitService service;
    public static final String BASE_URL = "http://192.168.0.66:8082/api/";//BASE_URL

    private static class HttpCenterHolder {
        private static final HttpCenter INSTANCE = new HttpCenter();
    }

    public static final HttpCenter getInstance() {
        return HttpCenterHolder.INSTANCE;
    }

    private HttpCenter() {
        //开启Log
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(READ_TIME_OUT, TimeUnit.MILLISECONDS)
                .connectTimeout(CONNECT_TIME_OUT, TimeUnit.MILLISECONDS)
                .addInterceptor(logInterceptor)
                .addInterceptor(headerInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())//添加gson转换器
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//添加rxjava2适配器
                .baseUrl(BASE_URL)
                .build();

        service = retrofit.create(RetrofitService.class);
    }

    Interceptor headerInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();
            String timestamp = PublicKetUtils.getTimestamp();
            Request build = originalRequest.newBuilder()
                    .addHeader("timestamp", timestamp)
                    .addHeader("publickey", PublicKetUtils.encryptMD5ToString(timestamp, "sqtapi123456"))
                    .build();
            return chain.proceed(build);
        }
    };
}
