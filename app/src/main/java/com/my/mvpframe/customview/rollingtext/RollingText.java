package com.my.mvpframe.customview.rollingtext;

import android.animation.IntEvaluator;
import android.animation.TypeEvaluator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.my.mvpframe.R;

/**
 * Create by jzhan on 2018/12/3
 * TextView 上下滚动控件
 */
public class RollingText extends View {
    private String orderList = "01234567890";
    private String rollText = "210";
    private Paint textPaint;
    private float bottom;
    private float top;
    private int textHeight;
    private float textWidth;
    private SparseArray<AssistBean> array = new SparseArray<>();
    private float textSize = 55;
    private int maxText;// rollText 里面的最大数
    private int maxTextPosition;// rollText 里面的最大数的位置
    private int width;
    private int height;
    private Paint.FontMetrics fontMetrics;
    private boolean isRollAnimEnd = false;
    private boolean isBigger = true;

    public RollingText(Context context) {
        this(context, null);
    }

    public RollingText(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RollingText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
        textWidth = textPaint.measureText(String.valueOf(orderList.charAt(0)));

        fontMetrics = textPaint.getFontMetrics();
        bottom = fontMetrics.bottom;
        top = fontMetrics.top;
//        textHeight = (int) (bottom - top);
        textHeight = (int) textSize;
//        textHeight = (int) (height/2+textSize);
//        textHeight = (int) (fontMetrics.bottom-fontMetrics.ascent);

        initRollText();

    }

    private void initRollText() {
        int length = rollText.length();
        if (length == 1) {
            rollText = "00" + rollText;
        } else if (length == 2) {
            rollText = "0" + rollText;
        }
        int finalLength = rollText.length();
//        array.clear();
        for (int i = finalLength - 1; i >= 0; i--) {
            array.append(i, new AssistBean(String.valueOf(rollText.charAt(i)),i));// 记录每一个数字，好计算每个数字的平移速度
        }
        getMaxWordAndPosition(rollText);
//        Log.i("TAG",array.toString());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawTextList(canvas);
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
        for (int i = finalLength - 1; i >= 0; i--) {
            char charAt = rollText.charAt(i);
            int indexOf = orderList.indexOf(charAt);
            String substring;
//            if (TextUtils.equals("0", String.valueOf(charAt))) {// 这个情况是9变0
                substring = orderList;
//            } else {
//                if (indexOf + 2 > orderList.length() - 1) {
//                    substring = orderList;
//                } else {
//                    substring = orderList.substring(0, indexOf + 2);
//                }
//            }
//            Log.i("TAG", "charAt = " + charAt + "   subString = " + substring);
            rollingText(canvas, substring, i);
//            Log.i("TAG","i = "+i+"  position = "+(finalLength-1-i)+"   chartAt = "+charAt);

            array.get(i).run();
        }
    }

