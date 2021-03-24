package com.ybw.yibai.module.sceneedit;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.Display;

import com.google.gson.Gson;
import com.ybw.yibai.R;
import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.bean.AddQuotation;
import com.ybw.yibai.common.bean.BTCBean;
import com.ybw.yibai.common.bean.BaseBean;
import com.ybw.yibai.common.bean.CategorySimilarSKU;
import com.ybw.yibai.common.bean.CreateSceneData;
import com.ybw.yibai.common.bean.FastImport;
import com.ybw.yibai.common.bean.ListBean;
import com.ybw.yibai.common.bean.ProductData;
import com.ybw.yibai.common.bean.ProductDetails;
import com.ybw.yibai.common.bean.QuotationData;
import com.ybw.yibai.common.bean.Recommend;
import com.ybw.yibai.common.bean.SceneInfo;
import com.ybw.yibai.common.bean.SimulationData;
import com.ybw.yibai.common.bean.SpecSuk;
import com.ybw.yibai.common.bean.UserPosition;
import com.ybw.yibai.common.helper.SceneHelper;
import com.ybw.yibai.common.interfaces.ApiService;
import com.ybw.yibai.common.model.CreateDesignModel;
import com.ybw.yibai.common.model.CreateSceneOrPicModel;
import com.ybw.yibai.common.model.ItemDesignSceneModel;
import com.ybw.yibai.common.network.response.BaseResponse;
import com.ybw.yibai.common.utils.FileUtil;
import com.ybw.yibai.common.utils.ImageUtil;
import com.ybw.yibai.common.utils.LogUtil;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.utils.RetrofitManagerUtil;
import com.ybw.yibai.common.utils.TimeUtil;
import com.ybw.yibai.module.sceneedit.SceneEditContract.CallBack;
import com.ybw.yibai.module.sceneedit.SceneEditContract.SceneEditModel;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.ybw.yibai.common.constants.HttpUrls.ADD_PURCART_METHOD;
import static com.ybw.yibai.common.constants.HttpUrls.ADD_QUOTATION_METHOD;
import static com.ybw.yibai.common.constants.HttpUrls.EDIT_SCHEME_METHOD;
import static com.ybw.yibai.common.constants.HttpUrls.FAST_IMPORT_METHOD;
import static com.ybw.yibai.common.constants.HttpUrls.GET_CATEGORY_SIMILAR_SUK_METHOD;
import static com.ybw.yibai.common.constants.HttpUrls.GET_NEWRECOMMEND_METHOD;
import static com.ybw.yibai.common.constants.HttpUrls.GET_PRODUCT_INFO_METHOD;
import static com.ybw.yibai.common.constants.HttpUrls.GET_RECOMMEND_METHOD;
import static com.ybw.yibai.common.constants.HttpUrls.GET_SPEC_SUK_METHOD;
import static com.ybw.yibai.common.constants.HttpUrls.SET_USER_POSITION_METHOD;
import static com.ybw.yibai.common.constants.Preferences.PLANT;
import static com.ybw.yibai.common.utils.FileUtil.judeFileExists;

/**
 * 场景编辑Model实现类
 *
 * @author sjl
 * @date 2019/9/23
 */
public class SceneEditModelImpl implements SceneEditModel {

    public static final String TAG = "SceneEditModelImpl";

    private ApiService mApiService;

    public SceneEditModelImpl() {
        RetrofitManagerUtil instance = RetrofitManagerUtil.getInstance();
        mApiService = instance.getApiService();
    }

