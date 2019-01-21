package com.my.mvpframe.customview.s14;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Create by jzhan on 2019/1/19
 * 弯曲滑动选择器
 */
public class SelectBar extends View {
    // 左边的文字
    private String leftText = "8";
    private float leftTextDistance = 100;
    private float leftTextLength;
    // 右边的文字
    private String rightText = "28";
    private float rightTextDistance = 100;
    private float rightTextLength;
    // 滑动的text
    private String rollText1 = "";
    private String rollText2 = "";
    // 选择器个数，默认为1个，最多为2个
    private int circleNum = 2;

    private Paint linePaint;
    private Path linePath;
    // 触摸圆点半径
    private float radius = 50;
    // 圆点
    private Point point1;
    private Point point2;
    // 圆与线之间的间距
    private int offset = 10;
    // 两个圆之间的最小距离
    private float circleDistance;
    // 两个圆之间的间隔数
    private int circleApart = 3;
    private Paint circlePaint;
    private Paint pointPaint;
    private Paint textPaint;
    private int mWidth;
    private int mHeight;
    private float sumx;
    private int dulX;
    private Point testPoint1;
    private Point testPoint2;
    // 针对于两个圆的情况，0 是没选，1是选的左边的圆，2是选择右边的圆
    private int select = 0;

    public SelectBar(Context context) {
        this(context, null);
    }

    public SelectBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SelectBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        //线条paint
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(Color.BLACK);
        linePaint.setStrokeWidth(5);
        linePaint.setStyle(Paint.Style.STROKE);

        // 触摸圆
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setColor(Color.GREEN);

        // 圆点
        point1 = new Point();
        point2 = new Point();
        // 线的路径
        linePath = new Path();

        // textView
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(50);

        leftTextLength = calTextWidth(leftText);
        rightTextLength = calTextWidth(rightText);

        // 测试Point
        testPoint1 = new Point(0, 0);
        testPoint2 = new Point(0, 0);


        pointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        pointPaint.setColor(Color.RED);
        pointPaint.setStrokeWidth(5);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        if (circleNum == 1) {
            point1.x = w / 2;
            point1.y = h / 2;
        } else {
            point1.x = w / 4;
            point1.y = h / 2;
            point2.x = w * 3 / 4;
            point2.y = h / 2;
        }
        // 总距离
        sumx = mWidth - rightTextDistance - rightTextLength - leftTextDistance - leftTextLength - radius * 2;
        // 总分段
        dulX = Integer.parseInt(rightText) - Integer.parseInt(leftText);
        circleDistance = circleApart * sumx / dulX;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 圆上面的线
        drawLine(canvas);
        // 触摸圆
        drawCircle(canvas);
        // Draw TextView
        drawTextView(canvas);
        // draw test
        drawTest(canvas);
    }

    private void drawTest(Canvas canvas) {
        canvas.drawPoint(testPoint1.x, testPoint1.y, pointPaint);
        canvas.drawPoint(testPoint2.x, testPoint2.y, pointPaint);
        canvas.drawPoint(point1.x - radius - offset, point1.y - radius, pointPaint);
        canvas.drawPoint(point1.x, point1.y - radius - radius, pointPaint);
        canvas.drawPoint(point1.x + radius + offset, point1.y - radius, pointPaint);
    }

    private void drawLine(Canvas canvas) {
        canvas.save();
        linePath.reset();
        linePath.moveTo(0, point1.y - radius);
        linePath.lineTo(point1.x - radius - offset, point1.y - radius);
        linePath.quadTo(point1.x, point1.y - radius - radius, point1.x + radius + offset, point1.y - radius);
        if (circleNum == 1) {
            linePath.lineTo(mWidth, point1.y - radius);
        } else {
            linePath.lineTo(point2.x - radius - offset, point2.y - radius);
            linePath.quadTo(point2.x, point2.y - radius - radius, point2.x + radius + offset, point2.y - radius);
            linePath.lineTo(mWidth, point2.y - radius);
        }
        canvas.drawPath(linePath, linePaint);
        canvas.restore();
    }

    private void drawCircle(Canvas canvas) {
        canvas.drawCircle(point1.x, point1.y, radius, circlePaint);
        if (circleNum != 1) {
            canvas.drawCircle(point2.x, point2.y, radius, circlePaint);
        }
    }

    private void drawTextView(Canvas canvas) {
        canvas.drawText(leftText, leftTextDistance, point1.y + radius / 2, textPaint);
        canvas.drawText(rightText, mWidth - rightTextDistance - rightTextLength, point1.y + radius / 2, textPaint);
        rollText1 = calShowText(point1);
        canvas.drawText(rollText1, point1.x - calTextWidth(rollText1) / 2, point1.y - radius * 2, textPaint);
        if (circleNum != 1) {
            rollText2 = calShowText(point2);
            canvas.drawText(rollText2, point2.x - calTextWidth(rollText2) / 2, point2.y - radius * 2, textPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                // 手指落下在圆内
                if (!calculatePointDownInCircle(event)) {
                    return super.onTouchEvent(event);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float x = event.getX();
                // 左边最小距离
                float minLeft = leftTextDistance + calTextWidth(leftText) + radius;
                // 右边最大距离
                float maxRight = mWidth - rightTextDistance - rightTextLength - radius;

                testPoint1.x = (int) maxRight;
                testPoint1.y = mHeight / 2;
                testPoint2.x = (int) (maxRight - circleDistance);
                testPoint2.y = mHeight / 2;

                // 一个圆
                if (circleNum == 1) {
                    if (x <= minLeft) {
                        point1.x = (int) minLeft;
                    } else if (x >= maxRight) {
                        point1.x = (int) maxRight;
                    } else {
                        point1.x = (int) x;
                    }
                } else {
                    // 两个圆，
                    if (downInPoint1(event) || select == 1) {
                        select = 1;
                        if (x <= minLeft) {
                            point1.x = (int) minLeft;
                        } else if (x >= maxRight - circleDistance) {
                            point1.x = (int) (maxRight - circleDistance);
                        } else {
                            point1.x = (int) x;
                            if (point2.x - point1.x <= circleDistance) {
                                point2.x = (int) (x + circleDistance);
                            }
                        }
                    } else if (downInPoint2(event) || select == 2){
                        select = 2;
                        if (x <= minLeft + circleDistance) {
                            point2.x = (int) (minLeft + circleDistance);
                        } else if (x >= maxRight) {
                            point2.x = (int) maxRight;
                        } else {
                            point2.x = (int) x;
                            if (point2.x - point1.x <= circleDistance) {
                                point1.x = (int) (x - circleDistance);
                            }
                        }
                    }
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                select = 0;
                break;
        }
        return true;
    }

    /*计算按下的点在圆内*/
    private boolean calculatePointDownInCircle(MotionEvent event) {
        return downInPoint1(event) || downInPoint2(event);
    }

    private boolean downInPoint1(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        return x >= point1.x - radius && x <= point1.x + radius && y >= point1.y - radius && y <= point1.y + radius;
    }

    private boolean downInPoint2(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        return x >= point2.x - radius && x <= point2.x + radius && y >= point2.y - radius && y <= point2.y + radius;
    }

    /*根据比例计算所显示的文字*/
    private String calShowText(Point point) {
        if (sumx > 0) {
            try {
                float v = (point.x - leftTextDistance - radius) / sumx;
                int num = (int) ((dulX * v) + 0) + Integer.parseInt(leftText);
                return String.valueOf(num);
            } catch (Exception e) {
            }
        }
        return "";
    }

    /*根据设置的获取位置*/
    private void calDistance() {

    }

    /*测量文字长度*/
    private float calTextWidth(String text) {
        return textPaint.measureText(text, 0, text.length());
    }
}
