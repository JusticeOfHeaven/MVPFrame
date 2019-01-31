package com.lucky.netlibrary;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;

/**
 * Created by ZJ on 2018/1/24.
 * Presenter的根父类
 *
 */

public abstract class BasePresenter<T> {
    //View接口类型的软引用
    protected Reference<T> mViewRef;

    public void attachView(T view) {
        //建立关系
        mViewRef = new SoftReference<T>(view);
    }

    public T getView() {
        if (mViewRef == null) {
            return null;
        }
        return mViewRef.get();
    }

    public boolean isViewAttached() {
        return mViewRef != null && mViewRef.get() != null;
    }

    public void detachView() {
        if (mViewRef != null) {
            mViewRef.clear();
        }
    }
}
