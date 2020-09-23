package com.ybw.yibai.common.bean;

/**
 * <pre>
 *     author : HKR
 *     time   : 2020/08/27
 *     desc   :
 * </pre>
 */
public class DesignSchemeRequest {
    private String desingNumber;   //设计编号
    private int type;               //1创建场景2添加图片和产品
    private String schemeId;       //场景id(type为2时必传)
    private String schemeName;     //场景名称(type为1时才有效,可不传)
    private String pic;             //图片,单图表单提交模式(type为2时必传,为1时也可以一起传入,相当于一起创建)
    private String productSkuId;  //关联的产品sku_id(如果是组合产品格式为:主id,副id)

    public String getDesingNumber() {
        return desingNumber;
    }

    public void setDesingNumber(String desingNumber) {
        this.desingNumber = desingNumber;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getProductSkuId() {
        return productSkuId;
    }

    public void setProductSkuId(String productSkuId) {
        this.productSkuId = productSkuId;
    }
}
