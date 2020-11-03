package com.ybw.yibai.common.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.PopupWindow;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.webkit.WebSettings.LayoutAlgorithm.SINGLE_COLUMN;
import static android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW;
import static com.ybw.yibai.common.constants.HttpUrls.PUBLIC_TOKEN;
import static com.ybw.yibai.common.constants.Preferences.AUTHORITY;

/**
 * 其他的一些工具类
 *
 * @author sjl
 */
public class OtherUtil {

    public static final String TAG = "OtherUtil";

    /**
     * 获取签名摘要
     *
     * @param timeStamp  时间搓
     * @param methodName 方法名称
     * @return 签名摘要
     */
    public static String getSign(String timeStamp, String methodName) {
        return EncryptionUtil.md5(methodName + timeStamp + PUBLIC_TOKEN);
    }

    /**
     * Android界面全屏
     *
     * @param activity 要全屏的Activity
     */
    public static void activityFullScreen(@NonNull Activity activity) {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 透明状态栏
     *
     * @param activity 上下文对象
     */
    @SuppressLint("ObsoleteSdkInt")
    public static void transparentStatusBar(Activity activity) {
        // 4.4 透明状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        // 5.0 透明状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * 获取状态栏的高度
     *
     * @param context 上下文对象
     * @return 状态栏的高度
     */
    public static int getStatusBarHeight(@NonNull Context context) {
        int height = 0;
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            height = context.getResources().getDimensionPixelSize(resourceId);
        }
        return height;
    }

    /**
     * 创建一个和"状态栏"一样高的view
     *
     * @param activity activity对象
     * @param barView  一个和"状态栏"一样高的view
     */
    public static void createBarView(Activity activity, @NonNull View barView) {
        // 获取状态栏高度
        int statusBarHeight = getStatusBarHeight(activity);
        ViewGroup.LayoutParams params = barView.getLayoutParams();
        params.height = statusBarHeight;
        barView.setLayoutParams(params);
    }

    /**
     * Android 5.0以上修改状态栏背景
     *
     * @param activity Activity对象
     * @param color    要设置的颜色
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void setStatusBarColor(@NonNull Activity activity, int color) {
        Window window = activity.getWindow();
        // 首先清除默认的FLAG_TRANSLUCENT_STATUS
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(color);
        // 6.0以上（浅色背景的适配）
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //修改为深色，因为我们把状态栏的背景色修改为主题色白色，默认的文字及图标颜色为白色，导致看不到了。
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    /**
     * Android6.0修改状态栏文字颜色
     *
     * @param activity Activity对象
     * @param dark     是否设置为深色的颜色
     */
    public static void setNativeLightStatusBar(Activity activity, boolean dark) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = activity.getWindow().getDecorView();
            if (dark) {
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            }
        }
    }

    /**
     * 判断导航栏是否显示
     *
     * @param activity 上下文对象
     * @return false 不显示, true 显示;
     */
    public static boolean isNavigationBarShow(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Display display = activity.getWindowManager().getDefaultDisplay();
            Point size = new Point();
            Point realSize = new Point();
            display.getSize(size);
            display.getRealSize(realSize);
            return realSize.y != size.y;
        } else {
            boolean menu = ViewConfiguration.get(activity).hasPermanentMenuKey();
            boolean back = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
            if (menu || back) {
                return false;
            } else {
                return true;
            }
        }
    }

    /**
     * 获取导航栏的高度
     *
     * @param activity 上下文对象
     * @return 导航栏的高度
     */
    public static int getNavigationBarHeight(Activity activity) {
        // 如果导航栏不显示,将返回导航栏的高度为0
        if (!isNavigationBarShow(activity)) {
            return 0;
        }
        Resources resources = activity.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }

    /**
     * 开启手机震动功能(前提是在清单文件中添加了<uses-permission android:name="android.permission.VIBRATE" />权限)
     *
     * @param context      调用该方法的Context实例
     * @param milliseconds 震动的时长,单位是毫秒
     */
    public static void vibrate(@NonNull Context context, long milliseconds) {
        // 获取震动效果的系统服务
        Vibrator vib = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        // 检测手机硬件是否有振动器
        if (null != vib && vib.hasVibrator()) {
            // 开启震动(无规律震动)
            vib.vibrate(milliseconds);
        }
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比,来判断是否隐藏键盘
     *
     * @param view  当前得到焦点的View
     * @param event 事件
     * @return true隐藏键盘, false不隐藏键盘
     */
    public static boolean isShouldHideInput(View view, MotionEvent event) {
        if (view != null && (view instanceof EditText)) {
            int[] leftTop = {0, 0};
            // 获取输入框当前的位置
            view.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + view.getHeight();
            int right = left + view.getWidth();
            if (event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域,保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    /**
     * 关闭输入法
     *
     * @param activity Activity对象
     */
    public static void hideSoftInput(@NonNull Activity activity) {
        // 获取输入法Manager对象
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null != inputMethodManager && null != activity.getCurrentFocus()) {
            // 关闭输入法
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param activity Activity对象
     * @param bgAlpha  屏幕透明度0.0-1.0 1表示完全不透明
     */
    public static void setBackgroundAlpha(@NonNull Activity activity, float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        activity.getWindow().setAttributes(lp);
    }

    /**
     * 当弹出的PopupWindow关闭时,将背景透明度改回来
     *
     * @param activity Activity对象
     * @param bgAlpha  屏幕透明度0.0-1.0 1表示完全不透明
     */
    @NonNull
    public static PopupWindow.OnDismissListener popupDismissListener(final Activity activity, final float bgAlpha) {
        return () -> setBackgroundAlpha(activity, bgAlpha);
    }

    /**
     * 跳转到当前应用详情页面
     *
     * @param context 上下文对象
     */
    @SuppressLint("ObsoleteSdkInt")
    public static void openDetailSettingIntent(Context context) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.FROYO) {
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            intent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        context.startActivity(intent);
    }

    /**
     * 跳转到当前应用安装页面
     *
     * @param activity activity对象
     * @param path     APK的保存在本地的路径
     */
    public static void openInstallIntent(Activity activity, String path) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri;
        // Android7.0以上URI
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // 通过FileProvider创建一个content类型的Uri
            uri = FileProvider.getUriForFile(activity, AUTHORITY, new File(path));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            // Android8.0以上URI
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                boolean hasInstallPermission = (activity.getPackageManager().canRequestPackageInstalls());
                if (!hasInstallPermission) {
                    // 请求安装未知应用来源的权限
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES}, 0);
                }
            }
        } else {
            uri = Uri.fromFile(new File(path));
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        activity.startActivity(intent);
    }

    /**
     * 设置WebView属性
     *
     * @param webView 要设置的WebView对象
     */
    @SuppressLint("SetJavaScriptEnabled")
    public static void setWebViewProperty(@NonNull WebView webView) {
        WebSettings webViewSettings = webView.getSettings();
        if (null != webViewSettings) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                webViewSettings.setMixedContentMode(MIXED_CONTENT_ALWAYS_ALLOW);
            }

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
            webViewSettings.setBlockNetworkImage(false);
            // WebView不启用缓存
            webViewSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
            webViewSettings.setLayoutAlgorithm(SINGLE_COLUMN);
        }
    }

    /**
     * 设置String中不同字段的字体大小和颜色
     *
     * @param context         上下文对象
     * @param wholeString     全部文字
     * @param highlightString 改变颜色的文字
     * @param size            改变颜色的文字的大小
     * @param color           改变颜色的文字的颜色
     * @return 不同字段的字体颜色
     */
    @NonNull
    public static SpannableStringBuilder stringFormat(Context context, String wholeString, String highlightString, int size, int color) {
        SpannableStringBuilder builder = new SpannableStringBuilder(wholeString);

        int start = 0, end = 0;
        if (!TextUtils.isEmpty(wholeString) && !TextUtils.isEmpty(highlightString)) {
            if (wholeString.contains(highlightString)) {
                // 返回highlightString字符串在wholeString字符串中第一次出现处的索引
                start = wholeString.indexOf(highlightString);
                end = start + highlightString.length();
            }
        }

        // 设置文本大小
        AbsoluteSizeSpan sizeSpan = new AbsoluteSizeSpan(DensityUtil.spToPx(context, size));
        // 设置文字颜色
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(color);

        builder.setSpan(sizeSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(colorSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return builder;
    }

    /**
     * 判断color字符串是否为color字符串
     *
     * @param colorString color字符串
     * @return true为color字符串, 不是color字符串
     */
    public static boolean isColor(String colorString) {
        try {
            Color.parseColor(colorString);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取Double类型数字小数位数
     *
     * @param balance Double类型数字
     * @return 小数位数
     */
    public static int getNumberDecimalDigits(Double balance) {
        int digits = 0;
        String balanceStr = Double.toString(balance);
        int indexOf = balanceStr.indexOf(".");
        if (indexOf > 0) {
            digits = balanceStr.length() - 1 - indexOf;
        }
        return digits;
    }

    /**
     * 将一个List按照步长分成n个list
     *
     * @param source   源集合
     * @param stepSize 步长
     * @return n个list
     */
    public static <T> List<List<T>> splitList(List<T> source, int stepSize) {
        if (null == source || source.isEmpty()) {
            return Collections.emptyList();
        }

        // 源集合大小
        int sourceSize = source.size();
        // 计算出商
        int quotient = sourceSize / stepSize;
        // 计算出余数
        int remainder = sourceSize % stepSize;
        List<List<T>> result = new ArrayList<>();

        // source拆分后的数量
        int count;
        if (remainder == 0) {
            count = quotient;
        } else {
            count = quotient + 1;
        }
        if (count == 1) {
            result.add(source);
            return result;
        }
        // 偏移量
        int offset = 0;
        for (int i = 0; i < count; i++) {
            List<T> value;
            if (i == count - 1) {
                value = source.subList(offset, sourceSize);
            } else {
                value = source.subList(offset, offset + stepSize);
            }
            offset += stepSize;
            result.add(value);
        }
        return result;
    }

    /**
     * 判断两个集合的元素是否完全相等
     * https://blog.csdn.net/Ecloss/article/details/86154344
     *
     * @param list1 集合1
     * @param list2 集合2
     * @return true元素是相等的 false元素是不相等的
     */
    @SuppressLint("UseSparseArrays")
    public static boolean checkListElementEqual(List<Integer> list1, List<Integer> list2) {
        if (null == list1 || list1.size() == 0) {
            return false;
        }
        if (null == list2 || list2.size() == 0) {
            return false;
        }
        if (list1.size() != list2.size()) {
            return false;
        }
        Map<Integer, Integer> map = new HashMap<>();
        for (int i : list1) {
            map.put(i, 1);
        }
        for (int i : list2) {
            Integer j = map.get(i);
            if (null != j) {
                continue;
            }
            return false;
        }
        return true;
    }
}
