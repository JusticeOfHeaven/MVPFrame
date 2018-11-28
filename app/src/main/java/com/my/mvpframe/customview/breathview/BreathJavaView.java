package com.my.mvpframe.customview.breathview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.my.mvpframe.R;

/**
 * Create by jzhan on 2018/11/27
 */
public class BreathJavaView extends View {
    private Bitmap bitmap;
    private Paint paint;
    private Paint bigCirclePaint;
    private int width;
    private int height;
    private int bigCircleRadius;

    public BreathJavaView(Context context) {
        this(context, null);
    }

    public BreathJavaView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BreathJavaView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BreathView);
        Drawable drawable = typedArray.getDrawable(R.styleable.BreathView_breathDrawable);
        bigCircleRadius = typedArray.getDimensionPixelSize(R.styleable.BreathView_bigCircleRadius,20);
        typedArray.recycle();

        setWillNotDraw(false);

        bitmap = drawableToBitmap(drawable);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // 最外层大圆
        bigCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bigCirclePaint.setColor(Color.RED);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        bigCircleRadius = width/2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(bitmap, 0f, 0f, paint);

        canvas.drawCircle(width / 2, height / 2, bigCircleRadius, bigCirclePaint);

    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        // 取 drawable 的长宽
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();

        // 取 drawable 的颜色格式
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        // 建立对应 bitmap
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        // 建立对应 bitmap 的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        // 把 drawable 内容画到画布中
        drawable.draw(canvas);
        return bitmap;
    }
}
