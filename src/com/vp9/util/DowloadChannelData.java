package com.vp9.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Base64;
import android.util.Log;

import com.vp9.player.serveTime.ServerTimeInfo;

public class DowloadChannelData {
	
	public static final String TAG = "DowloadChannelData";
	
	private String serverUrl;
	private String channelId;
	private ServerTimeInfo serverTimeInfo;
	private String token;

	private String strDate;

	private ArrayList<String> requestChannelIdList;

	public DowloadChannelData(String serverUrl, String channelId, ServerTimeInfo serverTimeInfo, String token){
		this.serverUrl = serverUrl;
		this.channelId = channelId;
		this.serverTimeInfo = serverTimeInfo;
		this.token = token;
		this.strDate = null;
	}
	
	public DowloadChannelData(String serverUrl, String channelId, String strDate, String token){
		this.serverUrl = serverUrl;
		this.channelId = channelId;
		this.strDate = strDate;
		this.token = token;
		this.serverTimeInfo = null;
	}

	public DowloadChannelData(String serverUrl,
			ArrayList<String> requestChannelIdList, String token) {
		this.serverUrl = serverUrl;
		this.token = token;
		this.requestChannelIdList = requestChannelIdList;
		this.strDate = null;
	}

	public void dowloadChannelDescription(ArrayList<String> requestChannelIdList, String vp9OfflineDirPath){
		Log.d(TAG, "dowloadChannelDescription method 1");
		final String fixServerUrl = serverUrl;
		
		File vp9Dir = new File(vp9OfflineDirPath);
		Log.d(TAG, "dowloadChannelDescription method 2");
		if(!vp9Dir.exists()){
			vp9Dir.mkdir();	
		}
		String dirPath = vp9OfflineDirPath + "/" + fixServerUrl.replaceAll("http://", "").replaceAll("https://", "").replaceAll("/", "").replaceAll(" ", "");
		File dir = new File(dirPath);
		if(!dir.exists()){
			dir.mkdir();	
		}
		ArrayList<Integer> existChannelIdList = getAllExistsOffChannel(dirPath);
		Log.d(TAG, "dowloadChannelDescription method 3");
//		int intChannelId = Vp9ParamUtil.getValue(fixChannelId, 0);
		for(String requestChannelId : requestChannelIdList){
			int intChannelId = Vp9ParamUtil.getValue(requestChannelId, 0);
			existChannelIdList.add(intChannelId);
		}
		getChannelDescriptionList(dirPath, fixServerUrl + JsonChannelConstant.CHANNEL_DESCR_LIST_JSON, existChannelIdList);
	}
	
	public boolean isNewChannelDescriptionFromServer(String vp9OfflineDirPath) {
		String dirPath = vp9OfflineDirPath + "/" + serverUrl.replaceAll("http://", "").replaceAll("https://", "").replaceAll("/", "").replaceAll(" ", "");
		String filePath = dirPath + "/" + JsonChannelConstant.CHANNEL_DESCR_LIST_NAME;
		return !isModified(serverUrl + JsonChannelConstant.CHANNEL_DESCR_LIST_JSON, filePath);
	}
	
