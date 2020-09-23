package com.ybw.yibai.common.widget.magicindicator.buildins.commonnavigator.titles.badge;

/**
 * 角标的定位规则
 */
public class BadgeRule {

    private int mOffset;
    private BadgeAnchor mAnchor;

    public BadgeRule(BadgeAnchor anchor, int offset) {
        mAnchor = anchor;
        mOffset = offset;
    }

    public BadgeAnchor getAnchor() {
        return mAnchor;
    }

    public void setAnchor(BadgeAnchor anchor) {
        mAnchor = anchor;
    }

    public int getOffset() {
        return mOffset;
    }

    public void setOffset(int offset) {
        mOffset = offset;
    }
}
