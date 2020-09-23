package com.ybw.yibai.common.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;

import java.util.UUID;

/**
 * APP相关的工具类
 *
 * @author sjl
 */
public class AppUtil {

    /**
     * 获取Android设备ID号
     *
     * @param context 上下文对象
     */
    @NonNull
    @SuppressLint({"MissingPermission", "HardwareIds"})
    public static String getDeviceId(@NonNull Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        // 唯一的设备ID,如GSM网络的IMEI,CDMA网络的MEID / ESN,可能返回null（API文档的描述）
        // 只对手机设备有效,对于不在手机网络的设备,会返回null
        // 获取到的Device ID值,即使设备恢复出厂设置也不会改变
        // 需要<uses-permission android:name="android.permission.READ_PHONE_STATE" />权限
        // 某些设备的Device ID实现有bug,会返回0或*
        String deviceID = "" + tm.getDeviceId();
        // Android ID 64位的十六进制字符串,设备首次启动时会随时生成
        // 如果设备恢复了出厂设置,这个值可能会改变
        // 设备root了之后,这个值可以手动修改
        // Android 2.2发现bug,部分设备具有相同Android ID（9774d56d682e549c）,模拟器的Android ID也是这个
        // 这个值有时会为null,一般不推荐使用
        String androidId = "" + Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        // 返回SIM卡的序列号,需要添加<uses-permission android:name="android.permission.READ_PHONE_STATE" />权限

        String serialNumber = "" + tm.getSimSerialNumber();

        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) deviceID.hashCode() << 32) | serialNumber.hashCode());
        String deviceId = deviceUuid.toString();

        return deviceId;
    }

    /**
     * 获取APP版本Name
     *
     * @param context 上下文对象
     * @return APP版本Name
     */
    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }

    /**
     * 获取APP版本Code
     *
     * @param context 上下文对象
     * @return APP版本Code
     */
    public static int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    /**
     * 获取APP包名
     *
     * @param context 上下文对象
     * @return APP包名
     */
    public static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;

        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pi;
    }

    /**
     * 获取手机厂商
     *
     * @return 手机厂商
     */
    public static String getPhoneBrand() {
        return android.os.Build.BRAND;
    }

    /**
     * 获取手机型号
     *
     * @return 手机型号
     */
    public static String getPhoneModel() {
        return android.os.Build.MODEL;
    }
}
