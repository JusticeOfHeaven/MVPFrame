package com.my.mvpframe.customview.BezierView1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by jzhan on 2018/11/16.
 **/
public class DeleteView extends View {
    private int width;
    private int height;
    private Paint paint;
    private Paint pointPaint;
    private GestureDetector gestureDetector;
    private RectF mUpRect;
    private RectF mDownRect;
    private Paint rectPaint;
    private boolean isScale = false;
    private float fraction = 1f;
    private float distance;

    public DeleteView(Context context) {
        this(context, null);
    }

    public DeleteView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DeleteView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    GestureDetector.SimpleOnGestureListener listener = new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//            Log.e("TAG","e1.x = "+e1.getX()+" , e1.y = "+e1.getY()+" , e2.x = "+e2.getX()+" , e2.y = "+e2.getY());
//            Log.e("TAG","distanceX = "+distanceX+" , distanceY = "+distanceY);
            updataDragCircle(distanceY);
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onDown(MotionEvent e) {

            return true;
        }
    };

    private void init(Context context) {
        gestureDetector = new GestureDetector(context, listener);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);

//        paint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
//        paint1.setColor(Color.RED);

        pointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        pointPaint.setColor(Color.BLUE);

        rectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        rectPaint.setColor(Color.BLUE);
        rectPaint.setStyle(Paint.Style.STROKE);
        rectPaint.setStrokeWidth(2);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.e("TAG", "onSizeChanged  height = " + h);
        width = w;
        height = h;

        distance = height / 2;
        mUpRect = new RectF();
        mUpRect.left = 0;
        mUpRect.top = 0;
        mUpRect.right = width;
        mUpRect.bottom = height;

        mDownRect = new RectF();
        mDownRect.left = 0;
        mDownRect.top = 0;
        mDownRect.right = width;
        mDownRect.bottom = height;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        canvas.translate(0, -distance);
        canvas.scale(fraction, fraction, mUpRect.centerX(), mUpRect.centerY());
        canvas.drawOval(mUpRect, paint);
        canvas.drawRect(mUpRect, rectPaint);
        canvas.restore();

        canvas.save();
        canvas.translate(0, distance);
        canvas.scale(fraction, fraction, mDownRect.centerX(), mDownRect.centerY());
        canvas.drawOval(mDownRect, paint);
        canvas.drawRect(mDownRect, rectPaint);
        canvas.restore();

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return true;
//        return super.onTouchEvent(event);
    }

    private void updataDragCircle(float y) {
        if (distance + y > height / 2) {

        }


        if (mUpRect.top - y < mUpRect.bottom + y) {
            mUpRect.top -= y;
            mUpRect.bottom += y;
        } else {
            mUpRect.top = mUpRect.bottom = distance;
        }
        if (mDownRect.top - y < mDownRect.bottom + y) {
            mDownRect.top -= y;
            mDownRect.bottom += y;
        } else {
            mDownRect.top = mDownRect.bottom = distance;
        }

//        if (mUpRect.bottom < height*2) {

//        }
        if (mUpRect.bottom > height) {
            if (y > 0) {
                fraction += 0.01f;
            } else {
                fraction = fraction - 0.01f;
                if (fraction <= 1f) {
                    fraction = 1f;
                }
            }
        } else {
            fraction = 1f;
        }
//        if (mUpRect.bottom >= height*2) {
//            mUpRect.top -=height*2;
//            mUpRect.bottom =height*2;
//            mDownRect.top -=y;
//            mDownRect.bottom =-height*2;
//            isScale = true;
//        }
        Log.e("TAG", "fraction = " + fraction);
        Log.e("TAG", "mUpRect.top = " + mUpRect.top + ", mUpRect.bottom = " + mUpRect.bottom);
        Log.e("TAG", "mDownRect.top = " + mDownRect.top + ", mDownRect.bottom = " + mDownRect.bottom);
        invalidate();
    }

    /**
     * 估值器
     */
    public Float evaluate(float fraction, Number startValue, Number endValue) {
        float startFloat = startValue.floatValue();
        return startFloat + fraction * (endValue.floatValue() - startFloat);
    }
}
