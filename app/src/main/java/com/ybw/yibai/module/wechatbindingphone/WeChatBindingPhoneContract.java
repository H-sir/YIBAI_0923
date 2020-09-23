package com.ybw.yibai.module.wechatbindingphone;

import android.widget.TextView;

import com.ybw.yibai.base.BaseCallBack;
import com.ybw.yibai.base.BasePresenter;
import com.ybw.yibai.base.BaseView;
import com.ybw.yibai.common.bean.VerificationCode;
import com.ybw.yibai.common.bean.WeChatBindingPhone;

/**
 * 合约模式,将三个Model,View,Presenter接口以及CallBack接口放在一个Contract接口里面方便统一管理
 *
 * @author sjl
 * @date 2019/10/17
 */
public interface WeChatBindingPhoneContract {

    interface WeChatBindingPhoneView extends BaseView {

        /**
         * 在获取验证码成功时回调
         *
         * @param verificationCode 获取验证码数据
         */
        void onGetVerificationCodeSuccess(VerificationCode verificationCode);

        /**
         * 在微信绑定手机号码成功时回调
         *
         * @param weChatBindingPhone 微信绑定手机时服务器端返回的数据
         */
        void onWeChatBindingPhoneSuccess(WeChatBindingPhone weChatBindingPhone);
    }

    interface WeChatBindingPhonePresenter extends BasePresenter<WeChatBindingPhoneView> {

        /**
         * 获取验证码
         *
         * @param getVerificationCodeTextView 获取验证码TextView
         * @param phoneNumber                 手机号/邮箱
         */
        void getVerificationCode(TextView getVerificationCodeTextView, String phoneNumber);

        /**
         * 微信绑定手机
         *
         * @param mobile  手机号码
         * @param code    微信给的code
         * @param unionId 当且仅当该移动应用已获得该用户的 userinfo 授权时,才会出现该字段
         * @param openid  授权用户唯一标识
         */
        void weChatBindingPhone(String mobile, String code, String unionId, String openid);
    }

    interface WeChatBindingPhoneModel {

        /**
         * 获取验证码
         *
         * @param phoneNumber 手机号
         * @param callBack    回调方法
         */
        void getVerificationCode(String phoneNumber, CallBack callBack);

        /**
         * 微信绑定手机
         *
         * @param mobile   手机号码
         * @param code     微信给的code
         * @param unionId  当且仅当该移动应用已获得该用户的 userinfo 授权时,才会出现该字段
         * @param openid   授权用户唯一标识
         * @param callBack 回调方法
         */
        void weChatBindingPhone(String mobile, String code, String unionId, String openid, CallBack callBack);
    }

    interface CallBack extends BaseCallBack {

        /**
         * 在获取验证码成功时回调
         *
         * @param verificationCode 获取验证码数据
         */
        void onGetVerificationCodeSuccess(VerificationCode verificationCode);

        /**
         * 在微信绑定手机号码成功时回调
         *
         * @param weChatBindingPhone 微信绑定手机时服务器端返回的数据
         */
        void onWeChatBindingPhoneSuccess(WeChatBindingPhone weChatBindingPhone);
    }
}
