package com.ybw.yibai.common.helper;

import android.content.Context;
import android.content.SharedPreferences;

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
     * 场景图片数量
     */
    private static final String Key_PhotoNum = "PhotoNum";
    private static int mPhotoNum;

    /**
     * 城市
     */
    private static final String Key_City = "City";
    private static String mCity;

    public static int getPhotoNum(Context context) {
        mPhotoNum = readPhotoNum(context);
        return mPhotoNum;
    }

    private static int readPhotoNum(Context context) {
        SharedPreferences pref = context.getSharedPreferences(Scene_Name, MODE_PRIVATE);
        return pref.getInt(Key_PhotoNum, 0);
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

    public static void savePhotoNum(Context context, int mPhotoNum) {
        savePhotoNumInfo(context, mPhotoNum);
    }

    private static synchronized boolean savePhotoNumInfo(Context context, int mPhotoNum) {
        SharedPreferences pref = context.getSharedPreferences(Scene_Name, MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.putInt(Key_PhotoNum, mPhotoNum);
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

}
