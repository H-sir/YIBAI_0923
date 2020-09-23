package com.ybw.yibai.module.cases;

import com.ybw.yibai.base.BasePresenterImpl;
import com.ybw.yibai.common.bean.Case;
import com.ybw.yibai.module.cases.CaseContract.*;

/**
 * 案例界面Presenter实现类
 *
 * @author sjl
 * @date 2019/5/16
 */
public class CasePresenterImpl extends BasePresenterImpl<CaseView> implements CasePresenter, CallBack {

    /**
     * PresenterImpl 持有 View的接口引用
     */
    private CaseView mCaseView;

    /**
     * PresenterImpl 持有 Model的接口引用
     */
    private CaseModel mCaseModel;

    /**
     * 构造方法
     *
     * @param view View的对象
     */
    public CasePresenterImpl(CaseView view) {
        super(view);

        // 调用父类的方法获取View的对象
        this.mCaseView = getView();
        mCaseModel = new CaseModelImpl();
    }

    /**
     * 获取公司案例
     *
     * @param projectClassifyId 案例分类Id
     */
    @Override
    public void getCase(int projectClassifyId) {
        mCaseModel.getCase(projectClassifyId, this);
    }

    /**
     * 在获取公司案例成功时回调
     *
     * @param case_ 公司案例
     */
    @Override
    public void onGetCaseSuccess(Case case_) {
        mCaseView.onGetCaseSuccess(case_);
    }
}
