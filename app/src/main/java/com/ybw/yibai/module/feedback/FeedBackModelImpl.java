package com.ybw.yibai.module.feedback;

import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.bean.FeedBack;
import com.ybw.yibai.common.interfaces.ApiService;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.utils.RetrofitManagerUtil;
import com.ybw.yibai.common.utils.TimeUtil;
import com.ybw.yibai.module.feedback.FeedBackContract.CallBack;
import com.ybw.yibai.module.feedback.FeedBackContract.FeedBackModel;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;

import static com.ybw.yibai.common.constants.HttpUrls.FEED_BACK_METHOD;

/**
 * 意见反馈界面Model实现类
 *
 * @author sjl
 */
public class FeedBackModelImpl implements FeedBackModel {

    private ApiService mApiService;

    public FeedBackModelImpl() {
        RetrofitManagerUtil instance = RetrofitManagerUtil.getInstance();
        mApiService = instance.getApiService();
    }

    /**
     * 意见反馈
     *
     * @param desc     建议或问题描述
     * @param contact  联系手机或者联系邮箱
     * @param parts    描述图片
     * @param callBack 回调方法
     */
    @Override
    public void feedBack(String desc, String contact, MultipartBody.Part[] parts, CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<FeedBack> observable = mApiService.feedBack(timeStamp,
                OtherUtil.getSign(timeStamp, FEED_BACK_METHOD),
                YiBaiApplication.getUid(),
                desc,
                contact,
                parts);
        Observer<FeedBack> observer = new Observer<FeedBack>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(FeedBack feedBack) {
                callBack.onRequestComplete();
                callBack.onFeedBackSuccess(feedBack);
            }

            @Override
            public void onError(Throwable e) {
                callBack.onRequestFailure(e);
            }

            @Override
            public void onComplete() {

            }
        };
        observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(observer);
    }
}
