package com.ybw.yibai.module.purchaseorder;

import com.ybw.yibai.base.BaseCallBack;
import com.ybw.yibai.base.BasePresenter;
import com.ybw.yibai.base.BaseView;
import com.ybw.yibai.common.bean.PurchaseOrderList;

/**
 * 合约模式,将三个Model,View,Presenter接口以及CallBack接口放在一个Contract接口里面方便统一管理
 *
 * @author sjl
 * @date 2019/10/14
 */
public interface PurchaseOrderContract {

    interface PurchaseOrderView extends BaseView {

        /**
         * 在获取获取采购单列表成功时回调
         *
         * @param purchaseOrderList 采购单列表
         */
        void onGetPurchaseOrderListSuccess(PurchaseOrderList purchaseOrderList);
    }

    interface PurchaseOrderPresenter extends BasePresenter<PurchaseOrderView> {

        /**
         * 获取获取采购单列表
         *
         * @param isAll 0分页1不分页默认为1
         * @param state 状态类型: 0/空获取所有,1待付款,2待配送3待收货4已完成
         */
        void getPurchaseOrderList(int isAll, int state);
    }

    interface PurchaseOrderModel {

        /**
         * 获取获取采购单列表
         *
         * @param isAll    0分页1不分页默认为1
         * @param state    状态类型: 0/空获取所有,1待付款,2待配送3待收货4已完成
         * @param callBack 回调方法
         */
        void getPurchaseOrderList(int isAll, int state, CallBack callBack);
    }

    interface CallBack extends BaseCallBack {

        /**
         * 在获取获取采购单列表成功时回调
         *
         * @param purchaseOrderList 采购单列表
         */
        void onGetPurchaseOrderListSuccess(PurchaseOrderList purchaseOrderList);
    }
}
