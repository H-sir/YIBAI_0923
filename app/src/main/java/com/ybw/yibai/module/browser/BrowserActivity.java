package com.ybw.yibai.module.browser;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ybw.yibai.R;
import com.ybw.yibai.base.BaseActivity;
import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.bean.NetworkType;
import com.ybw.yibai.common.classs.RoundCornersTransformation.CornerType;
import com.ybw.yibai.common.utils.DensityUtil;
import com.ybw.yibai.common.utils.FileUtil;
import com.ybw.yibai.common.utils.ImageUtil;
import com.ybw.yibai.common.utils.MessageUtil;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.utils.QRCodeUtil;
import com.ybw.yibai.module.main.MainActivity;
import com.ybw.yibai.module.picturecase.PictureCaseActivity;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static android.os.Environment.DIRECTORY_PICTURES;
import static android.webkit.WebSettings.LayoutAlgorithm.SINGLE_COLUMN;
import static android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW;
import static com.ybw.yibai.common.constants.Folder.PICTURES_PATH;
import static com.ybw.yibai.common.constants.Folder.QR_CODE_IMAGE_PREFIX;
import static com.ybw.yibai.common.constants.Folder.SHARE_IMAGE_PREFIX;
import static com.ybw.yibai.common.constants.Preferences.CUSTOMER_NAME;
import static com.ybw.yibai.common.constants.Preferences.GYS_DETAILS_URL_TYPE;
import static com.ybw.yibai.common.constants.Preferences.LOCAL_URL_TYPE;
import static com.ybw.yibai.common.constants.Preferences.ORDER_NUMBER;
import static com.ybw.yibai.common.constants.Preferences.ORDER_SHARE_URL_TYPE;
import static com.ybw.yibai.common.constants.Preferences.TAKE_CASE_URL;
import static com.ybw.yibai.common.constants.Preferences.TYPE;
import static com.ybw.yibai.common.constants.Preferences.URL;
import static com.ybw.yibai.common.constants.Preferences.USER_INFO;
import static com.ybw.yibai.common.constants.Preferences.WALLET_URL_TYPE;
import static com.ybw.yibai.common.utils.SDCardHelperUtil.getSDCardPrivateFilesDir;
import static com.ybw.yibai.common.utils.SDCardHelperUtil.isSDCardMounted;

/**
 * 浏览器
 *
 * @author sjl
 * @date 2019/10/14
 */
public class BrowserActivity extends BaseActivity implements View.OnClickListener {

    /**
     * 租摆订单的客户名称
     */
    private String customerName;

    /**
     * 租摆订单的订单号
     */
    private String orderNumber;

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    /**
     * 网页链接地址类型
     * localUrlType 本地URL
     * walletUrlType 我的钱包网页链接URL
     * orderShareUrlType 租摆订单分享链接URL
     * null 其他普通的URL
     */
    private String type;

    /**
     * 网页链接地址
     */
    private String url;

    /**
     * 余额提现地址
     */
    private String takeCashUrl;

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    private View mRootLayout;

    /**
     * 返回
     */
    private ImageView mBackImageView;

    /**
     * 标题
     */
    private TextView mTitleTextView;

    /**
     * 提现(在我的钱包网页链接URL时才显示)
     */
    private TextView mWithdrawDepositTextView;

    /**
     * 分享(在租摆订单分享链接URL时才显示)
     */
    private TextView mShareTextView;

    /**
     * 进度条
     */
    private ProgressBar mProgressBar;

    /**
     *
     */
    private WebView mWebView;

    /**
     * 查看案例
     */
    private Button mSeeTheCaseButton;

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    private ValueCallback<Uri> mUploadMsg;

    private ValueCallback<Uri[]> mFilePathCallback;

    private static final int REQUEST_SELECT_FILE = 1;

    private static final int FILE_CHOOSER_RESULT_CODE = 2;

    @Override
    protected int setLayout() {
        return R.layout.activity_browser;
    }

