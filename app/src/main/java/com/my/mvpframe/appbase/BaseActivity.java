package com.my.mvpframe.appbase;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.my.mvpframe.mvp.base.BasePresenter;
import com.my.mvpframe.mvp.base.BaseView;
import com.my.mvpframe.rxbus.RxBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by ZJ on 2018/1/24.
 *
 */

public abstract class BaseActivity<V, T extends BasePresenter<V>> extends AppCompatActivity{
    public String TAG = getClass().getSimpleName() + "";
    private T mPresenter;
    public Context mContext;
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        /*将Activity加入栈进行管理*/
        AppManager.getAppManager().addActivity(this);
        initActivityView(savedInstanceState);
        unbinder = ButterKnife.bind(this);
        mContext = BaseActivity.this;
        //创建presenter
        mPresenter = initPresenter();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null != mPresenter) {
            mPresenter.attachView((V) this);
        }
    }

    /**
     * 关于Activity的界面填充的抽象方法，需要子类必须实现
     * 有需求可重写
     */
    protected void initActivityView(Bundle savedInstanceState){

    };

    /**
     *  页面布局
     */
    protected abstract int getLayoutId();
    /**
     * 加载页面元素
     */
    protected abstract void initView();
    /**
     * 创建Presenter 对象,需要用的重写
     */
    protected  T initPresenter(){
        return null;
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (null != mPresenter) {
            mPresenter.detachView();
        }
        unbinder.unbind();
        AppManager.getAppManager().finishActivity(this);
    }
}
