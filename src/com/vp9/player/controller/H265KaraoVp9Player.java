package com.vp9.player.controller;

import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;

import org.json.JSONException;
import org.json.JSONObject;

import tv.vp9.videoproxy.MyService;
import android.content.res.AssetManager;
import android.graphics.PixelFormat;
import android.os.Looper;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;

import com.vp9.player.vp9Interface.Vp9KaraoPlayerInterface;
import com.vp9.plugin.EventKaraokePlugin;
import com.vp9.tv.KaraokeActivity;
import com.vp9.util.FileUtil;

public class H265KaraoVp9Player implements Vp9KaraoPlayerInterface{

//	private static final String WAITE_CHANNEL_IMAGE = "http://tv.vp9.tv/player/theme/pk-tv.png";
    private String errorMsg;
    private KaraokeActivity mController;
    private MediaPlayer player;
    
    int FPS = 1000/30;
	public H265KaraoVp9Player(KaraokeActivity karaokeActivity) {
		mController = karaokeActivity;
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
		  mController.isPlay = true;
		  mController.mCurrentState = STATE_PREPARED;
		  Log.d(TAG, "===== onPrepared =====");
		  try {
//			  startVideo();		  
			  updateVideoSize();
				if(mController.listenerStates[EventKaraokePlugin.ACTION_BIND_EVENT_PREPARED_KARAO_INDEX]){
					try {
						JSONObject jsonEvent = new JSONObject();
						jsonEvent.put("duration", getDuration());
						jsonEvent.put("currentTime", getCurrentPosition());
						jsonEvent.put("videoUrl", mController.videoUrl);
						jsonEvent.put("videoWidth", player.getVideoWidth());
						jsonEvent.put("videoHeight", player.getVideoHeight());
						mController.sendEvent(EventKaraokePlugin.ACTION_BIND_EVENT_PREPARED_KARAO_LISTENER,jsonEvent);
					} catch (JSONException e) {
						e.printStackTrace();
					}	
				}
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
			int screenWidth = mController.getWindowManager().getDefaultDisplay().getWidth();
			int screenHeight = mController.getWindowManager().getDefaultDisplay().getHeight();
			
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
			final android.view.ViewGroup.LayoutParams lp = mController.mVideoView.getLayoutParams();
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
			}
//			lp.width = screenWidth;
//			lp.height = screenHeight;
			Log.i(TAG, "REAL VIDEO SIZES: W: " + lp.width + " H: " + lp.height);

			// Commit the layout parameters
			mController.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					mController.mVideoView.setLayoutParams(lp);
					mController.mHolder.setFixedSize(lp.width, lp.height);
				}
			});
		}
	
	
	public boolean seekTo(final int time) {
		if (mController.clearParam) {
			return false;
		}
		boolean isSuccess = false;
		mController.isSeek = true;
		if (!isInPlaybackState()) {
			mController.mSeekWhenPrepared = time;
		} else {
			// .isSeek = true;
			player.seekTo(time);
			isSuccess = true;
			mController.mSeekWhenPrepared = 0;
			if(mController.listenerStates[EventKaraokePlugin.ACTION_BIND_EVENT_SEEKED_KARAO_INDEX]){
				try {
//					JSONObject jsonEvent = new JSONObject();
//					jsonEvent.put("time", time);
					mController.sendEvent(EventKaraokePlugin.ACTION_BIND_EVENT_SEEKED_KARAO_LISTENER,time);
				} catch (Exception e) {
					e.printStackTrace();
				}	
			}
		}
		return isSuccess;
	}

	private void initFunction() {
		if(mController.clearParam){
			return;
		}
		player.setOnErrorListener(new MediaPlayer.OnErrorListener() {
			public boolean onError(MediaPlayer paramMediaPlayer, int whatError, int extra) {
				return H265KaraoVp9Player.this.onError(paramMediaPlayer, whatError, extra);
			}
		});
		
		player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
			public void onPrepared(MediaPlayer paramMediaPlayer) {
				H265KaraoVp9Player.this.onPrepared(paramMediaPlayer);
			}
		});
		
		player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				H265KaraoVp9Player.this.onCompletion(mp);
			}
		});
		
		player.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
			
			@Override
			public void onBufferingUpdate(MediaPlayer mp, int percent) {
				H265KaraoVp9Player.this.onBufferingUpdate(mp,percent);
				
			}
		});
		
		player.setOnInfoListener(new MediaPlayer.OnInfoListener() {
			
			@Override
			public boolean onInfo(MediaPlayer mp, int what, int extra) {
				return  H265KaraoVp9Player.this.onInfo(mp, what, extra);
			}
		});
		
		player.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
			
			@Override
			public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
				H265KaraoVp9Player.this.onVideoSizeChanged(mp, width, height);
				
			}
		});	
	}

	public synchronized void release() {
		Thread thrRelease = new Thread(){
			public void run(){
//				Looper.prepare();
				executeRelease();
//				Looper.loop();
			}
		};
		thrRelease.setName("thrRelease_18");
		thrRelease.start();
	}
	
	protected synchronized void executeRelease() {
		try {
			mController.isPlay = false;

			if (player != null) {
				player.stop();
				MediaPlayer temPlayer = player;
				player = null;
				synchronized (temPlayer) {
					temPlayer.reset();
					temPlayer.release();
					temPlayer = null;
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
		if (!LibsChecker.checkVitamioLibs(mController)){
			return;
		}else{
			FileUtil.updateLibraryFiles(mController);
		}

		if(mController.mResumErrorHandle != null){
			mController.mResumErrorHandle.removeCallbacks(mController.mResumErrorVideoTask);
		}
		mController.videoUrl = videoUrl;
		mController.runOnUiThread(new Runnable() {
			@Override
		     public void run() {
				try {
					mController.setVisibility(mController.mVideoView, View.GONE);
					if (player != null) {
						release();
						mController.mVideoView.destroyDrawingCache();
						mController.mVideoView.refreshDrawableState();
					}
					mController.state = 1;
					delay(1000);
					player = new MediaPlayer(mController);
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
		Log.d(TAG, "onCompletion");
		mController.mVideoView.destroyDrawingCache();
		mController.cancelTask();
		mController.reset();
		mController.mCurrentState = STATE_PLAYBACK_COMPLETED;
		if(mController.listenerStates[EventKaraokePlugin.ACTION_BIND_EVENT_ENDED_KARAO_INDEX]){
			try {
//				JSONObject jsonEvent = new JSONObject();
//				jsonEvent.put("action", "ended");
				String event = "ended";
				mController.sendEvent(EventKaraokePlugin.ACTION_BIND_EVENT_ENDED_KARAO_LISTENER,event);
			} catch (Exception e) {
				e.printStackTrace();
			}	
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
			
			
			mController.mCurrentState = STATE_ERROR;
			
			mController.isSeek = true;
			mController.isError = true;
			
			
			if(mController.listenerStates[EventKaraokePlugin.ACTION_BIND_EVENT_ERROR_KARAO_INDEX]){
				try {
					JSONObject jsonEvent = new JSONObject();
					jsonEvent.put("error", this.errorMsg);
					mController.sendEvent(EventKaraokePlugin.ACTION_BIND_EVENT_ERROR_KARAO_LISTENER,jsonEvent);
				} catch (JSONException e) {
					e.printStackTrace();
				}	
			}
			
//	    	resume();
	    	
			if (mController.updateTimehandle != null) {
				mController.updateTimehandle.removeCallbacks(mController.mUpdateTimeTask);
			}
			
			mController.mResumErrorHandle.removeCallbacks(mController.mResumErrorVideoTask);
			if(mController.state == 1){
				mController.mResumErrorHandle.postDelayed(mController.mResumErrorVideoTask, 1000L);
			}
			return true;
		}
		

		public void resume() {
			mController.runOnUiThread(new Runnable() {
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

	public boolean startVideo() {
			Log.e(TAG, "startVideo");
		if (mController.clearParam) {
			return false;
		}
		boolean isSuccess = false;
		
		Log.e(TAG, "startVideo 1");
		try {

			if (isInPlaybackState() && ! H265KaraoVp9Player.this.isPlaying()) {
				player.start();
				mController.startCount++;
				isSuccess = true;
				mController.mCurrentState = STATE_PLAYING;
				if (mController.updateTimehandle != null) {
					mController.updateTimehandle.removeCallbacks(mController.mUpdateTimeTask);
				}
				if (mController.state == 1) {
					mController.updateTimehandle.postDelayed(mController.mUpdateTimeTask, FPS);
				}
				
				if(mController.listenerStates[EventKaraokePlugin.ACTION_BIND_EVENT_PLAY_KARAO_INDEX]){
					try {
						JSONObject jsonEvent = new JSONObject();
						jsonEvent.put("action", "play");
						mController.sendEvent(EventKaraokePlugin.ACTION_BIND_EVENT_PLAY_KARAO_LISTENER,jsonEvent);
					} catch (JSONException e) {
						e.printStackTrace();
					}	
				}
				
				if(mController.listenerStates[EventKaraokePlugin.ACTION_BIND_EVENT_FIRST_PLAY_KARAO_INDEX] && mController.startCount == 1){
					try {
						JSONObject jsonEvent = new JSONObject();
						jsonEvent.put("action", "first_play");
						mController.sendEvent(EventKaraokePlugin.ACTION_BIND_EVENT_FIRST_PLAY_KARAO_LISTENER,jsonEvent);
					} catch (JSONException e) {
						e.printStackTrace();
					}	
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return isSuccess;
	}
	  
		public boolean isInPlaybackState() {
			boolean isPlayBack = true;
			if ((this.player == null) || (mController.mCurrentState == STATE_ERROR) || (mController.mCurrentState == STATE_IDLE)
					|| (mController.mCurrentState == STATE_PREPARING)) {
				isPlayBack = false;
			}
			return isPlayBack;
		}
		
	  public boolean pause() {
			if(mController.clearParam){
				return false;
			}
			
			boolean isSuccess = false;
	    if ((isInPlaybackState()) && (H265KaraoVp9Player.this.isPlaying())) {
	    	try {
		        if (mController.updateTimehandle != null){
		        	mController.updateTimehandle.removeCallbacks(mController.mUpdateTimeTask);
		        }
		    	player.pause();
		    	isSuccess = true;
		    	mController.mCurrentState = STATE_PAUSED;
				if(mController.listenerStates[EventKaraokePlugin.ACTION_BIND_EVENT_PAUSE_KARAO_INDEX]){
					try {
						JSONObject jsonEvent = new JSONObject();
						jsonEvent.put("action", "pause");
						mController.sendEvent(EventKaraokePlugin.ACTION_BIND_EVENT_PAUSE_KARAO_LISTENER,jsonEvent);
					} catch (JSONException e) {
						e.printStackTrace();
					}	
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
	    }
	    
	    return isSuccess;
	  }
	  
	  
	  public int getCurrentPosition() {
			if(mController.clearParam){
				return 0;
			}
		    int i = 0;
		    if (!isInPlaybackState()){
		    	i = 0;
		    }else{
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
}
