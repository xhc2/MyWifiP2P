package com.example.tongmin.mywifip2p.debugutil;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.view.View;

/**
 *
 * 
 * 
 */
public class AppUtils
{

	private AppUtils()
	{
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");

	}

	/**
	 * 获取app name
	 */
	public static String getAppName(Context context)
	{
		try
		{
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			int labelRes = packageInfo.applicationInfo.labelRes;
			return context.getResources().getString(labelRes);
		} catch (NameNotFoundException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取版本名字
	 * 
	 * @param context
	 * @return ��ǰӦ�õİ汾����
	 */
	public static String getVersionName(Context context)
	{
		try
		{
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			return packageInfo.versionName;

		} catch (NameNotFoundException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	/**
	 *
	 * @param activity
	 * @return
	 */
//	public static Bitmap snapShotWithStatusBar(Activity activity)
//	{
//		View view = activity.getWindow().getDecorView();
//		view.setDrawingCacheEnabled(true);
//		view.buildDrawingCache();
//		Bitmap bmp = view.getDrawingCache();
//		int width = getScreenWidth(activity);
//		int height = getScreenHeight(activity);
//		Bitmap bp = null;
//		bp = Bitmap.createBitmap(bmp, 0, 0, width, height);
//		view.destroyDrawingCache();
//		return bp;
//
//	}
//
//	/**
//	 * ��ȡ��ǰ��Ļ��ͼ��������״̬��
//	 *
//	 * @param activity
//	 * @return
//	 */
//	public static Bitmap snapShotWithoutStatusBar(Activity activity)
//	{
////		View view = activity.getWindow().getDecorView();
//		view.setDrawingCacheEnabled(true);
//		view.buildDrawingCache();
//		Bitmap bmp = view.getDrawingCache();
//		Rect frame = new Rect();
//		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
//		int statusBarHeight = frame.top;
//
//		int width = getScreenWidth(activity);
//		int height = getScreenHeight(activity);
//		Bitmap bp = null;
//		bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height
//				- statusBarHeight);
//		view.destroyDrawingCache();
//		return bp;
//
//	}

}
