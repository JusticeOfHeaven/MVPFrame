package com.my.mvpframe.ui.activity

//import com.chad.library.adapter.base.BaseQuickAdapter
//import com.chad.library.adapter.base.BaseViewHolder
import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.google.gson.reflect.TypeToken
import com.my.mvpframe.module_base.net.BasePresenter
import com.my.mvpframe.module_base.net.BaseView
import com.my.mvpframe.R
import com.my.mvpframe.appbase.BaseActivity
import com.my.mvpframe.bean.NewEffectBean
import com.my.mvpframe.customview.tablayout.TabActivity
import com.my.mvpframe.utils.FileUtils
import com.my.mvpframe.utils.ImageLoaderUtils
import com.my.mvpframe.utils.JsonUtils
import com.my.mvpframe.widget.DividerGridItemDecoration
import kotlinx.android.synthetic.main.act_new_effect.*

/**
 * Create by jzhan on 2019/1/28
 */
class NewEffectActivity : BaseActivity<com.my.mvpframe.module_base.net.BaseView, com.my.mvpframe.module_base.net.BasePresenter<com.my.mvpframe.module_base.net.BaseView>>() {
    lateinit var adapter: MyAdapter
    inline fun <reified T> genericType() = object : TypeToken<T>() {}.type

    override fun getLayoutId(): Int = R.layout.act_new_effect

    override fun initView() {
        adapter = MyAdapter()
        recyclerView.layoutManager = GridLayoutManager(this, 3)
        recyclerView.adapter = adapter
        // 设置ItemDecoration
        recyclerView.addItemDecoration(DividerGridItemDecoration(this, resources.getColor(R.color.transparent)))

        val type = genericType<List<NewEffectBean>>()
        val dynamicBeans = JsonUtils.fromJson<List<NewEffectBean>>(FileUtils.readFromAsset(this, "NewEffectData.json"), type)

        adapter.addData(dynamicBeans)
    }


    inner class MyAdapter : BaseQuickAdapter<NewEffectBean, BaseViewHolder>(R.layout.item_new_effect) {
        override fun convert(helper: BaseViewHolder, item: NewEffectBean) {
            helper.setText(R.id.tvTitle, item.description)
            helper.setImageResource(R.id.isFinish, if (item.isFinish) R.drawable.finish else R.drawable.unfinish)

            val view = helper.getView<ImageView>(R.id.ivGif)
            ImageLoaderUtils.display(mContext, view, item.url)

            helper.itemView.setOnClickListener {
                when (item.id) {
                    0->startActivity(Intent(this@NewEffectActivity, TabActivity::class.java))
                    1->{}
                    2->{startActivity(Intent(this@NewEffectActivity, ViewPagerActivity::class.java))}
                    3->{startActivity(Intent(this@NewEffectActivity,DragViewActivity::class.java))}
                }

            }
        }
    }
}


