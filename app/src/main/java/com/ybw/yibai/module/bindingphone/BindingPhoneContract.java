package com.ybw.yibai.module.bindingphone;

import android.widget.TextView;

import com.ybw.yibai.base.BaseCallBack;
import com.ybw.yibai.base.BasePresenter;
import com.ybw.yibai.base.BaseView;
import com.ybw.yibai.common.bean.BindingPhone;
import com.ybw.yibai.common.bean.VerificationCode;

/**
 * 合约模式,将三个Model,View,Presenter接口以及CallBack接口放在一个Contract接口里面方便统一管理
 *
 * @author sjl
 */
public interface BindingPhoneContract {

    interface BindingPhoneView extends BaseView {

        /**
         * 在获取验证码成功时回调
         *
         * @param verificationCode 验证码
         */
        void onGetVerificationCodeSuccess(VerificationCode verificationCode);

        /**
         * 在绑定手机号成功时回调
         *
         * @param bindingPhone 在绑定手机号成功时服务器端返回的数据
         */
        void onBindingPhoneSuccess(BindingPhone bindingPhone);
    }

    interface BindingPhonePresenter extends BasePresenter<BindingPhoneView> {

        /**
         * 获取验证码
         *
         * @param getVerificationCodeTextView 获取验证码的TextView
         * @param phoneNumber                 手机号
         */
        void getVerificationCode(TextView getVerificationCodeTextView, String phoneNumber);

        /**
         * 绑定手机号
         *
         * @param phoneNumber      手机号
         * @param verificationCode 短信验证码
         */
        void bindingPhone(String phoneNumber, String verificationCode);
    }

    interface BindingPhoneModel {

        /**
         * 获取验证码
         *
         * @param phoneNumber 手机号
         * @param callBack    回调方法
         */
        void getVerificationCode(String phoneNumber, CallBack callBack);

        /**
         * 绑定手机号
         *
         * @param phoneNumber      手机号
         * @param verificationCode 短信验证码
         * @param callBack         回调方法
         */
        void bindingPhone(String phoneNumber, String verificationCode, CallBack callBack);
    }

    interface CallBack extends BaseCallBack {

        /**
         * 在获取验证码成功时回调
         *
         * @param verificationCode 验证码
         */
        void onGetVerificationCodeSuccess(VerificationCode verificationCode);

        /**
         * 在绑定手机号成功时回调
         *
         * @param bindingPhone 在绑定手机号成功时服务器端返回的数据
         */
        void onBindingPhoneSuccess(BindingPhone bindingPhone);
    }
}
