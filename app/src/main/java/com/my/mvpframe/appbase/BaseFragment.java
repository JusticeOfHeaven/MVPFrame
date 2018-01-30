package com.my.mvpframe.appbase;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.my.mvpframe.mvp.base.BasePresenter;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by ZJ on 2018/1/30.
 *
 */

public abstract class BaseFragment<V, T extends BasePresenter<V>> extends Fragment {
    private View rootView;
    private Unbinder unbinder;
    private T mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 缓存view
        if (rootView == null)
            rootView = inflater.inflate(getLayoutId(), container, false);
        unbinder = ButterKnife.bind(this, rootView);
        //创建presenter
        mPresenter = initPresenter();
        initView();
        return rootView;
    }
    /**
     * 加载页面元素
     */
    protected abstract void initView();
    /**
     * 创建Presenter 对象
     */
    protected abstract T initPresenter();
    /**
     *  页面布局
     */
    protected abstract int getLayoutId();

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (null != mPresenter) {
            mPresenter.detachView();
        }
        unbinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (null != mPresenter) {
            mPresenter.attachView((V) this);
        }
    }
}
