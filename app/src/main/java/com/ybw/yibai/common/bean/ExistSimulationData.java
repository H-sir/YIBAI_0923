package com.ybw.yibai.common.bean;

/**
 * 在某一个场景中是否存在"模拟搭配图片"数据
 *
 * @author sjl
 */
public class ExistSimulationData {

    private boolean exist;

    public ExistSimulationData(boolean exist) {
        this.exist = exist;
    }

    public boolean isExist() {
        return exist;
    }

    public void setExist(boolean exist) {
        this.exist = exist;
    }
}
