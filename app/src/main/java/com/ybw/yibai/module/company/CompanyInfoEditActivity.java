package com.ybw.yibai.module.company;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ybw.yibai.R;
import com.ybw.yibai.base.BaseActivity;
import com.ybw.yibai.common.bean.NetworkType;
import com.ybw.yibai.common.bean.UpdateCompany;
import com.ybw.yibai.common.utils.ExceptionUtil;
import com.ybw.yibai.common.utils.ImageUtil;
import com.ybw.yibai.common.utils.MessageUtil;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.widget.WaitDialog;
import com.ybw.yibai.module.company.CompanyInfoEditContract.CompanyInfoEditPresenter;
import com.ybw.yibai.module.company.CompanyInfoEditContract.CompanyInfoEditView;
import com.ybw.yibai.module.home.HomeFragment;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import static com.ybw.yibai.common.constants.Encoded.CODE_SUCCEED;
import static com.ybw.yibai.common.constants.Encoded.REQUEST_OPEN_PHOTOS_CODE;
import static com.ybw.yibai.common.constants.Preferences.COMPANY;
import static com.ybw.yibai.common.constants.Preferences.COMPANY_DETAILS;
import static com.ybw.yibai.common.constants.Preferences.COMPANY_DETAILS_PIC;
import static com.ybw.yibai.common.constants.Preferences.COMPANY_LOGO;
import static com.ybw.yibai.common.constants.Preferences.USER_INFO;
import static com.ybw.yibai.common.utils.FileUtil.judeFileExists;

/**
 * 公司资料编辑
 *
 * @author sjl
 */
public class CompanyInfoEditActivity extends BaseActivity implements View.OnClickListener, CompanyInfoEditView {

    /**
     * 类型
     * 1:用户点击了"公司Logo"打开相册
     * 2:用户点击了"公司主页展示图片"打开相册
     */
    private int type;

    /**
     * 公司logo图片路径
     */
    private String logoPicPath;

    /**
     * 介绍图图片路径
     */
    private String detailsPicPath;

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    /**
     * 返回
     */
    private ImageView mBackImageView;

    /**
     * 公司名称
     */
    private EditText mCompanyNameEditText;

    /**
     * 公司Logo
     */
    private RelativeLayout mCompanyLogoLayout;

    /**
     * 公司Logo
     */
    private ImageView mCompanyLogoImageView;

    /**
     * 公司主页展示图片
     */
    private RelativeLayout mHomePageDisplayPicturesLayout;

    /**
     * 公司主页展示图片
     */
    private ImageView mHomePageDisplayPicturesImageView;

    /**
     * 文字介绍
     */
    private EditText mTextDescriptionEditText;

    /**
     * 提交
     */
    private Button mSubmitButton;

    /**
     * 自定义等待Dialog
     */
    private WaitDialog mWaitDialog;

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    /**
     * View 持有 Presenter的接口引用
     * <p>
     * PresenterImpl 作用为触发加载数据
     */
    private CompanyInfoEditPresenter mCompanyInfoEditPresenter;

    @Override
    protected int setLayout() {
        return R.layout.activity_company_info_edit;
    }

    @Override
    protected void initView() {
        mBackImageView = findViewById(R.id.backImageView);
        mCompanyNameEditText = findViewById(R.id.companyNameEditText);
        mCompanyLogoLayout = findViewById(R.id.companyLogoLayout);
        mCompanyLogoImageView = findViewById(R.id.companyLogoImageView);
        mHomePageDisplayPicturesLayout = findViewById(R.id.homePageDisplayPicturesLayout);
        mHomePageDisplayPicturesImageView = findViewById(R.id.homePageDisplayPicturesImageView);
        mTextDescriptionEditText = findViewById(R.id.textDescriptionEditText);
        mSubmitButton = findViewById(R.id.submitButton);
        mWaitDialog = new WaitDialog(this);

        // 设置状态栏成白色的背景,字体颜色为黑色
        OtherUtil.setStatusBarColor(this, ContextCompat.getColor(this, android.R.color.white));
    }

    @Override
    protected void initData() {
        SharedPreferences mPreferences = getSharedPreferences(USER_INFO, MODE_PRIVATE);
        String company = mPreferences.getString(COMPANY, null);
        String companyLogo = mPreferences.getString(COMPANY_LOGO, null);
        String companyDetailsPic = mPreferences.getString(COMPANY_DETAILS_PIC, null);
        if (!TextUtils.isEmpty(company)) {
            mCompanyNameEditText.setText(company);
            mCompanyNameEditText.setSelection(mCompanyNameEditText.getText().length());
        }
        if (!TextUtils.isEmpty(companyLogo)) {
            ImageUtil.displayImage(this, mCompanyLogoImageView, companyLogo);
        }
        if (!TextUtils.isEmpty(companyDetailsPic)) {
            ImageUtil.displayImage(this, mHomePageDisplayPicturesImageView, companyDetailsPic);
        }
    }

