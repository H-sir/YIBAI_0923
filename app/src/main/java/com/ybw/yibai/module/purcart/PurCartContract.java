package com.ybw.yibai.module.purcart;

import android.widget.LinearLayout;

import com.ybw.yibai.base.BaseCallBack;
import com.ybw.yibai.base.BasePresenter;
import com.ybw.yibai.base.BaseView;
import com.ybw.yibai.common.bean.PurCartBean;

import java.util.List;

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
        void onUpAllCartSuccess(int isCheck);

        void applyPermissionsResults(boolean b);

        void checkIfGpsOpenResult(boolean gpsOpenResult);

        void onDeleteSuccess();
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

        /**
         * 全选或者全不选
         * */
        void upAllCart(String cartIds, int type, int isCheck);

        void applyPermissions(String[] permissions);

        void checkIfGpsOpen();

        void displayOpenGpsDialog(LinearLayout rootLayout);
    }

    interface PurCartModel {

        /**
         * 获取用户的进货信息
         */
        void getPurCartData(CallBack callBack);

        void updateCartGate(int cartId, int num, CallBack callBack);

        void updateCartGateCheck(int cartId, int check, CallBack callBack);

        void upAllCart(String cartIds, int type, int isCheck, CallBack callBack);
    }

    interface CallBack extends BaseCallBack {

        void onGetPurCartDataSuccess(PurCartBean purCartBean);

        void onUpdateCartGateSuccess(int num);

        void onUpAllCartSuccess(int isCheck);

        void onDeleteSuccess();
    }
}
