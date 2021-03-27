package com.ybw.yibai.common.utils;

import android.text.TextUtils;

import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.constants.HttpUrls;
import com.ybw.yibai.common.interfaces.ApiService;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Retrofit 工具类
 *
 * @author sjl
 */
public class RetrofitManagerUtil {

    private final String TAG = "RetrofitManagerUtil";

    private static Retrofit retrofit;

    private static volatile RetrofitManagerUtil instance;

    public static RetrofitManagerUtil getInstance() {
        if (null == instance) {
            // 使用synchronized防止多个线程同时访问一个对象时发生异常
            synchronized (RetrofitManagerUtil.class) {
                if (null == instance) {
                    instance = new RetrofitManagerUtil();
                }
            }
        }
        return instance;
    }

    private RetrofitManagerUtil() {
        System.setProperty("http.proxyHost", "my.proxyhost.com");
        System.setProperty("http.proxyPort", "1234");
        /*
         * OkHttp日志拦截器,APP发布时去掉
         */
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(message ->
                LogUtil.e(TAG, "OkHttpLog: " + message))
                .setLevel(HttpLoggingInterceptor.Level.BODY);

        /*
         * 设置OkHttp缓存
         */
        Interceptor cacheInterceptor = chain -> {
            // 获取请求
            Request request = chain.request();

            // 判断网络状态(网络不可用)
            if (!(NetworkStateUtil.isNetworkAvailable(YiBaiApplication.getContext()))) {
                request = request.newBuilder()
                        // 只获取缓存(强制使用缓存,如果没有缓存数据,则抛出504(only-if-cached))
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            } else {
                String token = YiBaiApplication.getToken();
                if (TextUtils.isEmpty(token)) {
                    token = "";
                }
                // 网络可用(添加公共请求头)
                request = request.newBuilder()
                        .addHeader("did", AppUtil.getDeviceId(YiBaiApplication.getContext()))
                        .addHeader("token", token)
                        .addHeader("apiver", "v3")
                        .addHeader("version", String.valueOf(AppUtil.getVersionCode(YiBaiApplication.getContext())))
                        .addHeader("mobile_brand", AppUtil.getPhoneBrand())
                        .addHeader("mobile_model", AppUtil.getPhoneModel())
                        // 有网络时只从网络获取
                        .cacheControl(CacheControl.FORCE_NETWORK)
                        .build();
            }

            Response response = chain.proceed(request);
            // 判断网络状态(网络可用)
            if (NetworkStateUtil.isNetworkAvailable(YiBaiApplication.getContext())) {
                // 有网络时,设置缓存超时时间为0
                int maxAge = 0;
                response.newBuilder()
                        // 清除头信息, 因为服务器如果不支持会返回一些干扰信息,不清除下面无法生效
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .build();
            } else {
                // 无网络时,设置缓存超时时间为30天
                int maxStale = 60 * 60 * 24 * 30;
                response.newBuilder()
                        // 清除头信息, 因为服务器如果不支持会返回一些干扰信息,不清除下面无法生效
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build();
            }
            return response;
        };

        // 获取SD卡私有Cache目录的路径
        String cacheDir = SDCardHelperUtil.getSDCardPrivateCacheDir(YiBaiApplication.getContext());
        // 创建缓存文件夹
        File cacheFile = new File(cacheDir);
        // 缓存文件的大小为20MB
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 20);

        /*
         * 设置OkHttp的超时时间为15s,且错误重连,并且将其缓存机制设置上去
         */
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                // 配置此客户端是否在遇到连接问题时重连
                .retryOnConnectionFailure(true)
                .addInterceptor(cacheInterceptor)
                .addInterceptor(loggingInterceptor)
                // 添加缓存
                .cache(cache)
                .build();

        // 创建Retrofit对象
        retrofit = new Retrofit.Builder()
                // 设置用于请求的HTTP Client
                .client(client)
                // 设置API base URL
                .baseUrl(HttpUrls.BASE_URL)
                // 添加ScalarsConverterFactory标准转换器支持,去掉以Multipart上传参数时,String参数会多一对双引号
                .addConverterFactory(ScalarsConverterFactory.create())
                // 设置Gson作为解析Json数据的Converter
                .addConverterFactory(GsonConverterFactory.create())
                // 设置使用RxJava2作为CallAdapter
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public ApiService getApiService() {
        // 得到联网工具对象
        return retrofit.create(ApiService.class);
    }
}
