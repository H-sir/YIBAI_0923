package com.ybw.yibai.module.design;

import com.ybw.yibai.base.BasePresenterImpl;
import com.ybw.yibai.common.bean.BaseBean;
import com.ybw.yibai.common.bean.DesignList;
import com.ybw.yibai.common.bean.SceneInfo;
import com.ybw.yibai.module.design.DesignContract.DesignModel;
import com.ybw.yibai.module.design.DesignContract.DesignPresenter;
import com.ybw.yibai.module.design.DesignContract.DesignView;

import java.util.List;

/**
 * <pre>
 *     author : HKR
 *     time   : 2020/09/02
 *     desc   :我的实际界面Presenter实现类
 * </pre>
 */
public class DesignPresenterImpl extends BasePresenterImpl<DesignView>
        implements DesignPresenter, DesignContract.CallBack {

    private static final String TAG = "DesignPresenterImpl";

    private DesignView mDesignView;
    private DesignModel mSceneModel;

    /**
     * 构造方法
     *
     * @param view View的对象
     */
    public DesignPresenterImpl(DesignView view) {
        super(view);
        // 调用父类的方法获取View的对象
        this.mDesignView = getView();
        mSceneModel = new DesignModelImpl();
    }

    /**
     * 获取设计列表接口
     */
    @Override
    public void getDesignList() {
        mSceneModel.getDesignList(this);
    }

    /**
     * 获取设计列表成功返回
     */
    @Override
    public void onGetDesignListSuccess(DesignList designList) {
        mDesignView.onGetDesignListSuccess(designList);
    }

    /**
     * 获取当前正在编辑的场景
     */
    @Override
    public void getEditSceneInfo() {
        mSceneModel.getEditSceneInfo(this);
    }

    @Override
    public void onFindEditSceneInfo(List<SceneInfo> defaultSceneInfoList) {
        mDesignView.onFindEditSceneInfo(defaultSceneInfoList);
    }

    /**
     * 删除设计
     *
     * @param listBean 设计
     */
    @Override
    public void deleteDesign(DesignList.DataBean.ListBean listBean) {
        mSceneModel.deleteDesign(listBean, this);
    }

    /**
     * 删除设计成功的回调
     * */
    @Override
    public void onDeleteDesignSuccess(BaseBean baseBean) {
        mDesignView.onDeleteDesignSuccess(baseBean);
    }


    @Override
    public void updateSceneInfo(DesignList.DataBean.ListBean.SchemelistBean schemelistBean) {
        mSceneModel.updateSceneInfo(schemelistBean,this);
    }

    @Override
    public void onUpdateSceneInfo() {
        mDesignView.onUpdateSceneInfo();
    }
}
