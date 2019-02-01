package com.my.mvpframe.ui.home.fragment

import android.support.v7.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lucky.netlibrary.BasePresenter
import com.lucky.netlibrary.BaseView
import com.my.mvpframe.R
import com.my.mvpframe.appbase.BaseFragment
import kotlinx.android.synthetic.main.fragment_first.*
import java.util.*

class ThirdFragment : BaseFragment<BaseView, BasePresenter<BaseView>>() {
    override val layoutId: Int
        get() = R.layout.fragment_third

    override fun initView() {

    }
}