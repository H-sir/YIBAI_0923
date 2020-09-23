package com.ybw.yibai.common.utils;

import com.ybw.yibai.R;
import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.widget.CustomToast;

/**
 * 显示错误信息的工具类
 *
 * @author sjl
 */
public class MessageUtil {

    /**
     * 显示信息
     *
     * @param message 要显示的信息
     */
    public static void showMessage(String message) {
        CustomToast.toast(
                YiBaiApplication.getContext(),
                message,
                R.color.toast_text_color,
                R.color.toast_background_color
        );
    }

    /**
     * 显示网络连接不可用
     */
    public static void showNoNetwork() {
        CustomToast.toast(
                YiBaiApplication.getContext(),
                YiBaiApplication.getContext().getResources().getString(R.string.no_network),
                R.color.toast_text_color,
                R.color.toast_background_color
        );
    }

    /**
     * 显示HTTP错误
     *
     * @param exceptionMessage 错误消息的内容
     */
    public static void showHttpException(String exceptionMessage) {
        CustomToast.toast(
                YiBaiApplication.getContext(),
                exceptionMessage,
                R.color.toast_text_color,
                R.color.toast_background_color
        );
    }

    /**
     * 显示网络连接异常
     */
    public static void showConnectException() {
        CustomToast.toast(
                YiBaiApplication.getContext(),
                YiBaiApplication.getContext().getResources().getString(R.string.connect_exception),
                R.color.toast_text_color,
                R.color.toast_background_color
        );
    }

    /**
     * 显示网络连接超时异常
     */
    public static void showSocketTimeoutException() {
        CustomToast.toast(
                YiBaiApplication.getContext(),
                YiBaiApplication.getContext().getResources().getString(R.string.socket_timeout_exception),
                R.color.toast_text_color,
                R.color.toast_background_color
        );
    }

    /**
     * 显示未知主机异常(抛出以指示无法确定主机的IP地址)
     */
    public static void showUnknownHostException() {
        CustomToast.toast(
                YiBaiApplication.getContext(),
                YiBaiApplication.getContext().getResources().getString(R.string.unknown_host_exception),
                R.color.toast_text_color,
                R.color.toast_background_color
        );
    }

    /**
     * 显示非法参数异常
     */
    public static void showIllegalArgumentException() {
        CustomToast.toast(
                YiBaiApplication.getContext(),
                YiBaiApplication.getContext().getResources().getString(R.string.illegal_argument_exception),
                R.color.toast_text_color,
                R.color.toast_background_color
        );
    }

    /**
     * 显示证书验证失败异常
     */
    public static void showSSLHandshakeException() {
        CustomToast.toast(
                YiBaiApplication.getContext(),
                YiBaiApplication.getContext().getResources().getString(R.string.SSL_handshake_exception),
                R.color.toast_text_color,
                R.color.toast_background_color
        );
    }

    /**
     * 显示数据解析异常
     */
    public static void showParseException() {
        CustomToast.toast(
                YiBaiApplication.getContext(),
                YiBaiApplication.getContext().getResources().getString(R.string.parse_exception),
                R.color.toast_text_color,
                R.color.toast_background_color
        );
    }
}
