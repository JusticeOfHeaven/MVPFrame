package com.my.mvpframe.customview.three_d_view;

import android.view.MotionEvent;
import android.widget.TextView;

import com.my.mvpframe.R;
import com.my.mvpframe.appbase.BaseActivity;

import butterknife.BindView;

/**
 * Created by jzhan on 2018/11/7.
 **/
public class ThreeDViewActivity extends BaseActivity {
    @BindView(R.id.tv_x_value)
    TextView xValue;
    @BindView(R.id.tv_y_value)
    TextView yValue;
    @BindView(R.id.tv_rotate_value)
    TextView rotateValue;
    @BindView(R.id.tv_cameraZ_value)
    TextView cameraZvalue;
    private ThreeDViewController threeDViewController;

    @Override
    protected int getLayoutId() {
        return R.layout.act_three_d;
    }

    @Override
    protected void initView() {
        ThreeDView threeDView = (ThreeDView)findViewById(R.id.three_d_view);
        threeDView.setStateValueListener(new ThreeDView.StateValueListener() {
            @Override
            public void stateValue(float distanceX, float distanceY, float rotateDeg, float distanceZ) {
                String xvalue = "" + distanceX, yvalue = "" + distanceY, rotateDegStr = "" + rotateDeg,
                        distanceZStr = "" + distanceZ;
                xValue.setText(xvalue);
                yValue.setText(yvalue);
                rotateValue.setText(rotateDegStr);
                cameraZvalue.setText(distanceZStr);
            }
        });

        threeDViewController = new ThreeDViewController(threeDView);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        threeDViewController.inputTouchEvent(event);
        return super.onTouchEvent(event);
    }
}
