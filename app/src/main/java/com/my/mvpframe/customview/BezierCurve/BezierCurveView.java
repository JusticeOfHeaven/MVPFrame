package com.my.mvpframe.customview.BezierCurve;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jzhan on 2018/11/15.
 * 仿QQ消息的粘性
 **/
public class BezierCurveView extends View {


    private Paint paint;
    private int width;
    private int height;
    private float fixRadius;
    private PointF mFixedCircle;
    private PointF mDragCircle;
    private float farestDistance = 5;
    private List<PointF> mFixedPoints = new ArrayList<>();
    private List<PointF> mDragPoints = new ArrayList<>();
    private PointF mControlPoint;
    private Paint pointpPaint;
    private Path path;
    private Paint circlePaint;
    private float maxDistance = 400;
    private GestureDetector gestureDetector;
    private float dragRadius;
    private int touchSlop;
    private float downX;
    private float downY;
    private boolean isOutRange;
    private Scroller mScroller;

    public BezierCurveView(Context context) {
        this(context, null);
    }

    public BezierCurveView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BezierCurveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        gestureDetector = new GestureDetector(context, listener);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mScroller = new Scroller(context, new DecelerateInterpolator());

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);

        pointpPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        pointpPaint.setColor(Color.BLUE);

        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setColor(Color.BLUE);
        circlePaint.setStrokeWidth(5);
        circlePaint.setStyle(Paint.Style.STROKE);

        path = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.e("TAG", "onSizeChanged");
        width = w;
        height = h;
