package com.vp9.player.controller;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tv.vp9.videoproxy.MyService;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.vp9.model.ViewLayoutParams;
import com.vp9.player.demandTivi.DemandTiviSchedule;
import com.vp9.player.logoAndTitle.LoadLeftLogoTask;
import com.vp9.player.logoAndTitle.LoadRightLogoTask;
import com.vp9.player.model.ConfigSubtile;
import com.vp9.player.model.TimeShow;
import com.vp9.player.model.VideoInfo;
import com.vp9.player.model.VideoResolution;
import com.vp9.player.model.VideoResolutionGroup;
import com.vp9.player.model.VideoResult;
import com.vp9.player.serveTime.ServerTimeInfo;
import com.vp9.player.subtitles.LoadViewTask;
import com.vp9.player.subtitles.SubtitleInfo;
import com.vp9.player.subtitles.Utilities;
import com.vp9.player.vp9Interface.Vp9ActivityInterface;
import com.vp9.player.vp9Interface.Vp9PlayerInterface;
import com.vp9.tv.R;
import com.vp9.tv.VpMainActivity;
import com.vp9.util.Vp9Contant;
import com.vp9.util.Vp9KeyEvent;
import com.vp9.util.Vp9MediaCodecInfo;
import com.vp9.util.Vp9ParamUtil;
import com.vp9.view.MagicTextView;
import com.vp9.view.MenuItemAdapter;
import com.vp9.view.Vp9LeftListView;
import com.vp9.view.Vp9RightListView;

public class RotationMediaController extends MediaController {

	public RotationMediaController(Context context) {
		super(context);
	}

	
	private String strDate;

	private int playType;

	private String codecResolution;

	private String codec;

	private String resolution;

	private String token;

	private int intH265;

	private String resolutionLimit;
	
	private String recordUrl;

	private int rotation;
	
	public void init() {
		
		Log.d(TAG, "init: " + video_view_id);
		
		parentLayout = (RelativeLayout) activity.findViewById(parent_layout_id);
		
		mVideoView = (SurfaceView) activity.findViewById(video_view_id);

		pdLoading = (ProgressBar) activity.findViewById(pdLoading_id);
		
		pdLoading2 = (ProgressBar) activity.findViewById(pdLoading_id2);

		loadRate = (TextView) activity.findViewById(load_rate_id);

		loadRate2 = (TextView) activity.findViewById(load_rate_id2);
		
		sbFull = (SeekBar) activity.findViewById(seekBar_id);
		
		sbFull2 = (SeekBar) activity.findViewById(seekBar_id2);

		tvFrom = (TextView) activity.findViewById(tvFrom_id);

		tvTo = (TextView) activity.findViewById(tvTo_id);
		
		tvFrom2 = (TextView) activity.findViewById(tvFrom_id2);

		tvTo2 = (TextView) activity.findViewById(tvTo_id2);

		btnPlay = (Button) activity.findViewById(btnPlay_id);
		
		btnPlay2 = (Button) activity.findViewById(btnPlay_id2);

		btnSub = (Button) activity.findViewById(btnSub_id);
		
		btnSub2 = (Button) activity.findViewById(btnSub_id2);

		btnBack = (Button) activity.findViewById(btnBack_id);
		
		btnBack2 = (Button) activity.findViewById(btnBack_id2);

		btnSetting = (Button) activity.findViewById(btnSetting_id);
		
		btnSetting2 = (Button) activity.findViewById(btnSetting_id2);

		vp9ChannelImage = (ImageView) activity.findViewById(vp9ChannelImage_id);
		
		vp9ChannelImage2 = (ImageView) activity.findViewById(vp9ChannelImage_id2);

		progessLayout = (RelativeLayout) getActivity().findViewById(progess_id);
		
		progessLayout2 = (RelativeLayout) getActivity().findViewById(progess_id2);

		loadingLayout = (RelativeLayout) activity.findViewById(loading_layout_id);
		
		loadingLayout2 = (RelativeLayout) activity.findViewById(loading_layout_id2);

		controllerLayout = (RelativeLayout) activity.findViewById(controller_id);
		
		controllerLayout2 = (RelativeLayout) activity.findViewById(controller_id2);
		
		controllerTopLayout = (RelativeLayout) activity.findViewById(controller_top_id);
		
		controllerTopLayout2 = (RelativeLayout) activity.findViewById(controller_top_id2);

		vp9PlayerLayout = (RelativeLayout) activity.findViewById(vp9_player_layout_id);
		
		subtitlesLayout = (RelativeLayout) activity.findViewById(subtitles_layout_id);
		
		subtitlesLayout2 = (RelativeLayout) activity.findViewById(subtitles_layout_id2);

		videoTitleLayout = (RelativeLayout) activity.findViewById(video_title_layout_id);
		
		videoTitleLayout2 = (RelativeLayout) activity.findViewById(video_title_layout_id2);

		logoLayout = (RelativeLayout) activity.findViewById(logo_layout_id);
		
		logoLayout2 = (RelativeLayout) activity.findViewById(logo_layout_id2);

		logoVideo = (ImageView) activity.findViewById(logo_video_id);
		
		logoVideo2 = (ImageView) activity.findViewById(logo_video_id2);

		videoTitle = (MagicTextView) activity.findViewById(video_title_id);
		
		videoTitle2 = (MagicTextView) activity.findViewById(video_title_id2);

		logoChannel = (ImageView) activity.findViewById(logo_id);
		
		logoChannel2 = (ImageView) activity.findViewById(logo_id2);

		logoText = (MagicTextView) activity.findViewById(logo_text_id);
		
		logoText2 = (MagicTextView) activity.findViewById(logo_text_id2);

		btnChoose = (Button) activity.findViewById(btnChoose_id);
		
		btnChoose2 = (Button) activity.findViewById(btnChoose_id2);

		btnPrev = (Button) activity.findViewById(btnPrev_id);
		
		btnPrev2 = (Button) activity.findViewById(btnPrev_id2);

		btnNext = (Button) activity.findViewById(btnNext_id);
		
		btnNext2 = (Button) activity.findViewById(btnNext_id2);

		notifyTextView = (TextView) activity.findViewById(notify_id);
		
		notifyTextView2 = (TextView) activity.findViewById(notify_id2);	

		// setTextForTextView();
		setTextForTextView(tvTo, Utilities.milliSecondsToTimer(0), 0);
		setTextForTextView(tvFrom, Utilities.milliSecondsToTimer(0), 0);
//		isLoading = true;
		setVisibility(loadingLayout, View.VISIBLE);
		setVisibility(controllerTopLayout, View.VISIBLE);
		setVisibility(progessLayout, View.GONE);
		setVisibility(logoLayout, View.GONE);
		setVisibility(videoTitleLayout, View.GONE);
		setVisibility(subtitlesLayout, View.GONE);
		setVisibility(logoVideo, View.GONE);
		setVisibility(videoTitle, View.GONE);
		if(is3D){
			setTextForTextView(tvTo2, Utilities.milliSecondsToTimer(0), 0);
			setTextForTextView(tvFrom2, Utilities.milliSecondsToTimer(0), 0);
//			isLoading = true;
			setVisibility(loadingLayout2, View.VISIBLE);
			setVisibility(controllerTopLayout2, View.VISIBLE);
			setVisibility(progessLayout2, View.GONE);
			setVisibility(logoLayout2, View.GONE);
 			setVisibility(videoTitleLayout2, View.GONE);
			setVisibility(subtitlesLayout2, View.GONE);
			setVisibility(logoVideo2, View.GONE);
			setVisibility(videoTitle2, View.GONE);
		}

		if (sbFull != null) {
			setProgressForSeekBar(sbFull, 0);
			setMaxForSeekBar(sbFull, 1000);
			setSecondaryProgress(sbFull, 0);
		}
		
		if (sbFull2 != null) {
			setProgressForSeekBar(sbFull2, 0);
			setMaxForSeekBar(sbFull2, 1000);
			setSecondaryProgress(sbFull2, 0);
		}

		initParam();
		
		mVideoView.requestFocus();
		mHolder = mVideoView.getHolder();
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		clearParam = false;

		// mainLayout.setOnKeyListener(new MainKeyListener());
		
//		rotateGUI();

	}



	public boolean handleMainKeyDown(View view, int keyCode, KeyEvent event) {
		return handleKeyEvent(view, keyCode, event);
	}

	// public class MainKeyListener implements OnKeyListener{
	//
	// @Override  
	// public boolean onKey(View v, int keyCode, KeyEvent event) {
	// int action = event.getAction();
	// Log.d(TAG, "keyCode: " + keyCode);
	// if(action == KeyEvent.ACTION_DOWN){
	// switch (keyCode) {
	// case KeyEvent.KEYCODE_DPAD_LEFT:
	// playPreVideo();
	// break;
	//
	// case KeyEvent.KEYCODE_DPAD_RIGHT:
	// playNextVideo();
	// break;
	//
	// default:
	// break;
	// }
	//
	// }
	// return false;
	// }
	//
	// }

