package com.ybw.yibai.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * 产品详情
 *
 * @author sjl
 */
public class ProductDetails {

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

        /**
         * 产品id
         */
        private int id;

        /**
         * 产品名称
         */
        private String name;

        /**
         * 产品主图(仅用于未确定sku时显示的图)
         */
        private String pic;

        /**
         * 产品类别(plant=植物,pot=花盆)
         */
        private String cate_code;

        /**
         * 产品描述需base64解码
         */
        private String content;

        /**
         * 产品规格数组
         */
        private List<SpecBean> spec;

        /**
         * 产品属性数组
         */
        private List<AttrBean> attr;

        /**
         * sku列表数组
         */
        private List<SkuListBean> skulist;

        private SourceBean source;

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

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getCate_code() {
            return cate_code;
        }

        public void setCate_code(String cate_code) {
            this.cate_code = cate_code;
        }

        public List<SkuListBean> getSkulist() {
            return skulist;
        }

        public void setSkulist(List<SkuListBean> skulist) {
            this.skulist = skulist;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public List<SpecBean> getSpec() {
            return spec;
        }

        public void setSpec(List<SpecBean> spec) {
            this.spec = spec;
        }

        public List<AttrBean> getAttr() {
            return attr;
        }

        public void setAttr(List<AttrBean> attr) {
            this.attr = attr;
        }

        public SourceBean getSource() {
            return source;
        }

        public void setSource(SourceBean source) {
            this.source = source;
        }

        public List<SkuListBean> getSkuList() {
            return skulist;
        }

        public void setSkuList(List<SkuListBean> skuList) {
            this.skulist = skuList;
        }

        public static class SourceBean {
            private String delivery_day;
            private String delivery;

            public String getDelivery_day() {
                return delivery_day;
            }

            public void setDelivery_day(String delivery_day) {
                this.delivery_day = delivery_day;
            }

            public String getDelivery() {
                return delivery;
            }

            public void setDelivery(String delivery) {
                this.delivery = delivery;
            }
        }


        public static class SpecBean {

            /**
             * 规格id
             */
            private int id;

            /**
             * 规格名
             */
            private String name;

            /**
             * 规格则数组
             */
            private List<SonBean> son;

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

            public List<SonBean> getSon() {
                return son;
            }

            public void setSon(List<SonBean> son) {
                this.son = son;
            }

            public static class SonBean {

                /**
                 * 规格值id
                 */
                private int id;

                /**
                 * 规格值名称
                 */
                private String name;

                /**
                 * 非后台给的数据,android开发人员自定义的字段,用于标识该规格是否被选中
                 */
                private boolean selected;

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

                public boolean isSelected() {
                    return selected;
                }

                public void setSelected(boolean selected) {
                    this.selected = selected;
                }
            }
        }

        public static class AttrBean {

            /**
             * 属性名称
             */
            private String name;

            /**
             * 属性值
             */
            private String value;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }
        }

        public static class SkuListBean implements Parcelable {

            /**
             * sku id
             */
            private int sku_id;

            /**
             * sku名称
             */
            private String name;

            /**
             * sku主图(确定sku后用于替换显示产品图片)
             */
            private String pic1;

            /**
             * sku模拟搭配图(没有此图,此sku将不能模拟)
             */
            private String pic2;

            /**
             * sku自由搭配图(没有此图,此sku将不能搭配)
             */
            private String pic3;

            /**
             * 产品规格简介
             */
            private String parstring;

            /**
             * 零售价
             */
            private double price;

            /**
             * 月租
             */
            private double month_rent;

            /**
             * 批发价
             */
            private double trade_price;

            /**
             * 搭配高度
             */
            private double height;

            /**
             * 搭配口径
             */
            private double diameter;

            /**
             * 使用状态 1使用0不使用
             */
            private int usestate;

            /**
             * 组合模式1组合2单产品
             */
            private int comtype;

