package com.ybw.yibai.module.main;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.LauncherApps;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;

import com.ybw.yibai.R;
import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.bean.AppUpdate;
import com.ybw.yibai.common.bean.ProductScreeningParam;
import com.ybw.yibai.common.bean.SceneInfo;
import com.ybw.yibai.common.bean.SystemParameter;
import com.ybw.yibai.common.bean.UserInfo;
import com.ybw.yibai.common.bean.UserInfo.DataBean;
import com.ybw.yibai.common.bean.VloeaBean;
import com.ybw.yibai.common.helper.SceneHelper;
import com.ybw.yibai.common.interfaces.ApiService;
import com.ybw.yibai.common.utils.LogUtil;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.utils.RetrofitManagerUtil;
import com.ybw.yibai.common.utils.TimeUtil;
import com.ybw.yibai.module.main.MainContract.CallBack;
import com.ybw.yibai.module.main.MainContract.MainModel;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.ybw.yibai.common.constants.Encoded.CODE_SUCCEED;
import static com.ybw.yibai.common.constants.HttpUrls.APP_UPDATE_METHOD;
import static com.ybw.yibai.common.constants.HttpUrls.GET_PRODUCT_SCREENING_PARAM_METHOD;
import static com.ybw.yibai.common.constants.HttpUrls.GET_SYSTEM_PARAMETER_METHOD;
import static com.ybw.yibai.common.constants.HttpUrls.GET_USER_INFO_METHOD;
import static com.ybw.yibai.common.constants.Preferences.ACTIVITY_INVITE_BG;
import static com.ybw.yibai.common.constants.Preferences.ADDRESS;
import static com.ybw.yibai.common.constants.Preferences.CITY_NAME;
import static com.ybw.yibai.common.constants.Preferences.COMPANY;
import static com.ybw.yibai.common.constants.Preferences.COMPANY_DETAILS;
import static com.ybw.yibai.common.constants.Preferences.COMPANY_DETAILS_PIC;
import static com.ybw.yibai.common.constants.Preferences.COMPANY_LOGO;
import static com.ybw.yibai.common.constants.Preferences.COM_OPEN;
import static com.ybw.yibai.common.constants.Preferences.CURRENCY_SYMBOL;
import static com.ybw.yibai.common.constants.Preferences.EMAIL;
import static com.ybw.yibai.common.constants.Preferences.HEAD;
import static com.ybw.yibai.common.constants.Preferences.INCREASE_RENT;
import static com.ybw.yibai.common.constants.Preferences.INCREASE_SELL;
import static com.ybw.yibai.common.constants.Preferences.INVITE_BG;
import static com.ybw.yibai.common.constants.Preferences.INVITE_RULE_BG;
import static com.ybw.yibai.common.constants.Preferences.INVITE_URL;
import static com.ybw.yibai.common.constants.Preferences.IS_BIND_WX;
import static com.ybw.yibai.common.constants.Preferences.MARKET_ID;
import static com.ybw.yibai.common.constants.Preferences.MARKET_NAME;
import static com.ybw.yibai.common.constants.Preferences.MY_WALLET_URL;
import static com.ybw.yibai.common.constants.Preferences.NICK_NAME;
import static com.ybw.yibai.common.constants.Preferences.RECORD_URL;
import static com.ybw.yibai.common.constants.Preferences.ROLE_NAME;
import static com.ybw.yibai.common.constants.Preferences.TAKE_CASE_URL;
import static com.ybw.yibai.common.constants.Preferences.TELEPHOTO;
import static com.ybw.yibai.common.constants.Preferences.TRUE_NAME;
import static com.ybw.yibai.common.constants.Preferences.USER_INFO;
import static com.ybw.yibai.common.constants.Preferences.USER_NAME;
import static com.ybw.yibai.common.constants.Preferences.USER_VIP_URL;
import static com.ybw.yibai.common.constants.Preferences.VIP_LEVEL;
import static com.ybw.yibai.common.constants.Preferences.WATERMARK_PIC;

/**
 * 主界面Model实现类
 *
 * @author sjl
 * @date 2019/09/05
 */
public class MainModelImpl implements MainModel {

    private ApiService mApiService;

    public MainModelImpl() {
        RetrofitManagerUtil instance = RetrofitManagerUtil.getInstance();
        mApiService = instance.getApiService();
    }

