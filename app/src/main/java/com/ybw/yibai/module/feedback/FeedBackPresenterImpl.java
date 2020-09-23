package com.ybw.yibai.module.feedback;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ybw.yibai.R;
import com.ybw.yibai.base.BasePresenterImpl;
import com.ybw.yibai.common.bean.FeedBack;
import com.ybw.yibai.common.bean.FeedBackImage;
import com.ybw.yibai.common.utils.ImageUtil;
import com.ybw.yibai.common.utils.MessageUtil;
import com.ybw.yibai.common.utils.NetworkStateUtil;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.utils.PermissionsUtil;
import com.ybw.yibai.module.feedback.FeedBackContract.CallBack;
import com.ybw.yibai.module.feedback.FeedBackContract.FeedBackModel;
import com.ybw.yibai.module.feedback.FeedBackContract.FeedBackPresenter;
import com.ybw.yibai.module.feedback.FeedBackContract.FeedBackView;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static android.app.Activity.RESULT_OK;
import static android.os.Environment.DIRECTORY_PICTURES;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.ybw.yibai.common.constants.Encoded.REQUEST_OPEN_CAMERA_CODE;
import static com.ybw.yibai.common.constants.Encoded.REQUEST_OPEN_PHOTOS_CODE;
import static com.ybw.yibai.common.utils.FileUtil.judeFileExists;
import static com.ybw.yibai.common.utils.SDCardHelperUtil.getSDCardPrivateFilesDir;

/**
 * 意见反馈界面Presenter实现类
 *
 * @author sjl
 * @date 2019/10/24
 */
public class FeedBackPresenterImpl extends BasePresenterImpl<FeedBackView> implements FeedBackPresenter, CallBack, View.OnClickListener {

    /**
     * 选择拍照还是从相册中选择的PopupWindow
     */
    private PopupWindow mPopupWindow;

    /**
     * PresenterImpl 持有 View的接口引用
     */
    private FeedBackView mFeedBackView;

    /**
     * PresenterImpl 持有 Model的接口引用
     */
    private FeedBackModel mFeedBackModel;

    /**
     * 构造方法
     *
     * @param view View的对象
     */
    public FeedBackPresenterImpl(FeedBackView view) {
        super(view);

        // 调用父类的方法获取View的对象
        this.mFeedBackView = getView();
        mFeedBackModel = new FeedBackModelImpl();
    }

