package com.my.mvpframe.widget

import android.support.v4.view.ViewPager
import android.util.Log
import android.view.View
import android.support.v4.view.ViewCompat.setScaleY
import android.support.v4.view.ViewCompat.setScaleX
import android.support.v4.view.ViewCompat.setTranslationX
import android.opengl.ETC1.getWidth



/**
 * Create by jzhan on 2018/11/30
 * 自定义ViewPager切换效果
 *
 */
class ViewPagerTransformer : ViewPager.PageTransformer {
    private val TAG = "ViewPagerTransformer"
    private val MIN_SCALE = 0.75f
    /**
     * @param view  当前页的View
     * @param position 在当前手机屏幕能看到的页面永远为0，往左递减，往右递增，例如 -1，0，1
     */
    override fun transformPage(view: View, position: Float) {
        Log.d(TAG, "position = $position")
        val pageWidth = view.getWidth()
        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            // 下一页将要划进来的pager
            view.setAlpha(0f)
        } else if (position <= 0) { // [-1,0]
            // Use the default slide transition when moving to the left page
            // 当前滑动的pager
            view.setAlpha(1f)
            view.setTranslationX(0f)
            view.setScaleX(1f)
            view.setScaleY(1f)
        } else if (position <= 1) { // (0,1]
            // Fade the page out.
            // 即将要划进来的pager
            view.setAlpha(1 - position)
            // Counteract the default slide transition
            view.setTranslationX(pageWidth * -position)
            // Scale the page down (between MIN_SCALE and 1)
            val scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position))
            view.setScaleX(scaleFactor)
            view.setScaleY(scaleFactor)
        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            // 划出去的pager
            view.setAlpha(0f)
        }
    }

}
