package com.ybw.yibai.common.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.yalantis.ucrop.UCrop;
import com.ybw.yibai.R;
import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.classs.RoundCornersTransformation;
import com.ybw.yibai.common.classs.RoundCornersTransformation.CornerType;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static android.os.Environment.DIRECTORY_PICTURES;
import static com.ybw.yibai.base.YiBaiApplication.getContext;
import static com.ybw.yibai.common.constants.Encoded.REQUEST_OPEN_CAMERA_CODE;
import static com.ybw.yibai.common.constants.Encoded.REQUEST_OPEN_PHOTOS_CODE;
import static com.ybw.yibai.common.constants.Preferences.AUTHORITY;
import static com.ybw.yibai.common.utils.FileUtil.judeFileExists;
import static com.ybw.yibai.common.utils.SDCardHelperUtil.getSDCardPrivateFilesDir;

/**
 * 图片工具类
 *
 * @author sjl
 */
public class ImageUtil {

    private static final String TAG = "ImageUtil";

    /**
     * 照片保存路径
     */
    private static String photoPath;

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    /**
     * 开始下载的位置
     */
    private static int position;

    /**
     * 本次要下载的图片总数
     */
    private static int sumTotal;

    /**
     * 当前下载图片成功的数量
     */
    private static int successesAmount;

    /**
     * 当前下载图片失败的数量
     */
    private static int failuresAmount;

    /**
     * 当前下载成功或者失败的数量
     */
    private static int completedAmount;

    /**
     * 下载完成的图片
     * TODO 这里存放大量的图片会导致OOM
     */
    private static List<Bitmap> mBitmapList = new ArrayList<>();

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    /**
     * 创建一个默认照片保存路径
     *
     * @return 照片保存路径
     */
    @NonNull
    public static String createPhotoPath() {
        String directory = getSDCardPrivateFilesDir(YiBaiApplication.getContext(), DIRECTORY_PICTURES);
        return directory + File.separator + "Photo_" + TimeUtil.getTimeStamp() + ".jpg";
    }

    /**
     * 默认照片保存路径
     *
     * @return 照片保存路径
     */
    public static String getCropPhotoPath() {
        if (judeFileExists(photoPath)) {
            return photoPath;
        }
        String directory = getSDCardPrivateFilesDir(YiBaiApplication.getContext(), DIRECTORY_PICTURES);
        return directory + File.separator + "Photo.jpg";
    }

    /**
     * Activity界面打开相机
     *
     * @param activity  Activity对象
     * @param photoPath 照片保存路径
     */
    public static void openCamera(Activity activity, String photoPath) {
        File file;
        if (TextUtils.isEmpty(photoPath)) {
            ImageUtil.photoPath = null;
            // 如果照片保存路径为null,就创建一个默认的照片保存路径
            file = new File(getCropPhotoPath());
        } else {
            ImageUtil.photoPath = photoPath;
            // 否则就按指定路径保存
            file = new File(photoPath);
        }
        if (!file.getParentFile().exists()) {
            boolean r = file.getParentFile().mkdirs();
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Android7.0以上URI
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // 添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            // 通过FileProvider创建一个content类型的Uri
            Uri uri = FileProvider.getUriForFile(activity, AUTHORITY, file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        } else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        }
        try {
            activity.startActivityForResult(intent, REQUEST_OPEN_CAMERA_CODE);
        } catch (ActivityNotFoundException e) {
            // 打开相机异常
            MessageUtil.showMessage(activity.getResources().getString(R.string.open_camera_exception));
        }
    }

    /**
     * Fragment界面打开相机
     *
     * @param fragment  Fragment对象
     * @param photoPath 照片保存路径
     */
    public static void openCamera(@NonNull Fragment fragment, String photoPath) {
        Activity activity = fragment.getActivity();
        if (null == activity) {
            return;
        }
        File file;
        if (TextUtils.isEmpty(photoPath)) {
            ImageUtil.photoPath = null;
            // 如果照片保存路径为null,就创建一个默认的照片保存路径
            file = new File(getCropPhotoPath());
        } else {
            ImageUtil.photoPath = photoPath;
            // 否则就按指定路径保存
            file = new File(photoPath);
        }
        if (!file.getParentFile().exists()) {
            boolean r = file.getParentFile().mkdirs();
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Android7.0以上URI
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // 添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            // 通过FileProvider创建一个content类型的Uri
            Uri uri = FileProvider.getUriForFile(activity, AUTHORITY, file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        } else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        }
        try {
            fragment.startActivityForResult(intent, REQUEST_OPEN_CAMERA_CODE);
        } catch (ActivityNotFoundException e) {
            // 打开相机异常
            MessageUtil.showMessage(fragment.getResources().getString(R.string.open_camera_exception));
        }
    }

