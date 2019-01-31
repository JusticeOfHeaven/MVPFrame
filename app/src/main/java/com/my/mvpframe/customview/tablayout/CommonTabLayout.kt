package com.my.mvpframe.customview.tablayout

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import com.my.mvpframe.R
import kotlinx.android.synthetic.main.layout_common_tab.view.*

/**
 * Create by jzhan on 2019/1/30
 * tab 切换
 */
class CommonTabLayout : HorizontalScrollView {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private lateinit var mContext: Context

    private lateinit var mTabsContainer: LinearLayout
    private var dataList = ArrayList<String>()

    private fun init(context: Context, attrs: AttributeSet?) {
        isFillViewport = true//设置滚动视图是否可以伸缩其内容以填充视口
        setWillNotDraw(false)//重写onDraw方法,需要调用这个方法来清除flag
        clipChildren = false
        clipToPadding = false

        this.mContext = context

        mTabsContainer = LinearLayout(context)
        mTabsContainer.orientation = LinearLayout.HORIZONTAL

        addView(mTabsContainer)
        //get layout_height
        val height = attrs?.getAttributeValue("http://schemas.android.com/apk/res/android", "layout_height")

    }

    fun setTabData(data: List<String>) {
        this.dataList.addAll(data)

        dataList.forEach {
            val tabView = View.inflate(mContext, R.layout.layout_common_tab, null)
            tabView.tvTagName.text = it
            mTabsContainer.addView(tabView)
        }
        invalidate()
    }
}