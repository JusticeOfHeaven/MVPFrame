package com.my.mvpframe.ui.home.fragment

import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.my.mvpframe.module_base.net.BasePresenter
import com.my.mvpframe.module_base.net.BaseView
import com.my.mvpframe.R
import com.my.mvpframe.appbase.BaseFragment
import com.my.mvpframe.ui.activity.CalculateActivity
import com.my.mvpframe.ui.activity.CardActivity
import com.my.mvpframe.ui.activity.NewEffectActivity
import com.my.mvpframe.widget.DividerGridItemDecoration
import kotlinx.android.synthetic.main.fragment_first.*


/**
 * 第一个界面，先写个轮播吧
 */
class FirstFragment : BaseFragment<com.my.mvpframe.module_base.net.BaseView, com.my.mvpframe.module_base.net.BasePresenter<com.my.mvpframe.module_base.net.BaseView>>() {

    override val layoutId: Int = R.layout.fragment_first

    override fun initView() {
        val listOf = listOf(
                "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1514180841,2293273948&fm=26&gp=0.jpg",
                "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2876895336,280180530&fm=11&gp=0.jpg",
                "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2228402546,2389446247&fm=26&gp=0.jpg",
                "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=940084039,1548298342&fm=26&gp=0.jpg"
        )
        // 设置轮播
        autoViewPager.setData(listOf).startCarousel()

        var list = listOf("动效", "卡牌滑动","计算")
        recyclerView.layoutManager = GridLayoutManager(mContext, 3)
        recyclerView.addItemDecoration(DividerGridItemDecoration(mContext, resources.getColor(R.color.red_600)))

        var adapter = object : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_block, list) {
            override fun convert(helper: BaseViewHolder, item: String) {
                helper.setText(R.id.tvTitle, item)

                helper.itemView.setOnClickListener {
                    when (helper.adapterPosition) {
                        // 动效
                        0 -> startActivity(Intent(activity, NewEffectActivity::class.java))
                        // 卡牌
                        1 -> startActivity(Intent(activity, CardActivity::class.java))
                        // 计算
                        2 -> startActivity(Intent(activity, CalculateActivity::class.java))
                    }
                }

            }

        }
        recyclerView.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        autoViewPager.stopCarousel()
    }

    override fun onPause() {
        super.onPause()
        autoViewPager.stopCarousel()
    }

    override fun onResume() {
        super.onResume()
        autoViewPager.startCarousel()
    }
}