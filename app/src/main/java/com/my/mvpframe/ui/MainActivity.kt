package com.my.mvpframe.ui

import android.content.Intent
import android.util.Log
import com.lucky.netlibrary.BasePresenter
import com.lucky.netlibrary.BaseView
import com.my.mvpframe.R
import com.my.mvpframe.appbase.BaseActivity
import com.my.mvpframe.appbase.BaseFragment
import com.my.mvpframe.bean.TabBean
import com.my.mvpframe.ui.contract.MainContract
import com.my.mvpframe.ui.presenter.MainPresenter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<MainContract.View, MainPresenter>(), MainContract.View {

    private var presenter: MainPresenter? = null
    private var currentFragment: BaseFragment<BaseView,BasePresenter<BaseView>>? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initView() {
        val listOf = listOf(
                TabBean("界面1", R.drawable.unfinish),
                TabBean("界面2", R.drawable.unfinish),
                TabBean("界面3", R.drawable.unfinish),
                TabBean("界面4", R.drawable.unfinish)
        )
        commontab.setTabData(listOf)
//        newAct.setOnClickListener { v -> startActivity(Intent(this, NewEffectActivity::class.java)) }
    }

//    private fun switchFragment() {
//        val fragments = supportFragmentManager.fragments
//        fragments.forEach {
//            supportFragmentManager.beginTransaction()
//                    .hide(it)
//                    .commitAllowingStateLoss()
//        }
//
//        if (currentFragment in fragments) {
//            supportFragmentManager.beginTransaction()
//                    .show(currentFragment)
//                    .commitAllowingStateLoss()
//        } else {
//            supportFragmentManager.beginTransaction()
//                    .add(R.id.flContainer, currentFragment, currentCheckType)
//                    .addToBackStack(null)
//                    .commitAllowingStateLoss()
//        }
//    }

    override fun initPresenter(): MainPresenter? {
        presenter = MainPresenter(this)
        return presenter
    }

    override fun showErrorTip(msg: String) {

    }

    override fun showTvResult() {

    }

    override fun showToast(str: String) {
        Log.i("123", str)
    }
}
