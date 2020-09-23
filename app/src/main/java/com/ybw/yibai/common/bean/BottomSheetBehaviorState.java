package com.ybw.yibai.common.bean;

/**
 * BottomSheetBehavior状态
 * (提供EventBus使用)
 *
 * @author sjl
 */
public class BottomSheetBehaviorState {

    /**
     * BottomSheetBehavior状态
     */
    private int state;

    public BottomSheetBehaviorState(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
