package com.ybw.yibai.module.quotationdetails;

import android.view.View;

import com.ybw.yibai.base.BaseCallBack;
import com.ybw.yibai.base.BasePresenter;
import com.ybw.yibai.base.BaseView;
import com.ybw.yibai.common.bean.QuotationAgain;
import com.ybw.yibai.common.bean.QuotationList;

/**
 * 合约模式,将三个Model,View,Presenter接口以及CallBack接口放在一个Contract接口里面方便统一管理
 *
 * @author sjl
 * @date 2019/09/28
 */
public interface QuotationDetailsContract {

    interface QuotationDetailsView extends BaseView {

        /**
         * 在获取报价列表数据成功时回调
         *
         * @param quotationList 报价列表
         */
        void onGetQuotationListSuccess(QuotationList quotationList);

        /**
         * 在报价单再次报价成功时回调
         *
         * @param quotationAgain 报价单再次报价时服务器端返回的数据
         */
        void onQuotationAgainSuccess(QuotationAgain quotationAgain);

        /**
         * 用户是否确定继续报价的结果
         *
         * @param result true继续 , false不继续
         */
        void continueQuoting(boolean result);
    }

    interface QuotationDetailsPresenter extends BasePresenter<QuotationDetailsView> {

        /**
         * 获取报价列表
         *
         * @param type  0/空获取所有,1待客户确定,2待跟进3已成交4已失效
         * @param isAll 0分页1不分页默认为1
         */
        void getQuotationList(int type, int isAll);

        /**
         * 报价单再次报价
         *
         * @param orderNumber 订单编号
         * @param clearOk     是否清除未完成报价
         */
        void quotationAgain(String orderNumber, int clearOk);

        /**
         * 初始化选择"分享"的PopupWindow
         *
         * @param rootLayout     View根布局
         * @param clientName     客户名称
         * @param orderNumber    订单号
         * @param qrCodeFilePath 二维码路径
         */
        void displaySharePopupWindow(View rootLayout, String clientName, String orderNumber, String qrCodeFilePath);

        /**
         * 初始化选择"提示"的PopupWindow
         *
         * @param rootLayout View根布局
         * @param title      标题
         * @param content    内容
         */
        void displayHintPopupWindow(View rootLayout, String title, String content);
    }

    interface QuotationDetailsModel {

        /**
         * 获取报价列表
         *
         * @param type     0/空获取所有,1待客户确定,2待跟进,3已成交,4已失效
         * @param isAll    0分页1不分页默认为1
         * @param callBack 回调方法
         */
        void getQuotationList(int type, int isAll, CallBack callBack);

        /**
         * 报价单再次报价
         *
         * @param orderNumber 订单编号
         * @param clearOk     是否清除未完成报价
         * @param callBack    回调方法
         */
        void quotationAgain(String orderNumber, int clearOk, CallBack callBack);
    }

    interface CallBack extends BaseCallBack {

        /**
         * 在获取报价列表数据成功时回调
         *
         * @param quotationList 报价列表
         */
        void onGetQuotationListSuccess(QuotationList quotationList);

        /**
         * 在报价单再次报价成功时回调
         *
         * @param quotationAgain 报价单再次报价时服务器端返回的数据
         */
        void onQuotationAgainSuccess(QuotationAgain quotationAgain);
    }
}
