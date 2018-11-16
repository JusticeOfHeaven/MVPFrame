package com.my.mvpframe.customview.pic_roll;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.my.mvpframe.R;

/**
 * Created by jzhan on 2018/11/16.
 **/
public class RollViewActivity extends AppCompatActivity {
    private RollView view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_roll_view);

        view = findViewById(R.id.view);

        ObjectAnimator animator1 = ObjectAnimator.ofFloat(view, "rightFlip", 1);
        animator1.setDuration(500);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(view, "flipRotation", 270);
        animator2.setDuration(1000);
        animator2.setStartDelay(300);
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(view, "leftFlip", 1);
        animator3.setDuration(500);
        animator3.setStartDelay(300);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(animator1, animator2, animator3);
        animatorSet.setStartDelay(600);


        findViewById(R.id.textView).setOnClickListener(view1 -> {
            animatorSet.start();
        });
    }
}
