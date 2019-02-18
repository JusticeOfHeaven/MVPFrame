package com.my.mvpframe.widget

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.my.mvpframe.utils.ImageLoaderUtils

/**
 * Create by jzhan on 2019/2/15
 */
class DefaultImageViewPagerAdapter(private var mContext: Context, private var listOf: List<String>) : PagerAdapter() {
    private var sparseArray = SparseArray<ImageView>()

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

    override fun isViewFromObject(p0: View, p1: Any): Boolean {
        return p0 == p1
    }

    override fun getCount(): Int = listOf.size

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val i = position % listOf.size
        container.removeView(sparseArray[i])
    }
}