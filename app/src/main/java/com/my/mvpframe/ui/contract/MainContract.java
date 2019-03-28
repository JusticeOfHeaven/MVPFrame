package com.my.mvpframe.ui.contract;

import com.my.mvpframe.db.User;
import com.my.mvpframe.db.UserDao;
import com.my.mvpframe.module_base.net.BasePresenter;
import com.my.mvpframe.module_base.net.BaseView;

/**
 * Created by ZJ on 2018/1/29 0029.
 * 统一管理view、presenter
 */

public class MainContract {
    /*定义view层的接口*/
    public interface View extends BaseView {
        void showTvResult();

        void showToast(String str);
    }
    /*定义p层的接口*/
    public abstract static class Presenter extends BasePresenter<View> {
        public abstract void getAreaList();
        public abstract void getInsertUser(UserDao userDao, User user);
    }

}
