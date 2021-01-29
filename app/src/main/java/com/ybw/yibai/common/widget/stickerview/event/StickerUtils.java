package com.ybw.yibai.common.widget.stickerview.event;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;

import com.ybw.yibai.R;
import com.ybw.yibai.common.utils.FileUtil;
import com.ybw.yibai.common.utils.MessageUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static com.ybw.yibai.common.constants.Folder.PICTURES_PATH;
import static java.lang.Math.round;

/**
 * "贴纸"工具类
 *
 * @author sjl
 */
public class StickerUtils {

    public static void trapToRect(@NonNull RectF rectF, @NonNull float[] array) {
        rectF.set(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);

        for (int i = 1; i < array.length; i += 2) {
            float x = round(array[i - 1] * 10) / 10.f;
            float y = round(array[i] * 10) / 10.f;
            rectF.left = (x < rectF.left) ? x : rectF.left;
            rectF.top = (y < rectF.top) ? y : rectF.top;
            rectF.right = (x > rectF.right) ? x : rectF.right;
            rectF.bottom = (y > rectF.bottom) ? y : rectF.bottom;
        }

        rectF.sort();
    }

    public static File saveImage(Context context, @NonNull File file, @NonNull Bitmap bmp) {
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();

            MediaStore.Images.Media.insertImage(context.getContentResolver(), bmp, "title", "description");

            String path = FileUtil.createExternalStorageFile(PICTURES_PATH);
            try {
                FileUtil.copyFile(file.getPath(), path + file.getName());
                //保存图片后发送广播通知更新数据库
                Uri uri = Uri.fromFile(file);
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}
