package com.ybw.yibai.module.design;

import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.bean.BaseBean;
import com.ybw.yibai.common.bean.DesignList;
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
import static com.ybw.yibai.common.constants.HttpUrls.GET_DESIGN_LIST_METHOD;

/**
 * <pre>
 *     author : HKR
 *     time   : 2020/09/02
 *     desc   :
 * </pre>
 */
public class DesignModelImpl implements DesignContract.DesignModel {

    public static final String TAG = "DesignModelImpl";

    private ApiService mApiService;

    public DesignModelImpl() {
        RetrofitManagerUtil instance = RetrofitManagerUtil.getInstance();
        mApiService = instance.getApiService();
    }

    @Override
    public void getDesignList(DesignContract.CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<DesignList> observable = mApiService.getDesignList(timeStamp,
                OtherUtil.getSign(timeStamp, GET_DESIGN_LIST_METHOD),
                YiBaiApplication.getUid());
        Observer<DesignList> observer = new Observer<DesignList>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(DesignList designList) {
                callBack.onGetDesignListSuccess(designList);
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
    public void deleteDesign(DesignList.DataBean.ListBean listBean, DesignContract.CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<BaseBean> observable = mApiService.deleteDesign(timeStamp,
                OtherUtil.getSign(timeStamp, DELETE_DESIGN_METHOD),
                YiBaiApplication.getUid(),
                listBean.getNumber());
        Observer<BaseBean> observer = new Observer<BaseBean>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(BaseBean deleteBase) {
                if (deleteBase.getCode() == 200) {
                    deleteScheme(listBean.getNumber(), callBack, deleteBase);
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

    private void deleteScheme(String number, DesignContract.CallBack callBack, BaseBean baseBean) {
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

    @Override
    public void getEditSceneInfo(DesignContract.CallBack callBack) {
        try {
            DbManager dbManager = YiBaiApplication.getDbManager();
            if (dbManager != null) {
                // 查找当前正在编辑的这一个场景
                List<SceneInfo> defaultSceneInfoList = dbManager.selector(SceneInfo.class)
                        .where("editScene", "=", true)
                        .findAll();

                callBack.onFindEditSceneInfo(defaultSceneInfoList);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateSceneInfo(DesignList.DataBean.ListBean.SchemelistBean schemelistBean, DesignContract.CallBack callBack) {
        try {
            DbManager dbManager = YiBaiApplication.getDbManager();
            // 查找当前正在编辑的这一个场景
            List<SceneInfo> defaultSceneInfoList = dbManager.selector(SceneInfo.class)
                    .findAll();
            for (Iterator<SceneInfo> iterator = defaultSceneInfoList.iterator(); iterator.hasNext(); ) {
                SceneInfo sceneInfo = iterator.next();
                if (sceneInfo.getScheme_id().equals(schemelistBean.getSchemeId())) {
                    sceneInfo.setEditScene(true);
                } else {
                    sceneInfo.setEditScene(false);
                }
            }
            dbManager.update(defaultSceneInfoList, "editScene");
            callBack.onUpdateSceneInfo();
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
}
