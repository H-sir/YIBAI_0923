package com.ybw.yibai.common.bean;

import java.util.List;

/**
 * 模拟场景植物花盆推荐列表
 *
 * @author sjl
 */
public class RecommendProduct {

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

        private PlantBean plant;

        private PotBean pot;

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

        public static class PlantBean {

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
        }

        public static class PotBean {

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
        }
    }
}
