package com.ybw.yibai.module.purchase;

import com.ybw.yibai.base.BaseCallBack;
import com.ybw.yibai.base.BasePresenter;
import com.ybw.yibai.base.BaseView;
import com.ybw.yibai.common.bean.QuotationData;

import java.util.List;

/**
 * 合约模式,将三个Model,View,Presenter接口以及CallBack接口放在一个Contract接口里面方便统一管理
 *
 * @author sjl
 * @date 2019/11/5
 */
public interface PurchaseContract {

    interface PurchaseView extends BaseView {

        /**
         * 在获取用户保存的"报价"数据成功时回调
         *
         * @param quotationDataList 用户保存的"报价"数据列表
         */
        void onGetQuotationDataSuccess(List<QuotationData> quotationDataList);

        /**
         * 在更新用户保存的"报价"数据完成时回调
         *
         * @param result 结果 true成功/ false失败
         */
        void onUpdateQuotationDataFinish(boolean result);

        /**
         * 在删除用户保存的"报价"数据完成时回调
         *
         * @param result 结果 true成功/ false失败
         */
        void onDeleteQuotationDataFinish(boolean result);

        /**
         * 在计算总批发价格成功时回调
         *
         * @param totalTradePrice 总批发价格
         */
        void onGetTotalTradePriceSuccess(double totalTradePrice);
    }

    interface PurchasePresenter extends BasePresenter<PurchaseView> {

        /**
         * 获取用户保存的"报价"数据
         */
        void getQuotationData();

        /**
         * 更新用户保存的"报价"数据
         *
         * @param quotationData 需要更新的"报价"数据
         */
        void updateQuotationData(QuotationData quotationData);

        /**
         * 显示是否删除该盆栽
         *
         * @param quotationData 该盆栽数据
         */
        void displayAreYouSureToDeleteTheBonsaiDialog(QuotationData quotationData);

        /**
         * 计算总批发价格
         *
         * @param quotationDataList 用户保存的"报价"数据列表
         */
        void getTotalTradePrice(List<QuotationData> quotationDataList);
    }

    interface PurchaseModel {

        /**
         * 获取用户保存的"报价"数据
         *
         * @param callBack 回调方法
         */
        void getQuotationData(CallBack callBack);

        /**
         * 更新用户保存的"报价"数据
         *
         * @param quotationData 需要更新的"报价"数据
         * @param callBack      回调方法
         */
        void updateQuotationData(QuotationData quotationData, CallBack callBack);

        /**
         * 删除用户保存的"报价"数据
         *
         * @param quotationData 需要删除的"报价"数据
         * @param callBack      回调方法
         */
        void deleteQuotationData(QuotationData quotationData, CallBack callBack);
    }

    interface CallBack extends BaseCallBack {

        /**
         * 在获取用户保存的"报价"数据成功时回调
         *
         * @param quotationDataList 用户保存的"报价"数据列表
         */
        void onGetQuotationDataSuccess(List<QuotationData> quotationDataList);

        /**
         * 在更新用户保存的"报价"数据完成时回调
         *
         * @param result 结果 true成功/ false失败
         */
        void onUpdateQuotationDataFinish(boolean result);

        /**
         * 在删除用户保存的"报价"数据完成时回调
         *
         * @param result 结果 true成功/ false失败
         */
        void onDeleteQuotationDataFinish(boolean result);
    }
}
