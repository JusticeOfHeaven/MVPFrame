package com.my.mvpframe.appbase;

import android.app.Application;
import android.content.Context;

/**
 * Created by ZJ register 2018/1/30.
 *
 */

public class BaseApplication extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }
    public static Context getContext(){
        return mContext;
    }
}
