package com.ybw.yibai.common.bean;

/**
 * "贴纸View"是否被选中
 *
 * @author sjl
 */
public class StickerViewSelected {

    private boolean selected;

    public StickerViewSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
