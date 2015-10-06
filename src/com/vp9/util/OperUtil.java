package com.vp9.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.vp9.model.AppInfo;
import com.vp9.tv.MainActivity;

public class OperUtil {

	private static final String TAG = "OperUtil";
	public static String CONFIG_FILE_NAME = "config.txt";
	public static String CONFIG_SPECIAL_PACKAGE = "SpecialPackage.txt";
	
	private static String[] blackPkgNameList = {};
	private static String[] whitePkgNameList = { "com.vp9.tv" , "com.vp9.launcher"};
	public static void removeOtherApps(Activity activity) {

		if(activity == null){
			return;
		}
		PackageManager pm = activity.getPackageManager();
		// get a list of installed apps.
		List<ApplicationInfo> packages = pm.getInstalledApplications(0);

		ActivityManager activityManager = (ActivityManager) activity.getSystemService(activity.ACTIVITY_SERVICE);

		ArrayList<AppInfo> appInfoList = new ArrayList<AppInfo>();

		List<RunningAppProcessInfo> processes = activityManager.getRunningAppProcesses();

		ArrayList<String> runningPkgNames = new ArrayList<String>();

		Object[] objs = getSpecialPackageNameInfo();
		ArrayList<String> whiteList = (ArrayList<String>) objs[0];
		ArrayList<String> blackList = (ArrayList<String>) objs[1];

		for (String specPkgName : blackPkgNameList) {
			blackList.add(specPkgName);
		}

		for (String whiteName : whitePkgNameList) {
			whiteList.add(whiteName);
		}

		if (processes != null) {
			for (RunningAppProcessInfo processe : processes) {
				// boolean isSpec = false;
				// for(String specPkgName : blackList){
				// if(specPkgName.equals(processe.processName)){
				// activityManager.killBackgroundProcesses(processe.processName);
				// isSpec = true;
				// break;
				// }
				// }
				// if(isSpec){
				// continue;
				// }
				runningPkgNames.add(processe.processName);

			}

		}

		for (ApplicationInfo packageInfo : packages) {

			if (packageInfo.flags == ApplicationInfo.FLAG_SYSTEM || packageInfo.flags == ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) {
				continue;
			}

			if (isWhitePackageName(whiteList, packageInfo.packageName)) {
				continue;
			}

			CharSequence appName = pm.getApplicationLabel(packageInfo);
			if (runningPkgNames.contains(packageInfo.packageName)) {

				if (appName != null && isBackPackageName(blackList, packageInfo.packageName)) {
					AppInfo appInfo = new AppInfo(appName.toString(), packageInfo.packageName);
					appInfoList.add(appInfo);
				} else {
					activityManager.killBackgroundProcesses(packageInfo.packageName);
				}
			}
		}

		killApp(activity, appInfoList);
	}
	
	
	public static void displayToast(final String message, final Activity activity, final int type){
		if(activity == null){
			return;
		}
		activity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				switch (type) {
				
				case 1:
					Toast.makeText(activity.getBaseContext(), 
							message, Toast.LENGTH_SHORT).show();
					break;
					
				case 2:
					Toast.makeText(activity.getBaseContext(), 
							message, Toast.LENGTH_LONG).show();
					break;

				default:
					break;
				}
				
			}
		});

		
	}
	
	protected static synchronized void killApp(Activity activity, ArrayList<AppInfo> appInfoList) {
		if (appInfoList != null) {
			ActivityManager activityManager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
			for (AppInfo appInfo : appInfoList) {
				activityManager.killBackgroundProcesses(appInfo.packageName);
			}
			appInfoList.clear();
		}
//		handleUrl();
	}
	
	private static boolean isBackPackageName(ArrayList<String> blackList, String packageName) {
		for (String blackName : blackList) {
			if (packageName.startsWith(blackName)) {
				return true;
			}
		}
		return false;
	}

	private static boolean isWhitePackageName(ArrayList<String> whiteList, String packageName) {
		for (String whiteName : whiteList) {
			if (packageName.startsWith(whiteName)) {
				return true;
			}
		}
		return false;
	}
	
	private static Object[] getSpecialPackageNameInfo() {
		File dir = Environment.getExternalStorageDirectory();
		File file = new File(dir, CONFIG_SPECIAL_PACKAGE);
		ArrayList<String> whiteList = new ArrayList<String>();
		ArrayList<String> blackList = new ArrayList<String>();
		if (file.exists()) {
			try {
				BufferedReader br = new BufferedReader(new FileReader(file));
				String line;
				while ((line = br.readLine()) != null) {
					if (!line.startsWith("#")) {
						String[] strs = line.split("=");
						if (strs[0] != null && strs[1] != null && strs[0].equals("enable")) {
							whiteList.add(strs[1]);
						} else if (strs[0] != null && strs[1] != null && strs[0].equals("disable")) {
							blackList.add(strs[1]);
						}
					}
				}
				br.close();
			} catch (IOException e) {
				Log.v(TAG, e.getMessage());
			}
		}
		Object[] objs = new Object[2];
		objs[0] = whiteList;
		objs[1] = blackList;
		return objs;

	}
}
