package com.ybw.yibai.common.bean;

import com.google.gson.annotations.SerializedName;

/**
 * <pre>
 *     author : HKR
 *     time   : 2020/10/12
 *     desc   :
 * </pre>
 */
public class VloeaBean {

    /**
     * vloea : true
     */

    @SerializedName("vloea") private String vloea;

    public String getVloea() {
        return vloea;
    }

    public void setVloea(String vloea) {
        this.vloea = vloea;
    }
}