    @SuppressLint("NewApi")
    @Override
    protected void initView() {
        mRootLayout = findViewById(R.id.rootLayout);
        mBackImageView = findViewById(R.id.backImageView);
        mTitleTextView = findViewById(R.id.titleTextView);
        mWithdrawDepositTextView = findViewById(R.id.withdrawDepositTextView);
        mShareTextView = findViewById(R.id.shareTextView);
        mProgressBar = findViewById(R.id.progressBar);
        mWebView = findViewById(R.id.webView);
        mSeeTheCaseButton = findViewById(R.id.seeTheCaseButton);

        WebSettings webViewSettings = mWebView.getSettings();
//        webViewSettings.setBlockNetworkImage(true);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            webViewSettings.setMixedContentMode(MIXED_CONTENT_ALWAYS_ALLOW);
//        }

        // 不支持手势缩放,默认为true
        webViewSettings.setSupportZoom(false);
        // 将图片调整到适合WebView到大小
        webViewSettings.setUseWideViewPort(true);
        // 支持js
        webViewSettings.setJavaScriptEnabled(true);
        // 开启DOM storage API 功能
        webViewSettings.setDomStorageEnabled(true);
        // 缩放至屏幕到大小
        webViewSettings.setLoadWithOverviewMode(true);
        // 解决图片不显示的问题
        // WebView不启用缓存
        webViewSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webViewSettings.setLayoutAlgorithm(SINGLE_COLUMN);

//        // 设置WebView属性
//        OtherUtil.setWebViewProperty(mWebView);
//        // 设置状态栏成白色的背景,字体颜色为黑色
//        OtherUtil.setStatusBarColor(this, ContextCompat.getColor(this, android.R.color.white));
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        if (null != intent) {
            url = intent.getStringExtra(URL);
            type = intent.getStringExtra(TYPE);
            customerName = intent.getStringExtra(CUSTOMER_NAME);
            orderNumber = intent.getStringExtra(ORDER_NUMBER);
        }
        SharedPreferences mSharedPreferences = getSharedPreferences(USER_INFO, MODE_PRIVATE);
        takeCashUrl = mSharedPreferences.getString(TAKE_CASE_URL, null);
    }

