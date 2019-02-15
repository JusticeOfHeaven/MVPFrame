package com.my.mvpframe.widget

import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.util.Log
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.Interpolator
import android.widget.FrameLayout
import android.widget.ImageView
import com.my.mvpframe.R
import com.my.mvpframe.utils.ImageLoaderUtils
import kotlinx.android.synthetic.main.widget_auto_vp.view.*
import java.util.*
import kotlin.concurrent.timerTask
import android.widget.Scroller


/**
 * Create by jzhan on 2019/2/14
 * 一个自动轮播的ViewPager，含有indicator
 * indicator是绘制出来的，详见 {@link onDrawForeground}
 */
class AutoScrollViewPager : FrameLayout {

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        mContext = context
        setWillNotDraw(false)

        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint.color = resources.getColor(R.color.red_600)
        mPaint.style = Paint.Style.FILL
        mPaint.strokeWidth = 2f

        initView()
    }

    private var mContext: Context
    private var mPaint: Paint
    private var mRadius = 15f
    private var listSize = 0
    private var currentPosition = 0

    /**
     * init view
     */
    private fun initView() {
        LayoutInflater.from(mContext).inflate(R.layout.widget_auto_vp, this, true)

    }

    private var timer: Timer? = null

    private val timerTask: TimerTask
        get() {
            return timerTask {
                (mContext as Activity).runOnUiThread {
                    //这里是设置当前页的下一页
                    viewPager.currentItem = viewPager.currentItem + 1
                }
            }
        }

    override fun onDrawForeground(canvas: Canvas) {
        super.onDrawForeground(canvas)
        // 绘制indicator指示器
        // 指示器距离底部的距离
        val circleBottom = measuredHeight - mRadius - 40
        //indicator之间的间距
        val circleGap = 20
        if (listSize % 2 == 0) {
            // 偶数
            for (i in 0 until listSize) {
                if (i == currentPosition) {
                    mPaint.color = resources.getColor(R.color.red_600)
                } else {
                    mPaint.color = resources.getColor(R.color.white)
                }
                canvas.drawCircle(measuredWidth / 2f - (mRadius * 2 + circleGap) * (listSize / 2 - i) + mRadius + circleGap / 2, circleBottom, mRadius, mPaint)
            }
        } else {
            // 奇数
            for (i in 0 until listSize) {
                if (i == currentPosition) {
                    mPaint.color = resources.getColor(R.color.red_600)
                } else {
                    mPaint.color = resources.getColor(R.color.white)
                }
                canvas.drawCircle(measuredWidth / 2f - (mRadius * 2 + circleGap) * ((listSize - 1) / 2 - i), circleBottom, mRadius, mPaint)
            }
        }
    }

    /**
     * 开始轮播
     */
    fun startCarousel() {
        //开启一个定时器，用于循环
        startCarousel(500)
    }

    private fun startCarousel(delay: Long) {
        timer = Timer()
        timer?.schedule(timerTask, delay, 3000)
    }

    /**
     * 设置数据
     */
    fun setData(listOf: List<String>): AutoScrollViewPager {
        listSize = listOf.size
        viewPager.adapter = ViewPagerAdapter(listOf)
        viewPager.setOnTouchListener { v: View?, event: MotionEvent? ->
            // 当ViewPager按下或在滑动的时候，就停止轮播，手指抬起的时候开始轮播
            if (event?.actionMasked == MotionEvent.ACTION_UP ||
                    event?.actionMasked == MotionEvent.ACTION_CANCEL) {
                startCarousel(2000)
            } else {
                timer?.cancel()
            }
            false
        }

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    currentPosition = viewPager.currentItem % listSize
                    Log.d("TAG", "currentPosition = $currentPosition")
                    invalidate()
                }
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
            }
        })
        invoke()

        return this
    }

    /**
     * 利用反射改变ViewPager的切换速率
     */
    private fun invoke() {
        try {
            val mScroller = ViewPager::class.java.getDeclaredField("mScroller")
            mScroller.isAccessible = true
            val scroller = FixedSpeedScroller(mContext, sInterpolator)
            mScroller.set(viewPager, scroller)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private val sInterpolator = Interpolator { t ->
        var t = t
        --t
        t * t * t * t * t + 1.0f
    }
    /**
     * 停止轮播
     */
    fun stopCarousel() {
        timer?.cancel()
    }

    /*控制ViewPager切换速率的Scroller*/
    inner class FixedSpeedScroller : Scroller {
        private var mDuration = 800

        constructor(context: Context) : super(context) {}

        constructor(context: Context, interpolator: Interpolator) : super(context, interpolator) {}

        override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int, duration: Int) {
            // Ignore received duration, use fixed one instead
            super.startScroll(startX, startY, dx, dy, mDuration)
        }

        override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int) {
            // Ignore received duration, use fixed one instead
            super.startScroll(startX, startY, dx, dy, mDuration)
        }
    }

    /*ViewPager 的 Adapter*/
    inner class ViewPagerAdapter(private var listOf: List<String>) : PagerAdapter() {
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
}