package com.my.mvpframe.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by jzhan on 2018/11/6.
 *
 **/
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    private int topBottom;
    private int leftRight;
    private int mDividerHeight = 2;//默认尺寸
    private Drawable mDivider;
    private Paint mPaint;
    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};

    public SpaceItemDecoration(Context context) {
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();
    }

    public SpaceItemDecoration(Context context, @DrawableRes int drawableId) {
        this(context);
        mDivider = ContextCompat.getDrawable(context, drawableId);
        mDividerHeight = mDivider.getIntrinsicHeight();
    }

    public SpaceItemDecoration(Context context, int dividerHeight, int dividerColor) {
        this(context);
        mDividerHeight = dividerHeight;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(dividerColor);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
//        Log.e("TAG", "onDraw");
        LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
        if (layoutManager.getOrientation() == LinearLayoutManager.VERTICAL) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }

    /*每次都会执行*/
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
//        Log.e("TAG", "getItemOffsets");
        LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
        if (layoutManager.getOrientation() == LinearLayoutManager.VERTICAL) {
            outRect.set(0, 0, 0, mDividerHeight);
        } else {
            outRect.set(0, 0, mDividerHeight, 0);
        }

//        //第二种方法
//        LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
//        if (layoutManager.getOrientation() == LinearLayoutManager.VERTICAL) {
//            // 垂直最后一项不需要bottom
//            if (parent.getChildAdapterPosition(view) == layoutManager.getItemCount() - 1) {
//                outRect.bottom = topBottom;
//            }
//            outRect.bottom = topBottom;
//            outRect.left = leftRight;
//            outRect.right = leftRight;
//        } else {
//            // 水平最后一项不需要right
//            if (parent.getChildAdapterPosition(view) == layoutManager.getItemCount() - 1) {
//                outRect.right = leftRight;
//            }
//            outRect.top = topBottom;
//            outRect.bottom = topBottom;
//            outRect.left = leftRight;
//        }
    }

    // 绘制横向divider
    private void drawVertical(Canvas c, RecyclerView parent) {
        int left = parent.getPaddingLeft();
        int right = parent.getMeasuredWidth() - parent.getPaddingRight();
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            int top = child.getBottom() + layoutParams.bottomMargin;
            int bottom = top + mDividerHeight;
            if (mDivider != null) {
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
            if (mPaint != null) {
                c.drawRect(left, top, right, bottom, mPaint);
            }

        }
    }

    private void drawHorizontal(Canvas c, RecyclerView parent) {
        int top = parent.getPaddingTop();
        int bottom = parent.getMeasuredHeight() - parent.getPaddingBottom();
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            int left = child.getRight() + layoutParams.rightMargin;
            int right = left + mDividerHeight;
            if (mDivider != null) {
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
            if (mPaint != null) {
                c.drawRect(left, top, right, bottom, mPaint);
            }
        }
    }
}
