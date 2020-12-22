package com.ybw.yibai.module.user;

import com.ybw.yibai.R;
import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.bean.CheckShareBean;
import com.ybw.yibai.common.bean.DesignCreate;
import com.ybw.yibai.common.bean.DesignScheme;
import com.ybw.yibai.common.bean.SceneInfo;
import com.ybw.yibai.common.helper.SceneHelper;
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

import static com.ybw.yibai.common.constants.HttpUrls.CHECK_SHARE_METHOD;
import static com.ybw.yibai.common.constants.HttpUrls.CREATE_DESIGN_METHOD;
import static com.ybw.yibai.common.constants.HttpUrls.DESIGN_SCHEME_METHOD;

/**
 * 用户场景界面Model实现类
 *
 * @author sjl
 * @date 2019/9/23
 */
public class UserModelImpl implements UserContract.UserModel {

    public static final String TAG = "SceneModelImpl";

    private ApiService mApiService;

    public UserModelImpl() {
        RetrofitManagerUtil instance = RetrofitManagerUtil.getInstance();
        mApiService = instance.getApiService();
    }

    /**
     * 查找用户的场景信息
     *
     * @param callBack 回调方法
     */
    @Override
    public void findUserSceneListInfo(UserContract.CallBack callBack) {
        try {
            DbManager manager = YiBaiApplication.getDbManager();
            if (null == manager) {
                return;
            }
            List<SceneInfo> sceneInfoList = manager.selector(SceneInfo.class)
                    .where("uid", "=", YiBaiApplication.getUid())
                    .findAll();
            callBack.onFindUserUserInfoSuccess(sceneInfoList);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建设计
     */
    public void createNewDesign(DbManager manager, UserContract.CallBack callBack) {
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
                designNewScheme(designCreate.getData().getDesingNumber(), manager, callBack);
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
    public void designNewScheme(String designNumber, DbManager manager, UserContract.CallBack callBack) {
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
                        callBack.onFindUserUserInfoSuccess(sceneInfoList);
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
    public void updateUserSceneList(List<SceneInfo> sceneInfoList, UserContract.CallBack callBack) {
        try {
            DbManager manager = YiBaiApplication.getDbManager();
            if (null == manager) {
                return;
            }
            manager.update(sceneInfoList);
            callBack.onUpdateUserSceneListSuccess();
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateUserScene(List<SceneInfo> sceneInfoList, UserContract.CallBack callBack) {
        try {
            DbManager manager = YiBaiApplication.getDbManager();
            if (null == manager) {
                return;
            }
            manager.update(sceneInfoList, "editScene", "count");
            callBack.onUpdateUserSceneListSuccess();
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateUserScene(SceneInfo sceneInfo, UserContract.CallBack callBack) {
        try {
            DbManager manager = YiBaiApplication.getDbManager();
            if (null == manager) {
                return;
            }
            manager.update(sceneInfo, "editScene", "count");
            callBack.onUpdateUserSceneListSuccess();
        } catch (DbException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void checkShare(UserContract.CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        int uid = YiBaiApplication.getUid();
        Observable<CheckShareBean> observable = mApiService.checkShare(timeStamp,
                OtherUtil.getSign(timeStamp, CHECK_SHARE_METHOD),
                uid);
        Observer<CheckShareBean> observer = new Observer<CheckShareBean>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(CheckShareBean checkShareBean) {
                if (checkShareBean.getCode() == 200) {
                    callBack.checkShareData(checkShareBean);
                } else if (checkShareBean.getCode() == 201) {
                    callBack.insufficientPermissions();
                } else {
                    callBack.onRequestFailure(new Throwable(checkShareBean.getMsg()));
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