	public boolean startNaviteVideo(final JSONObject jsonVideoInfo) {

		reset();
		if (clearParam) {
			return false;
		}
		clearParam = false;
		boolean isSuccess = false;
//		this.resolution = null;
//		this.codecResolution = null;
		codec = com.vp9.util.AppPreferences.INSTANCE.getCodec();
		codecResolution = com.vp9.util.AppPreferences.INSTANCE.getCodecResolution();
//		intProxySpeedDisplay = com.vp9.util.AppPreferences.INSTANCE.getIntProxySpeedDisplay();
		setMessage("");
		if (jsonVideoInfo != null) {
			setVisibility(vp9PlayerLayout, View.VISIBLE);
			setVisibility(mVideoView, View.VISIBLE);
			setVisibility(vp9ChannelImage, View.GONE);
//			if(!isLoading){
//				isLoading = true;
//				setVisibility(loadingLayout, View.VISIBLE);
//			}
			setVisibility(loadingLayout, View.VISIBLE);
			setVisibility(subtitlesLayout, View.VISIBLE);
			if(is3D){
				setVisibility(loadingLayout2, View.VISIBLE);
				setVisibility(subtitlesLayout2, View.VISIBLE);
				setVisibility(vp9ChannelImage2, View.GONE);
			}

			showController(0);
			updateColorForSubtitle(jsonVideoInfo);
			this.curVideoResult = null;
			// analysic video Info
			try {
				this.videoType = Vp9ParamUtil.getJsonInt(jsonVideoInfo, "videoType", -1);
				this.intProxy = Vp9ParamUtil.getJsonInt(jsonVideoInfo, "proxy", 0);
				this.intFullScreen = Vp9ParamUtil.getJsonInt(jsonVideoInfo, "fullscreen", 0);
				this.isRightDisplay = Vp9ParamUtil.getJSONBoolean(jsonVideoInfo, "isRightDisplay", true);
				this.channelName = Vp9ParamUtil.getJsonString(jsonVideoInfo, "channelName", "");
				this.channelIcon = Vp9ParamUtil.getJsonString(jsonVideoInfo, "channelIcon", "");
				this.vp9Logo = Vp9ParamUtil.getJsonString(jsonVideoInfo, "vp9Logo", "");
				this.topStretch = Vp9ParamUtil.getJsonInt(jsonVideoInfo, "topStretch", 0);
				this.bottomStretch = Vp9ParamUtil.getJsonInt(jsonVideoInfo, "bottomStretch", 0);
				this.leftStretch = Vp9ParamUtil.getJsonInt(jsonVideoInfo, "leftStretch", 0);
				this.rightStretch = Vp9ParamUtil.getJsonInt(jsonVideoInfo, "rightStretch", 0);
				this.token= Vp9ParamUtil.getJsonString(jsonVideoInfo, "token", "");
				this.intH265 = Vp9ParamUtil.getJsonInt(jsonVideoInfo, "h265", 0);
				this.resolutionLimit = Vp9ParamUtil.getJsonString(jsonVideoInfo, "resolution_limit", "");
				this.is3D = Vp9ParamUtil.getJSONBoolean(jsonVideoInfo, "is3D", false);
				this.recordUrl = Vp9ParamUtil.getJsonString(jsonVideoInfo, "recordUrl", "");
				this.serverTimeUrl = Vp9ParamUtil.getJsonString(jsonVideoInfo, "serverTimeUrl", "");
				this.rotation = Vp9ParamUtil.getJsonInt(jsonVideoInfo, "rotate", 0);
				this.serverTimeInfo = new ServerTimeInfo(serverTimeUrl);
				receiveProxySpeed(intProxy);
				updateScreenSize(jsonVideoInfo);
				RelativeLayout uiView2 = (RelativeLayout) activity.findViewById(R.id.ui_view2);
				if(videoType == 1){
					intProxySpeedDisplay = com.vp9.util.AppPreferences.INSTANCE.getIntProxySpeedDisplay();
				}else{
					intProxySpeedDisplay = 3;
				}
				
				if(is3D){
					setVisibility(uiView2, View.VISIBLE);
				}else{
					setVisibility(uiView2, View.GONE);
				}
				setScaleX(is3D);
				switch (videoType) {

				case 0:
					setEnabled(btnNext, true);
					setEnabled(btnPrev, true);
					setEnabled(btnSetting, false);
					setBackgroundResource(btnSetting, vp9_btnSetting_hide_id);
					setBackgroundResource(btnNext, vp9_btn_next_id);
					setBackgroundResource(btnPrev, vp9_btn_prev_id);
					if(is3D){
						setEnabled(btnNext2, true);
						setEnabled(btnPrev2, true);
						setEnabled(btnSetting2, false);
						setBackgroundResource(btnSetting2, vp9_btnSetting_hide_id);
						setBackgroundResource(btnNext2, vp9_btn_next_id);
						setBackgroundResource(btnPrev2, vp9_btn_prev_id);
					}
					handlerPlayVideo(jsonVideoInfo);
					break;

				case 1:
					setEnabled(btnNext, false);
					setEnabled(btnPrev, false);
					setEnabled(btnSetting, true);
					setBackgroundResource(btnSetting, vp9_btnSetting_id);
					setBackgroundResource(btnNext, vp9_btn_next_hide_id);
					setBackgroundResource(btnPrev, vp9_btn_prev_hide_id);
					if(is3D){
						setEnabled(btnNext2, false);
						setEnabled(btnPrev2, false);
						setEnabled(btnSetting2, true);
						setBackgroundResource(btnSetting2, vp9_btnSetting_id);
						setBackgroundResource(btnNext2, vp9_btn_next_hide_id);
						setBackgroundResource(btnPrev2, vp9_btn_prev_hide_id);	
					}
					handlerPlayLiveTivi(jsonVideoInfo);
					break;

				case 2:
					setEnabled(btnNext, true);
					setEnabled(btnPrev, true);
					setEnabled(btnSetting, true);
					setBackgroundResource(btnSetting, vp9_btnSetting_id);
					setBackgroundResource(btnNext, vp9_btn_next_id);
					setBackgroundResource(btnPrev, vp9_btn_prev_id);
					if(is3D){
						setEnabled(btnNext2, true);
						setEnabled(btnPrev2, true);
						setEnabled(btnSetting2, true);
						setBackgroundResource(btnSetting2, vp9_btnSetting_id);
						setBackgroundResource(btnNext2, vp9_btn_next_id);
						setBackgroundResource(btnPrev2, vp9_btn_prev_id);
					}
					handlerPlayDemandTivi1(jsonVideoInfo);
					break;

				case 3:
					setEnabled(btnNext, true);
					setEnabled(btnPrev, true);
					setEnabled(btnSetting, true);
					setBackgroundResource(btnSetting, vp9_btnSetting_id);
					setBackgroundResource(btnNext, vp9_btn_next_id);
					setBackgroundResource(btnPrev, vp9_btn_prev_id);
					if(is3D){
						setEnabled(btnNext2, true);
						setEnabled(btnPrev2, true);
						setEnabled(btnSetting2, true);
						setBackgroundResource(btnSetting2, vp9_btnSetting_id);
						setBackgroundResource(btnNext2, vp9_btn_next_id);
						setBackgroundResource(btnPrev2, vp9_btn_prev_id);
					}
					handlerPlayDemandTivi2(jsonVideoInfo);
					break;

				case 4:
					setEnabled(btnNext, true);
					setEnabled(btnPrev, true);
					setEnabled(btnSetting, true);
					setBackgroundResource(btnSetting, vp9_btnSetting_id);
					setBackgroundResource(btnNext, vp9_btn_next_id);
					setBackgroundResource(btnPrev, vp9_btn_prev_id);
					if(is3D){
						setEnabled(btnNext2, true);
						setEnabled(btnPrev2, true);
						setEnabled(btnSetting2, true);
						setBackgroundResource(btnSetting2, vp9_btnSetting_id);
						setBackgroundResource(btnNext2, vp9_btn_next_id);
						setBackgroundResource(btnPrev2, vp9_btn_prev_id);
					}
					handlerPlayOffTvProgram(jsonVideoInfo);
					break;
					
					
				case 5:
					setEnabled(btnNext, true);
					setEnabled(btnPrev, true);
					setEnabled(btnSetting, true);
					setBackgroundResource(btnSetting, vp9_btnSetting_id);
					setBackgroundResource(btnNext, vp9_btn_next_id);
					setBackgroundResource(btnPrev, vp9_btn_prev_id);
					if(is3D){
						setEnabled(btnNext2, true);
						setEnabled(btnPrev2, true);
						setEnabled(btnSetting2, true);
						setBackgroundResource(btnSetting2, vp9_btnSetting_id);
						setBackgroundResource(btnNext2, vp9_btn_next_id);
						setBackgroundResource(btnPrev2, vp9_btn_prev_id);
					}
					handlerPlayDemandTivi3(jsonVideoInfo);
					break;

				default:
					break;
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			isSuccess = true;
		}
		return isSuccess;
		// return true;
	}

	protected void receiveProxySpeed(int intProxy) {
		if(intProxy == 1){
			proxySpeedHandler = new Handler();
			proxySpeedHandler.postDelayed(mProxySpeedTask, 1000L);
		}
	}

	private void setScaleX(boolean is3d) {
		float scale;
		if(is3d){
			scale = 0.5f;
		}else{
			scale = 1f;
		}
//		setScaleX(progessLayout, scale);
//		setScaleX(controllerLayout, scale);
		setScaleX(tvFrom, scale, 1);
		setScaleX(tvTo, scale, 1);
//		setScaleX(sbFull, scale);
		setWidth(btnPlay, scale, 1);
		setWidth(btnSub, scale, 1);
		setWidth(btnBack, scale, 1);
		setWidth(btnSetting, scale, 1);
		setWidth(btnChoose, scale, 1);
		setWidth(btnPrev, scale, 1);
		setWidth(btnNext, scale, 1);
//		setWidth(notifyTextView, scale);
//		setScaleX(sbFull, scale);
//		setScaleX(tvFrom, scale);
//		setScaleX(tvTo, scale);
//		setScaleX(btnPlay, scale);
//		setScaleX(btnSub, scale);
//		setScaleX(btnBack, scale);
//		setScaleX(btnSetting, scale);
//		setScaleX(btnChoose, scale);
//		setScaleX(btnPrev, scale);
//		setScaleX(btnNext, scale);
//		setScaleX(notifyTextView, scale);
		setScaleX(vp9ChannelImage, scale, 1);
		setScaleX(loadingLayout, scale, 1);
//		setScaleX(subtitlesLayout, scale);
		setScaleX(logoVideo, scale, 1);
		setScaleX(videoTitle, scale, 1);
		setScaleX(logoChannel, scale, 1);
		setScaleX(logoText, scale, 1);
		
		// view 2
		
//		setScaleX(progessLayout2, scale);
//		setScaleX(controllerLayout2, scale);
		setScaleX(tvFrom2, scale, 1);
		setScaleX(tvTo2, scale, 1);
//		setScaleX(sbFull, scale);
		setWidth(btnPlay2, scale, 1);
		setWidth(btnSub2, scale, 1);
		setWidth(btnBack2, scale, 1);
		setWidth(btnSetting2, scale, 1);
		setWidth(btnChoose2, scale, 1);
		setWidth(btnPrev2, scale, 1);
		setWidth(btnNext2, scale, 1);
//		setWidth(notifyTextView2, scale);
		
//		setScaleX(tvFrom2, scale);
//		setScaleX(tvTo2, scale);
//		setScaleX(btnPlay2, scale);
//		setScaleX(btnSub2, scale);
//		setScaleX(btnBack2, scale);
//		setScaleX(btnSetting2, scale);
//		setScaleX(btnChoose2, scale);
//		setScaleX(btnPrev2, scale);
//		setScaleX(btnNext2, scale);
//		setScaleX(notifyTextView2, scale);
		setScaleX(vp9ChannelImage2, scale, 1);
		setScaleX(loadingLayout2, scale, 1);
//		setScaleX(subtitlesLayout2, scale);
		setScaleX(logoVideo2, scale, 1);
		setScaleX(videoTitle2, scale, 1);
		setScaleX(logoChannel2, scale, 1);
		setScaleX(logoText2, scale, 1);
	}

	public HashMap<View, ViewLayoutParams> viewLayoutParamMap = new HashMap<View, ViewLayoutParams>();
	
	private void setWidth(final View view, final float scale, final int layoutType) {
		if (activity != null && view != null) {
			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if(layoutType == 1){
						RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
						ViewLayoutParams viewLayoutParams = new ViewLayoutParams();
						viewLayoutParams.setWidth(layoutParams.width);
						viewLayoutParams.setLeftMargin(layoutParams.leftMargin);
						viewLayoutParams.setRightMargin(layoutParams.rightMargin);
						viewLayoutParams.setScale(scale);
						viewLayoutParams.setScale(false);
						viewLayoutParams.setLayoutType(layoutType);
						viewLayoutParamMap.put(view, viewLayoutParams);
						layoutParams.width = (int) (scale*layoutParams.width);
						layoutParams.leftMargin = (int) (scale*layoutParams.leftMargin);
						layoutParams.rightMargin = (int) (scale*layoutParams.rightMargin);
						
						view.setLayoutParams(layoutParams);
					}else{
						LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
						ViewLayoutParams viewLayoutParams = new ViewLayoutParams();
						viewLayoutParams.setWidth(layoutParams.width);
						viewLayoutParams.setLeftMargin(layoutParams.leftMargin);
						viewLayoutParams.setRightMargin(layoutParams.rightMargin);
						viewLayoutParams.setScale(scale);
						viewLayoutParams.setScale(false);
						viewLayoutParams.setLayoutType(layoutType);
						viewLayoutParamMap.put(view, viewLayoutParams);
						layoutParams.width = (int) (scale*layoutParams.width);
						layoutParams.leftMargin = (int) (scale*layoutParams.leftMargin);
						layoutParams.rightMargin = (int) (scale*layoutParams.rightMargin);
						
						view.setLayoutParams(layoutParams);
					}
	
				}
			});
		}
		
	}

	public void setScaleX(final View view, final float scale, final int layoutType) {
		if (activity != null && view != null) {
			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					view.setScaleX(scale);
					if(layoutType == 1){
						RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
						ViewLayoutParams viewLayoutParams = new ViewLayoutParams();
						viewLayoutParams.setWidth(layoutParams.width);
						viewLayoutParams.setLeftMargin(layoutParams.leftMargin);
						viewLayoutParams.setRightMargin(layoutParams.rightMargin);
						viewLayoutParams.setScale(scale);
						viewLayoutParams.setScale(true);
						viewLayoutParams.setLayoutType(layoutType);
						viewLayoutParamMap.put(view, viewLayoutParams);
						layoutParams.width = (int) ((1/scale)*layoutParams.width);
						layoutParams.leftMargin = (int) (scale*layoutParams.leftMargin);
						layoutParams.rightMargin = (int) (scale*layoutParams.rightMargin);
						view.setLayoutParams(layoutParams);
					}else{
						LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
						ViewLayoutParams viewLayoutParams = new ViewLayoutParams();
						viewLayoutParams.setWidth(layoutParams.width);
						viewLayoutParams.setLeftMargin(layoutParams.leftMargin);
						viewLayoutParams.setRightMargin(layoutParams.rightMargin);
						viewLayoutParams.setScale(scale);
						viewLayoutParams.setScale(true);
						viewLayoutParams.setLayoutType(layoutType);
						viewLayoutParamMap.put(view, viewLayoutParams);
						layoutParams.width = (int) ((1/scale)*layoutParams.width);
						layoutParams.leftMargin = (int) (scale*layoutParams.leftMargin);
						layoutParams.rightMargin = (int) (scale*layoutParams.rightMargin);
						view.setLayoutParams(layoutParams);
					}

				}
			});
		}
		
	}
	
	
	private void unScaleX(boolean is3d) {
		if(viewLayoutParamMap != null && !viewLayoutParamMap.isEmpty()){
			Set<View> kewSet = viewLayoutParamMap.keySet();
			for (Iterator<View> iterator = kewSet.iterator(); iterator.hasNext();) {
				final View view = (View) iterator.next();
				final ViewLayoutParams layoutParam = viewLayoutParamMap.get(view);
				if (activity != null && view != null && layoutParam != null) {
					activity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							int layoutType = layoutParam.getLayoutType();
							if(layoutType == 1){
								RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();

								layoutParams.width = layoutParam.getWidth();
								layoutParams.leftMargin = layoutParam.getLeftMargin();
								layoutParams.rightMargin = layoutParam.getRightMargin();
								view.setLayoutParams(layoutParams);
								if(layoutParam.isScale()){
									view.setScaleX(1);
								}
								view.requestLayout();	
							}else if(layoutType == 0){
								LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();

								layoutParams.width = layoutParam.getWidth();
								layoutParams.leftMargin = layoutParam.getLeftMargin();
								layoutParams.rightMargin = layoutParam.getRightMargin();
								view.setLayoutParams(layoutParams);
								if(layoutParam.isScale()){
									view.setScaleX(1);
								}
								view.requestLayout();
							}
		
						}
					});
				}
			}
			
			viewLayoutParamMap.clear();
		}
		
	}
	

	public void updateScreenSize(JSONObject jsonVideoInfo) {
		String screenSize = Vp9ParamUtil.getJsonString(jsonVideoInfo, "screenSize", "");
		String[] elemSizes = screenSize.trim().split("x");

		int width = 0, height = 0;

		if (elemSizes != null && elemSizes.length == 2) {
			width = Vp9ParamUtil.getValue(elemSizes[0], 0);
			height = Vp9ParamUtil.getValue(elemSizes[1], 0);
		}

		this.mScreenWidth = width;

		this.mScreenHeight = height;
		
		
	}

	public void handlerPlayOffTvProgram(JSONObject jsonVideoInfo) throws JSONException {
		if (clearParam) {
			return;
		}
		isLive = 0;
		videoType = 4;
		if (!isDisplayChannelImage && intShowControl != 2) {
//			if(!isLoading){
//				isLoading = true;
//				setVisibility(loadingLayout, View.VISIBLE);
//			}
			setVisibility(loadingLayout, View.VISIBLE);
			if(is3D){
				setVisibility(loadingLayout2, View.VISIBLE);
			}
		} else {
			Log.d(TAG, "loading 4: " + false);
//			if(isLoading){
//				isLoading = false;
//				setVisibility(loadingLayout, View.GONE);
//			}
			setVisibility(loadingLayout, View.GONE);
			if(is3D){
				setVisibility(loadingLayout2, View.GONE);
			}
		}
//		setEnabled(btnSetting, false);
		// setVisibility(progessLayout, View.VISIBLE);
		String videoUrl = jsonVideoInfo.getString("videoUrl");
		this.playType = Vp9ParamUtil.getJsonInt(jsonVideoInfo, "playType", 0);
		this.videoResolutions = getVideoResolutionGroup(jsonVideoInfo);
		
		Log.d(TAG, "handlerPlayVideo: " + videoUrl);
		
		playVideo(videoResolutions, videoUrl, false);

		// showController(0);
		showProgessLayout();
		subInfoArr = null;

		updateOffTvProgramRelated(jsonVideoInfo);
	}

	public void updateOffTvProgramRelated(JSONObject jsonVideoInfo) {
		if (clearParam) {
			return;
		}
		if (jsonVideoInfo == null || !jsonVideoInfo.has("videoRelated")) {
			setBackgroundResource(btnNext, vp9_btn_next_hide_id);
			setBackgroundResource(btnPrev, vp9_btn_prev_hide_id);
			if(is3D){
				setBackgroundResource(btnNext2, vp9_btn_next_hide_id);
				setBackgroundResource(btnPrev2, vp9_btn_prev_hide_id);
			}
			return;
		}
		try {
			
			String videoUrl = Vp9ParamUtil.getJsonString(jsonVideoInfo, "videoUrl", "");
			
			JSONArray jsonvideoRelateds = jsonVideoInfo.getJSONArray("videoRelated");

			if (jsonvideoRelateds != null) {

				demandTiviSchedule = new DemandTiviSchedule();

				VideoInfo curVideoInfo = demandTiviSchedule.updateInfoForOffTvPrograms(jsonvideoRelateds, videoUrl);
				this.playingVideo = curVideoInfo;
				Thread videoMenuThread = new Thread() {
					public void run() {
//						Looper.prepare();
						createVideoMenu();
//						Looper.loop();
					}
				};
				videoMenuThread.setName("videoMenuThread_37");
				videoMenuThread.start();

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

//	public ArrayList<VideoResolution> getVideoResolutions(JSONObject jsonVideoInfo){
//		try {
//			JSONArray jsaElems = Vp9ParamUtil.getJSONArray(jsonVideoInfo, "src_list");
//			
//			ArrayList<VideoResolution> videoResolutions = new ArrayList<VideoResolution>();
//					
//			if(jsaElems != null){
//				
//				for(int i = 0; i < jsaElems.length(); i++){
//					
//					JSONObject jsaElem = jsaElems.getJSONObject(i);
//					
//					if(jsaElem != null){
//						
//						String video_url = Vp9ParamUtil.getJsonString(jsaElem, "src", "");
//						
//						String type = Vp9ParamUtil.getJsonString(jsaElem, "type", "");
//						
//						String codec = Vp9ParamUtil.getJsonString(jsaElem, "codec", "");
//						
//						int bitrate = Vp9ParamUtil.getJsonInt(jsaElem, "bitrate", 0);
//						
//						String quality = Vp9ParamUtil.getJsonString(jsaElem, "quality", "");
//						
//						String resolution = Vp9ParamUtil.getJsonString(jsaElem, "resolution", "");
//						
//						int rotate = Vp9ParamUtil.getJsonInt(jsaElem, "rotate", 0);
//						
//						VideoResolution videoResolution = new VideoResolution();
//						
//						videoResolution.setVideoUrl(video_url);
//						
//						videoResolution.setType(type);
//						
//						videoResolution.setCodec(codec);
//						
//						videoResolution.setBitrate(bitrate);
//						
//						videoResolution.setQuality(quality);
//						
//						videoResolution.setResolution(resolution);
//						
//						videoResolution.setRotation(rotate);
//						
//						videoResolutions.add(videoResolution);
//						
//					}
//				}
//			}
//			return videoResolutions;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
	
	public ArrayList<VideoResolutionGroup> getVideoResolutionGroup(JSONObject jsonVideoInfo) {
		try {
			JSONArray jsaElems = Vp9ParamUtil.getJSONArray(jsonVideoInfo, "src_list");
			
			HashMap<String, VideoResolutionGroup> videoResolGroupMap = new HashMap<String, VideoResolutionGroup>();
					
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
						
						int priority = Vp9ParamUtil.getJsonInt(jsaElem, "prio", 0);
						
						String key = codec + "-" + resolution;
						
						VideoResolutionGroup videoResolGroup;
						if(videoResolGroupMap.containsKey(key)){
							videoResolGroup = videoResolGroupMap.get(key);
						}else{
							videoResolGroup = new VideoResolutionGroup();
							videoResolGroup.setCodec(codec);
							videoResolGroup.setResolution(resolution);
							videoResolGroupMap.put(key, videoResolGroup);
						}
						
						VideoResolution videoResolution = new VideoResolution();
						
						videoResolution.setVideoUrl(video_url);
						
						videoResolution.setType(type);
						
						videoResolution.setCodec(codec);
						
						videoResolution.setBitrate(bitrate);
						
						videoResolution.setQuality(quality);
						
						videoResolution.setResolution(resolution);
						
						videoResolution.setRotation(rotate);
						
						videoResolution.setPriority(priority);
						
						videoResolGroup.add(videoResolution);
						
					}
				}
			}
			ArrayList<VideoResolutionGroup> videoResolutions = new ArrayList<VideoResolutionGroup>();
			
			Collection<VideoResolutionGroup> values = videoResolGroupMap.values();
			
			if(values != null){
				for (Iterator<VideoResolutionGroup> iterator = values.iterator(); iterator.hasNext();) {
					VideoResolutionGroup videoResolutionGroup = (VideoResolutionGroup) iterator
							.next();
					videoResolutions.add(videoResolutionGroup);
				}
			}
			
			return videoResolutions;
		} catch (Exception e) {
			e.printStackTrace();
	    }
	    return null;
	}
	
	public void updateColorForSubtitle(JSONObject jsonVideoInfo) {
		if (clearParam) {
			return;
		}
		if (jsonVideoInfo != null && jsonVideoInfo.has("subtitleColor")) {
			ArrayList<ConfigSubtile> configSubtiles = new ArrayList<ConfigSubtile>();
			try {
				JSONArray subtitleColors = jsonVideoInfo.getJSONArray("subtitleColor");
				if (subtitleColors != null) {
					for (int i = 0; i < subtitleColors.length(); i++) {
						JSONObject jsonSubtitleColor = subtitleColors.getJSONObject(i);
						if (jsonSubtitleColor != null) {
							ConfigSubtile configSubtile = new ConfigSubtile();
							String language = Vp9ParamUtil.getJsonString(jsonSubtitleColor, "language", "");
							String color = Vp9ParamUtil.getJsonString(jsonSubtitleColor, "color", ""); 
							int width = Vp9ParamUtil.getJsonInt(jsonSubtitleColor, "width", -1);
							int height = Vp9ParamUtil.getJsonInt(jsonSubtitleColor, "height", -1);
							int textSize = Vp9ParamUtil.getJsonInt(jsonSubtitleColor, "textSize", -1);
							configSubtile.setLanguage(language);
							configSubtile.setColor(color);
							configSubtile.setWidth(width);
							configSubtile.setHeight(height);
							configSubtile.setTextSize(textSize);
							configSubtiles.add(configSubtile);
						}
					}
				}

				this.configSubtiles = configSubtiles;

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean setColorForSubtitle(final TextView textView, String subTpye) {
		if (clearParam) {
			return false;
		}
		if (configSubtiles != null) {
			for (int i = 0; i < configSubtiles.size(); i++) {
				final ConfigSubtile configSubtile = configSubtiles.get(i);
				if (configSubtile != null && configSubtile.getLanguage().equals(subTpye)) {
					if (activity != null && textView != null) {
						activity.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								Log.d(TAG, "Set Color: " + configSubtile.getColor());
								textView.setTextColor(Color.parseColor(configSubtile.getColor()));

								if (configSubtile.getHeight() != -1) {
									textView.setHeight(configSubtile.getHeight());
								}

								if (configSubtile.getWidth() != -1) {
									textView.setWidth(configSubtile.getWidth());
								}

								if (configSubtile.getTextSize() != -1) {
									textView.setTextSize(configSubtile.getTextSize());
								}
							}
						});
					}
					break;
				}
			}
		}
		return false;
	}

	public void playNextVideo() {
		if (clearParam) {
			return;
		}

		if (demandTiviSchedule != null) {
			cancelUpdateTimehandle();
			String msg = Vp9Contant.MSG_PLAY_NEXT_VIDEO;
			errorMsg = Vp9Contant.MSG_PLAY_NEXT_VIDEO;
			setTextForTextView(notifyTextView, msg, 0);
			if(is3D){
				setTextForTextView(notifyTextView2, msg, 0);
			}
			isRefreshNotify = true;
			if (videoType == 1) {
				if (popupVideoWindow.isShowing()) {
//					menuItemAdapter.serverTimeInfo = null;
					popupVideoWindow.dismiss();

				}
				if (popupVideoWindow2.isShowing()) {
//					menuItemAdapter.serverTimeInfo = null;
					popupVideoWindow2.dismiss();

				}
				updateTimehandle.postDelayed(mUpdateTimeTask, 500L);
				return;
			}

			else if (videoType == 0) {
				int currentIndex = demandTiviSchedule.getCurrentIndex();
				VideoResult videoResult = demandTiviSchedule.getVideoInfoByIndex(currentIndex + 1);
				if (videoResult != null && videoResult.getVideoInfo() != null) {
					isLive = 0;
//					setVisibility(loadingLayout, View.VISIBLE);
					if (!isDisplayChannelImage && intShowControl != 2) {
//						if(!isLoading){
//							isLoading = true;
//							setVisibility(loadingLayout, View.VISIBLE);
//						}
						setVisibility(loadingLayout, View.VISIBLE);
						if(is3D){
							setVisibility(loadingLayout2, View.VISIBLE);
						}
					} else {
						Log.d(TAG, "loading 17: " + false);
//						if(isLoading){
//							isLoading = false;
//							setVisibility(loadingLayout, View.GONE);
//						}
						setVisibility(loadingLayout, View.GONE);
						if(is3D){
							setVisibility(loadingLayout2, View.GONE);
						}
					}
					if (vp9Player != null) {
						vp9Player.release();
					}
					mVideoView.destroyDrawingCache();
					mVideoView.refreshDrawableState();
					sendMsgToGetVideoInfo(videoResult);
				}else{
					updateTimehandle.postDelayed(mUpdateTimeTask, 500L);
				}
//				menuItemAdapter.serverTimeInfo = null;
				popupVideoWindow.dismiss();
				if(is3D){
					popupVideoWindow2.dismiss();
				}
				return;
			} else if (videoType == 4) {
				int currentIndex = demandTiviSchedule.getCurrentIndex();
				VideoResult videoResult = demandTiviSchedule.getVideoInfoByIndex(currentIndex + 1);
				if (videoResult != null && videoResult.getVideoInfo() != null) {
					isLive = 0;
//					setVisibility(loadingLayout, View.VISIBLE);
					if (!isDisplayChannelImage && intShowControl != 2) {
//						if(!isLoading){
//							isLoading = true;
//							setVisibility(loadingLayout, View.VISIBLE);
//						}
						setVisibility(loadingLayout, View.VISIBLE);
						if(is3D){
							setVisibility(loadingLayout2, View.VISIBLE);
						}
					} else {
						Log.d(TAG, "loading 18: " + false);
//						if(isLoading){
//							isLoading = false;
//							setVisibility(loadingLayout, View.GONE);
//						}
						setVisibility(loadingLayout, View.GONE);
						if(is3D){
							setVisibility(loadingLayout2, View.GONE);
						}
					}
					if (vp9Player != null) {
						vp9Player.release();
					}
					mVideoView.destroyDrawingCache();
					mVideoView.refreshDrawableState();
					isLive = 0;
					cancelTask();
					reset();
					this.videoIndex = videoResult.getVideoInfo().getIndex();
					this.playingVideo = videoResult.getVideoInfo();
					vp9Player.playVideo(videoResult.getVideoInfo().getUrl(), false);
				}else{
					updateTimehandle.postDelayed(mUpdateTimeTask, 500L);
				}
//				menuItemAdapter.serverTimeInfo = null;
				popupVideoWindow.dismiss();
				if(is3D){
					popupVideoWindow2.dismiss();
				}
				return;
			}
//			setVisibility(loadingLayout, View.VISIBLE);
			if (!isDisplayChannelImage && intShowControl != 2) {
//				if(!isLoading){
//					isLoading = true;
//					setVisibility(loadingLayout, View.VISIBLE);
//				}
				setVisibility(loadingLayout, View.VISIBLE);
				if(is3D){
					setVisibility(loadingLayout2, View.VISIBLE);
				}
			} else {
				Log.d(TAG, "loading 19: " + false);
//				if(isLoading){
//					isLoading = false;
//					setVisibility(loadingLayout, View.GONE);
//				}
				setVisibility(loadingLayout, View.GONE);
				if(is3D){
					setVisibility(loadingLayout2, View.GONE);
				}
			}
//			if (vp9Player != null) {
//				vp9Player.release();
//			}
//			mVideoView.destroyDrawingCache();
//			mVideoView.refreshDrawableState();
//			isLive = 0;
//			cancelTask();
//			reset();
			if (activity != null) {
				Vp9ActivityInterface vp9Activity = (Vp9ActivityInterface) activity;
				vp9Activity.closeEPG();
			}
			int currentIndex = demandTiviSchedule.getCurrentIndex();
			if (currentIndex + 1 < demandTiviSchedule.getSizeVideoInfos()) {
				VideoResult videoResult = demandTiviSchedule.getVideoInfoByIndex(currentIndex + 1);
				if(videoResult.getVideoInfo() != null){
					if (vp9Player != null) {
						vp9Player.release();
					}
					mVideoView.destroyDrawingCache();
					mVideoView.refreshDrawableState();
					isLive = 0;
					cancelTask();
					reset();
					playNewVideo(videoResult);
				}else{
					updateTimehandle.postDelayed(mUpdateTimeTask, 500L);
				}

			}else{
				updateTimehandle.postDelayed(mUpdateTimeTask, 500L);
			}
		}
	}

	public void playPreVideo() {
		if (clearParam) {
			return;
		}

		int currentIndex = demandTiviSchedule.getCurrentIndex();
		Log.d(TAG, "playPreVideo-currentIndex: " + currentIndex);
		if(currentIndex == -1){
			currentIndex = demandTiviSchedule.getSizeVideoInfos();
			Log.d(TAG, "playPreVideo-SizeVideoInfos: " + currentIndex);
		}
//		if (demandTiviSchedule != null && demandTiviSchedule.getCurrentIndex() > 0) {
		if (demandTiviSchedule != null && currentIndex > 0) {
			cancelUpdateTimehandle();
			String msg = Vp9Contant.MSG_PLAY_PREV_VIDEO;
			setTextForTextView(notifyTextView, msg, 0);
			if(is3D){
				setTextForTextView(notifyTextView2, msg, 0);
			}
			isRefreshNotify = true;
			if (videoType == 1) {
//				menuItemAdapter.serverTimeInfo = null;
				popupVideoWindow.dismiss();
				if(is3D){
					popupVideoWindow2.dismiss();
				}
			     updateTimehandle.postDelayed(mUpdateTimeTask, 500L);
				return;
			}
			else if (videoType == 0) {
				VideoResult videoResult = demandTiviSchedule.getVideoInfoByIndex(currentIndex - 1);
				if (videoResult != null && videoResult.getVideoInfo() != null) {
					isLive = 0;
					if (!isDisplayChannelImage && intShowControl != 2) {
						setVisibility(loadingLayout, View.VISIBLE);
						if(is3D){
							setVisibility(loadingLayout2, View.VISIBLE);
						}
					} else {
						setVisibility(loadingLayout, View.GONE);
						if(is3D){
							setVisibility(loadingLayout2, View.GONE);
						}
					}
					if (vp9Player != null) {
						vp9Player.release();
					}
					mVideoView.destroyDrawingCache();
					mVideoView.refreshDrawableState();
					sendMsgToGetVideoInfo(videoResult);
				}else{
					updateTimehandle.postDelayed(mUpdateTimeTask, 500L);
				}
//				menuItemAdapter.serverTimeInfo = null;
				popupVideoWindow.dismiss();
				if(is3D){
					popupVideoWindow2.dismiss();
				}
				return;
			} else if (videoType == 4) {
//				int currentIndex = demandTiviSchedule.getCurrentIndex();
				VideoResult videoResult = demandTiviSchedule.getVideoInfoByIndex(currentIndex - 1);
				if (videoResult != null && videoResult.getVideoInfo() != null) {
					isLive = 0;
					if (!isDisplayChannelImage && intShowControl != 2) {
						setVisibility(loadingLayout, View.VISIBLE);
						if(is3D){
							setVisibility(loadingLayout2, View.VISIBLE);
						}
					} else {
						setVisibility(loadingLayout, View.GONE);
						if(is3D){
							setVisibility(loadingLayout2, View.GONE);
						}
					}
					if (vp9Player != null) {
						vp9Player.release();
					}
					mVideoView.destroyDrawingCache();
					mVideoView.refreshDrawableState();
					isLive = 0;
					cancelTask();
					reset();
					this.videoIndex = videoResult.getVideoInfo().getIndex();
					this.playingVideo = videoResult.getVideoInfo();
					vp9Player.playVideo(videoResult.getVideoInfo().getUrl(), false);
				} else {
					updateTimehandle.postDelayed(mUpdateTimeTask, 500L);
				}
//				menuItemAdapter.serverTimeInfo = null;
				popupVideoWindow.dismiss();
				if(is3D){
					popupVideoWindow2.dismiss();
				}
				return;
			}
//			setVisibility(loadingLayout, View.VISIBLE);
			if (!isDisplayChannelImage && intShowControl != 2) {
				setVisibility(loadingLayout, View.VISIBLE);
				if(is3D){
					setVisibility(loadingLayout2, View.VISIBLE);
				}
			} else {
				setVisibility(loadingLayout, View.GONE);
				if(is3D){
					setVisibility(loadingLayout2, View.GONE);
				}
			}
			if (vp9Player != null) {
				vp9Player.release();
			}
			mVideoView.destroyDrawingCache();
			mVideoView.refreshDrawableState();
			isLive = 0;
			cancelTask();
			reset();
			if (activity != null) {
				Vp9ActivityInterface vp9Activity = (Vp9ActivityInterface) activity;
				vp9Activity.closeEPG();
			}
			VideoResult videoResult = demandTiviSchedule.getVideoInfoByIndex(currentIndex - 1);
			if(videoResult.getVideoInfo() != null){
				playNewVideo(videoResult);
			}else{
				updateTimehandle.postDelayed(mUpdateTimeTask, 500L);
			}
		}
	}


	public void handlerPlayVideo(JSONObject jsonVideoInfo) throws JSONException {
		if (clearParam) {
			return;
		}
		// setVisibility(loadingLayout, View.VISIBLE);
		if (!isDisplayChannelImage && intShowControl != 2) {
//			if(!isLoading){
//				isLoading = true;
//				setVisibility(loadingLayout, View.VISIBLE);
//			}
			setVisibility(loadingLayout, View.VISIBLE);
			if(is3D){
				setVisibility(loadingLayout2, View.VISIBLE);
			}
		} else {
			Log.d(TAG, "loading 5: " + false);
//			if(isLoading){
//				isLoading = false;
//				setVisibility(loadingLayout, View.GONE);
//			}
			setVisibility(loadingLayout, View.GONE);
			if(is3D){
				setVisibility(loadingLayout2, View.GONE);
			}
		}
		setEnabled(btnSetting, false);
		if(is3D){
			setEnabled(btnSetting2, false);
		}
		// setVisibility(progessLayout, View.VISIBLE);
		String videoUrl = jsonVideoInfo.getString("videoUrl");
		this.playType = Vp9ParamUtil.getJsonInt(jsonVideoInfo, "playType", 0);
		this.videoResolutions = getVideoResolutionGroup(jsonVideoInfo);
		
		ArrayList<SubtitleInfo> subtitleInfos = new ArrayList<SubtitleInfo>();
		
		if (jsonVideoInfo.has("sub")) {
			JSONArray jsonArraySub = jsonVideoInfo.getJSONArray("sub");
			if (jsonArraySub != null && jsonArraySub.length() > 0) {
				for (int i = 0; i < jsonArraySub.length(); i++) {
					JSONObject jsonSub = jsonArraySub.getJSONObject(i);
					String subUrl = jsonSub.getString("subUrl");
					String subType = jsonSub.getString("subType");
					String subTypeName = jsonSub.getString("subTypeName");
					boolean isDisplay = jsonSub.getBoolean("isDisplay");
					SubtitleInfo subInfo = new SubtitleInfo(subUrl, subType, subTypeName, isDisplay);
					subtitleInfos.add(subInfo);
				}
			}
		}

		Log.d(TAG, "handlerPlayVideo: " + videoUrl);
		if (subtitleInfos.size() > 0) {
			subInfoArr = new SubtitleInfo[subtitleInfos.size()];
			subInfoArr = subtitleInfos.toArray(subInfoArr);
//			updateDefaultSubtitle();

		} else {
			subInfoArr = null;
		}
		
		playVideo(videoResolutions, videoUrl, false);

		// showController(0);
		showProgessLayout();

//		createPopupMenu(subInfoArr);
		updateFilmRelated(jsonVideoInfo);
	}

	public void updateFilmRelated(JSONObject jsonVideoInfo) {
		if (clearParam) {
			return;
		}
		if (jsonVideoInfo == null || !jsonVideoInfo.has("filmRelated")) {
			setBackgroundResource(btnNext, vp9_btn_next_hide_id);
			setBackgroundResource(btnPrev, vp9_btn_prev_hide_id);
			if(is3D){
				setBackgroundResource(btnNext2, vp9_btn_next_hide_id);
				setBackgroundResource(btnPrev2, vp9_btn_prev_hide_id);
			}
			return;
		}

		try {

			String curMovieID = Vp9ParamUtil.getJsonString(jsonVideoInfo, "movieID", "");

			JSONArray jsonFilmRelateds = jsonVideoInfo.getJSONArray("filmRelated");

			if (jsonFilmRelateds != null) {

				demandTiviSchedule = new DemandTiviSchedule();

				VideoInfo curVideoInfo = demandTiviSchedule.updateInfoForPlayVideos(jsonFilmRelateds, curMovieID);
				this.playingVideo = curVideoInfo;
				Thread videoMenuThread = new Thread() {
					public void run() {
//						Looper.prepare();
						createVideoMenu();
//						Looper.loop();
					}
				};
				videoMenuThread.setName("videoMenuThread_34");
				videoMenuThread.start();

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void handlerPlayLiveTivi(JSONObject jsonVideoInfo) throws JSONException {
		if (clearParam) {
			return;
		}
		reset();
		if (!isDisplayChannelImage && intShowControl != 2) {
//			if(!isLoading){
//				isLoading = true;
//				setVisibility(loadingLayout, View.VISIBLE);
//			}
			setVisibility(loadingLayout, View.VISIBLE);
			if(is3D){
				setVisibility(loadingLayout2, View.VISIBLE);
			}
		} else {
//			if(isLoading){
//				isLoading = false;
//				setVisibility(loadingLayout, View.GONE);
//			}
			setVisibility(loadingLayout, View.GONE);
			if(is3D){
				setVisibility(loadingLayout2, View.GONE);
			}
		}
		// tam dong
		setVisibility(progessLayout, View.GONE);
		if (is3D) {
			setVisibility(progessLayout2, View.GONE);
		}
		this.isLive = Vp9ParamUtil.getJsonInt(jsonVideoInfo, "isLive", -1);
		this.serverUrl = Vp9ParamUtil.getJsonString(jsonVideoInfo, "serverUrl", "");
		this.channelId = Vp9ParamUtil.getJsonString(jsonVideoInfo, "channelId", "");
		this.serverTimeUrl = Vp9ParamUtil.getJsonString(jsonVideoInfo, "serverTimeUrl", "");
		this.strDate = Vp9ParamUtil.getJsonString(jsonVideoInfo, "date", null);
		
		this.playType = Vp9ParamUtil.getJsonInt(jsonVideoInfo, "playType", 0);
//		ServerTimeInfo serverTimeInfo = new ServerTimeInfo(serverTimeUrl);
		serverTimeInfo.updateTime();
		if (strDate == null || strDate.length() != 8) {
			strDate = serverTimeInfo.getStrdate();
		}

		updateVideoSize(jsonVideoInfo);

//		String cheduleUrl = getCheduleUrl(serverUrl, channelId, strDate);
//
//		this.demandTiviSchedule = new DemandTiviSchedule(cheduleUrl, this.videoType, this);
//
//		this.demandTiviSchedule.setStrdate(serverTimeInfo.getStrdate());
//		
//		this.demandTiviSchedule.setRecordVideo(channelId, strDate);
		
		final ServerTimeInfo newServerTimeInfo = serverTimeInfo;

		
				Log.e(TAG, "hungvv2 : " + serverUrl);
				String cheduleUrl = getCheduleUrl(serverUrl, channelId, strDate);

				RotationMediaController.this.demandTiviSchedule = new DemandTiviSchedule(cheduleUrl, RotationMediaController.this.videoType, RotationMediaController.this);

				RotationMediaController.this.demandTiviSchedule.setStrdate(newServerTimeInfo.getStrdate());
				
//				RotationMediaController.this.demandTiviSchedule.setRecordVideo(channelId, strDate, recordUrl);
				
				int secondInDay = newServerTimeInfo.getSecondInDay() + 1;
				VideoResult videoResult = RotationMediaController.this.demandTiviSchedule.getVideoInfoByTime(secondInDay);
				if (videoResult != null && videoResult.getVideoInfo() != null) {
					VideoInfo videoInfo = videoResult.getVideoInfo();
					RotationMediaController.this.playingVideo = videoInfo;
				}
		Thread videoMenuThread = new Thread() {
			public void run() {
//				Looper.prepare();
//				RotationMediaController.this.demandTiviSchedule.setRecordVideo(channelId, strDate, recordUrl);
				createVideoMenu();
//				Looper.loop();
			}
		};
		videoMenuThread.setName("videoMenuThread_10");
		videoMenuThread.start();

		String videoUrl = jsonVideoInfo.getString("videoUrl");
		
		this.videoResolutions = getVideoResolutionGroup(jsonVideoInfo);
		
		Log.e(TAG, "handlerPlayLiveTivi: " + videoUrl);
		
		playVideo(videoResolutions, videoUrl, false);
		
		setEnabled(btnSetting, true);
		if(is3D){
			setEnabled(btnSetting2, true);
		}
		showController(0);
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

	public void handlerPlayDemandTivi1(JSONObject jsonVideoInfo) throws Exception {
		if (clearParam) {
			return;
		}
		// setVisibility(loadingLayout,
		// View.VISIBLE);

		this.isLive = Vp9ParamUtil.getJsonInt(jsonVideoInfo, "isLive", -1);
		this.serverUrl = Vp9ParamUtil.getJsonString(jsonVideoInfo, "serverUrl", "");
		this.channelId = Vp9ParamUtil.getJsonString(jsonVideoInfo, "channelId", "");
		this.serverTimeUrl = Vp9ParamUtil.getJsonString(jsonVideoInfo, "serverTimeUrl", "");
		this.strDate = Vp9ParamUtil.getJsonString(jsonVideoInfo, "date", null);
		this.playType = Vp9ParamUtil.getJsonInt(jsonVideoInfo, "playType", 0);
		this.channelName = Vp9ParamUtil.getJsonString(jsonVideoInfo, "channelName", "");
		this.channelIcon = Vp9ParamUtil.getJsonString(jsonVideoInfo, "channelIcon", "");
		
		if (!isDisplayChannelImage && intShowControl != 2) {
//			if(!isLoading){
//				isLoading = true;
//				setVisibility(loadingLayout, View.VISIBLE);
//			}
			setVisibility(loadingLayout, View.VISIBLE);
			if(is3D){
				setVisibility(loadingLayout2, View.VISIBLE);
			}
		} else { 
//			if(isLoading){
//				isLoading = false;
//				setVisibility(loadingLayout, View.GONE);
//			}
			setVisibility(loadingLayout, View.GONE);
			if(is3D){
				setVisibility(loadingLayout2, View.GONE);
			}
			Log.d(TAG, "loading 2: " + false);
		}

//		ServerTimeInfo serverTimeInfo = new ServerTimeInfo(serverTimeUrl);
		serverTimeInfo.updateTime();
		if (strDate == null || strDate.length() != 8) {
			strDate = serverTimeInfo.getStrdate();
		}

		String cheduleUrl = getCheduleUrl(serverUrl, channelId, strDate);

		this.demandTiviSchedule = new DemandTiviSchedule(cheduleUrl, this.videoType, this);

		this.demandTiviSchedule.setStrdate(serverTimeInfo.getStrdate());

		if (!this.demandTiviSchedule.isContainData()) {
			Vp9ActivityInterface vp9Player = (Vp9ActivityInterface) activity;
			vp9Player.showEPG();
			this.errorMsg = "Không có lịch chiếu";
			setTextForTextView(notifyTextView, this.errorMsg, 0);
			if(is3D){
				setTextForTextView(notifyTextView2, this.errorMsg, 0);
			}
			isRefreshNotify = true;
			return;
		}

		Thread videoMenuThread = new Thread() {
			public void run() {
//				Looper.prepare();
				createVideoMenu();
//				Looper.loop();
			}
		};
		videoMenuThread.setName("VideoMenuThread_4");
		videoMenuThread.start();

		Log.d(TAG, "Play Demand Tivi 1: " + this.demandTiviSchedule.getSizeVideoInfos());

		if (this.isLive == 1) {
			setVisibility(progessLayout, View.GONE);
			if (is3D) {
				setVisibility(progessLayout2, View.GONE);
			}
			Log.d(TAG, "Play Demand Tivi 1 - isLive: " + this.isLive);
			int secondInDay = serverTimeInfo.getSecondInDay() + 1;
			Log.d(TAG, "Play Demand Tivi 1 - secondInDay: " + secondInDay);

			int flag = demandTiviSchedule.checkTime(serverTimeInfo);

			VideoResult videoResult = null;

			Log.d(TAG, "Play Demand Tivi 1 - flag: " + flag);
			if (flag == -1) {
				Calendar calendar = Calendar.getInstance();
				calendar.set(serverTimeInfo.getYear(), serverTimeInfo.getMonth(), serverTimeInfo.getDay());
				calendar.add(Calendar.DATE, -1);
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
				String strYesterday = dateFormat.format(calendar.getTime());
				String yesterdayCheduleUrl = getCheduleUrl(serverUrl, channelId, strYesterday);
				videoResult = demandTiviSchedule.getLastVideoOfYesterday1(yesterdayCheduleUrl);
			} else {
				videoResult = demandTiviSchedule.getVideoInfoByTime(secondInDay);

			}

			if (videoResult != null && videoResult.getVideoInfo() != null) {
				int seekTime;
				VideoInfo videoInfo = videoResult.getVideoInfo();
				if (flag == -1) {
					seekTime = 24 * 60 * 60 + secondInDay - videoInfo.getIntStartTimeBySeconds();
				} else {
					seekTime = secondInDay - videoInfo.getIntStartTimeBySeconds();
				}
				Log.d(TAG, "Play Demand Tivi 1 - video index: " + videoInfo.getIndex());
				startDemandTivi(videoInfo, seekTime, this.isLive);
				this.videoIndex = videoInfo.getIndex();
			} else {
				// Error hoac khong co lich ngay hom truoc
				Log.d(TAG, "Play Demand Tivi 1 - video is not found");
//				if(isLoading){
//					isLoading = false;
//					setVisibility(loadingLayout, View.GONE);
//				}
				setVisibility(loadingLayout, View.GONE);
				if(is3D){
					setVisibility(loadingLayout2, View.GONE);
				}
				if (!isDisplayChannelImage) {
					setChanelTiviImage();
				}
				if (videoResult != null && videoResult.getNextIndex() > 0) {
					int nextIndex = videoResult.getNextIndex();
					int waitingTime = demandTiviSchedule.getWaitingTime(serverTimeInfo, nextIndex, 1000);
					mCheckPlayDemandVideoHandle1.postDelayed(mCheckPlayVideoTask1, waitingTime);
				} else {
					mCheckPlayDemandVideoHandle1.postDelayed(mCheckPlayVideoTask1, 1000L);
				}
			}

		} else {
			setVisibility(progessLayout, View.VISIBLE);
			if (is3D) {
				setVisibility(progessLayout2, View.VISIBLE);
			}
			int secondInDay = serverTimeInfo.getSecondInDay() + 3;

			VideoResult videoResult;

			videoIndex = Vp9ParamUtil.getJsonInt(jsonVideoInfo, "videoIndex", 0);

			if (videoIndex == -1) {
				videoResult = demandTiviSchedule.getVideoInfoByTime(secondInDay);
			} else {
				videoResult = demandTiviSchedule.getVideoInfoByIndex(videoIndex);
			}

			// int videoIndex = Vp9ParamUtil.getJsonInt(jsonVideoInfo,
			// "videoIndex", 0);

			// VideoResult videoResult =
			// demandTiviSchedule.getVideoInfoByIndex(videoIndex);
			if (videoResult != null && videoResult.getVideoInfo() != null) {
				VideoInfo videoInfo = videoResult.getVideoInfo();
				startDemandTivi(videoInfo, 0, this.isLive);
				this.videoIndex = videoInfo.getIndex();
			} else {
				Vp9ActivityInterface vp9Player = (Vp9ActivityInterface) activity;
				vp9Player.showEPG();
			}
		}
	}
	
	
	
	
	private String checkRecordVideoExist(ArrayList<String> arrayRecord, VideoInfo videoInfo){
		String result = "";
		if (arrayRecord != null && arrayRecord.size() > 0) {
			String startVideoInfo = videoInfo.getStartTime().trim().replace(":", "");
			
			for (int i = 0; i < arrayRecord.size(); i++) {
				if (startVideoInfo.equals(arrayRecord.get(i).trim())) {
					result = startVideoInfo;
					break;
				}
			}
		}
		return result;
	}

	public synchronized void createVideoMenu() {
		Log.e(TAG, "---------------- createVideoMenu");
		if (clearParam) {
			return;
		}
		if (this.demandTiviSchedule.isContainData()) {

			int len = this.demandTiviSchedule.getSizeVideoInfos();

			
			ArrayList<String> recordVideoIds = this.demandTiviSchedule.getRecordVideoIds();
			
			for (int i = 0; i < len; i++) {
				VideoResult videoResult = this.demandTiviSchedule.getVideoInfoByIndex(i);
				
				if (videoResult != null) {
					VideoInfo videoInfo = videoResult.getVideoInfo();
					if (videoInfo != null && videoInfo.getVideoName() != null) {
						if (videoType == 2 || videoType == 3) {
							String startTime = "";
							if (videoInfo.getStartTime() != null) {
								startTime = videoInfo.getStartTime().replace(" ", "");
								if (startTime.length() >= 5) {
									startTime = videoInfo.getStartTime().replace(" ", "").substring(0, 5);
								}
							}
//							 strVideos[i] = (i + 1) + ": " +
//							 videoInfo.getVideoName() + "::" + i;
							
//							strVideos[i] = startTime + " - " + videoInfo.getVideoName() + "::" + i;
//							VideoMenuItem videoMenuItem = new VideoMenuItem(videoInfo.getVideoName() + "::" + i);
//							strVideos[i] = videoMenuItem;
						} else if (videoType == 0 || videoType == 1) {

//							strVideos[i] = videoInfo.getVideoName() + "::" + i;
							
							String checkRecordVideoExist = checkRecordVideoExist(recordVideoIds, videoInfo);
							if (checkRecordVideoExist != "") {
								videoInfo.setRecordUrl(this.demandTiviSchedule.getRecordUrl() + checkRecordVideoExist + ".mp4");
							}
						} else if (videoType == 4) {
//							strVideos[i] = videoInfo.getVideoName() + "::" + i;
//							VideoMenuItem videoMenuItem = new VideoMenuItem(videoInfo.getVideoName() + "::" + i);
//							strVideos[i] = videoMenuItem;
						}
					} else {
//						strVideos[i] = "";
//						VideoMenuItem videoMenuItem = new VideoMenuItem("");
//						strVideos[i] = videoMenuItem;
					}
				} else {
//					strVideos[i] = "";
//					VideoMenuItem videoMenuItem = new VideoMenuItem("");
//					strVideos[i] = videoMenuItem;
				}

			}
			// Log.d(TAG, "Items: " + Arrays.toString(strVideos));
			if (activity != null) {
				
				popupVideoWindow = new PopupWindow();
				popupVideoWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
				
				Vp9LeftListView listViewVideos = new Vp9LeftListView(activity);
				listViewVideos.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
				listViewVideos.setLayoutParams(params);
				params.rightMargin = 0;
				params.gravity = Gravity.RIGHT;
				menuItemAdapter = new MenuItemAdapter(activity, R.layout.custom_item_list, this.demandTiviSchedule.getVideoInfoList(), videoType, demandTiviSchedule, serverTimeInfo, 1);
				listViewVideos.setAdapter(menuItemAdapter);
				
				// some other visual settings
				popupVideoWindow.setFocusable(true);
				listViewVideos.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), 
			            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
				
				popupVideoWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
				popupVideoWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
				// set the list view as pop up window content
//				popupVideoWindow.setWidth(listViewVideos.getMeasuredWidth());
//				popupVideoWindow.setHeight(listViewVideos.getMeasuredWidth());
				popupVideoWindow.setContentView(listViewVideos);
				Log.e(TAG, "w = " + listViewVideos.getWidth() + ", h = " + listViewVideos.getHeight());
				
				listViewVideos.setOnItemClickListener(new ItemVideoMenuClickListener());

				listViewVideos.setOnKeyListener(new KeyVideoMenuListener());
				
				popupVideoWindow.setOnDismissListener(new DismissPopupWindowListener(1));
				popupVideoWindow.setTouchInterceptor(new OnTouchListener() { 
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						float x = event.getX();
						float y = event.getY();
						if(x >= popupVideoWindow.getContentView().getX()
								&& x <= popupVideoWindow.getContentView().getX() + popupVideoWindow.getContentView().getWidth()
								&& y >= popupVideoWindow.getContentView().getY()
								&& y <= popupVideoWindow.getContentView().getY() + popupVideoWindow.getContentView().getHeight()){
							return false;
						}else if(popupVideoWindow2 != null){
							if(x >= popupVideoWindow2.getContentView().getX()
									&& x <= popupVideoWindow2.getContentView().getX() + popupVideoWindow2.getContentView().getWidth()
									&& y >= popupVideoWindow2.getContentView().getY()
									&& y <= popupVideoWindow2.getContentView().getY() + popupVideoWindow2.getContentView().getHeight()){
								return false;
							}
						}
					return true;
					}
					});
				
				if(is3D){
					popupVideoWindow2 = new PopupWindow();
					popupVideoWindow2.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
					
					Vp9RightListView listViewVideos2 = new Vp9RightListView(activity);
					listViewVideos2.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
					
					listViewVideos.setFriendListView(listViewVideos2);
					listViewVideos2.setFriendListView(listViewVideos);
					
					LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
					listViewVideos2.setLayoutParams(params2);
					params2.rightMargin = 0;
					params2.gravity = Gravity.RIGHT;
					menuItemAdapter2 = new MenuItemAdapter(activity, R.layout.custom_item_list, this.demandTiviSchedule.getVideoInfoList(), videoType, demandTiviSchedule, serverTimeInfo, 2);
					listViewVideos2.setAdapter(menuItemAdapter2);

					popupVideoWindow2.setFocusable(true);
					listViewVideos2.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), 
				            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
					popupVideoWindow2.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
					popupVideoWindow2.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
					setVisibility(listViewVideos2, View.VISIBLE);
					popupVideoWindow2.setContentView(listViewVideos2);
					Log.e(TAG, "w = " + listViewVideos2.getWidth() + ", h = " + listViewVideos2.getHeight());
					
					listViewVideos2.setOnItemClickListener(new ItemVideoMenuClickListener());

					listViewVideos2.setOnKeyListener(new KeyVideoMenuListener());
					
					popupVideoWindow2.setOnDismissListener(new DismissPopupWindowListener(2));
					popupVideoWindow2.setTouchInterceptor(new OnTouchListener() { 
						@Override
						public boolean onTouch(View v, MotionEvent event) {
							float x = event.getX();
							float y = event.getY();
							if(x >= popupVideoWindow2.getContentView().getX()
									&& x <= popupVideoWindow2.getContentView().getX() + popupVideoWindow2.getContentView().getWidth()
									&& y >= popupVideoWindow2.getContentView().getY()
									&& y <= popupVideoWindow2.getContentView().getY() + popupVideoWindow2.getContentView().getHeight()){
								return false;
							}else if(popupVideoWindow != null){
								if(x >= popupVideoWindow.getContentView().getX()
										&& x <= popupVideoWindow.getContentView().getX() + popupVideoWindow.getContentView().getWidth()
										&& y >= popupVideoWindow.getContentView().getY()
										&& y <= popupVideoWindow.getContentView().getY() + popupVideoWindow.getContentView().getHeight()){
									return false;
								}
							}
						return true;
						}
						});
//					showVideoMenu(btnChoose);
//					menuItemAdapter.serverTimeInfo = null;
//					menuItemAdapter2.serverTimeInfo = null;
					popupVideoWindow.dismiss();
					popupVideoWindow2.dismiss();
				}
				
//				if(RotationMediaController.this.is3D){
//					popupVideoWindow.setWidth(popupVideoWindow.getWidth());
//				}
			}
		}
	}


	
	public class DismissPopupWindowListener implements android.widget.PopupWindow.OnDismissListener {

		private int type;

		public DismissPopupWindowListener(int type) {
			this.type = type;
		}

		@Override
		public void onDismiss() {
			showControlLayout = false;
			if (handleHideController != null) {
				handleHideController.removeCallbacks(hideTask);
			}
			hideController();
			
			switch (type) {
			case 1:
//				if(popupVideoWindow2 != null && popupVideoWindow2.isShowing()){
//					popupVideoWindow2.dismiss();
//				}
				break;
				
			case 2:
//				if(popupVideoWindow != null && popupVideoWindow.isShowing()){
//					popupVideoWindow.dismiss();
//				}
				break;
			default:
				break;
			}
		}
	}

	public class KeyVideoMenuListener implements OnKeyListener {

		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			int action = event.getAction();

			boolean isSucess = false;
			if (action == KeyEvent.ACTION_DOWN) {
				Log.d(TAG, "KeyVideoMenuListener: " + keyCode);
				isSucess = handleKeyEvent(v, keyCode, event);
			}
			return isSucess;
		}
	}

	public class ItemVideoMenuClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
			Log.d(TAG, "ItemVideoMenuClickListener - position: " + position);
