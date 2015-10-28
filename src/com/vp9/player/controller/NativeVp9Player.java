package com.vp9.player.controller;

import java.net.URL;
import java.util.Date;
import java.util.HashMap;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.media.TimedText;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.vp9.player.model.VideoResult;
import com.vp9.player.vp9Interface.Vp9ActivityInterface;
import com.vp9.player.vp9Interface.Vp9PlayerInterface;
import com.vp9.tv.VpMainActivity;
import com.vp9.util.Vp9Contant;

public class NativeVp9Player implements Vp9PlayerInterface {

	// private static final String WAITE_CHANNEL_IMAGE =
	// "http://tv.vp9.tv/player/theme/pk-tv.png";
    private String errorMsg;
    private MediaController mController;
    private MediaPlayer player;
	protected PlayVideoCountDownTimer playVideoCountDownTimer;
    
	public NativeVp9Player(MediaController mediaController) {
		mController = mediaController;
	}

	private static final int STATE_ERROR = -1;
	private static final int STATE_IDLE = 0;
	private static final int STATE_PAUSED = 4;
	private static final int STATE_PLAYBACK_COMPLETED = 5;
	private static final int STATE_PLAYING = 3;
	private static final int STATE_PREPARED = 2;
	private static final int STATE_PREPARING = 1;
	private static final int STATE_RESUME = 7;
	private static final int STATE_SUSPEND = 6;
	private static final int STATE_SUSPEND_UNSUPPORTED = 8;

	private static final String TAG = "NativeVp9Player";



