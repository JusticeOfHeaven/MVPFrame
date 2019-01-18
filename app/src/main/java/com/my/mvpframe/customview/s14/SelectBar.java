package com.my.mvpframe.customview.s14;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Create by jzhan on 2019/1/19
 * 弯曲滑动选择器
 */
public class SelectBar extends View {
    // 左边的文字
    private String leftText;
    // 右边的文字
    private String rightText;
    private Paint linePaint;
    private Path linePath;

    public SelectBar(Context context) {
        this(context,null);
    }

    public SelectBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SelectBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        //线条paint
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(Color.BLACK);
        linePaint.setStrokeWidth(2);

        // 线的路径
        linePath = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        linePath.reset();
        linePath.moveTo(0,0);


        canvas.drawLine(0,0,getMeasuredWidth(),0,linePaint);
        canvas.drawPath(linePath, linePaint);
        canvas.restore();
    }
}
