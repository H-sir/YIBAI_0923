package com.ybw.yibai.module.details;

import android.graphics.Bitmap;
import android.view.View;

import com.ybw.yibai.base.BaseCallBack;
import com.ybw.yibai.base.BasePresenter;
import com.ybw.yibai.base.BaseView;
import com.ybw.yibai.common.bean.AddQuotation;
import com.ybw.yibai.common.bean.ProductData;
import com.ybw.yibai.common.bean.ProductDetails;
import com.ybw.yibai.common.bean.ProductDetails.DataBean.SkuListBean;
import com.ybw.yibai.common.bean.ProductDetails.DataBean.SpecBean;
import com.ybw.yibai.common.bean.PurCartBean;
import com.ybw.yibai.common.bean.SimilarSKU;
import com.ybw.yibai.common.bean.UpdateSKUUseState;

import java.util.List;
import java.util.Map;

/**
 * 合约模式,将三个Model,View,Presenter接口以及CallBack接口放在一个Contract接口里面方便统一管理
 *
 * @author sjl
 * @date 2019/5/16
 */
public interface ProductDetailsContract {

    interface ProductDetailsView extends BaseView {

        /**
         * 在获取产品详情成功时回调
         *
         * @param productDetails 产品详情
         */
        void onGetProductDetailsSuccess(ProductDetails productDetails);

        /**
         * 在获取相似推荐sku列表成功时回调
         *
         * @param similarSKU 相似推荐sku列表
         */
        void onGetSimilarSKUListSuccess(SimilarSKU similarSKU);

        /**
         * 在修改产品sku使用状态成功时回调
         *
         * @param updateSKUUseState 修改产品sku使用状态时服务器端返回的数据
         */
        void onUpdateSKUUseStateSuccess(UpdateSKUUseState updateSKUUseState);

        /**
         * 在获取用户选择的植物规格"成功时回调
         *
         * @param selectsSpecMap 用户选择的规格
         */
        void onGetSelectsSpecSuccess(Map<Integer, Integer> selectsSpecMap);

        /**
         * 在PopupWindow Item 点击时回调
         *
         * @param v 被点击的item
         */
        void onPopupWindowItemClick(View v);

        /**
         * 返回根据用户选中产品信息
         *
         * @param skuList 产品信息
         */
        void returnSelectsProductInfo(SkuListBean skuList);

        /**
         * 保存"模拟搭配产品"数据成功或者失败时回调
         *
         * @param result true成功 false失败
         */
        void onSaveSimulationDataDataResult(boolean result);

        /**
         * 保存"报价"数据成功或者失败时回调
         *
         * @param result true成功 false失败
         */
        void onSaveQuotationDataDataResult(boolean result);

        /**
         * 在sku加入待摆放清单成功时回调
         *
         * @param addQuotation sku加入待摆放清单时服务器端返回的数据
         */
        void onAddQuotationSuccess(AddQuotation addQuotation);

        void onGetPurCartDataSuccess(PurCartBean purCartBean);
    }

    interface ProductDetailsPresenter extends BasePresenter<ProductDetailsView> {

        /**
         * 获取产品详情
         *
         * @param productId 盆栽
         */
        void getProductDetails(int productId);

        /**
         * 获取相似推荐sku列表
         *
         * @param skuId 产品SKUid
         */
        void getSimilarSKUList(int skuId);

        /**
         * 修改产品sku使用状态
         *
         * @param skuId 产品SKUid
         */
        void updateSKUUseState(int skuId);

        /**
         * 获取一开始进入本界面的产品的规格
         *
         * @param skuId    当前产品的SKUid
         * @param specList 产品规格集合
         * @param skuList  产品Sku集合
         */
        void getInitialSelectsSpec(int skuId, List<SpecBean> specList, List<SkuListBean> skuList);

        /**
         * 根据用户选中的规格来获取规格对应的产品信息
         *
         * @param specIdList 用户选择的产品其规格ID的集合
         * @param skuList    产品Sku集合
         */
        void getProductInfoAccordingToSpec(List<Integer> specIdList, List<SkuListBean> skuList);

