package com.my.mvpframe.appbase;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.my.mvpframe.module_base.net.BasePresenter;


//import butterknife.ButterKnife;
//import butterknife.Unbinder;

/**
 * Created by ZJ on 2018/1/24.
 *
 */

public abstract class BaseActivity<V, T extends BasePresenter<V>> extends AppCompatActivity{
    public String TAG = getClass().getSimpleName() + "";
    private T mPresenter;
    public Context mContext;
//    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        /*将Activity加入栈进行管理*/
        AppManager.getAppManager().addActivity(this);
        initActivityView(savedInstanceState);
//        unbinder = ButterKnife.bind(this);
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
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (null != mPresenter) {
            mPresenter.detachView();
        }
//        unbinder.unbind();
        AppManager.getAppManager().finishActivity(this);
    }

    /**
     * 修改状态栏颜色，支持4.4以上版本
     *
     * @param colorId
     */
    public void setStatusBarColor(int colorId) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(colorId));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //使用SystemBarTint库使4.4版本状态栏变色，需要先将状态栏设置为透明
//            transparencyBar(activity);
//            SystemBarTintManager tintManager = new SystemBarTintManager(activity);
//            tintManager.setStatusBarTintEnabled(true);
//            tintManager.setStatusBarTintResource(colorId);
        }
    }
}
