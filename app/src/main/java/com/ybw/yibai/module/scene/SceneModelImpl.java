package com.ybw.yibai.module.scene;

import android.text.TextUtils;

import com.ybw.yibai.R;
import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.bean.BaseBean;
import com.ybw.yibai.common.bean.CreateSceneData;
import com.ybw.yibai.common.bean.DeletePlacement;
import com.ybw.yibai.common.bean.DesignCreate;
import com.ybw.yibai.common.bean.DesignScheme;
import com.ybw.yibai.common.bean.PlacementQrQuotationList;
import com.ybw.yibai.common.bean.QuotationData;
import com.ybw.yibai.common.bean.SceneInfo;
import com.ybw.yibai.common.bean.SimulationData;
import com.ybw.yibai.common.helper.SceneHelper;
import com.ybw.yibai.common.interfaces.ApiService;
import com.ybw.yibai.common.model.CreateSceneOrPicModel;
import com.ybw.yibai.common.network.response.BaseResponse;
import com.ybw.yibai.common.utils.LogUtil;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.utils.RetrofitManagerUtil;
import com.ybw.yibai.common.utils.TimeUtil;
import com.ybw.yibai.module.scene.SceneContract.CallBack;
import com.ybw.yibai.module.scene.SceneContract.SceneModel;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.io.File;
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

import static com.ybw.yibai.common.constants.HttpUrls.CREATE_DESIGN_METHOD;
import static com.ybw.yibai.common.constants.HttpUrls.DELETE_QUOTATION_METHOD;
import static com.ybw.yibai.common.constants.HttpUrls.DESIGN_SCHEME_METHOD;
import static com.ybw.yibai.common.constants.HttpUrls.EDIT_SCHEME_METHOD;
import static com.ybw.yibai.common.constants.HttpUrls.GET_QUOTATION_LIST_METHOD;
import static com.ybw.yibai.common.utils.FileUtil.judeFileExists;

/**
 * 场景界面Model实现类
 *
 * @author sjl
 * @date 2019/9/23
 */
public class SceneModelImpl implements SceneModel {

    public static final String TAG = "SceneModelImpl";

    private ApiService mApiService;

    public SceneModelImpl() {
        RetrofitManagerUtil instance = RetrofitManagerUtil.getInstance();
        mApiService = instance.getApiService();
    }

    /**
     * 新创建场景信息或者修改场景背景图片
     *
     * @param createSceneData 创建场景时需要的数据
     * @param callBack        回调方法
     */
    @Override
    public void addSceneInfo(List<CreateSceneData> createSceneData, CallBack callBack) {
        try {
            DbManager dbManager = YiBaiApplication.getDbManager();
            List<SceneInfo> sceneInfoList = dbManager.selector(SceneInfo.class)
                    .where("uid", "=", YiBaiApplication.getUid())
                    .and("editScene", "=", true)
                    .findAll();
            String number = "";
            for (SceneInfo sceneInfo : sceneInfoList) {
                if (sceneInfo.isEditScene()) {
                    number = sceneInfo.getNumber();
                }
                sceneInfo.setEditScene(false);
                // 更新SceneInfo列名为editScene的数据
                dbManager.update(sceneInfo, "editScene");
            }
            addSceneInfoPage(createSceneData, 0, number, dbManager, callBack);
        } catch (DbException e) {
            e.printStackTrace();
            callBack.addSceneInfoResult(false);
        }
    }

