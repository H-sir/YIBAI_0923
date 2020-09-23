package com.ybw.yibai.module.oldcustomer;

import com.ybw.yibai.base.BaseCallBack;
import com.ybw.yibai.base.BasePresenter;
import com.ybw.yibai.base.BaseView;
import com.ybw.yibai.common.bean.CustomerList;

/**
 * 合约模式,将三个Model,View,Presenter接口以及CallBack接口放在一个Contract接口里面方便统一管理
 *
 * @author sjl
 * @date 2019/9/16
 */
public interface OldCustomerContract {

    interface OldCustomerView extends BaseView {

        /**
         * 在获取客户列表成功时回调
         *
         * @param customersList 客户列表数据
         */
        void onGetCustomerListSuccess(CustomerList customersList);
    }

    interface OldCustomerPresenter extends BasePresenter<OldCustomerView> {

        /**
         * 获取客户列表
         */
        void getCustomerList();
    }

    interface OldCustomerModel {

        /**
         * 获取客户列表
         *
         * @param callBack 回调方法
         */
        void getCustomerList(CallBack callBack);
    }

    interface CallBack extends BaseCallBack {

        /**
         * 在获取客户列表成功时回调
         *
         * @param customersList 客户列表数据
         */
        void onGetCustomerListSuccess(CustomerList customersList);
    }
}