//			menuItemAdapter.serverTimeInfo = null;
			final int newPosition = position;
			popupVideoWindow.dismiss();
			if(popupVideoWindow.getContentView() != null){
				((ListView)popupVideoWindow.getContentView()).setSelection(newPosition);
			}
			if(is3D){
				popupVideoWindow2.dismiss();
				if(popupVideoWindow2.getContentView() != null){
					((ListView)popupVideoWindow2.getContentView()).setSelection(newPosition);
				}
			}
//			popupVideoWindow2.dismiss();
			if(activity != null){
				activity.runOnUiThread(new Runnable(){
		            public void run(){
		            	RotationMediaController.this.handlerVideoMenuClickListener(newPosition);
		            }
		        });
			}
		}
	}

	/*
	 * adapter where the list values will be set
	 */
	public ArrayAdapter<String> getVideosAdapter(String videosArray[], final Activity activity) {

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, R.id.custom_item_list, videosArray) {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {

				// setting the ID and text for every items in the list

				View view = convertView;
				if (view == null) {
					Log.e(TAG, "position: " + position + " view == null: ");

					// visual settings for the list item
					LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					view = inflater.inflate(R.layout.custom_item_list, null);
				}
				
				String item = getItem(position);
				if (item != null) {
					
					String[] itemArr = item.split("::");
					final String text = itemArr[0];
					final String id = itemArr[1];
					final int intPosition = position;
					
					final MagicTextView textItem = (MagicTextView) view.findViewById(R.id.custom_item_list);
					Log.e(TAG, "position: " + position + " view != null: ");
					
					activity.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							textItem.setText(text);
							textItem.setTag(id);
							textItem.setTextSize(30);
							textItem.setPadding(10, 10, 10, 10);
							// textItem.setShadowLayer(5.0f, 3, 3, Color.BLACK);
							textItem.setTextColor(Color.WHITE);
							textItem.setStroke(1, Color.BLACK);
							textItem.setVisibility(View.VISIBLE);
							
							if (videoType >= 0 && videoType != 1) {
								if (demandTiviSchedule != null && demandTiviSchedule.getCurrentIndex() == intPosition) {
									textItem.setTextColor(Color.RED);
								} else {
									textItem.setTextColor(Color.WHITE);
								}
							} else {
								textItem.setTextColor(Color.WHITE);
								if (serverTimeUrl != null) {
//									ServerTimeInfo serverTimeInfo = new ServerTimeInfo(serverTimeUrl);
									serverTimeInfo.updateTime();
									VideoResult videoResult = demandTiviSchedule.getVideoInfoByTime(serverTimeInfo.getSecondInDay());
									if (videoResult != null && videoResult.getVideoInfo() != null) {
										if (videoResult.getVideoInfo().getIndex() == intPosition) {
											textItem.setTextColor(Color.RED);
										}
									}
								}
	
							}
						}
					});
				}
				return view;

			}
		};

		return adapter;
	}

	public void handlerVideoMenuClickListener(int position) {
		
		if (clearParam) {
			return;
		}

		if (demandTiviSchedule != null) {
			String msg = Vp9Contant.MSG_PLAY_SELECT_VIDEO;
			setTextForTextView(notifyTextView, msg, 0);
			if(is3D){
				setTextForTextView(notifyTextView2, msg, 0);
			}
			isRefreshNotify = true;
			Log.d(TAG, "handlerVideoMenuClickListener - videoType: " + videoType);
			if (videoType == 1) {
				VideoResult videoResult = demandTiviSchedule.getVideoInfoByIndex(position);
				if (videoResult != null && videoResult.getVideoInfo() != null) {
					isLive = 0;
					sendMsgToGetOffTVVideoInfo(videoResult);
				}
//				popupVideoWindow.dismiss();
				return;
			} else if (videoType == 0) {
				VideoResult videoResult = demandTiviSchedule.getVideoInfoByIndex(position);
				if (videoResult != null && videoResult.getVideoInfo() != null) {
					isLive = 0;
//					setVisibility(loadingLayout, View.VISIBLE);
					if (!isDisplayChannelImage && intShowControl != 2) {
//						if(!isLoading){
//							isLoading = true;
//							setVisibility(loadingLayout, View.VISIBLE);
//						}
						setVisibility(loadingLayout, View.VISIBLE);
						if(is3D){
							setVisibility(loadingLayout2, View.VISIBLE);
						}
					} else {
//						if(isLoading){
//							isLoading = false;
//							setVisibility(loadingLayout, View.GONE);
//						}
						setVisibility(loadingLayout, View.GONE);
						if(is3D){
							setVisibility(loadingLayout2, View.GONE);
						}
					}
//					if (vp9Player != null) {
//						vp9Player.release();
//					}
//					mVideoView.destroyDrawingCache();
//					mVideoView.refreshDrawableState();
					sendMsgToGetVideoInfo(videoResult);
				}
//				popupVideoWindow.dismiss();
				return;
			} else if (videoType == 4) {
//				int currentIndex = demandTiviSchedule.getCurrentIndex();
				VideoResult videoResult = demandTiviSchedule.getVideoInfoByIndex(position);
				if (videoResult != null && videoResult.getVideoInfo() != null) {
					isLive = 0;
//					setVisibility(loadingLayout, View.VISIBLE);
					if (!isDisplayChannelImage && intShowControl != 2) {
//						if(!isLoading){
//							isLoading = true;
//							setVisibility(loadingLayout, View.VISIBLE);
//						}
						setVisibility(loadingLayout, View.VISIBLE);
						if(is3D){
							setVisibility(loadingLayout2, View.VISIBLE);
						}
					} else {
//						if(isLoading){
//							isLoading = false;
//							setVisibility(loadingLayout, View.GONE);
//						}
						setVisibility(loadingLayout, View.GONE);
						if(is3D){
							setVisibility(loadingLayout2, View.GONE);
						}
					}
//					if (vp9Player != null) {
//						vp9Player.release();
//					}
//					mVideoView.destroyDrawingCache();
//					mVideoView.refreshDrawableState();
					isLive = 0;
					cancelTask();
					reset();
					this.videoIndex = videoResult.getVideoInfo().getIndex();
					this.playingVideo = videoResult.getVideoInfo();
//					vp9Player.playVideo(videoResult.getVideoInfo().getUrl(), false);
					playVideo(videoResult.getVideoInfo().getVideoResolutionGroups(), videoResult.getVideoInfo().getUrl(), false);
				}
//				popupVideoWindow.dismiss();
				return;
			}

			if (demandTiviSchedule.getCurrentIndex() != position) {
				isLive = 0;
//				setVisibility(loadingLayout, View.VISIBLE);
				if (!isDisplayChannelImage && intShowControl != 2) {
//					if(!isLoading){
//						isLoading = true;
//						setVisibility(loadingLayout, View.VISIBLE);
//					}
					setVisibility(loadingLayout, View.VISIBLE);
					if(is3D){
						setVisibility(loadingLayout2, View.VISIBLE);
					}
				} else {
//					if(isLoading){
//						isLoading = false;
//						setVisibility(loadingLayout, View.GONE);
//					}
					setVisibility(loadingLayout, View.GONE);
					if(is3D){
						setVisibility(loadingLayout2, View.GONE);
					}
				}
//				if (vp9Player != null) {
//					vp9Player.release();
//				}
//				mVideoView.destroyDrawingCache();
//				mVideoView.refreshDrawableState();
				cancelTask();
				reset();
				VideoResult videoResult = demandTiviSchedule.getVideoInfoByIndex(position);
				playNewVideo(videoResult);
//				popupVideoWindow.dismiss();
			}
		}
		
	}

	public boolean sendMsgToGetOffTVVideoInfo(VideoResult videoResult) {
		if (clearParam) {
			return false;
		}
		boolean isSuccess = false;
		if (videoResult != null && videoResult.getVideoInfo() != null) {
			VideoInfo videoInfo = videoResult.getVideoInfo();
			try {
				JSONObject jsonEvent = new JSONObject();
				jsonEvent.put("action", "getOffTvVideoInfo");
				jsonEvent.put("videoUrl", videoInfo.getUrl());
				jsonEvent.put("videoName", videoInfo.getVideoName());
				jsonEvent.put("startTime", videoInfo.getStartTime());
				Vp9ActivityInterface vp9Activity = (Vp9ActivityInterface) activity;
				vp9Activity.sendEvent(jsonEvent);
				isSuccess = true;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return isSuccess;

	}

	public boolean sendMsgToGetVideoInfo(VideoResult videoResult) {
		if (clearParam) {
			return false;
		}
		boolean isSuccess = false;
		if (videoResult != null && videoResult.getVideoInfo() != null) {
			VideoInfo videoInfo = videoResult.getVideoInfo();
			String movieID = videoInfo.getMovieID();
			try {
				JSONObject jsonEvent = new JSONObject();
				jsonEvent.put("action", "getVideoInfo");
				jsonEvent.put("movieID", movieID);
				Vp9ActivityInterface vp9Activity = (Vp9ActivityInterface) activity;
				vp9Activity.sendEvent(jsonEvent);
				isSuccess = true;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return isSuccess;

	}

	public boolean handleKeyEvent(View v, int keyCode, KeyEvent event) {
		boolean isSucess = false;
		int vp9KeyCode = Vp9KeyEvent.getKeyCode(keyCode);
		Log.d(TAG, "Vp9KeyCode = " + vp9KeyCode);
		switch (vp9KeyCode) {

		case Vp9KeyEvent.KEYCODE_MEDIA_PREVIOUS:
			if (videoType == 1) {
				break;
			}
			isLive = 0;
			if (popupVideoWindow != null && popupVideoWindow.isShowing()) {
//				menuItemAdapter.serverTimeInfo = null;
				popupVideoWindow.dismiss();
			}
			
			if (is3D && popupVideoWindow2 != null && popupVideoWindow2.isShowing()) {
//				menuItemAdapter2.serverTimeInfo = null;
				popupVideoWindow2.dismiss();
				
			}
			
			runThreadPlayPreVideo();
//			playPreVideo();
			
			isSucess = true;
			
			break;
		case Vp9KeyEvent.KEYCODE_DPAD_LEFT:
			if (videoType == 1) {
				break;
			}
			if (popupVideoWindow != null && popupVideoWindow.isShowing()) {
				isLive = 0;
//				menuItemAdapter.serverTimeInfo = null;
				popupVideoWindow.dismiss();
				if(is3D || (popupVideoWindow2 != null && popupVideoWindow2.isShowing())){
					popupVideoWindow2.dismiss();
				}
				runThreadPlayPreVideo();
//				playPreVideo();
				
			}else if(isLive == 0 && vp9Player != null){
				activity.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						int currentPosition = vp9Player.getCurrentPosition();
						if (currentPosition > 5000) {
							vp9Player.seekTo(currentPosition - 5000);
						}
					}
				});
			}
			isSucess = true;
			break;
		case Vp9KeyEvent.KEYCODE_ZOOM_IN:
			showVideoMenu(btnChoose);
			isSucess = false;
			break;
		case Vp9KeyEvent.KEYCODE_F3:
		case Vp9KeyEvent.KEYCODE_F11:
			showVideoMenu(btnChoose);
//			if(is3D){
//				showVideoMenu(btnChoose2);
//			}
			break;

		case Vp9KeyEvent.KEYCODE_MEDIA_NEXT:
			if (videoType == 1) {
				break;
			}
			isLive = 0;
			if (popupVideoWindow != null && popupVideoWindow.isShowing()) {
//				menuItemAdapter.serverTimeInfo = null;
				popupVideoWindow.dismiss();
				if(is3D || (popupVideoWindow2 != null && popupVideoWindow2.isShowing())){
					popupVideoWindow2.dismiss();
				}
			}
			runThreadPlayNextVideo();
//			playNextVideo();
			isSucess = true;
			

			break;
		case Vp9KeyEvent.KEYCODE_DPAD_RIGHT:
			if (videoType == 1) {
				break;
			}
			if (popupVideoWindow != null && popupVideoWindow.isShowing()) {
				isLive = 0;
//				menuItemAdapter.serverTimeInfo = null;
				popupVideoWindow.dismiss();
				if(is3D || (popupVideoWindow2 != null && popupVideoWindow2.isShowing())){
					popupVideoWindow2.dismiss();
				}
				runThreadPlayNextVideo();
//				playNextVideo();
				
				
			}else if(isLive == 0 && vp9Player != null){				
				int currentPosition = vp9Player.getCurrentPosition();
				if (currentPosition > 0) {
					activity.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							int currentPosition = vp9Player.getCurrentPosition();
							if (currentPosition > 5000) {
								vp9Player.seekTo(currentPosition + 5000);
							}
						}
					});
				}
			}
			isSucess = true;
			break;
		case Vp9KeyEvent.KEYCODE_F2:
		case Vp9KeyEvent.KEYCODE_F10:
			handleClickPlayAndPause();
			isSucess = true;
			break;
		case Vp9KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
			handleClickPlayAndPause();
			isSucess = false;
			break;
		case Vp9KeyEvent.KEYCODE_MENU:
			if (videoType != 0) {
				showEPGOrCloseEPG();
			}
			isSucess = true;
			break;
		case Vp9KeyEvent.KEYCODE_UNKNOWN:
		case Vp9KeyEvent.KEYCODE_F4:
		case Vp9KeyEvent.KEYCODE_F12:
			showPopupMenu(btnSub);