//        fixRadius = width/2-50;
        fixRadius =  dragRadius = 50;
        mFixedCircle = new PointF(width / 2, height / 2);
        mDragCircle = new PointF(width / 2, height /2+maxDistance);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //两个圆的圆心的距离
        float distance = getDistanceBetween2Points(mFixedCircle, mDragCircle);

        //计算连接部分

        //通过几何工具获取焦点坐标
        this.mFixedPoints = getIntersectionPoints(mFixedCircle, mDragCircle, distance,fixRadius);
        this.mDragPoints = getIntersectionPoints(mDragCircle, mFixedCircle, distance, dragRadius);
        //2、获取控制点坐标
        this.mControlPoint = getMiddlePoint(mDragCircle, mFixedCircle);
        // 画两个半圆
        canvas.drawCircle(mDragCircle.x, mDragCircle.y, dragRadius, paint);

        if (!isOutRange) {
            canvas.drawCircle(mFixedCircle.x, mFixedCircle.y, fixRadius, paint);

            canvas.save();
            //画连接部分   这个是用的那个贝塞尔曲线绘制的连接部分
            path.reset();
            //跳到某个点1
            path.moveTo(mFixedPoints.get(0).x, mFixedPoints.get(0).y);
            //画曲线 1--->2
            path.quadTo(mControlPoint.x, mControlPoint.y, mDragPoints.get(1).x, mDragPoints.get(1).y);
            //画直线2---->3
            path.lineTo(mDragPoints.get(0).x, mDragPoints.get(0).y);
            //画曲线3---->4
            path.quadTo(mControlPoint.x, mControlPoint.y, mFixedPoints.get(1).x, mFixedPoints.get(1).y);
            path.close();
            canvas.drawPath(path, paint);
            canvas.restore();
        }

        // 画最大距离的那个圆
        drawBigDistance(canvas);

        // 五个辅助点
        drawFivePoint(canvas);
    }

    private void drawBigDistance(Canvas canvas) {
        canvas.drawCircle(mFixedCircle.x, mFixedCircle.y, maxDistance, circlePaint);
    }

    GestureDetector.SimpleOnGestureListener listener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            float x = e2.getX();
            float y = e2.getY();
            float distanceBetween2Points = getDistanceBetween2Points(mFixedCircle, x, y);
            Float evaluate = evaluate(0.5f, distanceBetween2Points, maxDistance);
            if (distanceBetween2Points <= maxDistance) {
                fixRadius = (maxDistance-distanceBetween2Points)*dragRadius/maxDistance;
            }
            updateDragCircle(x, y);

            return true;
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        gestureDetector.onTouchEvent(event);
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                Log.i("TAG","DOWN");
                downX = event.getX();
                downY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float x = event.getX();
                float y = event.getY();
                if (Math.abs(x - downX) >= touchSlop || Math.abs(y - downY) >= touchSlop) {
                    float distanceBetween2Points = getDistanceBetween2Points(mFixedCircle, x, y);
                    Float evaluate = evaluate(0.8f, distanceBetween2Points, maxDistance);
                    if (distanceBetween2Points <= maxDistance) {
                        isOutRange = false;
                        fixRadius = (maxDistance - distanceBetween2Points) * dragRadius / maxDistance;
                    } else {
                        isOutRange = true;
                    }
                    updateDragCircle(x, y);

                    // 记录位置
                    downX = Math.abs(x - downX);
                    downY = Math.abs(x - downY);
                }
                break;
            case MotionEvent.ACTION_UP:
            Log.i("TAG","UP");
                if (isOutRange) {
//                    mScroller.startScroll();
//                    scrollTo((int) mFixedCircle.x,(int)mFixedCircle.y);
                    updateDragCircle(mFixedCircle.x, mFixedCircle.y);
                }
            break;
        }

        return true;
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
        super.computeScroll();
    }

    private void updateDragCircle( float x, float y) {
        mDragCircle.x = x;
        mDragCircle.y = y;
        invalidate();
    }

    private void drawFivePoint(Canvas canvas) {
        //圆心
        canvas.drawCircle(mFixedCircle.x, mFixedCircle.y, 5, pointpPaint);
        canvas.drawCircle(mDragCircle.x, mDragCircle.y, 5, pointpPaint);
        //五个辅助点
        canvas.drawCircle(mFixedPoints.get(0).x, mFixedPoints.get(0).y, 5, pointpPaint);
        canvas.drawCircle(mFixedPoints.get(1).x, mFixedPoints.get(1).y, 5, pointpPaint);
        canvas.drawCircle(mDragPoints.get(0).x, mDragPoints.get(0).y, 5, pointpPaint);
        canvas.drawCircle(mDragPoints.get(1).x, mDragPoints.get(1).y, 5, pointpPaint);
        canvas.drawCircle(mControlPoint.x, mControlPoint.y, 5, pointpPaint);
    }

    private PointF getMiddlePoint(PointF mDragCircle, PointF mFixedCircle) {
        if (mDragCircle.x >= mFixedCircle.x && mDragCircle.y >= mFixedCircle.y) {
            return new PointF(Math.abs(mDragCircle.x - Math.abs(mFixedCircle.x - mDragCircle.x) / 2), Math.abs(mDragCircle.y - Math.abs(mDragCircle.y - mFixedCircle.y) / 2));
        } else if (mDragCircle.x >= mFixedCircle.x && mDragCircle.y <= mFixedCircle.y) {
            return new PointF(Math.abs(mDragCircle.x - Math.abs(mFixedCircle.x - mDragCircle.x) / 2), Math.abs(mFixedCircle.y - Math.abs(mDragCircle.y - mFixedCircle.y) / 2));
        } else if (mDragCircle.x <= mFixedCircle.x && mDragCircle.y >= mFixedCircle.y) {
            return new PointF(Math.abs(mFixedCircle.x - Math.abs(mFixedCircle.x - mDragCircle.x) / 2), Math.abs(mDragCircle.y - Math.abs(mDragCircle.y - mFixedCircle.y) / 2));
        } else {
            // (mDragCircle.x <= mFixedCircle.x && mDragCircle.y <= mFixedCircle.y)
            return new PointF(Math.abs(mFixedCircle.x - Math.abs(mFixedCircle.x - mDragCircle.x) / 2), Math.abs(mFixedCircle.y - Math.abs(mDragCircle.y - mFixedCircle.y) / 2));
        }
    }

    private List<PointF> getIntersectionPoints(PointF mFixedCircle, PointF mDragCircle, float distance, float radius) {
        ArrayList<PointF> list = new ArrayList<>();
        float sin = (mFixedCircle.x - mDragCircle.x) / distance;
        float cos = (mFixedCircle.y - mDragCircle.y) / distance;
//        Log.e("TAG", "sin = " + sin + " , cos = " + cos);
        list.add(new PointF(mFixedCircle.x + radius * cos, (mFixedCircle.y - radius * sin)));
        list.add(new PointF(mFixedCircle.x - radius * cos, (mFixedCircle.y + radius * sin)));
        return list;
    }

    private float getTempFiexdCircle() {
        //获取到两个圆心之间的距离
        float instance = getDistanceBetween2Points(mFixedCircle, mDragCircle);
        //这个是在连个圆之间的实际距离和我们定义的距离之间取得最小值
        instance = Math.min(instance, farestDistance);
        //0.0f--->1.0f>>>>>1.0f---》0.0f
        float percent = instance / farestDistance;
        return evaluate(percent, fixRadius, fixRadius * 0.2);
    }

    /**
     * 估值器
     */
    public Float evaluate(float fraction, Number startValue, Number endValue) {
        float startFloat = startValue.floatValue();
        return startFloat + fraction * (endValue.floatValue() - startFloat);
    }

    private float getDistanceBetween2Points(PointF circle1, PointF circle2) {
        return getDistanceBetween2Points(circle1, circle2.x, circle2.y);
    }

    private float getDistanceBetween2Points(PointF circle1, float x, float y) {
        return (float) Math.sqrt(Math.pow((circle1.x - x), 2) + Math.pow((circle1.y - y), 2));
    }
}
