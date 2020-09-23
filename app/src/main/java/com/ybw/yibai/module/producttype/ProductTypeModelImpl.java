package com.ybw.yibai.module.producttype;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.bean.ListBean;
import com.ybw.yibai.common.bean.SKUList;
import com.ybw.yibai.common.bean.SceneInfo;
import com.ybw.yibai.common.bean.SimulationData;
import com.ybw.yibai.common.interfaces.ApiService;
import com.ybw.yibai.common.utils.ImageUtil;
import com.ybw.yibai.common.utils.LogUtil;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.utils.RetrofitManagerUtil;
import com.ybw.yibai.common.utils.TimeUtil;
import com.ybw.yibai.module.producttype.ProductTypeContract.CallBack;
import com.ybw.yibai.module.producttype.ProductTypeContract.ProductTypeModel;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.ybw.yibai.common.constants.HttpUrls.GET_SUK_LIST_METHOD;
import static com.ybw.yibai.common.constants.Preferences.PLANT;
import static com.ybw.yibai.common.utils.FileUtil.judeFileExists;

/**
 * 产品类型Model实现类
 *
 * @author sjl
 * @date 2019/9/5
 */
public class ProductTypeModelImpl implements ProductTypeModel {

    private final String TAG = "ProductTypeModelImpl";

    private ApiService mApiService;

    public ProductTypeModelImpl() {
        RetrofitManagerUtil instance = RetrofitManagerUtil.getInstance();
        mApiService = instance.getApiService();
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
     * @param callBack 回调方法
     */
    @Override
    public void getSKUList(int isAll, int useState, String cateCode, String pcId, String keyWord, Map<String, String> param, CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<SKUList> observable;
        if (null == param || param.size() == 0) {
            observable = mApiService.getSKUList(timeStamp,
                    OtherUtil.getSign(timeStamp, GET_SUK_LIST_METHOD),
                    YiBaiApplication.getUid(),
                    isAll,
                    cateCode,
                    useState,
                    pcId,
                    null,
                    null,
                    null,
                    keyWord,
                    null,
                    null);
        } else {
            observable = mApiService.getSKUList(timeStamp,
                    OtherUtil.getSign(timeStamp, GET_SUK_LIST_METHOD),
                    YiBaiApplication.getUid(),
                    isAll,
                    cateCode,
                    useState,
                    pcId,
                    keyWord,
                    param, null,
                    null);
        }
        Observer<SKUList> observer = new Observer<SKUList>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(SKUList skuList) {
                callBack.onGetSKUListSuccess(skuList);
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
     * 保存到模拟列表数据
     *
     * @param plantBean 用户当前选择的植物信息
     * @param bitmap    植物位图
     * @param callBack  回调方法
     */
    @Override
    public void saveSimulation(ListBean plantBean, Bitmap bitmap, CallBack callBack) {
        int width = 0;
        int height = 0;
        String picturePath = null;
        if (null != bitmap) {
            width = bitmap.getWidth();
            height = bitmap.getHeight();
            // 保存Bitmap为图片到本地
            picturePath = ImageUtil.saveImage(bitmap, String.valueOf(TimeUtil.getTimeStamp()));
        }

        String cateCode = plantBean.getCategoryCode();
        int productSkuId = plantBean.getSku_id();
        String productName = plantBean.getName();
        double productPrice = plantBean.getPrice();
        double productMonthRent = plantBean.getMonth_rent();
        double productTradePrice = plantBean.getTrade_price();
        String productPic1 = plantBean.getPic1();
        String productPic2 = plantBean.getPic2();
        String productPic3 = plantBean.getPic3();
        double productHeight = plantBean.getHeight();
        int productCombinationType = plantBean.getComtype();
        String productPriceCode = plantBean.getPrice_code();
        String productTradePriceCode = plantBean.getTrade_price_code();

        int uid = YiBaiApplication.getUid();
        String finallySkuId = String.valueOf(productSkuId) + uid;

        LogUtil.e(TAG, "产品类别: " + cateCode);
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
            callBack.onSaveSimulationResult(true);
        } catch (DbException e) {
            e.printStackTrace();
            callBack.onSaveSimulationResult(false);
        }
    }
}