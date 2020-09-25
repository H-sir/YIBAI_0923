package com.ybw.yibai.common.utils;

import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * 与文件相关的类
 *
 * @author sjl
 */
public class FileUtil {

    /**
     * 存放我们需要查找的文件的集合
     */
    private static List<File> mFileList;

    /**
     * 判断文件是否存在
     *
     * @param filePath 文件路径
     * @return true 存在,false 不存在
     */
    public static boolean judeFileExists(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return false;
        }
        File file = new File(filePath);
        return file.exists();
    }

    /**
     * 递归模糊查询我们需要查找的文件
     *
     * @param file     文件所在的目录
     * @param fileName 需要模糊查找以头部为fileName开始的文件名称
     * @return 我们需要查找的文件的集合
     */
    public static List<File> indistinctSearchFile(File file, String fileName) {
        mFileList = new ArrayList<>();
        indistinctFindFile(file, fileName);
        if (null != mFileList && mFileList.size() > 0) {
            // 开始排序
            Collections.sort(mFileList, new Comparator<File>() {
                @Override
                public int compare(File f1, File f2) {
                    if (f1.isDirectory() && f2.isFile()) {
                        return -1;
                    }
                    if (f1.isFile() && f2.isDirectory()) {
                        return 1;
                    }
                    // 按照名称有小到大排列
                    // return f1.getName().compareTo(f2.getName());
                    // 按照名称有大到小排列
                    return f2.getName().compareTo(f1.getName());
                }
            });
        }
        return mFileList;
    }

    /**
     * 递归模糊查询我们需要查找的文件,然后将其添加到集合中
     *
     * @param file     文件所在的目录
     * @param fileName 需要模糊查找以头部为fileName开始的文件名称
     */
    private static void indistinctFindFile(@NonNull File file, String fileName) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (null != files) {
                for (File childFile : files) {
                    indistinctFindFile(childFile, fileName);
                }
            }
        } else {
            // startsWith(String prefix)测试这个字符串是否以指定的前缀开始
            if (file.getName().startsWith(fileName)) {
                mFileList.add(file);
            }
        }
    }

    /**
     * 复制单个文件
     *
     * @param oldPath String 原文件路径 如：c:/qq.txt
     * @param newPath String 复制后路径 如：f:/qq.txt
     */
    public static void copyFile(String oldPath, String newPath) throws IOException {
        int byteRead;
        File oldFile = new File(oldPath);
        // 文件存在时
        if (oldFile.exists()) {
            // 读入原文件
            InputStream inStream = new FileInputStream(oldPath);
            FileOutputStream fs = new FileOutputStream(newPath);
            byte[] buffer = new byte[2048];
            while ((byteRead = inStream.read(buffer)) != -1) {
                fs.write(buffer, 0, byteRead);
            }
            inStream.close();
        }
    }

    /**
     * 删除单个文件
     *
     * @param pathName 被删除文件的路径名称
     * @return 单个文件删除成功返回true, 否则返回false
     */
    public static boolean deleteFile(String pathName) {
        boolean flag = false;
        File file = new File(pathName);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            flag = file.delete();
        }
        return flag;
    }

    public interface DeleteFileCallback {

        /**
         * 在删除文件时回调
         *
         * @param currentNumber 还剩余的文件数量
         */
        void onDeleteFile(int currentNumber);
    }

    /**
     * 删除一个文件的集合
     *
     * @param fileList           文件的集合
     * @param deleteFileCallback 在删除文件时回调的接口
     */
    public static void deleteFile(List<File> fileList, DeleteFileCallback deleteFileCallback) {
        if (null == fileList || fileList.size() == 0) {
            return;
        }
        Iterator<File> iterator = fileList.iterator();
        while (iterator.hasNext()) {
            File file = iterator.next();
            // 路径为文件且不为空则进行删除
            if (file.isFile() && file.exists() && file.delete()) {
                iterator.remove();
            }
            deleteFileCallback.onDeleteFile(fileList.size());
        }
    }

    /**
     * 创建外部存储文件夹
     *
     * @param filePath 文件夹路径
     */
    public static String createExternalStorageFile(String filePath) {
        // 判断SD卡是否挂载成功,如果Environment.getExternalStorageState()方法的返回值是Environment.MEDIA_MOUNTED代表挂载成功
        if (!SDCardHelperUtil.isSDCardMounted()) {
            return null;
        } else {
            // 获得SD卡在安卓系统中的挂载路径
            File sdDir = Environment.getExternalStorageDirectory();
            String path = sdDir.getPath() + filePath;
            File file = new File(path);
            // 判断文件夹目录是否存在
            if (!file.exists()) {
                // 如果不存在则创建
                boolean mkdirs = file.mkdirs();
            }
            return path;
        }
    }

    public static void main(String[] arg) {
        int iiiIndex = 0;
        for (int i = 0; i < 3; i++) {
            iiiIndex++;
            int index = 0;
            int num = 3;
            for (int j = 0; j < 10; j++) {
                if(iiiIndex == 3){
                    if (num * (iiiIndex - 1) <= index && index < 10) {
                        System.out.print(j);
                        index++;
                    } else {
                        index++;
                        continue;
                    }
                }else{
                    if (num * (iiiIndex - 1) <= index && index < num * iiiIndex) {
                        System.out.print(j);
                        index++;
                    } else {
                        index++;
                        continue;
                    }
                }
            }
        }
    }
}
