package com.ybw.yibai.module.nickname;

import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.bean.EditUserInfo;
import com.ybw.yibai.common.interfaces.ApiService;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.utils.RetrofitManagerUtil;
import com.ybw.yibai.common.utils.TimeUtil;
import com.ybw.yibai.module.nickname.NicknameContract.CallBack;
import com.ybw.yibai.module.nickname.NicknameContract.NicknameModel;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.ybw.yibai.common.constants.HttpUrls.EDIT_USER_INFO_METHOD;

/**
 * 修改昵称Model实现类
 *
 * @author sjl
 */
public class NicknameModelImpl implements NicknameModel {

    private ApiService mApiService;

    public NicknameModelImpl() {
        RetrofitManagerUtil instance = RetrofitManagerUtil.getInstance();
        mApiService = instance.getApiService();
    }

    @Override
    public void editUserInfo(String nickName, CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<EditUserInfo> observable = mApiService.editUserInfo(timeStamp,
                OtherUtil.getSign(timeStamp, EDIT_USER_INFO_METHOD),
                YiBaiApplication.getUid(),
                nickName);
        Observer<EditUserInfo> observer = new Observer<EditUserInfo>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(EditUserInfo editUserInfo) {
                callBack.onEditUserInfoSuccess(editUserInfo);
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
