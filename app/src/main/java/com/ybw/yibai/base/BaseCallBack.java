package com.ybw.yibai.base;

import io.reactivex.disposables.Disposable;

/**
 * 基类的CallBack,用于在Presenter层回调Model层提供的信息
 *
 * @author sjl
 */
public interface BaseCallBack {

    /**
     * 在请求网络数据之前
     *
     * @param disposable 是RxJava切断Observer(观察者)与Observable(被观察者)之间的连接的对象
     */
    void onRequestBefore(Disposable disposable);

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
}
