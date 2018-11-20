package com.my.mvpframe.customview.collsion;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by jzhan on 2018/11/20.
 * 球体碰撞
 **/
public class BallCollisionView extends View {

    private int mCount = 10;   // 小球个数
    private int minSpeed = 1; // 小球最小移动速度
    private int maxSpeed = 5; // 小球最大移动速度
    private float stokeWidth = dp2px(5);
    private int stoke = (int) dp2px(5);
    private Random mRandom;
    private int mWidth = 200;
    private int mHeight = 200;
    private int maxRadius;
    private int minRadius;

    public Ball[] mBalls;   // 用来保存所有小球的数组
    public ArrayList<Ball> bList;//保存设置圆心的小球集合
    private ArrayList<TaskModel> datas = new ArrayList<>();

    private int position;
    private TextPaint tPaint;

    public BallCollisionView(Context context) {
        this(context, null);
    }

    public BallCollisionView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BallCollisionView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        bList = new ArrayList<>();

        tPaint = new TextPaint();
        tPaint.setAntiAlias(true);
        tPaint.setTextAlign(Paint.Align.CENTER);
        tPaint.setColor(Color.WHITE);
        tPaint.setStyle(Paint.Style.FILL);
        tPaint.setTextSize(dp2px(32));

        // 初始化所有球(设置颜色和画笔, 初始化移动的角度)
        mRandom = new Random();
        mBalls = new Ball[mCount];
        for (int i = 0; i < mCount; i++) {
            mBalls[i] = new Ball();
            // 设置画笔
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(Color.parseColor("#" + getRandColor()));
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(stokeWidth);
            paint.setAlpha(180);

            // 设置速度
            float speedX = (mRandom.nextInt(maxSpeed - minSpeed + 1) + 5) / 10f;
            float speedY = (mRandom.nextInt(maxSpeed - minSpeed + 1) + 5) / 10f;
            mBalls[i].paint = paint;
            mBalls[i].vx = mRandom.nextBoolean() ? speedX : -speedX;
            mBalls[i].vy = mRandom.nextBoolean() ? speedY : -speedY;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = resolveSize(mWidth, widthMeasureSpec);
        mHeight = resolveSize(mHeight, heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);
        maxRadius = mWidth / 10;
        minRadius = mWidth / 10;
        // 初始化圆的半径和圆心
        for (int i = 0; i < mCount; i++) {
            if (datas.size() > 0) {
                if ("1".equals(datas.get(i).getDifficultyLevel())) {
                    mBalls[i].radius = mWidth / 14 + stoke;
                } else if ("2".equals(datas.get(i).getDifficultyLevel())) {
                    mBalls[i].radius = mWidth / 13 + stoke;
                } else if ("3".equals(datas.get(i).getDifficultyLevel())) {
                    mBalls[i].radius = mWidth / 12 + stoke;
                } else if ("4".equals(datas.get(i).getDifficultyLevel())) {
                    mBalls[i].radius = mWidth / 11 + stoke;
                } else if ("5".equals(datas.get(i).getDifficultyLevel())) {
                    mBalls[i].radius = maxRadius + stoke;
                } else {
                    mBalls[i].radius = minRadius + stoke;
                }
            } else {
                mBalls[i].radius = maxRadius + stoke;
            }
            // 初始化圆心的位置， x最小为 radius， 最大为mwidth- radius
            mBalls[i].cx = mRandom.nextInt(mWidth - mBalls[i].radius) + mBalls[i].radius;
            mBalls[i].cy = mRandom.nextInt(mHeight - mBalls[i].radius) + mBalls[i].radius;
            //
            bList.add(mBalls[i]);
            position = i;
            checkCollide(mBalls[position], position);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        long startTime = System.currentTimeMillis();
        /**
         *  先画出所有圆
         */
        if (datas.size() > 0) {
            for (int i = 0; i < datas.size(); i++) {
                Ball ball = mBalls[i];
                canvas.drawCircle(ball.cx, ball.cy, ball.radius, ball.paint);
                Rect rect = new Rect((int) (ball.cx - ball.radius), (int) (ball.cy - ball.radius), (int) (ball.cx + ball.radius), (int) (ball.cy + ball.radius));
                Paint.FontMetrics fontMetrics = tPaint.getFontMetrics();
                float top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                float bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                int baseLineY = (int) (rect.centerY() + top);//基线中间点的y轴计算公式
                String str = datas.get(i).getName();
                ball.content = datas.get(i).getName();
                ball.taskId = datas.get(i).getId();
                StaticLayout layout = new StaticLayout(str, tPaint, 2 * ball.radius, Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
                canvas.save();
                canvas.translate(rect.centerX(), baseLineY);//从100，100开始画
                layout.draw(canvas);
                canvas.restore();//
            }
            /**
             *  球碰撞边界
             */

            for (int i = 0; i < datas.size(); i++) {
                Ball ball = mBalls[i];
                collisionDetectingAndChangeSpeed(ball); // 碰撞边界的计算
                collisionDetectingAndChangeSpeeds(ball, i);
                ball.move(); // 移动
            }

            long stopTime = System.currentTimeMillis();
            long runTime = stopTime - startTime;
            // 16毫秒执行一次
            postInvalidateDelayed(Math.abs(runTime - 16));
        }
    }

    /**
     * 气泡碰撞算法
     *
     * @param ball
     * @param position
     */
    private void collisionDetectingAndChangeSpeeds(Ball ball, int position) {
        float cx = ball.cx;
        float cy = ball.cy;
        int cr = ball.radius;
        for (int i = 0; i < mCount; i++) {
            if (i == position) {
                return;
            }
            Ball balls = mBalls[i];
            if (checkCollideCircle(cx, cy, cr, balls.cx, balls.cy, balls.radius)) {
                ball.vx = -ball.vx;
                ball.vy = -ball.vy;
                balls.vx = -balls.vx;
                balls.vy = -balls.vy;
            }
        }
    }

    // 判断球是否碰撞碰撞边界
    public void collisionDetectingAndChangeSpeed(Ball ball) {
        int left = getLeft();
        int top = getTop();
        int right = getRight();
        int bottom = getBottom();

        float speedX = ball.vx;
        float speedY = ball.vy;

        // 碰撞左右，X的速度取反。 speed的判断是防止重复检测碰撞，然后黏在墙上了=。=
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

    /**
     * 初始化圆球不重叠
     *
     * @param mBall
     * @param position
     */
    private void checkCollide(Ball mBall, int position) {
        float cx = mBall.cx;
        float cy = mBall.cy;
        int cr = mBall.radius;
        for (int i = 0; i < bList.size(); i++) {
            if (i == position) {
                return;
            }
            Ball balls = bList.get(i);
            if (checkCollideCircle(cx, cy, cr, balls.cx, balls.cy, balls.radius)) {
                mBalls[position].cx = mRandom.nextInt(mWidth - mBalls[i].radius) + mBalls[i].radius;
                mBalls[position].cy = mRandom.nextInt(mHeight - mBalls[i].radius) + mBalls[i].radius;
                checkCollide(mBalls[position], position);
            }
        }
    }

    /**
     * 两个圆的碰撞检测
     *
     * @param x1
     * @param y1
     * @param r1
     * @param x2
     * @param y2
     * @param r2
     * @return
     */
    private boolean checkCollideCircle(float x1, float y1, int r1, float x2,
                                       float y2, int r2) {
        boolean iscollide = false;
        //两点之间的距离 小于两半径之和就发生了碰撞
        if (Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2)) <= r1 + r2 + stoke) {
            iscollide = true;
        } else {
            iscollide = false;
        }
        return iscollide;
    }

    public void setData(ArrayList<TaskModel> mList) {
        this.mCount = mList.size();
        this.datas = mList;
        if (Looper.getMainLooper() == Looper.myLooper()) {
            invalidate();
        } else {
            postInvalidate();
        }
    }

    /**
     * 获取十六进制的颜色代码.例如  "#5A6677"
     * 分别取R、G、B的随机值，然后加起来即可
     *
     * @return String
     */
    public String getRandColor() {
        String R, G, B;
        Random random = new Random();
        R = Integer.toHexString(random.nextInt(256)).toUpperCase();
        G = Integer.toHexString(random.nextInt(256)).toUpperCase();
        B = Integer.toHexString(random.nextInt(256)).toUpperCase();

        R = R.length() == 1 ? "0" + R : R;
        G = G.length() == 1 ? "0" + G : G;
        B = B.length() == 1 ? "0" + B : B;

        return R + G + B;
    }

    public float dp2px(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                Resources.getSystem().getDisplayMetrics());
    }

    class Ball {
        int radius; // 半径
        float cx;   // 圆心
        float cy;   // 圆心
        float vx; // X轴速度
        float vy; // Y轴速度
        Paint paint;
        String content;
        String taskId;

        // 移动
        void move() {
            //向角度的方向移动，偏移圆心
            cx += vx;
            cy += vy;
        }

        int left() {
            return (int) (cx - radius);
        }

        int right() {
            return (int) (cx + radius);
        }

        int bottom() {
            return (int) (cy + radius);
        }

        int top() {
            return (int) (cy - radius);
        }
    }
}
