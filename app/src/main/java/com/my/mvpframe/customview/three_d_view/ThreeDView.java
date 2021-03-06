package com.my.mvpframe.customview.three_d_view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.my.mvpframe.R;

/**
 * Created by jzhan on 2018/11/7.
 **/
public class ThreeDView extends View {
    private static final int BIT_MAP_WIDTH = 200;
    private static final int BIT_MAP_HEIGHT = 200;
    private static final float CENTER_CIRCLE_SHADOW_R = 200f;
    private static final float CENTER_CIRCLE_R = 60f;

    private int THREE_D_VIEW_WIDTH;
    private int THREE_D_VIEW_HEIGHT;
    private int distanceZ;
    private float distanceToDeg;

    private float distanceX = 0f; // y axis rotation. y轴旋转
    private float distanceY = 0f; // x axis rotation. x轴旋转
    private float rotateDeg = 0f; // z axis rotation. z轴旋转

    private Camera camera = new Camera();
    private Matrix matrixFront = new Matrix();
    private Matrix matrixBack = new Matrix();
    private Matrix matrixLeft = new Matrix();
    private Matrix matrixRight = new Matrix();
    private Matrix matrixTop = new Matrix();
    private Matrix matrixBottom = new Matrix();

    private Paint paint = new Paint();
    private Bitmap bitmap;
    private float xVelocity = 0f;
    private float yVelocity = 0f;
    private Handler animHandler;
    private Handler touchHandler;
    private boolean isInfinity = false;
    private float distanceVelocityDecrease = 1f;// decrease 1 pixels/second when a message is handled in the loop.
    // loop frequency is 60hz or 120hz when handleMessage(msg) includes UI update code.


    public ThreeDView(Context context) {
        this(context, null);
    }

    public ThreeDView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ThreeDView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        // 用 R.mipmap 就报错
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
        bitmap = Bitmap.createScaledBitmap(bitmap, BIT_MAP_WIDTH, BIT_MAP_HEIGHT, false);

        animHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                distanceX += (xVelocity * 0.016);
                distanceY += (yVelocity * 0.016);

                ThreeDView.this.invalidate();

                if (ThreeDView.this.stateValueListener != null) {
                    ThreeDView.this.stateValueListener.stateValue(distanceX, -distanceY, rotateDeg, distanceZ);
                }

                if (xVelocity == 0f && yVelocity == 0f) { // anim will stop
                    return true;
                }

                if (ThreeDView.this.isInfinity) {
                    ThreeDView.this.sendMsgForAnim();

                } else {
                    // decrease the velocities.
                    // 'Math.abs(xVelocity) <= distanceVelocityDecrease' make sure the xVelocity will be 0 finally.
                    xVelocity =  Math.abs(xVelocity) <= distanceVelocityDecrease ? 0f :
                            (xVelocity > 0 ? xVelocity - distanceVelocityDecrease : xVelocity + distanceVelocityDecrease);

                    yVelocity = Math.abs(yVelocity) <= distanceVelocityDecrease ? 0f :
                            (yVelocity > 0 ? yVelocity - distanceVelocityDecrease : yVelocity + distanceVelocityDecrease);

                    ThreeDView.this.sendMsgForAnim();
                }

