package com.my.mvpframe.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;

import com.bumptech.glide.BitmapTypeRequest;
import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutionException;

/**
 * Created by ZJ on 2018/5/8
 * 保存图片到本地
 */

public class ImageUtil {
    /*获取图片*/
    public static File getImage(){
        File appDir = new File(Environment.getExternalStorageDirectory(),"portraitImage");
        String fileName = "portraitImage" + ".png";
        return new File(appDir, fileName);
    }
    /*图片是否存在*/
    public static boolean hasImage(){
        File appDir = new File(Environment.getExternalStorageDirectory(),"portraitImage");
        String fileName = "portraitImage" + ".png";
        File file = new File(appDir, fileName);
        if (!file.exists()) {
            return false;
        }
        return true;
    }
    /*保存图片*/
    public static void saveImage(String path){
        Bitmap mBitmap = returnBitMap(path);
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(),"portraitImage");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = "portraitImage" + ".png";
        File file = new File(appDir, fileName);
        try {
//            File file = new File(dir + fileName + ".jpg");
            FileOutputStream out = new FileOutputStream(file);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        // 把文件插入到系统图库
//        try {
//            MediaStore.Images.Media.insertImage(this.getContentResolver(), file.getAbsolutePath(), fileName, null);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
        // 通知图库更新
//        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + "/sdcard/namecard/")));
    }

    // 这个url只能是本地文件的url，网络url会报NetworkOnMainThreadException异常，即不能在主线程中进行网络请求
    private static Bitmap returnBitMap(String url){
        URL myFileUrl = null;
        Bitmap bitmap = null;
        try {
            myFileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     *  获取倒影图片
     */
    public static Bitmap getReverseBitmapByUrl(String url,Context context){
        Bitmap bitmap = null;
        try {
            bitmap = Glide.with(context).load(url).asBitmap().into(500,500).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return getBitmap(bitmap);
    }

    public static Bitmap getReverseBitmapById(int resId, Context context){
        Bitmap sourceBitmap= BitmapFactory.decodeResource(context.getResources(),resId);
        return getBitmap(sourceBitmap);
    }

    @NonNull
    private static Bitmap getBitmap(Bitmap sourceBitmap) {
        //绘制原图的下一半图片
        Matrix matrix=new Matrix();
        //倒影翻转
        matrix.setScale(1,-1);

        Bitmap inverseBitmap=Bitmap.createBitmap(sourceBitmap,0,sourceBitmap.getHeight()/2,sourceBitmap.getWidth(),sourceBitmap.getHeight()/3,matrix,false);
        //合成图片
        Bitmap groupbBitmap=Bitmap.createBitmap(sourceBitmap.getWidth(),sourceBitmap.getHeight()+sourceBitmap.getHeight()/3+60,sourceBitmap.getConfig());
        //以合成图片为画布
        Canvas gCanvas=new Canvas(groupbBitmap);
        //将原图和倒影图片画在合成图片上
        gCanvas.drawBitmap(sourceBitmap,0,0,null);
        gCanvas.drawBitmap(inverseBitmap,0,sourceBitmap.getHeight()+50,null);
        //添加遮罩
        Paint paint=new Paint();
        Shader.TileMode tileMode= Shader.TileMode.CLAMP;
        LinearGradient shader=new LinearGradient(0,sourceBitmap.getHeight()+50,0,
                groupbBitmap.getHeight(), Color.BLACK,Color.TRANSPARENT,tileMode);
        paint.setShader(shader);
        //这里取矩形渐变区和图片的交集
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        gCanvas.drawRect(0,sourceBitmap.getHeight()+50,sourceBitmap.getWidth(),groupbBitmap.getHeight(),paint);
        return groupbBitmap;
    }
}
