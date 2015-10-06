package com.vp9.plugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class Connection extends CordovaPlugin {
	private String intToIP(int intIP) {
		byte[] quads = new byte[4];
		for (int k = 0; k < 4; k++)
			quads[k] = (byte) ((intIP >> k * 8) & 0xFF);

		try {
			return InetAddress.getByAddress(quads).toString().substring(1);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return "";
		}
	}

	@Override
	public boolean execute(String action, JSONArray args,
			CallbackContext callbackContext) throws JSONException {
		if (action.equals("info")) {
			handleInfo(callbackContext);
		}else if (action.equals("ping")) {
			JSONObject jsonObject = args.getJSONObject(0);
			String urlString = jsonObject.get("url").toString();
			Integer loopNumber = (Integer) jsonObject.get("loop");
			Integer sizeNumber = (Integer) jsonObject.get("size");

			handlePing(urlString, loopNumber, callbackContext);

		}else if (action.equals("resolveHost")) {
			JSONObject jsonObject = args.getJSONObject(0);
			String urlString = jsonObject.get("url").toString();
			handleResolveHost(urlString, callbackContext);
		}else if(action.equals("getlocalip")){
			getLocalIP(callbackContext);
		}else if(action.equals("getWifiSignal")){
			JSONObject json = new JSONObject();
			int numLevels = 5;
			WifiManager wifimanager = (WifiManager) cordova.getActivity().getSystemService(Context.WIFI_SERVICE);
			if (wifimanager.isWifiEnabled()) {
				WifiInfo wifiInfo = wifimanager.getConnectionInfo();
				int linkSpeed = wifiInfo.getLinkSpeed();
				int level = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), numLevels);
				
				json.put("linkspeed", linkSpeed);
				json.put("level", level);
				json.put("numlevels", numLevels);
				
				callbackContext.success(json);
			}else{
				callbackContext.success(json);
			}
		}
		
		return true;
	}

	private void handleInfo(final CallbackContext callbackContext)
			throws JSONException {

		cordova.getThreadPool().execute(new Runnable() {

			@Override
			public void run() {

//				 
				JSONArray pingResult = new JSONArray();
				String pingCmd = "getprop";

				Runtime r = Runtime.getRuntime();
				Process p;
				try {
					p = r.exec(pingCmd);
					BufferedReader in = new BufferedReader(
							new InputStreamReader(p.getInputStream()));
					String inputLine;
					int i = 0;
					while ((inputLine = in.readLine()) != null) {
						pingResult.put(inputLine);
					}
					callbackContext.success(pingResult.toString());
					in.close();
				} catch (IOException e) {
					callbackContext.error("IOException");
					e.printStackTrace();
				}
			}
		});
	}

	private void handleResolveHost(final String urlString,
			final CallbackContext callbackContext) {

		cordova.getThreadPool().execute(new Runnable() {
			@Override
			public void run() {
				try {
					InetAddress inet = InetAddress.getByName(urlString);
					String resolvedIP = inet.getHostAddress();
					callbackContext.success(resolvedIP);
				} catch (UnknownHostException e) {
					callbackContext.error("UnknownHostException");
					e.printStackTrace();
				}
			}
		});
	}

	private void handlePing(final String urlString, final Integer loopNumber,
			final CallbackContext callbackContext) {
		/*
		 * try { InetAddress inet = InetAddress.getByName(urlString); boolean
		 * ping = inet.isReachable(5000); callbackContext.success(ping + ""); }
		 * catch (UnknownHostException e) {
		 * callbackContext.error("UnknownHostException"); e.printStackTrace(); }
		 * catch (IOException e) { callbackContext.error("IOException");
		 * e.printStackTrace(); }
		 */
		cordova.getThreadPool().execute(new Runnable() {

			@Override
			public void run() {
				String ip = urlString;
				JSONArray pingResult = new JSONArray();
				String[] pingCmd = new String[] { "ping", "-c " + loopNumber,
						ip };

				Runtime r = Runtime.getRuntime();
				Process p;
				try {
					p = r.exec(pingCmd);
					BufferedReader in = new BufferedReader(
							new InputStreamReader(p.getInputStream()));
					String inputLine;
					// Toast.makeText(cordova.getActivity(), "Going loop",
					// 1).show();
					int i = 0;
					while ((inputLine = in.readLine()) != null) {
						pingResult.put(inputLine);
					}

					// Toast.makeText(cordova.getActivity(), pingResult,
					// 1).show();
					callbackContext.success(pingResult.toString());
					in.close();
				} catch (IOException e) {
					callbackContext.error("IOException");
					e.printStackTrace();
				}
			}
		});
	}
	
	private void getLocalIP(CallbackContext callbackContext){
		try {
	        for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
	            NetworkInterface intf = en.nextElement();
	            for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
	                InetAddress inetAddress = enumIpAddr.nextElement();
	                if (!inetAddress.isLoopbackAddress() && checkFormatIP(inetAddress.getHostAddress())) {
	                    String ip = inetAddress.getHostAddress();
	                    callbackContext.success(ip);
	                }
	            }
	        }
	    } catch (SocketException ex) {
	        ex.printStackTrace();
	        callbackContext.error("fail");
	    }
	}
	
	public boolean checkFormatIP(String ip){
		boolean isIp = false;
		String IPADDRESS_PATTERN = 
				"^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
				"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
				"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
				"([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
		if(ip != null){
			Pattern pattern = Pattern.compile(IPADDRESS_PATTERN);
			Matcher matcher = pattern.matcher(ip);
			isIp =  matcher.matches();
		}
		return isIp;
	}
}
