package com.my.mvpframe.widget

import android.animation.TypeEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Parcelable
import android.support.annotation.DrawableRes
import android.util.AttributeSet
import android.util.SparseArray
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import android.widget.*
import com.my.mvpframe.R
import java.util.ArrayList

/**
 * Create by jzhan on 2019/1/30
 * tab 切换
 */
class CommonTabLayout @JvmOverloads constructor(private val mContext: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(mContext, attrs, defStyleAttr), ValueAnimator.AnimatorUpdateListener {
    private val mTabEntitys = ArrayList<CustomTabEntity>()
    private val mTabsContainer: LinearLayout
    private var mCurrentTab: Int = 0
    private var mLastTab: Int = 0
    var tabCount: Int = 0
        private set
    /** 用于绘制显示器  */
    private val mIndicatorRect = Rect()
    private val mIndicatorDrawable = GradientDrawable()

    private val mRectPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mDividerPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mTrianglePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mTrianglePath = Path()
    private var mIndicatorStyle = STYLE_NORMAL

    private var mTabPadding: Float = 0.toFloat()
    private var mTabSpaceEqual: Boolean = false
    private var mTabWidth: Float = 0.toFloat()

    /** indicator  */
    private var mIndicatorColor: Int = 0
    private var mIndicatorHeight: Float = 0.toFloat()
    private var mIndicatorWidth: Float = 0.toFloat()
    private var mIndicatorCornerRadius: Float = 0.toFloat()
    var indicatorMarginLeft: Float = 0.toFloat()
        private set
    var indicatorMarginTop: Float = 0.toFloat()
        private set
    var indicatorMarginRight: Float = 0.toFloat()
        private set
    var indicatorMarginBottom: Float = 0.toFloat()
        private set
    var indicatorAnimDuration: Long = 0
    var isIndicatorAnimEnable: Boolean = false
    var isIndicatorBounceEnable: Boolean = false
    private var mIndicatorGravity: Int = 0

    /** underline  */
    private var mUnderlineColor: Int = 0
    private var mUnderlineHeight: Float = 0.toFloat()
    private var mUnderlineGravity: Int = 0

    /** divider  */
    private var mDividerColor: Int = 0
    private var mDividerWidth: Float = 0.toFloat()
    private var mDividerPadding: Float = 0.toFloat()
    private var mTextsize: Float = 0.toFloat()
    private var mTextSelectColor: Int = 0
    private var mTextUnselectColor: Int = 0
    private var mTextBold: Int = 0
    private var mTextAllCaps: Boolean = false

    /** icon  */
    private var mIconVisible: Boolean = false
    private var mIconGravity: Int = 0
    private var mIconWidth: Float = 0.toFloat()
    private var mIconHeight: Float = 0.toFloat()
    private var mIconMargin: Float = 0.toFloat()

    private var mHeight: Int = 0

    /** anim  */
    private val mValueAnimator: ValueAnimator
    private val mInterpolator = OvershootInterpolator(1.5f)

    private var mIsFirstDraw = true

    //setter and getter
    var currentTab: Int
        get() = mCurrentTab
        set(currentTab) {
            mLastTab = this.mCurrentTab
            this.mCurrentTab = currentTab
            updateTabSelection(currentTab)

            if (isIndicatorAnimEnable) {
                calcOffset()
            } else {
                invalidate()
            }
        }

    var indicatorStyle: Int
        get() = mIndicatorStyle
        set(indicatorStyle) {
            this.mIndicatorStyle = indicatorStyle
            invalidate()
        }

    var tabPadding: Float
        get() = mTabPadding
        set(tabPadding) {
            this.mTabPadding = dp2px(tabPadding).toFloat()
            updateTabStyles()
        }

    var isTabSpaceEqual: Boolean
        get() = mTabSpaceEqual
        set(tabSpaceEqual) {
            this.mTabSpaceEqual = tabSpaceEqual
            updateTabStyles()
        }

    var tabWidth: Float
        get() = mTabWidth
        set(tabWidth) {
            this.mTabWidth = dp2px(tabWidth).toFloat()
            updateTabStyles()
        }

    var indicatorColor: Int
        get() = mIndicatorColor
        set(indicatorColor) {
            this.mIndicatorColor = indicatorColor
            invalidate()
        }

    var indicatorHeight: Float
        get() = mIndicatorHeight
        set(indicatorHeight) {
            this.mIndicatorHeight = dp2px(indicatorHeight).toFloat()
            invalidate()
        }

    var indicatorWidth: Float
        get() = mIndicatorWidth
        set(indicatorWidth) {
            this.mIndicatorWidth = dp2px(indicatorWidth).toFloat()
            invalidate()
        }

    var indicatorCornerRadius: Float
        get() = mIndicatorCornerRadius
        set(indicatorCornerRadius) {
            this.mIndicatorCornerRadius = dp2px(indicatorCornerRadius).toFloat()
            invalidate()
        }

    var underlineColor: Int
        get() = mUnderlineColor
        set(underlineColor) {
            this.mUnderlineColor = underlineColor
            invalidate()
        }

    var underlineHeight: Float
        get() = mUnderlineHeight
        set(underlineHeight) {
            this.mUnderlineHeight = dp2px(underlineHeight).toFloat()
            invalidate()
        }

    var dividerColor: Int
        get() = mDividerColor
        set(dividerColor) {
            this.mDividerColor = dividerColor
            invalidate()
        }

    var dividerWidth: Float
        get() = mDividerWidth
        set(dividerWidth) {
            this.mDividerWidth = dp2px(dividerWidth).toFloat()
            invalidate()
        }

    var dividerPadding: Float
        get() = mDividerPadding
        set(dividerPadding) {
            this.mDividerPadding = dp2px(dividerPadding).toFloat()
            invalidate()
        }

    var textsize: Float
        get() = mTextsize
        set(textsize) {
            this.mTextsize = sp2px(textsize).toFloat()
            updateTabStyles()
        }

    var textSelectColor: Int
        get() = mTextSelectColor
        set(textSelectColor) {
            this.mTextSelectColor = textSelectColor
            updateTabStyles()
        }

    var textUnselectColor: Int
        get() = mTextUnselectColor
        set(textUnselectColor) {
            this.mTextUnselectColor = textUnselectColor
            updateTabStyles()
        }

    var textBold: Int
        get() = mTextBold
        set(textBold) {
            this.mTextBold = textBold
            updateTabStyles()
        }

    var isTextAllCaps: Boolean
        get() = mTextAllCaps
        set(textAllCaps) {
            this.mTextAllCaps = textAllCaps
            updateTabStyles()
        }

    var iconGravity: Int
        get() = mIconGravity
        set(iconGravity) {
            this.mIconGravity = iconGravity
            notifyDataSetChanged()
        }

    var iconWidth: Float
        get() = mIconWidth
        set(iconWidth) {
            this.mIconWidth = dp2px(iconWidth).toFloat()
            updateTabStyles()
        }

    var iconHeight: Float
        get() = mIconHeight
        set(iconHeight) {
            this.mIconHeight = dp2px(iconHeight).toFloat()
            updateTabStyles()
        }

    var iconMargin: Float
        get() = mIconMargin
        set(iconMargin) {
            this.mIconMargin = dp2px(iconMargin).toFloat()
            updateTabStyles()
        }

    var isIconVisible: Boolean
        get() = mIconVisible
        set(iconVisible) {
            this.mIconVisible = iconVisible
            updateTabStyles()
        }

    //setter and getter

    // show MsgTipView
    private val mTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mInitSetMap = SparseArray<Boolean>()

    private var mListener: OnTabSelectListener? = null

    private val mCurrentP = IndicatorPoint()
    private val mLastP = IndicatorPoint()

    init {
        setWillNotDraw(false)//重写onDraw方法,需要调用这个方法来清除flag
        clipChildren = false
        clipToPadding = false
        mTabsContainer = LinearLayout(mContext)
        addView(mTabsContainer)

        obtainAttributes(mContext, attrs)

        //get layout_height
        val height = attrs!!.getAttributeValue("http://schemas.android.com/apk/res/android", "layout_height")

        //create ViewPager
        if (height == ViewGroup.LayoutParams.MATCH_PARENT.toString() + "") {
        } else if (height == ViewGroup.LayoutParams.WRAP_CONTENT.toString() + "") {
        } else {
            val systemAttrs = intArrayOf(android.R.attr.layout_height)
            val a = mContext.obtainStyledAttributes(attrs, systemAttrs)
            mHeight = a.getDimensionPixelSize(0, ViewGroup.LayoutParams.WRAP_CONTENT)
            a.recycle()
        }

        mValueAnimator = ValueAnimator.ofObject(PointEvaluator(), mLastP, mCurrentP)
        mValueAnimator.addUpdateListener(this)
    }

    private fun obtainAttributes(context: Context, attrs: AttributeSet?) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.CommonTabLayout)

        mIndicatorStyle = ta.getInt(R.styleable.CommonTabLayout_tl_indicator_style, 0)
        mIndicatorColor = ta.getColor(R.styleable.CommonTabLayout_tl_indicator_color, Color.parseColor(if (mIndicatorStyle == STYLE_BLOCK) "#4B6A87" else "#ffffff"))
        mIndicatorHeight = ta.getDimension(R.styleable.CommonTabLayout_tl_indicator_height,
                dp2px((if (mIndicatorStyle == STYLE_TRIANGLE) 4 else if (mIndicatorStyle == STYLE_BLOCK) -1 else 2).toFloat()).toFloat())
        mIndicatorWidth = ta.getDimension(R.styleable.CommonTabLayout_tl_indicator_width, dp2px((if (mIndicatorStyle == STYLE_TRIANGLE) 10 else -1).toFloat()).toFloat())
        mIndicatorCornerRadius = ta.getDimension(R.styleable.CommonTabLayout_tl_indicator_corner_radius, dp2px((if (mIndicatorStyle == STYLE_BLOCK) -1 else 0).toFloat()).toFloat())
        indicatorMarginLeft = ta.getDimension(R.styleable.CommonTabLayout_tl_indicator_margin_left, dp2px(0f).toFloat())
        indicatorMarginTop = ta.getDimension(R.styleable.CommonTabLayout_tl_indicator_margin_top, dp2px((if (mIndicatorStyle == STYLE_BLOCK) 7 else 0).toFloat()).toFloat())
        indicatorMarginRight = ta.getDimension(R.styleable.CommonTabLayout_tl_indicator_margin_right, dp2px(0f).toFloat())
        indicatorMarginBottom = ta.getDimension(R.styleable.CommonTabLayout_tl_indicator_margin_bottom, dp2px((if (mIndicatorStyle == STYLE_BLOCK) 7 else 0).toFloat()).toFloat())
        isIndicatorAnimEnable = ta.getBoolean(R.styleable.CommonTabLayout_tl_indicator_anim_enable, true)
        isIndicatorBounceEnable = ta.getBoolean(R.styleable.CommonTabLayout_tl_indicator_bounce_enable, true)
        indicatorAnimDuration = ta.getInt(R.styleable.CommonTabLayout_tl_indicator_anim_duration, -1).toLong()
        mIndicatorGravity = ta.getInt(R.styleable.CommonTabLayout_tl_indicator_gravity, Gravity.BOTTOM)

        mUnderlineColor = ta.getColor(R.styleable.CommonTabLayout_tl_underline_color, Color.parseColor("#ffffff"))
        mUnderlineHeight = ta.getDimension(R.styleable.CommonTabLayout_tl_underline_height, dp2px(0f).toFloat())
        mUnderlineGravity = ta.getInt(R.styleable.CommonTabLayout_tl_underline_gravity, Gravity.BOTTOM)

        mDividerColor = ta.getColor(R.styleable.CommonTabLayout_tl_divider_color, Color.parseColor("#ffffff"))
        mDividerWidth = ta.getDimension(R.styleable.CommonTabLayout_tl_divider_width, dp2px(0f).toFloat())
        mDividerPadding = ta.getDimension(R.styleable.CommonTabLayout_tl_divider_padding, dp2px(12f).toFloat())

        mTextsize = ta.getDimension(R.styleable.CommonTabLayout_tl_textsize, sp2px(13f).toFloat())
        mTextSelectColor = ta.getColor(R.styleable.CommonTabLayout_tl_textSelectColor, Color.parseColor("#ffffff"))
        mTextUnselectColor = ta.getColor(R.styleable.CommonTabLayout_tl_textUnselectColor, Color.parseColor("#AAffffff"))
        mTextBold = ta.getInt(R.styleable.CommonTabLayout_tl_textBold, TEXT_BOLD_NONE)
        mTextAllCaps = ta.getBoolean(R.styleable.CommonTabLayout_tl_textAllCaps, false)

        mIconVisible = ta.getBoolean(R.styleable.CommonTabLayout_tl_iconVisible, true)
        mIconGravity = ta.getInt(R.styleable.CommonTabLayout_tl_iconGravity, Gravity.TOP)
        mIconWidth = ta.getDimension(R.styleable.CommonTabLayout_tl_iconWidth, dp2px(0f).toFloat())
        mIconHeight = ta.getDimension(R.styleable.CommonTabLayout_tl_iconHeight, dp2px(0f).toFloat())
        mIconMargin = ta.getDimension(R.styleable.CommonTabLayout_tl_iconMargin, dp2px(2.5f).toFloat())

        mTabSpaceEqual = ta.getBoolean(R.styleable.CommonTabLayout_tl_tab_space_equal, true)
        mTabWidth = ta.getDimension(R.styleable.CommonTabLayout_tl_tab_width, dp2px(-1f).toFloat())
        mTabPadding = ta.getDimension(R.styleable.CommonTabLayout_tl_tab_padding, (if (mTabSpaceEqual || mTabWidth > 0) dp2px(0f) else dp2px(10f)).toFloat())

        ta.recycle()
    }

    fun setTabData(tabEntitys: ArrayList<CustomTabEntity>?) {
        if (tabEntitys == null || tabEntitys.size == 0) {
            throw IllegalStateException("TabEntitys can not be NULL or EMPTY !")
        }

        this.mTabEntitys.clear()
        this.mTabEntitys.addAll(tabEntitys)

        notifyDataSetChanged()
    }

    /** 更新数据  */
    fun notifyDataSetChanged() {
        mTabsContainer.removeAllViews()
        this.tabCount = mTabEntitys.size
        var tabView: View
        for (i in 0 until tabCount) {
            if (mIconGravity == Gravity.LEFT) {
                tabView = View.inflate(mContext, R.layout.layout_tab_left, null)
            } else if (mIconGravity == Gravity.RIGHT) {
                tabView = View.inflate(mContext, R.layout.layout_tab_right, null)
            } else if (mIconGravity == Gravity.BOTTOM) {
                tabView = View.inflate(mContext, R.layout.layout_tab_bottom, null)
            } else {
                tabView = View.inflate(mContext, R.layout.layout_tab_top, null)
            }

            tabView.tag = i
            addTab(i, tabView)
        }

        updateTabStyles()
    }

    /** 创建并添加tab  */
    private fun addTab(position: Int, tabView: View) {
        val tv_tab_title = tabView.findViewById<View>(R.id.tv_tab_title) as TextView
        tv_tab_title.text = mTabEntitys[position].tabTitle
        val iv_tab_icon = tabView.findViewById<View>(R.id.iv_tab_icon) as ImageView
        iv_tab_icon.setImageResource(mTabEntitys[position].tabUnselectedIcon)

        tabView.setOnClickListener { v ->
            val position = v.tag as Int
            if (mCurrentTab != position) {
                currentTab = position
                if (mListener != null) {
                    mListener!!.onTabSelect(position)
                }
            } else {
                if (mListener != null) {
                    mListener!!.onTabReselect(position)
                }
            }
        }

        /** 每一个Tab的布局参数  */
        var lp_tab = if (mTabSpaceEqual)
            LinearLayout.LayoutParams(0, FrameLayout.LayoutParams.MATCH_PARENT, 1.0f)
        else
            LinearLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.MATCH_PARENT)
        if (mTabWidth > 0) {
            lp_tab = LinearLayout.LayoutParams(mTabWidth.toInt(), FrameLayout.LayoutParams.MATCH_PARENT)
        }
        mTabsContainer.addView(tabView, position, lp_tab)
    }

    private fun updateTabStyles() {
        for (i in 0 until tabCount) {
            val tabView = mTabsContainer.getChildAt(i)
            tabView.setPadding(mTabPadding.toInt(), 0, mTabPadding.toInt(), 0)
            val tv_tab_title = tabView.findViewById<View>(R.id.tv_tab_title) as TextView
            tv_tab_title.setTextColor(if (i == mCurrentTab) mTextSelectColor else mTextUnselectColor)
            tv_tab_title.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextsize)
            //            tv_tab_title.setPadding((int) mTabPadding, 0, (int) mTabPadding, 0);
            if (mTextAllCaps) {
                tv_tab_title.text = tv_tab_title.text.toString().toUpperCase()
            }

            if (mTextBold == TEXT_BOLD_BOTH) {
                tv_tab_title.paint.isFakeBoldText = true
            } else if (mTextBold == TEXT_BOLD_NONE) {
                tv_tab_title.paint.isFakeBoldText = false
            }

            val iv_tab_icon = tabView.findViewById<View>(R.id.iv_tab_icon) as ImageView
            if (mIconVisible) {
                iv_tab_icon.visibility = View.VISIBLE
                val tabEntity = mTabEntitys[i]
                iv_tab_icon.setImageResource(if (i == mCurrentTab) tabEntity.tabSelectedIcon else tabEntity.tabUnselectedIcon)
                val lp = LinearLayout.LayoutParams(
                        if (mIconWidth <= 0) LinearLayout.LayoutParams.WRAP_CONTENT else mIconWidth.toInt(),
                        if (mIconHeight <= 0) LinearLayout.LayoutParams.WRAP_CONTENT else mIconHeight.toInt())
                if (mIconGravity == Gravity.LEFT) {
                    lp.rightMargin = mIconMargin.toInt()
                } else if (mIconGravity == Gravity.RIGHT) {
                    lp.leftMargin = mIconMargin.toInt()
                } else if (mIconGravity == Gravity.BOTTOM) {
                    lp.topMargin = mIconMargin.toInt()
                } else {
                    lp.bottomMargin = mIconMargin.toInt()
                }

                iv_tab_icon.layoutParams = lp
            } else {
                iv_tab_icon.visibility = View.GONE
            }
        }
    }

    private fun updateTabSelection(position: Int) {
        for (i in 0 until tabCount) {
            val tabView = mTabsContainer.getChildAt(i)
            val isSelect = i == position
            val tab_title = tabView.findViewById<View>(R.id.tv_tab_title) as TextView
            tab_title.setTextColor(if (isSelect) mTextSelectColor else mTextUnselectColor)
            val iv_tab_icon = tabView.findViewById<View>(R.id.iv_tab_icon) as ImageView
            val tabEntity = mTabEntitys[i]
            iv_tab_icon.setImageResource(if (isSelect) tabEntity.tabSelectedIcon else tabEntity.tabUnselectedIcon)
            if (mTextBold == TEXT_BOLD_WHEN_SELECT) {
                tab_title.paint.isFakeBoldText = isSelect
            }
        }
    }

    private fun calcOffset() {
        val currentTabView = mTabsContainer.getChildAt(this.mCurrentTab)
        mCurrentP.left = currentTabView.left.toFloat()
        mCurrentP.right = currentTabView.right.toFloat()

        val lastTabView = mTabsContainer.getChildAt(this.mLastTab)
        mLastP.left = lastTabView.left.toFloat()
        mLastP.right = lastTabView.right.toFloat()

        //        Log.d("AAA", "mLastP--->" + mLastP.left + "&" + mLastP.right);
        //        Log.d("AAA", "mCurrentP--->" + mCurrentP.left + "&" + mCurrentP.right);
        if (mLastP.left == mCurrentP.left && mLastP.right == mCurrentP.right) {
            invalidate()
        } else {
            mValueAnimator.setObjectValues(mLastP, mCurrentP)
            if (isIndicatorBounceEnable) {
                mValueAnimator.interpolator = mInterpolator
            }

            if (indicatorAnimDuration < 0) {
                indicatorAnimDuration = (if (isIndicatorBounceEnable) 500 else 250).toLong()
            }
            mValueAnimator.duration = indicatorAnimDuration
            mValueAnimator.start()
        }
    }

    private fun calcIndicatorRect() {
        val currentTabView = mTabsContainer.getChildAt(this.mCurrentTab)
        val left = currentTabView.left.toFloat()
        val right = currentTabView.right.toFloat()

        mIndicatorRect.left = left.toInt()
        mIndicatorRect.right = right.toInt()

        if (mIndicatorWidth < 0) {   //indicatorWidth小于0时,原jpardogo's PagerSlidingTabStrip

        } else {//indicatorWidth大于0时,圆角矩形以及三角形
            val indicatorLeft = currentTabView.left + (currentTabView.width - mIndicatorWidth) / 2

            mIndicatorRect.left = indicatorLeft.toInt()
            mIndicatorRect.right = (mIndicatorRect.left + mIndicatorWidth).toInt()
        }
    }

    override fun onAnimationUpdate(animation: ValueAnimator) {
        val currentTabView = mTabsContainer.getChildAt(this.mCurrentTab)
        val p = animation.animatedValue as IndicatorPoint
        mIndicatorRect.left = p.left.toInt()
        mIndicatorRect.right = p.right.toInt()

        if (mIndicatorWidth < 0) {   //indicatorWidth小于0时,原jpardogo's PagerSlidingTabStrip

        } else {//indicatorWidth大于0时,圆角矩形以及三角形
            val indicatorLeft = p.left + (currentTabView.width - mIndicatorWidth) / 2

            mIndicatorRect.left = indicatorLeft.toInt()
            mIndicatorRect.right = (mIndicatorRect.left + mIndicatorWidth).toInt()
        }
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (isInEditMode || tabCount <= 0) {
            return
        }

        val height = height
        val paddingLeft = paddingLeft
        // draw divider
        if (mDividerWidth > 0) {
            mDividerPaint.strokeWidth = mDividerWidth
            mDividerPaint.color = mDividerColor
            for (i in 0 until tabCount - 1) {
                val tab = mTabsContainer.getChildAt(i)
                canvas.drawLine((paddingLeft + tab.right).toFloat(), mDividerPadding, (paddingLeft + tab.right).toFloat(), height - mDividerPadding, mDividerPaint)
            }
        }

        // draw underline
        if (mUnderlineHeight > 0) {
            mRectPaint.color = mUnderlineColor
            if (mUnderlineGravity == Gravity.BOTTOM) {
                canvas.drawRect(paddingLeft.toFloat(), height - mUnderlineHeight, (mTabsContainer.width + paddingLeft).toFloat(), height.toFloat(), mRectPaint)
            } else {
                canvas.drawRect(paddingLeft.toFloat(), 0f, (mTabsContainer.width + paddingLeft).toFloat(), mUnderlineHeight, mRectPaint)
            }
        }

        //draw indicator line
        if (isIndicatorAnimEnable) {
            if (mIsFirstDraw) {
                mIsFirstDraw = false
                calcIndicatorRect()
            }
        } else {
            calcIndicatorRect()
        }


        if (mIndicatorStyle == STYLE_TRIANGLE) {
            if (mIndicatorHeight > 0) {
                mTrianglePaint.color = mIndicatorColor
                mTrianglePath.reset()
                mTrianglePath.moveTo((paddingLeft + mIndicatorRect.left).toFloat(), height.toFloat())
                mTrianglePath.lineTo((paddingLeft + mIndicatorRect.left / 2 + mIndicatorRect.right / 2).toFloat(), height - mIndicatorHeight)
                mTrianglePath.lineTo((paddingLeft + mIndicatorRect.right).toFloat(), height.toFloat())
                mTrianglePath.close()
                canvas.drawPath(mTrianglePath, mTrianglePaint)
            }
        } else if (mIndicatorStyle == STYLE_BLOCK) {
            if (mIndicatorHeight < 0) {
                mIndicatorHeight = height.toFloat() - indicatorMarginTop - indicatorMarginBottom
            } else {

            }

            if (mIndicatorHeight > 0) {
                if (mIndicatorCornerRadius < 0 || mIndicatorCornerRadius > mIndicatorHeight / 2) {
                    mIndicatorCornerRadius = mIndicatorHeight / 2
                }

                mIndicatorDrawable.setColor(mIndicatorColor)
                mIndicatorDrawable.setBounds(paddingLeft + indicatorMarginLeft.toInt() + mIndicatorRect.left,
                        indicatorMarginTop.toInt(), (paddingLeft + mIndicatorRect.right - indicatorMarginRight).toInt(),
                        (indicatorMarginTop + mIndicatorHeight).toInt())
                mIndicatorDrawable.cornerRadius = mIndicatorCornerRadius
                mIndicatorDrawable.draw(canvas)
            }
        } else {
            /* mRectPaint.setColor(mIndicatorColor);
                calcIndicatorRect();
                canvas.drawRect(getPaddingLeft() + mIndicatorRect.left, getHeight() - mIndicatorHeight,
                        mIndicatorRect.right + getPaddingLeft(), getHeight(), mRectPaint);*/

            if (mIndicatorHeight > 0) {
                mIndicatorDrawable.setColor(mIndicatorColor)
                if (mIndicatorGravity == Gravity.BOTTOM) {
                    mIndicatorDrawable.setBounds(paddingLeft + indicatorMarginLeft.toInt() + mIndicatorRect.left,
                            height - mIndicatorHeight.toInt() - indicatorMarginBottom.toInt(),
                            paddingLeft + mIndicatorRect.right - indicatorMarginRight.toInt(),
                            height - indicatorMarginBottom.toInt())
                } else {
                    mIndicatorDrawable.setBounds(paddingLeft + indicatorMarginLeft.toInt() + mIndicatorRect.left,
                            indicatorMarginTop.toInt(),
                            paddingLeft + mIndicatorRect.right - indicatorMarginRight.toInt(),
                            mIndicatorHeight.toInt() + indicatorMarginTop.toInt())
                }
                mIndicatorDrawable.cornerRadius = mIndicatorCornerRadius
                mIndicatorDrawable.draw(canvas)
            }
        }
    }

    fun setIndicatorGravity(indicatorGravity: Int) {
        this.mIndicatorGravity = indicatorGravity
        invalidate()
    }

    fun setIndicatorMargin(indicatorMarginLeft: Float, indicatorMarginTop: Float,
                           indicatorMarginRight: Float, indicatorMarginBottom: Float) {
        this.indicatorMarginLeft = dp2px(indicatorMarginLeft).toFloat()
        this.indicatorMarginTop = dp2px(indicatorMarginTop).toFloat()
        this.indicatorMarginRight = dp2px(indicatorMarginRight).toFloat()
        this.indicatorMarginBottom = dp2px(indicatorMarginBottom).toFloat()
        invalidate()
    }

    fun setUnderlineGravity(underlineGravity: Int) {
        this.mUnderlineGravity = underlineGravity
        invalidate()
    }


    fun getIconView(tab: Int): ImageView {
        val tabView = mTabsContainer.getChildAt(tab)
        return tabView.findViewById<View>(R.id.iv_tab_icon) as ImageView
    }

    fun getTitleView(tab: Int): TextView {
        val tabView = mTabsContainer.getChildAt(tab)
        return tabView.findViewById<View>(R.id.tv_tab_title) as TextView
    }

    fun setOnTabSelectListener(listener: OnTabSelectListener) {
        this.mListener = listener
    }


    override fun onSaveInstanceState(): Parcelable? {
        val bundle = Bundle()
        bundle.putParcelable("instanceState", super.onSaveInstanceState())
        bundle.putInt("mCurrentTab", mCurrentTab)
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        var state = state
        if (state is Bundle) {
            val bundle = state as Bundle?
            mCurrentTab = bundle!!.getInt("mCurrentTab")
            state = bundle.getParcelable("instanceState")
            if (mCurrentTab != 0 && mTabsContainer.childCount > 0) {
                updateTabSelection(mCurrentTab)
            }
        }
        super.onRestoreInstanceState(state)
    }

    internal inner class IndicatorPoint {
        var left: Float = 0.toFloat()
        var right: Float = 0.toFloat()
    }

    internal inner class PointEvaluator : TypeEvaluator<IndicatorPoint> {
        override fun evaluate(fraction: Float, startValue: IndicatorPoint, endValue: IndicatorPoint): IndicatorPoint {
            val left = startValue.left + fraction * (endValue.left - startValue.left)
            val right = startValue.right + fraction * (endValue.right - startValue.right)
            val point = IndicatorPoint()
            point.left = left
            point.right = right
            return point
        }
    }


    protected fun dp2px(dp: Float): Int {
        val scale = mContext.resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }

    protected fun sp2px(sp: Float): Int {
        val scale = this.mContext.resources.displayMetrics.scaledDensity
        return (sp * scale + 0.5f).toInt()
    }

    companion object {
        private val STYLE_NORMAL = 0
        private val STYLE_TRIANGLE = 1
        private val STYLE_BLOCK = 2

        /** title  */
        private val TEXT_BOLD_NONE = 0
        private val TEXT_BOLD_WHEN_SELECT = 1
        private val TEXT_BOLD_BOTH = 2
    }

    interface OnTabSelectListener {
        fun onTabSelect(position: Int)
        fun onTabReselect(position: Int)
    }

    interface CustomTabEntity {
        val tabTitle: String

        @get:DrawableRes
        val tabSelectedIcon: Int

        @get:DrawableRes
        val tabUnselectedIcon: Int
    }
}