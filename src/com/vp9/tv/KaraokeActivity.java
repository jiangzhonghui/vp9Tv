package com.vp9.tv;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.Config;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tv.vp9.videoproxy.MyService;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.RelativeLayout;

import com.vp9.laucher.main.application.Vp9Application;
import com.vp9.laucher.main.util.MyPreference;
import com.vp9.player.controller.H265KaraoVp9Player;
import com.vp9.player.controller.NativeKaraoVp9Player;
import com.vp9.player.model.VideoResolution;
import com.vp9.player.vp9Interface.Vp9KaraoPlayerInterface;
import com.vp9.plugin.EventKaraokePlugin;
import com.vp9.tv.R;
import com.vp9.util.Vp9ParamUtil;

public class KaraokeActivity extends Activity implements CordovaInterface {

	private static final String TAG = "KaraokeActivity";
	
	private CordovaWebView webview;
	
	private ArrayList<VideoResolution> videoResolutions;
	
	private int playType;

	private String codecResolution;

	private String codec;

	public int mVideoWidth;

	public int mVideoHeight;

	private Vp9KaraoPlayerInterface vp9Player;

	public int mCurrentState;

	public boolean isPlay;

	public boolean clearParam = false;

	public int mScreenWidth;

	public int mScreenHeight;

	public int intFullScreen;
	
	public int state = 0;
	
	public int intProxy;
	
	public long currentError = 0L;
	
	public long curPosition = 0;
	
	public long duration;
	
	public int count = 0;
	
	public boolean isResume;
	
	public boolean isSeek;
	
	public boolean isError;
	
	public String videoUrl;
	
	public Handler mResumErrorHandle = new Handler();
	
	public Handler updateTimehandle = new Handler();
	
	public SurfaceView mVideoView;
	
	public SurfaceHolder mHolder;
	
	public int mSeekWhenPrepared;

	public boolean[] listenerStates;
	
	public int startCount;
	
	private MyService proxySevice;
	
	private final ExecutorService threadPool = Executors.newCachedThreadPool();
	
	protected CordovaPlugin activityResultCallback = null;
	
	private int FPS = 1000/10;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Log.e(TAG, "--------- A1 KaraokeActivity");
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.vp9_karaoke_player);
		listenerStates = new boolean[9];
		Arrays.fill(listenerStates, false);
		mVideoView = (SurfaceView) findViewById(R.id.video_view);
		Config.init(this);
	    webview = (CordovaWebView) findViewById(R.id.cordova_web_view);
	    Intent callerIntent = getIntent();
		Bundle serInfoCaller = callerIntent.getBundleExtra("ServerInfo");
		if (serInfoCaller == null || !serInfoCaller.containsKey("url")) {
			exitApp();
			return;
		}

		String url = serInfoCaller.getString("url");
		
	    if (webview != null) {
	    	Log.e(TAG, "--------- : webview != null"); 
//	    	webview.loadUrl("http://10.10.10.159/phunn");
	    	webview.loadUrl(url);
		}else{
			Log.e(TAG, "--------- : webview == null");
		}

	 
	    webview.setBackgroundColor(Color.TRANSPARENT);
	    webview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//	    webview.setAlpha(0.5f);