    /**
     * 递归创建
     */
    private void addSceneInfoPage(List<CreateSceneData> createSceneData, int index, String number, DbManager dbManager, CallBack callBack) {
        if (index < createSceneData.size()) {
            CreateSceneData sceneData = createSceneData.get(index);

            String name = sceneData.getName();
            String path = sceneData.getFile().getAbsolutePath();
            SceneInfo sceneInfo = new SceneInfo();
            sceneInfo.setUid(YiBaiApplication.getUid());
            sceneInfo.setNumber(number);
            sceneInfo.setSceneId(TimeUtil.getNanoTime());

            if (TextUtils.isEmpty(name)) {
                String schemeName = SceneHelper.saveSceneNum(YiBaiApplication.getContext());
                sceneInfo.setSceneName(schemeName);
//                sceneInfo.setSceneName(YiBaiApplication.getContext().getResources().getString(R.string.my_scene));
            } else {
                sceneInfo.setSceneName(name);
            }
            sceneInfo.setSceneBackground(path);
            if (index == createSceneData.size() - 1) {
                sceneInfo.setEditScene(true);
            } else {
                sceneInfo.setEditScene(false);
            }
            designNewScheme(sceneInfo, dbManager, createSceneData, index, callBack);
        } else {
            callBack.addSceneInfoResult(true);
        }
    }

