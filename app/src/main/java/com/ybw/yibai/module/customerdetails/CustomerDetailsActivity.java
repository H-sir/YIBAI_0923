package com.ybw.yibai.module.customerdetails;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ybw.yibai.R;
import com.ybw.yibai.base.BaseActivity;
import com.ybw.yibai.common.bean.CustomerList.DataBean.ListBean;
import com.ybw.yibai.common.bean.CustomersInfo;
import com.ybw.yibai.common.bean.DeleteCustomer;
import com.ybw.yibai.common.bean.EditCustomer;
import com.ybw.yibai.common.bean.NetworkType;
import com.ybw.yibai.common.utils.ExceptionUtil;
import com.ybw.yibai.common.utils.ImageUtil;
import com.ybw.yibai.common.utils.MessageUtil;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.utils.PermissionsUtil;
import com.ybw.yibai.common.widget.WaitDialog;
import com.ybw.yibai.module.customerdetails.CustomerDetailsContract.CustomerDetailsPresenter;
import com.ybw.yibai.module.customerdetails.CustomerDetailsContract.CustomerDetailsView;
import com.ybw.yibai.module.customerlist.CustomerListFragment;

import org.greenrobot.eventbus.EventBus;

import static com.ybw.yibai.common.constants.Encoded.CODE_SUCCEED;
import static com.ybw.yibai.common.constants.Preferences.CUSTOMERS_ID;
import static com.ybw.yibai.common.constants.Preferences.CUSTOMERS_LOGO;
import static com.ybw.yibai.common.constants.Preferences.CUSTOMERS_MANE;
import static com.ybw.yibai.common.constants.Preferences.CUSTOMER_INFO;
import static com.ybw.yibai.common.constants.Preferences.USER_INFO;
import static com.ybw.yibai.common.utils.FileUtil.judeFileExists;

/**
 * 客户详情
 *
 * @author sjl
 * @date 2019/10/25
 */
public class CustomerDetailsActivity extends BaseActivity implements CustomerDetailsView, View.OnClickListener {

    /**
     * 客户的ID
     */
    private int cid;

    /**
     * 用户头像地址
     */
    private String imagePath;

    /**
     * Root布局
     */
    private View mRootView;

    /**
     * 返回图标
     */
    private ImageView mBackImageView;

    /**
     * 删除客户
     */
    private TextView mDeleteTextView;

    /**
     * 客户头像
     */
    private ImageView mImageView;

    /**
     * 客户名称
     */
    private EditText mNameEditText;

    /**
     * 客户电话
     */
    private EditText mPhoneEditText;

    /**
     * 客户详细地址
     */
    private EditText mAddressEditText;

    /**
     * 状态
     */
    private TextView mStateTextView;

    /**
     * 更新资料
     */
    private Button mButton;

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
     * 客户详情
     */
    private ListBean mCustomerInfo;

    /**
     * 要申请的权限(访问手机相机权限)
     */
    private String[] mPermissions = {
            Manifest.permission.CAMERA
    };

    /**
     * View 持有 Presenter的接口引用
     * <p>
     * PresenterImpl 作用为触发加载数据
     */
    private CustomerDetailsPresenter mCustomerDetailsPresenter;

    @Override
    protected int setLayout() {
        return R.layout.activity_customer_details;
    }

    @Override
    protected void initView() {
        mRootView = findViewById(R.id.rootLayout);
        mBackImageView = findViewById(R.id.backImageView);
        mDeleteTextView = findViewById(R.id.deleteTextView);

        mImageView = findViewById(R.id.imageView);
        mNameEditText = findViewById(R.id.nameEditText);
        mPhoneEditText = findViewById(R.id.phoneEditText);
        mAddressEditText = findViewById(R.id.addressEditText);
        mStateTextView = findViewById(R.id.stateTextView);

        mButton = findViewById(R.id.updateInformationButton);
        mWaitDialog = new WaitDialog(this);

        // 设置状态栏成白色的背景,字体颜色为黑色
        OtherUtil.setStatusBarColor(this, ContextCompat.getColor(this, android.R.color.white));
    }

    @Override
    protected void initData() {
        mCustomerInfo = (ListBean) getIntent().getSerializableExtra(CUSTOMER_INFO);
        if (null == mCustomerInfo) {
            return;
        }
        String logo = mCustomerInfo.getLogo();
        String name = mCustomerInfo.getName();
        String phone = mCustomerInfo.getTel();
        String area = mCustomerInfo.getArea();
        String address = mCustomerInfo.getAddress();

        if (!TextUtils.isEmpty(logo)) {
            ImageUtil.displayImage(this, mImageView, logo);
        }
        if (!TextUtils.isEmpty(name)) {
            mNameEditText.setText(name);
            mNameEditText.setSelection(name.length());
        }
        if (!TextUtils.isEmpty(phone)) {
            mPhoneEditText.setText(phone);
        }
        StringBuilder stringBuilder = new StringBuilder();
        if (!TextUtils.isEmpty(area)) {
            stringBuilder.append(area);
        }
        if (!TextUtils.isEmpty(address)) {
            stringBuilder.append(address);
        }
        String string = stringBuilder.toString();
        if (!TextUtils.isEmpty(string)) {
            mAddressEditText.setText(string);
        }
    }

