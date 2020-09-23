package com.ybw.yibai.common.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * 用户场景信息
 *
 * @author sjl
 */
@Table(name = "SceneInfo")
public class SceneInfo implements Serializable {
    /**
     * 用户登录成功时获得的Uid
     */
    @Column(name = "uid")
    private int uid;

    /**
     * 设计号
     */
    @Column(name = "number")
    private String number;

    /**
     * 场景id(一般为创建该场景的时间戳)
     */
    @Column(name = "sceneId", isId = true, autoGen = false)
    private long sceneId;

    @Column(name = "scheme_id")
    private String scheme_id;

    /**
     * 场景名称
     */
    @Column(name = "sceneName")
    private String sceneName;

    /**
     * 场景的背景图片
     */
    @Column(name = "sceneBackground")
    private String sceneBackground;

    /**
     * 该场景是否为当前正在编辑的这一个场景
     */
    @Column(name = "editScene")
    private boolean editScene;

    @Column(name = "count")
    private int count;

    /*----------*/

    /**
     * 是否显示CheckBox
     */
    private boolean showCheckBox;

    /**
     * 是否选中CheckBox
     */
    private boolean selectCheckBox;


    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public long getSceneId() {
        return sceneId;
    }

    public void setSceneId(long sceneId) {
        this.sceneId = sceneId;
    }

    public String getSceneName() {
        return sceneName;
    }

    public void setSceneName(String sceneName) {
        this.sceneName = sceneName;
    }

    public String getSceneBackground() {
        return sceneBackground;
    }

    public void setSceneBackground(String sceneBackground) {
        this.sceneBackground = sceneBackground;
    }

    public boolean isEditScene() {
        return editScene;
    }

    public void setEditScene(boolean editScene) {
        this.editScene = editScene;
    }

    public boolean isShowCheckBox() {
        return showCheckBox;
    }

    public void setShowCheckBox(boolean showCheckBox) {
        this.showCheckBox = showCheckBox;
    }

    public boolean isSelectCheckBox() {
        return selectCheckBox;
    }

    public void setSelectCheckBox(boolean selectCheckBox) {
        this.selectCheckBox = selectCheckBox;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getScheme_id() {
        return scheme_id;
    }

    public void setScheme_id(String scheme_id) {
        this.scheme_id = scheme_id;
    }
}
