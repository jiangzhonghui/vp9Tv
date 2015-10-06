package com.vp9.player.subtitles;

import java.util.Timer;
import java.util.TimerTask;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.text.Html;
import android.view.SurfaceView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.vp9.player.controller.MediaController;
import com.vp9.player.vp9Interface.Vp9PlayerInterface;

public class LoadViewTask extends AsyncTask<Void, Integer, Void> {
	
	private final String TAG = "LoadViewTask";
	private MediaController mController;
	private Timer autoUpdate;
//	private int currentSubIndex = -1;
	private Boolean isLibs;
	private SurfaceView myVideoView;
//	private SubtitleEntityModel subEntityModel;
	private SubtitleEntityModel[] subEntityModels;
	private TextView[] tvSubs;
	private TextView[] tvSubs2;
	private String[] urlSubs;
	private Vp9PlayerInterface player;

//	private float beginTime;
//
//	private float endTime;
//
//	private boolean isDisplay;
	
	private float[] beginTimes;

	private float[] endTimes;

	private boolean[] isDisplays;

//	private SubtitleEntity currSubtitle;
	
	private SubtitleEntity[] currSubtitles;
	
	private int[] currentSubIndexs;
	
	private int subLen;
	private TimerTask timerTask;
	
	private boolean[] isSeek;
	

	public LoadViewTask(MediaController mController, SubtitleInfo[] subtitleInfos,
			Boolean paramBoolean) {
		this.mController = mController;
		this.myVideoView = mController.mVideoView;
		this.player = mController.vp9Player;
		this.isLibs = paramBoolean;
		this.tvSubs = mController.tvSubs;
		this.tvSubs2 = mController.tvSubs2;
		this.subLen = subtitleInfos.length;
		this.urlSubs = new String[this.subLen];
		this.beginTimes = new float[this.subLen];
		this.endTimes = new float[this.subLen];
		this.isDisplays = new boolean[this.subLen];
		this.currSubtitles = new SubtitleEntity[this.subLen];
		this.currentSubIndexs = new int[this.subLen];
		this.isSeek = new boolean[this.subLen];
		for(int i = 0; i < this.urlSubs.length; i++){
			this.urlSubs[i] = subtitleInfos[i].getSubUrl();
			this.beginTimes[i] = 0;
			this.endTimes[i] = 0;
			this.isDisplays[i] = false;
			this.currentSubIndexs[i] = 1;
			this.isSeek[i] = this.mController.isSeek;
		}
	}
	
	public void setSeek(boolean blSeek){
		synchronized (this.isSeek) {
			if(this.isSeek != null){
				for(int i = 0; i < this.isSeek.length; i++){
					this.isSeek[i] = blSeek;
				}
			}
		}
	}

	public void cancelTask() {
		if(this.tvSubs != null){
			for(int i = 0; i < this.tvSubs.length; i++){
//				this.tvSubs[i].setText("");
				setTextForTextView(this.tvSubs[i], "");
			}
		}
		
		if(mController.is3D && this.tvSubs2 != null){
			for(int i = 0; i < this.tvSubs2.length; i++){
//				this.tvSubs[i].setText("");
				setTextForTextView(this.tvSubs2[i], "");
			}
		}
		
		if (this.autoUpdate != null) {
			this.autoUpdate.cancel();
			this.autoUpdate = null;
		}
		if(this.timerTask != null){
			this.timerTask.cancel();
		}
	}

