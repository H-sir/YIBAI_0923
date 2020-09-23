package com.ybw.yibai.module.newuser;

import android.content.Intent;
import android.view.View;

import com.ybw.yibai.base.BaseCallBack;
import com.ybw.yibai.base.BasePresenter;
import com.ybw.yibai.base.BaseView;
import com.ybw.yibai.common.bean.CustomersInfo;
import com.ybw.yibai.common.bean.CreateCustomers;

/**
 * 合约模式,将三个Model,View,Presenter接口以及CallBack接口放在一个Contract接口里面方便统一管理
 *
 * @author sjl
 * @date 2019/9/19
 */
public interface NewUserContract {

    interface NewUserView extends BaseView {

        /**
         * 在选择拍照还是从相册中选择的PopupWindow里面的Item View被点击的时候回调
         *
         * @param v 被点击的view
         */
        void onPopupWindowItemClick(View v);

        /**
         * 申请权限的结果
         *
         * @param b true 已经获取全部权限,false 没有获取全部权限
         */
        void applyPermissionsResults(boolean b);

        /**
         * 在用户完成图片裁剪时
         *
         * @param imagePath 裁剪后的图片路径
         */
        void onCropImageFinish(String imagePath);

        /**
         * 在创建客户信息成功时回调
         *
         * @param createCustomers 客户信息
         */
        void onNewCustomersSuccess(CreateCustomers createCustomers);
    }

    interface NewUserPresenter extends BasePresenter<NewUserView> {

        /**
         * 显示选择拍照还是从相册中选择的PopupWindow
         *
         * @param rootLayout View根布局
         */
        void displayPopupWindow(View rootLayout);

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
         * 创建客户
         *
         * @param clientInfo 客户信息
         */
        void newCustomers(CustomersInfo clientInfo);
    }

    interface NewUserModel {

        /**
         * 创建客户
         *
         * @param clientInfo 客户信息
         * @param callBack   回调方法
         */
        void newCustomers(CustomersInfo clientInfo, CallBack callBack);
    }

    interface CallBack extends BaseCallBack {

        /**
         * 在创建客户信息成功时回调
         *
         * @param createCustomers 客户信息
         */
        void onNewCustomersSuccess(CreateCustomers createCustomers);
    }
}