    // 滚动动画
    private void rollingText(Canvas canvas, String substring, int position) {
        int length = substring.length();
        int rollLength = rollText.length();
        canvas.save();

        canvas.translate(0, array.get(position).translateY);

        for (int i = 0; i < length; i++) {
//            canvas.drawText(String.valueOf(substring.charAt(i)), 0, 1, width-textWidth * (position+1), (float) (textHeight * (i + 1)), textPaint);
            canvas.drawText(String.valueOf(substring.charAt(i)), 0, 1, width - textWidth * (rollLength - position), (float) (textHeight * (i + 1)), textPaint);
        }

        canvas.restore();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

    private void begin() {
//        Log.e("TAG","begin");
        if (!isRollAnimEnd) {
            postDelayed(() -> {
                invalidate();
            }, 30);
        }
    }

    public void start() {
        isRollAnimEnd = false;
//        initRollText();
        begin();
    }

    public void setRollText(String rollText) {
        if (isDigit(rollText)) {

            if (Integer.parseInt(rollText) >= Integer.parseInt(this.rollText)) {
                isBigger = true;
            } else {
                isBigger = false;
            }
            // 新设置的数字跟之前的数字不同
            if (!TextUtils.equals(this.rollText, rollText)) {
                int oldLength = this.rollText.length();
                int newLength = rollText.length();
                if (oldLength == newLength) {// 位数相等
                    for (int i = 0; i < oldLength; i++) {
                        // 如果数字相同
//                        if (TextUtils.equals(String.valueOf(this.rollText.charAt(i)), String.valueOf(rollText.charAt(i)))) {
                        array.get(i).newText = String.valueOf(rollText.charAt(i));
                        array.get(i).x = 0;// 重置x
//                        }
                    }
//                    Log.i("TAG",array.toString());
                } else {
                    // 位数不相等
                    for (int i = newLength - 1; i >= 0; i--) {
                        array.append(i, new AssistBean(String.valueOf(rollText.charAt(i)),i));// 记录每一个数字，好计算每个数字的平移速度
                    }
                }
            }

            this.rollText = rollText;
            getMaxWordAndPosition(this.rollText);
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
        return -textHeight * Integer.parseInt(num) - 2;
    }

    // 获取每一个数字运动的总时长
    private float getFinalTime(String num) {
        return Integer.parseInt(num);
    }

    class AssistBean {
        float time;// 动画执行完的总时长
        String text;// 当前的数字
        String newText;// 新数字
        int drawTextPosition;// 当前绘制的字符位数
        int translateY;// 记录最终的平移量
        float x;// 用于计算平移量Y的x值

        public AssistBean(String text) {
            this.text = text;
        }

        public AssistBean(String text, int drawTextPosition) {
            this.text = text;
            this.drawTextPosition = drawTextPosition;
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
                if (newText != null) {
                    text = newText;
                }
            } else if (x <= 0) {
                x = 0f;
            }
            switch (text) {
                case "0":
                    // 变成"0"，要把translateY改成0，这样就能无限循环了
                    if (x == 1f) {
                        translateY = 0;
                    }else {
                        getTranslateY();
                    }

                    x += 0.1f;
                    break;
                case "1":
                    getTranslateY();
                    x += 0.1f;
                    break;
                case "2":
                    getTranslateY();
                    x += 0.09f;
                    break;
                case "3":
                    getTranslateY();
                    x += 0.08f;
                    break;
                case "4":
                    getTranslateY();
                    x += 0.07f;
                    break;
                case "5":
                    getTranslateY();
                    x += 0.06f;
                    break;
                case "6":
                    getTranslateY();
                    x += 0.05f;
                    break;
                case "7":
                    getTranslateY();
                    x += 0.04f;
                    break;
                case "8":
                    getTranslateY();
                    x += 0.03f;
                    break;
                case "9":
                    getTranslateY();
                    if (x == 1) {
                        translateY = getFinalY(text);
                    }
                    x += 0.02f;
                    break;
            }

        }

        private void getTranslateY() {
            if (newText == null) {
                translateY = (int) (getFinalY(text) * getInterpolation(1.8f, x));
            } else if (Integer.parseInt(newText) >= Integer.parseInt(text)) {// 新数比旧数大 translateY要增加
                translateY = (int) ((getFinalY(newText) - getFinalY(text)) * getInterpolation(1.8f, x)) + getFinalY(text);

            } else {
                if (Integer.parseInt(newText) == 0 && Integer.parseInt(text) == 9) {// 这种情况是  9 --> 0
                    translateY = (int) ((getFinalY("10") - getFinalY(text)) * getInterpolation(1.8f, x)) + getFinalY(text);
                }else{
                    if (Integer.parseInt(newText) < Integer.parseInt(text)){// 新数比旧数小，translateY要减小
                        translateY = (int) ((getFinalY(newText) - getFinalY(text)) * getInterpolation(1.8f, x)) + getFinalY(text);
//                Log.i("TAG","减少Y = "+translateY);
                    }
                }
            }

            if (drawTextPosition == 2) {
                Log.i("TAG","Y = "+translateY+"   x = "+x+"  text = "+text+"   newText = "+newText);
            }

        }

        @Override
        public String toString() {
            return "AssistBean{" +
                    "time=" + time +
                    ", text='" + text + '\'' +
                    ", newText='" + newText + '\'' +
                    ", drawTextPosition=" + drawTextPosition +
                    ", translateY=" + translateY +
                    ", x=" + x +
                    '}';
        }
    }

}
