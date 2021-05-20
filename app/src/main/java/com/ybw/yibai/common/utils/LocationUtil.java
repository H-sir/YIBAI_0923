package com.ybw.yibai.common.utils;

import android.content.Context;
import android.location.LocationManager;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.ybw.yibai.base.YiBaiApplication;

/**
 * 百度定位工具类
 *
 * @author sjl
 */
public class LocationUtil {

    private String TAG = "LocationUtil";

    private LocationClient mLocationClient;

    private BDAbstractLocationListener mLocationListener;

    private static volatile LocationUtil instance;

    public static LocationUtil getInstance() {
        if (null == instance) {
            // 使用synchronized防止多个线程同时访问一个对象时发生异常
            synchronized (LocationUtil.class) {
                if (null == instance) {
                    instance = new LocationUtil();
                }
            }
        }
        return instance;
    }

    private LocationUtil() {
        initBaiDuSdk();
    }

    /**
     * 初始化百度地图SDK
     */
    private void initBaiDuSdk() {
        // 声明LocationClient类
        mLocationClient = new LocationClient(YiBaiApplication.getContext());
        // 将配置好的LocationClientOption对象,通过setLocOption方法传递给LocationClient对象使用
        mLocationClient.setLocOption(configurationLocationSdk());
        // 注册监听函数
        mLocationClient.registerLocationListener(mBDAbstractLocationListener);
    }

    /**
     * 配置定位SDK参数
     * http://lbsyun.baidu.com/index.php?title=android-locsdk/guide/get-location/latlng
     * http://lbsyun.baidu.com/index.php?title=android-locsdk/guide/get-location/address
     *
     * @return 定位SDK参数
     */
    private LocationClientOption configurationLocationSdk() {
        LocationClientOption option = new LocationClientOption();
        // 是否需要地址信息
        option.setIsNeedAddress(true);
        return option;
    }

    private BDAbstractLocationListener mBDAbstractLocationListener = new BDAbstractLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (null != mLocationListener) {
                mLocationListener.onReceiveLocation(bdLocation);
            } else {
                throw new IllegalArgumentException("BDAbstractLocationListener 为空");
            }

            // 获取国家
            String country = bdLocation.getCountry();
            // 获取省份
            String province = bdLocation.getProvince();
            // 获取城市
            String city = bdLocation.getCity();
            // 获取区县
            String district = bdLocation.getDistrict();
            // 获取街道信息
            String street = bdLocation.getStreet();
            // 获取详细地址信息
            String address = bdLocation.getAddrStr();
            // 获取经度信息
            double longitude = bdLocation.getLongitude();
            // 获取纬度信息
            double latitude = bdLocation.getLatitude();

            LogUtil.e(TAG, "获取国家: " + country);
            LogUtil.e(TAG, "获取省份: " + province);
            LogUtil.e(TAG, "获取城市: " + city);
            LogUtil.e(TAG, "获取区县: " + district);
            LogUtil.e(TAG, "获取街道信息: " + street);
            LogUtil.e(TAG, "获取详细地址信息: " + address);
            LogUtil.e(TAG, "获取经度信息: " + longitude);
            LogUtil.e(TAG, "获取纬度信息: " + latitude);
        }
    };

    /**
     * 启动定位SDK
     *
     * @param mLocationListener 监听函数
     */
    public void startPositioning(BDAbstractLocationListener mLocationListener) {
        if (null != mLocationListener) {
            this.mLocationListener = mLocationListener;
        } else {
            throw new IllegalArgumentException("BDAbstractLocationListener 为空");
        }
        // 启动定位SDK
        mLocationClient.start();
    }

    /**
     * 关闭定位SDK
     */
    public void stopPositioning() {
        // 关闭定位SDK
        mLocationClient.stop();
    }

    /**
     * 判断GPS是否开启
     *
     * @param context 上下文对象
     * @return true开启, false没有开启
     */
    public static boolean isGpsOpen(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (null != locationManager) {
            // 通过GPS卫星定位,定位级别可以精确到街(通过24颗卫星定位,在室外和空旷的地方定位准确、速度快)
            boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            // 通过WLAN或移动网络(3G/2G)确定的位置(也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物(建筑群或茂密的深林等)密集的地方定位)
            boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            return gps || network;
        } else {
            return false;
        }
    }
}