//			showPopupMenu(btnSub2);
			isSucess = true;
			break;
		case Vp9KeyEvent.KEYCODE_DPAD_CENTER:
		case Vp9KeyEvent.KEYCODE_ENTER :	
			if ((popupVideoWindow != null && popupVideoWindow.isShowing())) {
				isSucess = false;
			}else{
				VpMainActivity activity = (VpMainActivity) getActivity();
				setFocusView(activity.getAppview());
				
				if (this.intShowControl == 0) {
					this.showController(1);
				} else {
					this.intShowControl = 0;
					this.setVisibility(this.controllerLayout, View.GONE);
					if(is3D){
						this.setVisibility(this.controllerLayout2, View.GONE);
					}
				}
				
				isSucess = true;
			}
			
			break;
			
		}
		

		return isSucess;
	}
	

	public void runThreadPlayNextVideo() {
		if(activity != null){
			activity.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					playNextVideo();
				}
			});
		}
		
	}

	public void runThreadPlayPreVideo() {
		if(activity != null){
			activity.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					playPreVideo();
				}
			});
		}
	}

//	public void showEPGOrCloseEPG() {
//		if (clearParam) {
//			return;
//		}
//		if (activity != null) {
//			Vp9ActivityInterface vp9Activity = (Vp9ActivityInterface) activity;
//			
//			CordovaWebView webview = ((VpMainActivity)activity).getAppview();
//			
//			if (webview.getVisibility() == View.INVISIBLE || webview.getVisibility() == View.GONE) {
//				vp9Activity.showEPG();
//			} else {
//				vp9Activity.closeEPG();
//			}
//
//		}
//	}
	
	public void showEPGOrCloseEPG() {
		if (clearParam) {
			return;
		}
		if (activity != null) {
			Vp9ActivityInterface vp9Activity = (Vp9ActivityInterface) activity;
			vp9Activity.handleShowOrCloseEPG();

		}
	}

	public void playNewVideo(VideoResult videoResult) {
		if (clearParam) {
			return;
		}
		VideoInfo videoInfo = videoResult.getVideoInfo();
		if (videoType == 2 && videoInfo != null) {
			startDemandTivi(videoInfo, 0, this.isLive);
			this.videoIndex = videoInfo.getIndex();
		} else if (videoType == 3 && videoInfo != null) {
			VideoInfo childVideo = videoInfo.getChildVideoInfoByIndex(0);
			if (childVideo != null) {
				startDemandTivi(childVideo, 0, this.isLive);
				this.videoIndex = videoInfo.getIndex();
				this.childVideoIndex = childVideo.getIndex();
			} else {
				this.errorMsg = "lịch chiếu video bị lỗi #2";
				setTextForTextView(notifyTextView, this.errorMsg, 0);
				if(is3D){
					setTextForTextView(notifyTextView2, this.errorMsg, 0);
				}
				isRefreshNotify = true;
			}
		} else {
			this.errorMsg = "lịch chiếu video bị lỗi #3";
			setTextForTextView(notifyTextView, this.errorMsg, 0);
			if(is3D){
				setTextForTextView(notifyTextView2, this.errorMsg, 0);
			}
			isRefreshNotify = true;
		}
	}

	public void handlerPlayDemandTivi2(JSONObject jsonVideoInfo) {
		if (clearParam) {
			return;
		}
		// setVisibility(loadingLayout,
		// View.VISIBLE);
		this.isLive = Vp9ParamUtil.getJsonInt(jsonVideoInfo, "isLive", -1);
		this.serverUrl = Vp9ParamUtil.getJsonString(jsonVideoInfo, "serverUrl", "");
		this.channelId = Vp9ParamUtil.getJsonString(jsonVideoInfo, "channelId", "");
		this.serverTimeUrl = Vp9ParamUtil.getJsonString(jsonVideoInfo, "serverTimeUrl", "");
		this.strDate = Vp9ParamUtil.getJsonString(jsonVideoInfo, "date", null);
		this.playType = Vp9ParamUtil.getJsonInt(jsonVideoInfo, "playType", 0);
//		ServerTimeInfo serverTimeInfo = new ServerTimeInfo(serverTimeUrl);
		serverTimeInfo.updateTime();
		if (!isDisplayChannelImage && intShowControl != 2) {
//			if(!isLoading){
//				isLoading = true;
//				setVisibility(loadingLayout, View.VISIBLE);
//			}
			setVisibility(loadingLayout, View.VISIBLE);
			if(is3D){
				setVisibility(loadingLayout2, View.VISIBLE);
			}
		} else {
//			if(isLoading){
//				isLoading = false;
//				setVisibility(loadingLayout, View.GONE);
//			}
			setVisibility(loadingLayout, View.GONE);
			if(is3D){
				setVisibility(loadingLayout2, View.GONE);
			}
		}
		if (strDate == null || strDate.length() != 8) {
			strDate = serverTimeInfo.getStrdate();
		}
		String cheduleUrl = getCheduleUrl(serverUrl, channelId, strDate);

		this.demandTiviSchedule = new DemandTiviSchedule(cheduleUrl, this.videoType, this);

		this.demandTiviSchedule.setStrdate(serverTimeInfo.getStrdate());

		if (demandTiviSchedule == null || !this.demandTiviSchedule.isContainData()) {
			Vp9ActivityInterface vp9Player = (Vp9ActivityInterface) activity;
			vp9Player.showEPG();
			this.errorMsg = "Không có lịch chiếu";
			setTextForTextView(notifyTextView, this.errorMsg, 0);
			if(is3D){
				setTextForTextView(notifyTextView2, this.errorMsg, 0);
			}
			isRefreshNotify = true;
			return;
		}

		Thread videoMenuThread = new Thread() {
			public void run() {
//				Looper.prepare();
				createVideoMenu();
//				Looper.loop();
			}
		};
		videoMenuThread.setName("VideoMenuThread_6");
		videoMenuThread.start();

		if (this.isLive == 1) {
			setVisibility(progessLayout, View.GONE);
			if (is3D) {
				setVisibility(progessLayout2, View.GONE);
			}
			int secondInDay = serverTimeInfo.getSecondInDay() + 1;
			int flag = demandTiviSchedule.checkTime(serverTimeInfo);
			VideoResult videoResult;

			Log.d(TAG, "flag - in day: " + flag);
			if (flag == -1) {
				Calendar calendar = Calendar.getInstance();
				calendar.set(serverTimeInfo.getYear(), serverTimeInfo.getMonth(), serverTimeInfo.getDay());
				calendar.add(Calendar.DATE, -1);
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
				String strYesterday = dateFormat.format(calendar.getTime());
				String yesterdayCheduleUrl = getCheduleUrl(serverUrl, channelId, strYesterday);
				videoResult = demandTiviSchedule.getLastVideoOfYesterday2(yesterdayCheduleUrl);
			} else {
				videoResult = demandTiviSchedule.getVideoInfoByTime(secondInDay);
			}

			if (videoResult != null && videoResult.getVideoInfo() != null) {
				this.curVideoResult = videoResult;
				VideoInfo videoInfo = videoResult.getVideoInfo();
				VideoInfo childVideoInfo;
				if (flag == -1) {
					childVideoInfo = videoInfo.getChildVideoInfoByTime(secondInDay);

					if (childVideoInfo == null && videoInfo.size() > 0) {
						childVideoInfo = videoInfo.getChildVideoInfoByIndex(videoInfo.size() - 1);
					}

				} else {
					childVideoInfo = videoInfo.getChildVideoInfoByTime(secondInDay);
				}

				if (childVideoInfo != null) {
					Log.d(TAG, "play video 1: " + videoInfo.getIndex() + "/" + childVideoInfo.getIndex());
					int seekTime;
					if (flag == -1) {
						seekTime = 24 * 60 * 60 + secondInDay - childVideoInfo.getIntStartTimeBySeconds();
					} else {
						seekTime = secondInDay - childVideoInfo.getIntStartTimeBySeconds();
					}
					startDemandTivi(childVideoInfo, seekTime, this.isLive);
					this.videoIndex = videoInfo.getIndex();
					this.childVideoIndex = childVideoInfo.getIndex();
				} else {
					Log.d(TAG, "play child video is null");
					if (isPlay) {
						mCheckPlayDemandVideoHandle2.postDelayed(mCheckPlayVideoTask2, 1000L);
					}
				}

				// demandTiviSchedule.setCurrentIndex(videoInfo.getIndex());
			} else {
				Log.d(TAG, "video is null");
				if (videoResult != null && videoResult.getNextIndex() != -1) {
					long waittingTime = demandTiviSchedule.getWaitingTime(serverTimeInfo, videoResult.getNextIndex(), 1000);
					mCheckPlayDemandVideoHandle2.postDelayed(mCheckPlayVideoTask2, waittingTime);
				} else {
					mCheckPlayDemandVideoHandle2.postDelayed(mCheckPlayVideoTask2, 1000L);
				}

			}
		} else {
			setVisibility(progessLayout, View.VISIBLE);
			if (is3D) {
				setVisibility(progessLayout2, View.VISIBLE);
			}
			videoIndex = Vp9ParamUtil.getJsonInt(jsonVideoInfo, "videoIndex", 0);
			VideoResult videoResult;
			int secondInDay = serverTimeInfo.getSecondInDay() + 3;
			if (videoIndex == -1) {
				videoResult = demandTiviSchedule.getVideoInfoByTime(secondInDay);
			} else {
				videoResult = demandTiviSchedule.getVideoInfoByIndex(videoIndex);
			}
			// VideoResult videoResult =
			// demandTiviSchedule.getVideoInfoByIndex(videoIndex);
			if (videoResult != null && videoResult.getVideoInfo() != null) {
				VideoInfo videoInfo = videoResult.getVideoInfo();
				this.videoIndex = videoInfo.getIndex();
				Log.d(TAG, "play video 20: " + videoInfo.getIndex());
				VideoInfo childVideoInfo = videoInfo.getChildVideoInfoByIndex(0);
				if (childVideoInfo != null) {
					Log.d(TAG, "play video 21: " + videoInfo.getIndex() + "/" + childVideoInfo.getIndex());
					startDemandTivi(childVideoInfo, 0, this.isLive);
					this.videoIndex = videoInfo.getIndex();
					this.childVideoIndex = childVideoInfo.getIndex();
				} else {
					Log.e(TAG, "Error: Info of DemandTivi 2 is error");
					mCheckPlayDemandVideoHandle2.postDelayed(mCheckPlayVideoTask2, 1000L);

				}
			} else {
				Log.e(TAG, "Error: video not found with index video");
				Vp9ActivityInterface vp9Player = (Vp9ActivityInterface) activity;
				vp9Player.showEPG();
			}
		}

	}

	public void handlerPlayDemandTivi3(JSONObject jsonVideoInfo) {
		if (clearParam) {
			return;
		}
		// setVisibility(loadingLayout,
		// View.VISIBLE);
		this.isLive = Vp9ParamUtil.getJsonInt(jsonVideoInfo, "isLive", -1);
		this.serverUrl = Vp9ParamUtil.getJsonString(jsonVideoInfo, "serverUrl", "");
		this.channelId = Vp9ParamUtil.getJsonString(jsonVideoInfo, "channelId", "");
		this.serverTimeUrl = Vp9ParamUtil.getJsonString(jsonVideoInfo, "serverTimeUrl", "");
		this.strDate = Vp9ParamUtil.getJsonString(jsonVideoInfo, "date", null);
		this.playType = Vp9ParamUtil.getJsonInt(jsonVideoInfo, "playType", 0);
//		ServerTimeInfo serverTimeInfo = new ServerTimeInfo(serverTimeUrl);
		serverTimeInfo.updateTime();
		if (!isDisplayChannelImage && intShowControl != 2) {
//			if(!isLoading){
//				isLoading = true;
//				setVisibility(loadingLayout, View.VISIBLE);
//			}
			setVisibility(loadingLayout, View.VISIBLE);
			if(is3D){
				setVisibility(loadingLayout2, View.VISIBLE);
			}
		} else {
//			if(isLoading){
//				isLoading = false;
//				setVisibility(loadingLayout, View.GONE);
//			}
			setVisibility(loadingLayout, View.GONE);
			if(is3D){
				setVisibility(loadingLayout2, View.GONE);
			}
		}
		if (strDate == null || strDate.length() != 8) {
			strDate = serverTimeInfo.getStrdate();
		}
		String cheduleUrl = getCheduleUrl(serverUrl, channelId, strDate);

		this.demandTiviSchedule = new DemandTiviSchedule(cheduleUrl, this.videoType, this);

		this.demandTiviSchedule.setStrdate(serverTimeInfo.getStrdate());

		if (demandTiviSchedule == null || !this.demandTiviSchedule.isContainData()) {
			Vp9ActivityInterface vp9Player = (Vp9ActivityInterface) activity;
			vp9Player.showEPG();
			this.errorMsg = "Không có lịch chiếu";
			setTextForTextView(notifyTextView, this.errorMsg, 0);
			if(is3D){
				setTextForTextView(notifyTextView2, this.errorMsg, 0);
			}
			isRefreshNotify = true;
			return;
		}

		Thread videoMenuThread = new Thread() {
			public void run() {
//				Looper.prepare();
				createVideoMenu();
//				Looper.loop();
			}
		};
		videoMenuThread.setName("VideoMenuThread_8");
		videoMenuThread.start();

		if (this.isLive == 1) {
			setVisibility(progessLayout, View.GONE);
			if (is3D) {
				setVisibility(progessLayout2, View.GONE);
			}
			int secondInDay = serverTimeInfo.getSecondInDay() + 1;
			int flag = demandTiviSchedule.checkTime(serverTimeInfo);
			VideoResult videoResult;

			Log.d(TAG, "flag - in day: " + flag);
			if (flag == -1) {
				Calendar calendar = Calendar.getInstance();
				calendar.set(serverTimeInfo.getYear(), serverTimeInfo.getMonth(), serverTimeInfo.getDay());
				calendar.add(Calendar.DATE, -1);
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
				String strYesterday = dateFormat.format(calendar.getTime());
				String yesterdayCheduleUrl = getCheduleUrl(serverUrl, channelId, strYesterday);
				videoResult = demandTiviSchedule.getLastVideoOfYesterday2(yesterdayCheduleUrl);
			} else {
				videoResult = demandTiviSchedule.getVideoInfoByTime(secondInDay);
			}

			if (videoResult != null && videoResult.getVideoInfo() != null) {
				this.curVideoResult = videoResult;
				VideoInfo videoInfo = videoResult.getVideoInfo();
				VideoInfo childVideoInfo;
				if (flag == -1) {
					childVideoInfo = videoInfo.getChildVideoInfoByTime(secondInDay);

					if (childVideoInfo == null && videoInfo.size() > 0) {
						childVideoInfo = videoInfo.getChildVideoInfoByIndex(videoInfo.size() - 1);
					}

				} else {
					childVideoInfo = videoInfo.getChildVideoInfoByTime(secondInDay);
				}

				if (childVideoInfo != null) {
					Log.d(TAG, "play video 1: " + videoInfo.getIndex() + "/" + childVideoInfo.getIndex());
					int seekTime;
					if (flag == -1) {
						seekTime = 24 * 60 * 60 + secondInDay - childVideoInfo.getIntStartTimeBySeconds();
					} else {
						seekTime = secondInDay - childVideoInfo.getIntStartTimeBySeconds();
					}
					startDemandTivi(childVideoInfo, seekTime, this.isLive);
					this.videoIndex = videoInfo.getIndex();
					this.childVideoIndex = childVideoInfo.getIndex();
				} else {
					Log.d(TAG, "play child video is null");
					if (isPlay) {
						mCheckPlayDemandVideoHandle2.postDelayed(mCheckPlayVideoTask2, 1000L);
					}
				}

				// demandTiviSchedule.setCurrentIndex(videoInfo.getIndex());
			} else {
				Log.d(TAG, "video is null");
				if (videoResult != null && videoResult.getNextIndex() != -1) {
					long waittingTime = demandTiviSchedule.getWaitingTime(serverTimeInfo, videoResult.getNextIndex(), 1000);
					mCheckPlayDemandVideoHandle2.postDelayed(mCheckPlayVideoTask2, waittingTime);
				} else {
					mCheckPlayDemandVideoHandle2.postDelayed(mCheckPlayVideoTask2, 1000L);
				}

			}
		} else {
			setVisibility(progessLayout, View.VISIBLE);
			if (is3D) {
				setVisibility(progessLayout2, View.VISIBLE);
			}
			videoIndex = Vp9ParamUtil.getJsonInt(jsonVideoInfo, "videoIndex", 0);
			VideoResult videoResult;
			int secondInDay = serverTimeInfo.getSecondInDay() + 3;
			if (videoIndex == -1) {
				videoResult = demandTiviSchedule.getVideoInfoByTime(secondInDay);
			} else {
				videoResult = demandTiviSchedule.getVideoInfoByIndex(videoIndex);
			}
			// VideoResult videoResult =
			// demandTiviSchedule.getVideoInfoByIndex(videoIndex);
			if (videoResult != null && videoResult.getVideoInfo() != null) {
				VideoInfo videoInfo = videoResult.getVideoInfo();
				this.videoIndex = videoInfo.getIndex();
				Log.d(TAG, "play video 20: " + videoInfo.getIndex());
				VideoInfo childVideoInfo = videoInfo.getChildVideoInfoByIndex(0);
				if (childVideoInfo != null) {
					Log.d(TAG, "play video 21: " + videoInfo.getIndex() + "/" + childVideoInfo.getIndex());
					startDemandTivi(childVideoInfo, 0, this.isLive);
					this.videoIndex = videoInfo.getIndex();
					this.childVideoIndex = childVideoInfo.getIndex();
				} else {
					Log.e(TAG, "Error: Info of DemandTivi 2 is error");
					mCheckPlayDemandVideoHandle2.postDelayed(mCheckPlayVideoTask2, 1000L);

				}
			} else {
				Log.e(TAG, "Error: video not found with index video");
				Vp9ActivityInterface vp9Player = (Vp9ActivityInterface) activity;
				vp9Player.showEPG();
			}
		}

	}
	
	public void loadLeftLogo(final VideoInfo playingVideo) {

	}
	
	public void loadRightLogo(final VideoInfo playingVideo) {
	}

	public void sendVideoInfoToRemote() {
		if (clearParam) {
			return;
		}
		JSONObject jsonInfCurVideo = new JSONObject();
		int curTime = vp9Player.getCurrentPosition();
		boolean isPlay = vp9Player.isPlaying();
		int duration = vp9Player.getDuration();
		String channelId = this.channelId;
		JSONArray jsonArrSub = new JSONArray();
		if (settingPopup != null) {

		}

		if (subInfoArr != null && settingPopup != null && checkboxSubtitle != null) {
			// Menu menu = popupMenu.getMenu();
			int menuSize = checkboxSubtitle.length;
			if (subInfoArr.length == menuSize) {
				try {
					for (int i = 0; i < menuSize; i++) {
						boolean isChoice = checkboxSubtitle[i].isChecked();
						String subType = subInfoArr[i].getSubType();
						JSONObject jsonSub = new JSONObject();
						jsonSub.put("subType", subType);
						jsonSub.put("isChoice", isChoice);
						jsonArrSub.put(jsonSub);
					}
					// result.put("result", jsonArrSub);

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		try {

			jsonInfCurVideo.put("time", curTime);
			jsonInfCurVideo.put("player_state", isPlay);
			jsonInfCurVideo.put("duration", duration);
			jsonInfCurVideo.put("subtitle", jsonArrSub);
			jsonInfCurVideo.put("channelId", channelId);

			if (playingVideo != null) {

				String movieID = playingVideo.getMovieID();

				String videoName = playingVideo.getVideoName();

				if (movieID != null && !"".equals(movieID)) {
					jsonInfCurVideo.put("movieID", movieID);
				}

				if (videoName != null && !"".equals(videoName)) {
					jsonInfCurVideo.put("videoName", videoName);
				}

				if (videoType == 2) {
					int currentIndex = demandTiviSchedule.getCurrentIndex();
					// int cCurrentChildIndex =
					// demandTiviSchedule.getCurrentChildIndex();
					VideoResult videoResult = demandTiviSchedule.getVideoInfoByIndex(currentIndex);
					if (videoResult != null && videoResult.getVideoInfo() != null) {

						String parentMovieID = videoResult.getVideoInfo().getMovieID();

						String parentVideoName = videoResult.getVideoInfo().getVideoName();

						if (parentMovieID != null && !"".equals(parentMovieID)) {
							jsonInfCurVideo.put("parentMovieID", parentMovieID);
						}

						if (parentVideoName != null && !"".equals(parentVideoName)) {
							jsonInfCurVideo.put("parentVideoName", parentVideoName);
						}
					}

				}

			}

			if (this.demandTiviSchedule != null) {
				jsonInfCurVideo.put("index", this.demandTiviSchedule.getCurrentIndex());
				jsonInfCurVideo.put("childIndex", this.demandTiviSchedule.getCurrentChildIndex());
			}

			if (this.isRemoteListener) {
				JSONObject jsonEvent = new JSONObject();
				jsonEvent.put("action", "firstPlay");
				jsonEvent.put("information", jsonInfCurVideo);
				Vp9ActivityInterface vp9Activity = (Vp9ActivityInterface) activity;
				vp9Activity.sendEvent(jsonEvent);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	public void destroy(int type) {
		Log.e(TAG, "destroy");
		cancelTask();
		if (vp9Player != null) {
			vp9Player.releaseNoneThread();
		}
		unScaleX(is3D);
		setVisibility(loadingLayout, View.GONE);
		setVisibility(controllerTopLayout, View.GONE);
		setVisibility(progessLayout, View.GONE);
		setVisibility(logoLayout, View.GONE);
		setVisibility(videoTitleLayout, View.GONE);
		setVisibility(subtitlesLayout, View.GONE);
		if(is3D){
			setVisibility(loadingLayout2, View.GONE);
			setVisibility(controllerTopLayout2, View.GONE);
			setVisibility(progessLayout2, View.GONE);
			setVisibility(logoLayout2, View.GONE);
			setVisibility(videoTitleLayout2, View.GONE);
			setVisibility(subtitlesLayout2, View.GONE);
		}
		refreshGUI();
		if (type == 1) {
			clearParam();
		}
		is3D = false;
		System.gc();
	}


	private void refreshGUI() {

		if(logoText != null){
			emptyTextView(logoText);
		}
		
		if(logoText2 != null){
			emptyTextView(logoText2);
		}
		
		if(loadRate != null){
			emptyTextView(loadRate);
		}
		
		if(loadRate != null){
			emptyTextView(loadRate);
		}
		
		if(loadRate2 != null){
			emptyTextView(loadRate2);
		}
		
		if(videoTitle != null){
			emptyTextView(videoTitle);
		}
		
		if(videoTitle2 != null){
			emptyTextView(videoTitle2);
		}
		
		if(notifyTextView != null){
			emptyTextView(notifyTextView);
		}
		
		if(notifyTextView2 != null){
			emptyTextView(notifyTextView2);
		}
		
		if(tvFrom != null){
			emptyTextView(tvFrom);
		}
		
		if(tvTo != null){
			emptyTextView(tvTo);
		}
		
		if(tvFrom2 != null){
			emptyTextView(tvFrom2);
		}
		
		if(tvTo2 != null){
			emptyTextView(tvTo2);
		}
		
		if(tvTo2 != null){
			emptyTextView(tvTo2);
		}
		
		if(configSubtiles != null){
			configSubtiles.clear();
		}
		
		if(videoResolutions != null){
			videoResolutions.clear();
		}
		
		if(configSubtiles != null){
			configSubtiles.clear();
		}
		
		if(settingSubTypes != null){
			settingSubTypes.clear();
		}
		
	}

	public void clearParam() {
		serverTimeInfo = null;
		checkboxSubtitle = null;
		clearParam = true;
		mVideoView = null;
		mMetadataRetriever = null;
		pdLoading = null;
		pdLoading2 = null;
		loadRate = null;
		loadRate2 = null;
		sbFull = null;
		sbFull2 = null;
		tvFrom = null;
		tvFrom2 = null;
		tvTo = null;
		tvTo2 = null;
		handleHideController = null;
		updateTimehandle = null;
		mResumHandle = null;
		mResumErrorHandle = null;
		mCheckPlayDemandVideoHandle1 = null;
		mCheckPlayDemandVideoHandle2 = null;
		proxySpeedHandler = null;
		loadSub = null;
		btnPlay = null;
		btnPlay2 = null;
		controllerLayout = null;
		controllerLayout2 = null;
		// popupMenu = null;
		settingPopup = null;
		btnSub = null;
		btnSub2 = null;
		btnBack = null;
		btnBack2 = null;
		subInfoArr = null;
		activity = null;
		btnSetting = null;
		btnSetting2 = null;
		demandTiviSchedule = null;
		serverTimeUrl = null;
		vp9ChannelImage = null;
		vp9ChannelImage2 = null;
		serverUrl = null;
		vp9Player = null;
//		mHolder = null;
		progessLayout = null;
		progessLayout2 = null;
		timeShowIndex = 0;
		token = null;
		this.resolutionLimit = null;
		proxySevice = null;
		settingSubTypes = null;
		activity = null;
	}

//	public void initFunction() {
//		if (clearParam) {
//			return;
//		}
//		player.setOnErrorListener(new MediaPlayer.OnErrorListener() {
//			public boolean onError(MediaPlayer paramMediaPlayer, int whatError, int extra) {
//				return onError(paramMediaPlayer, whatError, extra);
//			}
//		});
//
//		player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//			public void onPrepared(MediaPlayer paramMediaPlayer) {
//				onPrepared(paramMediaPlayer);
//			}
//		});
//
//		player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//
//			@Override
//			public void onCompletion(MediaPlayer mp) {
//				onCompletion(mp);
//			}
//		});
//
//		player.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
//
//			@Override
//			public void onBufferingUpdate(MediaPlayer mp, int percent) {
//				onBufferingUpdate(mp, percent);
//
//			}
//		});
//
//		player.setOnInfoListener(new MediaPlayer.OnInfoListener() {
//
//			@Override
//			public boolean onInfo(MediaPlayer mp, int what, int extra) {
//				return onInfo(mp, what, extra);
//			}
//		});
//
//		player.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
//
//			@Override
//			public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
//				onVideoSizeChanged(mp, width, height);
//
//			}
//		});
//
//		this.mVideoView.setOnTouchListener(new View.OnTouchListener() {
//			public boolean onTouch(View view, MotionEvent motionEvent) {
//				return onTouch(view, motionEvent);
//			}
//		});
//
//		// this.mVideoView.setOnClickListener(new Button.OnClickListener(){
//		//
//		// @Override
//		// public void onClick(View view) {
//		// onClickForDisplayControl(view);
//		// }
//		// });
//
//		this.mVideoView.setOnHoverListener(new View.OnHoverListener() {
//
//			@Override
//			public boolean onHover(View v, MotionEvent event) {
//				return onHover(v, event);
//			}
//
//		});
//
//		this.vp9ChannelImage.setOnTouchListener(new View.OnTouchListener() {
//			public boolean onTouch(View view, MotionEvent motionEvent) {
//				return onTouch(view, motionEvent);
//			}
//		});
//
//		// this.vp9ChannelImage.setOnClickListener(new Button.OnClickListener(){
//		//
//		// @Override
//		// public void onClick(View view) {
//		// onClickForDisplayControl(view);
//		// }
//		// });
//
//		// this.mVideoView.setOnTouchListener(new View.OnTouchListener(){
//		// public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
//		// {
//		// if (isEnableTouch.booleanValue())
//		// if (!isTouch.booleanValue())
//		// {
//		// showController();
//		// if (handleHideController != null)
//		// handleHideController.removeCallbacks(hideTask);
//		// hideController();
//		// isTouch = Boolean.valueOf(true);
//		// }
//		// else
//		// {
//		// hideAll();
//		// isTouch = Boolean.valueOf(false);
//		// }
//		// return false;
//		// }
//		// });
//
//		btnPlay.setOnClickListener(new Button.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				handleClickPlayAndPause();
//			}
//		});
//
//		this.btnSub.setOnClickListener(new Button.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				showPopupMenu(v);
//			}
//		});
//
//		this.btnChoose.setOnClickListener(new Button.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				showVideoMenu(v);
//			}
//		});
//
//		this.btnPrev.setOnClickListener(new Button.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				playPreVideo();
//			}
//		});
//
//		this.btnNext.setOnClickListener(new Button.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				playNextVideo();
//			}
//		});
//
//		// this.btnBack.setOnClickListener(new Button.OnClickListener(){
//		// @Override
//		// public void onClick(View v) {
//		// handleBackButtonEvent(v);
//		// }
//		// });
//
//	}

	public void onClickForDisplayControl(View view) {
		if (clearParam) {
			return;
		}
		// showController(1);
		if (intShowControl == 0) {
			showController(1);
		} else {
			intShowControl = 0;
			setVisibility(controllerLayout, View.GONE);
			if(is3D){
				setVisibility(controllerLayout2, View.GONE);
			}
			
		}

	}

	public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
		Log.d(TAG, "width = " + width);
		Log.d(TAG, "height = " + height);
		// updateVideoSize(width, height);
		// if(isInPlaybackState()){
		// Log.d(TAG, "width1 = " + width);
		// Log.d(TAG, "height1 = " + height);
		// updateVideoSize(width, height);
		// }

	}

	public void showVideoMenu(View v) {
		if (clearParam) {
			return;
		}
		if (popupVideoWindow != null) {
//			menuItemAdapter.serverTimeInfo = null;
			if(menuItemAdapter.serverTimeInfo != null){
				menuItemAdapter.serverTimeInfo.updateTime();
			}
			if (activity != null) {
				Vp9ActivityInterface vp9Activity = (Vp9ActivityInterface) activity;
				vp9Activity.closeEPG();
			}
			showControlLayout = true;
			int height = controllerTopLayout.getMeasuredHeight() + progessLayout.getMeasuredHeight();
			Log.d(TAG, "height = " + height);
			if(is3D){
				int screenWidth = activity.getWindowManager().getDefaultDisplay().getWidth();
				popupVideoWindow.getContentView().measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
				
				int popupWidth = popupVideoWindow.getContentView().getWidth();
				
				if(popupWidth == 0){
					popupWidth = (screenWidth*580)/1280;;
				}else{
					popupWidth = popupVideoWindow.getContentView().getWidth();
				}
				
				Log.d(TAG, "sw = " + ((screenWidth/2) - popupWidth) + ", pw = " + popupWidth + ", y = " + height);
				
				popupVideoWindow.showAsDropDown(progessLayout, (screenWidth/2) - popupWidth, 0);
				
				popupWidth = popupVideoWindow.getContentView().getWidth();
				
//				popupVideoWindow.update((screenWidth/2) - popupWidth, height, -1, -1);
				
//				popupVideoWindow.showAtLocation(vp9PlayerLayout, Gravity.NO_GRAVITY, (int) screenWidth/2, height);
				
				if(popupVideoWindow2 != null){
					
					popupVideoWindow2.getContentView().measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
					
					int popupWidth2 = popupVideoWindow2.getContentView().getWidth();
					
					if(popupWidth2 == 0){
						popupWidth2 = (screenWidth*580)/1280;
					}else{
						popupWidth2 = popupVideoWindow2.getContentView().getWidth();
					}
					
//					popupWidth2 = popupVideoWindow2.getContentView().getWidth();
					Log.d(TAG, "sw = " + (screenWidth - popupWidth2) + ", pw2 = " + popupWidth2 + ", y = " + height);
					popupVideoWindow2.showAsDropDown(progessLayout2, screenWidth - popupWidth2, 0);
					
				}
			}else{
				int xheight;
				if(isLive == 1){
					xheight = controllerTopLayout.getBottom() - btnChoose.getBottom();
				}else{
					xheight = progessLayout.getBottom() - btnChoose.getBottom();
				}
				popupVideoWindow.showAsDropDown(btnChoose, -5, xheight);
			}
		}
		// createVideoMenu();
		// popupVideoWindow.showAsDropDown(v, -5, 0)
	}

	public boolean onHover(View v, MotionEvent motionEvent) {
		// if(clearParam){
		// return false;
		// }
		// Log.d(TAG, "onHover: " + motionEvent.getAction());
		// int action = motionEvent.getAction();
		// if(action == MotionEvent.ACTION_HOVER_MOVE){
		// showController(1);
		// }
		return true;
	}

	public boolean onInfo(MediaPlayer mp, int whatInfo, int extra) {
		if (whatInfo == MediaPlayer.MEDIA_INFO_BAD_INTERLEAVING) {
			Log.v(TAG, "Media Info, Media Info Bad Interleaving " + extra);
		} else if (whatInfo == MediaPlayer.MEDIA_INFO_NOT_SEEKABLE) {
			Log.v(TAG, "Media Info, Media Info Not Seekable " + extra);
		} else if (whatInfo == MediaPlayer.MEDIA_INFO_UNKNOWN) {
			Log.v(TAG, "Media Info, Media Info Unknown " + extra);
		} else if (whatInfo == MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING) {
			Log.v(TAG, "MediaInfo, Media Info Video Track Lagging " + extra);
		} else if (whatInfo == MediaPlayer.MEDIA_INFO_METADATA_UPDATE) {
			Log.v(TAG, "MediaInfo, Media Info Metadata Update " + extra);
		}
		return false;
	}

	public void setTextForTextView() {
		if (clearParam) {
			return;
		}
		setTextForTextView(tvTo, Utilities.milliSecondsToTimer(0), 0);
		setTextForTextView(tvFrom, Utilities.milliSecondsToTimer(0), 0);
		if(is3D){
			setTextForTextView(tvTo2, Utilities.milliSecondsToTimer(0), 0);
			setTextForTextView(tvFrom2, Utilities.milliSecondsToTimer(0), 0);
		}

	}

	public String getCheduleUrl(String serverUrl, String channelId, String strDate) {
		String cheduleUrl = serverUrl + "/tivichannel/Channel_" + channelId + "_" + strDate + ".json";
		if(token != null && !"".equals(token)){
			cheduleUrl += "?token="  + token;
		}
		Log.d(TAG, "getCheduleUrl: " + cheduleUrl);
		return cheduleUrl;
	}

	public void setImageBitmap(final ImageView imageView, final String imageUrl) {
		if (activity != null && imageView != null) {
			Thread decodeStreamThrd = new Thread() {
				public void run() {
					// try {
					// Bitmap bmp = BitmapFactory.decodeStream(new
					// java.net.URL(imageUrl).openStream());
					// setImageBitmap(imageView, bmp);
					// // imageView.setImageBitmap(bmp);
					// } catch (MalformedURLException e) {
					// e.printStackTrace();
					// } catch (java.net.ConnectException e) {
					// e.printStackTrace();
					// } catch (IOException e) {
					// e.printStackTrace();
					// } catch (Exception e) {
					// e.printStackTrace();
					// }

					Bitmap bmp = loadImage(imageUrl);
					setImageBitmap(imageView, bmp);
				}
			};
			decodeStreamThrd.setName("decodeStreamThrd_31");
			decodeStreamThrd.start();
		}
	}

	public Bitmap loadImage(String image_location) {
		URL imageURL = null;
		Bitmap bitmap = null;
		if (image_location != null) {
			HttpURLConnection connection = null;
			try {
				imageURL = new URL(image_location);
				connection = (HttpURLConnection) imageURL.openConnection();
				connection.setReadTimeout(5000);
				connection.setDoInput(true);
				connection.connect();
				InputStream inputStream = connection.getInputStream();
				bitmap = BitmapFactory.decodeStream(inputStream);// Convert to
				connection.disconnect();													// bitmap
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}finally{
				if(connection != null){
					connection.disconnect();
				}
			}
		}
		return bitmap;
	}

	public void setImageBitmap(final ImageView imageView, final Bitmap bmp) {
		if (activity != null && imageView != null && bmp != null) {

			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {

					imageView.setImageBitmap(bmp);
				}
			});
		}

	}

	public void startDemandTivi(VideoInfo videoInfo, int seekTime, int isLive) {
		if (clearParam) {
			return;
		}
		reset();
		String videoUrl = videoInfo.getUrl();
		
		Log.d(TAG, "startDemandTivi: " + videoUrl);
		ArrayList<SubtitleInfo> subtitleInfos = videoInfo.getSubtitleInfos();
		this.videoSeekTime = seekTime * 1000;
		currentError = this.videoSeekTime;
		
		if (subtitleInfos != null && subtitleInfos.size() > 0) {
			subInfoArr = new SubtitleInfo[subtitleInfos.size()];
			subInfoArr = subtitleInfos.toArray(subInfoArr);
//			updateDefaultSubtitle();
		}else{
			subInfoArr = null;
		}
		this.videoResolutions = videoInfo.getVideoResolutionGroups();
		playVideo(this.videoResolutions, videoUrl, false);
		
		Log.d(TAG, "videoSeekTime: " + this.videoSeekTime);
		// showController(0);
		showProgessLayout();
		this.playingVideo = videoInfo;
		mVideoWidth = this.playingVideo.getWidth();
		mVideoHeight = this.playingVideo.getHeight();

		// if(videoInfo != null && videoInfo.getThumbIcon() != null){
		// setImageBitmap(logoVideo, videoInfo.getThumbIcon());
		// setVisibility(logoVideo, View.VISIBLE);
		// }else{
		// setVisibility(logoVideo, View.GONE);
		// }
		//
		// if(videoInfo != null){
		// setTextForTextView(videoTitle,"<b>" +
		// videoInfo.getVideoName() + "</b>");
		// setVisibility(videoTitle, View.VISIBLE);
		// }else{
		// setVisibility(videoTitle, View.GONE);
		// }
	}

	public void showProgessLayout() {
		if (clearParam) {
			return;
		}
		if (intShowControl != 2) {
			if (!isDisplayChannelImage) {
				if (videoType == 0 || (videoType > 1 && isLive == 0)) {
					setVisibility(progessLayout, View.VISIBLE);
					if(is3D){
						setVisibility(progessLayout2, View.VISIBLE);
					}
				}
			}
		} else {
			setVisibility(videoTitleLayout, View.GONE);
			setVisibility(progessLayout, View.GONE);
			if(is3D){
				setVisibility(progessLayout2, View.GONE);
				setVisibility(videoTitleLayout2, View.GONE);
			}
		}
	}

	public synchronized void playVideo(ArrayList<VideoResolutionGroup> videoResolutionGroups,
			String videoUrl, boolean loop) {
		
		final ArrayList<VideoResolutionGroup> newVideoResolutionGroups = videoResolutionGroups;
		
		final String newVideoUrl = videoUrl;
		
		final boolean newLoop = loop;
		
		if(activity != null){
			activity.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					threadPlayVideo(newVideoResolutionGroups, newVideoUrl, newLoop);
					
				}
			});
			
//			if (startVideoHandler != null && startVideoRunnable != null) {
//				startVideoHandler.removeCallbacks(startVideoRunnable);
//			}else{
//				startVideoHandler = new Handler();
//			}
//			
//			startVideoRunnable = new StartVideoRunnable(newVideoResolutions, newVideoUrl, newLoop);
//			
//			startVideoHandler.postDelayed(startVideoRunnable, 10);
		}
	}
	
