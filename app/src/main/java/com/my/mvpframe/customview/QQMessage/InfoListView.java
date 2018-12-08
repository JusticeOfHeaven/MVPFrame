package com.my.mvpframe.customview.QQMessage;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * Created by yinwei on 2015-12-01.
 */
public class InfoListView extends ListView {
    private int mScreenWidth;    // 屏幕宽度
    private float mDownX;            // 按下点的x值
    private float mDownY;            // 按下点的y值
    private int mActionViewWidth;// 操作view的宽度

    /**
     * 执行动画的时间
     */
    protected long mAnimationTime = 150;

    private boolean isActionViewShow = false;    // 删除按钮是否正在显示

    private ViewGroup mPointChild;    // 当前处理的item
    private LinearLayout.LayoutParams mLayoutParams;    // 当前处理的item的LayoutParams

    private int touchSlop;//最小偏移量超过这个值才处理滑动事件

    private float scroll;//偏移的距离

    private int openedIntemPosition = -1;//记录已经打开的item的位置

    private int childPosition;//手指落下位置的item的position

    private boolean iswiping = false;//手指是否正在滑动

    private boolean isDownToNormal = false;//判断之前是否是手指落下导致消失。

    private boolean isUPToNormal = false;//判断之前是否是手指离开导致消失。

    private long lastTime;//计算手指两次的触摸间隔

    public InfoListView(Context context) {
        this(context, null);
    }

