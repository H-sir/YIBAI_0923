package com.ybw.yibai.module.producttype;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.ybw.yibai.base.BasePresenterImpl;
import com.ybw.yibai.common.bean.ListBean;
import com.ybw.yibai.common.bean.SKUList;
import com.ybw.yibai.common.utils.ImageUtil;
import com.ybw.yibai.module.producttype.ProductTypeContract.CallBack;
import com.ybw.yibai.module.producttype.ProductTypeContract.ProductTypeModel;
import com.ybw.yibai.module.producttype.ProductTypeContract.ProductTypePresenter;
import com.ybw.yibai.module.producttype.ProductTypeContract.ProductTypeView;

import java.util.List;
import java.util.Map;

/**
 * 产品类型Presenter实现类
 *
 * @author sjl
 * @date 2019/9/5
 */
public class ProductTypePresenterImpl extends BasePresenterImpl<ProductTypeView> implements ProductTypePresenter, CallBack {

    /**
     * PresenterImpl 持有 View的接口引用
     */
    private ProductTypeView mProductTypeView;

    /**
     * PresenterImpl 持有 Model的接口引用
     */
    private ProductTypeModel mProductTypeModel;

    /**
     * 构造方法
     *
     * @param view View的对象
     */
    public ProductTypePresenterImpl(ProductTypeView view) {
        super(view);

        // 调用父类的方法获取View的对象
        this.mProductTypeView = getView();
        mProductTypeModel = new ProductTypeModelImpl();
    }

    /**
     * 获取产品sku列表
     *
     * @param isAll    是否获取全部:1获取全部0分页(默认为0分页)
     * @param useState 使用状态
     * @param cateCode 产品类别(产品筛选参数大类别名)默认获取植物
     * @param pcId     产品类别id(多个用逗号拼接)
     * @param keyWord  搜索关键词
     * @param param    动态设置的参数
     */
    @Override
    public void getSKUList(int isAll, int useState, String cateCode, String pcId, String keyWord, Map<String, String> param) {
        mProductTypeModel.getSKUList(isAll, useState, cateCode, pcId, keyWord, param, this);
    }

    /**
     * 在获取产品sku列表成功时回调
     *
     * @param skuList 获取产品sku列表时服务器端返回的数据
     */
    @Override
    public void onGetSKUListSuccess(SKUList skuList) {
        mProductTypeView.onGetSKUListSuccess(skuList);
    }

    /**
     * 保存到模拟列表数据
     *
     * @param plantBean 用户当前选择的植物信息
     */
    @Override
    public void saveSimulation(ListBean plantBean) {
        Fragment fragment = (Fragment) mProductTypeView;
        Activity activity = fragment.getActivity();
        String productPic2 = plantBean.getPic2();
        if (TextUtils.isEmpty(productPic2)) {
            return;
        }
        ImageUtil.downloadPicture(activity, new ImageUtil.DownloadCallback() {
            @Override
            public void onDownloadStarted() {

            }

            @Override
            public void onDownloadFinished(List<Bitmap> bitmapList) {
                Bitmap bitmap = bitmapList.get(0);
                if (null == bitmap) {
                    // 下载图片失败return
                    return;
                }
                mProductTypeModel.saveSimulation(
                        plantBean,
                        bitmap,
                        ProductTypePresenterImpl.this
                );
            }

            @Override
            public void onDownloading(int sumTotal, int successesAmount, int failuresAmount, int completedAmount) {

            }
        }, productPic2);
    }

    /**
     * 在保存"模拟"成功/失败时回调
     *
     * @param result true成功,false失败
     */
    @Override
    public void onSaveSimulationResult(boolean result) {
        mProductTypeView.onSaveSimulationResult(result);
    }
}