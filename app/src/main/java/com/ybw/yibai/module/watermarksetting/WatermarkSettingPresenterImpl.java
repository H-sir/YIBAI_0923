package com.ybw.yibai.module.watermarksetting;

import com.ybw.yibai.base.BasePresenterImpl;
import com.ybw.yibai.common.bean.UpdateWatermark;
import com.ybw.yibai.module.watermarksetting.WatermarkSettingContract.CallBack;
import com.ybw.yibai.module.watermarksetting.WatermarkSettingContract.WatermarkSettingModel;
import com.ybw.yibai.module.watermarksetting.WatermarkSettingContract.WatermarkSettingPresenter;
import com.ybw.yibai.module.watermarksetting.WatermarkSettingContract.WatermarkSettingView;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 水印设置界面Presenter实现类
 *
 * @author sjl
 * @date 2019/11/21
 */
public class WatermarkSettingPresenterImpl extends BasePresenterImpl<WatermarkSettingView> implements WatermarkSettingPresenter, CallBack {

    /**
     * PresenterImpl 持有 View的接口引用
     */
    private WatermarkSettingView mWatermarkSettingView;

    /**
     * PresenterImpl 持有 Model的接口引用
     */
    private WatermarkSettingModel mWatermarkSettingModel;

    /**
     * 构造方法
     *
     * @param view View的对象
     */
    public WatermarkSettingPresenterImpl(WatermarkSettingView view) {
        super(view);

        // 调用父类的方法获取View的对象
        this.mWatermarkSettingView = getView();
        mWatermarkSettingModel = new WatermarkSettingModelImpl();
    }

    /**
     * 修改水印图片
     *
     * @param file 水印图片
     */
    @Override
    public void updateWatermark(File file) {
        Map<String, RequestBody> params = new HashMap<>();
        String fileName = file.getName();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/" + suffix), file);
        params.put("pic\"; filename=\"" + file.getName(), requestBody);
        mWatermarkSettingModel.updateWatermark(params, this);
    }

    /**
     * 在修改水印图片成功时回调
     *
     * @param updateWatermark 修改水印图片时服务器端返回的数据
     */
    @Override
    public void onUpdateWatermarkSuccess(UpdateWatermark updateWatermark) {
        mWatermarkSettingView.onUpdateWatermarkSuccess(updateWatermark);
    }
}
