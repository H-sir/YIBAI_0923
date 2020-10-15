package com.ybw.yibai.module.purcart;

import com.ybw.yibai.base.BaseCallBack;
import com.ybw.yibai.base.BasePresenter;
import com.ybw.yibai.base.BaseView;
import com.ybw.yibai.common.bean.PurCartBean;

/**
 * 合约模式,将三个Model,View,Presenter接口以及CallBack接口放在一个Contract接口里面方便统一管理
 *
 * @author sjl
 * @date 2019/11/5
 */
public interface PurCartContract {

    interface PurCartView extends BaseView {

        void onGetPurCartDataSuccess(PurCartBean purCartBean);

        void onUpdateCartGateSuccess(int num);
    }

    interface PurCartPresenter extends BasePresenter<PurCartView> {

        /**
         * 获取用户的进货信息
         */
        void getPurCartData();

        /**
         * 修改购物车信息
         */
        void updateCartGate(int cartId, int num);

        /**
         * 修改购物车信息
         */
        void updateCartGateCheck(int cartId, int check);
    }

    interface PurCartModel {

        /**
         * 获取用户的进货信息
         */
        void getPurCartData(CallBack callBack);

        void updateCartGate(int cartId, int num, CallBack callBack);

        void updateCartGateCheck(int cartId, int check, CallBack callBack);
    }

    interface CallBack extends BaseCallBack {

        void onGetPurCartDataSuccess(PurCartBean purCartBean);

        void onUpdateCartGateSuccess(int num);
    }
}
