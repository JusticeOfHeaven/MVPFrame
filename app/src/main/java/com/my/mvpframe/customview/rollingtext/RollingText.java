package com.my.mvpframe.customview.rollingtext;

import android.animation.IntEvaluator;
import android.animation.TypeEvaluator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Create by jzhan on 2018/12/3
 * TextView 上下滚动控件
 */
public class RollingText extends View {
    private String orderList = "0123456789";
    private String rollText = "43251";
    private Paint textPaint;
    private float bottom;
    private float top;
    private int textHeight;
    private float textWidth;
    private SparseArray<AssistBean> array = new SparseArray<>();
    private float textSize = 55;
    private int maxText;
    private int maxTextPosition;

    public RollingText(Context context) {
        this(context, null);
    }

    public RollingText(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RollingText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setBackgroundColor(Color.BLUE);
        setWillNotDraw(false);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(textSize);
        textWidth = textPaint.measureText(String.valueOf(orderList.charAt(0)));

        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        bottom = fontMetrics.bottom;
        top = fontMetrics.top;
        textHeight = (int) (bottom - top);


        int length = rollText.length();
        if (length == 1) {
            rollText = "00" + rollText;
        } else if (length == 2) {
            rollText = "0" + rollText;
        }
        int finalLength = rollText.length();
        for (int i = 0; i < finalLength; i++) {
            array.append(i, new AssistBean(String.valueOf(rollText.charAt(i))));// 记录每一个数字，好计算每个数字的平移速度
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawTextList(canvas);

        canvas.drawLine(0, textHeight, getWidth(), textHeight, textPaint);
        begin();
    }

    private void drawTextList(Canvas canvas) {
        int length = rollText.length();
        if (length == 1) {
            rollText = "00" + rollText;
        } else if (length == 2) {
            rollText = "0" + rollText;
        }
        int finalLength = rollText.length();
        for (int i = 0; i < finalLength; i++) {
            char charAt = rollText.charAt(i);
            int indexOf = orderList.indexOf(charAt);
            String substring = orderList.substring(0, indexOf + 1);
            rollingText(canvas, substring, i);

            array.get(i).run();
        }
    }

    // 滚动动画
    private void rollingText(Canvas canvas, String substring, int position) {
        int length = substring.length();
        canvas.save();

//        if (-translateY >= (textHeight * (length - 1)) && array.indexOfKey(position) < 0) {
//            array.append(position, new AssistBean(length - 1, -(textHeight * (length - 1))));
//        }

//        if (array.indexOfKey(position) < 0) {
//            canvas.translate(0, translateY);
//        } else {
        canvas.translate(0, array.get(position).translateY);
//        }
        for (int i = 0; i < length; i++) {
            canvas.drawText(String.valueOf(substring.charAt(i)), 0, 1, textWidth * position, (float) (textHeight * (i + 1)), textPaint);
        }

        canvas.restore();
    }

    private void begin() {
//        if (array.size() != rollText.length()) {
        postDelayed(() -> {
//            int size = array.size();
//            for (int i = 0; i < size; i++) {
//                Integer evaluate = evaluate(0.1f, array.get(i).translateY, getFinalY(array.get(i).text));
//                array.get(i).translateY = evaluate;
//                array.get(i).run();
//            }
//                Log.i("TAG","list =  "+array.toString());
            invalidate();
        }, 30);
//        }
    }

    public void start() {
        array.clear();
        begin();
    }

    public void setRollText(String rollText) {
        if (isDigit(rollText)) {
            this.rollText = rollText;
        }
    }

    //获取设置rollText里面的最大值，及其位置
    public void getMaxWordAndPosition(String str) {
        int length = str.length();
        for (int i = 0; i < length; i++) {
            if (maxText < Integer.parseInt(String.valueOf(str.charAt(i)))) {
                maxText = Integer.parseInt(String.valueOf(str.charAt(i)));
                maxTextPosition = i;
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
        return -textHeight * Integer.parseInt(num);
    }

    // 获取每一个数字运动的总时长
    private float getFinalTime(String num){
        return Integer.parseInt(num);
    }

    class AssistBean {
        float time;// 动画执行完的总时长
        String text;// 当前的数字
        int drawTextPosition;// 当前绘制的字符位数
        int translateY;// 记录最终的平移量
        float x;// 用于计算平移量Y的x值

        public AssistBean(String text) {
            this.text = text;
        }

        public AssistBean(int drawTextPosition, int translateY) {
            this.drawTextPosition = drawTextPosition;
            this.translateY = translateY;
        }

        void run() {
            switch (text) {
                case "0":
                    translateY = 0;
                    break;
                default:
                    if (x < 1) {
                        translateY = (int) (getInterpolation(2, x) * getFinalY(text));
                        x = getFinalY(text)/getFinalTime(text);
                    } else {
                        translateY = getFinalY(text);
                    }
                    break;
            }

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
