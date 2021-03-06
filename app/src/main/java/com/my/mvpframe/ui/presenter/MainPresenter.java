package com.my.mvpframe.ui.presenter;

import com.my.mvpframe.bean.AppVersionBean;
import com.my.mvpframe.db.User;
import com.my.mvpframe.db.UserDao;
import com.my.mvpframe.module_base.net.BaseView;
import com.my.mvpframe.module_base.net.BaseObserver;
import com.my.mvpframe.module_base.net.BaseSubscriber;
import com.my.mvpframe.module_base.net.RxSchedulers;
import com.my.mvpframe.rxnet.Re;
import com.my.mvpframe.ui.contract.MainContract;

import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;
//import io.reactivex.android.schedulers.AndroidSchedulers;


/**
 * Created by ZJ register 2018/1/29 0029.
 */

public class MainPresenter extends MainContract.Presenter {
    private MainContract.View mainView;

    public MainPresenter(BaseView baseView) {
        this.mainView = (MainContract.View) baseView;

    }

    @Override
    public void getAreaList() {
        Re.Companion.newInstance().getService().getGameList()
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

    @Override
    public void getInsertUser(final UserDao userDao, final User user) {
        Flowable.create((FlowableOnSubscribe<Boolean>) emitter -> {
            userDao.insertOrUpdata(user);
            emitter.onNext(true);
            emitter.onComplete();
        }, BackpressureStrategy.BUFFER)
                .compose(RxSchedulers.<Boolean>composeFlowable())
                .subscribe(new BaseSubscriber<Boolean>() {

                    @Override
                    protected void _onNext(Boolean aBoolean) {
                        mainView.showToast("数据更新成功");
                    }

                    @Override
                    protected void _onError(String message) {
                        mainView.showToast("数据更新失败");
                    }
                });
    }

    public void getAllUser(final UserDao userDao) {
        Flowable.create((FlowableOnSubscribe<List<User>>) emitter -> {
            List<User> all = userDao.getAll();
            emitter.onNext(all);
            emitter.onComplete();
        }, BackpressureStrategy.BUFFER)
                .compose(RxSchedulers.<List<User>>composeFlowable())
                .subscribe(new BaseSubscriber<List<User>>() {

                    @Override
                    protected void _onNext(List<User> all) {
                        mainView.showToast(all.toString());
                    }

                    @Override
                    protected void _onError(String message) {
                        mainView.showToast("数据更新失败");
                    }
                });
    }
}
