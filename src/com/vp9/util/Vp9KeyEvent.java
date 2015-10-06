package com.vp9.util;

import android.view.KeyEvent;


public class Vp9KeyEvent {
	
	
	public static final  int KEYCODE_GAME = 141;

	public static final int KEYCODE_FILM = 142;
	
	public static final int KEYCODE_DPAD_UP = 19;
	
	public static final int KEYCODE_DPAD_DOWN = 20;
	
	public static final int KEYCODE_DPAD_LEFT = 21;
	
	public static final int KEYCODE_DPAD_RIGHT = 22;
	
    public static final int KEYCODE_DPAD_CENTER = 23;
	
	public static final int KEYCODE_VOLUME_UP = 24;
	
	public static final int KEYCODE_VOLUME_DOWN = 25;
	
	public static final int KEYCODE_MEDIA_PLAY_PAUSE = 85;
	
	public static final int KEYCODE_MEDIA_NEXT = 87;
	  
	public static final int KEYCODE_MEDIA_PREVIOUS = 88;
	
	public static final int KEYCODE_MENU = 82;
	
	public static final int KEYCODE_BACK = 4;
	
	public static final int KEYCODE_F1 = 131;
	  
	public static final int KEYCODE_F2 = 132;
	  
	public static final int KEYCODE_F3 = 133;
	  
	public static final int KEYCODE_F4 = 134;
	  
	public static final int KEYCODE_F9 = 139;
	  
	public static final int KEYCODE_F10 = 140;
	  
	public static final int KEYCODE_F11 = 141;
	  
	public static final int KEYCODE_F12 = 142;
	
	public static final int KEYCODE_ENTER = 66;
	
	public static final int KEYCODE_ZOOM_IN = 168;
	
	public static final int KEYCODE_UNKNOWN = 0;
	
	public static int getKeyCode(int key) {
		int vp9Key = -1;
		switch (key){
		
		case KeyEvent.KEYCODE_MENU:
			vp9Key = KEYCODE_MENU;
			break;
			
		case KeyEvent.KEYCODE_DPAD_UP:
			vp9Key = KEYCODE_DPAD_UP;
			break;

		case KeyEvent.KEYCODE_DPAD_DOWN:
			vp9Key = KEYCODE_DPAD_DOWN;
			break;
			
		case KeyEvent.KEYCODE_DPAD_LEFT:
			vp9Key = KEYCODE_DPAD_LEFT;
			break;

		case KeyEvent.KEYCODE_DPAD_RIGHT:
			vp9Key = KEYCODE_DPAD_RIGHT;
			break;
			
		case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
			vp9Key = KEYCODE_MEDIA_PLAY_PAUSE;
			break;

		case KeyEvent.KEYCODE_MEDIA_NEXT:
			vp9Key = KEYCODE_MEDIA_NEXT;
			break;
			
		case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
			vp9Key = KEYCODE_MEDIA_PREVIOUS;
			break;
			
		case KeyEvent.KEYCODE_F9:
			vp9Key = KEYCODE_F9;
			break;
			
		case KeyEvent.KEYCODE_F10:
			vp9Key = KEYCODE_F10;
			break;
			
		case KeyEvent.KEYCODE_F11:
			vp9Key = KEYCODE_F11;
			break;
			
		case KeyEvent.KEYCODE_F12:
			vp9Key = KEYCODE_F12;
			break;
			
			
		case KeyEvent.KEYCODE_F1:
			vp9Key = KEYCODE_F1;
			break;
			
		case KeyEvent.KEYCODE_F2:
			vp9Key = KEYCODE_F2;
			break;
			
		case KeyEvent.KEYCODE_F3:
			vp9Key = KEYCODE_F3;
			break;
			
		case KeyEvent.KEYCODE_F4:
			vp9Key = KEYCODE_F4;
			break;
			
		case KeyEvent.KEYCODE_ENTER :
			vp9Key = KEYCODE_ENTER;
			break;
		case KeyEvent.KEYCODE_ZOOM_IN:
			vp9Key = KEYCODE_ZOOM_IN;
			break;
		case KeyEvent.KEYCODE_UNKNOWN:
			vp9Key = KEYCODE_UNKNOWN;
			break;
		case KeyEvent.KEYCODE_DPAD_CENTER:
			vp9Key = KEYCODE_DPAD_CENTER;
			break;
		}
		
		return vp9Key;

	}
	
	
	
}
