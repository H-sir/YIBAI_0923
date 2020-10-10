package com.ybw.yibai.common.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * <pre>
 *     author : HKR
 *     time   : 2020/08/26
 *     desc   :
 * </pre>
 */
public class DesignDetails implements Serializable {

    /**
     * code : 200
     * msg : ok
     * data : {"number":"2020071351525454","addtime":"1594653507","schemelist":[{"scheme_id":"265","scheme_name":"默认场景","imglist":[]},{"scheme_id":"264","scheme_name":"家里墩","imglist":[{"id":"459","pic":"http://images.100ybw.com/design/2020/07/13/20200713111903425f0c7b6724bb0.jpg"},{"id":"460","pic":"http://images.100ybw.com/design/2020/07/13/20200713111904285f0c7b689f441.jpg"}]},{"scheme_id":"263","scheme_name":"66","imglist":[{"id":"457","pic":"http://images.100ybw.com/design/2020/07/13/20200713111828505f0c7b44b8f31.jpg"},{"id":"458","pic":"http://images.100ybw.com/design/2020/07/13/20200713111848455f0c7b58943a7.jpg"}]}],"offer_url":""}
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
         * number : 2020071351525454
         * addtime : 1594653507
         * schemelist : [{"scheme_id":"265","scheme_name":"默认场景","imglist":[]},{"scheme_id":"264","scheme_name":"家里墩","imglist":[{"id":"459","pic":"http://images.100ybw.com/design/2020/07/13/20200713111903425f0c7b6724bb0.jpg"},{"id":"460","pic":"http://images.100ybw.com/design/2020/07/13/20200713111904285f0c7b689f441.jpg"}]},{"scheme_id":"263","scheme_name":"66","imglist":[{"id":"457","pic":"http://images.100ybw.com/design/2020/07/13/20200713111828505f0c7b44b8f31.jpg"},{"id":"458","pic":"http://images.100ybw.com/design/2020/07/13/20200713111848455f0c7b58943a7.jpg"}]}]
         * offer_url :
         */

        @SerializedName("number") private String number;
        @SerializedName("addtime") private String addtime;
        @SerializedName("offer_url") private String offerUrl;
        @SerializedName("schemelist") private List<SchemelistBean> schemelist;

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getOfferUrl() {
            return offerUrl;
        }

        public void setOfferUrl(String offerUrl) {
            this.offerUrl = offerUrl;
        }

        public List<SchemelistBean> getSchemelist() {
            return schemelist;
        }

        public void setSchemelist(List<SchemelistBean> schemelist) {
            this.schemelist = schemelist;
        }

        public static class SchemelistBean {
            /**
             * scheme_id : 265
             * scheme_name : 默认场景
             * imglist : []
             */

            @SerializedName("scheme_id") private String schemeId;
            @SerializedName("scheme_name") private String schemeName;
            @SerializedName("bgpic") private String bgpic;
            @SerializedName("imglist") private List<ImaData> imglist;

            public String getBgpic() {
                return bgpic;
            }

            public void setBgpic(String bgpic) {
                this.bgpic = bgpic;
            }

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

            public List<ImaData> getImglist() {
                return imglist;
            }

            public void setImglist(List<ImaData> imglist) {
                this.imglist = imglist;
            }

            public static class ImaData {
                @SerializedName("id") private String id;
                @SerializedName("pic") private String pic;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
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
}
