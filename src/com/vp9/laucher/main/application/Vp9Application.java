package com.vp9.laucher.main.application;

import org.apache.cordova.CordovaActivity;
import org.apache.cordova.CordovaWebView;

import android.app.Activity;
import android.app.Application;
import android.widget.TextView;

public class Vp9Application extends Application {
	CordovaWebView appView;
	private TextView textViewChannelNum;
	private CordovaActivity activityLauncher;
	private CordovaActivity activityAppVP9;

	public Vp9Application() {
	}

	public CordovaWebView getAppView() {
		if (appView != null) {
			return appView;
		}
		return null;
	}

	public void setAppView(CordovaWebView appView) {
		if (appView != null) {
			this.appView = appView;
		}
	}

	public TextView getTextViewChannelNum() {
		return textViewChannelNum;
	}

	public void setTextViewChannelNum(TextView textViewChannelNum) {
		this.textViewChannelNum = textViewChannelNum;
	}
	
	public void setActivityLauncher(CordovaActivity activity){
		this.activityLauncher = activity;
	}
	
	public Activity getActivityLauncher() {
		return activityLauncher;
	}
	
	public void setActivityAppVP9(CordovaActivity activity){
		this.activityAppVP9 = activity;
	}
	
	public Activity getActivityAppVP9() {
		return activityAppVP9;
	}
	
	
}
