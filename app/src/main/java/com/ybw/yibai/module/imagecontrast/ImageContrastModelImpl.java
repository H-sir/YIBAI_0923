package com.ybw.yibai.module.imagecontrast;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.bean.ListBean;
import com.ybw.yibai.common.bean.Recommend;
import com.ybw.yibai.common.bean.SceneInfo;
import com.ybw.yibai.common.bean.SimulationData;
import com.ybw.yibai.common.interfaces.ApiService;
import com.ybw.yibai.common.utils.LogUtil;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.utils.RetrofitManagerUtil;
import com.ybw.yibai.common.utils.TimeUtil;
import com.ybw.yibai.module.imagecontrast.ImageContrastContract.CallBack;
import com.ybw.yibai.module.imagecontrast.ImageContrastContract.ImageContrastModel;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.math.BigDecimal;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.ybw.yibai.common.constants.HttpUrls.GET_NEWRECOMMEND_METHOD;
import static com.ybw.yibai.common.constants.HttpUrls.GET_RECOMMEND_METHOD;
import static com.ybw.yibai.common.constants.Preferences.PLANT;
import static com.ybw.yibai.common.utils.FileUtil.judeFileExists;
import static com.ybw.yibai.common.utils.ImageUtil.getLocalBitmap;

/**
 * 多张搭配图片模拟效果对比Model实现类
 *
 * @author sjl
 * @date 2019/11/8
 */
public class ImageContrastModelImpl implements ImageContrastModel {

    public static final String TAG = "SceneEditModelImpl";

    private ApiService mApiService;

    public ImageContrastModelImpl() {
        RetrofitManagerUtil instance = RetrofitManagerUtil.getInstance();
        mApiService = instance.getApiService();
    }

