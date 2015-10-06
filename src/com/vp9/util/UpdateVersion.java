package com.vp9.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Pattern;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;

public class UpdateVersion {
	Context context;

	String urlVersion;
	String packageName;
	String appName;
	Handler parentHandler;

	boolean updated;

	public UpdateVersion(Context context, String urlVersion, String packageName, String appName, Handler parantHandler) {
		this.context = context;
		this.urlVersion = urlVersion;
		this.packageName = packageName;
		this.appName = appName;
		this.parentHandler = parantHandler;

		updated = false;

		getVersionFromServer();
	}

	private final Handler handler = new Handler() {

		public void handleMessage(Message msg) {

			String aResponse = msg.getData().getString("message", "");

			if ((null != aResponse) && aResponse != "") {

				final Version versionFromServer = Version.parseData(aResponse);
				String version = UpdateVersion.this.getVersion();
				int signature = UpdateVersion.this.getSignature();

				String url = versionFromServer.getUrl();
				if (signature == versionFromServer.getSignatureOld()) {
					url = versionFromServer.getUrl();
				} else if(versionFromServer.getNewUrl() != null || versionFromServer.getNewUrl() != ""){
					url = versionFromServer.getNewUrl();
				}

//				if (versionFromServer.getVersion() != "" && (Float.parseFloat(version) < Float.parseFloat(versionFromServer.getVersion()))) {
				if (versionFromServer.getVersion() != "" && Version.compare(version, versionFromServer.getVersion()) < 0) {
					final String downloadUrl = url;

					AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
					builder1.setMessage(Vp9Contant.MSG_CONTENT_UPDATE_VERSION_1 + UpdateVersion.this.appName + Vp9Contant.MSG_CONTENT_UPDATE_VERSION_2);
					builder1.setTitle(Vp9Contant.MSG_TITLE_UPDATE_VERSION);
					builder1.setCancelable(true);
					builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {

//							File sdcard = Environment.getExternalStorageDirectory();
//							deleteDirectory(sdcard);
							String strSize = versionFromServer.getSize();
							long size = 0;
							try {
								size = Long.parseLong(strSize);
							} catch (Exception e) {
								// TODO: handle exception
							}
							
							if (size > DownloadFile.getAvailableSpaceInBytes()) {
								long value = size - DownloadFile.getAvailableSpaceInBytes() + 15;
								Builder builder = DownloadFile.displayQuitDownloadDiaglog(context, "SDCard thiếu " + value/1024/1024 + " MB để cập nhật bản mới");
								builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
										dialog.cancel();
										
										Message msgObj = parentHandler.obtainMessage();
										Bundle b = new Bundle();
										b.putString("updateSuccess", "");
										msgObj.setData(b);
										parentHandler.sendMessage(msgObj);
										
										UpdateVersion.this.updated = true;
									}
								});
								
								AlertDialog alertDialog = builder.create();
								alertDialog.setCancelable(false);
								alertDialog.setCanceledOnTouchOutside(false);
								alertDialog.show();
							}else {
								String[] strUrls = downloadUrl.split("/");
								if (strUrls != null && strUrls.length > 0 && strUrls[strUrls.length - 1] != null && strUrls[strUrls.length - 1].endsWith(".apk")) {
									String appName = strUrls[strUrls.length - 1];
									try {
										final DownloadFile downloadFile = new DownloadFile(UpdateVersion.this.context, downloadUrl, appName, UpdateVersion.this);
										downloadFile.execute();
									} catch (Exception e) {
	
									}
								}
							}
							dialog.cancel();
						}
					});
					builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							Message msgObj = parentHandler.obtainMessage();
							Bundle b = new Bundle();
							b.putString("updateSuccess", "");
							msgObj.setData(b);
							parentHandler.sendMessage(msgObj);
							
							UpdateVersion.this.updated = true;
							dialog.cancel();
						}
					});

					AlertDialog alert11 = builder1.create();
					alert11.setCancelable(false);
					alert11.setCanceledOnTouchOutside(false);
					alert11.show();
				} else {
					Message msgObj = parentHandler.obtainMessage();
					Bundle b = new Bundle();
					b.putString("updateSuccess", "");
					msgObj.setData(b);
					parentHandler.sendMessage(msgObj);
					
					UpdateVersion.this.updated = true;
				}
			}else{
				Message msgObj = parentHandler.obtainMessage();
				Bundle b = new Bundle();
				b.putString("updateSuccess", "");
				msgObj.setData(b);
				parentHandler.sendMessage(msgObj);
				
				UpdateVersion.this.updated = true;
			}
		}
	};
	
	public String getPackageName() {
		return packageName;
	}

	public boolean isUpdated() {
		return updated;
	}

	public void setUpdated(boolean updated) {
		this.updated = updated;
	}

	public static void deleteDirectory(File path) {
		// TODO Auto-generated method stub
		if (path.exists()) {
			File[] files = path.listFiles();
			for (int i = 0; i < files.length; i++) {

				int indexOf = files[i].getName().toLowerCase().indexOf("vp9");

				if (files[i].isDirectory() && indexOf < 0 && files[i].canWrite()) {

					deleteDirectory(files[i]);
				} else if (indexOf < 0 && files[i].canWrite()) {

					files[i].delete();
				}
			}
		}
	}

	private void getVersionFromServer() {

		Thread thrVersionFromServer = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					StringBuilder builder = new StringBuilder();
					// URL url = new URL(Config.URL_VERSION_LAUNCHER);
					URL url = new URL(UpdateVersion.this.urlVersion);
					HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
					urlConnection.setConnectTimeout(5000);
					urlConnection.setReadTimeout(5000);
					String data = "";
					try {
						InputStream in = new BufferedInputStream(urlConnection.getInputStream());
						BufferedReader reader = new BufferedReader(new InputStreamReader(in));
						String line;
						while ((line = reader.readLine()) != null) {
							builder.append(line + "\n");
						}
						data = builder.toString();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						urlConnection.disconnect();
					}

					
					threadMsg(data);

				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});
		thrVersionFromServer.setName("thrVersionFromServer_2");
		thrVersionFromServer.start();
		

	}

	private void threadMsg(String msg) {

		if (!msg.equals(null)) {
			Message msgObj = handler.obtainMessage();
			Bundle b = new Bundle();
			b.putString("message", msg);
			msgObj.setData(b);
			handler.sendMessage(msgObj);
		}
	}

	public String getVersion() {
		// String packageName = Config.PACKAGE_LAUNCHER;
		String packageName = this.packageName;
		PackageManager manager = this.context.getPackageManager();
		
		return getVersionStatic(packageName, manager);
	}
	
	public static String getVersionStatic(String packageName, PackageManager manager) {
		// String packageName = Config.PACKAGE_LAUNCHER;
		
		String versionName = "0";
		try {
			PackageInfo info = manager.getPackageInfo(packageName, 0);
			versionName = info.versionName;
		} catch (NameNotFoundException e) {

		}
		return versionName;
	}

	private int getSignature() {

		Signature[] sigs;
		try {
			// sigs =
			// this.context.getPackageManager().getPackageInfo(Config.PACKAGE_LAUNCHER,
			// PackageManager.GET_SIGNATURES).signatures;
			sigs = this.context.getPackageManager().getPackageInfo(this.packageName, PackageManager.GET_SIGNATURES).signatures;
			for (Signature sig : sigs) {
				return sig.hashCode();
			}

		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;

	}
	
}