	public void onPrepared(MediaPlayer mp) {
		mController.cancelPrepareAsyncTask();
		mController.setMessage(Vp9Contant.MSG_ON_PREPARE_PLAY_VIDEO);
		mController.isRefreshNotify = true;
		if (mController.clearParam) {
			return;
		}
		mController.isPlay = true;
		mController.mCurrentState = STATE_PREPARED;
		
		Log.d(TAG, "===== onPrepared =====");
		try {

			int preDuration = getDuration();
			mController.duration = preDuration;
			if (mController.videoType > 1) {
				Log.d(TAG, "onPrepared - seekTime/duration: " + mController.currentError + "/" + preDuration);
				if (mController.videoType == 2) {
					if (mController.currentError >= preDuration) {
						if (player != null && NativeVp9Player.this.isPlaying() && isInPlaybackState()) {
							NativeVp9Player.this.stop();
						}
						release();
						Log.d(TAG, "loading 11: " + false);
//						if(mController.isLoading){
//							mController.isLoading = false;
//							mController.setVisibility(mController.loadingLayout, View.GONE);
//						}
						mController.setVisibility(mController.loadingLayout, View.GONE);
						if(mController.is3D){
							mController.setVisibility(mController.loadingLayout2, View.GONE);
						}
						if (!mController.isDisplayChannelImage) {
							mController.setChanelTiviImage();
						}

						mController.mCheckPlayDemandVideoHandle1.postDelayed(mController.mCheckPlayVideoTask1, 1000L);
						return;
					}
					Log.d(TAG, "onPrepared - seek To: " + mController.currentError);
//					seekTo((int) mController.currentError);

				} else if (mController.videoType == 3) {
					if (mController.currentError >= preDuration) {
						Log.d(TAG, "onPrepared - seekTime/duration: " + mController.currentError + "/" + preDuration);
						if (player != null && NativeVp9Player.this.isPlaying() && isInPlaybackState()) {
							player.stop();
						}
						release();
						Log.d(TAG, "loading 12: " + false);
//						if(mController.isLoading){
//							mController.isLoading = false;
//							mController.setVisibility(mController.loadingLayout, View.GONE);
//						}
						mController.setVisibility(mController.loadingLayout, View.GONE);
						if(mController.is3D){
							mController.setVisibility(mController.loadingLayout2, View.GONE);
						}
						if (!mController.isDisplayChannelImage) {
							mController.setChanelTiviImage();
						}

						mController.mCheckPlayDemandVideoHandle2.postDelayed(mController.mCheckPlayVideoTask2, 1000L);
						return;
					}
					Log.d(TAG, "onPrepared - seek To: " + mController.currentError);
//					seekTo((int) mController.currentError);
				}
				seekTo((int) mController.currentError);
			}else if(mController.videoType == 0){
				  if(mController.currentError < preDuration){
					  seekTo((int) mController.currentError);
				  }
			}else{
				mController.currentError = 0;
			}

			Log.d(TAG, "onPrepared - Start Video: ");

			if (mController.playingVideo != null && mController.playingVideo.getThumbIcon() != null) {
				Log.d(TAG, "set logoVideo on Prepared");
				mController.setImageBitmap(mController.logoVideo, mController.playingVideo.getThumbIcon());
				mController.setVisibility(mController.logoVideo, View.GONE);
				if(mController.is3D){
					mController.setImageBitmap(mController.logoVideo2, mController.playingVideo.getThumbIcon());
					mController.setVisibility(mController.logoVideo2, View.GONE);
				}
			} else {
				mController.setVisibility(mController.logoVideo, View.GONE);
				if(mController.is3D){
					mController.setVisibility(mController.logoVideo2, View.GONE);
				}
			}

			if (mController.playingVideo != null && mController.playingVideo.getVideoName() != null && mController.videoType != 1) {
				mController.setTextForTextView(mController.videoTitle, "<b>" + mController.playingVideo.getVideoName() + "</b>", 1);
				mController.setVisibility(mController.videoTitle, View.VISIBLE);
				if(mController.is3D){
					mController.setTextForTextView(mController.videoTitle2, "<b>" +  mController.playingVideo.getVideoName() + "</b>", 1);
					mController.setVisibility(mController.videoTitle2, View.VISIBLE);
				}
			} else {
				mController.setVisibility(mController.videoTitle, View.GONE);
				if(mController.is3D){
					mController.setVisibility(mController.videoTitle2, View.GONE);
				}
			}

		    Log.d(TAG, "onPrepared - 2: none view, !2: view intShowControl: " + mController.intShowControl);
			if (mController.intShowControl != 2) {
				mController.setVisibility(mController.mVideoView, View.VISIBLE);
				if(!mController.isRightDisplay){
					mController.timeShowHandle.postDelayed(mController.timeShowTask, 1000L);
				}
			} else {
				mController.timeShowHandle.removeCallbacks(mController.timeShowTask, 1000L);
				mController.setVisibility(mController.mVideoView, View.GONE);
			}

//			if (mController.playingVideo != null && mController.demandTiviSchedule != null) {
////				int curIndex = mController.playingVideo.getIndex();
//				int curIndex = mController.demandTiviSchedule.getCurrentIndex();
//				if (curIndex == 0) {
//					mController.setBackgroundResource(mController.btnPrev, mController.vp9_btn_prev_hide_id);
//				} else {
//					mController.setBackgroundResource(mController.btnPrev, mController.vp9_btn_prev_id);
//				}
//
//				if (curIndex == mController.demandTiviSchedule.getSizeVideoInfos() - 1) {
//					mController.setBackgroundResource(mController.btnNext, mController.vp9_btn_next_hide_id);
//				} else {
//					mController.setBackgroundResource(mController.btnNext, mController.vp9_btn_next_id);
//				}
//
//			}

			
			startVideo();
			mController.isFirstPlay = true;
			Log.d(TAG, "mController.isFirstPlay 154: " + mController.isFirstPlay);
			updateVideoSize();
			
			// Tam dong lai
			if ((mController.videoType == 0 || (mController.videoType > 1 && mController.isLive == 0)) && mController.intShowControl != 2) {
				mController.setVisibility(mController.progessLayout, View.VISIBLE);
				if (mController.is3D) {
					mController.setVisibility(mController.progessLayout2, View.VISIBLE);
				}
			} else {
				mController.setVisibility(mController.progessLayout, View.GONE);
				if (mController.is3D) {
					mController.setVisibility(mController.progessLayout2, View.GONE);
				}
			}

			mController.setProgressForSeekBar(mController.sbFull, 0);
			// .setMaxForSeekBar(sbFull,1000);
			mController.setMaxForSeekBar(mController.sbFull, preDuration);
			if (mController.is3D) {
				mController.setProgressForSeekBar(mController.sbFull2, 0);
				mController.setMaxForSeekBar(mController.sbFull2, preDuration);
			}
			if (mController.isDisplayChannelImage) {
				mController.invisibleChanelTiviImage();
			}
			mController.setSeekFilmFullEvent();

			if (mController.videoIndex >= 0 && mController.demandTiviSchedule != null) {
				mController.demandTiviSchedule.setCurrentIndex(mController.videoIndex);
				if (mController.childVideoIndex >= 0) {
					mController.demandTiviSchedule.setCurrentChildIndex(mController.childVideoIndex);
				}
			}

			Log.d(TAG, "loading 13: " + mController.playingVideo);
			
			if (mController.playingVideo != null && mController.demandTiviSchedule != null) {

				int curIndex = mController.demandTiviSchedule.getCurrentIndex();
				Log.d(TAG, "curIndex1: " + curIndex);
				Log.d(TAG, "curIndex2: " + mController.demandTiviSchedule.getSizeVideoInfos());
				if (curIndex == 0) {
					mController.setBackgroundResource(mController.btnPrev, mController.vp9_btn_prev_hide_id);
					  if(mController.is3D){
						  mController.setBackgroundResource(mController.btnPrev2, mController.vp9_btn_prev_hide_id);
					  }
				}else if(mController.videoType != 1){
					mController.setBackgroundResource(mController.btnPrev, mController.vp9_btn_prev_id);
					  if(mController.is3D){
						  mController.setBackgroundResource(mController.btnPrev2, mController.vp9_btn_prev_id);
					  }
				}

				if (curIndex == mController.demandTiviSchedule.getSizeVideoInfos() - 1) {
					mController.setBackgroundResource(mController.btnNext, mController.vp9_btn_next_hide_id);
					  if(mController.is3D){
						  mController.setBackgroundResource(mController.btnNext2, mController.vp9_btn_next_hide_id);
					  }
				}else if(mController.videoType != 1){
					mController.setBackgroundResource(mController.btnNext, mController.vp9_btn_next_id);
					  if(mController.is3D){
						  mController.setBackgroundResource(mController.btnNext2, mController.vp9_btn_next_id);
					  }
				}

			}
			
			mController.hideController();
			mController.updateProgressBar();

			// .setVisibility(.pdLoading,
			// View.GONE);
			// .setVisibility(.loadRate, View.GONE);
//			if(mController.isLoading){
//				mController.isLoading = false;
//				mController.setVisibility(mController.loadingLayout, View.GONE);
//			}
			mController.setVisibility(mController.loadingLayout, View.GONE);
			if(mController.is3D){
				mController.setVisibility(mController.loadingLayout2, View.GONE);
			}
			mController.setBackgroundResource(mController.btnPlay, mController.vp9_btn_pause_id);
		    if(mController.is3D){
		    	mController.setBackgroundResource(mController.btnPlay2, mController.vp9_btn_pause_id);
		    }
			// .btnPlay.setBackgroundResource(R.drawable.vp9_btn_pause);
			mController.playSub(mController.subInfoArr);
			mController.loadLeftLogo(mController.playingVideo);
			mController.loadRightLogo(mController.playingVideo);
			sendVideoInfoToRemote();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	
	public void stop(){
		try {
			player.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void updateVideoSize() {
		if (mController.clearParam) {
			return;
		}
		// Get the dimensions of the video

		int videoWidth = player.getVideoWidth();
		int videoHeight = player.getVideoHeight();
		Log.i(TAG, "VIDEO SIZES -  1: W: " + videoWidth + " H: " + videoHeight);
		// new
		if (mController.mVideoWidth != 0) {
			videoWidth = mController.mVideoWidth;
		}

		if (mController.mVideoWidth != 0) {
			videoHeight = mController.mVideoHeight;
		}
		//

		if (videoWidth == 0 || videoHeight == 0) {
			return;
		}
		float videoProportion = (float) videoWidth / (float) videoHeight;
		Log.i(TAG, "VIDEO SIZES - 2: W: " + videoWidth + " H: " + videoHeight + " PROP: " + videoProportion);

		// Get the width of the screen
		int screenWidth = mController.activity.getWindowManager().getDefaultDisplay().getWidth();
		int screenHeight = mController.activity.getWindowManager().getDefaultDisplay().getHeight();
		
		Display display = mController.activity.getWindowManager().getDefaultDisplay();
		Point point = mController.getSize(display);
		if (point != null) {
			screenWidth = point.x;
			screenHeight = point.y;
		}
		
		if(mController.mScreenWidth != 0){
			screenWidth = mController.mScreenWidth;
		}
		
		if(mController.mScreenHeight != 0){
			
			screenHeight = mController.mScreenHeight;
			
		}

		float screenProportion = (float) screenWidth / (float) screenHeight;
		Log.i(TAG, "SCREEN SIZES: W: " + screenWidth + " H: " + screenHeight + " PROP: " + screenProportion);
		Log.i(TAG, "intFullScreen: " + mController.intFullScreen);
		// Get the SurfaceView layout parameters
		final android.widget.RelativeLayout.LayoutParams layoutparam = (LayoutParams) mController.mVideoView.getLayoutParams();
		
//		lp.setMargins(0, (screenHeight-lp.height)/2, 0, 0);
		layoutparam.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
		layoutparam.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
		layoutparam.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
		 
		// if(intFullScreen == 2){
		// lp.width = screenWidth;
		// lp.height = screenHeight;
		// }else
		if (mController.intFullScreen == 0) {
			if (videoProportion > screenProportion) {
				layoutparam.width = screenWidth;
//				lp.height = (int) ((float) screenWidth / videoProportion);
				layoutparam.height = (int) (((float) screenWidth*(float) videoHeight)/(float) videoWidth);
			} else {
//				lp.width = (int) (videoProportion * (float) screenHeight);
				layoutparam.width = (int) (((float) videoWidth * (float) screenHeight)/(float) videoHeight);
				layoutparam.height = screenHeight;
			}
		} else if (mController.intFullScreen == 1) {
			if (videoProportion > screenProportion) {
				layoutparam.width = screenWidth;
//				lp.height = (int) ((float) screenWidth / videoProportion);
				layoutparam.height = (int) (((float) screenWidth*(float) videoHeight)/(float) videoWidth);
			} else {
				// lp.width = videoWidth;
				// lp.height = screenHeight;
				layoutparam.width = screenWidth;
				layoutparam.height = screenHeight;
			}

			if (mController.videoType == 1) {
				layoutparam.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
				layoutparam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
				layoutparam.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
				layoutparam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
				
				
			}
		}
//		lp.width = screenWidth;
//		lp.height = screenHeight;
//		Log.i(TAG, "REAL VIDEO SIZES: W: " + lp.width + " H: " + lp.height);
		layoutparam.width += mController.leftStretch + mController.rightStretch;
        layoutparam.height += mController.topStretch + mController.topStretch;
        
//		Log.i(TAG, "AAAAAAAAAAAAA: VIDEO SIZES: W: " + point.x + " H: " + point.y);
		// Commit the layout parameters
		mController.activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mController.mVideoView.setLayoutParams(layoutparam);
				mController.mHolder.setFixedSize(layoutparam.width, layoutparam.height);
				mController.mVideoView.invalidate();
				mController.mVideoView.requestLayout();
			}
		});
		
//		mController.activity.runOnUiThread(new Runnable(){
//
//			@Override
//			public void run() {
//				mController.mVideoView.setRotation(90);
//				
//			}
//			
//		});
	}
	
	private void sendVideoInfoToRemote() {
		if (mController.clearParam) {
			return;
		}
		JSONObject jsonInfCurVideo = new JSONObject();
		int curTime = getCurrentPosition();
		boolean isPlay = NativeVp9Player.this.isPlaying();
		int duration = getDuration();
		String channelId = mController.channelId;
		JSONArray jsonArrSub = new JSONArray();
		if (mController.settingPopup != null) {

		}

		if (mController.subInfoArr != null && mController.settingPopup != null && mController.checkboxSubtitle != null) {
			// Menu menu = popupMenu.getMenu();
			int menuSize = mController.checkboxSubtitle.length;
			if (mController.subInfoArr.length == menuSize) {
				try {
					for (int i = 0; i < menuSize; i++) {
						boolean isChoice = mController.checkboxSubtitle[i].isChecked();
						String subType = mController.subInfoArr[i].getSubType();
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
			jsonInfCurVideo.put("url", mController.videoUrl);
			

			if (mController.playingVideo != null) {

				String movieID = mController.playingVideo.getMovieID();

				String videoName = mController.playingVideo.getVideoName();

				if (movieID != null && !"".equals(movieID)) {
					jsonInfCurVideo.put("movieID", movieID);
				}

				if (videoName != null && !"".equals(videoName)) {
					jsonInfCurVideo.put("videoName", videoName);
				}

				if (mController.videoType == 2) {
					int currentIndex = mController.demandTiviSchedule.getCurrentIndex();
					// int cCurrentChildIndex =
					// demandTiviSchedule.getCurrentChildIndex();
					VideoResult videoResult = mController.demandTiviSchedule.getVideoInfoByIndex(currentIndex);
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

			if (mController.demandTiviSchedule != null) {
				jsonInfCurVideo.put("index", mController.demandTiviSchedule.getCurrentIndex());
				jsonInfCurVideo.put("childIndex", mController.demandTiviSchedule.getCurrentChildIndex());
			}

			if (mController.isRemoteListener) {
				JSONObject jsonEvent = new JSONObject();
				jsonEvent.put("action", "firstPlay");
				jsonEvent.put("information", jsonInfCurVideo);
				Vp9ActivityInterface vp9Activity = (Vp9ActivityInterface) mController.activity;
				vp9Activity.sendEvent(jsonEvent);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}	
	
	public void seekTo(final int time) {
		if (mController.clearParam) {
			return;
		}
		// activity.runOnUiThread(new Runnable() {
		// @Override
		// public void run() {
		// .player.seekTo(time);
		// .isSeek = true;
		// // .updateProgressBar();
		// }
		// });
		mController.isSeek = true;
		if (mController.loadSub != null) {
			mController.loadSub.setSeek(true);
		}
		if (!isInPlaybackState()) {
			mController.mSeekWhenPrepared = time;
		} else {
			// .isSeek = true;
			player.seekTo(time);
			mController.updateProgressBar();
			mController.mSeekWhenPrepared = 0;

			if (mController.isRemoteListener) {
				try {
					JSONObject jsonEvent = new JSONObject();
					jsonEvent.put("action", "seek");
					jsonEvent.put("time", time);
					Vp9ActivityInterface vp9Activity = (Vp9ActivityInterface) mController.activity;
					vp9Activity.sendEvent(jsonEvent);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}

	}
	
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@SuppressLint("NewApi")
	private void initFunction() {
		if (mController.clearParam) {
			return;
		}
		player.setOnErrorListener(new MediaPlayer.OnErrorListener() {
			public boolean onError(MediaPlayer paramMediaPlayer, int whatError, int extra) {
				return NativeVp9Player.this.onError(paramMediaPlayer, whatError, extra);
			}
		});

		player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
			public void onPrepared(MediaPlayer paramMediaPlayer) {
				NativeVp9Player.this.onPrepared(paramMediaPlayer);
			}
		});
		
		player.setOnTimedTextListener(new MediaPlayer.OnTimedTextListener(){

			@Override
			public void onTimedText(MediaPlayer mp, TimedText text) {
				NativeVp9Player.this.onTimedText(mp, text);
				
			}});

		player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				NativeVp9Player.this.onCompletion(mp);
			}
		});

		player.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {

			@Override
			public void onBufferingUpdate(MediaPlayer mp, int percent) {
				NativeVp9Player.this.onBufferingUpdate(mp, percent);

			}
		});

		player.setOnInfoListener(new MediaPlayer.OnInfoListener() {

			@Override
			public boolean onInfo(MediaPlayer mp, int what, int extra) {
				return NativeVp9Player.this.onInfo(mp, what, extra);
			}
		});

		player.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {

			@Override
			public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
				NativeVp9Player.this.onVideoSizeChanged(mp, width, height);

			}
		});

		mController.mVideoView.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View view, MotionEvent motionEvent) {
				return mController.onTouch(view, motionEvent);
			}
		});

