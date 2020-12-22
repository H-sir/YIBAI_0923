package com.ybw.yibai.module.user;

import com.ybw.yibai.base.BasePresenterImpl;
import com.ybw.yibai.common.bean.CheckShareBean;
import com.ybw.yibai.common.bean.SceneInfo;

import java.util.List;

/**
 * 用户场景界面Presenter实现类
 *
 * @author sjl
 * @date 2019/9/14
 */
public class UserPresenterImpl extends BasePresenterImpl<UserContract.UserView> implements UserContract.UserPresenter, UserContract.CallBack {

    private static final String TAG = "UserPresenterImpl";

    /**
     * PresenterImpl 持有 View的接口引用
     */
    private UserContract.UserView mUserView;

    /**
     * PresenterImpl 持有 Model的接口引用
     */
    private UserContract.UserModel mUserModel;

    /**
     * 构造方法
     *
     * @param view View的对象
     */
    public UserPresenterImpl(UserContract.UserView view) {
        super(view);

        // 调用父类的方法获取View的对象
        this.mUserView = getView();
        mUserModel = new UserModelImpl();
    }

    /**
     * 查找用户的场景信息
     */
    @Override
    public void findUserSceneListInfo() {
        mUserModel.findUserSceneListInfo(this);
    }

    @Override
    public void onFindUserUserInfoSuccess(List<SceneInfo> sceneInfoList) {
        mUserView.onFindUserSceneInfoSuccess(sceneInfoList);
    }

    @Override
    public void updateUserSceneList(List<SceneInfo> sceneInfoList) {
        mUserModel.updateUserSceneList(sceneInfoList, this);
    }

    @Override
    public void updateUserScene(SceneInfo sceneInfo) {
        mUserModel.updateUserScene(sceneInfo,this);
    }

    @Override
    public void onUpdateUserSceneListSuccess() {
        mUserView.onUpdateUserSceneListSuccess();
    }

    @Override
    public void updateUserScene(List<SceneInfo> sceneInfoList) {
        mUserModel.updateUserScene(sceneInfoList,this);
    }

    @Override
    public void onUpdateUserSceneSuccess() {
        mUserView.onUpdateUserSceneSuccess();
    }


    @Override
    public void checkShare() {
        mUserModel.checkShare(this);
    }

    @Override
    public void checkShareData(CheckShareBean checkShareBean) {
        mUserView.checkShareData(checkShareBean);
    }

    @Override
    public void insufficientPermissions() {
        mUserView.insufficientPermissions();
    }
}
