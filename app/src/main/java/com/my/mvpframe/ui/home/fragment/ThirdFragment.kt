package com.my.mvpframe.ui.home.fragment

import com.my.mvpframe.module_base.net.BasePresenter
import com.my.mvpframe.module_base.net.BaseView
import com.my.mvpframe.R
import com.my.mvpframe.appbase.BaseFragment

class ThirdFragment : BaseFragment<com.my.mvpframe.module_base.net.BaseView, com.my.mvpframe.module_base.net.BasePresenter<com.my.mvpframe.module_base.net.BaseView>>() {
    override val layoutId: Int
        get() = R.layout.fragment_third

    override fun initView() {

    }
}