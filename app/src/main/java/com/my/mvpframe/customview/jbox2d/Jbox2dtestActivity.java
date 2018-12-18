package com.my.mvpframe.customview.jbox2d;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.my.mvpframe.R;
import com.my.mvpframe.appbase.BaseActivity;

import butterknife.BindView;

/**
 * Create by jzhan on 2018/12/12
 */
public class Jbox2dtestActivity extends BaseActivity implements SensorEventListener {
//    @BindView(R.id.demoView)
    JBoxDemoView demoView;
    private int[] imgs = {
            R.mipmap.share_fb,
            R.mipmap.share_kongjian,
            R.mipmap.share_pyq,
            R.mipmap.share_qq,
            R.mipmap.share_tw,
            R.mipmap.share_wechat,
            R.mipmap.share_weibo
    };
    private SensorManager sensorManager;
    private Sensor sensor;

    @Override
    protected int getLayoutId() {
        return R.layout.act_jbox_demo;
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        demoView = (JBoxDemoView) findViewById(R.id.demoView);
        init();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }
    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
    }
    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
    @Override
    protected void initView() {

    }
    private void init() {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        for (int i = 0; i < 1; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(imgs[i]);
            imageView.setTag(R.id.wd_view_circle_tag, true);
            demoView.addView(imageView, layoutParams);
        }
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1] * 2.0f;
//            demoView.onSensorChanged(-x, y);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
