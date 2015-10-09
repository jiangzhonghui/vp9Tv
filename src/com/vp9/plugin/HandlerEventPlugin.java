package com.vp9.plugin;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tv.vp9.videoproxy.MyService;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.AssetManager;
import android.graphics.Point;
import android.net.TrafficStats;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.os.StatFs;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.vp9.laucher.main.util.MyPreference;
import com.vp9.player.controller.MediaController;
import com.vp9.tv.KaraokeActivity;
import com.vp9.tv.R;
import com.vp9.tv.VpMainActivity;
import com.vp9.util.AppPreferences;
import com.vp9.util.Config;
import com.vp9.util.FileUtil;
import com.vp9.util.ParamUtil;
import com.vp9.view.MagicTextView;

public class HandlerEventPlugin extends CordovaPlugin {

	private String TAG = "HandlerEventPlugin";

	private static int uid;
	private static long totalRxBefore;
	private static long totalTxBefore;
	private static long BeforeTime;
	
	public static final String ACTION_START_SERVICE_PROXY = "startserviceproxy";
	
	public static final String ACTION_STOP_SERVICE_PROXY = "stopserviceproxy";

	// traffic

	private double[] arrayTraffic = new double[5];
	private static int index = -1;
	private int len = 0;
	Timer timerUpdateTraffic;

	FrameLayout textViewChannelNum;

	int[] ids = new int[] { R.id.tv1, R.id.tv2, R.id.tv3, R.id.tv4, R.id.tv5 };
	MagicTextView[] textview = new MagicTextView[5];
	String[] data = new String[5];

	// end traffic
	String batteryInfo = "";
	private BroadcastReceiver batteryInfoReceiver = null;

