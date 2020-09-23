package com.ybw.yibai.module.setting;

import com.ybw.yibai.base.BaseCallBack;
import com.ybw.yibai.base.BasePresenter;
import com.ybw.yibai.base.BaseView;
import com.ybw.yibai.common.bean.BindingWechat;

/**
 * 合约模式,将三个Model,View,Presenter接口以及CallBack接口放在一个Contract接口里面方便统一管理
 *
 * @author sjl
 * @date 2019/10/25
 */
public interface SettingContract {

    interface SettingView extends BaseView {

        /**
         * 在绑定微信成功时回调
         *
         * @param bindingWechat 在绑定微信成功时服务器端返回的数据
         */
        void onBindingWechatSuccess(BindingWechat bindingWechat);
    }

    interface SettingPresenter extends BasePresenter<SettingView> {

        /**
         * 绑定微信
         *
         * @param code 微信给的code
         */
        void bindingWechat(String code);

        /**
         * 显示切换账号的Dialog
         */
        void displaySwitchAccountDialog();
    }

    interface SettingModel {

        /**
         * 绑定微信
         *
         * @param code     微信给的code
         * @param callBack 回调方法
         */
        void bindingWechat(String code, CallBack callBack);
    }

    interface CallBack extends BaseCallBack {

        /**
         * 在绑定微信成功时回调
         *
         * @param bindingWechat 在绑定微信成功时服务器端返回的数据
         */
        void onBindingWechatSuccess(BindingWechat bindingWechat);
    }
}
