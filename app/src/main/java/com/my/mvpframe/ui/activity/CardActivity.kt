package com.my.mvpframe.ui.activity

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lucky.netlibrary.BasePresenter
import com.lucky.netlibrary.BaseView
import com.my.mvpframe.R
import com.my.mvpframe.appbase.BaseActivity
import com.my.mvpframe.utils.ImageLoaderUtils
import kotlinx.android.synthetic.main.act_card.*

/**
 * Create by jzhan on 2019/2/15
 * 左右滑切换view
 */
class CardActivity : BaseActivity<BaseView, BasePresenter<BaseView>>() {

    override fun getLayoutId(): Int = R.layout.act_card

    override fun initView() {
        val listOf = listOf(
                "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1896843850,550771211&fm=26&gp=0.jpg",
                "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2863128772,1675042542&fm=26&gp=0.jpg",
                "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=925036984,2670235460&fm=26&gp=0.jpg",
                "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=1650058714,2366548048&fm=26&gp=0.jpg",
                "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1718498648,2709299100&fm=26&gp=0.jpg",
                "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2787006723,13570&fm=26&gp=0.jpg",
                "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2883598650,1443518838&fm=26&gp=0.jpg",
                "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=4126178465,2692506777&fm=26&gp=0.jpg",
                "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1144406644,3504319693&fm=26&gp=0.jpg",
                "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=819748253,2218961528&fm=26&gp=0.jpg"
        )

        recyclerView.layoutManager = MyLayoutManager()
//        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = object : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_imageview, listOf) {
            override fun convert(helper: BaseViewHolder, item: String) {
                val view: ImageView = helper.getView(R.id.imageView)
                ImageLoaderUtils.display(mContext, view, item)
            }
        }
    }

    inner class MyLayoutManager : RecyclerView.LayoutManager() {
        private var mTotalHeight = 0

        override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
            return RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }

        override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
            super.onLayoutChildren(recycler, state)
            var offsetY = 0
            for (i in 0 until itemCount) {
                var scrap = recycler.getViewForPosition(i)
                addView(scrap)

                measureChildWithMargins(scrap, 0, 0)

                var perItemWidth = getDecoratedMeasuredWidth(scrap)
                var perItemHeight = getDecoratedMeasuredHeight(scrap)
                //测量每个item
                layoutDecorated(scrap, 0, offsetY, perItemWidth, offsetY + perItemHeight)
                offsetY += perItemHeight
            }

            mTotalHeight = offsetY
        }

        override fun canScrollVertically(): Boolean {
            return true
        }

        override fun scrollVerticallyBy(dy: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {
            offsetChildrenVertical(dy)
            return super.scrollVerticallyBy(dy, recycler, state)
        }
    }

}