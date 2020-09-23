package com.ybw.yibai.module.headportrait;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.github.chrisbanes.photoview.PhotoView;
import com.ybw.yibai.R;
import com.ybw.yibai.base.BaseActivity;
import com.ybw.yibai.common.bean.EditUserInfo;
import com.ybw.yibai.common.bean.EditUserInfo.DataBean;
import com.ybw.yibai.common.bean.NetworkType;
import com.ybw.yibai.common.utils.ExceptionUtil;
import com.ybw.yibai.common.utils.ImageUtil;
import com.ybw.yibai.common.utils.MessageUtil;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.utils.PermissionsUtil;
import com.ybw.yibai.common.widget.WaitDialog;
import com.ybw.yibai.module.headportrait.HeadPortraitContract.HeadPortraitPresenter;
import com.ybw.yibai.module.headportrait.HeadPortraitContract.HeadPortraitView;

import static com.ybw.yibai.common.constants.Encoded.CODE_SUCCEED;
import static com.ybw.yibai.common.constants.Preferences.HEAD;
import static com.ybw.yibai.common.constants.Preferences.USER_INFO;

/**
 * 头像
 *
 * @author sjl
 * @date 2019/10/29
 */
public class HeadPortraitActivity extends BaseActivity implements HeadPortraitView, View.OnClickListener {

    /**
     * View根布局
     */
    private RelativeLayout mRootLayout;

    /**
     * 和"状态栏"一样高的View
     */
    private View mBarView;

    /**
     * 返回图标
     */
    private ImageView mBackImageView;

    /**
     * 更多功能
     */
    private ImageView mMoreImageView;

    /**
     *
     */
    private PhotoView mPhotoView;

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
     * 要申请的权限(访问手机相机权限)
     */
    private String[] permissions = {
            Manifest.permission.CAMERA
    };

    /**
     *
     */
    private SharedPreferences mPreferences;

    /**
     * View 持有 Presenter的接口引用
     * <p>
     * PresenterImpl 作用为触发加载数据
     */
    private HeadPortraitPresenter mHeadPortraitPresenter;

    @Override
    protected int setLayout() {
        OtherUtil.transparentStatusBar(this);
        return R.layout.activity_head_portrait;
    }

    @Override
    protected void initView() {
        mRootLayout = findViewById(R.id.rootLayout);
        mBarView = findViewById(R.id.barView);
        mBackImageView = findViewById(R.id.backImageView);
        mMoreImageView = findViewById(R.id.moreImageView);
        mPhotoView = findViewById(R.id.photoView);
        mWaitDialog = new WaitDialog(this);
        OtherUtil.createBarView(this, mBarView);
    }

    @Override
    protected void initData() {
        mPreferences = getSharedPreferences(USER_INFO, MODE_PRIVATE);
        String mHead = mPreferences.getString(HEAD, null);
        ImageUtil.displayImage(this, mPhotoView, mHead);
    }

    @Override
    protected void initEvent() {
        mHeadPortraitPresenter = new HeadPortraitPresenterImpl(this);
        mBackImageView.setOnClickListener(this);
        mMoreImageView.setOnClickListener(this);
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

        if (id == R.id.moreImageView) {
            // 初始化选择拍照还是从相册中选择的PopupWindow
            mHeadPortraitPresenter.displayPopupWindow(mRootLayout);
        }
    }

    /**
     * 在选择拍照还是从相册中选择的PopupWindow里面的Item View被点击的时候回调
     *
     * @param v 被点击的view
     */
    @Override
    public void onPopupWindowItemClick(View v) {
        int id = v.getId();
        // 拍照上传
        if (id == R.id.takePhotosTextView) {
            // 申请权限
            mHeadPortraitPresenter.applyPermissions(permissions);
        }

        // 从相册中选择
        if (id == R.id.photoAlbumChooseTextView) {
            // 打开相册
            mHeadPortraitPresenter.openPhotoAlbum();
        }
    }

    /**
     * 申请权限的结果
     *
     * @param b true 已经获取全部权限,false 没有获取全部权限
     */
    @Override
    public void applyPermissionsResults(boolean b) {
        if (b) {
            // 打开相机
            mHeadPortraitPresenter.openCamera();
        }
    }

    /**
     * 请求权限的结果的回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 获得申请的全部权限
        if (PermissionsUtil.allPermissionsAreGranted(grantResults)) {
            // 打开相机
            mHeadPortraitPresenter.openCamera();
        } else {
            PermissionsUtil.showNoCameraPermissions(this);
        }
    }

    /**
     * 获得Activity返回的数据
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mHeadPortraitPresenter.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 在用户完成图片裁剪时
     *
     * @param imagePath 裁剪后的图片路径
     */
    @Override
    public void onCropImageFinish(String imagePath) {
        mHeadPortraitPresenter.editUserInfo(imagePath);
    }

    /**
     * 修改用户图片成功时回调
     *
     * @param editUserInfo 修改用户基础信息时服务器端返回的数据
     */
    @Override
    public void onEditUserInfoSuccess(EditUserInfo editUserInfo) {
        MessageUtil.showMessage(editUserInfo.getMsg());
        if (CODE_SUCCEED == editUserInfo.getCode()) {
            DataBean data = editUserInfo.getData();
            if (null == data) {
                return;
            }
            String head = data.getHead();
            if (TextUtils.isEmpty(head)) {
                return;
            }
            // 保存修改后的图片
            SharedPreferences.Editor edit = mPreferences.edit();
            edit.putString(HEAD, head);
            edit.apply();
            ImageUtil.displayImage(this, mPhotoView, head);
        }
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
        if (null != mHeadPortraitPresenter) {
            mHeadPortraitPresenter.onDetachView();
            mHeadPortraitPresenter = null;
        }
    }
}
