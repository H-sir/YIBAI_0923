package com.ybw.yibai.common.bean;

/**
 * 用于在其他界面跳转到首页的某一个Fragment的标记(EventBus使用)
 *
 * @author sjl
 */
public class ToFragment {

    /**
     * 页码,与首页排布一样
     */
    private int index;

    public ToFragment(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
