package com.ybw.yibai.module.collocation;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.bean.AddQuotation;
import com.ybw.yibai.common.bean.BaseBean;
import com.ybw.yibai.common.bean.ProductData;
import com.ybw.yibai.common.bean.QuotationData;
import com.ybw.yibai.common.bean.SKUList;
import com.ybw.yibai.common.bean.SceneInfo;
import com.ybw.yibai.common.bean.SimilarPlantSKU;
import com.ybw.yibai.common.bean.SimulationData;
import com.ybw.yibai.common.interfaces.ApiService;
import com.ybw.yibai.common.utils.ImageUtil;
import com.ybw.yibai.common.utils.LogUtil;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.utils.RetrofitManagerUtil;
import com.ybw.yibai.common.utils.TimeUtil;
import com.ybw.yibai.module.collocation.CollocationContract.CallBack;
import com.ybw.yibai.module.collocation.CollocationContract.CollocationModel;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

import static com.ybw.yibai.common.constants.HttpUrls.ADD_PURCART_METHOD;
import static com.ybw.yibai.common.constants.HttpUrls.ADD_QUOTATION_METHOD;
import static com.ybw.yibai.common.constants.HttpUrls.GET_SIMILAR_PLANT_SUK_LIST_METHOD;
import static com.ybw.yibai.common.constants.HttpUrls.GET_SUK_LIST_METHOD;
import static com.ybw.yibai.common.utils.FileUtil.judeFileExists;

/**
 * 搭配界面Model实现类
 *
 * @author sjl
 * @date 2019/9/20
 */
public class CollocationModelImpl implements CollocationModel {

    private static final String TAG = "CollocationModelImpl";

    private ApiService mApiService;

    public CollocationModelImpl() {
        RetrofitManagerUtil instance = RetrofitManagerUtil.getInstance();
        mApiService = instance.getApiService();
    }

