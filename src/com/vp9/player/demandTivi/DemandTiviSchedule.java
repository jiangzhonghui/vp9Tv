package com.vp9.player.demandTivi;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.vp9.player.controller.MediaController;
import com.vp9.player.model.LeftDisplayInfo;
import com.vp9.player.model.VideoInfo;
import com.vp9.player.model.VideoResolution;
import com.vp9.player.model.VideoResolutionGroup;
import com.vp9.player.model.VideoResult;
import com.vp9.player.serveTime.ServerTimeInfo;
import com.vp9.player.subtitles.SubtitleInfo;
import com.vp9.util.FileUtil;
import com.vp9.util.ParamUtil;
import com.vp9.util.Vp9ParamUtil;

public class DemandTiviSchedule {
	private static final String TAG = "DemandTiviSchedule";
	
	private MediaController mControl;
	private String channelIcon;
	
	private String channelUrl;
	
	private int channelType;
	
	private String channelName;
	
	private int defaultStart;
	
	private int currentIndex = -1; 
	
	private int currentChildIndex = -1;
	
	private String strdate;
	
	private ArrayList<VideoInfo> videoInfoList;

	private int channelId;
	
	private ArrayList<String> recordVideoIds;
	private String recordUrl;
	
	
	public DemandTiviSchedule(String cheduleUrl, int videoType, MediaController _mControl) {
		this.mControl = _mControl;
		parserCheduleUrl(cheduleUrl, videoType);

	}
	
	public DemandTiviSchedule() {

	}
	 
	public void clean(){
		
		if(videoInfoList != null){
			videoInfoList.clear();
		}
		
		if(recordVideoIds != null){
			recordVideoIds.clear();
		}
		
	}
	
	public int getSizeVideoInfos(){
		int size = 0;
		if(videoInfoList != null){
			size = videoInfoList.size();
		}
		return size;
	}

	public void parserCheduleUrl(final String cheduleUrl, int videoType) {
		if(videoType == 1){
			parserCheduleUrl1(cheduleUrl);
		} else if(videoType == 2){
			parserCheduleUrl1(cheduleUrl);
		}else if(videoType == 3){
			parserCheduleUrl2(cheduleUrl);
		}
	}
	
	public void parserCheduleUrl2(String cheduleUrl) {
//		Log.e(TAG, "cheduleUrl: " + cheduleUrl);
		currentIndex = -1;
		videoInfoList = new ArrayList<VideoInfo>();
	    // Making HTTP request
        
       String content = "";
//       HttpResponse response;
//       HttpParams httpParameters = new BasicHttpParams();
//       HttpConnectionParams.setConnectionTimeout(httpParameters, 15000);
//       HttpConnectionParams.setSoTimeout(httpParameters, 15000+15000);
//       
//       HttpClient myClient = new DefaultHttpClient();
//       HttpPost myConnection = new HttpPost(cheduleUrl);
//       myConnection.setParams(httpParameters);
//       try {
//           response = myClient.execute(myConnection);
//           content = EntityUtils.toString(response.getEntity(), "UTF-8");
//            
//       } catch (ClientProtocolException e) {
//           e.printStackTrace();
//       } catch (IOException e) {
//           e.printStackTrace();
//       }
//	   
//       if(content == null){
//    	   return;
//       }
//		
		try {
			URL urlLoc = new URL(cheduleUrl);
			URLConnection connection = urlLoc.openConnection();
			connection.setConnectTimeout(10000);
			connection.setReadTimeout(15000);
			connection.connect();
			InputStream input = new BufferedInputStream(urlLoc.openStream());
			content = FileUtil.readFileToStringUsingStringWriter(input);
			if(content == null){
				return;
			}
		JSONObject jsonVideoInfo = new JSONObject(content);
//		Log.e(TAG, "jsonVideoInfo: " + jsonVideoInfo);
		channelIcon = Vp9ParamUtil.getJsonString(jsonVideoInfo,"channelIcon", "");
		
		channelUrl = Vp9ParamUtil.getJsonString(jsonVideoInfo,"channelUrl", "");
		
		setChannelId(Vp9ParamUtil.getJsonInt(jsonVideoInfo, "channelId", -1));
		
		channelType = Vp9ParamUtil.getJsonInt(jsonVideoInfo, "channelType", -1);
		
		channelName = Vp9ParamUtil.getJsonString(jsonVideoInfo, "channelName", "");
		
		defaultStart = Vp9ParamUtil.getJsonInt(jsonVideoInfo, "defaultStart", -1);
		
		JSONArray jsonContentArray = jsonVideoInfo.getJSONArray("content");
		
		if(jsonContentArray != null && jsonContentArray.length() > 0){
			
//			for(int i = 0; i < 5; i++){
			for(int i = 0; i < jsonContentArray.length(); i++){
				
				JSONObject jsonContent = jsonContentArray.getJSONObject(i);
				
				VideoInfo videoInfo = new VideoInfo();
				
				videoInfo.setIndex(i);
				
				videoInfo.setCurrentDay(true);
				
				videoInfo.setThumbIcon(Vp9ParamUtil.getJsonString(jsonContent, "thumbIcon", ""));
				
				videoInfo.setMovieID(Vp9ParamUtil.getJsonString(jsonContent, "movieID", ""));
				
				videoInfo.setGenre(Vp9ParamUtil.getJsonString(jsonContent, "genre", ""));
				
				videoInfo.setIntProgram(Vp9ParamUtil.getValue(Vp9ParamUtil.getJsonString(jsonContent, "intProgram", "-1"), -1));
				
				videoInfo.setProgramName(Vp9ParamUtil.getJsonString(jsonContent, "program_name", ""));
				
				videoInfo.setUserVideoId(Vp9ParamUtil.getJsonString(jsonContent, "user_video_id", ""));
											
				videoInfo.setStatus(Vp9ParamUtil.getValue(Vp9ParamUtil.getJsonString(jsonContent, "status", "-1"), -1));
				
				videoInfo.setRuntime(Vp9ParamUtil.getJsonString(jsonContent, "runtime", ""));
				
				videoInfo.setResolution(Vp9ParamUtil.getJsonString(jsonContent, "resolution", ""));
							
				videoInfo.setType(Vp9ParamUtil.getJsonString(jsonContent, "type", ""));
				
				videoInfo.setDirector(Vp9ParamUtil.getJsonString(jsonContent, "director", ""));
				
				videoInfo.setLikedNum(Vp9ParamUtil.getValue(Vp9ParamUtil.getJsonString(jsonContent, "likedNum", "-1"), -1));
														
				videoInfo.setUrl(Vp9ParamUtil.getJsonString(jsonContent, "src", ""));
				
				
//				ArrayList<VideoResolution> videoResolutions = getVideoResolutions(jsonContent);
				
//				videoInfo.setVideoResolutions(videoResolutions);
				
				ArrayList<VideoResolutionGroup> videoResolutionGroups = getVideoResolutionGroup(jsonContent);
				
				videoInfo.setVideoResolutionGroups(videoResolutionGroups);
				
				videoInfo.setVideoName(Vp9ParamUtil.getJsonString(jsonContent, "video_name", ""));

				videoInfo.setCast(Vp9ParamUtil.getJsonString(jsonContent, "cast", ""));
				
				videoInfo.setStartTime(Vp9ParamUtil.getJsonString(jsonContent, "start_time", ""));
				
				if(jsonContent.has("end_time")){
					videoInfo.setEndTime(Vp9ParamUtil.getJsonString(jsonContent, "end_time", "0"));
				}else{
					videoInfo.setEndTime("0");
				}
				
				
				String public_rate = Vp9ParamUtil.getJsonString(jsonContent, "public_rate", "0").trim().replace(",", ".");
				
				videoInfo.setPublicRate(Vp9ParamUtil.getValue(public_rate, 0f));			
				
				videoInfo.setInDay(Vp9ParamUtil.getValue(Vp9ParamUtil.getJsonString(jsonContent, "in_day", "-1"), -1));
				
				videoInfo.setYear(Vp9ParamUtil.getValue(Vp9ParamUtil.getJsonString(jsonContent, "year", "-1"), -1));
				
				videoInfo.setVideoId(Vp9ParamUtil.getValue(Vp9ParamUtil.getJsonString(jsonContent, "video_id", "-1"), -1));
				
				videoInfo.setSerial(Vp9ParamUtil.getValue(Vp9ParamUtil.getJsonString(jsonContent, "serial", "-1"), -1));
				
				boolean isExistNewTypeSub = false;
				
				if(jsonContent.has("subtitles")){
					
					JSONArray jsArrSubtitles = jsonContent.getJSONArray("subtitles");
					if(jsArrSubtitles != null){
						ArrayList<SubtitleInfo> subtitleInfos = new ArrayList<SubtitleInfo>();
						
						if(jsArrSubtitles != null && jsArrSubtitles.length() > 0){
							for(int k = 0; k < jsArrSubtitles.length(); k++){
								isExistNewTypeSub = true;
								JSONObject jsSubtitle = jsArrSubtitles.getJSONObject(k);
								if(jsSubtitle != null){
									String subUrl = ParamUtil.getJsonString(jsSubtitle, "url", "");
//									String subType = "en";
									String subTypeName = ParamUtil.getJsonString(jsSubtitle, "name", "");
									boolean isDisplay = false;
									int languageId = ParamUtil.getJsonInt(jsSubtitle, "language_id", 0);
									if(!"".equals(subUrl.trim()) && !"".equals(subTypeName.trim())){
										SubtitleInfo subInfo = new SubtitleInfo(subUrl, languageId, subTypeName, isDisplay);
										subtitleInfos.add(subInfo);
									}
								}
							}
							videoInfo.setSubtitleInfos(subtitleInfos);
						}
					}
					
				}else if(!isExistNewTypeSub && jsonContent.has("subtitle")){
					JSONArray jsonArrSubtitles = jsonContent.getJSONArray("subtitle");
					
					ArrayList<SubtitleInfo> subtitleInfos = new ArrayList<SubtitleInfo>();
					
					if(jsonArrSubtitles != null && jsonArrSubtitles.length() > 0){
						for(int k = 0; k < jsonArrSubtitles.length(); k++){
							String jsonSubtitle = (String) jsonArrSubtitles.get(k);
							if(jsonSubtitle != null && jsonSubtitle.trim().endsWith("eng.ass")){
								String subUrl = jsonSubtitle;
								String subType = "en";
								String subTypeName = "English";
								boolean isDisplay = false;
								SubtitleInfo subInfo = new SubtitleInfo(subUrl, subType, subTypeName, isDisplay);
								subtitleInfos.add(subInfo);
							}else if(jsonSubtitle != null && jsonSubtitle.trim().endsWith("vie.ass")){
								String subUrl = jsonSubtitle;
								String subType = "vn";
								String subTypeName = "Viet Nam";
								boolean isDisplay = false;
								SubtitleInfo subInfo = new SubtitleInfo(subUrl, subType, subTypeName, isDisplay);
								subtitleInfos.add(subInfo);
							}
						}
						videoInfo.setSubtitleInfos(subtitleInfos);
					}
				}
				
//				if(jsonContent.has("subtitle")){
//					JSONArray jsonArrSubtitles = jsonContent.getJSONArray("subtitle");
//					
//					ArrayList<SubtitleInfo> subtitleInfos = new ArrayList<SubtitleInfo>();
//					
//					if(jsonArrSubtitles != null && jsonArrSubtitles.length() > 0){
//						for(int k = 0; k < jsonArrSubtitles.length(); k++){
//							String jsonSubtitle = (String) jsonArrSubtitles.get(k);
//							if(jsonSubtitle != null && jsonSubtitle.trim().endsWith("eng.ass")){
//								String subUrl = jsonSubtitle;
//								String subType = "en";
//								String subTypeName = "English";
//								boolean isDisplay = false;
//								SubtitleInfo subInfo = new SubtitleInfo(subUrl, subType, subTypeName, isDisplay);
//								subtitleInfos.add(subInfo);
//							}else if(jsonSubtitle != null && jsonSubtitle.trim().endsWith("vie.ass")){
//								String subUrl = jsonSubtitle;
//								String subType = "vn";
//								String subTypeName = "Viet Nam";
//								boolean isDisplay = false;
//								SubtitleInfo subInfo = new SubtitleInfo(subUrl, subType, subTypeName, isDisplay);
//								subtitleInfos.add(subInfo);
//							}
////							else{
////								String subUrl = jsonSubtitle;
////								String subType = "Default";
////								String subTypeName = "Default";
////								boolean isDisplay = false;
////								SubtitleInfo subInfo = new SubtitleInfo(subUrl, subType, subTypeName, isDisplay);
////								subtitleInfos.add(subInfo);
////							}
//						}
//						videoInfo.setSubtitleInfos(subtitleInfos);
//					}
//				}
				
				if(jsonContent.has("content")){
					JSONArray jsonArrContent = jsonContent.getJSONArray("content");
					ArrayList<VideoInfo> childVideoInfoInfos = new ArrayList<VideoInfo>();
					if(jsonArrContent != null && jsonArrContent.length() > 0){
						for(int k = 0; k < jsonArrContent.length(); k++){
							JSONObject jsonChildContent = jsonArrContent.getJSONObject(k);
							if(jsonChildContent != null && jsonChildContent.has("0")){
								JSONObject jsonChild = jsonChildContent.getJSONObject("0");
								if(jsonChild != null){
									VideoInfo childVideoInfo = getVideoInfo(jsonChild);
									childVideoInfo.setIndex(k);
									childVideoInfoInfos.add(childVideoInfo);
								}
							}
						}
						videoInfo.setChildVideoInfoList(childVideoInfoInfos);
					}
				}
				videoInfoList.add(videoInfo);
				
			}
		}
		
	} catch (Exception e) {
		e.printStackTrace();
	}
		
	}

