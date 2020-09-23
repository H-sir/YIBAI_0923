package com.ybw.yibai.common.interfaces;

import com.ybw.yibai.common.bean.NetworkType;

/**
 * 观察者(订阅者)接口,需要用到观察者模式的类需实现此接口 -> 网络状态
 *
 * @author sjl
 */
public interface Watcher {

    /**
     * 定义一个用来获取更新信息(网络状态)接收的方法
     *
     * @param networkType 网络类型
     */
    void onNetworkStateChange(NetworkType networkType);
}
