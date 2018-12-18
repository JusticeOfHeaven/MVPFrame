package com.my.mvpframe.customview.recyclerView;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.my.mvpframe.R;
import com.my.mvpframe.appbase.BaseActivity;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Create by jzhan on 2018/12/12
 */
public class ReActivity extends BaseActivity {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private LinearLayoutManager layout;
    private BaseQuickAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.act_recycler;
    }

    @Override
    protected void initView() {
//        new Recyc
        layout = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layout);

        ArrayList<Object> data = createData();

        adapter = new BaseQuickAdapter(R.layout.item_text, data) {
            @Override
            protected void convert(BaseViewHolder helper, Object item) {
                helper.setText(R.id.tvTitle, "第" + (helper.getLayoutPosition() + 1) + "个");
                helper.setText(R.id.tvDescribe, "");
            }
        };
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (adapter.getData().size() - layout.findLastVisibleItemPosition() == 20) {
                    adapter.addData(createData());
//                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    private ArrayList<Object> createData() {
        ArrayList<Object> list = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            list.add("");
        }
        return list;
    }
}
