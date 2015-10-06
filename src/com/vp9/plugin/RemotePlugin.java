package com.vp9.plugin;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vp9.laucher.main.application.Vp9Application;
import com.vp9.tv.VpMainActivity;

public class RemotePlugin extends CordovaPlugin {

	@Override
	public boolean execute(final String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException {
		cordova.getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (action.equals("run_app")) {
					handleRunApp(args, callbackContext);
				} else if (action.equals("run_launcher")) {
					handleRunLauncher(args, callbackContext);
				} else if (action.equals("open_app")) {
//					openAppVp9(args, callbackContext);
				}
			}
		});


		return super.execute(action, args, callbackContext);
	}

	private void handleRunApp(final JSONArray args, final CallbackContext callbackContext) {
		try {
			JSONObject jsonObject = args.getJSONObject(0);
			String js = jsonObject.getString("js");
			if (js != null) {
				Vp9Application vp9Application = (Vp9Application) cordova.getActivity().getApplicationContext();
				VpMainActivity activity = (VpMainActivity) vp9Application.getActivityAppVP9();
				if (activity != null) {
					activity.runApp(js);
					callbackContext.success("success");
					return;
				}
				callbackContext.error("activity null");
			}
			callbackContext.error("js null");
		} catch (JSONException e) {
			e.printStackTrace();
			callbackContext.error("JSON parse");
		}
	}

	private void handleRunLauncher(final JSONArray args, final CallbackContext callbackContext) {
		cordova.getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
//				try {
//					JSONObject jsonObject = args.getJSONObject(0);
//					String js = jsonObject.getString("js");
//					if (js != null) {
//						Vp9Application vp9Application = (Vp9Application) cordova.getActivity().getApplicationContext();
//						Vp9Launcher activity = (Vp9Launcher) vp9Application.getActivityLauncher();
//						if (activity != null) {
//							activity.runApp(js);
//							callbackContext.success("success");
//							return;
//						}
//						callbackContext.error("activity null");
//					}
//					callbackContext.error("js null");
//				} catch (JSONException e) {
//					e.printStackTrace();
//					callbackContext.error("JSON parse");
//				}
			}
		});
	}

//	private void openAppVp9(final JSONArray args, final CallbackContext callbackContext) {
//
//		Vp9Application vp9Application = (Vp9Application) cordova.getActivity().getApplicationContext();
//		VpMainActivity activity = (VpMainActivity) vp9Application.getActivityAppVP9();
//
//		if (activity != null) {
//			try {
//				String url = args.getJSONObject(0).getString("url").trim();
//				activity.loadUrl(url);
//				
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		} else {
//			try {
//				String appPackage = args.getJSONObject(0).getString("package").trim();
//				String appActivity = args.getJSONObject(0).getString("activity").trim();
//				String appType = "";
//				String appServer = "";
//				String appChannelNumber = "";
//
//				if (args.getJSONObject(0).has("type")) {
//					appType = args.getJSONObject(0).getString("type").trim();
//				}
//				if (args.getJSONObject(0).has("server")) {
//					appServer = args.getJSONObject(0).getString("server").trim();
//				}
//				if ("tivi".equals(appType)) {
//					if (args.getJSONObject(0).has("channelNumber")) {
//						appChannelNumber = args.getJSONObject(0).getString("channelNumber").trim();
//					}
//				}
//
//				Intent intents = new Intent(this.cordova.getActivity().getBaseContext(), MainActivity.class);
//				intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				intents.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//				intents.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				Bundle b = new Bundle();
//				if (appType != null && appType != "") {
//					b.putString("type", appType);
//				}
//
//				if (appServer != null && appServer != "") {
//					b.putString("server", appServer);
//				}
//
//				if (appChannelNumber != "" && appChannelNumber != null) {
//					b.putString("channelNum", appChannelNumber);
//				}
//				intents.putExtra("start", b);
//				this.cordova.getActivity().startActivity(intents);
//				
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//
//		// cordova.getActivity().runOnUiThread(new Runnable() {
//		// @Override
//		// public void run() {
//		// try {
//		// JSONObject jsonObject = args.getJSONObject(0);
//		// String js = jsonObject.getString("js");
//		// if (js != null) {
//		// Vp9Application vp9Application = (Vp9Application)
//		// cordova.getActivity().getApplicationContext();
//		// Vp9Launcher activity = (Vp9Launcher)
//		// vp9Application.getActivityLauncher();
//		// if (activity != null) {
//		// activity.runApp(js);
//		// callbackContext.success("success");
//		// return;
//		// }
//		// callbackContext.error("activity null");
//		// }
//		// callbackContext.error("js null");
//		// } catch (JSONException e) {
//		// e.printStackTrace();
//		// callbackContext.error("JSON parse");
//		// }
//		// }
//		// });
//	}

}
