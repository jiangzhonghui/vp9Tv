package com.vp9.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;

public class Vp9RightListView extends ListView {

	private static final String TAG = "Vp9RightListView";

	private Context context;
	
	private ListView friendListView;
	
	private boolean isContinues = true;

	public Vp9RightListView(Context context) {
		super(context);
		this.context = context;
	}
	
	public void setFriendListView(ListView friendListView){
		this.friendListView = friendListView;
	}
	
    @SuppressLint("NewApi")
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	final int newKeyCode = keyCode;
    	final KeyEvent newEevent = event;
    	if(friendListView != null && ((Vp9LeftListView)friendListView).isContinues()){
    		if(context != null){
    			((Activity)context).runOnUiThread(new Runnable() {
    				@Override
    				public void run() {
    					isContinues = false;
    					friendListView.onKeyDown(newKeyCode, newEevent);
    					isContinues = true;
    				}
    			});
    		}
    	}
    	boolean isSucccess = super.onKeyDown(newKeyCode, newEevent);
    	
    	if(friendListView != null){
            for (int i = 0; i < getChildCount(); i++){
                getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
            }
            int itemPosition = getSelectedItemPosition();
            getChildAt(itemPosition).setBackgroundColor(Color.YELLOW);
    	}
//    	int itemPosition = getSelectedItemPosition();
//    	Log.d(TAG, "focusedChild = " + itemPosition);
//    	if(itemPosition >= 0){
//    		Drawable focusedBackground = getChildAt(itemPosition).getBackground();
//        	friendListView.getChildAt(itemPosition).setBackground(focusedBackground);
//        	Log.d(TAG, "position = " + itemPosition);
//    	}
        return isSucccess;
    }

    @SuppressLint("NewApi")
	@Override
    public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
    	final int newKeyCode = keyCode;
    	final int newRepeatCount = repeatCount;
    	final KeyEvent newEvent = event;
    	if(friendListView != null && ((Vp9LeftListView)friendListView).isContinues()){
    		if(context != null){
    			((Activity)context).runOnUiThread(new Runnable() {
    				@Override
    				public void run() {
    					isContinues = false;
    					friendListView.onKeyMultiple(newKeyCode,  newRepeatCount,  newEvent);
    					isContinues = true;
    				}
    			});
    		}
    	}
    	
    	boolean isSucccess = super.onKeyMultiple(keyCode,  repeatCount,  event);
    	if(friendListView != null){
            for (int i = 0; i < getChildCount(); i++){
                getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
            }
            int itemPosition = getSelectedItemPosition();
            getChildAt(itemPosition).setBackgroundColor(Color.YELLOW);
    	}

        
//    	int itemPosition = getSelectedItemPosition();
//    	Log.d(TAG, "focusedChild = " + itemPosition);
//    	if(itemPosition >= 0){
//    		Drawable focusedBackground = getChildAt(itemPosition).getBackground();
//        	friendListView.getChildAt(itemPosition).setBackground(focusedBackground);
//        	Log.d(TAG, "position = " + itemPosition);
//    	}   
        return isSucccess;
    }

    @SuppressLint("NewApi")
	@Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
    	final int newKeyCode = keyCode;
    	final KeyEvent newEvent = event;
    	if(friendListView != null && ((Vp9LeftListView)friendListView).isContinues()){
    		if(context != null){
    			((Activity)context).runOnUiThread(new Runnable() {
    				@Override
    				public void run() {
    					isContinues = false;
    					friendListView.onKeyUp(newKeyCode,  newEvent);
    					isContinues = true;
    				}
    			});
    		}
    	}
    	
    	boolean isSucccess = super.onKeyUp(keyCode, event);
    	
    	if(friendListView != null){
            for (int i = 0; i < getChildCount(); i++){
                getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
            }
            int itemPosition = getSelectedItemPosition();
            getChildAt(itemPosition).setBackgroundColor(Color.YELLOW);
    	}
        
//    	int itemPosition = getSelectedItemPosition();
//    	Log.d(TAG, "focusedChild = " + itemPosition);
//    	if(itemPosition >= 0){
//    		Drawable focusedBackground = getChildAt(itemPosition).getBackground();
//        	friendListView.getChildAt(itemPosition).setBackground(focusedBackground);
//        	Log.d(TAG, "position = " + itemPosition);
//    	}
        return isSucccess;
    }

	public boolean isContinues() {
		return isContinues;
	}

	public void setContinues(boolean isContinues) {
		this.isContinues = isContinues;
	}

}
