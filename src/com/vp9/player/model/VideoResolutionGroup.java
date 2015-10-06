package com.vp9.player.model;

import java.util.ArrayList;

public class VideoResolutionGroup {
	
	private String codec;
	
	private String resolution;
	
	private int index;
	
	private ArrayList<VideoResolution> videoResolutionList;
	
	public VideoResolutionGroup(){
		videoResolutionList = new ArrayList<VideoResolution>();
	}

	public void add(VideoResolution videoResolution){
		
		videoResolutionList.add(videoResolution);
		
	}
	public String getCodec() {
		return codec;
	}

	public void setCodec(String codec) {
		this.codec = codec;
	}

	public String getResolution() {
		return resolution;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	public ArrayList<VideoResolution> getVideoResolutionList() {
		return videoResolutionList;
	}

	public void setVideoResolutionList(ArrayList<VideoResolution> videoResolutionList) {
		this.videoResolutionList = videoResolutionList;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getVideoUrl() {
		String videoUrl = "";
		if(videoResolutionList != null && videoResolutionList.size() > 0){
			VideoResolution maxVideoResolution = videoResolutionList.get(0);
			int maxPriority = maxVideoResolution.getPriority();
			for(int i = 1; i < videoResolutionList.size(); i++){
				VideoResolution videoResolution = videoResolutionList.get(i);
				int priority = videoResolution.getPriority();
				if(maxPriority < priority){
					maxPriority = priority;
					maxVideoResolution = videoResolution;
				}
			}
			
			videoUrl = maxVideoResolution.getVideoUrl();
		}
		return videoUrl;
	}
}
