package com.ybw.yibai.module.startdesign;

import android.content.Intent;
import android.view.View;

import com.ybw.yibai.base.BasePresenter;
import com.ybw.yibai.base.BaseView;

import java.io.File;
import java.util.List;

/**
 * 合约模式,将三个Model,View,Presenter接口以及CallBack接口放在一个Contract接口里面方便统一管理
 *
 * @author sjl
 * @date 2019/11/12
 */
public interface StartDesignContract {

    interface StartDesignView extends BaseView {

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

        /**
         * 在显示预设场景名称的RecyclerView的Item被点击时回调
         *
         * @param name 被点击的Item位置的场景名称
         */
        void onPresetSceneNameItemClick(String name);
    }

    interface StartDesignPresenter extends BasePresenter<StartDesignView> {

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
         * 显示场景名称列表的PopupWindow
         *
         * @param rootLayout    界面Root布局
         * @param sceneNameList 预设场景名称列表
         * @param oldSceneName  用户点击"选择设计的背景图"位置之前显示的场景名称
         */
        void displaySceneNamePopupWindow(View rootLayout, List<String> sceneNameList, String oldSceneName);
    }
}
