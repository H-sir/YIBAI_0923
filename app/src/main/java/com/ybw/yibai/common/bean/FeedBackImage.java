package com.ybw.yibai.common.bean;

import java.io.File;

/**
 * 意见反馈
 *
 * @author sjl
 */
public class FeedBackImage {

    /**
     * 意见反馈图片
     */
    private File file;

    private int type;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}