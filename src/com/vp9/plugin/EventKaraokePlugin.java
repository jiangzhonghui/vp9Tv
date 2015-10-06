package com.vp9.plugin;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Looper;
import android.util.Log;

import com.vp9.tv.KaraokeActivity;
import com.vp9.util.Vp9ParamUtil;

public class EventKaraokePlugin extends org.apache.cordova.CordovaPlugin {

	public static final String TAG = "EventKaraokePlugin";
	
	public static final int ACTION_BIND_EVENT_FIRST_PLAY_KARAO_INDEX = 0;
	
	public static final int ACTION_BIND_EVENT_PLAY_KARAO_INDEX = 1;
	
	public static final int ACTION_BIND_EVENT_PAUSE_KARAO_INDEX = 2;
	
	public static final int ACTION_BIND_EVENT_ENDED_KARAO_INDEX = 3;
	
	public static final int ACTION_BIND_EVENT_TIME_UPDATE_KARAO_INDEX = 4;
	
	public static final int ACTION_BIND_EVENT_PREPARED_KARAO_INDEX = 5;
	
	public static final int ACTION_BIND_EVENT_SEEKED_KARAO_INDEX = 6;
	
	public static final int ACTION_BIND_EVENT_WAITING_KARAO_INDEX = 7;
	
	public static final int ACTION_BIND_EVENT_ERROR_KARAO_INDEX = 8;
	

	public static final String ACTION_BIND_EVENT_FIRST_PLAY_KARAO_LISTENER = "onFirstPlay";
	
	public static final String ACTION_BIND_EVENT_PLAY_KARAO_LISTENER = "onPlay";
	
	public static final String ACTION_BIND_EVENT_PAUSE_KARAO_LISTENER = "onPause";
	
	public static final String ACTION_BIND_EVENT_ENDED_KARAO_LISTENER = "onEnded";
	
	public static final String ACTION_BIND_EVENT_TIME_UPDATE_KARAO_LISTENER = "onTimeupdate";
	
	public static final String ACTION_BIND_EVENT_PREPARED_KARAO_LISTENER = "onLoadedMetadata";
	
	public static final String ACTION_BIND_EVENT_SEEKED_KARAO_LISTENER = "onSeeked";
	
	public static final String ACTION_BIND_EVENT_WAITING_KARAO_LISTENER = "onWaiting";
	
	public static final String ACTION_BIND_EVENT_ERROR_KARAO_LISTENER = "onError";
	
	public static final String ACTION_SET_SOURCE_VIDEO_KARAOKE = "setVideo";
	
	public static final String ACTION_SET_PLAY_KARAOKE = "setPlay";
	
	public static final String ACTION_SET_PAUSE_KARAOKE = "setPause";
	
	public static final String ACTION_SET_SEEK_KARAOKE = "setSeek";
	
	public static final String ACTION_SET_STOP_KARAOKE = "setStop";
	
	public static final String ACTION_GET_VIDEO_URL_KARAOKE = "getVideo";
	
	public static final String ACTION_GET_CURRENT_TIME_KARAOKE = "getCurrentTime";
	
	public static final String ACTION_GET_DURATION_KARAOKE = "getDuration";
	
	public static final String ACTION_GET_STATE_KARAOKE = "getState";
	
	public static final String ACTION_TOGGLE_PLAY_PAUSE = "togglePlayPause";

	public ConcurrentHashMap<String, CallbackContext> callbackContextMaps;

	public EventKaraokePlugin() {
		super();
		Log.d(TAG, "Constructing EventPlayerPlugin");
		callbackContextMaps = new ConcurrentHashMap<String, CallbackContext>();
	}

	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		Log.e(TAG, "execute: " + action + "/" + args);
		final String _action = action;
		final JSONArray _args = args;
		final CallbackContext _callbackContext = callbackContext;
		
