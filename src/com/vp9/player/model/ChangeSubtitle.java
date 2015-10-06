package com.vp9.player.model;

public class ChangeSubtitle {
	
	private String subType;
	
	private boolean isChoice;
	
	private int languageID;
	
	public ChangeSubtitle(String subType, boolean isChoice, int languageID){
		this.subType = subType;
		this.isChoice = isChoice;
		this.setLanguageID(languageID);
	}

	public String getSubType() {
		return subType;
	}

	public void setSubType(String subType) {
		this.subType = subType;
	}

	public boolean isChoice() {
		return isChoice;
	}

	public void setChoice(boolean isChoice) {
		this.isChoice = isChoice;
	}

	public int getLanguageID() {
		return languageID;
	}

	public void setLanguageID(int languageID) {
		this.languageID = languageID;
	}

}