	public void dowloadChannelInfo(String vp9OfflineDirPath) {
		Log.d(TAG, "dowloadChannelInfo method 1");
		final String fixChannelId = channelId;
		final String fixServerUrl = serverUrl;
		
		File vp9Dir = new File(vp9OfflineDirPath);
		Log.d(TAG, "dowloadChannelInfo method 2");
		if(!vp9Dir.exists()){
			vp9Dir.mkdir();	
		}
		String dirPath = vp9OfflineDirPath + "/" + fixServerUrl.replaceAll("http://", "").replaceAll("https://", "").replaceAll("/", "").replaceAll(" ", "");
		File dir = new File(dirPath);
		if(!dir.exists()){
			dir.mkdir();	
		}
//		ArrayList<Integer> existChannelIdList = getAllExistsOffChannel(dirPath);
//		Log.d(TAG, "dowloadChannelInfo method 3");
//		int intChannelId = Vp9ParamUtil.getValue(fixChannelId, 0);
//		if(existChannelIdList.contains(intChannelId)){
//			String filePath = dirPath + "/" + JsonChannelConstant.CHANNEL_DESCR_LIST_NAME;
//			if(!isModified(fixServerUrl + JsonChannelConstant.CHANNEL_DESCR_LIST_JSON, filePath)){
//				getChannelDescriptionList(dirPath, fixServerUrl + JsonChannelConstant.CHANNEL_DESCR_LIST_JSON, fixChannelId, existChannelIdList);
//			}
//		}else{
//			getChannelDescriptionList(dirPath, fixServerUrl + JsonChannelConstant.CHANNEL_DESCR_LIST_JSON, fixChannelId, existChannelIdList);
////			existChannelIdList.add(intChannelId);
//		}
		String strDate;
		if(serverTimeInfo != null){
			
			serverTimeInfo.updateTime();
			
			strDate = serverTimeInfo.getStrdate();
		}else{
			strDate = this.strDate;
		}

//		for(int existChannelId : existChannelIdList){	// 803, 804
//			String strChannelId = String.valueOf(existChannelId);
//			String cheduleUrl = getCheduleUrl(fixServerUrl, strChannelId, strDate);
//			String strChannelDir = dirPath + "/" + JsonChannelConstant.PREFIX_CHANNEL_ID + channelId;
//			String filePath = strChannelDir + "/" + JsonChannelConstant.PREFIX_CHANNEL_ID + channelId + "_" + strDate + ".json";
//			if(!isModified(cheduleUrl, filePath) && strChannelId.equals(channelId)){
//				getChannelData(dirPath, strChannelId, cheduleUrl, strDate);
//			}
//		}
		
		String cheduleUrl = getCheduleUrl(fixServerUrl, fixChannelId, strDate);
		
		String strChannelDir = dirPath + "/" + JsonChannelConstant.PREFIX_CHANNEL_ID + fixChannelId;
		String filePath = strChannelDir + "/" + JsonChannelConstant.PREFIX_CHANNEL_ID + fixChannelId + "_" + strDate + ".json";
		
		if(!isModified(cheduleUrl, filePath)){
			getChannelData(dirPath, fixChannelId, cheduleUrl, strDate);
		}
		Log.d(TAG, "dowloadChannelInfo method 5");
		
		
	}
	