		cordova.getThreadPool().execute(new Runnable() {
			@Override
			public void run() {
				Looper.prepare();
				if (isListener(_action)) {
					bindListener(_action, _args, _callbackContext);
				}else if (ACTION_SET_SOURCE_VIDEO_KARAOKE.equals(_action)) {
					setSourceKaraoke(_args, _callbackContext);
				}else if (ACTION_SET_PLAY_KARAOKE.equals(_action)) {
					setPlayKaraoke(_args, _callbackContext);
				}else if (ACTION_SET_PAUSE_KARAOKE.equals(_action)) {
					setPauseKaraoke(_args, _callbackContext);
				}else if (ACTION_SET_SEEK_KARAOKE.equals(_action)) {
					setSeekKaraoke(_args, _callbackContext);
				}else if (ACTION_SET_STOP_KARAOKE.equals(_action)) {
					setStopKaraoke(_args, _callbackContext);
				}else if (ACTION_GET_DURATION_KARAOKE.equals(_action)) {
					getDurationKaraoke(_args, _callbackContext);
				}else if (ACTION_GET_CURRENT_TIME_KARAOKE.equals(_action)) {
					getCurrentTimeKaraoke(_args, _callbackContext);
				}else if (ACTION_GET_VIDEO_URL_KARAOKE.equals(_action)) {
					getVideoUrlKaraoke(_args, _callbackContext);
				}else if (ACTION_GET_STATE_KARAOKE.equals(_action)) {
					getStateKaraoke(_args, _callbackContext);
				}else if (ACTION_TOGGLE_PLAY_PAUSE.equals(_action)) {
					togglePlayPause(_args, _callbackContext);
				}
				
				
				Looper.loop();
			}
		});
		
