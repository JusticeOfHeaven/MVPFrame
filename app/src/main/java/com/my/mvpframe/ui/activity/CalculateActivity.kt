package com.my.mvpframe.ui.activity

import com.lucky.netlibrary.BasePresenter
import com.lucky.netlibrary.BaseView
import com.my.mvpframe.R
import com.my.mvpframe.appbase.BaseActivity
import com.my.mvpframe.custom.CalculateView
import kotlinx.android.synthetic.main.act_calculate.*

/**
 * Create by jzhan on 2019/3/2
 */
class CalculateActivity :BaseActivity<BaseView,BasePresenter<BaseView>>(), CalculateView.NotifyDataChanged {
    override fun onDataChanged(k: Float, b: Float, px: DoubleArray, y1: Float, y2: Float, percent: Float) {
        textInfo.text = "直线方程：y = $k*x "+(if (b>0) "+" else "-") + Math.abs(b)+"\n"+
                "交点X的坐标：\nX1 = ${px[0]}  \nX2 = ${px[1]}"+"\n"+
                "y1 = $y1   \ny2 = $y2"+"\n"+
                "百分比：$percent  \n"
    }

    override fun onDataChanged1(x1: Double, y1: Double, x2: Double, y2: Double, x: Float, y: Float) {
        textInfo1.text ="x1 = $x1\nx2 = $x2\ny1 = $y1\ny2 = $y2\nx = $x\ny = $y"
    }

    override fun getLayoutId(): Int = R.layout.act_calculate

    override fun initView() {
        calculateView.notifyDataChanged = this
    }
}