    /**
     * 申请权限
     *
     * @param permissions 要申请的权限列表
     */
    @Override
    public void applyPermissions(String[] permissions) {
        Activity activity = ((Activity) mFeedBackView);
        // android6.0 以上才进行动态权限申请
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!PermissionsUtil.checkPermissionAllGranted(activity, permissions)) {
                // 开始提交请求权限
                PermissionsUtil.startRequestPermission(activity, permissions);
            } else {
                // 已经获取全部权限
                mFeedBackView.applyPermissionsResults(true);
            }
        } else {
            // 6.0以下,默认获取全部权限
            mFeedBackView.applyPermissionsResults(true);
        }
    }

    /**
     * 初始化选择拍照还是从相册中选择的PopupWindow
     *
     * @param rootLayout View根布局
     */
    @Override
    public void displayTakePhotoOrAlbumPopupWindow(View rootLayout) {
        Activity activity = ((Activity) mFeedBackView);
        if (null == mPopupWindow) {
            View view = activity.getLayoutInflater().inflate(R.layout.popup_window_taking_pictures_layout, null);
            mPopupWindow = new PopupWindow(view, MATCH_PARENT, WRAP_CONTENT);

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
     * 打开相机
     */
    @Override
    public void openCamera() {
        Activity activity = ((Activity) mFeedBackView);
        ImageUtil.openCamera(activity, null);
    }

    /**
     * 打开相册
     */
    @Override
    public void openPhotoAlbum() {
        Activity activity = ((Activity) mFeedBackView);
        ImageUtil.openPhotoAlbum(activity);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Activity activity = ((Activity) mFeedBackView);
        // 获得系统相册返回的数据
        if (resultCode == RESULT_OK && requestCode == REQUEST_OPEN_PHOTOS_CODE) {
            Uri uri = data.getData();
            if (null == uri) {
                return;
            }
            // 根据Uri获取图片的绝对路径
            String path = ImageUtil.getRealPathFromUri(activity, uri);
            if (!judeFileExists(path)) {
                return;
            }
            File file = new File(path);
            String targetDir = getSDCardPrivateFilesDir(activity, DIRECTORY_PICTURES);
            Luban.with(activity).load(file).ignoreBy(150).setTargetDir(targetDir).setCompressListener(mOnCompressListener).launch();
        }

        // 获得系统相机返回的数据
        if (resultCode == RESULT_OK && requestCode == REQUEST_OPEN_CAMERA_CODE) {
            // 获得相机拍照后图片保存的路径
            String path = ImageUtil.getCropPhotoPath();
            if (!judeFileExists(path)) {
                return;
            }
            File file = new File(path);
            String targetDir = getSDCardPrivateFilesDir(activity, DIRECTORY_PICTURES);
            Luban.with(activity).load(file).ignoreBy(150).setTargetDir(targetDir).setCompressListener(mOnCompressListener).launch();
        }
    }

    /**
     * 意见反馈
     *
     * @param desc               建议或问题描述
     * @param contact            联系手机或者联系邮箱
     * @param mFeedBackImageList 描述图片
     */
    @Override
    public void feedBack(String desc, String contact, List<FeedBackImage> mFeedBackImageList) {
        Activity activity = ((Activity) mFeedBackView);
        if (!NetworkStateUtil.isNetworkAvailable(activity)) {
            MessageUtil.showNoNetwork();
            return;
        }
        if (TextUtils.isEmpty(desc) && TextUtils.isEmpty(contact)) {
            MessageUtil.showMessage(activity.getResources().getString(R.string.feedback_and_contact_information_cannot_be_empty));
            return;
        }
        if (TextUtils.isEmpty(desc)) {
            MessageUtil.showMessage(activity.getResources().getString(R.string.feedback_cannot_be_empty));
            return;
        }
        if (TextUtils.isEmpty(contact)) {
            MessageUtil.showMessage(activity.getResources().getString(R.string.contact_cannot_be_empty));
            return;
        }

        if (mFeedBackImageList.size() <= 1) {
            mFeedBackModel.feedBack(desc, contact, null, this);
            return;
        }

        MultipartBody.Part[] parts = new MultipartBody.Part[mFeedBackImageList.size()];
        for (int i = 0; i < mFeedBackImageList.size(); i++) {
            FeedBackImage feedBackImage = mFeedBackImageList.get(i);
            int type = feedBackImage.getType();
            if (1 == type) {
                continue;
            }
            File file = feedBackImage.getFile();
            if (null == file || !file.exists()) {
                continue;
            }
            String fileName = file.getName();
            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/" + suffix), file);
            parts[i] = MultipartBody.Part.createFormData("pic[]", file.getName(), requestBody);
        }
        mFeedBackModel.feedBack(desc, contact, parts, this);
    }

    /**
     * 在意见反馈成功时回调
     *
     * @param feedBack 意见反馈数据
     */
    @Override
    public void onFeedBackSuccess(FeedBack feedBack) {
        mFeedBackView.onFeedBackSuccess(feedBack);
    }

    /**
     * 鲁班图片压缩算法回调
     * <p>
     * https://github.com/Curzibn/Luban
     */
    private OnCompressListener mOnCompressListener = new OnCompressListener() {
        /**
         * 压缩开始前调用,可以在方法内启动loading UI
         */
        @Override
        public void onStart() {

        }

        /**
         * 压缩成功后调用,返回压缩后的图片文件
         *
         * @param file 压缩后的图片文件
         */
        @Override
        public void onSuccess(File file) {
            mFeedBackView.returnsTheImageReturnedFromTheCameraOrAlbum(file);
        }

        /**
         * 当压缩过程出现问题时调用
         *
         * @param e 异常
         */
        @Override
        public void onError(Throwable e) {

        }
    };

    @Override
    public void onClick(View v) {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
        mFeedBackView.onPopupWindowItemClick(v);
    }
}
