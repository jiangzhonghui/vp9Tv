package vp9Service;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.vp9.util.DowloadChannelData;

public class DownloadDataChannel extends IntentService {

	private String TAG = "DownloadDataChannel";
	
	private ArrayList<String> channelIdList;
	
	private ConcurrentHashMap<String, DownloadInfo> downloadInfoMap;
	private ConcurrentHashMap<String, DownloadInfoRunnable> downloadInfoRunnableMap;
	
	public DownloadDataChannel(String name) {
		super(name);
		Log.d(TAG, "Create DownloadDataChannel 1");
		setIntentRedelivery(true);
		channelIdList = new ArrayList<String>();
		downloadInfoMap = new ConcurrentHashMap<String, DownloadInfo>();
		downloadInfoRunnableMap = new ConcurrentHashMap<String, DownloadDataChannel.DownloadInfoRunnable>();
	}
	
	public DownloadDataChannel() {
		super(DownloadDataChannel.class.getName());
		setIntentRedelivery(true);
		Log.d(TAG, "Create DownloadDataChannel 2");
		channelIdList = new ArrayList<String>();
		downloadInfoMap = new ConcurrentHashMap<String, DownloadInfo>();
		downloadInfoRunnableMap = new ConcurrentHashMap<String, DownloadDataChannel.DownloadInfoRunnable>();
	}
	
