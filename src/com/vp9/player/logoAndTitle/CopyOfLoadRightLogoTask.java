package com.vp9.player.logoAndTitle;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Timer;
import java.util.TimerTask;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vp9.player.controller.MediaController;
import com.vp9.player.model.LeftDisplayInfo;
import com.vp9.player.model.VideoInfo;
import com.vp9.player.model.VideoResult;
import com.vp9.player.serveTime.ServerTimeInfo;
import com.vp9.player.vp9Interface.Vp9PlayerInterface;

public class CopyOfLoadRightLogoTask extends AsyncTask<Void, Integer, Void> {
	
private static final String TAG = "LoadRightLogoTask";

	//	private final String TAG = "LoadLeftLogoTask";
	private MediaController mController;
	
	private Timer autoUpdate;
	
	private boolean isDisplays;
	
	private TimerTask timerTask;
	
	private VideoInfo playingVideo;
	private ImageView logoVideo;
	private TextView videoTitle;
	private Vp9PlayerInterface player;
	
	public CopyOfLoadRightLogoTask(MediaController mController, VideoInfo playingVideo,
			Vp9PlayerInterface player, ImageView logoVideo, TextView videoTitle) {
		this.mController = mController;
		this.playingVideo = playingVideo;
		this.logoVideo = logoVideo;
		this.videoTitle = videoTitle;
		this.player = player;		
	}


	public void cancelTask() {
		setImageBitmap(logoVideo, "");
		setTextForTextView(videoTitle, "VP9.TV");
		setVisibility(logoVideo, View.GONE);
		setVisibility(videoTitle, View.GONE);
		if (this.autoUpdate != null) {
			this.autoUpdate.cancel();
			this.timerTask.cancel();
			this.autoUpdate = null;
		}
	}

	protected Void doInBackground(Void[] paramArrayOfVoid) {
		this.autoUpdate = new Timer();
		this.timerTask = new TimerTask() {
			public void run() {
				loadRightDisplays();
			}
		};
		
		this.autoUpdate.schedule(timerTask, 1000L, 500L);
		return null;
	}
	

	private void loadRightDisplays() {
		try {
			float currentPosition = (float)CopyOfLoadRightLogoTask.this.player.getCurrentPosition()/1000;
			
			LeftDisplayInfo displayInfo = null;
			if(this.mController.videoType != 1){
				if(playingVideo != null && playingVideo.getRightDisplays() != null && playingVideo.getRightDisplays().size() > 0){
					displayInfo = playingVideo.getRightDisplayInfo(currentPosition);
				}
			}else if(this.mController.demandTiviSchedule != null){
				ServerTimeInfo serverTimeInfo = new ServerTimeInfo(this.mController.serverTimeUrl);
				int secondInDay = serverTimeInfo.getSecondInDay() + 1;
				VideoResult liveVideoIResult = this.mController.demandTiviSchedule.getVideoInfoByTime(secondInDay);
				if(liveVideoIResult != null){
					VideoInfo liveVideoInfo = liveVideoIResult.getVideoInfo();
					if(liveVideoInfo != null && liveVideoInfo.getRightDisplays() != null && liveVideoInfo.getRightDisplays().size() > 0){
						displayInfo = liveVideoInfo.getRightDisplayInfo(currentPosition);
					}
				}
			}
			if(displayInfo != null){
				if(!isDisplays && displayInfo.getStart() <= currentPosition && displayInfo.getEnd() >= currentPosition) {
					
					int type = displayInfo.getRightType();
					Log.e(TAG, "RightType: " + type);
					switch (type) {
					case 0:
						setTextForTextView(videoTitle, displayInfo.getHtml());
						setVisibility(videoTitle, View.VISIBLE);
						setVisibility(logoVideo, View.GONE);
						break;
						
					case 1:
						setImageBitmap(logoVideo, displayInfo.getIconUrl());
						setVisibility(logoVideo, View.VISIBLE);
						setVisibility(videoTitle, View.GONE);
						if(displayInfo.getHeight() != -1 && displayInfo.getWidth() != -1){
							int width = displayInfo.getWidth();
							int height = displayInfo.getHeight();
							setLayoutParams(logoVideo, width, height);
						}
						break;

					case 2:
						Log.e(TAG, "case 2a: " + displayInfo.getHtml());
						Log.e(TAG, "case 2b: " + displayInfo.getIconUrl());
						setTextForTextView(videoTitle, displayInfo.getHtml());
						setImageBitmap(logoVideo, displayInfo.getIconUrl());
//						setVisibility(videoTitle, View.GONE);
						setVisibility(videoTitle, View.VISIBLE);
						setVisibility(logoVideo, View.VISIBLE);
						if(displayInfo.getHeight() != -1 && displayInfo.getWidth() != -1){
							int width = displayInfo.getWidth();
							int height = displayInfo.getHeight();
							setLayoutParams(logoVideo, width, height);
						}
						break;
					}
				}else{
					setVisibility(logoVideo, View.GONE);
					setVisibility(videoTitle, View.GONE);
				}
			}else{
				setVisibility(logoVideo, View.GONE);
				setVisibility(videoTitle, View.GONE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void setLayoutParams(final View view, final int width, final int height) {
		if(mController != null && mController.getActivity() != null && view != null){
			mController.getActivity().runOnUiThread(new Runnable() {
			     @Override
			     public void run() {
			    	 RelativeLayout.LayoutParams parms = new RelativeLayout.LayoutParams(width, height);
			    	 view.setLayoutParams(parms);
			     }
			});
		}
		
	}


	private void setImageBitmap(final ImageView imageView, final String imageUrl) {
		if(mController != null && mController.getActivity() != null && imageView != null && !("").equals(imageUrl.trim()) ){
			mController.getActivity().runOnUiThread(new Runnable() {
			     @Override
			     public void run() {
					try {
						Bitmap bmp = BitmapFactory.decodeStream(new java.net.URL(imageUrl).openStream());
						imageView.setImageBitmap(bmp);
					} catch (MalformedURLException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}  
			     }
			});
		}else if(imageUrl == null || ("").equals(imageUrl.trim())){
			mController.getActivity().runOnUiThread(new Runnable() {
			     @Override
			     public void run() {
			    	 try {
				    	 imageView.setImageResource(0);
				    	 imageView.setImageDrawable(null);
			    	 } catch (Exception e) {
						e.printStackTrace();
					}  
			     }
			});
		}
	}
	
	public void setVisibility(final View view, final int type){
		if(mController != null && mController.getActivity() != null && view != null){
			mController.getActivity().runOnUiThread(new Runnable() {
			     @Override
			     public void run() {
			    	 view.setVisibility(type);
			     }
			});
		}
	}

	private void setTextForTextView(final TextView view, final String text) {
		if(mController != null && mController.getActivity() != null && view != null){
			mController.getActivity().runOnUiThread(new Runnable() {
				public void run() {
					view.setText(Html.fromHtml(text));
//					view.setText(text);
//					view.setShadowLayer(3.0f, 5, 5, Color.BLACK);
					view.setShadowLayer(5.0f, 0, 0, Color.BLACK);
				}
			});
		}
	}

	protected void onPostExecute(Void paramVoid) {
	}

	protected void onPreExecute() {
	}

	protected void onProgressUpdate(Integer[] paramArrayOfInteger) {
	}
}