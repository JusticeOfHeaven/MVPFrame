package com.my.mvpframe.customview.breathview

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import com.my.mvpframe.R


/**
 * Create by jzhan on 2018/11/27
 */
class BreathView : View {

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    var drawable:Drawable? = null
    var bitmap:Bitmap? = null
    private lateinit var paint: Paint
    private lateinit var bigCirclePaint: Paint
//    private var width: Int =0


    private fun init(context: Context?, attrs: AttributeSet?) {
        val array = context?.obtainStyledAttributes(attrs, R.styleable.BreathView)
        drawable = array?.getDrawable(R.styleable.BreathView_breathDrawable)
        array?.recycle()


        setWillNotDraw(false)

//        val bd = drawable as BitmapDrawable
//        bitmap = bd.bitmap
        bitmap = drawableToBitmap(drawable!!)

        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        // 最外层大圆
        bigCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        bigCirclePaint.color = Color.parseColor("#E5E5E5")

//        rectF = RectF()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
//        width = w
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

//        canvas.drawCircle()

        canvas.drawBitmap(bitmap,0f,0f,paint)

    }

    fun drawableToBitmap(drawable:Drawable):Bitmap{
        // 取 drawable 的长宽
        var w = drawable.intrinsicWidth
        var h = drawable.intrinsicHeight

        // 取 drawable 的颜色格式
        var config = if (drawable.opacity != PixelFormat.OPAQUE)  Bitmap.Config.ARGB_8888 else Bitmap.Config.RGB_565
        // 建立对应 bitmap
        var bitmap = Bitmap.createBitmap(w, h, config)
        // 建立对应 bitmap 的画布
        var canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, w, h)
        // 把 drawable 内容画到画布中
        drawable.draw(canvas)
        return bitmap
    }
}