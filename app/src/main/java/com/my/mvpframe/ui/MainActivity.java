package com.my.mvpframe.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.my.mvpframe.R;
import com.my.mvpframe.appbase.BaseActivity;
import com.my.mvpframe.db.AppDatabase;
import com.my.mvpframe.db.User;
import com.my.mvpframe.db.UserDao;
import com.my.mvpframe.mvp.base.BasePresenter;
import com.my.mvpframe.ui.contract.MainContract;
import com.my.mvpframe.ui.presenter.MainPresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class MainActivity extends BaseActivity implements MainContract.View {

    @BindView(R.id.textview)
    TextView textview;
    @BindView(R.id.textview1)
    TextView textview1;
    private MainPresenter presenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        textview.setOnClickListener(view -> presenter.getAreaList());
    }

    @Override
    protected BasePresenter initPresenter() {
        presenter = new MainPresenter(this);
        return presenter;
    }

    @Override
    public void showErrorTip(String msg) {

    }

    @Override
    public void showTvResult() {

    }

    @Override
    public void showToast(String str) {
        Log.i("123", str);
    }

    @OnClick({R.id.textview1, R.id.textview2, R.id.textview3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.textview1:// 添加第一组数据
                final UserDao userDao = AppDatabase.getInstance(this).userDao();
                final User user = new User(1,"Z","J","18",null);
                presenter.getInsertUser(userDao,user);
                break;
            case R.id.textview2:// 添加第二组数据
                UserDao userDao1 = AppDatabase.getInstance(this).userDao();
                User user1 = new User(2,"T","Y","18",null);
                presenter.getInsertUser(userDao1,user1);
                break;
            case R.id.textview3:
                UserDao userDao2 = AppDatabase.getInstance(this).userDao();
                presenter.getAllUser(userDao2);
                break;
        }
    }
}
