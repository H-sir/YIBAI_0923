package com.ybw.yibai.module.modifypassword;

import android.widget.TextView;

import com.ybw.yibai.base.BaseCallBack;
import com.ybw.yibai.base.BasePresenter;
import com.ybw.yibai.base.BaseView;
import com.ybw.yibai.common.bean.ModifyPassword;
import com.ybw.yibai.common.bean.VerificationCode;

/**
 * 合约模式,将三个Model,View,Presenter接口以及CallBack接口放在一个Contract接口里面方便统一管理
 *
 * @author sjl
 */
public interface ModifyPasswordContract {

    interface ModifyPasswordView extends BaseView {

        /**
         * 在获取验证码成功时回调
         *
         * @param verificationCode 验证码
         */
        void onGetVerificationCodeSuccess(VerificationCode verificationCode);

        /**
         * 在修改账号密码成功时回调
         *
         * @param modifyPassword 在修改账号密码成功时服务器端返回的数据
         */
        void onModifyPasswordSuccess(ModifyPassword modifyPassword);
    }

    interface ModifyPasswordPresenter extends BasePresenter<ModifyPasswordView> {

        /**
         * 获取验证码
         *
         * @param getVerificationCodeTextView 获取验证码TextView
         * @param phoneNumber                 手机号
         */
        void getVerificationCode(TextView getVerificationCodeTextView, String phoneNumber);

        /**
         * 修改密码
         *
         * @param phoneNumber          手机号
         * @param verificationCode     短信验证码
         * @param newPassword          新密码
         * @param determineNewPassword 确认密码
         */
        void modifyPassword(String phoneNumber, String verificationCode, String newPassword, String determineNewPassword);
    }

    interface ModifyPasswordModel {

        /**
         * 获取验证码
         *
         * @param phoneNumber 手机号
         * @param callBack    回调方法
         */
        void getVerificationCode(String phoneNumber, CallBack callBack);

        /**
         * 修改密码
         *
         * @param verificationCode 短信验证码
         * @param newPassword      新密码
         * @param callBack         回调方法
         */
        void modifyPassword(String verificationCode, String newPassword, CallBack callBack);
    }

    interface CallBack extends BaseCallBack {

        /**
         * 在获取验证码成功时回调
         *
         * @param verificationCode 验证码
         */
        void onGetVerificationCodeSuccess(VerificationCode verificationCode);

        /**
         * 在修改账号密码成功时回调
         *
         * @param modifyPassword 在修改账号密码成功时服务器端返回的数据
         */
        void onModifyPasswordSuccess(ModifyPassword modifyPassword);
    }
}
