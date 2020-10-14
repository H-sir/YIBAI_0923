package com.ybw.yibai.common.constants;

/**
 * 这个类存放所有的编码表
 *
 * @author sjl
 */
public class Encoded {

    /**********
     * 编号:
     * 错误码表
     **********/

    /**
     * 请求网络数据成功
     */
    public static final int CODE_SUCCEED = 200;

    /**********
     * 网络类型编码
     **********/

    /**
     * 没有网络
     */
    public static final int NETWORK_TYPE_INVALID = 0;

    /**
     * 网络类型是: "WIFI"
     */
    public static final int NETWORK_TYPE_WIFI = 1;

    /**
     * 网络类型是: "2G"
     */
    public static final int NETWORK_TYPE_2G = 2;

    /**
     * 网络类型是: "3G"
     */
    public static final int NETWORK_TYPE_3G = 3;

    /**
     * 网络类型是: "4G"
     */
    public static final int NETWORK_TYPE_4G = 4;

    /**
     * 未知网络类型
     */
    public static final int NETWORK_TYPE_UNKNOWN = 5;

    /**********
     * 请求码编码
     **********/

    /**
     * 请求码(动态申请权限的请求码）
     */
    public static final int REQUEST_PERMISSIONS_CODE = 14;

    /**
     * 请求码(动态申请位置权限的请求码）
     */
    public static final int REQUEST_LOCATION_PERMISSIONS_CODE = 15;

    /**
     * 请求码(这是打开相册的Activity的请求码)
     */
    public static final int REQUEST_OPEN_PHOTOS_CODE = 16;

    /**
     * 请求码(这是打开相机的Activity的请求码)
     */
    public static final int REQUEST_OPEN_CAMERA_CODE = 17;

    /**
     * 请求码(这是打开图片裁剪的Activity的请求码)
     */
    public static final int REQUEST_OPEN_CROP_CODE = 18;

    /**
     * 请求码(申请打开GPS的请求码）
     */
    public static final int REQUEST_OPEN_GPS_CODE = 19;

    /**
     * 请求码(设计详情返回的请求码)
     */
    public static final int REQUEST_DESIGN_DETAILS_CODE = 20;
    /**
     * 请求码(设计详情返回的请求码)
     */
    public static final String REQUEST_DESIGN_DETAILS = "DesignDetails";

    /**********
     * 其他
     **********/
}
