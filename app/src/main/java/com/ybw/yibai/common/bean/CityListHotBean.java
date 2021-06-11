package com.ybw.yibai.common.bean;

import java.util.List;

/**
 * Created by HKR on 2021/6/11.
 */

public class CityListHotBean {


    /**
     * code : 200
     * msg : ok
     * data : {"hotcity":[{"RegionName":"北京市","Code":"110000"},{"RegionName":"天津市","Code":"120000"},{"RegionName":"石家庄市","Code":"130100"},{"RegionName":"太原市","Code":"140100"},{"RegionName":"呼和浩特市","Code":"150100"},{"RegionName":"沈阳市","Code":"210100"},{"RegionName":"长春市","Code":"220100"},{"RegionName":"哈尔滨市","Code":"230100"},{"RegionName":"上海市","Code":"310000"},{"RegionName":"南京市","Code":"320100"},{"RegionName":"杭州市","Code":"330100"},{"RegionName":"合肥市","Code":"340100"},{"RegionName":"福州市","Code":"350100"},{"RegionName":"南昌市","Code":"360100"},{"RegionName":"济南市","Code":"370100"},{"RegionName":"郑州市","Code":"410100"},{"RegionName":"武汉市","Code":"420100"},{"RegionName":"长沙市","Code":"430100"},{"RegionName":"广州市","Code":"440100"},{"RegionName":"南宁市","Code":"450100"},{"RegionName":"海口市","Code":"460100"},{"RegionName":"重庆市","Code":"500000"},{"RegionName":"成都市","Code":"510100"},{"RegionName":"贵阳市","Code":"520100"},{"RegionName":"昆明市","Code":"530100"},{"RegionName":"拉萨市","Code":"540100"},{"RegionName":"西安市","Code":"610100"},{"RegionName":"兰州市","Code":"620100"},{"RegionName":"西宁市","Code":"630100"},{"RegionName":"银川市","Code":"640100"},{"RegionName":"乌鲁木齐市","Code":"650100"},{"RegionName":"香港特别行政区","Code":"810000"},{"RegionName":"澳门特别行政区","Code":"820000"},{"RegionName":"台北市","Code":"710100"}]}
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
        private List<HotcityBean> hotcity;

        public List<HotcityBean> getHotcity() {
            return hotcity;
        }

        public void setHotcity(List<HotcityBean> hotcity) {
            this.hotcity = hotcity;
        }

        public static class HotcityBean {
            /**
             * RegionName : 北京市
             * Code : 110000
             */

            private String RegionName;
            private String Code;

            public String getRegionName() {
                return RegionName;
            }

            public void setRegionName(String RegionName) {
                this.RegionName = RegionName;
            }

            public String getCode() {
                return Code;
            }

            public void setCode(String Code) {
                this.Code = Code;
            }
        }
    }
}
