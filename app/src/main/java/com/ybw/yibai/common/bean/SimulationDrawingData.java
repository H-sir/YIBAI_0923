package com.ybw.yibai.common.bean;

import com.ybw.yibai.module.drawing.SimulationDrawingActivity;

import java.io.File;

/**
 * "已模拟图"数据
 * {@link SimulationDrawingActivity}
 *
 * @author sjl
 */
public class SimulationDrawingData {

    /**
     * 用户保存的"模拟图"存储在手机中的位置
     */
    private File file;

    /**
     * 是否显示CheckBox
     */
    private boolean showCheckBox;

    /**
     * 是否选中CheckBox
     */
    private boolean selectCheckBox;

    /**
     * 状态:
     * 0 表示为"全选"
     * 1 表示为"全不选"
     */
    private int state;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public boolean isShowCheckBox() {
        return showCheckBox;
    }

    public void setShowCheckBox(boolean showCheckBox) {
        this.showCheckBox = showCheckBox;
    }

    public boolean isSelectCheckBox() {
        return selectCheckBox;
    }

    public void setSelectCheckBox(boolean selectCheckBox) {
        this.selectCheckBox = selectCheckBox;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
