package com.ybw.yibai.base;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ybw.yibai.common.broadcast.NetWorkStateReceiver;
import com.ybw.yibai.common.classs.ConcreteWatched;
import com.ybw.yibai.common.interfaces.Watcher;

/**
 * 基类的Fragment(模板方法模式)
 *
 * @author sjl
 */
public abstract class BaseFragment extends Fragment implements Watcher {

    private BroadcastReceiver mBroadcastReceiver;

    /**
     * 定义onCreateView()方法为final类型,禁止子类重写该方法
     */
    @Nullable
    @Override
    public final View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(setLayouts(), container, false);

        findViews(view);
        initView();
        initData();
        initEvent();

        return view;
    }

    /**
     * 模板方法,初始化布局
     *
     * @return 布局资源ID
     */
    protected abstract int setLayouts();

    /**
     * 模板方法,findView各种控件
     *
     * @param view View控件
     */
    protected abstract void findViews(View view);

    /**
     * 模板方法,初始化各种控件
     */
    protected abstract void initView();

    /**
     * 模板方法,加载各种数据
     */
    protected abstract void initData();

    /**
     * 模板方法,初始化各种响应事件
     */
    protected abstract void initEvent();

    @Override
    public void onResume() {
        super.onResume();
        register();
        if (null != ConcreteWatched.getInstance()) {
            // 添加一个观察者对象
            ConcreteWatched.getInstance().add(this);
        }
    }

    /**
     * 动态注册用于监控当前网络状态的广播
     */
    private void register() {
        Activity activity = getActivity();
        if (null != activity) {
            mBroadcastReceiver = new NetWorkStateReceiver();
            IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            activity.registerReceiver(mBroadcastReceiver, filter);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (null != ConcreteWatched.getInstance()) {
            // 移除一个观察者对象
            ConcreteWatched.getInstance().remove(this);
        }
        Activity activity = getActivity();
        if (null == activity) {
            return;
        }
        if (null != mBroadcastReceiver) {
            // 注销广播
            activity.unregisterReceiver(mBroadcastReceiver);
        }
    }
}