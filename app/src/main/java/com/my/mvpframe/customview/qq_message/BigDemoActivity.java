package com.my.mvpframe.customview.qq_message;


import android.app.Activity;
import android.os.Bundle;

import com.my.mvpframe.R;

public class BigDemoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big);

        CoverManager.getInstance().init(this);

        CoverManager.getInstance().setMaxDragDistance(350);
        CoverManager.getInstance().setEffectDuration(150);
    }

}