class Version {
	String version;
	int important;
	String url;
	String size;
	String newUrl;
	int signatureOld;
	int signatureNew;

	public Version() {
		// TODO Auto-generated constructor stub
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public int getImportant() {
		return important;
	}

	public void setImportant(int important) {
		this.important = important;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getSignatureOld() {
		return signatureOld;
	}

	public void setSignatureOld(int signatureOld) {
		this.signatureOld = signatureOld;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getNewUrl() {
		return newUrl;
	}

	public void setNewUrl(String newUrl) {
		this.newUrl = newUrl;
	}

	public int getSignatureNew() {
		return signatureNew;
	}

	public void setSignatureNew(int signatureNew) {
		this.signatureNew = signatureNew;
	}

	public static Version parseData(String data) {
		String[] lines = data.split("\n");

		Version versionFromServer = new Version();
		for (int i = 0; i < lines.length; i++) {
			String line = lines[i];

			if (line != "" && !line.substring(0, 1).trim().equals("#")) {

				String[] split = line.split("=");
				if (split[0].indexOf("version") > -1) {
					versionFromServer.setVersion(split[1].trim());
				} else if (split[0].indexOf("important") > -1) {
					versionFromServer.setImportant(Integer.parseInt(split[1].trim()));
				} else if (split[0].indexOf("url") > -1) {
					versionFromServer.setUrl(split[1].trim());
				} else if (split[0].indexOf("size") > -1) {
					versionFromServer.setSize(split[1].trim());
				} else if (split[0].indexOf("newUrl") > -1) {
					versionFromServer.setNewUrl(split[1].trim());
				} else if (split[0].indexOf("oldSignature") > -1) {
					versionFromServer.setSignatureOld(Integer.parseInt(split[1].trim()));
				} else if (split[0].indexOf("newSignature") > -1) {
					versionFromServer.setSignatureNew(Integer.parseInt(split[1].trim()));
				}
			}
		}
		return versionFromServer;
	}
	
	public static int compare(String v1, String v2) {
		String s1 = normalisedVersion(v1);
		String s2 = normalisedVersion(v2);
		int cmp = s1.compareTo(s2);
		return cmp;
//		System.out.printf("%s %n", cmp);
//		String cmpStr = cmp < 0 ? "<" : cmp > 0 ? ">" : "==";
//		System.out.printf("'%s' %s '%s'%n", v1, cmpStr, v2);
	}

	public static String normalisedVersion(String version) {
		return normalisedVersion(version, ".", 4);
	}

	public static String normalisedVersion(String version, String sep, int maxWidth) {
		String[] split = Pattern.compile(sep, Pattern.LITERAL).split(version);
//		StringBuilder sb = new StringBuilder();
		String sb = "";
		for (String s : split) {
//			sb.append(String.format("%" + maxWidth + 's', s));
			sb += " " + s.trim();
		}
		return sb.toString();
	}

	@Override
	public String toString() {
		return "version = " + this.version + "\n important = " + this.important + "\n url: " + this.url + "\n newUrl: " + this.newUrl;
	}

}

class DownloadFile extends AsyncTask<String, Integer, String> {

	ProgressDialog mProgressDialog;
	private String downloadUrl;
	private String appName;
	Context context;
	UpdateVersion objUpdateVersion;

	public boolean isInstall;

	// public boolean isFinish;

	public DownloadFile(Context context, String downloadUrl, String appName, UpdateVersion objUpdateVersion) {
		this.context = context;
		this.downloadUrl = downloadUrl;
		this.appName = appName;
		this.isInstall = true;
		this.objUpdateVersion = objUpdateVersion;
		// this.isFinish = false;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		// Create ProgressBar
		mProgressDialog = new ProgressDialog(this.context);
		// Set your ProgressBar Title
		mProgressDialog.setTitle("Downloads");

		// Set your ProgressBar Message
		mProgressDialog.setMessage("Downloading app for install, Please Wait!");
		mProgressDialog.setIndeterminate(false);
		mProgressDialog.setMax(100);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		// Show ProgressBar
		mProgressDialog.setCancelable(false);
		// mProgressDialog.setCanceledOnTouchOutside(false);
		mProgressDialog.show();
	}

	@Override
	protected String doInBackground(String... sUrl) {
		HttpURLConnection c = null;
		try {

			URL url = new URL(downloadUrl);
			c = (HttpURLConnection) url.openConnection();
			c.setRequestMethod("GET");
			c.setDoOutput(true);
			c.connect();

			String PATH = "/mnt/sdcard/Download/";

			File file = new File(PATH);
			file.mkdirs();
			File outputFile = new File(file, appName);
			if (outputFile.exists()) {
				outputFile.delete();
			}
			FileOutputStream fos = new FileOutputStream(outputFile);

			InputStream is = c.getInputStream();

			int fileLength = c.getContentLength();

			long availableSpaceInBytes = getAvailableSpaceInBytes();

			if (fileLength >= availableSpaceInBytes) {
				// displayQuitDownloadDiaglog();
				isInstall = false;
			} else {
				byte[] buffer = new byte[1024];
				long total = 0;
				int count = 0;
				while ((count = is.read(buffer)) != -1) {
					total += count;
					// Publish the progress
					publishProgress((int) (total * 100 / fileLength));
					fos.write(buffer, 0, count);
				}
				fos.close();
				is.close();
			}
			c.disconnect();
			
		} catch (Exception e) {

			e.printStackTrace();
		}finally{
			if(c != null){
				c.disconnect();
			}
		}
		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		mProgressDialog.dismiss();
		if (isInstall) {
			// Message msgObj = handler.obtainMessage();
			// Bundle b = new Bundle();
			// b.putString("message", msg);
			// msgObj.setData(b);
			// handler.sendMessage(msgObj);

			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(new File("/mnt/sdcard/Download/" + appName)), "application/vnd.android.package-archive");
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // without this
															// flag android
															// returned a
															// intent error!
			context.startActivity(intent);
			
			Message msgObj = objUpdateVersion.parentHandler.obtainMessage();
			Bundle b = new Bundle();
			b.putString("updateSuccess", "");
			msgObj.setData(b);
			objUpdateVersion.parentHandler.sendMessage(msgObj);

		} else {
			Builder builder = displayQuitDownloadDiaglog(context, "");
			builder.create().show();
		}

		// isFinish = true;
	}

	@Override
	protected void onProgressUpdate(Integer... progress) {
		super.onProgressUpdate(progress);
		// Update the ProgressBar
		mProgressDialog.setProgress(progress[0]);

	}

	public static long getAvailableSpaceInBytes() {
		long availableSpace = -1L;
		// StatFs stat = new
		// StatFs(Environment.getExternalStorageDirectory().getPath());
		StatFs stat = new StatFs("/mnt/sdcard");
		availableSpace = (long) stat.getAvailableBlocks() * (long) stat.getBlockSize();

		return availableSpace;
	}

	public static Builder displayQuitDownloadDiaglog(Context context, String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("Thông báo");
		builder.setMessage("SDcard không đủ bộ nhớ. " + message );
		builder.setCancelable(false);

		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.dismiss();
			}
		});

//		AlertDialog _alert = builder.create();
//		_alert.show();
		
		return builder;

	}

}