            /**
             * 售价代码
             */
            private String price_code;

            /**
             * 批发价代码
             */
            private String trade_price_code;

            /**
             * 规格值数组
             */
            private List<Integer> spec_id;

            public int getSku_id() {
                return sku_id;
            }

            public void setSku_id(int sku_id) {
                this.sku_id = sku_id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPic1() {
                return pic1;
            }

            public void setPic1(String pic1) {
                this.pic1 = pic1;
            }

            public String getPic2() {
                return pic2;
            }

            public void setPic2(String pic2) {
                this.pic2 = pic2;
            }

            public String getPic3() {
                return pic3;
            }

            public void setPic3(String pic3) {
                this.pic3 = pic3;
            }

            public String getParstring() {
                return parstring;
            }

            public void setParstring(String parstring) {
                this.parstring = parstring;
            }

            public double getPrice() {
                return price;
            }

            public void setPrice(double price) {
                this.price = price;
            }

            public double getMonth_rent() {
                return month_rent;
            }

            public void setMonth_rent(double month_rent) {
                this.month_rent = month_rent;
            }

            public double getTrade_price() {
                return trade_price;
            }

            public void setTrade_price(double trade_price) {
                this.trade_price = trade_price;
            }

            public double getHeight() {
                return height;
            }

            public void setHeight(double height) {
                this.height = height;
            }

            public double getDiameter() {
                return diameter;
            }

            public void setDiameter(double diameter) {
                this.diameter = diameter;
            }

            public int getUsestate() {
                return usestate;
            }

            public void setUsestate(int usestate) {
                this.usestate = usestate;
            }

            public int getComtype() {
                return comtype;
            }

            public void setComtype(int comtype) {
                this.comtype = comtype;
            }

            public String getPrice_code() {
                return price_code;
            }

            public void setPrice_code(String price_code) {
                this.price_code = price_code;
            }

            public String getTrade_price_code() {
                return trade_price_code;
            }

            public void setTrade_price_code(String trade_price_code) {
                this.trade_price_code = trade_price_code;
            }

            public List<Integer> getSpec_id() {
                return spec_id;
            }

            public void setSpec_id(List<Integer> spec_id) {
                this.spec_id = spec_id;
            }


            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(this.sku_id);
                dest.writeString(this.name);
                dest.writeString(this.pic1);
                dest.writeString(this.pic2);
                dest.writeString(this.pic3);
                dest.writeString(this.parstring);
                dest.writeDouble(this.price);
                dest.writeDouble(this.month_rent);
                dest.writeDouble(this.trade_price);
                dest.writeDouble(this.height);
                dest.writeDouble(this.diameter);
                dest.writeInt(this.usestate);
                dest.writeInt(this.comtype);
                dest.writeString(this.price_code);
                dest.writeString(this.trade_price_code);
                dest.writeList(this.spec_id);
            }

            public SkuListBean() {
            }

            protected SkuListBean(Parcel in) {
                this.sku_id = in.readInt();
                this.name = in.readString();
                this.pic1 = in.readString();
                this.pic2 = in.readString();
                this.pic3 = in.readString();
                this.parstring = in.readString();
                this.price = in.readDouble();
                this.month_rent = in.readDouble();
                this.trade_price = in.readDouble();
                this.height = in.readDouble();
                this.diameter = in.readDouble();
                this.usestate = in.readInt();
                this.comtype = in.readInt();
                this.price_code = in.readString();
                this.trade_price_code = in.readString();
                this.spec_id = new ArrayList<Integer>();
                in.readList(this.spec_id, Integer.class.getClassLoader());
            }

            public static final Creator<SkuListBean> CREATOR = new Creator<SkuListBean>() {
                @Override
                public SkuListBean createFromParcel(Parcel source) {
                    return new SkuListBean(source);
                }

                @Override
                public SkuListBean[] newArray(int size) {
                    return new SkuListBean[size];
                }
            };
        }
    }
}
