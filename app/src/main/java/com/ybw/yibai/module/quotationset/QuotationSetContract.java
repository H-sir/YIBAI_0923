package com.ybw.yibai.module.quotationset;

import com.ybw.yibai.base.BaseCallBack;
import com.ybw.yibai.base.BasePresenter;
import com.ybw.yibai.base.BaseView;
import com.ybw.yibai.common.bean.SetIncrease;

/**
 * 合约模式,将三个Model,View,Presenter接口以及CallBack接口放在一个Contract接口里面方便统一管理
 *
 * @author sjl
 * @date 2019/12/2
 */
public interface QuotationSetContract {

    interface QuotationSetView extends BaseView {

        /**
         * 在设置设置价格增幅成功时回调
         *
         * @param setIncrease 设置价格增幅时服务器端返回的数据
         */
        void onSetIncreaseSuccess(SetIncrease setIncrease);
    }

    interface QuotationSetPresenter extends BasePresenter<QuotationSetView> {

        /**
         * 设置价格增幅
         *
         * @param increaseRent 租价幅度%
         * @param increaseSell 售价幅度%
         */
        void setIncrease(String increaseRent, String increaseSell);
    }

    interface QuotationSetModel {

        /**
         * 设置价格增幅
         *
         * @param increaseRent 租价幅度%
         * @param increaseSell 售价幅度%
         * @param callBack     回调方法
         */
        void setIncrease(String increaseRent, String increaseSell, CallBack callBack);
    }

    interface CallBack extends BaseCallBack {

        /**
         * 在设置设置价格增幅成功时回调
         *
         * @param setIncrease 设置价格增幅时服务器端返回的数据
         */
        void onSetIncreaseSuccess(SetIncrease setIncrease);
    }
}