	@Override
	public boolean execute(final String action, final JSONArray args, final CallbackContext callbackContext) {

		cordova.getThreadPool().execute(new Runnable() {

			@Override
			public void run() {
				if (batteryInfoReceiver == null) {
					batteryInfoReceiver = new BroadcastReceiver() {
						@Override
						public void onReceive(Context context, Intent intent) {

							int health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, 0);
							int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
							int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0);
							boolean present = intent.getExtras().getBoolean(BatteryManager.EXTRA_PRESENT);
							int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 0);
							int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, 0);
							String technology = intent.getExtras().getString(BatteryManager.EXTRA_TECHNOLOGY);
							int temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0);
							int voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0);

							batteryInfo = "Health: " + health + "\n" + "Level: " + level + "\n" + "Plugged: " + plugged + "\n" + "Present: " + present + "\n" + "Scale: " + scale + "\n" + "Status: " + status
									+ "\n" + "Technology: " + technology + "\n" + "Temperature: " + temperature + "\n" + "Voltage: " + voltage + "\n";
						}
					};
					cordova.getActivity().registerReceiver(batteryInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
				}
				if(action.equals("quit_tv")){
					handleQuitTV(callbackContext);
				}else if(action.equals("launchChrome")){
					handleLaunchChrome(callbackContext, args);
				}else if(action.equals("playVideo")){
					handlePlayVideo(callbackContext);
				}else if(action.equals("install_app")){
					handleInstallApp(callbackContext, args);
				}else if(action.equals("back")){
					handleBack(callbackContext);
				}else if(action.equals("getVersion")){
					handleGetVersion(callbackContext);
				}else if(action.equals("getVersionAnotherApp")){
					handleVersionAnotherApp(callbackContext, args);
				}else if (action.equals("set_tivi_channel")) {
					handleSetTvChannel(callbackContext);
				}else if (action.equals("cancel_tivi_channel")) {
					handleCancelTvChannel(callbackContext);
				}else if (action.equals("gohomelauncher")) {
					handleGoHomeLauncher(callbackContext);
				}else if (action.equals("govp9launcher")) {
					handleGoVp9Launcher(callbackContext);
				} else if (action.equals("enable_traffic")) {
					handleTraffic(callbackContext);
				} else if (action.equals("disable_traffic")) {
					handleDisableTraffic(callbackContext);
				} else if (action.equals("httprequest")) {
					handleHttpRequest(callbackContext, args);
				} else if (action.equals("httpRequestGetService")) {
					handleHttpRequestGetService(callbackContext, args);
				}else if (action.equals("getChannelList")) {
					handleGetChannelList(callbackContext, args);
				} else if (action.equals("confirmbackbutton")) {
					handleConfirmBackButton(callbackContext);
				} else if (action.equals("startvideonativeapp")) {
					handleStartVideoNativeApp(callbackContext, args);
				} else if (action.equals("isnativeappexist")) {

					Intent i = new Intent();
					i.setClassName("com.vp9.player", "com.vp9.player.Vp9Player");
					// ResolveInfo resolveActivity =
					// cordova.getActivity().getPackageManager().resolveActivity(i,
					// PackageManager.MATCH_DEFAULT_ONLY);
					List list = cordova.getActivity().getPackageManager().queryIntentActivities(i, PackageManager.MATCH_DEFAULT_ONLY);

					if (list.size() > 0) {
						callbackContext.success("success");
					} else {
						callbackContext.error("fail");
					}
				} else if (action.equals("getAndroidId")) {
					String android_id = Secure.getString(cordova.getActivity().getContentResolver(), Secure.ANDROID_ID);
					/*
					 * JSONObject json = new JSONObject(); json.put("androidId",
					 * android_id); callbackContext.success(json.toString());
					 */
					callbackContext.success(android_id);
				} else if (action.equals("visibleChannelNumber")) {
					JSONObject json;
					String view;
					try {
						json = args.getJSONObject(0);

						JSONArray channelNumber = null;
						if (json != null) {
							channelNumber = (JSONArray) json.getJSONArray("channelNumber");
						}
						// if (json.has("view")) {
						// view = json.getString("view");
						// }
						VpMainActivity vp = (VpMainActivity) cordova.getActivity();
						
						MediaController mc = (MediaController) vp.mController;
						
						mc.isClickSetting = true;
						
						Log.e(HandlerEventPlugin.class.getSimpleName(), "channelnumber: " + channelNumber.toString());

						if (channelNumber != null && channelNumber.length() > 0) {
							for (int i = 0; i < channelNumber.length(); i++) {

								data[i] = channelNumber.get(i).toString();
								Log.e(HandlerEventPlugin.class.getSimpleName(), "-----------: " + channelNumber.get(i).toString() + " - " + data[i]);
							}
						}

						if (channelNumber != null) {

							// if (textViewChannelNum != null) {
							// textViewChannelNum.setVisibility(View.VISIBLE);;
							// textViewChannelNum.setText(channelNumber);
							// }else{
							// textViewChannelNum = new
							// TextView(cordova.getActivity());
							// textViewChannelNum.setPadding(50, 30, 30, 30);
							// textViewChannelNum.setTextSize(42);
							// textViewChannelNum.setTypeface(textViewChannelNum.getTypeface(),
							// Typeface.BOLD);
							// textViewChannelNum.setTextColor(Color.BLUE);
							// textViewChannelNum.setVisibility(View.VISIBLE);
							// textViewChannelNum.setText(channelNumber);
							// LayoutParams params = new LayoutParams(500, 200);
							// cordova.getActivity().addContentView(textViewChannelNum,
							// params);
							// }

							if (textViewChannelNum != null) {
								cordova.getActivity().runOnUiThread(new Runnable() {
									public void run() {
										textViewChannelNum.setVisibility(View.VISIBLE);
										for (int i = 0; i < textview.length; i++) {
											textview[i].setText(data[i]);
										}
									}
								});
							} else {

								textViewChannelNum = (FrameLayout) cordova.getActivity().getLayoutInflater().inflate(R.layout.customtextview, null);
								cordova.getActivity().runOnUiThread(new Runnable() {
									public void run() {
										for (int i = 0; i < textview.length; i++) {
											textview[i] = (MagicTextView) textViewChannelNum.findViewById(ids[i]);
											textview[i].setText(data[i]);
										}
										LayoutParams params = new LayoutParams(500, 700);
										cordova.getActivity().addContentView(textViewChannelNum, params);
									}
								});
							}
							callbackContext.success("success");
						} else {
							callbackContext.error("fail");
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else if (action.equals("invisibleChannelNumber")) {
					cordova.getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// Vp9Application vp9Application = (Vp9Application)
							// HandlerEventPlugin.this.cordova.getActivity().getApplication();
							// textViewChannelNum =
							// vp9Application.getTextViewChannelNum();
							// if (textViewChannelNum != null &&
							// textViewChannelNum.getVisibility() == View.VISIBLE) {
							// Log.e(HandlerEventPlugin.class.getSimpleName(),
							// "textViewChannelNum visible");
							// textViewChannelNum.setVisibility(View.INVISIBLE);
							// callbackContext.success("success");
							//
							// }else{
							// Log.e(HandlerEventPlugin.class.getSimpleName(),
							// "textViewChannelNum invisible");
							// callbackContext.success("success");
							// }
							// VpMainActivity vpMainActivity = (VpMainActivity)
							// cordova.getActivity();
							// TextView textViewTvChannel =
							// vpMainActivity.getTextViewTvChannel();
							//
							// if (textViewTvChannel != null) {
							// textViewTvChannel.setText("");
							// textViewTvChannel.setVisibility(View.INVISIBLE);
							// callbackContext.success("success");
							// }

							// if (textViewChannelNum != null) {
							// // textViewChannelNum.setText("");
							// textViewChannelNum.setVisibility(View.INVISIBLE);
							//
							// callbackContext.success("success");
							// }

							if (textViewChannelNum != null) {
								textViewChannelNum.setVisibility(View.INVISIBLE);
								for (int i = 0; i < textview.length; i++) {
									textview[i].setText("");
									data[i] = "";
								}
								callbackContext.success("success");
							}
						}
					});
				} else if (action.equals("checkCurrentActivityRunning")) {
					ActivityManager am = (ActivityManager) cordova.getActivity().getSystemService(Context.ACTIVITY_SERVICE);

					boolean result = checkCurrentActivityRunning(am);

					if (result) {
						callbackContext.success("success");
					} else {
						callbackContext.error("fail");
					}
				} else if (action.equals("getFreeSpaceSdcard")) {
					handleGetFreeSpaceSdcard(callbackContext);
				} else if (action.equals("getPlayerInApp")) {
					handleGetPlayerInApp(callbackContext);
				} else if (action.equals("getVersionInApp")) {
					String version = Config.VERSION;

					callbackContext.success(version);
				} else if (action.equals("showKeyboard")) {
					showKeyboard();
					callbackContext.success("success");
				} else if (action.equals("hideKeyboard")) {
					hideKeyboard();
					callbackContext.success("success");
				} else if (action.equals("getCPU")) {

					final VpMainActivity VpMain = (VpMainActivity) cordova.getActivity();
					final MyService proxySevice = VpMain.getProxySevice();
					
					cordova.getThreadPool().execute(new Runnable() {

						@Override
						public void run() {
							int[] cpuUsageStatistic = getCpuUsageStatistic();
							long[] freeAndTotalMemorySize = getFreeAndTotalMemorySize();

//							Display display = cordova.getActivity().getWindowManager().getDefaultDisplay();
//							Point point = MediaController.getSize(display);
							
							double speed=0;
							double expectedSpeed = 0;
							if(VpMain.mController != null){
								return;
							}
							int proxy = VpMain.mController.intProxy;
							if (proxySevice != null && proxy == 1) {
								int intError = proxySevice.get_last_error();
								
								if(intError == 0){
									speed = proxySevice.get_speed(0);
									expectedSpeed = proxySevice.get_expected_speed(0);
								}
							}
							
							JSONObject object = new JSONObject();
							try {
								if (cpuUsageStatistic != null && cpuUsageStatistic.length > 1) {
									object.put("cpu_user", cpuUsageStatistic[0]);
									object.put("cpu_system", cpuUsageStatistic[1]);
								}
								if (freeAndTotalMemorySize != null && freeAndTotalMemorySize.length > 1) {
									object.put("free_memory", freeAndTotalMemorySize[0]);
									object.put("total_memory", freeAndTotalMemorySize[1]);
								}
								object.put("battery", batteryInfo);
								object.put("proxy", proxy);
								object.put("speed", speed);
								object.put("expected_speed", expectedSpeed);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								callbackContext.error("fail");
							}
							callbackContext.success(object);
						}
					});


				} else if (ACTION_START_SERVICE_PROXY.equals(action)) {
					handleStartServiceProxy(args, callbackContext);
				} else if (ACTION_STOP_SERVICE_PROXY.equals(action)) {
					handleStopServiceProxy(args, callbackContext);
				}

			}
		});
		

		return true;
	}

	protected void handleGetPlayerInApp(CallbackContext callbackContext) {
		try {
			String arrPlayer = Config.PLAYERS;
			JSONArray jsonArray = new JSONArray(arrPlayer);

			callbackContext.success(jsonArray.toString());	
		} catch (JSONException e) {
			callbackContext.error("fail");
			e.printStackTrace();
		}
		
	}

	protected void handleGetFreeSpaceSdcard(CallbackContext callbackContext) {
		try {
			// StatFs stat = new
			// StatFs(Environment.getExternalStorageDirectory().getPath());
			// double sdAvailSize = (double)stat.getAvailableBlocks() *
			// (double)stat.getBlockSize();

			long usableSpace = Environment.getExternalStorageDirectory().getUsableSpace();
			// Log.d(TAG, "sdAvailSize: " + sdAvailSize);
			Log.d(TAG, "usableSpace: " + usableSpace);

			JSONObject json = new JSONObject();
			json.put("freeSpace", usableSpace);
			callbackContext.success(json.toString());	
		} catch (JSONException e) {
			callbackContext.error("fail");
			e.printStackTrace();
		}
		
	}

	protected void handleStartVideoNativeApp(CallbackContext callbackContext,
			JSONArray args) {
		try {
			JSONObject json = args.getJSONObject(0);

//			Toast.makeText(cordova.getActivity(), json.toString(), Toast.LENGTH_LONG).show();
			/**/

			try {
				ComponentName name = new ComponentName("com.vp9.player", ".Vp9Player");
				Intent i = new Intent(Intent.ACTION_MAIN);

				i.addCategory(Intent.CATEGORY_LAUNCHER);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
				i.setComponent(name);

				Bundle b = new Bundle();
				b.putString("videoInfo", json.toString());
				i.putExtras(b);

				cordova.getActivity().startActivity(i);

				// cordova.getActivity().finish();

			} catch (Exception e) {
				e.printStackTrace();
				ComponentName name = new ComponentName("com.vp9.player", "com.vp9.player.Vp9Player");
				Intent i = new Intent(Intent.ACTION_MAIN);

				i.addCategory(Intent.CATEGORY_LAUNCHER);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
				i.setComponent(name);

				Bundle b = new Bundle();
				b.putString("videoInfo", json.toString());
				i.putExtras(b);

				cordova.getActivity().startActivity(i);
			}

			callbackContext.success("success");
		} catch (JSONException e) {
			callbackContext.error("fail");
			e.printStackTrace();
		}
		
	}

	protected void handleConfirmBackButton(CallbackContext callbackContext) {
		SharedPreferences preference = MyPreference.getPreference(cordova.getActivity());
		MyPreference.setBoolean("confirm_backbutton", true);
		callbackContext.success("success");
	}

	protected void handleGetChannelList(CallbackContext callbackContext,
			JSONArray args) {
		try {
			JSONObject json = args.getJSONObject(0);

			String address = null;
			if (json == null || !json.has("server_address")) {
				callbackContext.error("fail get Channel list");
			}

			address = json.getString("server_address");
			if (address == null || address == "") {
				callbackContext.error("fail get Channel list");
			}

			String serverId = json.getString("server_id");
			String serverName = json.getString("server_name");

			new HttpRequestGetChannelListAsyncTask(callbackContext).execute(new String[] { address, serverId, serverName });
//			callbackContext.success("success");
		} catch (JSONException e) {
			callbackContext.error("fail");
			e.printStackTrace();
		}


		
	}

	protected void handleHttpRequestGetService(CallbackContext callbackContext, JSONArray args) {
		try {
			JSONObject json = args.getJSONObject(0);
			
			String address = null;

			if (json == null || !json.has("url")) {
				callbackContext.error("fail");
			}

			address = json.getString("url");
			if (address == null || address == "") {
				callbackContext.error("fail");
			}

			new httpRequestGetServiceAsyncTask(callbackContext).execute(address);
//			callbackContext.success("success");
		} catch (JSONException e) {
			callbackContext.error("fail");
			e.printStackTrace();
		}
	}

	protected void handleHttpRequest(CallbackContext callbackContext, JSONArray args) {
		try {
			JSONObject json = args.getJSONObject(0);
			String address = null;

			if (json == null || !json.has("url")) {
				callbackContext.error("fail");
			}

			address = json.getString("url");
			if (address == null || address == "") {
				callbackContext.error("fail");
			}

			new httpRequestAsyncTask(callbackContext).execute(address);
//			callbackContext.success("success");
			
		} catch (JSONException e) {
			callbackContext.error("fail");
			e.printStackTrace();
		}

	}

	protected void handleDisableTraffic(CallbackContext callbackContext) {
		if (timerUpdateTraffic != null) {
			timerUpdateTraffic.cancel();
		}
		callbackContext.success("success");
	}

	protected void handleGoVp9Launcher(CallbackContext callbackContext) {
		this.cordova.getActivity().finish();
		callbackContext.success("success");
	}

	protected void handleGoHomeLauncher(CallbackContext callbackContext) {
		Log.d(TAG, "--------------- gohomelauncher : ");

		// Intent startMain = new Intent(cordova.getActivity(),
		// Vp9Launcher.class);
		// startMain.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		// startMain.setAction("android.intent.action.MAIN");
		// startMain.addCategory("android.intent.category.LAUNCHER");
		// this.cordova.getActivity().startActivity(startMain);

		Intent intent = cordova.getActivity().getIntent();
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		// cordova.getActivity().finish();
		cordova.getActivity().startActivity(intent);

		// Intent startMain = new Intent(Intent.ACTION_MAIN);
		// startMain.addCategory(Intent.CATEGORY_HOME);
		// startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// startMain.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		// startMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		//
		// this.cordova.getActivity().startActivity(startMain);

		// try{
		// ComponentName name=new
		// ComponentName("com.vp9.laucher.main.vp9launcher",
		// ".Vp9Launcher");
		// Intent i=new Intent(Intent.ACTION_MAIN);
		//
		// i.addCategory(Intent.CATEGORY_LAUNCHER);
		// i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		// i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		// i.setComponent(name);
		//
		// this.cordova.getActivity().startActivity(i);
		// }catch(Exception e){
		// ComponentName name=new
		// ComponentName("com.vp9.laucher.main.vp9launcher",
		// "com.vp9.laucher.main.vp9launcher.Vp9Launcher");
		// Intent i=new Intent(Intent.ACTION_MAIN);
		//
		// i.addCategory(Intent.CATEGORY_LAUNCHER);
		// i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		// i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		// i.setComponent(name);
		//
		// this.cordova.getActivity().startActivity(i);
		// }

		callbackContext.success("success");
		
	}

	protected void handleCancelTvChannel(CallbackContext callbackContext) {
		AppPreferences.INSTANCE.saveIsChannelNumber(false);
		callbackContext.success("success");
		
	}

	protected void handleSetTvChannel(CallbackContext callbackContext) {
		AppPreferences.INSTANCE.saveIsChannelNumber(true);
		callbackContext.success("success");
		
	}

	protected void handleVersionAnotherApp(CallbackContext callbackContext,
			JSONArray args) {
		try {
			JSONObject jsonObject = args.getJSONObject(0);
			String packageName = null;
			if (jsonObject == null || !jsonObject.has("package")) {
				callbackContext.error("fail");
				return;
			}

			packageName = jsonObject.get("package").toString();

			PackageManager manager = webView.getContext().getPackageManager();
			String versionName = "0";
			try {
				PackageInfo info = manager.getPackageInfo(packageName, 0);
				if (info == null) {
					callbackContext.success(versionName);
					return;
				}
				versionName = info.versionName;
				callbackContext.success(versionName);
				return;
			} catch (NameNotFoundException e) {
				Log.e(TAG, e.getMessage());
				callbackContext.success(versionName);
				return;
			}
		} catch (JSONException e) {
			callbackContext.error("fail");
			e.printStackTrace();
		}


		
	}

	protected void handleGetVersion(CallbackContext callbackContext) {
		PackageManager manager = webView.getContext().getPackageManager();
		String versionName = "";
		try {
			PackageInfo info = manager.getPackageInfo(webView.getContext().getPackageName(), 0);
			versionName = info.versionName;
			callbackContext.success(versionName);
		} catch (NameNotFoundException e) {
			Log.e(TAG, e.getMessage());
			callbackContext.error("fail");
		}
	}

	protected void handleBack(CallbackContext callbackContext) {
		try {
			webView.backHistory();
			callbackContext.success("success");
		} catch (IllegalStateException e) {
			callbackContext.error("fail");
			e.printStackTrace();
		}
		
	}

	protected void handleInstallApp(CallbackContext callbackContext, JSONArray args) {
		try {
			if (args != null && args.length() > 0) {
				JSONObject obj = args.getJSONObject(0);
				if (obj != null && obj.getString("url") != null) {
					String downloadUrl = obj.getString("url");
					String[] strUrls = downloadUrl.split("/");
					if (strUrls != null && strUrls.length > 0 && strUrls[strUrls.length - 1] != null && strUrls[strUrls.length - 1].endsWith(".apk")) {
						String appName = strUrls[strUrls.length - 1];
						try {
							final DownloadFile downloadFile = new DownloadFile(downloadUrl, appName);
							downloadFile.execute();
						} catch (Exception e) {
							Log.e("Download app", "Update error! " + e.getMessage());
						}
					}
				}
				callbackContext.success("success");
			}else{
				callbackContext.error("fail");
			}
		} catch (JSONException e) {
			callbackContext.error("fail");
			e.printStackTrace();
		}

		
		
	}

	protected void handlePlayVideo(CallbackContext callbackContext) {
		try {
			webView.loadUrl("javascript:handlerVideo()");
			callbackContext.success("success");
		} catch (IllegalStateException e) {
			e.printStackTrace();
			callbackContext.error("fail");
		}
		
	}

	protected void handleLaunchChrome(CallbackContext callbackContext, JSONArray args){
		try {
			if (args != null && args.length() > 0) {
				JSONObject obj = args.getJSONObject(0);
				String url_karao = obj.getString("url_karao");
				int id = ParamUtil.getValue(obj.getString("id"), -1);
				if (id > 0) {
					String packageName = "com.android.chrome";
					// String packageName = "com.chrome.beta";
					Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url_karao));
					browserIntent.putExtra("force_fullscreen", true);
					browserIntent.setPackage(packageName);
					browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

					List<ResolveInfo> activitiesList = webView.getContext().getPackageManager().queryIntentActivities(browserIntent, -1);
					if (activitiesList.size() > 0) {
						// Found the browser on the device, launch it
						webView.getContext().startActivity(browserIntent);
					} else {
						// // The browser isn't installed, so we should prompt
						// the user to get
						// Intent playStoreIntent = new
						// Intent(Intent.ACTION_VIEW);
						// //
						// playStoreIntent.setData(Uri.parse("market://details?id="+packageName));
						// playStoreIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						// webView.getContext().startActivity(playStoreIntent);

						AlertDialog.Builder builder = new AlertDialog.Builder(webView.getContext());
						builder.setTitle("Quit");
						builder.setMessage("Khong play duoc video. Thiet bi khong cai dat ung dung chrome");
						builder.setCancelable(true);

						builder.setPositiveButton("Okie", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.dismiss();
							}
						});

						builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.dismiss();
							}
						});

						final AlertDialog dlg = builder.create();
						dlg.show();

						final Timer t = new Timer();
						t.schedule(new TimerTask() {
							public void run() {
								dlg.dismiss();
								t.cancel();
							}
						}, 15000);
					}
				}
				callbackContext.success("success");
			}	
		} catch (JSONException e) {
			callbackContext.error("fail");
		}

		
	}

	protected void handleQuitTV(CallbackContext callbackContext) {
		this.cordova.getActivity().finish();
		callbackContext.success("success");
		
	}

	protected void handleStartServiceProxy(JSONArray args,
			CallbackContext callbackContext) {
//		updateLibraryFiles();
		FileUtil.updateLibraryFiles((Activity) cordova);
		MyService proxySevice = new MyService();
		proxySevice.startProxy();
		Log.d(TAG, "VP9-Proxy Starting");
		
		if(cordova instanceof VpMainActivity){
			VpMainActivity vpMainActivity = (VpMainActivity) cordova;
			if (vpMainActivity != null) {
				vpMainActivity.setProxySevice(proxySevice);
			}
		}else if(cordova instanceof KaraokeActivity){
			KaraokeActivity mainActivity = (KaraokeActivity) cordova;
			if (mainActivity != null) {
				mainActivity.setProxySevice(proxySevice);
			}
		}
		Log.d(TAG, "VP9-Proxy Start: success");
		callbackContext.success("success");
	}
	
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
	
	private void updateLibraryFiles() {
		Log.d(TAG, "loading library proxy");
		boolean isExistLibDir = true;
		String libsDirPath = cordova.getActivity().getFilesDir().getParent() + "/libs";
		File libsDir = new File(libsDirPath);
		if (!libsDir.exists()) {
			isExistLibDir = false;
			boolean isMkdir = libsDir.mkdir();
			if (!isMkdir) {
				return;
			}
		}

		AssetManager assetManager = cordova.getActivity().getResources().getAssets();
		try {
			String[] proxyLibNames = assetManager.list("proxyLibs");
			if (proxyLibNames != null && proxyLibNames.length > 0) {
				for (int i = 0; i < proxyLibNames.length; i++) {
					Log.d(TAG, "proxyLibName: " + proxyLibNames[i]);
					if (!isExistLibDir || !checkExistFile(libsDirPath + "/" + proxyLibNames[i])) {
						InputStream inputStream = assetManager.open("proxyLibs" + "/" + proxyLibNames[i]);
						if (inputStream != null) {
							String filePath = libsDirPath + "/" + proxyLibNames[i];
							writeFile(inputStream, filePath);
;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
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
	


	protected void exitApp() {
		android.os.Process.killProcess(android.os.Process.myPid());
		super.onDestroy();
		System.exit(0);

	}

	private class DownloadFile extends AsyncTask<String, Integer, String> {

		ProgressDialog mProgressDialog;
		private String downloadUrl;
		private String appName;

		public boolean isInstall;

		// public boolean isFinish;

		public DownloadFile(String downloadUrl, String appName) {
			this.downloadUrl = downloadUrl;
			this.appName = appName;
			this.isInstall = true;
			// this.isFinish = false;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Create ProgressBar
			mProgressDialog = new ProgressDialog(webView.getContext());
			// Set your ProgressBar Title
			mProgressDialog.setTitle("Downloads");

			// Set your ProgressBar Message
			mProgressDialog.setMessage("Downloading app game for install, Please Wait!");
			mProgressDialog.setIndeterminate(false);
			mProgressDialog.setMax(100);
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			// Show ProgressBar
			mProgressDialog.setCancelable(false);
			// mProgressDialog.setCanceledOnTouchOutside(false);
			mProgressDialog.show();
		}

		@Override
		protected String doInBackground(String... sUrl) {
			HttpURLConnection c = null;
			try {

				URL url = new URL(downloadUrl);
				c = (HttpURLConnection) url.openConnection();
				c.setRequestMethod("GET");
				c.setDoOutput(true);
				c.connect();

				String PATH = "/mnt/sdcard/Download/";

				File file = new File(PATH);
				file.mkdirs();
				File outputFile = new File(file, appName);
				if (outputFile.exists()) {
					outputFile.delete();
				}
				FileOutputStream fos = new FileOutputStream(outputFile);

				InputStream is = c.getInputStream();

				int fileLength = c.getContentLength();

				long availableSpaceInBytes = getAvailableSpaceInBytes();

				if (fileLength >= availableSpaceInBytes) {
					// displayQuitDownloadDiaglog();
					isInstall = false;
				} else {
					byte[] buffer = new byte[1024];
					long total = 0;
					int count = 0;
					while ((count = is.read(buffer)) != -1) {
						total += count;
						// Publish the progress
						publishProgress((int) (total * 100 / fileLength));
						fos.write(buffer, 0, count);
					}
					fos.close();
					is.close();
				}
				c.disconnect();
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}finally{
				if(c != null){
					c.disconnect();
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			mProgressDialog.dismiss();
			if (isInstall) {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setDataAndType(Uri.fromFile(new File("/mnt/sdcard/Download/" + appName)), "application/vnd.android.package-archive");
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // without this
																// flag android
																// returned a
																// intent error!
				webView.getContext().startActivity(intent);
			} else {
				displayQuitDownloadDiaglog();
			}

			// isFinish = true;
		}

		@Override
		protected void onProgressUpdate(Integer... progress) {
			super.onProgressUpdate(progress);
			// Update the ProgressBar
			mProgressDialog.setProgress(progress[0]);

		}

		public long getAvailableSpaceInBytes() {
			long availableSpace = -1L;
			// StatFs stat = new
			// StatFs(Environment.getExternalStorageDirectory().getPath());
			StatFs stat = new StatFs("/mnt/sdcard");
			availableSpace = (long) stat.getAvailableBlocks() * (long) stat.getBlockSize();

			return availableSpace;
		}

	}

	private class httpRequestAsyncTask extends AsyncTask<String, Void, Boolean> {
		CallbackContext callbackContext;

		public httpRequestAsyncTask(CallbackContext callback) {
			this.callbackContext = callback;
		}

		protected Boolean doInBackground(String... urls) {
			try {
				StringBuilder builder = new StringBuilder();
				URL url = new URL(urls[0]);
				HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
				urlConnection.setConnectTimeout(3000);
				try {
					InputStream in = new BufferedInputStream(urlConnection.getInputStream());
					BufferedReader reader = new BufferedReader(new InputStreamReader(in));
					String line;
					while ((line = reader.readLine()) != null) {
						builder.append(line + "\n");
					}
				} catch(Exception e){
					this.callbackContext.error("fail");
				}finally {
					urlConnection.disconnect();
				}

				this.callbackContext.success(builder.toString());
				return true;
			} catch (Exception e) {
				this.callbackContext.error("fail");
				return false;
			}
		}

		protected void onPostExecute(String feed) {
			// TODO: check this.exception
			// TODO: do something with the feed
		}
	}

	private class httpRequestGetServiceAsyncTask extends AsyncTask<String, Void, Boolean> {
		CallbackContext callbackContext;

		public httpRequestGetServiceAsyncTask(CallbackContext callback) {
			this.callbackContext = callback;

		}

		protected Boolean doInBackground(String... urls) {
			try {
				StringBuilder builder = new StringBuilder();
				URL url = new URL(urls[0]);
				HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
				urlConnection.setConnectTimeout(3000);
				try {
					InputStream in = new BufferedInputStream(urlConnection.getInputStream());
					BufferedReader reader = new BufferedReader(new InputStreamReader(in));
					String line;
					while ((line = reader.readLine()) != null) {
						builder.append(line.trim());
					}
				}catch(Exception e){
					this.callbackContext.error("fail");
				} finally {
					urlConnection.disconnect();
				}

				this.callbackContext.success(builder.toString());
				return true;
			} catch (Exception e) {
				this.callbackContext.error("fail");
				return false;
			}
		}

		protected void onPostExecute(String feed) {
			// TODO: check this.exception
			// TODO: do something with the feed
		}
	}

	private class HttpRequestGetChannelListAsyncTask extends AsyncTask<String, Void, Boolean> {
		CallbackContext callbackContext;

		public HttpRequestGetChannelListAsyncTask(CallbackContext callback) {
			this.callbackContext = callback;

		}

		protected Boolean doInBackground(String... urls) {
			try {

				StringBuilder builder = new StringBuilder();
				URL url = new URL(urls[0]);
				HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
				urlConnection.setConnectTimeout(3000);
				try {
					InputStream in = new BufferedInputStream(urlConnection.getInputStream());
					BufferedReader reader = new BufferedReader(new InputStreamReader(in));
					String line;
					while ((line = reader.readLine()) != null) {
						builder.append(line.trim());
					}
				} finally {
					urlConnection.disconnect();
				}

				JSONObject jsonObj = new JSONObject();
				jsonObj.put("server_id", urls[1]);
				jsonObj.put("server_name", urls[2]);
				jsonObj.put("channel_list", builder.toString());

				this.callbackContext.success(jsonObj.toString());
				return true;
			} catch (Exception e) {
				this.callbackContext.error("fail");
				return false;
			}
		}

		protected void onPostExecute(String feed) {
			// TODO: check this.exception
			// TODO: do something with the feed
		}
	}

	public void displayQuitDownloadDiaglog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(webView.getContext());
		builder.setTitle("Install Fail");
		builder.setMessage("SDcard is not enough storage space, please remove some apps and files.");
		builder.setCancelable(true);

		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.dismiss();
			}
		});

		AlertDialog _alert = builder.create();
		_alert.show();

	}

	public void handleTraffic(CallbackContext callbackContext) {
		String packageName = "com.vp9.tv";
		final PackageManager pm = cordova.getActivity().getPackageManager();
		try {
			ApplicationInfo applicationInfo = pm.getApplicationInfo(packageName, 0);
			uid = applicationInfo.uid;
			
		} catch (NameNotFoundException e) {
			callbackContext.error("fail");
			e.printStackTrace();
		}
		BeforeTime = System.currentTimeMillis();
		// totalRxBefore = TrafficStats.getUidRxBytes(uid);
		// totalTxBefore = TrafficStats.getUidTxBytes(uid);

		totalRxBefore = TrafficStats.getTotalRxBytes();
		totalTxBefore = TrafficStats.getTotalTxBytes();

		timerUpdateTraffic = new Timer();
		timerUpdateTraffic.schedule(new TimerTask() {
			@Override
			public void run() {

				cordova.getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// long totalRxAfter = TrafficStats.getUidRxBytes(uid);
						// long totalTxAfter = TrafficStats.getUidTxBytes(uid);
						long totalRxAfter = TrafficStats.getTotalRxBytes();
						long totalTxAfter = TrafficStats.getTotalTxBytes();

						long AfterTime = System.currentTimeMillis();
						double TimeDifference = AfterTime - BeforeTime;
						double rxDiff = totalRxAfter - totalRxBefore;
						double txDiff = totalTxAfter - totalTxBefore;

						totalRxBefore = totalRxAfter;
						totalTxBefore = totalTxAfter;
						BeforeTime = AfterTime;

						if ((rxDiff >= 0) && (txDiff >= 0)) {
							double rxBPS = ((rxDiff) / (TimeDifference / 1000)); // total
																					// rx
																					// bytes
																					// per
																					// second.
							double txBPS = ((txDiff) / (TimeDifference / 1000)); // total
																					// tx
																					// bytes
																					// per
																					// second.

							/*
							 * RX.setText(String.valueOf(rxBPS) +
							 * "KBps. Total rx = " + rxDiff);
							 * TX.setText(String.valueOf(txBPS) +
							 * "KBps. Total tx = " + txDiff);
							 */

							index = (index + 1) % 5;
							len = len + 1 - (len / 5);

							arrayTraffic[index] = txBPS;
							double resultSpeed = 0;
							for (int i = 0; i < arrayTraffic.length; i++) {
								resultSpeed += arrayTraffic[i];
							}
							resultSpeed = resultSpeed / len / 1024;
							HandlerEventPlugin.this.webView.sendJavascript("handleTrafficClient('" + String.format("%.2f", rxBPS) + "', '" + String.format("%.0f", resultSpeed) + "KB/s')");

						} else {
							HandlerEventPlugin.this.webView.sendJavascript("handleTrafficClient(' No uploaded or downloaded bytes.' , 'No uploaded or downloaded bytes.')");
						}

					}
				});
			}
		}, 0, 1000);
		callbackContext.success("success");
	}

	private static boolean deleteDir(File dir) {
		if (dir != null && dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
			// dir.delete(); // delete the directory ITSELF. Grayed out, because
			// it cripples the app until the app is fully restarted
		} else if (dir != null && dir.isFile()) {
			dir.delete(); // delete the file INSIDE the directory
		}
		return true;
	}

	public static boolean checkCurrentActivityRunning(ActivityManager am) {
		// ActivityManager am = (ActivityManager)
		// this.cordova.getActivity().getSystemService(Context.ACTIVITY_SERVICE);

		List<ActivityManager.RunningTaskInfo> alltasks = am.getRunningTasks(1);
		for (ActivityManager.RunningTaskInfo aTask : alltasks) {
			if (aTask.topActivity.getClassName().equals("com.vp9.tv.VpMainActivity") || aTask.topActivity.getClassName().equals("com.vp9.laucher.main.vp9launcher.Vp9Launcher")) {
				return true;
			}
		}
		return false;
	}

	private void showKeyboard() {
		InputMethodManager imm = (InputMethodManager) cordova.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);

		// cordova.getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
	}

	private void hideKeyboard() {
		InputMethodManager imm = (InputMethodManager) cordova.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

		// cordova.getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}

	/*
	 * */
	private int[] getCpuUsageStatistic() {

		String tempString = executeTop();
		int[] cpuUsageAsInt = null;
		if (tempString != null) {
			tempString = tempString.replaceAll(",", "");
			tempString = tempString.replaceAll("User", "");
			tempString = tempString.replaceAll("System", "");
			tempString = tempString.replaceAll("IOW", "");
			tempString = tempString.replaceAll("IRQ", "");
			tempString = tempString.replaceAll("%", "");
			for (int i = 0; i < 10; i++) {
				tempString = tempString.replaceAll("  ", " ");
			}
			tempString = tempString.trim();
			String[] myString = tempString.split(" ");
			cpuUsageAsInt = new int[myString.length];
			for (int i = 0; i < myString.length; i++) {
				myString[i] = myString[i].trim();
				cpuUsageAsInt[i] = Integer.parseInt(myString[i]);
			}
		}
		return cpuUsageAsInt;

	}

	private String executeTop() {
		java.lang.Process p = null;
		BufferedReader in = null;
		String returnString = null;
		try {
			p = Runtime.getRuntime().exec("top -n 1");
			in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			while (returnString == null || returnString.contentEquals("")) {
				returnString = in.readLine();
			}
		} catch (IOException e) {
			Log.e("executeTop", "error in getting first line of top");
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (p != null) {
					p.destroy();
				}
			} catch (IOException e) {
				Log.e("executeTop", "error in closing and destroying top process");
				e.printStackTrace();
			}
		}
		return returnString;

	}

	private static long[] getFreeAndTotalMemorySize() {

		long[] freeAndTotalMemory = null; 
		String str1 = "/proc/meminfo";
	    String str2="";
	    String[] arrayOfString;
	    try {
	        FileReader localFileReader = new FileReader(str1);
	        BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
	        for (int i = 0; i < 2; i++) {
	            str2 = str2+" "+ localBufferedReader.readLine();  
	        }
	        arrayOfString = str2.split("\\s+");
	        for (String num : arrayOfString) {
	            Log.i(str2, num + "\t");
	        }
	        // total Memory
	        freeAndTotalMemory = new long[2];
	        freeAndTotalMemory[0] = Integer.valueOf(arrayOfString[5]).intValue();
	        freeAndTotalMemory[1] = Integer.valueOf(arrayOfString[2]).intValue();

	        localBufferedReader.close();
	    } catch (IOException e) {
	    }
	    return freeAndTotalMemory;


	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (batteryInfoReceiver != null) {
			cordova.getActivity().unregisterReceiver(batteryInfoReceiver);
		}
	}

}
