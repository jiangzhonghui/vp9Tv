package com.vp9.player.controller;

import org.json.JSONException;
import org.json.JSONObject;

import tv.vp9.videoproxy.MyService;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Looper;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.View;

import com.vp9.player.vp9Interface.Vp9KaraoPlayerInterface;
import com.vp9.plugin.EventKaraokePlugin;
import com.vp9.tv.KaraokeActivity;

public class NativeKaraoVp9Player implements Vp9KaraoPlayerInterface {

	// private static final String WAITE_CHANNEL_IMAGE =
	// "http://tv.vp9.tv/player/theme/pk-tv.png";
    private String errorMsg;
    private KaraokeActivity mController;
    private MediaPlayer player;
	protected PlayVideoCountDownTimer playVideoCountDownTimer;
	int FPS = 1000/30;
    
	public NativeKaraoVp9Player(KaraokeActivity karaokeActivity) {
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

	private static final String TAG = "NativeVp9Player";



	public void onPrepared(MediaPlayer mp) {
		  mController.isPlay = true;
		  mController.mCurrentState = STATE_PREPARED;
		  Log.d(TAG, "===== onPrepared =====");
		  mController.setVisibility(mController.mVideoView, View.VISIBLE);
//		  startVideo();
		  try {
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
//				lp.height = (int) ((float) screenWidth / videoProportion);
				lp.height = (int) (((float) screenWidth*(float) videoHeight)/(float) videoWidth);
			} else {
//				lp.width = (int) (videoProportion * (float) screenHeight);
				lp.width = (int) (((float) videoWidth * (float) screenHeight)/(float) videoHeight);
				lp.height = screenHeight;
			}
		} else if (mController.intFullScreen == 1) {
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
		}
//		lp.width = screenWidth;
//		lp.height = screenHeight;
		Log.i(TAG, "REAL VIDEO SIZES: W: " + lp.width + " H: " + lp.height);
		Display display = mController.getWindowManager().getDefaultDisplay();

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
		if (mController.clearParam) {
			return;
		}
		player.setOnErrorListener(new MediaPlayer.OnErrorListener() {
			public boolean onError(MediaPlayer paramMediaPlayer, int whatError, int extra) {
				return NativeKaraoVp9Player.this.onError(paramMediaPlayer, whatError, extra);
			}
		});

		player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
			public void onPrepared(MediaPlayer paramMediaPlayer) {
				NativeKaraoVp9Player.this.onPrepared(paramMediaPlayer);
			}
		});

		player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				NativeKaraoVp9Player.this.onCompletion(mp);
			}
		});

		player.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {

			@Override
			public void onBufferingUpdate(MediaPlayer mp, int percent) {
				NativeKaraoVp9Player.this.onBufferingUpdate(mp, percent);

			}
		});

		player.setOnInfoListener(new MediaPlayer.OnInfoListener() {

			@Override
			public boolean onInfo(MediaPlayer mp, int what, int extra) {
				return NativeKaraoVp9Player.this.onInfo(mp, what, extra);
			}
		});

		player.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {

			@Override
			public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
				NativeKaraoVp9Player.this.onVideoSizeChanged(mp, width, height);

			}
		});


	}

	
	public synchronized void release() {
		
//		final MediaPlayer tempPlayer = player;
//		player = null;
		
		Thread thrRelease = new Thread(){
			
			public void run(){
//				Looper.prepare();
//				executeRelease(tempPlayer);
				executeRelease();
//				Looper.loop();
			}
		};
		thrRelease.setName("thrRelease_20");
		thrRelease.start();
//		try {
//			thrRelease.join();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
	}
	
	
	protected synchronized void executeRelease() {
		Log.d(TAG, "release");
		try {
			mController.isPlay = false;
			Log.d(TAG, "release 1");
			if (player != null) {
				player.stop();
				Log.d(TAG, "release 2");
				MediaPlayer tempPlayer = player;
				player = null;
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
		}
		return false;
	}
	
	
	public synchronized void playVideo(final String videoUrl, final boolean isLoop) {
		Log.d(TAG, "----- playVideo -----");
		if (mController.clearParam) {
			return;
		}
		Log.d(TAG, "playVideo 1: " + videoUrl);
		mController.videoUrl = videoUrl;
		Log.d(TAG, "playVideo 2");
		if(mController.mResumErrorHandle != null){
			mController.mResumErrorHandle.removeCallbacks(mController.mResumErrorVideoTask);
		}
		
		if(NativeKaraoVp9Player.this.playVideoCountDownTimer != null){
			NativeKaraoVp9Player.this.playVideoCountDownTimer.cancel();
		}
		mController.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Log.d(TAG, "playVideo 3");
				try {
					mController.setVisibility(mController.mVideoView, View.GONE);
					if (player != null) {
						Log.d(TAG, "playVideo 31");
						// player.reset();
						// player.release();
						// player = null;

						release();
						Log.d(TAG, "playVideo 4");
						if (mController.mVideoView != null) {
							mController.mVideoView.destroyDrawingCache();
							mController.mVideoView.refreshDrawableState();
						}
					}
					Log.d(TAG, "playVideo 5");
					mController.state = 1;
					delay(1000);
					player = new MediaPlayer();
					Log.d(TAG, "playVideo 6");
					NativeKaraoVp9Player.this.reset();
					if(mController.mVideoView != null){
						mController.mVideoView.destroyDrawingCache();
						mController.mVideoView.refreshDrawableState();
				    }
					Log.d(TAG, "playVideo 7");
					player.setLooping(isLoop);
					Log.d(TAG, "playVideo 8");
					mController.mHolder.addCallback(new SurfaceHolder.Callback() {
						@Override
						public void surfaceCreated(SurfaceHolder holder) {
							Log.d(TAG, "playVideo 8");
							mController.mHolder = holder;
							// if(isDisplayVideo){
							if (player != null) {

								Log.e(TAG, "========== surfaceCreated ==========");
								try {
									player.setDisplay(holder);
									holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
									holder.setFormat(PixelFormat.RGBA_8888);
								} catch (Exception e) {
									e.printStackTrace();
								}

							}

						}

						@Override
						public void surfaceDestroyed(SurfaceHolder holder) {
							Log.d(TAG, "playVideo 9");
							Log.e(TAG, "========== surfaceDestroyed ==========");
						}

						@Override
						public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
							Log.d(TAG, "playVideo 10");
							Log.e(TAG, "========== surfaceChanged ==========");
						}
					});


					Log.d(TAG, "playVideo 11");
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
					Log.d(TAG, "playVideo: " + proxyVideoUrl);

					Log.d(TAG, "playVideo 12");
					initFunction();
					Log.d(TAG, "playVideo 13");
					player.prepareAsync();
					Log.d(TAG, "prepareAsync");
					mController.mCurrentState = STATE_PREPARING;
//					Vp9Player.this.playVideoCountDownTimer = new PlayVideoCountDownTimer(5000, 500, videoUrl, isLoop);
//					Vp9Player.this.playVideoCountDownTimer.start();
				} catch (Exception e) {
					e.printStackTrace();
					playVideoInErrorStart(videoUrl, isLoop);
				}


			}
		});

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
		 
	protected synchronized void playVideoInErrorStart(String videoUrl, boolean isLoop) {
		delay(5000);
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
		if (mController.clearParam) {
			return false;
		}
		
		Log.e(TAG, "onError: " + whatError);
		if (whatError == MediaPlayer.MEDIA_ERROR_SERVER_DIED) {
			Log.e(TAG, "Media Error, Server Died: " + extra);
			this.errorMsg = "lỗi kết nối đến máy chủ";
		} else if (whatError == MediaPlayer.MEDIA_ERROR_UNKNOWN) {
			this.errorMsg = "Video bị lỗi.";
			Log.e(TAG, "Media Error, Error Unknown: " + extra);
		} else {
			Log.e(TAG, "Other Error: " + extra);
			this.errorMsg = "Video bị lỗi không rõ nguyên nhân.";
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
		
//		 resume();

		if (mController.updateTimehandle != null) {
			mController.updateTimehandle.removeCallbacks(mController.mUpdateTimeTask);
		}

		mController.mResumErrorHandle.removeCallbacks(mController.mResumErrorVideoTask);

		mController.mResumErrorHandle.postDelayed(mController.mResumErrorVideoTask, 1000L);

		return true;
	}
	
		public void resume() {
			Log.d(TAG, "resume");
		
		Log.d(TAG, "resume 1");
		mController.count = 0;
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

			if (isInPlaybackState() && ! NativeKaraoVp9Player.this.isPlaying()) {
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
				
				if(mController.listenerStates[EventKaraokePlugin.ACTION_BIND_EVENT_FIRST_PLAY_KARAO_INDEX] && mController.startCount == 1){
					try {
						JSONObject jsonEvent = new JSONObject();
						jsonEvent.put("action", "first_play");
						mController.sendEvent(EventKaraokePlugin.ACTION_BIND_EVENT_FIRST_PLAY_KARAO_LISTENER,jsonEvent);
					} catch (JSONException e) {
						e.printStackTrace();
					}	
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
				
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return isSuccess;
	}
	
	
		public boolean isInPlaybackState() {
		boolean isPlayBack = true;
		if ((this.player == null) || (mController.mCurrentState == STATE_ERROR) || (mController.mCurrentState == STATE_IDLE) || (mController.mCurrentState == STATE_PREPARING)) {
			isPlayBack = false;
		}
		return isPlayBack;
	}
	
	
	public boolean pause() {
		Log.d(TAG, "pause");
		if (mController.clearParam) {
			return false;
		}
		boolean isSuccess = false;
		if ((isInPlaybackState()) && (NativeKaraoVp9Player.this.isPlaying())) {
			try {
				if (mController.updateTimehandle != null) {
					mController.updateTimehandle.removeCallbacks(mController.mUpdateTimeTask);
				}
				try {

					player.pause();
					isSuccess = true;
				} catch (Exception e) {
					e.printStackTrace();
					isSuccess = false;
				}
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
				isSuccess = false;
			}
		}
		
		return isSuccess;
	}
	
	
		public int getCurrentPosition() {

		int i = 0;
		if (!isInPlaybackState()) {
			i = 0;
		} else {
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
	
