package com.ybw.yibai.common.constants;

import java.io.File;

/**
 * APP里面与文件相关的常量
 *
 * @author sjl
 */
public class Folder {

    /**
     * 主文件夹名称(/storage/emulated/0/Yibai)
     */
    private static final String YIBAI = "Yibai";

    /**
     * 保存文件的文件夹名称(/storage/emulated/0/Yibai/files)
     */
    private static final String FILES = "files";

    /**
     * 保存图片的文件夹名称(/storage/emulated/0/Yibai/files/Picture)
     */
    private static final String PICTURES = "Pictures";

    /**
     * 主文件夹的路径
     * <p>
     * (/storage/emulated/0/Yibai)
     */
    private static final String YIBAI_PATH = File.separator + YIBAI + File.separator;

    /**
     * 保存图片的文件夹的路径
     * <p>
     * (/storage/emulated/0/Yibai/files/Picture)
     */
    public static final String PICTURES_PATH = YIBAI_PATH + FILES + File.separator + PICTURES + File.separator;

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    /**
     * App 数据库的名称
     */
    public static final String APP_DATA_BASE_NAME = "YiBai.db";

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    /**
     * 保存的模拟图片名称前面的前缀
     * 完整名称例如: simulation_UID经过sha1加密后的字符串+时间搓.jpg
     */
    public static final String SIMULATION_IMAGE_PREFIX = "Simulation_";

    /**
     * 保存二维码图片名称前面的前缀
     */
    public static final String QR_CODE_IMAGE_PREFIX = "QrCode_";

    /**
     * 保存分享到其他APP图片名称前面的前缀
     */
    public static final String SHARE_IMAGE_PREFIX = "ShareImage_";
}
