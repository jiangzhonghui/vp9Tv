package com.vp9.plugin;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Looper;
import android.util.Log;
import android.view.View;

import com.vp9.player.controller.MediaController;
import com.vp9.player.controller.RotationMediaController;
import com.vp9.player.model.ChangeSubtitle;
import com.vp9.tv.VpMainActivity;
import com.vp9.util.ParamUtil;
import com.vp9.util.Vp9ParamUtil;

public class EventPlayerPlugin extends org.apache.cordova.CordovaPlugin {

	public static final String TAG = "EventPlayerPlugin";

	public static final String ACTION_BIND_EVENT_PLAYER_LISTENER = "ACTION_BIND_EVENT_PLAYER_LISTENER";

	public static final String ACTION_SET_START_VIDEO = "ACTION_SET_START_VIDEO";

	public static final String ACTION_SET_SHOW_EPG = "ACTION_SET_SHOW_EPG";

	public static final String ACTION_SET_CLOSE_EPG = "ACTION_SET_CLOSE_EPG";

	public static final String ACTION_SET_PLAY_VIDEO = "ACTION_SET_PLAY_VIDEO";

	public static final String ACTION_SET_PAUSE_VIDEO = "ACTION_SET_PAUSE_VIDEO";

	public static final String ACTION_SET_STOP_VIDEO = "ACTION_SET_STOP_VIDEO";

	public static final String ACTION_SET_CHANGE_SUBTITLE_VIDEO = "ACTION_SET_CHANGE_SUBTITLE_VIDEO";

	public static final String ACTION_SET_RESEND_INFORMATION = "ACTION_SET_RESEND_INFORMATION";

	public static final String ACTION_SET_SEEK_VIDEO = "ACTION_SET_SEEK_VIDEO";

	public static final String ACTION_ADD_PLAYER_LISTENER = "ACTION_ADD_PLAYER_LISTENER";

	public static final String ACTION_SET_FULL_SCREEN = "ACTION_SET_FULL_SCREEN";

	public static final String ACTION_SET_SCREEN_ORIENTATION = "ACTION_SET_SCREEN_ORIENTATION";

	public static final String ACTION_SET_MESSAGE = "ACTION_SET_MESSAGE";

	public static final String ACTION_SET_START_VIDEO_RELATED = "ACTION_SET_START_VIDEO_RELATED";

	public static final String ACTION_SET_RIGHT_VIDEO_DISPLAY = "ACTION_SET_RIGHT_VIDEO_DISPLAY";

	public static final String ACTION_IS_DISPLAY_EPG = "ACTION_IS_DISPLAY_EPG";

	public static final String ACTION_GET_SUBTITLE = "ACTION_GET_SUBTITLE";

	public static final String ACTION_GET_TIME = "ACTION_GET_TIME";
	
	public static final String ACTION_CHANGE_SOURCE = "ACTION_CHANGE_SOURCE";
	
	public static final String ACTION_CLEAR_CACHE = "ACTION_CLEAR_CACHE";
	
	public CallbackContext listenerCallbackContext;

