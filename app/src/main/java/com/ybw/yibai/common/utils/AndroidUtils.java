package com.ybw.yibai.common.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public final class AndroidUtils
{
	public static final String TAG = AndroidUtils.class.getSimpleName();

	public static final Handler MainHandler = new Handler(Looper.getMainLooper());

	public static DisplayMetrics getActivityDisplayMetrics(Activity activity)
	{
		DisplayMetrics displayMetrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		return displayMetrics;
	}

	public static Point getActivityDisplaySize(Activity activity)
	{
		Point outSize = new Point();
		activity.getWindowManager().getDefaultDisplay().getSize(outSize);
		return outSize;
	}

	public static Point getActivityDisplayRealSize(Activity activity)
	{
		Point outSize = new Point();
		activity.getWindowManager().getDefaultDisplay().getRealSize(outSize);
		return outSize;
	}

	public static WindowManager getWindowManager(Context context)
	{
		return (WindowManager)(context.getSystemService(Activity.WINDOW_SERVICE));
	}

	public static int getStatusBarHeight(Context context)
	{
		int result = 0;
		int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0)
		{
			result = context.getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}

	public static void showToast(final Activity activity, final String text)
	{
		LogUtil.d(TAG, "showToast: " + activity + " " + text);
		if (activity == null || text == null)
		{
			return;
		}
		activity.runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
			}
		});
	}

	public static void showToast(final View view, final String text)
	{
		LogUtil.d(TAG, "showToast: " + view + " " + text);
		view.post(new Runnable()
		{
			@Override
			public void run()
			{
				Toast.makeText(view.getContext(), text, Toast.LENGTH_SHORT).show();
			}
		});
	}

	public static Dialog createDialog(Activity activity, int theme, View view, boolean cancelable,
			boolean canceledOnTouchOutSide)
	{
		Dialog dialog = new Dialog(activity, theme);
		// WindowManager.LayoutParams params = new WindowManager.LayoutParams(
		// WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// PixelFormat.TRANSPARENT);
		// params.width = LayoutParams.WRAP_CONTENT;
		// params.height = LayoutParams.WRAP_CONTENT;
		// params.gravity = Gravity.CENTER;
		// dialog.getWindow().setAttributes(params);
		// dialog.getWindow().getDecorView()
		// .setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
		dialog.setCancelable(cancelable);
		dialog.setCanceledOnTouchOutside(canceledOnTouchOutSide);
		dialog.setContentView(view);
		return dialog;
	}


	public static boolean isUIThread()
	{
		return Looper.getMainLooper().getThread().getId() == Thread.currentThread().getId();
	}

	public static Activity getRootActivity(Activity activity)
	{
		Activity parent;
		while ((parent = activity.getParent()) != null)
		{
			activity = parent;
		}
		return activity;
	}

	/**
	 * ����Activity�������˳�����
	 * */
	public static void setActivityTransitionAnimation(Activity activity, int enterAnimId, int exitAnimId)
	{
		ReflectHelper.invokeDeclaredMethod(activity, Activity.class.getName(), "overridePendingTransition",
				new Class<?>[] { int.class, int.class }, new Object[] { enterAnimId, exitAnimId });
	}

	public static Intent getCallPhoneIntent(String phoneNumber)
	{
		Intent intent = new Intent(Intent.ACTION_CALL);
		if (!TextUtils.isEmpty(phoneNumber))
			intent.setData(Uri.parse("tel:" + phoneNumber));
		return intent;
	}

	/** ����ϵͳ���� */
	public static void callPhone(Activity activity, String phoneNumber)
	{
		Intent intent = getCallPhoneIntent(phoneNumber);
		activity.startActivity(intent);
	}

	/**
	 * ��ȡϵͳ�������Intent
	 * 
	 * ����captureImageFileָ��Ϊnull����᷵������ͼ������ԭͼ
	 * */
	public static Intent getLaunchCameraCaptureIntent(File captureImageFile)
	{
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if (captureImageFile != null)
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(captureImageFile));
		return intent;
	}

	public static Intent getLaunchCameraCaptureIntent(String captureImagePath)
	{
		if (TextUtils.isEmpty(captureImagePath))
			return getLaunchCameraCaptureIntent((File)null);
		return getLaunchCameraCaptureIntent(new File(captureImagePath));
	}

	/**
	 * ����ϵͳ���������
	 * 
	 * ����captureImageFileָ��Ϊnull����᷵������ͼ������ԭͼ
	 * */
	public static void launchCameraCapture(Activity activity, File captureImageFile, int requestCode)
	{
		Intent intent = getLaunchCameraCaptureIntent(captureImageFile);
		activity.startActivityForResult(intent, requestCode);
	}

	public static void launchCameraCapture(Fragment fragment, File captureImageFile, int requestCode)
	{
		Intent intent = getLaunchCameraCaptureIntent(captureImageFile);
		fragment.startActivityForResult(intent, requestCode);
	}

	public static void launchCameraCapture(Activity activity, String captureImagePath, int requestCode)
	{
		Intent intent = getLaunchCameraCaptureIntent(captureImagePath);
		activity.startActivityForResult(intent, requestCode);
	}

	public static void launchCameraCapture(Fragment fragment, String captureImagePath, int requestCode)
	{
		Intent intent = getLaunchCameraCaptureIntent(captureImagePath);
		fragment.startActivityForResult(intent, requestCode);
	}

	public static void launchCameraCapture(Activity activity, int requestCode)
	{
		Intent intent = getLaunchCameraCaptureIntent((File)null);
		activity.startActivityForResult(intent, requestCode);
	}

	public static void launchCameraCapture(Fragment fragment, int requestCode)
	{
		Intent intent = getLaunchCameraCaptureIntent((File)null);
		fragment.startActivityForResult(intent, requestCode);
	}

	/**
	 * ����ϵͳ���Intent
	 * */
	public static Intent getLaunchAlbumIntent()
	{
		Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		// intent.addCategory(Intent.CATEGORY_OPENABLE);
		// intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		// intent.setType("image/*");
		return intent;
	}

	/**
	 * ����ϵͳ���
	 * */
	public static void launchAlbum(Activity activity, int requestCode)
	{
		Intent intent = getLaunchAlbumIntent();
		activity.startActivityForResult(intent, requestCode);
	}

	public static void launchAlbum(Fragment fragment, int requestCode)
	{
		Intent intent = getLaunchAlbumIntent();
		fragment.startActivityForResult(intent, requestCode);
	}

	private static void putImageCropIntentParams(Intent intent, int outputWidth, int outputHeight,
			File outputImageFile, CompressFormat imageFormat)
	{
		intent.putExtra("crop", "true");
		int aspectY = 1000;
		int aspectX = outputWidth * 1000 / outputHeight;
		intent.putExtra("aspectX", aspectX);
		intent.putExtra("aspectY", aspectY);
		intent.putExtra("outputX", outputWidth);
		intent.putExtra("outputY", outputHeight);
		intent.putExtra("scale", true);
		if (outputImageFile == null)
		{
			intent.putExtra("return-data", true);
		}
		else
		{
			intent.putExtra("return-data", false);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outputImageFile));
		}
		if (imageFormat != null)
			intent.putExtra("outputFormat", imageFormat.toString());
		intent.putExtra("noFaceDetection", true); // no face detection
	}

	/**
	 * ����ϵͳͼƬ�ü�����Intent
	 * */
	public static Intent getLaunchImageCropIntent(File imageFile, int outputWidth, int outputHeight,
			File outputImageFile, CompressFormat imageFormat)
	{
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(Uri.fromFile(imageFile), "image/*");
		putImageCropIntentParams(intent, outputWidth, outputHeight, outputImageFile, imageFormat);
		return intent;
	}

	/**
	 * ����ϵͳͼƬ�ü�
	 * */
	public static void launchImageCrop(Activity activity, File imageFile, int cropWidth, int cropHeight,
			File cropImageFile, CompressFormat imageFormat, int requestCode)
	{
		Intent intent = getLaunchImageCropIntent(imageFile, cropWidth, cropHeight, cropImageFile, imageFormat);
		activity.startActivityForResult(intent, requestCode);
	}

	public static void launchImageCrop(Fragment fragment, File imageFile, int cropWidth, int cropHeight,
                                       File cropImageFile, CompressFormat imageFormat, int requestCode)
	{
		Intent intent = getLaunchImageCropIntent(imageFile, cropWidth, cropHeight, cropImageFile, imageFormat);
		fragment.startActivityForResult(intent, requestCode);
	}

	/**
	 * ����ϵͳ����Լ�ͼƬ�ü�����Intent
	 * */
	public static Intent getLaunchAlbumAndCropIntent(int outputWidth, int outputHeight, File outputImageFile,
			CompressFormat imageFormat)
	{
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		intent.setType("image/*");
		putImageCropIntentParams(intent, outputWidth, outputHeight, outputImageFile, imageFormat);
		return intent;
	}

	/**
	 * ����ϵͳ��Ტ�ü�ͼƬ
	 * */
	public static void launchAlbumAndCrop(Activity activity, int outputWidth, int outputHeight, File outputImageFile,
			CompressFormat imageFormat, int requestCode)
	{
		Intent intent = getLaunchAlbumAndCropIntent(outputWidth, outputHeight, outputImageFile, imageFormat);
		activity.startActivityForResult(intent, requestCode);
	}

	public static void launchAlbumAndCrop(Fragment fragment, int outputWidth, int outputHeight, File outputImageFile,
                                          CompressFormat imageFormat, int requestCode)
	{
		Intent intent = getLaunchAlbumAndCropIntent(outputWidth, outputHeight, outputImageFile, imageFormat);
		fragment.startActivityForResult(intent, requestCode);
	}

	public static void launchBrowser(Activity activity, String url)
	{
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse(url));
		activity.startActivity(intent);
	}

	public static void launchBrowser(Context context, String url)
	{
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse(url));
		context.startActivity(intent);
	}

	/**
	 * ����View��Window��APP��ʹ�ò��ֵľ�������
	 * */
	public static int[] getLocationInWindowVisible(View view, int[] location)
	{
		view.getLocationInWindow(location);
		// getLocationInWindow���ص���������˶���״̬����������Ҫ��ȥ״̬���߶�
		Rect rect = new Rect();
		view.getWindowVisibleDisplayFrame(rect);
		location[0] -= rect.left;
		location[1] -= rect.top;
		return location;
	}

	/**
	 * ��װAPKӦ�ó���
	 * */
	public static void installPackage(Context context, File file) throws FileNotFoundException, Exception
	{
		LogUtil.d(TAG, "installPackage: " + context + " " + file);
		if (!file.exists())
		{
			throw new FileNotFoundException("File is not exists filePath = " + file.getAbsolutePath());
		}
		Intent it = new Intent(Intent.ACTION_VIEW);
		it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		it.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
		context.startActivity(it);
	}

	/**
	 * ��װAPKӦ�ó���
	 * */
	public static void installPackage(Context context, String filePath) throws FileNotFoundException, Exception
	{
		installPackage(context, new File(filePath));
	}

	/**
	 * ���ݰ�������һ��Ӧ�ó���
	 * */
	public static boolean startPackage(Context context, String packageName)
	{
		final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		final List<ResolveInfo> apps = context.getPackageManager().queryIntentActivities(mainIntent, 0);
		if (apps != null)
		{
			// Find all activities that match the packageName
			int count = apps.size();
			for (int i = 0; i < count; i++)
			{
				final ResolveInfo resolveInfo = apps.get(i);
				final ActivityInfo activityInfo = resolveInfo.activityInfo;
				if (packageName.equals(activityInfo.packageName))
				{
					String className = activityInfo.name;
					LogUtil.i(TAG, "startPackage: " + packageName + " & " + className);
					ComponentName cn = new ComponentName(packageName, className);
					final Intent it = new Intent(Intent.ACTION_MAIN);
					it.addCategory(Intent.CATEGORY_LAUNCHER);
					it.setComponent(cn);
					it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(it);
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * ��ѹһ��apk�ļ����Ի�ȡPackageInfo
	 * */
	public static synchronized PackageInfo achievePackageInfo(Context context, String filePath) throws Exception
	{
		PackageInfo info = context.getPackageManager().getPackageArchiveInfo(filePath, PackageManager.GET_ACTIVITIES);
		return info;
	}

	/**
	 * �ֻ���
	 * */
	public static void vibrate(Context context, long milliseconds)
	{
		Vibrator vib = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
		vib.cancel();
		vib.vibrate(milliseconds);
	}

	public static void vibrate(Context context, long[] pattern, int repeat)
	{
		Vibrator vib = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
		vib.cancel();
		vib.vibrate(pattern, repeat);
	}

	public static void cancelVibrate(Context context)
	{
		Vibrator vib = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
		vib.cancel();
	}

	public static void sendScanFileBroadcast(Activity activity, File file)
	{
		Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		intent.setData(Uri.fromFile(file));
		getRootActivity(activity).sendBroadcast(intent);
	}

	public static void sendScanFileBroadcast(Activity activity, String filePath)
	{
		sendScanFileBroadcast(activity, new File(filePath));
	}

	public static void sendScanDirectoryBroadcast(Activity activity, File directory)
	{
		Intent intent = new Intent(Intent.ACTION_MEDIA_MOUNTED);
		intent.setData(Uri.parse("file://" + directory.getAbsolutePath()));
		activity.sendBroadcast(intent);
	}

	public static void sendScenExternalDirectoryBroadcast(Activity activity)
	{
		sendScanDirectoryBroadcast(activity, Environment.getExternalStorageDirectory());
	}

	/**
	 * ת��dpΪpx
	 */
	public static int dp2px(Context context, float dpValue)
	{
		final float density = context.getResources().getDisplayMetrics().density;
		return (int)(dpValue * density + 0.5f);
	}

	/**
	 * ת��pxΪdp
	 */
	public static int px2dp(Context context, float pxValue)
	{
		final float density = context.getResources().getDisplayMetrics().density;
		return (int)(pxValue / density + 0.5f);
	}

	/**
	 * ת��ptΪpx
	 */
	public static int pt2px(Context context, float ptValue)
	{
		final float dpi = context.getResources().getDisplayMetrics().xdpi;
		return (int)(ptValue * 72.0f / dpi + 0.5f);
	}

	/**
	 * ת��pxΪpt
	 * */
	public static int px2pt(Context context, float pxValue)
	{
		final float dpi = context.getResources().getDisplayMetrics().xdpi;
		return (int)(pxValue * dpi / 72.0f + 0.5f);
	}
}
