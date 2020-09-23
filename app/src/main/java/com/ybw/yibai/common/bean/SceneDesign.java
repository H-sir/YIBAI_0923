package com.ybw.yibai.common.bean;

/**
 * <pre>
 *     author : HKR
 *     time   : 2020/08/28
 *     desc   : 场景的类
 * </pre>
 */
public class SceneDesign {
    private String desingNumber;//设计编号
    private String schemeId;       //场景ID
    private String schemeName;  //场景名称
    private int sceneNum;       //设计中场景的数量

    public String getDesingNumber() {
        return desingNumber;
    }

    public void setDesingNumber(String desingNumber) {
        this.desingNumber = desingNumber;
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

    public int getSceneNum() {
        return sceneNum;
    }

    public void setSceneNum(int sceneNum) {
        this.sceneNum = sceneNum;
    }
}