		mController.mVideoView.setOnHoverListener(new View.OnHoverListener() {

			@Override
			public boolean onHover(View v, MotionEvent event) {
				return mController.onHover(v, event);
			}

		});

		mController.vp9ChannelImage.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View view, MotionEvent motionEvent) {
				return mController.onTouch(view, motionEvent);
			}
		});



		mController.btnPlay.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				mController.handleClickPlayAndPause();
			}
		});
		
		


		mController.btnSub.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				mController.showPopupMenu(v);
			}
		});
		
		mController.btnChoose.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				mController.showVideoMenu(v);
			}
		});
		
		mController.btnPrev.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
//				mController.playPreVideo();
				mController.runThreadPlayPreVideo();
			}
		});
		
		mController.btnNext.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
//				mController.playNextVideo();
				Log.d("NativeVp9Player","PlayNext line = 696");
				mController.runThreadPlayNextVideo();
				
			}
		});
		
		if(mController.is3D){
			mController.btnPlay2.setOnClickListener(new Button.OnClickListener() {

				@Override
				public void onClick(View v) {
					mController.handleClickPlayAndPause();
				}
			});
			mController.btnChoose2.setOnClickListener(new Button.OnClickListener(){
	            @Override
	            public void onClick(View v) {        
	            	mController.showVideoMenu(v);
	            }
	        });
			
			mController.btnSub2.setOnClickListener(new Button.OnClickListener() {
				@Override
				public void onClick(View v) {
					mController.showPopupMenu(v);
				}
			});
			
			mController.vp9ChannelImage.setOnTouchListener(new View.OnTouchListener() {
				public boolean onTouch(View view, MotionEvent motionEvent) {
					return mController.onTouch(view, motionEvent);
				}
			});	
			
			mController.btnPrev2.setOnClickListener(new Button.OnClickListener() {
				@Override
				public void onClick(View v) {
//					mController.playPreVideo();
					mController.runThreadPlayPreVideo();
				}
			});
			
			mController.btnNext2.setOnClickListener(new Button.OnClickListener() {
				@Override
				public void onClick(View v) {
//					mController.playNextVideo();
					mController.runThreadPlayNextVideo();
				}
			});
		}


		
		mController.parentLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mController != null && mController.getActivity() != null){
					VpMainActivity activity = (VpMainActivity) mController.getActivity();
					mController.setFocusView(activity.getAppview());
					
					if (mController.intShowControl == 0) {
						mController.showController(1);
					} else {
						mController.intShowControl = 0;
						mController.setVisibility(mController.controllerLayout, View.GONE);
						if(mController.is3D){
							mController.setVisibility(mController.controllerLayout2, View.GONE);
						}
					}
				}
			}
		});


		// this.btnBack.setOnClickListener(new Button.OnClickListener(){
		// @Override
		// public void onClick(View v) {
		// handleBackButtonEvent(v);
		// }
		// });

	}

	
	protected void onTimedText(MediaPlayer mp, TimedText text) {
		Log.d(TAG, "onTimedText = " + text);
	}


	public void release() {
		Log.d(TAG, "release");
		final MediaPlayer tempPlayer = player;
		player = null;
		
		Thread thrRelease = new Thread(){
			
			public void run(){
				executeRelease(tempPlayer);
			}
		};
		thrRelease.setName("thrRelease_21");
		thrRelease.start();
	}
	
	public void releaseNoneThread() {
		final MediaPlayer tempPlayer = player;
		player = null;
		executeRelease(tempPlayer);
	}
	
	protected void executeRelease(MediaPlayer tempPlayer) {
		Log.d(TAG, "executeRelease");
		try {
//			mController.isPlay = false;
			Log.d(TAG, "release 1");
			if (tempPlayer != null) {
				Log.d(TAG, "release 2");
//				MediaPlayer tempPlayer = player;
//				player = null;
				synchronized (tempPlayer) {
					Log.d(TAG, "release 3");
					tempPlayer.reset();
					Log.d(TAG, "release 4");
					tempPlayer.release();
					tempPlayer = null;
					Log.d(TAG, "release 5");
				}
//				synchronized (player) {
//					player.reset();
//					player.release();
//					player = null;
//				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
		Log.d(TAG, "width = " + width);
		Log.d(TAG, "height = " + height);
		// updateVideoSize(width, height);
		// if(isInPlaybackState()){
		// Log.d(TAG, "width1 = " + width);
		// Log.d(TAG, "height1 = " + height);
		// updateVideoSize(width, height);
		// }

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
		} else {
			Log.v(TAG, "MediaInfo, Other Media Info: whatInfo=" + whatInfo + ", extra="+extra);
		}
		return false;
	}
	
	
	public void playVideo(final String videoUrl, final boolean isLoop) {
		Log.d(TAG, "----- playVideo -----");
		if (mController.clearParam) {
			return;
		}
		mController.receiveProxySpeed(mController.intProxy);
		mController.videoUrl = videoUrl;
		
//		VpMainActivity vpMainActivity = (VpMainActivity) mController.activity;

		JSONObject jsonEvent = new JSONObject();
		try {
			jsonEvent.put("action", "setvideo");
			JSONObject jsonInfCurVideo = new JSONObject();
			jsonInfCurVideo.put("url", videoUrl);
			jsonInfCurVideo.put("cookie", mController.cookie);
			
			jsonEvent.put("information", jsonInfCurVideo);
			Vp9ActivityInterface vp9Activity = (Vp9ActivityInterface) mController.activity;
			vp9Activity.sendEvent(jsonEvent);
			
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		if(mController.mResumErrorHandle != null){
			mController.mResumErrorHandle.removeCallbacks(mController.mResumErrorVideoTask);
		}
		
//		if(NativeVp9Player.this.playVideoCountDownTimer != null){
//			NativeVp9Player.this.playVideoCountDownTimer.cancel();
//		}
		
		Thread thrPlayVideo = new Thread(){
			public void run(){
				try {
					mController.setVisibility(mController.mVideoView, View.GONE);
					if (player != null) {
						release();
						if (mController.mVideoView != null) {
							mController.setInvalidate(mController.mVideoView);
//							mController.mVideoView.invalidate();
//							mController.mVideoView.destroyDrawingCache();
							mController.refreshDrawableState(mController.mVideoView);
//							mController.mVideoView.refreshDrawableState();
						}
					}
					mController.state = 1;
					delay(1000);
					player = new MediaPlayer();
					NativeVp9Player.this.reset();
					if(mController.mVideoView != null){
//						mController.mVideoView.destroyDrawingCache();
//						mController.mVideoView.refreshDrawableState();
						mController.destroyDrawingCache(mController.mVideoView);
						mController.refreshDrawableState(mController.mVideoView);
				    }
					player.setLooping(isLoop);
					mController.mHolder.addCallback(new SurfaceHolder.Callback() {
						@Override
						public void surfaceCreated(SurfaceHolder holder) {
							Log.d(TAG, "playVideo 8a");
							mController.mHolder = holder;
							// if(isDisplayVideo){
							if (player != null) {

								Log.e(TAG, "========== surfaceCreated ==========");
								try {
									player.setDisplay(holder);
									holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
//									holder.setFormat(PixelFormat.RGBA_8888);
								} catch (Exception e) {
									e.printStackTrace();
								}

							}

						}

						@Override
						public void surfaceDestroyed(SurfaceHolder holder) {
							Log.e(TAG, "========== surfaceDestroyed ==========");
						}

						@Override
						public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
							Log.e(TAG, "========== surfaceChanged ==========");
						}
					});


					String proxyVideoUrl = videoUrl;
//					int port = MyService.get_port();
					int port = -1;
					if(mController != null && mController.getProxySevice() != null){
						port = mController.getProxySevice().get_port();
					}
					if (videoUrl.endsWith(".m3u8") && mController.intProxy == 1 && port != -1) {
						proxyVideoUrl = "http://127.0.0.1:" + port + "//" + videoUrl;
					}
					
					playVideoWithHeader(player, proxyVideoUrl, mController.isUseHeader);
					
					/*player.setDataSource(proxyVideoUrl);
					Log.d(TAG, "playVideo: " + proxyVideoUrl);
					initFunction();
					player.prepareAsync();
					mController.setMessage(Vp9Contant.MSG_PREPARE_PLAY_VIDEO);
					mController.isRefreshNotify = true;
					Log.d(TAG, "prepareAsync");
					mController.mCurrentState = STATE_PREPARING;*/

				} catch (Exception e) {
					e.printStackTrace();
					playVideoInErrorStart(videoUrl, isLoop);
				}
			}
		};
		
		thrPlayVideo.setName("thrPlayVideo 38");
		thrPlayVideo.start();
	}
	
	class PlayVideoCountDownTimer extends CountDownTimer {
		private String videoUrl;
		private boolean isLoop;

		public PlayVideoCountDownTimer(long startTime, long interval,
				String videoUrl, boolean isLoop) {
			super(startTime, interval);
			this.videoUrl = videoUrl;
			this.isLoop = isLoop;

		}

		
		@Override
		public void onFinish() {
		}

		@Override
		public void onTick(long millisUntilFinished) {
			playVideoInErrorStart(videoUrl, isLoop);
		}
	}
		 
	protected void playVideoInErrorStart(String videoUrl, boolean isLoop) {
//		delay(5000);
		if(playVideoCountDownTimer != null){
			playVideoCountDownTimer.cancel();
		}
		if(!mController.isPlay){
			playVideo(videoUrl, isLoop);
		}
		
	}


	private void delay(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}


	public boolean isPlaying(){
		if(player != null){
			try {
				return player.isPlaying();
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			
		}
		return false;
	}
	
	protected void onCompletion(MediaPlayer mp) {
		if(mController.videoType == 1){
			return;
		}
		Log.d(TAG, "onCompletion");
		if ((mController.mVideoView != null) && (!mController.mVideoView.equals(""))) {
			mController.mVideoView.destroyDrawingCache();
		}
		mController.cancelTask();
		mController.reset();
		mController.mCurrentState = STATE_PLAYBACK_COMPLETED;
		switch (mController.videoType) {

		case 0:
//			mController.playNextVideo();
			Log.d("Native","PlayNext onCompletion Case =0");
			mController.runThreadPlayNextVideo();
			
			break;

		case 2:
			mController.mCheckPlayVideoTask1.run();
			break;
		case 3:
			mController.mCheckPlayVideoTask2.run();
			break;
//		case 4:
//			mController.runThreadPlayOffTVVideo();
//			break;

		default:
			mController.runThreadPlayNextVideo();
			break;
		}
	}

		public boolean onError(MediaPlayer mp, int whatError, int extra) {
		if (mController.clearParam) {
			return false;
		}
		Log.e(TAG, "onError: " + whatError);
		if (whatError == MediaPlayer.MEDIA_ERROR_SERVER_DIED) {
			Log.e(TAG, "Media Error, Server Died: " + extra);
			this.errorMsg = Vp9Contant.MSG_ERROR_VIDEO_1;
		} else if (whatError == MediaPlayer.MEDIA_ERROR_UNKNOWN) {
			this.errorMsg = Vp9Contant.MSG_ERROR_VIDEO_2;
			Log.e(TAG, "Media Error, Error Unknown: " + extra);
		} else {
			Log.e(TAG, "Other Error: " + extra);
			this.errorMsg = Vp9Contant.MSG_ERROR_VIDEO_3;
		}

		mController.sendError(whatError, extra);
		
		if (!mController.isChangeSource || (mController.isChangeSource && mController.intProxy == 1)) {
			Log.e(TAG, "onError: 2");
			mController.setTextForTextView(mController.notifyTextView, this.errorMsg, 0);
			
			if(mController.is3D){
				mController.setTextForTextView(mController.notifyTextView2, this.errorMsg, 0);
			}
			mController.isRefreshNotify = true;
			mController.mCurrentState = STATE_ERROR;
			mController.isSeek = true;
			if (mController.loadSub != null) {
				mController.loadSub.setSeek(true);
			}
			mController.isError = true;
	
	//		mController.setVisibility(mController.loadingLayout, View.VISIBLE);
			if (!mController.isDisplayChannelImage && mController.intShowControl != 2) {
	//			if(!mController.isLoading){
	//				mController.isLoading = true;
	//				mController.setVisibility(mController.loadingLayout, View.VISIBLE);
	//			}
				mController.setVisibility(mController.loadingLayout, View.VISIBLE);
				if(mController.is3D){
					mController.setVisibility(mController.loadingLayout2, View.VISIBLE);
				}
			} else {
				Log.d(TAG, "loading 6: " + false);
	//			if(mController.isLoading){
	//				mController.isLoading = false;
	//				mController.setVisibility(mController.loadingLayout, View.GONE);
	//			}
				mController.setVisibility(mController.loadingLayout, View.GONE);
				if(mController.is3D){
					mController.setVisibility(mController.loadingLayout2, View.GONE);
				}
			}
	
			// .resume();
	
			if (mController.updateTimehandle != null) {
				Log.d(TAG, "Cancel-mUpdateTimeTask-3");
				mController.updateTimehandle.removeCallbacks(mController.mUpdateTimeTask);
			}
	
			mController.mResumErrorHandle.removeCallbacks(mController.mResumErrorVideoTask);
			// if(state == 1){
			// .mResumErrorHandle.postDelayed(.mResumErrorVideoTask,
			// 1000L);
			// }
			Log.e(TAG, "onError: 3");
			mController.mResumErrorHandle.postDelayed(mController.mResumErrorVideoTask, 1000L);
		}

		return true;
	}
	
	public void resume() {
		Log.d(TAG, "resume");
		
		mController.count = 0;
//		mController.activity.runOnUiThread(new Runnable() {
//			@Override
//			public void run() {
//				if (mController.clearParam) {
//					return;
//				}
//				try {
//					playVideo(mController.videoUrl, false);
//				} catch (IllegalStateException e) {
//					e.printStackTrace();
//				}
//			}
//		});
		Thread thrdResume = new Thread(){
			public void run(){
//				if (mController.clearParam) {
//					return;
//				}
				Log.d(TAG, "thrdResume");
				try {
					playVideo(mController.videoUrl, false);
				} catch (IllegalStateException e) {
					e.printStackTrace();
				}
			}
		};
		thrdResume.setName("thrdResume 35");
		thrdResume.start();
	}
	
		public void startVideo() {
			Log.e(TAG, "startVideo");
		if (mController.clearParam) {
			return;
		}
		Log.e(TAG, "startVideo 1");
		// resume();
		// playVideo(.videoUrl, false);
		try {

			if (isInPlaybackState() && !NativeVp9Player.this.isPlaying()) {
				Log.d(TAG, "loading 38: " + false);
//				if(mController.isLoading){
//					mController.isLoading = false;
//					mController.setVisibility(mController.loadingLayout, View.GONE);
//				}
				mController.setVisibility(mController.loadingLayout, View.GONE);
				if(mController.is3D){
					mController.setVisibility(mController.loadingLayout2, View.GONE);
				}
				player.start();
				mController.setMessage(Vp9Contant.MSG_START_PLAY_VIDEO);
				mController.setBackgroundResource(mController.btnPlay, mController.vp9_btn_pause_id);
				if (mController.is3D) {
					mController.setBackgroundResource(mController.btnPlay2, mController.vp9_btn_pause_id);
				}
				mController.mCurrentState = 3;
				if (mController.updateTimehandle != null) {
					Log.d(TAG, "Cancel-mUpdateTimeTask-10");
					mController.updateTimehandle.removeCallbacks(mController.mUpdateTimeTask);
				}
				if (mController.state == 1) {
					Log.e(TAG, "startVideo 2");
					mController.updateTimehandle.postDelayed(mController.mUpdateTimeTask, 500L);
					Log.e(TAG, "startVideo 3 - mUpdateTimeTask");
				}
				if (mController.isRemoteListener) {
					try {
						JSONObject jsonEvent = new JSONObject();
						jsonEvent.put("action", "play");
						Vp9ActivityInterface vp9Activity = (Vp9ActivityInterface) mController.activity;
						vp9Activity.sendEvent(jsonEvent);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

			} else if (player != null && !NativeVp9Player.this.isPlaying()) {
//				mController.setVisibility(mController.loadingLayout, View.VISIBLE);
				if (!mController.isDisplayChannelImage && mController.intShowControl != 2) {
//					if(!mController.isLoading){
//						mController.isLoading = true;
//						mController.setVisibility(mController.loadingLayout, View.VISIBLE);
//					}
					mController.setVisibility(mController.loadingLayout, View.VISIBLE);
					if(mController.is3D){
						mController.setVisibility(mController.loadingLayout2, View.VISIBLE);
					}
				} else {
//					if(mController.isLoading){
//						mController.isLoading = false;
//						mController.setVisibility(mController.loadingLayout, View.GONE);
//					}
					mController.setVisibility(mController.loadingLayout, View.GONE);
					if(mController.is3D){
						mController.setVisibility(mController.loadingLayout2, View.GONE);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
		public boolean isInPlaybackState() {
		boolean isPlayBack = true;
		if ((this.player == null) || (mController.mCurrentState == STATE_ERROR) || (mController.mCurrentState == STATE_IDLE) || (mController.mCurrentState == STATE_PREPARING)) {
			isPlayBack = false;
		}
		return isPlayBack;
	}
	
	
		public void pause() {
			Log.d(TAG, "pause");
		if (mController.clearParam) {
			return;
		}
		Log.d(TAG, "pause 1");
		if ((isInPlaybackState()) && (NativeVp9Player.this.isPlaying())) {
			Log.d(TAG, "pause 2");
			try {
				if (mController.updateTimehandle != null) {
					Log.d(TAG, "Cancel-mUpdateTimeTask-7");
					mController.updateTimehandle.removeCallbacks(mController.mUpdateTimeTask);
				}
				try {

					player.pause();
				} catch (Exception e) {
					e.printStackTrace();
				}
				mController.setBackgroundResource(mController.btnPlay, mController.vp9_btn_play_id);
			    if(mController.is3D){
			    	mController.setBackgroundResource(mController.btnPlay2, mController.vp9_btn_play_id);
			    }
				// .btnPlay.setBackgroundResource(vp9_btn_play_id);
				mController.mCurrentState = 4;
				if (mController.isRemoteListener) {
					JSONObject jsonEvent = new JSONObject();
					jsonEvent.put("action", "pause");
					Vp9ActivityInterface vp9Activity = (Vp9ActivityInterface) mController.activity;
					vp9Activity.sendEvent(jsonEvent);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
		public int getCurrentPosition() {

		int i = 0;
		if (!isInPlaybackState()) {
			i = 0;
		}
//		else if(mController.videoType == 1){
//			return 0; // kênh m3u8 return 0;
//		} 
		else {
			try {
				i = this.player.getCurrentPosition();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return i;
	}
	
		public int getDuration() {
		int duration = 0;

		if (isInPlaybackState()) {
			try {
				duration = this.player.getDuration();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		return duration;
	}
	

	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		if (mController.clearParam) {
			return;
		}
		int bfDuration = getDuration();

		final int progress;
		if (bfDuration != 0) {
			progress = percent * bfDuration / 100;
		} else {
			progress = 0;
		}
		if (mController.percentBuffer != progress) {
			mController.percentBuffer = progress;

			mController.setSecondaryProgress(mController.sbFull, progress);
			if(mController.is3D){
				mController.setSecondaryProgress(mController.sbFull2, progress);
			}

		}
	}

	@Override
	public void reset() {
		if(player != null){
			player.reset();
		}
	}

	@Override
	public int getVideoWidth() {
		int videoWidth = 0;
		if(player != null){
			videoWidth = player.getVideoWidth();
		}
		return videoWidth;
	}

	@Override
	public int getVideoHeight() {
		int videoHeight = 0;
		if(player != null){
			videoHeight = player.getVideoHeight();
		}
		return videoHeight;
	}

	private void playVideoWithHeader(MediaPlayer mediaplayer, String url, boolean isUseHeader) {
		
		if (isUseHeader && !mController.cookie.trim().equals("")) {
			try {				
				HashMap<String, String> abc = new HashMap<String, String>();
//				abc.put("User-Agent", mController.getUserAgent());
//				abc.put("Referer", mController.tokenPath);
				abc.put("Cookie", mController.cookie);
				mController.setMessage(Vp9Contant.MSG_PREPARE_PLAY_VIDEO);
				mediaplayer.setDataSource(mController.activity.getApplicationContext(), Uri.parse(url), abc);				
				initFunction();
				player.prepareAsync();
				mController.resetPrepareAsync(15000);
				mController.isRefreshNotify = true;
				mController.mCurrentState = STATE_PREPARING;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			try {
				mediaplayer.setDataSource(url);
				Log.d(TAG, "playVideo 12");
				initFunction();
				Log.d(TAG, "playVideo 13");
				mediaplayer.prepareAsync();
				mController.resetPrepareAsync(15000);
				mController.setMessage(Vp9Contant.MSG_PREPARE_PLAY_VIDEO);
				mController.isRefreshNotify = true;
				//Fix error Thiet lap thanh cong cua chuong trinh chieu lai
				//mController.mCurrentState = STATE_PREPARING;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
	}
	

	@Override
	public void changeSource(final String videoUrl) {
		String subdomain = "";
		try {
			String host = new URL(videoUrl).getHost();
			int indexOf = host.indexOf(".");
			subdomain = host.substring(0, indexOf);
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		mController.setTextForTextView(mController.notifyTextView, "đổi video: " + subdomain, 0);
		
		mController.cancelTask();
		Log.d(TAG, "----- changeSource -----");
		if (mController.clearParam) {
			return;
		}
		mController.videoUrl = videoUrl;
		mController.state = 1;
		JSONObject jsonEvent = new JSONObject();
		try {
			jsonEvent.put("action", "setvideo");
			JSONObject jsonInfCurVideo = new JSONObject();
			jsonInfCurVideo.put("url", videoUrl);
			
			jsonEvent.put("information", jsonInfCurVideo);
			Vp9ActivityInterface vp9Activity = (Vp9ActivityInterface) mController.activity;
			vp9Activity.sendEvent(jsonEvent);
			
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		if(mController.mResumErrorHandle != null){
			mController.mResumErrorHandle.removeCallbacks(mController.mResumErrorVideoTask);
		}
		
		if(NativeVp9Player.this.playVideoCountDownTimer != null){
			NativeVp9Player.this.playVideoCountDownTimer.cancel();
		}
		reset();
		
		try {
			String proxyVideoUrl = videoUrl;
//					int port = MyService.get_port();
			int port = -1;
			if(mController != null && mController.getProxySevice() != null){
				port = mController.getProxySevice().get_port();
			}
			if (videoUrl.endsWith(".m3u8") && mController.intProxy == 1 && port != -1) {
				proxyVideoUrl = "http://127.0.0.1:" + port + "//" + videoUrl;
			}
			
			player.setDataSource(proxyVideoUrl);

			player.prepareAsync();
			mController.resetPrepareAsync(15000);
			mController.mCurrentState = STATE_PREPARING;

		} catch (Exception e) {
			e.printStackTrace();
			playVideoInErrorStart(videoUrl, false);
		}



	
	}
	
}
	
