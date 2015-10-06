package com.vp9.player.subtitles;

import java.util.Locale;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import com.vp9.util.Vp9ParamUtil;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;

public class Utilities {
//	private static final String OK = "Ok";

	public static String getDataString(JSONObject paramJSONObject,
			String paramString) {
		String localObject = "";
		try {
			if (!paramJSONObject.getString(paramString).equals(JSONObject.NULL)) {
				String str = paramJSONObject.getString(paramString);
				localObject = str;
			}
			return localObject;
		} catch (JSONException localJSONException) {
			while (true)
				localJSONException.printStackTrace();
		}
	}

	public static int getProgressPercentage(long paramLong1, long paramLong2) {
//		Double.valueOf(0.0D);
//		long l1 = (int) (paramLong1 / 1000L);
//		long l2 = (int) (paramLong2 / 1000L);
//		if (l2 == 0) {
//			return 0;
//		}
//		int value = Double.valueOf((100.0D * l1) / l2).intValue();
		return (int)paramLong1;
	}

	public static boolean isValidateEmail(String paramString) {
		return Pattern
				.compile("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
				.matcher(paramString).matches();
	}

	public static String milliSecondsToTimer(long paramLong) {
		if(paramLong < 0){
			paramLong = 0;
		}
		String strTime = "";
		int m = (int) (paramLong / 60000);
		int s = (int) ((paramLong % 60000L) / 1000L);
	
		if (m < 10){
			strTime = "0" + m + ":";
		}else {
			strTime = m + ":";
		}
			
		if (s >= 10){
			strTime += String.valueOf(s);
		}else{
			strTime += "0" + String.valueOf(s);
		}
			
		return strTime;
	}
	
	
	public static int convertStringTimeToSeconds(String strTime) {
		int seconds = 0;
		String[] strTimes = strTime.split(":");
		int[] indexs = {3600, 60, 1};
		if(strTimes != null){
			for(int i = 0; i < strTimes.length; i++){
				if(strTimes[i] != null && strTimes[i].trim().startsWith("0")){
					strTimes[i] = strTimes[i].trim().substring(1);
				}
				seconds += Vp9ParamUtil.getValue(strTimes[i].trim(), 0)*indexs[i];
			}
		}
			
		return seconds;
	}

	public static int progressToTimer(int paramInt1, int paramInt2) {	
		int time = paramInt2*paramInt1 / 100;
		return time;
	}
	
	public static long progressToTimer(int paramInt1, long paramInt2) {	
		long time = paramInt2*paramInt1 / 100;
		return time;
	}

	public static AlertDialog showDialogNoBack(Context paramContext,
			String paramString) {
		AlertDialog.Builder localBuilder = new AlertDialog.Builder(paramContext);
		localBuilder.setMessage(paramString).setNeutralButton("Ok",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface paramDialogInterface,
							int paramInt) {
					}
				});
		return localBuilder.create();
	}

	public static void updateLanguage(Context paramContext, String paramString) {
		Locale localLocale = new Locale(paramString);
		Locale.setDefault(localLocale);
		Configuration localConfiguration = new Configuration();
		localConfiguration.locale = localLocale;
		paramContext.getResources().updateConfiguration(localConfiguration,
				paramContext.getResources().getDisplayMetrics());
	}
}

/*
 * Location: C:\Androidvn\dex2jar\classes_dex2jar.jar Qualified Name:
 * com.hayhaytv.libs.Utilities JD-Core Version: 0.6.0
 */