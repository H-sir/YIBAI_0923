package com.ybw.yibai.common.bean;

/**
 * 在Fragment隐藏状态发生改变
 *
 * @author sjl
 */
public class HiddenChanged {

    /**
     * hidden true Fragment隐藏,false Fragment显示
     */
    private boolean hidden;

    public HiddenChanged(boolean hidden) {
        this.hidden = hidden;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }
}
