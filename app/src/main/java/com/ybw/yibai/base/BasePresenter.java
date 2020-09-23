package com.ybw.yibai.base;

import io.reactivex.disposables.Disposable;

/**
 * 基类的Presenter
 *
 * @param <V> 泛型 -> View的对象
 * @author sjl
 */
public interface BasePresenter<V extends BaseView> {

    /**
     * 在创建PresenterImpl对象时,把view对象添加进来
     *
     * @param view 要添加的view对象
     */
    void onAttachView(V view);

    /**
     * 在进行网络请求时,将网络请求的每一个disposable添加进入CompositeDisposable,再在退出时候一并注销
     *
     * @param disposable 是RxJava切断Observer(观察者)与Observable(被观察者)之间的连接的对象
     */
    void onAddDisposable(Disposable disposable);

    /**
     * 在Activity销毁时注销所有的网络请求
     */
    void onUnDisposable();

    /**
     * 在Activity销毁时把view对象置为null
     */
    void onDetachView();
}
