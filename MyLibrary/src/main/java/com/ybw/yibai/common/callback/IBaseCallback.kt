package com.ybw.yibai.common.callback

import io.reactivex.disposables.Disposable

interface IBaseCallback {
    //void onShowWait(@Nullable String _msg,@Nullable DialogInterface.OnCancelListener _listener);
    /**
     * 在请求网络数据之前
     */
    fun onRequestBefore(disposable: Disposable)

    /**
     * 在请求网络数据完成
     */
    fun onRequestComplete()

    /**
     * 在请求网络数据失败
     *
     * @param throwable 异常类型
     */
    fun onRequestFailure(throwable: Throwable?)
}