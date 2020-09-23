package com.ybw.yibai.module.picturecase;

import com.ybw.yibai.base.BasePresenterImpl;
import com.ybw.yibai.common.bean.CaseClassify;
import com.ybw.yibai.module.picturecase.PictureCaseContract.CallBack;
import com.ybw.yibai.module.picturecase.PictureCaseContract.PictureCaseModel;
import com.ybw.yibai.module.picturecase.PictureCaseContract.PictureCasePresenter;
import com.ybw.yibai.module.picturecase.PictureCaseContract.PictureCaseView;

/**
 * 图片案例Presenter实现类
 *
 * @author sjl
 * @date 2019/11/9
 */
public class PictureCasePresenterImpl  extends BasePresenterImpl<PictureCaseView> implements PictureCasePresenter, CallBack{

    /**
     * PresenterImpl 持有 View的接口引用
     */
    private PictureCaseView mPictureCaseView;

    /**
     * PresenterImpl 持有 Model的接口引用
     */
    private PictureCaseModel mPictureCaseModel;

    /**
     * 构造方法
     *
     * @param view View的对象
     */
    public PictureCasePresenterImpl(PictureCaseView view) {
        super(view);

        // 调用父类的方法获取View的对象
        this.mPictureCaseView = getView();
        mPictureCaseModel = new PictureCaseModelImpl();
    }

    /**
     * 获取公司案例分类
     */
    @Override
    public void getCaseClassify() {
        mPictureCaseModel.getCaseClassify(this);
    }

    /**
     * 在获取案例分类成功时回调
     *
     * @param caseClassify 案例分类
     */
    @Override
    public void onGetCaseClassifySuccess(CaseClassify caseClassify) {
        mPictureCaseView.onGetCaseClassifySuccess(caseClassify);
    }
}
