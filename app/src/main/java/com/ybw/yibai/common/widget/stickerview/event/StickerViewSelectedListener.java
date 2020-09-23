package com.ybw.yibai.common.widget.stickerview.event;

import android.support.annotation.NonNull;

import com.ybw.yibai.common.widget.stickerview.BaseSticker;

/**
 * "贴纸View"是否被选中状态侦听器
 *
 * @author sjl
 */
public interface StickerViewSelectedListener {

    /**
     * 在有一个"贴纸View"被选中时回调
     *
     * @param sticker 被选中的"贴纸View"
     */
    void onSelectedStickerView(@NonNull BaseSticker sticker);

    /**
     * 在没有"贴纸View"被选中时回调
     */
    void onUnselectedStickerView();
}
