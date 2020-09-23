package com.ybw.yibai.common.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.View.OnLayoutChangeListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public final class ViewTools
{
	public static final String TAG = ViewTools.class.getSimpleName();

	public enum FitMode
	{
		FIT_IN_PARENT_WIDTH, FIT_IN_PARENT_HEIGHT, FIT_IN_WIDTH, FIT_IN_HEIGHT
	}

	public static void autoFitViewDimension(final View view, final View parent, final FitMode mode, final float xyRatio)
	{
		LogUtil.d(TAG, "autoFitViewDimension: " + view + "; " + parent + "; " + mode + "; " + xyRatio);
		switch (mode)
		{
			case FIT_IN_PARENT_WIDTH:
			case FIT_IN_PARENT_HEIGHT:
				if (parent != null)
				{
					switch (mode)
					{
						case FIT_IN_PARENT_WIDTH:
							if (parent.getWidth() > 0)
							{
								int width = parent.getWidth();
								int height = (int)(parent.getWidth() / xyRatio);
								LayoutParams params = view.getLayoutParams();
								if (params != null)
								{
									params.width = width;
									params.height = height;
									LogUtil.v(TAG, "fitViewDimension finished: view=" + view + "; mode=" + mode
											+ "; dimension=" + params.width + "x" + params.height);
								}
								else
								{
									LogUtil.w(TAG, view + " LayoutParams is null!");
									params = new LayoutParams(width, height);
								}
								view.setLayoutParams(params);
							}
							else
							{
								parent.addOnLayoutChangeListener(new OnLayoutChangeListener()
								{
									@Override
									public void onLayoutChange(View v, int left, int top, int right, int bottom,
															   int oldLeft, int oldTop, int oldRight, int oldBottom)
									{
										if (parent.getWidth() > 0)
										{
											parent.removeOnLayoutChangeListener(this);
											int width = parent.getWidth();
											int height = (int)(parent.getWidth() / xyRatio);
											LayoutParams params = view.getLayoutParams();
											if (params != null)
											{
												params.width = width;
												params.height = height;
												LogUtil.v(TAG, "fitViewDimension finished: view=" + view + "; mode="
														+ mode + "; dimension=" + params.width + "x" + params.height);
											}
											else
											{
												LogUtil.w(TAG, view + " LayoutParams is null!");
												params = new LayoutParams(width, height);
											}
											view.setLayoutParams(params);
										}
									}
								});
							}
							break;
						case FIT_IN_PARENT_HEIGHT:
							if (parent.getHeight() > 0)
							{
								int height = parent.getHeight();
								int width = (int)(parent.getHeight() * xyRatio);
								LayoutParams params = view.getLayoutParams();
								if (params != null)
								{
									params.height = height;
									params.width = width;
									LogUtil.v(TAG, "fitViewDimension finished: view=" + view + "; mode=" + mode
											+ "; dimension=" + params.width + "x" + params.height);
								}
								else
								{
									LogUtil.w(TAG, view + " LayoutParams is null!");
									params = new LayoutParams(width, height);
								}
								view.setLayoutParams(params);
							}
							else
							{
								parent.addOnLayoutChangeListener(new OnLayoutChangeListener()
								{
									@Override
									public void onLayoutChange(View v, int left, int top, int right, int bottom,
															   int oldLeft, int oldTop, int oldRight, int oldBottom)
									{
										if (parent.getHeight() > 0)
										{
											parent.removeOnLayoutChangeListener(this);
											int height = parent.getHeight();
											int width = (int)(parent.getHeight() * xyRatio);
											LayoutParams params = view.getLayoutParams();
											if (params != null)
											{
												params.height = height;
												params.width = width;
												LogUtil.v(TAG, "fitViewDimension finished: view=" + view + "; mode="
														+ mode + "; dimension=" + params.width + "x" + params.height);
											}
											else
											{
												LogUtil.w(TAG, view + " LayoutParams is null!");
												params = new LayoutParams(width, height);
											}
											view.setLayoutParams(params);
										}
									}
								});
							}
							break;
						default:
							break;
					}
				}
				break;
			case FIT_IN_WIDTH:
				if (view.getWidth() > 0)
				{
					LayoutParams params = view.getLayoutParams();
					if (params != null)
					{
						params.height = (int)(view.getWidth() / xyRatio);
						view.setLayoutParams(params);
						LogUtil.v(TAG, "fitViewDimension finished: view=" + view + "; mode=" + mode + "; dimension="
								+ params.width + "x" + params.height);
					}
					else
					{
						LogUtil.w(TAG, view + " LayoutParams is null!");
					}
				}
				else
				{
					view.addOnLayoutChangeListener(new OnLayoutChangeListener()
					{
						@Override
						public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft,
												   int oldTop, int oldRight, int oldBottom)
						{
							if (view.getWidth() > 0)
							{
								view.removeOnLayoutChangeListener(this);
								LayoutParams params = view.getLayoutParams();
								if (params != null)
								{
									params.height = (int)(view.getWidth() / xyRatio);
									view.setLayoutParams(params);
									LogUtil.v(TAG, "fitViewDimension finished: view=" + view + "; mode=" + mode
											+ "; dimension=" + params.width + "x" + params.height);
								}
								else
								{
									LogUtil.w(TAG, view + " LayoutParams is null!");
								}
							}
						}
					});
					// view.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener()
					// {
					// @Override
					// public void onGlobalLayout()
					// {
					// if (view.getWidth() > 0)
					// {
					// view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
					// LayoutParams params = view.getLayoutParams();
					// if (params != null)
					// {
					// params.height = (int)(view.getWidth() / xyRatio);
					// view.setLayoutParams(params);
					// LogUtil.v(TAG, "fitViewDimension finished: view=" + view + "; mode=" + mode
					// + "; dimension=" + params.width + "x" + params.height);
					// }
					// else
					// {
					// LogUtil.w(TAG, view + " LayoutParams is null!");
					// }
					// }
					// }
					// });
				}
				break;
			case FIT_IN_HEIGHT:
				if (view.getHeight() > 0)
				{
					LayoutParams params = view.getLayoutParams();
					if (params != null)
					{
						params.width = (int)(view.getHeight() * xyRatio);
						view.setLayoutParams(params);
						LogUtil.v(TAG, "fitViewDimension finished: view=" + view + "; mode=" + mode + "; dimension="
								+ params.width + "x" + params.height);
					}
					else
					{
						LogUtil.w(TAG, view + " LayoutParams is null!");
					}
				}
				else
				{
					view.addOnLayoutChangeListener(new OnLayoutChangeListener()
					{
						@Override
						public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft,
												   int oldTop, int oldRight, int oldBottom)
						{
							if (view.getHeight() > 0)
							{
								view.removeOnLayoutChangeListener(this);
								LayoutParams params = view.getLayoutParams();
								if (params != null)
								{
									params.width = (int)(view.getHeight() * xyRatio);
									view.setLayoutParams(params);
									LogUtil.v(TAG, "fitViewDimension finished: view=" + view + "; mode=" + mode
											+ "; dimension=" + params.width + "x" + params.height);
								}
								else
								{
									LogUtil.w(TAG, view + " LayoutParams is null!");
								}
							}
						}
					});
					// view.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener()
					// {
					// @Override
					// public void onGlobalLayout()
					// {
					// if (view.getHeight() > 0)
					// {
					// view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
					// LayoutParams params = view.getLayoutParams();
					// if (params != null)
					// {
					// params.width = (int)(view.getHeight() * xyRatio);
					// view.setLayoutParams(params);
					// LogUtil.v(TAG, "fitViewDimension finished: view=" + view + "; mode=" + mode
					// + "; dimension=" + params.width + "x" + params.height);
					// }
					// else
					// {
					// LogUtil.w(TAG, view + " LayoutParams is null!");
					// }
					// }
					// }
					// });
				}
				break;
		}
	}

	public static ViewGroup getActivityDecorView(Activity activity)
	{
		return (ViewGroup)activity.getWindow().getDecorView();
	}

	public static ViewGroup getActivityContentRootView(Activity activity)
	{
		return (ViewGroup)activity.findViewById(android.R.id.content);
	}

	public static void setViewVisibilityInMainThread(final View view, final int visibility)
	{
		if (view == null)
			return;
		AndroidUtils.MainHandler.post(new Runnable()
		{
			@Override
			public void run()
			{
				view.setVisibility(visibility);
			}
		});
	}

	public static void setImageViewResourceInMainThread(Context context, final ImageView imageView, String resIdName)
	{
		if (context == null || imageView == null)
			return;
		final int resId = ResourceUtil.getDrawableId(context, resIdName);
		AndroidUtils.MainHandler.post(new Runnable()
		{
			@Override
			public void run()
			{
				imageView.setImageResource(resId);
			}
		});
	}

	/**
	 * 回收一个ImageView的Bitmap
	 * */
	public static void recycleImageView(ImageView imageView)
	{

	}

	/**
	 * 回收View及子View的Bitmap
	 * */
	public static void recycleViewImageInChilds(View view)
	{

	}

	public static List<View> findAllViewsById(ViewGroup group, int id)
	{
		List<View> list = new ArrayList<View>();
		int count = group.getChildCount();
		for (int i = 0; i < count; i++)
		{
			View child = group.getChildAt(i);
			if (child.getId() == id)
			{
				list.add(child);
			}
		}
		return list;
	}

	public static void removeAllViewsById(ViewGroup group, int id)
	{
		View view;
		while ((view = group.findViewById(id)) != null)
		{
			group.removeView(view);
		}
	}

	public static void removeAllViewsInChildren(ViewGroup group)
	{
		int count = group.getChildCount();
		for (int i = 0; i < count; i++)
		{
			View child = group.getChildAt(i);
			if (child instanceof ViewGroup)
			{
				removeAllViewsInChildren((ViewGroup)child);
			}
		}
		if (group instanceof AdapterView<?>)
		{
		}
		else
		{
			group.removeAllViews();
		}
	}

	public static void removeViewParent(View view)
	{
		ReflectHelper.setDeclaredFieldValue(view, View.class.getName(), "mParent", null);
	}

	public static void removeViewParentInChildren(View view)
	{
		LogUtil.v(TAG, "removeViewParentInChildren: " + view);
		if (view instanceof ViewGroup)
		{
			ViewGroup group = (ViewGroup)view;
			int count = group.getChildCount();
			for (int i = 0; i < count; i++)
			{
				View child = group.getChildAt(i);
				removeViewParentInChildren(child);
			}
		}
		removeViewParent(view);
	}

	public static void clearViewTags(View view)
	{
		try
		{
			Object tags = ReflectHelper.getDeclaredFieldValue(view, View.class.getName(), "mKeyedTags");
			ReflectHelper.invokePublicMethod(tags, "clear", null, null);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static boolean containsView(ViewGroup container, View view)
	{
		int count = container.getChildCount();
		for (int i = 0; i < count; i++)
		{
			View child = container.getChildAt(i);
			if (child == view)
				return true;
		}
		return false;
	}
}