	private VideoInfo getVideoInfo(JSONObject jsonContent) throws JSONException {
		
		VideoInfo videoInfo = new VideoInfo();
		
		videoInfo.setThumbIcon(Vp9ParamUtil.getJsonString(jsonContent, "thumbIcon", ""));
		
		videoInfo.setMovieID(Vp9ParamUtil.getJsonString(jsonContent, "movieID", ""));
		
		videoInfo.setGenre(Vp9ParamUtil.getJsonString(jsonContent, "genre", ""));
		
		videoInfo.setIntProgram(Vp9ParamUtil.getValue(Vp9ParamUtil.getJsonString(jsonContent, "intProgram", "-1"), -1));
		
		videoInfo.setProgramName(Vp9ParamUtil.getJsonString(jsonContent, "program_name", ""));
		
		videoInfo.setUserVideoId(Vp9ParamUtil.getJsonString(jsonContent, "user_video_id", ""));
									
		videoInfo.setStatus(Vp9ParamUtil.getValue(Vp9ParamUtil.getJsonString(jsonContent, "status", "-1"), -1));
		
		videoInfo.setRuntime(Vp9ParamUtil.getJsonString(jsonContent, "runtime", ""));
		
		videoInfo.setResolution(Vp9ParamUtil.getJsonString(jsonContent, "resolution", ""));
					
		videoInfo.setType(Vp9ParamUtil.getJsonString(jsonContent, "type", ""));
		
		videoInfo.setDirector(Vp9ParamUtil.getJsonString(jsonContent, "director", ""));
		
		videoInfo.setLikedNum(Vp9ParamUtil.getValue(Vp9ParamUtil.getJsonString(jsonContent, "likedNum", "-1"), -1));
												
		videoInfo.setUrl(Vp9ParamUtil.getJsonString(jsonContent, "src", ""));
			
//		ArrayList<VideoResolution> videoResolutions = getVideoResolutions(jsonContent);
//		
//		videoInfo.setVideoResolutions(videoResolutions);
		
		ArrayList<VideoResolutionGroup> videoResolutionGroups = getVideoResolutionGroup(jsonContent);
		
		videoInfo.setVideoResolutionGroups(videoResolutionGroups);
		
		videoInfo.setVideoName(Vp9ParamUtil.getJsonString(jsonContent, "name", ""));

		videoInfo.setCast(Vp9ParamUtil.getJsonString(jsonContent, "cast", ""));
		
		videoInfo.setStartTime(Vp9ParamUtil.getJsonString(jsonContent, "start_time", ""));
		
		if(jsonContent.has("end_time")){
			videoInfo.setEndTime(Vp9ParamUtil.getJsonString(jsonContent, "end_time", "0"));
		}else{
			videoInfo.setEndTime("0");
		}
		
		String videoSize = Vp9ParamUtil.getJsonString(jsonContent, "video_size", "");
		
		String[] elemSizes = videoSize.trim().split("x");
		
		int width = 0, height = 0;
		
		if(elemSizes != null && elemSizes.length == 2){
			width = Vp9ParamUtil.getValue(elemSizes[0], 0);
			height = Vp9ParamUtil.getValue(elemSizes[1], 0);
		}
		
		videoInfo.setWidth(width);
		
		videoInfo.setHeight(height);
		
		String public_rate = Vp9ParamUtil.getJsonString(jsonContent, "public_rate", "0").trim().replace(",", ".");
		
		videoInfo.setPublicRate(Vp9ParamUtil.getValue(public_rate, 0f));			
		
		videoInfo.setInDay(Vp9ParamUtil.getValue(Vp9ParamUtil.getJsonString(jsonContent, "in_day", "-1"), -1));
		
		videoInfo.setYear(Vp9ParamUtil.getValue(Vp9ParamUtil.getJsonString(jsonContent, "year", "-1"), -1));
		
		videoInfo.setVideoId(Vp9ParamUtil.getValue(Vp9ParamUtil.getJsonString(jsonContent, "video_id", "-1"), -1));
		
		videoInfo.setSerial(Vp9ParamUtil.getValue(Vp9ParamUtil.getJsonString(jsonContent, "serial", "-1"), -1));
		
//		if(jsonContent.has("subtitle")){
//			
//			JSONArray jsonArrSubtitles = jsonContent.getJSONArray("subtitle");
//			
//			ArrayList<SubtitleInfo> subtitleInfos = new ArrayList<SubtitleInfo>();
			
//			if(jsonArrSubtitles != null && jsonArrSubtitles.length() > 0){
//				for(int k = 0; k < jsonArrSubtitles.length(); k++){
//					String jsonSubtitle = (String) jsonArrSubtitles.get(k);
//					if(jsonSubtitle != null && jsonSubtitle.trim().endsWith("eng.ass")){
//						String subUrl = jsonSubtitle;
//						String subType = "en";
//						String subTypeName = "English";
//						boolean isDisplay = false;
//						SubtitleInfo subInfo = new SubtitleInfo(subUrl, subType, subTypeName, isDisplay);
//						subtitleInfos.add(subInfo);
//					}else if(jsonSubtitle != null && jsonSubtitle.trim().endsWith("vie.ass")){
//						String subUrl = jsonSubtitle;
//						String subType = "vn";
//						String subTypeName = "Viet Nam";
//						boolean isDisplay = false;
//						SubtitleInfo subInfo = new SubtitleInfo(subUrl, subType, subTypeName, isDisplay);
//						subtitleInfos.add(subInfo);
//					}
////					else{
////						String subUrl = jsonSubtitle;
////						String subType = "Default";
////						String subTypeName = "Default";
////						boolean isDisplay = false;
////						SubtitleInfo subInfo = new SubtitleInfo(subUrl, subType, subTypeName, isDisplay);
////						subtitleInfos.add(subInfo);
////					}
//				}
//				videoInfo.setSubtitleInfos(subtitleInfos);
//			}
//		}
		    boolean isExistNewTypeSub = false;
			if(jsonContent.has("subtitles")){
				
				JSONArray jsArrSubtitles = jsonContent.getJSONArray("subtitles");
				if(jsArrSubtitles != null){
					ArrayList<SubtitleInfo> subtitleInfos = new ArrayList<SubtitleInfo>();
					
					if(jsArrSubtitles != null && jsArrSubtitles.length() > 0){
						for(int k = 0; k < jsArrSubtitles.length(); k++){
							isExistNewTypeSub = true;
							JSONObject jsSubtitle = jsArrSubtitles.getJSONObject(k);
							if(jsSubtitle != null){
								String subUrl = ParamUtil.getJsonString(jsSubtitle, "url", "");
//								String subType = "en";
								String subTypeName = ParamUtil.getJsonString(jsSubtitle, "name", "");
								boolean isDisplay = false;
								int languageId = ParamUtil.getJsonInt(jsSubtitle, "language_id", 0);
								if(!"".equals(subUrl.trim()) && !"".equals(subTypeName.trim())){
									SubtitleInfo subInfo = new SubtitleInfo(subUrl, languageId, subTypeName, isDisplay);
									subtitleInfos.add(subInfo);
								}
							}
						}
						videoInfo.setSubtitleInfos(subtitleInfos);
					}
				}
				
			}else if(!isExistNewTypeSub && jsonContent.has("subtitle")){
				JSONArray jsonArrSubtitles = jsonContent.getJSONArray("subtitle");
				
				ArrayList<SubtitleInfo> subtitleInfos = new ArrayList<SubtitleInfo>();
				
				if(jsonArrSubtitles != null && jsonArrSubtitles.length() > 0){
					for(int k = 0; k < jsonArrSubtitles.length(); k++){
						String jsonSubtitle = (String) jsonArrSubtitles.get(k);
						if(jsonSubtitle != null && jsonSubtitle.trim().endsWith("eng.ass")){
							String subUrl = jsonSubtitle;
							String subType = "en";
							String subTypeName = "English";
							boolean isDisplay = false;
							SubtitleInfo subInfo = new SubtitleInfo(subUrl, subType, subTypeName, isDisplay);
							subtitleInfos.add(subInfo);
						}else if(jsonSubtitle != null && jsonSubtitle.trim().endsWith("vie.ass")){
							String subUrl = jsonSubtitle;
							String subType = "vn";
							String subTypeName = "Viet Nam";
							boolean isDisplay = false;
							SubtitleInfo subInfo = new SubtitleInfo(subUrl, subType, subTypeName, isDisplay);
							subtitleInfos.add(subInfo);
						}
					}
					videoInfo.setSubtitleInfos(subtitleInfos);
				}
			}

		String left_displays_url = Vp9ParamUtil.getJsonString(jsonContent, "left_sub", "");
//		updateLeftDisplays(videoInfo, left_displays_url);
		videoInfo.setLeftDisplaysUrl(left_displays_url);
		String right_displays_url = Vp9ParamUtil.getJsonString(jsonContent, "right_sub", "");
//		updateRightDisplays(videoInfo, right_displays_url);
		videoInfo.setRightDisplaysUrl(right_displays_url);
		return videoInfo;
		
		
	}

