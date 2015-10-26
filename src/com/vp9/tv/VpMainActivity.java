/*
       Licensed to the Apache Software Foundation (ASF) under one
       or more contributor license agreements.  See the NOTICE file
       distributed with this work for additional information
       regarding copyright ownership.  The ASF licenses this file
       to you under the Apache License, Version 2.0 (the
       "License"); you may not use this file except in compliance
       with the License.  You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing,
       software distributed under the License is distributed on an
       "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
       KIND, either express or implied.  See the License for the
       specific language governing permissions and limitations
       under the License.
 */

package com.vp9.tv;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaActivity;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tv.vp9.videoproxy.MyService;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.TrafficStats;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.vp9.laucher.main.application.Vp9Application;
import com.vp9.laucher.main.util.MyPreference;
import com.vp9.model.KeyCodeInfo;
import com.vp9.player.controller.MediaController;
import com.vp9.player.model.ChangeSubtitle;
import com.vp9.player.vp9Interface.Vp9ActivityInterface;
import com.vp9.plugin.EventPlayerPlugin;
import com.vp9.util.AppPreferences;
import com.vp9.util.Config;
import com.vp9.util.FileUtil;
import com.vp9.util.KeyCodeUtil;
import com.vp9.util.OperUtil;
import com.vp9.util.ServerThread;
import com.vp9.util.Vp9Contant;
import com.vp9.util.Vp9ParamUtil;

public class VpMainActivity extends CordovaActivity implements Vp9ActivityInterface {

	private String TAG = VpMainActivity.class.getSimpleName();
	// int MIN_FILE_SIZE = 100; // Byte
	int MIN_FILE_SIZE = -100; // Byte
	// String CONFIG_FILE_NAME = "config.txt";
	// String INDEX_FILE = "index.html";
	// private static final String TAG_SETTING = "settings";
	// private static final String TAG_URL = "url";

	// ArrayList<String> listUrl;

	public boolean isFinishLoad = false;

	AlertDialog _alert;
	private TextView tvChannel;
	private Timer time;

	private static int uid;
	private static long totalRxBefore;
	private static long totalTxBefore;
	private static long BeforeTime;
	
	public static int locationClickVideoMenu = 0;
	// traffic

	private double[] arrayTraffic = new double[5];
	private static int index = -1;
	private int len = 0;
	// end traffic
	private Timer timer;
	private String channelNum = null;

	//
	// private float x1,y1,x2,y2;
	//

	public MediaController mController;

	private boolean isShowEPG = false;

	private RelativeLayout vp9PlayerLayout;

	private int videoType;

	public CordovaWebView webView;
	private RelativeLayout uiView2;
	public boolean is3D;

	public ServerThread socketServer;

	private MyService proxySevice;

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		Debug.startMethodTracing();
		// android.os.Debug.getNativeHeapAllocatedSize();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.vp9_player);
//		View decorView = getWindow().getDecorView();
//		// Hide the status bar.
//		// int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
//		int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
//		decorView.setSystemUiVisibility(uiOptions);
//		// Remember that you should never show the action bar if the
//		// status bar is hidden, so hide that too if necessary.
//		ActionBar actionBar = getActionBar();
//		if (actionBar != null) {
//			actionBar.hide();
//		}

		int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
		int screenHeight = getWindowManager().getDefaultDisplay().getHeight();
		Log.i(TAG, "VpMainActivity - SCREEN SIZES: W: " + screenWidth + " H: " + screenHeight);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		Intent callerIntent = getIntent();

		if (!checkOnlineState()) {
			displayQuitAppDiaglog();
		}
		//
		// RelativeLayout relativeWebview = (RelativeLayout)
		// findViewById(R.id.webview_layout);
		// relativeWebview.setBackgroundColor(0x00000000);

		org.apache.cordova.Config.init(this);

		this.splashscreenTime = this.getIntegerProperty("SplashScreenDelay", this.splashscreenTime);
		if (this.splashscreenTime > 0) {
			this.splashscreen = this.getIntegerProperty("SplashScreen", 0);
			if (this.splashscreen != 0) {
				this.showSplashScreen(this.splashscreenTime);
			}
		}
		loadSpinner();

		Bundle serInfoCaller = callerIntent.getBundleExtra("ServerInfo");
		if (serInfoCaller == null || !serInfoCaller.containsKey("url")) {
			exitApp();
			return;
		}
		updateLibrary();
		String url = serInfoCaller.getString("url");
		Log.d("TAG ", "---------------------" + VpMainActivity.class.getSimpleName() + " url: " + url);
		String type = serInfoCaller.getString("type");
		String channelNum = serInfoCaller.getString("channelNum");
		Log.d("TAG ", "---------------------" + VpMainActivity.class.getSimpleName() + " channelNum: " + channelNum);

		try {
			if (type != null && !"".equals(type)) {

				url = url + "?type=" + type + "&id=" + channelNum;
				//url = "";
				//url = "http://demo.vp9.tv/nhankv/?type=" + type;
				
			}
			// final String urlVideo = url;
			// loadUrl(urlVideo);

			final String urlVideo = url;

			View vp9PlayerView = View.inflate(this, R.layout.vp9_player, null);
			this.vp9PlayerLayout = (RelativeLayout) vp9PlayerView.findViewById(R.id.vp9_player_layout);
//			this.vp9PlayerLayout.setOnHoverListener(new View.OnHoverListener() {
//				@Override
//				public boolean onHover(View v, MotionEvent event) {
//
//					return onVp9TouchEvent(event);
//				}
//			});
			this.uiView2 = (RelativeLayout) findViewById(R.id.ui_view2);
			// RelativeLayout uiView = (RelativeLayout)
			// findViewById(R.id.ui_view);
			// uiView2.setScaleX(0.5f);
			// uiView.setScaleX(0.5f);
			openServerSocket(serInfoCaller);
			setVisibility(uiView2, View.GONE);
			webView = (CordovaWebView) findViewById(R.id.cordova_web_view);
			CordovaWebView webView2 = (CordovaWebView) findViewById(R.id.cordova_web_view2);
			setVisibility(webView2, View.GONE);
			if (webView != null) {
				Log.d(TAG, "--------- : webview != null");
				webView.loadUrl(urlVideo);
				// webview.loadUrl("http://10.10.10.159/code/index.html");

				if (!type.equals("karaoke")) {
					webView.setBackgroundColor(Color.TRANSPARENT);
					webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
				}
			} else {
				Log.e(TAG, "--------- : webview == null");
			}

			setUserAgent();
			setupAppcache();

			// webview.setAlpha(0.5f);

			// new by mr.bigsky
			if (webView != null && savedInstanceState != null && !savedInstanceState.isEmpty()) {
				webView.restoreState(savedInstanceState);
				return;
			}

			webView.addJavascriptInterface(CordovaWebView.CORDOVA_VERSION, "CORDOVA_VERSION");
			if (webView != null) {
				if (Build.VERSION.SDK_INT < 8) {
					webView.getSettings().setPluginsEnabled(true);
				} else {
					webView.getSettings().setPluginState(PluginState.ON);
					webView.getSettings().setJavaScriptEnabled(true);
					webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
					webView.getSettings().setRenderPriority(RenderPriority.HIGH);
					webView.getSettings().setPluginState(android.webkit.WebSettings.PluginState.ON_DEMAND);
				}
			}

			com.vp9.util.AppPreferences.INSTANCE.assignContext(this);
			com.vp9.util.AppPreferences.INSTANCE.saveIsChannelNumber(true);

			this.tvChannel = new TextView(this);
			tvChannel.setPadding(50, 30, 30, 30);
			tvChannel.setTextSize(42);
			tvChannel.setTypeface(tvChannel.getTypeface(), Typeface.BOLD);
			tvChannel.setText("");
			tvChannel.setTextColor(Color.BLUE);
			tvChannel.bringToFront();
			this.tvChannel.setVisibility(View.INVISIBLE);
			LayoutParams params = new LayoutParams(200, 200);
			addContentView(tvChannel, params);

			setListener();
			// handleTraffic();
		} catch (Exception e) {
			e.printStackTrace();
			Intent intent = new Intent(this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			showDialogLoadUrl(intent);
		}
		
		downloadTest();
		Log.d(TAG, "Load url is ok");
	}

