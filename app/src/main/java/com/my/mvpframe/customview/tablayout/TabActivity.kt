package com.my.mvpframe.customview.tablayout

import com.lucky.netlibrary.BasePresenter
import com.lucky.netlibrary.BaseView
import com.my.mvpframe.R
import com.my.mvpframe.appbase.BaseActivity
import kotlinx.android.synthetic.main.act_tab.*

/**
 * Create by jzhan on 2019/1/30
 */
class TabActivity :BaseActivity<BaseView, BasePresenter<BaseView>>() {
    override fun getLayoutId(): Int = R.layout.act_tab

    override fun initView() {
//        var list =  listOf("1","2")
//        commontab.setTabData(list)
    }

}