package com.my.mvpframe.ui;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.reflect.TypeToken;
import com.my.mvpframe.R;
import com.my.mvpframe.appbase.BaseActivity;
import com.my.mvpframe.bean.DynamicBean;
import com.my.mvpframe.customview.BezierCurve.BezierCurveActivity;
import com.my.mvpframe.customview.BezierView1.DeleteActivity;
import com.my.mvpframe.customview.breathview.BreathActivity;
import com.my.mvpframe.customview.collsion.CollsionActivity;
import com.my.mvpframe.customview.delete.ScrollDeleteActivity;
import com.my.mvpframe.customview.pic_roll.RollViewActivity;
import com.my.mvpframe.customview.three_d_view.ThreeDViewActivity;
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
        List<DynamicBean> dynamicBeans = (List<DynamicBean>) JsonUtils.fromJson(json, new TypeToken<List<DynamicBean>>() {
        }.getType());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        recyclerView.addItemDecoration(new SpaceItemDecoration(this, 2, Color.RED));
        recyclerView.setAdapter(new BaseQuickAdapter<DynamicBean, BaseViewHolder>(R.layout.item_text, dynamicBeans) {

            @Override
            protected void convert(BaseViewHolder helper, DynamicBean item) {
                helper.setText(R.id.tvTitle, item.name);
                helper.setText(R.id.tvDescribe, item.description);

                helper.itemView.setOnClickListener(view -> onItemClick(item));
            }
        });
    }

    private void onItemClick(DynamicBean bean) {
        switch (bean.type) {
            case "0":
                startActivity(new Intent(this, ThreeDViewActivity.class));
                break;
            case "1":
                startActivity(new Intent(this, BezierCurveActivity.class));
                break;
            case "2":
                startActivity(new Intent(this, DeleteActivity.class));
                break;
            case "3":
                startActivity(new Intent(this, RollViewActivity.class));
                break;
            case "4":
                startActivity(new Intent(this, CollsionActivity.class));
                break;
            case "5":
                startActivity(new Intent(this, ScrollDeleteActivity.class));
                break;
            case "6":
                startActivity(new Intent(this, BreathActivity.class));
                break;
        }
    }

    private String getData() {
        try {
            InputStreamReader inputReader = new InputStreamReader(getResources().getAssets().open("DynamicData.json"));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String Result = "";
            while ((line = bufReader.readLine()) != null)
                Result += line;
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
