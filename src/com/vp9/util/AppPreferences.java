package com.vp9.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public enum AppPreferences {

	INSTANCE;

	public static final String IS_QUIT = "isQuit";

	private SharedPreferences pre;
	
	public static final String IS_CHANNEL_NUMBER = "is_channel_number";
	
	public static final String SUBTITLES = "subtitles";
	
	
	public static final String CODEC = "codec";
	
	public static final String CODEC_RESOLUTION = "codecResolution";
	
	public static final String INT_PROXY_SPEED_DISPLAY = "intProxySpeedDisplay";

	/**
	 * This method must be called in Application class.
	 * 
	 * @param context
	 */
	public static void assignContext(Context context) {
		if (INSTANCE.pre != null || context == null) {
			return;
		}
		INSTANCE.pre = PreferenceManager.getDefaultSharedPreferences(context);
	}

	public void saveQuit(boolean isQuit) {
		SharedPreferences.Editor editor = pre.edit();
		editor.putBoolean(IS_QUIT, isQuit);
		editor.commit();
	}

	public boolean isQuit() {
		return pre.getBoolean(IS_QUIT, true);
	}
	
	public void saveIsChannelNumber(boolean isChannelNumber) {
		SharedPreferences.Editor editor = pre.edit();
		editor.putBoolean(IS_CHANNEL_NUMBER, isChannelNumber);
		editor.commit();
	}
	
	public void saveSubtitles(ArrayList<String> subtitles) {
		SharedPreferences.Editor editor = pre.edit();
		editor.putStringSet(SUBTITLES, new HashSet<String>(subtitles));
		editor.commit();
	}
	
	
	public ArrayList<String> getSubtitles() {
		ArrayList<String> subtitles = null;
		Set<String> subtitlesSet = pre.getStringSet(SUBTITLES, new HashSet<String>());
		if(subtitlesSet != null){
			subtitles = new ArrayList<String>();
			
		    Iterator<String> iterator = subtitlesSet.iterator();

		    while(iterator.hasNext()){

		        String value = iterator.next();
		        
		        subtitles.add(value);
		    }
		}
		
		return subtitles;
	}
	
	
	public boolean isChannelNumber() {
		return pre.getBoolean(IS_CHANNEL_NUMBER, false);
	}
	
	public void saveCodec(String codec) {
		SharedPreferences.Editor editor = pre.edit();
		editor.putString(CODEC, codec);
		editor.commit();
	}
	
	public String getCodec() {
		String codec = null;
		if(pre.contains(CODEC)){
			codec = pre.getString(CODEC, null);
		}
		return codec;
	}
	
	public void saveCodecResolution(String codecResolution) {
		SharedPreferences.Editor editor = pre.edit();
		editor.putString(CODEC_RESOLUTION, codecResolution);
		editor.commit();
	}
	
	public String getCodecResolution() {
		String codecResolution = null;
		if(pre.contains(CODEC_RESOLUTION)){
			codecResolution = pre.getString(CODEC_RESOLUTION, null);
		}
		return codecResolution;
	}
	
	
	public void saveIntProxySpeedDisplay(int intProxySpeedDisplay) {
		SharedPreferences.Editor editor = pre.edit();
		editor.putInt(INT_PROXY_SPEED_DISPLAY, intProxySpeedDisplay);
		editor.commit();
	}
	
	public int getIntProxySpeedDisplay() {
		int intProxySpeedDisplay = 3;
		if(pre.contains(INT_PROXY_SPEED_DISPLAY)){
			intProxySpeedDisplay = pre.getInt(INT_PROXY_SPEED_DISPLAY, 3);
		}
		return intProxySpeedDisplay;
	}
	
	
}
