package com.ybw.yibai.base;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;

import com.ybw.yibai.common.broadcast.NetWorkStateReceiver;
import com.ybw.yibai.common.classs.ConcreteWatched;
import com.ybw.yibai.common.interfaces.Watcher;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.utils.ScreenAdaptationUtils;
import com.ybw.yibai.common.utils.StackManagerUtil;

import butterknife.ButterKnife;

import static android.content.res.Configuration.ORIENTATION_PORTRAIT;

/**
 * 基类的Activity(模板方法模式)
 *
 * @author sjl
 */
public abstract class BaseActivity extends AppCompatActivity implements Watcher {

    private BroadcastReceiver mBroadcastReceiver;

    /**
     * 定义onCreate()方法为final类型,禁止子类重写该方法
     */
    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        ScreenAdaptationUtils.applyAdaptationScreen((Application) YiBaiApplication.getContext(), this, ORIENTATION_PORTRAIT);
        setContentView(setLayout());
        ButterKnife.bind(this);
        initView();
        initData();
        initEvent();

        // 将当前Activity推入栈中
        StackManagerUtil.getInstance().pushActivity(this);
    }

    /**
     * 模板方法,初始化布局
     *
     * @return 布局资源ID
     */
    protected abstract int setLayout();

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
    protected void onResume() {
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
        mBroadcastReceiver = new NetWorkStateReceiver();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mBroadcastReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (null != ConcreteWatched.getInstance()) {
            // 移除一个观察者对象
            ConcreteWatched.getInstance().remove(this);
        }
        if (null != mBroadcastReceiver) {
            // 注销广播
            unregisterReceiver(mBroadcastReceiver);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 将当前的Activity推出栈中
        StackManagerUtil.getInstance().removeActivity(this);
    }

    /**
     * 点击屏幕上EditText区域以外的任何地方隐藏键盘的方法
     * 覆写事件的分发方法dispatchTouchEvent
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            // 获得当前得到焦点的View,一般情况下就是EditText
            View view = getCurrentFocus();
            if (OtherUtil.isShouldHideInput(view, ev)) {
                OtherUtil.hideSoftInput(this);
            }
            return super.dispatchTouchEvent(ev);
        }

        // 必不可少,否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }

        return onTouchEvent(ev);
    }
}
