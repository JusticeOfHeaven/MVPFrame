package com.my.mvpframe.customview.delete;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.my.mvpframe.R;

/**
 * Created by jzhan on 2018/11/20.
 **/
public class ScrollDelete1View extends HorizontalScrollView {
    public ScrollDelete1View(Context context) {
        this(context,null);
    }

    public ScrollDelete1View(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ScrollDelete1View(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LinearLayout linearLayout = new LinearLayout(context);
        View view = LayoutInflater.from(context).inflate(R.layout.item_demo, null, false);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.addView(view);

        addView(linearLayout);
    }
}