        /**
         * 显示产品规格PopupWindow
         *
         * @param rootLayout 界面Root布局
         * @param specList   当前植物规格列表
         */
        void displaySpecPopupWindow(View rootLayout, List<SpecBean> specList);

        /**
         * 显示产品价格PopupWindow
         *
         * @param rootLayout 界面Root布局
         * @param sku        产品Sku
         */
        void displayPricePopupWindow(View rootLayout, SkuListBean sku);

        /**
         * 将ProductData数据保存到"模拟搭配产品"数据的数据库中
         *
         * @param cateCode    产品类别(plant=植物,pot=花盆)
         * @param productData 产品数据
         */
        void saveSimulationData(String cateCode, ProductData productData);

        /**
         * 将ProductData数据保存到"报价"数据的数据库中
         *
         * @param productData 产品数据
         */
        void saveQuotationData(ProductData productData);

        /**
         * sku加入待摆放清单
         *
         * @param firstSkuId 主产品 skuId
         */
        void addQuotation(int firstSkuId);

        /**
         *添加商品到进货列表
         * */
        void addpurcart(ProductData productData);

        void getPurCartData();
    }

    interface ProductDetailsModel {

        /**
         * 获取产品详情
         *
         * @param productId 盆栽
         * @param callBack  回调方法
         */
        void getProductDetails(int productId, CallBack callBack);

        /**
         * 获取相似推荐sku列表
         *
         * @param skuId    产品SKUid
         * @param callBack 回调方法
         */
        void getSimilarSKUList(int skuId, CallBack callBack);

        /**
         * 修改产品sku使用状态
         *
         * @param skuId    产品SKUid
         * @param callBack 回调方法
         */
        void updateSKUUseState(int skuId, CallBack callBack);

        /**
         * 将ProductData数据保存到"模拟搭配产品"数据的数据库中
         *
         * @param cateCode    产品类别(plant=植物,pot=花盆)
         * @param productData 产品数据
         * @param bitmap      产品"模拟搭图片"
         * @param callBack    回调方法
         */
        void saveSimulationData(String cateCode, ProductData productData, Bitmap bitmap, CallBack callBack);

        /**
         * 将ProductData数据保存到"报价"数据的数据库中
         *
         * @param productData 产品数据
         * @param callBack    回调方法
         */
        void saveQuotationData(ProductData productData, CallBack callBack);

        /**
         * sku加入待摆放清单
         *
         * @param firstSkuId 主产品 skuId
         * @param callBack   回调方法
         */
        void addQuotation(int firstSkuId, CallBack callBack);

        void addPurcart(ProductData productData, CallBack callBack);

        void getPurCartData(CallBack callBack);
    }

    interface CallBack extends BaseCallBack {

        /**
         * 在获取产品详情成功时回调
         *
         * @param productDetails 产品详情
         */
        void onGetProductDetailsSuccess(ProductDetails productDetails);

        /**
         * 在获取相似推荐sku列表成功时回调
         *
         * @param similarSKU 相似推荐sku列表
         */
        void onGetSimilarSKUListSuccess(SimilarSKU similarSKU);

        /**
         * 在修改产品sku使用状态成功时回调
         *
         * @param updateSKUUseState 修改产品sku使用状态时服务器端返回的数据
         */
        void onUpdateSKUUseStateSuccess(UpdateSKUUseState updateSKUUseState);

        /**
         * 保存"模拟搭配产品"数据成功或者失败时回调
         *
         * @param result true成功 false失败
         */
        void onSaveSimulationDataDataResult(boolean result);

        /**
         * 保存"报价"数据成功或者失败时回调
         *
         * @param result true成功 false失败
         */
        void onSaveQuotationDataDataResult(boolean result);

        /**
         * 在sku加入待摆放清单成功时回调
         *
         * @param addQuotation sku加入待摆放清单时服务器端返回的数据
         */
        void onAddQuotationSuccess(AddQuotation addQuotation);

        void onGetPurCartDataSuccess(PurCartBean purCartBean);
    }
}