//	    
//	    RelativeLayout relativeWebview = (RelativeLayout) findViewById(R.id.webview_layout);
//	    relativeWebview.setBackgroundColor(0x00000000);

	    
	    
	    mVideoView.requestFocus();
		mHolder = mVideoView.getHolder();
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	    
//	    JSONObject jsonObject = new JSONObject();
//	    try {
//			jsonObject.put("videoUrl", "http://f.vp9.tv/720p/2013/planes.2013.vp9.mp4");
//			setSourceKaraoke(jsonObject, null);
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	    
	    
	}

	public void setSourceKaraoke(JSONObject jsonVideoInfo, CallbackContext callbackContext) throws JSONException {
		String videoUrl = jsonVideoInfo.getString("videoUrl");
		this.playType = Vp9ParamUtil.getJsonInt(jsonVideoInfo, "playType", 0);
		this.intProxy = Vp9ParamUtil.getJsonInt(jsonVideoInfo, "proxy", 0);
		this.videoResolutions = getVideoResolutions(jsonVideoInfo);
		playVideo(videoResolutions, videoUrl, false, callbackContext);
		
	}
	
	private void playVideo(ArrayList<VideoResolution> videoResolutions,
			String videoUrl, boolean loop, final CallbackContext callbackContext) {
		final ArrayList<VideoResolution> newVideoResolutions = videoResolutions;
		
		final String newVideoUrl = videoUrl;
		
		final boolean newLoop = loop;
		
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				threadPlayVideo(newVideoResolutions, newVideoUrl, newLoop, callbackContext);
				
			}
		});
		
	}
	
	public Runnable mResumErrorVideoTask = new Runnable() {
		public void run() {
			if (clearParam) {
				return;
			}
			vp9Player.playVideo(videoUrl, false);

		}
	};

	protected void threadPlayVideo(
			ArrayList<VideoResolution> newVideoResolutions, String videoUrl,
			boolean newLoop, CallbackContext callbackContext) {
		int type = 0;
		String newVideoUrl = videoUrl;
		int resolutionIndex = 0;
//		String resolution;
		if(videoResolutions != null && videoResolutions.size() > 0){

			boolean isExist = false;
			if(this.codecResolution != null && this.codec != null){
				for(int i = 0; i < videoResolutions.size(); i++){
					VideoResolution videoResolution = videoResolutions.get(i);
					String codec = videoResolution.getCodec();
					String resolution = videoResolution.getResolution();
					StringBuffer key = new StringBuffer();
					if(codec != null){
						key.append(codec.trim());
					}
					if(resolution != null){
						key.append("-").append(resolution.trim());
					}
					if(this.codecResolution.equals(key.toString().trim()) && this.codec.equals(codec)){
						
						Log.d(TAG, "Codec - Resolution: " + this.codecResolution);
						if("h264".equals(codec)){
							type = 0;
						}else{
							type = 1;
						}
						
						resolutionIndex = i;
						Log.d(TAG, "Codec - resolutionIndex 1: " + resolutionIndex);
						newVideoUrl = videoResolution.getVideoUrl();
						
						updateVideoSize(resolution);
						isExist = true;
						break;
					}
				}
			}

			
			if(!isExist && (playType == 0 || playType == 1)){
				for(int i = 0; i < videoResolutions.size(); i++){
					VideoResolution videoResolution = videoResolutions.get(i);
					String codec = videoResolution.getCodec();
//					resolution = videoResolution.getResolution();
					if("h264".equals(codec)){
						type = 0;
						resolutionIndex = i;
						Log.d(TAG, "Codec - resolutionIndex 2: " + resolutionIndex);
						newVideoUrl = videoResolution.getVideoUrl();
						String resolution = videoResolution.getResolution();
						updateVideoSize(resolution);
						StringBuffer key = new StringBuffer();
						if(codec != null){
							key.append(codec.trim());
						}
						if(resolution != null){
							key.append("-").append(resolution.trim());
						}
						this.codecResolution = key.toString();
						this.codec = codec;
						isExist = true;
						break;
					}
				}
			}
			
			if(!isExist && playType == 2){
				for(int i = 0; i < videoResolutions.size(); i++){
					VideoResolution videoResolution = videoResolutions.get(i);
					String codec = videoResolution.getCodec();
//					resolution = videoResolution.getResolution();
					if("h265".equals(codec)){
						type = 1;
						resolutionIndex = i;
						Log.d(TAG, "Codec - resolutionIndex 3: " + resolutionIndex);
						newVideoUrl = videoResolution.getVideoUrl();
						String resolution = videoResolution.getResolution();
						updateVideoSize(resolution);
						StringBuffer key = new StringBuffer();
						if(codec != null){
							key.append(codec.trim());
						}
						if(resolution != null){
							key.append("-").append(resolution.trim());
						}
						this.codecResolution = key.toString();
						this.codec = codec;
						isExist = true;
						break;
					}
				}
			}else if(!isExist){
				VideoResolution videoResolution = videoResolutions.get(0);
				String codec = videoResolution.getCodec();
				if("h265".equals(codec)){
					type = 1;
				}else{
					type = 0;
				}
				resolutionIndex = 0;
				Log.d(TAG, "Codec - resolutionIndex 4: " + resolutionIndex);
				newVideoUrl = videoResolution.getVideoUrl();
				String resolution = videoResolution.getResolution();
				updateVideoSize(resolution);
				StringBuffer key = new StringBuffer();
				if(codec != null){
					key.append(codec.trim());
				}
				if(resolution != null){
					key.append("-").append(resolution.trim());
				}
				this.codecResolution = key.toString();
				this.codec = codec;
			}
			
		}
		
		Log.d(TAG, "playVideo - type:" + type);
		Log.d(TAG, "playVideo - newVideoUrl: " + newVideoUrl);
//		if(mVideoView != null){
//			mVideoView.destroyDrawingCache();
//			mVideoView.refreshDrawableState();
//		}
		
		if(vp9Player != null){
			vp9Player.release();
		}
		
//		newVideoUrl = "http://f.vp9.tv/music/tru_tinh/Thuong_hoai_ngan_nam-Phuong_Diem_Hanh/mvhd.SD5.mp4";
//		type = 1;
		if(type == 1){
			vp9Player = new H265KaraoVp9Player(this);
			vp9Player.playVideo(newVideoUrl, false);
		}else{
			vp9Player = new NativeKaraoVp9Player(this);
			vp9Player.playVideo(newVideoUrl, false);
		}
		
		if(callbackContext != null){
			callbackContext.success();
			Date date = new Date(System.currentTimeMillis());
			Log.e(TAG, "setsource response: " + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds());
		}
	}
	
	
	public void updateVideoSize(JSONObject jsonVideoInfo) {
		String videoSize = Vp9ParamUtil.getJsonString(jsonVideoInfo, "video_size", "");

		String[] elemSizes = videoSize.trim().split("x");

		int width = 0, height = 0;

		if (elemSizes != null && elemSizes.length == 2) {
			width = Vp9ParamUtil.getValue(elemSizes[0], 0);
			height = Vp9ParamUtil.getValue(elemSizes[1], 0);
		}

		this.mVideoWidth = width;

		this.mVideoHeight = height;
		
	}
	
	private void updateVideoSize(String resolution) {
		
		if(resolution != null){

			String[] elemSizes = resolution.trim().split("x");

			int width = 0, height = 0;

			if (elemSizes != null && elemSizes.length == 2) {
				
				width = Vp9ParamUtil.getValue(elemSizes[0], 0);
				
				height = Vp9ParamUtil.getValue(elemSizes[1], 0);
				
			}

			this.mVideoWidth = width;

			this.mVideoHeight = height;
		}
	}

	public ArrayList<VideoResolution> getVideoResolutions(JSONObject jsonVideoInfo){
		try {
			JSONArray jsaElems = Vp9ParamUtil.getJSONArray(jsonVideoInfo, "src_list");
			
			ArrayList<VideoResolution> videoResolutions = new ArrayList<VideoResolution>();
					
			if(jsaElems != null){
				
				for(int i = 0; i < jsaElems.length(); i++){
					
					JSONObject jsaElem = jsaElems.getJSONObject(i);
					
					if(jsaElem != null){
						
						String video_url = Vp9ParamUtil.getJsonString(jsaElem, "src", "");
						
						String type = Vp9ParamUtil.getJsonString(jsaElem, "type", "");
						
						String codec = Vp9ParamUtil.getJsonString(jsaElem, "codec", "");
						
						int bitrate = Vp9ParamUtil.getJsonInt(jsaElem, "bitrate", 0);
						
						String quality = Vp9ParamUtil.getJsonString(jsaElem, "quality", "");
						
						String resolution = Vp9ParamUtil.getJsonString(jsaElem, "resolution", "");
						
						int rotate = Vp9ParamUtil.getJsonInt(jsaElem, "rotate", 0);
						
						VideoResolution videoResolution = new VideoResolution();
						
						videoResolution.setVideoUrl(video_url);
						
						videoResolution.setType(type);
						
						videoResolution.setCodec(codec);
						
						videoResolution.setBitrate(bitrate);
						
						videoResolution.setQuality(quality);
						
						videoResolution.setResolution(resolution);
						
						videoResolution.setRotation(rotate);
						
						videoResolutions.add(videoResolution);
						
					}
				}
			}
			return videoResolutions;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void setFullScreen() {
		
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
				getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
				mVideoView.setKeepScreenOn(true);
			}
		});
		
	}

	public void setVisibility(final View view, final int type) {
		
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				view.setVisibility(type);
			}
		});
		
	}
	
	public Runnable mUpdateTimeTask = new Runnable() {

		

		public void run() {
			Log.d(TAG, "mUpdateTimeTask Start");
			if (clearParam) {
				return;
			} 
			if (vp9Player.isInPlaybackState()) {
				try {
					// Log.e(TAG, "----- mUpdateTimeTask - duration: " +
					// duration);
					long l1 = vp9Player.getCurrentPosition();
					long l2 = vp9Player.getDuration();

					if (l1 == 0) {
						if (currentError > 0 && isPlay) {
							vp9Player.seekTo((int) currentError);
							return;
						} else if (isResume) {
							// mVideoView.seekTo(0);
						}
					}

					if (curPosition == l1 && curPosition != 0) {
						count++;
						if(listenerStates[EventKaraokePlugin.ACTION_BIND_EVENT_WAITING_KARAO_INDEX]){
							try {
								JSONObject jsonEvent = new JSONObject();
								jsonEvent.put("time", curPosition);
								sendEvent(EventKaraokePlugin.ACTION_BIND_EVENT_WAITING_KARAO_LISTENER,jsonEvent);
							} catch (JSONException e) {
								e.printStackTrace();
							}	
						}
						if (count == 20) {
							updateTimehandle.removeCallbacks(this);
							vp9Player.pause();
							count = 0;
							Thread startThread = new Thread(){
								public void run() {

								if (updateTimehandle != null) {
									updateTimehandle.removeCallbacks(mUpdateTimeTask);
								}
								vp9Player.resume();
								// startVideo();
							  }
						   };
						   startThread.setName("mUpdateTimeThr_26");
						   startThread.start();
							return;
						}

					} else {
						count = 0;
					}

					curPosition = l1;
					
					if(listenerStates[EventKaraokePlugin.ACTION_BIND_EVENT_TIME_UPDATE_KARAO_INDEX]){
						try {
							sendEvent(EventKaraokePlugin.ACTION_BIND_EVENT_TIME_UPDATE_KARAO_LISTENER, curPosition);
						} catch (Exception e) {
							e.printStackTrace();
						}	
					}
					
					isResume = false;
					duration = l2;
					if (state == 1) {
						updateTimehandle.postDelayed(this, FPS);
					}
					if (l1 > currentError) {
						currentError = l1;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		}
	};

	

	public void setPlayKaraoke(CallbackContext callbackContext) {
		if (vp9Player != null && !vp9Player.isPlaying()) {
			boolean isSuccess = vp9Player.startVideo();
			if(isSuccess){
				callbackContext.success("play");
			}else{
				callbackContext.error("fail");
			}
		}else{
			callbackContext.error("fail");
		}
	}

	public void setPauseKaraoke(CallbackContext callbackContext) {
		if (vp9Player != null && vp9Player.isPlaying()) {
			boolean isSuccess = vp9Player.pause();
			if(isSuccess){
				callbackContext.success("pause");
			}else{
				callbackContext.error("fail");
			}
		}else{
			callbackContext.error("fail");
		}
	}

	public void setSeekKaraoke(int seek, CallbackContext callbackContext) {
		if (vp9Player != null) {
			boolean isSuccess = vp9Player.seekTo(seek);
			if(isSuccess){
				callbackContext.success();
			}else{
				callbackContext.error("fail");
			}
		}else{
			callbackContext.error("fail");
		}
		
	}

	public void setStopKaraoke(CallbackContext callbackContext) {
		try {
			Log.e(TAG, "setStopKaraoke");
			if (vp9Player != null) {
				vp9Player.release();
			}
			cancelTask();
//			clearParam();
			reset();
			callbackContext.success("stop");
		} catch (Exception e) {
			e.printStackTrace();
			callbackContext.error("Fail");
		}

		
	}
	
	
	public void cancelTask() {
		try {
			state = 0;
			// mVideoView.release();
			Log.e(TAG, "cancelTask 1");
			if (this.updateTimehandle != null) {
				this.updateTimehandle.removeCallbacks(this.mUpdateTimeTask);
			}

			if (this.mResumErrorHandle != null) {
				this.mResumErrorHandle.removeCallbacks(this.mResumErrorVideoTask);
			}

			Log.e(TAG, "cancelTask 2");
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.e(TAG, "cancelTask 3");
	}

	public void reset() {
		isPlay = false;
		isError = false;
		currentError = 0l;
		isResume = false;
		isSeek = false;
		isResume = false;
		this.mVideoWidth = 0;
		this.mVideoHeight = 0;
		this.state = 0;
		Arrays.fill(listenerStates, false);
		startCount = 0;
	}

	public void clearParam() {
		clearParam = true;
		mVideoView = null;
		updateTimehandle = null;
		mResumErrorHandle = null;
		vp9Player = null;
		mSeekWhenPrepared = 0;
		listenerStates = null;
		startCount = 0;
	}

	public void getVideoUrlKaraoke(CallbackContext callbackContext) {
		
		try {
			JSONObject message = new JSONObject();
			if(videoUrl != null){
				message.put("videoUrl", videoUrl);
			}else{
				message.put("videoUrl", "");
			}
			callbackContext.success(message);
		} catch (JSONException e) {
			callbackContext.error("Fail: " + e.getMessage());
			e.printStackTrace();
		}
		
		
	}

	public void getCurrentTimeKaraoke(CallbackContext callbackContext) {
		try {
			if(vp9Player != null){
				int currentTime = vp9Player.getCurrentPosition();
//				JSONObject message = new JSONObject();
//				message.put("currentTime", currentTime);
				callbackContext.success(currentTime + "");
				return;
			}
			callbackContext.error("Fail: player is destroy");
		} catch (Exception e) {
			callbackContext.error("Fail: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void getDurationKaraoke(CallbackContext callbackContext) {
		try {
			if(vp9Player != null){
				int duration = vp9Player.getDuration();
//				JSONObject message = new JSONObject();
//				message.put("duration", duration);
				callbackContext.success(duration + "");
				return;
			}
			callbackContext.error("Fail: player is destroy");
		} catch (Exception e) {
			callbackContext.error("Fail: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void getStateKaraoke(CallbackContext callbackContext) {
		try {
			if(vp9Player != null){
				String strState = getStringState();
//				JSONObject message = new JSONObject();
//				message.put("state", strState);
				callbackContext.success(strState);
				return;
			}
			callbackContext.error("Fail: player is destroy");
		} catch (Exception e) {
			callbackContext.error("Fail: " + e.getMessage());
			e.printStackTrace();
		}
		
	}

	private String getStringState() {
		String strState = "";
		
		switch (mCurrentState) {
		case -1:
			strState = "ERROR";
			break;

		case 0:
			strState = "IDLE";
			break;
			
		case 1:
			strState = "IDLE";
			break;
			
		case 2:
			strState = "READY";
			break;
		case 3:
			strState = "PLAYING";
			break;
		case 4:
			strState = "PAUSED";
			break;
		default:
			strState = "IDLE";
			break;
		}
		return strState;
	}

	public void setListenerState(int index) {
		this.listenerStates[index] = true;
	}
	

	public void sendEvent(String action, JSONObject jsonEvent) {
		if(webview != null){
			EventKaraokePlugin bridge = (EventKaraokePlugin) webview.pluginManager.getPlugin("EventKaraokePlugin");
			bridge.reportEvent(action, jsonEvent);
		}

	}
	
	public void sendEvent(String action, Object jsonEvent) {
		if(webview != null){
			EventKaraokePlugin bridge = (EventKaraokePlugin) webview.pluginManager.getPlugin("EventKaraokePlugin");
			bridge.reportEvent(action, jsonEvent);
		}

	}

	@Override
	public void startActivityForResult(CordovaPlugin command, Intent intent, int requestCode) {
        this.activityResultCallback = command;
        // Start activity
        super.startActivityForResult(intent, requestCode);
		
	}

	@Override
	public void setActivityResultCallback(CordovaPlugin plugin) {
		this.activityResultCallback = plugin;
		
	}

	@Override
	public Activity getActivity() {
		return this;
	}

	@Override
	public Object onMessage(String id, Object data) {
		return null;
	}
	
	@Override
	public ExecutorService getThreadPool() {
		return threadPool;
	}
	
	@Override
	protected void onPause() {

		if (vp9Player != null) {
			vp9Player.pause();
		}
		super.onPause();
	}
	
	@Override
	protected void onStop() {
		
		
		
		try {
			Log.d(TAG, "---------------- onStop");
			
			
			PackageManager pm = this.getPackageManager();
		    
		    ActivityManager activityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
		    
		    List<RunningAppProcessInfo> processes = activityManager.getRunningAppProcesses();
		    
		    if(processes != null){
			    for(RunningAppProcessInfo processe : processes){
			    	Log.d(TAG, processe.processName);
			    	
			    	if (processe.processName.contains("vp9.videoproxy")) {
			    		Log.d(TAG, "---------------" + processe.processName);
			    		activityManager.killBackgroundProcesses("vp9.videoproxy");
			    		
			    		Intent i = new Intent("VpService");
						i.setComponent(new ComponentName("vp9.videoproxy",
								"vp9.videoproxy.MyService"));
						boolean stopService = this.stopService(i);
						if (stopService) {
							Log.e(TAG, "----------- stop service proxy ok");
						} else {
							Log.e(TAG, "----------- stop service proxy fail");
						}
						break;
					}
			    }
		    }
		    
		    
		    
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onStop();
		Log.e(TAG, "------------- onstop FINISH 1");
		exitApp(); 
		
		Log.e(TAG, "------------- onstop FINISH 2");
		
	}
	
	private void exitApp() {
		finish();
		
		android.os.Process.killProcess(android.os.Process.myPid());

		System.exit(0);
	}
	
    @Override
    public void onDestroy()
    {
    	if (vp9Player != null) {
			vp9Player.pause();
		}
    	vp9Player.release();
		cancelTask();
		reset();
		Log.d(VpMainActivity.class.getSimpleName(), "ondestroy");
		super.onDestroy();
    }

	public void togglePlayPause(CallbackContext callbackContext) {
		if (vp9Player != null && vp9Player.isPlaying()) {
			boolean isSuccess = vp9Player.pause();
			if(isSuccess){
				callbackContext.success("pause");
			}else{
				callbackContext.error("fail");
			}
		}else if (!vp9Player.isPlaying()) {
			boolean isSuccess = vp9Player.startVideo();
			if(isSuccess){
				callbackContext.success("play");
			}else{
				callbackContext.error("fail");
			}
		}else{
			callbackContext.error("fail");
		}
		
	}
	
	public void setProxySevice(MyService proxySevice) {
		this.proxySevice = proxySevice;
	}
	
	public MyService getProxySevice() {
		return this.proxySevice;
		
	}
	
}
