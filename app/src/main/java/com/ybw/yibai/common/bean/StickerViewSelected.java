package com.ybw.yibai.common.bean;

import com.ybw.yibai.common.widget.stickerview.BaseSticker;

/**
 * "贴纸View"是否被选中
 *
 * @author sjl
 */
public class StickerViewSelected {

    private BaseSticker baseSticker = null;
    private boolean selected;

    public StickerViewSelected(boolean selected) {
        this.selected = selected;
    }

    public BaseSticker getBaseSticker() {
        return baseSticker;
    }

    public void setBaseSticker(BaseSticker baseSticker) {
        this.baseSticker = baseSticker;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
