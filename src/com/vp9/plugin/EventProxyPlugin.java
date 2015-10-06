 package com.vp9.plugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tv.vp9.videoproxy.MyService;
import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Looper;
import android.util.Log;

import com.vp9.tv.KaraokeActivity;
import com.vp9.tv.VpMainActivity;
import com.vp9.util.FileUtil;
import com.vp9.util.Vp9ParamUtil;

public class EventProxyPlugin extends CordovaPlugin {
	
	public static final String TAG = "EventProxyPlugin";

	public static final String ACTION_SEND_SPEED = "send_speed";
	
	public static final String ACTION_SEND_NOTIFY = "send_notify";
	
	public static final String ACTION_START_SERVICE_PROXY = "startserviceproxy";
	
	public static final String ACTION_STOP_SERVICE_PROXY = "stopserviceproxy";
	
	public ConcurrentHashMap<String, CallbackContext> callbackContextMaps;
	
	public EventProxyPlugin() {
		super();
		Log.d(TAG, "Constructing EventProxyPlugin");
		callbackContextMaps = new ConcurrentHashMap<String, CallbackContext>();
	}
	
	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		Log.d(TAG, "execute: " + action + "/" + args);
		final String _action = action;
		final JSONArray _args = args;
		final CallbackContext _callbackContext = callbackContext;
		
		cordova.getThreadPool().execute(new Runnable() {

			@Override
			public void run() {
				if (isListener(_action)) {
					bindListener(_action, _args, _callbackContext);
				} else if (ACTION_SEND_SPEED.equals(_action)) {
					handleSendSpeed(_args, _callbackContext);
				} else if (ACTION_START_SERVICE_PROXY.equals(_action)) {
					handleStartServiceProxy(_args, _callbackContext);
				} else if (ACTION_STOP_SERVICE_PROXY.equals(_action)) {
					handleStopServiceProxy(_args, _callbackContext);
				}
			}
		});
		
		return true;

	}

//	protected void handleStopServiceProxy(JSONArray args,
//			CallbackContext callbackContext) {
//		Log.d(TAG, "VP9-Proxy Stop");
//		MyService.stop();
//		callbackContext.success("success");
//	}
	
	protected void handleStopServiceProxy(JSONArray args,
			CallbackContext callbackContext) {
		Log.d(TAG, "VP9-Proxy Stop");
		VpMainActivity vpMainActivity = (VpMainActivity) cordova;
		
		MyService proxySevice = null;
		
		if (vpMainActivity != null) {
			
			proxySevice = vpMainActivity.getProxySevice();
			if(proxySevice != null){
				proxySevice.stopThread();
			}
			
			callbackContext.success("success");
			
		}else{
			callbackContext.error("fail");
		}
	}

	protected void handleStartServiceProxy(JSONArray args,
			CallbackContext callbackContext) {
//		updateLibraryFiles();
		FileUtil.updateLibraryFiles((Activity) cordova);
		MyService proxySevice = new MyService();
		proxySevice.startProxy();
		Log.d(TAG, "VP9-Proxy Starting");
		
		if(cordova instanceof VpMainActivity){
			Log.d(TAG, "VP9-Proxy Starting: VpMainActivity 1");
			VpMainActivity vpMainActivity = (VpMainActivity) cordova;
			if (vpMainActivity != null) {
				Log.d(TAG, "VP9-Proxy Starting: VpMainActivity 2");
				vpMainActivity.setProxySevice(proxySevice);
			}
		}else if(cordova instanceof KaraokeActivity){
			KaraokeActivity mainActivity = (KaraokeActivity) cordova;
			if (mainActivity != null) {
				Log.d(TAG, "VP9-Proxy Starting: KaraokeActivity 2");
				mainActivity.setProxySevice(proxySevice);
			}
		}
		

		try {
			Log.d(TAG, "VP9-Proxy Start: success");
			callbackContext.success("success");
			JSONObject eventData = new JSONObject();
			eventData.put("type", 0);
			eventData.put("name", "start proxy");
			eventData.put("message", "Compression enabled");
			sendEvent(ACTION_SEND_NOTIFY, eventData);
			
		} catch (Exception e) {
			callbackContext.success("success");
			e.printStackTrace();
		}
	}
	
//	protected void handleStartServiceProxy(JSONArray args,
//			CallbackContext callbackContext) {
////		updateLibraryFiles();
//		Log.d(TAG, "VP9-Proxy Starting");
//		Intent intent = new Intent(cordova.getActivity(), MyService.class);
//		ComponentName componentName = cordova.getActivity().startService(intent);
//		if (componentName != null) {
//			try {
//				Log.d(TAG, "VP9-Proxy Start: success");
//				callbackContext.success("success");
//				JSONObject eventData = new JSONObject();
//				eventData.put("type", 0);
//				eventData.put("name", "start proxy");
//				eventData.put("message", "Compression enabled");
//				sendEvent(ACTION_SEND_NOTIFY, eventData);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}else{
//			callbackContext.error("fail");
//		}
//	}

