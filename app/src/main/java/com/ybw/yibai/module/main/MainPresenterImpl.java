package com.ybw.yibai.module.main;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.ybw.yibai.R;
import com.ybw.yibai.base.BasePresenterImpl;
import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.bean.AppUpdate;
import com.ybw.yibai.common.bean.ProductScreeningParam;
import com.ybw.yibai.common.bean.SystemParameter;
import com.ybw.yibai.common.bean.UserInfo;
import com.ybw.yibai.common.utils.DownloadApkUtil;
import com.ybw.yibai.common.utils.EncryptionUtil;
import com.ybw.yibai.common.utils.FileUtil;
import com.ybw.yibai.common.utils.ImageUtil;
import com.ybw.yibai.common.utils.LogUtil;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.utils.PermissionsUtil;
import com.ybw.yibai.common.utils.SDCardHelperUtil;
import com.ybw.yibai.common.utils.XUtilsUtil;
import com.ybw.yibai.common.widget.AppUpdateDialog;
import com.ybw.yibai.module.browser.BrowserActivity;
import com.ybw.yibai.module.main.MainContract.CallBack;
import com.ybw.yibai.module.main.MainContract.MainModel;
import com.ybw.yibai.module.main.MainContract.MainPresenter;
import com.ybw.yibai.module.main.MainContract.MainView;

import org.xutils.DbManager;

import java.io.File;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static com.ybw.yibai.common.constants.Encoded.REQUEST_OPEN_CAMERA_CODE;
import static com.ybw.yibai.common.constants.Encoded.REQUEST_OPEN_PHOTOS_CODE;
import static com.ybw.yibai.common.constants.Preferences.TOKEN;
import static com.ybw.yibai.common.constants.Preferences.UID;
import static com.ybw.yibai.common.constants.Preferences.URL;
import static com.ybw.yibai.common.constants.Preferences.USER_INFO;
import static com.ybw.yibai.common.constants.Preferences.USER_VIP_URL;
import static com.ybw.yibai.common.utils.FileUtil.judeFileExists;
import static com.ybw.yibai.common.utils.ImageUtil.createPhotoPath;

/**
 * 主界面Presenter实现类
 *
 * @author sjl
 * @date 2018/12/10
 */
public class MainPresenterImpl extends BasePresenterImpl<MainView> implements MainPresenter, CallBack {

    private final String TAG = "MainPresenterImpl";

    /**
     * 显示升级会员的PopupWindow
     */
    private PopupWindow mPopupWindow;

    /**
     * PresenterImpl 持有 View的接口引用
     */
    private MainView mMainView;

    /**
     * PresenterImpl 持有 Model的接口引用
     */
    private MainModel mMainModel;

    /**
     * 构造方法
     *
     * @param view View的对象
     */
    public MainPresenterImpl(MainView view) {
        super(view);

        // 调用父类的方法获取View的对象
        this.mMainView = getView();
        mMainModel = new MainModelImpl();
    }

    /**
     * 初始化XUtils数据库和一些其他数据
     */
    @Override
    public void initDataAndXUtilsDataBase() {
        Activity activity = (Activity) mMainView;
        // 获取用户登陆成功后保存的UID
        int uid = activity.getSharedPreferences(USER_INFO, MODE_PRIVATE).getInt(UID, 0);
        if (0 != uid) {
            YiBaiApplication.setUid(uid);
        }

        // 获取用户登陆成功后保存的TOKEN
        String token = activity.getSharedPreferences(USER_INFO, MODE_PRIVATE).getString(TOKEN, null);
        if (!TextUtils.isEmpty(token)) {
            YiBaiApplication.setToken(token);
        }

        try {
            // 初始化XUtils数据库
            DbManager dbManager = XUtilsUtil.getInstance().initXUtilsDataBase();
            YiBaiApplication.setDbManager(dbManager);
        } catch (Exception e) {
            LogUtil.e(TAG, "初始化XUtils数据库错误: " + e.getMessage());
        }
    }