                return true;
            }
        });

        touchHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (ThreeDView.this.stateValueListener != null) {
                    ThreeDView.this.stateValueListener.stateValue(distanceX, -distanceY, rotateDeg, distanceZ);
                }
                return true;
            }
        });
    }

    private void sendMsgForAnim() {
        animHandler.sendEmptyMessage(0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // convert distances in pixels into degrees 转换为度
        float xDeg = -distanceY * distanceToDeg;
        float yDeg = distanceX * distanceToDeg;

        // 前后左右上下
        setMatrixFront(xDeg, yDeg);
        setMatrixBack(xDeg, yDeg);
        setMatrixLeft(xDeg, yDeg);
        setMatrixRight(xDeg, yDeg);
        setMatrixTop(xDeg, yDeg);
        setMatrixBottom(xDeg, yDeg);

        canvas.translate((THREE_D_VIEW_WIDTH - BIT_MAP_WIDTH) / 2f,
                (THREE_D_VIEW_HEIGHT - BIT_MAP_HEIGHT) / 2f);

        drawCanvas(canvas,xDeg,yDeg);
    }

    private void drawCanvas(Canvas canvas, float xDeg, float yDeg) {
        if (Math.cos(Math.toRadians(xDeg)) <= 0 || Math.cos(Math.toRadians(yDeg))<=0) {
            canvas.drawBitmap(bitmap, matrixFront, paint);
            if (Math.cos(Math.toRadians(xDeg)) <= 0 || Math.cos(Math.toRadians(yDeg - 90f)) <= 0) {
                canvas.drawBitmap(bitmap, matrixLeft, paint);
                if (Math.cos(Math.toRadians(xDeg - 90f)) <= 0 || Math.cos(Math.toRadians(yDeg)) <= 0) {
                    canvas.drawBitmap(bitmap, matrixTop, paint);
                    drawCenter(canvas);
                    canvas.drawBitmap(bitmap, matrixBottom, paint);
                } else {
                    canvas.drawBitmap(bitmap, matrixBottom, paint);
                    drawCenter(canvas);
                    canvas.drawBitmap(bitmap, matrixTop, paint);
                }
                canvas.drawBitmap(bitmap, matrixRight, paint);
            } else {
                canvas.drawBitmap(bitmap, matrixRight, paint);
                if (Math.cos(Math.toRadians(xDeg - 90f)) <= 0 || Math.cos(Math.toRadians(yDeg)) <= 0) {
                    canvas.drawBitmap(bitmap, matrixTop, paint);
                    drawCenter(canvas);
                    canvas.drawBitmap(bitmap, matrixBottom, paint);
                } else {
                    canvas.drawBitmap(bitmap, matrixBottom, paint);
                    drawCenter(canvas);
                    canvas.drawBitmap(bitmap, matrixTop, paint);
                }
                canvas.drawBitmap(bitmap, matrixLeft, paint);
            }
            canvas.drawBitmap(bitmap, matrixBack, paint);
        }  else {
            canvas.drawBitmap(bitmap, matrixBack, paint);
            if (Math.cos(Math.toRadians(xDeg)) <= 0 || Math.cos(Math.toRadians(yDeg - 90f)) <= 0) {
                canvas.drawBitmap(bitmap, matrixLeft, paint);
                if (Math.cos(Math.toRadians(xDeg - 90f)) <= 0 || Math.cos(Math.toRadians(yDeg)) <= 0) {
                    canvas.drawBitmap(bitmap, matrixTop, paint);
                    drawCenter(canvas);
                    canvas.drawBitmap(bitmap, matrixBottom, paint);
                } else {
                    canvas.drawBitmap(bitmap, matrixBottom, paint);
                    drawCenter(canvas);
                    canvas.drawBitmap(bitmap, matrixTop, paint);
                }
                canvas.drawBitmap(bitmap, matrixRight, paint);
            } else {
                canvas.drawBitmap(bitmap, matrixRight, paint);
                if (Math.cos(Math.toRadians(xDeg - 90f)) <= 0 || Math.cos(Math.toRadians(yDeg)) <= 0) {
                    canvas.drawBitmap(bitmap, matrixTop, paint);
                    drawCenter(canvas);
                    canvas.drawBitmap(bitmap, matrixBottom, paint);
                } else {
                    canvas.drawBitmap(bitmap, matrixBottom, paint);
                    drawCenter(canvas);
                    canvas.drawBitmap(bitmap, matrixTop, paint);
                }
                canvas.drawBitmap(bitmap, matrixLeft, paint);
            }
            canvas.drawBitmap(bitmap, matrixFront, paint);
        }
    }

    private void drawCenter(Canvas canvas) {
        // draw center circle shadow first
        paint.setColor(Color.parseColor("#550000ff"));
        canvas.drawCircle(BIT_MAP_WIDTH / 2, BIT_MAP_HEIGHT / 2, CENTER_CIRCLE_SHADOW_R, paint);

        // draw center circle second, it's above the shadow
        paint.setColor(Color.parseColor("#0000ff"));
        canvas.drawCircle(BIT_MAP_WIDTH / 2, BIT_MAP_HEIGHT / 2, CENTER_CIRCLE_R, paint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        THREE_D_VIEW_WIDTH = w;// 这是像素，不是dp
        THREE_D_VIEW_HEIGHT = h;
        // todo 为啥是宽高的最小值 除以2
        distanceZ = Math.min(w, h) / 2;
        // todo 一个能将px转换为角度的值，原理不清楚
        distanceToDeg = 90f / distanceZ;//NOT changed when distanceZ changed in the future
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (animHandler != null) {
            animHandler.removeCallbacksAndMessages(null);
        }
    }

    private void setMatrixBottom(float xDeg, float yDeg) {
        matrixBottom.reset();

        camera.save();
        camera.rotateX(xDeg - 90f);
        camera.rotateY(yDeg);
        camera.rotateZ(rotateDeg);
        camera.translate(0f, 0f, distanceZ);
        camera.getMatrix(matrixBottom);
        camera.restore();

        matrixBottom.preTranslate(-(BIT_MAP_WIDTH / 2), -(BIT_MAP_HEIGHT / 2));
        matrixBottom.postTranslate(BIT_MAP_WIDTH / 2, BIT_MAP_HEIGHT / 2);
    }

    private void setMatrixTop(float xDeg, float yDeg) {
        matrixTop.reset();

        camera.save();
        camera.rotateX(xDeg - 90f);
        camera.rotateY(yDeg);
        camera.rotateZ(-rotateDeg);
        camera.translate(0f, 0f, -distanceZ);
        camera.getMatrix(matrixTop);
        camera.restore();

        matrixTop.preTranslate(-(BIT_MAP_WIDTH / 2), -(BIT_MAP_HEIGHT / 2));
        matrixTop.postTranslate(BIT_MAP_WIDTH / 2, BIT_MAP_HEIGHT / 2);
    }

    private void setMatrixRight(float xDeg, float yDeg) {
        matrixRight.reset();

        camera.save();
        camera.rotateX(xDeg);
        camera.rotateY(yDeg - 90f);
        camera.rotateZ(rotateDeg);
        camera.translate(0f, 0f, distanceZ);
        camera.getMatrix(matrixRight);
        camera.restore();

        matrixRight.preTranslate(-(BIT_MAP_WIDTH / 2), -(BIT_MAP_HEIGHT / 2));
        matrixRight.postTranslate(BIT_MAP_WIDTH / 2, BIT_MAP_HEIGHT / 2);
    }

    private void setMatrixLeft(float xDeg, float yDeg) {
        matrixLeft.reset();

        camera.save();
        camera.rotateX(xDeg);
        camera.rotateY(yDeg - 90f);
        camera.rotateZ(-rotateDeg);
        camera.translate(0f, 0f, -distanceZ);
        camera.getMatrix(matrixLeft);
        camera.restore();

        matrixLeft.preTranslate(-(BIT_MAP_WIDTH / 2), -(BIT_MAP_HEIGHT / 2));
        matrixLeft.postTranslate(BIT_MAP_WIDTH / 2, BIT_MAP_HEIGHT / 2);
    }

    private void setMatrixBack(float xDeg, float yDeg) {
        matrixBack.reset();

        camera.save();
        camera.rotateX(xDeg);
        camera.rotateY(yDeg);
        camera.rotateZ(rotateDeg);
        camera.translate(0, 0, distanceZ);
        camera.getMatrix(matrixBack);
        camera.restore();

        matrixBack.preTranslate(-(BIT_MAP_WIDTH / 2), -(BIT_MAP_HEIGHT / 2));
        matrixBack.postTranslate(BIT_MAP_WIDTH / 2, BIT_MAP_HEIGHT / 2);
    }

    private void setMatrixFront(float xDeg, float yDeg) {
        matrixFront.reset();// 重置

        camera.save();
        camera.rotateX(xDeg);
        camera.rotateY(yDeg);
        camera.rotateZ(-rotateDeg);
        camera.translate(0, 0, -distanceZ);
        camera.getMatrix(matrixFront);
        camera.restore();

        // 将camera的坐标圆点转移至图片的中心
        matrixFront.preTranslate(-(BIT_MAP_WIDTH / 2), -(BIT_MAP_HEIGHT / 2));
        matrixFront.postTranslate(BIT_MAP_WIDTH / 2, BIT_MAP_HEIGHT / 2);
    }

    public void setDistanceVelocityDecrease(float distanceVelocityDecrease) {
        if (distanceVelocityDecrease <= 0f) {
            this.isInfinity = true;
            this.distanceVelocityDecrease = 0f;
        } else {
            this.isInfinity = false;
            this.distanceVelocityDecrease = distanceVelocityDecrease;
        }
    }

    public void stopAnim() {
        animHandler.removeCallbacksAndMessages(null);
    }

    public void startAnim(float xVelocity, float yVelocity) {
        this.xVelocity = xVelocity;
        this.yVelocity = yVelocity;

        sendMsgForAnim();
    }

    public void updateXY(float movedX, float movedY) {
        this.distanceX += movedX;
        this.distanceY += movedY;
        invalidate();
        touchHandler.sendEmptyMessage(0);
    }

    public void updateRotateDeg(float deltaRotateDeg) {
        this.rotateDeg += deltaRotateDeg;
        invalidate();
        touchHandler.sendEmptyMessage(0);
    }

    public void updateDistanceZ(float distanceZ) {
        this.distanceZ += distanceZ;
        invalidate();
        touchHandler.sendEmptyMessage(0);
    }

    public interface StateValueListener {
        void stateValue(float distanceX, float distanceY, float rotateDegree, float distanceZ);
    }

    private StateValueListener stateValueListener;

    public void setStateValueListener(StateValueListener stateValueListener) {
        this.stateValueListener = stateValueListener;
    }
}
