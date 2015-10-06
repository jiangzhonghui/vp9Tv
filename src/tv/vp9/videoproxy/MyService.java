package tv.vp9.videoproxy;

import android.util.Log;


public class MyService {
	
	private String TAG = "MyService";
	private Thread proxyThread;

	public MyService(){
    	System.loadLibrary("ffmpeg_vn");
    	System.loadLibrary("vp9_proxy");
	}
	
	public void startProxy(){
		Log.d(TAG, "VP9-Proxy Start");
		this.proxyThread = new Thread(){
			
			public void run(){
				native_start_proxy();
			}
			
		};
		proxyThread.setName("proxyThread_31");
		proxyThread.start();
		
	}
	
	private native int native_start_proxy();
	private native int native_get_port();
	private native int native_stop();
	private native double native_get_speed(int index);
	private native double native_get_expected_speed(int index);
	private native double native_get_durations(int index);
	private native int native_get_last_error();
	private native int native_get_number_segment();
	private native int native_get_number_connection();
	private native int native_get_bytes();
	private native double native_get_download_time();
	
	public int get_port()
	{
		return native_get_port();
	}
	
	public double get_speed(int index)
	{
		return native_get_speed(index);
	}
	
//	public proxyError get_last_error()
//	{
//		int last_error = native_get_last_error();
//		proxyError res = proxyError.SUCCESS;
//		
//		switch (last_error) 
//		{
//			case 1:
//				res = proxyError.LOAD_M3U8_FAIL;
//				break;
//			case 2:
//				res = proxyError.LOAD_VN_FAIL;
//				break;
//		}
//		
//		return res;
//	}
	
	
	public int get_download_connection()
	{
		int connection = native_get_number_connection();
		return connection;
	}
	
	public int get_number_segment()
	{
		int segment = native_get_number_segment();
		return segment;
	}
	
	public int get_last_error()
	{
		int last_error = native_get_last_error();
		return last_error;
	}
	
	public double get_expected_speed(int index)
	{
		return native_get_expected_speed(index);
	}
	
	public void stopThread(){
		native_stop();
		Log.d(TAG, "call this.proxyThread.join()");
		try {
			this.proxyThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Log.d(TAG, "call this.proxyThread.join() finish");
	}
	
	public double get_durations(int index)
	{
		return native_get_durations(index);
	}
	
	public int get_bytes()
	{
		return native_get_bytes();
	}
	
	public double get_download_time()
	{
		return native_get_download_time();
	}
	
}