	public void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "ondestroy service");
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		super.onStartCommand(intent, flags, startId);
		return START_STICKY;
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		Log.d(TAG, "DownloadDataChannel start");
//		new DoBackgroundTask().execute();
		if(intent == null){
			return;
		}
		Bundle serInfoCaller = /*new Bundle()*/ intent.getBundleExtra("downloadInfo");
		if(serInfoCaller != null){
			String cookie = serInfoCaller.getString("cookie", "");
			String token = serInfoCaller.getString("token", "");
			ArrayList<String> requestChannelIdList = /*new ArrayList<String>()*/serInfoCaller.getStringArrayList("channelIdList");
//			requestChannelIdList.add("803");
//			requestChannelIdList.add("804");
			String serverUrl = serInfoCaller.getString("serverUrl", "http://vi.vp9.tv/");
			String directoryPath = serInfoCaller.getString("directoryPath", "/mnt/usb_storage/USB_DISK0/udisk0/");
			DownloadInfo downloadInfo = getDownloadInfo(serverUrl);
			if(downloadInfo == null){
				downloadInfo = new DownloadInfo();
				downloadInfo.setCookie(cookie);
				downloadInfo.setToken(token);
				downloadInfo.setRequestChannelIdList(requestChannelIdList);
				downloadInfo.setServerUrl(serverUrl);
				downloadInfo.setDirectoryPath(directoryPath);
				downloadInfo.setNew(true);
				downloadInfoMap.put(serverUrl, downloadInfo);
				DownloadInfoRunnable downloadInfoRunnable = new DownloadInfoRunnable(serverUrl, true);
				downloadInfoRunnableMap.put(serverUrl, downloadInfoRunnable);
				new Thread(downloadInfoRunnable).start();
			}else {
				boolean isNew = false;
				boolean isNewWork = false;
				if(!cookie.equals(downloadInfo.getCookie())){
					downloadInfo.setCookie(cookie);
					isNew = true;
				}
				if(!token.equals(downloadInfo.getToken())){
					downloadInfo.setToken(token);
					isNew = true;
				}
				if(!directoryPath.equals(downloadInfo.getDirectoryPath())){
					downloadInfo.setDirectoryPath(directoryPath);
					isNew = true;
					isNewWork = true;
				}
				boolean isAdd = downloadInfo.addChannelIdList(requestChannelIdList);
				if(isNewWork || isAdd){
					downloadInfo.setNew(true);
				}
				if(isNew){
					DownloadInfoRunnable downloadInfoRunnable = downloadInfoRunnableMap.get(serverUrl);
					downloadInfoRunnable.setWork(false);
					DownloadInfoRunnable newDownloadInfoRuna= new DownloadInfoRunnable(serverUrl, true);
					downloadInfoRunnableMap.put(serverUrl, newDownloadInfoRuna);
					new Thread(newDownloadInfoRuna).start();
				}
			}
		}
		
	}
	
	public DownloadInfo getDownloadInfo(String serverUrl){
		DownloadInfo downloadInfo = downloadInfoMap.get(serverUrl);
		return downloadInfo;
	}
	
	
	class DownloadInfoRunnable implements Runnable{
		
		private String serverUrl;
		private boolean isWork;

		public DownloadInfoRunnable(String serverUrl, boolean isWork){
			Log.d("DownloadInfoRunnable", "init DownloadInfoRunnable");
			this.setServerUrl(serverUrl);
			this.setWork(isWork);
		}

		@Override
		public void run() {
			while (isWork) {
				if(downloadInfoMap != null){
					DownloadInfo downloadInfo = downloadInfoMap.get(this.serverUrl);
					
					if(downloadInfo != null){
						Log.d(TAG, "DownloadDataTask");
						String token = downloadInfo.getToken();
						ArrayList<String> requestChannelIdList = downloadInfo.getRequestChannelIdList();
						String directoryPath = downloadInfo.getDirectoryPath();
						String vp9OfflineDirPath = directoryPath + "/vp9_offline_data";
						Date currentDate = new Date();
						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
						String[] strDates = new String[3];
						strDates[0] = dateFormat.format(currentDate.getTime());

						Calendar calendar = Calendar.getInstance();
						calendar.setTime(currentDate);
						
						calendar.add(Calendar.DATE, 1);
						strDates[1] = dateFormat.format(calendar.getTime());
						calendar.add(Calendar.DATE, -2);
						strDates[2] = dateFormat.format(calendar.getTime());

						DowloadChannelData dowloadChannelData = new DowloadChannelData(
								serverUrl, requestChannelIdList, token);
						if(downloadInfo.isNew() || dowloadChannelData.isNewChannelDescriptionFromServer(vp9OfflineDirPath)){
							dowloadChannelData.dowloadChannelDescription(requestChannelIdList, vp9OfflineDirPath);
							downloadInfo.setNew(false);
						}
						if(requestChannelIdList != null){
							Log.d(TAG, "strDate: " + Arrays.toString(strDates));
							for (String strDate : strDates) {
								Log.d(TAG, "strDate : " + strDate);
								Log.d(TAG, "requestChannelIdList : " + Arrays.toString(requestChannelIdList.toArray()));
								for(String channelId: requestChannelIdList){
									dowloadChannelData.setChannelId(channelId);
									dowloadChannelData.setStrDate(strDate);
									dowloadChannelData.dowloadChannelInfo(vp9OfflineDirPath);
								}
							}
						}
						
					}
				}
				sleep(30000);
			}
		}

			
		public String getServerUrl() {
			return serverUrl;
		}

		public void setServerUrl(String serverUrl) {
			this.serverUrl = serverUrl;
		}

		public boolean isWork() {
			return isWork;
		}

		public void setWork(boolean isWork) {
			this.isWork = isWork;
		}
		
	}
	class DownloadInfo{
		
		private String cookie;
		private String token;
		private ArrayList<String> requestChannelIdList;
		private String serverUrl;
		private String directoryPath;
		private boolean isNew;
		
		public String getCookie() {
			return cookie;
		}
		
		public boolean addChannelIdList(ArrayList<String> newChannelIdList) {
			boolean isNew = false;
			if(newChannelIdList != null){
				if(requestChannelIdList == null){
					requestChannelIdList = newChannelIdList;
					isNew = true;
				}else{
					for(String newChannelId : newChannelIdList){
						if(!requestChannelIdList.contains(newChannelId)){
							requestChannelIdList.add(newChannelId);
							isNew = true;
						}
					}
				}
			}
			return isNew;
		}

		public void setCookie(String cookie) {
			this.cookie = cookie;
		}
		
		public String getToken() {
			return token;
		}
		
		public void setToken(String token) {
			this.token = token;
		}
		
		public ArrayList<String> getRequestChannelIdList() {
			return requestChannelIdList;
		}
		
		public void setRequestChannelIdList(ArrayList<String> requestChannelIdList) {
			this.requestChannelIdList = requestChannelIdList;
		}
		
		public String getDirectoryPath() {
			return directoryPath;
		}
		
		public void setDirectoryPath(String directoryPath) {
			this.directoryPath = directoryPath;
		}
		
		public String getServerUrl() {
			return serverUrl;
		}
		
		public void setServerUrl(String serverUrl) {
			this.serverUrl = serverUrl;
		}

		public boolean isNew() {
			return isNew;
		}

		public void setNew(boolean isNew) {
			this.isNew = isNew;
		}
		
	}

}
