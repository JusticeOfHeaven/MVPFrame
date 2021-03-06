package com.my.mvpframe.customview.three_d_view;

import android.view.MotionEvent;


/**
 * Created by jzhan on 2018/11/7.
 * control the ThreeDView
 */

public class ThreeDViewController {

    private ThreeDView threeDView;

    private TwoFingersGestureDetector twoFingersGestureDetector;

    public ThreeDViewController(ThreeDView threeDView) {
        this.threeDView = threeDView;
        this.threeDView.setDistanceVelocityDecrease(1.5f);

        twoFingersGestureDetector = new TwoFingersGestureDetector();
        twoFingersGestureDetector.setTwoFingersGestureListener(
                new TwoFingersGestureDetector.TwoFingersGestureListener() {
            @Override
            public void onDown(float downX, float downY, long downTime) {
                ThreeDViewController.this.threeDView.stopAnim();
            }

            @Override
            public void onMoved(float deltaMovedX, float deltaMovedY, long deltaMilliseconds) {
                ThreeDViewController.this.threeDView.updateXY(deltaMovedX, deltaMovedY);
            }

            @Override
            public void onRotated(float deltaRotatedDeg, long deltaMilliseconds) {
                ThreeDViewController.this.threeDView.updateRotateDeg(deltaRotatedDeg);
            }

            @Override
            public void onScaled(float deltaScaledX, float deltaScaledY,
                                 float deltaScaledDistance, long deltaMilliseconds) {
                ThreeDViewController.this.threeDView.updateDistanceZ(deltaScaledDistance);
            }

            @Override
            public void onUp(float upX, float upY, long upTime, float xVelocity, float yVelocity) {
                ThreeDViewController.this.threeDView.startAnim(xVelocity, yVelocity);
            }

            @Override
            public void onCancel() {}
        });
    }

    public void inputTouchEvent(MotionEvent event) {
        twoFingersGestureDetector.onTouchEvent(event);
    }

}