	private boolean isModified(String url, String filePath) {
		boolean isModified = false;
		try {
			long urlLastModified = getLastModified(url);
			File file = new File(filePath);
			long fileLastModified = file.lastModified();
			if(urlLastModified == fileLastModified){
				isModified = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.d(TAG, "isModified: url=" + url + ", filePath=" + filePath + " => " + isModified);
		return isModified;
	}

	// GET THE LAST MODIFIED TIME
	public static long getLastModified(String url) {
		long date = 0;
		HttpURLConnection.setFollowRedirects(false);
		try {
			HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
			date = con.getLastModified();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return date;
	}

	private ArrayList<Integer> getAllExistsOffChannel(String dirPath) {
		ArrayList<Integer> existChannelIdList = new ArrayList<Integer>();
		File dir = new File(dirPath);
		if(dir != null){
			File[] listFiles = dir.listFiles();
			if(listFiles != null){
				for(File file : listFiles){
					if(file != null && file.isDirectory()){
						String fileName = file.getName();
						if(fileName != null && fileName.startsWith(JsonChannelConstant.PREFIX_CHANNEL_ID) && fileName.length() >= 9){
							String strChannelId = fileName.substring(8, fileName.length());
							int channelId = Vp9ParamUtil.getValue(strChannelId, -1);
							if(channelId != -1){
								existChannelIdList.add(channelId);
							}
						}
					}
				}
			}
		}
		return existChannelIdList;
	}







	private void getChannelData(String dirPath, String channelId, String cheduleUrl, String strFixDate) {
//		String content = "";
//		HttpResponse response;
//		HttpClient myClient = new DefaultHttpClient();
//		HttpPost myConnection = new HttpPost(cheduleUrl);
//		try {
//			response = myClient.execute(myConnection);
//			content = EntityUtils.toString(response.getEntity(), "UTF-8");
//
//		} catch (ClientProtocolException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		if (content == null) {
//			return;
//		}

		try {
			URL urlLoc = new URL(cheduleUrl);
			URLConnection connection = urlLoc.openConnection();
			connection.setConnectTimeout(10000);
			connection.setReadTimeout(15000);
			connection.connect();
			long longDate = connection.getLastModified();
			 
			// download the file
			InputStream input = new BufferedInputStream(urlLoc.openStream());
//			int available = input.available();
//			if(available <= 0){
//				return;
//			}
//			byte[] buffer = new byte[available];
//			input.read(buffer);
//			String content = new String(buffer, "UTF-8");
			
			String content = readFileToStringUsingStringWriter(input);
			Log.d(TAG, "getChannelData 1");
			if(content == null){
				return;
			}
			Log.d(TAG, "getChannelData 2");
			JSONObject jsonChannelInfo = new JSONObject(content);
			Log.d(TAG, "getChannelData 3");
			if(jsonChannelInfo != null){
				Log.d(TAG, "getChannelData 4");
				String channelIconUrl = Vp9ParamUtil.getJsonString(jsonChannelInfo, JsonChannelConstant.CHANNEL_ICON, "");
				String fileName = getFileNameByUrl(channelIconUrl);
				if(fileName != null){
					String newChannelIconUrl = JsonChannelConstant.CHANNEL_ICON_DIR + "/" + fileName;
					jsonChannelInfo.put(JsonChannelConstant.CHANNEL_ICON, newChannelIconUrl);
				}
				JSONArray jsonContents = Vp9ParamUtil.getJSONArray(jsonChannelInfo, JsonChannelConstant.CHANNEL_CONTENT);
				
				if(jsonContents != null && jsonContents.length() > 0){
					Log.d(TAG, "getChannelData 5: " + jsonContents.length());
					int len = jsonContents.length();
					for(int i = 0; i < len; i++){
						Log.d(TAG, "getChannelData 6: " + i);
						JSONObject jsonContent = jsonContents.getJSONObject(i);
						if(jsonContent == null){
							continue;
						}
						Log.d(TAG, "getChannelData 7: ");
						String thumbIconUrl = Vp9ParamUtil.getJsonString(jsonContent, JsonChannelConstant.THUMB_ICON, null);
						if(thumbIconUrl != null && !"".equals(thumbIconUrl.trim())){
							String newThumbIconUrl = extrudeUrl(thumbIconUrl);
							jsonContent.put(JsonChannelConstant.THUMB_ICON, newThumbIconUrl);
//							saveIcon(dirPath, thumbIconUrl, newThumbIconUrl);
							saveFile(dirPath, thumbIconUrl, newThumbIconUrl);
						}
						Log.d(TAG, "getChannelData 8: ");
						String rightSub = Vp9ParamUtil.getJsonString(jsonContent, JsonChannelConstant.RIGHT_SUB, null);
						if(rightSub != null && !"".equals(rightSub.trim())){
							String newRightSub = extrudeUrl(rightSub);
							jsonContent.put(JsonChannelConstant.RIGHT_SUB, newRightSub);
							saveFile(dirPath, rightSub, newRightSub);
						}
						Log.d(TAG, "getChannelData 9: ");
						String leftSub = Vp9ParamUtil.getJsonString(jsonContent, JsonChannelConstant.LEFT_SUB, null);
						if(leftSub != null && !"".equals(leftSub.trim())){
							String newLeftSub = extrudeUrl(leftSub);
							jsonContent.put(JsonChannelConstant.LEFT_SUB, newLeftSub);
							saveFile(dirPath, leftSub, newLeftSub);
						}
						Log.d(TAG, "getChannelData 10: ");
						String url = Vp9ParamUtil.getJsonString(jsonContent, JsonChannelConstant.URL_VIDEO, null);
						if(url != null && !"".equals(url.trim())){
							String newUrl = extrudeUrl(url);
							jsonContent.put(JsonChannelConstant.URL_VIDEO, newUrl);
							saveFile(dirPath, url, newUrl);
						}
						Log.d(TAG, "getChannelData 11: ");
						String src = Vp9ParamUtil.getJsonString(jsonContent, JsonChannelConstant.SRC_VIDEO, null);
						if(src != null && !"".equals(src.trim())){
							String newSrc = extrudeUrl(src);
							jsonContent.put(JsonChannelConstant.SRC_VIDEO, newSrc);
							saveFile(dirPath, src, newSrc);
						}
						Log.d(TAG, "getChannelData 12: ");
						JSONArray jsArrSubtitles = Vp9ParamUtil.getJSONArray(jsonContent, JsonChannelConstant.SUBTITLES);
						if(jsArrSubtitles != null){
							if(jsArrSubtitles != null && jsArrSubtitles.length() > 0){
								for(int k = 0; k < jsArrSubtitles.length(); k++){
									JSONObject jsSubtitle = jsArrSubtitles.getJSONObject(k);
									if(jsSubtitle != null){
										String subUrl = ParamUtil.getJsonString(jsSubtitle, JsonChannelConstant.URL_VIDEO, null);

										if(subUrl != null && !"".equals(subUrl.trim())){
											String newUrl = extrudeUrl(subUrl);
											jsSubtitle.put(JsonChannelConstant.URL_VIDEO, newUrl);
											saveFile(dirPath, subUrl, newUrl);
										}
									}
								}
							}
						}
						Log.d(TAG, "getChannelData 13: ");
						saveVideoResolutionGroup(dirPath, jsonContent);
						Log.d(TAG, "getChannelData 13 -a: ");
						JSONArray jsonArrContent = Vp9ParamUtil.getJSONArray(jsonContent, JsonChannelConstant.CHANNEL_CONTENT);
						if(jsonArrContent != null && jsonArrContent.length() > 0){
							for(int k = 0; k < jsonArrContent.length(); k++){
								Log.d(TAG, "getChannelData 13 -b: " + k);
								JSONObject jsonChildContent = jsonArrContent.getJSONObject(k);
								if(jsonChildContent != null && jsonChildContent.has("0")){
									Log.d(TAG, "getChannelData 13 -c: ");
									JSONObject jsonChild = jsonChildContent.getJSONObject("0");
									if(jsonChild != null){
										Log.d(TAG, "getChannelData 13 -d: ");
										handleJsonChildContent(dirPath, jsonChild);
									}
								}
							}
						}
					}
				}
				Log.d(TAG, "getChannelData 14: ");
				String strChannelDir = dirPath + "/" + JsonChannelConstant.PREFIX_CHANNEL_ID + channelId;
				File channelDir = new File(strChannelDir);
				if(!channelDir.exists()){
					channelDir.mkdir();
				}
				
				String filePath = strChannelDir + "/" + JsonChannelConstant.PREFIX_CHANNEL_ID + channelId + "_" + strFixDate + ".json";
				FileUtil.writeFile(jsonChannelInfo.toString().getBytes(), filePath);
				File file = new File(filePath);
				file.setLastModified(longDate);
				Log.d(TAG, "getChannelData 5");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}



	private void saveVideoResolutionGroup(String dirPath, JSONObject jsonContent) throws JSONException {
		Log.d(TAG, "getChannelData 13 - 1 ");
		JSONArray jsaElems = Vp9ParamUtil.getJSONArray(jsonContent, "src_list");
				
		if(jsaElems != null){
			Log.d(TAG, "getChannelData 13 - 2: " + jsaElems.length());
			for(int i = 0; i < jsaElems.length(); i++){
				
				JSONObject jsaElem = jsaElems.getJSONObject(i);
				Log.d(TAG, "getChannelData 13 - 3: " + i);
				if(jsaElem != null){
					Log.d(TAG, "getChannelData 13 - 4 ");
					String url = Vp9ParamUtil.getJsonString(jsaElem, JsonChannelConstant.URL_VIDEO, null);
					if(url != null && !"".equals(url.trim())){
						String newUrl = extrudeUrl(url);
						jsonContent.put(JsonChannelConstant.URL_VIDEO, newUrl);
						saveFile(dirPath, url, newUrl);
					}
					Log.d(TAG, "getChannelData 13 - 5 ");
					String src = Vp9ParamUtil.getJsonString(jsaElem, JsonChannelConstant.SRC_VIDEO, null);
					if(src != null && !"".equals(src.trim())){
						String newSrc = extrudeUrl(src);
						jsaElem.put(JsonChannelConstant.SRC_VIDEO, newSrc);
						saveFile(dirPath, src, newSrc);
					}
					Log.d(TAG, "getChannelData 13 - 6 ");
				}
			}
		}
		
	}



	private void handleJsonChildContent(String dirPath, JSONObject jsonChild) throws JSONException {
		Log.d(TAG, "getChannelData 13 -d - 1: ");
		String thumbIconUrl = Vp9ParamUtil.getJsonString(jsonChild, JsonChannelConstant.THUMB_ICON, null);
		if(thumbIconUrl != null && !"".equals(thumbIconUrl.trim())){
			String newThumbIconUrl = extrudeUrl(thumbIconUrl);
			jsonChild.put(JsonChannelConstant.THUMB_ICON, newThumbIconUrl);
//			saveIcon(dirPath, thumbIconUrl, newThumbIconUrl);
			saveFile(dirPath, thumbIconUrl, newThumbIconUrl);
		}
		Log.d(TAG, "getChannelData 13 -d - 2: ");
		String rightSub = Vp9ParamUtil.getJsonString(jsonChild, JsonChannelConstant.RIGHT_SUB, null);
		if(rightSub != null && !"".equals(rightSub.trim())){
			String newRightSub = extrudeUrl(rightSub);
			jsonChild.put(JsonChannelConstant.RIGHT_SUB, newRightSub);
			saveFile(dirPath, rightSub, newRightSub);
		}
		Log.d(TAG, "getChannelData 13 -d - 3: ");
		String leftSub = Vp9ParamUtil.getJsonString(jsonChild, JsonChannelConstant.LEFT_SUB, null);
		if(leftSub != null && !"".equals(leftSub.trim())){
			String newLeftSub = extrudeUrl(leftSub);
			jsonChild.put(JsonChannelConstant.LEFT_SUB, newLeftSub);
			saveFile(dirPath, leftSub, newLeftSub);
		}
		Log.d(TAG, "getChannelData 13 -d - 4: ");
		String url = Vp9ParamUtil.getJsonString(jsonChild, JsonChannelConstant.URL_VIDEO, null);
		Log.d(TAG, "truv-url: " + url);
		if(url != null && !"".equals(url.trim())){
			String newUrl = extrudeUrl(url);
			jsonChild.put(JsonChannelConstant.URL_VIDEO, newUrl);
			saveFile(dirPath, url, newUrl);
		}
		Log.d(TAG, "getChannelData 13 -d - 5: ");
		String src = Vp9ParamUtil.getJsonString(jsonChild, JsonChannelConstant.SRC_VIDEO, null);
		Log.d(TAG, "truv-src: " + src);
		if(src != null && !"".equals(src.trim())){
			String newSrc = extrudeUrl(src);
			jsonChild.put(JsonChannelConstant.SRC_VIDEO, newSrc);
			saveFile(dirPath, src, newSrc);
		}
		Log.d(TAG, "getChannelData 13 -d - 6: ");
		JSONArray jsArrSubtitles = Vp9ParamUtil.getJSONArray(jsonChild, JsonChannelConstant.SUBTITLES);
		if(jsArrSubtitles != null){
			if(jsArrSubtitles != null && jsArrSubtitles.length() > 0){
				for(int k = 0; k < jsArrSubtitles.length(); k++){
					JSONObject jsSubtitle = jsArrSubtitles.getJSONObject(k);
					if(jsSubtitle != null){
						String subUrl = ParamUtil.getJsonString(jsSubtitle, JsonChannelConstant.URL_VIDEO, null);

						if(subUrl != null && !"".equals(subUrl.trim())){
							String newUrl = extrudeUrl(subUrl);
							jsSubtitle.put(JsonChannelConstant.URL_VIDEO, newUrl);
							saveFile(dirPath, subUrl, newUrl);
						}
					}
				}
			}
		}
		Log.d(TAG, "getChannelData 13 -d - 7: ");
		saveVideoResolutionGroup(dirPath, jsonChild);
		Log.d(TAG, "getChannelData 13 -d - 8: ");
	}



	private void saveFile(String dirPath, String httpUrl, String filePath) {
		Log.d(TAG, "saveFile: url=" + httpUrl + ", filePath=" + filePath);
		File file = new File(filePath);
		if(!file.exists()){
			int index = filePath.lastIndexOf("/");
			if(index != -1){
				String tempNewIconUrl = filePath.substring(0, index);
				String[] dirPathElems = tempNewIconUrl.split("/");
				String strDirPath = dirPath;
				if(dirPathElems != null){
					for(int i = 0; i < dirPathElems.length; i++){
						strDirPath = strDirPath + "/" + dirPathElems[i];
						File tempFile = new File(strDirPath);
						if(!tempFile.exists()){
							tempFile.mkdir();
						}
					}
				}
				
			}
			
			saveFileToStore(httpUrl, dirPath + "/" + filePath);
		}
	}

	private void saveFileToStore(String httpUrl, String filePath) {
		Log.d(TAG, "truv-httpUrl: " + httpUrl);
		if(isModified(httpUrl, filePath)){
			return;
		}
		try {
			URL urlLoc = new URL(httpUrl);
			URLConnection connection = urlLoc.openConnection();
			long longDate = connection.getLastModified();
			connection.setConnectTimeout(10000);
			connection.setReadTimeout(15000);
			connection.connect();
			 
			// download the file
			InputStream input = new BufferedInputStream(urlLoc.openStream());
			FileUtil.writeFileByInputStream(input, filePath);
			File file = new File(filePath);
			file.setLastModified(longDate);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
//	private void saveFileToStore(String httpUrl, String filePath) {
//		try {
//			File file = new File(filePath);
//			if(file.exists()){
//				return;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		Log.d(TAG, "truv-httpUrl: " + httpUrl);
//		HttpResponse response;
//		HttpClient myClient = new DefaultHttpClient();
//		HttpPost myConnection = new HttpPost(httpUrl);
//		try {
//			response = myClient.execute(myConnection);
//			HttpEntity entity = response.getEntity();
//			if(entity != null){
//				InputStream is = response.getEntity().getContent();
//				if(is != null){
//					FileUtil.writeFileByInputStream(is, filePath);
//					Log.d(TAG, "truv-filePath - write: " + filePath);
//				}
//			}
//		    
//
////			content = EntityUtils.toByteArray(response.getEntity());
////			FileUtil.writeFile(content, filePath);
//			Log.d(TAG, "truv-filePath - write: " + filePath);
//		} catch (ClientProtocolException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}		
//	}



	private void saveIcon(String dirPath, String thumbIconUrl, String newThumbIconUrl) {
		File thumbIconFile = new File(thumbIconUrl);
		if(!thumbIconFile.exists()){
			int index = newThumbIconUrl.lastIndexOf("/");
			if(index != -1){
				String tempNewThumbIconUrl = newThumbIconUrl.substring(0, index);
				String[] dirPathElems = tempNewThumbIconUrl.split("/");
				String strIconDirPath = dirPath;
				if(dirPathElems != null){
					for(int i = 0; i < dirPathElems.length; i++){
						strIconDirPath = strIconDirPath + "/" + dirPathElems[i];
						File file = new File(strIconDirPath);
						if(!file.exists()){
							file.mkdir();
						}
					}
				}
				
			}
			
			saveIconToStore(thumbIconUrl, dirPath + "/" + newThumbIconUrl);
		}
	}



	private String extrudeUrl(String thumbIconUrl) {
		String newThumbIconUrl = thumbIconUrl.replace("http://", "").replace("https://", "");
		int index = newThumbIconUrl.indexOf("/");
		if(index != -1 && index + 1 < newThumbIconUrl.length()){
			newThumbIconUrl = newThumbIconUrl.substring(index + 1, newThumbIconUrl.length());
		}
		
		return newThumbIconUrl;
	}



	private void getChannelDescriptionList(String dirPath, String url, ArrayList<Integer> existChannelIdList) {
//		String content = "";
//		HttpResponse response;
//		HttpClient myClient = new DefaultHttpClient();
//		HttpPost myConnection = new HttpPost(url);
//		try {
//			response = myClient.execute(myConnection);
//			content = EntityUtils.toString(response.getEntity(), "UTF-8");
//
//		} catch (ClientProtocolException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		if (content == null) {
//			return;
//		}
		
		
		try {
			URL urlLoc = new URL(url);
			URLConnection connection = urlLoc.openConnection();
			connection.setConnectTimeout(10000);
			connection.setReadTimeout(15000);
			connection.connect();
			long longDate = connection.getLastModified();
			 
			// download the file
			InputStream input = new BufferedInputStream(urlLoc.openStream());
//			int available = input.available();
//			if(available <= 0){
//				return;
//			}
//			byte[] buffer = new byte[available];
//			input.read(buffer);
//			String content = new String(buffer, "UTF-8");
			
			String content = readFileToStringUsingStringWriter(input);
			if(content == null){
				return;
			}
			
			JSONObject jsonChannelInfo = new JSONObject(content);
			JSONObject groupChannel = Vp9ParamUtil.getJsonObject(jsonChannelInfo, JsonChannelConstant.GROUP_CHANNEL);
			if(groupChannel != null){
				Iterator keys = groupChannel.keys();
				if(keys != null){
					while (keys.hasNext()) {
						
						String key = (String) keys.next();
						
						if(key == null){
							continue;
						}
						
						JSONObject jsonGroup = groupChannel.getJSONObject(key);
						
						if(jsonGroup == null){
							continue;
						}
						
						JSONObject jsonChannelList = Vp9ParamUtil.getJsonObject(jsonGroup, JsonChannelConstant.CHANNEL_LIST);
						
						if(jsonChannelList == null){
							continue;
						}
						
						Iterator jsonChannelKeyIterator = jsonChannelList.keys();
						
						if(jsonChannelKeyIterator == null){
							continue;
						}
						
						ArrayList<String> jsonChannelKeyList = new ArrayList<String>();
						while (jsonChannelKeyIterator.hasNext()) {
							String jsonChannelKey = (String) jsonChannelKeyIterator.next();
							jsonChannelKeyList.add(jsonChannelKey);
						}
						for(String jsonChannelKey : jsonChannelKeyList){
							JSONObject jsonChannel = jsonChannelList.getJSONObject(jsonChannelKey);
							if(jsonChannel == null){
								continue;
							}
							String strChannelId = Vp9ParamUtil.getJsonString(jsonChannel, JsonChannelConstant.CHANNEL_ID, null);
							int intChannelId = Vp9ParamUtil.getValue(strChannelId, -1);
							if(!existChannelIdList.contains(intChannelId)){
								jsonChannelList.remove(jsonChannelKey);
								continue;
							}
							
							String strChannelIcon = Vp9ParamUtil.getJsonString(jsonChannel, JsonChannelConstant.CHANNEL_ICON, null);
							if(strChannelIcon == null){
								continue;
							}
							String fileName = getFileNameByUrl(strChannelIcon);
							if(fileName == null){
								continue;
							}
//							saveIconToStore(strChannelIcon, dirPath, fileName);
							String strChannelDir = dirPath + "/" + JsonChannelConstant.CHANNEL_ICON_DIR;
							File channelDir = new File(strChannelDir);
							if(!channelDir.exists()){
								channelDir.mkdir();
							}
//							saveFile(dirPath, strChannelIcon, strChannelDir + "/" + fileName);
							saveFileToStore(strChannelIcon, strChannelDir + "/" + fileName);
							String strNewChannelIcon = JsonChannelConstant.CHANNEL_ICON_DIR + "/" + fileName;
							jsonChannel.put(JsonChannelConstant.CHANNEL_ICON, strNewChannelIcon);
						}
					}
				}
				
				String filePath = dirPath + "/" + JsonChannelConstant.CHANNEL_DESCR_LIST_NAME;
				FileUtil.writeFile(jsonChannelInfo.toString().getBytes(), filePath);
				File file = new File(filePath);
				file.setLastModified(longDate);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
			
	}
	
    public static String readFileToStringUsingStringWriter(InputStream inputStream) throws IOException{
    	
    	try {
    		Writer writer = new StringWriter();
            char[] buffer = new char[1024];
            try {
                Reader reader = new BufferedReader(
                        new InputStreamReader(inputStream, "UTF-8"));
                int len;
                while ((len = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, len);
                }
                return writer.toString();
            } finally {
                writer.close();
                inputStream.close();
            }
            
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return null;
    }
	
	private void getChannelDescriptionList(String dirPath, String url) {
		String content = "";
		HttpResponse response;
		HttpClient myClient = new DefaultHttpClient();
		HttpPost myConnection = new HttpPost(url);
		try {
			response = myClient.execute(myConnection);
			content = EntityUtils.toString(response.getEntity(), "UTF-8");

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (content == null) {
			return;
		}

		try {
			JSONObject jsonChannelInfo = new JSONObject(content);
			JSONObject groupChannel = Vp9ParamUtil.getJsonObject(jsonChannelInfo, JsonChannelConstant.GROUP_CHANNEL);
			if(groupChannel != null){
				Iterator keys = groupChannel.keys();
				if(keys != null){
					while (keys.hasNext()) {
						
						String key = (String) keys.next();
						
						if(key == null){
							continue;
						}
						
						JSONObject jsonGroup = groupChannel.getJSONObject(key);
						
						if(jsonGroup == null){
							continue;
						}
						
						JSONObject jsonChannelList = Vp9ParamUtil.getJsonObject(jsonGroup, JsonChannelConstant.CHANNEL_LIST);
						
						if(jsonChannelList == null){
							continue;
						}
						
						Iterator jsonChannelKeyIterator = jsonChannelList.keys();
						
						if(jsonChannelKeyIterator == null){
							continue;
						}
						
						while (jsonChannelKeyIterator.hasNext()) {
							String jsonChannelKey = (String) jsonChannelKeyIterator.next();
							JSONObject jsonChannel = jsonChannelList.getJSONObject(jsonChannelKey);
							if(jsonChannel == null){
								continue;
							}
							String strChannelIcon = Vp9ParamUtil.getJsonString(jsonChannel, JsonChannelConstant.CHANNEL_ICON, null);
							if(strChannelIcon == null){
								continue;
							}
							String fileName = getFileNameByUrl(strChannelIcon);
							if(fileName == null){
								continue;
							}
//							saveIconToStore(strChannelIcon, dirPath, fileName);
							String strChannelDir = dirPath + "/" + JsonChannelConstant.CHANNEL_ICON_DIR;
							File channelDir = new File(strChannelDir);
							if(!channelDir.exists()){
								channelDir.mkdir();
							}
//							saveFile(dirPath, strChannelIcon, strChannelDir + "/" + fileName);
							saveFileToStore(strChannelIcon, strChannelDir + "/" + fileName);
							String strNewChannelIcon = JsonChannelConstant.CHANNEL_ICON_DIR + "/" + fileName;
							jsonChannel.put(JsonChannelConstant.CHANNEL_ICON, strNewChannelIcon);
						}
					}
				}
				
				FileUtil.writeFile(jsonChannelInfo.toString().getBytes(), dirPath + "/" + "ChannelDescriptionList.json");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
			
		
	}

	private void saveIconToStore(String strChannelIcon, String filePath) {
		try {
			URL url = new URL(strChannelIcon);
			BufferedInputStream bis = new BufferedInputStream(url.openConnection().getInputStream());
			int available = bis.available();
			byte[] byteArray = new byte[available];
			bis.read(byteArray);
			byte[] base64s = Base64.encode(byteArray, Base64.DEFAULT);
			FileUtil.writeFile(base64s, filePath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private void saveIconToStore(String strChannelIcon, String dirPath, String fileName) {
		try {
			URL url = new URL(strChannelIcon);
			BufferedInputStream bis = new BufferedInputStream(url.openConnection().getInputStream());
			int available = bis.available();
			byte[] byteArray = new byte[available];
			bis.read(byteArray);
			byte[] base64s = Base64.encode(byteArray, Base64.DEFAULT);
			String strChannelDir = dirPath + "/" + JsonChannelConstant.CHANNEL_ICON_DIR;
			File channelDir = new File(strChannelDir);
			if(!channelDir.exists()){
				channelDir.mkdir();
			}
			FileUtil.writeFile(base64s, strChannelDir + "/" + fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	private String getFileNameByUrl(String strChannelDir) {
		if(strChannelDir != null){
			int lastIndex = strChannelDir.lastIndexOf("/");
			if(lastIndex != -1 && lastIndex + 1 < strChannelDir.length()){
				String fileName = strChannelDir.substring(lastIndex + 1, strChannelDir.length());
				return fileName;
			}
		}
		return null;
	}



	protected void displayFile(File file) {
		if(file != null){
			if(file.isDirectory()){
				Log.d(TAG, "Directory: " + file.getAbsolutePath() + "/" + file.getName());
				File[] chilFiles = file.listFiles();
				if(chilFiles != null){
					for(File chilFile : chilFiles){
						displayFile(chilFile);
					}
				}
			}else{
				Log.d(TAG, "File: " + file.getAbsolutePath() + "/" + file.getName());
			}

		}
	}

	public String getCheduleUrl(String serverUrl, String channelId, String strDate) {
		String cheduleUrl = serverUrl + "/tivichannel/Channel_" + channelId + "_" + strDate + ".json";
		if(token != null && !"".equals(token)){
			cheduleUrl += "?token="  + token;
		}
		Log.d(TAG, "getCheduleUrl: " + cheduleUrl);
		return cheduleUrl;
	}
	
	public void setChannelId(String channelId){
		this.channelId = channelId;
	}

	public void setStrDate(String strDate){
		this.strDate = strDate;
	}


}
