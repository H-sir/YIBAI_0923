package com.ybw.yibai.module.change;

import android.view.View;

import com.ybw.yibai.base.BaseCallBack;
import com.ybw.yibai.base.BasePresenter;
import com.ybw.yibai.base.BaseView;
import com.ybw.yibai.common.bean.CityListBean;
import com.ybw.yibai.common.bean.EditUserInfo;
import com.ybw.yibai.common.bean.MarketListBean;
import com.ybw.yibai.common.bean.PlaceBean;
import com.ybw.yibai.common.bean.UserPosition;

/**
 * 合约模式,将三个Model,View,Presenter接口以及CallBack接口放在一个Contract接口里面方便统一管理
 *
 * @author sjl
 * @date 2019/5/16
 */
public interface ChangeAddressContract {

    interface ChangeAddressView extends BaseView {

        void onGetCitySuccess(CityListBean cityListBean);
    }

    interface ChangeAddressPresenter extends BasePresenter<ChangeAddressView> {

        /**
         * 获取城市列表
         */
        void getCity();
    }

    interface ChangeAddressModel {

        /**
         * 获取城市列表
         */
        void getCity(CallBack callBack);

    }

    interface CallBack extends BaseCallBack {

        /**
         * 获取城市列表成功时回调
         */
        void onGetCitySuccess(CityListBean cityListBean);

    }
}
