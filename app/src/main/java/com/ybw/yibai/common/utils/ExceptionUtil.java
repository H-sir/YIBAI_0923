package com.ybw.yibai.common.utils;

import android.net.ParseException;

import com.google.gson.JsonParseException;
import com.google.gson.stream.MalformedJsonException;
import com.ybw.yibai.R;
import com.ybw.yibai.base.YiBaiApplication;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLHandshakeException;

import retrofit2.HttpException;

/**
 * 异常处理工具类
 *
 * @author sjl
 */
public class ExceptionUtil {

    private static final String TAG = "ExceptionUtil";

    public static void handleException(Throwable e) {

        if (e instanceof HttpException) {

            LogUtil.e(TAG, "HTTP错误: " + e.getMessage());
            /* HTTP 504 Unsatisfiable Request (only-if-cached) */
            int code = ((HttpException) e).code();
            if (500 == code) {
                MessageUtil.showHttpException(YiBaiApplication.getContext().getResources().getString(R.string.five_hundred_code));
            } else if (504 == code) {
                MessageUtil.showNoNetwork();
            } else {
                MessageUtil.showHttpException("HTTP错误: " + e.getMessage());
            }

        } else if (e instanceof ConnectException) {

            LogUtil.e(TAG, "网络连接异常: " + e.getMessage());
            MessageUtil.showConnectException();

        } else if (e instanceof SocketTimeoutException) {

            LogUtil.e(TAG, "网络连接超时异常: " + e.getMessage());
            MessageUtil.showSocketTimeoutException();

        } else if (e instanceof UnknownHostException) {

            LogUtil.e(TAG, "未知主机异常: " + e.getMessage());
            MessageUtil.showUnknownHostException();

        } else if (e instanceof IllegalArgumentException) {

            LogUtil.e(TAG, "非法参数异常: " + e.getMessage());
            MessageUtil.showIllegalArgumentException();

        } else if (e instanceof SSLHandshakeException) {

            LogUtil.e(TAG, "证书验证失败异常: " + e.getMessage());
            MessageUtil.showSSLHandshakeException();

        } else if (e instanceof JSONException
                || e instanceof ParseException
                || e instanceof JsonParseException
                || e instanceof MalformedJsonException) {

            LogUtil.e(TAG, "数据解析异常: " + e.getMessage());
            MessageUtil.showParseException();

        } else {
            /* CLEARTEXT communication to api.100ybw.com not permitted by network security policy */
            /* 在Android P(API 28)系统的设备上,默认要求使用加密连接。譬如默认只能使用HTTPS进行通信,若强行使用HTTP通信,则会得到上面的异常 */
            /* 解决方法: https://www.jianshu.com/p/8501175311e8 */
            LogUtil.e(TAG, "未知错误Debug调试: " + e.getMessage());
        }
    }
}
