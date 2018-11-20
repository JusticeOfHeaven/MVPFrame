package com.my.mvpframe.customview.delete;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.my.mvpframe.R;

/**
 * Created by jzhan on 2018/11/20.
 * 左滑删除
 **/
public class ScrollDeleteView extends FrameLayout {
    private int touchSlop;
    private int downX;
    private LottieAnimationView lottieView;
    private RelativeLayout rlContent;
    private VelocityTracker mVelocityTracker;
    private int width;
    private int leftDistance = dp2px(30f);
    private boolean isLottieFinish;
    private float X_CONSTANT = 50;
    private int lastDownX;

    public ScrollDeleteView(Context context) {
        this(context, null);
    }

    public ScrollDeleteView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollDeleteView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        Log.i("TAG","init");
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        View view = LayoutInflater.from(context).inflate(R.layout.item_demo, null, false);
        lottieView = view.findViewById(R.id.lottieView);
//        lottieView.animate().translationX(-lottieView.getMeasuredWidth()/2).start();
        rlContent = view.findViewById(R.id.rlContent);
        addView(view);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.i("TAG","onSizeChanged");
        width = w;
        int i = lottieView.getMeasuredWidth() / 2 + width;

    }

    int sumX = 0;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        initVelocityTrackerIfNotExists();

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getRawX();
                if (mVelocityTracker == null) {
                    // Retrieve a new VelocityTracker object to watch the velocity of a motion.
                    mVelocityTracker = VelocityTracker.obtain();
                } else {
                    mVelocityTracker.clear();
                }
                mVelocityTracker.addMovement(event);
                lastDownX = downX;// 布局移动需要的
                break;
            case MotionEvent.ACTION_MOVE:
                mVelocityTracker.addMovement(event);
                float x = event.getRawX();

                int dx = (int) (x - lastDownX);
                this.sumX -= dx;
                if (this.sumX <= 0) {
                    this.sumX = 0;
                }
                if (this.sumX >= leftDistance) {
                    this.sumX = leftDistance;
                }
                float fraction = this.sumX * 0.034f / leftDistance;

                Log.i("TAG","fraction = "+fraction+", sumx = "+ this.sumX);
                lottieView.setProgress(fraction);
                upDateContentLayout(dx);
                lastDownX = (int) x;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mVelocityTracker.computeCurrentVelocity(1000);
                float xVelocity = mVelocityTracker.getXVelocity();
                if (xVelocity >= 0 && Math.abs(xVelocity) >= X_CONSTANT) {
                    // 向右滑
//                    Log.i("TAG", "向右滑");
                    this.sumX = 0;
                    lottieView.setProgress(0f);
                    rlContent.setLeft(0);
                    rlContent.setRight(width);
                } else if (xVelocity <= 0 && Math.abs(xVelocity) >= X_CONSTANT) {
                    // 向左滑
//                    Log.i("TAG", "向左滑");
                    this.sumX = leftDistance;
                    lottieView.setProgress(0.034f);
                    rlContent.setLeft(-leftDistance);
                    rlContent.setRight(width);
                } else {
                    this.sumX = 0;
                    lottieView.setProgress(0f);
                    rlContent.setLeft(0);
                    rlContent.setRight(width);
                }
                recycleVelocityTracker();
                break;
        }
        return true;
    }

    private void upDateContentLayout(int dx) {
        int left = rlContent.getLeft();
        int right = rlContent.getRight();
        int lastLeft = left + dx;
        int lastRight = right + dx;
        // 左滑不超出
        if (lastLeft <= -leftDistance) {
            lastLeft = -leftDistance;
        }
        if (lastRight <= width - leftDistance) {
            lastRight = width - leftDistance;
        }
        // 右滑不超出
        if (lastLeft >= 0) {
            lastLeft = 0;
        }
        if (lastRight >= width) {
            lastRight = width;
        }
        Log.i("TAG", "left = " + lastLeft + " , right = " + lastRight);
        rlContent.setLeft(lastLeft);
        rlContent.setRight(width);
    }

    private void initVelocityTrackerIfNotExists() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
    }

    private void recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    public int dp2px(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                Resources.getSystem().getDisplayMetrics());
    }
}
