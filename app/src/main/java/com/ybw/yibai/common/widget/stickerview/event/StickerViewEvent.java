package com.ybw.yibai.common.widget.stickerview.event;

import com.ybw.yibai.common.widget.stickerview.StickerView;

/**
 * StickerView TouchEvent 侦听器
 *
 * @author sjl
 */
public interface StickerViewEvent {

    /**
     * 按下手势
     *
     * @param stickerView 事件对象(贴纸View)
     * @param bounds      StickerView范围点的坐标
     */
    void onActionDown(StickerView stickerView, float[] bounds);

    /**
     * 移动手势
     *
     * @param stickerView 事件对象(贴纸View)
     * @param bounds      StickerView范围点的坐标
     */
    void onActionMove(StickerView stickerView, float[] bounds);

    /**
     * 抬起手势
     *
     * @param stickerView 事件对象(贴纸View)
     * @param bounds      StickerView范围点的坐标
     */
    void onActionUp(StickerView stickerView, float[] bounds);
}