    /**
     * 查找是否有默认场景,如果没有就创建一个默认创建
     */
    @Override
    public void findIfThereIsDefaultScene() {
        mMainModel.findIfThereIsDefaultScene();
    }

    /**
     * 应用更新
     */
    @Override
    public void appUpdate() {
        mMainModel.appUpdate(this);
    }

    /**
     * 请求应用更新成功时回调
     *
     * @param appUpdate 请求应用更新成功时服务器端返回的数据
     */
    @Override
    public void onAppUpdateSuccess(AppUpdate appUpdate) {
        mMainView.onAppUpdateSuccess(appUpdate);
    }

    /**
     * 下载新版本App
     *
     * @param data 服务器新版本信息
     */
    @Override
    public void downloadApp(AppUpdate data) {
        initDownloadAppCustomDialog(data);
    }

    /**
     * 获取用户信息
     */
    @Override
    public void getUserInfo() {
        mMainModel.getUserInfo(this);
    }

    /**
     * 在获取用户信息成功时回调
     *
     * @param userInfo 用户信息
     */
    @Override
    public void onGetUserInfoSuccess(UserInfo userInfo) {
        mMainView.onGetUserInfoSuccess(userInfo);
    }

    /**
     * 获取系统参数
     */
    @Override
    public void getSystemParameter() {
        mMainModel.getSystemParameter(this);
    }

    /**
     * 在获取系统参数成功时回调
     *
     * @param systemParameter 系统参数
     */
    @Override
    public void onGetSystemParameterSuccess(SystemParameter systemParameter) {
        mMainView.onGetSystemParameterSuccess(systemParameter);
    }

    /**
     * 获取产品筛选参数
     */
    @Override
    public void getProductScreeningParam() {
        mMainModel.getProductScreeningParam(this);
    }

    /**
     * 在获取产品筛选参数成功时回调
     *
     * @param productScreeningParam 获取产品筛选参数时服务器端返回的数据
     */
    @Override
    public void onGetProductScreeningParamSuccess(ProductScreeningParam productScreeningParam) {
        mMainView.onGetProductScreeningParamSuccess(productScreeningParam);
    }

