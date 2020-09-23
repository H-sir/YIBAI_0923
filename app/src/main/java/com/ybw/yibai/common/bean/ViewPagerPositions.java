package com.ybw.yibai.common.bean;

import com.ybw.yibai.module.filter.FilterActivity;

/**
 * {@link FilterActivity}界面ViewPager当前滑动的位置
 *
 * @author sjl
 */
public class ViewPagerPositions {

    private int position;

    public ViewPagerPositions(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

}