    /**
     * 根据场景id查找保存用户的"模拟搭配产品"数据
     *
     * @param sceneId  场景id
     * @param callBack 回调方法
     */
    @Override
    public void getSimulationData(long sceneId, CallBack callBack) {
        try {
            DbManager dbManager = YiBaiApplication.getDbManager();
            List<SimulationData> simulationDataList = dbManager.selector(SimulationData.class)
                    .where("uid", "=", YiBaiApplication.getUid())
                    .and("sceneId", "=", sceneId)
                    .findAll();
            callBack.onGetSimulationDataSuccess(simulationDataList);

            LogUtil.e(TAG, "SimulationDataList: " + new Gson().toJson(simulationDataList));
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新当前场景的信息
     *
     * @param sceneInfo 当前场景的信息
     * @param callBack  回调方法
     */
    @Override
    public void updateSceneInfo(SceneInfo sceneInfo, CallBack callBack) {
        try {
            DbManager dbManager = YiBaiApplication.getDbManager();
            // 更新SceneInfo列名为sceneBackground的数据
            dbManager.update(sceneInfo, "sceneBackground");
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将场景中产品一键创建并导入服务器中
     *
     * @param placeName  位置名称
     * @param jsonString 产品数据(jsonObj)格式
     * @param params     位置场景图,单图模式
     * @param parts      产品组合图,多图模式(注意不要打乱顺序)
     * @param callBack   回调方法
     */
    @Override
    public void fastImport(String placeName, String jsonString, Map<String, RequestBody> params, MultipartBody.Part[] parts, CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<FastImport> observable = mApiService.fastImport(timeStamp,
                OtherUtil.getSign(timeStamp, FAST_IMPORT_METHOD),
                YiBaiApplication.getUid(),
                YiBaiApplication.getCid(),
                placeName,
                jsonString,
                params,
                parts);
        Observer<FastImport> observer = new Observer<FastImport>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(FastImport fastImport) {
                callBack.onFastImportSuccess(fastImport);
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
     * 根据大中小随机获取组合
     *
     * @param type     规格大中小id
     * @param callBack 回调方法
     */
    @Override
    public void getSpecSuk(int type, CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<SpecSuk> observable = mApiService.getSpecSuk(timeStamp,
                OtherUtil.getSign(timeStamp, GET_SPEC_SUK_METHOD),
                YiBaiApplication.getUid(),
                type);
        Observer<SpecSuk> observer = new Observer<SpecSuk>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(SpecSuk specSuk) {
                callBack.onGetSpecSukSuccess(specSuk);
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
     * 获取相似类型sku列表
     *
     * @param skuId    产品SKUid
     * @param callBack 回调方法
     */
    @Override
    public void getCategorySimilarSKUList(int skuId, CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<CategorySimilarSKU> observable = mApiService.getCategorySimilarSKUList(timeStamp,
                OtherUtil.getSign(timeStamp, GET_CATEGORY_SIMILAR_SUK_METHOD),
                YiBaiApplication.getUid(),
                skuId);
        Observer<CategorySimilarSKU> observer = new Observer<CategorySimilarSKU>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(CategorySimilarSKU categorySimilarSKU) {
                callBack.onGetCategorySimilarSKUListSuccess(categorySimilarSKU);
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
     * 换搭配获取植物/花盆列表
     *
     * @param cateCode   类别:plant,获取植物 pot获取盆
     * @param plantSkuId 植物SkuId
     * @param potSkuId   盆SkuId
     * @param specType   大中小id(此参数仅在获取花盆时有效)
     * @param pCid       分类id(多个用逗号拼接)
     * @param param      动态设置的参数
     * @param callBack   回调方法
     */
    @Override
    public void getRecommend(String cateCode, Integer plantSkuId, Integer potSkuId, Integer specType,
                             Integer pCid, Map<String, String> param, CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<Recommend> observable;
        if (null == param || param.size() == 0) {
            observable = mApiService.getRecommend(timeStamp,
                    OtherUtil.getSign(timeStamp, GET_RECOMMEND_METHOD),
                    YiBaiApplication.getUid(),
                    cateCode,
                    plantSkuId,
                    potSkuId,
                    specType,
                    pCid,
                    null,
                    null,
                    null,
                    null,
                    null);
        } else {
            observable = mApiService.getRecommend(timeStamp,
                    OtherUtil.getSign(timeStamp, GET_RECOMMEND_METHOD),
                    YiBaiApplication.getUid(),
                    cateCode,
                    plantSkuId,
                    potSkuId,
                    specType,
                    pCid,
                    param,
                    null);
        }
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
    public void getNewRecommend(String cateCode, Integer plantSkuId, Integer potSkuId,
                                Integer specType, Integer pCid, String attrid, Map<String, String> param,
                                CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<Recommend> observable;
        if (null == param || param.size() == 0) {
            observable = mApiService.getNewRecommend(timeStamp,
                    OtherUtil.getSign(timeStamp, "mybw.newrecommend"),
                    YiBaiApplication.getUid(), cateCode, plantSkuId, potSkuId,
                    specType, null, null, null);
        } else {
            observable = mApiService.getNewRecommend(timeStamp,
                    OtherUtil.getSign(timeStamp, "mybw.newrecommend"),
                    YiBaiApplication.getUid(), cateCode, plantSkuId, potSkuId, specType,
                    String.valueOf(pCid), attrid, param);
        }
        Observer<Recommend> observer = new Observer<Recommend>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(Recommend recommend) {
                if (recommend.getCode() == 200) {
                    callBack.onGetNewRecommendSuccess(recommend);
                } else callBack.onRequestFailure(new Throwable(recommend.getMsg()));
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
     * 点击新增按钮时获取搭配列表
     *
     * @param specTypeId 只传盆栽规格类型
     */
    @Override
    public void getNewRecommedByAddSpec(int specTypeId, CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<Recommend> observable = mApiService.getNewRecommend(timeStamp,
                OtherUtil.getSign(timeStamp, GET_NEWRECOMMEND_METHOD),
                YiBaiApplication.getUid(),
                specTypeId,
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
                    recommend.getData().setSpecTypeId(specTypeId);
                    callBack.onGetRecommendByAddSpecSuccess(recommend);
                } else if (recommend.getCode() == 201) {
                    callBack.onGetRecommendByAddSpecSuccess(recommend);
                } else callBack.onRequestFailure(new Throwable(recommend.getMsg()));
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
    public void getNewRecommedChangeStyle(int productSkuId, int augmentedProductSkuId, CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<Recommend> observable = mApiService.getNewRecommedChangeStyle(timeStamp,
                OtherUtil.getSign(timeStamp, GET_NEWRECOMMEND_METHOD),
                YiBaiApplication.getUid(),
                productSkuId,
                augmentedProductSkuId,
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


    @Override
    public void getNewRecommed(String cateCode, int plantSkuId, int potSkuId, int specType, String specid, String attrid, Map<String, String> param, CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<Recommend> observable;
        observable = mApiService.getNewRecommend(timeStamp,
                OtherUtil.getSign(timeStamp, GET_NEWRECOMMEND_METHOD),
                YiBaiApplication.getUid(),
                cateCode,
                plantSkuId,
                specType,
                specid,
                attrid,
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
     * 更新背景图片
     */
    @Override
    public void updateSceneBg(String absolutePath, String sceneName, String scheme_id, CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        int uid = YiBaiApplication.getUid();
        Observable<BaseBean> observable = mApiService.editScheme(timeStamp,
                OtherUtil.getSign(timeStamp, EDIT_SCHEME_METHOD),
                uid, sceneName, scheme_id, getBgPicByPath(absolutePath));
        Observer<BaseBean> observer = new Observer<BaseBean>() {
            @Override
            public void onSubscribe(Disposable d) {
//                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(BaseBean baseBean) {
                if (baseBean.getCode() == 200) {
                    callBack.updateSceneBgSuccess();
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

    private MultipartBody.Part getBgPicByPath(String path) {
        File file = new File(path);
        String fileName = file.getName();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/" + suffix), file);
        MultipartBody.Part parts = MultipartBody.Part.createFormData("bgpic", file.getName(), requestBody);
        return parts;
    }

    /**
     * 将"模拟搭配产品"数据添加到报价列表数据中
     *
     * @param simulationData "模拟搭配产品"数据
     * @param callBack       回调方法
     */
    @Override
    public void addQuotationData(SimulationData simulationData, CallBack callBack) {
        int productSkuId = simulationData.getProductSkuId();
        int augmentedProductSkuId = simulationData.getAugmentedProductSkuId();
        String picturePath = simulationData.getPicturePath();

        File file = new File(picturePath);
        Map<String, RequestBody> params = new HashMap<>();
        String fileName = file.getName();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/" + suffix), file);
        params.put("com_pic\"; filename=\"" + file.getName(), requestBody);

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

    @Override
    public void addQuotationData(int productSkuId, int augmentedProductSkuId, Map<String, RequestBody> params, CallBack callBack) {
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

    @Override
    public void getProductDetails(int productSkuId, CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<ProductDetails> observable = mApiService.getProductDetails(timeStamp,
                OtherUtil.getSign(timeStamp, GET_PRODUCT_INFO_METHOD),
                YiBaiApplication.getUid(),
                productSkuId);
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
     * 删除贴纸信息
     *
     * @param simulationData "模拟搭配产品"信息
     * @param callBack       回调方法
     */
    @Override
    public void deleteSimulationData(SimulationData simulationData, CallBack callBack) {
        try {
            DbManager dbManager = YiBaiApplication.getDbManager();
            dbManager.delete(simulationData);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加贴纸信息
     *
     * @param simulationData 位置发生改变的"模拟搭配产品"信息
     * @param callBack       回调方法
     */
    @Override
    public void addSimulationData(SimulationData simulationData, CallBack callBack) {
        try {
            DbManager dbManager = YiBaiApplication.getDbManager();
            dbManager.save(simulationData);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新贴纸的位置信息
     *
     * @param simulationData 位置发生改变的"模拟搭配产品"信息
     * @param callBack       回调方法
     */
    @Override
    public void updateSimulationData(SimulationData simulationData, CallBack callBack) {
        try {
            DbManager dbManager = YiBaiApplication.getDbManager();
            // 更新SimulationData列名为x,y,xScale,yScale的数据
            dbManager.update(simulationData, "x", "y", "xScale", "yScale");
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加产品数据到"模拟搭配产品"数据
     *
     * @param listBean 产品数据
     * @param bitmap   产品"模拟搭产品"
     * @param callBack 回调方法
     */
    @Override
    public void addSimulationData(ListBean listBean, Bitmap bitmap, CallBack callBack) {
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
        String productPriceCode = listBean.getPrice_code();
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
        LogUtil.e(TAG, "主产品组合模式: 1单图模式,2搭配上部,3搭配下部: " + productCombinationType);
        LogUtil.e(TAG, "主产品售价代码: " + productPriceCode);
        LogUtil.e(TAG, "主产品批发价代码: " + productTradePriceCode);
        LogUtil.e(TAG, "主产品的款名Id+附加产品的款名Id的组合: " + finallySkuId);
        LogUtil.e(TAG, "保存Bitmap为图片到本地的路径: " + picturePath);
        LogUtil.e(TAG, "添加产品数据到模拟搭配产品数据: " + new Gson().toJson(listBean));

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
            callBack.onAddSimulationDataResult(true);
        } catch (DbException e) {
            e.printStackTrace();
            callBack.onAddSimulationDataResult(false);
        }
    }

    /**
     * 添加"随机组合"数据到"模拟搭配产品"数据
     *
     * @param data     产品数据
     * @param bitmap   产品"模拟搭图片"
     * @param callBack 回调方法
     */
    @Override
    public void addSimulationData(SpecSuk.DataBean data, Bitmap bitmap, CallBack callBack) {
        int width = 0;
        int height = 0;
        String picturePath = null;
        if (null != bitmap) {
            width = bitmap.getWidth();
            height = bitmap.getHeight();
            // 保存Bitmap为图片到本地
            picturePath = ImageUtil.saveImage(bitmap, String.valueOf(TimeUtil.getTimeStamp()));
        }

        int productSkuId = 0;
        String productName = null;
        double productPrice = 0;
        double productMonthRent = 0;
        double productTradePrice = 0;
        String productPic1 = null;
        String productPic2 = null;
        String productPic3 = null;
        double productHeight = 0;
        double productOffsetRatio = 0;
        int productCombinationType = 0;
        String productPriceCode = null;
        String productTradePriceCode = null;

        int augmentedProductSkuId = 0;
        String augmentedProductName = null;
        double augmentedProductPrice = 0;
        double augmentedProductMonthRent = 0;
        double augmentedProductTradePrice = 0;
        String augmentedProductPic1 = null;
        String augmentedProductPic2 = null;
        String augmentedProductPic3 = null;
        double augmentedProductHeight = 0;
        double augmentedProductOffsetRatio = 0;
        int augmentedCombinationType = 0;
        String augmentedPriceCode = null;
        String augmentedTradePriceCode = null;

        List<SpecSuk.DataBean.ListBean> list = data.getList();
        for (SpecSuk.DataBean.ListBean listBean : list) {
            String type = listBean.getType();
            if (PLANT.equals(type)) {
                productSkuId = listBean.getSku_id();
                productName = listBean.getName();
                productPrice = listBean.getPrice();
                productMonthRent = listBean.getMonth_rent();
                productTradePrice = listBean.getTrade_price();
                productPic1 = listBean.getPic1();
                productPic2 = listBean.getPic2();
                productPic3 = listBean.getPic3();
                productHeight = listBean.getHeight();
                productOffsetRatio = listBean.getOffset_ratio();
                productCombinationType = listBean.getComtype();
                productPriceCode = listBean.getPrice_code();
                productTradePriceCode = listBean.getTrade_price_code();
            } else {
                augmentedProductSkuId = listBean.getSku_id();
                augmentedProductName = listBean.getName();
                augmentedProductPrice = listBean.getPrice();
                augmentedProductMonthRent = listBean.getMonth_rent();
                augmentedProductTradePrice = listBean.getTrade_price();
                augmentedProductPic1 = listBean.getPic1();
                augmentedProductPic2 = listBean.getPic2();
                augmentedProductPic3 = listBean.getPic3();
                augmentedProductHeight = listBean.getHeight();
                augmentedProductOffsetRatio = listBean.getOffset_ratio();
                augmentedCombinationType = listBean.getComtype();
                augmentedPriceCode = listBean.getPrice_code();
                augmentedTradePriceCode = listBean.getTrade_price_code();
            }
        }

        int uid = YiBaiApplication.getUid();
        String finallySkuId = String.valueOf(productSkuId) + augmentedProductSkuId + uid;

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
            simulationData.setProductOffsetRatio(productOffsetRatio);
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
            callBack.onAddSimulationDataResult(true);
        } catch (DbException e) {
            e.printStackTrace();
            callBack.onAddSimulationDataResult(false);
        }
    }

    /**
     * 点击新增按钮时获取搭配列表成功的数据
     * 添加到"模拟搭配产品"数据下
     * 处理在下载全部图片完成之后的一些事情
     * 添加"随机组合"数据到"模拟搭配产品"数据
     *
     * @param data     产品数据
     * @param bitmap   产品"模拟搭图片"
     * @param callBack 回调方法
     */
    /**
     * 添加"随机组合"数据到"模拟搭配产品"数据
     *
     * @param data     产品数据
     * @param bitmap   产品"模拟搭图片"
     * @param callBack 回调方法
     */
    @Override
    public void addSimulationData(Recommend.DataBean data, Bitmap bitmap, CallBack callBack) {
        int width = 0;
        int height = 0;
        String picturePath = null;
        if (null != bitmap) {
            width = bitmap.getWidth();
            height = bitmap.getHeight();
            // 保存Bitmap为图片到本地
            picturePath = ImageUtil.saveImage(bitmap, String.valueOf(TimeUtil.getTimeStamp()));
        }

        int productSkuId = 0;
        String productName = null;
        double productPrice = 0;
        double productMonthRent = 0;
        double productTradePrice = 0;
        String productPic1 = null;
        String productPic2 = null;
        String productPic3 = null;
        double productHeight = 0;
        double productOffsetRatio = 0;
        int productCombinationType = 0;
        String productPriceCode = null;
        String productTradePriceCode = null;

        int augmentedProductSkuId = 0;
        String augmentedProductName = null;
        double augmentedProductPrice = 0;
        double augmentedProductMonthRent = 0;
        double augmentedProductTradePrice = 0;
        String augmentedProductPic1 = null;
        String augmentedProductPic2 = null;
        String augmentedProductPic3 = null;
        double augmentedProductHeight = 0;
        double augmentedProductOffsetRatio = 0;
        int augmentedCombinationType = 0;
        String augmentedPriceCode = null;
        String augmentedTradePriceCode = null;

        Recommend.DataBean.PlantBean plant = data.getPlant();
        for (Iterator<ListBean> iterator = plant.getList().iterator(); iterator.hasNext(); ) {
            ListBean listBean = iterator.next();
            productSkuId = listBean.getSku_id();
            productName = listBean.getName();
            productPrice = listBean.getPrice();
            productMonthRent = listBean.getMonth_rent();
            productTradePrice = listBean.getTrade_price();
            productPic1 = listBean.getPic1();
            productPic2 = listBean.getPic2();
            productPic3 = listBean.getPic3();
            productHeight = listBean.getHeight();
            productOffsetRatio = listBean.getOffset_ratio();
            productCombinationType = listBean.getComtype();
            productPriceCode = listBean.getPrice_code();
            productTradePriceCode = listBean.getTrade_price_code();
            break;
        }
        Recommend.DataBean.PotBean pot = data.getPot();
        for (Iterator<ListBean> iterator = pot.getList().iterator(); iterator.hasNext(); ) {
            ListBean listBean = iterator.next();
            augmentedProductSkuId = listBean.getSku_id();
            augmentedProductName = listBean.getName();
            augmentedProductPrice = listBean.getPrice();
            augmentedProductMonthRent = listBean.getMonth_rent();
            augmentedProductTradePrice = listBean.getTrade_price();
            augmentedProductPic1 = listBean.getPic1();
            augmentedProductPic2 = listBean.getPic2();
            augmentedProductPic3 = listBean.getPic3();
            augmentedProductHeight = listBean.getHeight();
            augmentedProductOffsetRatio = listBean.getOffset_ratio();
            augmentedCombinationType = listBean.getComtype();
            augmentedPriceCode = listBean.getPrice_code();
            augmentedTradePriceCode = listBean.getTrade_price_code();
            break;
        }

        int uid = YiBaiApplication.getUid();
        String finallySkuId = String.valueOf(productSkuId) + augmentedProductSkuId + uid;

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
            simulationData.setProductOffsetRatio(productOffsetRatio);
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
            int windowHeight = YiBaiApplication.getWindowHeight();
            int windowWidth = YiBaiApplication.getWindowWidth();
            if (windowHeight > 0 && windowWidth > 0) {
                if (height > width) {
                    double mYScale = (double) 1 / ((double) height / (double) 720);
                    float newY = (float) ((float) (windowHeight / 2) - height * mYScale / 2);
                    float newX = (float) ((float) (windowWidth / 2) - width * mYScale / 2);
                    // 本來默认为1，现在修改为再中间固定大小
                    simulationData.setxScale(mYScale);
                    simulationData.setyScale(mYScale);
                    simulationData.setX(newX);
                    simulationData.setY(newY);
                } else {
                    double mXScale = (double) 1 / ((double) width / (double) 420);
                    float newY = (float) ((float) (windowHeight / 2) - height * mXScale / 2);
                    float newX = (float) ((float) (windowWidth / 2) - width * mXScale / 2);
                    // 默认为1
                    simulationData.setxScale(mXScale);
                    simulationData.setyScale(mXScale);
                    simulationData.setX(newX);
                    simulationData.setY(newY);
                }
            } else {
                simulationData.setxScale(1);
                simulationData.setyScale(1);
            }


            /*----------*/
            dbManager.save(simulationData);
            // 提示用户添加成功
            callBack.onAddSimulationDataResult(true);
        } catch (DbException e) {
            e.printStackTrace();
            callBack.onAddSimulationDataResult(false);
        }
    }

    /**
     * 添加报价数据到"模拟搭配产品"数据
     *
     * @param quotationData 报价数据
     * @param bitmap        产品"模拟搭图片"
     * @param callBack      回调方法
     */
    @Override
    public void addSimulationData(QuotationData quotationData, Bitmap bitmap, CallBack callBack) {
        int width = 0;
        int height = 0;
        String picturePath = null;
        if (null != bitmap) {
            width = bitmap.getWidth();
            height = bitmap.getHeight();
            // 保存Bitmap为图片到本地
            picturePath = ImageUtil.saveImage(bitmap, String.valueOf(TimeUtil.getTimeStamp()));
        }

        int productSkuId = quotationData.getProductSkuId();
        String productName = quotationData.getProductName();
        double productPrice = quotationData.getProductPrice();
        double productMonthRent = quotationData.getProductMonthRent();
        double productTradePrice = quotationData.getProductTradePrice();
        String productPic1 = quotationData.getProductPic1();
        String productPic2 = quotationData.getProductPic2();
        String productPic3 = quotationData.getProductPic3();
        String productSpecText = quotationData.getProductSpecText();
        double productHeight = quotationData.getProductHeight();
        int productCombinationType = quotationData.getProductCombinationType();
        String productPriceCode = quotationData.getProductPriceCode();
        String productTradePriceCode = quotationData.getProductTradePriceCode();

        int augmentedProductSkuId = quotationData.getAugmentedProductSkuId();
        String augmentedProductName = quotationData.getAugmentedProductName();
        double augmentedProductPrice = quotationData.getAugmentedProductPrice();
        double augmentedProductMonthRent = quotationData.getAugmentedProductMonthRent();
        double augmentedProductTradePrice = quotationData.getAugmentedProductTradePrice();
        String augmentedProductPic1 = quotationData.getAugmentedProductPic1();
        String augmentedProductPic2 = quotationData.getAugmentedProductPic2();
        String augmentedProductPic3 = quotationData.getAugmentedProductPic3();
        String augmentedProductSpecText = quotationData.getAugmentedProductSpecText();
        double augmentedProductHeight = quotationData.getAugmentedProductHeight();
        int augmentedCombinationType = quotationData.getAugmentedCombinationType();
        String augmentedPriceCode = quotationData.getAugmentedPriceCode();
        String augmentedTradePriceCode = quotationData.getAugmentedTradePriceCode();

        String finallySkuId;
        int uid = YiBaiApplication.getUid();
        if (0 == augmentedProductSkuId) {
            finallySkuId = String.valueOf(productSkuId) + uid;
        } else {
            finallySkuId = String.valueOf(productSkuId) + augmentedProductSkuId + uid;
        }

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
        LogUtil.e(TAG, "附加产品组合模式: 1单图模式,2搭配上部,3搭配下部: " + augmentedCombinationType);
        LogUtil.e(TAG, "附加产品售价代码: " + augmentedPriceCode);
        LogUtil.e(TAG, "附加产品批发价代码: " + augmentedTradePriceCode);

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
            callBack.onAddSimulationDataResult(true);
        } catch (DbException e) {
            e.printStackTrace();
            callBack.onAddSimulationDataResult(false);
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
        LogUtil.e(TAG, "Uid: " + YiBaiApplication.getUid());
        LogUtil.e(TAG, "Cid: " + YiBaiApplication.getCid());
        LogUtil.e(TAG, "firstSkuId: " + firstSkuId);
        LogUtil.e(TAG, "secondSkuId: " + secondSkuId);

        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<AddQuotation> observable;
        // 当0 == firstSkuId 说明主产品为null,所以附加产品在这里就是主产品
        if (0 == firstSkuId) {
            observable = mApiService.addQuotation(timeStamp,
                    OtherUtil.getSign(timeStamp, ADD_QUOTATION_METHOD),
                    YiBaiApplication.getUid(),
                    YiBaiApplication.getCid(),
                    secondSkuId,
                    null,
                    params);
        } else {
            observable = mApiService.addQuotation(timeStamp,
                    OtherUtil.getSign(timeStamp, ADD_QUOTATION_METHOD),
                    YiBaiApplication.getUid(),
                    YiBaiApplication.getCid(),
                    firstSkuId,
                    secondSkuId,
                    params);
        }
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

    /**
     * 将新搭配的产品数据添加到"模拟搭配产品"数据
     *
     * @param productData        产品相关数据
     * @param simulationDataList 用户保存的"模拟搭配图片"数据
     * @param finallySkuId       当前正在操作贴纸所属的主产品的款名Id+附加产品的款名Id的组合+用户的uid
     * @param single             true 单图模式/false 上下搭配
     * @param bitmap             产品"模拟搭图片"
     * @param callBack           回调方法
     */
    @Override
    public void addSimulationData(ProductData productData, List<SimulationData> simulationDataList, String finallySkuId, boolean single, Bitmap bitmap, CallBack callBack) {
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
        double productOffsetRatio = productData.getProductOffsetRatio();
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
        String newFinallySkuId;
        if (single) {
            newFinallySkuId = String.valueOf(augmentedProductSkuId) + uid;
        } else {
            newFinallySkuId = String.valueOf(productSkuId) + augmentedProductSkuId + uid;
        }

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
        LogUtil.e(TAG, "附加产品组合模式: 1单图模式,2搭配上部,3搭配下部: " + augmentedCombinationType);
        LogUtil.e(TAG, "附加产品售价代码: " + augmentedPriceCode);
        LogUtil.e(TAG, "附加产品批发价代码: " + augmentedTradePriceCode);

        LogUtil.e(TAG, "主产品的款名Id+附加产品的款名Id的组合: " + finallySkuId);
        LogUtil.e(TAG, "偏移量(如果该产品是花盆): " + productOffsetRatio);
        LogUtil.e(TAG, "偏移量(如果该产品是花盆): " + augmentedProductOffsetRatio);
        LogUtil.e(TAG, "保存Bitmap为图片到本地的路径: " + picturePath);
        LogUtil.e(TAG, "Bitmap_width: " + width);
        LogUtil.e(TAG, "Bitmap_height: " + height);

        // 根据当前正在操作贴纸所属的主产品的款名Id+附加产品的款名Id的组合+用户的uid
        // 在用户保存的"模拟搭配图片"数据找出同款的产品
        List<SimulationData> list = new ArrayList<>();
        for (SimulationData simulationData : simulationDataList) {
            String finallySkuId_ = simulationData.getFinallySkuId();
            if (TextUtils.isEmpty(finallySkuId) || TextUtils.isEmpty(finallySkuId_)) {
                continue;
            }
            if (!finallySkuId.equals(finallySkuId_)) {
                continue;
            }
            list.add(simulationData);
        }
        if (list.isEmpty()) {
            return;
        }

        // 当前正在编辑的这一个场景的id
        long sceneId = 0;
        DbManager dbManager = YiBaiApplication.getDbManager();
        try {
            // 查找当前正在编辑的这一个场景
            List<SceneInfo> defaultSceneInfoList = dbManager.selector(SceneInfo.class)
                    .where("uid", "=", uid)
                    .and("editScene", "=", true)
                    .findAll();
            if (null != defaultSceneInfoList && defaultSceneInfoList.size() > 0) {
                sceneId = defaultSceneInfoList.get(0).getSceneId();
            }
        } catch (DbException e) {
            e.printStackTrace();
        }

        for (SimulationData currentSimulationData : list) {
            float x = currentSimulationData.getX();
            float y = currentSimulationData.getY();
            double oldWidth = currentSimulationData.getWidth();
            double oldHeight = currentSimulationData.getHeight();
            double oldXScale = currentSimulationData.getxScale();
            double oldYScale = currentSimulationData.getyScale();
            int showWidth = BigDecimal.valueOf(oldWidth * oldXScale)
                    .setScale(2, BigDecimal.ROUND_HALF_UP).intValue();
            int showHeight = BigDecimal.valueOf(oldHeight * oldYScale)
                    .setScale(2, BigDecimal.ROUND_HALF_UP).intValue();
            double yScale =
//                    oldYScale;
                    new BigDecimal(((double) showHeight / (double) height))
                            .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            // 新合成的图片距离X轴的距离(要保持新合成的图片的中心点要和当前用户正在操作的贴纸的中心点相同)
            // 算法: 1,原中心点的位置 = 贴纸左上角距离屏幕左上角X轴的距离 + (更换搭配之前贴纸实际的宽度 * 更换搭配之前贴纸的X轴的缩放比例 / 2)
            //      2,新合成的图片距离X轴的距离 = 原中心点的位置 - (新合成的图片宽度 * 新合成的图片Y轴的缩放 / 2)
            float newX = BigDecimal.valueOf((oldWidth * oldXScale / 2 + x) - (width * yScale / 2))
                    .setScale(2, BigDecimal.ROUND_HALF_UP).intValue();

            LogUtil.e(TAG, "贴纸左上角距离屏幕左上角X轴的距离: " + x);
            LogUtil.e(TAG, "贴纸左上角距离屏幕左上角Y轴的距离: " + y);
            LogUtil.e(TAG, "更换搭配之前贴纸实际的宽度: " + oldWidth);
            LogUtil.e(TAG, "更换搭配之前贴纸实际的高度: " + oldHeight);
            LogUtil.e(TAG, "更换搭配之前贴纸的X轴的缩放比例: " + oldXScale);
            LogUtil.e(TAG, "更换搭配之前贴纸的Y轴的缩放比例: " + oldYScale);
            LogUtil.e(TAG, "更换搭配之前贴纸显示的宽度: " + showWidth);
            LogUtil.e(TAG, "更换搭配之前贴纸显示的高度: " + showHeight);
            LogUtil.e(TAG, "新合成的图片Y轴的缩放?就与更换搭配之前贴纸显示的高度一样: " + yScale);

            try {
                SimulationData simulationData = new SimulationData();
                simulationData.setUid(uid);
                simulationData.setSceneId(sceneId);
                simulationData.setFinallySkuId(newFinallySkuId);

                /*----------*/
                if (!single) {
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
                simulationData.setProductOffsetRatio(productOffsetRatio);
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
                simulationData.setX(newX);
                simulationData.setY(y);
                if (0 < width && 0 < height) {
                    simulationData.setWidth(width);
                    simulationData.setHeight(height);
                }
                // 注意: 这里的宽高应该是一样的缩放比例
                simulationData.setxScale(yScale);
                simulationData.setyScale(yScale);
                /*----------*/
                // 保存新的贴纸数据
                dbManager.save(simulationData);
                // 删除当前用户正在操作的贴纸数据
                dbManager.delete(currentSimulationData);
            } catch (DbException e) {
                e.printStackTrace();
                // 提示用户添加失败
                callBack.onAddSimulationDataResult(false);
            }
        }
        // 提示用户添加成功
        callBack.onAddSimulationDataResult(true);
    }

    @Override
    public void addParam(CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<BaseResponse<List<BTCBean>>> observable = mApiService.getParams(timeStamp,
                OtherUtil.getSign(timeStamp, "mybw.getparam"),
                YiBaiApplication.getUid());
        Observer<BaseResponse<List<BTCBean>>> observer = new Observer<BaseResponse<List<BTCBean>>>() {
            @Override
            public void onSubscribe(Disposable d) {
//                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(BaseResponse<List<BTCBean>> response) {
                if (response.getCode() == 200) {
                    callBack.onAddParam(response.getData());
                } else callBack.onRequestFailure(new Throwable(response.getMsg()));
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
    public void setUserPosition(String position, CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<UserPosition> observable = mApiService.setUserPosition(timeStamp,
                OtherUtil.getSign(timeStamp, SET_USER_POSITION_METHOD),
                YiBaiApplication.getUid(),
                position);
        Observer<UserPosition> observer = new Observer<UserPosition>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(UserPosition userPosition) {
                if (userPosition.getCode() == 200)
                    callBack.onSetUserPositionSuccess(userPosition);
                else callBack.onRequestFailure(new Throwable(userPosition.getMsg()));
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
    public void getDesignDetail(String number, CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<BaseResponse<ItemDesignSceneModel>> observable = mApiService.getDesignInfo(timeStamp,
                OtherUtil.getSign(timeStamp, "mybw.getdesigninfo"),
                String.valueOf(YiBaiApplication.getUid()),
                number);
        Observer<BaseResponse<ItemDesignSceneModel>> observer = new Observer<BaseResponse<ItemDesignSceneModel>>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(BaseResponse<ItemDesignSceneModel> response) {
                if (response.getCode() == 200) {
                    callBack.onGetDesignDetailCallback(response.getData());
                } else callBack.onRequestFailure(new Throwable(response.getMsg()));
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
    public void createDesign(CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<BaseResponse<CreateDesignModel>> observable = mApiService.createDesign(timeStamp,
                OtherUtil.getSign(timeStamp, "mybw.createdesign"),
                String.valueOf(YiBaiApplication.getUid()));
        Observer<BaseResponse<CreateDesignModel>> observer = new Observer<BaseResponse<CreateDesignModel>>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(BaseResponse<CreateDesignModel> response) {
                if (response.getCode() == 200) callBack.onCreateDesignCallback(response.getData());
                else callBack.onRequestFailure(new Throwable(response.getMsg()));
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
    public void createSceneOrPic(String desing_number, String type, String scheme_id,
                                 String scheme_name, Map<String, RequestBody> pic, String product_sku_id,
                                 CallBack callback) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<BaseResponse<CreateSceneOrPicModel>> observable = mApiService.createSceneOrPic(
                OtherUtil.getSign(timeStamp, "mybw.designscheme"),
                timeStamp, String.valueOf(YiBaiApplication.getUid()),
                desing_number, type, scheme_id, scheme_name, pic, product_sku_id
        );
        Observer<BaseResponse<CreateSceneOrPicModel>> observer = new Observer<BaseResponse<CreateSceneOrPicModel>>() {
            @Override
            public void onSubscribe(Disposable d) {
                callback.onRequestBefore(d);
            }

            @Override
            public void onNext(BaseResponse<CreateSceneOrPicModel> response) {
                if (response.getCode() == 200) callback.onCreateSceneOrPic(response.getData());
                else callback.onRequestFailure(new Throwable(response.getMsg()));
            }

            @Override
            public void onError(Throwable e) {
                callback.onRequestFailure(e);
            }

            @Override
            public void onComplete() {
                callback.onRequestComplete();
            }
        };
        observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(observer);
    }

    @Override
    public void getSceneList(CallBack callBack) {
        try {
            DbManager manager = YiBaiApplication.getDbManager();
            if (null == manager) {
                return;
            }
            List<SceneInfo> sceneInfoList = manager.selector(SceneInfo.class)
                    .where("uid", "=", YiBaiApplication.getUid())
                    .findAll();
            if (null != sceneInfoList && sceneInfoList.size() > 0) {
                callBack.onGetSceneInfo(sceneInfoList);
                return;
            }

            // 没有场景,新建场景
            SceneInfo sceneInfo = new SceneInfo();
            sceneInfo.setUid(YiBaiApplication.getUid());
            sceneInfo.setSceneId(TimeUtil.getTimeStamp());
            String sceneName = SceneHelper.saveSceneNum(YiBaiApplication.getContext());
            sceneInfo.setSceneName(sceneName);
//            sceneInfo.setSceneName(YiBaiApplication.getContext().getResources().getString(R.string.my_scene));
            sceneInfo.setEditScene(true);
            // 保存场景信息
            manager.save(sceneInfo);
            // 重新查找
            sceneInfoList = manager.selector(SceneInfo.class)
                    .where("uid", "=", YiBaiApplication.getUid())
                    .findAll();
            callBack.onGetSceneInfo(sceneInfoList);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
}