package com.vp9.player.vp9Interface;


public interface Vp9PlayerInterface {

	public void startVideo();
	
	public int getDuration();
	
	public void release();
	
	public void releaseNoneThread();
	
	public boolean isInPlaybackState();
	
	public void playVideo(final String videoUrl, final boolean isLoop);
	
	public void pause();
	
	public void seekTo(final int time);
	
	public int getCurrentPosition();
	
	public boolean isPlaying();

	public void reset();
	
	public void resume();

	public int getVideoWidth();

	public int getVideoHeight();

	public void changeSource(String url);
	
	
}
