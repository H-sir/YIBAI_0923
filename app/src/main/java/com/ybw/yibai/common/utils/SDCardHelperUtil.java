package com.ybw.yibai.common.utils;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.support.annotation.NonNull;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * SD卡工具类
 *
 * @author sjl
 */
public class SDCardHelperUtil {

    private static final String TAG = "SDCardHelperUtil";

    /**
     * 判断SD卡是否被挂载
     *
     * @return SD卡是否被挂载
     */
    public static boolean isSDCardMounted() {
        // 判断SD卡是否挂载成功,如果Environment.getExternalStorageState()方法的返回值是Environment.MEDIA_MOUNTED代表挂载成功
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取SD卡的根路径
     *
     * @return SD卡的根路径
     */
    public static String getSDCardBaseDir() {
        if (isSDCardMounted()) {
            // Environment.getExternalStorageDirectory()方法是获得SD卡在安卓系统中的挂载路径
            // getAbsolutePath()：返回抽象路径名的绝对路径名字符串
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        return "";
    }

    /**
     * 获取SD卡的完整空间大小,返回MB
     *
     * @return SD卡的完整空间大小, 返回MB
     */
    public static long getSDCardSize() {
        if (isSDCardMounted()) {
            // Android StatFs类：是用于获取存储空间
            // 1：构造函数为StatFs(String path)
            // 2：方法：API<18
            // 				getAvailableBlocks()文件系统中可被应用程序使用的空闲存储区块的数量
            //				getFreeBlocks()文件系统中总的空闲存储区块的数量，包括保留的存储区块（不能被普通应用程序使用）
            //				getBlockCount()文件系统中总的存储区块的数量
            //				getBlockSize()文件系统中每个存储区块的字节数
            // 		   API>=18
            //				getAvailableBlocksLong()文件系统中可被应用程序使用的空闲存储区块的数量
            //				getFreeBytes()文件系统中总的空闲字节数，包括保留的存储区块（不能被普通应用程序使用）
            //				getAvailableBytes()文件系统中可被应用程序使用的空闲字节数
            //				getFreeBlocksLong()文件系统中总的空闲存储区块的数量，包括保留的存储区块（不能被普通应用程序使用）
            //				getTotalBytes()文件系统支持的总的存储字节数
            //				getBlockCountLong()文件系统中总的存储区块的数量
            //				getBlockSizeLong()文件系统中每个存储区块的字节数
            StatFs fs = new StatFs(getSDCardBaseDir());
            long count = fs.getBlockCountLong();
            long size = fs.getBlockSizeLong();
            return count * size / 1024 / 1024;
        }
        return 0;
    }

    /**
     * 获取SD卡的剩余空间大小,返回MB
     *
     * @return SD卡的剩余空间大小, 返回MB
     */
    public static long getSDCardFreeSize() {
        if (isSDCardMounted()) {
            StatFs fs = new StatFs(getSDCardBaseDir());
            long count = fs.getFreeBlocksLong();
            long size = fs.getBlockSizeLong();
            return count * size / 1024 / 1024;
        }
        return 0;
    }

    /**
     * 获取SD卡的可用空间大小,返回MB
     *
     * @return SD卡的可用空间大小, 返回MB
     */
    public static long getSDCardAvailableSize() {
        if (isSDCardMounted()) {
            StatFs fs = new StatFs(getSDCardBaseDir());
            long count = fs.getAvailableBlocksLong();
            long size = fs.getBlockSizeLong();
            return count * size / 1024 / 1024;
        }
        return 0;
    }

    /**
     * 从SD卡获取文件
     *
     * @param filePath 文件路径
     * @return SD卡获取文件
     */
    public static byte[] loadFileFromSDCard(String filePath) {
        BufferedInputStream bis = null;
        ByteArrayOutputStream b = new ByteArrayOutputStream();

        try {
            bis = new BufferedInputStream(new FileInputStream(new File(filePath)));
            byte[] buffer = new byte[1024 * 8];
            int c = 0;
            while ((c = bis.read(buffer)) != -1) {
                b.write(buffer, 0, c);
                b.flush();
            }
            return b.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                if (b != null) {
                    b.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取SD卡公有目录的路径
     *
     * @param type 文件类型
     * @return SD卡公有目录的路径
     */
    public static String getSDCardPublicDir(String type) {
        return Environment.getExternalStoragePublicDirectory(type).getAbsolutePath();
    }

    /**
     * 获取SD卡私有Cache目录的路径
     *
     * @param context 上下文对象
     * @return SD卡私有Cache目录的路径
     */
    public static String getSDCardPrivateCacheDir(@NonNull Context context) {
        return context.getExternalCacheDir().getAbsolutePath();
    }

    /**
     * 获取SD卡私有Files目录的路径
     *
     * @param context 上下文对象
     * @param type    文件类型
     *                DIRECTORY_MUSIC,
     *                DIRECTORY_PODCASTS,
     *                DIRECTORY_RINGTONES,
     *                DIRECTORY_ALARMS,
     *                DIRECTORY_NOTIFICATIONS,
     *                DIRECTORY_PICTURES,
     *                DIRECTORY_MOVIES,
     *                DIRECTORY_DOWNLOADS,
     *                DIRECTORY_DCIM,
     *                DIRECTORY_DOCUMENTS
     * @return SD卡私有Files目录的路径
     */
    public static String getSDCardPrivateFilesDir(@NonNull Context context, String type) {
        return context.getExternalFilesDir(type).getAbsolutePath();
    }

    /**
     * 往SD卡的公有目录下保存文件
     *
     * @param data     要保存的数据
     * @param fileName 文件名称
     * @return 成功或者失败
     */
    public static boolean saveFileToSDCardPublicDir(byte[] data, String fileName) {
        BufferedOutputStream bos = null;
        if (isSDCardMounted()) {
            File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
            try {
                bos = new BufferedOutputStream(new FileOutputStream(new File(file, fileName)));
                bos.write(data);
                bos.flush();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bos != null) {
                        bos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * 往SD卡的私有Files目录下保存文件
     *
     * @param data     要保存的数据
     * @param type     文件类型
     * @param fileName 文件名称
     * @param context  上下文对象
     * @return 成功或者失败
     */
    public static boolean saveFileToSDCardPrivateFilesDir(byte[] data, String type, String fileName, Context context) {
        BufferedOutputStream bos = null;
        if (isSDCardMounted()) {
            // Context.getExternalFilesDir()方法可以获取到 SDCard/Android/data/你的应用的包名/files/目录
            File file = context.getExternalFilesDir(type);
            try {
                bos = new BufferedOutputStream(new FileOutputStream(new File(file, fileName)));
                bos.write(data);
                bos.flush();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bos != null) {
                        bos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * 往SD卡的私有Cache目录下保存文件
     *
     * @param data     要保存的数据
     * @param fileName 文件名称
     * @param context  上下文对象
     * @return 成功或者失败
     */
    public static boolean saveFileToSDCardPrivateCacheDir(byte[] data, String fileName, Context context) {
        BufferedOutputStream bos = null;
        if (isSDCardMounted()) {
            File file = context.getExternalCacheDir();
            try {
                bos = new BufferedOutputStream(new FileOutputStream(new File(file, fileName)));
                bos.write(data);
                bos.flush();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bos != null) {
                        bos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * 往SD卡的私有Cache目录下保存文件
     *
     * @param data     输入流
     * @param fileName 文件名称
     * @param context  上下文对象
     * @return 成功或者失败
     */
    public static boolean saveFileToSDCardPrivateCacheDir(InputStream data, String fileName, Context context) {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        if (isSDCardMounted()) {
            File file = context.getExternalCacheDir();
            try {
                bis = new BufferedInputStream(data);
                bos = new BufferedOutputStream(new FileOutputStream(new File(file, fileName)));
                byte[] buffer = new byte[1024 * 8];
                int c;
                while ((c = bis.read(buffer)) != -1) {
                    bos.write(buffer, 0, c);
                    bos.flush();
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bis != null) {
                        bis.close();
                    }
                    if (bos != null) {
                        bos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
}