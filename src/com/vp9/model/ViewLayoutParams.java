package com.vp9.model;

public class ViewLayoutParams {
	
	private int width;
	
	private int leftMargin;
	
	private int rightMargin;
	
	private float scale;
	
	private boolean isScale;
	
	private int layoutType;

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getLeftMargin() {
		return leftMargin;
	}

	public void setLeftMargin(int leftMargin) {
		this.leftMargin = leftMargin;
	}

	public int getRightMargin() {
		return rightMargin;
	}

	public void setRightMargin(int rightMargin) {
		this.rightMargin = rightMargin;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public boolean isScale() {
		return isScale;
	}

	public void setScale(boolean isScale) {
		this.isScale = isScale;
	}

	public int getLayoutType() {
		return layoutType;
	}

	public void setLayoutType(int layoutType) {
		this.layoutType = layoutType;
	}

}
