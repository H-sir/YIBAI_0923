package com.ybw.yibai.common.bean;

import com.ybw.yibai.module.pictureview.PictureViewActivity;

import java.io.File;

/**
 * {@link PictureViewActivity} 选择以模拟图片数据
 *
 * @author sjl
 */
public class SelectImage {

    private File file;

    public SelectImage() {

    }

    public SelectImage(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
