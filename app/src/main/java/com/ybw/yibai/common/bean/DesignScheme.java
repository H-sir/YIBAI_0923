package com.ybw.yibai.common.bean;

import com.google.gson.annotations.SerializedName;

/**
 * <pre>
 *     author : HKR
 *     time   : 2020/08/28
 *     desc   :
 * </pre>
 */
public class DesignScheme {

    /**
     * code : 200
     * msg : ok
     * data : {"desing_number":"2020082810110099","scheme_name":"yy","scheme_id":489,"imglist":{"id":1292,"pic":"http://images.100ybw.com/design/2020/08/28/20200828115345795f487fc9142dc.jpg"}}
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
         * desing_number : 2020082810110099
         * scheme_name : yy
         * scheme_id : 489
         * imglist : {"id":1292,"pic":"http://images.100ybw.com/design/2020/08/28/20200828115345795f487fc9142dc.jpg"}
         */

        @SerializedName("desing_number") private String desingNumber;
        @SerializedName("scheme_name") private String schemeName;
        @SerializedName("scheme_id") private String schemeId;
//        @SerializedName("imglist") private ImglistBean imglist;

        public String getDesingNumber() {
            return desingNumber;
        }

        public void setDesingNumber(String desingNumber) {
            this.desingNumber = desingNumber;
        }

        public String getSchemeName() {
            return schemeName;
        }

        public void setSchemeName(String schemeName) {
            this.schemeName = schemeName;
        }

        public String getSchemeId() {
            return schemeId;
        }

        public void setSchemeId(String schemeId) {
            this.schemeId = schemeId;
        }

//        public ImglistBean getImglist() {
//            return imglist;
//        }
//
//        public void setImglist(ImglistBean imglist) {
//            this.imglist = imglist;
//        }

        public static class ImglistBean {
            /**
             * id : 1292
             * pic : http://images.100ybw.com/design/2020/08/28/20200828115345795f487fc9142dc.jpg
             */

            @SerializedName("id") private int id;
            @SerializedName("pic") private String pic;

            public int getId() {
                return id;
            }

            public void setId(int id) {
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
