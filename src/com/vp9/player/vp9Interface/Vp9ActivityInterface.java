package com.vp9.player.vp9Interface;

import java.util.ArrayList;

import org.json.JSONObject;

public interface Vp9ActivityInterface {
	
	public void showEPG();
	public void closeEPG();
	public void sendEvent(JSONObject jsonEvent);
	public void saveSubtiles(ArrayList<String> subTypes);
	public void handleShowOrCloseEPG();
}
