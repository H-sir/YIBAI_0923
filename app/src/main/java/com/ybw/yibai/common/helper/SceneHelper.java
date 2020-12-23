package com.ybw.yibai.common.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.ybw.yibai.base.YiBaiApplication;

import static android.content.Context.MODE_PRIVATE;

/**
 * <pre>
 *     author : HKR
 *     time   : 2020/09/11
 *     desc   :
 * </pre>
 */
public class SceneHelper {
    private static final String Scene_Name = "Scene";

    /**
     * 商品植物ID
     */
    private static final String Key_ProductSkuId = "ProductSkuId";
    private static int mProductSkuId;
    /**
     * 商品盆ID
     */
    private static final String Key_AugmentedProductSkuId = "AugmentedProductSkuId";
    private static int mAugmentedProductSkuId;

    /**
     * 商品规格Id
     */
    private static final String Key_SpecTypeId = "SpecTypeId";
    private static int mSpecTypeId;

    /**
     * 城市
     */
    private static final String Key_City = "City";
    private static String mCity;

    /**
     * 进货位置新增
     */
    private static final String Key_QuotationLocationNum = "QuotationLocationNum";
    private static int mQuotationLocationNum = 1;

    /**
     * 场景新增
     */
    private static final String Key_SceneNum = "SceneNum";
    private static int mSceneNum = 1;

    /**
     * 天数
     */
    private static final String Key_SceneProductTime = "SceneProductTime";
    private static int mSceneProductTime = -1;

    public static int getSceneProductTime(Context context) {
        mSceneProductTime = readSceneProductTime(context);
        return mSceneProductTime;
    }

    private static int readSceneProductTime(Context context) {
        SharedPreferences pref = context.getSharedPreferences(Scene_Name, MODE_PRIVATE);
        return pref.getInt(Key_SceneProductTime, -1);
    }

    public static int getSceneNum(Context context) {
        mSceneNum = readSceneNum(context);
        return mSceneNum;
    }

    private static int readSceneNum(Context context) {
        SharedPreferences pref = context.getSharedPreferences(Scene_Name, MODE_PRIVATE);
        return pref.getInt(Key_SceneNum, 1);
    }

    public static int getQuotationLocationNum(Context context) {
        mQuotationLocationNum = readQuotationLocationNum(context);
        return mQuotationLocationNum;
    }

    private static int readQuotationLocationNum(Context context) {
        SharedPreferences pref = context.getSharedPreferences(Scene_Name, MODE_PRIVATE);
        return pref.getInt(Key_QuotationLocationNum, 1);
    }

    public static int getProductSkuId(Context context) {
        mProductSkuId = readProductSkuId(context);
        return mProductSkuId;
    }

    private static int readProductSkuId(Context context) {
        SharedPreferences pref = context.getSharedPreferences(Scene_Name, MODE_PRIVATE);
        return pref.getInt(Key_ProductSkuId, 0);
    }

    public static int getAugmentedProductSkuId(Context context) {
        mAugmentedProductSkuId = readAugmentedProductSkuId(context);
        return mAugmentedProductSkuId;
    }

    private static int readAugmentedProductSkuId(Context context) {
        SharedPreferences pref = context.getSharedPreferences(Scene_Name, MODE_PRIVATE);
        return pref.getInt(Key_AugmentedProductSkuId, 0);
    }

    public static int getSpecTypeId(Context context) {
        mSpecTypeId = readSpecTypeId(context);
        return mSpecTypeId;
    }

    private static int readSpecTypeId(Context context) {
        SharedPreferences pref = context.getSharedPreferences(Scene_Name, MODE_PRIVATE);
        return pref.getInt(Key_SpecTypeId, 0);
    }

    public static String getCity(Context context) {
        mCity = readCity(context);
        if (mCity.length() > 5)
            return "全国";
        return mCity;
    }

    private static String readCity(Context context) {
        SharedPreferences pref = context.getSharedPreferences(Scene_Name, MODE_PRIVATE);
        return pref.getString(Key_City, "全国");
    }

    public static void saveSpecTypeId(Context context, int mSpecTypeId) {
        saveSpecTypeIdInfo(context, mSpecTypeId);
    }

    private static synchronized boolean saveSpecTypeIdInfo(Context context, int mSpecTypeId) {
        SharedPreferences pref = context.getSharedPreferences(Scene_Name, MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.putInt(Key_SpecTypeId, mSpecTypeId);
        return edit.commit();
    }

    public static void saveProductId(Context context, int mProductSkuId, int mAugmentedProductSkuId) {
        saveProductIdInfo(context, mProductSkuId, mAugmentedProductSkuId);
    }

    private static synchronized boolean saveProductIdInfo(Context context, int mProductSkuId, int mAugmentedProductSkuId) {
        SharedPreferences pref = context.getSharedPreferences(Scene_Name, MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.putInt(Key_ProductSkuId, mProductSkuId);
        edit.putInt(Key_AugmentedProductSkuId, mAugmentedProductSkuId);
        return edit.commit();
    }

    public static void saveCity(Context context, String mCity) {
        saveCityInfo(context, mCity);
    }

    private static synchronized boolean saveCityInfo(Context context, String mCity) {
        SharedPreferences pref = context.getSharedPreferences(Scene_Name, MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.putString(Key_City, mCity);
        return edit.commit();
    }

    public static void saveQuotationLocationNum(Context context, int mQuotationLocationNum) {
        saveQuotationLocationNumInfo(context, mQuotationLocationNum);
    }

    private static synchronized boolean saveQuotationLocationNumInfo(Context context, int mQuotationLocationNum) {
        SharedPreferences pref = context.getSharedPreferences(Scene_Name, MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.putInt(Key_QuotationLocationNum, mQuotationLocationNum);
        return edit.commit();
    }

    public static void saveSceneNum(Context context, int mSceneNum) {
        saveSceneNumInfo(context, mSceneNum);
    }

    private static synchronized boolean saveSceneNumInfo(Context context, int mSceneNum) {
        SharedPreferences pref = context.getSharedPreferences(Scene_Name, MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.putInt(Key_SceneNum, mSceneNum);
        return edit.commit();
    }

    public static String saveSceneNum(Context context) {
        String sceneName = "位置" + getSceneNum(context);
        saveSceneNum(context, getSceneNum(context) + 1);
        return sceneName;
    }

    private static synchronized boolean saveSceneProductTimeInfo(Context context, int mSceneProductTime) {
        SharedPreferences pref = context.getSharedPreferences(Scene_Name, MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.putInt(Key_SceneProductTime, mSceneProductTime);
        return edit.commit();
    }

    public static void saveSceneProductTime(Context context, int mSceneProductTime) {
        saveSceneProductTimeInfo(context, mSceneProductTime);
    }
}
