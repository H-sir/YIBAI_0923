package com.ybw.yibai.module.shippingaddress;

import com.ybw.yibai.base.BaseCallBack;
import com.ybw.yibai.base.BasePresenter;
import com.ybw.yibai.base.BaseView;
import com.ybw.yibai.common.bean.Address;

/**
 * 合约模式,将三个Model,View,Presenter接口以及CallBack接口放在一个Contract接口里面方便统一管理
 *
 * @author sjl
 * @date 2019/10/11
 */
public interface ShippingAddressContract {

    interface ShippingAddressView extends BaseView {

        /**
         * 在获取收货地址成功时回调
         *
         * @param shippingAddress 收获地址
         */
        void onGetShippingAddressSuccess(Address shippingAddress);
    }

    interface ShippingAddressPresenter extends BasePresenter<ShippingAddressView> {

        /**
         * 获取收货地址
         */
        void getShippingAddress();
    }

    interface ShippingAddressModel {

        /**
         * 获取收货地址
         *
         * @param callBack 回调方法
         */
        void getShippingAddress(CallBack callBack);
    }

    interface CallBack extends BaseCallBack {

        /**
         * 在获取收货地址成功时回调
         *
         * @param shippingAddress 收获地址
         */
        void onGetShippingAddressSuccess(Address shippingAddress);
    }
}
