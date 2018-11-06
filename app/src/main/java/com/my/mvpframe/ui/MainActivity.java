package com.my.mvpframe.ui;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.my.mvpframe.R;
import com.my.mvpframe.appbase.BaseActivity;
import com.my.mvpframe.db.AppDatabase;
import com.my.mvpframe.db.User;
import com.my.mvpframe.db.UserDao;
import com.my.mvpframe.rxbus.RxBus;
import com.my.mvpframe.rxbus.RxBusConstants;
import com.my.mvpframe.ui.contract.MainContract;
import com.my.mvpframe.ui.presenter.MainPresenter;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

public class MainActivity extends BaseActivity<MainContract.View,MainPresenter> implements MainContract.View {

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
        textview.setOnClickListener(view -> {
            startActivity(new Intent(this,DynamicEffectActivity.class));
        });

        RxBus.getIntanceBus().register(RxBusConstants.TYPE_ONE, (Consumer<String>) o -> textview.setText(o));
        RxBus.getIntanceBus().register(RxBusConstants.TYPE_TWO, (Consumer<String>) o -> textview.setText(o));
        RxBus.getIntanceBus().register(RxBusConstants.TYPE_THREE, (Consumer<String>) o -> textview.setText(o));

        //新增的分支dev
    }

    @Override
    protected MainPresenter initPresenter() {
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

    @OnClick({R.id.textview1, R.id.textview2, R.id.textview3, R.id.textview4})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.textview1:// 添加第一组数据
                final UserDao userDao = AppDatabase.getInstance(this).userDao();
                final User user = new User(1,"Z","J","18",null);
                presenter.getInsertUser(userDao,user);
                RxBus.getIntanceBus().post(RxBusConstants.TYPE_ONE,"我是"+user.getFirstName().toString());
                break;
            case R.id.textview2:// 添加第二组数据
                UserDao userDao1 = AppDatabase.getInstance(this).userDao();
                User user1 = new User(2,"T","Y","18",null);
                presenter.getInsertUser(userDao1,user1);
                RxBus.getIntanceBus().post(RxBusConstants.TYPE_TWO,"我是"+user1.getFirstName().toString());
                break;
            case R.id.textview3:
                UserDao userDao2 = AppDatabase.getInstance(this).userDao();
                presenter.getAllUser(userDao2);
                RxBus.getIntanceBus().post(RxBusConstants.TYPE_THREE,"我是-------");
                break;
            case R.id.textview4:
                RxBus.getIntanceBus().unRegister(RxBusConstants.TYPE_ONE);

                break;
        }
    }
}
