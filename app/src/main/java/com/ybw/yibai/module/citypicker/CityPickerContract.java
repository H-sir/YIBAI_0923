package com.ybw.yibai.module.citypicker;

import com.ybw.yibai.base.BaseCallBack;
import com.ybw.yibai.base.BasePresenter;
import com.ybw.yibai.base.BaseView;
import com.ybw.yibai.common.bean.CityListBean;
import com.ybw.yibai.common.bean.CityListHotBean;

/**
 * 合约模式,将三个Model,View,Presenter接口以及CallBack接口放在一个Contract接口里面方便统一管理
 *
 * @author sjl
 * @date 2019/5/16
 */
public interface CityPickerContract {

    interface CityPickerView extends BaseView {

        void onGetCitySuccess(int type, CityListBean cityListBean);

        /**
         * 获取城市列表成功时回调(热门)
         */
        void onGetCityHotSuccess(int type, CityListHotBean cityListHotBean);
    }

    interface CityPickerPresenter extends BasePresenter<CityPickerView> {

        /**
         * 获取城市列表
         */
        void getCity(int type);
    }

    interface CityPickerModel {

        /**
         * 获取城市列表
         */
        void getCity(int type, CallBack callBack);

    }

    interface CallBack extends BaseCallBack {

        /**
         * 获取城市列表成功时回调
         */
        void onGetCitySuccess(int type, CityListBean cityListBean);

        /**
         * 获取城市列表成功时回调(热门)
         */
        void onGetCityHotSuccess(int type, CityListHotBean cityListHotBean);

    }
}
