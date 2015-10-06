package com.vp9.tv;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.vp9.plugin.HandlerEventPlugin;


public class VpService extends Service
{
    private static final String TAG = "VpService";
    
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public void onDestroy() {
        //Toast.makeText(this, "My Service Stopped", Toast.LENGTH_LONG).show();
        Log.d(TAG, "onDestroy");
    }
    
    @Override
    public void onCreate() {
    	super.onCreate();
    	Log.d("TAG", "--------------------------onCreate VpService");
    }
    
    @Override
    public void onStart(Intent intent, int startid)
    {
    	super.onStart(intent, startid);
    	
    	Log.d("TAG", "--------------------------onStart VpService");
    	
		SharedPreferences prefs = this.getSharedPreferences(
			      "com.vp9.app", Context.MODE_PRIVATE); 
		boolean isRstart = prefs.getBoolean("isRstart", false);
    	if(isRstart){
    		Editor editor = prefs.edit(); 
            editor.putBoolean("isRstart", false);
            editor.commit();
            
//    		ActivityManager am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
//    		boolean isVp9ActivityRunning = HandlerEventPlugin.checkCurrentActivityRunning(am);
//    		try {
//    			JSONObject jsonObject = readFileConfig();
//    			if (jsonObject != null) {
//    				launchApp(jsonObject);
//				}else{
//					Intent intents = new Intent(getBaseContext(),MainActivity.class);
//	                intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//	                startActivity(intents);
//				}
//				
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}
    		
    		
//    		if (!isVp9ActivityRunning) {
//                Intent intents = new Intent(getBaseContext(),MainActivity.class);
//                intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intents);
//                Toast.makeText(this, "My Service Started Vp9Launcher", Toast.LENGTH_LONG).show();
//                Log.d(TAG, "onStart");
//			}
    	}

    }
    
    private JSONObject readFileConfig(){
    	
    	File sdcard = Environment.getExternalStorageDirectory();
		File configFile = new File(sdcard, "/vp9setting.txt");
		
		if (configFile.exists()) {
			try {
				FileInputStream is = new FileInputStream(configFile);
				int size = is.available();
				byte[] buffer = new byte[size];
				is.read(buffer);
				is.close();
				String text = new String(buffer);
				JSONObject jsnobject = null;
				try {
					jsnobject = new JSONObject(text);
				} catch (JSONException e) {
					e.printStackTrace();
				}

				if (jsnobject != null) {
					JSONObject launchingJson = null;
					if (jsnobject.has("launching")) {
						launchingJson = jsnobject.getJSONObject("launching");
						
						if (launchingJson.has("package") && launchingJson.has("activity")) {
							return launchingJson;
						}
						
						return null;
					}
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return null;
    }
    
    private void launchApp (JSONObject jsonObject) throws JSONException{
    	
    	String appPackage = jsonObject.getString("package").trim();
		String appActivity = jsonObject.getString("activity").trim();
		String appType = "";
		String appServer = "";
		String appChannelNumber = "";

		if (jsonObject.has("type")) {
			appType = jsonObject.getString("type").trim();
		}
		if (jsonObject.has("server")) {
			appServer = jsonObject.getString("server").trim();
		}
		if ("tivi".equals(appType)) {
			if (jsonObject.has("channelNumber")) {
				appChannelNumber = jsonObject.getString("channelNumber").trim();
			}
		}
		
		try {
			ComponentName name = new ComponentName(appPackage, appActivity);
			Intent i = new Intent(Intent.ACTION_MAIN);

			i.addCategory(Intent.CATEGORY_LAUNCHER);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
			i.setComponent(name);
			Bundle b = new Bundle();
			
			if (appType != null && appType != "") {
				b.putString("type", appType);
			}
			if (appServer != null && appServer != "") {
				b.putString("server", appServer);
			}
			if (appChannelNumber != "" && appChannelNumber != null) {
				b.putString("channelNum", appChannelNumber);
			}

			i.putExtra("start", b);
			startActivity(i);

		} catch (Exception e) {

			if (appActivity.substring(0, 1).indexOf(".") < 0) {
				appActivity = "." + appActivity;
			}

			ComponentName name = new ComponentName(appPackage, appPackage + appActivity);
			Intent i = new Intent(Intent.ACTION_MAIN);

			i.addCategory(Intent.CATEGORY_LAUNCHER);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
			i.setComponent(name);

			 Bundle b = new Bundle();
			 
			 if (appType != null && appType != "") {
				 b.putString("type",appType);
			 }
			 if (appServer != null && appServer != "") {
				 b.putString("server",appServer);
			 }
			 if (appChannelNumber != "" && appChannelNumber != null) {
				 b.putString("channelNum", appChannelNumber);
			 }
			
			 i.putExtra("start", b);

			startActivity(i);
		}
    }

//    @Override
//    public void onStart(Intent intent, int startid)
//    {
//    	Log.d("TAG", "-------------------------- VpService");
//		SharedPreferences prefs = this.getSharedPreferences(
//			      "com.vp9.app", Context.MODE_PRIVATE); 
//		boolean isRstart = prefs.getBoolean("isRstart", false);
//    	if(isRstart){
//            Editor editor = prefs.edit(); 
//            editor.putBoolean("isRstart", false);
//            editor.commit();
//            Intent intents = new Intent(getBaseContext(),MainActivity.class);
//            intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intents);
//            //Toast.makeText(this, "My Service Started", Toast.LENGTH_LONG).show();
//            Log.d(TAG, "onStart");
//    	}
//
//    }
}