    /**
     * 获取搭配时植物sku推荐列表
     *
     * @param skuId    植物SKUid
     * @param callBack 回调方法
     */
    @Override
    public void getSimilarPlantSKUList(int skuId, CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<SimilarPlantSKU> observable = mApiService.getSimilarPlantSKUList(timeStamp,
                OtherUtil.getSign(timeStamp, GET_SIMILAR_PLANT_SUK_LIST_METHOD),
                YiBaiApplication.getUid(),
                skuId,
                null,
                null);
        Observer<SimilarPlantSKU> observer = new Observer<SimilarPlantSKU>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(SimilarPlantSKU similarPlantSKU) {
                callBack.onGetSimilarPlantSKUListSuccess(similarPlantSKU);
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
     * 获取搭配时盆器sku推荐列表
     *
     * @param cateCode    产品类别
     * @param curDiameter 当前口径:搭配口径+边距*2的值
     * @param diameter    搭配口径:格式同上
     * @param callBack    回调方法
     */
    @Override
    public void getSimilarPotSKUList(String cateCode, double curDiameter, double diameter, CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<SKUList> observable = mApiService.getSKUList(timeStamp,
                OtherUtil.getSign(timeStamp, GET_SUK_LIST_METHOD),
                YiBaiApplication.getUid(),
                0,
                cateCode,
                null,
                null,
                curDiameter,
                diameter,
                null,
                null,
                null,
                null);
        Observer<SKUList> observer = new Observer<SKUList>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(SKUList skuList) {
                callBack.onGetSimilarPotSKUListSuccess(skuList);
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
     * 根据type将ProductData数据保存到"模拟"数据or"报价"数据的数据库中
     *
     * @param productData 产品数据
     * @param bitmap      植物与盆栽合成后的位图
     * @param type        类型 1: 添加到模拟列表 4: 报价(加入进货)
     * @param callBack    回调方法
     */
    @Override
    public void saveProductData(ProductData productData, Bitmap bitmap, int type, CallBack callBack) {
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

        int augmentedProductSkuId = productData.getAugmentedProductSkuId();
        String augmentedProductName = productData.getAugmentedProductName();
        double augmentedProductPrice = productData.getAugmentedProductPrice();
        double augmentedProductMonthRent = productData.getAugmentedProductMonthRent();
        double augmentedProductTradePrice = productData.getAugmentedProductTradePrice();
        String augmentedProductPic1 = productData.getAugmentedProductPic1();
        String augmentedProductPic2 = productData.getAugmentedProductPic2();
        String augmentedProductPic3 = productData.getAugmentedProductPic3();
        String augmentedProductSpecText = productData.getAugmentedProductSpecText();
        double augmentedProductHeight = productData.getAugmentedProductHeight();
        double augmentedProductOffsetRatio = productData.getAugmentedProductOffsetRatio();
        int augmentedCombinationType = productData.getAugmentedCombinationType();
        String augmentedPriceCode = productData.getAugmentedPriceCode();
        String augmentedTradePriceCode = productData.getAugmentedTradePriceCode();

        int uid = YiBaiApplication.getUid();
        String finallySkuId = String.valueOf(productSkuId) + augmentedProductSkuId + uid;

        LogUtil.e(TAG, "主产品的款名Id: " + productSkuId);
        LogUtil.e(TAG, "主产品名称: " + productName);
        LogUtil.e(TAG, "主产品价格: " + productPrice);
        LogUtil.e(TAG, "主产品月租: " + productMonthRent);
        LogUtil.e(TAG, "主产品批发价: " + productTradePrice);
        LogUtil.e(TAG, "主产品主图url地址: " + productPic1);
        LogUtil.e(TAG, "主产品模拟图url地址: " + productPic2);
        LogUtil.e(TAG, "主产品配图url地址: " + productPic3);
        LogUtil.e(TAG, "主产品的规格: " + productSpecText);
        LogUtil.e(TAG, "主产品的高度: " + productHeight);
        LogUtil.e(TAG, "主产品组合模式: 1单图模式,2搭配上部,3搭配下部: " + productCombinationType);
        LogUtil.e(TAG, "主产品售价代码: " + productPriceCode);
        LogUtil.e(TAG, "主产品批发价代码: " + productTradePriceCode);

        LogUtil.e(TAG, "附加产品的款名Id: " + augmentedProductSkuId);
        LogUtil.e(TAG, "附加产品名称: " + augmentedProductName);
        LogUtil.e(TAG, "附加产品价格: " + augmentedProductPrice);
        LogUtil.e(TAG, "附加产品月租: " + augmentedProductMonthRent);
        LogUtil.e(TAG, "附加产品批发价: " + augmentedProductTradePrice);
        LogUtil.e(TAG, "附加产品主图url地址: " + augmentedProductPic1);
        LogUtil.e(TAG, "附加产品模拟图url地址: " + augmentedProductPic2);
        LogUtil.e(TAG, "附加产品配图url地址: " + augmentedProductPic3);
        LogUtil.e(TAG, "附加品的规格: " + augmentedProductSpecText);
        LogUtil.e(TAG, "附加产品的高度: " + augmentedProductHeight);
        LogUtil.e(TAG, "偏移量(如果该产品是花盆): " + augmentedProductOffsetRatio);
        LogUtil.e(TAG, "附加产品组合模式: 1单图模式,2搭配上部,3搭配下部: " + augmentedCombinationType);
        LogUtil.e(TAG, "附加产品售价代码: " + augmentedPriceCode);
        LogUtil.e(TAG, "附加产品批发价代码: " + augmentedTradePriceCode);

        LogUtil.e(TAG, "主产品的款名Id+附加产品的款名Id的组合: " + finallySkuId);
        LogUtil.e(TAG, "保存Bitmap为图片到本地的路径: " + picturePath);

        try {
            DbManager dbManager = YiBaiApplication.getDbManager();
            if (1 == type) {
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

                simulationData.setFinallySkuId(finallySkuId);
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
                /*----------*/
                simulationData.setAugmentedProductSkuId(augmentedProductSkuId);
                if (!TextUtils.isEmpty(augmentedProductName)) {
                    simulationData.setAugmentedProductName(augmentedProductName);
                }
                simulationData.setAugmentedProductPrice(augmentedProductPrice);
                simulationData.setAugmentedProductMonthRent(augmentedProductMonthRent);
                simulationData.setAugmentedProductTradePrice(augmentedProductTradePrice);
                if (!TextUtils.isEmpty(augmentedProductPic1)) {
                    simulationData.setAugmentedProductPic1(augmentedProductPic1);
                }
                if (!TextUtils.isEmpty(augmentedProductPic2)) {
                    simulationData.setAugmentedProductPic2(augmentedProductPic2);
                }
                if (!TextUtils.isEmpty(augmentedProductPic3)) {
                    simulationData.setAugmentedProductPic3(augmentedProductPic3);
                }
                if (!TextUtils.isEmpty(augmentedProductSpecText)) {
                    simulationData.setAugmentedProductSpecText(augmentedProductSpecText);
                }
                if (0 != augmentedProductHeight) {
                    simulationData.setAugmentedProductHeight(augmentedProductHeight);
                }
                simulationData.setAugmentedProductOffsetRatio(augmentedProductOffsetRatio);
                simulationData.setAugmentedCombinationType(augmentedCombinationType);
                if (!TextUtils.isEmpty(augmentedPriceCode)) {
                    simulationData.setAugmentedPriceCode(augmentedPriceCode);
                }
                if (!TextUtils.isEmpty(augmentedTradePriceCode)) {
                    simulationData.setAugmentedTradePriceCode(augmentedTradePriceCode);
                }
                /*----------*/
                simulationData.setTimeStamp(TimeUtil.getNanoTime());
                if (judeFileExists(picturePath)) {
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
                callBack.onSaveSimulationDataSuccess();
            } else if (4 == type) {
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
                    /*----------*/
                    quotationData.setAugmentedProductSkuId(augmentedProductSkuId);
                    if (!TextUtils.isEmpty(augmentedProductName)) {
                        quotationData.setAugmentedProductName(augmentedProductName);
                    }
                    quotationData.setAugmentedProductPrice(augmentedProductPrice);
                    quotationData.setAugmentedProductMonthRent(augmentedProductMonthRent);
                    quotationData.setAugmentedProductTradePrice(augmentedProductTradePrice);
                    if (!TextUtils.isEmpty(augmentedProductPic1)) {
                        quotationData.setAugmentedProductPic1(augmentedProductPic1);
                    }
                    if (!TextUtils.isEmpty(augmentedProductPic2)) {
                        quotationData.setAugmentedProductPic2(augmentedProductPic2);
                    }
                    if (!TextUtils.isEmpty(augmentedProductPic3)) {
                        quotationData.setAugmentedProductPic3(augmentedProductPic3);
                    }
                    if (!TextUtils.isEmpty(augmentedProductSpecText)) {
                        quotationData.setAugmentedProductSpecText(augmentedProductSpecText);
                    }
                    if (0 != augmentedProductHeight) {
                        quotationData.setAugmentedProductHeight(augmentedProductHeight);
                    }
                    quotationData.setAugmentedCombinationType(augmentedCombinationType);
                    if (!TextUtils.isEmpty(augmentedPriceCode)) {
                        quotationData.setAugmentedPriceCode(augmentedPriceCode);
                    }
                    if (!TextUtils.isEmpty(augmentedTradePriceCode)) {
                        quotationData.setAugmentedTradePriceCode(augmentedTradePriceCode);
                    }
                    /*----------*/
                    quotationData.setNumber(1);
                    quotationData.setTimeStamp(TimeUtil.getNanoTime());
                    if (judeFileExists(picturePath)) {
                        quotationData.setPicturePath(picturePath);
                    }
                    /*----------*/
                    dbManager.save(quotationData);
                    // 提示用户添加成功
                    callBack.onSaveQuotationDataDataSuccess();
                } else {
                    // 已经报过价
                    int number = quotation.getNumber();
                    // 数量+1
                    quotation.setNumber(++number);
                    quotation.setTimeStamp(TimeUtil.getNanoTime());
                    String picPath = quotation.getPicturePath();
                    if (!judeFileExists(picPath) && judeFileExists(picturePath)) {
                        quotation.setPicturePath(picturePath);
                    }
                    // 更新quotation列名为number,picturePath,timeStamp的数据
                    dbManager.update(quotation, "number", "timeStamp", "picturePath");
                    // 提示用户添加成功
                    callBack.onSaveQuotationDataDataSuccess();
                }
            }
        } catch (DbException e) {
            e.printStackTrace();
            callBack.onSaveSimulationOrQuotationDataFairly();
        }
    }

    /**
     * sku加入待摆放清单
     *
     * @param firstSkuId  主产品 skuId
     * @param secondSkuId 附加产品 skuId
     * @param params      组合图片
     * @param callBack    回调方法
     */
    @Override
    public void addQuotation(int firstSkuId, int secondSkuId, Map<String, RequestBody> params, CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<AddQuotation> observable = mApiService.addQuotation(timeStamp,
                OtherUtil.getSign(timeStamp, ADD_QUOTATION_METHOD),
                YiBaiApplication.getUid(),
                YiBaiApplication.getCid(),
                firstSkuId,
                secondSkuId,
                params);
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

    @Override
    public void addPurcart(int productSkuId, int augmentedProductSkuId, Map<String, RequestBody> params, CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<BaseBean> observable = mApiService.addPurcart(timeStamp,
                OtherUtil.getSign(timeStamp, ADD_PURCART_METHOD),
                YiBaiApplication.getUid(),
                productSkuId,
                augmentedProductSkuId,
                0, 0,
                params);
        Observer<BaseBean> observer = new Observer<BaseBean>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(BaseBean baseBean) {
                if (baseBean.getCode() == 200) {
                    AddQuotation addQuotation = new AddQuotation();
                    addQuotation.setMsg(baseBean.getMsg());
                    addQuotation.setCode(baseBean.getCode());
                    callBack.onAddQuotationSuccess(addQuotation);
                } else {
                    callBack.onRequestFailure(new Throwable(baseBean.getMsg()));
                }

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
