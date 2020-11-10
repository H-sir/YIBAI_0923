package com.ybw.yibai.module.newuser;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ybw.yibai.R;
import com.ybw.yibai.base.BaseFragment;
import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.bean.CreateCustomers;
import com.ybw.yibai.common.bean.CreateCustomers.DataBean;
import com.ybw.yibai.common.bean.CustomersInfo;
import com.ybw.yibai.common.bean.NetworkType;
import com.ybw.yibai.common.utils.ExceptionUtil;
import com.ybw.yibai.common.utils.ImageUtil;
import com.ybw.yibai.common.utils.MessageUtil;
import com.ybw.yibai.common.utils.PermissionsUtil;
import com.ybw.yibai.common.widget.WaitDialog;
import com.ybw.yibai.module.home.HomeFragment;
import com.ybw.yibai.module.newuser.NewUserContract.NewUserPresenter;
import com.ybw.yibai.module.newuser.NewUserContract.NewUserView;

import org.greenrobot.eventbus.EventBus;

import static com.ybw.yibai.common.constants.Encoded.CODE_SUCCEED;
import static com.ybw.yibai.common.utils.FileUtil.judeFileExists;

/**
 * 创建新用户Fragment
 *
 * @author sjl
 * @date 2019/9/16
 */
public class NewUserFragment extends BaseFragment implements NewUserView, View.OnClickListener {

    /**
     * 用户头像地址
     */
    private String imagePath;

    /**
     * Root布局
     */
    private LinearLayout mRootLayout;

    /**
     * 头像
     */
    private ImageView mHeadPortraitImageView;

    /**
     * 客户名称
     */
    private EditText mCustomerNameEditText;

    /**
     * 联系电话
     */
    private EditText mTelephoneEditText;

    /**
     * 联系地址
     */
    private EditText mAddressEditText;

    /**
     * 取消
     */
    private TextView mCancelTextView;

    /**
     * 保存
     */
    private TextView mSaveTextView;

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
     * 上下文对象
     */
    private Context mContext;

    /**
     * Activity对象
     */
    private Activity mActivity;

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
    private NewUserPresenter mNewUserPresenter;

    @Override
    protected int setLayouts() {
        return R.layout.fragment_new_user;
    }

    @Override
    protected void findViews(View view) {
        mContext = getContext();
        mActivity = getActivity();

        mRootLayout = view.findViewById(R.id.rootLayout);
        mHeadPortraitImageView = view.findViewById(R.id.headPortraitImageView);
        mCustomerNameEditText = view.findViewById(R.id.customerNameEditText);
        mTelephoneEditText = view.findViewById(R.id.telephoneEditText);
        mAddressEditText = view.findViewById(R.id.addressEditText);
        mCancelTextView = view.findViewById(R.id.cancelTextView);
        mSaveTextView = view.findViewById(R.id.saveTextView);

        mCustomerNameEditText.setLayerType(View.LAYER_TYPE_HARDWARE,null);
        mTelephoneEditText.setLayerType(View.LAYER_TYPE_HARDWARE,null);
        mAddressEditText.setLayerType(View.LAYER_TYPE_HARDWARE,null);

        // 初始化自定义的WaitDialog
        mWaitDialog = new WaitDialog(mContext);
    }

    @Override
    protected void initView() {
        // 用户如果没有选择一个报价客户,就隐藏取消功能,强制用户选择一个报价客户
        if (0 == YiBaiApplication.getCid()) {
            mCancelTextView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {
        mNewUserPresenter = new NewUserPresenterImpl(this);

        mHeadPortraitImageView.setOnClickListener(this);
        mCancelTextView.setOnClickListener(this);
        mSaveTextView.setOnClickListener(this);
    }

    @Override
    public void onNetworkStateChange(NetworkType networkType) {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        // 头像
        if (id == R.id.headPortraitImageView) {
            // 显示选择拍照还是从相册中选择的PopupWindow
            mNewUserPresenter.displayPopupWindow(mRootLayout);
        }

        // 取消
        if (id == R.id.cancelTextView) {
            mActivity.finish();
        }

        // 保存
        if (id == R.id.saveTextView) {
            String name = mCustomerNameEditText.getText().toString().trim();
            String telephone = mTelephoneEditText.getText().toString().trim();
            String address = mAddressEditText.getText().toString().trim();
            CustomersInfo info = new CustomersInfo(imagePath, name, telephone, address);
            mNewUserPresenter.newCustomers(info);
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
            mNewUserPresenter.applyPermissions(mPermissions);
        }

        // 从相册中选择
        if (id == R.id.photoAlbumChooseTextView) {
            // 打开相册
            mNewUserPresenter.openPhotoAlbum();
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
            mNewUserPresenter.openCamera();
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
            mNewUserPresenter.openCamera();
        } else {
            PermissionsUtil.showNoCameraPermissions(mActivity);
        }
    }

    /**
     * 获得Activity返回的数据
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mNewUserPresenter.onActivityResult(requestCode, resultCode, data);
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
        mHeadPortraitImageView.setImageBitmap(bitmap);
    }

    /**
     * 在创建客户信息成功时回调
     *
     * @param createCustomers 客户信息
     */
    @Override
    public void onNewCustomersSuccess(CreateCustomers createCustomers) {
        MessageUtil.showMessage(createCustomers.getMsg());
        if (CODE_SUCCEED != createCustomers.getCode()) {
            return;
        }
        DataBean data = createCustomers.getData();
        if (null == data) {
            return;
        }
        int id = data.getId();
        String name = data.getName();
        String logo = data.getLogo();
        /**
         * 发送事件通知"Home界面"获取用户信息成功
         * {@link HomeFragment#returnCustomersInfo(CustomersInfo)}
         */
        EventBus.getDefault().post(new CustomersInfo(id, name, logo));
        mActivity.finish();
    }

    /**
     * 在请求网络数据之前显示Loading界面
     */
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
        if (null != mNewUserPresenter) {
            mNewUserPresenter.onDetachView();
            mNewUserPresenter = null;
        }
    }
}