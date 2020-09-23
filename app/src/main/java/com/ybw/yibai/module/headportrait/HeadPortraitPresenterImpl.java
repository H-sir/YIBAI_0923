package com.ybw.yibai.module.headportrait;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yalantis.ucrop.UCrop;
import com.ybw.yibai.R;
import com.ybw.yibai.base.BasePresenterImpl;
import com.ybw.yibai.common.bean.EditUserInfo;
import com.ybw.yibai.common.utils.FileUtil;
import com.ybw.yibai.common.utils.ImageUtil;
import com.ybw.yibai.common.utils.MessageUtil;
import com.ybw.yibai.common.utils.NetworkStateUtil;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.utils.PermissionsUtil;
import com.ybw.yibai.module.headportrait.HeadPortraitContract.CallBack;
import com.ybw.yibai.module.headportrait.HeadPortraitContract.HeadPortraitModel;
import com.ybw.yibai.module.headportrait.HeadPortraitContract.HeadPortraitPresenter;
import com.ybw.yibai.module.headportrait.HeadPortraitContract.HeadPortraitView;

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
 * 修改头像Presenter实现类
 *
 * @author sjl
 */
public class HeadPortraitPresenterImpl extends BasePresenterImpl<HeadPortraitView> implements HeadPortraitPresenter, CallBack, View.OnClickListener {

    /**
     * 选择拍照还是从相册中选择的PopupWindow
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
    private HeadPortraitView mHeadPortraitView;

    /**
     * PresenterImpl 持有 Model的接口引用
     */
    private HeadPortraitModel mHeadPortraitModel;

    /**
     * 构造方法
     *
     * @param view View的对象
     */
    public HeadPortraitPresenterImpl(HeadPortraitView view) {
        super(view);

        // 调用父类的方法获取View的对象
        this.mHeadPortraitView = getView();
        mHeadPortraitModel = new HeadPortraitModelImpl();
    }

    /**
     * 初始化选择拍照还是从相册中选择的PopupWindow
     */
    @Override
    public void displayPopupWindow(RelativeLayout rootLayout) {
        Activity activity = ((Activity) mHeadPortraitView);
        if (mPopupWindow == null) {
            View view = activity.getLayoutInflater().inflate(R.layout.popup_window_taking_pictures_layout, null);
            mPopupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            // 拍照上传的TextView
            TextView mTakePhotosTextView = view.findViewById(R.id.takePhotosTextView);
            // 从相册中选择的TextView
            TextView mPhotoAlbumChooseTextView = view.findViewById(R.id.photoAlbumChooseTextView);
            // 取消的TextView
            TextView mCancelTextView = view.findViewById(R.id.cancelTextView);

            mTakePhotosTextView.setOnClickListener(this);
            mPhotoAlbumChooseTextView.setOnClickListener(this);
            mCancelTextView.setOnClickListener(this);

            mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.setFocusable(true);

            // 设置一个动画效果
            mPopupWindow.setAnimationStyle(R.style.PopupWindow_Anim);

            // 在弹出PopupWindow设置屏幕透明度
            OtherUtil.setBackgroundAlpha(activity, 0.6f);
            // 添加PopupWindow窗口关闭事件
            mPopupWindow.setOnDismissListener(OtherUtil.popupDismissListener(activity, 1f));
            mPopupWindow.showAtLocation(rootLayout, Gravity.BOTTOM, 0, 0);
        } else {
            // 在弹出PopupWindow设置屏幕透明度
            OtherUtil.setBackgroundAlpha(activity, 0.6f);
            // 添加PopupWindow窗口关闭事件
            mPopupWindow.setOnDismissListener(OtherUtil.popupDismissListener(activity, 1f));
            mPopupWindow.showAtLocation(rootLayout, Gravity.BOTTOM, 0, 0);
        }
    }

    /**
     * 申请权限
     *
     * @param permissions 要申请的权限列表
     */
    @Override
    public void applyPermissions(String[] permissions) {
        Activity activity = ((Activity) mHeadPortraitView);
        // android6.0 以上才进行动态权限申请
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!PermissionsUtil.checkPermissionAllGranted(activity, permissions)) {
                // 开始提交请求权限
                PermissionsUtil.startRequestPermission(activity, permissions);
            } else {
                // 已经获取全部权限
                mHeadPortraitView.applyPermissionsResults(true);
            }
        } else {
            // 6.0以下,默认获取全部权限
            mHeadPortraitView.applyPermissionsResults(true);
        }
    }

    /**
     * 打开相机
     */
    @Override
    public void openCamera() {
        Activity activity = ((Activity) mHeadPortraitView);
        ImageUtil.openCamera(activity, null);
    }

    /**
     * 打开相册
     */
    @Override
    public void openPhotoAlbum() {
        Activity activity = ((Activity) mHeadPortraitView);
        ImageUtil.openPhotoAlbum(activity);
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
        Activity activity = ((Activity) mHeadPortraitView);
        // 获得系统相册Activity返回的数据
        if (resultCode == RESULT_OK && requestCode == REQUEST_OPEN_PHOTOS_CODE) {
            Uri uri = data.getData();
            if (null == uri) {
                return;
            }
            // 调用照片的裁剪功能
            startUCrop(activity, uri);
        }

        // 获得系统相机Activity返回的数据
        else if (resultCode == RESULT_OK && requestCode == REQUEST_OPEN_CAMERA_CODE) {
            Uri uri = ImageUtil.getCameraResultUri(activity);
            if (null == uri) {
                return;
            }
            // 调用照片的裁剪功能
            startUCrop(activity, uri);
        }

        // 获取裁剪图片
        else if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            Uri resultUri = UCrop.getOutput(data);
            String path = ImageUtil.getRealPathFromUri(activity, resultUri);
            mHeadPortraitView.onCropImageFinish(path);
        }

        // 裁剪图片错误
        else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }
    }

    /**
     * 修改用户图片
     *
     * @param picPath 图片路径
     */
    @Override
    public void editUserInfo(String picPath) {
        Activity activity = (Activity) this.mHeadPortraitView;
        if (!NetworkStateUtil.isNetworkAvailable(activity)) {
            MessageUtil.showNoNetwork();
            return;
        }
        if (!FileUtil.judeFileExists(picPath)) {
            return;
        }
        File file = new File(picPath);
        Map<String, RequestBody> params = new HashMap<>();
        String fileName = file.getName();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/" + suffix), file);
        params.put("head\"; filename=\"" + file.getName(), requestBody);
        mHeadPortraitModel.editUserInfo(params, this);
    }

    /**
     * 修改用户图片成功时回调
     *
     * @param editUserInfo 修改用户基础信息时服务器端返回的数据
     */
    @Override
    public void onEditUserInfoSuccess(EditUserInfo editUserInfo) {
        mHeadPortraitView.onEditUserInfoSuccess(editUserInfo);
    }

    @Override
    public void onClick(View v) {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mHeadPortraitView.onPopupWindowItemClick(v);
            mPopupWindow.dismiss();
        }
    }
}
