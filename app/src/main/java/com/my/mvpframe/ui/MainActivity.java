package com.my.mvpframe.ui;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.my.mvpframe.R;
import com.my.mvpframe.appbase.BaseActivity;
import com.my.mvpframe.mvp.base.BasePresenter;
import com.my.mvpframe.ui.contract.MainContract;
import com.my.mvpframe.ui.presenter.MainPresenter;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements MainContract.View {

    @BindView(R.id.textview)
    TextView textview;
    private MainPresenter presenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.getAreaList();
            }
        });

    }

    @Override
    protected BasePresenter initPresenter() {
        presenter = new MainPresenter(this);
        return presenter;
    }

    @Override
    protected void initData() {

    }

    @Override
    public void showErrorTip(String msg) {

    }

    @Override
    public void showTvResult() {

    }

    @Override
    public void showToast(String str) {
        Log.i("123",str);
    }
}
