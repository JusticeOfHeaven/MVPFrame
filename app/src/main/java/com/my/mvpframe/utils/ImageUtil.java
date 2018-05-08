package com.my.mvpframe.utils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

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
}
