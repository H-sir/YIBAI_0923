package com.ybw.yibai.common.interfaces;

import com.ybw.yibai.common.bean.NetworkType;

/**
 * 被观察者(又称为主题)接口 -> 网络状态
 *
 * @author sjl
 */
public interface Watched {

    /**
     * 定义一个用来增加观察者的方法
     *
     * @param watcher 观察者对象
     */
    void add(Watcher watcher);

    /**
     * 定义一个用来删除观察者权利的方法
     *
     * @param watcher 观察者对象
     */
    void remove(Watcher watcher);

    /**
     * 定义一个通知观察者对象数据发生了改变的方法
     *
     * @param networkType 网络类型
     */
    void notifyWatcher(NetworkType networkType);
}