//	private void updateLibraryFiles() {
//		Log.d(TAG, "loading library proxy");
//		boolean isExistLibDir = true;
//		String libsDirPath = cordova.getActivity().getFilesDir().getParent() + "/libs";
//		File libsDir = new File(libsDirPath);
//		if (!libsDir.exists()) {
//			isExistLibDir = false;
//			boolean isMkdir = libsDir.mkdir();
//			if (!isMkdir) {
//				return;
//			}
//		}
//
//		AssetManager assetManager = cordova.getActivity().getResources().getAssets();
//		try {
//			String[] proxyLibNames = assetManager.list("proxyLibs");
//			if (proxyLibNames != null && proxyLibNames.length > 0) {
//				for (int i = 0; i < proxyLibNames.length; i++) {
//					Log.d(TAG, "proxyLibName: " + proxyLibNames[i]);
//					if (!isExistLibDir || !checkExistFile(libsDirPath + "/" + proxyLibNames[i])) {
//						InputStream inputStream = assetManager.open("proxyLibs" + "/" + proxyLibNames[i]);
//						if (inputStream != null) {
//							String filePath = libsDirPath + "/" + proxyLibNames[i];
//							writeFile(inputStream, filePath);
//;
//						}
//					}
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
	private boolean writeFile(InputStream fis, String fileName) {
		Log.e(TAG, "writeFile: " + fileName);
		OutputStreamWriter osw = null;
		try {
			int available = fis.available();
			byte[] buffer = new byte[available];
			fis.read(buffer);
			FileOutputStream fOut = new FileOutputStream(new File(fileName));
			fOut.write(buffer);
			fOut.flush();
			fOut.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (osw != null) {
					osw.flush();
					osw.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return true;
	}
	
	private boolean checkExistFile(String fileName) {
		boolean isExist = false;
		try {
			File file = new File(fileName);
			if (file.exists()) {
				isExist = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return isExist;

	}
	
	protected boolean isListener(String _action) {
		String[] listenerArr =  {ACTION_SEND_NOTIFY};
		for(int i = 0; i < listenerArr.length; i++){
			String listener = listenerArr[i];
			if(listener.equals(_action)){
				return true;
			}
		}
		return false;
	}
	
	private boolean bindListener(String _action, JSONArray args, CallbackContext callbackContext) {
		Log.d(TAG, "bindListener: " + _action);
		callbackContextMaps.put(_action, callbackContext);
		PluginResult pluginResult = new PluginResult(PluginResult.Status.OK);
		pluginResult.setKeepCallback(true);
		callbackContext.sendPluginResult(pluginResult);
		return true;
	}
	
	protected void handleSendSpeed(JSONArray args,
			CallbackContext callbackContext) {
		
		VpMainActivity vpMainActivity = (VpMainActivity) cordova;
		
		MyService proxySevice = null;
		
		if (vpMainActivity != null) {
			
			proxySevice = vpMainActivity.getProxySevice();
			
		}
		
		if(proxySevice != null){
			try {
				JSONObject json = args.getJSONObject(0);
				if(json != null){
					int type = Vp9ParamUtil.getJsonInt(json, "type", 0);
					double speed = proxySevice.get_speed(type);
					double expected_speed = proxySevice.get_expected_speed(type);
					double durations = proxySevice.get_durations(type);

					JSONObject eventData = new JSONObject();
					eventData.put("type", type);
					eventData.put("speed", speed);
					eventData.put("expected_speed", expected_speed);
					eventData.put("durations", durations);
					callbackContext.success(eventData);
				}else{
					callbackContext.error("fail: No parameters");
				}
			} catch (Exception e) {
				callbackContext.error("fail: " + e.getMessage());
				e.printStackTrace();
			}
		}else{
			callbackContext.error("fail");
		}

		
	}
	
//	protected void handleSendSpeed(JSONArray args,
//			CallbackContext callbackContext) {
//		try {
//			JSONObject json = args.getJSONObject(0);
//			if(json != null){
//				int type = Vp9ParamUtil.getJsonInt(json, "type", 0);
//				double speed = MyService.get_speed(type);
//				double expected_speed = MyService.get_expected_speed(type);
//				double durations = MyService.get_durations(type);
//
//				JSONObject eventData = new JSONObject();
//				eventData.put("type", type);
//				eventData.put("speed", speed);
//				eventData.put("expected_speed", expected_speed);
//				eventData.put("durations", durations);
//				callbackContext.success(eventData);
//			}else{
//				callbackContext.error("fail: No parameters");
//			}
//		} catch (Exception e) {
//			callbackContext.error("fail: " + e.getMessage());
//			e.printStackTrace();
//		}
//		
//	}

	public void sendEvent(String _action, JSONObject eventData) {
		
		Log.d(TAG, "reportEvent: " + eventData.toString() + "/" + _action);
		PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, eventData);
		pluginResult.setKeepCallback(true);
		if (_action != null && callbackContextMaps.containsKey(_action)) {
			CallbackContext listenerCallbackContext = callbackContextMaps.get(_action);
			listenerCallbackContext.sendPluginResult(pluginResult);
		}
	}


}
