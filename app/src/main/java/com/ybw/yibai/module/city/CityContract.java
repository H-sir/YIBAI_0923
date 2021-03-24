package com.ybw.yibai.module.city;

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
public interface CityContract {

    interface CityView extends BaseView {

        /**
         * 申请权限的结果
         *
         * @param b true 已经获取全部权限,false 没有获取全部权限
         */
        void applyPermissionsResults(boolean b);

        /**
         * 检查手机是否打开GPS功能的结果
         *
         * @param result true 已经打开GPS功能,false 没有打开GPS功能
         */
        void checkIfGpsOpenResult(boolean result);

        /**
         * 在设置货源城市成功时回调
         *
         * @param userPosition 设置货源城市时服务器端返回的数据
         */
        void onSetUserPositionSuccess(UserPosition userPosition);

        /**
         * 获取城市列表成功时回调
         * */
        void onGetCitySuccess(CityListBean cityListBean);

        void onGetLocationSuccess(PlaceBean placeBean);

        void onSetProductSuccess(EditUserInfo editUserInfo);

        void onGetMarketListSuccess(MarketListBean marketListBean);
    }

    interface CityPresenter extends BasePresenter<CityView> {

        /**
         * 申请权限
         *
         * @param permissions 要申请的权限列表
         */
        void applyPermissions(String[] permissions);

        /**
         * 检查手机是否打开GPS功能
         */
        void checkIfGpsOpen();

        /**
         * 显示打开GPS功能的Dialog
         *
         * @param rootView 页面根布局
         */
        void displayOpenGpsDialog(View rootView);

        /**
         * 设置货源城市
         *
         * @param position 城市字符串（广东省广州市）
         */
        void setUserPosition(String position);

        /**
         * 获取城市列表
         */
        void getCity();

        void getLocation(double latitude, double longitude);

        void onSetProduct(int comOpen);

        /**
         * 获取市场列表
         * */
        void getMarketList(double latitude, double longitude);

        void selectProductType(View view, String cityName);
    }

    interface CityModel {

        /**
         * 设置货源城市
         *
         * @param position 城市字符串（广东省广州市）
         * @param callBack 回调方法
         */
        void setUserPosition(String position, CallBack callBack);

        /**
         * 获取城市列表
         * */
        void getCity(CallBack callBack);

        void getLocation(double latitude, double longitude,CallBack callBack);

        void onSetProduct(int comOpen,CallBack callBack);

        void getMarketList(double latitude, double longitude, CallBack callBack);
    }

    interface CallBack extends BaseCallBack {

        /**
         * 在设置货源城市成功时回调
         *
         * @param userPosition 设置货源城市时服务器端返回的数据
         */
        void onSetUserPositionSuccess(UserPosition userPosition);
        /**
         * 获取城市列表成功时回调
         * */
        void onGetCitySuccess(CityListBean cityListBean);

        void onGetLocationSuccess(PlaceBean placeBean);

        void onSetProductSuccess(EditUserInfo editUserInfo);

        void onGetMarketListSuccess(MarketListBean marketListBean);
    }
}
