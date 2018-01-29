package com.my.mvpframe.ui.presenter;

import com.my.mvpframe.bean.AppVersionBean;
import com.my.mvpframe.mvp.base.BaseView;
import com.my.mvpframe.rxjava.BaseObserver;
import com.my.mvpframe.rxjava.RxSchedulers;
import com.my.mvpframe.rxnet.HttpCenter;
import com.my.mvpframe.ui.contract.MainContract;

/**
 * Created by ZJ on 2018/1/29 0029.
 */

public class MainPresenter extends MainContract.Presenter {
    private MainContract.View mainView;

    public MainPresenter(BaseView baseView) {
        this.mainView = (MainContract.View) baseView;

    }

    @Override
    public void getAreaList() {
        HttpCenter.getInstance().service.getGameList()
                .compose(RxSchedulers.<AppVersionBean>compose())
                .subscribe(new BaseObserver<AppVersionBean>() {
                    @Override
                    protected void _onNext(AppVersionBean appVersionBean) {
                        mainView.showToast("成功");
                    }

                    @Override
                    protected void _onError(String message) {

                    }
                });
    }
}
