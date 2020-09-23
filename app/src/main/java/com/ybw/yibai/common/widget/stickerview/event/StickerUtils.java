package com.ybw.yibai.common.widget.stickerview.event;

import android.graphics.Bitmap;
import android.graphics.RectF;
import android.support.annotation.NonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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

    public static File saveImage(@NonNull File file, @NonNull Bitmap bmp) {
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}
