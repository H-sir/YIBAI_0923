package com.ybw.yibai.common.bean;

import java.util.List;

/**
 * 公司案例
 *
 * @author sjl
 */
public class Case {

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

        public static class ListBean {

            /**
             *
             */
            private int id;

            /**
             * 案例分类id
             */
            private int cateid;

            /**
             * 案例分类名称
             */
            private String name;

            /**
             * 案例分类图片
             */
            private String pic;

            /**
             * 案例分类图片
             */
            private String pic_M;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getCateid() {
                return cateid;
            }

            public void setCateid(int cateid) {
                this.cateid = cateid;
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

            public String getPic_M() {
                return pic_M;
            }

            public void setPic_M(String pic_M) {
                this.pic_M = pic_M;
            }
        }
    }
}
