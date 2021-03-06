package com.ybw.yibai.common.widget.magicindicator.buildins.commonnavigator.abs;

import com.ybw.yibai.common.widget.magicindicator.buildins.commonnavigator.model.PositionData;

import java.util.List;

/**
 * 抽象的viewpager指示器,适用于CommonNavigator
 */
public interface IPagerIndicator {

    void onPageSelected(int position);

    void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

    void onPageScrollStateChanged(int state);

    void onPositionDataProvide(List<PositionData> dataList);
}
