package com.vp9.util;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

import android.util.Log;



public class Config {
	public static final String PACKAGE_TVAPP = "com.vp9.tv";
	public static final String ACTIVITY_TVAPP = ".MainActivity";
	public static final String URL_TVAPP = "http://tv.vp9.tv";
	public static String VERSION = "1.83.1";
	public static String PLAYERS = "[{\"player\" : \"Native\",\"version\" : \"1.0\"}, {\"player\" : \"Special\",\"version\" : \"1.0\"}, {\"player\" : \"Proxy\",\"version\" : \"1.0\"}]";
	
	public static String PACKAGE_LAUNCHER = "com.vp9.launcher";
	public static String PACKAGE_PROXY = "vp9.videoproxy";	

	public static String URL_VERSION_VP9 = "http://tv.vp9.tv/version/version.txt";
	public static String URL_VERSION_LAUNCHER = "http://tv.vp9.tv/version/launcherversion.txt";
	public static String URL_VERSION_PROXY = "http://tv.vp9.tv/version/version_proxy.txt";
	
	public static String APP_VP9 = "VP9 TV";
	public static String APP_LAUNCHER = "VP9 Launcher";
	public static String APP_PROXY = "Proxy";
	
	
	public static String URL_ALL_VERSION = "http://vtvnet.tv/version/all_version.txt";
	
	public static Map<Integer, String> ERROR_CODE = new HashMap<Integer, String>();
	
	static{
		ERROR_CODE.put(0, "");
		ERROR_CODE.put(1, "nguồn bị gián đoạn");
		ERROR_CODE.put(2 , "nguồn không tồn tại");
		ERROR_CODE.put(4 , "không thể kết nối tới server");
		ERROR_CODE.put(8 , "kết nối mạng gián đoạn");
		ERROR_CODE.put(16 , "mạng chậm, dữ liệu gián đoạn");
		ERROR_CODE.put(32 , "chương trình bị gián đoạn");
		ERROR_CODE.put(64 , "khởi tạo socket lỗi");
		ERROR_CODE.put(128 , "phân giải địa chỉ nguồn lỗi");
		ERROR_CODE.put(256 , "không thể giải mã dữ liệu");
	}
	
}