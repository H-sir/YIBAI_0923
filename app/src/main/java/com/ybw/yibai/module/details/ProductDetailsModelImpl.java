package com.ybw.yibai.module.details;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.bean.AddQuotation;
import com.ybw.yibai.common.bean.ProductData;
import com.ybw.yibai.common.bean.ProductDetails;
import com.ybw.yibai.common.bean.QuotationData;
import com.ybw.yibai.common.bean.SceneInfo;
import com.ybw.yibai.common.bean.SimilarSKU;
import com.ybw.yibai.common.bean.SimulationData;
import com.ybw.yibai.common.bean.UpdateSKUUseState;
import com.ybw.yibai.common.interfaces.ApiService;
import com.ybw.yibai.common.utils.ImageUtil;
import com.ybw.yibai.common.utils.LogUtil;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.utils.RetrofitManagerUtil;
import com.ybw.yibai.common.utils.TimeUtil;
import com.ybw.yibai.module.details.ProductDetailsContract.CallBack;
import com.ybw.yibai.module.details.ProductDetailsContract.ProductDetailsModel;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.ybw.yibai.common.constants.HttpUrls.ADD_QUOTATION_METHOD;
import static com.ybw.yibai.common.constants.HttpUrls.GET_PRODUCT_INFO_METHOD;
import static com.ybw.yibai.common.constants.HttpUrls.GET_SIMILAR_SUK_LIST_METHOD;
import static com.ybw.yibai.common.constants.HttpUrls.UPDATE_SKU_USE_STATE_METHOD;
import static com.ybw.yibai.common.constants.Preferences.PLANT;

/**
 * 盆栽详情界面Model实现类
 *
 * @author sjl
 * @date 2019/5/16
 */
public class ProductDetailsModelImpl implements ProductDetailsModel {

    private static final String TAG = "ProductDetailsModelImpl";

    private ApiService mApiService;

    public ProductDetailsModelImpl() {
        RetrofitManagerUtil instance = RetrofitManagerUtil.getInstance();
        mApiService = instance.getApiService();
    }

