package com.my.mvpframe.module_base.net

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

/**
 * Create by jzhan on 2019/3/14
 */
class Api {
    private var okHttpClient: OkHttpClient
    private var READ_TIME_OUT: Long = 10
    private var CONNECT_TIME_OUT: Long = 10

    private constructor() {
        //开启Log
        val logInterceptor = HttpLoggingInterceptor()
        logInterceptor.level = HttpLoggingInterceptor.Level.BODY

        okHttpClient = OkHttpClient.Builder()
                .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
                .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
                .addInterceptor(logInterceptor)
                .build()
    }

    companion object {
        fun newInstance(): Api {
            return Api()
        }
    }

    fun <S> getService(BASE_URL: String, cls: Class<S>): S {
        return Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())//添加gson转换器
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//添加rxjava2适配器
                .baseUrl(BASE_URL)
                .build()
                .create(cls)
    }
}