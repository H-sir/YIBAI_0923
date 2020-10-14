package com.ybw.yibai.module.designdetails;

import com.ybw.yibai.base.BasePresenterImpl;
import com.ybw.yibai.common.bean.BaseBean;
import com.ybw.yibai.common.bean.DesignDetails;

/**
 * <pre>
 *     author : HKR
 *     time   : 2020/09/02
 *     desc   :我的实际界面Presenter实现类
 * </pre>
 */
public class DesignDetailsPresenterImpl extends BasePresenterImpl<DesignDetailsContract.DesignDetailsView>
        implements DesignDetailsContract.DesignDetailsPresenter, DesignDetailsContract.CallBack {

    private static final String TAG = "DesignDetailsPresenterImpl";

    private DesignDetailsContract.DesignDetailsView mDesignDetailsView;
    private DesignDetailsContract.DesignDetailsModel mSceneDetailsModel;

    /**
     * 构造方法
     *
     * @param view View的对象
     */
    public DesignDetailsPresenterImpl(DesignDetailsContract.DesignDetailsView view) {
        super(view);
        // 调用父类的方法获取View的对象
        this.mDesignDetailsView = getView();
        mSceneDetailsModel = new DesignDetailsModelImpl();
    }

    /**
     * 获取设计详情
     *
     * @param designNumber 设计号
     */
    @Override
    public void getDesignDetails(String designNumber) {
        mSceneDetailsModel.getDesignDetails(designNumber, this);
    }

    /**
     * 获取设计详情成功回调
     */
    @Override
    public void onGetDesignDetailsSuccess(DesignDetails designDetails) {
        mDesignDetailsView.onGetDesignDetailsSuccess(designDetails);
    }

    /**
     * 删除设计图片
     *
     * @param picId 图片ID
     */
    @Override
    public void deleteSchemePic(String picId) {
        mSceneDetailsModel.deleteSchemePic(picId, this);
    }

    @Override
    public void onDeleteSchemePicSuccess(String pic) {
        mDesignDetailsView.onDeleteSchemePicSuccess(pic);
    }

    /**
     * 删除场景
     */
    @Override
    public void deleteScheme(DesignDetails mDesignDetails,DesignDetails.DataBean.SchemelistBean schemelistBean) {
        mSceneDetailsModel.deleteScheme(mDesignDetails,schemelistBean, this);
    }

    @Override
    public void onDeleteSchemeSuccess(DesignDetails.DataBean.SchemelistBean schemelistBean) {
        mDesignDetailsView.onDeleteSchemeSuccess(schemelistBean);
    }

    /**
     * 删除设计
     */
    @Override
    public void deleteDesign(DesignDetails designDetails) {
        mSceneDetailsModel.deleteDesign(designDetails, this);
    }

    @Override
    public void onDeleteDesignSuccess(BaseBean baseBean) {
        mDesignDetailsView.onDeleteDesignSuccess(baseBean);
    }

    @Override
    public void editSceneName(String scnenName, DesignDetails.DataBean.SchemelistBean schemelistBean) {
        mSceneDetailsModel.editSceneName(scnenName,schemelistBean,this);
    }

    @Override
    public void editSceneNameSuccess() {
        mDesignDetailsView.editSceneNameSuccess();
    }
}
