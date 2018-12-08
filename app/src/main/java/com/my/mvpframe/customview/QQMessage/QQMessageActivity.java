package com.my.mvpframe.customview.QQMessage;

import android.os.Build;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.my.mvpframe.R;
import com.my.mvpframe.appbase.BaseActivity;
import com.my.mvpframe.ui.MainActivity;

/**
 * Create by jzhan on 2018/12/6
 */
public class QQMessageActivity extends BaseActivity implements MsgXlListView.IXListViewListener{
    private TipsView tipsView;
    private MsgXlListView listView;

    @Override
    protected int getLayoutId() {
        return R.layout.act_qq_message;
    }

    @Override
    protected void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

            tipsView = (TipsView) findViewById(R.id.tipsView);
        }
        listView = (MsgXlListView) findViewById(R.id.listview);
        listView.setPullLoadEnable(false);
        InfoAdapter myAdapter = new InfoAdapter(this);
        listView.setAdapter(myAdapter);
        myAdapter.setListView(listView);
        myAdapter.setTipsView(tipsView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(QQMessageActivity.this, position + "", Toast.LENGTH_SHORT).show();
            }
        });
        listView.setXListViewListener(this);
    }

    @Override
    public void onRefresh() {
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                listView.stopRefresh();
            }
        },2000);
    }

    @Override
    public void onLoadMore() {

    }
}
