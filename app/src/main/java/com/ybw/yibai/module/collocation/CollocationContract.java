package com.ybw.yibai.module.collocation;

import android.graphics.Bitmap;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.ybw.yibai.base.BaseCallBack;
import com.ybw.yibai.base.BasePresenter;
import com.ybw.yibai.base.BaseView;
import com.ybw.yibai.common.bean.AddQuotation;
import com.ybw.yibai.common.bean.ProductData;
import com.ybw.yibai.common.bean.SKUList;
import com.ybw.yibai.common.bean.SimilarPlantSKU;

import java.util.Map;

import okhttp3.RequestBody;

/**
 * 合约模式,将三个Model,View,Presenter接口以及CallBack接口放在一个Contract接口里面方便统一管理
 *
 * @author sjl
 * @date 2019/9/20
 */
@SuppressWarnings("all")
public interface CollocationContract {

    interface CollocationView extends BaseView {

        /**
         * 在获取搭配时植物sku推荐列表成功时回调
         *
         * @param similarPlantSKU 获取搭配时植物sku推荐列表时服务器端返回的数据
         */
        void onGetSimilarPlantSKUListSuccess(SimilarPlantSKU similarPlantSKU);

        /**
         * 在获取搭配时盆器sku推荐列表成功时回调
         *
         * @param similarPotSKU 获取搭配时盆器sku推荐列表时服务器端返回的数据
         */
        void onGetSimilarPotSKUListSuccess(SKUList similarPotSKU);

        /**
         * 在保存"模拟"数据成功时回调
         */
        void onSaveSimulationDataSuccess();

        /**
         * 在保存"报价"数据成功时回调
         */
        void onSaveQuotationDataDataSuccess();

        /**
         * 在保存"模拟"or"报价"数据失败时回调
         */
        void onSaveSimulationOrQuotationDataFairly();

        /**
         * 在sku加入待摆放清单成功时回调
         *
         * @param addQuotation sku加入待摆放清单时服务器端返回的数据
         */
        void onAddQuotationSuccess(AddQuotation addQuotation);
    }

    interface CollocationPresenter extends BasePresenter<CollocationView> {

        /**
         * 获取搭配时植物sku推荐列表
         *
         * @param skuId 植物SKUid
         */
        void getSimilarPlantSKUList(int skuId);

        /**
         * 获取搭配时盆器sku推荐列表
         *
         * @param cateCode    产品类别
         * @param curDiameter 当前口径:搭配口径+边距*2的值
         * @param diameter    搭配口径:格式同上
         */
        void getSimilarPotSKUList(String cateCode, double curDiameter, double diameter);

        /**
         * 动态设置"搭配图片的布局"的宽度和高度
         *
         * @param collocationLayout 搭配图片布局的容器
         */
        void setCollocationRootLayoutParams(View collocationLayout);

        /**
         * 动态设置"搭配图片的布局里面的ViewPager,ViewPager的高度,使其比例与植物高度:盆器高度比例一致
         *
         * @param plantViewPager 放置"植物自由搭配图"的ViewPager
         * @param potViewPager   放置"盆器自由搭配图"的ViewPager
         * @param plantHeight    用户当前选择的植物规格的高度
         * @param potHeight      用户当前选择的盆规格的高度
         * @param offsetRatio    花盆的偏移量
         */
        void setCollocationContentParams(ViewPager plantViewPager, ViewPager potViewPager, double plantHeight, double potHeight, double productOffsetRatio,double offsetRatio);

        /**
         * 添加到模拟列表
         *
         * @param pottingData 产品相关数据
         */
        void saveSimulationData(ProductData productData);

        /**
         * 加入预选
         *
         * @param pottingData 产品相关数据
         */
        void joinPreselection(ProductData productData);

        /**
         * 加入相册
         *
         * @param productData 产品相关数据
         */
        void joinPhotoAlbum(ProductData productData);

        /**
         * 加入进货
         *
         * @param productData 产品相关数据
         */
        void saveQuotationData(ProductData productData);
    }

    interface CollocationModel {

        /**
         * 获取搭配时植物sku推荐列表
         *
         * @param skuId    植物SKUid
         * @param callBack 回调方法
         */
        void getSimilarPlantSKUList(int skuId, CallBack callBack);

        /**
         * 获取搭配时盆器sku推荐列表
         *
         * @param cateCode    产品类别
         * @param curDiameter 当前口径:搭配口径+边距*2的值
         * @param diameter    搭配口径:格式同上
         * @param callBack    回调方法
         */
        void getSimilarPotSKUList(String cateCode, double curDiameter, double diameter, CallBack callBack);

        /**
         * 根据type将ProductData数据保存到"模拟"数据or"报价"数据的数据库中
         *
         * @param productData 产品数据
         * @param bitmap      植物与盆栽合成后的位图
         * @param type        类型 1: 添加到模拟列表 2: 报价
         * @param callBack    回调方法
         */
        void saveProductData(ProductData productData, Bitmap bitmap, int type, CallBack callBack);

        /**
         * sku加入待摆放清单
         *
         * @param firstSkuId  主产品 skuId
         * @param secondSkuId 附加产品 skuId
         * @param params      组合图片
         * @param callBack    回调方法
         */
        void addQuotation(int firstSkuId, int secondSkuId, Map<String, RequestBody> params, CallBack callBack);
    }

    interface CallBack extends BaseCallBack {

        /**
         * 在获取搭配时植物sku推荐列表成功时回调
         *
         * @param similarPlantSKU 获取搭配时植物sku推荐列表时服务器端返回的数据
         */
        void onGetSimilarPlantSKUListSuccess(SimilarPlantSKU similarPlantSKU);

        /**
         * 在获取搭配时盆器sku推荐列表成功时回调
         *
         * @param similarPotSKU 获取搭配时盆器sku推荐列表时服务器端返回的数据
         */
        void onGetSimilarPotSKUListSuccess(SKUList similarPotSKU);

        /**
         * 在保存"模拟"数据成功时回调
         */
        void onSaveSimulationDataSuccess();

        /**
         * 在保存"报价"数据成功时回调
         */
        void onSaveQuotationDataDataSuccess();

        /**
         * 在保存"模拟"or"报价"数据失败时回调
         */
        void onSaveSimulationOrQuotationDataFairly();

        /**
         * 在sku加入待摆放清单成功时回调
         *
         * @param addQuotation sku加入待摆放清单时服务器端返回的数据
         */
        void onAddQuotationSuccess(AddQuotation addQuotation);
    }
}