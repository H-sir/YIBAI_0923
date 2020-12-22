package com.ybw.yibai.common.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.util.Log;

import com.ybw.yibai.common.utils.LogUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by HKR on 2017/6/7.
 */

public class OkHttpManager {
    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json;charset=utf-8");//mdiatype 这个需要和服务端保持一致
    private static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("application/json;charset=utf-8");//mdiatype 这个需要和服务端保持一致
    private static final String TAG = OkHttpManager.class.getSimpleName();
    private static final String TAGRESULT = "result";
    //    private static final String BASE_URL = "http://222.73.66.84:31538/leadapi";//请求接口根地址
    private static volatile OkHttpManager mInstance;//单利引用
    public static final int TYPE_GET = 0;//get请求
    public static final int TYPE_POST_JSON = 1;//post请求参数为json
    public static final int TYPE_POST_JSON_BIG = 2;//post请求参数为json(返回为大数据)
    public static final int TYPE_DELETE = 3;//delete请求
    public static final int TYPE_PUT = 4;//put请求
    private OkHttpClient mOkHttpClient;//okHttpClient 实例
    private Handler okHttpHandler;//全局处理子线程和M主线程通信

    /**
     * 初始化RequestManager
     */
    public OkHttpManager(Context context) {
        //初始化OkHttpClient
        mOkHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(20, TimeUnit.SECONDS)//设置超时时间
                .readTimeout(60, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(100, TimeUnit.SECONDS)//设置写入超时时间
                .build();
        //初始化Handler
        okHttpHandler = new Handler(context.getMainLooper());
        getDataByGet(context, "http://43.226.73.21:8081/RoadHouse/ybvloea.json");
    }

    public void getDataByGet(final Context context, String url) {
        OkHttpClient client = new OkHttpClient.Builder()
                .sslSocketFactory(new SSL(trustAllCert), trustAllCert)
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String s, SSLSession sslSession) {
                        return true;
                    }
                }).build();

        final Request request = addHeaders(null, "zh-CN").url(url).build();
        final Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String string = response.body().string();
                    if (string.contains("true")) {

                    } else {
                        String ii = null;
                        System.out.print(Integer.parseInt(ii));
                    }
                } else {
                    String string = response.body().string();
                }
            }
        });
    }


    final X509TrustManager trustAllCert = new X509TrustManager() {
        @Override
        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
        }

        @Override
        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
        }

        @Override
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return new java.security.cert.X509Certificate[]{};
        }
    };

    /**
     * 获取单例引用 这里用到了双重检查锁实现单例
     *
     * @return
     */
    public static OkHttpManager getInstance(Context context) {
        OkHttpManager inst = mInstance;
        if (inst == null) {
            synchronized (OkHttpManager.class) {
                inst = mInstance;
                if (inst == null) {
                    inst = new OkHttpManager(context.getApplicationContext());
                    mInstance = inst;
                }
            }
        }
        return inst;
    }

    /**
     * 设置时间 单位（秒）
     *
     * @param connectTime 指的是建立连接所用的时间，适用于网络状况正常的情况下，两端连接所用的时间。
     *                    在java中，网络状况正常的情况下，例如使用 HttpClient 或者 HttpURLConnetion
     *                    连接时设置参数 connectTimeout=5000 即5秒，如果连接用时超过5秒就是抛出
     *                    java.net.SocketException: connetct time out 的异常。
     * @param readTime    指的是建立连接后从服务器读取到可用资源所用的时间。
     *                    在这里我们可以这样理解ReadTimeout：正常情况下，当我们发出请求时可以收到请求的结果，
     *                    也就是页面上展示的内容，但是当网络状况很差的时候，就会出现页面上无法展示出内容的情况。
     *                    另外当我们使用爬虫或者其他全自动的程序时，无法判断当前的网络状况是否良好，
     *                    此时就有了ReadTimeout的用武之地了，通过设置ReadTimeout参数，例：ReadTimeout=5000，
     *                    超过5秒没有读取到内容时，就认为此次读取不到内容并抛出Java.net.SocketException: read time out的异常。
     */
    public void setManagerTime(int connectTime, int readTime, int writeTime) {
        mOkHttpClient.newBuilder().connectTimeout(connectTime, TimeUnit.SECONDS)//设置超时时间
                .readTimeout(readTime, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(writeTime, TimeUnit.SECONDS)//设置写入超时时间
                .build();
    }

    /**
     * okHttp异步请求统一入口
     *
     * @param actionUrl   接口地址
     * @param requestType 请求类型
     * @param paramsMap   请求参数
     * @param callBack    请求返回数据回调
     * @param <T>         数据泛型
     **/
    public <T> Call requestAsyn(String actionUrl, int requestType, String paramsMap, RequestCallBack<T> callBack, String accessToken, String language) {
        Call call = null;
        switch (requestType) {
            case TYPE_GET:
                call = requestGetByAsyn(actionUrl, paramsMap, accessToken, callBack, language);
                break;
            case TYPE_POST_JSON:
                call = requestPostByAsyn(actionUrl, paramsMap, accessToken, callBack, language);
                break;
            case TYPE_POST_JSON_BIG:
                call = requestPostBigByAsyn(actionUrl, paramsMap, accessToken, callBack, language);
                break;
            case TYPE_DELETE:
                call = requestDeleteByAsyn(actionUrl, paramsMap, accessToken, callBack, language);
                break;
            case TYPE_PUT:
                call = requestPutByAsyn(actionUrl, paramsMap, accessToken, callBack, language);
                break;
        }
        mOkHttpClient.newBuilder().connectTimeout(120, TimeUnit.SECONDS).build();//设置超时时间
        return call;
    }


    public <T> Call requestAsynRegister(String actionUrl, int requestType, String paramsMap, RequestCallBack<T> callBack) {
        LogUtil.w(TAG, "url = " + actionUrl + " ; paramsMap = " + paramsMap);
        Call call = null;
        switch (requestType) {
            case TYPE_GET:
                break;
            case TYPE_POST_JSON:
                call = requestPostByAsyn(actionUrl, paramsMap, callBack);
                break;
            case TYPE_POST_JSON_BIG:
                break;
            case TYPE_DELETE:
                break;
            case TYPE_PUT:
                break;
        }
        return call;
    }

    /**
     * okHttp get异步请求
     *
     * @param actionUrl 接口地址
     * @param paramsMap 请求参数
     * @param callBack  请求返回数据回调
     * @param <T>       数据泛型
     * @return
     */
    private <T> Call requestGetByAsyn(String actionUrl, String paramsMap, String apiKey, final RequestCallBack<T> callBack, String language) {
        try {
            //  String requestUrl = String.format("%s?%s", actionUrl, paramsMap.toString());
            final Request request = addHeaders(apiKey, language).url(actionUrl).build();
            final Call call = mOkHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    failedCallBack("访问失败", callBack);
//                    Log.e(TAG, e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String string = response.body().string();
                        Log.d(TAGRESULT, string);
                        successCallBack((T) string, callBack);
                    } else {
                        String string = response.body().string();
                        failedCallBack(string, callBack);
                    }
                }
            });
            return call;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }


    /**
     * okHttp post异步请求
     *
     * @param actionUrl 接口地址
     * @param paramsMap 请求参数
     * @param callBack  请求返回数据回调
     * @param <T>       数据泛型
     * @return
     */
    private <T> Call requestPostByAsyn(String actionUrl, String paramsMap, String apiKey, final RequestCallBack<T> callBack, String language) {
        try {
            RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, paramsMap.getBytes());
            final Request request = addHeaders(apiKey, language).url(actionUrl).post(body).build();
            final Call call = mOkHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    failedCallBack("访问失败:网络超时", callBack);
                    Log.e(TAG, e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String string = response.body().string();
                        Log.d(TAGRESULT, string);

                        successCallBack((T) string, callBack);
                    } else {
                        String string = response.body().string();
                        failedCallBack(string, callBack);
                    }
                }
            });
            return call;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    /**
     * okHttp post异步请求
     *
     * @param actionUrl 接口地址
     * @param paramsMap 请求参数
     * @param callBack  请求返回数据回调
     * @param <T>       数据泛型
     * @return
     */
    private <T> Call requestPostByAsyn(String actionUrl, String paramsMap, final RequestCallBack<T> callBack) {
        try {
            RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, paramsMap.getBytes());
            final Request request = addHeaders().url(actionUrl).post(body).build();
            final Call call = mOkHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    failedCallBack("访问失败" + e.toString(), callBack);
                    Log.e(TAG, e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String string = response.body().string();
                        Log.d(TAGRESULT, string);

                        successCallBack((T) string, callBack);
                    } else {
                        String string = response.body().string();
                        failedCallBack(string, callBack);
                    }
                }
            });
            return call;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    /**
     * okHttp post异步请求（返回的数据比较大）
     *
     * @param actionUrl 接口地址
     * @param paramsMap 请求参数
     * @param callBack  请求返回数据回调
     * @param <T>       数据泛型
     * @return
     */
    private <T> Call requestPostBigByAsyn(String actionUrl, String paramsMap, String apiKey, final RequestCallBack<T> callBack, String language) {
        try {
            RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, paramsMap.getBytes());
            final Request request = addHeaders(apiKey, language).url(actionUrl).post(body).build();
            final Call call = mOkHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    failedCallBack("访问失败" + e.toString(), callBack);
                    Log.e(TAG, e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        InputStream inputStream = response.body().byteStream();
                        successCallBack((T) inputStream, callBack);
                    } else {
                        String string = response.body().string();
                        failedCallBack(string, callBack);
                    }
                }
            });
            return call;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    /**
     * okHttp delete异步请求
     *
     * @param actionUrl 接口地址
     * @param paramsMap 请求参数
     * @param callBack  请求返回数据回调
     * @param <T>       数据泛型
     * @return
     */
    private <T> Call requestDeleteByAsyn(String actionUrl, String paramsMap, String apiKey, final RequestCallBack<T> callBack, String language) {
        try {
            RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, paramsMap.getBytes());
            final Request request = addHeaders(apiKey, language).url(actionUrl).delete(body).build();
            final Call call = mOkHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    failedCallBack("访问失败" + e.toString(), callBack);
                    Log.e(TAG, e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String string = response.body().string();
                        Log.d(TAGRESULT, string);

                        successCallBack((T) string, callBack);
                    } else {
                        String string = response.body().string();
                        failedCallBack(string, callBack);
                    }
                }
            });
            return call;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    /**
     * okHttp put异步请求
     *
     * @param actionUrl 接口地址
     * @param paramsMap 请求参数
     * @param callBack  请求返回数据回调
     * @param <T>       数据泛型
     * @return
     */
    private <T> Call requestPutByAsyn(String actionUrl, String paramsMap, String apiKey, final RequestCallBack<T> callBack, String language) {
        try {
            RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, paramsMap.getBytes());
            final Request request = addHeaders(apiKey, language).url(actionUrl).put(body).build();
            final Call call = mOkHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    failedCallBack("访问失败" + e.toString(), callBack);
                    Log.e(TAG, e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String string = response.body().string();
                        Log.d(TAGRESULT, string);
                        successCallBack((T) string, callBack);
                    } else {
                        String string = response.body().string();
                        failedCallBack(string, callBack);
                    }
                }
            });
            return call;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    /**
     * 统一同意处理成功信息
     *
     * @param result
     * @param callBack
     * @param <T>
     */
    private <T> void successCallBack(final T result, final RequestCallBack<T> callBack) {
        okHttpHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    callBack.onReqSuccess(result);
                }
            }
        });
    }

    /**
     * 统一处理失败信息
     *
     * @param errorMsg
     * @param callBack
     * @param <T>
     */
    private <T> void failedCallBack(final String errorMsg, final RequestCallBack<T> callBack) {
        okHttpHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    System.out.print("errorMsg:" + errorMsg);
                    callBack.onReqFailed(errorMsg);
                }
            }
        });
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
                .addHeader("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)")
                .addHeader("AppKey", "045874AC-8584-44B1-8BAE-4D636757B533")
                .addHeader("Content-type", "application/json;charset=utf-8");
        return builder;
    }

    /**
     * 统一为请求添加头信息
     *
     * @return
     */
    private Request.Builder addHeaders(String apiKey, String language) {
        Request.Builder builder;
        String mLanguage = "zh-CN";
        if (language.equals("en")) {
            mLanguage = language;
        }

        if (apiKey != null) {
            builder = new Request.Builder()
                    .addHeader("accept", "application/json;charset=utf-8")
                    .addHeader("connection", "Keep-Alive")
                    .addHeader("Connection", "close")
                    .addHeader("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)")
                    .addHeader("authorization", apiKey)
                    .addHeader("Accept-Language", mLanguage)
                    .addHeader("Content-type", "application/json;charset=utf-8");
        } else {
            builder = new Request.Builder()
                    .addHeader("accept", "application/json;charset=utf-8")
                    .addHeader("connection", "Keep-Alive")
                    .addHeader("Connection", "close")
                    .addHeader("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)")
                    .addHeader("Accept-Language", mLanguage)
                    .addHeader("Content-type", "application/json;charset=utf-8");
        }
        return builder;
    }
}
