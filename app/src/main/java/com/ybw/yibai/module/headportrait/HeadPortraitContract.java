package com.ybw.yibai.module.headportrait;

import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;

import com.ybw.yibai.base.BaseCallBack;
import com.ybw.yibai.base.BasePresenter;
import com.ybw.yibai.base.BaseView;
import com.ybw.yibai.common.bean.EditUserInfo;

import java.util.Map;

import okhttp3.RequestBody;

/**
 * 合约模式,将三个Model,View,Presenter接口以及CallBack接口放在一个Contract接口里面方便统一管理
 *
 * @author sjl
 */
public interface HeadPortraitContract {

    interface HeadPortraitView extends BaseView {

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
         * 修改用户图片成功时回调
         *
         * @param editUserInfo 修改用户基础信息时服务器端返回的数据
         */
        void onEditUserInfoSuccess(EditUserInfo editUserInfo);
    }

    interface HeadPortraitPresenter extends BasePresenter<HeadPortraitView> {

        /**
         * 初始化选择拍照还是从相册中选择的PopupWindow
         *
         * @param rootLayout View根布局
         */
        void displayPopupWindow(RelativeLayout rootLayout);

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
         * 修改用户图片
         *
         * @param path 图片路径
         */
        void editUserInfo(String path);
    }

    interface HeadPortraitModel {

        /**
         * 修改用户图片
         *
         * @param params   图片数据
         * @param callBack 回调方法
         */
        void editUserInfo(Map<String, RequestBody> params, CallBack callBack);
    }

    interface CallBack extends BaseCallBack {

        /**
         * 修改用户图片成功时回调
         *
         * @param editUserInfo 修改用户基础信息时服务器端返回的数据
         */
        void onEditUserInfoSuccess(EditUserInfo editUserInfo);
    }
}
