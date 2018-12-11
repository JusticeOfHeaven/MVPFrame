package com.my.mvpframe.customview.rollingtext;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;

import com.my.mvpframe.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Create by jzhan on 2018/12/3
 * TextView 上下滚动控件
 */
public class RollingTextA extends View {

    private int textSize;
    private Paint textPaint;
    private float textWidth;
    private Paint.FontMetrics fontMetrics;
    private int textHeight;
    private boolean isBigger = true;
    private boolean isRollAnimEnd = false;
    private int maxText;
    private String rollText;
    private SparseArray<AssistBean> sparseArray = new SparseArray<>();
    private int width;

    public RollingTextA(Context context) {
        this(context, null);
    }

    public RollingTextA(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RollingTextA(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        init(context);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RollingText);
        textSize = a.getDimensionPixelOffset(R.styleable.RollingText_rollTextSize, 55);
        a.recycle();
    }

    private void init(Context context) {
//        setBackgroundColor(Color.BLUE);
        setWillNotDraw(false);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(textSize);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textWidth = textPaint.measureText("0");

        fontMetrics = textPaint.getFontMetrics();
//        textHeight = (int) (bottom - top);
        textHeight = (int) textSize;

        initRollText();

    }

    private void initRollText() {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawTextList(canvas);
        begin();
    }

    private void drawTextList(Canvas canvas) {
        int length = sparseArray.size();
        for (int i = 0; i < length; i++) {
            canvas.drawText(sparseArray.get(i).nextText, 0, 1, width - textWidth * (i+1), textHeight, textPaint);
            sparseArray.get(i).run();
        }
    }

    // 滚动动画
    private void rollingText(Canvas canvas, String substring, int position) {

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
    }

    private void begin() {
        postDelayed(() -> {
            invalidate();
        }, 30);
    }

    public void start() {

    }

    public void setRollText(String rollText) {
        if (isDigit(rollText)) {
            int length = rollText.length();
            if (length == 1) {
                this.rollText = "00" + rollText;
            } else if (length == 2) {
                this.rollText = "0" + rollText;
            } else {
                this.rollText = rollText;
            }
            length = this.rollText.length();
            for (int i = length - 1; i >= 0; i--) {
                // 记录每一个数据
                sparseArray.append(length - 1 - i, new AssistBean(String.valueOf(this.rollText.charAt(i))));
            }
        }
    }

    // 验证是否是数字
    private boolean isDigit(String strNum) {
        Pattern pattern = Pattern.compile("-?[0-9]+\\.?[0-9]*");
        Matcher isNum = pattern.matcher(strNum);
        return isNum.matches();
    }

    // 估值
    public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
        int startInt = startValue;
        return (int) (startInt + fraction * (endValue - startInt));
    }

    /**
     * @param mFactor 影响因子
     * @param input   x值
     */
    public float getInterpolation(float mFactor, float input) {
        float result;
        if (mFactor == 1.0f) {
            result = (float) (1.0f - (1.0f - input) * (1.0f - input));
        } else {
            result = (float) (1.0f - Math.pow((1.0f - input), 2 * mFactor));
        }
        return result;
    }

    // 每一个数字最终平移的距离
    private int getFinalY(String num) {
        return -textHeight * Integer.parseInt(num) - 2;
    }

    // 获取每一个数字运动的总时长
    private float getFinalTime(String num) {
        return Integer.parseInt(num);
    }

    class AssistBean {
        float time;// 动画执行完的总时长
        String text;// 当前的数字
        String nextText;// 下个数字
        int drawTextPosition;// 当前绘制的字符位数
        int translateY;// 记录最终的平移量
        float x;// 用于计算平移量Y的x值

        public AssistBean(String text) {
            this.text = text;
            this.nextText = text;
        }

        public AssistBean(int drawTextPosition, int translateY) {
            this.drawTextPosition = drawTextPosition;
            this.translateY = translateY;
        }

        void run() {
            if (x >= 1) {
                x = 1f;
                if (maxText == Integer.parseInt(text)) {
                    isRollAnimEnd = true;
                }
            } else if (x <= 0) {
                x = 0f;
            }

            switch (text) {
                case "0":
                    translateY = 0;
                    break;
                case "1":
                    getTranslateY();
                    x = isBigger ? (x + 0.1f) : (x - 0.1f);
                    break;
                case "2":
                    getTranslateY();
                    x = isBigger ? (x + 0.09f) : (x - 0.09f);
                    break;
                case "3":
                    getTranslateY();
                    x = isBigger ? (x + 0.08f) : (x - 0.08f);
                    break;
                case "4":
                    getTranslateY();
                    x = isBigger ? (x + 0.07f) : (x - 0.07f);
                    break;
                case "5":
                    getTranslateY();
                    x = isBigger ? (x + 0.06f) : (x - 0.06f);
                    break;
                case "6":
                    getTranslateY();
                    x = isBigger ? (x + 0.05f) : (x - 0.05f);
                    break;
                case "7":
                    getTranslateY();
                    x = isBigger ? (x + 0.04f) : (x - 0.04f);
                    break;
                case "8":
                    getTranslateY();
                    x = isBigger ? (x + 0.03f) : (x - 0.03f);
                    break;
                case "9":
                    getTranslateY();
                    if (x == 1) {
                        translateY = getFinalY(text);
                    }
                    x = isBigger ? (x + 0.02f) : (x - 0.02f);
                    break;
            }

        }

        private void getTranslateY() {
            translateY = (int) (getFinalY(text) * getInterpolation(1.8f, x));
        }

        @Override
        public String toString() {
            return "AssistBean{" +
                    "text='" + text + '\'' +
                    ", drawTextPosition=" + drawTextPosition +
                    ", translateY=" + translateY +
                    '}';
        }
    }

}