	private void setupAppcache() {
		CookieSyncManager.createInstance(this);
		WebSettings settings = webView.getSettings();

		settings.setCacheMode(WebSettings.LOAD_DEFAULT);

		settings.setSaveFormData(true);
		settings.setLoadsImagesAutomatically(true);
		settings.setDatabaseEnabled(true);
		settings.setDomStorageEnabled(true);
		settings.setAppCacheMaxSize(1024*1024*1024*8);
		String sDataPath = this.getDir("database", Context.MODE_PRIVATE).getPath();
		settings.setDatabasePath(sDataPath);
		settings.setAppCachePath(sDataPath);
		settings.setAllowFileAccess(true);
		settings.setAppCacheEnabled(true);

		settings.setAllowContentAccess(true);
		settings.setLoadsImagesAutomatically(true);
		CookieManager c = CookieManager.getInstance();
		c.setAcceptCookie(true);
		
	}

	private void updateLibrary() {
		Thread updatethread = new Thread() {

			public void run() {
				FileUtil.updateLibraryFiles(VpMainActivity.this);
			}
		};
		updatethread.setName("updateLibrary_35");
		updatethread.start();
	}

	private void openServerSocket(Bundle serInfoCaller) {
		// ScrollView debugView = (ScrollView) findViewById(R.id.debug_layout);
		// setVisibility(debugView, View.GONE);
		if (serInfoCaller.containsKey("debug")) {
			boolean isDebug = serInfoCaller.getBoolean("debug");
			ScrollView debugView = (ScrollView) findViewById(R.id.debug_layout);
			if (isDebug) {
				int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
				int screenHeight = getWindowManager().getDefaultDisplay().getHeight();
				LayoutParams layoutParams = debugView.getLayoutParams();
				layoutParams.width = screenWidth / 2;
				layoutParams.height = screenHeight / 2;
				debugView.setLayoutParams(layoutParams);
				setVisibility(debugView, View.VISIBLE);
				TextView tvDebug = (TextView) findViewById(R.id.tvDebug);
				tvDebug.setMaxLines(ServerThread.MAX_LINES * 2);
				this.socketServer = new ServerThread(tvDebug, debugView, this);
				Thread serverThread = new Thread(this.socketServer);
				serverThread.setName("serverThread_15");
				serverThread.start();
				Thread logcatThread = new Thread() {

					public void run() {
						// Looper.prepare();
						handleLogCat();
						// Looper.loop();
					}
				};
				logcatThread.setName("logcatThread_16");
				logcatThread.start();
			} else {
				setVisibility(debugView, View.GONE);
			}
		}

	}

