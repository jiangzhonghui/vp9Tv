package com.vp9.plugin;

import java.io.File;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;

public class downloadmanager extends CordovaPlugin {
	private static final String LOG_TAG = "DownloadManager";
	private DownloadManager mgr = null;
	private SharedPreferences prefs;

	public downloadmanager() {
		// Activity activity = cordova.getActivity();
		// mgr = (DownloadManager)
		// activity.getSystemService(Context.DOWNLOAD_SERVICE);
		if (cordova != null) {

		}

	}

	BroadcastReceiver onNotificationClick = null;

	private void viewLog() {
		cordova.getActivity().startActivity(new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS));
	}

	public boolean execute(String action, CordovaArgs args, CallbackContext callbackContext) throws JSONException {
		Log.d(LOG_TAG, action);

		if (onNotificationClick == null) {
			onNotificationClick = new BroadcastReceiver() {
				public void onReceive(Context ctxt, Intent intent) {
					viewLog();
				}
			};

			cordova.getActivity().registerReceiver(onNotificationClick, new IntentFilter(DownloadManager.ACTION_NOTIFICATION_CLICKED));
		}

		initDownloadManager();

		if ("startDownload".equals(action)) {
			startDownload(args, callbackContext);
		} else if ("openDownload".equals(action)) {
			openDownload(args, callbackContext);
		} else if ("getDownload".equals(action)) {
			getDownload(args, callbackContext);
		} else if ("queryDownload".equals(action)) {
			JSONArray queryDownload = queryDownload(args, callbackContext);
			callbackContext.success(queryDownload);
		}
		return false;
	}

	private JSONArray queryDownload(CordovaArgs args, CallbackContext callbackContext) {

		JSONArray jsonArray = new JSONArray();
		Query query = new DownloadManager.Query();
//		query.setFilterByStatus(DownloadManager.STATUS_PENDING | DownloadManager.STATUS_RUNNING);
		Cursor c = mgr.query(query);
		while (c != null && c.moveToNext()) {
			String title = c.getString(c.getColumnIndex(DownloadManager.COLUMN_TITLE));
			String description = c.getString(c.getColumnIndex(DownloadManager.COLUMN_DESCRIPTION));
			String uri = c.getString(c.getColumnIndex(DownloadManager.COLUMN_URI));
			String local_uri = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
			int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
			String byte_downloaded = c.getString(c.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
			String size_bytes = c.getString(c.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));

			JSONObject jsonObj = new JSONObject();
			try {
				jsonObj.put("title", title);
				jsonObj.put("description", description);
				jsonObj.put("uri", uri);
				jsonObj.put("local_uri", local_uri);
				jsonObj.put("status", status);
				jsonObj.put("downloaded", byte_downloaded);
				jsonObj.put("totalBytes", size_bytes);
				jsonArray.put(jsonObj);

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return jsonArray;

	}

	private void getDownload(CordovaArgs args, CallbackContext callbackContext) {
		// TODO Auto-generated method stub

	}

	private void openDownload(CordovaArgs args, CallbackContext callbackContext) {
		Activity activity = cordova.getActivity();
		activity.startActivity(new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS));
	}
	
	private void checkDownloadByDescription(CordovaArgs args, CallbackContext callbackContext){ // kiểm tra phim đã download hay chưa  theo movieID
		
	}

	private void startDownload(CordovaArgs args, CallbackContext callbackContext) throws JSONException {
		boolean downloadFound = false;
		
		String directory = "", source = "", title = "", fileName = "", description = "";
		try {
			JSONObject json = args.getJSONObject(0);
			source = json.getString("source").trim();
			fileName = json.getString("fileName");
			title = json.has("title") ? json.getString("title") : fileName;
			description = json.has("description") ? json.getString("description") : title;
			File sdcard = Environment.getExternalStorageDirectory();
			// directory = sdcard.getAbsolutePath() +
			// json.getString("directory");
			directory = json.getString("directory");
		} catch (Exception ex) {
			callbackContext.error("error");
			return;
		}
		
		JSONArray queriesDownload = queryDownload(args, callbackContext);
		for (int i = 0; i < queriesDownload.length(); i++) {
			JSONObject queryDownload = queriesDownload.getJSONObject(i);
			String _uri = queryDownload.getString("uri").trim();
			int _status = queryDownload.getInt("status");
			String _description= queryDownload.getString("description");
			
			if (_uri.equals(source) && (_status == DownloadManager.STATUS_PENDING || _status == DownloadManager.STATUS_RUNNING)) {
				downloadFound = true;
			}
		}
		
		if (downloadFound) {
			callbackContext.success("đang download");
			return;
		}
		
		Log.d(LOG_TAG, "download " + source + " to " + directory);
		Uri uri = Uri.parse(source);
		Environment.getExternalStoragePublicDirectory(directory).mkdirs();
		Log.e(LOG_TAG, "=================" + directory);
		long enqueue = mgr.enqueue(new DownloadManager.Request(uri).
				setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
				.setAllowedOverRoaming(false)
				.setTitle(title)
				.setDescription(description)
				.setDestinationInExternalPublicDir(directory, fileName));
		
		
		callbackContext.success(directory);
	}

	public void onDestroy() {
		offNotificationClick();
	}

	public void onReset() {
		offNotificationClick();
	}

	private void offNotificationClick() {
		if (this.onNotificationClick != null) {
			try {
				cordova.getActivity().unregisterReceiver(this.onNotificationClick);
				this.onNotificationClick = null;
			} catch (Exception e) {
				Log.e(LOG_TAG, e.getMessage());
			}
		}
	}

	private void initDownloadManager() {
		Activity activity = cordova.getActivity();
		if (mgr == null) {
			mgr = (DownloadManager) activity.getSystemService(Context.DOWNLOAD_SERVICE);
		}

		if (prefs == null) {
			prefs = PreferenceManager.getDefaultSharedPreferences(activity);
		}

	}

}