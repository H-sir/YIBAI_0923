package com.ybw.yibai.common.widget.magicindicator.abs;

/**
 * 抽象的ViewPager导航器
 */
public interface IPagerNavigator {

    void onPageSelected(int position);

    void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

    void onPageScrollStateChanged(int state);

    /**
     * 当IPagerNavigator被添加到MagicIndicator时调用
     */
    void onAttachToMagicIndicator();

    /**
     * 当IPagerNavigator从MagicIndicator上移除时调用
     */
    void onDetachFromMagicIndicator();

    /**
     * ViewPager内容改变时需要先调用此方法,自定义的IPagerNavigator应当遵守此约定
     */
    void notifyDataSetChanged();
}
