package com.ybw.yibai.common.bean;

import java.util.List;

/**
 * 系统参数
 *
 * @author sjl
 */
public class SystemParameter {

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
         * 场景列表
         */
        private List<String> scene;

        /**
         * 推荐花盆分类
         */
        private List<PotBean> pot;

        /**
         *
         */
        private List<PlantBean> plant;

        /**
         * 分类列表
         */
        private List<CatelistBean> catelist;

        /**
         * 产品使用状态列表
         */
        private List<UsestateBean> usestate;

        /**
         * 植物大中小类别
         */
        private List<SpectypeBean> spectype;

        public List<String> getScene() {
            return scene;
        }

        public void setScene(List<String> scene) {
            this.scene = scene;
        }

        public List<PotBean> getPot() {
            return pot;
        }

        public void setPot(List<PotBean> pot) {
            this.pot = pot;
        }

        public List<PlantBean> getPlant() {
            return plant;
        }

        public void setPlant(List<PlantBean> plant) {
            this.plant = plant;
        }

        public List<CatelistBean> getCatelist() {
            return catelist;
        }

        public void setCatelist(List<CatelistBean> catelist) {
            this.catelist = catelist;
        }

        public List<UsestateBean> getUsestate() {
            return usestate;
        }

        public void setUsestate(List<UsestateBean> usestate) {
            this.usestate = usestate;
        }

        public List<SpectypeBean> getSpectype() {
            return spectype;
        }

        public void setSpectype(List<SpectypeBean> spectype) {
            this.spectype = spectype;
        }

        public static class PotBean {

            private int id;
            private String name;
            private String pic;

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
        }

        public static class PlantBean {

            private int id;
            private String name;
            private String pic;

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
        }

        public static class CatelistBean {

            private String cate_code;

            private List<ListBean> list;

            public String getCate_code() {
                return cate_code;
            }

            public void setCate_code(String cate_code) {
                this.cate_code = cate_code;
            }

            public List<ListBean> getList() {
                return list;
            }

            public void setList(List<ListBean> list) {
                this.list = list;
            }

            public static class ListBean {

                private int id;
                private String name;
                private String pic;

                /**
                 * 是否选中该二级分类
                 * {非后台给的字段,Android 程序员自定义字段}
                 */
                private boolean select;

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

                public boolean isSelect() {
                    return select;
                }

                public void setSelect(boolean select) {
                    this.select = select;
                }
            }
        }

        public static class UsestateBean {

            private int id;
            private String name;

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
        }

        public static class SpectypeBean {

            private int id;
            private String name;

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
        }
    }
}
