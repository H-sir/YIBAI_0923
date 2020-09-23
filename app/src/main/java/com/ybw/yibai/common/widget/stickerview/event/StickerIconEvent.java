package com.ybw.yibai.common.widget.stickerview.event;

import android.view.MotionEvent;

import com.ybw.yibai.common.widget.stickerview.StickerView;

/**
 * 定义"贴纸"四个方向的Icon图标的事件
 *
 * @author sjl
 */
public interface StickerIconEvent {

    /**
     * 按下手势
     *
     * @param stickerView 事件对象(贴纸View)
     * @param event       触屏事件
     */
    void onActionDown(StickerView stickerView, MotionEvent event);

    /**
     * 移动手势
     *
     * @param stickerView 事件对象(贴纸View)
     * @param event       触屏事件
     */
    void onActionMove(StickerView stickerView, MotionEvent event);

    /**
     * 抬起手势
     *
     * @param stickerView 事件对象(贴纸View)
     * @param event       触屏事件
     */
    void onActionUp(StickerView stickerView, MotionEvent event);
}
