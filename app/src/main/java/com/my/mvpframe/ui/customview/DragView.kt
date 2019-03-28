package com.my.mvpframe.ui.customview

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.os.Build
import android.os.Handler
import android.os.Message
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import android.util.Log
import android.view.*
import android.view.animation.LinearInterpolator

/**
 * Create by jzhan on 2019/3/6
 */
class DragView : AppCompatImageView, ViewTreeObserver.OnGlobalLayoutListener {

    private val TAG = "DragView"
    private lateinit var mScaleGestureDetector: ScaleGestureDetector
    private lateinit var mGestureDetector: GestureDetector
    private lateinit var mMatrix: Matrix
    private lateinit var mLinePaint: Paint
    private var mFirstLayout: Boolean = false

    private var mBaseScale = 1.0f
    private var mMaxScale = 3.0f
    private var mCurrentScaleAnimCount = 0
    // 缩放手势(两个手指)中点位置
    private var mLastFocusX: Float = 0f
    private var mLastFocusY: Float = 0f
    private var mPreScaleFactor = 1.0f
    // 是否绘制线条
    private var mIsDragging: Boolean = false

    private var mTouchSlop = -1
    private val SCALE_ANIM_COUNT = 10L
    private val MAX_SCROLL_FACTOR = 3
    private val DAMP_FACTOR = 9
    private val ZOOM_OUT_ANIM_WHIT = 0
    private val ZOOM_ANIM_WHIT = 1
    private val LINE_ROW_NUMBER = 3
    private val LINE_COLUMN_NUMBER = 3

