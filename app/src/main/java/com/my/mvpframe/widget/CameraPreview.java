package com.my.mvpframe.widget;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.List;

/**
 * Activity中代码
 * 初始化
 * mCamera = Camera.open(0);//0 是前置摄像头  1  是后置摄像头
 * CameraPreview.setCamera(mCamera);
 * CameraPreview.surfaceCreated(CameraPreview.getHolder());
 * 在onResume重新open()
 * 在onDestory，onBackPress释放相机release()
 * 拍照的时候调用c
 * mCamera.takePicture(null, null, new Camera.PictureCallback() {
 *      @Override
 *      public void onPictureTaken(byte[] data, Camera camera) {
 *          mData = data;
 *  }
 * });
 */

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private Camera mCamera;

    public CameraPreview(Context context) {
        super(context);
    }

    public CameraPreview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CameraPreview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setCamera(Camera camera) {
        mCamera = camera;
         // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        Camera.Parameters params = mCamera.getParameters();
        List<String> focusModes = params.getSupportedFocusModes();
        if(
                focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);}
        mCamera.setParameters(params);

        mCamera.setDisplayOrientation(90);
    }
    //重拍
    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        try {
            //绑定Camera和SurfaceView
            mCamera.setPreviewDisplay(holder);
            //预览
            mCamera.startPreview();
        } catch (IOException e) {
            Log.d("this", "Error setting camera preview: " + e.getMessage());
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // empty. Take care of releasing the Camera preview in your activity.
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (mHolder.getSurface() == null){
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            //横屏切换的时候要先停止
            mCamera.stopPreview();
        } catch (Exception e){
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();

        } catch (Exception e){
            Log.d("this", "Error starting camera preview: " + e.getMessage());
        }
    }
}
