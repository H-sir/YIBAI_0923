/*
 * Copyright (c) 2020/1/7. by Onlly
 * 个人简介：野生程序员
 * QQ:412102100
 * WX:i412102100
 * Github: Github.com/mingyouzhu
 * 简书: https://www.jianshu.com/u/4198eba0fbdd
 */

package com.ybw.yibai.view.inter;

import android.content.Context;

public interface IBaseView {
    void onFinish();
    //void onHideWait();
    Context onGetContext();
    void onShowToastShort(String msg);
    void onShowToastLength(String msg);
    //void onShowWait(@Nullable String _msg,@Nullable DialogInterface.OnCancelListener _listener);

    /**
     * 在请求网络数据之前
     */
    void onRequestBefore();

    /**
     * 在请求网络数据完成
     */
    void onRequestComplete();
    /**
     * 在请求网络数据失败
     *
     * @param throwable 异常类型
     */
    void onRequestFailure(Throwable throwable);

    /**
     * 在请求网络数据之前显示Loading界面
     */
    void onShowLoading();

    /**
     * 在请求网络数据完成隐藏Loading界面
     */
    void onHideLoading();
    /**
     * 在请求网络数据失败时进行一些操作,如显示错误信息...
     *
     * @param throwable 异常类型
     */
    void onLoadDataFailure(Throwable throwable);
}