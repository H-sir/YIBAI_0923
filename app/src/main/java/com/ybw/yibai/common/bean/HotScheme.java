package com.ybw.yibai.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 热门方案列表
 *
 * @author sjl
 */
public class HotScheme {

    private int code;
    private String msg;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {

        private int count;
        private List<ListBean> list;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean implements Parcelable {

            /**
             *
             */
            private String id;

            /**
             * 名称
             */
            private String name;

            /**
             * 背景图
             */
            private String bg_pic;

            /**
             * 封面图
             */
            private String cover_pic;

            /**
             * 合成图左上角定点(背景图比例)格式(宽:高)
             */
            private String points;

            /**
             * 合成图区域占用背景图高度比例
             */
            private float height;

            /**
             * 可用植物列表
             */
            private List<com.ybw.yibai.common.bean.ListBean> plant;

            /**
             * 可用盆器列表
             */
            private List<com.ybw.yibai.common.bean.ListBean> pot;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getBg_pic() {
                return bg_pic;
            }

            public void setBg_pic(String bg_pic) {
                this.bg_pic = bg_pic;
            }

            public String getCover_pic() {
                return cover_pic;
            }

            public void setCover_pic(String cover_pic) {
                this.cover_pic = cover_pic;
            }

            public String getPoints() {
                return points;
            }

            public void setPoints(String points) {
                this.points = points;
            }

            public float getHeight() {
                return height;
            }

            public void setHeight(float height) {
                this.height = height;
            }

            public List<com.ybw.yibai.common.bean.ListBean> getPlant() {
                return plant;
            }

            public void setPlant(List<com.ybw.yibai.common.bean.ListBean> plant) {
                this.plant = plant;
            }

            public List<com.ybw.yibai.common.bean.ListBean> getPot() {
                return pot;
            }

            public void setPot(List<com.ybw.yibai.common.bean.ListBean> pot) {
                this.pot = pot;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.id);
                dest.writeString(this.name);
                dest.writeString(this.bg_pic);
                dest.writeString(this.cover_pic);
                dest.writeString(this.points);
                dest.writeFloat(this.height);
                dest.writeTypedList(this.plant);
                dest.writeTypedList(this.pot);
            }

            public ListBean() {
            }

            protected ListBean(Parcel in) {
                this.id = in.readString();
                this.name = in.readString();
                this.bg_pic = in.readString();
                this.cover_pic = in.readString();
                this.points = in.readString();
                this.height = in.readFloat();
                this.plant = in.createTypedArrayList(com.ybw.yibai.common.bean.ListBean.CREATOR);
                this.pot = in.createTypedArrayList(com.ybw.yibai.common.bean.ListBean.CREATOR);
            }

            public static final Creator<ListBean> CREATOR = new Creator<ListBean>() {
                @Override
                public ListBean createFromParcel(Parcel source) {
                    return new ListBean(source);
                }

                @Override
                public ListBean[] newArray(int size) {
                    return new ListBean[size];
                }
            };
        }
    }
}