    /**
     * 获得系统相机Activity返回的Uri
     * (/storage/emulated/0/Android/data/com.ybw.yibai/files/Pictures/Photo.jpg) 转成
     * (content://media/external/images/media/99)
     *
     * @param context Context对象
     */
    @Nullable
    public static Uri getCameraResultUri(@NonNull Context context) {
        File imageFile = new File(ImageUtil.getCropPhotoPath());
        String filePath = imageFile.getAbsolutePath();

        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath},
                null);

        if (null != cursor && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            cursor.close();
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    /**
     * Activity界面打开相册
     *
     * @param activity Activity对象
     */
    public static void openPhotoAlbum(@NonNull Activity activity) {
        try {
            // 跳转到系统相册界面
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            // 相片类型
            intent.setType("image/*");
            activity.startActivityForResult(intent, REQUEST_OPEN_PHOTOS_CODE);
        } catch (ActivityNotFoundException e) {
            // 打开相册异常
            MessageUtil.showMessage(activity.getResources().getString(R.string.open_album_exception));
        }
    }

    /**
     * Fragment界面打开相册
     *
     * @param fragment Fragment对象
     */
    public static void openPhotoAlbum(@NonNull Fragment fragment) {
        try {
            // 跳转到系统相册界面
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            // 相片类型
            intent.setType("image/*");
            fragment.startActivityForResult(intent, REQUEST_OPEN_PHOTOS_CODE);
        } catch (ActivityNotFoundException e) {
            MessageUtil.showMessage(fragment.getResources().getString(R.string.open_album_exception));
        }
    }

    /**
     * 调用系统照片的裁剪功能
     */
    @NonNull
    public static Intent invokeSystemCrop(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // 添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(uri, "image/*");

        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);

        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", 500);
        intent.putExtra("outputY", 500);

        intent.putExtra("return-data", false);
        intent.putExtra("noFaceDetection", true);

        File out = new File(getCropPhotoPath());
        if (!out.getParentFile().exists()) {
            boolean r = out.getParentFile().mkdirs();
        }

        // 设置裁剪后的照片保存的位置
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(out));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

        return intent;
    }

    /**
     * Activity界面启动UCrop图片裁剪
     *
     * @param activity Activity 对象
     * @param uri      用于图像裁剪的Uri
     */
    public static void startUCrop(Activity activity, @NonNull Uri uri) {
        String directory = getSDCardPrivateFilesDir(YiBaiApplication.getContext(), DIRECTORY_PICTURES);
        String outPath = directory + File.separator + "Photo.jpg";
        File out = new File(outPath);
        if (!out.getParentFile().exists()) {
            boolean r = out.getParentFile().mkdirs();
        }

        UCrop uCrop = UCrop.of(uri, Uri.fromFile(out));

        UCrop.Options options = new UCrop.Options();
        // 设置状态栏颜色
        options.setStatusBarColor(Color.WHITE);
        // 设置Toolbar颜色
        options.setToolbarColor(Color.WHITE);
        // 是否隐藏底部容器,默认显示
        options.setHideBottomControls(true);
        // UCrop配置
        uCrop.withOptions(options);

        uCrop.useSourceImageAspectRatio();
        uCrop.withAspectRatio(1, 1);
        uCrop.start(activity);
    }

    /**
     * Fragment界面启动UCrop图片裁剪
     *
     * @param activity Activity 对象
     * @param fragment Fragment 对象
     * @param uri      用于图像裁剪的Uri
     */
    public static void startUCrop(Activity activity, Fragment fragment, @NonNull Uri uri) {
        String directory = getSDCardPrivateFilesDir(YiBaiApplication.getContext(), DIRECTORY_PICTURES);
        String outPath = directory + File.separator + "Photo.jpg";
        File out = new File(outPath);
        if (!out.getParentFile().exists()) {
            boolean r = out.getParentFile().mkdirs();
        }

        UCrop uCrop = UCrop.of(uri, Uri.fromFile(out));

        UCrop.Options options = new UCrop.Options();
        // 设置状态栏颜色
        options.setStatusBarColor(Color.WHITE);
        // 设置Toolbar颜色
        options.setToolbarColor(Color.WHITE);
        // 是否隐藏底部容器,默认显示
        options.setHideBottomControls(true);
        // UCrop配置
        uCrop.withOptions(options);

        uCrop.useSourceImageAspectRatio();
        uCrop.withAspectRatio(1, 1);
        uCrop.start(activity, fragment);
    }

    /**
     * 根据Uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    public static String getRealPathFromUri(Context context, Uri uri) {
        int sdkVersion = Build.VERSION.SDK_INT;
        if (sdkVersion >= 19) {
            return getRealPathFromUriAboveApi19(context, uri);
        } else {
            return getRealPathFromUriBelowAPI19(context, uri);
        }
    }

    /**
     * 适配api19及以上,根据uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    @SuppressLint("NewApi")
    private static String getRealPathFromUriAboveApi19(Context context, Uri uri) {
        String filePath = null;
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // 如果是document类型的uri,则通过document id来进行处理
            String documentId = DocumentsContract.getDocumentId(uri);
            if (isMediaDocument(uri)) {
                // 使用':'分割
                String id = documentId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = {id};
                filePath = getDataColumn(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection, selectionArgs);
            } else if (isDownloadsDocument(uri)) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(documentId));
                filePath = getDataColumn(context, contentUri, null, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是content类型的Uri
            filePath = getDataColumn(context, uri, null, null);
        } else if ("file".equals(uri.getScheme())) {
            // 如果是file类型的Uri,直接获取图片对应的路径
            filePath = uri.getPath();
        }
        return filePath;
    }

    /**
     * 适配api19以下(不包括api19),根据uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    private static String getRealPathFromUriBelowAPI19(Context context, Uri uri) {
        return getDataColumn(context, uri, null, null);
    }

    /**
     * 获取数据库表中的 _data列,即返回Uri对应的文件路径
     */
    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        String path = null;
        String[] projection = new String[]{MediaStore.Images.Media.DATA};
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(projection[0]);
                path = cursor.getString(columnIndex);
            }
        } catch (Exception e) {
            if (cursor != null) {
                cursor.close();
            }
        }
        return path;
    }

    /**
     * @param uri 要检查的Uri
     * @return Uri权限是否为MediaProvider
     */
    private static boolean isMediaDocument(@NonNull Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri 要检查的Uri
     * @return Uri权限是否为DownloadsProvider
     */
    private static boolean isDownloadsDocument(@NonNull Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    /**
     * 显示图片
     *
     * @param context   上下文大小
     * @param imageView ImageView对象
     * @param file      图片
     */
    public static void displayImage(Context context, ImageView imageView, File file) {
        Glide.with(context).load(file).into(imageView);
    }

    /**
     * 显示圆形图片
     *
     * @param context   上下文大小
     * @param imageView ImageView对象
     * @param bitmap    Bitmap图片
     */
    public static void displayImage(Context context, ImageView imageView, Bitmap bitmap) {
        Glide.with(context).load(bitmap).into(imageView);
    }

    /**
     * 显示图片
     *
     * @param context   上下文大小
     * @param imageView ImageView对象
     * @param imageUrl  图片url地址
     */
    public static void displayImage(Context context, ImageView imageView, String imageUrl) {
        Glide.with(context).load(imageUrl).into(imageView);
    }

    /**
     * 显示圆形图片
     *
     * @param context   上下文大小
     * @param imageView ImageView对象
     * @param bitmap    Bitmap图片
     */
    public static void displayRoundImage(Context context, ImageView imageView, Bitmap bitmap) {
        RequestOptions requestOptions = RequestOptions.circleCropTransform();
        Glide.with(context).load(bitmap).apply(requestOptions).into(imageView);
    }

    /**
     * 显示圆形图片
     *
     * @param context   上下文大小
     * @param imageView ImageView对象
     * @param imageUrl  图片url地址
     */
    public static void displayRoundImage(Context context, ImageView imageView, String imageUrl) {
        RequestOptions requestOptions = RequestOptions.circleCropTransform();
        Glide.with(context).load(imageUrl).apply(requestOptions).into(imageView);
    }

    /**
     * 显示圆角图片
     * 解决RoundedCorners跟CenterCrop冲突问题解决
     *
     * @param context   上下文大小
     * @param imageView ImageView对象
     * @param file      图片
     * @param angle     圆角的角度
     */
    public static void displayImage(Context context, ImageView imageView, File file, int angle) {
        CenterCrop centerCrop = new CenterCrop();
        RoundedCorners roundedCorners = new RoundedCorners(angle);
        RequestOptions transforms = new RequestOptions().transforms(centerCrop, roundedCorners);
        Glide.with(context).load(file).apply(transforms).into(imageView);
    }

    /**
     * 显示圆角图片
     *
     * @param context   上下文大小
     * @param imageView ImageView对象
     * @param imageUrl  图片url地址
     * @param angle     圆角的角度
     */
    public static void displayImages(Context context, ImageView imageView, String imageUrl, int angle) {
        RoundedCorners roundedCorners = new RoundedCorners(angle);
        Glide.with(context).load(imageUrl).apply(RequestOptions.bitmapTransform(roundedCorners)).into(imageView);
    }

    /**
     * 显示圆角图片
     * 解决RoundedCorners跟CenterCrop冲突问题解决
     *
     * @param context   上下文大小
     * @param imageView ImageView对象
     * @param imageUrl  图片url地址
     * @param angle     圆角的角度
     */
    public static void displayImage(Context context, ImageView imageView, String imageUrl, int angle) {
        CenterCrop centerCrop = new CenterCrop();
        RoundedCorners roundedCorners = new RoundedCorners(angle);
        RequestOptions transforms = new RequestOptions().transforms(centerCrop, roundedCorners);
        Glide.with(context).load(imageUrl).apply(transforms).into(imageView);
    }

    /**
     * 显示圆角图片
     *
     * @param context    上下文大小
     * @param imageView  ImageView对象
     * @param file       图片
     * @param angle      圆角的角度
     * @param cornerType 角型
     */
    public static void displayImage(Context context, ImageView imageView, File file, int angle, CornerType cornerType) {
        RoundCornersTransformation transformation = new RoundCornersTransformation(context, angle, cornerType);
        Glide.with(context).load(file).apply(RequestOptions.bitmapTransform(transformation)).into(imageView);
    }

    /**
     * 点击放大图片
     */
    public static void showImage(Activity context, String url) {
        // 全屏显示的方法
        final Dialog dialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        ImageView imgView = getImageView(context);
        displayImage(context, imgView, url);
        dialog.setContentView(imgView);
        dialog.show();
        // 点击图片消失
        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private static ImageView getImageView(Context context) {
        ImageView imgView = new ImageView(context);
        imgView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return imgView;
    }

    /**
     * 点击放大图片
     */
    public static void showImage(Activity context, List<String> urls,int position) {
        // 全屏显示的方法
        final Dialog dialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        View view = View.inflate(context, R.layout.show_image_layout, null);
        ViewPager viewPager = view.findViewById(R.id.view_pager);
        dialog.setContentView(view);
        dialog.show();
        PagerAdapter adapter = new ViewAdapter(dialog,context,urls);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position);
    }

    static class ViewAdapter extends PagerAdapter {
        private List<String> datas;
        private Context context;
        private Dialog dialog;
        public ViewAdapter(Dialog dialog, Context context, List<String> list) {
            this.dialog = dialog;
            this.context = context;
            datas = list;
        }

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imgView = new ImageView(context);
            imgView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            displayImage(context, imgView, datas.get(position));
            container.addView(imgView);

            // 点击图片消失
            imgView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            return imgView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    /**
     * 图片下载回调
     */
    public interface DownloadCallback {

        /**
         * 开始下载图片时回调
         */
        void onDownloadStarted();

        /**
         * 在下载成功或者失败一张图片时回调
         *
         * @param sumTotal        本次要下载的图片总数
         * @param successesAmount 当前下载图片成功的数量
         * @param failuresAmount  当前下载图片失败的数量
         * @param completedAmount 当前下载图片成功或者失败的数量
         */
        void onDownloading(int sumTotal, int successesAmount, int failuresAmount, int completedAmount);

        /**
         * 在下载全部图片完成时回调
         *
         * @param bitmapList 下载完成的图片
         */
        void onDownloadFinished(List<Bitmap> bitmapList);

    }

    /**
     * 下载图片
     *
     * @param picUrl 要下载图片的地址
     */
    public static void downloadPicture(Context context, DownloadCallback callback, @NonNull String... picUrl) {
        sumTotal = picUrl.length;
        if (position < picUrl.length) {
            if (null != callback) {
                callback.onDownloading(sumTotal, successesAmount, failuresAmount, completedAmount);
            } else {
                throw new RuntimeException("没有回调异常");
            }
            downloadPicture(context, callback, position, picUrl);
        } else {
            // 全部下载完成
            if (null != callback) {
                callback.onDownloading(sumTotal, successesAmount, failuresAmount, completedAmount);
                callback.onDownloadFinished(mBitmapList);
            } else {
                throw new RuntimeException("没有回调异常");
            }
            position = 0;
            sumTotal = 0;
            successesAmount = 0;
            failuresAmount = 0;
            completedAmount = 0;
            mBitmapList.clear();
        }
    }

    /**
     * 开始下载图片
     *
     * @param pos    开始下载的位置
     * @param picUrl 要下载图片的地址
     */
    private static void downloadPicture(Context context, DownloadCallback callback, int pos, @NonNull String... picUrl) {
        // https://muyangmin.github.io/glide-docs-cn/doc/getting-started.html
        SimpleTarget<Drawable> simpleTarget = new SimpleTarget<Drawable>() {
            @Override
            public void onLoadStarted(@Nullable Drawable placeholder) {
                callback.onDownloadStarted();
            }

            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                // 下载成功将下载成功的图片转化为Bitmap对象保存到数组中
                mBitmapList.add(drawableToBitmap(resource));
                position++;
                successesAmount++;
                completedAmount++;
                downloadPicture(context, callback, picUrl);
            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                // 下载失败
                LogUtil.e(TAG, "下载图片失败: " + picUrl[pos]);

                mBitmapList.add(null);
                position++;
                failuresAmount++;
                completedAmount++;
                downloadPicture(context, callback, picUrl);
            }
        };
        Glide.with(context).load(picUrl[pos]).into(simpleTarget);
    }

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    /**
     * 将两个Bitmap合成一个Bitmap
     *
     * @param firstProportion             第一个Bitmap的高占合成后图片高的比
     * @param secondProportion            第二个Bitmap的高占合成后图片高的比
     * @param productOffsetRatio          第一张图片向上的偏移量
     * @param augmentedProductOffsetRatio 第二张图片向上的偏移量
     * @param bitmaps                     要合成的Bitmap
     * @return 合成后的Bitmap
     */
    @Nullable
    public static Bitmap pictureSynthesis(double firstProportion, double secondProportion, double productOffsetRatio, double augmentedProductOffsetRatio, @NonNull Bitmap... bitmaps) {
        if (bitmaps.length <= 0) {
            return null;
        }
        if (bitmaps.length == 1) {
            return bitmaps[0];
        }
        Bitmap newBitmap = bitmaps[0];
        for (int i = 1; i < bitmaps.length; i++) {
            newBitmap = createBitmap(newBitmap, bitmaps[i], firstProportion, secondProportion, productOffsetRatio, augmentedProductOffsetRatio);
        }
        return newBitmap;
    }

    /**
     * 将两个Bitmap合成一个Bitmap
     *
     * @param firstProportion             第一个Bitmap的高占合成后图片高的比
     * @param secondProportion            第二个Bitmap的高占合成后图片高的比
     * @param productOffsetRatio          第一张图片向上的偏移量
     * @param augmentedProductOffsetRatio 第二张图片向上的偏移量
     * @param bitmaps                     要合成的Bitmap
     * @return 合成后的Bitmap
     */
    @Nullable
    public static Bitmap pictureSynthesis(double firstProportion, double secondProportion, double productOffsetRatio, double augmentedProductOffsetRatio, List<Bitmap> bitmaps) {
        if (bitmaps.size() <= 0) {
            return null;
        }
        if (bitmaps.size() == 1) {
            return bitmaps.get(0);
        }
        Bitmap newBitmap = bitmaps.get(0);
        for (int i = 1; i < bitmaps.size(); i++) {
            newBitmap = createBitmap(newBitmap, bitmaps.get(i), firstProportion, secondProportion, productOffsetRatio, augmentedProductOffsetRatio);
        }
        return newBitmap;
    }

    /**
     * 将两个Bitmap上下合成合成一个Bitmap
     *
     * @param firstBitmap      第一个Bitmap
     * @param secondBitmap     第二个Bitmap
     * @param firstProportion  第一个Bitmap的高占合成后图片高的比
     * @param secondProportion 第二个Bitmap的高占合成后图片高的比
     * @param plantOffsetRatio 第一张图片向上的偏移量
     * @param potOffsetRatio   第二张图片向上的偏移量
     * @return 合成后的Bitmap
     */
    private static Bitmap createBitmap(Bitmap firstBitmap, Bitmap secondBitmap, double firstProportion, double secondProportion, double plantOffsetRatio, double potOffsetRatio) {
        if (null == firstBitmap) {
            return null;
        }
        if (null == secondBitmap) {
            return firstBitmap;
        }

        // 返回Bitmap的宽度
        int firstWidth = firstBitmap.getWidth();
        // 返回Bitmap的高度
        int firstHeight = firstBitmap.getHeight();
        int secondWidth = secondBitmap.getWidth();
        int secondHeight = secondBitmap.getHeight();
        if (firstWidth > firstHeight) {

        }

        LogUtil.e(TAG, "原始第一个Bitmap的宽度: " + firstWidth);
        LogUtil.e(TAG, "原始第一个Bitmap的高度: " + firstHeight);
        LogUtil.e(TAG, "原始第二个Bitmap的宽度: " + secondWidth);
        LogUtil.e(TAG, "原始第二个Bitmap的高度: " + secondHeight);
        LogUtil.e(TAG, "第一张图片向上的偏移量: " + plantOffsetRatio);
        LogUtil.e(TAG, "第二张图片向上的偏移量: " + potOffsetRatio);
        LogUtil.e(TAG, "原始第一个Bitmap和第二个Bitmap高度比: " + firstProportion + " : " + secondProportion);

        Bitmap newFirstBitmap;
        Bitmap newSecondBitmap;
        if (firstProportion > secondProportion) {
            // 第一张图片占比高,那应该修改第二张图片的高度,计算第二张图片压缩的比例(firstHeight * secondProportion / firstProportion) = 第二张图片压缩后的高度
            double proportion = new BigDecimal((float) (firstHeight * secondProportion / firstProportion) / secondHeight)
                    .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

            Matrix matrix = new Matrix();
            matrix.postScale((float) proportion, (float) proportion);

            // 缩放后的图片
            newSecondBitmap = Bitmap.createBitmap(secondBitmap, 0, 0, secondWidth, secondHeight, matrix, false);
            newFirstBitmap = firstBitmap;
        } else if (Double.doubleToLongBits(firstProportion) == Double.doubleToLongBits(secondProportion)) {
            // 两张图片占比一样高,那首先判断两张图片的真实高度,将矮的作为两张照片的高度
            if (firstHeight > secondHeight) {
                // 第一张图片真实高度高,那等比例压缩第一张图片
                double proportion = new BigDecimal((float) secondHeight / firstHeight)
                        .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

                Matrix matrix = new Matrix();
                matrix.postScale((float) proportion, (float) proportion);

                // 缩放后的图片
                newFirstBitmap = Bitmap.createBitmap(firstBitmap, 0, 0, firstWidth, firstHeight, matrix, false);
                newSecondBitmap = secondBitmap;
            } else {
                // 第二张图片真实高度高,那等比例压缩第二张图片
                double proportion = new BigDecimal((float) firstHeight / secondHeight)
                        .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

                Matrix matrix = new Matrix();
                matrix.postScale((float) proportion, (float) proportion);

                // 缩放后的图片
                newSecondBitmap = Bitmap.createBitmap(secondBitmap, 0, 0, secondWidth, secondHeight, matrix, false);
                newFirstBitmap = firstBitmap;
            }
        } else {
            // 第二张图片占比高,那应该修改第一张图片的高度,计算第一张图片压缩的比例(secondHeight * firstProportion / secondProportion) = 第一张图片压缩后的高度
            double proportion = new BigDecimal((float) (secondHeight * firstProportion / secondProportion) / firstHeight)
                    .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

            Matrix matrix = new Matrix();
            matrix.postScale((float) proportion, (float) proportion);

            // 缩放后的图片
            newFirstBitmap = Bitmap.createBitmap(firstBitmap, 0, 0, firstWidth, firstHeight, matrix, false);
            newSecondBitmap = secondBitmap;
        }

        int newFirstWidth = newFirstBitmap.getWidth();
        int newFirstHeight = newFirstBitmap.getHeight();
        int newSecondWidth = newSecondBitmap.getWidth();
        int newSecondHeight = newSecondBitmap.getHeight();

        LogUtil.e(TAG, "新合成第一个Bitmap的宽度: " + newFirstWidth);
        LogUtil.e(TAG, "新合成第一个Bitmap的高度: " + newFirstHeight);
        LogUtil.e(TAG, "新合成第二个Bitmap的宽度: " + newSecondWidth);
        LogUtil.e(TAG, "新合成第二个Bitmap的高度: " + newSecondHeight);

        // 新合成Bitmap的宽度
        int newBitmapWidth = newSecondWidth > newFirstWidth ? newSecondWidth : newFirstWidth;
        // 新合成Bitmap的高度
        int newBitmapHeight;
        // 第二个Bitmap向上的偏移量
        int potOffset = 0;
        if (0 != potOffsetRatio) {
            potOffset = (int) new BigDecimal((float) potOffsetRatio * newSecondHeight / secondProportion)
                    .setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        // 第一个Bitmap向下的偏移量
        int plantOffset = 0;
        if (0 != plantOffsetRatio) {
            plantOffset = (int) new BigDecimal((float) plantOffsetRatio * newFirstHeight / firstProportion)
                    .setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        newBitmapHeight = newFirstHeight + newSecondHeight - potOffset - plantOffset;
        Bitmap newBitmap = Bitmap.createBitmap(newBitmapWidth, newBitmapHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);

        // 获取图片密度
        int bitmapDensity = newFirstBitmap.getDensity();
        // 获取画布的密度
        int canvasDensity = canvas.getDensity();
        // 如果画布密度和图片密度不同,就将画布密度设置为图片密度
        if (canvasDensity != bitmapDensity) {
            canvas.setDensity(bitmapDensity);
        }

        LogUtil.e(TAG, "第一个Bitmap向上的偏移量: " + potOffset);
        LogUtil.e(TAG, "第二个Bitmap向上的偏移量: " + plantOffset);

        LogUtil.e(TAG, "新合成第一个Bitmap的密度: " + newFirstBitmap.getDensity());
        LogUtil.e(TAG, "新合成第二个Bitmap的密度: " + newSecondBitmap.getDensity());

        LogUtil.e(TAG, "新合成Bitmap的宽度: " + newBitmapWidth);
        LogUtil.e(TAG, "新合成Bitmap的高度: " + newBitmapHeight);
        LogUtil.e(TAG, "画布的密度: " + canvasDensity);

        /* TODO 注意这个方法有坑,源代码中有这么一句话
         * TODO If the bitmap and canvas have different densities,
         * TODO this function will take care of automatically scaling the bitmap to draw at the same density as the canvas.
         * TODO (如果位图和画布的密度不同,这个函数将自动缩放位图,使其与画布的密度相同)
         * TODO 所以必须将画布密度设置为和图片的密度相同,否则在一些屏幕分辨率比较高的手机会出现使用drawBitmap()绘制的Bitmap比原Bitmap小
         *
         * @param bitmap 要绘制的bitmap对象
         * @param left   绘制图片左上角的x坐标值
         * @param top    绘制图片左上角的y坐标值
         * @param paint  用于绘制位图的paint(可能为空)
         *
         * drawBitmap(@NonNull Bitmap bitmap, float left, float top, @Nullable Paint paint)
         */

        // 这里先绘制底部图片,这样才可以在植物与盆器重合的部分植物是在盆器上面
        // 判断新合成Bitmap的宽度是否大于底部Bitmap的宽度
        if (newBitmapWidth > newSecondWidth) {
            // 那么绘制图片左上角的x坐标值应该为newBitmapWidth - secondWidth) / 2 (目的是让图片水平居中)
            double left = new BigDecimal((float) (newBitmapWidth - newSecondWidth) / 2).
                    setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            canvas.drawBitmap(newSecondBitmap, (float) left, newFirstHeight - potOffset - plantOffset, null);
        } else {
            canvas.drawBitmap(newSecondBitmap, 0, newFirstHeight - potOffset - plantOffset, null);
        }

        // 判断新合成Bitmap的宽度是否大于顶部Bitmap的宽度
        if (newBitmapWidth > newFirstWidth) {
            // 那么绘制图片左上角的x坐标值应该为newBitmapWidth - firstWidth) / 2 (目的是让图片水平居中)
            double left = new BigDecimal((float) (newBitmapWidth - newFirstWidth) / 2).
                    setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            canvas.drawBitmap(newFirstBitmap, (float) left, 0, null);
        } else {
            canvas.drawBitmap(newFirstBitmap, 0, 0, null);
        }
        return newBitmap;
    }

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    /**
     * Drawable转Bitmap
     *
     * @param drawable 要转化的 Bitmap 对象
     * @return Bitmap对象
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        Bitmap bitmap = Bitmap.createBitmap(
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565
        );
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * 根据图片路径生成 Bitmap
     *
     * @param imagePath 图片路径
     * @return Bitmap
     */
    @Nullable
    public static Bitmap imageToBitmap(String imagePath) {
        if (!judeFileExists(imagePath)) {
            return null;
        }
        try {
            FileInputStream fis = new FileInputStream(imagePath);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 保存Bitmap为图片到本地
     *
     * @param bmp      要保存Bitmap
     * @param fileName 保存后图片的名称
     * @return 保存后图片的路径
     */
    @Nullable
    public static String saveImage(@NonNull Bitmap bmp, String fileName) {
        String pathName = getSDCardPrivateFilesDir(getContext(), DIRECTORY_PICTURES) + "/" + fileName + ".png";
        File file = new File(pathName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            return pathName;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (null != fos) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 将本地图片转化为Bitmap对象
     *
     * @param url 本地图片路径
     * @return Bitmap对象
     */
    @Nullable
    public static Bitmap getLocalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    /**
     * View转Bitmap
     * (View不可见时也可以调用)
     */
    public static Bitmap viewConversionBitmap(@NonNull View view) {
        // 测量view
        int measuredWidth = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int measuredHeight = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(measuredWidth, measuredHeight);
        // 调用layout方法布局后,可以得到view的尺寸大小
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bitmap);
        // 指定画布为白色,否正图片会存在透明
        c.drawColor(Color.WHITE);
        view.draw(c);
        return bitmap;
    }

    /**
     * View转Bitmap(背景透明)
     * (View不可见时也可以调用)
     */
    public static Bitmap viewConversionBitmapBackgroundTransparent(@NonNull View view) {
        view.buildDrawingCache();
        return view.getDrawingCache();
    }

    /**
     * Android 系统原生 API 实现分享图片功能
     *
     * @param activity activity对象
     * @param path     分享图片的路径
     */
    public static void shareImage(Activity activity, String path) {
        File file = new File(path);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/jpg");
        // Android7.0以上URI
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // 添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            // 通过FileProvider创建一个content类型的Uri
            Uri uri = FileProvider.getUriForFile(activity, AUTHORITY, file);
            intent.putExtra(Intent.EXTRA_STREAM, uri);
        } else {
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        }
        activity.startActivity(Intent.createChooser(intent, activity.getString(R.string.share)));
    }

    /**
     * Android 系统原生 API 实现分享多张图片功能
     * <p>
     * https://www.jianshu.com/p/c9f071c51eba
     *
     * @param activity activity对象
     * @param fileList 分享图片的File集合
     */
    public static void shareImage(Activity activity, @NonNull List<File> fileList) {
        ArrayList<Uri> imageUris = new ArrayList<>();
        try {
            for (File file : fileList) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    // Android7.0以上URI
                    ContentResolver cr = activity.getContentResolver();
                    String path = file.getAbsolutePath();
                    String fileName = file.getName();
                    String uriString = MediaStore.Images.Media.insertImage(cr, path, fileName, null);
                    Uri uri = Uri.parse(uriString);
                    imageUris.add(uri);
                } else {
                    // Android7.0以下URI
                    imageUris.add(Uri.fromFile(file));
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        intent.setType("image/jpg");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
        activity.startActivity(Intent.createChooser(intent, activity.getString(R.string.share)));
    }

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    /**
     * 实现分享图片到微信好友的功能
     *
     * @param bitmap 图片
     */
    public static void shareImageToWeChatFriend(Bitmap bitmap) {
        // 初始化WXImageObject和WXMediaMessage对象
        WXImageObject imageObject = new WXImageObject(bitmap);
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imageObject;

        // 构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;

        // 调用api接口发送数据到微信
        YiBaiApplication.getIWXAPI().sendReq(req);
    }

    /**
     * 实现分享图片到微信朋友圈的功能
     *
     * @param bitmap 图片
     */
    public static void shareImageToWeChatMoment(Bitmap bitmap) {
        // 初始化WXImageObject和WXMediaMessage对象
        WXImageObject imageObject = new WXImageObject(bitmap);
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imageObject;

        // 构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneTimeline;

        // 调用api接口发送数据到微信
        YiBaiApplication.getIWXAPI().sendReq(req);
    }
}
