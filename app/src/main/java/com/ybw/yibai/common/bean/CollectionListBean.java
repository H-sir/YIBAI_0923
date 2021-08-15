package com.ybw.yibai.common.bean;

import java.util.List;

/**
 * 收藏列表
 *
 * @author sjl
 */
public class CollectionListBean {
    /**
     * code : 200
     * msg : ok
     * data : {"page":1,"pagesize":10,"count":2,"list":[{"collect_id":"47","plant_sku_id":"8473","plant_name":"天堂鸟 ","pot_sku_id":"7282","pot_name":"大肚圆柱盆 ","pic":"https://images.100ybw.com/collect/2021/08/15/20210815044830636118d4de724cc_L.png"},{"collect_id":"48","plant_sku_id":"6657","plant_name":"袖珍椰子 ","pot_sku_id":"7564","pot_name":"XA1111 灰","pic":"https://images.100ybw.com/collect/2021/08/15/20210815044910966118d50657510_L.png"}]}
     */

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
         * page : 1
         * pagesize : 10
         * count : 2
         * list : [{"collect_id":"47","plant_sku_id":"8473","plant_name":"天堂鸟 ","pot_sku_id":"7282","pot_name":"大肚圆柱盆 ","pic":"https://images.100ybw.com/collect/2021/08/15/20210815044830636118d4de724cc_L.png"},{"collect_id":"48","plant_sku_id":"6657","plant_name":"袖珍椰子 ","pot_sku_id":"7564","pot_name":"XA1111 灰","pic":"https://images.100ybw.com/collect/2021/08/15/20210815044910966118d50657510_L.png"}]
         */

        private int page;
        private int pagesize;
        private int count;
        private List<ListBean> list;

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getPagesize() {
            return pagesize;
        }

        public void setPagesize(int pagesize) {
            this.pagesize = pagesize;
        }

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
             * collect_id : 47
             * plant_sku_id : 8473
             * plant_name : 天堂鸟
             * pot_sku_id : 7282
             * pot_name : 大肚圆柱盆
             * pic : https://images.100ybw.com/collect/2021/08/15/20210815044830636118d4de724cc_L.png
             */

            private String collect_id;
            private String sku_id;
            private String name;
            private String plant_sku_id;
            private String plant_name;
            private String pot_sku_id;
            private String pot_name;
            private String pic;
            private boolean isEdit = false;
            private boolean isSelect = false;
            private boolean isCombination = false;

            public boolean isCombination() {
                return isCombination;
            }

            public void setCombination(boolean combination) {
                isCombination = combination;
            }

            public boolean isEdit() {
                return isEdit;
            }

            public void setEdit(boolean edit) {
                isEdit = edit;
            }

            public boolean isSelect() {
                return isSelect;
            }

            public void setSelect(boolean select) {
                isSelect = select;
            }

            public String getSku_id() {
                return sku_id;
            }

            public void setSku_id(String sku_id) {
                this.sku_id = sku_id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getCollect_id() {
                return collect_id;
            }

            public void setCollect_id(String collect_id) {
                this.collect_id = collect_id;
            }

            public String getPlant_sku_id() {
                return plant_sku_id;
            }

            public void setPlant_sku_id(String plant_sku_id) {
                this.plant_sku_id = plant_sku_id;
            }

            public String getPlant_name() {
                return plant_name;
            }

            public void setPlant_name(String plant_name) {
                this.plant_name = plant_name;
            }

            public String getPot_sku_id() {
                return pot_sku_id;
            }

            public void setPot_sku_id(String pot_sku_id) {
                this.pot_sku_id = pot_sku_id;
            }

            public String getPot_name() {
                return pot_name;
            }

            public void setPot_name(String pot_name) {
                this.pot_name = pot_name;
            }

            public String getPic() {
                return pic;
            }

            public void setPic(String pic) {
                this.pic = pic;
            }
        }
    }
}
