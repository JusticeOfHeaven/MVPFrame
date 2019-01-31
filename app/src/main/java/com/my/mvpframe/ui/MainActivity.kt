package com.my.mvpframe.ui

import android.content.Intent
import android.util.Log
import com.my.mvpframe.R
import com.my.mvpframe.appbase.BaseActivity
import com.my.mvpframe.bean.TabBean
import com.my.mvpframe.ui.contract.MainContract
import com.my.mvpframe.ui.presenter.MainPresenter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<MainContract.View, MainPresenter>(), MainContract.View {

    private var presenter: MainPresenter? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initView() {
        val listOf = listOf(
                TabBean("", R.drawable.unfinish)
        )
//        newAct.setOnClickListener { v -> startActivity(Intent(this, NewEffectActivity::class.java)) }
    }

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
