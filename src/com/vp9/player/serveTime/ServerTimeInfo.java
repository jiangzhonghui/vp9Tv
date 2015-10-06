package com.vp9.player.serveTime;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.util.Log;

import com.vp9.util.Vp9ParamUtil;

@SuppressLint("SimpleDateFormat")
public class ServerTimeInfo {

	private static final String TAG = "ServerTimeInfo";

	private int second;
	
	private int minute;
	
	private int hour;
	
	private int day;
	
	private int month;
	
	private int year;

	private String strdate;

	private Calendar calendar;

	private long startTime;
	
//	private boolean isSuccess;
	
	public ServerTimeInfo() {
		
	}
	
	public ServerTimeInfo(final String serverTimeUrl) {
//		 Thread thread = new Thread(new Runnable() {
//		     public void run() {
//		    	 Log.d(TAG, "Init ServerTimeInfo");
//		    	 isSuccess = false;
//		    	 getServerTimeUrl(serverTimeUrl);
//		    	 isSuccess = true;
//		    	 Log.d(TAG, "Finish ServerTimeInfo");
//		     }
//		 });
//		 thread.start();
//		 try {
//			thread.join(2000);
//		} catch (InterruptedException e) {
//			usingLocalTime();
//			thread.stop();
//			e.printStackTrace();
//		}
//		 if (thread.isAlive()) {
//		     thread.stop();
//		 }
//		 if(!isSuccess){
//			 usingLocalTime();
//		 }
		Log.d(TAG, "Init ServerTimeInfo");
		ExecutorService executor = Executors.newSingleThreadExecutor();
		final Future<Boolean> handler = executor.submit(new Callable<Boolean>() {
		    @Override
		    public Boolean call() throws Exception {
		        return getServerTimeUrl(serverTimeUrl);
		    }
		});
		try {
			boolean isFinish = handler.get(2, TimeUnit.SECONDS);
			handler.cancel(true);
			executor.shutdown();
			if(!isFinish){
				usingLocalTime();
			}
		} catch (Exception e) {
			usingLocalTime();
			handler.cancel(true);
			executor.shutdown();
		}
		
		Log.d(TAG, "Finish ServerTimeInfo");

	} 
	
	public Boolean getServerTimeUrl(String serverTimeUrl) {
		Log.d(TAG, "Init getServerTimeUrl: " + serverTimeUrl);
		try {
			URL url = new URL(serverTimeUrl);
			DataInputStream dis = new DataInputStream(new BufferedInputStream(url.openStream()));
			int size = dis.available();
			byte[] buffer = new byte[size];
			dis.read(buffer);
			String content = new String(buffer);
			
			JSONObject jsonTimeInfo = new JSONObject(content);
			
//			Log.e(TAG, "serverTimeUrl: " + jsonTimeInfo);
			
			second = Vp9ParamUtil.getValue(jsonTimeInfo.getString("seconds"), 0);
			
			minute = Vp9ParamUtil.getValue(jsonTimeInfo.getString("minutes"), 0);
			
			hour = Vp9ParamUtil.getValue(jsonTimeInfo.getInt("hours"), 0);
			
			this.strdate = Vp9ParamUtil.getValue(jsonTimeInfo.getString("date"), "").trim().replace("-", "");
			
			day = Vp9ParamUtil.getValue(jsonTimeInfo.getInt("mday"), 0);
			
			month = Vp9ParamUtil.getValue(jsonTimeInfo.getInt("mon"), 0) - 1;
			
			year = Vp9ParamUtil.getValue(jsonTimeInfo.getInt("year"), 0);
			
			this.calendar = Calendar.getInstance();
			
			this.startTime = (new Date()).getTime();
			
			this.calendar.set(year, month, day, hour, minute, second);
			
		} catch (Exception e) {
			usingLocalTime();
			e.printStackTrace();
		}
		Log.d(TAG, "Finish serverTimeUrl");
		return true;
	}
	@SuppressWarnings("deprecation")
	private void usingLocalTime() {
		Date date = new Date();
		second = date.getSeconds();
		minute = date.getMinutes();
		hour = date.getHours();
		day = date.getDate();
		month = date.getMonth();
		year = date.getYear() + 1900;
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    	this.setStrdate(dateFormat.format(date));
    	this.startTime = (new Date()).getTime();
		this.calendar = Calendar.getInstance();
		this.calendar.set(year, month, day, hour, minute, second);
		
	}
	
	public void updateTime(){
		long toTime = (new Date()).getTime();
		int milliSecond = (int) (toTime - startTime);
		Calendar newCalendar = Calendar.getInstance();
		newCalendar.setTime(this.calendar.getTime());
//		this.calendar.add(Calendar.MILLISECOND, milliSecond);
//		startTime = toTime;
//		Date currentDate = this.calendar.getTime();
		newCalendar.add(Calendar.MILLISECOND, milliSecond);
		Date currentDate = newCalendar.getTime();
		second = currentDate.getSeconds();
		minute = currentDate.getMinutes();
		hour = currentDate.getHours();
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    	this.setStrdate(dateFormat.format(currentDate));
		day = currentDate.getDate();
		month = currentDate.getMonth();
		year = currentDate.getYear();
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
		
//		Log.d(TAG, "updateTime: " + formatter.format(currentDate.getTime()));
	}
	
	public int getSecondInDay() {
		int secondInDay = second + minute*60 + hour*60*60;
		return secondInDay;
	}

	public String getStrdate() {
		return strdate;
	}

	public void setStrdate(String strdate) {
		this.strdate = strdate;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

}
