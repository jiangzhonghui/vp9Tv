package com.vp9.player.subtitles;

public class SubtitleEntity {
	
	private String strBeginTime = "";

	private String strEndTime = "";

	private String content = "";

	private float beginTime = 0.0F;

	private float endTime = 0.0F;
	
	private int index;
	
	
	public SubtitleEntity(String strBeginTime, String strEndTime, String content){
		
		this.strBeginTime = strBeginTime;
		
		this.strEndTime = strEndTime;
		
		
		this.content = content;
		
		format();
		

		
		this.beginTime = toSeconds(this.strBeginTime);
		
		this.endTime = toSeconds(this.strEndTime);

	}

	private void format() {
		if(this.content != null){
			this.content = this.content.trim().replace("\\N", "<br/>");
			this.content = this.content.trim().replace("{\\b1}", "<b>");
			this.content = this.content.trim().replace("{\\b0}", "</b>");
			this.content = this.content.trim().replace("{\\i1}", "<i>");
			this.content = this.content.trim().replace("{\\i0}", "</i>");
			this.content = this.content.trim().replace("{\\u1}", "<u>");
			this.content = this.content.trim().replace("{\\u0}", "</u>");
			this.content = this.content.trim().replace("{\\s1}", "<s>");
			this.content = this.content.trim().replace("{\\s0}", "</s>");
		}
		
	}

	public float toSeconds(String paramString) {
		float f = 0.0F;
		if (paramString != "") {
			String[] arrayOfString = paramString.split(":");
			for (int i = 0; i < arrayOfString.length; i++)
				f = 60.0F * f
						+ Float.parseFloat(arrayOfString[i].replace(",", "."));
		}
		return f;
	}
	
	public String getStrBeginTime() {
		return strBeginTime;
	}

	public void setStrBeginTime(String strBeginTime) {
		this.strBeginTime = strBeginTime;
	}

	public String getStrEndTime() {
		return strEndTime;
	}

	public void setStrEndTime(String strEndTime) {
		this.strEndTime = strEndTime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public float getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(float beginTime) {
		this.beginTime = beginTime;
	}

	public float getEndTime() {
		return endTime;
	}

	public void setEndTime(float endTime) {
		this.endTime = endTime;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
}