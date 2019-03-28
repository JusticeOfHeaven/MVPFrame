package com.my.mvpframe.ui.home

import android.util.Log
import com.my.mvpframe.module_base.net.BasePresenter
import com.my.mvpframe.module_base.net.BaseView
import com.my.mvpframe.R
import com.my.mvpframe.appbase.BaseActivity
import com.my.mvpframe.appbase.BaseFragment
import com.my.mvpframe.bean.TabBean
import com.my.mvpframe.ui.contract.MainContract
import com.my.mvpframe.ui.home.fragment.FirstFragment
import com.my.mvpframe.ui.home.fragment.FourFragment
import com.my.mvpframe.ui.home.fragment.SecondFragment
import com.my.mvpframe.ui.home.fragment.ThirdFragment
import com.my.mvpframe.ui.presenter.MainPresenter
import com.my.mvpframe.widget.CommonTabLayout
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<MainContract.View, MainPresenter>(), MainContract.View, CommonTabLayout.OnTabSelectListener {

    private var presenter: MainPresenter? = null
    private var currentFragment: BaseFragment<com.my.mvpframe.module_base.net.BaseView, com.my.mvpframe.module_base.net.BasePresenter<com.my.mvpframe.module_base.net.BaseView>>? = null
    private val firstFragment by lazy { FirstFragment() }
    private val secondFragment by lazy { SecondFragment() }
    private val thirdFragment by lazy { ThirdFragment() }
    private val fourFragment by lazy { FourFragment() }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initPresenter(): MainPresenter? {
        presenter = MainPresenter(this)
        return presenter
    }

    override fun initView() {
        val listOf = listOf(
                TabBean("首页", R.drawable.icon_home_s,R.drawable.icon_home_uns),
                TabBean("界面2", R.drawable.unfinish, R.drawable.unfinish),
                TabBean("界面3", R.drawable.unfinish, R.drawable.unfinish),
                TabBean("我的", R.drawable.icon_my_s, R.drawable.icon_my_uns)
        )
        commontab.setTabData(listOf)
        commontab.setOnTabSelectListener(this)
        onTabSelect(0)
    }

    private fun switchFragment() {
        val fragments = supportFragmentManager.fragments
        fragments.forEach {
            supportFragmentManager.beginTransaction()
                    .hide(it)
                    .commitAllowingStateLoss()
        }
        if (currentFragment != null) {
            if (currentFragment in fragments) {
                supportFragmentManager.beginTransaction()
                        .show(currentFragment!!)
                        .commitAllowingStateLoss()
            } else {
                supportFragmentManager.beginTransaction()
                        .add(R.id.flContainer, currentFragment!!,"")
                        .addToBackStack(null)
                        .commitAllowingStateLoss()
            }
        }
    }

    override fun onTabSelect(position: Int) {
        currentFragment = when (position) {
            0 -> firstFragment
            1 -> secondFragment
            2 -> thirdFragment
            3 -> fourFragment
            else -> firstFragment
        }
        switchFragment()
    }

    override fun showErrorTip(msg: String) {

    }

    override fun showTvResult() {

    }

    override fun showToast(str: String) {
        Log.i("123", str)
    }
}
