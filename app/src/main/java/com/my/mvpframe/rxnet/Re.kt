package com.my.mvpframe.rxnet

import com.my.mvpframe.module_base.net.Api

/**
 * Create by jzhan on 2019/3/14
 */
class Re {
    private constructor()

    fun getService(): RetrofitService {
        return Api.newInstance().getService("", RetrofitService::class.java)
    }

    companion object {
        fun newInstance():Re{
            return Re()
        }
    }
}