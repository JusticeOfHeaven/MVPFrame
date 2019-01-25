package com.my.mvpframe.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * Create by jzhan on 2019/1/25
 * 像素
 */
public class DisplayUtil {

    public static float dp2px(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
    }

    public static int dpToPx(Context context, int dp) {
        return (int) ((dp * context.getResources().getDisplayMetrics().density) + 0.5);
    }

    public static int pxToDp(Context context, int px) {
        return (int) ((px / context.getResources().getDisplayMetrics().density) + 0.5);
    }

    public static int pxToDp(float px) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return Math.round(dp);
    }

    public static int dpToPx(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }
}
