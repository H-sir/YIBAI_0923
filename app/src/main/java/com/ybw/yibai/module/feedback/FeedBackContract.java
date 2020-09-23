package com.ybw.yibai.module.feedback;

import android.content.Intent;
import android.view.View;

import com.ybw.yibai.base.BaseCallBack;
import com.ybw.yibai.base.BasePresenter;
import com.ybw.yibai.base.BaseView;
import com.ybw.yibai.common.bean.FeedBack;
import com.ybw.yibai.common.bean.FeedBackImage;

import java.io.File;
import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.http.Part;

/**
 * 合约模式,将三个Model,View,Presenter接口以及CallBack接口放在一个Contract接口里面方便统一管理
 *
 * @author sjl
 */
public interface FeedBackContract {

    interface FeedBackView extends BaseView {

        /**
         * 申请权限的结果
         *
         * @param b true 已经获取全部权限,false 没有获取全部权限
         */
        void applyPermissionsResults(boolean b);

        /**
         * 在选择拍照还是从相册中选择的PopupWindow里面的Item View被点击的时候回调
         *
         * @param v 被点击的view
         */
        void onPopupWindowItemClick(View v);

        /**
         * 返回从相机或相册返回的图像
         *
         * @param file 图像文件
         */
        void returnsTheImageReturnedFromTheCameraOrAlbum(File file);

        /**
         * 在意见反馈成功时回调
         *
         * @param feedBack 意见反馈数据
         */
        void onFeedBackSuccess(FeedBack feedBack);
    }

    interface FeedBackPresenter extends BasePresenter<FeedBackView> {

        /**
         * 申请权限
         *
         * @param permissions 要申请的权限列表
         */
        void applyPermissions(String[] permissions);

        /**
         * 初始化选择拍照还是从相册中选择的PopupWindow
         *
         * @param rootLayout View根布局
         */
        void displayTakePhotoOrAlbumPopupWindow(View rootLayout);

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
         * 意见反馈
         *
         * @param desc               建议或问题描述
         * @param contact            联系手机或者联系邮箱
         * @param mFeedBackImageList 描述图片
         */
        void feedBack(String desc, String contact, List<FeedBackImage> mFeedBackImageList);
    }

    interface FeedBackModel {

        /**
         * 意见反馈
         *
         * @param desc     建议或问题描述
         * @param parts    描述图片
         * @param contact  联系手机或者联系邮箱
         * @param callBack 回调方法
         */
        void feedBack(String desc, String contact, @Part MultipartBody.Part[] parts, CallBack callBack);
    }

    interface CallBack extends BaseCallBack {

        /**
         * 在意见反馈成功时回调
         *
         * @param feedBack 意见反馈数据
         */
        void onFeedBackSuccess(FeedBack feedBack);
    }
}