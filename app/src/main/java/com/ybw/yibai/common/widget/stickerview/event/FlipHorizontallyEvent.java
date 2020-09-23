package com.ybw.yibai.common.widget.stickerview.event;

import com.ybw.yibai.common.widget.stickerview.StickerView;

/**
 * "贴纸"四个方向的Icon图标中的"翻转Icon"的事件
 *
 * @author sjl
 */
public class FlipHorizontallyEvent extends AbstractFlipEvent {

    @Override
    @StickerView.Flip
    protected int getFlipDirection() {
        return StickerView.FLIP_HORIZONTALLY;
    }
}