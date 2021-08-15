package com.ybw.yibai.module.collection;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.bean.AddQuotation;
import com.ybw.yibai.common.bean.BaseBean;
import com.ybw.yibai.common.bean.CollectionListBean;
import com.ybw.yibai.common.bean.ProductScreeningParam;
import com.ybw.yibai.common.bean.SceneInfo;
import com.ybw.yibai.common.bean.SimulationData;
import com.ybw.yibai.common.bean.SkuDetailsBean;
import com.ybw.yibai.common.bean.UpdateSKUUseState;
import com.ybw.yibai.common.interfaces.ApiService;
import com.ybw.yibai.common.network.response.BaseResponse;
import com.ybw.yibai.common.utils.ImageUtil;
import com.ybw.yibai.common.utils.LogUtil;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.utils.RetrofitManagerUtil;
import com.ybw.yibai.common.utils.TimeUtil;
import com.ybw.yibai.module.productusestate.ProductUseStateContract.CallBack;
import com.ybw.yibai.module.productusestate.ProductUseStateContract.ProductUseStateModel;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.util.Iterator;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.ybw.yibai.common.constants.HttpUrls.ADD_PURCART_METHOD;
import static com.ybw.yibai.common.constants.HttpUrls.DELETE_COLLECT_METHOD;
import static com.ybw.yibai.common.constants.HttpUrls.GET_COLLECT_METHOD;
import static com.ybw.yibai.common.constants.HttpUrls.GET_PRODUCT_SCREENING_PARAM_METHOD;
import static com.ybw.yibai.common.constants.HttpUrls.GET_SKU_LIST_IDS_METHOD;
import static com.ybw.yibai.common.constants.HttpUrls.UPDATE_SKU_USE_STATE_METHOD;
import static com.ybw.yibai.common.constants.Preferences.PLANT;
import static com.ybw.yibai.common.utils.FileUtil.judeFileExists;

/**
 * 产品使用状态Model实现类
 *
 * @author sjl
 * @date 2019/9/5
 */
public class CollectionLayoutModelImpl implements CollectionLayoutContract.CollectionLayoutModel {

    private final String TAG = "CollectionLayoutModelImpl";

    private ApiService mApiService;

    public CollectionLayoutModelImpl() {
        RetrofitManagerUtil instance = RetrofitManagerUtil.getInstance();
        mApiService = instance.getApiService();
    }

    /**
     * 获取收藏列表
     */
    @Override
    public void getCollect(int type, int page, CollectionLayoutContract.CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<CollectionListBean> observable = mApiService.getCollection(timeStamp,
                OtherUtil.getSign(timeStamp, GET_COLLECT_METHOD),
                YiBaiApplication.getUid(), type, page, 10);
        Observer<CollectionListBean> observer = new Observer<CollectionListBean>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(CollectionListBean collectionListBean) {
                callBack.onCollectionListBeanSuccess(collectionListBean);
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
    public void deleteCollection(List<String> skuOrCollectId, CollectionLayoutContract.CallBack callBack) {
        String collectId = "";
        for (Iterator<String> iterator = skuOrCollectId.iterator(); iterator.hasNext(); ) {
            String next = iterator.next();
            collectId = next + ",";
        }
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<BaseBean> observable = mApiService.deleteCollection(timeStamp,
                OtherUtil.getSign(timeStamp, DELETE_COLLECT_METHOD),
                YiBaiApplication.getUid(), collectId.substring(0, collectId.length() - 1)
                , "v2", "no");
        Observer<BaseBean> observer = new Observer<BaseBean>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(BaseBean baseBean) {
                callBack.onDeleteCollectionListSuccess(skuOrCollectId);
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
    public void upuseskuCollection(List<String> skuOrCollectId, CollectionLayoutContract.CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<UpdateSKUUseState> observable = mApiService.updateSKUUseState(timeStamp,
                OtherUtil.getSign(timeStamp, UPDATE_SKU_USE_STATE_METHOD),
                YiBaiApplication.getUid(), Integer.parseInt(skuOrCollectId.get(0)));
        Observer<UpdateSKUUseState> observer = new Observer<UpdateSKUUseState>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(UpdateSKUUseState updateSKUUseState) {
                callBack.onDeleteCollectionListSuccess(skuOrCollectId);
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
    public void getSkuListIds(String sku_id, String pot_sku_id, CollectionLayoutContract.CallBack callBack) {
        String skuIds = "";
        if (pot_sku_id.isEmpty()) {
            skuIds = sku_id;
        } else {
            skuIds = sku_id + "," + pot_sku_id;
        }
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<SkuDetailsBean> observable = mApiService.getSkuDetails(timeStamp,
                OtherUtil.getSign(timeStamp, GET_SKU_LIST_IDS_METHOD),
                YiBaiApplication.getUid(),
                skuIds, "v2", "no");
        Observer<SkuDetailsBean> observer = new Observer<SkuDetailsBean>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(SkuDetailsBean skuDetailsBean) {
                if (skuDetailsBean.getCode() == 200) {
                    callBack.onGetProductDetailsSuccess(skuDetailsBean);
                } else {
                    callBack.onRequestFailure(new Throwable(skuDetailsBean.getMsg()));
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

    @Override
    public void saveSimulation(SkuDetailsBean.DataBean.ListBean listBean, Bitmap bitmap, CollectionLayoutContract.CallBack callBack) {
        int width = 0;
        int height = 0;
        String picturePath = null;
        if (null != bitmap) {
            width = bitmap.getWidth();
            height = bitmap.getHeight();
            // 保存Bitmap为图片到本地
            picturePath = ImageUtil.saveImage(bitmap, String.valueOf(TimeUtil.getTimeStamp()));
        }

        String cateCode = listBean.getCategoryCode();
        int productSkuId = listBean.getSku_id();
        String productName = listBean.getName();
        double productPrice = listBean.getPrice();
        double productMonthRent = listBean.getMonth_rent();
        double productTradePrice = listBean.getTrade_price();
        String productPic1 = listBean.getPic1();
        String productPic2 = listBean.getPic2();
        String productPic3 = listBean.getPic3();
        double productHeight = listBean.getHeight();
        int productCombinationType = listBean.getComtype();
        String productPriceCode = String.valueOf(listBean.getPrice_code());
        String productTradePriceCode = listBean.getTrade_price_code();

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