    /**
     * 创建场景
     */
    public void designNewScheme(SceneInfo sceneInfo, DbManager manager, List<CreateSceneData> createSceneData, int index, CallBack callBack) {
        String schemeName = sceneInfo.getSceneName();
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        int uid = YiBaiApplication.getUid();
        Observable<DesignScheme> observable;
        if (sceneInfo.getSceneBackground() != null) {
            observable = mApiService.designNewScheme(timeStamp,
                    OtherUtil.getSign(timeStamp, DESIGN_SCHEME_METHOD),
                    uid, sceneInfo.getNumber(), 1, schemeName, getParamByBgPic(sceneInfo.getSceneBackground()));
        } else {
            observable = mApiService.designNewScheme(timeStamp,
                    OtherUtil.getSign(timeStamp, DESIGN_SCHEME_METHOD),
                    uid, sceneInfo.getNumber(), 1, schemeName);
        }
        Observer<DesignScheme> observer = new Observer<DesignScheme>() {
            @Override
            public void onSubscribe(Disposable d) {
//                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(DesignScheme designScheme) {
                try {
                    if (designScheme.getCode() == 200) {
                        sceneInfo.setScheme_id(designScheme.getData().getSchemeId());
                        // 保存场景信息
                        manager.save(sceneInfo);
                        addSceneInfoPage(createSceneData, index + 1, sceneInfo.getNumber(), manager, callBack);
                    } else if (designScheme.getCode() == 201) {
                        callBack.insufficientPermissions();
                    } else {
                        callBack.onRequestFailure(new Throwable(designScheme.getMsg()));
                    }
                } catch (DbException e) {
                    e.printStackTrace();
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
     * 修改场景信息
     */
    private void editScene(SceneInfo sceneInfo, List<CreateSceneData> createSceneData, CallBack callBack) {
        CreateSceneData sceneData = createSceneData.get(0);
        String name = sceneData.getName();
        String path = sceneData.getFile().getAbsolutePath();
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        int uid = YiBaiApplication.getUid();
        Observable<BaseBean> observable = mApiService.editScheme(timeStamp,
                OtherUtil.getSign(timeStamp, EDIT_SCHEME_METHOD),
                uid, name, sceneInfo.getScheme_id(), getBgPicByPath(path));
        Observer<BaseBean> observer = new Observer<BaseBean>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(BaseBean baseBean) {
                if (baseBean.getCode() == 200) {
                    try {
                        DbManager manager = YiBaiApplication.getDbManager();
                        sceneInfo.setSceneBackground(path);
                        manager.update(sceneInfo, "sceneBackground");
                        sceneInfo.setSceneName(name);
                        manager.update(sceneInfo, "sceneName");
                        findUserSceneListInfo(callBack);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }

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
     * 查找用户的场景信息
     *
     * @param callBack 回调方法
     */
    @Override
    public void findUserSceneListInfo(CallBack callBack) {
        try {
            DbManager manager = YiBaiApplication.getDbManager();
            if (null == manager) {
                return;
            }
            List<SceneInfo> sceneInfoList = manager.selector(SceneInfo.class)
                    .where("uid", "=", YiBaiApplication.getUid())
                    .findAll();
            if (null != sceneInfoList && sceneInfoList.size() > 0) {
                callBack.onFindUserSceneInfoSuccess(sceneInfoList);
                return;
            }

            // 没有场景,新建场景
            SceneInfo sceneInfo = new SceneInfo();
            sceneInfo.setUid(YiBaiApplication.getUid());
            sceneInfo.setSceneId(TimeUtil.getTimeStamp());
            String schemeName = SceneHelper.saveSceneNum(YiBaiApplication.getContext());
            sceneInfo.setSceneName(schemeName);
//            sceneInfo.setSceneName(YiBaiApplication.getContext().getResources().getString(R.string.my_scene));
            sceneInfo.setEditScene(true);
            // 保存场景信息
            manager.save(sceneInfo);
            // 重新查找
            sceneInfoList = manager.selector(SceneInfo.class)
                    .where("uid", "=", YiBaiApplication.getUid())
                    .findAll();
            callBack.onFindUserSceneInfoSuccess(sceneInfoList);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void findUserSceneListInfo(boolean createFlag, CallBack callBack) {
        try {
            DbManager manager = YiBaiApplication.getDbManager();
            if (null == manager) {
                return;
            }
            List<SceneInfo> sceneInfoList = manager.selector(SceneInfo.class)
                    .where("uid", "=", YiBaiApplication.getUid())
                    .findAll();
            if (null != sceneInfoList && sceneInfoList.size() > 0) {
                if (createFlag) {
                    boolean flag = false;
                    for (Iterator<SceneInfo> iterator = sceneInfoList.iterator(); iterator.hasNext(); ) {
                        SceneInfo sceneInfo = iterator.next();
                        if (sceneInfo.isEditScene()) {
                            flag = true;
                            if (sceneInfo.getNumber() != null && !sceneInfo.getNumber().isEmpty())
                                callBack.onFindUserSceneInfoSuccess(sceneInfoList);
                            else {
                                createNewDesign(manager, sceneInfo, sceneInfoList, callBack);
                            }
                            break;
                        }
                    }
                    if (!flag) {
                        createNewDesign(manager, callBack);
                    }
                } else {
                    callBack.onFindUserSceneInfoSuccess(sceneInfoList);
                }
                return;
            }
            createNewDesign(manager, callBack);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建设计
     */
    public void createNewDesign(DbManager manager, SceneInfo
            sceneInfo, List<SceneInfo> sceneInfoList, CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<DesignCreate> observable = mApiService.createDesign(timeStamp,
                OtherUtil.getSign(timeStamp, CREATE_DESIGN_METHOD),
                YiBaiApplication.getUid());
        Observer<DesignCreate> observer = new Observer<DesignCreate>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(DesignCreate designCreate) {
                if (designCreate.getCode() == 200) {
                    sceneInfoList.remove(sceneInfo);
                    sceneInfo.setNumber(designCreate.getData().getDesingNumber());
                    try {
                        manager.update(sceneInfo, "number");
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                    if (sceneInfo.getScheme_id() == null || sceneInfo.getScheme_id().isEmpty()) {
                        designNewScheme(sceneInfoList, sceneInfo, callBack);
                    } else {
                        sceneInfoList.add(sceneInfo);
                        callBack.onFindUserSceneInfoSuccess(sceneInfoList);
                    }
                } else {
                    callBack.onRequestFailure(new Throwable(designCreate.getMsg()));
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
     * 创建场景
     */
    public void designNewScheme(List<SceneInfo> sceneInfos, SceneInfo sceneInfo, CallBack
            callBack) {
        String schemeName = SceneHelper.saveSceneNum(YiBaiApplication.getContext());
//        String schemeName = YiBaiApplication.getContext().getResources().getString(R.string.my_scene);
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        int uid = YiBaiApplication.getUid();
        Observable<DesignScheme> observable;
        if (sceneInfo.getSceneBackground() != null) {
            observable = mApiService.designScheme(timeStamp,
                    OtherUtil.getSign(timeStamp, DESIGN_SCHEME_METHOD),
                    uid, sceneInfo.getNumber(), 1, schemeName, getParamByBgPic(sceneInfo.getSceneBackground()));
        } else {
            observable = mApiService.designNewScheme(timeStamp,
                    OtherUtil.getSign(timeStamp, DESIGN_SCHEME_METHOD),
                    uid, sceneInfo.getNumber(), 1, schemeName);
        }
        Observer<DesignScheme> observer = new Observer<DesignScheme>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(DesignScheme designScheme) {
                if (designScheme.getCode() == 200) {
                    try {
                        DbManager dbManager = YiBaiApplication.getDbManager();
                        // 没有场景/正在编辑的场景,新建场景
                        sceneInfo.setScheme_id(designScheme.getData().getSchemeId());
                        sceneInfos.add(sceneInfo);
                        // 保存场景信息
                        dbManager.update(sceneInfo, "scheme_id");
                        callBack.onFindUserSceneInfoSuccess(sceneInfos);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                } else if (designScheme.getCode() == 201) {
                    callBack.insufficientPermissions();
                } else {
                    callBack.onRequestFailure(new Throwable(designScheme.getMsg()));
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
     * 获取摆放清单列表
     *
     * @param callBack 回调方法
     */
    @Override
    public void getPlacementList(CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<PlacementQrQuotationList> observable = mApiService.getPlacementQrQuotationList(timeStamp,
                OtherUtil.getSign(timeStamp, GET_QUOTATION_LIST_METHOD),
                YiBaiApplication.getUid(),
                YiBaiApplication.getCid(),
                1);
        Observer<PlacementQrQuotationList> observer = new Observer<PlacementQrQuotationList>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(PlacementQrQuotationList placementQrQuotationList) {
                if (placementQrQuotationList.getCode() == 200) {
                    callBack.onGetPlacementListSuccess(placementQrQuotationList);
                } else {
                    callBack.onRequestFailure(new Throwable(placementQrQuotationList.getMsg()));
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
     * 删除待摆放清单列表
     *
     * @param quoteIds 清单产品id,多个用英文逗号分隔
     * @param callBack 回调方法
     */
    @Override
    public void deletePlacementList(String quoteIds, CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<DeletePlacement> observable = mApiService.deletePlacement(timeStamp,
                OtherUtil.getSign(timeStamp, DELETE_QUOTATION_METHOD),
                YiBaiApplication.getUid(),
                YiBaiApplication.getCid(),
                quoteIds);
        Observer<DeletePlacement> observer = new Observer<DeletePlacement>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(DeletePlacement deletePlacement) {
                if (deletePlacement.getCode() == 200) {
                    callBack.onDeletePlacementListSuccess(deletePlacement);
                } else {
                    callBack.onRequestFailure(new Throwable(deletePlacement.getMsg()));
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
     * 获取用户保存的"报价"数据
     *
     * @param callBack 回调方法
     */
    @Override
    public void getQuotationData(CallBack callBack) {
        try {
            int uid = YiBaiApplication.getUid();
            DbManager dbManager = YiBaiApplication.getDbManager();
            List<QuotationData> quotationDataList = dbManager
                    .selector(QuotationData.class)
                    .where("uid", "=", uid)
                    .findAll();
            callBack.onGetQuotationDataSuccess(quotationDataList);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新用户场景信息列表的集合
     *
     * @param sceneInfoList 用户场景信息列表的集合
     * @param callBack      回调方法
     */
    @Override
    public void updateSceneListInfo(List<SceneInfo> sceneInfoList, CallBack callBack) {
        try {
            DbManager dbManager = YiBaiApplication.getDbManager();
            // 更新SceneInfo列名为editScene的数据
            for (SceneInfo sceneInfo : sceneInfoList) {
                dbManager.update(sceneInfo);
            }
            callBack.updateSceneListInfoResult(true);
        } catch (DbException e) {
            e.printStackTrace();
            callBack.updateSceneListInfoResult(false);
        }
    }

    /**
     * 将某一个场景的"模拟搭配产品"数据添加到报价列表数据中
     *
     * @param sceneId  场景id(一般为创建该场景的时间戳)
     * @param callBack 回调方法
     */
    @Override
    public void addQuotation(long sceneId, CallBack callBack) {
        try {
            int uid = YiBaiApplication.getUid();
            DbManager dbManager = YiBaiApplication.getDbManager();
            List<SimulationData> simulationDataList = dbManager.selector(SimulationData.class)
                    .where("uid", "=", YiBaiApplication.getUid())
                    .and("sceneId", "=", sceneId)
                    .findAll();
            if (null == simulationDataList || simulationDataList.size() == 0) {
                // 错误,没有找到该场景的"模拟搭配产品"数据
                return;
            }
            for (SimulationData simulationData : simulationDataList) {
                int productSkuId = simulationData.getProductSkuId();
                String productName = simulationData.getProductName();
                double productPrice = simulationData.getProductPrice();
                double productMonthRent = simulationData.getProductMonthRent();
                double productTradePrice = simulationData.getProductTradePrice();
                String productPic1 = simulationData.getProductPic1();
                String productPic2 = simulationData.getProductPic2();
                String productPic3 = simulationData.getProductPic3();
                String productSpecText = simulationData.getProductSpecText();
                double productHeight = simulationData.getProductHeight();
                int productCombinationType = simulationData.getProductCombinationType();
                String productPriceCode = simulationData.getProductPriceCode();
                String productTradePriceCode = simulationData.getProductTradePriceCode();

                int augmentedProductSkuId = simulationData.getAugmentedProductSkuId();
                String augmentedProductName = simulationData.getAugmentedProductName();
                double augmentedProductPrice = simulationData.getAugmentedProductPrice();
                double augmentedProductMonthRent = simulationData.getAugmentedProductMonthRent();
                double augmentedProductTradePrice = simulationData.getAugmentedProductTradePrice();
                String augmentedProductPic1 = simulationData.getAugmentedProductPic1();
                String augmentedProductPic2 = simulationData.getAugmentedProductPic2();
                String augmentedProductPic3 = simulationData.getAugmentedProductPic3();
                String augmentedProductSpecText = simulationData.getAugmentedProductSpecText();
                double augmentedProductHeight = simulationData.getAugmentedProductHeight();
                double augmentedProductOffsetRatio = simulationData.getAugmentedProductOffsetRatio();
                int augmentedCombinationType = simulationData.getAugmentedCombinationType();
                String augmentedPriceCode = simulationData.getAugmentedPriceCode();
                String augmentedTradePriceCode = simulationData.getAugmentedTradePriceCode();

                String finallySkuId = simulationData.getFinallySkuId();
                String picturePath = simulationData.getPicturePath();

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
                LogUtil.e(TAG, "偏移量(如果该产品是花盆): " + augmentedProductOffsetRatio);
                LogUtil.e(TAG, "保存Bitmap为图片到本地的路径: " + picturePath);

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
                    if (0 != augmentedProductSkuId) {
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
                }
            }
            callBack.addQuotationResult(true);
        } catch (DbException e) {
            e.printStackTrace();
            callBack.addQuotationResult(false);
        }
    }

    /**
     * 删除场景信息
     *
     * @param sceneInfoList 用户场景信息列表的集合
     * @param callBack      回调方法
     */
    @Override
    public void deleteSceneListInfo(List<SceneInfo> sceneInfoList, CallBack callBack) {
        try {
            for (SceneInfo sceneInfo : sceneInfoList) {
                // 获取用户选中的场景信息
                if (!sceneInfo.isSelectCheckBox()) {
                    continue;
                }
                DbManager dbManager = YiBaiApplication.getDbManager();
                dbManager.delete(sceneInfo);
            }
            findUserSceneListInfo(callBack);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createSceneOrPic(String desing_number, String type, String scheme_id, String
            scheme_name,
                                 Map<String, RequestBody> pic, String product_sku_id, CallBack callback) {
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
    public void addDesignAndSceneInfo(CallBack callBack) {
        DbManager manager = YiBaiApplication.getDbManager();
        if (null == manager) {
            return;
        }
        createNewDesign(manager, callBack);
    }

    /**
     * 创建设计
     */
    public void createNewDesign(DbManager manager, CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<DesignCreate> observable = mApiService.createDesign(timeStamp,
                OtherUtil.getSign(timeStamp, CREATE_DESIGN_METHOD),
                YiBaiApplication.getUid());
        Observer<DesignCreate> observer = new Observer<DesignCreate>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(DesignCreate designCreate) {
                if (designCreate.getCode() == 200) {
                    designNewScheme(designCreate.getData().getDesingNumber(), manager, callBack);
                } else {
                    callBack.onRequestFailure(new Throwable(designCreate.getMsg()));
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
     * 创建场景
     */
    public void designNewScheme(String designNumber, DbManager manager, CallBack callBack) {
        String schemeName = SceneHelper.saveSceneNum(YiBaiApplication.getContext());
//        String schemeName = YiBaiApplication.getContext().getResources().getString(R.string.my_scene);
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        int uid = YiBaiApplication.getUid();
        Observable<DesignScheme> observable;
        observable = mApiService.designNewScheme(timeStamp,
                OtherUtil.getSign(timeStamp, DESIGN_SCHEME_METHOD),
                uid, designNumber, 1, schemeName);
        Observer<DesignScheme> observer = new Observer<DesignScheme>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(DesignScheme designScheme) {
                try {
                    if (designScheme.getCode() == 200) {
                        // 没有场景/正在编辑的场景,新建场景
                        SceneInfo sceneInfo = new SceneInfo();
                        sceneInfo.setUid(YiBaiApplication.getUid());
                        sceneInfo.setNumber(designNumber);
                        sceneInfo.setSceneId(TimeUtil.getNanoTime());
                        sceneInfo.setScheme_id(designScheme.getData().getSchemeId());
                        sceneInfo.setSceneName(schemeName);
                        sceneInfo.setEditScene(true);
                        // 保存场景信息
                        manager.save(sceneInfo);
                        // 重新查找
                        List<SceneInfo> sceneInfoList = manager.selector(SceneInfo.class)
                                .where("uid", "=", YiBaiApplication.getUid())
                                .findAll();
                        callBack.onFindUserSceneInfoSuccess(sceneInfoList);
                    } else if (designScheme.getCode() == 201) {
                        callBack.insufficientPermissions();
                    } else {
                        callBack.onRequestFailure(new Throwable(designScheme.getMsg()));
                    }
                } catch (DbException e) {
                    e.printStackTrace();
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
    public void addDesignAndSceneInfo(List<CreateSceneData> mCreateSceneDataList, CallBack
            callBack) {
        DbManager manager = YiBaiApplication.getDbManager();
        if (null == manager) {
            return;
        }
        createNewDesign(manager, mCreateSceneDataList, callBack);
    }

    /**
     * 创建设计
     */
    public void createNewDesign(DbManager
                                        manager, List<CreateSceneData> mCreateSceneDataList, CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<DesignCreate> observable = mApiService.createDesign(timeStamp,
                OtherUtil.getSign(timeStamp, CREATE_DESIGN_METHOD),
                YiBaiApplication.getUid());
        Observer<DesignCreate> observer = new Observer<DesignCreate>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(DesignCreate designCreate) {
                designNewScheme(designCreate.getData().getDesingNumber(), mCreateSceneDataList, manager, callBack);
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
     * 创建场景
     */
    public void designNewScheme(String designNumber, List<CreateSceneData> mCreateSceneDataList, DbManager manager, CallBack
            callBack) {
        String schemeName = SceneHelper.saveSceneNum(YiBaiApplication.getContext());
//        String schemeName = YiBaiApplication.getContext().getResources().getString(R.string.my_scene);
        if (mCreateSceneDataList.get(0).getName() != null && !mCreateSceneDataList.get(0).getName().isEmpty()) {
            if (!mCreateSceneDataList.get(0).getName().equals("我的场景")) {
                schemeName = mCreateSceneDataList.get(0).getName();
            }
        }
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        String path = mCreateSceneDataList.get(0).getFile().getAbsolutePath();
        int uid = YiBaiApplication.getUid();
        Observable<DesignScheme> observable;
        observable = mApiService.designScheme(timeStamp,
                OtherUtil.getSign(timeStamp, DESIGN_SCHEME_METHOD),
                uid, designNumber, 1, schemeName, getParamByBgPic(path));
        String finalSchemeName = schemeName;
        Observer<DesignScheme> observer = new Observer<DesignScheme>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(DesignScheme designScheme) {
                try {
                    if (designScheme.getCode() == 200) {
                        // 没有场景/正在编辑的场景,新建场景
                        SceneInfo sceneInfo = new SceneInfo();
                        sceneInfo.setUid(YiBaiApplication.getUid());
                        sceneInfo.setNumber(designNumber);
                        sceneInfo.setSceneId(TimeUtil.getNanoTime());
                        sceneInfo.setSceneBackground(path);
                        sceneInfo.setScheme_id(designScheme.getData().getSchemeId());
                        sceneInfo.setSceneName(finalSchemeName);
                        sceneInfo.setEditScene(true);
                        // 保存场景信息
                        manager.save(sceneInfo);
                        // 重新查找
                        List<SceneInfo> sceneInfoList = manager.selector(SceneInfo.class)
                                .where("uid", "=", YiBaiApplication.getUid())
                                .findAll();
                        callBack.onFindUserSceneInfoSuccess(sceneInfoList);
                    } else if (designScheme.getCode() == 201) {
                        callBack.insufficientPermissions();
                    } else {
                        callBack.onRequestFailure(new Throwable(designScheme.getMsg()));
                    }
                } catch (DbException e) {
                    e.printStackTrace();
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

    private MultipartBody.Part getParamByBgPic(String pic) {
        File file = new File(pic);
        String fileName = file.getName();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/" + suffix), file);
        return MultipartBody.Part.createFormData("bgpic", file.getName(), requestBody);
    }

    /**
     * 创建场景
     */
    @Override
    public void createDesignScheme(String desingNumber, int type, String schemeId, String
            schemeName, MultipartBody.Part parts, String productSkuId, CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        int uid = YiBaiApplication.getUid();
        Observable<DesignScheme> observable = mApiService.designScheme(timeStamp,
                OtherUtil.getSign(timeStamp, DESIGN_SCHEME_METHOD),
                uid, desingNumber, type, schemeName, parts);
        Observer<DesignScheme> observer = new Observer<DesignScheme>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(DesignScheme designScheme) {
                if (designScheme.getCode() == 200) {
                    if (type == 1)
                        callBack.onGetDesignSchemeSuccess(designScheme);
                    else
                        callBack.onGetAddDesignSchemeSuccess(designScheme);
                } else if (designScheme.getCode() == 201) {
                    callBack.insufficientPermissions();
                } else {
                    callBack.onRequestFailure(new Throwable(designScheme.getMsg()));
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
    public void designScheme(String desingNumber, int type, String schemeId, String
            schemeName, Map<String, RequestBody> params, String productSkuId, CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        int uid = YiBaiApplication.getUid();
        Observable<DesignScheme> observable = mApiService.designScheme(timeStamp,
                OtherUtil.getSign(timeStamp, DESIGN_SCHEME_METHOD),
                uid, desingNumber, type, schemeId, params, productSkuId);
        Observer<DesignScheme> observer = new Observer<DesignScheme>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(DesignScheme designScheme) {
                if (designScheme.getCode() == 200) {
                    if (type == 1)
                        callBack.onGetDesignSchemeSuccess(designScheme);
                    else
                        callBack.onGetAddDesignSchemeSuccess(designScheme);
                } else if (designScheme.getCode() == 201) {
                    callBack.insufficientPermissions();
                } else {
                    callBack.onRequestFailure(new Throwable(designScheme.getMsg()));
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
