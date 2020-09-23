package com.ybw.yibai.module.newuser;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yalantis.ucrop.UCrop;
import com.ybw.yibai.R;
import com.ybw.yibai.base.BasePresenterImpl;
import com.ybw.yibai.common.bean.CreateCustomers;
import com.ybw.yibai.common.bean.CustomersInfo;
import com.ybw.yibai.common.utils.FileUtil;
import com.ybw.yibai.common.utils.ImageUtil;
import com.ybw.yibai.common.utils.MessageUtil;
import com.ybw.yibai.common.utils.NetworkStateUtil;
import com.ybw.yibai.common.utils.PermissionsUtil;
import com.ybw.yibai.module.newuser.NewUserContract.CallBack;
import com.ybw.yibai.module.newuser.NewUserContract.NewUserModel;
import com.ybw.yibai.module.newuser.NewUserContract.NewUserPresenter;
import com.ybw.yibai.module.newuser.NewUserContract.NewUserView;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;
import static com.ybw.yibai.common.constants.Encoded.REQUEST_OPEN_CAMERA_CODE;
import static com.ybw.yibai.common.constants.Encoded.REQUEST_OPEN_PHOTOS_CODE;
import static com.ybw.yibai.common.utils.ImageUtil.startUCrop;

/**
 * 创建新用户Presenter实现类
 *
 * @author sjl
 * @date 2019/9/19
 */
public class NewUserPresenterImpl extends BasePresenterImpl<NewUserView> implements NewUserPresenter, CallBack, View.OnClickListener {

    /**
     * 显示选择拍照还是从相册中选择的PopupWindow
     */
    private PopupWindow mPopupWindow;

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    /**
     * PresenterImpl 持有 View的接口引用
     */
    private NewUserView mNewUserView;

    /**
     * PresenterImpl 持有 Model的接口引用
     */
    private NewUserModel mNewUserModel;

    /**
     * 构造方法
     *
     * @param view View的对象
     */
    public NewUserPresenterImpl(NewUserView view) {
        super(view);

        // 调用父类的方法获取View的对象
        this.mNewUserView = getView();
        mNewUserModel = new NewUserModelImpl();
    }

    /**
     * 显示选择拍照还是从相册中选择的PopupWindow
     *
     * @param rootView View根布局
     */
    @Override
    public void displayPopupWindow(View rootView) {
        Fragment fragment = (Fragment) mNewUserView;
        if (null == mPopupWindow) {
            View view = fragment.getLayoutInflater().inflate(R.layout.popup_window_special_taking_pictures_layout, null);
            mPopupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

            RelativeLayout rootLayout = view.findViewById(R.id.rootLayout);
            // 拍照上传的TextView
            TextView takePhotosTextView = view.findViewById(R.id.takePhotosTextView);
            // 从相册中选择的TextView
            TextView photoAlbumChooseTextView = view.findViewById(R.id.photoAlbumChooseTextView);
            // 取消的TextView
            TextView cancelTextView = view.findViewById(R.id.cancelTextView);

            rootLayout.setOnClickListener(this);
            takePhotosTextView.setOnClickListener(this);
            photoAlbumChooseTextView.setOnClickListener(this);
            cancelTextView.setOnClickListener(this);

            mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.setFocusable(true);

            // 设置一个动画效果
            mPopupWindow.setAnimationStyle(R.style.PopupWindow_Anim);

            mPopupWindow.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
        } else {
            mPopupWindow.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
        }
    }

    /**
     * 申请权限
     *
     * @param permissions 要申请的权限列表
     */
    @Override
    public void applyPermissions(String[] permissions) {
        Fragment fragment = (Fragment) mNewUserView;
        // android6.0 以上才进行动态权限申请
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!PermissionsUtil.checkPermissionAllGranted(fragment, permissions)) {
                // 开始提交请求权限
                PermissionsUtil.startRequestPermission(fragment, permissions);
            } else {
                // 已经获取全部权限
                mNewUserView.applyPermissionsResults(true);
            }
        } else {
            // 6.0以下,默认获取全部权限
            mNewUserView.applyPermissionsResults(true);
        }
    }

    /**
     * 打开相机
     */
    @Override
    public void openCamera() {
        Fragment fragment = (Fragment) mNewUserView;
        ImageUtil.openCamera(fragment, null);
    }

    /**
     * 打开相册
     */
    @Override
    public void openPhotoAlbum() {
        Fragment fragment = (Fragment) mNewUserView;
        ImageUtil.openPhotoAlbum(fragment);
    }

    /**
     * 获得Activity返回的数据
     *
     * @param requestCode 请求代码
     * @param resultCode  结果代码
     * @param data        返回的数据
     */
    @Override
    @SuppressWarnings("all")
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Fragment fragment = (Fragment) mNewUserView;
        Activity activity = fragment.getActivity();
        // 获得系统相册Activity返回的数据
        if (resultCode == RESULT_OK && requestCode == REQUEST_OPEN_PHOTOS_CODE) {
            Uri uri = data.getData();
            if (null == uri) {
                return;
            }
            // 调用照片的裁剪功能
            startUCrop(activity, fragment, uri);
        }

        // 获得系统相机Activity返回的数据
        else if (resultCode == RESULT_OK && requestCode == REQUEST_OPEN_CAMERA_CODE) {
            Uri uri = ImageUtil.getCameraResultUri(fragment.getContext());
            if (null == uri) {
                return;
            }
            // 调用照片的裁剪功能
            startUCrop(activity, fragment, uri);
        }

        // 获取裁剪图片
        else if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            Uri resultUri = UCrop.getOutput(data);
            String path = ImageUtil.getRealPathFromUri(activity, resultUri);
            mNewUserView.onCropImageFinish(path);
        }

        // 裁剪图片错误
        else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }
    }

    /**
     * 创建客户
     *
     * @param clientInfo 客户信息
     */
    @Override
    public void newCustomers(CustomersInfo clientInfo) {
        Activity activity = ((Fragment) mNewUserView).getActivity();
        if (null == activity) {
            return;
        }
        if (!NetworkStateUtil.isNetworkAvailable(activity)) {
            MessageUtil.showNoNetwork();
            return;
        }
        String name = clientInfo.getName();
        if (TextUtils.isEmpty(name)) {
            MessageUtil.showMessage(activity.getResources().getString(R.string.customer_name_needs_to_be_filled_in));
            return;
        }
        String logo = clientInfo.getLogo();
        if (!TextUtils.isEmpty(logo) && FileUtil.judeFileExists(logo)) {
            File file = new File(logo);
            Map<String, RequestBody> params = new HashMap<>();
            String fileName = file.getName();
            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/" + suffix), file);
            params.put("logo\"; filename=\"" + file.getName(), requestBody);
            clientInfo.setParams(params);
        }
        mNewUserModel.newCustomers(clientInfo, this);
    }

    /**
     * 在创建客户信息成功时回调
     *
     * @param createCustomers 客户信息
     */
    @Override
    public void onNewCustomersSuccess(CreateCustomers createCustomers) {
        mNewUserView.onNewCustomersSuccess(createCustomers);
    }

    @Override
    public void onClick(View v) {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
        mNewUserView.onPopupWindowItemClick(v);
    }
}