	public void parserCheduleUrl1(final String cheduleUrl) {
//		Log.e(TAG, "cheduleUrl: " + cheduleUrl);
		currentIndex = -1;
		videoInfoList = new ArrayList<VideoInfo>();
	    // Making HTTP request
        
       String content = "";
//       HttpResponse response;
//       HttpClient myClient = new DefaultHttpClient();
//       HttpPost myConnection = new HttpPost(cheduleUrl);
//       try {
//           response = myClient.execute(myConnection);
//           content = EntityUtils.toString(response.getEntity(), "UTF-8");
//            
//       } catch (ClientProtocolException e) {
//           e.printStackTrace();
//       } catch (IOException e) {
//           e.printStackTrace();
//       }
//	   
//       if(content == null){
//    	   return;
//       }
//       Log.e(TAG, "parserCheduleUrl1a: ");
		try {
		
			URL urlLoc = new URL(cheduleUrl);
			URLConnection connection = urlLoc.openConnection();
			connection.setConnectTimeout(10000);
			connection.setReadTimeout(15000);
			connection.connect();
			InputStream input = new BufferedInputStream(urlLoc.openStream());
			
			content = FileUtil.readFileToStringUsingStringWriter(input);
			if(content == null){
				return;
			}
		JSONObject jsonVideoInfo = new JSONObject(content);
//		Log.e(TAG, "parserCheduleUrl1b: ");
//		Log.e(TAG, "jsonVideoInfo: " + jsonVideoInfo);
		
		channelIcon = Vp9ParamUtil.getJsonString(jsonVideoInfo,"channelIcon", "");
		
		channelUrl = Vp9ParamUtil.getJsonString(jsonVideoInfo,"channelUrl", "");
		
		setChannelId(Vp9ParamUtil.getJsonInt(jsonVideoInfo, "channelId", -1));
		
		channelType = Vp9ParamUtil.getJsonInt(jsonVideoInfo, "channelType", -1);
		
		channelName = Vp9ParamUtil.getJsonString(jsonVideoInfo, "channelName", "");
		
		defaultStart = Vp9ParamUtil.getJsonInt(jsonVideoInfo, "defaultStart", -1);
		
		JSONArray jsonContentArray = jsonVideoInfo.getJSONArray("content");
		Log.e(TAG, "parserCheduleUrl1c: " + jsonVideoInfo.toString());
		
		if(jsonContentArray != null && jsonContentArray.length() > 0){
//			Log.e(TAG, "parserCheduleUrl1c: " + jsonContentArray.length());
			Log.e(TAG, "parserCheduleUrl1c: " + jsonContentArray);
			int len = jsonContentArray.length();
//			len = 1;
			for(int i = 0; i < len; i++){
//				Log.e(TAG, "parserCheduleUrl1 - " +i);
				JSONObject jsonContent = jsonContentArray.getJSONObject(i);
				
				VideoInfo videoInfo = new VideoInfo();
				
				videoInfo.setIndex(i);
				videoInfo.setCurrentDay(true);
				
				videoInfo.setThumbIcon(Vp9ParamUtil.getJsonString(jsonContent, "thumbIcon", ""));
				
				videoInfo.setMovieID(Vp9ParamUtil.getJsonString(jsonContent, "movieID", ""));
				
				videoInfo.setGenre(Vp9ParamUtil.getJsonString(jsonContent, "genre", ""));
				
				videoInfo.setIntProgram(Vp9ParamUtil.getValue(Vp9ParamUtil.getJsonString(jsonContent, "intProgram", "-1"), -1));
				
				videoInfo.setProgramName(Vp9ParamUtil.getJsonString(jsonContent, "program_name", ""));
				
				videoInfo.setUserVideoId(Vp9ParamUtil.getJsonString(jsonContent, "user_video_id", ""));
											
				videoInfo.setStatus(Vp9ParamUtil.getValue(Vp9ParamUtil.getJsonString(jsonContent, "status", "-1"), -1));
				
				videoInfo.setRuntime(Vp9ParamUtil.getJsonString(jsonContent, "runtime", ""));
				
				videoInfo.setResolution(Vp9ParamUtil.getJsonString(jsonContent, "resolution", ""));
							
				videoInfo.setType(Vp9ParamUtil.getJsonString(jsonContent, "type", ""));
				
				videoInfo.setDirector(Vp9ParamUtil.getJsonString(jsonContent, "director", ""));
				
				videoInfo.setLikedNum(Vp9ParamUtil.getValue(Vp9ParamUtil.getJsonString(jsonContent, "likedNum", "-1"), -1));
														
				videoInfo.setUrl(Vp9ParamUtil.getJsonString(jsonContent, "url", ""));
				
//				ArrayList<VideoResolution> videoResolutions = getVideoResolutions(jsonContent);
//				
//				videoInfo.setVideoResolutions(videoResolutions);
				
				ArrayList<VideoResolutionGroup> videoResolutionGroups = getVideoResolutionGroup(jsonContent);
				
				videoInfo.setVideoResolutionGroups(videoResolutionGroups);
				
				videoInfo.setVideoName(Vp9ParamUtil.getJsonString(jsonContent, "video_name", ""));

				videoInfo.setCast(Vp9ParamUtil.getJsonString(jsonContent, "cast", ""));
				
				videoInfo.setStartTime(Vp9ParamUtil.getJsonString(jsonContent, "start_time", ""));
				
				String videoSize = Vp9ParamUtil.getJsonString(jsonContent, "video_size", "");
				
				String[] elemSizes = videoSize.trim().split("x");
				
				int width = 0, height = 0;
//				Log.e(TAG, "parserCheduleUrl1 - " +i + " -a");
				if(elemSizes != null && elemSizes.length == 2){
					width = Vp9ParamUtil.getValue(elemSizes[0], 0);
					height = Vp9ParamUtil.getValue(elemSizes[1], 0);
				}
//				Log.e(TAG, "parserCheduleUrl1 - " +i + " -b");
				videoInfo.setWidth(width);
				
				videoInfo.setHeight(height);
				
				
				if(jsonContent.has("end_time")){
					videoInfo.setEndTime(Vp9ParamUtil.getJsonString(jsonContent, "end_time", "0"));
				}else{
					videoInfo.setEndTime("0");
				}
				
				String public_rate = Vp9ParamUtil.getJsonString(jsonContent, "public_rate", "0").trim().replace(",", ".");
				
				videoInfo.setPublicRate(Vp9ParamUtil.getValue(public_rate, 0f));			
				
				videoInfo.setInDay(Vp9ParamUtil.getValue(Vp9ParamUtil.getJsonString(jsonContent, "in_day", "-1"), -1));
				
				videoInfo.setYear(Vp9ParamUtil.getValue(Vp9ParamUtil.getJsonString(jsonContent, "year", "-1"), -1));
				
				videoInfo.setVideoId(Vp9ParamUtil.getValue(Vp9ParamUtil.getJsonString(jsonContent, "video_id", "-1"), -1));
				
				videoInfo.setSerial(Vp9ParamUtil.getValue(Vp9ParamUtil.getJsonString(jsonContent, "serial", "-1"), -1));
//				Log.e(TAG, "parserCheduleUrl1 - " +i + " -c");
				
				Log.d(TAG, "jsonContent-subtitle: " + jsonContent);
				boolean isExistNewTypeSub = false;
				if(jsonContent.has("subtitles")){
					
					JSONArray jsArrSubtitles = jsonContent.getJSONArray("subtitles");
					if(jsArrSubtitles != null){
						ArrayList<SubtitleInfo> subtitleInfos = new ArrayList<SubtitleInfo>();
						
						if(jsArrSubtitles != null && jsArrSubtitles.length() > 0){
							isExistNewTypeSub = true;
							for(int k = 0; k < jsArrSubtitles.length(); k++){
								JSONObject jsSubtitle = jsArrSubtitles.getJSONObject(k);
								if(jsSubtitle != null){
									String subUrl = ParamUtil.getJsonString(jsSubtitle, "url", "");
//									String subType = "en";
									String subTypeName = ParamUtil.getJsonString(jsSubtitle, "name", "");
									boolean isDisplay = false;
									int languageId = ParamUtil.getJsonInt(jsSubtitle, "language_id", 0);
									if(!"".equals(subUrl.trim()) && !"".equals(subTypeName.trim())){
										SubtitleInfo subInfo = new SubtitleInfo(subUrl, languageId, subTypeName, isDisplay);
										subtitleInfos.add(subInfo);
									}
								}
							}
							videoInfo.setSubtitleInfos(subtitleInfos);
						}
					}
					
				}else if(!isExistNewTypeSub && jsonContent.has("subtitle")){
					Log.d(TAG, "subtitle");
					JSONArray jsonArrSubtitles = jsonContent.getJSONArray("subtitle");
					
					ArrayList<SubtitleInfo> subtitleInfos = new ArrayList<SubtitleInfo>();
					
					if(jsonArrSubtitles != null && jsonArrSubtitles.length() > 0){
						Log.d(TAG, "subtitle 1: " + jsonArrSubtitles.toString());
						Log.d(TAG, "subtitle: " + jsonArrSubtitles.length());
						for(int k = 0; k < jsonArrSubtitles.length(); k++){
							String jsonSubtitle = (String) jsonArrSubtitles.get(k);
							Log.d(TAG, "subtitle 2: " + jsonSubtitle);
							if(jsonSubtitle != null && jsonSubtitle.trim().endsWith("eng.ass")){
								Log.d(TAG, "subtitle 3: ");
								String subUrl = jsonSubtitle;
								String subType = "en";
								String subTypeName = "English";
								boolean isDisplay = false;
								SubtitleInfo subInfo = new SubtitleInfo(subUrl, subType, subTypeName, isDisplay);
								subtitleInfos.add(subInfo);
							}else if(jsonSubtitle != null && jsonSubtitle.trim().endsWith("vie.ass")){
								Log.d(TAG, "subtitle 4: " + jsonSubtitle);
								String subUrl = jsonSubtitle;
								String subType = "vn";
								String subTypeName = "Viet Nam";
								boolean isDisplay = false;
								SubtitleInfo subInfo = new SubtitleInfo(subUrl, subType, subTypeName, isDisplay);
								subtitleInfos.add(subInfo);
							}
						}
						videoInfo.setSubtitleInfos(subtitleInfos);
					}
				}
//				if(jsonContent.has("subtitle")){
//					
//					JSONArray jsonArrSubtitles = jsonContent.getJSONArray("subtitle");
//					
//					ArrayList<SubtitleInfo> subtitleInfos = new ArrayList<SubtitleInfo>();
//					
//					if(jsonArrSubtitles != null && jsonArrSubtitles.length() > 0){
//						for(int k = 0; k < jsonArrSubtitles.length(); k++){
//							String jsonSubtitle = (String) jsonArrSubtitles.get(k);
//							if(jsonSubtitle != null && jsonSubtitle.trim().endsWith("eng.ass")){
//								String subUrl = jsonSubtitle;
//								String subType = "en";
//								String subTypeName = "English";
//								boolean isDisplay = false;
//								SubtitleInfo subInfo = new SubtitleInfo(subUrl, subType, subTypeName, isDisplay);
//								subtitleInfos.add(subInfo);
//							}else if(jsonSubtitle != null && jsonSubtitle.trim().endsWith("vie.ass")){
//								String subUrl = jsonSubtitle;
//								String subType = "vn";
//								String subTypeName = "Viet Nam";
//								boolean isDisplay = false;
//								SubtitleInfo subInfo = new SubtitleInfo(subUrl, subType, subTypeName, isDisplay);
//								subtitleInfos.add(subInfo);
//							}
////							else{
////								String subUrl = jsonSubtitle;
////								String subType = "Default";
////								String subTypeName = "Default";
////								boolean isDisplay = false;
////								SubtitleInfo subInfo = new SubtitleInfo(subUrl, subType, subTypeName, isDisplay);
////								subtitleInfos.add(subInfo);
////							}
//						}
//						videoInfo.setSubtitleInfos(subtitleInfos);
//					}
//				}
//				Log.e(TAG, "parserCheduleUrl1 - " +i + " -d");
				String left_displays_url = Vp9ParamUtil.getJsonString(jsonContent, "left_sub", "");
//				updateLeftDisplays(videoInfo, left_displays_url);
				String right_displays_url = Vp9ParamUtil.getJsonString(jsonContent, "right_sub", "");
//				updateRightDisplays(videoInfo, right_displays_url);
				videoInfo.setLeftDisplaysUrl(left_displays_url);
//				updateRightDisplays(videoInfo, right_displays_url);
				videoInfo.setRightDisplaysUrl(right_displays_url);
				
//				Log.e(TAG, "parserCheduleUrl1 - " +i + " -e");
				videoInfoList.add(videoInfo);
//				Log.e(TAG, "parserCheduleUrl1 - " +i + " -f");
			}
		}
//		Log.e(TAG, "parserCheduleUrl1d");
	} catch (Exception e) {
		e.printStackTrace();
	}
		
//		Thread threadParserCheduleUrl = new Thread(new Runnable() {
//			@Override
//			public void run() {
//				
//				try {
//					
//					URL url = new URL(cheduleUrl);
//					DataInputStream dis = new DataInputStream(new BufferedInputStream(url.openStream()));
//					int size = dis.available();
//					byte[] buffer = new byte[size];
//					dis.read(buffer);
//					String content = new String(buffer);
//					
//					JSONObject jsonVideoInfo = new JSONObject(content);
//					Log.e("Bigsky", "jsonVideoInfo: " + jsonVideoInfo);
//					channelIcon = ParamUtil.getValue(jsonVideoInfo.getString("channelIcon"), "");
//					
//					channelUrl = ParamUtil.getValue(jsonVideoInfo.getString("channelUrl"), "");
//					
//					channelType = ParamUtil.getValue(jsonVideoInfo.getInt("channelId"), -1);
//					
//					channelName = ParamUtil.getValue(jsonVideoInfo.getString("channelName"), "");
//					
//					defaultStart = ParamUtil.getValue(jsonVideoInfo.getInt("channelId"), -1);
//					
//					JSONArray jsonContentArray = jsonVideoInfo.getJSONArray("content");
//					
//					if(jsonContentArray != null && jsonContentArray.length() > 0){
//						
//						for(int i = 0; i < jsonContentArray.length(); i++){
//							
//							JSONObject jsonContent = jsonContentArray.getJSONObject(i);
//							
//							VideoInfo videoInfo = new VideoInfo();
//							
//							videoInfo.setIndex(i);
//							
//							videoInfo.setThumbIcon(ParamUtil.getValue(jsonContent.getString("thumbIcon"), ""));
//							
//							videoInfo.setMovieID(ParamUtil.getValue(jsonContent.getString("movieID"), ""));
//							
//							videoInfo.setGenre(ParamUtil.getValue(jsonContent.getString("genre"), ""));
//							
//							videoInfo.setIntProgram(ParamUtil.getValue(jsonContent.getString("intProgram"), -1));
//							
//							videoInfo.setUserVideoId(ParamUtil.getValue(jsonContent.getString("user_video_id"), ""));
//														
//							videoInfo.setStatus(ParamUtil.getValue(jsonContent.getString("status"), -1));
//							
//							videoInfo.setRuntime(ParamUtil.getValue(jsonContent.getString("runtime"), ""));
//							
//							videoInfo.setResolution(ParamUtil.getValue(jsonContent.getString("resolution"), ""));
//										
//							videoInfo.setType(ParamUtil.getValue(jsonContent.getString("type"), ""));
//							
//							videoInfo.setDirector(ParamUtil.getValue(jsonContent.getString("director"), ""));
//							
//							videoInfo.setLikedNum(ParamUtil.getValue(jsonContent.getString("likedNum"), -1));
//																	
//							videoInfo.setUrl(ParamUtil.getValue(jsonContent.getString("url"), ""));
//							
//							videoInfo.setVideoName(ParamUtil.getValue(jsonContent.getString("video_name"), ""));
//
//							videoInfo.setCast(ParamUtil.getValue(jsonContent.getString("cast"), ""));
//							
//							videoInfo.setStartTime(ParamUtil.getValue(jsonContent.getString("start_time"), ""));
//							
//							videoInfo.setEndTime(ParamUtil.getValue(jsonContent.getString("end_time"), ""));
//							String public_rate = ParamUtil.getValue(jsonContent.getString("public_rate"), "0").trim().replace(",", ".");
//							
//							videoInfo.setPublicRate(ParamUtil.getValue(public_rate, 0f));			
//							
//							videoInfo.setInDay(ParamUtil.getValue(jsonContent.getString("in_day"), -1));
//							
//							videoInfo.setYear(ParamUtil.getValue(jsonContent.getString("year"), -1));
//							
//							videoInfo.setVideoId(ParamUtil.getValue(jsonContent.getString("video_id"), -1));
//							
//							videoInfo.setSerial(ParamUtil.getValue(jsonContent.getString("serial"), -1));
//							
//							JSONArray jsonArrSubtitles = jsonContent.getJSONArray("subtitle");
//							
//							ArrayList<SubtitleInfo> subtitleInfos = new ArrayList<SubtitleInfo>();
//							
//							if(jsonArrSubtitles != null && jsonArrSubtitles.length() > 0){
//								for(int k = 0; k < jsonArrSubtitles.length(); k++){
//									String jsonSubtitle = (String) jsonArrSubtitles.get(k);
//									if(jsonSubtitle != null && jsonSubtitle.trim().endsWith("eng.ass")){
//										String subUrl = jsonSubtitle;
//										String subType = "en";
//										String subTypeName = "English";
//										boolean isDisplay = false;
//										SubtitleInfo subInfo = new SubtitleInfo(subUrl, subType, subTypeName, isDisplay);
//										subtitleInfos.add(subInfo);
//									}else if(jsonSubtitle != null && jsonSubtitle.trim().endsWith("vie.ass")){
//										String subUrl = jsonSubtitle;
//										String subType = "vn";
//										String subTypeName = "Viet Nam";
//										boolean isDisplay = false;
//										SubtitleInfo subInfo = new SubtitleInfo(subUrl, subType, subTypeName, isDisplay);
//										subtitleInfos.add(subInfo);
//									}else{
//										String subUrl = jsonSubtitle;
//										String subType = "Default";
//										String subTypeName = "Default";
//										boolean isDisplay = false;
//										SubtitleInfo subInfo = new SubtitleInfo(subUrl, subType, subTypeName, isDisplay);
//										subtitleInfos.add(subInfo);
//									}
//								}	
//							}
//							
//							videoInfoList.add(videoInfo);
//							
//						}
//					}
//					
//				} catch (JSONException e) {
//					e.printStackTrace();
//				} catch (MalformedURLException e) {
//					e.printStackTrace();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		});
		
//		threadParserCheduleUrl.start();
}