	public EventPlayerPlugin() {
		super();
		Log.d(TAG, "Constructing EventPlayerPlugin");
	}

	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		Log.e(TAG, "execute: " + action + "/" + args);
		final String _action = action;
		final JSONArray _args = args;
		final CallbackContext _callbackContext = callbackContext;
		if (ACTION_SET_START_VIDEO.equals(_action)) {
			try {
//				Looper.prepare();
				startVideo(_args, _callbackContext);
//				Looper.loop();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return true;
		}
		cordova.getThreadPool().execute(new Runnable() {

			@Override
			public void run() {
//				Looper.prepare();
				if (ACTION_BIND_EVENT_PLAYER_LISTENER.equals(_action)) {
					bindListener(_args, _callbackContext);
				}
//				else if (ACTION_SET_START_VIDEO.equals(_action)) {
//					try {
////						Looper.prepare();
//						startVideo(_args, _callbackContext);
////						Looper.loop();
//					} catch (JSONException e) {
//						e.printStackTrace();
//					}
//					;
//				}
				else if (ACTION_SET_SHOW_EPG.equals(_action)) {
					showEPG(_args, _callbackContext);
				} else if (ACTION_SET_CLOSE_EPG.equals(_action)) {
					closeEPG(_args, _callbackContext);
				} else if (ACTION_SET_PLAY_VIDEO.equals(_action)) {
					playVideo(_args, _callbackContext);
				} else if (ACTION_SET_PAUSE_VIDEO.equals(_action)) {
					pauseVideo(_args, _callbackContext);
				} else if (ACTION_SET_STOP_VIDEO.equals(_action)) {
					stopVideo(_args, _callbackContext);
				} else if (ACTION_SET_SEEK_VIDEO.equals(_action)) {
					seekVideo(_args, _callbackContext);
				} else if (ACTION_SET_CHANGE_SUBTITLE_VIDEO.equals(_action)) {
					changeSubtitleVideo(_args, _callbackContext);
				} else if (ACTION_SET_RESEND_INFORMATION.equals(_action)) {
					resendInfoVideo(_args, _callbackContext);
				} else if (ACTION_ADD_PLAYER_LISTENER.equals(_action)) {
					addListener(_args, _callbackContext);
				} else if (ACTION_SET_FULL_SCREEN.equals(_action)) {
					changeDisplayScreen(_args, _callbackContext);
				} else if (ACTION_SET_SCREEN_ORIENTATION.equals(_action)) {
					changeScreenOrientation(_args, _callbackContext);
				} else if (ACTION_SET_MESSAGE.equals(_action)) {
					setMessage(_args, _callbackContext);
				} else if (ACTION_SET_START_VIDEO_RELATED.equals(_action)) {
					playRelateVideo(_args, _callbackContext);
				} else if (ACTION_SET_RIGHT_VIDEO_DISPLAY.equals(_action)) {
					setRightVideoDisplay(_args, _callbackContext);
				} else if (ACTION_IS_DISPLAY_EPG.equals(_action)) {
					checkDisplayEPG(_args, _callbackContext);
				} else if (ACTION_GET_SUBTITLE.equals(_action)) {
					getSubtitles(_args, _callbackContext);
				} else if (ACTION_GET_TIME.equals(_action)) {
					getTime(_args, _callbackContext);
				}else if(ACTION_CHANGE_SOURCE.equals(_action)){
					changeSource(_args, _callbackContext);
				}else if (ACTION_CLEAR_CACHE.equals(_action)) {
					clearCache(_args, _callbackContext);
				}
//				Looper.loop();
			}
		});
		return true;
	}
	
	

	private boolean getTime(JSONArray args, CallbackContext callbackContext) {

		VpMainActivity vpMainActivity = (VpMainActivity) cordova;
		if (vpMainActivity != null) {
			vpMainActivity.getTimeRemoteVideo(callbackContext);
			return true;
		}
		return false;
	}

	private boolean getSubtitles(JSONArray args, CallbackContext callbackContext) {
		VpMainActivity vpMainActivity = (VpMainActivity) cordova;
		if (vpMainActivity != null) {
			vpMainActivity.getSubtitleRemoteVideo(callbackContext);
			return true;
		}
		return false;
	}

	private boolean checkDisplayEPG(JSONArray args, CallbackContext callbackContext) {
		Log.d(TAG, "checkDisplayEPG = " + args);

		boolean isShowEPG = false;
		boolean isForceShowEPG = false;

		VpMainActivity vpMainActivity = (VpMainActivity) cordova;
		if (vpMainActivity != null) {

			int visibility = webView.getVisibility();
			if (visibility == View.VISIBLE && vpMainActivity.mController != null && vpMainActivity.mController.vp9Player != null) {
				isShowEPG = true;
			}else if(visibility == View.VISIBLE && (vpMainActivity.mController == null || vpMainActivity.mController.vp9Player == null)){
				isShowEPG = true;
				isForceShowEPG = true;
			}

			try {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("isShowEPG", isShowEPG);
				jsonObj.put("isForceShowEPG", isForceShowEPG);
				callbackContext.success(jsonObj.toString());
			} catch (JSONException e) {
				callbackContext.error("fail");
				e.printStackTrace();
			}
			return true;
		}
		callbackContext.error("fail");
		return false;
	}

