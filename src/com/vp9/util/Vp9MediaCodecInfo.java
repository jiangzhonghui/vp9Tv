package com.vp9.util;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;

public class Vp9MediaCodecInfo {

	@SuppressLint("NewApi")
	public static boolean checkDecoder(String type) {

		int currentapiVersion = android.os.Build.VERSION.SDK_INT;

		if (currentapiVersion >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
			int numCodecs = MediaCodecList.getCodecCount();
			for (int i = 0; i < numCodecs; i++) {
				try {
					MediaCodecInfo codecInfo = MediaCodecList.getCodecInfoAt(i);
					if (codecInfo.isEncoder()) {
						continue;
					}
					String[] types = codecInfo.getSupportedTypes();
					for (int j = 0; j < types.length; j++) {
						if (type.equals(types[j])) {
							return true;
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return false;

	}

	@SuppressLint("NewApi")
	public static boolean checkEncoder(String type) {

		int currentapiVersion = android.os.Build.VERSION.SDK_INT;

		if (currentapiVersion >= android.os.Build.VERSION_CODES.JELLY_BEAN) {

			int numCodecs = MediaCodecList.getCodecCount();
			for (int i = 0; i < numCodecs; i++) {
				try {
					MediaCodecInfo codecInfo = MediaCodecList.getCodecInfoAt(i);
					if (!codecInfo.isEncoder()) {
						continue;
					}
					String[] types = codecInfo.getSupportedTypes();
					for (int j = 0; j < types.length; j++) {
						if (type.equals(types[j])) {
							return true;
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return false;

	}

	@SuppressLint("NewApi")
	public static JSONArray getListSupportCodec() {

		int currentapiVersion = android.os.Build.VERSION.SDK_INT;

		if (currentapiVersion >= android.os.Build.VERSION_CODES.JELLY_BEAN) {

			JSONArray jsonCodecArr = new JSONArray();
			
			int numCodecs = MediaCodecList.getCodecCount();
			for (int i = 0; i < numCodecs; i++) {
				try {

					MediaCodecInfo codecInfo = MediaCodecList.getCodecInfoAt(i);

					String codecName = codecInfo.getName();

					String coder = "Decoder";

					if (!codecInfo.isEncoder()) {

						coder = "Encoder";

					}
					String[] types = codecInfo.getSupportedTypes();

					JSONObject jsonCodec = new JSONObject();

					jsonCodec.put("codecName", codecName);
					
					jsonCodec.put("coder", coder);
					
					if(types != null && types.length > 0){
						
						ArrayList<String> typeList = new ArrayList<String>();
						
						for(String type : types){
							
							typeList.add(type);
							
						}
						
						jsonCodec.put("types", new JSONArray(typeList));
						
					}
					
					jsonCodecArr.put(jsonCodec);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
			
			return jsonCodecArr;
			
		}
		
		return null;
		
	}
	
	
	@SuppressLint("NewApi")
	public static JSONArray getSupportCodec(String type) {

		int currentapiVersion = android.os.Build.VERSION.SDK_INT;

		if (currentapiVersion >= android.os.Build.VERSION_CODES.JELLY_BEAN) {

			JSONArray jsonCodecArr = new JSONArray();
			
			int numCodecs = MediaCodecList.getCodecCount();
			
			for (int i = 0; i < numCodecs; i++) {
				try {

					MediaCodecInfo codecInfo = MediaCodecList.getCodecInfoAt(i);

					String codecName = codecInfo.getName();

					String coder = "Decoder";

					if (!codecInfo.isEncoder()) {

						coder = "Encoder";

					}
					String[] types = codecInfo.getSupportedTypes();

					if(types != null && types.length > 0){
						
						for(int j = 0; j < types.length; j++){
							
							if(type.equalsIgnoreCase(types[j])){
								
								JSONObject jsonCodec = new JSONObject();

								jsonCodec.put("codecName", codecName);
								
								jsonCodec.put("coder", coder);
								
								jsonCodec.put("type", types[j]);
								
								jsonCodecArr.put(jsonCodec);
							}
							
						}

						return jsonCodecArr;
						
					}
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
			
			return jsonCodecArr;
			
		}
		
		return null;
		
	}

}
