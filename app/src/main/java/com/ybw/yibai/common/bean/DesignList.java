package com.ybw.yibai.common.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * 设计列表
 *
 * @author sjl
 */
public class DesignList implements Serializable {

    /**
     * code : 200
     * msg : ok
     * data : {"count":"490","pagesize":10,"page":1,"list":[{"number":"2020091297561009","lasttime":"13:45","schemelist":[{"scheme_id":"668","scheme_name":"会议室","bgpic":"http://images.100ybw.com/design/2020/09/12/20200912014514105f5c606ad0db0.jpg"}]},{"number":"2020090857100484","lasttime":"09-08","schemelist":[{"scheme_id":"576","scheme_name":"默认场景","bgpic":"http://images.100ybw.com/public/default_logo.jpg"}]},{"number":"2020090754100519","lasttime":"09-07","schemelist":[{"scheme_id":"575","scheme_name":"默认场景","bgpic":"http://images.100ybw.com/public/default_logo.jpg"},{"scheme_id":"568","scheme_name":"默认场景","bgpic":"http://images.100ybw.com/public/default_logo.jpg"},{"scheme_id":"567","scheme_name":"默认场景","bgpic":"http://images.100ybw.com/public/default_logo.jpg"}]},{"number":"2020090754985210","lasttime":"09-07","schemelist":[{"scheme_id":"566","scheme_name":"默认场景","bgpic":"http://images.100ybw.com/public/default_logo.jpg"}]},{"number":"2020083048495050","lasttime":"08-30","schemelist":[{"scheme_id":"517","scheme_name":"默认场景","bgpic":"http://images.100ybw.com/public/default_logo.jpg"}]},{"number":"2020083097985750","lasttime":"08-30","schemelist":[{"scheme_id":"515","scheme_name":"默认场景","bgpic":"http://images.100ybw.com/public/default_logo.jpg"},{"scheme_id":"512","scheme_name":"默认场景","bgpic":"http://images.100ybw.com/public/default_logo.jpg"}]},{"number":"2020083054991029","lasttime":"08-30","schemelist":[{"scheme_id":"510","scheme_name":"默认场景","bgpic":"http://images.100ybw.com/public/default_logo.jpg"}]},{"number":"2020083054545610","lasttime":"08-30","schemelist":[{"scheme_id":"509","scheme_name":"默认场景","bgpic":"http://images.100ybw.com/public/default_logo.jpg"}]},{"number":"2020083010297491","lasttime":"08-30","schemelist":[{"scheme_id":"508","scheme_name":"默认场景","bgpic":"http://images.100ybw.com/public/default_logo.jpg"}]},{"number":"2020083010052991","lasttime":"08-30","schemelist":[{"scheme_id":"507","scheme_name":"默认场景","bgpic":"http://images.100ybw.com/public/default_logo.jpg"}]}]}
     */

    @SerializedName("code") private int code;
    @SerializedName("msg") private String msg;
    @SerializedName("data") private DataBean data;

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
         * count : 490
         * pagesize : 10
         * page : 1
         * list : [{"number":"2020091297561009","lasttime":"13:45","schemelist":[{"scheme_id":"668","scheme_name":"会议室","bgpic":"http://images.100ybw.com/design/2020/09/12/20200912014514105f5c606ad0db0.jpg"}]},{"number":"2020090857100484","lasttime":"09-08","schemelist":[{"scheme_id":"576","scheme_name":"默认场景","bgpic":"http://images.100ybw.com/public/default_logo.jpg"}]},{"number":"2020090754100519","lasttime":"09-07","schemelist":[{"scheme_id":"575","scheme_name":"默认场景","bgpic":"http://images.100ybw.com/public/default_logo.jpg"},{"scheme_id":"568","scheme_name":"默认场景","bgpic":"http://images.100ybw.com/public/default_logo.jpg"},{"scheme_id":"567","scheme_name":"默认场景","bgpic":"http://images.100ybw.com/public/default_logo.jpg"}]},{"number":"2020090754985210","lasttime":"09-07","schemelist":[{"scheme_id":"566","scheme_name":"默认场景","bgpic":"http://images.100ybw.com/public/default_logo.jpg"}]},{"number":"2020083048495050","lasttime":"08-30","schemelist":[{"scheme_id":"517","scheme_name":"默认场景","bgpic":"http://images.100ybw.com/public/default_logo.jpg"}]},{"number":"2020083097985750","lasttime":"08-30","schemelist":[{"scheme_id":"515","scheme_name":"默认场景","bgpic":"http://images.100ybw.com/public/default_logo.jpg"},{"scheme_id":"512","scheme_name":"默认场景","bgpic":"http://images.100ybw.com/public/default_logo.jpg"}]},{"number":"2020083054991029","lasttime":"08-30","schemelist":[{"scheme_id":"510","scheme_name":"默认场景","bgpic":"http://images.100ybw.com/public/default_logo.jpg"}]},{"number":"2020083054545610","lasttime":"08-30","schemelist":[{"scheme_id":"509","scheme_name":"默认场景","bgpic":"http://images.100ybw.com/public/default_logo.jpg"}]},{"number":"2020083010297491","lasttime":"08-30","schemelist":[{"scheme_id":"508","scheme_name":"默认场景","bgpic":"http://images.100ybw.com/public/default_logo.jpg"}]},{"number":"2020083010052991","lasttime":"08-30","schemelist":[{"scheme_id":"507","scheme_name":"默认场景","bgpic":"http://images.100ybw.com/public/default_logo.jpg"}]}]
         */

        @SerializedName("count") private String count;
        @SerializedName("pagesize") private int pagesize;
        @SerializedName("page") private int page;
        @SerializedName("list") private List<ListBean> list;

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public int getPagesize() {
            return pagesize;
        }

        public void setPagesize(int pagesize) {
            this.pagesize = pagesize;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * number : 2020091297561009
             * lasttime : 13:45
             * schemelist : [{"scheme_id":"668","scheme_name":"会议室","bgpic":"http://images.100ybw.com/design/2020/09/12/20200912014514105f5c606ad0db0.jpg"}]
             */

            @SerializedName("number") private String number;
            @SerializedName("lasttime") private String lasttime;
            @SerializedName("schemelist") private List<SchemelistBean> schemelist;

            public String getNumber() {
                return number;
            }

            public void setNumber(String number) {
                this.number = number;
            }

            public String getLasttime() {
                return lasttime;
            }

            public void setLasttime(String lasttime) {
                this.lasttime = lasttime;
            }

            public List<SchemelistBean> getSchemelist() {
                return schemelist;
            }

            public void setSchemelist(List<SchemelistBean> schemelist) {
                this.schemelist = schemelist;
            }

            public static class SchemelistBean {
                /**
                 * scheme_id : 668
                 * scheme_name : 会议室
                 * bgpic : http://images.100ybw.com/design/2020/09/12/20200912014514105f5c606ad0db0.jpg
                 */

                @SerializedName("scheme_id") private String schemeId;
                @SerializedName("scheme_name") private String schemeName;
                @SerializedName("bgpic") private String bgpic;

                public String getSchemeId() {
                    return schemeId;
                }

                public void setSchemeId(String schemeId) {
                    this.schemeId = schemeId;
                }

                public String getSchemeName() {
                    return schemeName;
                }

                public void setSchemeName(String schemeName) {
                    this.schemeName = schemeName;
                }

                public String getBgpic() {
                    return bgpic;
                }

                public void setBgpic(String bgpic) {
                    this.bgpic = bgpic;
                }
            }
        }
    }
}
