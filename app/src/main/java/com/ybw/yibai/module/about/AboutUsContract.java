package com.ybw.yibai.module.about;

import com.ybw.yibai.base.BaseCallBack;
import com.ybw.yibai.base.BasePresenter;
import com.ybw.yibai.base.BaseView;
import com.ybw.yibai.common.bean.AppUpdate;

/**
 * 合约模式,将三个Model,View,Presenter接口以及CallBack接口放在一个Contract接口里面方便统一管理
 *
 * @author sjl
 */
public interface AboutUsContract {

    interface AboutUsView extends BaseView {

        /**
         * 请求应用更新成功时回调
         *
         * @param appUpdate 请求应用更新成功时服务器端返回的数据
         */
        void onAppUpdateSuccess(AppUpdate appUpdate);
    }

    interface AboutUsPresenter extends BasePresenter<AboutUsView> {

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
    }

    interface AboutUsModel {

        /**
         * 请求应用更新
         *
         * @param callBack 回调方法
         */
        void appUpdate(CallBack callBack);
    }

    interface CallBack extends BaseCallBack {

        /**
         * 请求应用更新成功时回调
         *
         * @param appUpdate 请求应用更新成功时服务器端返回的数据
         */
        void onAppUpdateSuccess(AppUpdate appUpdate);
    }
}
