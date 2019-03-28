package com.my.mvpframe.ui.activity

import com.my.mvpframe.module_base.net.BasePresenter
import com.my.mvpframe.module_base.net.BaseView
import com.my.mvpframe.R
import com.my.mvpframe.appbase.BaseActivity

/**
 * Create by jzhan on 2019/3/6
 */
class DragViewActivity :BaseActivity<BaseView, BasePresenter<BaseView>>() {
    override fun getLayoutId(): Int = R.layout.act_drag_view

    override fun initView() {

    }

}