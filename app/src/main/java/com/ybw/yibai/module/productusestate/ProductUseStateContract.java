package com.ybw.yibai.module.productusestate;

import com.ybw.yibai.base.BaseCallBack;
import com.ybw.yibai.base.BasePresenter;
import com.ybw.yibai.base.BaseView;
import com.ybw.yibai.common.bean.ProductScreeningParam;

/**
 * 合约模式,将三个Model,View,Presenter接口以及CallBack接口放在一个Contract接口里面方便统一管理
 *
 * @author sjl
 * @date 2019/12/10
 */
public interface ProductUseStateContract {

    interface ProductUseStateView extends BaseView {

        /**
         * 在获取产品筛选参数成功时回调
         *
         * @param productScreeningParam 获取产品筛选参数时服务器端返回的数据
         */
        void onGetProductScreeningParamSuccess(ProductScreeningParam productScreeningParam);
    }

    interface ProductUseStatePresenter extends BasePresenter<ProductUseStateView> {

        /**
         * 获取产品筛选参数
         */
        void getProductScreeningParam();
    }

    interface ProductUseStateModel {

        /**
         * 获取产品筛选参数
         *
         * @param callBack 回调方法
         */
        void getProductScreeningParam(CallBack callBack);
    }

    interface CallBack extends BaseCallBack {

        /**
         * 在获取产品筛选参数成功时回调
         *
         * @param productScreeningParam 获取产品筛选参数时服务器端返回的数据
         */
        void onGetProductScreeningParamSuccess(ProductScreeningParam productScreeningParam);
    }
}
