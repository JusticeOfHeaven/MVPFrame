package com.my.mvpframe.ui.activity

import android.view.ViewGroup
import com.my.mvpframe.module_base.net.BasePresenter
import com.my.mvpframe.module_base.net.BaseView
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
class ViewPagerActivity : BaseActivity<com.my.mvpframe.module_base.net.BaseView, com.my.mvpframe.module_base.net.BasePresenter<com.my.mvpframe.module_base.net.BaseView>>() {
    private var list = ArrayList<String>()

    override fun getLayoutId(): Int = R.layout.activity_view_pager

    override fun initView() {
        val listOf = listOf(
                "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1514180841,2293273948&fm=26&gp=0.jpg",
                "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2876895336,280180530&fm=11&gp=0.jpg",
                "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2228402546,2389446247&fm=26&gp=0.jpg",
                "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=940084039,1548298342&fm=26&gp=0.jpg"
        )
        // 第一个ViewPager
        viewPager.adapter = DefaultImageViewPagerAdapter(this, listOf)
        /**
         * reverseDrawingOrder  加载到ViewPager的Pager页面是按正序还是逆序添加
         *
         */
        viewPager.pageMargin = 40
        viewPager.setPageTransformer(true, ViewPagerTransformer())
        // 第二个ViewPager
        var pagerWidth = (resources.displayMetrics.widthPixels * 3.0f / 5.0f).toInt()
        var lp: ViewGroup.LayoutParams? = viewPager1.layoutParams
        if (lp == null) {
            lp = ViewGroup.LayoutParams(pagerWidth, ViewGroup.LayoutParams.MATCH_PARENT)
        } else {
            lp.width = pagerWidth
        }
        viewPager1.layoutParams = lp
        viewPager1.setPageTransformer(true, ViewPagerTransformer(2))
        viewPager1.adapter = DefaultImageViewPagerAdapter(this, listOf,true)
        viewPager1.pageMargin = -50
    }
}


