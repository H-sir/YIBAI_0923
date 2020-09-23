package com.ybw.yibai.module.company;

import android.text.TextUtils;

import com.ybw.yibai.base.BasePresenterImpl;
import com.ybw.yibai.common.bean.UpdateCompany;
import com.ybw.yibai.module.company.CompanyInfoEditContract.CallBack;
import com.ybw.yibai.module.company.CompanyInfoEditContract.CompanyInfoEditModel;
import com.ybw.yibai.module.company.CompanyInfoEditContract.CompanyInfoEditPresenter;
import com.ybw.yibai.module.company.CompanyInfoEditContract.CompanyInfoEditView;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.ybw.yibai.common.utils.FileUtil.judeFileExists;

/**
 * 公司资料编辑Presenter实现类
 *
 * @author sjl
 */
public class CompanyInfoEditPresenterImpl extends BasePresenterImpl<CompanyInfoEditView> implements CompanyInfoEditPresenter, CallBack {

    /**
     * PresenterImpl 持有 View的接口引用
     */
    private CompanyInfoEditView mCompanyInfoEditView;

    /**
     * PresenterImpl 持有 Model的接口引用
     */
    private CompanyInfoEditModel mCompanyInfoEditModel;

    /**
     * 构造方法
     *
     * @param view View的对象
     */
    public CompanyInfoEditPresenterImpl(CompanyInfoEditView view) {
        super(view);

        // 调用父类的方法获取View的对象
        this.mCompanyInfoEditView = getView();
        mCompanyInfoEditModel = new CompanyInfoEditModelImpl();
    }

    /**
     * 修改用户公司信息
     *
     * @param name           公司名称
     * @param logoPicPath    公司logo图片路径
     * @param detailsPicPath 介绍图图片路径
     * @param details        描述
     */
    @Override
    public void updateCompanyInfo(String name, String logoPicPath, String detailsPicPath, String details) {
        Map<String, RequestBody> logoPicParams = new HashMap<>();
        if (!TextUtils.isEmpty(logoPicPath) && judeFileExists(logoPicPath)) {
            File file = new File(logoPicPath);

            String fileName = file.getName();
            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/" + suffix), file);
            logoPicParams.put("logo\"; filename=\"" + file.getName(), requestBody);
        }

        Map<String, RequestBody> detailsPicParams = new HashMap<>();
        if (!TextUtils.isEmpty(detailsPicPath) && judeFileExists(detailsPicPath)) {
            File file = new File(detailsPicPath);
            String fileName = file.getName();
            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/" + suffix), file);
            detailsPicParams.put("details_pic\"; filename=\"" + file.getName(), requestBody);
        }

        mCompanyInfoEditModel.updateCompanyInfo(name, logoPicParams, detailsPicParams, details, this);
    }

    /**
     * 在修改用户公司信息成功时回调
     *
     * @param updateCompany 修改用户公司信息时服务器端返回的数据
     */
    @Override
    public void onUpdateCompanyInfoSuccess(UpdateCompany updateCompany) {
        mCompanyInfoEditView.onUpdateCompanyInfoSuccess(updateCompany);
    }
}
