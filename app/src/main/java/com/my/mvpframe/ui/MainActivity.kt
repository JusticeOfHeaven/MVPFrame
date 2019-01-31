package com.my.mvpframe.ui

import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.TextView

import com.my.mvpframe.R
import com.my.mvpframe.appbase.BaseActivity
import com.my.mvpframe.db.AppDatabase
import com.my.mvpframe.db.User
import com.my.mvpframe.db.UserDao
import com.my.mvpframe.rxbus.RxBus
import com.my.mvpframe.rxbus.RxBusConstants
import com.my.mvpframe.ui.contract.MainContract
import com.my.mvpframe.ui.presenter.MainPresenter

import butterknife.BindView
import butterknife.OnClick
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<MainContract.View, MainPresenter>(), MainContract.View {

//    @BindView(R.id.textview)
//    internal var textview: TextView? = null
//    @BindView(R.id.textview1)
//    internal var textview1: TextView? = null
//    @BindView(R.id.newAct)
//    internal var newAct: TextView? = null
    private var presenter: MainPresenter? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initView() {
//        textview.setOnClickListener { view -> startActivity(Intent(this, DynamicEffectActivity::class.java)) }

//        RxBus.getIntanceBus().register(RxBusConstants.TYPE_ONE, { o -> textview!!.setText(o) } as Consumer<String>)
//        RxBus.getIntanceBus().register(RxBusConstants.TYPE_TWO, { o -> textview!!.setText(o) } as Consumer<String>)
//        RxBus.getIntanceBus().register(RxBusConstants.TYPE_THREE, { o -> textview!!.setText(o) } as Consumer<String>)

        newAct.setOnClickListener { v -> startActivity(Intent(this, NewEffectActivity::class.java)) }
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

    @OnClick(R.id.textview1, R.id.textview2, R.id.textview3, R.id.textview4)
    fun onViewClicked(view: View) {
        when (view.id) {
            R.id.textview1// 添加第一组数据
            -> {
                val userDao = AppDatabase.getInstance(this).userDao()
                val user = User(1, "Z", "J", "18", null)
                presenter!!.getInsertUser(userDao, user)
                RxBus.getIntanceBus().post(RxBusConstants.TYPE_ONE, "我是" + user.firstName.toString())
            }
            R.id.textview2// 添加第二组数据
            -> {
                val userDao1 = AppDatabase.getInstance(this).userDao()
                val user1 = User(2, "T", "Y", "18", null)
                presenter!!.getInsertUser(userDao1, user1)
                RxBus.getIntanceBus().post(RxBusConstants.TYPE_TWO, "我是" + user1.firstName.toString())
            }
            R.id.textview3 -> {
                val userDao2 = AppDatabase.getInstance(this).userDao()
                presenter!!.getAllUser(userDao2)
                RxBus.getIntanceBus().post(RxBusConstants.TYPE_THREE, "我是-------")
            }
            R.id.textview4 -> RxBus.getIntanceBus().unRegister(RxBusConstants.TYPE_ONE)
        }
    }
}
