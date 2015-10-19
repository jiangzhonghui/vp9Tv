package com.vp9.player.controller;

import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tv.vp9.videoproxy.MyService;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Looper;
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
import com.vp9.util.FileUtil;
import com.vp9.util.OperUtil;
import com.vp9.util.Vp9Contant;

public class H265Vp9Player implements Vp9PlayerInterface{

//	private static final String WAITE_CHANNEL_IMAGE = "http://tv.vp9.tv/player/theme/pk-tv.png";
    private String errorMsg;
    private MediaController mController;
    private MediaPlayer player;
    
	public H265Vp9Player(MediaController mediaController) {
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

	private static final String TAG = "H265Vp9Player";

	
	  public void onPrepared(MediaPlayer mp) {
		  player.setPlaybackSpeed(1.0f);
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
			  
			  int preDuration = H265Vp9Player.this.getDuration();
			  mController.duration = preDuration;
			  if(mController.videoType > 1){
				  Log.d(TAG, "onPrepared - seekTime/duration: " + mController.currentError + "/" + preDuration);
				  if(mController.videoType == 2){
					  if(mController.currentError >= preDuration){
						  if(player != null && H265Vp9Player.this.isPlaying() && isInPlaybackState()){
							  H265Vp9Player.this.stop();
						  }
						  release();
						  Log.d(TAG, "loading 14: " + false);
						  
//							if(mController.isLoading){
//								mController.isLoading = false;
//								mController.setVisibility(mController.loadingLayout, View.GONE);
//							}
						  mController.setVisibility(mController.loadingLayout, View.GONE);
							if(mController.is3D){
								mController.setVisibility(mController.loadingLayout2, View.GONE);
							}
						  if(!mController.isDisplayChannelImage){
							  mController.setChanelTiviImage();
						  }
						  mController.mCheckPlayDemandVideoHandle1.postDelayed(mController.mCheckPlayVideoTask1, 1000L);
						  return;
					  }
					  Log.d(TAG,"onPrepared - seek To: " + mController.currentError);
					  seekTo((int) mController.currentError);
					  
				  }else if(mController.videoType == 3){
					  if(mController.currentError >= preDuration){
						  Log.d(TAG, "onPrepared - seekTime/duration: " + mController.currentError + "/" + preDuration);
						  if(player != null && player.isPlaying() && isInPlaybackState()){
							  player.stop();  
						  }
						  release();
						  Log.d(TAG, "loading 15: " + false);
//						  if(mController.isLoading){
//							  mController.isLoading = false;
//							  mController.setVisibility(mController.loadingLayout, View.GONE);
//						  }
						  mController.setVisibility(mController.loadingLayout, View.GONE);
							if(mController.is3D){
								mController.setVisibility(mController.loadingLayout2, View.GONE);
							}
						  if(!mController.isDisplayChannelImage){
							  mController.setChanelTiviImage();
						  }
						  
						  mController.mCheckPlayDemandVideoHandle2.postDelayed(mController.mCheckPlayVideoTask2, 1000L);
						  return;
					  }
					  Log.d(TAG,"onPrepared - seek To: " + mController.currentError);
					  seekTo((int) mController.currentError);
//						if(this.playingVideo != null && this.playingVideo.getThumbIcon() != null){
//							NewVp9Player.this.setImageBitmap(logoVideo, this.playingVideo.getThumbIcon());
////							NewVp9Player.this.setVisibility(logoVideo, View.VISIBLE);
//							NewVp9Player.this.setVisibility(logoVideo, View.GONE);
//						}else{
//							NewVp9Player.this.setVisibility(logoVideo, View.GONE);
//						}
//						
//						if(this.playingVideo != null){
////							String link = "<span><img src=\"http://vp9.tv/wp-content/uploads/2013/12/VP9TVfinal2233ok.png\"></span>" + "\n"
////							+"<span style=\"color:#f00\">red</span>" + "\n" +
////									"<span style=\"color:#0f0\">green</span>" + "\n" + 
////							"<span style=\"color:#00f\">blue</span> Link: <a href=\"tv.vp9.tv\" target=\"_blank\"> VP9.TV</a>;";
////							NewVp9Player.this.setTextForTextView(videoTitle, link);
//							NewVp9Player.this.setTextForTextView(videoTitle,"<b>" +  this.playingVideo.getVideoName() + "</b>");
//							NewVp9Player.this.setVisibility(videoTitle, View.VISIBLE);
//						}else{
//							NewVp9Player.this.setVisibility(videoTitle, View.GONE);
//						}
				  }
				  
			  }else if(mController.videoType == 0){
				  if(mController.currentError < preDuration){
					  seekTo((int) mController.currentError);
				  }
			  }else{
					mController.currentError = 0;
			  }
			  
				if(mController.playingVideo != null && mController.playingVideo.getThumbIcon() != null){
					mController.setImageBitmap(mController.logoVideo, mController.playingVideo.getThumbIcon());
					mController.setVisibility(mController.logoVideo, View.GONE);
					if(mController.is3D){
						mController.setImageBitmap(mController.logoVideo2, mController.playingVideo.getThumbIcon());
						mController.setVisibility(mController.logoVideo2, View.GONE);
					}
				}else{
					mController.setVisibility(mController.logoVideo, View.GONE);
					if(mController.is3D){
						mController.setVisibility(mController.logoVideo2, View.GONE);
					}
				}
				
				if(mController.playingVideo != null && mController.playingVideo.getVideoName() != null){
					mController.setTextForTextView(mController.videoTitle,"<b>" +  mController.playingVideo.getVideoName() + "</b>", 1);
					mController.setVisibility(mController.videoTitle, View.VISIBLE);
					if(mController.is3D){
						mController.setTextForTextView(mController.videoTitle2, "<b>" +  mController.playingVideo.getVideoName() + "</b>", 1);
						mController.setVisibility(mController.videoTitle2, View.VISIBLE);
					}
				}else{
					mController.setVisibility(mController.videoTitle, View.GONE);
					if(mController.is3D){
						mController.setVisibility(mController.videoTitle2, View.GONE);	
					}
				}
			  
			  if(mController.intShowControl != 2 &&  !mController.isRightDisplay){
//				  mVideoView.setVisibility(View.VISIBLE); 
//				  setVisibility(mVideoView, View.VISIBLE);
					if(!mController.isRightDisplay){
						mController.timeShowHandle.postDelayed(mController.timeShowTask, 1000L);
					}
			  }else{
				  mController.timeShowHandle.removeCallbacks(mController.timeShowTask, 1000L);
//				  mVideoView.setVisibility(View.GONE);
//				  setVisibility(mVideoView, View.GONE);
			  }
			  
//			  if(mController.playingVideo != null && mController.demandTiviSchedule != null){
////				  int curIndex = mController.playingVideo.getIndex();
//				  int curIndex = mController.demandTiviSchedule.getCurrentIndex();
//				  if(curIndex == 0){
//					  mController.setBackgroundResource(mController.btnPrev, mController.vp9_btn_prev_hide_id);
//				  }else{
//					  mController.setBackgroundResource(mController.btnPrev, mController.vp9_btn_prev_id);
//				  }
//				  
//				  if(curIndex == mController.demandTiviSchedule.getSizeVideoInfos() - 1){
//					  mController.setBackgroundResource(mController.btnNext, mController.vp9_btn_next_hide_id);
//				  }else{
//					  mController.setBackgroundResource(mController.btnNext, mController.vp9_btn_next_id);
//				  }
//				  
//			  }
			  
			  startVideo();
			  mController.isFirstPlay = true;
			  updateVideoSize();
			  
			  if((mController.videoType == 0 || (mController.videoType > 1 && mController.isLive == 0)) && mController.intShowControl != 2){
				  mController.setVisibility(mController.progessLayout, View.VISIBLE); 
					if (mController.is3D) {
						mController.setVisibility(mController.progessLayout2, View.VISIBLE);
					}
			  }else{
				  mController.setVisibility(mController.progessLayout, View.GONE);
					if (mController.is3D) {
						mController.setVisibility(mController.progessLayout2, View.GONE);
					}
			  }
			  
			  mController.setProgressForSeekBar(mController.sbFull,0);
//				NewVp9Player.this.setMaxForSeekBar(sbFull,1000);
			  mController.setMaxForSeekBar(mController.sbFull, preDuration);
				if (mController.is3D) {
					mController.setProgressForSeekBar(mController.sbFull2, 0);
					mController.setMaxForSeekBar(mController.sbFull2, preDuration);
				}
				if(mController.isDisplayChannelImage){
					mController.invisibleChanelTiviImage();
				}
				mController.setSeekFilmFullEvent();
			   
			   if(mController.videoIndex >= 0 && mController.demandTiviSchedule != null){
				   mController.demandTiviSchedule.setCurrentIndex(mController.videoIndex);
				   if(mController.childVideoIndex >= 0){
					   mController.demandTiviSchedule.setCurrentChildIndex(mController.childVideoIndex);
				   }
			   }

				  if(mController.playingVideo != null && mController.demandTiviSchedule != null){
//					  int curIndex = mController.playingVideo.getIndex();
					  int curIndex = mController.demandTiviSchedule.getCurrentIndex();
					  if(curIndex == 0){
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
					  
					  if(curIndex == mController.demandTiviSchedule.getSizeVideoInfos() - 1){
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

//		    	NewVp9Player.this.setVisibility(NewVp9Player.this.pdLoading, View.GONE);
//		    	NewVp9Player.this.setVisibility(NewVp9Player.this.loadRate, View.GONE);
			    
//				if(mController.isLoading){
//					mController.isLoading = false;
//					mController.setVisibility(mController.loadingLayout, View.GONE);
//				}
			    mController.setVisibility(mController.loadingLayout, View.GONE);
				if(mController.is3D){
					mController.setVisibility(mController.loadingLayout2, View.GONE);
				}
			    mController.setBackgroundResource(mController.btnPlay, mController.vp9_btn_pause_id);
			    if(mController.is3D){
			    	mController.setBackgroundResource(mController.btnPlay2, mController.vp9_btn_pause_id);
			    }
//		    	NewVp9Player.this.btnPlay.setBackgroundResource(R.drawable.vp9_btn_pause);
			    mController.playSub(mController.subInfoArr);
			    mController.loadLeftLogo(mController.playingVideo);
			    mController.loadRightLogo(mController.playingVideo);
				sendVideoInfoToRemote();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	  
		private void stop() {
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
			Log.i(TAG, "VIDEO SIZES: W: " + videoWidth + " H: " + videoHeight + " PROP: " + videoProportion);

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
			final android.view.ViewGroup.LayoutParams layoutparam = mController.mVideoView.getLayoutParams();
			
			final LayoutParams lp = new RelativeLayout.LayoutParams(layoutparam);
//			lp.setMargins(0, (screenHeight-lp.height)/2, 0, 0);
			lp.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
			lp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
			lp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
			 
			// if(intFullScreen == 2){
			// lp.width = screenWidth;
			// lp.height = screenHeight;
			// }else
			if (mController.intFullScreen == 0) {
				if (videoProportion > screenProportion) {
					lp.width = screenWidth;
//					lp.height = (int) ((float) screenWidth / videoProportion);
					lp.height = (int) (((float) screenWidth*(float) videoHeight)/(float) videoWidth);
				} else {
//					lp.width = (int) (videoProportion * (float) screenHeight);
					lp.width = (int) (((float) videoWidth * (float) screenHeight)/(float) videoHeight);
					lp.height = screenHeight;
				}
			} else if (mController.intFullScreen == 1) {
				if (videoProportion > screenProportion) {
					lp.width = screenWidth;
//					lp.height = (int) ((float) screenWidth / videoProportion);
					lp.height = (int) (((float) screenWidth*(float) videoHeight)/(float) videoWidth);
				} else {
					// lp.width = videoWidth;
					// lp.height = screenHeight;
					lp.width = screenWidth;
					lp.height = screenHeight;
				}

				if (mController.videoType == 1) {
					lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
					lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
					lp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
					lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
					
					
				}
			}
//			lp.width = screenWidth;
//			lp.height = screenHeight;
//			Log.i(TAG, "REAL VIDEO SIZES: W: " + lp.width + " H: " + lp.height);
			lp.width += mController.leftStretch + mController.rightStretch;
	        lp.height += mController.topStretch + mController.topStretch;
	        
//			Log.i(TAG, "AAAAAAAAAAAAA: VIDEO SIZES: W: " + point.x + " H: " + point.y);
			// Commit the layout parameters
			mController.activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
//					LayoutParams layoutParams = new RelativeLayout.LayoutParams(500, 500);
					mController.mVideoView.setLayoutParams(lp);
					mController.mHolder.setFixedSize(lp.width, lp.height);
				}
			});
		}

	private void sendVideoInfoToRemote() {
		if(mController.clearParam){
			return;
		}
			JSONObject jsonInfCurVideo = new JSONObject();
			int curTime = getCurrentPosition();
			boolean isPlay = H265Vp9Player.this.isPlaying();
			int duration = getDuration();
			String channelId = mController.channelId;
			JSONArray jsonArrSub = new JSONArray();
			if(mController.settingPopup != null){
				
			}

	        if(mController.subInfoArr != null && mController.settingPopup != null){
//				Menu menu = popupMenu.getMenu();
				int menuSize = mController.checkboxSubtitle.length;
	            if(mController.subInfoArr.length == menuSize){
	        	try {				
	        		for(int i = 0; i < menuSize; i++){
	        			boolean isChoice = mController.checkboxSubtitle[i].isChecked();
	        			String subType = mController.subInfoArr[i].getSubType();
	        			JSONObject jsonSub = new JSONObject();
						jsonSub.put("subType", subType);
						jsonSub.put("isChoice", isChoice);
						jsonArrSub.put(jsonSub);
	        		}
//	        		result.put("result", jsonArrSub);
	        		
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
				jsonInfCurVideo.put("channelId" ,channelId);
				jsonInfCurVideo.put("url", mController.videoUrl);
				
				if(mController.playingVideo != null){
					
					String movieID = mController.playingVideo.getMovieID();
					
					String videoName = mController.playingVideo.getVideoName();
					
					if(movieID != null && !"".equals(movieID)){
						jsonInfCurVideo.put("movieID" ,movieID);
					}
					
					if(videoName != null && !"".equals(videoName)){
						jsonInfCurVideo.put("videoName" ,videoName);
					}
					
					if(mController.videoType == 2){
						int currentIndex = mController.demandTiviSchedule.getCurrentIndex();
//						int cCurrentChildIndex = demandTiviSchedule.getCurrentChildIndex();
						VideoResult videoResult = mController.demandTiviSchedule.getVideoInfoByIndex(currentIndex);
						if(videoResult != null && videoResult.getVideoInfo() != null){
							
							String parentMovieID = videoResult.getVideoInfo().getMovieID();
							
							String parentVideoName = videoResult.getVideoInfo().getVideoName();
							
							if(parentMovieID != null && !"".equals(parentMovieID)){
								jsonInfCurVideo.put("parentMovieID" ,parentMovieID);
							}
							
							if(parentVideoName != null && !"".equals(parentVideoName)){
								jsonInfCurVideo.put("parentVideoName" ,parentVideoName);
							}
						}
						
					}
					
				}
				
				if(mController.demandTiviSchedule != null){
					jsonInfCurVideo.put("index", mController.demandTiviSchedule.getCurrentIndex());
					jsonInfCurVideo.put("childIndex", mController.demandTiviSchedule.getCurrentChildIndex());
				}

				if(mController.isRemoteListener){
					JSONObject jsonEvent = new JSONObject();
					jsonEvent.put("action", "firstPlay");
					jsonEvent.put("information", jsonInfCurVideo);
					Vp9ActivityInterface vp9Activity = (Vp9ActivityInterface)mController.activity;
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

		mController.isSeek = true;
		if (mController.loadSub != null) {
			mController.loadSub.setSeek(true);
		}
		if (!isInPlaybackState()) {
			mController.mSeekWhenPrepared = time;
		} else {
			// Vp9Player.this.isSeek = true;
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

	private void initFunction() {
		if(mController.clearParam){
			return;
		}
		player.setOnErrorListener(new MediaPlayer.OnErrorListener() {
			public boolean onError(MediaPlayer paramMediaPlayer, int whatError, int extra) {
				return H265Vp9Player.this.onError(paramMediaPlayer, whatError, extra);
			}
		});
		
		player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
			public void onPrepared(MediaPlayer paramMediaPlayer) {
				H265Vp9Player.this.onPrepared(paramMediaPlayer);
			}
		});
		
		player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				H265Vp9Player.this.onCompletion(mp);
			}
		});
		
		player.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
			
			@Override
			public void onBufferingUpdate(MediaPlayer mp, int percent) {
				H265Vp9Player.this.onBufferingUpdate(mp,percent);
				
			}
		});
		
		player.setOnInfoListener(new MediaPlayer.OnInfoListener() {
			
			@Override
			public boolean onInfo(MediaPlayer mp, int what, int extra) {
				return  H265Vp9Player.this.onInfo(mp, what, extra);
			}
		});
		
		player.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
			
			@Override
			public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
				H265Vp9Player.this.onVideoSizeChanged(mp, width, height);
				
			}
		});
		
		mController.mVideoView.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View view, MotionEvent motionEvent) {
				return mController.onTouch(view, motionEvent);
			}
		});
		