    /**
     * 换搭配获取植物/花盆列表
     *
     * @param cateCode   类别:plant,获取植物 pot获取盆
     * @param plantSkuId 植物SkuId
     * @param potSkuId   盆SkuId
     * @param specType   大中小id(此参数仅在获取花盆时有效)
     * @param pCid       分类id(多个用逗号拼接)
     * @param specId     规格id(同类别规格用逗号分隔,不同类别用|分隔) 例:specId=11,12|15,16
     * @param attrId     属性id,(格式同规格)
     * @param height     搭配高度范围,用|分隔 例:height=5|20
     * @param diameter   搭配口径范围,格式同上
     * @param callBack   回调方法
     */
    @Override
    public void getRecommend(String cateCode, Integer plantSkuId, Integer potSkuId, Integer specType,
                             Integer pCid, String specId, String attrId, String height, String diameter, CallBack callBack) {

        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<Recommend> observable = mApiService.getRecommend(timeStamp,
                OtherUtil.getSign(timeStamp, GET_RECOMMEND_METHOD),
                YiBaiApplication.getUid(),
                cateCode,
                plantSkuId,
                potSkuId,
                specType,
                pCid,
                specId,
                attrId,
                height,
                diameter,
                1);
        Observer<Recommend> observer = new Observer<Recommend>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(Recommend recommend) {
                callBack.onGetRecommendSuccess(recommend);
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
    public void getNewRecommed(String cateCode, int productSkuId, int augmentedProductSkuId, int potTypeId, CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<Recommend> observable;
        observable = mApiService.getNewRecommend(timeStamp,
                OtherUtil.getSign(timeStamp, GET_NEWRECOMMEND_METHOD),
                YiBaiApplication.getUid(),
                cateCode,
                productSkuId,
                augmentedProductSkuId,
                potTypeId,
                "v2",
                "no");
        Observer<Recommend> observer = new Observer<Recommend>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(Recommend recommend) {
                if (recommend.getCode() == 200) {
                    callBack.onGetRecommendSuccess(recommend);
                } else {
                    callBack.onRequestFailure(new Throwable(recommend.getMsg()));
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

    /**
     * 添加到模拟列表
     *
     * @param simulationData 贴纸数据
     * @param productInfo    推荐产品
     * @param picturePath    组合图片在本地保存的路径
     * @param callBack       回调方法
     */
    @Override
    public void addSimulationData(SimulationData simulationData, ListBean productInfo, String picturePath, CallBack callBack) {
        int productSkuId;
        String productName;
        double productPrice;
        double productMonthRent;
        double productTradePrice;
        String productPic1;
        String productPic2;
        String productPic3;
        double productHeight;
        int productCombinationType;
        String productPriceCode;
        String productTradePriceCode;

        int augmentedProductSkuId;
        String augmentedProductName;
        double augmentedProductPrice;
        double augmentedProductMonthRent;
        double augmentedProductTradePrice;
        String augmentedProductPic1;
        String augmentedProductPic2;
        String augmentedProductPic3;
        double augmentedProductHeight;
        double augmentedProductOffsetRatio;
        int augmentedCombinationType;
        String augmentedPriceCode;
        String augmentedTradePriceCode;

        String categoryCode = productInfo.getCategoryCode();
        if (PLANT.equals(categoryCode)) {
            productSkuId = productInfo.getSku_id();
            productName = productInfo.getName();
            productPrice = productInfo.getPrice();
            productMonthRent = productInfo.getMonth_rent();
            productTradePrice = productInfo.getTrade_price();
            productPic1 = productInfo.getPic1();
            productPic2 = productInfo.getPic2();
            productPic3 = productInfo.getPic3();
            productHeight = productInfo.getHeight();
            productCombinationType = productInfo.getComtype();
            productPriceCode = productInfo.getPrice_code();
            productTradePriceCode = productInfo.getTrade_price_code();

            augmentedProductSkuId = simulationData.getAugmentedProductSkuId();
            augmentedProductName = simulationData.getAugmentedProductName();
            augmentedProductPrice = simulationData.getAugmentedProductPrice();
            augmentedProductMonthRent = simulationData.getAugmentedProductMonthRent();
            augmentedProductTradePrice = simulationData.getAugmentedProductTradePrice();
            augmentedProductPic1 = simulationData.getAugmentedProductPic1();
            augmentedProductPic2 = simulationData.getAugmentedProductPic2();
            augmentedProductPic3 = simulationData.getAugmentedProductPic3();
            augmentedProductHeight = simulationData.getAugmentedProductHeight();
            augmentedProductOffsetRatio = simulationData.getAugmentedProductOffsetRatio();
            augmentedCombinationType = simulationData.getAugmentedCombinationType();
            augmentedPriceCode = simulationData.getAugmentedPriceCode();
            augmentedTradePriceCode = simulationData.getAugmentedTradePriceCode();
        } else {
            productSkuId = simulationData.getAugmentedProductSkuId();
            productName = simulationData.getAugmentedProductName();
            productPrice = simulationData.getAugmentedProductPrice();
            productMonthRent = simulationData.getAugmentedProductMonthRent();
            productTradePrice = simulationData.getAugmentedProductTradePrice();
            productPic1 = simulationData.getAugmentedProductPic1();
            productPic2 = simulationData.getAugmentedProductPic2();
            productPic3 = simulationData.getAugmentedProductPic3();
            productHeight = simulationData.getAugmentedProductHeight();
            productCombinationType = simulationData.getAugmentedCombinationType();
            productPriceCode = simulationData.getAugmentedPriceCode();
            productTradePriceCode = simulationData.getAugmentedTradePriceCode();

            augmentedProductSkuId = productInfo.getSku_id();
            augmentedProductName = productInfo.getName();
            augmentedProductPrice = productInfo.getPrice();
            augmentedProductMonthRent = productInfo.getMonth_rent();
            augmentedProductTradePrice = productInfo.getTrade_price();
            augmentedProductPic1 = productInfo.getPic1();
            augmentedProductPic2 = productInfo.getPic2();
            augmentedProductPic3 = productInfo.getPic3();
            augmentedProductHeight = productInfo.getHeight();
            augmentedProductOffsetRatio = productInfo.getOffset_ratio();
            augmentedCombinationType = productInfo.getComtype();
            augmentedPriceCode = productInfo.getPrice_code();
            augmentedTradePriceCode = productInfo.getTrade_price_code();
        }

        int uid = YiBaiApplication.getUid();
        String finallySkuId = String.valueOf(productSkuId) + augmentedProductSkuId + uid;

        float oldX = simulationData.getX();
        float oldY = simulationData.getY();
        // 更换搭配之前贴纸实际的宽度
        double oldWidth = simulationData.getWidth();
        // 更换搭配之前贴纸实际的高度
        double oldHeight = simulationData.getHeight();
        // 更换搭配之前贴纸的X轴的缩放比例
        double oldXScale = simulationData.getxScale();
        // 更换搭配之前贴纸的Y轴的缩放比例
        double oldYScale = simulationData.getyScale();
        // 更换搭配之前贴纸显示的宽度
        double showWidth = oldWidth * oldXScale;
        // 更换搭配之前贴纸显示的高度
        double showHeight = oldHeight * oldYScale;

        Bitmap bitmap = getLocalBitmap(picturePath);
        // bitmap 宽度
        int width = bitmap.getWidth();
        // bitmap 高度
        int height = bitmap.getHeight();

        // 新合成的图片Y轴的缩放?就与更换搭配之前贴纸显示的高度一样
        BigDecimal yBigDecimal = new BigDecimal((float) showHeight / height);
        double yScale = yBigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

        LogUtil.e(TAG, "主产品的款名Id: " + productSkuId);
        LogUtil.e(TAG, "主产品名称: " + productName);
        LogUtil.e(TAG, "主产品价格: " + productPrice);
        LogUtil.e(TAG, "主产品月租: " + productMonthRent);
        LogUtil.e(TAG, "主产品批发价: " + productTradePrice);
        LogUtil.e(TAG, "主产品主图url地址: " + productPic1);
        LogUtil.e(TAG, "主产品模拟图url地址: " + productPic2);
        LogUtil.e(TAG, "主产品配图url地址: " + productPic3);
        LogUtil.e(TAG, "主产品的高度: " + productHeight);
        LogUtil.e(TAG, "主产品组合模式: 1组合2单产品: " + productCombinationType);
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
        LogUtil.e(TAG, "附加产品的高度: " + augmentedProductHeight);
        LogUtil.e(TAG, "附加产品组合模式: 1组合2单产品: " + augmentedCombinationType);
        LogUtil.e(TAG, "附加产品售价代码: " + augmentedPriceCode);
        LogUtil.e(TAG, "附加产品批发价代码: " + augmentedTradePriceCode);

        LogUtil.e(TAG, "主产品的款名Id+附加产品的款名Id的组合: " + finallySkuId);
        LogUtil.e(TAG, "偏移量(如果该产品是花盆): " + augmentedProductOffsetRatio);
        LogUtil.e(TAG, "保存Bitmap为图片到本地的路径: " + picturePath);

        try {
            // 模拟(在这里可以添加finallySkuId相同的盆栽)
            SimulationData newSimulationData = new SimulationData();
            newSimulationData.setUid(uid);

            DbManager dbManager = YiBaiApplication.getDbManager();
            // 查找当前正在编辑的这一个场景
            List<SceneInfo> defaultSceneInfoList = dbManager.selector(SceneInfo.class)
                    .where("uid", "=", uid)
                    .and("editScene", "=", true)
                    .findAll();
            if (null != defaultSceneInfoList && defaultSceneInfoList.size() > 0) {
                long sceneId = defaultSceneInfoList.get(0).getSceneId();
                newSimulationData.setSceneId(sceneId);
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
            if (0 != augmentedProductHeight) {
                simulationData.setAugmentedProductHeight(augmentedProductHeight);
            }
            simulationData.setAugmentedProductOffsetRatio(augmentedProductOffsetRatio);
            simulationData.setProductCombinationType(augmentedCombinationType);
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
            simulationData.setX(oldX);
            simulationData.setY(oldY);
            simulationData.setWidth(width);
            simulationData.setHeight(height);
            simulationData.setxScale(yScale);
            simulationData.setyScale(yScale);
            /*----------*/
            dbManager.save(simulationData);
            // 提示用户添加成功
        } catch (DbException e) {

        }
    }
}
