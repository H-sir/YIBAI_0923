package com.ybw.yibai.module.welcome;

import com.ybw.yibai.base.BasePresenter;
import com.ybw.yibai.base.BaseView;

/**
 * 合约模式,将三个Model,View,Presenter接口以及CallBack接口放在一个Contract接口里面方便统一管理
 *
 * @author sjl
 * @date 2018/12/10
 */
public interface WelcomeContract {

    interface WelComeView extends BaseView {

        /**
         * 申请权限的结果
         *
         * @param b true 已经获取全部权限,false 没有获取全部权限
         */
        void applyPermissionsResults(boolean b);

        /**
         * 跳转到登陆界面
         */
        void startLoginActivity();

        /**
         * 跳转到主界面
         */
        void startMainActivity();
    }

    interface WelComePresenter extends BasePresenter<WelComeView> {

        /**
         * 申请权限
         *
         * @param permissions 要申请的权限列表
         */
        void applyPermissions(String[] permissions);

        /**
         * 获取用户的Token判断用户是不是已经登录过了
         *
         * @param token 用户登陆时获得的token
         */
        void getToken(String token);
    }
}