    @Override
    protected void initEvent() {
        mBackImageView.setOnClickListener(this);
        mWithdrawDepositTextView.setOnClickListener(this);
        mShareTextView.setOnClickListener(this);
        mSeeTheCaseButton.setOnClickListener(this);

        mWebView.setWebViewClient(mWebViewClient);
        mWebView.setWebChromeClient(mWebChromeClient);
        if (TextUtils.isEmpty(url)) {
            return;
        }
        if (!TextUtils.isEmpty(type) && type.equals(LOCAL_URL_TYPE)) {
            mTitleTextView.setText("公司简介");
            mWebView.loadDataWithBaseURL(null, url, "text/html", "utf-8", null);
            // 用户从点击首页图片进入本页面,查看公司信息时才显示本按钮
            mSeeTheCaseButton.setVisibility(View.VISIBLE);
        } else if (!TextUtils.isEmpty(type) && type.equals(WALLET_URL_TYPE)) {
            Map<String, String> headMap = new HashMap<>(2);
            headMap.put("uid", String.valueOf(YiBaiApplication.getUid()));
            headMap.put("token", YiBaiApplication.getToken());
            mWebView.loadUrl(url, headMap);
            mWithdrawDepositTextView.setVisibility(View.VISIBLE);
        } else if (!TextUtils.isEmpty(type) && type.equals(ORDER_SHARE_URL_TYPE)) {
            Map<String, String> headMap = new HashMap<>(2);
            headMap.put("uid", String.valueOf(YiBaiApplication.getUid()));
            headMap.put("token", YiBaiApplication.getToken());
            String substring = url.substring(4, url.length());

            mWebView.loadUrl("https" + substring, headMap);
//            mWebView.loadUrl(url, headMap);
//            mWebView.loadUrl("http://mybw.100ybw.com/index/detailed.html?number=2020102398484998");
            mShareTextView.setVisibility(View.VISIBLE);
        } else if (!TextUtils.isEmpty(type) && type.equals(GYS_DETAILS_URL_TYPE)) {
            Map<String, String> headMap = new HashMap<>(2);
            headMap.put("uid", String.valueOf(YiBaiApplication.getUid()));
            headMap.put("token", YiBaiApplication.getToken());
            mWebView.loadUrl(url, headMap);
        } else {
            Map<String, String> headMap = new HashMap<>(2);
            headMap.put("uid", String.valueOf(YiBaiApplication.getUid()));
            headMap.put("token", YiBaiApplication.getToken());
            mWebView.loadUrl(url, headMap);
        }
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

        // 提现(在我的钱包网页链接URL时才显示)
        if (id == R.id.withdrawDepositTextView) {
            if (TextUtils.isEmpty(takeCashUrl)) {
                return;
            }
            Intent intent = new Intent(this, BrowserActivity.class);
            intent.putExtra(URL, takeCashUrl);
            startActivity(intent);
        }

        // 分享
        if (id == R.id.shareTextView) {
            displaySharePopupWindow();
        }

        // 查看案例(用户从点击首页图片进入本页面,查看公司信息时才显示本按钮)
        if (id == R.id.seeTheCaseButton) {
            Intent intent = new Intent(this, PictureCaseActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private WebViewClient mWebViewClient = new WebViewClient() {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
//            mWebView.getSettings().setBlockNetworkImage(false);
//            //判断webview是否加载了，图片资源
//            if (!mWebView.getSettings().getLoadsImagesAutomatically()) {
//                //设置wenView加载图片资源
//                mWebView.getSettings().setLoadsImagesAutomatically(true);
//            }
            super.onPageFinished(view, url);
            String title = view.getTitle();
            if (!TextUtils.isEmpty(title) && !"about:blank".equals(title)) {
                // 获取到的网站title
                mTitleTextView.setText(title);
            }


        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            // 证书已经过期
            if (error.getPrimaryError() == SslError.SSL_EXPIRED
                    || error.getPrimaryError() == SslError.SSL_UNTRUSTED // 不受信任的机构颁发的证书
                    || error.getPrimaryError() == SslError.SSL_DATE_INVALID // 证书的日期是无效的
                    || error.getPrimaryError() == SslError.SSL_INVALID) { // WebView BUG
                handler.proceed(); // 接受所有网站的证书
            } else {
                handler.cancel();
            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    };

    private WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            // 当进度到100%时隐藏ProgressBar,关闭下拉刷新
            if (100 == newProgress) {
                mProgressBar.setVisibility(View.GONE);
            } else {
                mProgressBar.setVisibility(View.VISIBLE);
                // 将进度设置到ProgressBar
                mProgressBar.setProgress(newProgress);
            }
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (!TextUtils.isEmpty(title) && !"about:blank".equals(title)) {
                // 获取到的网站title
                mTitleTextView.setText(view.getTitle());
            }
        }

        // For Android 3.0-
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            mUploadMsg = uploadMsg;
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "File Chooser"), FILE_CHOOSER_RESULT_CODE);
        }