//	StartVideoRunnable startVideoRunnable = new StartVideoRunnable(newVideoResolutions, newVideoUrl, newLoop);
	

	
//	class StartVideoRunnable implements Runnable {
//		ArrayList<VideoResolution> videoResolutions; 
//		String videoUrl;
//		boolean loop;
//		
//		public StartVideoRunnable(ArrayList<VideoResolution> _videoResolutions, String _videoUrl, boolean _loop) {
//			this.videoResolutions = _videoResolutions;
//			this.videoUrl = _videoUrl;
//			this.loop = _loop;
//		}
//		
//		@Override
//		public void run() {
//			threadPlayVideo(videoResolutions, videoUrl, loop);
//		}
//	}

	protected synchronized void threadPlayVideo(ArrayList<VideoResolutionGroup> videoResolutionGroups,
			String videoUrl, boolean loop) {
		int type = 0;
		String newVideoUrl = videoUrl;
		int resolutionIndex = 0;
//		String resolution;a
		boolean isH265 = false;
		if(this.intH265 == 1){
			isH265 = true;
		}else if(this.intH265 == 2){
			boolean isDecoder = Vp9MediaCodecInfo.checkDecoder("video/hevc");
			if(isDecoder){
				isH265 = true;
			}
		}
		
		int maxResolution = getIntResolution(this.resolutionLimit);
		
		if(videoResolutionGroups != null){
			Log.d(TAG, "isH265: " + isH265 + ", maxResolution: " + maxResolution + ", videoResolutions size: " + videoResolutionGroups.size());
		}else{
			Log.d(TAG, "isH265: " + isH265 + ", maxResolution: " + maxResolution);
		}
		
		
		if(!isH265 && maxResolution > 0 && videoResolutionGroups != null && videoResolutionGroups.size() > 0){
			
			ArrayList<VideoResolutionGroup> newVideoResolGroups = new ArrayList<VideoResolutionGroup>();
			for(int i = 0; i < videoResolutionGroups.size(); i++){
				VideoResolutionGroup videoResolutionGroup = videoResolutionGroups.get(i);
				if(videoResolutionGroup != null){
					int intResolution = getIntResolution(videoResolutionGroup.getResolution());
					if(intResolution <= maxResolution){
						newVideoResolGroups.add(videoResolutionGroup);
					}
				}
			}
			
			videoResolutionGroups = newVideoResolGroups;
			this.videoResolutions = newVideoResolGroups;
		}
		
		if(videoResolutionGroups != null && videoResolutionGroups.size() > 0){
			
			for(int i = 0; i < videoResolutionGroups.size(); i++){
				VideoResolutionGroup videoResolutionGroup = videoResolutionGroups.get(i);
				if(videoResolutionGroup != null){
					videoResolutionGroups.get(i).setIndex(i);
				}
			}
			if (!isDisplayChannelImage && intShowControl != 2) {
				setVisibility(loadingLayout, View.VISIBLE);
				if(is3D){
					setVisibility(loadingLayout2, View.VISIBLE);
				}
			} else {
				setVisibility(loadingLayout, View.GONE);
				if(is3D){
					setVisibility(loadingLayout2, View.GONE);
				}
			}
			boolean isExist = false;
			if(this.codecResolution != null && this.codec != null){
				VideoResolutionGroup newVideoResolGroup = null;
				ArrayList<VideoResolutionGroup> videoResolGroupList = new ArrayList<VideoResolutionGroup>();
				for(int i = 0; i < videoResolutionGroups.size(); i++){
					VideoResolutionGroup videoResolutionGroup = videoResolutionGroups.get(i);
					String codec = videoResolutionGroup.getCodec();
					String resolution = videoResolutionGroup.getResolution();
//					videoResolution.setIndex(i);
					StringBuffer key = new StringBuffer();
					if(codec != null){
						key.append(codec.trim());
					}
					if(resolution != null){
						key.append("-").append(resolution.trim());
					}
					if(this.codec.equals(codec)){
						videoResolGroupList.add(videoResolutionGroup);
//						videoResolution.setIndex(i);
						if(this.codecResolution.equals(key.toString().trim())){
							newVideoResolGroup = videoResolutionGroup;
							break;
						}
					}
				}
				
				if(newVideoResolGroup == null && videoResolGroupList.size() > 0){
					newVideoResolGroup = videoResolGroupList.get(0);
					int intOldResolution = getIntResolution(this.resolution);
					int intNewResolution = getIntResolution(newVideoResolGroup.getResolution());
					int minResolution = Math.abs(intNewResolution - intOldResolution);
					for(int k = 1; k < videoResolGroupList.size(); k++){
						intNewResolution = getIntResolution(videoResolGroupList.get(k).getResolution());
						int min = Math.abs(intNewResolution - intOldResolution);
						if(minResolution > min){
							newVideoResolGroup = videoResolGroupList.get(k);
							minResolution = min;
						}
					}
				}
				
				if(newVideoResolGroup != null){
					String codec = newVideoResolGroup.getCodec();
					String resolution = newVideoResolGroup.getResolution();
					StringBuffer key = new StringBuffer();
					if(codec != null){
						key.append(codec.trim());
					}
					
					if(resolution != null){
						this.resolution = resolution.trim();
						key.append("-").append(resolution.trim());
					}
					
					Log.d(TAG, "Codec - Resolution: " + this.codecResolution);
					if("h264".equals(codec)){
						type = 0;
					}else{
						type = 1;
					}
					
					resolutionIndex = newVideoResolGroup.getIndex();
					Log.d(TAG, "Codec - resolutionIndex 1: " + resolutionIndex);
					newVideoUrl = newVideoResolGroup.getVideoUrl();
					updateVideoSize(resolution);
					
					this.codecResolution = key.toString();
					this.codec = codec;
					isExist = true;
				} 
			}

			
			if(!isExist && (playType == 0 || playType == 1)){
				for(int i = 0; i < videoResolutionGroups.size(); i++){
					VideoResolutionGroup videoResolutionGroup = videoResolutionGroups.get(i);
					String codec = videoResolutionGroup.getCodec();
//					resolution = videoResolution.getResolution();
					if("h264".equals(codec)){
						type = 0;
						resolutionIndex = i;
						Log.d(TAG, "Codec - resolutionIndex 2: " + resolutionIndex);
						newVideoUrl = videoResolutionGroup.getVideoUrl();
						String resolution = videoResolutionGroup.getResolution();
						updateVideoSize(resolution);
						StringBuffer key = new StringBuffer();
						if(codec != null){
							key.append(codec.trim());
						}
						if(resolution != null){
							this.resolution = resolution.trim();
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
				for(int i = 0; i < videoResolutionGroups.size(); i++){
					VideoResolutionGroup videoResolutionGroup = videoResolutionGroups.get(i);
					String codec = videoResolutionGroup.getCodec();
//					resolution = videoResolution.getResolution();
					if("h265".equals(codec)){
						type = 1;
						resolutionIndex = i;
						Log.d(TAG, "Codec - resolutionIndex 3: " + resolutionIndex);
						newVideoUrl = videoResolutionGroup.getVideoUrl();
						String resolution = videoResolutionGroup.getResolution();
						updateVideoSize(resolution);
						StringBuffer key = new StringBuffer();
						if(codec != null){
							key.append(codec.trim());
						}
						if(resolution != null){
							this.resolution = resolution.trim();
							key.append("-").append(resolution.trim());
						}
						this.codecResolution = key.toString();
						this.codec = codec;
						isExist = true;
						break;
					}
				}
			}else if(!isExist){
				VideoResolutionGroup videoResolutionGroup = videoResolutionGroups.get(0);
				String codec = videoResolutionGroup.getCodec();
				if("h265".equals(codec)){
					type = 1;
				}else{
					type = 0;
				}
				resolutionIndex = 0;
				Log.d(TAG, "Codec - resolutionIndex 4: " + resolutionIndex);
				newVideoUrl = videoResolutionGroup.getVideoUrl();
				String resolution = videoResolutionGroup.getResolution();
				updateVideoSize(resolution);
				StringBuffer key = new StringBuffer();
				if(codec != null){
					key.append(codec.trim());
				}
				if(resolution != null){
					this.resolution = resolution.trim();
					key.append("-").append(resolution.trim());
				}
				this.codecResolution = key.toString();
				this.codec = codec;
			}
			
		}
		
		Log.d(TAG, "playVideo - type:" + type);
		Log.d(TAG, "playVideo - newVideoUrl: " + newVideoUrl);
		
		if(vp9Player != null){
			vp9Player.release();
			cancelTask();
		}
		
		if(activity != null){
			activity.runOnUiThread(new Runnable(){

				@Override
				public void run() {
					if(popupVideoWindow != null && popupVideoWindow.isShowing()){
						popupVideoWindow.dismiss();
					}
					
					if(settingPopup != null && settingPopup.isShowing()){
						settingPopup.dismiss();
					}
				}
				
			});
		}
		
		if(isH265){
			type = 0;
		}
		
		if(type == 1){
			vp9Player = new H265Vp9Player(this);
			vp9Player.playVideo(newVideoUrl, false);
		}else{
			vp9Player = new NativeVp9Player(this);
			vp9Player.playVideo(newVideoUrl, false);
		}
		if(settingPopup != null){
			settingPopup.dismiss();
		}
		
		createPopupMenu(subInfoArr, videoResolutionGroups, resolutionIndex, type);
		
	}

	private int getIntResolution(String strResolution) {
		int intResolution = 0;
		if(strResolution != null){
			String[] strs = strResolution.split("x");
 			if(strs != null && strs.length > 1){
				int param1 = Vp9ParamUtil.getValue(strs[0], 0);
				int param2 = Vp9ParamUtil.getValue(strs[1], 0);
				intResolution = param1*param2;
			}
		}
		return intResolution;
	}

	public void setMaxForSeekBar(final SeekBar seekBar, final int progress) {
		if (clearParam) {
			return;
		}
		if (activity != null && seekBar != null) {
			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					seekBar.setMax(progress);
				}
			});
		}
	}

	public void setEnabled(final View view, final boolean isEnabled) {
		if (activity != null && view != null) {
			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					view.setEnabled(isEnabled);
					view.requestLayout();
				}
			});
		}
	}

	public void initParam() {
		isError = false;
		currentError = 0l;
		isResume = false;
		isSeek = false;
		isResume = false;
		// childVideoIndex = 0;
		setTextForTextView();
	}

	public void reset() {
		isPlay = false;
		isError = false;
		currentError = 0l;
		isResume = false;
		isSeek = false;
		isResume = false;
		videoSeekTime = 0;
		videoSeekTime = 0;
		this.mVideoWidth = 0;
		this.mVideoHeight = 0;
//		this.mScreenWidth = 0;
//		this.mScreenHeight = 0;
		this.videoSeekTime = 0;
		subInfoArr = null;
		checkboxSubtitle = null;
		playingVideo = null;
		this.videoIndex = -1;
		this.timeShowIndex = 0;
		this.state = 0;
		// demandTiviSchedule = null;
		// childVideoIndex = 0;
		if (sbFull != null) {
			setProgressForSeekBar(sbFull, 0);
			setMaxForSeekBar(sbFull, 1000);
			setSecondaryProgress(sbFull, 0);
		}
		
		if (sbFull2 != null) {
			setProgressForSeekBar(sbFull2, 0);
			setMaxForSeekBar(sbFull2, 1000);
			setSecondaryProgress(sbFull2, 0);
		}
		
		setTextForTextView();
		setVisibility(logoVideo, View.GONE);
		setVisibility(videoTitle, View.GONE);
		if(is3D){
			setVisibility(logoVideo2, View.GONE);	
			setVisibility(videoTitle2, View.GONE);
		}
	}

	public void setFullScreen() {
		
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (activity != null && mVideoView != null) {
					activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
					activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
					mVideoView.setKeepScreenOn(true);
				}
			}
		});
		
	}

	public void setVisibility(final View view, final int type) {
		if (activity != null){
			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (view != null && view.getVisibility() != type) {
						view.setVisibility(type);
						view.invalidate();
						view.requestLayout();
					}
				}
			});
		}
	}

	public void playSub(SubtitleInfo[] subtitleInfos) {
		if (clearParam) {
			return;
		}
		if (subtitleInfos != null && subtitleInfos.length > 0) {
			this.loadSub = new LoadViewTask(this, subtitleInfos, Boolean.valueOf(false));
			this.loadSub.execute(new Void[0]);
//			setBackgroundResource(btnSub, vp9_btn_sub_id);
		} else {
//			setBackgroundResource(btnSub, vp9_btn_sub_hide_id);
			if (this.loadSub != null) {
				this.loadSub.cancelTask();
			}
		}
	}

	public void createPopupMenu(SubtitleInfo[] subtitleInfos, ArrayList<VideoResolutionGroup> videoResolutionGroups, int resolutionIndex, int type) {
		settingPopup = null;
		if (clearParam || (subtitleInfos == null && (videoResolutionGroups == null || videoResolutionGroups.size() == 0) && !(videoType == 1 && intProxy == 1))) {
			setBackgroundResource(btnSub, vp9_btn_sub_hide_id);
			setEnabled(btnSub, false);
			if(is3D){
				setBackgroundResource(btnSub2, vp9_btn_sub_hide_id);
				setEnabled(btnSub2, false);
			}
			return;
		}
		setBackgroundResource(btnSub, vp9_btn_sub_id);
		setEnabled(btnSub, true);
		if(is3D){
			setBackgroundResource(btnSub2, vp9_btn_sub_id);
			setEnabled(btnSub2, true);
		}
		
		createSettingPopup(subtitleInfos, videoResolutionGroups, resolutionIndex, type);
		
		

	}

	public CheckBox[] checkboxSubtitle;


	public void createSettingPopup(final SubtitleInfo[] subtitleInfos, final ArrayList<VideoResolutionGroup> videoResolutionGroups, final int resolutionIndex, final int type) {

		activity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				if(activity == null){
					return;
				}
				LayoutInflater systemService = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				LinearLayout layoutOfPopup = (LinearLayout) systemService.inflate(R.layout.settings, null);
				final float scale = activity.getResources().getDisplayMetrics().density;
				settingPopup = new PopupWindow();
				settingPopup.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.WHITE));
				
				LinearLayout layoutProxySpeed = (LinearLayout) layoutOfPopup.findViewById(R.id.layoutProxySpeed);
				TextView tvProxySpeed = (TextView) layoutOfPopup.findViewById(R.id.tvProxySpeed);
				
				if(videoType == 1 && intProxy == 1){
					addComponentForProxySpeedLayout(layoutProxySpeed, tvProxySpeed);
				}else{
					setVisibility(tvProxySpeed, View.GONE);
				}
				LinearLayout layoutSubtitle = (LinearLayout) layoutOfPopup.findViewById(R.id.layoutSubtitle);
				setVisibility(layoutSubtitle, View.GONE);
				LinearLayout layoutResolution = (LinearLayout) layoutOfPopup.findViewById(R.id.layoutResolution);
				TextView tvResolution = (TextView) layoutOfPopup.findViewById(R.id.tvResolution);
			
				

				if (videoResolutionGroups != null && videoResolutionGroups.size() > 0) {
					
				
					setVisibility(tvResolution, View.VISIBLE);
					
			//		LinearLayout layoutResolution = new LinearLayout(activity);
			//		LayoutParams resolutionParams = new LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
			//		layoutResolution.setLayoutParams(resolutionParams);
					
			//		TextView tvResolution = new TextView(activity);
			//		tvResolution.setText("�? ph�n gi?i");
			//		layoutResolution.addView(tvResolution);
					
					RadioGroup radioGroup = new RadioGroup(getActivity());
					
					for(int i = 0; i < videoResolutionGroups.size(); i++){
						VideoResolutionGroup videoResolutionGroup = videoResolutionGroups.get(i);
						String resolutionName = getResolutionName(videoResolutionGroup);
						String codec = videoResolutionGroup.getCodec();
						Log.d(TAG, "ResolutionName: " + resolutionName + "/" + codec);
						RadioButton radioButton = new RadioButton(getActivity());
//						radioButton.setText(resolutionName);
						radioButton.setTextSize(35);
						
						if("h264".equals(codec)){
							radioButton.setTextColor(Color.BLUE);
						}else{
							radioButton.setTextColor(Color.BLACK);
						}
						
						radioButton.setTag(i);
						radioButton.setPadding(radioButton.getPaddingLeft() + (int)(10.0f * scale + 0.5f),
								radioButton.getPaddingTop(),
								radioButton.getPaddingRight(),
								radioButton.getPaddingBottom());
						
						radioGroup.addView(radioButton);
						Log.d(TAG, "Resolution - Index: " + resolutionIndex + " = " + i);
//						if(resolutionIndex == i){
//							Log.d(TAG, "Resolution - choise");
//							radioButton.setChecked(true);
//						}
						if(resolutionIndex == i){
							Log.d(TAG, "Resolutionchoise - type: " + type);
							if(type == 0){
								radioButton.setText(resolutionName + " - HW");
							}else{
								radioButton.setText(resolutionName + " - SW");
							}
							
							radioButton.setChecked(true);
						}else{
							radioButton.setText(resolutionName);
							radioButton.setChecked(false);
						}
					}
					radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() 
				        {
				            public void onCheckedChanged(RadioGroup group, int checkedId) {
			//	            	group.indexOfChild(child)
				            	final RadioGroup newGroup = group;
				            	final int newCheckedId = checkedId;
				            	
				        		if(activity != null){
				        			activity.runOnUiThread(new Runnable() {
				        				
				        				@Override
				        				public void run() {
							            	View view = newGroup.findViewById(newCheckedId);
							            	if(view != null){
								            	int index = Integer.valueOf(view.getTag().toString());
								            	settingPopup.dismiss();
								            	handleResolutionRadioButton(index);
							            	}
				        				}
				        			});
				        		}
//				            	View view = group.findViewById(checkedId);
//				            	if(view != null){
//					            	int index = Integer.valueOf(view.getTag().toString());
//					            	settingPopup.dismiss();
//					            	handleResolutionRadioButton(index);
//				            	}
				            }
				        });
					 
					layoutResolution.addView(radioGroup);
				}else{
					setVisibility(tvResolution, View.GONE);
				}
				settingPopup.setFocusable(true);

				settingPopup.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
				settingPopup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
				settingPopup.setContentView(layoutOfPopup);
				settingPopup.setOnDismissListener(new DismissPopupWindowListener(3));
				
