package com.my.mvpframe.customview.jbox2d;

import android.graphics.Canvas;
import android.graphics.Paint;

import org.jbox2d.dynamics.Body;

import static com.my.mvpframe.customview.jbox2d.Constant.RATE;

/**
 * Create by jzhan on 2018/12/3
 */
public class MyCircleColor extends MyBody {

    private final Body body;
    private final float radius;
    private final int color;

    public MyCircleColor(Body body, float radius, int color) {

        this.body = body;
        this.radius = radius;
        this.color = color;
    }

    @Override
    public void drawSelf(Canvas canvas, Paint paint) {
        paint.setColor(color&0x8CFFFFFF);
        float x = body.getPosition().x * RATE;
        float y = body.getPosition().y * RATE;
        canvas.drawCircle(x,y,radius,paint);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        paint.setColor(color);
        canvas.drawCircle(x,y,radius,paint);
        paint.reset();
    }
}