    @Override
    protected void initEvent() {
        mCustomerDetailsPresenter = new CustomerDetailsPresenterImpl(this);
        mBackImageView.setOnClickListener(this);
        mStateTextView.setOnClickListener(this);
        mDeleteTextView.setOnClickListener(this);
        mImageView.setOnClickListener(this);
        mButton.setOnClickListener(this);
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

        // 状态
        if (id == R.id.stateTextView) {

        }

        // 删除客户
        if (id == R.id.deleteTextView) {
            mCustomerDetailsPresenter.displayDeleteCustomerDialog();
        }

        // 客户头像
        if (id == R.id.imageView) {
            mCustomerDetailsPresenter.displayPopupWindow(mRootView);
        }

        // 更新资料
        if (id == R.id.updateInformationButton) {
            int cid = mCustomerInfo.getId();
            String name = mNameEditText.getText().toString().trim();
            String phone = mPhoneEditText.getText().toString().trim();
            String address = mAddressEditText.getText().toString().trim();
            CustomersInfo customersInfo = new CustomersInfo(cid, imagePath, name, phone, address);
            mCustomerDetailsPresenter.editCustomer(customersInfo);
        }
    }

    /**
     * PopupWindow里面的Item View被点击的时候回调
     *
     * @param v 被点击的view
     */
    @Override
    public void onPopupWindowItemClick(View v) {
        int id = v.getId();

        // 拍照上传
        if (id == R.id.takePhotosTextView) {
            // 申请权限
            mCustomerDetailsPresenter.applyPermissions(mPermissions);
        }

        // 从相册中选择
        if (id == R.id.photoAlbumChooseTextView) {
            // 打开相册
            mCustomerDetailsPresenter.openPhotoAlbum();
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
            mCustomerDetailsPresenter.openCamera();
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
            mCustomerDetailsPresenter.openCamera();
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
        mCustomerDetailsPresenter.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 在用户完成图片裁剪时
     *
     * @param imagePath 裁剪后的图片路径
     */
    @Override
    public void onCropImageFinish(String imagePath) {
        this.imagePath = imagePath;
        if (!judeFileExists(imagePath)) {
            return;
        }
        Bitmap bitmap = ImageUtil.imageToBitmap(imagePath);
        if (null == bitmap) {
            return;
        }
        ImageUtil.displayImage(this, mImageView, bitmap);
    }

    /**
     * 在修改客户信息成功时回调
     *
     * @param editCustomer 在修改客户信息成功时回调服务器端返回的信息
     */
    @Override
    public void onEditCustomerSuccess(EditCustomer editCustomer) {
        MessageUtil.showMessage(editCustomer.getMsg());
        if (CODE_SUCCEED != editCustomer.getCode()) {
            return;
        }
        /**
         * 发送数据到{@link CustomerListFragment#onEditCustomerSuccess(EditCustomer)}
         * 通知其重新获取客户列表
         */
        EventBus.getDefault().post(editCustomer);
        onBackPressed();
    }

    /**
     * 是否确定删除该客户
     *
     * @param result true确定删除该客户,false不删除该客户
     */
    @Override
    public void areYouSureToDeleteThisCustomer(boolean result) {
        if (result) {
            cid = mCustomerInfo.getId();
            mCustomerDetailsPresenter.deleteCustomer(cid);
        }
    }

    /**
     * 在删除客户成功时回调
     *
     * @param deleteCustomer 在删除客户成功时服务器端返回的数据
     */
    @Override
    public void onDeleteCustomerSuccess(DeleteCustomer deleteCustomer) {
        MessageUtil.showMessage(deleteCustomer.getMsg());
        if (CODE_SUCCEED != deleteCustomer.getCode()) {
            return;
        }
        SharedPreferences mPreferences = getSharedPreferences(USER_INFO, MODE_PRIVATE);
        int customersId = mPreferences.getInt(CUSTOMERS_ID, 0);
        if (cid == customersId) {
            // 如果当前删除的客户是当前用户选择的服务客户,就清空吧
            SharedPreferences.Editor edit = mPreferences.edit();
            edit.putInt(CUSTOMERS_ID, 0);
            edit.putString(CUSTOMERS_LOGO, null);
            edit.putString(CUSTOMERS_MANE, null);
            edit.apply();
        }
        /**
         * 发送数据到{@link CustomerListFragment#onDeleteCustomerSuccess(DeleteCustomer)}
         * 通知其重新获取客户列表
         */
        EventBus.getDefault().post(deleteCustomer);
        onBackPressed();
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
        if (null != mCustomerDetailsPresenter) {
            mCustomerDetailsPresenter.onDetachView();
            mCustomerDetailsPresenter = null;
        }
    }
}