    @Override
    protected void initEvent() {
        mCompanyInfoEditPresenter = new CompanyInfoEditPresenterImpl(this);
        mBackImageView.setOnClickListener(this);
        mCompanyLogoLayout.setOnClickListener(this);
        mHomePageDisplayPicturesLayout.setOnClickListener(this);
        mSubmitButton.setOnClickListener(this);
    }

    @Override
    public void onNetworkStateChange(NetworkType networkType) {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        // 返回
        if (id == R.id.backImageView) {
            onBackPressed();
        }

        // 公司Logo
        if (id == R.id.companyLogoLayout) {
            type = 1;
            ImageUtil.openPhotoAlbum(this);
        }

        // 公司主页展示图片
        if (id == R.id.homePageDisplayPicturesLayout) {
            type = 2;
            ImageUtil.openPhotoAlbum(this);
        }

        // 提交
        if (id == R.id.submitButton) {
            String companyName = mCompanyNameEditText.getText().toString().trim();
            String textDescription = mTextDescriptionEditText.getText().toString().trim();
            mCompanyInfoEditPresenter.updateCompanyInfo(companyName, logoPicPath, detailsPicPath, textDescription);
        }
    }

    /**
     * 获得Activity返回的数据
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 获得系统相册Activity返回的数据
        if (resultCode == RESULT_OK && requestCode == REQUEST_OPEN_PHOTOS_CODE) {
            Uri uri = data.getData();
            if (null == uri) {
                return;
            }
            // 根据Uri获取图片的绝对路径
            String path = ImageUtil.getRealPathFromUri(this, uri);
            if (!judeFileExists(path)) {
                return;
            }
            File file = new File(path);
            if (1 == type) {
                logoPicPath = path;
                ImageUtil.displayImage(this, mCompanyLogoImageView, file);
            } else if (2 == type) {
                detailsPicPath = path;
                ImageUtil.displayImage(this, mHomePageDisplayPicturesImageView, file);
            }
        }
    }

    /**
     * 在修改用户公司信息成功时回调
     *
     * @param updateCompany 修改用户公司信息时服务器端返回的数据
     */
    @Override
    public void onUpdateCompanyInfoSuccess(UpdateCompany updateCompany) {
        if (CODE_SUCCEED != updateCompany.getCode()) {
            MessageUtil.showMessage(updateCompany.getMsg());
            return;
        }
        UpdateCompany.DataBean data = updateCompany.getData();

        SharedPreferences sharedPreferences = getSharedPreferences(USER_INFO, MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        String company = data.getName();
        if (!TextUtils.isEmpty(company)) {
            edit.putString(COMPANY, company);
        }
        String logo = data.getLogo();
        if (!TextUtils.isEmpty(logo)) {
            edit.putString(COMPANY_LOGO, logo);
        }
        String companyDetails = data.getDetails();
        if (!TextUtils.isEmpty(companyDetails)) {
            edit.putString(COMPANY_DETAILS, companyDetails);
        }
        String companyDetailsPic = data.getDetails_pic();
        if (!TextUtils.isEmpty(companyDetailsPic)) {
            edit.putString(COMPANY_DETAILS_PIC, companyDetailsPic);
        }
        edit.apply();
        /**
         * 发送数据到{@link HomeFragment#onUpdateCompanyInfoSuccess(UpdateCompany)}使其修改公司介绍图/描述
         */
        EventBus.getDefault().post(updateCompany);
        finish();
    }

    /**
     * 在请求网络数据之前显示Loading界面
     */
    @Override
    public void onShowLoading() {
        if (!mWaitDialog.isShowing()) {
            mWaitDialog.setWaitDialogText(getResources().getString(R.string.loading));
            mWaitDialog.show();
        }
    }

    /**
     * 在请求网络数据完成隐藏Loading界面
     */
    @Override
    public void onHideLoading() {
        if (mWaitDialog.isShowing()) {
            mWaitDialog.dismiss();
        }
    }

    /**
     * 在请求网络数据失败时进行一些操作,如显示错误信息...
     *
     * @param throwable 异常类型
     */
    @Override
    public void onLoadDataFailure(Throwable throwable) {
        ExceptionUtil.handleException(throwable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mCompanyInfoEditPresenter) {
            mCompanyInfoEditPresenter.onDetachView();
            mCompanyInfoEditPresenter = null;
        }
    }
}
