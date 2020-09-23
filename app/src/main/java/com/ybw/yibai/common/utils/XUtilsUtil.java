package com.ybw.yibai.common.utils;

import com.ybw.yibai.base.YiBaiApplication;

import org.xutils.DbManager;
import org.xutils.x;

import java.io.File;

import static android.os.Environment.DIRECTORY_DOCUMENTS;
import static com.ybw.yibai.common.constants.Folder.APP_DATA_BASE_NAME;

/**
 * xUtils3工具类
 *
 * @author sjl
 */
public class XUtilsUtil {

    /**
     * xUtils3工具类唯一的实例
     */
    private static volatile XUtilsUtil instance;

    private XUtilsUtil() {

    }

    public static XUtilsUtil getInstance() {
        if (null == instance) {
            // 使用synchronized防止多个线程同时访问一个对象时发生异常
            synchronized (XUtilsUtil.class) {
                if (null == instance) {
                    instance = new XUtilsUtil();
                }
            }
        }
        return instance;
    }

    /**
     * 初始化XUtils数据库
     */
    public DbManager initXUtilsDataBase() {
        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
                // 数据库保存的位置
                .setDbDir(new File(SDCardHelperUtil.getSDCardPrivateFilesDir(YiBaiApplication.getContext(), DIRECTORY_DOCUMENTS)))
                // 创建数据库的名称
                .setDbName(APP_DATA_BASE_NAME)
                // 数据库版本号
                .setDbVersion(16)
                .setDbOpenListener(db -> {
                    // 开启WAL,对写入加速提升巨大
                    db.getDatabase().enableWriteAheadLogging();
                });

        // 得到数据库管理对象
        return x.getDb(daoConfig);
    }
}
