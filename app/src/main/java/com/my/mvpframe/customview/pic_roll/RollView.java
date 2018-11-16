package com.my.mvpframe.customview.pic_roll;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by jzhan on 2018/11/16.
 **/
public class RollView extends View {
    private static final float IMAGE_SIZE = Utils.dpToPixel(150);
    private static final float PADDING = Utils.dpToPixel(50);
    private static final float FLIP_ANGLE = 45;

    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Bitmap bitmap;
    Camera camera = new Camera();

    private float leftFlip;
    private float rightFlip;
    private float flipRotation;

    public RollView(Context context) {
        this(context,null);
    }

    public RollView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RollView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {

    }
    {
        bitmap = Utils.getAvatar(getResources(), (int) IMAGE_SIZE);
        camera.setLocation(0, 0, Utils.getCameraZ());
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float offset = IMAGE_SIZE / 2 + PADDING;

        // 左半边
        camera.save();
        camera.rotateY(leftFlip * FLIP_ANGLE);

        canvas.save();
        canvas.translate(offset, offset);
        canvas.rotate(-flipRotation);
        camera.applyToCanvas(canvas);
        canvas.clipRect(0, -IMAGE_SIZE, -IMAGE_SIZE, IMAGE_SIZE);
        canvas.rotate(flipRotation);
        canvas.translate(-offset, -offset);
        canvas.drawBitmap(bitmap, PADDING, PADDING, paint);
        canvas.restore();

        camera.restore();

        // 右半边
        camera.save();
        camera.rotateY(- rightFlip * FLIP_ANGLE);

        canvas.save();
        canvas.translate(offset, offset);
        canvas.rotate(-flipRotation);
        camera.applyToCanvas(canvas);
        canvas.clipRect(0, -IMAGE_SIZE, IMAGE_SIZE, IMAGE_SIZE);
        canvas.rotate(flipRotation);
        canvas.translate(-offset, -offset);
        canvas.drawBitmap(bitmap, PADDING, PADDING, paint);
        canvas.restore();

        camera.restore();
    }
    public float getLeftFlip() {
        return leftFlip;
    }

    public void setLeftFlip(float leftFlip) {
        this.leftFlip = leftFlip;
        invalidate();
    }

    public float getRightFlip() {
        return rightFlip;
    }

    public void setRightFlip(float rightFlip) {
        this.rightFlip = rightFlip;
        invalidate();
    }

    public float getFlipRotation() {
        return flipRotation;
    }

    public void setFlipRotation(float flipRotation) {
        this.flipRotation = flipRotation;
        invalidate();
    }
}