//				float viewScale;
//				if(RotationMediaController.this.is3D){
//					viewScale = 0.5f;
//				}else{
//					viewScale = 1f;
//				}
				
				if(RotationMediaController.this.is3D){
					settingPopup.setWidth(settingPopup.getWidth());
				}
				
				
//				RotationMediaController.this.setScaleX(layoutSubtitle, viewScale, 0);
//				RotationMediaController.this.setScaleX(layoutResolution, viewScale, 0);
			}
		});


	}

	protected void addComponentForProxySpeedLayout(
			LinearLayout layoutProxySpeed, final TextView tvProxySpeed) {

		setVisibility(tvProxySpeed, View.VISIBLE);
		
		tvProxySpeed.setFocusable(true);

//		setTextForTextView(tvProxySpeed, "Tốc độ tải video: None  ", 0);
		String[] displayTexts = {"Hiện tại  ", "Trung bình", "Cả kênh   ", "None      "};
		
		setTextForTextView(tvProxySpeed, "Tốc độ: " + displayTexts[intProxySpeedDisplay], 0);
		
		tvProxySpeed.setOnFocusChangeListener(new TextView.OnFocusChangeListener(){

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				
				if(hasFocus){
					tvProxySpeed.setTextColor(Color.BLUE);
				}else{
					tvProxySpeed.setTextColor(Color.BLACK);
				}
				
			}
			
		});
		
		tvProxySpeed.setOnKeyListener(new TextView.OnKeyListener(){
            
			private String[] displayTexts = {"Hiện tại  ", "Trung bình", "Cả kênh   ", "None      "};
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				
				boolean isSucess = false;
				int vp9KeyCode = Vp9KeyEvent.getKeyCode(keyCode);
				
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					switch (vp9KeyCode) {
					
					case Vp9KeyEvent.KEYCODE_DPAD_LEFT:
						intProxySpeedDisplay = (intProxySpeedDisplay + 3)%4;
						setTextForTextView(tvProxySpeed, "Tốc độ: " + displayTexts[intProxySpeedDisplay], 0);
						com.vp9.util.AppPreferences.INSTANCE.saveIntProxySpeedDisplay(intProxySpeedDisplay);
						if(intProxySpeedDisplay == 3){
							emptyTextView(notifyTextView);
							if(is3D){
								emptyTextView(notifyTextView2);
							}
						}
						isSucess = true;
						break;

					case Vp9KeyEvent.KEYCODE_DPAD_RIGHT:
						
						intProxySpeedDisplay = (intProxySpeedDisplay + 1)%4;
						setTextForTextView(tvProxySpeed, "Tốc độ: " + displayTexts[intProxySpeedDisplay], 0);
						com.vp9.util.AppPreferences.INSTANCE.saveIntProxySpeedDisplay(intProxySpeedDisplay);
						if(intProxySpeedDisplay == 3){
							emptyTextView(notifyTextView);
							if(is3D){
								emptyTextView(notifyTextView2);
							}
						}
						isSucess = true;
						break;

					default:
						break;
					}
				}
				

				return isSucess;
			}
			
		});
		
		
