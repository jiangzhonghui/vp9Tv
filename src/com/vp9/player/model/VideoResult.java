package com.vp9.player.model;

public class VideoResult {
	
	private VideoInfo videoInfo;
	
	private boolean isCurrentDay;
	
	private int prevIndex;
	
	private int nextIndex;
	
	
	public VideoResult(VideoInfo videoInfo, boolean isCurrentDay){
		this.videoInfo = videoInfo;
		this.isCurrentDay = isCurrentDay;
		this.prevIndex = -1;
		this.nextIndex = -1;
	}

	public VideoInfo getVideoInfo() {
		return videoInfo;
	}

	public void setVideoInfo(VideoInfo videoInfo) {
		this.videoInfo = videoInfo;
	}

	public boolean isCurrentDay() {
		return isCurrentDay;
	}

	public void setCurrentDay(boolean isCurrentDay) {
		this.isCurrentDay = isCurrentDay;
	}

	public int getPrevIndex() {
		return prevIndex;
	}

	public void setPrevIndex(int prevIndex) {
		this.prevIndex = prevIndex;
	}

	public int getNextIndex() {
		return nextIndex;
	}

	public void setNextIndex(int nextIndex) {
		this.nextIndex = nextIndex;
	}

}