	protected Void doInBackground(Void[] paramArrayOfVoid) {
		this.subEntityModels = new SubtitleEntityModel[this.subLen];
		for(int i = 0; i < this.subLen; i++){
			this.subEntityModels[i] = new SubtitleParser(mController, i).playSubtitle(this.urlSubs[i]);
		}
		final Timer newTime = new Timer();
		final TimerTask newTimerTask = new TimerTask() {
			public void run() {
				loadSubtitle();
			}
		};
//		this.autoUpdate = new Timer();
//		this.timerTask = new TimerTask() {
//			public void run() {
//				loadSubtitle();
//			}
//		};
		
		newTime.schedule(newTimerTask, 500L, 200L);
		
		this.autoUpdate = newTime;
		this.timerTask = newTimerTask;
		return null;
	}
	
	
	private synchronized void loadSubtitle() {
		try {
			if(mController.vp9Player != null && mController.vp9Player.isInPlaybackState()){
				float currentPosition = (float)LoadViewTask.this.player.getCurrentPosition()/1000;
//				boolean[] isSeek = new boolean[this.subLen];
//				boolean isSeek = LoadViewTask.this.vp9Player.isSeek;
				for(int i = 0; i < this.subLen; i++){
//					loadSubtitle(currentPosition, isSeek, i);
					loadSubtitle(currentPosition, i);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	private void loadSubtitle(float currentPosition, boolean isSeek, int index) {
	private void loadSubtitle(float currentPosition, int index) {
//		if(isSeek){
//		Log.d(TAG, "loadSubtitle 1");
		if(isSeek[index]){
//			Log.d(TAG, "isSeek " + index +": " + isSeek[index]);
//			LoadViewTask.this.tvSubs[index].setText(Html.fromHtml(""));
			setTextForTextView(tvSubs[index], "");
			if(mController.is3D){
				setTextForTextView(tvSubs2[index], "");
			}
			currSubtitles[index] = subEntityModels[index].getSubtitleByTime(currentPosition);
			
//			Log.d(TAG, "loadSubtitle 2");
			if(currSubtitles[index] != null){
//				Log.d(TAG, "loadSubtitle 3");
				isSeek[index] = false;
				beginTimes[index] = currSubtitles[index].getBeginTime();
				endTimes[index] = currSubtitles[index].getEndTime();
				currentSubIndexs[index] = currSubtitles[index].getIndex();
				if(currentPosition >= beginTimes[index] && currentPosition <= endTimes[index]){
//					Log.d(TAG, "loadSubtitle 4");
					isDisplays[index] = true;
					setTextForTextView(tvSubs[index], currSubtitles[index].getContent());
					if(mController.is3D){
						setTextForTextView(tvSubs2[index], currSubtitles[index].getContent());
					}
				}else{
//					Log.d(TAG, "loadSubtitle 5");
					isDisplays[index] = false;
					setTextForTextView(tvSubs[index], "");
					if(mController.is3D){
						setTextForTextView(tvSubs2[index], "");
					}
				}

			}else{
//				Log.d(TAG, "loadSubtitle 6");
				isDisplays[index] = false;
				setTextForTextView(LoadViewTask.this.tvSubs[index], "");
				if(mController.is3D){
					setTextForTextView(LoadViewTask.this.tvSubs2[index], "");
				}
			}
//			Log.d(TAG, "loadSubtitle 7");
			return;
		}
//		Log.d(TAG, "loadSubtitle 8");
		if (endTimes[index] <= currentPosition) {
//			Log.d(TAG, "loadSubtitle 9");
			currSubtitles[index] = subEntityModels[index].getSubtitleByIndex(currentSubIndexs[index] + 1);
			if (currSubtitles[index] == null) {
//				Log.d(TAG, "loadSubtitle 10");
				isDisplays[index] = false;
				setTextForTextView(LoadViewTask.this.tvSubs[index], "");
				if(mController.is3D){
					setTextForTextView(LoadViewTask.this.tvSubs2[index], "");
				}
				return;
			}
//			Log.d(TAG, "loadSubtitle 11");
			beginTimes[index] = currSubtitles[index].getBeginTime();
			endTimes[index] = currSubtitles[index].getEndTime();
			currentSubIndexs[index]++;
			if (beginTimes[index] <= currentPosition && endTimes[index] >= currentPosition) {
//				Log.d(TAG, "loadSubtitle 12");
				isDisplays[index] = true;
				setTextForTextView(tvSubs[index], currSubtitles[index].getContent());
				if(mController.is3D){
					setTextForTextView(tvSubs2[index], currSubtitles[index].getContent());	
				}
			} else {
//				Log.d(TAG, "loadSubtitle 13");
				isDisplays[index] = false;
				setTextForTextView(tvSubs[index], "");
				if(mController.is3D){
					setTextForTextView(tvSubs2[index], "");
				}
			}
			
		} else {
//			Log.d(TAG, "loadSubtitle 14");
			if (!isDisplays[index] && beginTimes[index] < currentPosition) {
				isDisplays[index] = true;
				setTextForTextView(LoadViewTask.this.tvSubs[index], currSubtitles[index].getContent());
				if(mController.is3D){
					setTextForTextView(LoadViewTask.this.tvSubs2[index], currSubtitles[index].getContent());
				}
			}
		}
		
	}

	private void setTextForTextView(final TextView view, final String text) {
		LoadViewTask.this.mController.getActivity().runOnUiThread(new Runnable() {
			public void run() {
				String newText = text.replaceAll("\\{\\\\[\\w]*\\}", "");
				view.setText(Html.fromHtml(newText));
//				view.setText(text);
//				view.setShadowLayer(3.0f, 5, 5, Color.BLACK);
				view.setShadowLayer(10.0f, 0, 0, Color.BLACK);
			}

		});
		
	}

	protected void onPostExecute(Void paramVoid) {
	}

	protected void onPreExecute() {
	}

	protected void onProgressUpdate(Integer[] paramArrayOfInteger) {
	}
}