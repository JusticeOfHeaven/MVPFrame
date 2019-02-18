package com.my.mvpframe.ui.activity

import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import com.lucky.netlibrary.BasePresenter
import com.lucky.netlibrary.BaseView
import com.my.mvpframe.R
import com.my.mvpframe.appbase.BaseActivity
import com.my.mvpframe.widget.DefaultImageViewPagerAdapter
import com.my.mvpframe.widget.ViewPagerTransformer
import kotlinx.android.synthetic.main.activity_view_pager.*

/**
 * Create by jzhan on 2019/2/15
 * ViewPager切换的各种效果，主要包括
 * 1、viewPager.setPageMargin(40)
 * 2、PageTransFormer
 */
class ViewPagerActivity : BaseActivity<BaseView, BasePresenter<BaseView>>() {
    private var list = ArrayList<String>()

    override fun getLayoutId(): Int = R.layout.activity_view_pager

    override fun initView() {
        val listOf = listOf(
                "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1514180841,2293273948&fm=26&gp=0.jpg",
                "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2876895336,280180530&fm=11&gp=0.jpg",
                "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2228402546,2389446247&fm=26&gp=0.jpg",
                "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=940084039,1548298342&fm=26&gp=0.jpg"
        )

        viewPager.adapter = DefaultImageViewPagerAdapter(this, listOf)
        /**
         * reverseDrawingOrder  加载到ViewPager的Pager页面是按正序还是逆序添加
         *
         */
        viewPager.setPageTransformer(true, ViewPagerTransformer())
        viewPager.pageMargin = 40
    }
}


