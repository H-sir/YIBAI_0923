package com.ybw.yibai.module.designdetails;

import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.bean.BaseBean;
import com.ybw.yibai.common.bean.DesignDetails;
import com.ybw.yibai.common.bean.SceneInfo;
import com.ybw.yibai.common.interfaces.ApiService;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.utils.RetrofitManagerUtil;
import com.ybw.yibai.common.utils.TimeUtil;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.util.Iterator;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.ybw.yibai.common.constants.HttpUrls.DELETE_DESIGN_METHOD;
import static com.ybw.yibai.common.constants.HttpUrls.DELETE_DESIGN_SCHEME_METHOD;
import static com.ybw.yibai.common.constants.HttpUrls.DELETE_DESIGN_SCHEME_PIC_METHOD;
import static com.ybw.yibai.common.constants.HttpUrls.EDIT_SCHEME_METHOD;
import static com.ybw.yibai.common.constants.HttpUrls.GET_DESIGN_INFO_METHOD;

/**
 * <pre>
 *     author : HKR
 *     time   : 2020/09/02
 *     desc   :
 * </pre>
 */
public class DesignDetailsModelImpl implements DesignDetailsContract.DesignDetailsModel {

    public static final String TAG = "DesignDetailsModelImpl";

    private ApiService mApiService;

    public DesignDetailsModelImpl() {
        RetrofitManagerUtil instance = RetrofitManagerUtil.getInstance();
        mApiService = instance.getApiService();
    }

    @Override
    public void getDesignDetails(String designNumber, DesignDetailsContract.CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<DesignDetails> observable = mApiService.getDesignDetails(timeStamp,
                OtherUtil.getSign(timeStamp, GET_DESIGN_INFO_METHOD),
                YiBaiApplication.getUid(),
                designNumber);
        Observer<DesignDetails> observer = new Observer<DesignDetails>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(DesignDetails designDetails) {
                callBack.onGetDesignDetailsSuccess(designDetails);
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
    public void deleteSchemePic(String picId, DesignDetailsContract.CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<BaseBean> observable = mApiService.deleteDesignSchemePic(timeStamp,
                OtherUtil.getSign(timeStamp, DELETE_DESIGN_SCHEME_PIC_METHOD),
                YiBaiApplication.getUid(),
                picId);
        Observer<BaseBean> observer = new Observer<BaseBean>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(BaseBean deleteBase) {
                callBack.onDeleteSchemePicSuccess(picId);
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
    public void deleteScheme(DesignDetails mDesignDetails, DesignDetails.DataBean.SchemelistBean schemelistBean, DesignDetailsContract.CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<BaseBean> observable = mApiService.deleteDesignScheme(timeStamp,
                OtherUtil.getSign(timeStamp, DELETE_DESIGN_SCHEME_METHOD),
                YiBaiApplication.getUid(),
                schemelistBean.getSchemeId());
        Observer<BaseBean> observer = new Observer<BaseBean>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(BaseBean deleteBase) {
                try {
                    DbManager dbManager = YiBaiApplication.getDbManager();

                    List<SceneInfo> defaultSceneInfoList = dbManager.selector(SceneInfo.class)
                            .where("number", "=", mDesignDetails.getData().getNumber())
                            .findAll();
                    if (defaultSceneInfoList != null && defaultSceneInfoList.size() > 0) {
                        boolean flag = false;
                        for (Iterator<SceneInfo> iterator = defaultSceneInfoList.iterator(); iterator.hasNext(); ) {
                            SceneInfo sceneInfo = iterator.next();
                            if (sceneInfo.getScheme_id().equals(schemelistBean.getSchemeId())) {
                                if (sceneInfo.isEditScene()) {
                                    flag = true;
                                }
                                defaultSceneInfoList.remove(sceneInfo);
                                dbManager.delete(defaultSceneInfoList);
                                break;
                            }
                        }
                        if (flag) {
                            if (defaultSceneInfoList.size() > 1) {
                                defaultSceneInfoList.get(0).setEditScene(true);
                                dbManager.update(defaultSceneInfoList, "editScene");
                            }
                        }
                    }
                    callBack.onDeleteSchemeSuccess(schemelistBean);
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
    public void deleteDesign(DesignDetails designDetails, DesignDetailsContract.CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<BaseBean> observable = mApiService.deleteDesign(timeStamp,
                OtherUtil.getSign(timeStamp, DELETE_DESIGN_METHOD),
                YiBaiApplication.getUid(),
                designDetails.getData().getNumber());
        Observer<BaseBean> observer = new Observer<BaseBean>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(BaseBean deleteBase) {
                deleteScheme(designDetails.getData().getNumber(), callBack, deleteBase);

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
    public void editSceneName(String scnenName, DesignDetails.DataBean.SchemelistBean schemelistBean, DesignDetailsContract.CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        int uid = YiBaiApplication.getUid();
        Observable<BaseBean> observable = mApiService.editScheme(timeStamp,
                OtherUtil.getSign(timeStamp, EDIT_SCHEME_METHOD),
                uid, scnenName, schemelistBean.getSchemeId());
        Observer<BaseBean> observer = new Observer<BaseBean>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(BaseBean baseBean) {
                if (baseBean.getCode() == 200) {
                    try {
                        DbManager dbManager = YiBaiApplication.getDbManager();
                        // 查找当前正在编辑的这一个场景
                        List<SceneInfo> defaultSceneInfoList = dbManager.selector(SceneInfo.class)
                                .where("scheme_id", "=", schemelistBean.getSchemeId())
                                .findAll();
                        if (defaultSceneInfoList != null && defaultSceneInfoList.size() > 0) {
                            defaultSceneInfoList.get(0).setSceneName(scnenName);
                            dbManager.update(defaultSceneInfoList, "sceneName");
                        }
                        callBack.editSceneNameSuccess();
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

    private void deleteScheme(String number, DesignDetailsContract.CallBack callBack, BaseBean baseBean) {
        try {
            DbManager dbManager = YiBaiApplication.getDbManager();
            // 查找当前正在编辑的这一个场景
            List<SceneInfo> defaultSceneInfoList = dbManager.selector(SceneInfo.class)
                    .where("number", "=", number)
                    .findAll();

            dbManager.delete(defaultSceneInfoList);
            callBack.onDeleteDesignSuccess(baseBean);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
}
