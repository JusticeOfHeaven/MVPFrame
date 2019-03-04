package com.my.mvpframe.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 * Create by jzhan on 2019/3/2
 */
class CalculateView : View {
    private lateinit var mPaint: Paint
    private lateinit var mPaint1: Paint
    private var downX = 0f
    private var downY = 0f
    private var px = DoubleArray(2)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    private fun init(context: Context) {
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint.color = Color.RED
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = 2f

        mPaint1 = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint1.color = Color.BLACK
        mPaint1.style = Paint.Style.FILL
        mPaint1.strokeWidth = 5f
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        downX = w / 2f
        downY = h / 2f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // 椭圆长轴
        val a = 400f
        // 椭圆短轴
        val b = 260f

        canvas.drawRect(width / 2f - b, height / 2f - a, width / 2f + b, height / 2f + a, mPaint)
        canvas.drawOval(width / 2f - b, height / 2f - a, width / 2f + b, height / 2f + a, mPaint)
        canvas.drawPoint(width / 2f, height / 2f, mPaint)
        canvas.drawLine(width / 2f, height / 2f, downX, downY, mPaint)

        canvas.drawPoint(width / 2f, height / 2f + Math.sqrt(getDobule(a) - getDobule(b)).toFloat(), mPaint1)
        canvas.drawPoint(width / 2f, height / 2f - Math.sqrt(getDobule(a) - getDobule(b)).toFloat(), mPaint1)

        // 得到的直线斜率
        var k = 0f
        var e = 0f
        var x = 0f
        var y = 0f
        var y1 = 0f
        var y2 = 0f
        if (downX - width / 2f != 0f) {
            k = (downY - height / 2f) / (downX - width / 2f)
            e = downY - k * downX

            // 交点的坐标x
            val a1 = (1 / getDobule(b)) + (getDobule(k) / getDobule(a))
            val b1 = (2 * k * e / getDobule(a)) - (width / getDobule(b)) - (height * k / getDobule(a))
            val c1 = (getDobule(width / 2f) / getDobule(b)) + (getDobule(e) / getDobule(a)) - (e * height / getDobule(a)) + (getDobule(height / 2f) / getDobule(a)) - 1
            // 求解交点x的坐标
            getXPoint(a1, b1, c1)
            // 求得交点y的坐标
            y1 = (k * px[0] + e).toFloat()
            y2 = (k * px[1] + e).toFloat()
            // 排除另一个反方向的点
            if (downX - width / 2f >= 0) {
                x = px[0].toFloat()
                y = y1
            } else {
                x = px[1].toFloat()
                y = y2
            }
        } else {
            // 当downX = width/2的时候
            px[0] = width / 2.0
            px[1] = width / 2.0
            x = width / 2f
            y1 = height / 2f + a
            y2 = height / 2f - a
            if (downY >= height / 2f) {
                y = height / 2f + a
            } else {
                y = height / 2f - a
            }
        }

//        canvas.drawPoint(x, y, mPaint1)

        // 交点到椭圆圆心的距离
        val distance = calculateDistance(x, y, width / 2f, height / 2f)
        // 触摸点到椭圆圆心的距离
        val distance1 = calculateDistance(downX, downY, width / 2f, height / 2f)
        val percent = distance1 / distance

        notifyDataChanged?.onDataChanged(k, e, px, y1, y2, percent)

        calPercentBetweenOvalAndLine(a, b, width / 2f, height / 2f, downX, downY,canvas)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                downX = event.x
                downY = event.y
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
//                downX = width/2f
//                downY = height/2f
            }
        }
        return true
    }

    fun getDobule(a: Float): Double = Math.pow(a.toDouble(), 2.0)

    // 计算两点之间的距离
    fun calculateDistance(x: PointF, y: PointF): Float {
        return Math.sqrt(Math.pow(((x.x - y.x).toDouble()), 2.0) + Math.pow(((x.y - y.y).toDouble()), 2.0)).toFloat()
    }

    fun calculateDistance(startX: Float, startY: Float, endX: Float, endY: Float): Float {
        return Math.sqrt(Math.pow(((startX - endX).toDouble()), 2.0) + Math.pow(((startY - endY).toDouble()), 2.0)).toFloat()
    }

    /**
     * 计算椭圆内任意一点到椭圆原点的距离，与该点和椭圆原点形成的直线与椭圆的交点的距离的百分比
     * @param a 椭圆长轴
     * @param b 椭圆短轴
     * @param ovalX 椭圆原点X坐标
     * @param ovalY 椭圆原点Y坐标
     * @param downX 椭圆任意一点X坐标
     * @param downY 椭圆任意一点Y坐标
     */
    fun calPercentBetweenOvalAndLine(a: Float, b: Float, ovalX: Float, ovalY: Float,
                                     downX: Float, downY: Float, canvas: Canvas):Float {
        // 交点坐标
        var x = 0f
        var y = 0f
        var k = 0f
        var e = 0f
        var x1 = 0.0
        var x2 = 0.0
        var y1 = 0.0
        var y2 = 0.0
        if (downX != ovalX) {
            // 当直线斜率存在的时候
            // 计算原点与任意一点生成的直线
            k = (downY - ovalY) / (downX - ovalX)
            e = ovalY - ovalX * k

            // 计算交点坐标
            val a1 = (1 / getDobule(b)) + (getDobule(k) / getDobule(a))
            val b1 = (2 * k * e / getDobule(a)) - (2 * ovalX / getDobule(b)) - (2 * ovalY * k / getDobule(a))
            val c1 = (getDobule(ovalX) / getDobule(b)) + (getDobule(e) / getDobule(a)) -
                    (e * ovalY * 2 / getDobule(a)) + (getDobule(ovalY) / getDobule(a)) - 1

            val d = Math.pow(b1, 2.0) - 4 * a1 * c1

            if (d >= 0) {
                val sqrt = Math.sqrt(d)
                x1 = ((-b1 + sqrt) / (2f * a1))
                x2 = ((-b1 - sqrt) / (2f * a1))
            }

            y1 = k * x1 + e
            y2 = k * x2 + e

            // 直线与椭圆的交点是两个，筛选出以椭圆原点为射点的直线与椭圆相交的点
            if (downX >= ovalX) {
                x = x1.toFloat()
                y = y1.toFloat()
            } else {
                x = x2.toFloat()
                y = y2.toFloat()
            }
        } else {
            // 当直线斜率不存在的时候
            x = ovalX
            if (downY >= ovalY) {
                y = (ovalY + a)
            } else {
                y = (ovalY - a)
            }
        }

        // 交点到椭圆圆心的距离
        val distance = calculateDistance(x, y, ovalX, ovalY)
        // 触摸点到椭圆圆心的距离
        val distance1 = calculateDistance(downX, downY, ovalX, ovalY)
        val fl = distance1 / distance

        notifyDataChanged?.onDataChanged1(x1,y1,x2,y2,x,y)
        canvas.drawPoint(x,y,mPaint1)
        return fl
    }


    // 二元一次方程求解
    fun getXPoint(a: Double, b: Double, c: Double) {
        val d = Math.pow(b, 2.0) - 4 * a * c
        if (d >= 0) {
            val sqrt = Math.sqrt(d)
            px[0] = (-b + sqrt) / (2 * a)
            px[1] = (-b - sqrt) / (2 * a)
        } else {
            px[0] = 0.0
            px[1] = 0.0
        }
    }

    // 椭圆方程自测
    fun getTest(x: Float, y: Float, a: Float, b: Float): Double {
        return (getDobule(x - width / 2f) / getDobule(b)) + (getDobule(y - height / 2f) / getDobule(a))
    }

    var notifyDataChanged: NotifyDataChanged? = null

    public interface NotifyDataChanged {
        fun onDataChanged(k: Float, b: Float, px: DoubleArray, y1: Float, y2: Float, percent: Float) {

        }
        fun onDataChanged1(x1: Double, y1: Double, x2: Double, y2: Double, x: Float, y: Float) {

        }
    }

}