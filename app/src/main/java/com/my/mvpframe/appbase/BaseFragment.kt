package com.my.mvpframe.appbase

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


import com.lucky.netlibrary.BasePresenter

//import butterknife.ButterKnife;
//import butterknife.Unbinder;

/**
 * Created by ZJ on 2018/1/30.
 *
 */

abstract class BaseFragment<V, T : BasePresenter<V>> : Fragment() {
    private var rootView: View? = null
    //    private Unbinder unbinder;
    private var mPresenter: T? = null
    protected var mContext:Activity ? = null

    /**
     * 页面布局
     */
    protected abstract val layoutId: Int

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mContext = activity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // 缓存view
        if (rootView == null)
            rootView = inflater.inflate(layoutId, container, false)
        //        unbinder = ButterKnife.bind(this, rootView);
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //创建presenter
        initPresenter()
        initView()
    }

    /**
     * 创建Presenter 对象
     */
    protected open fun initPresenter(){

    }

    /**
     * 加载页面元素
     */
    protected abstract fun initView()

    override fun onDestroyView() {
        super.onDestroyView()

        if (null != mPresenter) {
            mPresenter!!.detachView()
        }
        //        unbinder.unbind();
    }

    override fun onResume() {
        super.onResume()
        if (null != mPresenter) {
            mPresenter!!.attachView(this as V)
        }
    }
}
