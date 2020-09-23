package com.ybw.yibai.common.bean;

import java.util.Map;

/**
 * 用户选中的产品筛选参数
 *
 * @author sjl
 */
public class SelectProductParam {

    /**
     * 当前筛选Fragment页面在ViewPager中的位置
     */
    private int position;

    /**
     * 用户选中的产品筛选参数
     * K:field
     * V:个属性ID拼接的String
     */
    private Map<String, String> selectProductParamMap;

    public SelectProductParam(int position, Map<String, String> selectProductParamMap) {
        this.position = position;
        this.selectProductParamMap = selectProductParamMap;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Map<String, String> getSelectProductParamMap() {
        return selectProductParamMap;
    }

    public void setSelectProductParamMap(Map<String, String> selectProductParamMap) {
        this.selectProductParamMap = selectProductParamMap;
    }
}
