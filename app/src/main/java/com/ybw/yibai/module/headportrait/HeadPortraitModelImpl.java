package com.ybw.yibai.module.headportrait;

import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.bean.EditUserInfo;
import com.ybw.yibai.common.interfaces.ApiService;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.utils.RetrofitManagerUtil;
import com.ybw.yibai.common.utils.TimeUtil;
import com.ybw.yibai.module.headportrait.HeadPortraitContract.CallBack;
import com.ybw.yibai.module.headportrait.HeadPortraitContract.HeadPortraitModel;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

import static com.ybw.yibai.common.constants.HttpUrls.EDIT_USER_INFO_METHOD;

/**
 * 修改头像Model实现类
 *
 * @author sjl
 */
public class HeadPortraitModelImpl implements HeadPortraitModel {

    private ApiService mApiService;

    public HeadPortraitModelImpl() {
        RetrofitManagerUtil instance = RetrofitManagerUtil.getInstance();
        mApiService = instance.getApiService();
    }

    /**
     * 修改用户图片
     *
     * @param params   图片数据
     * @param callBack 回调方法
     */
    @Override
    public void editUserInfo(Map<String, RequestBody> params, CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<EditUserInfo> observable = mApiService.editUserInfo(timeStamp,
                OtherUtil.getSign(timeStamp, EDIT_USER_INFO_METHOD),
                YiBaiApplication.getUid(),
                params);
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
