package com.ybw.yibai.module.login;

import android.widget.TextView;

import com.ybw.yibai.base.BaseCallBack;
import com.ybw.yibai.base.BasePresenter;
import com.ybw.yibai.base.BaseView;
import com.ybw.yibai.common.bean.LoginInfo;
import com.ybw.yibai.common.bean.VerificationCode;

/**
 * 合约模式,将三个Model,View,Presenter接口以及CallBack接口放在一个Contract接口里面方便统一管理
 *
 * @author sjl
 * @date 2018/12/10
 */
public interface LoginContract {

    interface LoginView extends BaseView {

        /**
         * 在获取验证码成功时回调
         *
         * @param verificationCode 获取验证码数据
         */
        void onGetVerificationCodeSuccess(VerificationCode verificationCode);

        /**
         * 在请求登录成功时回调
         *
         * @param loginInfo 登陆数据
         */
        void onLoginSuccess(LoginInfo loginInfo);
    }

    interface LoginPresenter extends BasePresenter<LoginView> {

        /**
         * 获取验证码
         *
         * @param getVerificationCodeTextView 获取验证码TextView
         * @param phoneNumber                 手机号/邮箱
         */
        void getVerificationCode(TextView getVerificationCodeTextView, String phoneNumber);

        /**
         * 请求登录
         *
         * @param account  用户名/手机号
         * @param password 用户密码
         * @param isRandom 是否是验证码登陆(1是0否)(验证码登陆的没有账号则会自动生成账号)
         */
        void login(String account, String password, int isRandom);

        /**
         * 微信登陆
         *
         * @param code 微信给的code
         */
        void wechatLogin(String code);
    }

    interface LoginModel {

        /**
         * 获取验证码
         *
         * @param phoneNumber 手机号
         * @param callBack    回调方法
         */
        void getVerificationCode(String phoneNumber, CallBack callBack);

        /**
         * 请求登录
         *
         * @param account  用户名/手机号
         * @param password 用户密码/验证码
         * @param isRandom 是否是验证码登陆(1是0否)(验证码登陆的没有账号则会自动生成账号)
         * @param callBack 回调方法
         */
        void login(String account, String password, int isRandom, CallBack callBack);

        /**
         * 微信登陆
         *
         * @param code     微信给的code
         * @param callBack 回调方法
         */
        void wechatLogin(String code, CallBack callBack);
    }

    interface CallBack extends BaseCallBack {

        /**
         * 在获取验证码成功时回调
         *
         * @param verificationCode 获取验证码数据
         */
        void onGetVerificationCodeSuccess(VerificationCode verificationCode);

        /**
         * 在请求登录成功时回调
         *
         * @param loginInfo 登陆数据
         */
        void onLoginSuccess(LoginInfo loginInfo);
    }
}
