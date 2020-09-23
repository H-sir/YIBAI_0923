package com.ybw.yibai.module.confirmorder;

import com.ybw.yibai.base.BaseCallBack;
import com.ybw.yibai.base.BasePresenter;
import com.ybw.yibai.base.BaseView;
import com.ybw.yibai.common.bean.PurchaseOrder;
import com.ybw.yibai.common.bean.QuotationData;
import com.ybw.yibai.common.bean.Address;

import java.util.List;

/**
 * 合约模式,将三个Model,View,Presenter接口以及CallBack接口放在一个Contract接口里面方便统一管理
 *
 * @author sjl
 * @date 2019/5/16
 */
public interface ConfirmOrderContract {

    interface ConfirmOrderView extends BaseView {

        /**
         * 在计算总批发价格成功时回调
         *
         * @param totalTradePrice 总批发价格
         */
        void onGetTotalTradePriceSuccess(double totalTradePrice);

        /**
         * 在获取收货地址成功时回调
         *
         * @param shippingAddress 收货地址
         */
        void onGetShippingAddressSuccess(Address shippingAddress);

        /**
         * 在创建采购订单成功时回调
         *
         * @param purchaseOrder 创建采购订单时服务器端返回的数据
         */
        void onCreatePurchaseOrderSuccess(PurchaseOrder purchaseOrder);
    }

    interface ConfirmOrderPresenter extends BasePresenter<ConfirmOrderView> {

        /**
         * 计算总批发价格
         *
         * @param quotationDataList 用户保存的"报价"数据列表
         */
        void getTotalTradePrice(List<QuotationData> quotationDataList);

        /**
         * 获取收货地址
         */
        void getShippingAddress();

        /**
         * 创建采购订单
         *
         * @param quotationDataList 用户保存的"报价"数据列表
         * @param addressId         收货地址id
         */
        void createPurchaseOrder(List<QuotationData> quotationDataList, int addressId);
    }

    interface ConfirmOrderModel {

        /**
         * 获取收货地址
         *
         * @param callBack 回调方法
         */
        void getShippingAddress(CallBack callBack);

        /**
         * 创建采购订单
         *
         * @param json      商品信息
         * @param addressId 收货地址id
         * @param callBack  回调方法
         */
        void createPurchaseOrder(String json, int addressId, CallBack callBack);
    }

    interface CallBack extends BaseCallBack {

        /**
         * 在获取收货地址成功时回调
         *
         * @param shippingAddress 收货地址
         */
        void onGetShippingAddressSuccess(Address shippingAddress);

        /**
         * 在创建采购订单成功时回调
         *
         * @param purchaseOrder 创建采购订单时服务器端返回的数据
         */
        void onCreatePurchaseOrderSuccess(PurchaseOrder purchaseOrder);
    }
}
