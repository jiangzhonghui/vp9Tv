package com.vp9.player.vp9Interface;


public interface Vp9KaraoPlayerInterface {

	public boolean startVideo();
	
	public int getDuration();
	
	public void release();
	
	public boolean isInPlaybackState();
	
	public void playVideo(final String videoUrl, final boolean isLoop);
	
	public boolean pause();
	
	public boolean seekTo(final int time);
	
	public int getCurrentPosition();
	
	public boolean isPlaying();

	public void reset();
	
	public void resume();

	public int getVideoWidth();

	public int getVideoHeight();

}
