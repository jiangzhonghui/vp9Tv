package com.vp9.player.model;

import java.util.ArrayList;

import com.vp9.player.subtitles.SubtitleInfo;
import com.vp9.player.subtitles.Utilities;

public class VideoInfo {
	
	private String thumbIcon;
	
	private String movieID;
	
	private String genre;
	
	private int intProgram;
	
	private String program_name;
	
	private String userVideoId;
	
	private int status;
	
	private String runtime;
				
	private String resolution;
	
	private String type;
	
	private String director;
	
	private int likedNum;
				
	private String url;
	
	private String videoName;	

	private String cast;
	
	private String startTime;
				
	private String endTime;
	
	private ArrayList<SubtitleInfo> subtitleInfos;
	
	private int inDay;
	
	private float publicRate;
	
	private int year;
	
	private int videoId;
				
	private int serial;

	private int intStartTimeBySeconds;
	
	private int index;
	
	private int width;
	
	private int height;
	
	private ArrayList<VideoInfo> childVideoInfoList;
	
	private ArrayList<LeftDisplayInfo> leftDisplays;
	
	private ArrayList<LeftDisplayInfo> rightDisplays;
	
	private boolean isCurrentDay;
	
	private String record;
	
//	private ArrayList<VideoResolution> videoResolutions;
	
	private ArrayList<VideoResolutionGroup> videoResolutionGroups;
	
	private String recordUrl;
	
	private String leftDisplaysUrl;
	
	private String rightDisplaysUrl;

	public int size(){
		int size = 0;
		if(childVideoInfoList != null){
			size = childVideoInfoList.size();
		}
		return size;
	}
	public String getThumbIcon() {
		return thumbIcon;
	}

	public void setThumbIcon(String thumbIcon) {
		this.thumbIcon = thumbIcon;
	}

	public String getMovieID() {
		return movieID;
	}

