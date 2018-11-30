package com.my.mvpframe.customview.collsion;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by jzhan on 2018/11/28
 */
public class MyCollisionView extends View {

    private Ball ball;
    private Ball ball1;
    private List<Ball> list = new ArrayList<>();

    public MyCollisionView(Context context) {
        this(context, null);
    }

    public MyCollisionView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyCollisionView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        ball = new Ball().create();
        ball1 = new Ball().create();

        ball.x = 200;
        ball.y = 200;

        ball1.x = 200;
        ball1.y = 600;
        list.add(ball);
        list.add(ball1);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        long startTime = System.currentTimeMillis();

        int size = list.size();
        for (int i = 0; i < size; i++) {
            canvas.drawCircle(list.get(i).x, list.get(i).y, list.get(i).radius, list.get(i).paint);
            collisionEdge(list.get(i));
            calculateCollision(list.get(i));
            list.get(i).move();
        }


        long stopTime = System.currentTimeMillis();
        long runTime = stopTime - startTime;
        // 20毫秒执行一次
        postInvalidateDelayed(Math.abs(runTime - 20));
    }

    private void calculateCollision(Ball currentBall) {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            Ball ball = list.get(i);
            // 计算圆心距离
            double sqrt = Math.sqrt(Math.pow(Math.abs(currentBall.x - ball.x), 2) + Math.pow(Math.abs(currentBall.y - ball.y), 2));
            if (sqrt <= currentBall.radius + ball.radius) {
                // 计算碰撞之后的速度
                if (ball.vx > 0 && currentBall.vx > 0) {
                    ball.vy = -ball.vy;
                    currentBall.vy = -currentBall.vy;
                } else if (ball.vy > 0 && currentBall.vy > 0) {
                    ball.vx = -ball.vx;
                    currentBall.vx = -currentBall.vx;
                }
                ball.vx = -ball.vx;
                ball.vy = -ball.vy;
                currentBall.vx = -currentBall.vx;
                currentBall.vy = -currentBall.vy;
            }
        }
    }

    private void collisionEdge(Ball ball) {
        // 判断是否到边界
        int left = getLeft();
        int top = getTop();
        int right = getRight();
        int bottom = getBottom();

        float speedX = ball.vx;
        float speedY = ball.vy;

        if (ball.left() <= left && speedX < 0) {
            ball.vx = -ball.vx;
        } else if (ball.top() <= top && speedY < 0) {
            ball.vy = -ball.vy;
        } else if (ball.right() >= right && speedX > 0) {
            ball.vx = -ball.vx;
        } else if (ball.bottom() >= bottom && speedY > 0) {
            ball.vy = -ball.vy;
        }
    }

    private class Ball {
        private Paint paint;
        private float radius;
        private float x;
        private float y;
        private float vx;
        private float vy;

        public Ball create() {
            paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(2);
            paint.setColor(Color.RED);

            radius = 100;
            x = (float) (Math.random() * 100 + 300);
            y = (float) (Math.random() * 100 + 300);
            vx = 10;
            vy = 10;
            return this;
        }

        public void move() {
            x += vx;
            y += vy;
        }

        public int left() {
            return (int) (x - radius);
        }

        public int top() {
            return (int) (y - radius);
        }

        public int right() {
            return (int) (x + radius);
        }

        public int bottom() {
            return (int) (y + radius);
        }

    }
}
