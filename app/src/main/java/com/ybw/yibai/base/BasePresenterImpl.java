package com.ybw.yibai.base;

import com.ybw.yibai.common.utils.LogUtil;
import com.ybw.yibai.common.utils.MessageUtil;

import java.lang.ref.WeakReference;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * 基类BasePresenterImpl
 * 实现了所有的Presenter都共有的一些方法,然后在由某一个具体的PresenterImpl继承就可以了
 *
 * @param <V> 泛型 -> View的对象
 * @author sjl
 */
public class BasePresenterImpl<V extends BaseView> implements BasePresenter<V>, BaseCallBack {

    private final String TAG = "BasePresenterImpl";

    /**
     * 采用了弱引用的方式去保存这个View的对象引用,减少内存泄露的可能性
     */
    private WeakReference<V> mWeakReference;

    /**
     * RxJava2中一个可以管理Disposable对象的类,例如添加or删除操作
     */
    private CompositeDisposable mCompositeDisposable;

    /**
     * 构造方法
     *
     * @param view View的对象
     */
    public BasePresenterImpl(V view) {
        onAttachView(view);
    }

    /**
     * 在创建PresenterImpl对象时,把view对象添加进来
     *
     * @param view View的对象
     */
    @Override
    public void onAttachView(V view) {
        mWeakReference = new WeakReference<>(view);
    }

    /**
     * 在进行网络请求时,将网络请求的每一个disposable添加进入CompositeDisposable,再在退出时候一并注销
     *
     * @param disposable 是RxJava切断Observer(观察者)与Observable(被观察者)之间的连接的对象
     */
    @Override
    public void onAddDisposable(Disposable disposable) {
        if (null == mCompositeDisposable || mCompositeDisposable.isDisposed()) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);
    }

    /**
     * 在请求网络数据之前显示Loading界面
     */
    @Override
    public void onRequestBefore(Disposable disposable) {
        onAddDisposable(disposable);
        if (null != mWeakReference && null != mWeakReference.get()) {
            mWeakReference.get().onShowLoading();
        } else {
            LogUtil.e(TAG, "mWeakReference是空的,请进行Debug调试");
        }
    }

    /**
     * 在请求网络数据完成隐藏Loading界面
     */
    @Override
    public void onRequestComplete() {
        if (null != mWeakReference && null != mWeakReference.get()) {
            mWeakReference.get().onHideLoading();
        } else {
            LogUtil.e(TAG, "mWeakReference是空的,请进行Debug调试");
        }
    }

    /**
     * 在请求网络数据失败时进行一些操作,如隐藏Loading界面and显示错误信息...
     *
     * @param throwable 异常类型
     */
    @Override
    public void onRequestFailure(Throwable throwable) {
        if (null != mWeakReference && null != mWeakReference.get()) {
            mWeakReference.get().onHideLoading();
            MessageUtil.showMessage(throwable.getMessage());
            mWeakReference.get().onLoadDataFailure(throwable);
        } else {
            LogUtil.e(TAG, "mWeakReference是空的,请进行Debug调试");
        }
    }

    /**
     * 在Activity销毁时注销所有的网络请求
     */
    @Override
    public void onUnDisposable() {
        if (null != mCompositeDisposable && !mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.clear();
        }
    }

    /**
     * 在Activity销毁时把view对象置为null
     */
    @Override
    public void onDetachView() {
        onUnDisposable();
        if (null != mWeakReference) {
            mWeakReference.clear();
            mWeakReference = null;
        }
    }

    /**
     * 额外添加一个方法给继承BasePresenterImpl的Presenter类提供当前View的弱引用对象
     *
     * @return View的弱引用对象
     */
    protected V getView() {
        if (null != mWeakReference && null != mWeakReference.get()) {
            return mWeakReference.get();
        }
        LogUtil.e(TAG, "View是空的,请进行Debug调试");
        return null;
    }
}
