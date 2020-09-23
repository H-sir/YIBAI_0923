package com.ybw.yibai.module.main;

import android.content.Intent;
import android.view.View;

import com.ybw.yibai.base.BaseCallBack;
import com.ybw.yibai.base.BasePresenter;
import com.ybw.yibai.base.BaseView;
import com.ybw.yibai.common.bean.AppUpdate;
import com.ybw.yibai.common.bean.ProductScreeningParam;
import com.ybw.yibai.common.bean.SystemParameter;
import com.ybw.yibai.common.bean.UserInfo;

import java.io.File;

/**
 * 合约模式,将三个Model,View,Presenter接口以及CallBack接口放在一个Contract接口里面方便统一管理
 *
 * @author sjl
 * @date 2018/12/10
 */
public interface MainContract {

    interface MainView extends BaseView {

        /**
         * 请求应用更新成功时回调
         *
         * @param appUpdate 请求应用更新成功时服务器端返回的数据
         */
        void onAppUpdateSuccess(AppUpdate appUpdate);

        /**
         * 在获取用户信息成功时回调
         *
         * @param userInfo 用户信息
         */
        void onGetUserInfoSuccess(UserInfo userInfo);

        /**
         * 在获取系统参数成功时回调
         *
         * @param systemParameter 系统参数
         */
        void onGetSystemParameterSuccess(SystemParameter systemParameter);

        /**
         * 在获取产品筛选参数成功时回调
         *
         * @param productScreeningParam 获取产品筛选参数时服务器端返回的数据
         */
        void onGetProductScreeningParamSuccess(ProductScreeningParam productScreeningParam);

        /**
         * 申请权限的结果
         *
         * @param b true 已经获取全部权限,false 没有获取全部权限
         */
        void applyPermissionsResults(boolean b);

        /**
         * 返回从相机或相册返回的图像
         *
         * @param file 图像文件
         */
        void returnsTheImageReturnedFromTheCameraOrAlbum(File file);
    }

    interface MainPresenter extends BasePresenter<MainView> {

        /**
         * 初始化XUtils数据库和一些其他数据
         */
        void initDataAndXUtilsDataBase();

        /**
         * 查找是否有默认场景,如果没有就创建一个默认创建
         */
        void findIfThereIsDefaultScene();

        /**
         * 应用更新
         */
        void appUpdate();

        /**
         * 下载新版本App
         *
         * @param appUpdate 服务器新版本信息
         */
        void downloadApp(AppUpdate appUpdate);

        /**
         * 获取用户信息
         */
        void getUserInfo();

        /**
         * 获取系统参数
         */
        void getSystemParameter();

        /**
         * 获取产品筛选参数
         */
        void getProductScreeningParam();

        /**
         * 申请权限
         *
         * @param permissions 要申请的权限列表
         */
        void applyPermissions(String[] permissions);

        /**
         * 打开相机
         */
        void openCamera();

        /**
         * 打开相册
         */
        void openPhotoAlbum();

        /**
         * 获得Activity返回的数据
         *
         * @param requestCode 请求代码
         * @param resultCode  结果代码
         * @param data        返回的数据
         */
        void onActivityResult(int requestCode, int resultCode, Intent data);

        /**
         * 显示升级会员的PopupWindow
         *
         * @param rootLayout View根布局
         */
        void displayUpdateVipPopupWindow(View rootLayout);
    }

    interface MainModel {

        /**
         * 查找是否有默认场景,如果没有就创建一个默认创建
         */
        void findIfThereIsDefaultScene();

        /**
         * 请求应用更新
         *
         * @param callBack 回调方法
         */
        void appUpdate(CallBack callBack);

        /**
         * 获取用户信息
         *
         * @param callBack 回调方法
         */
        void getUserInfo(CallBack callBack);

        /**
         * 获取系统参数
         *
         * @param callBack 回调方法
         */
        void getSystemParameter(CallBack callBack);

        /**
         * 获取产品筛选参数
         *
         * @param callBack 回调方法
         */
        void getProductScreeningParam(CallBack callBack);
    }

    interface CallBack extends BaseCallBack {

        /**
         * 请求应用更新成功时回调
         *
         * @param appUpdate 请求应用更新成功时服务器端返回的数据
         */
        void onAppUpdateSuccess(AppUpdate appUpdate);

        /**
         * 在获取用户信息成功时回调
         *
         * @param userInfo 用户信息
         */
        void onGetUserInfoSuccess(UserInfo userInfo);

        /**
         * 在获取系统参数成功时回调
         *
         * @param systemParameter 系统参数
         */
        void onGetSystemParameterSuccess(SystemParameter systemParameter);

        /**
         * 在获取产品筛选参数成功时回调
         *
         * @param productScreeningParam 获取产品筛选参数时服务器端返回的数据
         */
        void onGetProductScreeningParamSuccess(ProductScreeningParam productScreeningParam);
    }
}
