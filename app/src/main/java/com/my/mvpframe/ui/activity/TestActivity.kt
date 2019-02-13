package com.my.mvpframe.ui.activity

import com.lucky.netlibrary.BasePresenter
import com.lucky.netlibrary.BaseView
import com.my.mvpframe.R
import com.my.mvpframe.appbase.BaseActivity

/**
 * Create by jzhan on 2019/2/13
 * 这是各种自定义布局用来展示效果的activity，不用的直接注释，用的打开，布局xml文件也要做相应的处理
 */
class TestActivity:BaseActivity<BaseView,BasePresenter<BaseView>>() {
    override fun getLayoutId(): Int = R.layout.activity_test

    override fun initView() {

    }
}