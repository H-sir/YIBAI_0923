package com.ybw.yibai.module.watermarksetting;

import com.ybw.yibai.base.BaseCallBack;
import com.ybw.yibai.base.BasePresenter;
import com.ybw.yibai.base.BaseView;
import com.ybw.yibai.common.bean.UpdateWatermark;

import java.io.File;
import java.util.Map;

import okhttp3.RequestBody;

/**
 * 合约模式,将三个Model,View,Presenter接口以及CallBack接口放在一个Contract接口里面方便统一管理
 *
 * @author sjl
 * @date 2019/11/21
 */
public interface WatermarkSettingContract {

    interface WatermarkSettingView extends BaseView {

        /**
         * 在修改水印图片成功时回调
         *
         * @param updateWatermark 修改水印图片时服务器端返回的数据
         */
        void onUpdateWatermarkSuccess(UpdateWatermark updateWatermark);
    }

    interface WatermarkSettingPresenter extends BasePresenter<WatermarkSettingView> {

        /**
         * 修改水印图片
         *
         * @param file 水印图片
         */
        void updateWatermark(File file);
    }

    interface WatermarkSettingModel {

        /**
         * 修改水印图片
         *
         * @param params   水印图片
         * @param callBack 回调方法
         */
        void updateWatermark(Map<String, RequestBody> params, CallBack callBack);
    }

    interface CallBack extends BaseCallBack {

        /**
         * 在修改水印图片成功时回调
         *
         * @param updateWatermark 修改水印图片时服务器端返回的数据
         */
        void onUpdateWatermarkSuccess(UpdateWatermark updateWatermark);
    }
}