    /**
     * 获取产品详情
     *
     * @param productId 盆栽
     * @param callBack  回调方法
     */
    @Override
    public void getProductDetails(int productId, CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<ProductDetails> observable = mApiService.getProductDetails(timeStamp,
                OtherUtil.getSign(timeStamp, GET_PRODUCT_INFO_METHOD),
                YiBaiApplication.getUid(),
                productId);
        Observer<ProductDetails> observer = new Observer<ProductDetails>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(ProductDetails productDetails) {
                callBack.onGetProductDetailsSuccess(productDetails);
            }

            @Override
            public void onError(Throwable e) {
                callBack.onRequestFailure(e);
            }

            @Override
            public void onComplete() {
                callBack.onRequestComplete();
            }
        };
        observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(observer);
    }

    /**
     * 获取相似推荐sku列表
     *
     * @param skuId    产品SKUid
     * @param callBack 回调方法
     */
    @Override
    public void getSimilarSKUList(int skuId, CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<SimilarSKU> observable = mApiService.getSimilarSKUList(timeStamp,
                OtherUtil.getSign(timeStamp, GET_SIMILAR_SUK_LIST_METHOD),
                YiBaiApplication.getUid(),
                skuId);
        Observer<SimilarSKU> observer = new Observer<SimilarSKU>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(SimilarSKU similarSKU) {
                callBack.onGetSimilarSKUListSuccess(similarSKU);
            }

            @Override
            public void onError(Throwable e) {
                callBack.onRequestFailure(e);
            }

            @Override
            public void onComplete() {
                callBack.onRequestComplete();
            }
        };
        observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(observer);
    }

    /**
     * 修改产品sku使用状态
     *
     * @param skuId    产品SKUid
     * @param callBack 回调方法
     */
    @Override
    public void updateSKUUseState(int skuId, CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<UpdateSKUUseState> observable = mApiService.updateSKUUseState(timeStamp,
                OtherUtil.getSign(timeStamp, UPDATE_SKU_USE_STATE_METHOD),
                YiBaiApplication.getUid(),
                skuId);
        Observer<UpdateSKUUseState> observer = new Observer<UpdateSKUUseState>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(UpdateSKUUseState updateSKUUseState) {
                callBack.onUpdateSKUUseStateSuccess(updateSKUUseState);
            }

            @Override
            public void onError(Throwable e) {
                callBack.onRequestFailure(e);
            }

            @Override
            public void onComplete() {
                callBack.onRequestComplete();
            }
        };
        observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(observer);
    }

    /**
     * 将ProductData数据保存到"模拟搭配产品"数据的数据库中
     *
     * @param cateCode    产品类别(plant=植物,pot=花盆)
     * @param productData 产品数据
     * @param bitmap      产品"模拟搭图片"
     * @param callBack    回调方法
     */
    @Override
    public void saveSimulationData(String cateCode, ProductData productData, Bitmap bitmap, CallBack callBack) {
        int width = 0;
        int height = 0;
        String picturePath = null;
        if (null != bitmap) {
            width = bitmap.getWidth();
            height = bitmap.getHeight();
            // 保存Bitmap为图片到本地
            picturePath = ImageUtil.saveImage(bitmap, String.valueOf(TimeUtil.getTimeStamp()));
        }

        int productSkuId = productData.getProductSkuId();
        String productName = productData.getProductName();
        double productPrice = productData.getProductPrice();
        double productMonthRent = productData.getProductMonthRent();
        double productTradePrice = productData.getProductTradePrice();
        String productPic1 = productData.getProductPic1();
        String productPic2 = productData.getProductPic2();
        String productPic3 = productData.getProductPic3();
        String productSpecText = productData.getProductSpecText();
        double productHeight = productData.getProductHeight();
        int productCombinationType = productData.getProductCombinationType();
        String productPriceCode = productData.getProductPriceCode();
        String productTradePriceCode = productData.getProductTradePriceCode();

        int uid = YiBaiApplication.getUid();
        String finallySkuId = String.valueOf(productSkuId) + uid;

        LogUtil.e(TAG, "产品类别: " + cateCode);
        LogUtil.e(TAG, "产品款名Id: " + productSkuId);
        LogUtil.e(TAG, "产品名称: " + productName);
        LogUtil.e(TAG, "产品价格: " + productPrice);
        LogUtil.e(TAG, "产品月租: " + productMonthRent);
        LogUtil.e(TAG, "产品批发价: " + productTradePrice);
        LogUtil.e(TAG, "产品主图url地址: " + productPic1);
        LogUtil.e(TAG, "产品模拟图url地址: " + productPic2);
        LogUtil.e(TAG, "产品配图url地址: " + productPic3);
        LogUtil.e(TAG, "产品高度: " + productHeight);
        LogUtil.e(TAG, "产品组合模式: 1单图模式,2搭配上部,3搭配下部: " + productCombinationType);
        LogUtil.e(TAG, "产品售价代码: " + productPriceCode);
        LogUtil.e(TAG, "产品批发价代码: " + productTradePriceCode);
        LogUtil.e(TAG, "主产品的款名Id+附加产品的款名Id的组合: " + finallySkuId);
        LogUtil.e(TAG, "保存Bitmap为图片到本地的路径: " + picturePath);

        try {
            DbManager dbManager = YiBaiApplication.getDbManager();
            // 模拟(在这里可以添加finallySkuId相同的盆栽)
            SimulationData simulationData = new SimulationData();
            simulationData.setUid(uid);

            // 查找当前正在编辑的这一个场景
            List<SceneInfo> defaultSceneInfoList = dbManager.selector(SceneInfo.class)
                    .where("uid", "=", uid)
                    .and("editScene", "=", true)
                    .findAll();
            if (null != defaultSceneInfoList && defaultSceneInfoList.size() > 0) {
                long sceneId = defaultSceneInfoList.get(0).getSceneId();
                simulationData.setSceneId(sceneId);
            }

            // 之所以要区分CateCode是为了在”场景编辑“界面进行“改款“时区分植物与盆器用的,用于获取植物/盆器的SkuId
            simulationData.setFinallySkuId(finallySkuId);
            if (PLANT.equals(cateCode)) {
                simulationData.setProductSkuId(productSkuId);
                if (!TextUtils.isEmpty(productName)) {
                    simulationData.setProductName(productName);
                }
                simulationData.setProductPrice(productPrice);
                simulationData.setProductMonthRent(productMonthRent);
                simulationData.setProductTradePrice(productTradePrice);
                if (!TextUtils.isEmpty(productPic1)) {
                    simulationData.setProductPic1(productPic1);
                }
                if (!TextUtils.isEmpty(productPic2)) {
                    simulationData.setProductPic2(productPic2);
                }
                if (!TextUtils.isEmpty(productPic3)) {
                    simulationData.setProductPic3(productPic3);
                }
                if (!TextUtils.isEmpty(productSpecText)) {
                    simulationData.setProductSpecText(productSpecText);
                }
                if (0 != productHeight) {
                    simulationData.setProductHeight(productHeight);
                }
                simulationData.setProductCombinationType(productCombinationType);
                if (!TextUtils.isEmpty(productPriceCode)) {
                    simulationData.setProductPriceCode(productPriceCode);
                }
                if (!TextUtils.isEmpty(productTradePriceCode)) {
                    simulationData.setProductTradePriceCode(productTradePriceCode);
                }
            } else {
                simulationData.setAugmentedProductSkuId(productSkuId);
                if (!TextUtils.isEmpty(productName)) {
                    simulationData.setAugmentedProductName(productName);
                }
                simulationData.setAugmentedProductPrice(productPrice);
                simulationData.setAugmentedProductMonthRent(productMonthRent);
                simulationData.setAugmentedProductTradePrice(productTradePrice);
                if (!TextUtils.isEmpty(productPic1)) {
                    simulationData.setAugmentedProductPic1(productPic1);
                }
                if (!TextUtils.isEmpty(productPic2)) {
                    simulationData.setAugmentedProductPic2(productPic2);
                }
                if (!TextUtils.isEmpty(productPic3)) {
                    simulationData.setAugmentedProductPic3(productPic3);
                }
                if (!TextUtils.isEmpty(productSpecText)) {
                    simulationData.setAugmentedProductSpecText(productSpecText);
                }
                if (0 != productHeight) {
                    simulationData.setAugmentedProductHeight(productHeight);
                }
                simulationData.setProductCombinationType(productCombinationType);
                if (!TextUtils.isEmpty(productPriceCode)) {
                    simulationData.setAugmentedPriceCode(productPriceCode);
                }
                if (!TextUtils.isEmpty(productTradePriceCode)) {
                    simulationData.setAugmentedTradePriceCode(productTradePriceCode);
                }
            }
            /*----------*/
            simulationData.setTimeStamp(TimeUtil.getNanoTime());
            if (!TextUtils.isEmpty(picturePath)) {
                simulationData.setPicturePath(picturePath);
            }
            if (0 < width && 0 < height) {
                simulationData.setWidth(width);
                simulationData.setHeight(height);
            }
            // 默认为1
            simulationData.setxScale(1);
            simulationData.setyScale(1);
            /*----------*/
            dbManager.save(simulationData);
            // 提示用户添加成功
            callBack.onSaveSimulationDataDataResult(true);
        } catch (DbException e) {
            e.printStackTrace();
            // 操作失败
            callBack.onSaveSimulationDataDataResult(false);
        }
    }

    /**
     * 将ProductData数据保存到"报价"数据的数据库中
     *
     * @param productData 产品数据
     * @param callBack    回调方法
     */
    @Override
    public void saveQuotationData(ProductData productData, CallBack callBack) {
        int productSkuId = productData.getProductSkuId();
        String productName = productData.getProductName();
        double productPrice = productData.getProductPrice();
        double productMonthRent = productData.getProductMonthRent();
        double productTradePrice = productData.getProductTradePrice();
        String productPic1 = productData.getProductPic1();
        String productPic2 = productData.getProductPic2();
        String productPic3 = productData.getProductPic3();
        String productSpecText = productData.getProductSpecText();
        double productHeight = productData.getProductHeight();
        int productCombinationType = productData.getProductCombinationType();
        String productPriceCode = productData.getProductPriceCode();
        String productTradePriceCode = productData.getProductTradePriceCode();

        int uid = YiBaiApplication.getUid();
        String finallySkuId = String.valueOf(productSkuId) + uid;

        LogUtil.e(TAG, "产品款名Id: " + productSkuId);
        LogUtil.e(TAG, "产品名称: " + productName);
        LogUtil.e(TAG, "产品价格: " + productPrice);
        LogUtil.e(TAG, "产品月租: " + productMonthRent);
        LogUtil.e(TAG, "产品批发价: " + productTradePrice);
        LogUtil.e(TAG, "产品主图url地址: " + productPic1);
        LogUtil.e(TAG, "产品模拟图url地址: " + productPic2);
        LogUtil.e(TAG, "产品配图url地址: " + productPic3);
        LogUtil.e(TAG, "产品高度: " + productHeight);
        LogUtil.e(TAG, "产品组合模式: 1单图模式,2搭配上部,3搭配下部: " + productCombinationType);
        LogUtil.e(TAG, "产品售价代码: " + productPriceCode);
        LogUtil.e(TAG, "产品批发价代码: " + productTradePriceCode);
        LogUtil.e(TAG, "组合好的用户选择的某种产品的规格: " + productSpecText);

        try {
            DbManager dbManager = YiBaiApplication.getDbManager();
            QuotationData quotation = dbManager.findById(QuotationData.class, finallySkuId);
            if (null == quotation) {
                // 没有报价
                QuotationData quotationData = new QuotationData();
                quotationData.setUid(uid);
                quotationData.setFinallySkuId(finallySkuId);
                quotationData.setProductSkuId(productSkuId);
                if (!TextUtils.isEmpty(productName)) {
                    quotationData.setProductName(productName);
                }
                quotationData.setProductPrice(productPrice);
                quotationData.setProductMonthRent(productMonthRent);
                quotationData.setProductTradePrice(productTradePrice);
                if (!TextUtils.isEmpty(productPic1)) {
                    quotationData.setProductPic1(productPic1);
                }
                if (!TextUtils.isEmpty(productPic2)) {
                    quotationData.setProductPic2(productPic2);
                }
                if (!TextUtils.isEmpty(productPic3)) {
                    quotationData.setProductPic3(productPic3);
                }
                if (!TextUtils.isEmpty(productSpecText)) {
                    quotationData.setProductSpecText(productSpecText);
                }
                if (0 != productHeight) {
                    quotationData.setProductHeight(productHeight);
                }
                quotationData.setProductCombinationType(productCombinationType);
                if (!TextUtils.isEmpty(productPriceCode)) {
                    quotationData.setProductPriceCode(productPriceCode);
                }
                if (!TextUtils.isEmpty(productTradePriceCode)) {
                    quotationData.setProductTradePriceCode(productTradePriceCode);
                }
                quotationData.setNumber(1);
                quotationData.setTimeStamp(TimeUtil.getNanoTime());
                dbManager.save(quotationData);
                // 提示用户添加成功
                callBack.onSaveQuotationDataDataResult(true);
            } else {
                // 已经报过价
                int number = quotation.getNumber();
                // 数量+1
                quotation.setNumber(++number);
                quotation.setTimeStamp(TimeUtil.getNanoTime());
                // 更新quotation列名为number,timeStamp的数据
                dbManager.update(quotation, "number", "timeStamp");
                // 提示用户添加成功
                callBack.onSaveQuotationDataDataResult(true);
            }
        } catch (DbException e) {
            e.printStackTrace();
            LogUtil.e(TAG,"错误： "+  e.toString());
            // 操作失败
            callBack.onSaveQuotationDataDataResult(false);
        }
    }

    /**
     * sku加入待摆放清单
     *
     * @param firstSkuId 主产品 skuId
     * @param callBack   回调方法
     */
    @Override
    public void addQuotation(int firstSkuId, CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<AddQuotation> observable = mApiService.addQuotation(timeStamp,
                OtherUtil.getSign(timeStamp, ADD_QUOTATION_METHOD),
                YiBaiApplication.getUid(),
                YiBaiApplication.getCid(),
                firstSkuId,
                null);
        Observer<AddQuotation> observer = new Observer<AddQuotation>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(AddQuotation addQuotation) {
                callBack.onAddQuotationSuccess(addQuotation);
            }

            @Override
            public void onError(Throwable e) {
                callBack.onRequestFailure(e);
            }

            @Override
            public void onComplete() {
                callBack.onRequestComplete();
            }
        };
        observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(observer);
    }
}
