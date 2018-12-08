package com.my.mvpframe.customview.qq_message;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.os.Build.VERSION;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

/**
 * NOTE1:
 * <p/>
 * The origin circle drawn by  canvas.drawCircle(mBaseX, mBaseY, mStrokeWidth / 2, mPaint);
 * <p/>
 * so mBaseX, mBaseY are center position.
 * <p/>
 * The Moved circle drawn by  canvas.drawBitmap(mDest, mTargetX, mTargetY, mPaint);
 * <p/>
 * so mTargetX,mTargetY are left top corner position.
 *
 *
 * NOTE2:
 *
 * the mBaseX, mBaseY comes from view.getLocationOnScreen()
 *
 * It contains StatusBarHeight
 *
 * The targetX, targetY comes from event.getRawX() / getRawY() , it doesn't contain StatusBarHeight
 *
 *
 */
public class DropCover extends SurfaceView implements SurfaceHolder.Callback {

    private int mMaxDistance = 100;


    private float mBaseX;
    private float mBaseY;

    private float mTargetX;
    private float mTargetY;

    private Bitmap mDest;
    private Paint mPaint = new Paint();

    private float targetWidth;
    private float targetHeight;
    private float mRadius = 0;
    private float mStrokeWidth = 20;
    private boolean isDraw = true;
    private float mStatusBarHeight = 0;

    @SuppressLint("NewApi")
    public DropCover(Context context) {
        super(context);
        this.setBackgroundColor(Color.TRANSPARENT);
        this.setZOrderOnTop(true);
        getHolder().setFormat(PixelFormat.TRANSPARENT);
        getHolder().addCallback(this);
        setFocusable(false);
        setClickable(false);
        setFocusableInTouchMode(false);
        mPaint.setAntiAlias(true);


        if (VERSION.SDK_INT > 11) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    /**
     * draw drop and line
     */
    private void drawDrop() {
        Canvas canvas = getHolder().lockCanvas();
        if (canvas != null) {
            canvas.drawColor(Color.TRANSPARENT, Mode.CLEAR);

            if (isDraw) {
                double distance = Math.sqrt(Math.pow(mBaseX - mTargetX, 2) + Math.pow(mBaseY - mTargetY, 2));
                mPaint.setColor(0xffff0000);
                mPaint.setStyle(Paint.Style.FILL);
                if (distance < mMaxDistance) {
                    mStrokeWidth = (float) ((1f - distance / mMaxDistance) * mRadius);
//                    mPaint.setStrokeWidth(mStrokeWidth);
                    canvas.drawCircle(mBaseX, mBaseY, mStrokeWidth / 2, mPaint);
                    drawBezier(canvas);
                }
                canvas.drawBitmap(mDest, mTargetX, mTargetY, mPaint);
            }
            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    private void drawBezier(Canvas canvas) {
//        mPaint.setStyle(Paint.Style.FILL);

        Point[] points = calculate(new Point(mBaseX, mBaseY), new Point(mTargetX + mDest.getWidth() / 2f, mTargetY + mDest.getHeight() / 2f));

        float centerX = (points[0].x + points[1].x + points[2].x + points[3].x) / 4f;
        float centerY = (points[0].y + points[1].y + points[2].y + points[3].y) / 4f;

        Path path1 = new Path();
        path1.moveTo(points[0].x, points[0].y);
        path1.quadTo(centerX, centerY, points[1].x, points[1].y);
        path1.lineTo(points[3].x, points[3].y);

        path1.quadTo(centerX, centerY, points[2].x, points[2].y);
        path1.lineTo(points[0].x, points[0].y);
        canvas.drawPath(path1, mPaint);
    }

    /**
     * ax=by=0 x^2+y^2=s/2
     * <p/>
     * ==>
     * <p/>
     * x=a^2/(a^2+b^2)*s/2
     *
     * @param start
     * @param end
     * @return
     */
    private Point[] calculate(Point start, Point end) {
        float a = end.x - start.x;
        float b = end.y - start.y;

        float y1 = (float) Math.sqrt(a * a / (a * a + b * b) * (mStrokeWidth / 2f) * (mStrokeWidth / 2f));
        float x1 = -b / a * y1;

        float y2 = (float) Math.sqrt(a * a / (a * a + b * b) * (targetWidth / 2f) * (targetHeight / 2f));
        float x2 = -b / a * y2;


        Point[] result = new Point[4];

        result[0] = new Point(start.x + x1, start.y + y1);
        result[1] = new Point(end.x + x2, end.y + y2);

        result[2] = new Point(start.x - x1, start.y - y1);
        result[3] = new Point(end.x - x2, end.y - y2);

        return result;
    }

    public void setTarget(Bitmap dest) {
        mDest = dest;
        targetWidth = dest.getWidth();
        targetHeight = dest.getHeight();

        mRadius = dest.getWidth() / 2;
        mStrokeWidth = mRadius;
    }

    public void init(float baseX, float baseY, float x, float y) {
        mBaseX = baseX + mDest.getWidth() / 2f;
//        mBaseY = baseY - mDest.getWidth() / 2f + mStatusBarHeight;
        mBaseY = baseY + mDest.getHeight() / 2f - mStatusBarHeight;

        mTargetX = x - mDest.getWidth() / 2f;
        mTargetY = y - mDest.getWidth() / 2f - mStatusBarHeight;

        isDraw = true;
        drawDrop();
    }

    /**
     * move the drop
     *
     * @param x
     * @param y
     */
    public void update(float x, float y) {
        mTargetX = x - mDest.getWidth() / 2f;
        mTargetY = y - mDest.getWidth() / 2f - mStatusBarHeight;
        drawDrop();
    }

    /**
     * reset datas
     */
    public void clearDatas() {
        mBaseX = -1;
        mBaseY = -1;
//        mTargetX = -1;
//        mTargetY = -1;
        mDest = null;
    }

    /**
     * remove DropCover
     */
    public void clearViews() {
        if (getParent() != null) {
            CoverManager.getInstance().getWindowManager().removeView(this);
        }
    }

    /**
     * finish drag event and start explosion
     *
     * @param target
     * @param x
     * @param y
     */
    public double stopDrag(View target, float x, float y , int resourceId) {

        double distance = Math.sqrt(Math.pow(mBaseX - mTargetX, 2) + Math.pow(mBaseY - mTargetY, 2));

        clearDatas();
        Canvas canvas = getHolder().lockCanvas();
        if (canvas != null) {
            canvas.drawColor(Color.TRANSPARENT, Mode.CLEAR);
            getHolder().unlockCanvasAndPost(canvas);
        }
        isDraw = false;
        return distance;
    }

    /**
     * Deprecated
     *
     * @param statusBarHeight
     */
    public void setStatusBarHeight(int statusBarHeight) {
        mStatusBarHeight = statusBarHeight;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        drawDrop();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        CoverManager.getInstance().stopEffect();
    }



    // private void displayFps(Canvas canvas, String fps) {
    // if (canvas != null && fps != null) {
    // Paint paint = new Paint();
    // paint.setARGB(255, 255, 255, 255);
    // canvas.drawText(fps, this.getWidth() - 50, 20, paint);
    // }
    // }

    /**
     * please call it before animation start
     *
     * @param maxDistance
     */
    public void setMaxDragDistance(int maxDistance) {
        mMaxDistance = maxDistance;
    }

    class Point {
        float x, y;

        public Point(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }


    public float getTargetX(){
        return mTargetX;
    }

    public float getTargetY(){
        return  mTargetY;
    }
}
