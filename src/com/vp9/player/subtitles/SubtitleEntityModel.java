package com.vp9.player.subtitles;

import java.util.ArrayList;

public class SubtitleEntityModel {
	
//	private static final int LEN = 75;

	private ArrayList<SubtitleEntity> subtitleEntityList;
	
//	private Object[] formatTimes;
	
	private float toTime;
	
	private int size;
	
	public SubtitleEntityModel(){
//		formatTimes = new Object[LEN];
//		for(int i = 0; i < LEN; i++){
//			formatTimes[i] = new ArrayList<SubtitleEntity>();
//		}
		this.subtitleEntityList = new ArrayList<SubtitleEntity>();
		size = 0;
	}
	
	public int size(){
		return size;
	}
	
	public void add(SubtitleEntity subtitleEntity){	
		subtitleEntityList.add(subtitleEntity);
		subtitleEntity.setIndex(size);
		size++;
//		float fromTime = subtitleEntity.getBeginTime();
		toTime = subtitleEntity.getEndTime();
	}
	
	public SubtitleEntity getSubtitleByTime(float time){
		int begin = 0;
		int end = subtitleEntityList.size() - 1;
		while(begin <= end){
			int mid = (begin + end)/2;
			SubtitleEntity midSubtitle = subtitleEntityList.get(mid);
			if(time < midSubtitle.getBeginTime()){
				end = mid - 1;  
			}else if(time > midSubtitle.getEndTime()){
				begin = mid + 1; 
			}else{
				return midSubtitle;
			}
		}
		
		if(begin < subtitleEntityList.size()){
			return subtitleEntityList.get(begin);
		}
		return null;
	}

	public SubtitleEntity getSubtitleByIndex(int index) {
		SubtitleEntity subtitleEntity = null;
		if(index < subtitleEntityList.size()){
			subtitleEntity = subtitleEntityList.get(index);
		}
		return subtitleEntity;
	} 
	
	public float getToTime() {
		return toTime;
	}

	public void setToTime(float toTime) {
		this.toTime = toTime;
	}
	
}
