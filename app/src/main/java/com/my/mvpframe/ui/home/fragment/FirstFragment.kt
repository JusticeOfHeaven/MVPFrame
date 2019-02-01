package com.my.mvpframe.ui.home.fragment

import android.support.v4.view.PagerAdapter
import android.support.v7.widget.LinearLayoutManager
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lucky.netlibrary.BasePresenter
import com.lucky.netlibrary.BaseView
import com.my.mvpframe.R
import com.my.mvpframe.appbase.BaseFragment
import com.my.mvpframe.utils.ImageLoaderUtils
import kotlinx.android.synthetic.main.fragment_first.*


/**
 * 第一个界面，先写个轮播吧
 */
class FirstFragment : BaseFragment<BaseView, BasePresenter<BaseView>>() {
    private var isLooper = false

    override val layoutId: Int
        get() = R.layout.fragment_first

    override fun initView() {
        val listOf = listOf(
                "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1514180841,2293273948&fm=26&gp=0.jpg",
                "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2876895336,280180530&fm=11&gp=0.jpg",
                "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2228402546,2389446247&fm=26&gp=0.jpg",
                "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=940084039,1548298342&fm=26&gp=0.jpg"
        )
        viewPager.adapter = ViewPagerAdapter(listOf)


        //开启一个线程，用于循环
        Thread(Runnable {
            isLooper = true
            while (isLooper) {
                try {
                    Thread.sleep(2000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

                mContext?.runOnUiThread(Runnable {
                    //这里是设置当前页的下一页
                    viewPager.currentItem = viewPager.currentItem + 1
                })
            }
        }).start()


        recyclerView.layoutManager = LinearLayoutManager(mContext)

        var adapter = object : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_text) {
            override fun convert(helper: BaseViewHolder?, item: String?) {

            }

        }
        recyclerView.adapter = adapter
    }

    inner class ViewPagerAdapter(var listOf: List<String>) : PagerAdapter() {

        var sparseArray = SparseArray<ImageView>()

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val imageView = ImageView(mContext)
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            val i = position % listOf.size
            ImageLoaderUtils.display(mContext, imageView, listOf[i])
            if (sparseArray.indexOfKey(i) == -1) {
                sparseArray.append(i, imageView)
            }
            container.addView(imageView)
            return imageView
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            val i = position % listOf.size
            container.removeView(sparseArray[i])
        }

        override fun isViewFromObject(p0: View, p1: Any): Boolean {
            return p0 == p1
        }

        override fun getCount(): Int {
            return Int.MAX_VALUE
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        isLooper = false
    }
}