package com.ybw.yibai.common.widget.stickerview.event;

import android.support.annotation.NonNull;

import com.ybw.yibai.common.widget.stickerview.BaseSticker;

/**
 * "贴纸View"操作监听器
 *
 * @author sjl
 */
public interface OnStickerOperationListener {

    /**
     * 在添加贴纸时回调
     *
     * @param sticker 事件对象
     */
    void onStickerAdded(@NonNull BaseSticker sticker);

    /**
     * 在删除贴纸时回调
     *
     * @param sticker 事件对象
     */
    void onStickerDeleted(@NonNull BaseSticker sticker);

    /**
     * 在翻转贴纸时回调
     *
     * @param sticker 事件对象
     */
    void onStickerFlipped(@NonNull BaseSticker sticker);

    /**
     * 在单击贴纸时回调
     *
     * @param sticker 事件对象
     */
    void onStickerClicked(@NonNull BaseSticker sticker);

    /**
     * 在贴纸拖动完成时回调
     *
     * @param sticker 事件对象
     */
    void onStickerDragFinished(@NonNull BaseSticker sticker);

    /**
     * 在贴纸缩放完成时回调
     *
     * @param sticker 事件对象
     */
    void onStickerZoomFinished(@NonNull BaseSticker sticker);

    /**
     * @param sticker 事件对象
     */
    void onStickerTouchedDown(@NonNull BaseSticker sticker);

    /**
     * @param sticker 事件对象
     */
    void onStickerDoubleTapped(@NonNull BaseSticker sticker);
}
