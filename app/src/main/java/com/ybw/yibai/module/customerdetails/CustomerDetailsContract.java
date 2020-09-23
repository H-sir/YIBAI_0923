package com.ybw.yibai.module.customerdetails;

import android.content.Intent;
import android.view.View;

import com.ybw.yibai.base.BaseCallBack;
import com.ybw.yibai.base.BasePresenter;
import com.ybw.yibai.base.BaseView;
import com.ybw.yibai.common.bean.CustomersInfo;
import com.ybw.yibai.common.bean.DeleteCustomer;
import com.ybw.yibai.common.bean.EditCustomer;

/**
 * 合约模式,将三个Model,View,Presenter接口以及CallBack接口放在一个Contract接口里面方便统一管理
 *
 * @author sjl
 * @date 2019/10/25
 */
public interface CustomerDetailsContract {

    interface CustomerDetailsView extends BaseView {

        /**
         * PopupWindow里面的Item View被点击的时候回调
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
         * 在修改客户信息成功时回调
         *
         * @param editCustomer 在修改客户信息成功时回调服务器端返回的信息
         */
        void onEditCustomerSuccess(EditCustomer editCustomer);

        /**
         * 是否确定删除该客户
         *
         * @param result true确定删除该客户,false不删除该客户
         */
        void areYouSureToDeleteThisCustomer(boolean result);

        /**
         * 在删除客户成功时回调
         *
         * @param deleteCustomer 在删除客户成功时服务器端返回的数据
         */
        void onDeleteCustomerSuccess(DeleteCustomer deleteCustomer);
    }

    interface CustomerDetailsPresenter extends BasePresenter<CustomerDetailsView> {

        /**
         * 显示选择拍照还是从相册中选择的PopupWindow
         *
         * @param view View根布局
         */
        void displayPopupWindow(View view);

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
         * 修改客户信息
         *
         * @param customersInfo 客户信息
         */
        void editCustomer(CustomersInfo customersInfo);

        /**
         * 显示是否删除客户的Dialog
         */
        void displayDeleteCustomerDialog();

        /**
         * 删除客户
         *
         * @param cid 客户ID
         */
        void deleteCustomer(int cid);
    }

    interface CustomerDetailsModel {

        /**
         * 修改客户信息
         *
         * @param customersInfo 客户信息
         * @param callBack      回调方法
         */
        void editCustomer(CustomersInfo customersInfo, CallBack callBack);

        /**
         * 删除客户
         *
         * @param cid      客户ID
         * @param callBack 回调方法
         */
        void deleteCustomer(int cid, CallBack callBack);
    }

    interface CallBack extends BaseCallBack {

        /**
         * 在修改客户信息成功时回调
         *
         * @param editCustomer 在修改客户信息成功时回调服务器端返回的信息
         */
        void onEditCustomerSuccess(EditCustomer editCustomer);

        /**
         * 在删除客户成功时回调
         *
         * @param deleteCustomer 在删除客户成功时服务器端返回的数据
         */
        void onDeleteCustomerSuccess(DeleteCustomer deleteCustomer);
    }
}
