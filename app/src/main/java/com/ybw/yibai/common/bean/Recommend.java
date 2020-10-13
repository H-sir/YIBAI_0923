package com.ybw.yibai.common.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 换搭配获取植物花盆列表
 *
 * @author sjl
 */
public class Recommend {

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

        @SerializedName("count") private int count;
        @SerializedName("cate_code") private String cateCode;
        @SerializedName("sql") private String sql;
        @SerializedName("list") private List<ListBean> list;
        private PlantBean plant;
        private PotBean pot;
        private int specTypeId;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getCateCode() {
            return cateCode;
        }

        public void setCateCode(String cateCode) {
            this.cateCode = cateCode;
        }

        public String getSql() {
            return sql;
        }

        public void setSql(String sql) {
            this.sql = sql;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public int getSpecTypeId() {
            return specTypeId;
        }

        public void setSpecTypeId(int specTypeId) {
            this.specTypeId = specTypeId;
        }



        public PlantBean getPlant() {
            return plant;
        }

        public void setPlant(PlantBean plant) {
            this.plant = plant;
        }

        public PotBean getPot() {
            return pot;
        }

        public void setPot(PotBean pot) {
            this.pot = pot;
        }

        public static class PotBean {
            private int count;
            private String sql;
            private List<ListBean> list;

            public int getCount() {
                return count;
            }

            public void setCount(int count) {
                this.count = count;
            }

            public String getSql() {
                return sql;
            }

            public void setSql(String sql) {
                this.sql = sql;
            }

            public List<ListBean> getList() {
                return list;
            }

            public void setList(List<ListBean> list) {
                this.list = list;
            }
        }

        public static class PlantBean {
            private int count;
            private String sql;
            private List<ListBean> list;

            public int getCount() {
                return count;
            }

            public void setCount(int count) {
                this.count = count;
            }

            public String getSql() {
                return sql;
            }

            public void setSql(String sql) {
                this.sql = sql;
            }

            public List<ListBean> getList() {
                return list;
            }

            public void setList(List<ListBean> list) {
                this.list = list;
            }
        }
    }
}
