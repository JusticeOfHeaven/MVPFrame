package com.my.mvpframe.customview.pic_roll;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by jzhan on 2018/11/16.
 **/
public class RollView extends View {
    public RollView(Context context) {
        this(context,null);
    }

    public RollView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RollView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {

    }
}
