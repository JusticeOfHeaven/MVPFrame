package com.my.mvpframe.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.my.mvpframe.R;

/**
 * Created by ZJ on 2018/1/30.
 * 通用的Dialog
 * layoutId已设置成外部传入的方式，但要注意里面控件的命名，以及控件的选取应用(比如按钮选用的是TextView)
 */

public class NormalDialog extends Dialog {

    private final int layoutId;
    private boolean canCancleOutSide = false;
    private TextView mTitle;
    private TextView mMessage;
    private TextView mYes;
    private TextView mNo;
    private onNoOnclickListener noOnclickListener;//取消按钮被点击了的监听器
    private onYesOnclickListener yesOnclickListener;//确定按钮被点击了的监听器

    public NormalDialog(@NonNull Context context, @LayoutRes int layoutId) {
        super(context, R.style.MyDialog);
        this.layoutId = layoutId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutId);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(canCancleOutSide);
        //初始化界面控件
        initView();
        //初始化界面数据
        initData();
        //初始化界面控件的事件
        initEvent();
    }

    private void initView() {
        mTitle = findViewById(R.id.title);
        mMessage = findViewById(R.id.message);
        mYes = findViewById(R.id.YES);
        mNo = findViewById(R.id.NO);
    }

    private void initData() {

    }

    private void initEvent() {
        mYes.setOnClickListener(view -> {
            if (yesOnclickListener != null)
                yesOnclickListener.onYesClick();
        });

        mNo.setOnClickListener(view -> {
            if (noOnclickListener !=null)
                noOnclickListener.onNoClick();
        });
    }

    /**
     * 设置标题
     */
    public NormalDialog setTitle(String title) {
        mTitle.setText(title);
        return this;
    }
    /**
     * 设置标题是否可见
     */
    public NormalDialog setTitleVisiable(boolean visiable) {
        mTitle.setVisibility(visiable? View.VISIBLE:View.GONE);
        return this;
    }
    /**
     * 设置内容
     */
    public NormalDialog setMessage(String message) {
        mMessage.setText(message);
        return this;
    }

    /**
     * Dialog外点击消失
     */
    public NormalDialog setCanCancleOutSide(boolean cancleOutSide){
        setCanceledOnTouchOutside(cancleOutSide);
        return this;
    }

    /**
     * 单个按钮,Dialog 只有一个功能按钮，默认是两个
     */
    public NormalDialog setSingleButton(){
        mNo.setVisibility(View.GONE);
        return this;
    }
    public interface onYesOnclickListener {
        void onYesClick();
    }

    public interface onNoOnclickListener {
        void onNoClick();
    }
}
