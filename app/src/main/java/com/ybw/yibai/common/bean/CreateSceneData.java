package com.ybw.yibai.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;

/**
 * 创建场景时需要的数据
 *
 * @author sjl
 */
public class CreateSceneData implements Parcelable {

    /**
     * 场景类型名称
     */
    private String name;

    /**
     * 场景背景图片
     */
    private File file;

    public CreateSceneData(File file) {
        this.file = file;
    }

    public CreateSceneData(String name, File file) {
        this.name = name;
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeSerializable(this.file);
    }

    protected CreateSceneData(Parcel in) {
        this.name = in.readString();
        this.file = (File) in.readSerializable();
    }

    public static final Creator<CreateSceneData> CREATOR = new Creator<CreateSceneData>() {
        @Override
        public CreateSceneData createFromParcel(Parcel source) {
            return new CreateSceneData(source);
        }

        @Override
        public CreateSceneData[] newArray(int size) {
            return new CreateSceneData[size];
        }
    };
}