    /**
     * 查找是否有默认场景,如果没有就创建一个默认创建
     */
    @Override
    public void findIfThereIsDefaultScene() {
        try {
            DbManager dbManager = YiBaiApplication.getDbManager();
            if (null == dbManager) {
                return;
            }
            // 用户默认场景
            List<SceneInfo> defaultSceneInfoList = dbManager.selector(SceneInfo.class)
                    .where("uid", "=", YiBaiApplication.getUid())
                    .and("editScene", "=", true)
                    .findAll();
            if (null != defaultSceneInfoList && defaultSceneInfoList.size() > 0) {
                // 有默认场景
                return;
            }
            // 没有默认场景
            SceneInfo sceneInfo = new SceneInfo();
            sceneInfo.setUid(YiBaiApplication.getUid());
            sceneInfo.setSceneId(TimeUtil.getTimeStamp());
            String sceneName = SceneHelper.saveSceneNum(YiBaiApplication.getContext());
            sceneInfo.setSceneName(sceneName);
//            sceneInfo.setSceneName(YiBaiApplication.getContext().getResources().getString(R.string.my_scene));
            sceneInfo.setEditScene(true);
            // 保存默认的场景信息
            dbManager.save(sceneInfo);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 统一为请求添加头信息
     *
     * @return
     */
    private Request.Builder addHeaders() {
        Request.Builder builder = new Request.Builder()
                .addHeader("accept", "application/json;charset=utf-8")
                .addHeader("connection", "Keep-Alive")
                .addHeader("Connection", "close")
                .addHeader("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)")
                .addHeader("Content-type", "application/json;charset=utf-8");
        return builder;
    }

    /**
     * 请求应用更新
     *
     * @param callBack 回调方法
     */
    @Override
    public void appUpdate(final CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<AppUpdate> observable = mApiService.appUpdate(timeStamp,
                OtherUtil.getSign(timeStamp, APP_UPDATE_METHOD),
                YiBaiApplication.getUid());
        Observer<AppUpdate> observer = new Observer<AppUpdate>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(AppUpdate value) {
                callBack.onAppUpdateSuccess(value);
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
     * 获取用户信息
     *
     * @param callBack 回调方法
     */
    @Override
    public void getUserInfo(CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<UserInfo> observable = mApiService.getUserInfo(timeStamp,
                OtherUtil.getSign(timeStamp, GET_USER_INFO_METHOD),
                YiBaiApplication.getUid());
        Observer<UserInfo> observer = new Observer<UserInfo>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(UserInfo userInfo) {
                saveUserInfo(userInfo);
                callBack.onGetUserInfoSuccess(userInfo);
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
     * 获取系统参数
     *
     * @param callBack 回调方法
     */
    @Override
    public void getSystemParameter(CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<SystemParameter> observable = mApiService.getSystemParameter(timeStamp,
                OtherUtil.getSign(timeStamp, GET_SYSTEM_PARAMETER_METHOD),
                YiBaiApplication.getUid());
        Observer<SystemParameter> observer = new Observer<SystemParameter>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(SystemParameter systemParameter) {
                callBack.onGetSystemParameterSuccess(systemParameter);
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
     * 获取产品筛选参数
     *
     * @param callBack 回调方法
     */
    @Override
    public void getProductScreeningParam(CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<ProductScreeningParam> observable = mApiService.getProductScreeningParam(timeStamp,
                OtherUtil.getSign(timeStamp, GET_PRODUCT_SCREENING_PARAM_METHOD),
                YiBaiApplication.getUid());
        Observer<ProductScreeningParam> observer = new Observer<ProductScreeningParam>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(ProductScreeningParam productScreeningParam) {
                callBack.onGetProductScreeningParamSuccess(productScreeningParam);
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

    private void saveUserInfo(UserInfo userInfo) {
        if (CODE_SUCCEED != userInfo.getCode()) {
            return;
        }
        DataBean data = userInfo.getData();
        if (null == data) {
            return;
        }
        Context context = YiBaiApplication.getContext();
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER_INFO, MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        // 成功后保存这些信息
        String userName = data.getUsername();
        if (!TextUtils.isEmpty(userName)) {
            edit.putString(USER_NAME, userName);
        }
        String nickname = data.getNickname();
        if (!TextUtils.isEmpty(nickname)) {
            edit.putString(NICK_NAME, nickname);
        }
        String trueName = data.getTruename();
        if (!TextUtils.isEmpty(trueName)) {
            edit.putString(TRUE_NAME, trueName);
        }
        String roleName = data.getRole();
        if (!TextUtils.isEmpty(roleName)) {
            edit.putString(ROLE_NAME, roleName);
            YiBaiApplication.setRoleName(roleName);
        }
        String telephone = data.getTelephone();
        if (!TextUtils.isEmpty(telephone)) {
            edit.putString(TELEPHOTO, telephone);
        }
        String head = data.getHead();
        if (!TextUtils.isEmpty(head)) {
            edit.putString(HEAD, head);
        }
        String email = data.getEmail();
        if (!TextUtils.isEmpty(email)) {
            edit.putString(EMAIL, email);
        }
        String address = data.getAddress();
        if (!TextUtils.isEmpty(address)) {
            edit.putString(ADDRESS, address);
        }
        String company = data.getCompany();
        if (!TextUtils.isEmpty(company)) {
            edit.putString(COMPANY, company);
        }
        String companyLogoPic = data.getCompany_logo_pic();
        if (!TextUtils.isEmpty(companyLogoPic)) {
            edit.putString(COMPANY_LOGO, companyLogoPic);
        }
        String companyDetails = data.getCompany_details();
        if (!TextUtils.isEmpty(companyDetails)) {
            edit.putString(COMPANY_DETAILS, companyDetails);
        }
        String companyDetailsPic = data.getCompany_details_pic();
        if (!TextUtils.isEmpty(companyDetailsPic)) {
            edit.putString(COMPANY_DETAILS_PIC, companyDetailsPic);
        }

        int isBindWX = data.getIsbindwx();
        edit.putInt(IS_BIND_WX, isBindWX);
        int vipLevel = data.getVip_level();
        if (YiBaiApplication.getUid() == 1007) {
            edit.putInt(VIP_LEVEL, 3);
        } else {
            edit.putInt(VIP_LEVEL, vipLevel);
        }

        int increaseRent = data.getIncrease_rent();
        edit.putInt(INCREASE_RENT, increaseRent);
        int increaseSell = data.getIncrease_sell();
        edit.putInt(INCREASE_SELL, increaseSell);
        String currency = data.getCurrency();
        if (!TextUtils.isEmpty(currency)) {
            edit.putString(CURRENCY_SYMBOL, currency);
            YiBaiApplication.setCurrencySymbol(currency);
        }

        String yqbg = data.getYqbg();
        if (!TextUtils.isEmpty(yqbg)) {
            edit.putString(INVITE_BG, yqbg);
        }
        String xxgzbg = data.getXxgzbg();
        if (!TextUtils.isEmpty(xxgzbg)) {
            edit.putString(INVITE_RULE_BG, xxgzbg);
        }
        String yqhybg = data.getYqhybg();
        if (!TextUtils.isEmpty(yqhybg)) {
            edit.putString(ACTIVITY_INVITE_BG, yqhybg);
        }

        String inviteUrl = data.getInvite_url();
        if (!TextUtils.isEmpty(inviteUrl)) {
            edit.putString(INVITE_URL, inviteUrl);
        }
        String recordUrl = data.getRecord_url();
        if (!TextUtils.isEmpty(recordUrl)) {
            edit.putString(RECORD_URL, recordUrl);
        }
        String userVipUrl = data.getUservip_url();
        if (!TextUtils.isEmpty(userVipUrl)) {
            edit.putString(USER_VIP_URL, userVipUrl);
        }
        String myWalletUrl = data.getMywallet_url();
        if (!TextUtils.isEmpty(myWalletUrl)) {
            edit.putString(MY_WALLET_URL, myWalletUrl);
        }
        String takeCashUrl = data.getTakecash_url();
        if (!TextUtils.isEmpty(takeCashUrl)) {
            edit.putString(TAKE_CASE_URL, takeCashUrl);
        }
        String watermarkPic = data.getWatermark_pic();
        if (!TextUtils.isEmpty(watermarkPic)) {
            edit.putString(WATERMARK_PIC, watermarkPic);
        }
        String cityName = data.getCity_name();
        if (!TextUtils.isEmpty(cityName)) {
            edit.putString(CITY_NAME, cityName);
            SceneHelper.saveCity(context, cityName);
        }
        String com_open = data.getCom_open();
        if (!TextUtils.isEmpty(com_open)) {
            edit.putString(COM_OPEN, com_open);
        }
        DataBean.MarketData market = data.getMarket();
        if (market != null) {
            if (!TextUtils.isEmpty(market.getId())) {
                edit.putString(MARKET_ID, market.getId());
            }
            if (!TextUtils.isEmpty(market.getName())) {
                edit.putString(MARKET_NAME, market.getName());
            }
        }
        edit.apply();
    }

    @Override
    public void findUserSceneListInfo(CallBack callBack) {
        {
            try {
                DbManager manager = YiBaiApplication.getDbManager();
                if (null == manager) {
                    return;
                }
                List<SceneInfo> sceneInfoList = manager.selector(SceneInfo.class)
                        .where("uid", "=", YiBaiApplication.getUid())
                        .and("editScene", "=", true)
                        .findAll();
                if (null != sceneInfoList && sceneInfoList.size() > 0) {
                    if (sceneInfoList.get(0).getNumber() != null && !sceneInfoList.get(0).getNumber().isEmpty()) {
                        callBack.findUserSceneListInfo(true);
                    } else {
                        callBack.findUserSceneListInfo(false);
                    }
                } else {
                    callBack.findUserSceneListInfo(false);
                }
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
    }
}