//		layoutProxySpeed.setOrientation(LinearLayout.VERTICAL);
//		
//		final float scale = activity.getResources().getDisplayMetrics().density;
//        
//		RadioGroup radioGroup = new RadioGroup(getActivity());
//		
//		final RadioButton[] rdProxySpeeds = new RadioButton[2];
//		
//		rdProxySpeeds[0] = new RadioButton(getActivity());
//		rdProxySpeeds[0].setTextSize(35);
//		rdProxySpeeds[0].setTag(0);
//		rdProxySpeeds[0].setPadding(rdProxySpeeds[0].getPaddingLeft() + (int)(10.0f * scale + 0.5f),
//				rdProxySpeeds[0].getPaddingTop(),
//				rdProxySpeeds[0].getPaddingRight(),
//				rdProxySpeeds[0].getPaddingBottom());
//		rdProxySpeeds[0].setText("Hiển thị tốc độ");
//		radioGroup.addView(rdProxySpeeds[0]);
//		
//		rdProxySpeeds[1] = new RadioButton(getActivity());
//		rdProxySpeeds[1].setTextSize(35);
//		rdProxySpeeds[1].setTag(1);
//		rdProxySpeeds[1].setPadding(rdProxySpeeds[1].getPaddingLeft() + (int)(10.0f * scale + 0.5f),
//				rdProxySpeeds[1].getPaddingTop(),
//				rdProxySpeeds[1].getPaddingRight(),
//				rdProxySpeeds[1].getPaddingBottom());
//		
//		rdProxySpeeds[1].setText("Không hiển thị tốc độ");
//		for(int i = 0; i < 2; i++){
//			if(i == intProxySpeedDisplay){
//				rdProxySpeeds[i].setChecked(true);
//				rdProxySpeeds[i].setTextColor(Color.BLUE);
//			}else{
//				rdProxySpeeds[i].setChecked(false);
//				rdProxySpeeds[i].setTextColor(Color.BLACK);
//			}
//		}
//		radioGroup.addView(rdProxySpeeds[1]);
//		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() 
//	        {
//	            public void onCheckedChanged(RadioGroup group, int checkedId) {
//	            	final RadioGroup newGroup = group;
//	            	final int newCheckedId = checkedId;
//	            	
//	        		if(activity != null){
//	        			activity.runOnUiThread(new Runnable() {
//	        				
//	        				@Override
//	        				public void run() {
//				            	View view = newGroup.findViewById(newCheckedId);
//				            	if(view != null){
//					            	intProxySpeedDisplay = Integer.valueOf(view.getTag().toString());
//					            	com.vp9.util.AppPreferences.INSTANCE.saveIntProxySpeedDisplay(intProxySpeedDisplay);
//					            	for(int i = 0; i < 2; i++){
//					            		if(i == intProxySpeedDisplay){
//					            			rdProxySpeeds[i].setTextColor(Color.BLUE);
//					            		}else{
//					            			rdProxySpeeds[i].setTextColor(Color.BLACK);
//					            		}
//					            	}
//					            	
//					            	if(intProxySpeedDisplay == 1){
//					            		emptyTextView(notifyTextView);
//					            		if(is3D){
//					            			emptyTextView(notifyTextView2);
//					            		}
//					            	}
//				            	}
//	        				}
//	        			});
//	        		}
//	            }
//	        });
//		 
//		layoutProxySpeed.addView(radioGroup);
	}

	protected void handleResolutionRadioButton(int checkedId) {
		Log.d(TAG, "handleResolutionRadioButton: " + checkedId);
		Log.d(TAG, "handleResolutionRadioButton - size: " + this.videoResolutions.size());
		if(this.videoResolutions != null && checkedId < this.videoResolutions.size() && checkedId >= 0){
			if (!isDisplayChannelImage && intShowControl != 2) {
				Log.d(TAG, "loading 1: " + true);
				setVisibility(loadingLayout, View.VISIBLE);
				if(is3D){
					setVisibility(loadingLayout2, View.VISIBLE);
				}
			} else {
				Log.d(TAG, "loading 1: " + false);
				setVisibility(loadingLayout, View.GONE);
				if(is3D){
					setVisibility(loadingLayout2, View.GONE);
				}
			}
			VideoResolutionGroup videoResolutionGroup = this.videoResolutions.get(checkedId);
			String codec = videoResolutionGroup.getCodec();
			int type;
			if("h265".equals(codec)){
				type = 1;
			}else{
				type = 0;
			}
			String newVideoUrl = videoResolutionGroup.getVideoUrl();
			Log.d(TAG, "handleResolutionRadioButton - type: " + type);
			Log.d(TAG, "handleResolutionRadioButton - VideoUrl: " + newVideoUrl);

			if(vp9Player != null){
				vp9Player.release();
				cancelTask();
			}
			StringBuffer key = new StringBuffer();
			String resolution = videoResolutionGroup.getResolution();
			if(codec != null){
				key.append(codec.trim());
			}
			if(resolution != null){
				this.resolution = resolution.trim();
				key.append("-").append(resolution.trim());
			}
			this.codecResolution = key.toString();
			this.codec = codec;
			com.vp9.util.AppPreferences.INSTANCE.saveCodec(codec);
			com.vp9.util.AppPreferences.INSTANCE.saveCodecResolution(codecResolution);
			
			if(this.intH265 == 1){
				type = 0;
			}else if(this.intH265 == 2){
				boolean isDecoder = Vp9MediaCodecInfo.checkDecoder("video/hevc");
				if(isDecoder){
					type = 0;
				}
			}
			
			if(type == 1){
				vp9Player = new H265Vp9Player(this);
				vp9Player.playVideo(newVideoUrl, false);
			}else{
				vp9Player = new NativeVp9Player(this);
				vp9Player.playVideo(newVideoUrl, false);
			}
			int resolutionIndex = videoResolutionGroup.getIndex();
			createPopupMenu(subInfoArr, videoResolutions, resolutionIndex, type);
		}
		
		if(settingPopup != null){
			settingPopup.dismiss();
		}
		
		
	}

	private String getResolutionName(VideoResolutionGroup videoResolutionGroup) {
//		String resolutionName = "";
//		
//		String quality = videoResolutionGroup.getQuality();
//		
//		String resolution = videoResolutionGroup.getResolution();
//		if(resolution != null && !"".equals(resolution.trim())){
//			resolutionName = resolution;
//		}else if(quality != null && !"".equals(quality.trim())){
//			resolutionName = quality;
//		}
//		
//		return resolutionName;
		String resolutionName;
		String resolution = videoResolutionGroup.getResolution();
		if(resolution != null && !"".equals(resolution.trim())){
			resolutionName = resolution;
		}else{
			resolutionName = "";
		}
		
		return resolutionName;
	}

	public class DismissPopupMenuListener implements OnDismissListener {

		@Override
		public void onDismiss() {
			showControlLayout = false;
			if (handleHideController != null) {
				handleHideController.removeCallbacks(hideTask);
			}

			hideController();

		}

	}

	public void sendSubtitlesInfoToRemote() {
		if (clearParam) {
			return;
		}
		JSONArray jsonArrSub = new JSONArray();
		// Menu menu = popupMenu.getMenu();
		if (checkboxSubtitle != null && checkboxSubtitle.length > 0) {
			int menuSize = checkboxSubtitle.length;
			if (subInfoArr != null && subInfoArr.length == menuSize) {
				ArrayList<String> subTypes = new ArrayList<String>();
				try {
					for (int i = 0; i < menuSize; i++) {
						boolean isChoice = checkboxSubtitle[i].isChecked();
						String subType = subInfoArr[i].getSubType();
						subInfoArr[i].setDisplay(isChoice);
						if (isChoice) {
							subTypes.add(subType);
						}
						JSONObject jsonSub = new JSONObject();
						jsonSub.put("subType", subType);
						jsonSub.put("isChoice", isChoice);
						jsonArrSub.put(jsonSub);
					}
					if (activity != null) {
						Vp9ActivityInterface vp9Activity = (Vp9ActivityInterface) activity;
						settingSubTypes = subTypes;
						vp9Activity.saveSubtiles(subTypes);
					}
					// result.put("result", jsonArrSub);
	
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		if (this.isRemoteListener) {
			try {
				JSONObject jsonEvent = new JSONObject();
				jsonEvent.put("action", "changeSubtitle");
				jsonEvent.put("subtitle", jsonArrSub);
				Vp9ActivityInterface vp9Activity = (Vp9ActivityInterface) activity;
				vp9Activity.sendEvent(jsonEvent);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	public void showController(int hide) {
		if (clearParam) {
			return;
		}
		if (this.intShowControl == 0) {
			if (this.handleHideController != null) {
				this.handleHideController.removeCallbacks(this.hideTask);
			}
			this.intShowControl = 1;
			setVisibility(controllerLayout, View.VISIBLE);
			if(is3D){
				setVisibility(controllerLayout2, View.VISIBLE);
			}
			if (hide == 1) {
				hideController();
			}

//			setVisibility(controllerLayout, View.VISIBLE);

			if (intShowControl != 2) {
				if (!isDisplayChannelImage) {
					if (videoType == 0 || (videoType > 1 && isLive == 0)) {
						setVisibility(progessLayout, View.VISIBLE);
						if(is3D){
							setVisibility(progessLayout2, View.VISIBLE);
						}
					}
				}
			}
		}

	}

	public void handleClickPlayAndPause() {
		if (clearParam) {
			return;
		}
		if (vp9Player == null) {
			return;
		}
		if (!vp9Player.isPlaying()) {
			if (activity != null) {
				Vp9ActivityInterface vp9Activity = (Vp9ActivityInterface) activity;
				vp9Activity.closeEPG();
			}
			if (intShowControl == 0) {
				showController(1);
			}
			vp9Player.startVideo();

		} else {
			// isLive = 0;
			if (intShowControl == 0) {
				showController(1);
			}
			vp9Player.pause();
			if(startTimeError != 0){
				sendErrorMsgToServer();
			}
		}

	}

	public void cancelTask() {
		try {
			state = 0;
			// mVideoView.release();
			Log.e(TAG, "cancelTask 1");

//			if (this.showLeftDisplayHandle != null) {
//				this.showLeftDisplayHandle.removeCallbacks(this.showLeftDisplayT);
//			}
			
			if (this.updateTimehandle != null) {
				this.updateTimehandle.removeCallbacks(this.mUpdateTimeTask);
			}

			if (this.mResumErrorHandle != null) {
				this.mResumErrorHandle.removeCallbacks(this.mResumErrorVideoTask);
			}
			
			if(proxySpeedHandler != null){
				this.proxySpeedHandler.removeCallbacks(this.mProxySpeedTask);
			}

			if (this.mResumHandle != null) {
				this.mResumHandle.removeCallbacks(this.mResumVideoTask);
			}

			if (this.handleHideController != null) {
				this.handleHideController.removeCallbacks(this.hideTask);
			}

			if (this.mCheckPlayDemandVideoHandle1 != null) {
				this.mCheckPlayDemandVideoHandle1.removeCallbacks(this.mCheckPlayVideoTask1);
			}

			if (this.mCheckPlayDemandVideoHandle2 != null) {
				this.mCheckPlayDemandVideoHandle2.removeCallbacks(this.mCheckPlayVideoTask2);
			}


			if (this.loadSub != null) {
				((LoadViewTask) this.loadSub).cancelTask();
				((LoadViewTask) this.loadSub).cancel(true);
			}

			if (this.loadLeftLogoTask != null) {
				((LoadLeftLogoTask) this.loadLeftLogoTask).cancelTask();
				((LoadLeftLogoTask) this.loadLeftLogoTask).cancel(true);
			}
			
			if (this.loadRightLogoTask != null) {
				((LoadRightLogoTask) this.loadRightLogoTask).cancelTask();
				((LoadRightLogoTask) this.loadRightLogoTask).cancel(true);
			}
			// if (mVideoView != null) {
			// stopPlayback(mVideoView);
			// // mVideoView.stopPlayback();
			// }
			// clearFocus(mVideoView);
			Log.e(TAG, "cancelTask 2");
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.e(TAG, "cancelTask 3");
	}


	public void showPopupMenu(View view) {
		if (clearParam) {
			return;
		}

		if (settingPopup != null) {
			if (activity != null) {
				Vp9ActivityInterface vp9Activity = (Vp9ActivityInterface) activity;
				vp9Activity.closeEPG();
			}
			showControlLayout = true;
			// settingPopup.show();
			Log.d(TAG, "show settingPopup");
//			settingPopup.showAsDropDown(view, -5, 0);
			if(is3D){
				settingPopup.showAsDropDown(btnSub2, -5, 0);
			}else{
				settingPopup.showAsDropDown(btnSub, -5, 0);
			}
		}
	}

	public boolean onTouch(View view, MotionEvent motionEvent) {
		if (clearParam) {
			return false;
		}
		int action = motionEvent.getAction();
		Log.d(TAG, "onTouch: " + action);
		// if(action == MotionEvent.ACTION_MOVE || action ==
		// MotionEvent.ACTION_HOVER_MOVE || action == MotionEvent.ACTION_DOWN){
		/*if (action == MotionEvent.ACTION_DOWN) {
			if (intShowControl == 0) {
				showController(1);
			} else {
				intShowControl = 0;
				setVisibility(controllerLayout, View.GONE);
			}
		}*/
		// showController();
		// if (handleHideController != null){
		// handleHideController.removeCallbacks(hideTask);
		// }
		// hideController();
		// isTouch = true;
		// }
		return false;

	}


//	public void resume() {
//		if (clearParam) {
//			return;
//		}
//		Log.d(TAG, "resume");
//		count = 0;
//		activity.runOnUiThread(new Runnable() {
//			@Override
//			public void run() {
//				try {
//					vp9Player.playVideo(videoUrl, false);
//				} catch (IllegalStateException e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	// public void updateVideoSize(int videoWidth, int videoHeight) {
	// if(clearParam){
	// return;
	// }
	// // Get the dimensions of the video
	//
	// // int videoWidth = player.getVideoWidth();
	// // int videoHeight = player.getVideoHeight();
	// if(videoWidth == 0 || videoHeight == 0){
	// return;
	// }
	// float videoProportion = (float) videoWidth / (float) videoHeight;
	// Log.i(TAG, "VIDEO SIZES: W: " + videoWidth + " H: " + videoHeight +
	// " PROP: " + videoProportion);
	//
	// // Get the width of the screen
	// int screenWidth =
	// activity.getWindowManager().getDefaultDisplay().getWidth();
	// int screenHeight =
	// activity.getWindowManager().getDefaultDisplay().getHeight();
	// float screenProportion = (float) screenWidth / (float) screenHeight;
	// Log.i(TAG, "SCREEN SIZES: W: " + screenWidth + " H: " + screenHeight +
	// " PROP: " + screenProportion);
	// Log.i(TAG, "intFullScreen: " + intFullScreen);
	// // Get the SurfaceView layout parameters
	// final android.view.ViewGroup.LayoutParams lp =
	// mVideoView.getLayoutParams();
	// if(intFullScreen == 2){
	// lp.width = screenWidth;
	// lp.height = screenHeight;
	// }else if(intFullScreen == 0){
	// if (videoProportion > screenProportion) {
	// lp.width = screenWidth;
	// lp.height = (int) ((float) screenWidth / videoProportion);
	// } else {
	// lp.width = (int) (videoProportion * (float) screenHeight);
	// lp.height = screenHeight;
	// }
	// }else if(intFullScreen == 1){
	// if (videoProportion > screenProportion) {
	// lp.width = screenWidth;
	// lp.height = (int) ((float) screenWidth / videoProportion);
	// } else {
	// lp.width = videoWidth;
	// lp.height = screenHeight;
	// }
	// }
	//
	// Log.i(TAG, "REAL VIDEO SIZES: W: " + lp.width + " H: " + lp.height);
	// // Commit the layout parameters
	// activity.runOnUiThread(new Runnable() {
	// @Override
	// public void run() {
	// mVideoView.setLayoutParams(lp);
	// }
	// });
	// }

	private void updateVideoSize() {
		if (this.clearParam) {
			return;
		}
		// Get the dimensions of the video

		int videoWidth = vp9Player.getVideoWidth();
		int videoHeight = vp9Player.getVideoHeight();

		// new
		if (this.mVideoWidth != 0) {
			videoWidth = this.mVideoWidth;
		}

		if (this.mVideoWidth != 0) {
			videoHeight = this.mVideoHeight;
		}
		//

		if (videoWidth == 0 || videoHeight == 0) {
			return;
		}
		float videoProportion = (float) videoWidth / (float) videoHeight;
		Log.i(TAG, "VIDEO SIZES: W: " + videoWidth + " H: " + videoHeight + " PROP: " + videoProportion);

		// Get the width of the screen
		int screenWidth = activity.getWindowManager().getDefaultDisplay().getWidth();
		int screenHeight = activity.getWindowManager().getDefaultDisplay().getHeight();
		
		Display display = activity.getWindowManager().getDefaultDisplay();
		Point point = this.getSize(display);
		if (point != null) {
			screenWidth = point.x;
			screenHeight = point.y;
		}
		
		if(this.mScreenWidth != 0){
			screenWidth = this.mScreenWidth;
		}
		
		if(this.mScreenHeight != 0){
			
			screenHeight = this.mScreenHeight;
			
		}

		float screenProportion = (float) screenWidth / (float) screenHeight;
		Log.i(TAG, "SCREEN SIZES: W: " + screenWidth + " H: " + screenHeight + " PROP: " + screenProportion);
		Log.i(TAG, "intFullScreen: " + intFullScreen);
		// Get the SurfaceView layout parameters
		final android.view.ViewGroup.LayoutParams layoutparam = mVideoView.getLayoutParams();
		
		final android.widget.RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(layoutparam);
//		lp.setMargins(0, (screenHeight-lp.height)/2, 0, 0);
		lp.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
		lp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
		lp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
		 
		// if(intFullScreen == 2){
		// lp.width = screenWidth;
		// lp.height = screenHeight;
		// }else
		if (intFullScreen == 0) {
			if (videoProportion > screenProportion) {
				lp.width = screenWidth;
//				lp.height = (int) ((float) screenWidth / videoProportion);
				lp.height = (int) (((float) screenWidth*(float) videoHeight)/(float) videoWidth);
			} else {
//				lp.width = (int) (videoProportion * (float) screenHeight);
				lp.width = (int) (((float) videoWidth * (float) screenHeight)/(float) videoHeight);
				lp.height = screenHeight;
			}
		} else if (intFullScreen == 1) {
			if (videoProportion > screenProportion) {
				lp.width = screenWidth;
//				lp.height = (int) ((float) screenWidth / videoProportion);
				lp.height = (int) (((float) screenWidth*(float) videoHeight)/(float) videoWidth);
			} else {
				// lp.width = videoWidth;
				// lp.height = screenHeight;
				lp.width = screenWidth;
				lp.height = screenHeight;
			}

			if (this.videoType == 1) {
				lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
				lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
				lp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
				lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
				
				
			}
		}
//		lp.width = screenWidth;
//		lp.height = screenHeight;
//		Log.i(TAG, "REAL VIDEO SIZES: W: " + lp.width + " H: " + lp.height);
		lp.width += this.leftStretch + this.rightStretch;
        lp.height += this.topStretch + this.topStretch;
        
//		Log.i(TAG, "AAAAAAAAAAAAA: VIDEO SIZES: W: " + point.x + " H: " + point.y);
		// Commit the layout parameters
        activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (mVideoView != null) {
					mVideoView.setLayoutParams(lp);
					mHolder.setFixedSize(lp.width, lp.height);
				}
			}
		});
	}
	
	public static Point getRealSize(Display display) {
        Point outPoint = new Point();
        Method mGetRawH;
        try {
            mGetRawH = Display.class.getMethod("getRawHeight");
            Method mGetRawW = Display.class.getMethod("getRawWidth");
            outPoint.x = (Integer) mGetRawW.invoke(display);
            outPoint.y = (Integer) mGetRawH.invoke(display);
            return outPoint;
        } catch (Throwable e) {
            return null;
        }
    }

    public static Point getSize(Display display) {
        if (Build.VERSION.SDK_INT >= 17) {
            Point outPoint = new Point();
            DisplayMetrics metrics = new DisplayMetrics();
            display.getMetrics(metrics);
            outPoint.x = metrics.widthPixels;
            outPoint.y = metrics.heightPixels;
            return outPoint;
        }
        if (Build.VERSION.SDK_INT >= 14) {
            Point outPoint = getRealSize(display);
            if (outPoint != null)
                return outPoint;
        }
        Point outPoint = new Point();
        if (Build.VERSION.SDK_INT >= 13) {
            display.getSize(outPoint);
        } else {
            outPoint.x = display.getWidth();
            outPoint.y = display.getHeight();
        }
        return outPoint;
    }

	public void setBackgroundResource(final View view, final int background) {
		try {
			
			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (activity != null && view != null) {
						view.setBackgroundResource(background);
					}
				}
			});
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void hideController() {
		if (clearParam) {
			return;
		}
		if (isPlay && intShowControl != 2) {
			this.handleHideController.postDelayed(this.hideTask, 20000L);
		}

	}

	public boolean showEPG() {
		Log.d(TAG, "showEPG - clearParam: " + clearParam);
		if (clearParam) {
			return false;
		}
		intShowControl = 2;
//		if (updateTimehandle != null) {
//			updateTimehandle.removeCallbacks(mUpdateTimeTask);
//		}
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
//				if (vp9Player != null) {
//					vp9Player.pause();
//				}
				setVisibility(controllerLayout, View.VISIBLE);
				setVisibility(logoLayout, View.GONE);
//				setVisibility(mVideoView, View.GONE);
				setVisibility(vp9ChannelImage, View.GONE);
				Log.d(TAG, "loading 35: " + false);
				setVisibility(loadingLayout, View.GONE);

				setVisibility(subtitlesLayout, View.GONE);
				setVisibility(progessLayout, View.GONE);
				setVisibility(videoTitleLayout, View.GONE);
				if(is3D){
					setVisibility(controllerLayout2, View.VISIBLE);
					setVisibility(logoLayout2, View.GONE);
					setVisibility(vp9ChannelImage2, View.GONE);
					setVisibility(loadingLayout2, View.GONE);
					setVisibility(subtitlesLayout2, View.GONE);
					setVisibility(progessLayout2, View.GONE);
					setVisibility(videoTitleLayout2, View.GONE);
				}
			}
		});

		return true;
	}

	public boolean closeEPG() {
		if (clearParam) {
			return false;
		}
		Log.e(TAG, "closeEPG");
		intShowControl = 1;
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// if (!isError){
				hideController();
//				if (vp9Player != null) {
//					vp9Player.startVideo();
//				}
				if (isDisplayChannelImage) {
					setVisibility(vp9ChannelImage, View.VISIBLE);
					Log.d(TAG, "loading 23: " + false);
					setVisibility(loadingLayout, View.GONE);
					if(is3D){
						setVisibility(loadingLayout2, View.GONE);
						setVisibility(vp9ChannelImage2, View.VISIBLE);
					}
				} else {
					setVisibility(logoLayout, View.GONE);
//					mVideoView.setVisibility(View.VISIBLE);
//					subtitlesLayout.setVisibility(View.VISIBLE);
					setVisibility(subtitlesLayout, View.VISIBLE);
					if(is3D){
						setVisibility(subtitlesLayout2, View.VISIBLE);
						setVisibility(videoTitleLayout2, View.VISIBLE);
						setVisibility(logoLayout2, View.VISIBLE);
					}
					if (videoType == 0 || (videoType > 1 && isLive == 0)) {
						setVisibility(progessLayout, View.VISIBLE);
						if(is3D){
							setVisibility(progessLayout2, View.VISIBLE);
						}
					}
				}
			}
		}); 

		return true;
	}

	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		if (clearParam) {
			return;
		}
		int bfDuration = vp9Player.getDuration();

		final int progress;
		if (bfDuration != 0) {
			progress = percent * bfDuration / 100;
		} else {
			progress = 0;
		}
		if (this.percentBuffer != progress) {
			this.percentBuffer = progress;

			setSecondaryProgress(sbFull, progress);
			
			if(is3D){
				setSecondaryProgress(sbFull2, progress);
			}

		}
	}

	public void setSecondaryProgress(SeekBar sbFull, final int progress) {
		if (clearParam) {
			return;
		}
		if (activity == null) {
			return;
		}
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				try {
					if (RotationMediaController.this.sbFull != null) {
						RotationMediaController.this.sbFull.setSecondaryProgress(progress);
					}
					
					if (is3D && RotationMediaController.this.sbFull2 != null) {
						RotationMediaController.this.sbFull2.setSecondaryProgress(progress);
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	public void setSeekFilmFullEvent() {

		this.sbFull.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			// public int progress;

			public void onProgressChanged(SeekBar paramSeekBar, int paramInt, boolean paramBoolean) {
			}

			public void onStartTrackingTouch(SeekBar paramSeekBar) {
				if (clearParam) {
					return;
				}
				// isLive = 0;
				if (updateTimehandle != null) {
					// this.progress = sbFull.getProgress();
					updateTimehandle.removeCallbacks(mUpdateTimeTask);
				}

			}

			public void onStopTrackingTouch(SeekBar paramSeekBar) {
				if (clearParam) {
					return;
				}
				isLive = 0;
				if (updateTimehandle != null) {
					updateTimehandle.removeCallbacks(mUpdateTimeTask);
				}

				// if(isError){
				// sbFull.setProgress(this.progress);
				// return;
				// }

				int duration = vp9Player.getDuration();
				Log.d(TAG, "----- Touch SeekBar - duration: " + duration);
				duration = Utilities.progressToTimer(paramSeekBar.getProgress(), duration);
				Log.d(TAG, "----- Touch SeekBar - seek To: " + paramSeekBar.getProgress());
				if (isPlay) {
					// seekTo(duration);
					vp9Player.seekTo(paramSeekBar.getProgress());
				}

				// isSeek = true;
				// updateProgressBar();
			}
		});
	}

	public void setChanelTiviImage() {
		if (clearParam) {
			return;
		}
		// controllerView =
		// (RelativeLayout)activity.findViewById(controller_id);
		// RelativeLayout vp9PlayerLayout =
		// (RelativeLayout)activity.findViewById(vp9_player_layout_id);
		this.isDisplayChannelImage = true;
		setVisibility(mVideoView, View.GONE);
		if (intShowControl != 2) {
			setVisibility(vp9ChannelImage, View.VISIBLE);
			if(is3D){
				setVisibility(vp9ChannelImage2, View.VISIBLE);	
			}
		} else {
			setVisibility(vp9ChannelImage, View.GONE);
			if(is3D){
				setVisibility(vp9ChannelImage2, View.GONE);	
			}
		}
//		if(isLoading){
//			isLoading = false;
//			setVisibility(loadingLayout, View.GONE);
//		}
		setVisibility(loadingLayout, View.GONE);
		setVisibility((RelativeLayout) activity.findViewById(loading_layout_id), View.GONE);
		setVisibility((RelativeLayout) activity.findViewById(subtitles_layout_id), View.GONE);
		if(is3D){
			setVisibility(loadingLayout2, View.GONE);
			setVisibility((RelativeLayout) activity.findViewById(loading_layout_id2), View.GONE);
			setVisibility((RelativeLayout) activity.findViewById(subtitles_layout_id2), View.GONE);
		}
	}

	public void invisibleChanelTiviImage() {
		if (clearParam) {
			return;
		}
		this.isDisplayChannelImage = false;
		setVisibility(vp9ChannelImage, View.GONE);
		if(is3D){
			setVisibility(vp9ChannelImage2, View.GONE);
		}
		if (intShowControl != 2) {
			setVisibility(mVideoView, View.VISIBLE);
			setVisibility((RelativeLayout) activity.findViewById(loading_layout_id), View.VISIBLE);
			setVisibility((RelativeLayout) activity.findViewById(subtitles_layout_id), View.VISIBLE);
			if(is3D){
				setVisibility((RelativeLayout) activity.findViewById(loading_layout_id2), View.VISIBLE);
				setVisibility((RelativeLayout) activity.findViewById(subtitles_layout_id2), View.VISIBLE);
			}
		} else {
			setVisibility(mVideoView, View.GONE);
			setVisibility((RelativeLayout) activity.findViewById(loading_layout_id), View.GONE);
			setVisibility((RelativeLayout) activity.findViewById(subtitles_layout_id), View.GONE);
			if(is3D){
				setVisibility((RelativeLayout) activity.findViewById(loading_layout_id2), View.GONE);
				setVisibility((RelativeLayout) activity.findViewById(subtitles_layout_id2), View.GONE);
			}
		}
	}



	// /////////////////////// RUNNABLE /////////////////////////

	public Runnable mCheckPlayVideoTask1 = new Runnable() {
		public void run() {
			if (clearParam) {
				return;
			}
			if (isLive == 1) {
//				ServerTimeInfo serverTimeInfo = new ServerTimeInfo(serverTimeUrl);
				serverTimeInfo.updateTime();
				int secondInDay = serverTimeInfo.getSecondInDay() + 1;
				int flag = demandTiviSchedule.checkTime(serverTimeInfo);
				if (flag == 1) {
					String cheduleUrl = getCheduleUrl(serverUrl, channelId, serverTimeInfo.getStrdate());
					demandTiviSchedule.parserCheduleUrl1(cheduleUrl);
					demandTiviSchedule.setStrdate(serverTimeInfo.getStrdate());
				}
				VideoResult videoResult = demandTiviSchedule.getVideoInfoByTime(secondInDay);

				if (videoResult == null || videoResult.getVideoInfo() == null || videoResult.getVideoInfo().getIndex() == demandTiviSchedule.getCurrentIndex()) {
					Log.d(TAG, "Play Demand Tivi 1 - Video is not found or not correct start time");
					Log.d(TAG, "loading 24: " + false);
//					if(isLoading){
//						isLoading = false;
//						setVisibility(loadingLayout, View.GONE);
//					}
					setVisibility(loadingLayout, View.GONE);
					if(is3D){
						setVisibility(loadingLayout2, View.GONE);
					}
					if (!isDisplayChannelImage) {
						setChanelTiviImage();
					}
					Log.d(TAG, "Play Demand Tivi 24: " + isPlay);
					long waittingTime = 1000L;
					if (videoResult != null && videoResult.getNextIndex() != -1) {
						waittingTime = demandTiviSchedule.getWaitingTime(serverTimeInfo, videoResult.getNextIndex(), 1000);
					}
					Log.d(TAG, "Play Demand Tivi 25: " + waittingTime);
//					if (isPlay) {
//						mCheckPlayDemandVideoHandle1.postDelayed(mCheckPlayVideoTask1, waittingTime);
//					}
					mCheckPlayDemandVideoHandle1.removeCallbacks(mCheckPlayVideoTask1);
					mCheckPlayDemandVideoHandle1.postDelayed(mCheckPlayVideoTask1, waittingTime);
				} else {

					VideoInfo videoInfo = videoResult.getVideoInfo();
					startDemandTivi(videoInfo, secondInDay - videoInfo.getIntStartTimeBySeconds(), isLive);
					videoIndex = videoInfo.getIndex();
					// if(isDisplayChannelImage){
					// invisibleChanelTiviImage();
					// }
				}

			} else {
				int curIndex = demandTiviSchedule.getCurrentIndex();
				VideoResult videoResult = demandTiviSchedule.getVideoInfoByIndex(curIndex + 1);
				if (videoResult != null && videoResult.getVideoInfo() != null) {
					VideoInfo videoInfo = videoResult.getVideoInfo();

					startDemandTivi(videoInfo, 0, isLive);
					videoIndex = videoInfo.getIndex();
					// if(isDisplayChannelImage){
					// invisibleChanelTiviImage();
					// }
				} else {
//					ServerTimeInfo serverTimeInfo = new ServerTimeInfo(serverTimeUrl);
					serverTimeInfo.updateTime();
					Calendar calendar = getCalendar(strDate);
					calendar.add(Calendar.DATE, 1);
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
					String strTomorow = dateFormat.format(calendar.getTime());
					String cheduleUrl = getCheduleUrl(serverUrl, channelId, strTomorow);
					demandTiviSchedule.parserCheduleUrl1(cheduleUrl);
					demandTiviSchedule.setStrdate(serverTimeInfo.getStrdate());
					// videoIndex = 0;
					int index = 0;
					// VideoResult videoRes =
					// demandTiviSchedule.getVideoInfoByIndex(videoIndex);
					VideoResult videoRes = demandTiviSchedule.getVideoInfoByIndex(index);
					if (videoRes == null || videoRes.getVideoInfo() == null) {
//						if(isLoading){
//							isLoading = false;
//							setVisibility(loadingLayout, View.GONE);
//						}
						setVisibility(loadingLayout, View.GONE);
						if(is3D){
							setVisibility(loadingLayout2, View.GONE);
						}
						if (!isDisplayChannelImage) {
							setChanelTiviImage();
						}

						if (videoResult != null && videoResult.getNextIndex() != -1) {
							long waittingTime = demandTiviSchedule.getWaitingTime(serverTimeInfo, videoResult.getNextIndex(), 1000);
							mCheckPlayDemandVideoHandle1.postDelayed(mCheckPlayVideoTask1, waittingTime);
						} else {
							mCheckPlayDemandVideoHandle1.postDelayed(mCheckPlayVideoTask1, 1000);
						}
						// mCheckPlayDemandVideoHandle1.removeCallbacks(mCheckPlayVideoTask1);
						// mCheckPlayDemandVideoHandle1.postDelayed(mCheckPlayVideoTask1,
						// 1000L);
					} else {
						startDemandTivi(videoRes.getVideoInfo(), 0, isLive);
						videoIndex = videoRes.getVideoInfo().getIndex();
						// if(isDisplayChannelImage){
						// invisibleChanelTiviImage();
						// }
					}
				}
			}
		}

	};

	public Runnable mCheckPlayVideoTask2 = new Runnable() {
		public void run() {
			if (clearParam) {
				return;
			}
//			setVisibility(loadingLayout, View.VISIBLE);
			if (!isDisplayChannelImage && intShowControl != 2) {
//				if(!isLoading){
//					isLoading = true;
//					setVisibility(loadingLayout, View.VISIBLE);
//				}
				setVisibility(loadingLayout, View.VISIBLE);
				if(is3D){
					setVisibility(loadingLayout2, View.VISIBLE);
				}
			} else {
//				if(isLoading){
//					isLoading = false;
//					setVisibility(loadingLayout, View.GONE);
//				}
				setVisibility(loadingLayout, View.GONE);
				if(is3D){
					setVisibility(loadingLayout2, View.GONE);
				}
			}
			Log.d(TAG, "mCheckPlayVideoTask - isLive: " + isLive);
			if (isLive == 1) {
//				ServerTimeInfo serverTimeInfo = new ServerTimeInfo(serverTimeUrl);
				serverTimeInfo.updateTime();
				int secondInDay = serverTimeInfo.getSecondInDay() + 1;
				int curIndex = demandTiviSchedule.getCurrentIndex();
				int curChildIndex = demandTiviSchedule.getCurrentChildIndex();
				Log.d(TAG, "mCheckPlayVideoTask2: " + 0);
				if (curVideoResult != null && curVideoResult.getVideoInfo() != null) {
					Log.d(TAG, "mCheckPlayVideoTask2: " + 1);
					VideoInfo curChildVideo = null;
					int childSeekTime = 0;
					if (curChildIndex == -1) {
						curChildVideo = curVideoResult.getVideoInfo().getChildVideoInfoByTime(secondInDay);
						if (curVideoResult.getVideoInfo().isCurrentDay()) {
							childSeekTime = 24 * 60 * 60 + secondInDay - curChildVideo.getIntStartTimeBySeconds();
						} else {
							childSeekTime = secondInDay - curChildVideo.getIntStartTimeBySeconds();
						}
					} else {
						curChildVideo = curVideoResult.getVideoInfo().getChildVideoInfoByIndex(curChildIndex + 1);
					}

					if (curChildVideo != null) {
						Log.d(TAG, "mCheckPlayVideoTask2: " + 2);
						startDemandTivi(curChildVideo, childSeekTime, isLive);
						videoIndex = curVideoResult.getVideoInfo().getIndex();
						childVideoIndex = curChildVideo.getIndex();
						return;
					} else if (!curVideoResult.isCurrentDay()) {
						Log.d(TAG, "mCheckPlayVideoTask2: " + 3);
						curVideoResult = demandTiviSchedule.getVideoInfoByIndex(0);
						if (curVideoResult != null && curVideoResult.getVideoInfo() != null) {
							Log.d(TAG, "mCheckPlayVideoTask2: " + 4);
							VideoInfo curVideoInfo = curVideoResult.getVideoInfo();
							int waittime = curVideoInfo.getIntStartTimeBySeconds() - secondInDay;
							curChildVideo = curVideoInfo.getChildVideoInfoByIndex(0);
							if (waittime <= 180 && curChildVideo != null) {
								Log.d(TAG, "mCheckPlayVideoTask2: " + 5);
								startDemandTivi(curChildVideo, 0, isLive);
								videoIndex = curVideoInfo.getIndex();
								childVideoIndex = curChildVideo.getIndex();
								return;
							} else if (curChildVideo != null) {
								Log.d(TAG, "loading 27: " + false);
//								if(isLoading){
//									isLoading = false;
//									setVisibility(loadingLayout, View.GONE);
//								}
								setVisibility(loadingLayout, View.GONE);
								if(is3D){
									setVisibility(loadingLayout2, View.GONE);
								}
								if (!isDisplayChannelImage) {
									setChanelTiviImage();
								}
								mCheckPlayDemandVideoHandle2.removeCallbacks(mCheckPlayVideoTask2);
								mCheckPlayDemandVideoHandle2.postDelayed(mCheckPlayVideoTask2, waittime);
								return;
							} else {
//								if(isLoading){
//									isLoading = false;
//									setVisibility(loadingLayout, View.GONE);
//								}
								setVisibility(loadingLayout, View.GONE);
								if(is3D){
									setVisibility(loadingLayout2, View.GONE);
								}
								if (!isDisplayChannelImage) {
									setChanelTiviImage();
								}
								mCheckPlayDemandVideoHandle2.removeCallbacks(mCheckPlayVideoTask2);
								mCheckPlayDemandVideoHandle2.postDelayed(mCheckPlayVideoTask2, 1000);
								curVideoResult = null;
								return;
							}
						}
					} else if (demandTiviSchedule.getCurrentIndex() != -1) {
						Log.d(TAG, "mCheckPlayVideoTask - curVideoInfo:5");
						int index = demandTiviSchedule.getCurrentIndex();
						curVideoResult = demandTiviSchedule.getVideoInfoByIndex(index + 1);
						if (curVideoResult != null && curVideoResult.getVideoInfo() != null) {
							VideoInfo curVideoInfo = curVideoResult.getVideoInfo();
							int waittime = curVideoInfo.getIntStartTimeBySeconds() - secondInDay;
							curChildVideo = curVideoInfo.getChildVideoInfoByIndex(0);
							if (waittime <= 180 && curChildVideo != null) {
								Log.d(TAG, "mCheckPlayVideoTask - curVideoInfo:6");
								startDemandTivi(curChildVideo, 0, isLive);
								videoIndex = curVideoInfo.getIndex();
								childVideoIndex = curChildVideo.getIndex();
								return;
							} else if (curChildVideo != null) {
								Log.d(TAG, "mCheckPlayVideoTask - curVideoInfo:7");
//								if(isLoading){
//									isLoading = false;
//									setVisibility(loadingLayout, View.GONE);
//								}
								setVisibility(loadingLayout, View.GONE);
								if(is3D){
									setVisibility(loadingLayout2, View.GONE);
								}
								if (!isDisplayChannelImage) {
									setChanelTiviImage();
								}
								mCheckPlayDemandVideoHandle2.removeCallbacks(mCheckPlayVideoTask2);
								mCheckPlayDemandVideoHandle2.postDelayed(mCheckPlayVideoTask2, waittime);
								return;
							} else {
//								if(isLoading){
//									isLoading = false;
//									setVisibility(loadingLayout, View.GONE);
//								}
								setVisibility(loadingLayout, View.GONE);
								if(is3D){
									setVisibility(loadingLayout2, View.GONE);
								}
								if (!isDisplayChannelImage) {
									setChanelTiviImage();
								}
								mCheckPlayDemandVideoHandle2.removeCallbacks(mCheckPlayVideoTask2);
								mCheckPlayDemandVideoHandle2.postDelayed(mCheckPlayVideoTask2, 1000);
								curVideoResult = null;
								return;
							}
						}

					}

				} else {
					Log.d(TAG, "mCheckPlayVideoTask2: " + 9);
					int flag = demandTiviSchedule.checkTime(serverTimeInfo);
					if (flag == 1) {
						String cheduleUrl = getCheduleUrl(serverUrl, channelId, serverTimeInfo.getStrdate());
						demandTiviSchedule.parserCheduleUrl2(cheduleUrl);
						demandTiviSchedule.setStrdate(serverTimeInfo.getStrdate());

					}
					Log.d(TAG, "mCheckPlayVideoTask2: " + 10 + ": flag = " + flag);
					VideoResult videoResult = demandTiviSchedule.getVideoInfoByTime(secondInDay);
					if (videoResult == null || videoResult.getVideoInfo() == null) {
						Log.d(TAG, "loading 30: " + false);
						// Error
//						if(isLoading){
//							isLoading = false;
//							setVisibility(loadingLayout, View.GONE);
//						}
						setVisibility(loadingLayout, View.GONE);
						if(is3D){
							setVisibility(loadingLayout2, View.GONE);
						}
						if (!isDisplayChannelImage) {
							setChanelTiviImage();
						}
						if (videoResult != null && videoResult.getNextIndex() != -1) {
							Log.d(TAG, "mCheckPlayVideoTask2: " + 12);
							long waittingTime = demandTiviSchedule.getWaitingTime(serverTimeInfo, videoResult.getNextIndex(), 1000);
							mCheckPlayDemandVideoHandle2.removeCallbacks(mCheckPlayVideoTask2);
							mCheckPlayDemandVideoHandle2.postDelayed(mCheckPlayVideoTask2, waittingTime);
						} else {
							Log.d(TAG, "mCheckPlayVideoTask2: " + 13);
							mCheckPlayDemandVideoHandle2.removeCallbacks(mCheckPlayVideoTask2);
							mCheckPlayDemandVideoHandle2.postDelayed(mCheckPlayVideoTask2, 1000);
						}
					} else {
						Log.d(TAG, "mCheckPlayVideoTask2: " + 14);
						VideoInfo videoInfo = videoResult.getVideoInfo();
						Log.d(TAG, "Play Video with index/current: " + videoInfo.getIndex() + "/" + demandTiviSchedule.getCurrentIndex());
						VideoInfo childVideoInfo = videoInfo.getChildVideoInfoByTime(secondInDay);
						if (childVideoInfo != null) {
							Log.d(TAG, "mCheckPlayVideoTask2: " + 15);
							Log.d(TAG, "Play child video: " + childVideoInfo.getIndex());
							startDemandTivi(childVideoInfo, secondInDay - childVideoInfo.getIntStartTimeBySeconds(), isLive);
							videoIndex = videoInfo.getIndex();
							childVideoIndex = childVideoInfo.getIndex();
						} else {
							Log.d(TAG, "loading 31: " + false);
//							if(isLoading){
//								isLoading = false;
//								setVisibility(loadingLayout, View.GONE);
//							}
							setVisibility(loadingLayout, View.GONE);
							if(is3D){
								setVisibility(loadingLayout2, View.GONE);
							}
							if (!isDisplayChannelImage) {
								setChanelTiviImage();
							}
							mCheckPlayDemandVideoHandle2.removeCallbacks(mCheckPlayVideoTask2);
							mCheckPlayDemandVideoHandle2.postDelayed(mCheckPlayVideoTask2, 1000);

						}
					}
				}
			} else {
				int curIndex = demandTiviSchedule.getCurrentIndex();
				VideoResult videoResult = demandTiviSchedule.getVideoInfoByIndex(curIndex);
				VideoInfo childVideo = null;
				if (videoResult != null && videoResult.getVideoInfo() != null) {
					int curChildIndex = demandTiviSchedule.getCurrentChildIndex();
					childVideo = videoResult.getVideoInfo().getChildVideoInfoByIndex(curChildIndex + 1);
				}
				if (childVideo != null) {

					startDemandTivi(childVideo, 0, isLive);
					videoIndex = videoResult.getVideoInfo().getIndex();
					childVideoIndex = childVideo.getIndex();

				} else {
					videoResult = demandTiviSchedule.getVideoInfoByIndex(curIndex + 1);
					int curChildIndex = 0;
					if (videoResult != null && videoResult.getVideoInfo() != null) {
						childVideo = videoResult.getVideoInfo().getChildVideoInfoByIndex(curChildIndex);
						startDemandTivi(childVideo, 0, isLive);
						videoIndex = videoResult.getVideoInfo().getIndex();
						childVideoIndex = childVideo.getIndex();
						// if(isDisplayChannelImage){
						// invisibleChanelTiviImage();
						// }
					} else {
//						ServerTimeInfo serverTimeInfo = new ServerTimeInfo(serverTimeUrl);
						serverTimeInfo.updateTime();
						Calendar calendar = getCalendar(strDate);
						calendar.add(Calendar.DATE, 1);
						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
						String strTomorow = dateFormat.format(calendar.getTime());
						String cheduleUrl = getCheduleUrl(serverUrl, channelId, strTomorow);
						demandTiviSchedule.parserCheduleUrl2(cheduleUrl);
						demandTiviSchedule.setStrdate(serverTimeInfo.getStrdate());
						videoIndex = 0;
						videoResult = demandTiviSchedule.getVideoInfoByIndex(0);
//						if(isLoading){
//							isLoading = false;
//							setVisibility(loadingLayout, View.GONE);
//						}
						setVisibility(loadingLayout, View.GONE);
						if(is3D){
							setVisibility(loadingLayout2, View.GONE);
						}
						if (videoResult == null || videoResult.getVideoInfo() == null) {
							if (!isDisplayChannelImage) {
								setChanelTiviImage();
							}
						} else {
							childVideo = videoResult.getVideoInfo().getChildVideoInfoByIndex(0);
							startDemandTivi(childVideo, 0, isLive);
							videoIndex = videoResult.getVideoInfo().getIndex();
							childVideoIndex = childVideo.getIndex();
						}
					}
				}
			}

		}

	};

	public Runnable hideTask = new Runnable() {
		public void run() {
			
			
			if (clearParam) {
				return;
			}
			
			if (intShowControl == 2 || showControlLayout) {
				return;
			}
			setVisibility(controllerLayout, View.GONE);
			if(is3D){
				setVisibility(controllerLayout2, View.GONE);
			}
			intShowControl = 0;
			isTouch = false;
			try {
				activity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
//						int systemUiVisibility = getActivity().getWindow().getDecorView().getSystemUiVisibility();
//						if (systemUiVisibility == View.SYSTEM_UI_FLAG_VISIBLE) {
//							getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
//						}
						if (popupVideoWindow != null && popupVideoWindow.isShowing()) {
//							menuItemAdapter.serverTimeInfo = null;
							popupVideoWindow.dismiss();
						}
						if (popupVideoWindow2 != null && popupVideoWindow2.isShowing()) {
//							menuItemAdapter.serverTimeInfo = null;
							popupVideoWindow2.dismiss();

						}
						if (settingPopup != null && settingPopup.isShowing()) {
							settingPopup.dismiss();
						}
						
						
					}
				});
				return;
			} catch (Exception localException) {
				localException.printStackTrace();
			}
		}
	};

	public long duration;

	public long curPosition = 0;

	public int count = 0;

	public boolean isFirstPlay = false;
	