	private boolean setRightVideoDisplay(JSONArray args, CallbackContext callbackContext) {
		Log.d(TAG, "setRightVideoDisplay = " + args);
		VpMainActivity vpMainActivity = (VpMainActivity) cordova;
		if (vpMainActivity != null && args != null && args.length() > 0) {
			try {
				JSONObject json = args.getJSONObject(0);
				vpMainActivity.setRightVideoDisplay(json);
				return true;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	private boolean playRelateVideo(JSONArray args, CallbackContext callbackContext) {
		Log.d(TAG, "playRelateVideo = " + args);
		VpMainActivity vpMainActivity = (VpMainActivity) cordova;
		if (vpMainActivity != null && args != null && args.length() > 0) {
			try {
				JSONObject json = args.getJSONObject(0);
				vpMainActivity.playRelateVideo(json);
				return true;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	private boolean setMessage(JSONArray args, CallbackContext callbackContext) {
		VpMainActivity vpMainActivity = (VpMainActivity) cordova;
		if (vpMainActivity != null && args != null && args.length() > 0) {
			try {
				JSONObject json = args.getJSONObject(0);
				String msg = json.getString("msg");
				vpMainActivity.setMessage(msg);
				return true;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	private boolean changeScreenOrientation(JSONArray args, CallbackContext callbackContext) {
		VpMainActivity vpMainActivity = (VpMainActivity) cordova;
		if (vpMainActivity != null && args != null && args.length() > 0) {
			try {
				JSONObject json = args.getJSONObject(0);
				String orientation = json.getString("orientation");
				vpMainActivity.changeScreenOrientation(orientation);
				return true;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	private boolean changeDisplayScreen(JSONArray args, CallbackContext callbackContext) {
		VpMainActivity vpMainActivity = (VpMainActivity) cordova;
		if (vpMainActivity != null && args != null && args.length() > 0) {
			try {
				JSONObject jsonScreen = args.getJSONObject(0);
				int intFullScreen = jsonScreen.getInt("fullscreen");
				vpMainActivity.changeDisplayScreen(intFullScreen);
				return true;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	private boolean addListener(JSONArray args, CallbackContext callbackContext) {
		VpMainActivity vpMainActivity = (VpMainActivity) cordova;
		if (vpMainActivity != null) {
			vpMainActivity.addListenerRemoteVideo();
			return true;
		}
		return false;
	}

	private boolean resendInfoVideo(JSONArray args, CallbackContext callbackContext) {
		VpMainActivity vpMainActivity = (VpMainActivity) cordova;
		if (vpMainActivity != null) {
			vpMainActivity.resendInfoRemoteVideo(callbackContext);
			return true;
		}
		return false;
	}

	private boolean changeSubtitleVideo(JSONArray args, CallbackContext callbackContext) {
		VpMainActivity vpMainActivity = (VpMainActivity) cordova;
		if (vpMainActivity != null && args != null && args.length() > 0) {
			try {
				ArrayList<ChangeSubtitle> changeSubtitles = new ArrayList<ChangeSubtitle>();
				for (int i = 0; i < args.length(); i++) {
					JSONObject json = args.getJSONObject(i);
					if (json != null && json.has("subType")) {
						String subType = ParamUtil.getJsonString(json, "subType", null);
						int languageID = ParamUtil.getJsonInt(json, "languageID", -1);
						if (subType != null) {
							boolean isChoice = false;
							if (json.has("isChoice")) {
								isChoice = ParamUtil.getJSONBoolean(json, "isChoice", false);

								ChangeSubtitle changeSubtitle = new ChangeSubtitle(subType, isChoice, languageID);
								changeSubtitles.add(changeSubtitle);
							}
						}
					}
				}
				vpMainActivity.changeSubtitleRemoteVideo(changeSubtitles);
				return true;

			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
		return false;
	}

	private boolean seekVideo(JSONArray args, CallbackContext callbackContext) {
		VpMainActivity vpMainActivity = (VpMainActivity) cordova;
		if (vpMainActivity != null && args != null && args.length() > 0) {
			try {
				JSONObject jsonSeek = args.getJSONObject(0);
				if (jsonSeek != null && jsonSeek.has("time")) {
					int timeSeek = ParamUtil.getJsonInt(jsonSeek, "time", 0);
					vpMainActivity.seekRemoteVideo(timeSeek);
					return true;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
		return false;
	}

	private boolean stopVideo(JSONArray args, CallbackContext callbackContext) {
		VpMainActivity vpMainActivity = (VpMainActivity) cordova;
		if (vpMainActivity != null) {
			vpMainActivity.stopRemoteVideo();
			return true;
		}
		return false;
	}

	private boolean pauseVideo(JSONArray args, CallbackContext callbackContext) {
		VpMainActivity vpMainActivity = (VpMainActivity) cordova;
		if (vpMainActivity != null) {
			vpMainActivity.pauseRemoteVideo();
			return true;
		}
		return false;
	}

	private boolean playVideo(JSONArray args, CallbackContext callbackContext) {
		VpMainActivity vpMainActivity = (VpMainActivity) cordova;
		if (vpMainActivity != null) {
			vpMainActivity.playRemoteVideo();
			return true;
		}
		return false;
	}

	private boolean closeEPG(JSONArray args, CallbackContext callbackContext) {
		VpMainActivity vpMainActivity = (VpMainActivity) cordova;
		if (vpMainActivity != null) {
			vpMainActivity.closeEPG();
			return true;
		}
		return false;
	}

	private boolean showEPG(JSONArray args, CallbackContext callbackContext) {
		VpMainActivity vpMainActivity = (VpMainActivity) cordova;
		Log.d(TAG, "showEPG A1");
		if (vpMainActivity != null) {
			Log.d(TAG, "showEPG A2");
			vpMainActivity.showEPG();
			return true;
		}
		return false;
	}
	
	

	private boolean bindListener(JSONArray args, CallbackContext callbackContext) {
		Log.d(TAG, "bindListener");
		listenerCallbackContext = callbackContext;
		PluginResult pluginResult = new PluginResult(PluginResult.Status.OK);
		pluginResult.setKeepCallback(true);
		callbackContext.sendPluginResult(pluginResult);
		// appView.setVisibility(View.VISIBLE);
		JSONObject eventData = new JSONObject();
		try {
			eventData.put("action", "showEPG");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		reportEvent(eventData);
		return true;
	}

	private boolean startVideo(JSONArray args, final CallbackContext callbackContext) throws JSONException {
		boolean isSuccess = false;
		Log.d(TAG, "navPlayVideo");

		if (args != null && args.length() > 0) {
			final JSONObject jsonVideoInfo = args.getJSONObject(0);
			final VpMainActivity vpMainActivity = (VpMainActivity) cordova;

			if (vpMainActivity != null) {

				if (vpMainActivity.mController != null) {
					vpMainActivity.mController.cancelUpdateTimehandle();
//					mController.setMessage(Vp9Contant.MSG_RESQUEST_PLAY_VIDEO);
					Log.d(TAG, "startNativeVideo mController.destroy(1);");
					vpMainActivity.mController.destroy(1);
//					isOld = true;
				}
				int rotate = Vp9ParamUtil.getJsonInt(jsonVideoInfo, "rotate", 0);
				Log.d(TAG, "rotate: " + rotate);

				if (rotate == 0) {
					vpMainActivity.mController = new MediaController(vpMainActivity);
				} else {
					vpMainActivity.mController = new RotationMediaController(vpMainActivity);
				}
				vpMainActivity.getThreadPool().execute(new Runnable() {

					@Override
					public void run() {
						boolean newIsSuccess = false;
						try {
							newIsSuccess = vpMainActivity.startNativeVideo(jsonVideoInfo);
						} catch (Exception e) {
							e.printStackTrace();
						}
						PluginResult newPluginResult;
						if (newIsSuccess) {
							newPluginResult = new PluginResult(PluginResult.Status.OK);
						} else {
							newPluginResult = new PluginResult(PluginResult.Status.INVALID_ACTION);
						}
						callbackContext.sendPluginResult(newPluginResult);
					}
				});


			} else {
				PluginResult pluginResult = new PluginResult(PluginResult.Status.INVALID_ACTION);
				callbackContext.sendPluginResult(pluginResult);
			}

		} else {
			PluginResult pluginResult = new PluginResult(PluginResult.Status.INVALID_ACTION);
			callbackContext.sendPluginResult(pluginResult);
		}
//		callbackContext.sendPluginResult(pluginResult);
		return isSuccess;
	}

	private boolean changeSource(JSONArray args, CallbackContext callbackContext){
		try {
			if (args != null && args.length() > 0) {
				JSONObject jsonVideoInfo = args.getJSONObject(0);
				
				VpMainActivity vpMainActivity = (VpMainActivity) cordova;
				
				if (vpMainActivity != null) {
					vpMainActivity.changeSource(jsonVideoInfo);
					
					callbackContext.success();
					return true;
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
	
	private boolean clearCache(JSONArray args, CallbackContext callbackContext){
		VpMainActivity vpMainActivity = (VpMainActivity) cordova;
		
		if (vpMainActivity != null) {
			vpMainActivity.clearWebViewCache();
			
			return true;
		}
		callbackContext.error("error");
		return false;
	}
	
	public void reportEvent(JSONObject eventData) {
		Log.d(TAG, "reportEvent");
		Log.e(TAG, "reportEvent: " + eventData.toString());
		PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, eventData);
		pluginResult.setKeepCallback(true);
		if (listenerCallbackContext != null) {
			listenerCallbackContext.sendPluginResult(pluginResult);
		}
	}
}