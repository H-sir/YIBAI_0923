package com.ybw.yibai.module.about;

import android.app.Activity;
import android.os.Environment;
import android.text.TextUtils;

import com.ybw.yibai.R;
import com.ybw.yibai.base.BasePresenterImpl;
import com.ybw.yibai.common.bean.AppUpdate;
import com.ybw.yibai.common.utils.DownloadApkUtil;
import com.ybw.yibai.common.utils.DownloadUtil;
import com.ybw.yibai.common.utils.EncryptionUtil;
import com.ybw.yibai.common.utils.FileUtil;
import com.ybw.yibai.common.utils.MessageUtil;
import com.ybw.yibai.common.utils.NetworkStateUtil;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.utils.SDCardHelperUtil;
import com.ybw.yibai.common.widget.AppUpdateDialog;
import com.ybw.yibai.module.about.AboutUsContract.AboutUsModel;
import com.ybw.yibai.module.about.AboutUsContract.AboutUsPresenter;
import com.ybw.yibai.module.about.AboutUsContract.AboutUsView;
import com.ybw.yibai.module.about.AboutUsContract.CallBack;

/**
 * 关于我们界面Presenter实现类
 *
 * @author sjl
 */
public class AboutUsPresenterImpl extends BasePresenterImpl<AboutUsView> implements AboutUsPresenter, CallBack {

    /**
     * PresenterImpl 持有 View的接口引用
     */
    private AboutUsView mAboutUsView;

    /**
     * PresenterImpl 持有 Model的接口引用
     */
    private AboutUsModel mAboutUsModel;

    /**
     * 构造方法
     *
     * @param view View的对象
     */
    public AboutUsPresenterImpl(AboutUsView view) {
        super(view);

        // 调用父类的方法获取View的对象
        this.mAboutUsView = getView();
        mAboutUsModel = new AboutUsModelImpl();
    }

    /**
     * 应用更新
     */
    @Override
    public void appUpdate() {
        Activity activity = (Activity) mAboutUsView;
        if (!DownloadUtil.getInstance().canDownload()) {
            // 下载管理程序不可用
            MessageUtil.showMessage(activity.getResources().getString(R.string.download_manager_is_not_available));
            return;
        }
        if (!NetworkStateUtil.isNetworkAvailable(activity)) {
            // 没有网络
            MessageUtil.showNoNetwork();
            return;
        }
        mAboutUsModel.appUpdate(this);
    }

    /**
     * 请求应用更新成功时
     *
     * @param data 数据类型
     */
    @Override
    public void onAppUpdateSuccess(AppUpdate data) {
        mAboutUsView.onAppUpdateSuccess(data);
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
     * 初始化自定义的Dialog
     */
    private void initDownloadAppCustomDialog(final AppUpdate data) {
        Activity activity = (Activity) mAboutUsView;
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
        Activity activity = (Activity) mAboutUsView;
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
