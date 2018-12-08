package com.my.mvpframe.customview.jbox2d;

import android.graphics.Canvas;
import android.graphics.Paint;

import org.jbox2d.dynamics.Body;

/**
 * Create by jzhan on 2018/12/3
 */
public abstract class MyBody {
    Body body;//JBox2D物理引擎中的刚体
    int color;//刚体的颜色

    public abstract void drawSelf(Canvas canvas, Paint paint);//绘制的方法
}
