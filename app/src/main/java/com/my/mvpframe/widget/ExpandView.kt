package com.my.mvpframe.widget

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.support.v7.widget.AppCompatTextView
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver

/**
 * Create by jzhan on 2019/3/27
 *
 * 能够展开、收起的TextView
 *
 */
class ExpandView : AppCompatTextView {

    private val TAG = "ExpandView"
    private lateinit var mContext: Context
    private var mMaxLines = 2
    private var originText = ""
    private var isExpand = false
    private var mMeasured: Boolean = false
    private var expandSpan:SpannableString?=null// 展开的文案(颜色处理)
    private var closeSpan:SpannableString?=null// 收起的文案(颜色处理)
    private val EXPAND_TEXT = "展开"
    private val CLOSE_TEXT = "收起"
    private var expandOrCloseTextColor:Int = Color.parseColor("#fe9901")

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    private fun init(context: Context) {
        expandSpan = createSpannable(EXPAND_TEXT)
        closeSpan = createSpannable(CLOSE_TEXT)
        this.mContext = context
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mMeasured = true
    }

    fun setConTent(content: String) {
        this.originText = content
        mMaxLines = maxLines
        if (mMeasured) {
            setCloseText(content)
        } else {
            viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                    setCloseText(content)
                }
            })
        }
    }

    // 设置关闭的文字
    private fun setCloseText(content: String) {
        if (expandSpan == null) {
            expandSpan = createSpannable(EXPAND_TEXT)
        }
        // 创建展开的按钮Spannable
        var layout = createLayout(content)

        val stringBuilder = SpannableStringBuilder()
        var tempText = ""

        if (layout.lineCount > mMaxLines) {
            // 获取规定行数显示的全部文字
            tempText = content.substring(0, layout.getLineEnd(mMaxLines - 1)).trim()
            var length = tempText.length
            var showText = "$tempText...$expandSpan"

            var layout1 = createLayout(showText)
            while (layout1.lineCount > mMaxLines) {
                length -= 1
                if (length == -1) break

                tempText = tempText.substring(0, length)

                showText = "$tempText...$expandSpan"
                layout1 = createLayout(showText)
                Log.d(TAG,"$showText")
            }
        }
        stringBuilder.append(tempText).append("...").append(expandSpan)
        Log.d(TAG,"${stringBuilder.toString()}")
        text = stringBuilder
        movementMethod = LinkMovementMethod.getInstance()
        highlightColor = Color.TRANSPARENT
    }

    private fun createSpannable(content: String) :SpannableString{
        val spannableString = SpannableString(content)
        spannableString.setSpan(
            ClickSpan(expandOrCloseTextColor, OnClickListener {
                if (!isExpand) {
                    isExpand = true
                    super@ExpandView.setMaxLines(Integer.MAX_VALUE)
                    setExpandText()
                } else {
                    isExpand = false
                    super@ExpandView.setMaxLines(mMaxLines)
                    setCloseText(originText)
                }
            }),
            0,
            content.length,
            Spanned.SPAN_INCLUSIVE_EXCLUSIVE
        )
        return spannableString
    }

    // 设置展开的文字
    private fun setExpandText() {
        if (closeSpan == null) {
            closeSpan = createSpannable(CLOSE_TEXT)
        }
        val stringBuilder = SpannableStringBuilder()
        val append = stringBuilder.append(originText).append(closeSpan)
        text = append
    }

    private fun createLayout(content: String): StaticLayout {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StaticLayout.Builder.obtain(
                content, 0, content.length, paint,
                (width - paddingLeft - paddingRight).toInt()
            ).build()
        } else {
            StaticLayout(
                content,
                paint,
                width - paddingLeft - paddingRight,
                Layout.Alignment.ALIGN_NORMAL,
                lineSpacingMultiplier,
                lineSpacingExtra,
                false
            )
        }
    }

    inner class ClickSpan : ClickableSpan {

        private var onClickListener: View.OnClickListener
        private var colorId: Int

        constructor(colorId: Int, onClickListener: View.OnClickListener) : super() {
            this.colorId = colorId
            this.onClickListener = onClickListener
        }

        override fun updateDrawState(ds: TextPaint) {
            ds.color = context.resources.getColor(colorId)
            ds.isUnderlineText = true
        }

        override fun onClick(widget: View) {
            onClickListener?.onClick(widget)
        }
    }
}


