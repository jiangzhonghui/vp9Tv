package com.vp9.player.subtitles;

public class SubtitleInfo {
	
	private String subUrl;
	private String subType;
	private String subTypeName;
	private boolean isDisplay;
	private int languageId;
	private boolean isExist;
		
	public SubtitleInfo(String subUrl, String subType, String subTypeName, boolean isDisplay){
		this.subUrl = subUrl;
		this.subType = subType;
		this.subTypeName = subTypeName;
		this.setDisplay(isDisplay);
		this.isExist = true;
	}
	
	public SubtitleInfo(String subUrl, int languageId, String subTypeName, boolean isDisplay){
		this.subUrl = subUrl;
		this.languageId = languageId;
		this.subType = String.valueOf(this.languageId);
		this.subTypeName = subTypeName;
		this.setDisplay(isDisplay);
		this.isExist = true;
	}

	public String getSubUrl() {
		return subUrl;
	}

	public void setSubUrl(String subUrl) {
		this.subUrl = subUrl;
	}

	public String getSubType() {
		return subType;
	}

	public void setSubType(String subType) {
		this.subType = subType;
	}

	public String getSubTypeName() {
		return subTypeName;
	}

	public void setSubTypeName(String subTypeName) {
		this.subTypeName = subTypeName;
	}

	public boolean isDisplay() {
		return isDisplay;
	}

	public void setDisplay(boolean isDisplay) {
		this.isDisplay = isDisplay;
	}

	public int getLanguageId() {
		return languageId;
	}

	public void setLanguageId(int languageId) {
		this.languageId = languageId;
	}

	public boolean isExist() {
		return isExist;
	}

	public void setExist(boolean isExist) {
		this.isExist = isExist;
	}

}