//		this.mVideoView.setOnClickListener(new Button.OnClickListener(){
//	       	 
//            @Override
//            public void onClick(View view) {        
//            	NewVp9Player.this.onClickForDisplayControl(view);
//            }
//        });
		
		
		mController.mVideoView.setOnHoverListener(new View.OnHoverListener(){

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
		

		
		mController.btnPlay.setOnClickListener(new Button.OnClickListener(){
	       	 
            @Override
            public void onClick(View v) {        
            	mController.handleClickPlayAndPause();
            }
        });
		
		
		mController.btnSub.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {        
            	mController.showPopupMenu(v);
            }
        });
		
		mController.btnChoose.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {        
            	mController.showVideoMenu(v);
            }
        });
		
		mController.btnPrev.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {        
//            	mController.playPreVideo();
            	mController.runThreadPlayPreVideo();
            }
        });
		
		mController.btnNext.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {        
//            	mController.playNextVideo();
            	Log.d("h265", "PlayNext = onclick");
            	mController.runThreadPlayNextVideo();
            	
            }
        });
		
		if(mController.is3D){
			mController.btnPlay2.setOnClickListener(new Button.OnClickListener(){
		       	 
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
			
			mController.btnSub2.setOnClickListener(new Button.OnClickListener(){
	            @Override
	            public void onClick(View v) {        
	            	mController.showPopupMenu(v);
	            }
	        });
			
			mController.btnPrev2.setOnClickListener(new Button.OnClickListener(){
	            @Override
	            public void onClick(View v) {        
//	            	mController.playPreVideo();
	            	mController.runThreadPlayPreVideo();
	            }
	        });
			
			mController.btnNext2.setOnClickListener(new Button.OnClickListener(){
	            @Override
	            public void onClick(View v) {        
//	            	mController.playNextVideo();
	            	mController.runThreadPlayNextVideo();
	            }
	        });
			
			mController.vp9ChannelImage.setOnTouchListener(new View.OnTouchListener() {
				public boolean onTouch(View view, MotionEvent motionEvent) {
					return mController.onTouch(view, motionEvent);
				}
			});
		}

		
		mController.parentLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mController != null){
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
		
//		this.btnBack.setOnClickListener(new Button.OnClickListener(){
//            @Override
//            public void onClick(View v) {        
//            	handleBackButtonEvent(v);
//            }
//        });
		
	}

	public synchronized void release() {
		final MediaPlayer tempPlayer = player;
		player = null;
		Thread thrRelease = new Thread(){
			public void run(){
//				Looper.prepare();
				executeRelease(tempPlayer);
//				executeRelease();
//				Looper.loop();
			}
		};
		thrRelease.setName("thrRelease_19");
		thrRelease.start();
	}
	
	public synchronized void releaseNoneThread() {
		final MediaPlayer tempPlayer = player;
		player = null;
		executeRelease(tempPlayer);
	}
	
	protected synchronized void executeRelease(MediaPlayer tempPlayer) {
		try {
//			mController.isPlay = false;

			if (tempPlayer != null) {
//				MediaPlayer temPlayer = player;
//				player = null;
				synchronized (tempPlayer) {
					tempPlayer.reset();
					tempPlayer.release();
					tempPlayer = null;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
		Log.d(TAG, "width = " + width);
		Log.d(TAG, "height = " + height);
//		updateVideoSize(width, height);
//		if(isInPlaybackState()){
//			Log.d(TAG, "width1 = " + width);
//			Log.d(TAG, "height1 = " + height);
//			 updateVideoSize(width, height);
//		}

		 
	}

	public boolean onInfo(MediaPlayer mp, int whatInfo, int extra) {
//	    if (whatInfo == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
//		      Log.v(TAG, "Media Info, Media Info buffering end " + extra);
//		    } else if (whatInfo == MediaPlayer.MEDIA_INFO_NOT_SEEKABLE) {
//		      Log.v(TAG, "Media Info, Media Info Not Seekable " + extra);
//		    } else if (whatInfo == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
//		      Log.v(TAG, "Media Info, Media Info buffering start " + extra);
//		    } else if (whatInfo == MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING) {
////		      Log.v(TAG, "MediaInfo, Media Info Video Track Lagging " + extra);
//		    }else if (whatInfo == MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED) {
//		        Log.v(TAG,"MediaInfo, Media Info download rate changed " + extra);
//		    }
		    return false;
    }

	public void playVideo(final String videoUrl, final boolean isLoop) {
		if (mController.clearParam) {
			return;
		}
		if (!LibsChecker.checkVitamioLibs(mController.activity)){
			return;
		}else{
			FileUtil.updateLibraryFiles(mController.activity);
		}

		if(mController.mResumErrorHandle != null){
			mController.mResumErrorHandle.removeCallbacks(mController.mResumErrorVideoTask);
		}
		mController.videoUrl = videoUrl;
		mController.activity.runOnUiThread(new Runnable() {
			@Override
		     public void run() {
				try {
					mController.setVisibility(mController.mVideoView, View.GONE);
					if (player != null) {
						release();
						mController.mVideoView.invalidate();
//						mController.mVideoView.destroyDrawingCache();
						mController.mVideoView.refreshDrawableState();
					}
					mController.state = 1;
					delay(1000);
					player = new MediaPlayer(mController.activity);
					if(mController.mVideoView != null){
						mController.mVideoView.destroyDrawingCache();
						mController.mVideoView.refreshDrawableState();
				    }
//					player.reset();
					player.setLooping(isLoop);
					mController.mHolder.addCallback(new SurfaceHolder.Callback() {
							@Override
							public void surfaceCreated(SurfaceHolder holder) {
							if (player != null) {
								Log.e(TAG, "========== surfaceCreated ==========");
								try {
									player.setDisplay(holder);
									mController.mHolder = holder;
									holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
									holder.setFormat(PixelFormat.RGBX_8888);
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
							public void surfaceChanged(SurfaceHolder holder, int format,
									int width, int height) {
								Log.e(TAG, "========== surfaceChanged ==========");
							}
				 		});
					
					  if(mController.intShowControl != 2){
						  mController.setVisibility(mController.mVideoView, View.VISIBLE);
					  }
					  
						String url = videoUrl;
//						int port = MyService.get_port();
						int port = -1;
						if(mController != null && mController.getProxySevice() != null){
							port = mController.getProxySevice().get_port();
						}
						if (videoUrl.endsWith(".m3u8") && mController.intProxy == 1 && port != -1) {
							url = "http://127.0.0.1:" + port + "//" + videoUrl;
						}
						player.setDataSource(url);
						player.setVideoQuality(MediaPlayer.VIDEOQUALITY_HIGH);
					
					initFunction();
					player.prepareAsync();
					mController.resetPrepareAsync(20000);
					mController.setMessage(Vp9Contant.MSG_PREPARE_PLAY_VIDEO);
					mController.isRefreshNotify = true;
					Log.d(TAG, "prepareAsync");
					
					mController.mCurrentState = STATE_PREPARING;
				} catch (Exception e) {
					e.printStackTrace();
					playVideoInErrorStart(videoUrl, isLoop);
				}

		     }
		});
		
	}
	
	protected synchronized void playVideoInErrorStart(String videoUrl, boolean isLoop) {
		delay(5000);
		if(!mController.isPlay){
			playVideo(videoUrl, isLoop);
		}
		
	}
	
	private void delay(int i) {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
//	private void updateLibraryFiles(Activity activity) {
//		Log.d(TAG, "loading library proxy");
//		boolean isExistLibDir = true;
//		String libsDirPath = activity.getFilesDir().getParent() + "/libs";
//		File libsDir = new File(libsDirPath);
//		if (!libsDir.exists()) {
//			isExistLibDir = false;
//			boolean isMkdir = libsDir.mkdir();
//			if (!isMkdir) {
//				return;
//			}
//		}
//
//		AssetManager assetManager = activity.getResources().getAssets();
//		try {
//			String[] proxyLibNames = assetManager.list("proxyLibs");
//			Log.e(TAG, "A1 proxyLibNames: " + proxyLibNames.length);
//			if (proxyLibNames != null && proxyLibNames.length > 0) {
//				Log.e(TAG, "A2");
//				for (int i = 0; i < proxyLibNames.length; i++) {
//					Log.e(TAG, "proxyLibName: " + proxyLibNames[i]);
//					if (!isExistLibDir || !checkExistFile(libsDirPath + "/" + proxyLibNames[i])) {
//						InputStream inputStream = assetManager.open("proxyLibs" + "/" + proxyLibNames[i]);
//						if (inputStream != null) {
//							String filePath = libsDirPath + "/" + proxyLibNames[i];
//							writeFile(inputStream, filePath);
//						}
//					}
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	private boolean checkExistFile(String fileName) {
//		boolean isExist = false;
//		try {
//			File file = new File(fileName);
//			if (file.exists()) {
//				isExist = true;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return isExist;
//
//	}
//
//	private boolean writeFile(InputStream fis, String fileName) {
//		Log.e(TAG, "B1: " + fileName);
//		int i;
//		char c;
//		OutputStreamWriter osw = null;
//		try {
//			// InputStreamReader isr = new InputStreamReader(fis);
//			int available = fis.available();
//			byte[] buffer = new byte[available];
//			fis.read(buffer);
//			FileOutputStream fOut = new FileOutputStream(new File(fileName));
//			// osw = new OutputStreamWriter(fOut);
//			fOut.write(buffer);
//			// while ((i = isr.read()) != -1) {
//			// c = (char) i;
//			// osw.write(c);
//			// }
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if (osw != null) {
//					osw.flush();
//					osw.close();
//				}
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//
//		}
//		return true;
//	}
	




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

	public void onCompletion(MediaPlayer mp) {
		if(mController.videoType == 1){
			return;
		}
		Log.d(TAG, "onCompletion");
		mController.mVideoView.destroyDrawingCache();
		mController.cancelTask();
		mController.reset();
		mController.mCurrentState = STATE_PLAYBACK_COMPLETED;
		switch (mController.videoType) {

		case 0:
//			mController.playNextVideo();
			Log.d("H265Vp9Player","PlayNext onCompletion Case =0");
			mController.runThreadPlayNextVideo();
			
			break;

		case 2:
			mController.mCheckPlayVideoTask1.run();
			break;
		case 3:
			mController.mCheckPlayVideoTask2.run();
			break;

		default:
			break;
		}
	}


		public boolean onError(MediaPlayer mp, int whatError, int extra) {

			Log.e(TAG, "onError: " + whatError);
			if (whatError == MediaPlayer.MEDIA_ERROR_IO) {
				Log.e(TAG, "Media Error, Server Died " + extra);
				this.errorMsg = "lỗi kết nối đến máy chủ";
			} else if (whatError == MediaPlayer.MEDIA_ERROR_UNKNOWN) {
				Log.e(TAG, "Media Error, Error Unknown " + extra);
				this.errorMsg = "Video bị lỗi";
			}else{
				this.errorMsg = "Video bị lỗi không rõ nguyên nhân";
			}
			
			mController.setTextForTextView(mController.notifyTextView, this.errorMsg, 0);
			
			if(mController.is3D){
				mController.setTextForTextView(mController.notifyTextView2, this.errorMsg, 0);
			}
			mController.isRefreshNotify = true;
			mController.mCurrentState = STATE_ERROR;
			
			mController.isSeek = true;
			if(mController.loadSub != null){
				mController.loadSub.setSeek(true);
			}
			mController.isError = true;
			
//			mController.setVisibility(mController.loadingLayout, View.VISIBLE);
			if (!mController.isDisplayChannelImage && mController.intShowControl != 2) {
//				if(!mController.isLoading){
//					mController.isLoading = true;
//					mController.setVisibility(mController.loadingLayout, View.VISIBLE);
//				}
				mController.setVisibility(mController.loadingLayout, View.VISIBLE);
				if(mController.is3D){
					mController.setVisibility(mController.loadingLayout2, View.VISIBLE);
				}
			} else {
//				if(mController.isLoading){
//					mController.isLoading = false;
//	 				mController.setVisibility(mController.loadingLayout, View.GONE);
//				}
				mController.setVisibility(mController.loadingLayout, View.GONE);
				if(mController.is3D){
					mController.setVisibility(mController.loadingLayout2, View.GONE);
				}
			}
	    	resume();
//	    	try {
//		    	NewVp9Player.this.player.prepareAsync();
//		    	NewVp9Player.this.player.start();
//			} catch (IllegalStateException e) {
//				e.printStackTrace(); 
//			}
	    	
			if (mController.updateTimehandle != null) {
				Log.d(TAG, "Cancel-mUpdateTimeTask-2");
				mController.updateTimehandle.removeCallbacks(mController.mUpdateTimeTask);
			}
			
			mController.mResumErrorHandle.removeCallbacks(mController.mResumErrorVideoTask);
			if(mController.state == 1){
				mController.mResumErrorHandle.postDelayed(mController.mResumErrorVideoTask, 1000L);
			}
	    	
//			if(NewVp9Player.this.player != null && NewVp9Player.this.player.isPlaying()){
//				Log.v(TAG, "Time " + NewVp9Player.this.player.getCurrentPosition() + " === " + NewVp9Player.this.currentError);
//			}
//			
//			if (NewVp9Player.this.updateTimehandle != null) {
//				NewVp9Player.this.updateTimehandle.removeCallbacks(NewVp9Player.this.mUpdateTimeTask);
//			}
//			new Handler().postDelayed(new Runnable() {
//				public void run() {
//					if(NewVp9Player.this.player != null && NewVp9Player.this.player.isPlaying()){
//						NewVp9Player.this.player.pause();
//					}
//					if(NewVp9Player.this.isPlay){
//						NewVp9Player.this.mResumErrorHandle.postDelayed(NewVp9Player.this.mResumErrorVideoTask, 500L);
//					}
//					
//				}
//			}, 3000L);
////		    }, 5000L);
//			if(NewVp9Player.this.isPlay && NewVp9Player.this.player != null && NewVp9Player.this.player.isPlaying()){
//				NewVp9Player.this.updateTimehandle.postAtTime(NewVp9Player.this.mUpdateTimeTask, 500L);
//			}
			return true;
		}
		

		public void resume() {
			mController.count = 0;
			mController.activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (mController.clearParam) {
						return;
					}
					try {
						playVideo(mController.videoUrl, false);
					} catch (IllegalStateException e) {
						e.printStackTrace();
					}
				}
			});
		}

	  public void startVideo(){
		  try {
				if(mController.clearParam){
					return;
				}
			    if (isInPlaybackState() && !H265Vp9Player.this.isPlaying()) {
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
			        if (mController.updateTimehandle != null){
			        	Log.d(TAG, "Cancel-mUpdateTimeTask-9");
			        	mController.updateTimehandle.removeCallbacks(mController.mUpdateTimeTask);
			        }
			        if(mController.state == 1){
			        	mController.updateTimehandle.postDelayed(mController.mUpdateTimeTask, 500L);
			        }
			        if(mController.isRemoteListener){
						try {
							JSONObject jsonEvent = new JSONObject();
							jsonEvent.put("action", "play");
							Vp9ActivityInterface vp9Activity = (Vp9ActivityInterface) mController.activity;
							vp9Activity.sendEvent(jsonEvent);
						} catch (JSONException e) {
							e.printStackTrace();
						}	
			        }

			    }else if(player != null && !player.isPlaying()){
			    		mController.setVisibility(mController.loadingLayout, View.VISIBLE);
						if(mController.is3D){
							mController.setVisibility(mController.loadingLayout2, View.VISIBLE);
						}
			    }
			    if (!mController.isDisplayChannelImage && mController.intShowControl != 2){
			    	mController.setVisibility(mController.loadingLayout, View.VISIBLE);
					if(mController.is3D){
						mController.setVisibility(mController.loadingLayout2, View.VISIBLE);
					}
			    } else {
			    	mController.setVisibility(mController.loadingLayout, View.GONE);
					if(mController.is3D){
						mController.setVisibility(mController.loadingLayout2, View.GONE);
					}
				}			        
		} catch (Exception e) {
			e.printStackTrace();
		}
	  }
	  
		public boolean isInPlaybackState() {
			boolean isPlayBack = true;
			if ((this.player == null) || (mController.mCurrentState == STATE_ERROR) || (mController.mCurrentState == STATE_IDLE)
					|| (mController.mCurrentState == STATE_PREPARING)) {
				isPlayBack = false;
			}
			return isPlayBack;
		}
		
	  public void pause() {
			if(mController.clearParam){
				return;
			}
		  Log.e(TAG, "11");
	    if ((isInPlaybackState()) && (H265Vp9Player.this.isPlaying())) {
	    	try {
		    	Log.e(TAG, "12");
		        if (mController.updateTimehandle != null){
		        	Log.d(TAG, "Cancel-mUpdateTimeTask-6");
		        	mController.updateTimehandle.removeCallbacks(mController.mUpdateTimeTask);
		        }
		        Log.e(TAG, "13");
		    	player.pause();
		    	mController.setBackgroundResource(mController.btnPlay, mController.vp9_btn_play_id);
			    if(mController.is3D){
			    	mController.setBackgroundResource(mController.btnPlay2, mController.vp9_btn_play_id);
			    }
//		    	Vp9Player.this.btnPlay.setBackgroundResource(vp9_btn_play_id);
		    	Log.e(TAG, "14");
		    	mController.mCurrentState = 4;
		       if(mController.isRemoteListener){
					try {
						JSONObject jsonEvent = new JSONObject();
						jsonEvent.put("action", "pause");
						Vp9ActivityInterface vp9Activity = (Vp9ActivityInterface) mController.activity;
						vp9Activity.sendEvent(jsonEvent);
					} catch (JSONException e) {
						e.printStackTrace();
					}
		       }
			} catch (Exception e) {
				e.printStackTrace();
			}


			
	    }
	  }
	  
	  
	  public int getCurrentPosition() {
			if(mController.clearParam){
				return 0;
			}
		    int i = 0;
		    if (!isInPlaybackState()){
		    	i = 0;
//		    } else if(mController.videoType == 1){
//				return 0; // kênh m3u8 return 0;
			}
		    else{
		    	try {
		    		i = (int) this.player.getCurrentPosition();
				} catch (Exception e) {
					e.printStackTrace();
				}
		    }
		    return i;
	  }

		  
	       
	       public void onBufferingUpdate(MediaPlayer mp, int percent) {
	    	   int bfDuration = getDuration();
	    	   
	    	   final int progress;
	    	   if(bfDuration != 0){
	    		   progress = percent*bfDuration/100;
	    	   }else{
	    		   progress = 0;
	    	   }
	    	   if(mController.percentBuffer != progress){
	    		   mController.percentBuffer = progress;

	    		   mController.setSecondaryProgress(mController.sbFull, progress);

	    		   if(mController.is3D){
	    			   mController.setSecondaryProgress(mController.sbFull2, progress);
	    		   }
	    	     }
	    	  }
	       
	   	public int getDuration() {
			int duration = 0;

			if (isInPlaybackState()) {
				try {
					duration = (int) this.player.getDuration();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			return duration;
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


		@Override
		public void changeSource(String url) {
			// TODO Auto-generated method stub
			
		}
}
