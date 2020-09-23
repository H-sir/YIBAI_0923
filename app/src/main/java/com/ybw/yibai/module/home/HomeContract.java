package com.ybw.yibai.module.home;

import android.view.View;

import com.ybw.yibai.base.BaseCallBack;
import com.ybw.yibai.base.BasePresenter;
import com.ybw.yibai.base.BaseView;
import com.ybw.yibai.common.bean.CustomerList;
import com.ybw.yibai.common.bean.HotScheme;
import com.ybw.yibai.common.bean.HotSchemeCategory;
import com.ybw.yibai.common.bean.HotSchemes;
import com.ybw.yibai.common.bean.RecommendProductList;
import com.ybw.yibai.common.bean.UserPosition;

/**
 * 合约模式,将三个Model,View,Presenter接口以及CallBack接口放在一个Contract接口里面方便统一管理
 *
 * @author sjl
 * @date 2019/5/16
 */
public interface HomeContract {

    interface HomeView extends BaseView {

        /**
         * 在获取客户列表成功时回调
         *
         * @param customersList 客户列表数据
         */
        void onGetCustomerListSuccess(CustomerList customersList);

        /**
         * 在获取热门方案分类成功时回调
         *
         * @param hotSchemeCategory 热门方案分类
         */
        void onGetHotSchemeCategorySuccess(HotSchemeCategory hotSchemeCategory);

        /**
         * 在获取热门方案列表成功时回调
         *
         * @param hotScheme 热门方案列表
         */
        void onGetHotSchemeSuccess(HotScheme hotScheme);

        /**
         * 在获取新热门方案列表成功时回调
         *
         * @param hotSchemes 新热门方案列表
         */
        void onGetHotSchemesSuccess(HotSchemes hotSchemes);

        /**
         * 在获取推荐产品列表成功时回调
         *
         * @param recommendProductList 推荐产品列表
         */
        void onGetRecommendProductListSuccess(RecommendProductList recommendProductList);

        /**
         * 是否切换货源的结果
         *
         * @param results true切换货源,false不切换货源
         */
        void whetherToSwitchResults(boolean results);

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
    }

    interface HomePresenter extends BasePresenter<HomeView> {

        /**
         * 获取客户列表
         */
        void getCustomerList();

        /**
         * 获取热门方案分类
         */
        void getHotSchemeCategory();

        /**
         * 获取热门方案列表
         *
         * @param cateId 方案分类id
         */
        void getHotScheme(int cateId);

        /**
         * 获取热门方案列表
         *
         * @param cateId 方案分类id
         */
        void getHotSchemes(int cateId);

        /**
         * 获取推荐产品列表
         */
        void getRecommendProductList();

        /**
         * 显示切换货源Dialog
         *
         * @param isNationwide 当前货源是否在全国范围
         */
        void displaySwitchSupplyOfGoodsDialog(boolean isNationwide);

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
    }

    interface HomeModel {

        /**
         * 获取客户列表
         *
         * @param callBack 回调方法
         */
        void getCustomerList(CallBack callBack);

        /**
         * 获取热门方案分类
         *
         * @param callBack 回调方法
         */
        void getHotSchemeCategory(CallBack callBack);

        /**
         * 获取热门方案列表
         *
         * @param cateId   方案分类id
         * @param callBack 回调方法
         */
        void getHotScheme(int cateId, CallBack callBack);

        /**
         * 获取新热门方案列表
         *
         * @param cateId   方案分类id
         * @param callBack 回调方法
         */
        void getHotSchemes(int cateId, CallBack callBack);

        /**
         * 获取推荐产品列表
         *
         * @param callBack 回调方法
         */
        void getRecommendProductList(CallBack callBack);

        /**
         * 设置货源城市
         *
         * @param position 城市字符串（广东省广州市）
         * @param callBack 回调方法
         */
        void setUserPosition(String position, CallBack callBack);
    }

    interface CallBack extends BaseCallBack {

        /**
         * 在获取客户列表成功时回调
         *
         * @param customersList 客户列表数据
         */
        void onGetCustomerListSuccess(CustomerList customersList);

        /**
         * 在获取热门方案分类成功时回调
         *
         * @param hotSchemeCategory 热门方案分类
         */
        void onGetHotSchemeCategorySuccess(HotSchemeCategory hotSchemeCategory);

        /**
         * 在获取热门方案列表成功时回调
         *
         * @param hotScheme 热门方案列表
         */
        void onGetHotSchemeSuccess(HotScheme hotScheme);

        /**
         * 在获取新热门方案列表成功时回调
         *
         * @param hotSchemes 新热门方案列表
         */
        void onGetHotSchemesSuccess(HotSchemes hotSchemes);

        /**
         * 在获取推荐产品列表成功时回调
         *
         * @param recommendProductList 推荐产品列表
         */
        void onGetRecommendProductListSuccess(RecommendProductList recommendProductList);

        /**
         * 在设置货源城市成功时回调
         *
         * @param userPosition 设置货源城市时服务器端返回的数据
         */
        void onSetUserPositionSuccess(UserPosition userPosition);
    }
}