	public void setMovieID(String movieID) {
		this.movieID = movieID;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public int getIntProgram() {
		return intProgram;
	}

	public void setIntProgram(int intProgram) {
		this.intProgram = intProgram;
	}
	
	public String getProgramName(){
		return program_name;
	}
	
	public void setProgramName(String program_name){
		this.program_name = program_name;
	}
	
	public String getRuntime() {
		return runtime;
	}

	public void setRuntime(String runtime) {
		this.runtime = runtime;
	}

	public String getResolution() {
		return resolution;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public String getUserVideoId() {
		return userVideoId;
	}

	public void setUserVideoId(String userVideoId) {
		this.userVideoId = userVideoId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getLikedNum() {
		return likedNum;
	}

	public void setLikedNum(int likedNum) {
		this.likedNum = likedNum;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getVideoName() {
		return videoName;
	}

	public void setVideoName(String videoName) {
		this.videoName = videoName;
	}

	public String getCast() {
		return cast;
	}

	public void setCast(String cast) {
		this.cast = cast;
	}

	public String getStartTime() {
		
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
		this.intStartTimeBySeconds = Utilities.convertStringTimeToSeconds(startTime);
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public int getInDay() {
		return inDay;
	}

	public void setInDay(int inDay) {
		this.inDay = inDay;
	}

	public float getPublicRate() {
		return publicRate;
	}

	public void setPublicRate(float publicRate) {
		this.publicRate = publicRate;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getVideoId() {
		return videoId;
	}

	public void setVideoId(int videoId) {
		this.videoId = videoId;
	}

	public int getSerial() {
		return serial;
	}

	public void setSerial(int serial) {
		this.serial = serial;
	}

	public int getIntStartTimeBySeconds() {
		return intStartTimeBySeconds;
	}

	public void setIntStartTimeBySeconds(int intStartTimeBySeconds) {
		this.intStartTimeBySeconds = intStartTimeBySeconds;
	}

	public ArrayList<SubtitleInfo> getSubtitleInfos() {
		return subtitleInfos;
	}

	public void setSubtitleInfos(ArrayList<SubtitleInfo> subtitleInfos) {
		this.subtitleInfos = subtitleInfos;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public ArrayList<VideoInfo> getChildVideoInfoList() {
		return childVideoInfoList;
	}

	public void setChildVideoInfoList(ArrayList<VideoInfo> childVideoInfoList) {
		this.childVideoInfoList = childVideoInfoList;
	}

	public VideoInfo getChildVideoInfoByIndex(int index) {
		if(childVideoInfoList != null && childVideoInfoList.size() > 0 && index < childVideoInfoList.size()){
			return childVideoInfoList.get(index);
		}
		return null;
	}

	public VideoInfo getChildVideoInfoByTime(int secondInDay) {
		if(childVideoInfoList != null){
			int begin = 0;
			int end = childVideoInfoList.size() - 1;
			while(begin <= end){
				int mid = (begin + end)/2;
				VideoInfo midVideoInfo = childVideoInfoList.get(mid);
				if(secondInDay > midVideoInfo.getIntStartTimeBySeconds()){
					begin = mid + 1; 
				}else if(secondInDay < midVideoInfo.getIntStartTimeBySeconds()){
					end = mid - 1; 
				}else{
					return midVideoInfo;
				}
			}
					
			if(begin - 1 >= 0){
				return childVideoInfoList.get(begin - 1);
			}
		}

		return null;
	}

	public boolean isCurrentDay() {
		return isCurrentDay;
	}

	public void setCurrentDay(boolean isCurrentDay) {
		this.isCurrentDay = isCurrentDay;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public ArrayList<LeftDisplayInfo> getLeftDisplays() {
		return leftDisplays;
	}
	public void setLeftDisplays(ArrayList<LeftDisplayInfo> leftDisplays) {
		this.leftDisplays = leftDisplays;
	}
	public ArrayList<LeftDisplayInfo> getRightDisplays() {
		return rightDisplays;
	}
	public void setRightDisplays(ArrayList<LeftDisplayInfo> rightDisplays) {
		this.rightDisplays = rightDisplays;
	}
	public LeftDisplayInfo getLeftDisplayInfo(float time) {
		int begin = 0;
		int end = this.leftDisplays.size() - 1;
		while(begin <= end){
			int mid = (begin + end)/2;
			LeftDisplayInfo midLeftDisplay = this.leftDisplays.get(mid);
			if(time < midLeftDisplay.getStart()){
				end = mid - 1;  
			}else if(time > midLeftDisplay.getEnd()){
				begin = mid + 1; 
			}else{
				return midLeftDisplay;
			}
		}
		
		if(begin < leftDisplays.size()){
			return leftDisplays.get(begin);
		}
		return null;
	}
	
	public LeftDisplayInfo getRightDisplayInfo(float time) {
		int begin = 0;
		int end = this.rightDisplays.size() - 1;
		while(begin <= end){
			int mid = (begin + end)/2;
			LeftDisplayInfo midRightDisplay = this.rightDisplays.get(mid);
			if(time < midRightDisplay.getStart()){
				end = mid - 1;  
			}else if(time > midRightDisplay.getEnd()){
				begin = mid + 1; 
			}else{
				return midRightDisplay;
			}
		}
		
		if(begin < rightDisplays.size()){
			return rightDisplays.get(begin);
		}
		return null;
	}
	
	public String getRecord() {
		return record;
	}
	public void setRecord(String record) {
		this.record = record;
	}
	
//	public ArrayList<VideoResolution> getVideoResolutions() {
//		return videoResolutions;
//	}
	
//	public void setVideoResolutions(ArrayList<VideoResolution> videoResolutions) {
//		this.videoResolutions = videoResolutions;
//	}

	public void setRecordUrl(String recordUrl) {
		this.recordUrl = recordUrl;
	}
	
	public String getRecordUrl() {
		return recordUrl;
	}
	public String getLeftDisplaysUrl() {
		return leftDisplaysUrl;
	}
	public void setLeftDisplaysUrl(String leftDisplaysUrl) {
		this.leftDisplaysUrl = leftDisplaysUrl;
	}
	public String getRightDisplaysUrl() {
		return rightDisplaysUrl;
	}
	public void setRightDisplaysUrl(String rightDisplaysUrl) {
		this.rightDisplaysUrl = rightDisplaysUrl;
	}
	public ArrayList<VideoResolutionGroup> getVideoResolutionGroups() {
		return videoResolutionGroups;
	}
	public void setVideoResolutionGroups(ArrayList<VideoResolutionGroup> videoResolutionGroups) {
		this.videoResolutionGroups = videoResolutionGroups;
	}
	
}