	public void updateRightDisplays(VideoInfo videoInfo) {
		   if(videoInfo == null){
			   return;
		   }
		   String right_displays_url = videoInfo.getRightDisplaysUrl();
		   if(right_displays_url == null || "".equals(right_displays_url.trim())){
			   return;
		   }
	       String content = "";
//	       HttpResponse response = null;
//	       HttpClient myClient = new DefaultHttpClient();
//	       HttpPost myConnection = new HttpPost(right_displays_url);
//	       try {
//	           response = myClient.execute(myConnection);
//	           content = EntityUtils.toString(response.getEntity(), "UTF-8");
//	            
//	       } catch (ClientProtocolException e) {
//	           e.printStackTrace();
//	       }catch (Exception e) {
//	           e.printStackTrace();
//	       }
//	       if(content == null){
//	    	   return;
//	       }
			try {
				URL urlLoc = new URL(right_displays_url);
				URLConnection connection = urlLoc.openConnection();
				connection.setConnectTimeout(10000);
				connection.setReadTimeout(15000);
				connection.connect();
				InputStream input = new BufferedInputStream(urlLoc.openStream());
				content = FileUtil.readFileToStringUsingStringWriter(input);
				if(content == null){
					return;
				}
				JSONObject jsonObj = new JSONObject(content);
				JSONArray jsonDisplays = null;
				
				if(jsonObj != null){
					jsonDisplays = Vp9ParamUtil.getJSONArray(jsonObj, "logo");
				}
				if(jsonDisplays != null){
					ArrayList<LeftDisplayInfo> displays = new ArrayList<LeftDisplayInfo>();
					
					for(int i = 0; i < jsonDisplays.length(); i++){
						JSONObject jsonLeftDisplay = jsonDisplays.getJSONObject(i);
						if(jsonLeftDisplay != null){
							LeftDisplayInfo displayInfo = new LeftDisplayInfo();
							
							int start = Vp9ParamUtil.getJsonInt(jsonLeftDisplay, "start", 0);
							
							int end = Vp9ParamUtil.getJsonInt(jsonLeftDisplay, "end", 0);
							
							String html = "";
							
							String iconUrl = "";
							
							int width = 0;
							
							int height = 0;
							
							JSONArray elems = Vp9ParamUtil.getJSONArray(jsonLeftDisplay, "content");
							int intType = -1;
							if(elems != null){
								for(int k = 0; k < elems.length(); k++){
									
									JSONObject elem = elems.getJSONObject(k);
									if(elem != null && elem.has("type")){
										String type = Vp9ParamUtil.getJsonString(elem, "type", "");
										if("html".equals(type.toLowerCase())){
											html = Vp9ParamUtil.getJsonString(elem, "html", "");
											html = html.replaceAll("\\{channel_name\\}", this.mControl.channelName);
											html = html.replaceAll("\\{program_name\\}", videoInfo.getVideoName());
											if(intType == 1 || intType == 2){
												intType = 2;
											}else{
												intType = 0;
											}
										}
										
										else if("icon".equals(type.toLowerCase())){
											if(intType == 0 || intType == 2){
												intType = 2;
											}else{
												intType = 1;
											}
											iconUrl = Vp9ParamUtil.getJsonString(elem, "src", "");
											iconUrl = iconUrl.replaceAll("\\{channel_logo\\}", this.mControl.channelIcon);
											iconUrl = iconUrl.replaceAll("\\{vp9_logo\\}", this.mControl.vp9Logo);
											iconUrl = iconUrl.replaceAll("\\{program_logo\\}", videoInfo.getThumbIcon());
											width = Vp9ParamUtil.getJsonInt(elem, "width", 0);
											height = Vp9ParamUtil.getJsonInt(elem, "height", 0);
											
										}
									}
								}
							}
							
							if(intType >= 0){
								displayInfo.setStart(start);
								displayInfo.setHeight(height);
								displayInfo.setHtml(html);
								displayInfo.setWidth(width);
								displayInfo.setEnd(end);
								displayInfo.setIconUrl(iconUrl);
								displayInfo.setRightType(intType);
								
								displays.add(displayInfo);
							}
						}
					}
					videoInfo.setRightDisplays(displays);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	public void updateLeftDisplays(VideoInfo videoInfo) {
		   if(videoInfo == null){
			   return;
		   }
		   String left_displays_url = videoInfo.getLeftDisplaysUrl();
		   if(left_displays_url == null || "".equals(left_displays_url.trim())){
			   return;
		   }
	       String content = "";
//	       HttpResponse response = null;
//	       HttpClient myClient = new DefaultHttpClient();
//	       HttpPost myConnection = new HttpPost(left_displays_url);
//	       try {
//	           response = myClient.execute(myConnection);
//	           content = EntityUtils.toString(response.getEntity(), "UTF-8");
//	            
//	       } catch (ClientProtocolException e) {
//	           e.printStackTrace();
//	       } catch (Exception e) {
//	           e.printStackTrace();
//	       }
//	       if(content == null){
//	    	   return;
//	       }
			try {
				URL urlLoc = new URL(left_displays_url);
				URLConnection connection = urlLoc.openConnection();
				connection.setConnectTimeout(10000);
				connection.setReadTimeout(15000);
				connection.connect();
				InputStream input = new BufferedInputStream(urlLoc.openStream());
				content = FileUtil.readFileToStringUsingStringWriter(input);
				if(content == null){
					return;
				}
				JSONObject jsonObj = new JSONObject(content);
				JSONArray jsonLeftDisplays = null;
				
				if(jsonObj != null){
					jsonLeftDisplays = Vp9ParamUtil.getJSONArray(jsonObj, "logo");
				}
				if(jsonLeftDisplays != null){
					ArrayList<LeftDisplayInfo> leftDisplays = new ArrayList<LeftDisplayInfo>();
					
					for(int i = 0; i < jsonLeftDisplays.length(); i++){
						JSONObject jsonLeftDisplay = jsonLeftDisplays.getJSONObject(i);
						if(jsonLeftDisplay != null){
							LeftDisplayInfo leftDisplayInfo = new LeftDisplayInfo();
							
							int start = Vp9ParamUtil.getJsonInt(jsonLeftDisplay, "start", 0);
							
							int end = Vp9ParamUtil.getJsonInt(jsonLeftDisplay, "end", 0);
							
							String html = "";
							
							String iconUrl = "";
							
							int width = 0;
							
							int height = 0;
							
							JSONArray elems = Vp9ParamUtil.getJSONArray(jsonLeftDisplay, "content");
							int intType = -1;
							if(elems != null){
								for(int k = 0; k < elems.length(); k++){
									
									JSONObject elem = elems.getJSONObject(k);
									if(elem != null && elem.has("type")){
										String type = Vp9ParamUtil.getJsonString(elem, "type", "");
										if("html".equals(type.toLowerCase())){
											html = Vp9ParamUtil.getJsonString(elem, "html", "");
											html = html.replaceAll("\\{channel_name\\}", this.mControl.channelName);
											html = html.replaceAll("\\{program_name\\}", videoInfo.getVideoName());
											if(intType == 1 || intType == 2){
												intType = 2;
											}else{
												intType = 0;
											}
										}
										
										else if("icon".equals(type.toLowerCase())){
											if(intType == 0 || intType == 2){
												intType = 2;
											}else{
												intType = 1;
											}
											iconUrl = Vp9ParamUtil.getJsonString(elem, "src", "");
											iconUrl = iconUrl.replaceAll("\\{channel_logo\\}", this.mControl.channelIcon);
											iconUrl = iconUrl.replaceAll("\\{vp9_logo\\}", this.mControl.vp9Logo);
											iconUrl = iconUrl.replaceAll("\\{program_logo\\}", videoInfo.getThumbIcon());
											width = Vp9ParamUtil.getJsonInt(elem, "width", 0);
											height = Vp9ParamUtil.getJsonInt(elem, "height", 0);
										}
									}
								}
							}
							
							if(intType >= 0){
								leftDisplayInfo.setStart(start);
								leftDisplayInfo.setHeight(height);
								leftDisplayInfo.setHtml(html);
								leftDisplayInfo.setWidth(width);
								leftDisplayInfo.setEnd(end);
								leftDisplayInfo.setIconUrl(iconUrl);
								leftDisplayInfo.setLeftType(intType);
								
								leftDisplays.add(leftDisplayInfo);
							}
						}
					}
					videoInfo.setLeftDisplays(leftDisplays);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	public String getChannelIcon() {
		return channelIcon;
	}

	public void setChannelIcon(String channelIcon) {
		this.channelIcon = channelIcon;
	}

	public String getChannelUrl() {
		return channelUrl;
	}

	public void setChannelUrl(String channelUrl) {
		this.channelUrl = channelUrl;
	}

	public int getChannelType() {
		return channelType;
	}

	public void setChannelType(int channelType) {
		this.channelType = channelType;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public int getDefaultStart() {
		return defaultStart;
	}

	public void setDefaultStart(int defaultStart) {
		this.defaultStart = defaultStart;
	}

//	public VideoResult getVideoInfoByTime(int secondInDay) {
//	
//		if(videoInfoList == null || videoInfoList.size() == 0){
//			return null;
//		}
//		Log.d(TAG, "getVideoInfoByTime - videoInfoList size: " + videoInfoList.size());
//		int begin = 0;
//		int end = videoInfoList.size() - 1;
//		while(begin <= end){
//			int mid = (begin + end)/2;
//			VideoInfo midVideoInfo = videoInfoList.get(mid);
//			if(secondInDay > midVideoInfo.getIntStartTimeBySeconds()){
//				begin = mid; 
//			}else if(secondInDay < midVideoInfo.getIntStartTimeBySeconds()){
//				end = mid - 1; 
//			}else{
//				VideoResult videoResult = new VideoResult(midVideoInfo, true);
////				int nextIndex = begin;
////				int prevIndex = begin - 2;
//				int nextIndex = mid + 1;
//				int prevIndex = mid - 1;
//				if(nextIndex < videoInfoList.size()){
//					videoResult.setNextIndex(nextIndex);
//				}
//				if(prevIndex >= 0){
//					videoResult.setPrevIndex(prevIndex);
//				}
//				return videoResult;
//			}
//		}
//		 
//		Log.d(TAG, "getVideoInfoByTime: " + (begin - 1));
//		
//		if(begin - 1 >= 0){
//			VideoInfo videoInfo = videoInfoList.get(begin - 1);
//			VideoResult videoResult = new VideoResult(videoInfo, true);
//			int nextIndex = begin;
//			int prevIndex = begin - 2;
//			if(nextIndex < videoInfoList.size()){
//				videoResult.setNextIndex(nextIndex);
//			}
//			if(prevIndex >= 0){
//				videoResult.setPrevIndex(prevIndex);
//			}
//			return videoResult;
//		}
//		
//		VideoResult videoResult = new VideoResult(null, true);
//		videoResult.setNextIndex(0);
//		return videoResult;
//	}
	
	
	public VideoResult getVideoInfoByTime(int secondInDay) {
		
		if(videoInfoList == null || videoInfoList.size() == 0){
			return null;
		}
		
		int size = videoInfoList.size() - 1;
//		Log.d(TAG, "getVideoInfoByTime - videoInfoList size: " + size);
		int index = size - 1;
		for(int i = 0; i < size; i++){
			VideoInfo midVideoInfo = videoInfoList.get(i);
			if(secondInDay < midVideoInfo.getIntStartTimeBySeconds()){
				index = i - 1;
				break;
			}
		}
		
		if(index >= 0){
			VideoInfo videoInfo = videoInfoList.get(index);
			VideoResult videoResult = new VideoResult(videoInfo, true);
			int nextIndex = index + 1;
			int prevIndex = index - 1;
			if(nextIndex < videoInfoList.size()){
				videoResult.setNextIndex(nextIndex);
			}
			if(prevIndex >= 0){
				videoResult.setPrevIndex(prevIndex);
			}
			return videoResult;
		}
		
		VideoResult videoResult = new VideoResult(null, true);
		videoResult.setNextIndex(0);
		return videoResult;
		
	}

	public int getCurrentIndex() {
		return currentIndex;
	}

	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
	}

	public VideoResult getVideoInfoByIndex(int videoIndex) {
//		if(videoInfoList != null){
//			Log.d(TAG, "videoIndex/videoInfoSize: " + videoIndex + "/" + videoInfoList.size());	
//		}else{
//			Log.d(TAG, "videoIndex/videoInfoSize: " + videoIndex + "/-1");	
//		}
		
		if(videoInfoList != null && videoInfoList.size() > 0 && videoIndex < videoInfoList.size() && videoIndex >= 0){
			VideoInfo videoInfo = videoInfoList .get(videoIndex);
			VideoResult videoResult = new VideoResult(videoInfo, true);
			int nextIndex = videoIndex + 1;
			int prevIndex = videoIndex - 1;
			if(nextIndex < videoInfoList.size()){
				videoResult.setNextIndex(nextIndex);
			}
			
			if(prevIndex >= 0){
				videoResult.setPrevIndex(prevIndex);
			}
			return videoResult;
		}
		return null;
	}

	public String getStrdate() {
		return strdate;
	}

	public void setStrdate(String strdate) {
		this.strdate = strdate;
	}

	public int checkTime(ServerTimeInfo serverTimeInfo) {
		int flag = 0;
		if(videoInfoList != null && videoInfoList.size() > 0){
			VideoInfo videoInfo = videoInfoList.get(0);
			if (serverTimeInfo.getStrdate().compareTo(strdate) > 0) {
				flag = 1;
			} else if (serverTimeInfo.getSecondInDay() < videoInfo.getIntStartTimeBySeconds()) {
				flag = -1;
			}
		}
		return flag;
	}

	public VideoResult getLastVideoOfYesterday1(String yesterdayCheduleUrl) {
	    // Making HTTP request
        
	       String content = "";
//	       HttpResponse response;
//	       HttpClient myClient = new DefaultHttpClient();
//	       HttpPost myConnection = new HttpPost(yesterdayCheduleUrl);
//	       try {
//	           response = myClient.execute(myConnection);
//	           content = EntityUtils.toString(response.getEntity(), "UTF-8");
//	       } catch (ClientProtocolException e) {
//	           e.printStackTrace();
//	       } catch (IOException e) {
//	           e.printStackTrace();
//	       }
//		   
//	       if(content == null){
//	    	   return null;
//	       }
//			
			try {
				URL urlLoc = new URL(yesterdayCheduleUrl);
				URLConnection connection = urlLoc.openConnection();
				connection.setConnectTimeout(10000);
				connection.setReadTimeout(15000);
				connection.connect();
				InputStream input = new BufferedInputStream(urlLoc.openStream());
				content = FileUtil.readFileToStringUsingStringWriter(input);
				if(content == null){
					return null;
				}
			JSONObject jsonVideoInfo = new JSONObject(content);
			Log.e("Bigsky", "jsonYesterdayVideoInfo: " + jsonVideoInfo);
			
			JSONArray jsonContentArray = jsonVideoInfo.getJSONArray("content");
			
			if(jsonContentArray != null && jsonContentArray.length() > 0){
				
				JSONObject jsonContent = jsonContentArray.getJSONObject(jsonContentArray.length() - 1);
				
				VideoInfo videoInfo = new VideoInfo();
				
				videoInfo.setIndex(jsonContentArray.length() - 1);
				
				videoInfo.setCurrentDay(false);
				
				videoInfo.setThumbIcon(Vp9ParamUtil.getValue(jsonContent.getString("thumbIcon"), ""));
				
				videoInfo.setMovieID(Vp9ParamUtil.getValue(jsonContent.getString("movieID"), ""));
				
				videoInfo.setGenre(Vp9ParamUtil.getValue(jsonContent.getString("genre"), ""));
				
				videoInfo.setIntProgram(Vp9ParamUtil.getValue(jsonContent.getString("intProgram"), -1));
				
				videoInfo.setProgramName(Vp9ParamUtil.getJsonString(jsonContent, "program_name", ""));
				
				videoInfo.setUserVideoId(Vp9ParamUtil.getValue(jsonContent.getString("user_video_id"), ""));
											
				videoInfo.setStatus(Vp9ParamUtil.getValue(jsonContent.getString("status"), -1));
				
				videoInfo.setRuntime(Vp9ParamUtil.getValue(jsonContent.getString("runtime"), ""));
				
				videoInfo.setResolution(Vp9ParamUtil.getValue(jsonContent.getString("resolution"), ""));
							
				videoInfo.setType(Vp9ParamUtil.getValue(jsonContent.getString("type"), ""));
				
				videoInfo.setDirector(Vp9ParamUtil.getValue(jsonContent.getString("director"), ""));
				
				videoInfo.setLikedNum(Vp9ParamUtil.getValue(jsonContent.getString("likedNum"), -1));
														
				videoInfo.setUrl(Vp9ParamUtil.getValue(jsonContent.getString("url"), ""));
				
//				ArrayList<VideoResolution> videoResolutions = getVideoResolutions(jsonContent);
//				
//				videoInfo.setVideoResolutions(videoResolutions);
				
				ArrayList<VideoResolutionGroup> videoResolutionGroups = getVideoResolutionGroup(jsonContent);
				
				videoInfo.setVideoResolutionGroups(videoResolutionGroups);
				
				videoInfo.setVideoName(Vp9ParamUtil.getValue(jsonContent.getString("video_name"), ""));

				videoInfo.setCast(Vp9ParamUtil.getValue(jsonContent.getString("cast"), ""));
				
				videoInfo.setStartTime(Vp9ParamUtil.getValue(jsonContent.getString("start_time"), ""));
				
				if(jsonContent.has("end_time")){
					videoInfo.setEndTime(Vp9ParamUtil.getValue(jsonContent.getString("end_time"), "0"));
				}else{
					videoInfo.setEndTime("0");
				}
				
				String videoSize = Vp9ParamUtil.getJsonString(jsonContent, "video_size", "");
				
				String[] elemSizes = videoSize.trim().split("x");
				
				int width = 0, height = 0;
				
				if(elemSizes != null && elemSizes.length == 2){
					width = Vp9ParamUtil.getValue(elemSizes[0], 0);
					height = Vp9ParamUtil.getValue(elemSizes[1], 0);
				}
				
				videoInfo.setWidth(width);
				
				videoInfo.setHeight(height);
				
				String public_rate = Vp9ParamUtil.getValue(jsonContent.getString("public_rate"), "0").trim().replace(",", ".");
				
				videoInfo.setPublicRate(Vp9ParamUtil.getValue(public_rate, 0f));			
				
				videoInfo.setInDay(Vp9ParamUtil.getValue(jsonContent.getString("in_day"), -1));
				
				videoInfo.setYear(Vp9ParamUtil.getValue(jsonContent.getString("year"), -1));
				
				videoInfo.setVideoId(Vp9ParamUtil.getValue(jsonContent.getString("video_id"), -1));
				
				videoInfo.setSerial(Vp9ParamUtil.getValue(jsonContent.getString("serial"), -1));
				
				
				boolean isExistNewTypeSub = false;
				if(jsonContent.has("subtitles")){
					
					JSONArray jsArrSubtitles = jsonContent.getJSONArray("subtitles");
					if(jsArrSubtitles != null){
						ArrayList<SubtitleInfo> subtitleInfos = new ArrayList<SubtitleInfo>();
						
						if(jsArrSubtitles != null && jsArrSubtitles.length() > 0){
							for(int k = 0; k < jsArrSubtitles.length(); k++){
								isExistNewTypeSub = true;
								JSONObject jsSubtitle = jsArrSubtitles.getJSONObject(k);
								if(jsSubtitle != null){
									String subUrl = ParamUtil.getJsonString(jsSubtitle, "url", "");
//									String subType = "en";
									String subTypeName = ParamUtil.getJsonString(jsSubtitle, "name", "");
									boolean isDisplay = false;
									int languageId = ParamUtil.getJsonInt(jsSubtitle, "language_id", 0);
									if(!"".equals(subUrl.trim()) && !"".equals(subTypeName.trim())){
										SubtitleInfo subInfo = new SubtitleInfo(subUrl, languageId, subTypeName, isDisplay);
										subtitleInfos.add(subInfo);
									}
								}
							}
							videoInfo.setSubtitleInfos(subtitleInfos);
						}
					}
					
				}else if(!isExistNewTypeSub && jsonContent.has("subtitle")){
					JSONArray jsonArrSubtitles = jsonContent.getJSONArray("subtitle");
					
					ArrayList<SubtitleInfo> subtitleInfos = new ArrayList<SubtitleInfo>();
					
					if(jsonArrSubtitles != null && jsonArrSubtitles.length() > 0){
						for(int k = 0; k < jsonArrSubtitles.length(); k++){
							String jsonSubtitle = (String) jsonArrSubtitles.get(k);
							if(jsonSubtitle != null && jsonSubtitle.trim().endsWith("eng.ass")){
								String subUrl = jsonSubtitle;
								String subType = "en";
								String subTypeName = "English";
								boolean isDisplay = false;
								SubtitleInfo subInfo = new SubtitleInfo(subUrl, subType, subTypeName, isDisplay);
								subtitleInfos.add(subInfo);
							}else if(jsonSubtitle != null && jsonSubtitle.trim().endsWith("vie.ass")){
								String subUrl = jsonSubtitle;
								String subType = "vn";
								String subTypeName = "Viet Nam";
								boolean isDisplay = false;
								SubtitleInfo subInfo = new SubtitleInfo(subUrl, subType, subTypeName, isDisplay);
								subtitleInfos.add(subInfo);
							}
						}
						videoInfo.setSubtitleInfos(subtitleInfos);
					}
				}
				
//				JSONArray jsonArrSubtitles = jsonContent.getJSONArray("subtitle");
//				
//				ArrayList<SubtitleInfo> subtitleInfos = new ArrayList<SubtitleInfo>();
//				
//				if(jsonArrSubtitles != null && jsonArrSubtitles.length() > 0){
//					for(int k = 0; k < jsonArrSubtitles.length(); k++){
//						String jsonSubtitle = (String) jsonArrSubtitles.get(k);
//						if(jsonSubtitle != null && jsonSubtitle.trim().endsWith("eng.ass")){
//							String subUrl = jsonSubtitle;
//							String subType = "en";
//							String subTypeName = "English";
//							boolean isDisplay = false;
//							SubtitleInfo subInfo = new SubtitleInfo(subUrl, subType, subTypeName, isDisplay);
//							subtitleInfos.add(subInfo);
//						}else if(jsonSubtitle != null && jsonSubtitle.trim().endsWith("vie.ass")){
//							String subUrl = jsonSubtitle;
//							String subType = "vn";
//							String subTypeName = "Viet Nam";
//							boolean isDisplay = false;
//							SubtitleInfo subInfo = new SubtitleInfo(subUrl, subType, subTypeName, isDisplay);
//							subtitleInfos.add(subInfo);
//						}
//					}
//					videoInfo.setSubtitleInfos(subtitleInfos);
//				}
				
				String left_displays_url = Vp9ParamUtil.getJsonString(jsonContent, "left_sub", "");
//				updateLeftDisplays(videoInfo, left_displays_url);
				videoInfo.setLeftDisplaysUrl(left_displays_url);
				String right_displays_url = Vp9ParamUtil.getJsonString(jsonContent, "right_sub", "");
//				updateRightDisplays(videoInfo, right_displays_url);
				videoInfo.setRightDisplaysUrl(right_displays_url);
				
				VideoResult videoResult = new VideoResult(videoInfo, false);
				videoResult.setNextIndex(0);
				int prevIndex = videoInfo.getIndex() - 1;
				if(prevIndex >= 0){
					videoResult.setPrevIndex(prevIndex);
				}
				return videoResult;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
			VideoResult videoResult = new VideoResult(null, false);
			videoResult.setNextIndex(0);
			return videoResult;
	}

	public int getChannelId() {
		return channelId;
	}

	public void setChannelId(int channelId) {
		this.channelId = channelId;
	}

	public int getWaitingTime(ServerTimeInfo serverTimeInfo, int nextIndex, int defaultTime) {
		int waitingTime = defaultTime;
		if(videoInfoList != null && videoInfoList.size() > 0 && nextIndex < videoInfoList.size() && nextIndex >= 0){
			VideoInfo videoInfo = videoInfoList.get(nextIndex);
			waitingTime = (videoInfo.getIntStartTimeBySeconds() - serverTimeInfo.getSecondInDay())*1000;
		}
		return waitingTime;
	}

	public VideoResult getLastVideoOfYesterday2(String yesterdayCheduleUrl) {
		currentIndex = -1;
		videoInfoList = new ArrayList<VideoInfo>();
	    // Making HTTP request
        
       String content = "";
//       HttpResponse response;
//       HttpClient myClient = new DefaultHttpClient();
//       HttpPost myConnection = new HttpPost(yesterdayCheduleUrl);
//       try {
//           response = myClient.execute(myConnection);
//           content = EntityUtils.toString(response.getEntity(), "UTF-8");
//            
//       } catch (ClientProtocolException e) {
//           e.printStackTrace();
//       } catch (IOException e) {
//           e.printStackTrace();
//       }
//	   
//       if(content == null){
//    	   return null;
//       }
		
		try {
			URL urlLoc = new URL(yesterdayCheduleUrl);
			URLConnection connection = urlLoc.openConnection();
			connection.setConnectTimeout(10000);
			connection.setReadTimeout(15000);
			connection.connect();
			InputStream input = new BufferedInputStream(urlLoc.openStream());
			content = FileUtil.readFileToStringUsingStringWriter(input);
			if(content == null){
				return null;
			}
		JSONObject jsonVideoInfo = new JSONObject(content);
//		Log.e(TAG, "jsonVideoInfo: " + jsonVideoInfo);
		channelIcon = Vp9ParamUtil.getJsonString(jsonVideoInfo,"channelIcon", "");
		
		channelUrl = Vp9ParamUtil.getJsonString(jsonVideoInfo,"channelUrl", "");
		
		setChannelId(Vp9ParamUtil.getJsonInt(jsonVideoInfo, "channelId", -1));
		
		channelType = Vp9ParamUtil.getJsonInt(jsonVideoInfo, "channelType", -1);
		
		channelName = Vp9ParamUtil.getJsonString(jsonVideoInfo, "channelName", "");
		
		defaultStart = Vp9ParamUtil.getJsonInt(jsonVideoInfo, "defaultStart", -1);
		
		JSONArray jsonContentArray = jsonVideoInfo.getJSONArray("content");
		
		if(jsonContentArray != null && jsonContentArray.length() > 0){
			
			JSONObject jsonContent = jsonContentArray.getJSONObject(jsonContentArray.length() - 1);
			
			VideoInfo videoInfo = new VideoInfo();
			
			videoInfo.setIndex(jsonContentArray.length() - 1);
			
			videoInfo.setCurrentDay(true);
			
			videoInfo.setThumbIcon(Vp9ParamUtil.getJsonString(jsonContent, "thumbIcon", ""));
			
			videoInfo.setMovieID(Vp9ParamUtil.getJsonString(jsonContent, "movieID", ""));
			
			videoInfo.setGenre(Vp9ParamUtil.getJsonString(jsonContent, "genre", ""));
			
			videoInfo.setIntProgram(Vp9ParamUtil.getValue(Vp9ParamUtil.getJsonString(jsonContent, "intProgram", "-1"), -1));
			
			videoInfo.setProgramName(Vp9ParamUtil.getJsonString(jsonContent, "program_name", ""));
			
			videoInfo.setUserVideoId(Vp9ParamUtil.getJsonString(jsonContent, "user_video_id", ""));
										
			videoInfo.setStatus(Vp9ParamUtil.getValue(Vp9ParamUtil.getJsonString(jsonContent, "status", "-1"), -1));
			
			videoInfo.setRuntime(Vp9ParamUtil.getJsonString(jsonContent, "runtime", ""));
			
			videoInfo.setResolution(Vp9ParamUtil.getJsonString(jsonContent, "resolution", ""));
						
			videoInfo.setType(Vp9ParamUtil.getJsonString(jsonContent, "type", ""));
			
			videoInfo.setDirector(Vp9ParamUtil.getJsonString(jsonContent, "director", ""));
			
			videoInfo.setLikedNum(Vp9ParamUtil.getValue(Vp9ParamUtil.getJsonString(jsonContent, "likedNum", "-1"), -1));
													
			videoInfo.setUrl(Vp9ParamUtil.getJsonString(jsonContent, "src", ""));
			
			videoInfo.setVideoName(Vp9ParamUtil.getJsonString(jsonContent, "video_name", ""));

			videoInfo.setCast(Vp9ParamUtil.getJsonString(jsonContent, "cast", ""));
			
			videoInfo.setStartTime(Vp9ParamUtil.getJsonString(jsonContent, "start_time", ""));
			
			if(jsonContent.has("end_time")){
				videoInfo.setEndTime(Vp9ParamUtil.getJsonString(jsonContent, "end_time", "0"));
			}else{
				videoInfo.setEndTime("0");
			}
			
			String videoSize = Vp9ParamUtil.getJsonString(jsonContent, "video_size", "");
			
			String[] elemSizes = videoSize.trim().split("x");
			
			int width = 0, height = 0;
			
			if(elemSizes != null && elemSizes.length == 2){
				width = Vp9ParamUtil.getValue(elemSizes[0], 0);
				height = Vp9ParamUtil.getValue(elemSizes[1], 0);
			}
			
			videoInfo.setWidth(width);
			
			videoInfo.setHeight(height);
			
			String public_rate = Vp9ParamUtil.getJsonString(jsonContent, "public_rate", "0").trim().replace(",", ".");
			
			videoInfo.setPublicRate(Vp9ParamUtil.getValue(public_rate, 0f));			
			
			videoInfo.setInDay(Vp9ParamUtil.getValue(Vp9ParamUtil.getJsonString(jsonContent, "in_day", "-1"), -1));
			
			videoInfo.setYear(Vp9ParamUtil.getValue(Vp9ParamUtil.getJsonString(jsonContent, "year", "-1"), -1));
			
			videoInfo.setVideoId(Vp9ParamUtil.getValue(Vp9ParamUtil.getJsonString(jsonContent, "video_id", "-1"), -1));
			
			videoInfo.setSerial(Vp9ParamUtil.getValue(Vp9ParamUtil.getJsonString(jsonContent, "serial", "-1"), -1));
			
			
			boolean isExistNewTypeSub = false;
			if(jsonContent.has("subtitles")){
				
				JSONArray jsArrSubtitles = jsonContent.getJSONArray("subtitles");
				if(jsArrSubtitles != null){
					ArrayList<SubtitleInfo> subtitleInfos = new ArrayList<SubtitleInfo>();
					
					if(jsArrSubtitles != null && jsArrSubtitles.length() > 0){
						for(int k = 0; k < jsArrSubtitles.length(); k++){
							isExistNewTypeSub = true;
							JSONObject jsSubtitle = jsArrSubtitles.getJSONObject(k);
							if(jsSubtitle != null){
								String subUrl = ParamUtil.getJsonString(jsSubtitle, "url", "");
//								String subType = "en";
								String subTypeName = ParamUtil.getJsonString(jsSubtitle, "name", "");
								boolean isDisplay = false;
								int languageId = ParamUtil.getJsonInt(jsSubtitle, "language_id", 0);
								if(!"".equals(subUrl.trim()) && !"".equals(subTypeName.trim())){
									SubtitleInfo subInfo = new SubtitleInfo(subUrl, languageId, subTypeName, isDisplay);
									subtitleInfos.add(subInfo);
								}
							}
						}
						videoInfo.setSubtitleInfos(subtitleInfos);
					}
				}
				
			}else if(!isExistNewTypeSub && jsonContent.has("subtitle")){
				JSONArray jsonArrSubtitles = jsonContent.getJSONArray("subtitle");
				
				ArrayList<SubtitleInfo> subtitleInfos = new ArrayList<SubtitleInfo>();
				
				if(jsonArrSubtitles != null && jsonArrSubtitles.length() > 0){
					for(int k = 0; k < jsonArrSubtitles.length(); k++){
						String jsonSubtitle = (String) jsonArrSubtitles.get(k);
						if(jsonSubtitle != null && jsonSubtitle.trim().endsWith("eng.ass")){
							String subUrl = jsonSubtitle;
							String subType = "en";
							String subTypeName = "English";
							boolean isDisplay = false;
							SubtitleInfo subInfo = new SubtitleInfo(subUrl, subType, subTypeName, isDisplay);
							subtitleInfos.add(subInfo);
						}else if(jsonSubtitle != null && jsonSubtitle.trim().endsWith("vie.ass")){
							String subUrl = jsonSubtitle;
							String subType = "vn";
							String subTypeName = "Viet Nam";
							boolean isDisplay = false;
							SubtitleInfo subInfo = new SubtitleInfo(subUrl, subType, subTypeName, isDisplay);
							subtitleInfos.add(subInfo);
						}
					}
					videoInfo.setSubtitleInfos(subtitleInfos);
				}
			}
			
//			if(jsonContent.has("subtitle")){
//				JSONArray jsonArrSubtitles = jsonContent.getJSONArray("subtitle");
//				
//				ArrayList<SubtitleInfo> subtitleInfos = new ArrayList<SubtitleInfo>();
//				
//				if(jsonArrSubtitles != null && jsonArrSubtitles.length() > 0){
//					for(int k = 0; k < jsonArrSubtitles.length(); k++){
//						String jsonSubtitle = (String) jsonArrSubtitles.get(k);
//						if(jsonSubtitle != null && jsonSubtitle.trim().endsWith("eng.ass")){
//							String subUrl = jsonSubtitle;
//							String subType = "en";
//							String subTypeName = "English";
//							boolean isDisplay = false;
//							SubtitleInfo subInfo = new SubtitleInfo(subUrl, subType, subTypeName, isDisplay);
//							subtitleInfos.add(subInfo);
//						}else if(jsonSubtitle != null && jsonSubtitle.trim().endsWith("vie.ass")){
//							String subUrl = jsonSubtitle;
//							String subType = "vn";
//							String subTypeName = "Viet Nam";
//							boolean isDisplay = false;
//							SubtitleInfo subInfo = new SubtitleInfo(subUrl, subType, subTypeName, isDisplay);
//							subtitleInfos.add(subInfo);
//						}
//					}
//					videoInfo.setSubtitleInfos(subtitleInfos);
//				}
//			}
			
			if(jsonContent.has("content")){
				JSONArray jsonArrContent = jsonContent.getJSONArray("content");
				ArrayList<VideoInfo> childVideoInfoInfos = new ArrayList<VideoInfo>();
				if(jsonArrContent != null && jsonArrContent.length() > 0){
					for(int k = 0; k < jsonArrContent.length(); k++){
						JSONObject jsonChildContent = jsonArrContent.getJSONObject(k);
						if(jsonChildContent != null && jsonChildContent.has("0")){
							JSONObject jsonChild = jsonChildContent.getJSONObject("0");
							if(jsonChild != null){
								VideoInfo childVideoInfo = getVideoInfo(jsonChild);
								childVideoInfo.setIndex(k);
								childVideoInfoInfos.add(childVideoInfo);
							}
						}
					}
					videoInfo.setChildVideoInfoList(childVideoInfoInfos);
				}
			}
			
			VideoResult videoResult = new VideoResult(videoInfo, false);
			videoResult.setNextIndex(0);
			int prevIndex = videoInfo.getIndex() - 1;
			if(prevIndex >= 0){
				videoResult.setPrevIndex(prevIndex);
			}
			return videoResult;
		}
		
	} catch (Exception e) {
		e.printStackTrace();
	}	

	VideoResult videoResult = new VideoResult(null, false);
	videoResult.setNextIndex(0);
	return videoResult;
	
	}

	public int getCurrentChildIndex() {
		return currentChildIndex;
	}

	public void setCurrentChildIndex(int currentChildIndex) {
		this.currentChildIndex = currentChildIndex;
	}

	public boolean isContainData() {
		if(videoInfoList != null && videoInfoList.size() > 0){
			return true;
		}
		return false;
	}

	public VideoInfo updateInfoForPlayVideos(JSONArray jsonFilmRelateds, String curMovieID) {
		VideoInfo curVideoInfo = null;
		try {
			if(jsonFilmRelateds != null){
				ArrayList<VideoInfo> videoInfos = new ArrayList<VideoInfo>();
				int index = 0;
				for(int i = 0; i < jsonFilmRelateds.length(); i++){
					JSONObject jsonFilmRelated = jsonFilmRelateds.getJSONObject(i);
					if(jsonFilmRelated != null){
						VideoInfo videoInfo = getVideoInfoForPlayVideos(jsonFilmRelated);
						if(videoInfo.getIntProgram() == 0){
							videoInfo.setIndex(index);
							videoInfos.add(videoInfo);
							if(curMovieID.trim().equals(videoInfo.getMovieID())){
								setCurrentIndex(index);
								curVideoInfo = videoInfo;
							}
							index++;
						}

					}
				}
				
				if(videoInfos != null && videoInfos.size() > 0){
					videoInfoList = videoInfos;
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return curVideoInfo;
	}

	private VideoInfo getVideoInfoForPlayVideos(JSONObject jsonFilmRelated) {
		
		VideoInfo videoInfo = new VideoInfo();
		
		videoInfo.setThumbIcon(Vp9ParamUtil.getJsonString(jsonFilmRelated, "thumbIcon", ""));
		
		videoInfo.setMovieID(Vp9ParamUtil.getJsonString(jsonFilmRelated, "movieID", ""));
		
		videoInfo.setGenre(Vp9ParamUtil.getJsonString(jsonFilmRelated, "genre", ""));
		
		videoInfo.setIntProgram(Vp9ParamUtil.getValue(Vp9ParamUtil.getJsonString(jsonFilmRelated, "intProgram", "-1"), -1));
		
		videoInfo.setProgramName(Vp9ParamUtil.getJsonString(jsonFilmRelated, "program_name", ""));
		
		videoInfo.setStatus(Vp9ParamUtil.getValue(Vp9ParamUtil.getJsonString(jsonFilmRelated, "status", "-1"), -1));
		
		videoInfo.setRuntime(Vp9ParamUtil.getJsonString(jsonFilmRelated, "runtime", ""));
		
		videoInfo.setResolution(Vp9ParamUtil.getJsonString(jsonFilmRelated, "resolution", ""));
					
		videoInfo.setType(Vp9ParamUtil.getJsonString(jsonFilmRelated, "type", ""));
		
		videoInfo.setDirector(Vp9ParamUtil.getJsonString(jsonFilmRelated, "director", ""));
		
		videoInfo.setLikedNum(Vp9ParamUtil.getValue(Vp9ParamUtil.getJsonString(jsonFilmRelated, "likedNum", "-1"), -1));
												
//		videoInfo.setUrl(Vp9ParamUtil.getJsonString(jsonFilmRelated, "url", ""));
		
		videoInfo.setVideoName(Vp9ParamUtil.getJsonString(jsonFilmRelated, "name", ""));

		videoInfo.setCast(Vp9ParamUtil.getJsonString(jsonFilmRelated, "cast", ""));
				
		String videoSize = Vp9ParamUtil.getJsonString(jsonFilmRelated, "resolution", "");
		
		String[] elemSizes = videoSize.trim().split("x");
		
		int width = 0, height = 0;
		
		if(elemSizes != null && elemSizes.length == 2){
			width = Vp9ParamUtil.getValue(elemSizes[0], 0);
			height = Vp9ParamUtil.getValue(elemSizes[1], 0);
		}
		
		videoInfo.setWidth(width);
		
		videoInfo.setHeight(height);
			
		String public_rate = Vp9ParamUtil.getJsonString(jsonFilmRelated, "public_rate", "0").trim().replace(",", ".");
		
		videoInfo.setPublicRate(Vp9ParamUtil.getValue(public_rate, 0f));			
		
		videoInfo.setYear(Vp9ParamUtil.getValue(Vp9ParamUtil.getJsonString(jsonFilmRelated, "year", "-1"), -1));
		
		videoInfo.setSerial(Vp9ParamUtil.getValue(Vp9ParamUtil.getJsonString(jsonFilmRelated, "serial", "-1"), -1));
				
		return videoInfo;
	}

	public VideoInfo getVideoInfoByVideoId(String movieID) {
		
		if(videoInfoList != null && videoInfoList.size() > 0){
			for(int i = 0; i < videoInfoList.size(); i++){
				VideoInfo videoInfo = videoInfoList.get(i);
				if(movieID.equals(videoInfo.getMovieID())){
					return videoInfo;
				}
			}
		}
		return null;
	}

	public VideoInfo updateInfoForOffTvPrograms(JSONArray jsonvideoRelateds,
			String videoUrl) {
		VideoInfo curVideoInfo = null;
		try {
			if(jsonvideoRelateds != null){
				ArrayList<VideoInfo> videoInfos = new ArrayList<VideoInfo>();
				int index = 0;
				for(int i = 0; i < jsonvideoRelateds.length(); i++){
					JSONObject jsonVideoRelated = jsonvideoRelateds.getJSONObject(i);
					if(jsonVideoRelated != null){
						VideoInfo videoInfo = new VideoInfo();
																
						videoInfo.setVideoName(Vp9ParamUtil.getJsonString(jsonVideoRelated, "videoName", ""));

						videoInfo.setUrl(Vp9ParamUtil.getJsonString(jsonVideoRelated, "videoUrl", ""));
						
						videoInfo.setStartTime(Vp9ParamUtil.getJsonString(jsonVideoRelated, "startTime", ""));
						
						videoInfo.setEndTime(Vp9ParamUtil.getJsonString(jsonVideoRelated, "endTime", ""));
//						ArrayList<VideoResolution> videoResolutions = getVideoResolutions(jsonVideoRelated);
//						
//						videoInfo.setVideoResolutions(videoResolutions);
						
						ArrayList<VideoResolutionGroup> videoResolutionGroups = getVideoResolutionGroup(jsonVideoRelated);
						
						videoInfo.setVideoResolutionGroups(videoResolutionGroups);
						
						videoInfo.setRecord(Vp9ParamUtil.getJsonString(jsonVideoRelated, "record", ""));
						
						videoInfo.setIndex(index);
						
						videoInfos.add(videoInfo);
						
						if(videoUrl.trim().equals(videoInfo.getUrl())){
							setCurrentIndex(index);
							curVideoInfo = videoInfo;
						}
						index++;

					}
				}
				
				if(videoInfos != null && videoInfos.size() > 0){
					videoInfoList = videoInfos;
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return curVideoInfo;
	}
	
	
//	public ArrayList<VideoResolution> getVideoResolutions(JSONObject jsonVideoInfo) throws JSONException{
//		
//		JSONArray jsaElems = Vp9ParamUtil.getJSONArray(jsonVideoInfo, "src_list");
//		
//		ArrayList<VideoResolution> videoResolutions = new ArrayList<VideoResolution>();
//				
//		if(jsaElems != null){
//			
//			for(int i = 0; i < jsaElems.length(); i++){
//				
//				JSONObject jsaElem = jsaElems.getJSONObject(i);
//				
//				if(jsaElem != null){
//					
//					String video_url = Vp9ParamUtil.getJsonString(jsaElem, "src", "");
//					
//					String type = Vp9ParamUtil.getJsonString(jsaElem, "type", "");
//					
//					String codec = Vp9ParamUtil.getJsonString(jsaElem, "codec", "");
//					
//					int bitrate = Vp9ParamUtil.getJsonInt(jsaElem, "bitrate", 0);
//					
//					String quality = Vp9ParamUtil.getJsonString(jsaElem, "quality", "");
//					
//					String resolution = Vp9ParamUtil.getJsonString(jsaElem, "resolution", "");
//					
//					int rotate = Vp9ParamUtil.getJsonInt(jsaElem, "rotate", 0);
//					
//					VideoResolution videoResolution = new VideoResolution();
//					
//					videoResolution.setVideoUrl(video_url);
//					
//					videoResolution.setType(type);
//					
//					videoResolution.setCodec(codec);
//					
//					videoResolution.setBitrate(bitrate);
//					
//					videoResolution.setQuality(quality);
//					
//					videoResolution.setResolution(resolution);
//					
//					videoResolution.setRotation(rotate);
//					
//					videoResolutions.add(videoResolution);
//					
//				}
//			}
//		}
//		return videoResolutions;
//	}
	
	public ArrayList<VideoResolutionGroup> getVideoResolutionGroup(JSONObject jsonVideoInfo) throws JSONException{
		
		JSONArray jsaElems = Vp9ParamUtil.getJSONArray(jsonVideoInfo, "src_list");
		
		HashMap<String, VideoResolutionGroup> videoResolGroupMap = new HashMap<String, VideoResolutionGroup>();
				
		if(jsaElems != null){
			
			for(int i = 0; i < jsaElems.length(); i++){
				
				JSONObject jsaElem = jsaElems.getJSONObject(i);
				
				if(jsaElem != null){
					
					String video_url = Vp9ParamUtil.getJsonString(jsaElem, "src", "");
					
					String type = Vp9ParamUtil.getJsonString(jsaElem, "type", "");
					
					String codec = Vp9ParamUtil.getJsonString(jsaElem, "codec", "");
					
					int bitrate = Vp9ParamUtil.getJsonInt(jsaElem, "bitrate", 0);
					
					String quality = Vp9ParamUtil.getJsonString(jsaElem, "quality", "");
					
					String resolution = Vp9ParamUtil.getJsonString(jsaElem, "resolution", "");
					
					int rotate = Vp9ParamUtil.getJsonInt(jsaElem, "rotate", 0);
					
					int priority = Vp9ParamUtil.getJsonInt(jsaElem, "prio", 0);
					
					String key = codec + "-" + resolution;
					
					VideoResolutionGroup videoResolGroup;
					if(videoResolGroupMap.containsKey(key)){
						videoResolGroup = videoResolGroupMap.get(key);
					}else{
						videoResolGroup = new VideoResolutionGroup();
						videoResolGroup.setCodec(codec);
						videoResolGroup.setResolution(resolution);
						videoResolGroupMap.put(key, videoResolGroup);
					}
					
					VideoResolution videoResolution = new VideoResolution();
					
					videoResolution.setVideoUrl(video_url);
					
					videoResolution.setType(type);
					
					videoResolution.setCodec(codec);
					
					videoResolution.setBitrate(bitrate);
					
					videoResolution.setQuality(quality);
					
					videoResolution.setResolution(resolution);
					
					videoResolution.setRotation(rotate);
					
					videoResolution.setPriority(priority);
					
					videoResolGroup.add(videoResolution);
					
				}
			}
		}
		ArrayList<VideoResolutionGroup> videoResolutions = new ArrayList<VideoResolutionGroup>();
		
		Collection<VideoResolutionGroup> values = videoResolGroupMap.values();
		
		if(values != null){
			for (Iterator<VideoResolutionGroup> iterator = values.iterator(); iterator.hasNext();) {
				VideoResolutionGroup videoResolutionGroup = (VideoResolutionGroup) iterator
						.next();
				videoResolutions.add(videoResolutionGroup);
			}
		}
		
		Log.d(TAG, "videoResolutions-size: " + videoResolutions.size());
		return videoResolutions;
	}

	public void setRecordVideo(String _channelId, String _date, final String vp9RecordUrl) {
		
		final String channelId= _channelId;
		final String date = _date;
//		new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
				String year = date.substring(0, 4);
				String month = date.substring(4, 6);
				String day = date.substring(6, 8);
				String recordUrl;
				if(vp9RecordUrl != null && !"".equals(vp9RecordUrl.trim())){
					recordUrl = vp9RecordUrl.trim() + "/" + year + "/" + month + "/" + day + "/" + channelId + "/"; 
				}else{
					recordUrl = "http://f.vp9.tv/truyenhinh/" + year + "/" + month + "/" + day + "/" + channelId + "/"; 
				}
				
				
						
				URL url = null;
				HttpURLConnection urlConnection = null;
				ArrayList<String> recordsArrayList = new ArrayList<String>();
				try {
					url = new URL(recordUrl + "/list.txt");
					urlConnection = (HttpURLConnection) url.openConnection();
				
					InputStream in = new BufferedInputStream(urlConnection.getInputStream());
					BufferedReader reader = new BufferedReader(new InputStreamReader(in));
					String line;
					while ((line = reader.readLine()) != null) {
						if (line != null) {
							recordsArrayList.add(line);
						}
					}
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					urlConnection.disconnect();
				}
				
				DemandTiviSchedule.this.recordUrl = recordUrl;
				DemandTiviSchedule.this.recordVideoIds = recordsArrayList;
				
//			}
//		}).start();
		
	}
	
	public ArrayList<String> getRecordVideoIds() {
		return recordVideoIds;
	}
	
	public String getRecordUrl() {
		return recordUrl;
	}
	
	public ArrayList<VideoInfo> getVideoInfoList() {
		return videoInfoList;
	}
	
	
}
