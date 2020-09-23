package com.ybw.yibai.module.nickname;

import android.app.Activity;
import android.text.TextUtils;

import com.ybw.yibai.base.BasePresenterImpl;
import com.ybw.yibai.common.bean.EditUserInfo;
import com.ybw.yibai.common.utils.MessageUtil;
import com.ybw.yibai.common.utils.NetworkStateUtil;
import com.ybw.yibai.module.nickname.NicknameContract.CallBack;
import com.ybw.yibai.module.nickname.NicknameContract.NicknameModel;
import com.ybw.yibai.module.nickname.NicknameContract.NicknamePresenter;
import com.ybw.yibai.module.nickname.NicknameContract.NicknameView;

/**
 * 修改昵称Presenter实现类
 *
 * @author sjl
 */
public class NicknamePresenterImpl extends BasePresenterImpl<NicknameView> implements NicknamePresenter, CallBack {

    /**
     * PresenterImpl 持有 View的接口引用
     */
    private NicknameView mModifyNicknameView;

    /**
     * PresenterImpl 持有 Model的接口引用
     */
    private NicknameModel mModifyNicknameModel;

    /**
     * 构造方法
     *
     * @param view View的对象
     */
    public NicknamePresenterImpl(NicknameView view) {
        super(view);

        // 调用父类的方法获取View的对象
        this.mModifyNicknameView = getView();
        mModifyNicknameModel = new NicknameModelImpl();
    }

    /**
     * 修改用户昵称
     *
     * @param nickName 昵称
     */
    @Override
    public void editUserInfo(String nickName) {
        Activity activity = (Activity) mModifyNicknameView;
        if (!NetworkStateUtil.isNetworkAvailable(activity)) {
            MessageUtil.showNoNetwork();
            return;
        }
        if (!TextUtils.isEmpty(nickName)) {
            mModifyNicknameModel.editUserInfo(nickName, this);
        }
    }

    /**
     * 修改用户昵称成功时回调
     *
     * @param editUserInfo 修改用户基础信息时服务器端返回的数据
     */
    @Override
    public void onEditUserInfoSuccess(EditUserInfo editUserInfo) {
        mModifyNicknameView.onEditUserInfoSuccess(editUserInfo);
    }
}