	protected void handleLogCat() {
		try {
			ArrayList<String> commandLine = new ArrayList<String>();
			commandLine.add("logcat");
			commandLine.add("-v");
			commandLine.add("time");

			Process process = Runtime.getRuntime().exec(commandLine.toArray(new String[0]));
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String filePath = Environment.getExternalStorageDirectory().getAbsolutePath();
			File f = new File(filePath + "/vp9_proxy.log");
			BufferedWriter bw = new BufferedWriter(new FileWriter(f, true));

			while (!Thread.currentThread().isInterrupted()) {
				String line = bufferedReader.readLine();
				bw.append(line + "\n");
				bw.flush();
				if (line != null && this.socketServer != null) {
					this.socketServer.displayMessage(line);
					Thread.sleep(300);
				}
			}
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	public void displayMessage(String msg) {
		if (this.socketServer != null && msg != null) {
			this.socketServer.displayMessage(msg);
		}
	}

	private void setUserAgent() {
		WebSettings settings = webView.getSettings();
		String userAgent = settings.getUserAgentString();
		userAgent += " VP9Cordova";
		settings.setUserAgentString(userAgent);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onKeyDown: " + keyCode);
		// if (mController != null) {
		// if (mController.handleMainKeyDown(null, keyCode, event)) {
		// // return super.onKeyDown(keyCode, event);
		// return true;
		// }
		// return super.onKeyDown(keyCode, event);
		// }
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		Log.d(TAG, "onKeyUp: " + keyCode);
		if (mController != null) {
			if (mController.handleMainKeyDown(null, keyCode, event)) {
				return true;
			}
			return super.onKeyUp(keyCode, event);
		}
		return super.onKeyUp(keyCode, event);
	}

	@Override
	public void onDestroy() {
		MyPreference.getPreference(this);
		MyPreference.setBoolean("isOpenedApp", true);

		Log.d(VpMainActivity.class.getSimpleName(), "ondestroy");
		// android.os.Debug.getNativeHeapAllocatedSize();
		// Debug.stopMethodTracing();
		super.onDestroy();
	}

	private void showDialogLoadUrl(final Intent intent) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Url not found");
		builder.setMessage("Error url not found or network is slowly.");
		builder.setCancelable(true);

		Bundle bundle = new Bundle();
		bundle.putBoolean("isUrl", false);
		intent.putExtra("urlStatus", bundle);

		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.dismiss();
				// isFinishLoad = true;
				startActivity(intent);
				finish();
			}
		});

		builder.setNegativeButton("Quit", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.dismiss();
				exitApp();
			}
		});

		_alert = builder.create();
		_alert.show();

	}

	private void delay(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public boolean checkOnlineState() {
		try {
			ConnectivityManager cManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo nInfo = cManager.getActiveNetworkInfo();

			Log.v(TAG, "Connection internet Info: " + nInfo);

			if (nInfo != null && nInfo.isConnected()) {
				Log.v(TAG, "Connection internet: " + nInfo.isConnected());
				return true;
			}
			Log.v(TAG, "Connection internet: " + false);
		} catch (Exception e) {
			Log.v(TAG, "Exception Connection internet: " + e.getMessage());
			return false;
		}

		return false;
	}

	private void displayQuitAppDiaglog() {
		PackageManager manager = getPackageManager();
		String versionName = "";

		try {
			PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
			versionName = info.versionName;
		} catch (NameNotFoundException e) {
			Log.e(TAG, e.getMessage());
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Disconnect Internet");
		builder.setMessage("Internet is not connect. Please, quit App Version " + versionName + "?");
		builder.setCancelable(true);

		builder.setPositiveButton("Try connect", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.dismiss();
				if (!checkOnlineState()) {
					displayQuitAppDiaglog();
				}
			}
		});

		builder.setNegativeButton("Quit", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.dismiss();
				exitApp();
			}
		});

		_alert = builder.create();
		_alert.show();

		// final Timer t = new Timer();
		// t.schedule(new TimerTask() {
		// public void run() {
		// dlg.dismiss();
		// t.cancel();
		// }
		// }, 15000);

	}

	// @Override
	// protected void onResume() {
	// super.onResume();
	// if(!checkOnlineState()){
	// displayQuitAppDiaglog();
	// }
	// }

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		if (webView != null) {
			webView.saveState(outState);
		}

	}

	@Override
	protected void onRestoreInstanceState(Bundle state) {
		if (webView != null) {
			webView.restoreState(state);
		}
		super.onRestoreInstanceState(state);
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		// Log.d("VpMainActivity", "keyCode: " + event.getKeyCode());
		if (event.getAction() != KeyEvent.ACTION_UP) {
			return super.dispatchKeyEvent(event);
		}

		// Toast.makeText(VpMainActivity.this, "keycode: " + event.getKeyCode()
		// , Toast.LENGTH_SHORT).show();
		if (event.getKeyCode() == KeyEvent.KEYCODE_HOME) {
			Log.v(TAG, "dispatchKeyEvent-KEY_HOME_CODE: " + event.getKeyCode());
			// int duration = Toast.LENGTH_SHORT;
			// Toast.makeText(getContext(), "onKeyDown-KEY_HOME_CODE = " +
			// event.getKeyCode(), duration).show();
			exitApp();
			return true;
		} else {
			boolean isSuccess = false;
			String msgValue = null;
			int keyCode = -1;
			if (event.getAction() == KeyEvent.ACTION_UP) {
				KeyCodeInfo keyCodeInfo;
				keyCode = event.getKeyCode();
				switch (keyCode) {
				case KeyEvent.KEYCODE_BACK:
					// Log.v(TAG, "KEYCODE_BACK");
					msgValue = "key_back";
					isSuccess = true;
					keyCodeInfo = new KeyCodeInfo();
					keyCodeInfo.setMsgValue(msgValue);
					keyCodeInfo.setSuccess(isSuccess);

					SharedPreferences preference = MyPreference.getPreference(this.getBaseContext());
					boolean confirm = MyPreference.getBoolean("confirm_backbutton");
					Log.d(VpMainActivity.class.getSimpleName(), "-------------------- confirm: " + confirm);

					if (!confirm) {
						VpMainActivity.this.finish();
					}
					// }
					// }, 2000);

					break;

				case KeyEvent.KEYCODE_DEL:
					msgValue = "key_del";
					isSuccess = true;
					keyCodeInfo = new KeyCodeInfo();
					keyCodeInfo.setMsgValue(msgValue);
					keyCodeInfo.setSuccess(isSuccess);
					break;
				case KeyEvent.KEYCODE_MENU:
					this.closeContextMenu();
					this.closeOptionsMenu();

					msgValue = "menu";
					isSuccess = true;
					keyCodeInfo = new KeyCodeInfo();
					keyCodeInfo.setMsgValue(msgValue);
					keyCodeInfo.setSuccess(isSuccess);
					break;
				case KeyEvent.KEYCODE_F1:
					msgValue = "key_F1";
					isSuccess = true;
					keyCodeInfo = new KeyCodeInfo();
					keyCodeInfo.setMsgValue(msgValue);
					keyCodeInfo.setSuccess(isSuccess);
					break;
				case KeyEvent.KEYCODE_F2:
					msgValue = "key_F2";
					isSuccess = true;
					keyCodeInfo = new KeyCodeInfo();
					keyCodeInfo.setMsgValue(msgValue);
					keyCodeInfo.setSuccess(isSuccess);
					break;
				case KeyEvent.KEYCODE_F3:
					msgValue = "key_F3";
					isSuccess = true;
					keyCodeInfo = new KeyCodeInfo();
					keyCodeInfo.setMsgValue(msgValue);
					keyCodeInfo.setSuccess(isSuccess);
					break;
				case KeyEvent.KEYCODE_F4:
					msgValue = "key_F4";
					isSuccess = true;
					keyCodeInfo = new KeyCodeInfo();
					keyCodeInfo.setMsgValue(msgValue);
					keyCodeInfo.setSuccess(isSuccess);
					break;
				case KeyEvent.KEYCODE_F9:
					msgValue = "key_F9";
					isSuccess = true;
					keyCodeInfo = new KeyCodeInfo();
					keyCodeInfo.setMsgValue(msgValue);
					keyCodeInfo.setSuccess(isSuccess);
					break;
				case KeyEvent.KEYCODE_F10:
					msgValue = "key_F10";
					isSuccess = true;
					keyCodeInfo = new KeyCodeInfo();
					keyCodeInfo.setMsgValue(msgValue);
					keyCodeInfo.setSuccess(isSuccess);
					break;
				case KeyEvent.KEYCODE_F11:
					msgValue = "key_F11";
					isSuccess = true;
					keyCodeInfo = new KeyCodeInfo();
					keyCodeInfo.setMsgValue(msgValue);
					keyCodeInfo.setSuccess(isSuccess);
					break;
				case KeyEvent.KEYCODE_F12:
					msgValue = "key_F12";
					isSuccess = true;
					keyCodeInfo = new KeyCodeInfo();
					keyCodeInfo.setMsgValue(msgValue);
					keyCodeInfo.setSuccess(isSuccess);
					break;
				case KeyEvent.KEYCODE_UNKNOWN:
					msgValue = "key_unknow";
					isSuccess = true;
					keyCodeInfo = new KeyCodeInfo();
					keyCodeInfo.setMsgValue(msgValue);
					keyCodeInfo.setSuccess(isSuccess);
					break;
				case KeyEvent.KEYCODE_MOVE_HOME:
					msgValue = "key_menu_virtual";
					isSuccess = true;
					keyCodeInfo = new KeyCodeInfo();
					keyCodeInfo.setMsgValue(msgValue);
					keyCodeInfo.setSuccess(isSuccess);
					break;

				default:
					keyCodeInfo = KeyCodeUtil.getKeyCodeInfo(keyCode);
					msgValue = keyCodeInfo.getMsgValue();

					break;
				}
				// int duration = Toast.LENGTH_SHORT;
				// Toast.makeText(getContext(), "KEY_CODE = " + keyCode +
				// " ==> " + keyCodeInfo.getMsgValue(), duration).show();

				if (KeyCodeUtil.isNumberKey(keyCode)) {
					handleChannelNumber(keyCodeInfo);
				}

				if (keyCode == KeyEvent.KEYCODE_DEL) {
					handleDelChannelNumber(keyCodeInfo);
				}
			}

			if (webView != null && msgValue != null) {

				try {
					JSONObject message = new JSONObject();
					message.put("action", "keyEvent");
					message.put("key", msgValue);
					webView.sendJavascript("handlerCordovaMsg('" + message.toString() + "')");

				} catch (JSONException e) {
					Log.e(TAG, "JSONException: " + e.getMessage());
				}

			}

			if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
				return isSuccess;
			}
			return super.dispatchKeyEvent(event);
		}
	}

	// Override
	// public void onAttachedToWindow(){
	// this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD);
	// super.onAttachedToWindow();
	// }

	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// if (keyCode == KeyEvent.KEYCODE_HOME) {
	// Log.v(TAG, "onKeyDown-KEY_HOME_CODE: " + event.getKeyCode());
	// // int duration = Toast.LENGTH_SHORT;
	// // Toast.makeText(getContext(), "onKeyDown-KEY_HOME_CODE = " +
	// event.getKeyCode(), duration).show();
	// exitApp();
	// return true;
	// }
	// return super.onKeyDown(keyCode, event);
	// }

	private void setListener() {
		if (webView == null) {
			return;
		}
		// appView.setOnKeyListener(new OnKeyListener() {
		//
		// public boolean onKey(View v, int keyCode, KeyEvent event) {
		// return isFinishLoad;
		// boolean isSuccess = false;
		// String msgValue = null;
		// // if(event.getAction() == KeyEvent.ACTION_UP){
		// // Toast.makeText(getContext(), "KEY_CODE = " + keyCode + " ==> " +
		// keyCode, duration).show();
		//
		// if(event.getAction() == KeyEvent.ACTION_DOWN){
		// KeyCodeInfo keyCodeInfo;
		//
		// switch(keyCode){
		// // case KeyEvent.KEYCODE_BACK:
		// //// Log.v(TAG, "KEYCODE_BACK");
		// // msgValue = "key_back";
		// // isSuccess = true;
		// // keyCodeInfo = new KeyCodeInfo();
		// // keyCodeInfo.setMsgValue(msgValue);
		// // keyCodeInfo.setSuccess(isSuccess);
		// //
		// // if(vp9Player != null){
		// // vp9Player.destroy(1);
		// // vp9Player = null;
		// // }
		// //
		// //// timerBackButton.schedule(new TimerTask() {
		// //// @Override
		// //// public void run() {
		// //// SharedPreferences preference =
		// MyPreference.getPreference(getContext());
		// //// boolean confirm = MyPreference.getBoolean("confirm_backbutton");
		// //// Log.d(VpMainActivity.class.getSimpleName(),
		// "-------------------- confirm: " + confirm);
		// ////
		// //// if (!confirm) {
		// //// VpMainActivity.this.finish();
		// //// }
		// //
		// // SharedPreferences preference =
		// MyPreference.getPreference(getContext());
		// // boolean confirm = MyPreference.getBoolean("confirm_backbutton");
		// // Log.d(VpMainActivity.class.getSimpleName(),
		// "-------------------- confirm: " + confirm);
		// //
		// // if (!confirm) {
		// // VpMainActivity.this.finish();
		// // }
		// //
		// //// }
		// //// }, 2000);
		// //
		// //
		// // break;
		//
		// // return true;
		// case KeyEvent.KEYCODE_HOME:
		// msgValue = "key_home";
		// // Log.v(TAG, "KEYCODE_HOME");
		// // int duration = Toast.LENGTH_SHORT;
		// // Toast.makeText(getContext(), "KEY_CODE = " + keyCode,
		// duration).show();
		// // int duration = Toast.LENGTH_SHORT;
		// // Toast.makeText(getContext(), "KEYCODE_HOME-KEY_CODE = " + keyCode,
		// duration).show();
		// exitApp();
		// isSuccess = true;
		// keyCodeInfo = new KeyCodeInfo();
		// keyCodeInfo.setMsgValue(msgValue);
		// keyCodeInfo.setSuccess(isSuccess);
		// break;
		// case KeyEvent.KEYCODE_DEL:
		// msgValue = "key_del";
		// isSuccess = true;
		// keyCodeInfo = new KeyCodeInfo();
		// keyCodeInfo.setMsgValue(msgValue);
		// keyCodeInfo.setSuccess(isSuccess);
		// break;
		//
		// default:
		// keyCodeInfo = KeyCodeUtil.getKeyCodeInfo(keyCode);
		// msgValue = keyCodeInfo.getMsgValue();
		//
		// break;
		// }
		// // int duration = Toast.LENGTH_SHORT;
		// // Toast.makeText(getContext(), "KEY_CODE = " + keyCode + " ==> " +
		// keyCodeInfo.getMsgValue(), duration).show();
		//
		// if(KeyCodeUtil.isNumberKey(keyCode)){
		// handleChannelNumber(keyCodeInfo);
		// }
		//
		// if(keyCode == KeyEvent.KEYCODE_DEL){
		// handleDelChannelNumber(keyCodeInfo);
		//
		// }
		//
		//
		// }
		//
		// if(appView != null && msgValue != null){
		//
		// try {
		// JSONObject message = new JSONObject();
		// message.put("action", "keyEvent");
		// message.put("key", msgValue);
		// appView.sendJavascript("handlerCordovaMsg('" + message.toString() +
		// "')");
		// // if("key_back".equals(msgValue)){
		// // appView.backHistory();
		// // }
		// // if(checkOnlineState()){
		// // appView.sendJavascript("handlerCordovaMsg('" + message.toString()
		// + "')");
		// // if("key_back".equals(msgValue)){
		// // appView.backHistory();
		// // }
		// // }else{
		// // PackageManager manager = getContext().getPackageManager();
		// // String versionName = "";
		// // try {
		// // PackageInfo info =
		// manager.getPackageInfo(getContext().getPackageName(), 0);
		// // versionName = info.versionName;
		// // } catch (NameNotFoundException e) {
		// // Log.e(TAG, e.getMessage());
		// // }
		// // AlertDialog.Builder builder = new
		// AlertDialog.Builder(getActivity());
		// // builder.setTitle("Quit");
		// // builder.setMessage("Are you want to quit App Version " +
		// versionName + "?");
		// // builder.setCancelable(true);
		// //
		// // builder.setPositiveButton("Quit",
		// // new DialogInterface.OnClickListener() {
		// // public void onClick(DialogInterface dialog, int id) {
		// // dialog.dismiss();
		// // exitApp();
		// // }
		// // });
		// //
		// // builder.setNegativeButton("Cancel",
		// // new DialogInterface.OnClickListener() {
		// // public void onClick(DialogInterface dialog, int id) {
		// // dialog.dismiss();
		// // }
		// // });
		// //
		// // final AlertDialog dlg = builder.create();
		// // dlg.show();
		// ////
		// ////
		// //// final Timer t = new Timer();
		// //// t.schedule(new TimerTask() {
		// //// public void run() {
		// //// dlg.dismiss();
		// //// t.cancel();
		// //// }
		// //// }, 15000);
		// // }
		//
		// } catch (JSONException e) {
		// Log.e(TAG, "JSONException: " +e.getMessage());
		// }
		//
		// }
		//
		//
		//
		//
		// if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() ==
		// KeyEvent.ACTION_DOWN){
		// return isSuccess;
		// }
		// return onKeyDown(keyCode, event);
		// // return isSuccess;
		// }
		// });
	}

	private void handleDelChannelNumber(KeyCodeInfo keyCodeInfo) {
		Log.d(VpMainActivity.class.getSimpleName(),
				"---------------handleDelChannelNumber: " + keyCodeInfo.getMsgValue() + "AppPreferences.INSTANCE.isChannelNumber(): " + AppPreferences.INSTANCE.isChannelNumber());
		if (!AppPreferences.INSTANCE.isChannelNumber()) {
			// AppPreferences.INSTANCE.saveIsChannelNumber(true);
			Log.d(VpMainActivity.class.getSimpleName(),
					"---------------handleDelChannelNumber: " + keyCodeInfo.getMsgValue() + "AppPreferences.INSTANCE.isChannelNumber(): " + AppPreferences.INSTANCE.isChannelNumber());
			tvChannel.setVisibility(View.INVISIBLE);
			return;
		} else {
			tvChannel.setVisibility(View.VISIBLE);
		}

		runOnUiThread(new Runnable() {
			@Override
			public void run() {

				if (time != null) {
					time.cancel();
					if (timer != null) {
						timer.cancel();
					}
				}

				CharSequence text = tvChannel.getText();
				if (text != null) {
					String channelNum = text.toString();
					if (channelNum != null && channelNum.trim().length() < 1) {
						if (channelNum.endsWith("-")) {

						} else {
							channelNum = "-";
							tvChannel.setText(channelNum);
						}
					} else if (channelNum != null) {
						if (channelNum.endsWith("-")) {
							channelNum = "-";
							tvChannel.setText(channelNum);

						} else {
							channelNum = channelNum.trim().substring(0, channelNum.trim().length() - 1);
							channelNum += "-";
							tvChannel.setText(channelNum);
						}
					}
				}

				time = new Timer();
				time.schedule(new TimerTask() {
					public void run() {
						intVisibilityTextChannel();
					}
				}, 3000);

			}
		});

	}

	protected synchronized void handleChannelNumber(final KeyCodeInfo keyCodeInfo) {
		Log.d(VpMainActivity.class.getSimpleName(),
				"---------------handleChannelNumber: " + keyCodeInfo.getMsgValue() + "AppPreferences.INSTANCE.isChannelNumber(): " + AppPreferences.INSTANCE.isChannelNumber());
		if (!AppPreferences.INSTANCE.isChannelNumber()) {
			// AppPreferences.INSTANCE.saveIsChannelNumber(true);
			Log.d(VpMainActivity.class.getSimpleName(),
					"---------------handleChannelNumber: " + keyCodeInfo.getMsgValue() + "AppPreferences.INSTANCE.isChannelNumber(): " + AppPreferences.INSTANCE.isChannelNumber());
			tvChannel.setVisibility(View.INVISIBLE);
			return;
		} else {
			tvChannel.setVisibility(View.VISIBLE);
		}
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				CharSequence text = tvChannel.getText();
				if (text != null) {
					String channelNum = text.toString().trim();
					if (channelNum != null) {

						int lengNumber = 3;
						if (channelNum.length() > 0 && !channelNum.startsWith("-")) {
							int charStart = Integer.parseInt(channelNum.substring(0, 1));
							if (charStart > 4) {
								lengNumber = 4;
							}
						}

						if (channelNum.endsWith("-") && channelNum.length() < lengNumber) {
							if (time != null) {
								time.cancel();
							}
							channelNum = channelNum.replace("-", keyCodeInfo.getMsgValue());
							tvChannel.setVisibility(View.VISIBLE);
							if (channelNum.length() < lengNumber - 1) {
								tvChannel.setText(channelNum + "-");
							} else {
								tvChannel.setText(channelNum);
								// appView.sendJavascript("handlerTiviChannel('"
								// + channelNum + "')");

								// time = new Timer();
								// time.schedule(new TimerTask() {
								// public void run() {
								intVisibilityTextChannel();
								// }
								// }, 0);

								return;
							}

						} else if (channelNum.length() < 1) {
							if (time != null) {
								time.cancel();
								if (timer != null) {
									timer.cancel();
								}
							}
							tvChannel.setVisibility(View.VISIBLE);
							tvChannel.setText(channelNum + keyCodeInfo.getMsgValue() + "-");
						}
						// else if (channelNum.length() <= lengNumber-1 &&
						// channelNum.length() >= 1) {
						// if (time != null) {
						// time.cancel();
						// if (timer != null) {
						// timer.cancel();
						// }
						// }
						// tvChannel.setVisibility(View.VISIBLE);
						// tvChannel.setText(keyCodeInfo.getMsgValue() + "-");
						// }
					}
				} else {
					if (time != null) {
						time.cancel();
					}
					tvChannel.setVisibility(View.VISIBLE);
					tvChannel.setText(keyCodeInfo.getMsgValue() + "-");
				}

				time = new Timer();
				time.schedule(new TimerTask() {
					public void run() {
						intVisibilityTextChannel();
					}
				}, 3000);

			}
		});

	}

	protected synchronized void intVisibilityTextChannel() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				CharSequence text = tvChannel.getText();

				if (text != null) {
					channelNum = text.toString();
					if (channelNum != null && channelNum.trim().endsWith("-")) {
						channelNum = channelNum.trim().substring(0, channelNum.trim().length() - 1);
						tvChannel.setText(channelNum);
					}
				}

				timer = new Timer();
				timer.schedule(new TimerTask() {
					public void run() {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								tvChannel.setVisibility(View.INVISIBLE);
								tvChannel.setText("");

								// appView.sendJavascript("handlerTiviChannel('"
								// + channelNum
								// + "')");

								launchingApp(channelNum, Config.PACKAGE_TVAPP, Config.ACTIVITY_TVAPP);

							}
						});
					}
				}, 1000);

			}

		});

	}

	private void exitApp() {
		if (this.socketServer != null) {
			this.socketServer.close();
		}

		// super.onDestroy();
		// if(webView != null){
		// vp9PlayerLayout.removeView(webView);
		// webView.removeAllViews();
		// webView.clearHistory();
		// webView.handlePause(true);
		// webView.clearCache(true);
		// webView.freeMemory(); //new code
		// webView.pauseTimers(); //new code
		//
		// webView.destroy();
		// }

		Log.e(TAG, "------------- onstop FINISH 2a");

		try {
			// MyService.stop();
			if (proxySevice != null) {
				Log.e(TAG, "------------- onstop FINISH 2a");
				proxySevice.stopThread();
				proxySevice = null;
			}
			Log.e(TAG, "------------- onstop FINISH 2b");
		} catch (Exception e) {
			e.printStackTrace();
		}

		Log.e(TAG, "------------- onstop FINISH 2b1");

		cleanParam();

		Log.e(TAG, "------------- onstop FINISH 2b1-1");
		finish();
		// delay(2000);
		Log.e(TAG, "------------- onstop FINISH 2b2: " + android.os.Process.myPid());
		android.os.Process.killProcess(android.os.Process.myPid());
		Log.e(TAG, "------------- onstop FINISH 2b3");
		System.exit(0);
		Log.e(TAG, "------------- onstop FINISH 2b4");
	}

	private void exitApp2() {

		final ServerThread newSocketServer = this.socketServer;

		socketServer = null;

		if (newSocketServer != null) {
			newSocketServer.close();
		}

		Log.e(TAG, "------------- onstop FINISH aaa");
		// super.onDestroy();
		// if(webView != null){
		// vp9PlayerLayout.removeView(webView);
		// webView.removeAllViews();
		// webView.clearHistory();
		// webView.clearCache(true);
		// webView.freeMemory(); //new code
		// webView.pauseTimers(); //new code
		// webView.handlePause(true);
		// webView.destroy();
		// }

		Log.e(TAG, "------------- onstop FINISH 2a1");

		try {
			// MyService.stop();
			if (proxySevice != null) {
				Log.e(TAG, "------------- onstop FINISH 2a2");
				proxySevice.stopThread();
				proxySevice = null;
			}
			Log.e(TAG, "------------- onstop FINISH 2b");
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.e(TAG, "------------- onstop FINISH 2b1");

		cleanParam();

		Log.e(TAG, "------------- onstop FINISH 2b1-1");

		finish();
		Log.e(TAG, "------------- onstop FINISH 2b2");
		android.os.Process.killProcess(android.os.Process.myPid());
		Log.e(TAG, "------------- onstop FINISH 2b3");
		System.exit(0);
		Log.e(TAG, "------------- onstop FINISH 2b4");
	}

	private void cleanParam() {
		_alert = null;
		tvChannel = null;
		time = null;
		arrayTraffic = null;
		index = -1;
		len = 0;
		timer = null;
		channelNum = null;
		mController = null;
		vp9PlayerLayout = null;
		videoType = -1;
		webView = null;
		uiView2 = null;
		socketServer = null;
		proxySevice = null;
		mController = null;
	}

	@Override
	public void onPause() {
		


		
		boolean isQuit = AppPreferences.INSTANCE.isQuit();

		Log.d(VpMainActivity.class.getSimpleName(), "------------------------ isquit: " + isQuit);

		if (isQuit) {

			try {
				Log.d(TAG, "---------------- onStop");

				Vp9Application vp9Application = (Vp9Application) this.getApplicationContext();
				vp9Application.setActivityAppVP9(null);

				SharedPreferences preference = MyPreference.getPreference(getBaseContext());
				MyPreference.setBoolean("confirm_backbutton", false);

				if (mController != null) {
					Log.d(TAG, "onstop mController.destroy(1);");
					mController.destroy(1);
					mController = null;
				}

				if (_alert != null) {
					_alert.dismiss();
					_alert = null;
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			super.onStop();
			Log.e(TAG, "------------- onstop FINISH 1");

			if (webView != null) {
				webView.onPause();
			}

			exitApp2();

		} else {
			// Editor editor = prefs.edit();
			// editor.putBoolean("isQuit", true);
			// editor.commit();
			AppPreferences.INSTANCE.saveQuit(true);
		}

		if (mController != null && mController.vp9Player != null) {
			// mController.vp9Player.pause();
		}

		if (webView != null) {
			webView.onPause();
		}

		super.onPause();
	}

	@Override
	protected void onStop() {

		try {
			Log.d(TAG, "---------------- onStop");

			Vp9Application vp9Application = (Vp9Application) this.getApplicationContext();
			vp9Application.setActivityAppVP9(null);

			SharedPreferences preference = MyPreference.getPreference(getBaseContext());
			MyPreference.setBoolean("confirm_backbutton", false);

			if (mController != null) {
				Log.d(TAG, "onstop mController.destroy(1);");
				mController.destroy(1);
				mController = null;
			}

			if (_alert != null) {
				_alert.dismiss();
				_alert = null;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onStop();
		Log.e(TAG, "------------- onstop FINISH 1");

		if (webView != null) {
			webView.onPause();
		}

		exitApp2();
		// super.onDestroy();
		Log.e(TAG, "------------- onstop FINISH 3");
	}

	private boolean isWorkingUrl(String file_url) {
		URL url;
		try {
			url = new URL(file_url);
			URLConnection urlConnection = url.openConnection();
			urlConnection.connect();
			int file_size = urlConnection.getContentLength();
			Log.i("File Size", file_size + "");
			if (file_size > MIN_FILE_SIZE) {
				return true;
			} else {
				return false;
			}
		} catch (MalformedURLException e) {
			Log.e("ERROR", "MalformedURLException: " + e.getMessage());
			return false;
		} catch (IOException e) {
			Log.e("ERROR", "IOException: " + e.getMessage());
			return false;
		} catch (Exception e) {
			Log.e("ERROR", e.getMessage());
			return false;
		}
	}

	private void handleTraffic() {
		String packageName = "com.vp9.tv";
		final PackageManager pm = getPackageManager();
		try {
			ApplicationInfo applicationInfo = pm.getApplicationInfo(packageName, 0);
			uid = applicationInfo.uid;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BeforeTime = System.currentTimeMillis();
		totalRxBefore = TrafficStats.getUidRxBytes(uid);
		totalTxBefore = TrafficStats.getUidTxBytes(uid);

		Timer timerUpdateTraffic = new Timer();
		timerUpdateTraffic.schedule(new TimerTask() {
			@Override
			public void run() {

				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						long totalRxAfter = TrafficStats.getUidRxBytes(uid);
						long totalTxAfter = TrafficStats.getUidTxBytes(uid);

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
							resultSpeed = resultSpeed / len;
							VpMainActivity.this.webView.sendJavascript("handleTrafficClient('" + String.format("%.2f", rxBPS) + "', '" + String.format("%.0f", resultSpeed) + "Kb/s')");

						} else {
							VpMainActivity.this.webView.sendJavascript("handleTrafficClient(' No uploaded or downloaded bytes.' , 'No uploaded or downloaded bytes.')");
						}

					}
				});
			}
		}, 0, 1000);

	}

	/*
	 * public boolean onTouchEvent(MotionEvent touchevent) { switch
	 * (touchevent.getAction()) { // when user first touches the screen we get x
	 * and y coordinate case MotionEvent.ACTION_DOWN: { x1 = touchevent.getX();
	 * y1 = touchevent.getY();
	 * 
	 * Log.d(VpMainActivity.class.getSimpleName(), "Tocuh: x1: " + x1 +
	 * " - y1: " + y1); break; } case MotionEvent.ACTION_UP: { x2 =
	 * touchevent.getX(); y2 = touchevent.getY();
	 * 
	 * //if left to right sweep event on screen if (x1 < x2) {
	 * Toast.makeText(this, "Left to Right Swap Performed",
	 * Toast.LENGTH_LONG).show(); }
	 * 
	 * // if right to left sweep event on screen if (x1 > x2) {
	 * Toast.makeText(this, "Right to Left Swap Performed",
	 * Toast.LENGTH_LONG).show(); }
	 * 
	 * // if UP to Down sweep event on screen if (y1 < y2) {
	 * Toast.makeText(this, "UP to Down Swap Performed",
	 * Toast.LENGTH_LONG).show(); }
	 * 
	 * //if Down to UP sweep event on screen if (y1 > y2) { Toast.makeText(this,
	 * "Down to UP Swap Performed", Toast.LENGTH_LONG).show(); }
	 * Log.d(VpMainActivity.class.getSimpleName(), "Tocuh: x2: " + x2 +
	 * " - y2: " + y2); break;
	 * 
	 * } } return false; }
	 */

	private void launchingApp(String channelNum, String _apppackage, String _appactivity) {
		try {
			String appPackage = _apppackage;
			String appActivity = _appactivity;
			Log.e("TAG " + this.getClass().getSimpleName(), "channelNum: " + channelNum);
			Log.e("TAG " + this.getClass().getSimpleName(), appPackage + "-" + appActivity);

			/*
			 * Intent intent = this.cordova.getActivity().getPackageManager().
			 * getLaunchIntentForPackage(appPackage);
			 * intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			 * this.cordova.getActivity().startActivity(intent);
			 */

			// ComponentName name = new ComponentName(appPackage, appActivity);
			// Intent i = new Intent(Intent.ACTION_MAIN);
			//
			// Bundle b = new Bundle();
			// b.putString("type","tivi");
			// b.putString("channelNum", channelNum);
			// b.putString("url", Config.URL_TVAPP);
			// i.putExtra("start", b);
			//
			// i.addCategory(Intent.CATEGORY_LAUNCHER);
			// i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
			// | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
			// i.setComponent(name);
			//
			// startActivity(i);

			VpMainActivity.this.webView.sendJavascript("playtiviChannel('" + channelNum + "')");

		} catch (Exception e) {
			Log.e("TAG " + this.getClass().getSimpleName(), "Exception: " + e.getMessage());

			// String appPackage = _apppackage;
			// String appActivity = _appactivity;
			//
			// if (appActivity.substring(0, 1).indexOf(".") < 0) {
			// appActivity = "." + appActivity;
			// }
			//
			// ComponentName name = new ComponentName(appPackage,
			// appPackage+appActivity);
			// Intent i = new Intent(Intent.ACTION_MAIN);
			// Bundle b = new Bundle();
			// b.putString("type","tivi");
			// b.putString("channelNum", channelNum);
			// b.putString("url", Config.URL_TVAPP);
			// i.putExtra("start", b);
			//
			// i.addCategory(Intent.CATEGORY_LAUNCHER);
			// i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
			// | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
			// i.setComponent(name);
			//
			// startActivity(i);
		}
	}

	public boolean startNativeVideo(final JSONObject jsonVideoInfo) {
		
//		boolean isOld = false;
		
//		if (mController != null) {
//			mController.cancelUpdateTimehandle();
////			mController.setMessage(Vp9Contant.MSG_RESQUEST_PLAY_VIDEO);
//			Log.d(TAG, "startNativeVideo mController.destroy(1);");
//			mController.destroy(1);
//			isOld = true;
//		}
		this.is3D = Vp9ParamUtil.getJSONBoolean(jsonVideoInfo, "is3D", false);

//		int rotate = Vp9ParamUtil.getJsonInt(jsonVideoInfo, "rotate", 0);
//		Log.d(TAG, "rotate: " + rotate);
//
//		if (rotate == 0) {
//			mController = new MediaController(this);
//		} else {
//			mController = new RotationMediaController(this);
//		}
//		
//		if(!isOld){
//			mController.cancelUpdateTimehandle();
//			mController.setMessage(Vp9Contant.MSG_RESQUEST_PLAY_VIDEO);
//		}
		mController.isRefreshNotify = true;
		clearWebViewCache();
		clearOtherApp();
		Log.d(TAG, "startNativeVideo");
		isShowEPG = false;



		mController.setIs3D(is3D);
		mController.setActivity(this);
		mController.setProxySevice(proxySevice);
		ArrayList<String> subtitles = com.vp9.util.AppPreferences.INSTANCE.getSubtitles();
		mController.setSettingSubTypes(subtitles);
		initViewIdForPlayer(mController);
		mController.setMainLayout(this.vp9PlayerLayout);
		mController.init();
		mController.setMessage(Vp9Contant.MSG_RESQUEST_PLAY_VIDEO);
		setVisibility(webView, View.GONE);
		registerListenerForVp9Player(jsonVideoInfo);
		mController.cancelUpdateTimehandle();
		mController.showController(0);
		
		if(is3D){
			this.vp9PlayerLayout.setOnHoverListener(new View.OnHoverListener() {
				@Override
				public boolean onHover(View v, MotionEvent event) {

					return onVp9TouchEvent(event);
				}
			});
		}

		return this.mController.startNaviteVideo(jsonVideoInfo);
	}

	public void clearWebViewCache() {
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				webView.clearHistory();
				webView.clearCache(true);
				webView.clearAnimation();
				
			}
		});
		
	}

	private void clearOtherApp() {
		Thread clearThread = new Thread() {

			public void run() {
				OperUtil.removeOtherApps(VpMainActivity.this);
			}

		};

		clearThread.setName("clearThread_101");
		clearThread.start();
	}

	private void setVisibility(final View appView, final int visibility) {
		if (appView == null) {
			return;
		}
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (appView.getVisibility() != visibility) {
					appView.setVisibility(visibility);
				}
			}
		});

	}

	private void registerListenerForVp9Player(final JSONObject jsonVideoInfo) {
		try {
			this.videoType = jsonVideoInfo.getInt("videoType");
			this.mController.btnSetting.setOnClickListener(new Button.OnClickListener() {
				@Override
				public void onClick(View v) {
					handleSettingButtonEvent(v);
				}
			});

			this.mController.btnSetting2.setOnClickListener(new Button.OnClickListener() {
				@Override
				public void onClick(View v) {
					handleSettingButtonEvent(v);
				}
			});

			this.mController.btnBack.setOnClickListener(new Button.OnClickListener() {
				@Override
				public void onClick(View v) {
					handleBackButtonEvent(v, videoType);
				}
			});

			if (is3D) {
				this.mController.btnBack2.setOnClickListener(new Button.OnClickListener() {
					@Override
					public void onClick(View v) {
						handleBackButtonEvent(v, videoType);
					}
				});
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	protected void handleBackButtonEvent(View view, int videoType) {
		// this.vp9Player.destroy(1);
		// this.vp9PlayerLayout.setVisibility(View.GONE);
		// setVisibility(appView, View.VISIBLE);
		// appView.setVisibility(View.VISIBLE);
		JSONObject eventData = new JSONObject();
		EventPlayerPlugin bridge = (EventPlayerPlugin) webView.pluginManager.getPlugin("EventPlayerPlugin");
		try {
			eventData.put("action", "backPlayer");
			eventData.put("videoType", videoType);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		bridge.reportEvent(eventData);
	}

	protected void handleBackButtonEventForNew(View view, int videoType) {
		// this.newVp9Player.destroy(1);
		// this.vp9PlayerLayout.setVisibility(View.GONE);
		// setVisibility(appView, View.VISIBLE);
		// appView.setVisibility(View.VISIBLE);
		JSONObject eventData = new JSONObject();
		EventPlayerPlugin bridge = (EventPlayerPlugin) webView.pluginManager.getPlugin("EventPlayerPlugin");
		try {
			eventData.put("action", "backPlayer");
			eventData.put("videoType", videoType);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		bridge.reportEvent(eventData);
	}

	private void initViewIdForPlayer(MediaController mController) {
		
		mController.parent_layout_id = R.id.vp9_player_layout;

		mController.video_view_id = R.id.video_view;

		mController.pdLoading_id = R.id.pdLoading;

		mController.load_rate_id = R.id.load_rate;

		mController.tvSub_id = R.id.tvSub;

		mController.tvSubMargin_id = R.id.tvSubMargin;

		mController.seekBar_id = R.id.seekBar;

		mController.tvFrom_id = R.id.tvFrom;

		mController.tvTo_id = R.id.tvTo;

		mController.btnPlay_id = R.id.btnPlay;

		mController.btnSub_id = R.id.btnSub;

		mController.btnBack_id = R.id.btnBack;

		mController.btnSetting_id = R.id.btnSetting;

		mController.controller_id = R.id.controller;

		mController.controller_top_id = R.id.ControlTop;

		mController.vp9_player_layout_id = R.id.vp9_player_layout;

		mController.loading_layout_id = R.id.loading_layout;

		mController.subtitles_layout_id = R.id.subtitles_layout;

		mController.progess_id = R.id.progess;

		mController.vp9_btn_play_id = R.drawable.vp9_btn_play;

		mController.vp9_btn_pause_id = R.drawable.vp9_btn_pause;

		mController.vp9_btn_sub_id = R.drawable.vp9_btn_sub;

		mController.vp9_btn_sub_hide_id = R.drawable.vp9_btn_sub_hide;

		mController.vp9ChannelImage_id = R.id.Vp9ChannelImage;

		mController.video_title_layout_id = R.id.video_title_layout;

		mController.logo_video_id = R.id.logo_video;

		mController.video_title_id = R.id.video_title;

		mController.logo_id = R.id.logo;

		mController.logo_text_id = R.id.logo_text;

		mController.logo_layout_id = R.id.logo_layout;

		mController.btnChoose_id = R.id.btnChoose;

		mController.btnPrev_id = R.id.btnPrev;

		mController.btnNext_id = R.id.btnNext;

		mController.notify_id = R.id.notify;

		mController.vp9_btn_next_hide_id = R.drawable.vp9_btn_next_hide;

		mController.vp9_btn_prev_hide_id = R.drawable.vp9_btn_prev_hide;

		mController.vp9_btnSetting_hide_id = R.drawable.vp9_btn_setting_hide;

		mController.vp9_btnSetting_id = R.drawable.vp9_btn_setting;

		mController.vp9_btn_next_id = R.drawable.vp9_btn_next;

		mController.vp9_btn_prev_id = R.drawable.vp9_btn_prev;

		// view 2

		mController.pdLoading_id2 = R.id.pdLoading2;

		mController.load_rate_id2 = R.id.load_rate2;

		mController.tvSub_id2 = R.id.tvSub2;

		mController.tvSubMargin_id2 = R.id.tvSubMargin2;

		mController.seekBar_id2 = R.id.seekBar2;

		mController.tvFrom_id2 = R.id.tvFrom2;

		mController.tvTo_id2 = R.id.tvTo2;

		mController.btnPlay_id2 = R.id.btnPlay2;

		mController.btnSub_id2 = R.id.btnSub2;

		mController.btnBack_id2 = R.id.btnBack2;

		mController.btnSetting_id2 = R.id.btnSetting2;

		mController.controller_id2 = R.id.controller2;

		mController.controller_top_id2 = R.id.ControlTop2;

		mController.loading_layout_id2 = R.id.loading_layout2;

		mController.subtitles_layout_id2 = R.id.subtitles_layout2;

		mController.progess_id2 = R.id.progess2;

		mController.vp9ChannelImage_id2 = R.id.Vp9ChannelImage2;

		mController.video_title_layout_id2 = R.id.video_title_layout2;

		mController.logo_video_id2 = R.id.logo_video2;

		mController.video_title_id2 = R.id.video_title2;

		mController.logo_id2 = R.id.logo2;

		mController.logo_text_id2 = R.id.logo_text2;

		mController.logo_layout_id2 = R.id.logo_layout2;

		mController.btnChoose_id2 = R.id.btnChoose2;

		mController.btnPrev_id2 = R.id.btnPrev2;

		mController.btnNext_id2 = R.id.btnNext2;

		mController.notify_id2 = R.id.notify2;

	}

	protected void handleSettingButtonEvent(View v) {
		handleShowOrCloseEPG();
	}

	public void handleShowOrCloseEPG() {
		if (!this.isShowEPG) {
			showEPG();
		} else {
			closeEPG();
		}

	}

	public void closeEPG() {
		this.isShowEPG = false;
		setVisibility(webView, View.GONE);
		// setVisibility(webView, View.INVISIBLE);
		if (mController != null) {
			if (mController.is3D) {
				CordovaWebView webView2 = (CordovaWebView) findViewById(R.id.cordova_web_view2);
				setVisibility(webView2, View.GONE);
			}
			mController.closeEPG();
			// JSONObject eventData = new JSONObject();
			// EventPlayerPlugin bridge = (EventPlayerPlugin)
			// appView.pluginManager.getPlugin("EventPlayerPlugin");
			// try {
			// eventData.put("action", "closeEPG");
			// } catch (JSONException e) {
			// e.printStackTrace();
			// }
			// bridge.reportEvent(eventData);
		}

	}

	public void showEPG() {
		this.isShowEPG = true;
		Log.d(TAG, "showEPG A3");
		if (mController != null) {

			Log.d(TAG, "showEPG A4");
			mController.showEPG();

			setVisibility(webView, View.VISIBLE);
			Log.d(TAG, "showEPG A5");
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					webView.requestFocus();

				}
			});
			// appView.setVisibility(View.VISIBLE);
			JSONObject eventData = new JSONObject();
			EventPlayerPlugin bridge = (EventPlayerPlugin) webView.pluginManager.getPlugin("EventPlayerPlugin");
			try {
				eventData.put("action", "showEPG");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			bridge.reportEvent(eventData);
		}
	}

	// public boolean handlerEnventShowEPG() {
	// if (this.mController != null) {
	// return this.mController.showEPG();
	// }
	//
	// return false;
	// }

	// public boolean handlerEnventCloseEPG() {
	// if (this.mController != null) {
	// return this.mController.closeEPG();
	// }
	//
	// return false;
	// }

	public void playRemoteVideo() {

		if (this.mController != null && this.mController.vp9Player != null) {
			if (this.isShowEPG = true) {
				closeEPG();
				this.mController.vp9Player.startVideo();
			} else {
				this.mController.vp9Player.startVideo();
			}
		}
	}

	public void pauseRemoteVideo() {
		if (this.mController != null && this.mController.vp9Player != null) {
			// this.mController.vp9Player.pause();
			this.mController.handleClickPlayAndPause();
		}
	}

	public void stopRemoteVideo() {
		if (this.mController != null) {
			Log.d(TAG, "stopRemoteVideo mController.destroy(1);");
			mController.setFocusView(this.getAppview());
			// mController.showController(1);

			setVisibility(mController.controllerLayout, View.VISIBLE);
			this.mController.destroy(1);
			// setVisibility(this.vp9PlayerLayout, View.GONE);

			setVisibility(mController.controllerTopLayout, View.GONE);
			setVisibility(mController.progessLayout, View.GONE);
			// if(mController.is3D){
			// setVisibility(mController.controllerLayout2, View.GONE);
			// setVisibility(mController.controllerTopLayout2, View.GONE);
			// setVisibility(mController.progessLayout2, View.GONE);
			// }
			setVisibility(mController.controllerLayout2, View.GONE);
			setVisibility(mController.controllerTopLayout2, View.GONE);
			setVisibility(mController.progessLayout2, View.GONE);
			setVisibility(uiView2, View.GONE);
			// isShowEPG = true;
			setVisibility(webView, View.VISIBLE);
			// JSONObject eventData = new JSONObject();
			// EventPlayerPlugin bridge = (EventPlayerPlugin)
			// appView.pluginManager.getPlugin("EventPlayerPlugin");
			// try {
			// eventData.put("action", "stopPlayer");
			// eventData.put("videoType", videoType);
			// } catch (JSONException e) {
			// e.printStackTrace();
			// }
			// bridge.reportEvent(eventData);
			if (this.mController.isRemoteListener) {
				try {
					JSONObject jsonEvent = new JSONObject();
					jsonEvent.put("action", "stop");
					sendEvent(jsonEvent);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

		}
	}

	public void seekRemoteVideo(int timeSeek) {
		if (this.mController != null && this.mController.vp9Player != null) {
			this.mController.vp9Player.seekTo(timeSeek);
		}
	}

	public void getTimeRemoteVideo(CallbackContext callbackContext) {
		if (this.mController != null) {
			int currentPosition = this.mController.getTime();
			JSONObject eventData = new JSONObject();
			EventPlayerPlugin bridge = (EventPlayerPlugin) webView.pluginManager.getPlugin("EventPlayerPlugin");
			try {
				eventData.put("action", "time");
				eventData.put("time", currentPosition);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			// bridge.reportEvent(eventData);
			callbackContext.success(eventData);
		}
	}

	public void getSubtitleRemoteVideo(CallbackContext callbackContext) {
		if (this.mController != null) {
			JSONArray jsonArrSub = this.mController.changeSubtitle(null);
			JSONObject eventData = new JSONObject();
			EventPlayerPlugin bridge = (EventPlayerPlugin) webView.pluginManager.getPlugin("EventPlayerPlugin");
			try {
				eventData.put("action", "getSubtitle");
				eventData.put("subtitle", jsonArrSub);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			// bridge.reportEvent(eventData);
			callbackContext.success(eventData);

		}
	}

	public void changeSubtitleRemoteVideo(ArrayList<ChangeSubtitle> changeSubtitles) {
		if (this.mController != null) {
			JSONArray jsonArrSub = this.mController.changeSubtitle(changeSubtitles);
			JSONObject eventData = new JSONObject();
			EventPlayerPlugin bridge = (EventPlayerPlugin) webView.pluginManager.getPlugin("EventPlayerPlugin");
			try {
				eventData.put("action", "changeSubtitle");
				eventData.put("subtitle", jsonArrSub);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			bridge.reportEvent(eventData);
		}
	}

	public void resendInfoRemoteVideo(CallbackContext callbackContext) {
		if (this.mController != null) {
			JSONObject jsonInfCurVideo = this.mController.getInfoCurrentVideo();
			JSONObject eventData = new JSONObject();
			EventPlayerPlugin bridge = (EventPlayerPlugin) webView.pluginManager.getPlugin("EventPlayerPlugin");
			try {
				eventData.put("action", "resend_video_info");
				eventData.put("resend_information", jsonInfCurVideo);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			bridge.reportEvent(eventData);
			// callbackContext.success(eventData);

		}
	}

	public void addListenerRemoteVideo() {
		if (this.mController != null) {
			this.mController.addListenerRemoteVideo();
		}
	}

	public void sendEvent(JSONObject jsonEvent) {
		EventPlayerPlugin bridge = (EventPlayerPlugin) webView.pluginManager.getPlugin("EventPlayerPlugin");
		bridge.reportEvent(jsonEvent);
	}

	public void changeDisplayScreen(int intFullScreen) {
		if (this.mController != null) {
			this.mController.changeDisplayScreen(intFullScreen);
		}
	}

	public void changeScreenOrientation(String orientation) {
		if (this.mController != null) {
			this.mController.changeScreenOrientation(orientation);
		}
	}

	public void saveSubtiles(ArrayList<String> subTypes) {
		com.vp9.util.AppPreferences.INSTANCE.saveSubtitles(subTypes);
	}

	public void setMessage(String msg) {
		if (this.mController != null) {
			this.mController.setMessage(msg);
		}
	}

	public void playRelateVideo(JSONObject json) {
		Log.d(TAG, "playRelateVideo - playType: ");
		if (this.mController != null) {
			this.mController.playRelateVideo(json);
		}

	}

	public void setRightVideoDisplay(JSONObject json) {
		Log.d(TAG, "setRightVideoDisplay");
		if (this.mController != null) {
			this.mController.setRightVideoDisplay(json);
		}
	}

	public boolean runApp(String js) {
		Log.d(TAG, js);
		if (webView == null) {
			return false;
		}
		webView.sendJavascript(js);
		return true;
	}

	public boolean checkDisplayEPG() {
		// JSONObject eventData = new JSONObject();
		// EventPlayerPlugin bridge = (EventPlayerPlugin)
		// appView.pluginManager.getPlugin("EventPlayerPlugin");
		// try {
		// eventData.put("action", "checkDisplayEPG");
		// eventData.put("isShowEPG", isShowEPG);
		// } catch (JSONException e) {
		// e.printStackTrace();
		// }
		// bridge.reportEvent(eventData);

		return isShowEPG;
	}

	public CordovaWebView getAppview() {
		return webView;
	}

	@Override
	protected void loadSpinner() {
		// If loadingDialog property, then show the App loading dialog for first
		// page of app
		String loading = null;
		if ((this.webView == null) || !this.webView.canGoBack()) {
			loading = this.getStringProperty("LoadingDialog", null);
		} else {
			loading = this.getStringProperty("LoadingPageDialog", null);
		}
		if (loading != null) {

			String title = "";
			String message = "Loading Application...";

			if (loading.length() > 0) {
				int comma = loading.indexOf(',');
				if (comma > 0) {
					title = loading.substring(0, comma);
					message = loading.substring(comma + 1);
				} else {
					title = "";
					message = loading;
				}
			}
			this.spinnerStart(title, message);
		}
	}

	public boolean onVp9TouchEvent(MotionEvent event) {
		int action = event.getAction() & MotionEvent.ACTION_MASK;
		float x = event.getX();
		// float y = event.getY();
		int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
		Log.d(TAG, "x1 = " + x + ", screenWidth1 = " + screenWidth);
		switch (action) {
		case MotionEvent.ACTION_MOVE:
			if (mController != null) {
				Log.d(TAG, "x2 = " + x + ", screenWidth2 = " + screenWidth);
				// int screenHeight =
				// getWindowManager().getDefaultDisplay().getHeight();
				if (x <= screenWidth / 2 && mController.popupVideoWindow != null && mController.popupVideoWindow.isShowing()) {
					if (mController.popupVideoWindow2 != null) {
						mController.popupVideoWindow2.getContentView().clearFocus();
					}
					mController.popupVideoWindow.getContentView().requestFocus();
				} else if (x > screenWidth / 2 && mController.popupVideoWindow2 != null && mController.popupVideoWindow2.isShowing()) {
					if (mController.popupVideoWindow != null) {
						mController.popupVideoWindow.getContentView().clearFocus();
					}
					mController.popupVideoWindow2.getContentView().requestFocus();
				}
			}

			break;

		}

		return true;
	}

	public void setProxySevice(MyService proxySevice) {
		this.proxySevice = proxySevice;
		if (mController != null) {
			mController.setProxySevice(proxySevice);
		}

	}

	public MyService getProxySevice() {
		return this.proxySevice;
	}

	public void changeSource(JSONObject jsonVideoInfo) {
		String videoUrl = Vp9ParamUtil.getJsonString(jsonVideoInfo, "videoUrl", "").trim();
		String cookie = Vp9ParamUtil.getJsonString(jsonVideoInfo, "cookie", "").trim();
		if (videoUrl != null && !"".equals(videoUrl.trim()) && mController != null) {
			mController.changeSource(videoUrl, cookie);
		}
	}

	@Override
	public void onReceivedError(int errorCode, final String description, String failingUrl) {
		final CordovaActivity me = this;
		final String errorUrl = me.getStringProperty("errorUrl", null);
		if ((errorUrl != null) && (errorUrl.startsWith("file://") || org.apache.cordova.Config.isUrlWhiteListed(errorUrl)) && (!failingUrl.equals(errorUrl))) {
			me.runOnUiThread(new Runnable() {
				public void run() {
					me.spinnerStop();
					if (webView != null) {
						me.displayError("Thông báo", description, "OK", true);
					}
				}
			});
		} else {
			final boolean exit = !(errorCode == WebViewClient.ERROR_HOST_LOOKUP);
			me.runOnUiThread(new Runnable() {
				public void run() {
					if (webView != null) {
						webView.setVisibility(View.GONE);
					}
					spinnerStop();
					me.displayError("Thông báo", description, "OK", true);
				}
			});
		}
	}
	
	@Override
    public boolean onTouchEvent(MotionEvent event) {
		Log.d(TAG, "Touch Event: " + event.getAction());
		OperUtil.displayToast("Touch Event: " +  + event.getAction(), this, 1);
		return super.onTouchEvent(event);
    }
	
	
	public void downloadTest(){
//		DownloadDataChannel obj = new DownloadDataChannel();
//		obj.onHandleIntentTest(new Intent());
	}
}