    /**
     * 申请权限
     *
     * @param permissions 要申请的权限列表
     */
    @Override
    public void applyPermissions(String[] permissions) {
        Activity activity = (Activity) mMainView;
        // android6.0 以上才进行动态权限申请
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!PermissionsUtil.checkPermissionAllGranted(activity, permissions)) {
                // 开始提交请求权限
                PermissionsUtil.startRequestPermission(activity, permissions);
            } else {
                // 已经获取全部权限
                mMainView.applyPermissionsResults(true);
            }
        } else {
            // 6.0以下,默认获取全部权限
            mMainView.applyPermissionsResults(true);
        }
    }

    /**
     * 打开相机
     */
    @Override
    public void openCamera() {
        Activity activity = (Activity) mMainView;
        ImageUtil.openCamera(activity, createPhotoPath());
    }

    /**
     * 打开相册
     */
    @Override
    public void openPhotoAlbum() {
        Activity activity = (Activity) mMainView;
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Activity activity = (Activity) mMainView;
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
            mMainView.returnsTheImageReturnedFromTheCameraOrAlbum(file);
        }

        // 获得系统相机返回的数据
        if (resultCode == RESULT_OK && requestCode == REQUEST_OPEN_CAMERA_CODE) {
            // 获得相机拍照后图片保存的路径
            String path = ImageUtil.getCropPhotoPath();
            if (!judeFileExists(path)) {
                return;
            }
            File file = new File(path);
            mMainView.returnsTheImageReturnedFromTheCameraOrAlbum(file);
        }
    }

    /**
     * 显示升级会员的PopupWindow
     *
     * @param rootLayout View根布局
     */
    @Override
    public void displayUpdateVipPopupWindow(View rootLayout) {
        Activity activity = (Activity) mMainView;
        if (mPopupWindow == null) {
            View view = activity.getLayoutInflater().inflate(R.layout.popup_window_update_vip_layout, null);
            mPopupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            ImageView updateImmediatelyImageView = view.findViewById(R.id.updateImmediatelyImageView);
            ImageView closeImageView = view.findViewById(R.id.closeImageView);

            updateImmediatelyImageView.setOnClickListener(v -> {
                SharedPreferences mSharedPreferences = activity.getSharedPreferences(USER_INFO, MODE_PRIVATE);
                String userVipUrl = mSharedPreferences.getString(USER_VIP_URL, null);
                if (!TextUtils.isEmpty(userVipUrl)) {
                    Intent intent = new Intent(activity, BrowserActivity.class);
                    intent.putExtra(URL, userVipUrl);
                    activity.startActivity(intent);
                }
                if (mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                }
            });
            closeImageView.setOnClickListener(v -> {
                if (mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                }
            });

            mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.setFocusable(true);

            // 设置一个动画效果
            mPopupWindow.setAnimationStyle(R.style.PopupWindow_Anim);

            // 在弹出PopupWindow设置屏幕透明度
            OtherUtil.setBackgroundAlpha(activity, 0.6f);
            // 添加PopupWindow窗口关闭事件
            mPopupWindow.setOnDismissListener(OtherUtil.popupDismissListener(activity, 1f));
            mPopupWindow.showAtLocation(rootLayout, Gravity.CENTER, 0, 0);
        } else {
            // 在弹出PopupWindow设置屏幕透明度
            OtherUtil.setBackgroundAlpha(activity, 0.6f);
            // 添加PopupWindow窗口关闭事件
            mPopupWindow.setOnDismissListener(OtherUtil.popupDismissListener(activity, 1f));
            mPopupWindow.showAtLocation(rootLayout, Gravity.CENTER, 0, 0);
        }
    }

    /**
     * 初始化自定义的Dialog
     */
    private void initDownloadAppCustomDialog(final AppUpdate data) {
        Activity activity = (Activity) mMainView;
        if (null != activity) {
            String content = data.getData().getContent();
            String htmlString = EncryptionUtil.base64DecodeString(content);
            OtherUtil.setBackgroundAlpha(activity, 0.6f);
            AppUpdateDialog appUpdateDialog = new AppUpdateDialog(activity);
            appUpdateDialog.setAppUpdateContentHtmlString(htmlString);
            appUpdateDialog.setYesOnclickListener(() -> {
                OtherUtil.setBackgroundAlpha(activity, 1f);
                appUpdateDialog.dismiss();
                download(data);
            });
            appUpdateDialog.setNoOnclickListener(() -> {
                OtherUtil.setBackgroundAlpha(activity, 1f);
                appUpdateDialog.dismiss();
            });
            appUpdateDialog.show();
        }
    }

    /**
     * 下载
     *
     * @param data 服务器端返回的App更新数据
     */
    private void download(AppUpdate data) {
        // 判断SD卡是否被挂载
        if (!SDCardHelperUtil.isSDCardMounted() || SDCardHelperUtil.getSDCardAvailableSize() < 15) {
            return;
        }
        Activity activity = (Activity) mMainView;
        // 下载地址
        String downloadUrl = data.getData().getApk();
        // APP名称
        String name = downloadUrl.substring(downloadUrl.lastIndexOf('/') + 1);
        // 获取APK的保存在本地的路径
        String path = SDCardHelperUtil.getSDCardBaseDir() + "/" + Environment.DIRECTORY_DOWNLOADS + "/" + name;
        // 判断APK文件是否存在
        if (!TextUtils.isEmpty(path) && FileUtil.judeFileExists(path)) {
            OtherUtil.openInstallIntent(activity, path);
        } else {
            DownloadApkUtil.downloadApk(activity, name, downloadUrl, Environment.DIRECTORY_DOWNLOADS, activity.getResources().getString(R.string.yibai_new_version), activity.getResources().getString(R.string.new_version));
        }
    }
}
