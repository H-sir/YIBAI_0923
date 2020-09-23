package com.ybw.yibai.common.bean;

import java.util.List;

/**
 * 城市列表
 */
public class CityListBean {

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
        private List<ListBean> hotcity;

        public List<ListBean> getList() {
            return hotcity;
        }

        public void setList(List<ListBean> hotcity) {
            this.hotcity = hotcity;
        }

        public static class ListBean {
            private String RegionName;//城市名称
            private String Code;//城市Code

            public String getRegionName() {
                return RegionName;
            }

            public void setRegionName(String regionName) {
                RegionName = regionName;
            }

            public String getCode() {
                return Code;
            }

            public void setCode(String code) {
                Code = code;
            }
        }
    }
}
