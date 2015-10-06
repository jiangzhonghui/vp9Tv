package com.vp9.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;

import android.app.Activity;
import android.content.res.AssetManager;
import android.util.Log;

public class FileUtil {

	private static String TAG = "FileUtil";
	
	public static synchronized void updateVitamioLibraryFiles(Activity activity) {
		TAG = activity.getClass().getSimpleName();
		Log.d(TAG, "loading library of vitamio");
		boolean isExistLibDir = true;
		String libsDirPath = activity.getFilesDir().getParent() + "/lib";
		File libsDir = new File(libsDirPath);
		if (!libsDir.exists()) {
			isExistLibDir = false;
			boolean isMkdir = libsDir.mkdir();
			if (!isMkdir) {
				return;
			}
		}
		
		AssetManager assetManager = activity.getResources().getAssets();
		try {
			String libDir = "VLibs/armeabi";
			String[] proxyLibNames = assetManager.list(libDir);
			Log.d(TAG, "A1 proxyLibNames: " + proxyLibNames.length);
			if (proxyLibNames != null && proxyLibNames.length > 0) {
				Log.d(TAG, "A2");
				for (int i = 0; i < proxyLibNames.length; i++) {
					Log.d(TAG, "proxyLibName: " + proxyLibNames[i]);
					if (!isExistLibDir || !checkExistFile(libsDirPath + "/" + proxyLibNames[i])) {
						InputStream inputStream = assetManager.open(libDir + "/" + proxyLibNames[i]);
						if (inputStream != null) {
							String filePath = libsDirPath + "/" + proxyLibNames[i];
							writeFile(inputStream, filePath);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public static synchronized void updateLibraryFiles(Activity activity) {
		Log.d(TAG, "loading library proxy: " + (new Date()).toString());
		TAG = activity.getClass().getSimpleName();
		boolean isExistLibDir = true;
		String libsDirPath = activity.getFilesDir().getParent() + "/libs";
		File libsDir = new File(libsDirPath);
		if (!libsDir.exists()) {
			isExistLibDir = false;
			boolean isMkdir = libsDir.mkdir();
			if (!isMkdir) {
				return;
			}
		}

		AssetManager assetManager = activity.getResources().getAssets();
		try {
			String[] proxyLibNames = assetManager.list("proxyLibs");
			Log.d(TAG, "A1 proxyLibNames: " + proxyLibNames.length);
			if (proxyLibNames != null && proxyLibNames.length > 0) {
				Log.d(TAG, "A2");
				for (int i = 0; i < proxyLibNames.length; i++) {
					Log.d(TAG, "proxyLibName: " + proxyLibNames[i]);
					if (!isExistLibDir || !checkExistFile(libsDirPath + "/" + proxyLibNames[i])) {
						InputStream inputStream = assetManager.open("proxyLibs" + "/" + proxyLibNames[i]);
						if (inputStream != null) {
							String filePath = libsDirPath + "/" + proxyLibNames[i];
							writeFile(inputStream, filePath);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
//		// update logFile
//		File lock = new File(libsDirPath + ".lock");
//		if (lock.exists()){
//			lock.delete();
//		}
		Log.d(TAG, "finish load library proxy: " + (new Date()).toString());	      
	}

	private static synchronized boolean checkExistFile(String fileName) {
		boolean isExist = false;
		try {
			File file = new File(fileName);
			if (file.exists()) {
				isExist = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return isExist;

	}

	private static synchronized boolean writeFile(InputStream fis, String fileName) {
		OutputStreamWriter osw = null;
		try {
			// InputStreamReader isr = new InputStreamReader(fis);
			int available = fis.available();
			byte[] buffer = new byte[available];
			fis.read(buffer);
			FileOutputStream fOut = new FileOutputStream(new File(fileName));
			// osw = new OutputStreamWriter(fOut);
			fOut.write(buffer);
			fOut.flush();
			fOut.close();
			// while ((i = isr.read()) != -1) {
			// c = (char) i;
			// osw.write(c);
			// }
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (osw != null) {
					osw.flush();
					osw.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return true;
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
    
	public static void writeFileByInputStream(InputStream is, String filePath) {
		FileOutputStream fOut = null;
		try {
		    fOut = new FileOutputStream(new File(filePath));
		    byte[] buffer = new byte[8 * 1024];
		    int bytesRead;
		    while ((bytesRead = is.read(buffer)) != -1) {
		    	fOut.write(buffer, 0, bytesRead);
		    }
		    is.close();
		    fOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		    try {
				is.close();
			    if(fOut != null){
			    	fOut.close();
			    }
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

	}

	public static boolean writeFile(byte[] content, String fileName) {
		FileOutputStream fOut =  null;
		try {
			fOut = new FileOutputStream(new File(fileName));
			// osw = new OutputStreamWriter(fOut);
			fOut.write(content);
			fOut.flush();
			fOut.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fOut != null) {
					fOut.flush();
					fOut.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return true;
	}
}