//	public boolean isLoading = false;
	
	private boolean isFirstDisconnect = true;
	
	
	private long startTimeError = 0;
	
	private int countError = 0;
	
	private int range = 6;
	
	public Runnable mUpdateTimeTask = new Runnable() {
		public void run() {
			
			
			if (clearParam) {
				return;
			} 
			if (vp9Player.isInPlaybackState()) {
				try {
					long l1 = vp9Player.getCurrentPosition();
					long l2 = vp9Player.getDuration();

					if (l1 == 0) {
						if (currentError > 0 && isPlay && currentError < l2) {
							vp9Player.seekTo((int) currentError);
							return;
						} else if (isResume) {
							// mVideoView.seekTo(0);
						}
					}
					if (curPosition == l1){
						/*waiting*/
						/**/
						
						setVisibility(loadingLayout, View.VISIBLE);
						if(is3D){
							setVisibility(loadingLayout2, View.VISIBLE);
						}
						if(RotationMediaController.this.isFirstPlay){
							count++;
							if(count != 20){
								if (state == 1) {
									updateTimehandle.postDelayed(this, 500L);
								}
								return;
							}else{
								count = 0;
								RotationMediaController.this.isFirstPlay = false;
							}
						}
						
						if(startTimeError == 0){
							startTimeError = System.currentTimeMillis();
							sendStartErrorMsgToServer(startTimeError);
						}
						
						count++;
						errorMsg = "Mạng bị gián đoạn";
						if ((count == 20 && !isFirstDisconnect) || (count == 100 && isFirstDisconnect)) {
							
							
							isFirstDisconnect = false;
							setTextForTextView(notifyTextView, errorMsg, 0);
							if(is3D){
								setTextForTextView(notifyTextView2, errorMsg, 0);
							}
							isRefreshNotify = true;
							showController(0);
							updateTimehandle.removeCallbacks(this);
							vp9Player.pause(); 
							count = 0;
							Thread startThread = new Thread(){
								public void run() {
								isSeek = true;
								if (loadSub != null) {
									loadSub.setSeek(true);
								}
								isError = true;

								if (updateTimehandle != null) { 
									updateTimehandle.removeCallbacks(mUpdateTimeTask);
								}
								vp9Player.resume();
							  }
						   };
						startThread.setName("mUpdateTimeThr_25");
						startThread.start();
							return;
						}

					} else {
						count = 0;
						RotationMediaController.this.isFirstPlay = false;
						if(isRefreshNotify){
							isRefreshNotify = false;
							setTextForTextView(notifyTextView, "", 0);
							if(is3D){
								setTextForTextView(notifyTextView2, "", 0);
							}
						}

						setVisibility(loadingLayout, View.GONE);
						if(is3D){
							setVisibility(loadingLayout2, View.GONE);
						}
						
						
						if(startTimeError != 0){
							sendErrorMsgToServer();
						}
					}

					isFirstDisconnect = true;
					
					curPosition = l1;

					isResume = false;
					// if(l2 != duration){
					duration = l2;
					setTextForTextView(tvTo, Utilities.milliSecondsToTimer(l2), 0);
					// }
					setTextForTextView(tvFrom, Utilities.milliSecondsToTimer(l1), 0);
					int percentage = Utilities.getProgressPercentage(l1, l2);
					setProgressForSeekBar(sbFull, percentage);
					if(is3D){
						setProgressForSeekBar(sbFull2, percentage);
						setTextForTextView(tvTo2, Utilities.milliSecondsToTimer(l2), 0);
						setTextForTextView(tvFrom2, Utilities.milliSecondsToTimer(l1), 0);
					}
					if (l1 > currentError) {
						currentError = l1;
					}
					
					if (state == 1) {
						updateTimehandle.postDelayed(this, 500L);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		}

		
	};
	
	
	public Runnable mProxySpeedTask = new Runnable() {
		public void run() {
			
			
			if (clearParam) {
				return;
			}
			
			if(intProxySpeedDisplay != 3 && proxySevice != null){
//				double durations = MyService.get_durations(intProxySpeedDisplay);
//				double speed = MyService.get_speed(intProxySpeedDisplay);
//				double expectedSpeed = MyService.get_expected_speed(intProxySpeedDisplay);
				
				double durations = proxySevice.get_durations(intProxySpeedDisplay);
				double speed = proxySevice.get_speed(intProxySpeedDisplay);
				double expectedSpeed = proxySevice.get_expected_speed(intProxySpeedDisplay);
				
				double mSpeed = speed / 1000;
				double mExpectedSpeed = expectedSpeed / 1000;
				
				long intTime = Math.round(durations);
				String strTime;
				if(durations >= 1000000){
					intTime = intTime/3600;
					strTime = intTime + "h cuối:";
				}else if(durations >= 1000){
					intTime = intTime/60;
					strTime = intTime + "m cuối:";
				}else{
					strTime = intTime + "s cuối:";
				}
			
				String msg;
				
				if(mSpeed >= 2*mExpectedSpeed){
					msg =  strTime + " Cần " + String.format("%.2f", mExpectedSpeed) + "; Mạng <font color='blue'>" + String.format("%.1f", mSpeed) + "</font> (mbps)";
				}else if(mSpeed >= (12*mExpectedSpeed)/10){
					msg =  strTime + " Cần " + String.format("%.2f", mExpectedSpeed) + "; Mạng <font color='yellow'>" + String.format("%.1f", mSpeed) + "</font> (mbps)";
				}else{
					msg =  strTime + " Cần " + String.format("%.2f", mExpectedSpeed) + "; Mạng <font color='red'>" + String.format("%.1f", mSpeed) + "</font> (mbps)";
				}
				
//				String msg =  strTime + " Cần " + String.format("%.2f", mExpectedSpeed) + "; Mạng: " + String.format("%.1f", mSpeed) + " (mbps)";
				setMessage2(msg);
			}

			
			proxySpeedHandler.postDelayed(this, 1000L);

		}

		
	};

	public int timeShowIndex = 0;


	public Runnable mResumErrorVideoTask = new Runnable() {
		public void run() {
			if (clearParam) {
				return;
			}
			vp9Player.playVideo(videoUrl, false);

		}
	};

	public Runnable mResumVideoTask = new Runnable() {
		public void run() {
			if (clearParam) {
				return;
			}
			if (RotationMediaController.this.vp9Player.getCurrentPosition() <= 1000) {

				if (isPlay && state == 1) {
					mResumHandle.postDelayed(this, 500L);
				}
			} else {
				// mVideoView.seekTo((int)(5000L +
				// indexPause));
				mResumHandle.removeCallbacks(mResumVideoTask);
			}
		}
	};

	public int mSeekWhenPrepared;
	public boolean isRemoteListener = true;
	public int intFullScreen;

	private MyService proxySevice;

	

	public Calendar getCalendar(String strDate) {
		String strYear = strDate.substring(0, 4);
		String strMonth = strDate.substring(4, 6);
		String strDay = strDate.substring(6, 8);
		if (strMonth.startsWith("0")) {
			strMonth.substring(1);
		}

		if (strDay.startsWith("0")) {
			strDay.substring(1);
		}
		Calendar calendar = Calendar.getInstance();
		calendar.set(Integer.valueOf(strYear), Integer.valueOf(strMonth), Integer.valueOf(strDay));
		return calendar;
	}



	protected void sendErrorMsgToServer() {
		long currentTime = System.currentTimeMillis();
		JSONObject jsonEvent = new JSONObject();
		JSONObject objJson = new JSONObject();
		try {
			jsonEvent.put("action", "waiting");
			objJson.put("duration", currentTime - startTimeError);
			objJson.put("videoUrl", videoUrl);
			objJson.put("position", vp9Player.getCurrentPosition());
			objJson.put("startTime", startTimeError);
			objJson.put("endTime", currentTime);
			jsonEvent.put("information", objJson);
			Vp9ActivityInterface vp9Activity = (Vp9ActivityInterface) activity;
			vp9Activity.sendEvent(jsonEvent);
			startTimeError = 0;
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	private void sendStartErrorMsgToServer(long startTimeError) {
		JSONObject jsonEvent = new JSONObject();
		JSONObject objJson = new JSONObject();
		try {
			jsonEvent.put("action", "waiting");
			objJson.put("videoUrl", videoUrl);
			objJson.put("position", vp9Player.getCurrentPosition());
			objJson.put("startTime", startTimeError);
			jsonEvent.put("information", objJson);
			Vp9ActivityInterface vp9Activity = (Vp9ActivityInterface) activity;
			vp9Activity.sendEvent(jsonEvent);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	

	public void setTextForTextView(final TextView textView, final String text, final int type) {
		if (activity != null && textView != null) {
			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if(type == 0){
						textView.setText(text);
					}else{
						textView.setText(Html.fromHtml(text));
					}
					
				}
			});
		}
	}
	
	public void emptyTextView(final TextView textView) {
		if (activity != null && textView != null) {
			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					textView.setText("");
				}
			});
		}
	}

	public void setProgressForSeekBar(final SeekBar seekBar, final int progress) {
		if (activity != null && seekBar != null) {
			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					seekBar.setProgress(progress);
				}
			});
		}
	}

	public void updateProgressBar() {
		if (clearParam) {
			return;
		}
		if (this.updateTimehandle != null) {
			this.updateTimehandle.removeCallbacks(this.mUpdateTimeTask);
		}
		if (state == 1) {
			updateTimehandle.postDelayed(mUpdateTimeTask, 500L);
		}
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}
	
	public int getTime() {
		int currentPosition = 0;
		if (vp9Player != null) {
			currentPosition = vp9Player.getCurrentPosition();
		}
		
		return currentPosition;
		
	}

//	public JSONArray changeSubtitle(ArrayList<ChangeSubtitle> changeSubtitles) {
//		JSONArray jsonArrSub = new JSONArray();
//		// Menu menu = popupMenu.getMenu();
//		if (checkboxSubtitle != null && checkboxSubtitle.length > 0) {
//			int menuSize = checkboxSubtitle.length;
//			if (subInfoArr != null && changeSubtitles != null && subInfoArr.length == menuSize) {
//				for (int i = 0; i < menuSize; i++) {
//					for (ChangeSubtitle changeSubtitle : changeSubtitles) {
//						
//						int languageID = changeSubtitle.getLanguageID();
//						int languageIDModify = -1;
//						try {
//							languageIDModify = Integer.parseInt(subInfoArr[i].getSubType().trim());
//						} catch (Exception e) {
//							
//						}
//						
//						if (changeSubtitle.getSubType().trim().equalsIgnoreCase(subInfoArr[i].getSubType().trim()) || (languageID == languageIDModify && languageIDModify != -1)) {
//							checkboxSubtitle[i].setChecked(changeSubtitle.isChoice());
//							if (changeSubtitle.isChoice()) {
//								setVisibility(tvSubs[i], View.VISIBLE);
//								setColorForSubtitle(tvSubs[i], changeSubtitle.getSubType());
//								if(is3D){
//									setVisibility(tvSubs2[i], View.VISIBLE);
//									setColorForSubtitle(tvSubs2[i], changeSubtitle.getSubType());
//								}
//							} else {
//								setVisibility(tvSubs[i], View.GONE);
//								if(is3D){
//									setVisibility(tvSubs2[i], View.GONE);
//								}
//							}
//							break;
//						}
//					}
//				}
//			}
//	
//			if (subInfoArr != null && subInfoArr.length == menuSize) {
//				try {
//					for (int i = 0; i < menuSize; i++) {
//						boolean isChoice = checkboxSubtitle[i].isChecked();
//						String subType = subInfoArr[i].getSubType();
//						JSONObject jsonSub = new JSONObject();
//						jsonSub.put("subType", subType);
//						jsonSub.put("isChoice", isChoice);
//						jsonArrSub.put(jsonSub);
//					}
//					// result.put("result", jsonArrSub);
//	
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
//	
//			}
//		}
//		return jsonArrSub;
//
//	}

	public JSONObject getInfoCurrentVideo() {
		JSONObject jsonInfCurVideo = new JSONObject();
		int curTime = vp9Player.getCurrentPosition();
		boolean isPlay = vp9Player.isPlaying();
		int duration = vp9Player.getDuration();
		String channelId = this.channelId;
		JSONArray jsonArrSub = new JSONArray();
		boolean isLive = this.isLive == 1 ? true : false;
		// Menu menu = popupMenu.getMenu();
		if (checkboxSubtitle != null && checkboxSubtitle.length > 0) {
			int menuSize = checkboxSubtitle.length;
			if (subInfoArr != null && subInfoArr.length == menuSize) {
				try {
					for (int i = 0; i < menuSize; i++) {
						boolean isChoice = checkboxSubtitle[i].isChecked();
						String subType = subInfoArr[i].getSubType();
						JSONObject jsonSub = new JSONObject();
						jsonSub.put("subType", subType);
						jsonSub.put("isChoice", isChoice);
						jsonArrSub.put(jsonSub);
					}
					// result.put("result", jsonArrSub);
	
				} catch (JSONException e) {
					e.printStackTrace();
				}
	
			}
		}
		try {
			jsonInfCurVideo.put("time", curTime);
			jsonInfCurVideo.put("player_state", isPlay);
			jsonInfCurVideo.put("duration", duration);
			jsonInfCurVideo.put("subtitle", jsonArrSub);
			jsonInfCurVideo.put("channelId", channelId);
			jsonInfCurVideo.put("isLive", isLive);
			
			if (this.demandTiviSchedule != null) {
				jsonInfCurVideo.put("index", this.demandTiviSchedule.getCurrentIndex());
				jsonInfCurVideo.put("childIndex", this.demandTiviSchedule.getCurrentChildIndex());
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return jsonInfCurVideo;
	}

	public void addListenerRemoteVideo() {
		this.isRemoteListener = true;

	}

	public void changeDisplayScreen(int intFullScreen) {
		Log.d(TAG, "changeDisplayScreen");
		this.intFullScreen = intFullScreen;
		if (vp9Player != null) {
			updateVideoSize();
		}
	}

	public void changeScreenOrientation(String orientation) {
		if (activity != null) {
//			if ("landscape".equals(orientation)) {
//				int intOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
//				activity.setRequestedOrientation(intOrientation);
//			} else if ("portrait".equals(orientation)) {
//				int intOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
//				activity.setRequestedOrientation(intOrientation);
//			}
			
			int intOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
			activity.setRequestedOrientation(intOrientation);
		}
	}

	public void setMessage(String msg) {
		if (msg != null) {
			setTextForTextView(notifyTextView, msg, 0);
			setVisibility(notifyTextView, View.VISIBLE);
			if(is3D){
				setTextForTextView(notifyTextView2, msg, 0);
				setVisibility(notifyTextView2, View.VISIBLE);
			}
		}
	}
	
	public void setMessage2(String msg) {
		if (msg != null) {
			setTextForTextView(notifyTextView, msg, 1);
			
			if(is3D){
				setTextForTextView(notifyTextView2, msg, 1);
//				if(notifyTextView.isShown()){
//					setVisibility(notifyTextView, View.VISIBLE);
//				}
			}
		}
	}

	public void playRelateVideo(JSONObject json) {
		reset();
		Log.d(TAG, "playRelateVideo = " + json);
		if (json != null) {

			String movieID = Vp9ParamUtil.getJsonString(json, "movieID", "");
			
			String movieUrl = Vp9ParamUtil.getJsonString(json, "movieUrl", "");
			
			VideoInfo videoInfo = demandTiviSchedule.getVideoInfoByVideoId(movieID);
			
			ArrayList<VideoResolutionGroup> videoResolutionGroups = getVideoResolutionGroup(json);
			
			videoInfo.setVideoResolutionGroups(videoResolutionGroups);

			if (videoInfo == null) {
				return;
			}
			videoInfo.setUrl(movieUrl);
			videoInfo.setMovieID(movieID);
			
			updateVideoSize(json);

			ArrayList<SubtitleInfo> subtitleInfos = new ArrayList<SubtitleInfo>();
			if (json.has("sub")) {
				JSONArray jsonArraySub = Vp9ParamUtil.getJSONArray(json, "sub");
				if (jsonArraySub != null && jsonArraySub.length() > 0) {
					for (int i = 0; i < jsonArraySub.length(); i++) {
						try {
							JSONObject jsonSub = jsonArraySub.getJSONObject(i);
							String subUrl = Vp9ParamUtil.getJsonString(jsonSub, "subUrl", "");
							String subType = Vp9ParamUtil.getJsonString(jsonSub, "subType", "");
							String subTypeName = Vp9ParamUtil.getJsonString(jsonSub, "subTypeName", "");
							boolean isDisplay = Vp9ParamUtil.getJSONBoolean(jsonSub, "isDisplay", false);
							SubtitleInfo subInfo = new SubtitleInfo(subUrl, subType, subTypeName, isDisplay);
							subtitleInfos.add(subInfo);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}
			}

			playRelateVideo(videoInfo, subtitleInfos);

		}

	}

	public void playRelateVideo(VideoInfo videoInfo, ArrayList<SubtitleInfo> subtitleInfos) {
		if (clearParam) {
			return;
		}
		if (!isDisplayChannelImage && intShowControl != 2) {
//			if(!isLoading){
//				isLoading = true;
//				setVisibility(loadingLayout, View.VISIBLE);
//			}
			setVisibility(loadingLayout, View.VISIBLE);
			if(is3D){
				setVisibility(loadingLayout2, View.VISIBLE);
			}
		} else {
//			if(isLoading){
//				isLoading = false;
//				setVisibility(loadingLayout, View.GONE);
//			}
			setVisibility(loadingLayout, View.GONE);
			if(is3D){
				setVisibility(loadingLayout2, View.GONE);
			}
		}
		setEnabled(btnSetting, false);
		if(is3D){
			setEnabled(btnSetting2, false);
		}
		// setVisibility(progessLayout, View.VISIBLE);
		String videoUrl = videoInfo.getUrl();

		Log.d(TAG, "playRelateVideo: " + videoUrl);
		this.playingVideo = videoInfo;
		if (subtitleInfos.size() > 0) {
			subInfoArr = new SubtitleInfo[subtitleInfos.size()];
			subInfoArr = subtitleInfos.toArray(subInfoArr);
//			updateDefaultSubtitle();
		} else {
			subInfoArr = null;
		}
		this.videoResolutions = videoInfo.getVideoResolutionGroups();
		playVideo(this.videoResolutions, videoUrl, false);
		// showController(0);
		showProgessLayout();
		demandTiviSchedule.setCurrentIndex(videoInfo.getIndex());
	}

	public RelativeLayout getMainLayout() {
		return mainLayout;
	}

	public void setMainLayout(RelativeLayout mainLayout) {
		this.mainLayout = mainLayout;
	}

	public ArrayList<String> getSettingSubTypes() {
		return settingSubTypes;
	}

	public void setSettingSubTypes(ArrayList<String> settingSubTypes) {
		this.settingSubTypes = settingSubTypes;
	}
	
	public void setFocusView(final View view){
		Activity activity = getActivity();
		if (activity != null) {
			activity.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					if (view != null) {
						view.requestFocus();
					}
				}
			});
		}
		
	}

	public void setIs3D(boolean is3d) {
		this.is3D = is3d;
		
	}
	
	public void setProxySevice(MyService proxySevice) {
		this.proxySevice = proxySevice;
		
	}
	
	public MyService getProxySevice() {
		return this.proxySevice;
		
	}
}
