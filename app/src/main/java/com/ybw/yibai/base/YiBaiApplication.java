package com.ybw.yibai.base;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.view.Display;

import com.huxin.common.network.HttpClient;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.ybw.yibai.common.bean.ProductScreeningParam.DataBean;
import com.ybw.yibai.common.bean.SystemParameter;
import com.ybw.yibai.common.utils.LogUtil;

import org.xutils.DbManager;
import org.xutils.x;

import java.util.List;

/**
 * @author sjl
 */
public class YiBaiApplication extends Application {

    /**
     * 用户登录成功时获得的Uid
     */
    private static int uid;

    /**
     * 报价客户的id
     */
    private static int cid;

    /**
     * 用户登录成功时获得的Token
     */
    private static String token;

    /**
     * 角色(除了service以外,进货改价需验证密码)
     */
    private static String roleName;

    /**
     * 结算货币符号
     */
    private static String currencySymbol;

    /**
     * 用户(员工)点击"报价"界面,需要修改价格时,验证查看密码是否通过
     */
    private static boolean authorization;

    /**
     * 是否显示APP底部导航栏,默认显示
     * <p>
     * true:  显示
     * false: 隐藏
     */
    private static boolean display = true;

    /**
     * 在搭配界面是否开启背景,默认关闭
     * <p>
     * true:  打开
     * false: 关闭
     */
    private static boolean enableBackground;

    private static int windowWidth  =0;

    private static int windowHeight = 0;

    /**
     * 全局的上下文
     */
    @SuppressLint("StaticFieldLeak")
    private static Context context;

    /**
     * 微信API是第三方app和微信通信的openApi接口
     */
    private static IWXAPI iWXAPI;

    /**
     * XUtils数据库Manager对象
     */
    private static DbManager dbManager;

    /**
     * 系统参数
     */
    private static SystemParameter systemParameter;

    /**
     * 产品筛选参数集合
     */
    private static List<DataBean> productScreeningParamList;

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();
        LogUtil.showLog(true);
        initBugly();
        initXUtils();
        initWeChat();
        initUmeng();

        MultiDex.install(this);

        HttpClient.Companion.init();
        HttpClient.Companion.getInstance().setHostUrl("http://api.100ybw.com/");
        HttpClient.Companion.getInstance().setOpenLOG(true);
        //HttpClient.Companion.getInstance().setTokenInterceptor(HttpInterceptor(context!!));
        HttpClient.Companion.getInstance().initialize();
    }

    /**
     * 初始化腾讯Bugly
     */
    private void initBugly() {
        // 第二个参数为注册时申请的AppId
        CrashReport.initCrashReport(context, "327948e610", false);
    }

    /**
     * 初始化xUtils
     */
    protected void initXUtils() {
        x.Ext.init(this);
    }

    /**
     * 初始化微信SDK
     */
    private void initWeChat() {
        // 应用在微信开放平台上的AppID
        String appId = "wx08cd98ecf42af1d7";
        iWXAPI = WXAPIFactory.createWXAPI(this, appId, true);
        // 将该app注册到微信
        iWXAPI.registerApp(appId);
    }

    /**
     * 初始化友盟SDK
     */
    private void initUmeng() {
        String appKey = "5e184cc8cb23d224f70006d7";
        String channel = "YiBaiOfficialSite";
        // 选用AUTO页面采集模式
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);
        // 设置Log开关
        UMConfigure.setLogEnabled(false);
        // 设置日志加密
        UMConfigure.setEncryptEnabled(true);
        // 初始化SDK
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, null);
        // UMConfigure.init(this, appKey, channel, UMConfigure.DEVICE_TYPE_PHONE, null);
    }



    public static int getUid() {
        return uid;
    }

    public static void setUid(int uid) {
        YiBaiApplication.uid = uid;
    }

    public static int getCid() {
        return cid;
    }

    public static void setCid(int cid) {
        YiBaiApplication.cid = cid;
    }

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        YiBaiApplication.token = token;
    }

    public static String getRoleName() {
        return roleName;
    }

    public static void setRoleName(String roleName) {
        YiBaiApplication.roleName = roleName;
    }

    public static String getCurrencySymbol() {
        return currencySymbol;
    }

    public static void setCurrencySymbol(String currencySymbol) {
        YiBaiApplication.currencySymbol = currencySymbol;
    }

    public static boolean isAuthorization() {
        return authorization;
    }

    public static void setAuthorization(boolean authorization) {
        YiBaiApplication.authorization = authorization;
    }

    public static int getWindowWidth() {
        return windowWidth;
    }

    public static void setWindowWidth(int windowWidth) {
        YiBaiApplication.windowWidth = windowWidth;
    }

    public static int getWindowHeight() {
        return windowHeight;
    }

    public static void setWindowHeight(int windowHeight) {
        YiBaiApplication.windowHeight = windowHeight;
    }

    public static boolean isDisplay() {
        return display;
    }

    public static void setDisplay(boolean display) {
        YiBaiApplication.display = display;
    }

    public static boolean isEnableBackground() {
        return enableBackground;
    }

    public static void setEnableBackground(boolean enableBackground) {
        YiBaiApplication.enableBackground = enableBackground;
    }

    public static Context getContext() {
        return context;
    }

    public static IWXAPI getIWXAPI() {
        return iWXAPI;
    }

    public static DbManager getDbManager() {
        return dbManager;
    }

    public static void setDbManager(DbManager dbManager) {
        YiBaiApplication.dbManager = dbManager;
    }

    public static SystemParameter getSystemParameter() {
        return systemParameter;
    }

    public static void setSystemParameter(SystemParameter systemParameter) {
        YiBaiApplication.systemParameter = systemParameter;
    }

    public static List<DataBean> getProductScreeningParamList() {
        return productScreeningParamList;
    }

    public static void setProductScreeningParamList(List<DataBean> productScreeningParamList) {
        YiBaiApplication.productScreeningParamList = productScreeningParamList;
    }
}