    private val mHandler = object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            if (msg != null) {
                if (mCurrentScaleAnimCount < SCALE_ANIM_COUNT) {
                    val obj = msg!!.obj as Float
                    mMatrix.postScale(obj, obj, mLastFocusX, mLastFocusY)
                    imageMatrix = mMatrix
                    mCurrentScaleAnimCount++
                    // what scale > mMaxScale 取0 不然取 1
                    sendScaleMessage(obj, msg!!.what, SCALE_ANIM_COUNT)
                } else if (mCurrentScaleAnimCount >= SCALE_ANIM_COUNT) {
                    val values = FloatArray(9)
                    mMatrix.getValues(values)
                    if (msg!!.what === ZOOM_OUT_ANIM_WHIT) {
                        values[Matrix.MSCALE_X] = mMaxScale
                        values[Matrix.MSCALE_Y] = mMaxScale
                    } else if (msg!!.what === ZOOM_ANIM_WHIT) {
                        values[Matrix.MSCALE_X] = mBaseScale
                        values[Matrix.MSCALE_Y] = mBaseScale
                    }
                    mMatrix.setValues(values)
                    imageMatrix = mMatrix

                    // 边界检测
                    boundCheck()
                }
            }
        }
    }

    // 处理双指的缩放
    private val mOnScaleGestureListener = object : ScaleGestureDetector.OnScaleGestureListener {
        override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean {
            // 注意返回true
            return true
        }

        override fun onScale(detector: ScaleGestureDetector?): Boolean {
            if (drawable == null || mMatrix == null) {
                // 如果返回true那么detector就会重置缩放事件
                return true
            }
            mIsDragging = true
            // 缩放因子,缩小小于1,放大大于1
            val scaleFactor = mScaleGestureDetector.scaleFactor
            // 缩放因子偏移量
            val deltaFactor = scaleFactor - mPreScaleFactor

            if (scaleFactor != 1.0F && deltaFactor != 0F) {
                mLastFocusX = mScaleGestureDetector.getFocusX()
                mLastFocusY = mScaleGestureDetector.getFocusY()
                mMatrix.postScale(deltaFactor + 1F, deltaFactor + 1F, mLastFocusX,
                        mLastFocusY);
                setImageMatrix(mMatrix);
            }
            mPreScaleFactor = scaleFactor;
            return false;
        }

        override fun onScaleEnd(detector: ScaleGestureDetector?) {}
    }

    // 处理手指滑动
    private val mSimpleOnGestureListener = object : GestureDetector.SimpleOnGestureListener() {
        /**
         * 按下。返回值表示事件是否处理
         */
        override fun onDown(e: MotionEvent?): Boolean {
            return true
        }
        /**
         * 短按(手指尚未松开也没有达到scroll条件)
         */
        override fun onShowPress(e: MotionEvent?) {
            super.onShowPress(e)
        }
        /**
         * 轻触(手指松开)
         */
        override fun onSingleTapUp(e: MotionEvent?): Boolean {
            return super.onSingleTapUp(e)
        }
        /**
         * 单击事件(onSingleTapConfirmed，onDoubleTap是两个互斥的函数)
         */
        override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
            return super.onSingleTapConfirmed(e)
        }
        /**
         * 双击事件产生之后手指还没有抬起的时候的后续事件
         */
        override fun onDoubleTapEvent(e: MotionEvent?): Boolean {
            return super.onDoubleTapEvent(e)
        }
        /**
         * 双击事件
         */
        override fun onDoubleTap(e: MotionEvent?): Boolean {
            return super.onDoubleTap(e)
        }

        /**
         * 滑屏(用户按下触摸屏、快速滑动后松开，返回值表示事件是否处理)
         */
        override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
            return super.onFling(e1, e2, velocityX, velocityY)
        }

        /**
         * 滑动(一次完整的事件可能会多次触发该函数)。返回值表示事件是否处理
         */
        override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
            var distanceX = distanceX
            var distanceY = distanceY
            if (e1?.pointerCount == e2?.pointerCount && e1?.pointerCount == 1) {
                mIsDragging = true
                // 获取图片矩阵
                val rectF = getMatrixRectF()

                val leftEdgeDistanceLeft = rectF.left
                val topEdgeDistanceTop = rectF.top

                val rightEdgeDistanceRight = leftEdgeDistanceLeft + rectF.right - rectF.left - width
                val bottomEdgeDistanceBottom = topEdgeDistanceTop + rectF.bottom - rectF.top - height

                // MAX_SCROLL_FACTOR = 3
                val maxOffsetWidth = width / MAX_SCROLL_FACTOR
                val maxOffsetHeight = height / MAX_SCROLL_FACTOR

                // 图片左侧越界并且图片右侧未越界
                if (leftEdgeDistanceLeft > 0 && rightEdgeDistanceRight > 0) {
                    // distanceX < 0 表示继续向右滑动
                    if (distanceX < 0) {
                        if (leftEdgeDistanceLeft < maxOffsetWidth) {
                            // DAMP_FACTOR = 9 系数越大阻尼越大  +1防止ratio为0
                            val ratio = (DAMP_FACTOR / maxOffsetWidth * leftEdgeDistanceLeft) + 1
                            distanceX /= ratio
                        } else {
                            // 图片向右滑动超过了最大偏移量 图片则不平移
                            distanceX = 0f
                        }
                    }
                }
                // 图片右侧越界并且图片左侧未越界 （同上处理）
                else if (rightEdgeDistanceRight < 0 && leftEdgeDistanceLeft < 0) {
                    // distanceX > 0 表示继续向左滑动
                    if (distanceX > 0) {
                        if (rightEdgeDistanceRight > -maxOffsetWidth) {
                            val ratio = (DAMP_FACTOR / maxOffsetWidth * -rightEdgeDistanceRight) + 1;
                            distanceX /= ratio;
                        } else {
                            // 图片右侧距离控件右侧超过最大偏移量 图片则不平移
                            distanceX = 0f;
                        }
                    }
                }
                // 图片左侧越界并且图片右侧越界
                else if (leftEdgeDistanceLeft > 0 && rightEdgeDistanceRight < 0) {
                    // 控件宽度的一半
                    val halfWidth = getWidth() / 2;
                    // 获取图片中点x坐标
                    val centerX = (rectF.right - rectF.left) / 2 + rectF.left;
                    // 图片中点x坐标是否右侧偏移
                    val rightOffsetCenterX = centerX >= halfWidth;
                    // 右侧偏移并且向右滑动
                    if (distanceX < 0 && rightOffsetCenterX) {
                        // centerX - halfWidth 图片右侧偏移量
                        val ratio = (DAMP_FACTOR / maxOffsetWidth * (centerX - halfWidth)) + 1;
                        distanceX /= ratio;
                    }
                    // 左侧偏移并且向左滑动
                    else if (distanceX > 0 && !rightOffsetCenterX) {
                        // halfWidth - centerX 左侧的偏移量
                        val ratio = (DAMP_FACTOR / maxOffsetWidth * (halfWidth - centerX)) + 1;
                        distanceX /= ratio;
                    }
                }
                // 上下越界 处理方式同左右处理方式一样 本可以提成一个方法但为了方便理解先这样了
                // 图片上侧越界并且图片下侧未越界
                if (topEdgeDistanceTop > 0 && bottomEdgeDistanceBottom > 0) {
                    // distanceY < 0 表示图片继续向下滑动
                    if (distanceY < 0) {
                        if (topEdgeDistanceTop < maxOffsetHeight) {
                            // 获取阻尼比例
                            val ratio = (DAMP_FACTOR / maxOffsetHeight * topEdgeDistanceTop) + 1
                            distanceY /= ratio
                        } else {
                            // 向下滑动超过了最大偏移量 则图片不滑动
                            distanceY = 0f
                        }
                    }
                }
                else if (bottomEdgeDistanceBottom < 0 && topEdgeDistanceTop < 0) {
                    if (distanceY > 0) {
                        if (bottomEdgeDistanceBottom > -maxOffsetHeight) {
                            val ratio =(DAMP_FACTOR / maxOffsetHeight * -bottomEdgeDistanceBottom)+ 1;
                            distanceY /= ratio;
                        } else {
                            // 向上滑动超过了最大偏移量 则图片不滑动
                            distanceY = 0f;
                        }
                    }
                } else if (topEdgeDistanceTop > 0 && bottomEdgeDistanceBottom < 0) {
                    val halfHeight = getHeight() / 2;
                    // 获取图片中点y坐标
                    val centerY = (rectF.bottom - rectF.top) / 2 + rectF.top;
                    // 图片中点y坐标是否向下偏移
                    val bottomOffsetCenterY = centerY >= halfHeight;
                    // 向下偏移并且向下移动
                    if (distanceY < 0 && bottomOffsetCenterY) {
                        // centerY - halfHeight 图片偏移量
                        val ratio = (DAMP_FACTOR / maxOffsetHeight * (centerY - halfHeight)) + 1;
                        distanceY /= ratio;
                    } else if (distanceY > 0 && !bottomOffsetCenterY) { // 向上偏移并且向上移动
                        val ratio = (DAMP_FACTOR / maxOffsetHeight * (halfHeight - centerY))+ 1;
                        distanceY /= ratio;
                    }
                }

                mMatrix.postTranslate(-distanceX, -distanceY);
                setImageMatrix(mMatrix);
                return true;
            }
            return super.onScroll(e1, e2, distanceX, distanceY)
        }
        /**
         * 长按(手指尚未松开也没有达到scroll条件)
         */
        override fun onLongPress(e: MotionEvent?) {
            super.onLongPress(e)
        }
        override fun onContextClick(e: MotionEvent?): Boolean {
            return super.onContextClick(e)
        }
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {

        mScaleGestureDetector = ScaleGestureDetector(context, mOnScaleGestureListener)
        mGestureDetector = GestureDetector(context, mSimpleOnGestureListener)

        mFirstLayout = true
        mMatrix = Matrix()
        scaleType = ScaleType.MATRIX

        mLinePaint = Paint()
        mLinePaint.isAntiAlias = true
        mLinePaint.color = Color.WHITE
        mLinePaint.strokeWidth = dip2px(context, 0.5f)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        if (widthSize > heightSize) {
            super.onMeasure(heightMeasureSpec, heightMeasureSpec)
        } else {
            super.onMeasure(widthMeasureSpec, widthMeasureSpec)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        Log.d(TAG, "onSizeChanged")
    }

    override fun onGlobalLayout() {
        Log.d(TAG, "onGlobalLayout")
        if (mFirstLayout) {
            mFirstLayout = false
            mMatrix.reset()
            // 获取控件的宽度和高度
            val viewWidth = width
            val viewHeight = height

            // 图片的固定宽度  高度
            // 获取图片的宽度和高度
            val drawable = drawable
            if (null == drawable) {
                return;
            }
            val drawableWidth = drawable.intrinsicWidth
            val drawableHeight = drawable.intrinsicHeight

            // 将图片移动到屏幕的中点位置
            val dx = (viewWidth - drawableWidth) / 2f
            val dy = (viewHeight - drawableHeight) / 2f

            mBaseScale = Math.max(viewWidth / drawableWidth, viewHeight / drawableHeight).toFloat()
            // 平移居中
            mMatrix.postTranslate(dx, dy)
            // 缩放
            mMatrix.postScale(mBaseScale, mBaseScale, viewWidth / 2f, viewHeight / 2f);
            setImageMatrix(mMatrix);

            if (mBaseScale >= mMaxScale) {
                mMaxScale = Math.floor(mBaseScale.toDouble()).toFloat() + 2f
            } else if (mBaseScale < 1.0f) {
                mMaxScale = 1.0f
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (mIsDragging) {
            canvas.save();
            drawLine(canvas);
            canvas.restore();
        }
    }
    // 绘制九宫线条
    private fun drawLine(canvas: Canvas) {
        // 开始点
        var startX = 0f
        var startY = 0f
        // 结束点
        var endX = 0f
        var endY = 0f

        val rectF = getMatrixRectF()
        startX = if (rectF.left <= 0) 0f else rectF.left
        startY = if (rectF.top <= 0) 0f else rectF.top

        endX = if (rectF.right >= width) width.toFloat() else rectF.right
        endY = if (rectF.bottom >= height) height.toFloat() else rectF.bottom

        var lineWidth = 0f
        var lineHeight = 0f

        lineWidth = endX - startX;
        lineHeight = endY - startY;

        // LINE_ROW_NUMBER = 3 表示多少行
        for (i in 1 until LINE_ROW_NUMBER) {
            canvas.drawLine(startX + 0, startY + lineHeight / LINE_ROW_NUMBER * i, endX, startY + lineHeight / LINE_ROW_NUMBER * i, mLinePaint)
        }

        // LINE_COLUMN_NUMBER = 3 表示多少列
        for (i in 1 until LINE_COLUMN_NUMBER) {
            canvas.drawLine(startX + lineWidth / LINE_COLUMN_NUMBER * i, startY, startX + lineWidth / LINE_COLUMN_NUMBER * i, endY, mLinePaint)
        }

        mHandler.removeCallbacks(lineRunnable);
        mHandler.postDelayed(lineRunnable, 400);
    }

    // 获取图片矩阵区域
    private fun getMatrixRectF(): RectF {
        val rectF = RectF()
        val drawable = drawable
        if (drawable != null) {
            rectF.set(0f, 0f, drawable.intrinsicWidth.toFloat(), drawable.intrinsicHeight.toFloat())
            mMatrix.mapRect(rectF)
        }
        return rectF
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (mTouchSlop < 0) {
            mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        }

        when (event.getAction()) {
            MotionEvent.ACTION_DOWN -> {
                // 防止父类拦截事件
                parent.requestDisallowInterceptTouchEvent(true)
                mIsDragging = false
            }
            MotionEvent.ACTION_MOVE -> {
            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                mCurrentScaleAnimCount = 0
                mIsDragging = false
                invalidate()
                val scale = getScale()
                if (scale > mMaxScale) {
                    // 缩小 SCALE_ANIM_COUNT = 10
                    sendScaleMessage(getRelativeValue((mMaxScale / scale).toDouble(), SCALE_ANIM_COUNT.toDouble()), ZOOM_OUT_ANIM_WHIT, 0)
                } else if (scale < mBaseScale) {
                    // 放大
                    sendScaleMessage(getRelativeValue((mBaseScale / scale).toDouble(), SCALE_ANIM_COUNT.toDouble()), ZOOM_ANIM_WHIT, 0)
                } else {
                    // 平移
                    boundCheck()
                }
                parent.requestDisallowInterceptTouchEvent(true)
            }
        }
        if (mGestureDetector.onTouchEvent(event)) {
            return true
        }
        mScaleGestureDetector.onTouchEvent(event)
        return true
    }

    private fun getScale(): Float {
        val values = FloatArray(9)
        mMatrix.getValues(values)
        return values[Matrix.MSCALE_X]
    }

    /**
     * 计算d的1/count次幂
     *
     * @param d
     * @param count 开根的次数
     * @return 相对值
     */
    private fun getRelativeValue(d: Double, count: Double): Float {
        var count = count
        if (count == 0.0) {
            return 1f
        }
        count = 1 / count
        return Math.pow(d, count).toFloat()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        viewTreeObserver.addOnGlobalLayoutListener(this)
    }

    override fun onDetachedFromWindow() {
        if (mHandler != null) {
            // 防止内存泄露
            mHandler.removeCallbacksAndMessages(null);
        }
        super.onDetachedFromWindow()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            viewTreeObserver.removeOnGlobalLayoutListener(this);
        } else {
            viewTreeObserver.removeGlobalOnLayoutListener(this);
        }
    }

    /**
     * 发送消息
     *
     * @param relativeScale
     * @param what
     * @param delayMillis
     */
    private fun sendScaleMessage(relativeScale: Float, what: Int, delayMillis: Long) {
        val mes = Message()
        mes.obj = relativeScale
        mes.what = what
        mHandler.sendMessageDelayed(mes, delayMillis)
    }

    private fun boundCheck() {
        // 获取图片矩阵
        val rectF = getMatrixRectF()

        if (rectF.left >= 0) {
            // 左越界
            startBoundAnimator(rectF.left, 0f, true)
        }

        if (rectF.top >= 0) {
            // 上越界
            startBoundAnimator(rectF.top, 0f, false)
        }

        if (rectF.right <= width) {
            // 右越界
            startBoundAnimator(rectF.left, rectF.left + width - rectF.right, true)
        }

        if (rectF.bottom <= height) {
            // 下越界
            startBoundAnimator(rectF.top, height - rectF.bottom + rectF.top, false)
        }
    }

    var boundAnimator: ValueAnimator? = null

    /**
     * 开始越界动画
     *
     * @param start      开始点坐标
     * @param end        结束点坐标
     * @param horizontal 是否水平动画  true 水平动画 false 垂直动画
     */
    private fun startBoundAnimator(start: Float, end: Float, horizontal: Boolean) {
        boundAnimator = ValueAnimator.ofFloat(start, end)
        boundAnimator?.setDuration(200)
        boundAnimator?.setInterpolator(LinearInterpolator())
        boundAnimator?.addUpdateListener { animation ->
            val v:Float = animation.animatedValue as Float

            val values = FloatArray(9)
            mMatrix.getValues(values)
            values[if (horizontal) Matrix.MTRANS_X else Matrix.MTRANS_Y] = v

            mMatrix.setValues(values)
            imageMatrix = mMatrix
        }
        boundAnimator?.start()
    }



    fun dip2px(context: Context, dpValue: Float): Float {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f)
    }

    private val lineRunnable = Runnable {
        mIsDragging = false
        invalidate()
    }
}