		return true;

	}

	protected void getStateKaraoke(JSONArray _args,
			CallbackContext callbackContext) {
		Log.d(TAG, "getStateKaraoke");
		if(cordova != null && cordova instanceof KaraokeActivity){
			KaraokeActivity karaokeActivity = (KaraokeActivity) cordova;
			karaokeActivity.getStateKaraoke(callbackContext);

		}else{
			callbackContext.error("fail: " + "incorrect config plugin");
		}
		
	}
	
	
	protected void togglePlayPause(JSONArray _args,
			CallbackContext callbackContext) {
		Log.d(TAG, "setPlayKaraoke");
		if(cordova != null && cordova instanceof KaraokeActivity){
			KaraokeActivity karaokeActivity = (KaraokeActivity) cordova;
			karaokeActivity.togglePlayPause(callbackContext);
		}else{
			callbackContext.error("fail: " + "incorrect config plugin");
		}
		
	}

	protected void getDurationKaraoke(JSONArray _args,
			CallbackContext callbackContext) {
		Log.d(TAG, "getDurationKaraoke");
		if(cordova != null && cordova instanceof KaraokeActivity){
			KaraokeActivity karaokeActivity = (KaraokeActivity) cordova;
			karaokeActivity.getDurationKaraoke(callbackContext);

		}else{
			callbackContext.error("fail: " + "incorrect config plugin");
		}
	}

	protected void getCurrentTimeKaraoke(JSONArray _args,
			CallbackContext callbackContext) {
		Log.d(TAG, "getCurrentTimeKaraoke");
		if(cordova != null && cordova instanceof KaraokeActivity){
			KaraokeActivity karaokeActivity = (KaraokeActivity) cordova;
			karaokeActivity.getCurrentTimeKaraoke(callbackContext);

		}else{
			callbackContext.error("fail: " + "incorrect config plugin");
		}
	}

	protected void getVideoUrlKaraoke(JSONArray _args,
			CallbackContext callbackContext) {
		Log.d(TAG, "getVideoUrlKaraoke");
		if(cordova != null && cordova instanceof KaraokeActivity){
			KaraokeActivity karaokeActivity = (KaraokeActivity) cordova;
			karaokeActivity.getVideoUrlKaraoke(callbackContext);

		}else{
			callbackContext.error("fail: " + "incorrect config plugin");
		}
	}

	protected void setStopKaraoke(JSONArray _args,
			CallbackContext callbackContext) {
		Log.d(TAG, "setStopKaraoke");
		if(cordova != null && cordova instanceof KaraokeActivity){
			KaraokeActivity karaokeActivity = (KaraokeActivity) cordova;
			karaokeActivity.setStopKaraoke(callbackContext);

		}else{
			callbackContext.error("fail: " + "incorrect config plugin");
		}
		
	}

	protected void setSeekKaraoke(JSONArray args,
			CallbackContext callbackContext) {
		Log.d(TAG, "setSeekKaraoke");
		if(cordova != null && cordova instanceof KaraokeActivity && args != null && args.length() > 0){
			try {
				JSONObject json = args.getJSONObject(0);
				int seek = -1;
				if(json != null){
					seek = Vp9ParamUtil.getJsonInt(json, "seek", -1);
				}
				
				if(seek == -1){
					callbackContext.error("fail: no parameter");
				}else{
					KaraokeActivity karaokeActivity = (KaraokeActivity) cordova;
					karaokeActivity.setSeekKaraoke(seek, callbackContext);
				}
			} catch (JSONException e) {
				callbackContext.error("fail: " + e.getMessage());
				e.printStackTrace();
			}

		}else{
			callbackContext.error("fail: " + "incorrect config plugin");
		}
	}

	protected void setPauseKaraoke(JSONArray _args,
			CallbackContext callbackContext) {
		Log.d(TAG, "setPauseKaraoke");
		if(cordova != null && cordova instanceof KaraokeActivity){
			KaraokeActivity karaokeActivity = (KaraokeActivity) cordova;
			karaokeActivity.setPauseKaraoke(callbackContext);
		}else{
			callbackContext.error("fail: " + "incorrect config plugin");
		}
	}

	protected void setPlayKaraoke(JSONArray _args,
			CallbackContext callbackContext) {
		Log.d(TAG, "setPlayKaraoke");
		if(cordova != null && cordova instanceof KaraokeActivity){
			KaraokeActivity karaokeActivity = (KaraokeActivity) cordova;
			karaokeActivity.setPlayKaraoke(callbackContext);
		}else{
			callbackContext.error("fail: " + "incorrect config plugin");
		}
		
	}

	protected void setSourceKaraoke(JSONArray args,
			CallbackContext callbackContext) {
		Log.d(TAG, "setPlayKaraokeListener");
		if(cordova != null && cordova instanceof KaraokeActivity && args != null && args.length() > 0){
			KaraokeActivity karaokeActivity = (KaraokeActivity) cordova;
			try {
				JSONObject json = args.getJSONObject(0);
				karaokeActivity.setSourceKaraoke(json, callbackContext);
			} catch (JSONException e) {
				callbackContext.error("fail: " + e.getMessage());
				e.printStackTrace();
			}
		}else{
			callbackContext.error("fail: " + "incorrect config plugin");
		}
	}

	protected boolean isListener(String _action) {
		String[] listenerArr =  {ACTION_BIND_EVENT_FIRST_PLAY_KARAO_LISTENER, 
				ACTION_BIND_EVENT_PLAY_KARAO_LISTENER,
				ACTION_BIND_EVENT_PAUSE_KARAO_LISTENER,
				ACTION_BIND_EVENT_ENDED_KARAO_LISTENER,
				ACTION_BIND_EVENT_TIME_UPDATE_KARAO_LISTENER,
				ACTION_BIND_EVENT_PREPARED_KARAO_LISTENER,
				ACTION_BIND_EVENT_SEEKED_KARAO_LISTENER,
				ACTION_BIND_EVENT_WAITING_KARAO_LISTENER,
				ACTION_BIND_EVENT_ERROR_KARAO_LISTENER};
		for(int i = 0; i < listenerArr.length; i++){
			String listener = listenerArr[i];
			if(listener.equals(_action)){
				if(cordova != null && cordova instanceof KaraokeActivity){
					KaraokeActivity karaokeActivity = (KaraokeActivity) cordova;
					karaokeActivity.setListenerState(i);
				}
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
	
	public void reportEvent(String _action, JSONObject eventData) {
		
		Log.e(TAG, "reportEvent: " + eventData.toString() + "/" + _action);
		PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, eventData);
		pluginResult.setKeepCallback(true);
		if (_action != null && callbackContextMaps.containsKey(_action)) {
			CallbackContext listenerCallbackContext = callbackContextMaps.get(_action);
			listenerCallbackContext.sendPluginResult(pluginResult);
		}
	}
	
	public void reportEvent(String _action, Object eventData) {
		
		Log.e(TAG, "reportEvent: " + eventData.toString() + "/" + _action);
		PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, eventData.toString());
		pluginResult.setKeepCallback(true);
		if (_action != null && callbackContextMaps.containsKey(_action)) {
			CallbackContext listenerCallbackContext = callbackContextMaps.get(_action);
			listenerCallbackContext.sendPluginResult(pluginResult);
		}
	}
}