package com.my.mvpframe.ui

import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.google.gson.reflect.TypeToken
import com.my.mvpframe.R
import com.my.mvpframe.appbase.BaseActivity
import com.my.mvpframe.bean.DynamicBean
import com.my.mvpframe.customview.tablayout.TabActivity
import com.my.mvpframe.mvp.base.BasePresenter
import com.my.mvpframe.mvp.base.BaseView
import com.my.mvpframe.utils.FileUtils
import com.my.mvpframe.utils.ImageLoaderUtils
import com.my.mvpframe.utils.JsonUtils
import kotlinx.android.synthetic.main.act_new_effect.*
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Create by jzhan on 2019/1/28
 */
class NewEffectActivity : BaseActivity<BaseView, BasePresenter<BaseView>>() {
    var dataList = ArrayList<String>()
    var url = "http://www.apkbus.com/data/attachment/forum/201901/17/103835v88uenapnen8w8ve.gif"
    lateinit var adapter: MyAdapter


    override fun getLayoutId(): Int {
        return R.layout.act_new_effect
    }

    inline fun <reified T> genericType() = object : TypeToken<T>() {}.type
    override fun initView() {
        adapter = MyAdapter()
        recyclerView.adapter = adapter

        val json = getData()
        val type = genericType<List<DynamicBean>>()
        val dynamicBeans = JsonUtils.fromJson<List<DynamicBean>>(json, type)
        recyclerView.layoutManager = GridLayoutManager(this, 3)

        adapter.addData(dynamicBeans)
    }


    inner class MyAdapter : BaseQuickAdapter<DynamicBean, BaseViewHolder>(R.layout.item_text_1) {
        override fun convert(helper: BaseViewHolder?, item: DynamicBean?) {
            val view = helper?.getView<ImageView>(R.id.ivGif)
            ImageLoaderUtils.display(mContext, view, item?.url)

            helper?.itemView?.setOnClickListener {
                startActivity(Intent(this@NewEffectActivity, TabActivity::class.java))
            }
        }
    }

    private fun getData(): String {
        try {
            val open = resources.assets.open("NewEffectData.json")
            val inputReader = InputStreamReader(open)
            val bufReader = BufferedReader(inputReader)
            var Result = ""

            while (bufReader.ready()) {
                val readLine = bufReader.readLine()
                Log.d("TAG", "readLine = $readLine")
                Result += readLine
            }
            return Result
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ""
    }
}


