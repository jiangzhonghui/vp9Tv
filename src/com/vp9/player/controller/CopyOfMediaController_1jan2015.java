package com.vp9.player.controller;


public class CopyOfMediaController_1jan2015 {

/*	// public static final String WAITE_CHANNEL_IMAGE =
	// "http://tv.vp9.tv/player/theme/pk-tv.png";
	public String errorMsg;

	public CopyOfMediaController_1jan2015(Context context) {
	}
	


//	public static final int STATE_ERROR = -1;
//	public static final int STATE_IDLE = 0;
//	public static final int STATE_PAUSED = 4;
//	public static final int STATE_PLAYBACK_COMPLETED = 5;
//	public static final int STATE_PLAYING = 3;
//	public static final int STATE_PREPARED = 2;
//	public static final int STATE_PREPARING = 1;
//	public static final int STATE_RESUME = 7;
//	public static final int STATE_SUSPEND = 6;
//	public static final int STATE_SUSPEND_UNSUPPORTED = 8;

	// public Boolean isBundle = Boolean.valueOf(false);

	public boolean isTouch = false;

	public String serverUrl;

	// public Vp9Player(Activity activity){
	// this.activity = activity;
	// }

	public static final String TAG = "MediaController";

	public int video_view_id;

	public int pdLoading_id;

	public int load_rate_id;

	public int tvSub_id;

	public int tvSubMargin_id;

	public int seekBar_id;

	public int tvFrom_id;

	public int tvTo_id;

	public int btnPlay_id;

	public int btnSub_id;

	public int btnBack_id;

	public int btnSetting_id;
	
	public int vp9_btnSetting_hide_id;
	
	public int vp9_btnSetting_id;

	public int controller_id;
	
	public int controller_top_id;

	public int vp9_player_layout_id;

	public int loading_layout_id;

	public int subtitles_layout_id;

	public int progess_id;

	public int vp9_btn_play_id;

	public int vp9_btn_pause_id;

	public int vp9_btn_sub_id;

	public int vp9_btn_sub_hide_id;

	public int vp9ChannelImage_id;

	public int video_title_layout_id;

	public int logo_video_id;

	public int video_title_id;

	public int logo_id;

	public int logo_text_id;

	public int logo_layout_id;

	public int btnChoose_id;

	public int btnPrev_id;

	public int btnNext_id;

	public int notify_id;

	public int vp9_btn_next_hide_id;

	public int vp9_btn_prev_hide_id;

	public int vp9_btn_next_id;

	public int vp9_btn_prev_id;
	
	public int parent_layout_id;
	
	public MagicTextView logoText;

	public SurfaceView mVideoView;
	
	public ProgressBar pdLoading;

	public TextView loadRate;
	
	public MagicTextView videoTitle;
	
	public TextView notifyTextView;

	// public TextView tvSub1;
	//
	// public TextView tvSub2;

	public MagicTextView[] tvSubs;

	public SeekBar sbFull;

	public TextView tvFrom;

	public TextView tvTo;
	
	public ImageView vp9ChannelImage;
	
	public Button btnPlay;
	
	public Button btnSub;

	public Button btnBack;
	
	public Button btnSetting;
	
	public Button btnChoose;
	
	public Button btnPrev;
	
	public Button btnNext;
	
	public PopupWindow popupVideoWindow;

	public PopupWindow settingPopup;
	
//	public MediaPlayer player;

	public SurfaceHolder mHolder;
	
	public ImageView logoChannel;
	
	public ImageView logoVideo;
	
	public RelativeLayout parentLayout;
	
	public RelativeLayout loadingLayout;

	public RelativeLayout vp9PlayerLayout;

	public RelativeLayout subtitlesLayout;

	public RelativeLayout controllerLayout;
	
	public LinearLayout controllerTopLayout;

	public RelativeLayout progessLayout;

	public RelativeLayout videoTitleLayout;

	public RelativeLayout mainLayout;
	
	public Activity activity;
	
	public ArrayList<TimeShow> timeShowList;
	
	public boolean isResume;

	// public Boolean isBundle = Boolean.valueOf(false);

	public boolean isError = Boolean.valueOf(false);

	public boolean isPause = Boolean.valueOf(false);

	public boolean isSeek = Boolean.valueOf(false);
	
	public Handler handleHideController = new Handler();

	public Handler updateTimehandle = new Handler();

	public Handler mResumHandle = new Handler();

	public Handler mResumErrorHandle = new Handler();

	public Handler mCheckPlayDemandVideoHandle1 = new Handler();

	public Handler mCheckPlayDemandVideoHandle2 = new Handler();

	public Handler timeShowHandle = new Handler();

	public Handler showLeftDisplayHandle = new Handler();
	
	public Handler startVideoHandler = new Handler();
	
	public RelativeLayout logoLayout;
	
	public LoadViewTask loadSub;
	
	public LoadLeftLogoTask loadLeftLogoTask;
	
	public LoadRightLogoTask loadRightLogoTask;

	public int intShowControl;
	
	public int videoType;
	
	public int intProxy;
	
	public boolean clearParam;
	
	public boolean isDisplayChannelImage;

	public int state = 0;
	
	public int isLive;
	
	public boolean isPlay;
	
	public int mScreenWidth;
	
	public int mScreenHeight;
	
	public int videoIndex;
	
	public int childVideoIndex;
	
	public int videoSeekTime;
	
	public ArrayList<ConfigSubtile> configSubtiles;
	
	public ArrayList<String> settingSubTypes;
	
	public VideoResult curVideoResult;
	
	public SubtitleInfo[] subInfoArr;

	public ArrayList<VideoResolution> videoResolutions;
	
	public String channelId;
	
	public String videoUrl;
	
	public int mCurrentState = 0;
	
	public boolean showControlLayout;
	
	public int mVideoWidth;
	
	public int mVideoHeight;
		
	public int percentBuffer;
	
	public long currentError = 0L;
	
	public VideoInfo playingVideo;
	
	public DemandTiviSchedule demandTiviSchedule;
	
	public MediaMetadataRetriever mMetadataRetriever;
	
	public String serverTimeUrl;
	
	private String strDate;

	public Vp9PlayerInterface vp9Player;

	private int playType;

	private String codecResolution;

	private String codec;

	public boolean isRightDisplay;
	
	public String channelName;
	
	public String channelIcon;

	public String vp9Logo;

	private String resolution;

	public int topStretch;

	public int bottomStretch;

	public int leftStretch;

	public int rightStretch;

	private String token;
	
	public void init() {
		
		getActivity().runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				RelativeLayout ControlTop2 = (RelativeLayout) getActivity().findViewById(R.id.ControlTop2);
				ControlTop2.setVisibility(View.GONE);
			}
		});
		
		parentLayout = (RelativeLayout) activity.findViewById(parent_layout_id);
		
		mVideoView = (SurfaceView) activity.findViewById(video_view_id);

		pdLoading = (ProgressBar) activity.findViewById(pdLoading_id);

		loadRate = (TextView) activity.findViewById(load_rate_id);

		sbFull = (SeekBar) activity.findViewById(seekBar_id);

		tvFrom = (TextView) activity.findViewById(tvFrom_id);

		tvTo = (TextView) activity.findViewById(tvTo_id);

		btnPlay = (Button) activity.findViewById(btnPlay_id);

		btnSub = (Button) activity.findViewById(btnSub_id);

		btnBack = (Button) activity.findViewById(btnBack_id);

		btnSetting = (Button) activity.findViewById(btnSetting_id);

		vp9ChannelImage = (ImageView) activity.findViewById(vp9ChannelImage_id);

		// setImageBitmap(vp9ChannelImage, WAITE_CHANNEL_IMAGE);

		tvSubs = new MagicTextView[4];

		MagicTextView tvSub1 = (MagicTextView) activity.findViewById(tvSub_id);

		MagicTextView tvSub2 = (MagicTextView) activity.findViewById(tvSubMargin_id);

		MagicTextView tvSub3 = (MagicTextView) activity.findViewById(R.id.tvSub1);

		MagicTextView tvSub4 = (MagicTextView) activity.findViewById(R.id.tvSubMargin1);
		
		tvSubs[0] = tvSub1;
		tvSubs[1] = tvSub2;
		tvSubs[2] = tvSub3;
		tvSubs[3] = tvSub4;
		

		progessLayout = (RelativeLayout) getActivity().findViewById(progess_id);

		loadingLayout = (RelativeLayout) activity.findViewById(loading_layout_id);

		controllerLayout = (RelativeLayout) activity.findViewById(controller_id);
		
		controllerTopLayout = (LinearLayout) activity.findViewById(controller_top_id);

		vp9PlayerLayout = (RelativeLayout) activity.findViewById(vp9_player_layout_id);

		subtitlesLayout = (RelativeLayout) activity.findViewById(subtitles_layout_id);

		videoTitleLayout = (RelativeLayout) activity.findViewById(video_title_layout_id);

		logoLayout = (RelativeLayout) activity.findViewById(logo_layout_id);

		logoVideo = (ImageView) activity.findViewById(logo_video_id);

		videoTitle = (MagicTextView) activity.findViewById(video_title_id);

		logoChannel = (ImageView) activity.findViewById(logo_id);

		logoText = (MagicTextView) activity.findViewById(logo_text_id);

		btnChoose = (Button) activity.findViewById(btnChoose_id);

		btnPrev = (Button) activity.findViewById(btnPrev_id);

		btnNext = (Button) activity.findViewById(btnNext_id);

		notifyTextView = (TextView) activity.findViewById(notify_id);
		
		

		// setTextForTextView();
		setTextForTextView(tvTo, Utilities.milliSecondsToTimer(0));
		setTextForTextView(tvFrom, Utilities.milliSecondsToTimer(0));
//		isLoading = true;
		setVisibility(loadingLayout, View.VISIBLE);
		setVisibility(controllerTopLayout, View.VISIBLE);
		setVisibility(progessLayout, View.VISIBLE);
		setVisibility(logoLayout, View.VISIBLE);
		setVisibility(videoTitleLayout, View.VISIBLE);
		setVisibility(subtitlesLayout, View.VISIBLE);
		setVisibility(logoVideo, View.GONE);
		setVisibility(videoTitle, View.GONE);
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// videoTitle.setShadowLayer(3.0f, 5, 5, Color.BLACK);
				// logoText.setShadowLayer(3.0f, 5, 5, Color.BLACK);
				videoTitle.setShadowLayer(10.0f, 0, 0, Color.BLACK);
				logoText.setShadowLayer(10.0f, 0, 0, Color.BLACK);
			}
		});

		if (sbFull != null) {
			setProgressForSeekBar(sbFull, 0);
			setMaxForSeekBar(sbFull, 1000);
			setSecondaryProgress(sbFull, 0);
		}

		initParam();

		mVideoView.requestFocus();
		mHolder = mVideoView.getHolder();
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		clearParam = false;

		// mainLayout.setOnKeyListener(new MainKeyListener());

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
		this.resolution = null;
		this.codecResolution = null;
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
//			setFullScreen();
			analyzeTimeShow(jsonVideoInfo);
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
				
				updateScreenSize(jsonVideoInfo);
				switch (videoType) {

				case 0:
					setEnabled(btnNext, true);
					setEnabled(btnPrev, true);
					setEnabled(btnSetting, false);
					setBackgroundResource(btnSetting, vp9_btnSetting_hide_id);
					setBackgroundResource(btnNext, vp9_btn_next_id);
					setBackgroundResource(btnPrev, vp9_btn_prev_id);
					handlerPlayVideo(jsonVideoInfo);
					break;

				case 1:
					setEnabled(btnNext, false);
					setEnabled(btnPrev, false);
					setEnabled(btnSetting, true);
					setBackgroundResource(btnSetting, vp9_btnSetting_id);
					setBackgroundResource(btnNext, vp9_btn_next_hide_id);
					setBackgroundResource(btnPrev, vp9_btn_prev_hide_id);
					handlerPlayLiveTivi(jsonVideoInfo);
					break;

				case 2:
					setEnabled(btnNext, true);
					setEnabled(btnPrev, true);
					setEnabled(btnSetting, true);
					setBackgroundResource(btnSetting, vp9_btnSetting_id);
					setBackgroundResource(btnNext, vp9_btn_next_id);
					setBackgroundResource(btnPrev, vp9_btn_prev_id);
					handlerPlayDemandTivi1(jsonVideoInfo);
					break;

				case 3:
					setEnabled(btnNext, true);
					setEnabled(btnPrev, true);
					setEnabled(btnSetting, true);
					setBackgroundResource(btnSetting, vp9_btnSetting_id);
					setBackgroundResource(btnNext, vp9_btn_next_id);
					setBackgroundResource(btnPrev, vp9_btn_prev_id);
					handlerPlayDemandTivi2(jsonVideoInfo);
					break;

				case 4:
					setEnabled(btnNext, true);
					setEnabled(btnPrev, true);
					setEnabled(btnSetting, true);
					setBackgroundResource(btnSetting, vp9_btnSetting_id);
					setBackgroundResource(btnNext, vp9_btn_next_id);
					setBackgroundResource(btnPrev, vp9_btn_prev_id);
					handlerPlayOffTvProgram(jsonVideoInfo);
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
		} else {
			Log.d(TAG, "loading 4: " + false);
//			if(isLoading){
//				isLoading = false;
//				setVisibility(loadingLayout, View.GONE);
//			}
			setVisibility(loadingLayout, View.GONE);
		}
//		setEnabled(btnSetting, false);
		// setVisibility(progessLayout, View.VISIBLE);
		String videoUrl = jsonVideoInfo.getString("videoUrl");
		this.playType = Vp9ParamUtil.getJsonInt(jsonVideoInfo, "playType", 0);
		this.videoResolutions = getVideoResolutions(jsonVideoInfo);
		
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
						createVideoMenu();
					}
				};
				videoMenuThread.start();

			}
		} catch (JSONException e) {
			e.printStackTrace();
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
						
						VideoResolution videoResolution = new VideoResolution();
						
						videoResolution.setVideoUrl(video_url);
						
						videoResolution.setType(type);
						
						videoResolution.setCodec(codec);
						
						videoResolution.setBitrate(bitrate);
						
						videoResolution.setQuality(quality);
						
						videoResolution.setResolution(resolution);
						
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
		String msg = Vp9Contant.MSG_PLAY_NEXT_VIDEO;
		setTextForTextView(notifyTextView, msg);
		
		if (demandTiviSchedule != null) {
			if (videoType == 1) {
				if (popupVideoWindow.isShowing()) {
					popupVideoWindow.dismiss();
				}
				return;
			}

			if (videoType == 0) {
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
					} else {
						Log.d(TAG, "loading 17: " + false);
//						if(isLoading){
//							isLoading = false;
//							setVisibility(loadingLayout, View.GONE);
//						}
						setVisibility(loadingLayout, View.GONE);
					}
					if (vp9Player != null) {
						vp9Player.release();
					}
					mVideoView.destroyDrawingCache();
					mVideoView.refreshDrawableState();
					sendMsgToGetVideoInfo(videoResult);
				}
				popupVideoWindow.dismiss();
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
					} else {
						Log.d(TAG, "loading 18: " + false);
//						if(isLoading){
//							isLoading = false;
//							setVisibility(loadingLayout, View.GONE);
//						}
						setVisibility(loadingLayout, View.GONE);
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
				}
				popupVideoWindow.dismiss();
				return;
			}
//			setVisibility(loadingLayout, View.VISIBLE);
			if (!isDisplayChannelImage && intShowControl != 2) {
//				if(!isLoading){
//					isLoading = true;
//					setVisibility(loadingLayout, View.VISIBLE);
//				}
				setVisibility(loadingLayout, View.VISIBLE);
			} else {
				Log.d(TAG, "loading 19: " + false);
//				if(isLoading){
//					isLoading = false;
//					setVisibility(loadingLayout, View.GONE);
//				}
				setVisibility(loadingLayout, View.GONE);
			}
			if (vp9Player != null) {
				// player.reset();
				// player.release();
				// player = null;
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
			int currentIndex = demandTiviSchedule.getCurrentIndex();
			if (currentIndex + 1 < demandTiviSchedule.getSizeVideoInfos()) {
				VideoResult videoResult = demandTiviSchedule.getVideoInfoByIndex(currentIndex + 1);
				playNewVideo(videoResult);
			}
		}
	}

	public void playPreVideo() {
		if (clearParam) {
			return;
		}
		
		String msg = Vp9Contant.MSG_PLAY_PREV_VIDEO;
		setTextForTextView(notifyTextView, msg);
		
		int currentIndex = demandTiviSchedule.getCurrentIndex();
		Log.d(TAG, "playPreVideo-currentIndex: " + currentIndex);
		if(currentIndex == -1){
			currentIndex = demandTiviSchedule.getSizeVideoInfos();
			Log.d(TAG, "playPreVideo-SizeVideoInfos: " + currentIndex);
		}
//		if (demandTiviSchedule != null && demandTiviSchedule.getCurrentIndex() > 0) {
		if (demandTiviSchedule != null && currentIndex > 0) {
			if (videoType == 1) {
				popupVideoWindow.dismiss();
				return;
			}
			if (videoType == 0) {
				
				VideoResult videoResult = demandTiviSchedule.getVideoInfoByIndex(currentIndex - 1);
				if (videoResult != null && videoResult.getVideoInfo() != null) {
					isLive = 0;
//					setVisibility(loadingLayout, View.VISIBLE);
					if (!isDisplayChannelImage && intShowControl != 2) {
//						if(!isLoading){
//							isLoading = true;
//							setVisibility(loadingLayout, View.VISIBLE);
//						}
						setVisibility(loadingLayout, View.VISIBLE);
					} else {
						Log.d(TAG, "loading 20: " + false);
//						if(isLoading){
//							isLoading = false;
//							setVisibility(loadingLayout, View.GONE);
//						}
						setVisibility(loadingLayout, View.GONE);
					}
					if (vp9Player != null) {
						vp9Player.release();
					}
					mVideoView.destroyDrawingCache();
					mVideoView.refreshDrawableState();
					sendMsgToGetVideoInfo(videoResult);
				}
				popupVideoWindow.dismiss();
				return;
			} else if (videoType == 4) {
//				int currentIndex = demandTiviSchedule.getCurrentIndex();
				VideoResult videoResult = demandTiviSchedule.getVideoInfoByIndex(currentIndex - 1);
				if (videoResult != null && videoResult.getVideoInfo() != null) {
					isLive = 0;
//					setVisibility(loadingLayout, View.VISIBLE);
					if (!isDisplayChannelImage && intShowControl != 2) {
//						if(!isLoading){
//							isLoading = true;
//							setVisibility(loadingLayout, View.VISIBLE);
//						}
						setVisibility(loadingLayout, View.VISIBLE);
					} else {
//						if(isLoading){
//							isLoading = false;
//							setVisibility(loadingLayout, View.GONE);
//						}
						setVisibility(loadingLayout, View.GONE);
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
				}
				popupVideoWindow.dismiss();
				return;
			}
//			setVisibility(loadingLayout, View.VISIBLE);
			if (!isDisplayChannelImage && intShowControl != 2) {
//				if(!isLoading){
//					isLoading = true;
//					setVisibility(loadingLayout, View.VISIBLE);
//				}
				setVisibility(loadingLayout, View.VISIBLE);
			} else {
//				if(isLoading){
//					isLoading = false;
//					setVisibility(loadingLayout, View.GONE);
//				}
				setVisibility(loadingLayout, View.GONE);
			}
			if (vp9Player != null) {
				// player.reset();
				// player.release();
				// player = null;
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
//			int currentIndex = demandTiviSchedule.getCurrentIndex();
			VideoResult videoResult = demandTiviSchedule.getVideoInfoByIndex(currentIndex - 1);
			playNewVideo(videoResult);
		}
	}

	public void analyzeTimeShow(JSONObject jsonVideoInfo) {
		if (clearParam || isRightDisplay) {
			return;
		}
		if (jsonVideoInfo != null && jsonVideoInfo.has("logo")) {
			try {
				JSONArray timeShowJSONArr = jsonVideoInfo.getJSONArray("logo");
				if (timeShowJSONArr != null && timeShowJSONArr.length() > 0) {
					timeShowList = new ArrayList<TimeShow>();
					for (int i = 0; i < timeShowJSONArr.length(); i++) {
						JSONObject timeShowJSON = timeShowJSONArr.getJSONObject(i);

						int interval = Vp9ParamUtil.getJsonInt(timeShowJSON, "interval", 0);

						int type = Vp9ParamUtil.getJsonInt(timeShowJSON, "type", 0);

						int logoType = Vp9ParamUtil.getJsonInt(timeShowJSON, "logoType", 0);

						String url = Vp9ParamUtil.getJsonString(timeShowJSON, "url", "");

						String content = Vp9ParamUtil.getJsonString(timeShowJSON, "content", "");

						TimeShow timeShow = new TimeShow();

						timeShow.setInterval(interval);

						timeShow.setType(type);

						timeShow.setLogoType(logoType);

						timeShow.setUrl(url);

						timeShow.setContent(content);

						timeShowList.add(timeShow);
					}
				}
				timeShowHandle.postDelayed(timeShowTask, 1000L);

			} catch (JSONException e) {
				e.printStackTrace();
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
		} else {
			Log.d(TAG, "loading 5: " + false);
//			if(isLoading){
//				isLoading = false;
//				setVisibility(loadingLayout, View.GONE);
//			}
			setVisibility(loadingLayout, View.GONE);
		}
		setEnabled(btnSetting, false);
		// setVisibility(progessLayout, View.VISIBLE);
		String videoUrl = jsonVideoInfo.getString("videoUrl");
		this.playType = Vp9ParamUtil.getJsonInt(jsonVideoInfo, "playType", 0);
		this.videoResolutions = getVideoResolutions(jsonVideoInfo);
		
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
			updateDefaultSubtitle();

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
						createVideoMenu();
					}
				};
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
		} else {
//			if(isLoading){
//				isLoading = false;
//				setVisibility(loadingLayout, View.GONE);
//			}
			setVisibility(loadingLayout, View.GONE);
		}
		// tam dong
		setVisibility(progessLayout, View.GONE);
		this.isLive = Vp9ParamUtil.getJsonInt(jsonVideoInfo, "isLive", -1);
		this.serverUrl = Vp9ParamUtil.getJsonString(jsonVideoInfo, "serverUrl", "");
		this.channelId = Vp9ParamUtil.getJsonString(jsonVideoInfo, "channelId", "");
		this.serverTimeUrl = Vp9ParamUtil.getJsonString(jsonVideoInfo, "serverTimeUrl", "");
		this.strDate = Vp9ParamUtil.getJsonString(jsonVideoInfo, "date", null);
		
		this.playType = Vp9ParamUtil.getJsonInt(jsonVideoInfo, "playType", 0);
		Log.e(TAG, "hungvv1 : " + this.serverUrl);
		ServerTimeInfo serverTimeInfo = new ServerTimeInfo(serverTimeUrl);
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

				CopyOfMediaController_1jan2015.this.demandTiviSchedule = new DemandTiviSchedule(cheduleUrl, CopyOfMediaController_1jan2015.this.videoType, CopyOfMediaController_1jan2015.this);

				CopyOfMediaController_1jan2015.this.demandTiviSchedule.setStrdate(newServerTimeInfo.getStrdate());
				
				CopyOfMediaController_1jan2015.this.demandTiviSchedule.setRecordVideo(channelId, strDate);
				
				int secondInDay = newServerTimeInfo.getSecondInDay() + 1;
				VideoResult videoResult = CopyOfMediaController_1jan2015.this.demandTiviSchedule.getVideoInfoByTime(secondInDay);
				if (videoResult != null && videoResult.getVideoInfo() != null) {
					VideoInfo videoInfo = videoResult.getVideoInfo();
					CopyOfMediaController_1jan2015.this.playingVideo = videoInfo;
				}
		Thread videoMenuThread = new Thread() {
			public void run() {
				createVideoMenu();
			}
		};
		videoMenuThread.start();

		String videoUrl = jsonVideoInfo.getString("videoUrl");
		
		this.videoResolutions = getVideoResolutions(jsonVideoInfo);
		
		Log.e(TAG, "handlerPlayLiveTivi: " + videoUrl);
		
		playVideo(videoResolutions, videoUrl, false);
		
		setEnabled(btnSetting, true);

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
		} else { 
//			if(isLoading){
//				isLoading = false;
//				setVisibility(loadingLayout, View.GONE);
//			}
			setVisibility(loadingLayout, View.GONE);
			Log.d(TAG, "loading 2: " + false);
		}

		ServerTimeInfo serverTimeInfo = new ServerTimeInfo(serverTimeUrl);

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
			setTextForTextView(notifyTextView, this.errorMsg);
			return;
		}

		Thread videoMenuThread = new Thread() {
			public void run() {
				createVideoMenu();
			}
		};
		videoMenuThread.start();

		Log.d(TAG, "Play Demand Tivi 1: " + this.demandTiviSchedule.getSizeVideoInfos());

		if (this.isLive == 1) {
			setVisibility(progessLayout, View.GONE);
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

	public void createVideoMenu() {
		Log.e(TAG, "---------------- createVideoMenu");
		if (clearParam) {
			return;
		}
		if (this.demandTiviSchedule.isContainData()) {
			popupVideoWindow = new PopupWindow();
			popupVideoWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
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
				

				ListView listViewVideos = new ListView(activity);
				listViewVideos.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
//				listViewVideos.setAdapter(getVideosAdapter(strVideos, activity));
				listViewVideos.setAdapter(new MenuItemAdapter(activity, R.layout.custom_item_list, this.demandTiviSchedule.getVideoInfoList(), videoType, demandTiviSchedule, serverTimeUrl));

				// some other visual settings
				popupVideoWindow.setFocusable(true);
				// popupVideoWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
				popupVideoWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
				popupVideoWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

				// set the list view as pop up window content
				popupVideoWindow.setContentView(listViewVideos);

				listViewVideos.setOnItemClickListener(new ItemVideoMenuClickListener());

				listViewVideos.setOnKeyListener(new KeyVideoMenuListener());

				popupVideoWindow.setOnDismissListener(new DismissPopupWindowListener());
			}
		}
	}

	public class DismissPopupWindowListener implements android.widget.PopupWindow.OnDismissListener {

		@Override
		public void onDismiss() {
			showControlLayout = false;
			if (handleHideController != null) {
				handleHideController.removeCallbacks(hideTask);
			}
			hideController();
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
			popupVideoWindow.dismiss();
			final int newPosition = position;
			if(activity != null){
				activity.runOnUiThread(new Runnable(){
		            public void run(){
		            	CopyOfMediaController_1jan2015.this.handlerVideoMenuClickListener(newPosition);
		            }
		        });
			}
		}
	}

	
	 * adapter where the list values will be set
	 
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
									ServerTimeInfo serverTimeInfo = new ServerTimeInfo(serverTimeUrl);
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
			setTextForTextView(notifyTextView, msg);
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
					} else {
//						if(isLoading){
//							isLoading = false;
//							setVisibility(loadingLayout, View.GONE);
//						}
						setVisibility(loadingLayout, View.GONE);
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
					} else {
//						if(isLoading){
//							isLoading = false;
//							setVisibility(loadingLayout, View.GONE);
//						}
						setVisibility(loadingLayout, View.GONE);
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
					playVideo(videoResult.getVideoInfo().getVideoResolutions(), videoResult.getVideoInfo().getUrl(), false);
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
				} else {
//					if(isLoading){
//						isLoading = false;
//						setVisibility(loadingLayout, View.GONE);
//					}
					setVisibility(loadingLayout, View.GONE);
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
				popupVideoWindow.dismiss();
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
				
				popupVideoWindow.dismiss();
				
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
		case Vp9KeyEvent.KEYCODE_F3:
		case Vp9KeyEvent.KEYCODE_F11:
			showVideoMenu(btnChoose);
			break;

		case Vp9KeyEvent.KEYCODE_MEDIA_NEXT:
			if (videoType == 1) {
				break;
			}
			isLive = 0;
			if (popupVideoWindow != null && popupVideoWindow.isShowing()) {
				popupVideoWindow.dismiss();
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
				
				popupVideoWindow.dismiss();
				
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
		case Vp9KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
			handleClickPlayAndPause();
			isSucess = true;
			break;
		case Vp9KeyEvent.KEYCODE_MENU:
			if (videoType != 0) {
				showEPGOrCloseEPG();
			}
			isSucess = true;
			break;
		case Vp9KeyEvent.KEYCODE_F4:
		case Vp9KeyEvent.KEYCODE_F12:
			showPopupMenu(btnSub);
			isSucess = true;
			break;
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
				setTextForTextView(notifyTextView, this.errorMsg);
			}
		} else {
			this.errorMsg = "lịch chiếu video bị lỗi #3";
			setTextForTextView(notifyTextView, this.errorMsg);
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
		ServerTimeInfo serverTimeInfo = new ServerTimeInfo(serverTimeUrl);
		if (!isDisplayChannelImage && intShowControl != 2) {
//			if(!isLoading){
//				isLoading = true;
//				setVisibility(loadingLayout, View.VISIBLE);
//			}
			setVisibility(loadingLayout, View.VISIBLE);
		} else {
//			if(isLoading){
//				isLoading = false;
//				setVisibility(loadingLayout, View.GONE);
//			}
			setVisibility(loadingLayout, View.GONE);
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
			setTextForTextView(notifyTextView, this.errorMsg);
			return;
		}

		Thread videoMenuThread = new Thread() {
			public void run() {
				createVideoMenu();
			}
		};
		videoMenuThread.start();

		if (this.isLive == 1) {
			setVisibility(progessLayout, View.GONE);
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
		if (clearParam || demandTiviSchedule == null) {
			return;
		}
		Thread thdLoadLeftLogo = new Thread(){
			
			public void run(){
				demandTiviSchedule.updateLeftDisplays(playingVideo);
				if (playingVideo != null && playingVideo.getLeftDisplays() != null && playingVideo.getLeftDisplays().size() > 0) {
					if (CopyOfMediaController_1jan2015.this.loadLeftLogoTask != null) {
						((LoadLeftLogoTask) CopyOfMediaController_1jan2015.this.loadLeftLogoTask).cancelTask();
						((LoadLeftLogoTask) CopyOfMediaController_1jan2015.this.loadLeftLogoTask).cancel(true);
					}
					CopyOfMediaController_1jan2015.this.loadLeftLogoTask = new LoadLeftLogoTask(CopyOfMediaController_1jan2015.this, playingVideo, vp9Player, logoVideo, videoTitle);
					CopyOfMediaController_1jan2015.this.loadLeftLogoTask.execute(new Void[0]);
				}
			}
		};
		
		thdLoadLeftLogo.start();
//		demandTiviSchedule.updateLeftDisplays(playingVideo);
//		if (playingVideo != null && playingVideo.getLeftDisplays() != null && playingVideo.getLeftDisplays().size() > 0) {
//			this.loadLeftLogoTask = new LoadLeftLogoTask(this, playingVideo, vp9Player, logoVideo, videoTitle);
//			this.loadLeftLogoTask.execute(new Void[0]);
//		}

	}
	
	public void loadRightLogo(final VideoInfo playingVideo) {
		if (clearParam || !this.isRightDisplay || demandTiviSchedule == null) {
			return;
		}
//		demandTiviSchedule.updateRightDisplays(playingVideo);
//		this.loadRightLogoTask = new LoadRightLogoTask(this, playingVideo, vp9Player, logoChannel, logoText);
//		this.loadRightLogoTask.execute(new Void[0]);
		
		Thread thdLoadRightLogo = new Thread(){
			
			public void run(){
				demandTiviSchedule.updateRightDisplays(playingVideo);
				if (CopyOfMediaController_1jan2015.this.loadRightLogoTask != null) {
					((LoadRightLogoTask) CopyOfMediaController_1jan2015.this.loadRightLogoTask).cancelTask();
					((LoadRightLogoTask) CopyOfMediaController_1jan2015.this.loadRightLogoTask).cancel(true);
				}
				CopyOfMediaController_1jan2015.this.loadRightLogoTask = new LoadRightLogoTask(CopyOfMediaController_1jan2015.this, playingVideo, vp9Player, logoChannel, logoText);
				CopyOfMediaController_1jan2015.this.loadRightLogoTask.execute(new Void[0]);
			}
		};
		
		thdLoadRightLogo.start();
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
			vp9Player.release();
		}
//		isLoading = false;
		setVisibility(loadingLayout, View.GONE);
		setVisibility(controllerTopLayout, View.GONE);
		setVisibility(progessLayout, View.GONE);
		setVisibility(logoLayout, View.GONE);
		setVisibility(videoTitleLayout, View.GONE);
		setVisibility(subtitlesLayout, View.GONE);
		if (type == 1) {
			clearParam();
		}
	}

	public void clearParam() {
		checkboxSubtitle = null;
		clearParam = true;
		mVideoView = null;
		mMetadataRetriever = null;
		pdLoading = null;
		loadRate = null;
		tvSubs = null;
		sbFull = null;
		tvFrom = null;
		tvTo = null;
		handleHideController = null;
		updateTimehandle = null;
		mResumHandle = null;
		mResumErrorHandle = null;
		mCheckPlayDemandVideoHandle1 = null;
		mCheckPlayDemandVideoHandle2 = null;
		loadSub = null;
		btnPlay = null;
		controllerLayout = null;
		// popupMenu = null;
		settingPopup = null;
		btnSub = null;
		btnBack = null;
		subInfoArr = null;
		activity = null;
		btnSetting = null;
		demandTiviSchedule = null;
		serverTimeUrl = null;
		vp9ChannelImage = null;
		serverUrl = null;
		vp9Player = null;
//		mHolder = null;
		progessLayout = null;
		timeShowIndex = 0;
		token = null;
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
			if (activity != null) {
				Vp9ActivityInterface vp9Activity = (Vp9ActivityInterface) activity;
				vp9Activity.closeEPG();
			}
			showControlLayout = true;
			// popupVideoWindow.dismiss();
			if (videoType == 1) {

			}
			popupVideoWindow.showAsDropDown(v, -5, 0);
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
		setTextForTextView(tvTo, Utilities.milliSecondsToTimer(0));
		setTextForTextView(tvFrom, Utilities.milliSecondsToTimer(0));

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

			decodeStreamThrd.start();
		}
	}

	public Bitmap loadImage(String image_location) {
		URL imageURL = null;
		Bitmap bitmap = null;
		if (image_location != null) {
			try {
				imageURL = new URL(image_location);
				HttpURLConnection connection = (HttpURLConnection) imageURL.openConnection();
				connection.setDoInput(true);
				connection.connect();
				InputStream inputStream = connection.getInputStream();

				bitmap = BitmapFactory.decodeStream(inputStream);// Convert to
																	// bitmap
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
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
			updateDefaultSubtitle();
		}else{
			subInfoArr = null;
		}
		this.videoResolutions = videoInfo.getVideoResolutions();
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
				if (state == 1) {
					setVisibility(videoTitleLayout, View.VISIBLE);
				}
				if (videoType == 0 || (videoType > 1 && isLive == 0)) {
					setVisibility(progessLayout, View.VISIBLE);
				}
			}
		} else {
			setVisibility(videoTitleLayout, View.GONE);
			setVisibility(progessLayout, View.GONE);
		}
	}

	public void updateDefaultSubtitle() {
		if (settingSubTypes != null) {
			for (int i = 0; i < subInfoArr.length; i++) {
				boolean isDisPlays = false;
				for (String subType : settingSubTypes) {
					if (subType.equals(subInfoArr[i].getSubType())) {
						isDisPlays = true;
						break;
					}
				}
				subInfoArr[i].setDisplay(isDisPlays);
			}
		}
	}

	public synchronized void playVideo(ArrayList<VideoResolution> videoResolutions,
			String videoUrl, boolean loop) {
		
		final ArrayList<VideoResolution> newVideoResolutions = videoResolutions;
		
		final String newVideoUrl = videoUrl;
		
		final boolean newLoop = loop;
		
		if(activity != null){
			activity.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					threadPlayVideo(newVideoResolutions, newVideoUrl, newLoop);
					
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

	protected synchronized void threadPlayVideo(ArrayList<VideoResolution> videoResolutions,
			String videoUrl, boolean loop) {
		int type = 0;
		String newVideoUrl = videoUrl;
		int resolutionIndex = 0;
//		String resolution;
		if(videoResolutions != null && videoResolutions.size() > 0){
			if (!isDisplayChannelImage && intShowControl != 2) {
				Log.d(TAG, "loading 22a: " + true);
//				if(!isLoading){
//					isLoading = true;
//					setVisibility(loadingLayout, View.VISIBLE);
//				}
				setVisibility(loadingLayout, View.VISIBLE);
			} else {
				Log.d(TAG, "loading 22b: " + false);
//				if(isLoading){
//					isLoading = false;
//					setVisibility(loadingLayout, View.GONE);
//				}
				setVisibility(loadingLayout, View.GONE);
			}
			boolean isExist = false;
			if(this.codecResolution != null && this.codec != null){
				VideoResolution newVideoResolution = null;
				ArrayList<VideoResolution> videoResolutionList = new ArrayList<VideoResolution>();
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
					if(this.codec.equals(codec)){
						videoResolutionList.add(videoResolution);
						videoResolution.setIndex(i);
						if(this.codecResolution.equals(key.toString().trim())){
							newVideoResolution = videoResolution;
							break;
						}
					}
				}
				
				if(newVideoResolution == null && videoResolutionList.size() > 0){
					newVideoResolution = videoResolutionList.get(0);
					int intOldResolution = getIntResolution(this.resolution);
					int intNewResolution = getIntResolution(newVideoResolution.getResolution());
					int minResolution = Math.abs(intNewResolution - intOldResolution);
					for(int k = 1; k < videoResolutionList.size(); k++){
						intNewResolution = getIntResolution(videoResolutionList.get(k).getResolution());
						int min = Math.abs(intNewResolution - intOldResolution);
						if(minResolution > min){
							newVideoResolution = videoResolutionList.get(k);
							minResolution = min;
						}
					}
				}
				
				if(newVideoResolution != null){
					String codec = newVideoResolution.getCodec();
					String resolution = newVideoResolution.getResolution();
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
					
					resolutionIndex = newVideoResolution.getIndex();
					Log.d(TAG, "Codec - resolutionIndex 1: " + resolutionIndex);
					newVideoUrl = newVideoResolution.getVideoUrl();
					updateVideoSize(resolution);
					
					this.codecResolution = key.toString();
					this.codec = codec;
					isExist = true;
				} 
				
//				for(int i = 0; i < videoResolutions.size(); i++){
//					VideoResolution videoResolution = videoResolutions.get(i);
//					String codec = videoResolution.getCodec();
//					String resolution = videoResolution.getResolution();
//					StringBuffer key = new StringBuffer();
//					if(codec != null){
//						key.append(codec.trim());
//					}
//					if(resolution != null){
//						key.append("-").append(resolution.trim());
//					}
//					if(this.codecResolution.equals(key.toString().trim()) && this.codec.equals(codec)){
//						
//						Log.d(TAG, "Codec - Resolution: " + this.codecResolution);
//						if("h264".equals(codec)){
//							type = 0;
//						}else{
//							type = 1;
//						}
//						
//						resolutionIndex = i;
//						Log.d(TAG, "Codec - resolutionIndex 1: " + resolutionIndex);
//						newVideoUrl = videoResolution.getVideoUrl();
//						
//						updateVideoSize(resolution);
//						isExist = true;
//						break;
//					}
//				}
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
					this.resolution = resolution.trim();
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
			cancelTask();
		}
		
//		newVideoUrl = "http://f.vp9.tv/music/tru_tinh/Thuong_hoai_ngan_nam-Phuong_Diem_Hanh/mvhd.SD5.mp4";
//		type = 1;
		if(type == 1){
			vp9Player = new H265Vp9Player(this);
			vp9Player.playVideo(newVideoUrl, false);
		}else{
			vp9Player = new NativeVp9Player(this);
			vp9Player.playVideo(newVideoUrl, false);
		}
		Log.d(TAG, "Codec - resolutionIndex 5: " + resolutionIndex);
		createPopupMenu(subInfoArr, videoResolutions, resolutionIndex);
		
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
		setTextForTextView();
		setVisibility(logoVideo, View.GONE);
		setVisibility(videoTitle, View.GONE);
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
			this.loadSub = new LoadViewTask(this, subtitleInfos, this.mVideoView, vp9Player, Boolean.valueOf(false), this.tvSubs, this.pdLoading);
			this.loadSub.execute(new Void[0]);
//			setBackgroundResource(btnSub, vp9_btn_sub_id);
		} else {
//			setBackgroundResource(btnSub, vp9_btn_sub_hide_id);
			if (this.loadSub != null) {
				this.loadSub.cancelTask();
			}

			if (tvSubs != null) {
				for (int i = 0; i < tvSubs.length; i++) {
					setVisibility(tvSubs[i], View.GONE);
				}
			}
		}
	}

	public void createPopupMenu(SubtitleInfo[] subtitleInfos, ArrayList<VideoResolution> videoResolutions, int resolutionIndex) {
		if (clearParam || (subtitleInfos == null && (videoResolutions == null || videoResolutions.size() == 0))) {
			setBackgroundResource(btnSub, vp9_btn_sub_hide_id);
			setEnabled(btnSub, false);
			return;
		}
		setBackgroundResource(btnSub, vp9_btn_sub_id);
		setEnabled(btnSub, true);
		
		createSettingPopup(subtitleInfos, videoResolutions, resolutionIndex);
		

	}

	public CheckBox[] checkboxSubtitle;


	public void createSettingPopup(final SubtitleInfo[] subtitleInfos, final ArrayList<VideoResolution> videoResolutions, final int resolutionIndex) {

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
				LinearLayout layoutSubtitle = (LinearLayout) layoutOfPopup.findViewById(R.id.layoutSubtitle);
				TextView tvSubtitle = (TextView) layoutOfPopup.findViewById(R.id.tvSubtitle);
				
				LinearLayout layoutResolution = (LinearLayout) layoutOfPopup.findViewById(R.id.layoutResolution);
				TextView tvResolution = (TextView) layoutOfPopup.findViewById(R.id.tvResolution);
				
				if(subtitleInfos != null && subtitleInfos.length > 0){
//					LinearLayout layoutOfPopup = new LinearLayout(activity);
//					LayoutParams params = new LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
//					layoutOfPopup.setLayoutParams(params);
//					settingPopup = new PopupWindow(layoutOfPopup, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					checkboxSubtitle = new CheckBox[subtitleInfos.length];

					setVisibility(tvSubtitle, View.VISIBLE);
					
					
//					LinearLayout layoutSubtitle = new LinearLayout(activity);
//					LayoutParams subParams = new LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
//					layoutSubtitle.setLayoutParams(subParams);
//					TextView tvSub = new TextView(activity);
//					tvSub.setText("Ph? �?");
//					layoutSubtitle.addView(tvSub);
					layoutSubtitle.setOrientation(LinearLayout.VERTICAL);
					for (int i = 0; i < subtitleInfos.length; i++) {
						checkboxSubtitle[i] = new CheckBox(getActivity());
						checkboxSubtitle[i].setText(subtitleInfos[i].getSubTypeName());
						checkboxSubtitle[i].setTag(i);
						checkboxSubtitle[i].setTextSize(35);
						checkboxSubtitle[i].setTextColor(Color.BLACK);
						checkboxSubtitle[i].setPadding(checkboxSubtitle[i].getPaddingLeft() + (int)(10.0f * scale + 0.5f),
								checkboxSubtitle[i].getPaddingTop(),
								checkboxSubtitle[i].getPaddingRight(),
								checkboxSubtitle[i].getPaddingBottom());

						layoutSubtitle.addView(checkboxSubtitle[i]);

						final int index = i;
						if (subtitleInfos[i].isDisplay()) {
							checkboxSubtitle[i].setChecked(true);
							setVisibility(tvSubs[index], View.VISIBLE);		
							setVisibility(tvSubs[index+2], View.VISIBLE);		
						} else {
							checkboxSubtitle[i].setChecked(false);
							setVisibility(tvSubs[index], View.GONE);			
							setVisibility(tvSubs[index+2], View.GONE);			
						}
						checkboxSubtitle[i].setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								CheckBox cb = (CheckBox) v;
								int index = Integer.valueOf(cb.getTag().toString());
								
								if (cb.isChecked()) {
									setVisibility(tvSubs[index], View.VISIBLE);
									setVisibility(tvSubs[index+2], View.VISIBLE);
									 setColorForSubtitle(tvSubs[index], subInfoArr[index].getSubType());
									 setColorForSubtitle(tvSubs[index + 2], subInfoArr[index].getSubType());
								} else {
									setVisibility(tvSubs[index], View.GONE);
									setVisibility(tvSubs[index+2], View.GONE);
								}
								
								settingPopup.dismiss();
								sendSubtitlesInfoToRemote();
							}
						});
					}
				}else{
					setVisibility(tvSubtitle, View.GONE);
				}
				

				if (videoResolutions != null && videoResolutions.size() > 0) {
					
				
					setVisibility(tvResolution, View.VISIBLE);
					
			//		LinearLayout layoutResolution = new LinearLayout(activity);
			//		LayoutParams resolutionParams = new LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
			//		layoutResolution.setLayoutParams(resolutionParams);
					
			//		TextView tvResolution = new TextView(activity);
			//		tvResolution.setText("�? ph�n gi?i");
			//		layoutResolution.addView(tvResolution);
					
					RadioGroup radioGroup = new RadioGroup(getActivity());
					
					for(int i = 0; i < videoResolutions.size(); i++){
						VideoResolution videoResolution = videoResolutions.get(i);
						String resolutionName = getResolutionName(videoResolution);
						String codec = videoResolution.getCodec();
						Log.d(TAG, "ResolutionName: " + resolutionName + "/" + codec);
						RadioButton radioButton = new RadioButton(getActivity());
						radioButton.setText(resolutionName);
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
						if(resolutionIndex == i){
							Log.d(TAG, "Resolution - choise");
							radioButton.setChecked(true);
						}
					}
					radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() 
				        {
				            public void onCheckedChanged(RadioGroup group, int checkedId) {
			//	            	group.indexOfChild(child)
				            	View view = group.findViewById(checkedId);
				            	if(view != null){
					            	int index = Integer.valueOf(view.getTag().toString());
					            	settingPopup.dismiss();
					            	handleResolutionRadioButton(index);
				            	}
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
				settingPopup.setOnDismissListener(new DismissPopupWindowListener());
			}
		});


	}

	protected void handleResolutionRadioButton(int checkedId) {
		Log.d(TAG, "handleResolutionRadioButton: " + checkedId);
		Log.d(TAG, "handleResolutionRadioButton: " + this.videoResolutions.size());
		if(this.videoResolutions != null && checkedId < this.videoResolutions.size() && checkedId >= 0){
			if (!isDisplayChannelImage && intShowControl != 2) {
				Log.d(TAG, "loading 1: " + true);
//				if(!isLoading){
//					isLoading = true;
//					setVisibility(loadingLayout, View.VISIBLE);
//				}
				setVisibility(loadingLayout, View.VISIBLE);
			} else {
				Log.d(TAG, "loading 1: " + false);
//				if(isLoading){
//					isLoading = false;
//					setVisibility(loadingLayout, View.GONE);
//				}
				setVisibility(loadingLayout, View.GONE);
			}
			VideoResolution videoResolution = this.videoResolutions.get(checkedId);
			String codec = videoResolution.getCodec();
			int type;
			if("h265".equals(codec)){
				type = 1;
			}else{
				type = 0;
			}
			String newVideoUrl = videoResolution.getVideoUrl();
			Log.d(TAG, "handleResolutionRadioButton - type: " + type);
			Log.d(TAG, "handleResolutionRadioButton - VideoUrl: " + newVideoUrl);

			if(vp9Player != null){
				vp9Player.release();
				cancelTask();
			}
			StringBuffer key = new StringBuffer();
			String resolution = videoResolution.getResolution();
			if(codec != null){
				key.append(codec.trim());
			}
			if(resolution != null){
				this.resolution = resolution.trim();
				key.append("-").append(resolution.trim());
			}
			this.codecResolution = key.toString();
			this.codec = codec;
			if(type == 1){
				vp9Player = new H265Vp9Player(this);
				vp9Player.playVideo(newVideoUrl, false);
			}else{
				vp9Player = new NativeVp9Player(this);
				vp9Player.playVideo(newVideoUrl, false);
			}
		}
		
		if(settingPopup != null){
			settingPopup.dismiss();
		}
	}

	private String getResolutionName(VideoResolution videoResolution) {
		String resolutionName = "";
		
		String quality = videoResolution.getQuality();
//		
//		String codec = videoResolution.getCodec();
		
		String resolution = videoResolution.getResolution();
		
//		if(quality != null && !"".equals(quality.trim())){
//			resolutionName += quality + ": ";
//		}
//		String subFix = "";
//		String url = videoResolution.getVideoUrl();
//		if(url != null){
//			url = url.trim();
//			int index = url.lastIndexOf(".");
//			if(index != -1){
//				subFix = url.substring(index + 1, url.length());
//			}
//		}
//
//		if(codec != null && !"".equals(codec.trim())){
//			resolutionName += codec;
//		}
		
//		if(resolution != null && !"".equals(resolution.trim())){
//			if(subFix != null && !"".equals(subFix)){
//				resolutionName += "(" + subFix.toUpperCase() + ": " + resolution + ")";
//			}else{
//				resolutionName += "(" + resolution + ")";
//			}
//			
//		}
		if(resolution != null && !"".equals(resolution.trim())){
			resolutionName = resolution;
		}else if(quality != null && !"".equals(quality.trim())){
			resolutionName = quality;
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
			if (hide == 1) {
				hideController();
			}

//			setVisibility(controllerLayout, View.VISIBLE);

			if (intShowControl != 2) {
				if (!isDisplayChannelImage) {
					if (state == 1) {
						setVisibility(videoTitleLayout, View.VISIBLE);
					}
					if (videoType == 0 || (videoType > 1 && isLive == 0)) {
						setVisibility(progessLayout, View.VISIBLE);
					}
				}
			} else {
				setVisibility(videoTitleLayout, View.GONE);
				setVisibility(progessLayout, View.GONE);
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
			if (this.updateTimehandle != null) {
				this.updateTimehandle.removeCallbacks(this.mUpdateTimeTask);
			}

			if (this.mResumErrorHandle != null) {
				this.mResumErrorHandle.removeCallbacks(this.mResumErrorVideoTask);
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

			if (this.timeShowHandle != null) {
				this.timeShowHandle.removeCallbacks(this.timeShowTask);
			}
			
//			if (this.startVideoHandler != null) {
//				this.startVideoHandler.removeCallbacks(startVideoRunnable);
//			}

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
			settingPopup.showAsDropDown(view, -5, 0);
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
		if (action == MotionEvent.ACTION_DOWN) {
			if (intShowControl == 0) {
				showController(1);
			} else {
				intShowControl = 0;
				setVisibility(controllerLayout, View.GONE);
			}
		}
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
				mVideoView.setLayoutParams(lp);
				mHolder.setFixedSize(lp.width, lp.height);
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
				timeShowHandle.removeCallbacks(timeShowTask);
				setVisibility(controllerLayout, View.VISIBLE);
				// setVisibility(logoChannel, View.GONE);
				// setVisibility(logoText, View.GONE);
				setVisibility(logoLayout, View.GONE);
//				setVisibility(mVideoView, View.GONE);
				setVisibility(vp9ChannelImage, View.GONE);
				Log.d(TAG, "loading 35: " + false);
//				if(isLoading){
//					isLoading = false;
//					setVisibility(loadingLayout, View.GONE);
//				}
				setVisibility(loadingLayout, View.GONE);
				setVisibility(subtitlesLayout, View.GONE);
				setVisibility(progessLayout, View.GONE);
//				 mVideoView.setVisibility(View.GONE);
				// vp9ChannelImage.setVisibility(View.GONE);
				// loadingLayout.setVisibility(View.GONE);
				// subtitlesLayout.setVisibility(View.GONE);
				// progessLayout.setVisibility(View.GONE);
				setVisibility(videoTitleLayout, View.GONE);
				// }
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
				if(!isRightDisplay){
					timeShowHandle.postDelayed(timeShowTask, 1000L);
				}
				if (isDisplayChannelImage) {
					vp9ChannelImage.setVisibility(View.VISIBLE);
					Log.d(TAG, "loading 23: " + false);
//					if(isLoading){
//						isLoading = false;
//						setVisibility(loadingLayout, View.GONE);
//					}
					setVisibility(loadingLayout, View.GONE);
				} else {
					setVisibility(videoTitleLayout, View.VISIBLE);
					setVisibility(logoLayout, View.VISIBLE);
//					mVideoView.setVisibility(View.VISIBLE);
//					subtitlesLayout.setVisibility(View.VISIBLE);
					setVisibility(subtitlesLayout, View.VISIBLE);
					if (videoType == 0 || (videoType > 1 && isLive == 0)) {
						setVisibility(progessLayout, View.VISIBLE);
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
					if (CopyOfMediaController_1jan2015.this.sbFull != null) {
						CopyOfMediaController_1jan2015.this.sbFull.setSecondaryProgress(progress);
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
		} else {
			setVisibility(vp9ChannelImage, View.GONE);
		}
//		if(isLoading){
//			isLoading = false;
//			setVisibility(loadingLayout, View.GONE);
//		}
		setVisibility(loadingLayout, View.GONE);
		setVisibility((RelativeLayout) activity.findViewById(loading_layout_id), View.GONE);
		setVisibility((RelativeLayout) activity.findViewById(subtitles_layout_id), View.GONE);
		timeShowHandle.removeCallbacks(timeShowTask);
	}

	public void invisibleChanelTiviImage() {
		if (clearParam) {
			return;
		}
		this.isDisplayChannelImage = false;
		// controllerView =
		// (RelativeLayout)activity.findViewById(controller_id);
		// RelativeLayout vp9PlayerLayout =
		// (RelativeLayout)activity.findViewById(vp9_player_layout_id);
		setVisibility(vp9ChannelImage, View.GONE);
		if (intShowControl != 2) {
			setVisibility(mVideoView, View.VISIBLE);
			setVisibility((RelativeLayout) activity.findViewById(loading_layout_id), View.VISIBLE);
			setVisibility((RelativeLayout) activity.findViewById(subtitles_layout_id), View.VISIBLE);
		} else {
			setVisibility(mVideoView, View.GONE);
			setVisibility((RelativeLayout) activity.findViewById(loading_layout_id), View.GONE);
			setVisibility((RelativeLayout) activity.findViewById(subtitles_layout_id), View.GONE);
		}
	}



	// /////////////////////// RUNNABLE /////////////////////////

	public Runnable mCheckPlayVideoTask1 = new Runnable() {
		public void run() {
			if (clearParam) {
				return;
			}
			if (isLive == 1) {
				ServerTimeInfo serverTimeInfo = new ServerTimeInfo(serverTimeUrl);
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
					ServerTimeInfo serverTimeInfo = new ServerTimeInfo(serverTimeUrl);
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
			} else {
//				if(isLoading){
//					isLoading = false;
//					setVisibility(loadingLayout, View.GONE);
//				}
				setVisibility(loadingLayout, View.GONE);
			}
			Log.d(TAG, "mCheckPlayVideoTask - isLive: " + isLive);
			if (isLive == 1) {
				ServerTimeInfo serverTimeInfo = new ServerTimeInfo(serverTimeUrl);
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
						ServerTimeInfo serverTimeInfo = new ServerTimeInfo(serverTimeUrl);
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
	

	// public Runnable mCheckPlayVideoTask2 = new Runnable() {
	// public String errorMsg;
	//
	// public void run(){
	// Log.d(TAG, "mCheckPlayVideoTask - isLive: " + isLive);
	// if(isLive == 1){
	// ServerTimeInfo serverTimeInfo = new ServerTimeInfo(serverTimeUrl);
	// int secondInDay = serverTimeInfo.getSecondInDay() + 1;
	// int curIndex = demandTiviSchedule.getCurrentIndex();
	// int curChildIndex = demandTiviSchedule.getCurrentChildIndex();
	// VideoResult curVideoInfo =
	// demandTiviSchedule.getVideoInfoByIndex(curIndex);
	//
	// if(curVideoInfo != null && curVideoInfo.getVideoInfo() != null &&
	// curVideoInfo.getVideoInfo().getChildVideoInfoByIndex(curChildIndex + 1)
	// != null){
	// VideoInfo curChildVideo =
	// curVideoInfo.getVideoInfo().getChildVideoInfoByIndex(curChildIndex + 1);
	// int waittime = curChildVideo.getIntStartTimeBySeconds() - secondInDay;
	// Log.d(TAG, "mCheckPlayVideoTask - curChildVideo: " + waittime);
	// startDemandTivi(curChildVideo, 0, isLive);
	// videoIndex = curVideoInfo.getVideoInfo().getIndex();
	// childVideoIndex = curChildVideo.getIndex();
	// // if(isDisplayChannelImage){
	// // invisibleChanelTiviImage();
	// // }
	// // if(waittime <= 180){
	// // startDemandTivi(curChildVideo, 0, isLive);
	// // videoIndex = curVideoInfo.getVideoInfo().getIndex();
	// // childVideoIndex = curChildVideo.getIndex();
	// // }else{
	// // if(!isDisplayChannelImage){
	// // setChanelTiviImage();
	// // }
	// //
	// // long waittingTime =
	// demandTiviSchedule.getWaitingTime(serverTimeInfo,
	// curChildVideo.getIndex(), 1000);
	// //
	// mCheckPlayDemandVideoHandle2.removeCallbacks(mCheckPlayVideoTask2);
	// //
	// mCheckPlayDemandVideoHandle2.postDelayed(mCheckPlayVideoTask2,
	// waittingTime);
	// // }
	// return;
	// }else if(curVideoInfo == null || curVideoInfo.getVideoInfo() == null){
	// if(!isDisplayChannelImage){
	// setChanelTiviImage();
	// }
	//
	//
	// VideoResult videoResult =
	// demandTiviSchedule.getVideoInfoByTime(secondInDay);
	// if(videoResult == null || videoResult.getVideoInfo() == null){
	//
	// if(videoResult != null && videoResult.getNextIndex() != -1){
	// long waittingTime =
	// demandTiviSchedule.getWaitingTime(serverTimeInfo,
	// videoResult.getNextIndex(), 1000);
	// mCheckPlayDemandVideoHandle2.removeCallbacks(mCheckPlayVideoTask2);
	// mCheckPlayDemandVideoHandle2.postDelayed(mCheckPlayVideoTask2,
	// waittingTime);
	// }else{
	// mCheckPlayDemandVideoHandle2.removeCallbacks(mCheckPlayVideoTask2);
	// mCheckPlayDemandVideoHandle2.postDelayed(mCheckPlayVideoTask2,
	// 1000);
	// }
	// }else{
	// VideoInfo videoInfo = videoResult.getVideoInfo();
	// Log.d(TAG, "Play Video with index/current2: " + videoInfo.getIndex() +
	// "/" + demandTiviSchedule.getCurrentIndex());
	// VideoInfo childVideoInfo =
	// videoInfo.getChildVideoInfoByTime(secondInDay);
	// if(childVideoInfo != null){
	// Log.d(TAG, "Play child video: " + childVideoInfo.getIndex());
	// startDemandTivi(childVideoInfo, secondInDay -
	// childVideoInfo.getIntStartTimeBySeconds(), isLive);
	// videoIndex = videoInfo.getIndex();
	// childVideoIndex = childVideoInfo.getIndex();
	// }else{
	// if(!isDisplayChannelImage){
	// setChanelTiviImage();
	// }
	// mCheckPlayDemandVideoHandle2.removeCallbacks(mCheckPlayVideoTask2);
	// mCheckPlayDemandVideoHandle2.postDelayed(mCheckPlayVideoTask2,
	// 1000);
	//
	// }
	// }
	// //
	// mCheckPlayDemandVideoHandle2.removeCallbacks(mCheckPlayVideoTask2);
	// //
	// mCheckPlayDemandVideoHandle2.postDelayed(mCheckPlayVideoTask2,
	// 1000);
	// this.errorMsg = "Lá»—i lá»‹ch chiáº¿u cá»§a kĂªnh #1";
	// Log.d(TAG, "mCheckPlayVideoTask - errorMsg: " + errorMsg);
	// return;
	// }else
	// if(curVideoInfo.getVideoInfo().getChildVideoInfoByIndex(curChildIndex +
	// 1) == null){
	// Log.d(TAG, "mCheckPlayVideoTask - curChildVideo: NULL");
	// curVideoInfo =
	// demandTiviSchedule.getVideoInfoByIndex(curIndex + 1);
	// if(curVideoInfo != null && curVideoInfo.getVideoInfo() != null){
	// Log.d(TAG, "mCheckPlayVideoTask - curVideoInfo:1");
	// VideoInfo curVideo = curVideoInfo.getVideoInfo();
	// int waittime = curVideo.getIntStartTimeBySeconds() - secondInDay;
	// VideoInfo curChildVideo = curVideo.getChildVideoInfoByIndex(0);
	// if (waittime <= 180 && curChildVideo != null) {
	// Log.d(TAG, "mCheckPlayVideoTask - curVideoInfo:2");
	// startDemandTivi(curChildVideo, 0, isLive);
	// videoIndex = curVideoInfo.getVideoInfo().getIndex();
	// childVideoIndex = curChildVideo.getIndex();
	// return;
	// }else if(curChildVideo != null){
	// Log.d(TAG, "mCheckPlayVideoTask - curVideoInfo:3");
	// if(!isDisplayChannelImage){
	// setChanelTiviImage();
	// }
	// mCheckPlayDemandVideoHandle2.removeCallbacks(mCheckPlayVideoTask2);
	// mCheckPlayDemandVideoHandle2.postDelayed(mCheckPlayVideoTask2,
	// waittime);
	// return;
	// }else{
	// if(curVideoInfo.getNextIndex() != -1){
	// Log.d(TAG, "mCheckPlayVideoTask - curVideoInfo:4");
	// VideoResult tempCurVideoInfo =
	// demandTiviSchedule.getVideoInfoByIndex(curVideoInfo.getNextIndex());
	// if(tempCurVideoInfo != null && tempCurVideoInfo.getVideoInfo() != null){
	// Log.d(TAG, "mCheckPlayVideoTask - curVideoInfo:5");
	// waittime = tempCurVideoInfo.getVideoInfo().getIntStartTimeBySeconds() -
	// secondInDay;
	// if(!isDisplayChannelImage){
	// setChanelTiviImage();
	// }
	// mCheckPlayDemandVideoHandle2.removeCallbacks(mCheckPlayVideoTask2);
	// mCheckPlayDemandVideoHandle2.postDelayed(mCheckPlayVideoTask2,
	// waittime);
	// return;
	// }
	//
	// }
	//
	// if(!isDisplayChannelImage){
	// setChanelTiviImage();
	// }
	// mCheckPlayDemandVideoHandle2.removeCallbacks(mCheckPlayVideoTask2);
	// mCheckPlayDemandVideoHandle2.postDelayed(mCheckPlayVideoTask2,
	// 1000);
	// return;
	// }
	// }else{
	// int flag = demandTiviSchedule.checkTime(serverTimeInfo);
	// if(flag == 1){
	// String cheduleUrl = getCheduleUrl(serverUrl, channelId,
	// serverTimeInfo.getStrdate());
	// demandTiviSchedule.parserCheduleUrl2(cheduleUrl);
	// demandTiviSchedule.setStrdate(serverTimeInfo.getStrdate());
	//
	//
	// VideoResult videoResult =
	// demandTiviSchedule.getVideoInfoByTime(secondInDay);
	// if(videoResult == null || videoResult.getVideoInfo() == null){
	// // Error
	// if(!isDisplayChannelImage){
	// setChanelTiviImage();
	// }
	// if(videoResult != null && videoResult.getNextIndex() != -1){
	// long waittingTime =
	// demandTiviSchedule.getWaitingTime(serverTimeInfo,
	// videoResult.getNextIndex(), 1000);
	// mCheckPlayDemandVideoHandle2.removeCallbacks(mCheckPlayVideoTask2);
	// mCheckPlayDemandVideoHandle2.postDelayed(mCheckPlayVideoTask2,
	// waittingTime);
	// }else{
	// mCheckPlayDemandVideoHandle2.removeCallbacks(mCheckPlayVideoTask2);
	// mCheckPlayDemandVideoHandle2.postDelayed(mCheckPlayVideoTask2,
	// 1000);
	// }
	// }else{
	// VideoInfo videoInfo = videoResult.getVideoInfo();
	// Log.d(TAG, "Play Video with index/current: " + videoInfo.getIndex() + "/"
	// + demandTiviSchedule.getCurrentIndex());
	// VideoInfo childVideoInfo =
	// videoInfo.getChildVideoInfoByTime(secondInDay);
	// if(childVideoInfo != null){
	// Log.d(TAG, "Play child video: " + childVideoInfo.getIndex());
	// startDemandTivi(childVideoInfo, secondInDay -
	// childVideoInfo.getIntStartTimeBySeconds(), isLive);
	// videoIndex = videoInfo.getIndex();
	// childVideoIndex = childVideoInfo.getIndex();
	// }else{
	// Log.d(TAG, "mCheckPlayVideoTask - curVideoInfo:4");
	//
	// if(!isDisplayChannelImage){
	// setChanelTiviImage();
	// }
	//
	// VideoInfo tempChildVideoInfo =
	// videoInfo.getChildVideoInfoByTime(childVideoInfo.getIndex() + 1);
	// if(tempChildVideoInfo != null){
	// long waittingTime = (tempChildVideoInfo.getIntStartTimeBySeconds() -
	// secondInDay)*1000;
	// mCheckPlayDemandVideoHandle2.removeCallbacks(mCheckPlayVideoTask2);
	// mCheckPlayDemandVideoHandle2.postDelayed(mCheckPlayVideoTask2,
	// waittingTime);
	// }else{
	// mCheckPlayDemandVideoHandle2.removeCallbacks(mCheckPlayVideoTask2);
	// mCheckPlayDemandVideoHandle2.postDelayed(mCheckPlayVideoTask2,
	// 1000);
	// }
	// }
	// }
	//
	// }else{
	// Log.d(TAG, "mCheckPlayVideoTask - curVideoInfo:5");
	// if(!isDisplayChannelImage){
	// setChanelTiviImage();
	// }
	// int waitingTime = (24*60*60 - secondInDay)*1000;
	// mCheckPlayDemandVideoHandle2.removeCallbacks(mCheckPlayVideoTask2);
	// mCheckPlayDemandVideoHandle2.postDelayed(mCheckPlayVideoTask2,
	// waitingTime);
	// return;
	// }
	// }
	// }
	//
	// //
	// //
	// //
	// // int flag =
	// demandTiviSchedule.checkTime(serverTimeInfo);
	// // if(flag == 1){
	// // String cheduleUrl = getCheduleUrl(serverUrl, channelId,
	// serverTimeInfo.getStrdate());
	// // demandTiviSchedule.parserCheduleUrl2(cheduleUrl);
	// //
	// demandTiviSchedule.setStrdate(serverTimeInfo.getStrdate());
	// // }
	// // VideoResult videoResult =
	// demandTiviSchedule.getVideoInfoByTime(secondInDay);
	// // if(videoResult == null || videoResult.getVideoInfo() == null){
	// // // Error
	// // if(!isDisplayChannelImage){
	// // setChanelTiviImage();
	// // }
	// // if(videoResult != null && videoResult.getNextIndex() != -1){
	// // long waittingTime =
	// demandTiviSchedule.getWaitingTime(serverTimeInfo,
	// videoResult.getNextIndex(), 1000);
	// //
	// mCheckPlayDemandVideoHandle2.removeCallbacks(mCheckPlayVideoTask2);
	// //
	// mCheckPlayDemandVideoHandle2.postDelayed(mCheckPlayVideoTask2,
	// waittingTime);
	// // }else{
	// //
	// mCheckPlayDemandVideoHandle2.removeCallbacks(mCheckPlayVideoTask2);
	// //
	// mCheckPlayDemandVideoHandle2.postDelayed(mCheckPlayVideoTask2,
	// 1000);
	// // }
	// // }else{
	// // VideoInfo videoInfo = videoResult.getVideoInfo();
	// // Log.d(TAG, "Play Video with index/current: " + videoInfo.getIndex() +
	// "/" + demandTiviSchedule.getCurrentIndex());
	// // VideoInfo childVideoInfo =
	// videoInfo.getChildVideoInfoByTime(secondInDay);
	// // if(childVideoInfo != null){
	// // Log.d(TAG, "Play child video: " + childVideoInfo.getIndex());
	// // startDemandTivi(childVideoInfo, secondInDay -
	// childVideoInfo.getIntStartTimeBySeconds(), isLive);
	// // videoIndex = videoInfo.getIndex();
	// // childVideoIndex = childVideoInfo.getIndex();
	// // }else{
	// // if(!isDisplayChannelImage){
	// // setChanelTiviImage();
	// // }
	// //
	// mCheckPlayDemandVideoHandle2.removeCallbacks(mCheckPlayVideoTask2);
	// //
	// mCheckPlayDemandVideoHandle2.postDelayed(mCheckPlayVideoTask2,
	// 1000);
	// //
	// // }
	// // }
	// }else{
	// int curIndex = demandTiviSchedule.getCurrentIndex();
	// VideoResult videoResult =
	// demandTiviSchedule.getVideoInfoByIndex(curIndex);
	// VideoInfo childVideo = null;
	// if(videoResult != null && videoResult.getVideoInfo() != null){
	// childVideo =
	// videoResult.getVideoInfo().getChildVideoInfoByIndex(childVideoIndex);
	// }
	// if(childVideo != null){
	//
	// startDemandTivi(childVideo, 0, isLive);
	// videoIndex = videoResult.getVideoInfo().getIndex();
	// childVideoIndex = childVideo.getIndex();
	//
	// }else {
	// videoResult =
	// demandTiviSchedule.getVideoInfoByIndex(curIndex + 1);
	// childVideoIndex = 0;
	// if(videoResult != null && videoResult.getVideoInfo() != null){
	// childVideo =
	// videoResult.getVideoInfo().getChildVideoInfoByIndex(childVideoIndex);
	// startDemandTivi(childVideo, 0, isLive);
	// videoIndex = videoResult.getVideoInfo().getIndex();
	// childVideoIndex = childVideo.getIndex();
	// // if(isDisplayChannelImage){
	// // invisibleChanelTiviImage();
	// // }
	// }else{
	// ServerTimeInfo serverTimeInfo = new ServerTimeInfo(serverTimeUrl);
	// Calendar calendar = Calendar.getInstance();
	// calendar.set(serverTimeInfo.getYear(), serverTimeInfo.getMonth(),
	// serverTimeInfo.getDay());
	// calendar.add(Calendar.DATE, 1);
	// SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
	// String strTomorow = dateFormat.format(calendar.getTime());
	// String cheduleUrl = getCheduleUrl(serverUrl, channelId, strTomorow);
	// demandTiviSchedule.parserCheduleUrl2(cheduleUrl);
	// demandTiviSchedule.setStrdate(serverTimeInfo.getStrdate());
	// videoIndex = 0;
	// videoResult =
	// demandTiviSchedule.getVideoInfoByIndex(videoIndex);
	// if(videoResult == null || videoResult.getVideoInfo() == null){
	// if(!isDisplayChannelImage){
	// setChanelTiviImage();
	// }
	// }else{
	// childVideo =
	// videoResult.getVideoInfo().getChildVideoInfoByIndex(childVideoIndex);
	// startDemandTivi(childVideo, 0, isLive);
	// videoIndex = videoResult.getVideoInfo().getIndex();
	// childVideoIndex = childVideo.getIndex();
	// // if(isDisplayChannelImage){
	// // invisibleChanelTiviImage();
	// // }
	// }
	// }
	// }
	// }
	//
	//
	// }
	//
	// };

	public Runnable hideTask = new Runnable() {
		public void run() {
			
			
			if (clearParam) {
				return;
			}
			
			if (intShowControl == 2 || showControlLayout) {
				return;
			}
			setVisibility(controllerLayout, View.GONE);
//			setVisibility(videoTitleLayout, View.GONE);
			intShowControl = 0;
			isTouch = false;
			try {
				activity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						getActivity().getWindow().getDecorView().setSystemUiVisibility(1);
						getActivity().getWindow().getDecorView().setSystemUiVisibility(2);
						if (popupVideoWindow != null && popupVideoWindow.isShowing()) {
							popupVideoWindow.dismiss();

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
						waiting
						
						
						setVisibility(loadingLayout, View.VISIBLE);
						if(CopyOfMediaController_1jan2015.this.isFirstPlay){
							count++;
							if(count != 20){
								if (state == 1) {
									updateTimehandle.postDelayed(this, 500L);
								}
								return;
							}else{
								count = 0;
								CopyOfMediaController_1jan2015.this.isFirstPlay = false;
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
							setTextForTextView(notifyTextView, errorMsg);
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
						
						startThread.start();
							return;
						}

					} else {
						count = 0;
						CopyOfMediaController_1jan2015.this.isFirstPlay = false;
						setTextForTextView(notifyTextView, "");
						setVisibility(loadingLayout, View.GONE);
						setVisibility(loadingLayout, View.GONE);
						
						
						if(startTimeError != 0){
							sendErrorMsgToServer();
						}
					}

					isFirstDisconnect = true;
					
					curPosition = l1;

					isResume = false;
					// if(l2 != duration){
					duration = l2;
					setTextForTextView(tvTo, Utilities.milliSecondsToTimer(l2));
					// }
					setTextForTextView(tvFrom, Utilities.milliSecondsToTimer(l1));
					int percentage = Utilities.getProgressPercentage(l1, l2);
					setProgressForSeekBar(sbFull, percentage);

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
	
	

	public int timeShowIndex = 0;

	public Runnable timeShowTask = new Runnable() {
		
		public void run() {
			if (clearParam || isRightDisplay) {
				return;
			}
			
			if (timeShowList != null && timeShowList.size() > 0 && timeShowIndex < timeShowList.size()) {
				TimeShow timeShow = timeShowList.get(timeShowIndex);
				if (timeShow.getType() == 0) { // xuat hien theo tan xuat
					int logoType = timeShow.getLogoType();
					switch (logoType) {
					case 1:
						Log.d(TAG, "set logoVideo on Prepared");
						setImageBitmap(logoChannel, timeShow.getUrl());
						setVisibility(logoChannel, View.VISIBLE);
						setVisibility(logoText, View.GONE);
						break;

					case 2:
						setTextForTextView(logoText, "<b>" + timeShow.getContent() + "</b>");
						setVisibility(logoText, View.VISIBLE);
						setVisibility(logoChannel, View.GONE);
						break;

					default:
						setVisibility(logoChannel, View.GONE);
						setVisibility(logoText, View.GONE);
						break;
					}
				}
				timeShowIndex = (timeShowIndex + 1) % timeShowList.size();
				timeShowHandle.postDelayed(timeShowTask, timeShow.getInterval());
			}
		}
	};

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
			if (CopyOfMediaController_1jan2015.this.vp9Player.getCurrentPosition() <= 1000) {

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
	
	

	public void setTextForTextView(final TextView textView, final String text) {
		if (activity != null && textView != null) {
			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					textView.setText(Html.fromHtml(text));
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
		int currentPosition = vp9Player.getCurrentPosition();
		
		
		return currentPosition;
		
	}

	public JSONArray changeSubtitle(ArrayList<ChangeSubtitle> changeSubtitles) {
		JSONArray jsonArrSub = new JSONArray();
		// Menu menu = popupMenu.getMenu();
		if (checkboxSubtitle != null && checkboxSubtitle.length > 0) {
			int menuSize = checkboxSubtitle.length;
			if (subInfoArr != null && changeSubtitles != null && subInfoArr.length == menuSize) {
				for (int i = 0; i < menuSize; i++) {
					for (ChangeSubtitle changeSubtitle : changeSubtitles) {
						if (changeSubtitle.getSubType().trim().equalsIgnoreCase(subInfoArr[i].getSubType().trim())) {
							checkboxSubtitle[i].setChecked(changeSubtitle.isChoice());
							if (changeSubtitle.isChoice()) {
								setVisibility(tvSubs[i], View.VISIBLE);
								setVisibility(tvSubs[i+2], View.VISIBLE);
								setColorForSubtitle(tvSubs[i], changeSubtitle.getSubType());
								setColorForSubtitle(tvSubs[i+2], changeSubtitle.getSubType());
							} else {
								setVisibility(tvSubs[i], View.GONE);
								setVisibility(tvSubs[i+2], View.GONE);
							}
							break;
						}
					}
				}
			}
	
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
		return jsonArrSub;

	}

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
			if ("landscape".equals(orientation)) {
				int intOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
				activity.setRequestedOrientation(intOrientation);
			} else if ("portrait".equals(orientation)) {
				int intOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
				activity.setRequestedOrientation(intOrientation);
			}
		}
	}

	public void setMessage(String msg) {
		if (msg != null) {
			setTextForTextView(notifyTextView, msg);
			setVisibility(notifyTextView, View.VISIBLE);
		}
	}

	public void playRelateVideo(JSONObject json) {
		reset();
		Log.d(TAG, "playRelateVideo = " + json);
		if (json != null) {

			String movieID = Vp9ParamUtil.getJsonString(json, "movieID", "");
			
			String movieUrl = Vp9ParamUtil.getJsonString(json, "movieUrl", "");
			
			VideoInfo videoInfo = demandTiviSchedule.getVideoInfoByVideoId(movieID);
			
			ArrayList<VideoResolution> videoResolutions = getVideoResolutions(json);
			
			videoInfo.setVideoResolutions(videoResolutions);

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
		} else {
//			if(isLoading){
//				isLoading = false;
//				setVisibility(loadingLayout, View.GONE);
//			}
			setVisibility(loadingLayout, View.GONE);
		}
		setEnabled(btnSetting, false);
		// setVisibility(progessLayout, View.VISIBLE);
		String videoUrl = videoInfo.getUrl();

		Log.d(TAG, "playRelateVideo: " + videoUrl);
		this.playingVideo = videoInfo;
		if (subtitleInfos.size() > 0) {
			subInfoArr = new SubtitleInfo[subtitleInfos.size()];
			subInfoArr = subtitleInfos.toArray(subInfoArr);
			updateDefaultSubtitle();
		} else {
			subInfoArr = null;
		}
		this.videoResolutions = videoInfo.getVideoResolutions();
		playVideo(this.videoResolutions, videoUrl, false);
		// showController(0);
		showProgessLayout();
		demandTiviSchedule.setCurrentIndex(videoInfo.getIndex());
	}

	public void setRightVideoDisplay(JSONObject json) {
		if (timeShowHandle != null) {
			timeShowHandle.removeCallbacks(timeShowTask);
		}
		analyzeTimeShow(json);

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
*/}
