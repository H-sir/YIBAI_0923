package com.ybw.yibai.common.widget.stickerview.event;

import android.view.MotionEvent;

import com.ybw.yibai.common.widget.stickerview.StickerView;

/**
 * 抽象的翻转事件
 *
 * @author sjl
 */
public abstract class AbstractFlipEvent implements StickerIconEvent {

    /**
     * 按下手势
     *
     * @param stickerView 事件对象(贴纸View)
     * @param event       触屏事件
     */
    @Override
    public void onActionDown(StickerView stickerView, MotionEvent event) {

    }

    /**
     * 移动手势
     *
     * @param stickerView 事件对象(贴纸View)
     * @param event       触屏事件
     */
    @Override
    public void onActionMove(StickerView stickerView, MotionEvent event) {

    }

    /**
     * 抬起手势
     *
     * @param stickerView 事件对象(贴纸View)
     * @param event       触屏事件
     */
    @Override
    public void onActionUp(StickerView stickerView, MotionEvent event) {
        stickerView.flipCurrentSticker(getFlipDirection());
    }

    /**
     * 获取翻转方向
     *
     * @return 翻转方向
     */
    @StickerView.Flip
    protected abstract int getFlipDirection();
}
