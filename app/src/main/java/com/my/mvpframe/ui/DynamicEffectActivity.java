package com.my.mvpframe.ui;

import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.reflect.TypeToken;
import com.my.mvpframe.R;
import com.my.mvpframe.appbase.BaseActivity;
import com.my.mvpframe.bean.DynamicBean;
import com.my.mvpframe.utils.JsonUtils;
import com.my.mvpframe.widget.SpaceItemDecoration;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import butterknife.BindView;

/**
 * Created by jzhan on 2018/11/6.
 * 各种动效界面
 **/
public class DynamicEffectActivity extends BaseActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Override
    protected int getLayoutId() {
        return R.layout.act_dynamic;
    }

    @Override
    protected void initView() {
        String json = getData();
        List<DynamicBean> dynamicBeans = (List<DynamicBean>) JsonUtils.fromJson(json, new TypeToken<List<DynamicBean>>(){}.getType());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        recyclerView.addItemDecoration(new SpaceItemDecoration(this, 2,Color.RED));
        recyclerView.setAdapter(new BaseQuickAdapter<DynamicBean,BaseViewHolder>(R.layout.item_text,dynamicBeans) {

            @Override
            protected void convert(BaseViewHolder helper, DynamicBean item) {
                helper.setText(R.id.tvTitle,item.name);
            }
        });
    }

    private String getData() {
        try {
            InputStreamReader inputReader = new InputStreamReader( getResources().getAssets().open("DynamicData.json") );
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line="";
            String Result="";
            while((line = bufReader.readLine()) != null)
                Result += line;
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
