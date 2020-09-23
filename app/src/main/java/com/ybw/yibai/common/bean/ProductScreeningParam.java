package com.ybw.yibai.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * 产品筛选参数
 *
 * @author sjl
 */
public class ProductScreeningParam {

    private int code;
    private String msg;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Parcelable{

        /**
         * 分类id
         */
        private int id;

        /**
         * 大分类名称
         */
        private String name;

        /**
         * 大分类别名
         */
        private String cate_code;

        /**
         *
         */
        private List<ParamBean> param;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCate_code() {
            return cate_code;
        }

        public void setCate_code(String cate_code) {
            this.cate_code = cate_code;
        }

        public List<ParamBean> getParam() {
            return param;
        }

        public void setParam(List<ParamBean> param) {
            this.param = param;
        }

        public static class ParamBean implements Parcelable{

            /**
             * 参数id
             */
            private String id;

            /**
             * 参数传参字段
             */
            private String field;

            /**
             * 参数名称
             */
            private String name;

            /**
             * 参数副标题
             */
            private String sub;

            /**
             * 传参类别
             */
            private int type;

            /**
             * 范围格式专用:最小值
             */
            private int min;

            /**
             * 范围格式专用:最大值
             */
            private int max;

            /**
             * 参数格式(1文字2图文3颜色rgb格式4范围)
             */
            private int screen_type;

            /**
             * RangeSeekBar左边的值(非后台返回的数据)用于在用户点击"重置"按钮时使RangeSeekBar左边的值为0
             */
            private float leftValue;

            /**
             * RangeSeekBar右边的值(非后台返回的数据)用于在用户点击"重置"按钮时使RangeSeekBar右边的值为0
             */
            private float rightValue;

            private List<SonBean> son;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getField() {
                return field;
            }

            public void setField(String field) {
                this.field = field;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getSub() {
                return sub;
            }

            public void setSub(String sub) {
                this.sub = sub;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public int getMin() {
                return min;
            }

            public void setMin(int min) {
                this.min = min;
            }

            public int getMax() {
                return max;
            }

            public void setMax(int max) {
                this.max = max;
            }

            public int getScreen_type() {
                return screen_type;
            }

            public void setScreen_type(int screen_type) {
                this.screen_type = screen_type;
            }

            public float getLeftValue() {
                return leftValue;
            }

            public void setLeftValue(float leftValue) {
                this.leftValue = leftValue;
            }

            public float getRightValue() {
                return rightValue;
            }

            public void setRightValue(float rightValue) {
                this.rightValue = rightValue;
            }

            public List<SonBean> getSon() {
                return son;
            }

            public void setSon(List<SonBean> son) {
                this.son = son;
            }

            public static class SonBean implements Parcelable{

                /**
                 * 参数值id
                 */
                private String id;

                /**
                 * 参数值名称
                 */
                private String name;

                /**
                 * 颜色值:仅格式为3才有
                 */
                private String pic;

                /**
                 * 图片地址:仅格式为2才有
                 */
                private String color;

                /**
                 * 用户是否选中了该数据(非后台返回的数据)
                 */
                private boolean isSelected;

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

                public String getPic() {
                    return pic;
                }

                public void setPic(String pic) {
                    this.pic = pic;
                }

                public String getColor() {
                    return color;
                }

                public void setColor(String color) {
                    this.color = color;
                }

                public boolean isSelected() {
                    return isSelected;
                }

                public void setSelected(boolean selected) {
                    isSelected = selected;
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeString(this.id);
                    dest.writeString(this.name);
                    dest.writeString(this.pic);
                    dest.writeString(this.color);
                    dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
                }

                public SonBean() {
                }

                protected SonBean(Parcel in) {
                    this.id = in.readString();
                    this.name = in.readString();
                    this.pic = in.readString();
                    this.color = in.readString();
                    this.isSelected = in.readByte() != 0;
                }

                public static final Creator<SonBean> CREATOR = new Creator<SonBean>() {
                    @Override
                    public SonBean createFromParcel(Parcel source) {
                        return new SonBean(source);
                    }

                    @Override
                    public SonBean[] newArray(int size) {
                        return new SonBean[size];
                    }
                };
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.id);
                dest.writeString(this.field);
                dest.writeString(this.name);
                dest.writeString(this.sub);
                dest.writeInt(this.type);
                dest.writeInt(this.min);
                dest.writeInt(this.max);
                dest.writeInt(this.screen_type);
                dest.writeList(this.son);
            }

            public ParamBean() {
            }

            protected ParamBean(Parcel in) {
                this.id = in.readString();
                this.field = in.readString();
                this.name = in.readString();
                this.sub = in.readString();
                this.type = in.readInt();
                this.min = in.readInt();
                this.max = in.readInt();
                this.screen_type = in.readInt();
                this.son = new ArrayList<>();
                in.readList(this.son, SonBean.class.getClassLoader());
            }

            public static final Creator<ParamBean> CREATOR = new Creator<ParamBean>() {
                @Override
                public ParamBean createFromParcel(Parcel source) {
                    return new ParamBean(source);
                }

                @Override
                public ParamBean[] newArray(int size) {
                    return new ParamBean[size];
                }
            };
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeString(this.name);
            dest.writeString(this.cate_code);
            dest.writeList(this.param);
        }

        public DataBean() {
        }

        protected DataBean(Parcel in) {
            this.id = in.readInt();
            this.name = in.readString();
            this.cate_code = in.readString();
            this.param = new ArrayList<>();
            in.readList(this.param, ParamBean.class.getClassLoader());
        }

        public static final Creator<DataBean> CREATOR = new Creator<DataBean>() {
            @Override
            public DataBean createFromParcel(Parcel source) {
                return new DataBean(source);
            }

            @Override
            public DataBean[] newArray(int size) {
                return new DataBean[size];
            }
        };
    }
}