    public InfoListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InfoListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        // 获取屏幕宽度
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        mScreenWidth = dm.widthPixels;
        final ViewConfiguration configuration = ViewConfiguration.get(getContext());
        touchSlop = configuration.getScaledTouchSlop();

    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                performActionDown(ev);
                break;
            case MotionEvent.ACTION_MOVE:
                return performActionMove(ev);
            case MotionEvent.ACTION_UP:
                performActionUp(ev);
                break;
        }


        return super.onTouchEvent(ev);

    }

    // 处理action_down事件
    private void performActionDown(MotionEvent ev) {


        //如果触摸间隔小于100MS 不让进来~
        if (System.currentTimeMillis() - lastTime <= mAnimationTime) {

            lastTime = System.currentTimeMillis();
            return;
        }

        lastTime = System.currentTimeMillis();

        mDownX = ev.getX();
        mDownY = ev.getY();

        if (isActionViewShow) {

            childPosition = pointToPosition((int) mDownX, (int) mDownY);

            if (childPosition == AdapterView.INVALID_POSITION) {

                return;

            }
            if ((openedIntemPosition != childPosition - getFirstVisiblePosition())) {

                //全部恢复初始值不作响应
                turnToNormal();
                isDownToNormal = true;
                mDownX = -1;
                mDownY = -1;
                return;
            }

        }

        if ((!isActionViewShow && openedIntemPosition == -1)) {
            // 获取当前点的item
            childPosition = pointToPosition((int) mDownX, (int) mDownY);

            if (childPosition == AdapterView.INVALID_POSITION) {

                return;

            }

            openedIntemPosition = childPosition - getFirstVisiblePosition();

            mPointChild = (ViewGroup) getChildAt(openedIntemPosition);
            // 获取操作view宽度
            mActionViewWidth = mPointChild.getChildAt(1).getLayoutParams().width;


            mLayoutParams = (LinearLayout.LayoutParams) mPointChild.getChildAt(0)
                    .getLayoutParams();
            // 为什么要重新设置layout_width 等于屏幕宽度
            // 因为match_parent时，不管你怎么滑，都不会显示删除按钮
            // why？ 因为match_parent时，ViewGroup就不去布局剩下的view
            mLayoutParams.width = mScreenWidth;
            mPointChild.getChildAt(0).setLayoutParams(mLayoutParams);

        }

    }

    // 处理action_move事件
    private boolean performActionMove(MotionEvent ev) {


        if ((Math.abs(ev.getX() - mDownX) > touchSlop
                && Math.abs(ev.getY() - mDownY) < touchSlop)
                || (Math.abs(ev.getX() - mDownX) > touchSlop
                && Math.abs(ev.getY() - mDownY) > touchSlop
                && (Math.abs(ev.getX() - mDownX) > Math.abs(ev.getY() - mDownY)))) {


            iswiping = true;

            //当手指滑动item,取消item的点击事件，不然我们滑动Item也伴随着item点击事件的发生
            MotionEvent cancelEvent = MotionEvent.obtain(ev);
            cancelEvent.setAction(MotionEvent.ACTION_CANCEL |
                    (ev.getActionIndex() << MotionEvent.ACTION_POINTER_INDEX_SHIFT));
            onTouchEvent(cancelEvent);

        }

        if (isActionViewShow) {

            childPosition = pointToPosition((int) mDownX, (int) mDownY);

            if (childPosition == AdapterView.INVALID_POSITION) {

                return true;

            }
            if ((openedIntemPosition != childPosition - getFirstVisiblePosition())) {

                //全部恢复初始值不作响应
                turnToNormal();
                isDownToNormal = true;
                mDownX = -1;
                mDownY = -1;
                iswiping = false;
                return true;
            }

        }


        if (iswiping) {

            scroll = ev.getX() - mDownX;

            if (Math.abs(scroll) <= mActionViewWidth) {

                if ((scroll <= 0 && !isActionViewShow)) {
//                    ViewHelper.setTranslationX(mPointChild.getChildAt(0), scroll);
                    mPointChild.getChildAt(0).animate().translationX(scroll);
//                    ViewHelper.setTranslationX(mPointChild.getChildAt(1), scroll);
                    mPointChild.getChildAt(1).animate().translationX(scroll);

                } else if ((scroll >= 0 && isActionViewShow && openedIntemPosition != -1)) {

//                    ViewHelper.setTranslationX(mPointChild.getChildAt(0), (scroll - mActionViewWidth));
//                    ViewHelper.setTranslationX(mPointChild.getChildAt(1), (scroll - mActionViewWidth));
                    mPointChild.getChildAt(0).animate().translationX(scroll - mActionViewWidth);
                    mPointChild.getChildAt(1).animate().translationX(scroll - mActionViewWidth);

                }

            }

            return true;
        }

        return super.onTouchEvent(ev);

    }


    // 处理action_up事件
    private void performActionUp(MotionEvent ev) {


        if (!iswiping) {

            if (isActionViewShow && openedIntemPosition != -1) {

                isUPToNormal = true;
                turnToNormal();
            } else {

                turnToNormal();
            }


            return;
        }


        scroll = ev.getX() - mDownX;
        // 偏移量大于操作view的一半，则显示
        // 否则恢复默认
        //向左滑动


        if (scroll < 0) {


            if ((-scroll >= mActionViewWidth / 4) && !isActionViewShow) {

                show();

            } else if ((-scroll < mActionViewWidth / 4) || ((-scroll >= mActionViewWidth / 4) && isActionViewShow)) {

                if (isActionViewShow) {

                    childPosition = pointToPosition((int) mDownX, (int) mDownY);

                    if (childPosition == AdapterView.INVALID_POSITION) {

                        return;

                    }
                    if (openedIntemPosition == childPosition - getFirstVisiblePosition()) {

                        return;
                    }

                }

                turnToNormal();

            }

        } else if (scroll > 0) {//向右滑动


            if (scroll < mActionViewWidth / 4 && isActionViewShow && openedIntemPosition != -1) {

                show();

            } else if (scroll >= mActionViewWidth / 4 && isActionViewShow && openedIntemPosition != -1) {

                turnToNormal();

            } else {

                //如果没有展开的其它情况全部恢复原样
                turnToNormal();

            }

        }


        iswiping = false;


    }


    @Override
    public boolean performItemClick(View view, int position, long id) {

        //当有展开的item的时候，不响应item的点击事件
        if (isActionViewShow || isDownToNormal || isUPToNormal) {

            isDownToNormal = false;
            isUPToNormal = false;
            if (isActionViewShow && openedIntemPosition != -1) {

                turnToNormal();
            }
            return false;
        }
        return super.performItemClick(view, position, id);
    }


    /**
     * 隐藏操作view
     */
    public void turnToNormal() {

        if (mPointChild == null) {

            return;
        }
        isActionViewShow = false;
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mPointChild.getChildAt(0), "translationX", 0);

        ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(mPointChild.getChildAt(1),
                "translationX", mActionViewWidth);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(objectAnimator, objectAnimator1);
        animatorSet.setDuration(mAnimationTime);
        animatorSet.start();
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {

                openedIntemPosition = -1;

            }
        });

    }

    /**
     * 显示操作view
     */
    private void show() {

        if (mPointChild == null) {

            return;
        }

        isActionViewShow = true;
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mPointChild.getChildAt(0), "translationX",
                -mActionViewWidth);

        ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(mPointChild.getChildAt(1), "translationX",
                -mActionViewWidth);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(objectAnimator, objectAnimator1);
        animatorSet.setDuration(mAnimationTime);
        animatorSet.start();
        
    }

    /**
     * 是否显示
     * @return
     */
    public boolean isActionViewShow() {
        return isActionViewShow;
    }

}