        // For Android 3.0+
        public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
            mUploadMsg = uploadMsg;
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "File Browser"), FILE_CHOOSER_RESULT_CODE);
        }

        // For Android 4.1 only
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            mUploadMsg = uploadMsg;
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "File Browser"), FILE_CHOOSER_RESULT_CODE);
        }

        // For Lollipop 5.0+ Devices
        @Override
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public boolean onShowFileChooser(WebView mWebView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            if (null != mFilePathCallback) {
                mFilePathCallback.onReceiveValue(null);
                mFilePathCallback = null;
            }
            mFilePathCallback = filePathCallback;
            Intent intent = fileChooserParams.createIntent();
            try {
                startActivityForResult(intent, REQUEST_SELECT_FILE);
            } catch (ActivityNotFoundException e) {
                mFilePathCallback = null;
                Toast.makeText(getBaseContext(), "无法打开文件选择器", Toast.LENGTH_LONG).show();
                return false;
            }
            return true;
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (requestCode == REQUEST_SELECT_FILE) {
                if (null == mFilePathCallback) {
                    return;
                }
                mFilePathCallback.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, intent));
                mFilePathCallback = null;
            }
        } else if (requestCode == FILE_CHOOSER_RESULT_CODE) {
            if (null == mUploadMsg) {
                return;
            }
            // Use MainActivity.RESULT_OK if you're implementing WebView inside Fragment
            // Use RESULT_OK only if you're implementing WebView inside an Activity
            Uri result = intent == null || resultCode != MainActivity.RESULT_OK ? null : intent.getData();
            mUploadMsg.onReceiveValue(result);
            mUploadMsg = null;
        } else {
            Toast.makeText(getBaseContext(), "上传图片失败", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 显示"分享"的PopupWindow
     */
    private void displaySharePopupWindow() {
        String qrCodeFilePath = getSDCardPrivateFilesDir(this, DIRECTORY_PICTURES) + File.separator + QR_CODE_IMAGE_PREFIX + orderNumber + ".png";
        if (isSDCardMounted() || !TextUtils.isEmpty(url)) {
            QRCodeUtil.createQRImage(url, 400, 400, null, qrCodeFilePath);
        }

        View view = getLayoutInflater().inflate(R.layout.popup_window_share_quotation_layout, null);
        PopupWindow mPopupWindow = new PopupWindow(view, RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout shareLayout = view.findViewById(R.id.shareLinearLayout);
        ImageView qrCodeImageView = view.findViewById(R.id.qrCodeImageView);
        TextView clientNameTextView = view.findViewById(R.id.clientNameTextView);

        File file = new File(qrCodeFilePath);
        ImageUtil.displayImage(this, qrCodeImageView, file, DensityUtil.dpToPx(this, 2), CornerType.TOP);
        if (!TextUtils.isEmpty(customerName)) {
            clientNameTextView.setText(customerName);
        }

        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);

        // 在弹出PopupWindow设置屏幕透明度
        OtherUtil.setBackgroundAlpha(this, 0.7f);
        // 添加PopupWindow窗口关闭事件
        mPopupWindow.setOnDismissListener(OtherUtil.popupDismissListener(this, 1f));
        mPopupWindow.showAtLocation(mRootLayout, Gravity.CENTER, 0, 0);

        // 延时0.1S
        new Handler().postDelayed(() -> {
            Bitmap bitmap = ImageUtil.viewConversionBitmap(shareLayout);
            if (null == bitmap) {
                return;
            }

            String shareImageFilePath = ImageUtil.saveImage(bitmap, SHARE_IMAGE_PREFIX + orderNumber);
            ImageUtil.shareImage(this, shareImageFilePath);

            view.setOnLongClickListener(v -> {
                String path = FileUtil.createExternalStorageFile(PICTURES_PATH);
                if (TextUtils.isEmpty(path)) {
                    MessageUtil.showMessage(getResources().getString(R.string.failed_to_save_image));
                    return false;
                }
                try {
                    FileUtil.copyFile(shareImageFilePath, path + file.getName());
                    // 发送广播通知相册更新图片
                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    Uri uri = Uri.fromFile(new File(path + file.getName()));
                    intent.setData(uri);
                    sendBroadcast(intent);
                    MessageUtil.showMessage(getResources().getString(R.string.image_saved_to) + path + getResources().getString(R.string.folder));
                } catch (IOException e) {
                    e.printStackTrace();
                    MessageUtil.showMessage(getResources().getString(R.string.failed_to_save_image));
                }
                return false;
            });
        }, 100);
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 避免WebView内存泄露(在Activity销毁WebView)的时候,先让WebView加载null内容,然后移除WebView,再销毁WebView最后置空)
        if (mWebView != null) {
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.clearHistory();
            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.destroy();
            mWebView = null;
        }
    }
}
