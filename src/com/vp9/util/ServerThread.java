package com.vp9.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ScrollView;
import android.widget.TextView;

import com.vp9.player.controller.MediaController;
import com.vp9.tv.VpMainActivity;

public class ServerThread implements Runnable {


 

        private static final int SERVERPORT = 2912;
        
		public Handler updateConversationHandler;

		private TextView tvDebug;

		private ScrollView debugView;

		private Activity mainAcActivity;
		
//		private final static String TAG = "ServerThread";
		
		public final static int MAX_LINES = 30;
		
		private ArrayList<String> strDebugList;
		
		private int bugIndex;

		private ServerSocket serverSocket;
		
		private boolean isRun = false;

		public String speed;
		
		public ArrayList<CommunicationThread> commThreadList;
		
		public ServerThread(TextView tvDebug, ScrollView debugView, Activity mainAcActivity) {
			this.tvDebug = tvDebug;
			this.debugView = debugView;
			this.mainAcActivity = mainAcActivity;
			tvDebug.append("Debug View Create by Bigsky \n");
			tvDebug.append("=========================== \n");
			bugIndex = 0;
			strDebugList = new ArrayList<String>();
			isRun = true;
			commThreadList = new ArrayList<ServerThread.CommunicationThread>();
		}

		public void close(){
			try {
				isRun = false;
				if(this.serverSocket != null){
					this.serverSocket.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}



	            
	        	public void run() {
	        		Looper.prepare();
	        		Socket socket = null;
	        		this.updateConversationHandler = new Handler();

	        		try {

	        			ServerSocket serverSocket = new ServerSocket(SERVERPORT);

	        			// Log.d(TAG, "current Thread 1: " +
	        			// Thread.currentThread().isInterrupted());

	        			while (isRun && !Thread.currentThread().isInterrupted()) {

	        				try {

	        					socket = serverSocket.accept();

	        					CommunicationThread commThread = new CommunicationThread(socket);
	        					commThread.setServerThread(this);
	        					Thread serverSocketThr = new Thread(commThread);
	        					serverSocketThr.setName("serverSocketThr_26");
	        					serverSocketThr.start();

	        				} catch (IOException e) {

	        					e.printStackTrace();

	        				}

	        			}

	        		} catch (IOException e) {

	        			e.printStackTrace();

	        		}

	        		Looper.loop();

	        	}



    class CommunicationThread implements Runnable {

//        private Socket clientSocket;

        private BufferedReader input;
        
        private ServerThread serverThread;

		private BufferedWriter output;

		private Socket clientSocket;

        public CommunicationThread(Socket clientSocket) {
//            this.clientSocket = clientSocket;
            try {

                this.input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                this.output = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                this.clientSocket = clientSocket;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        public void setServerThread(ServerThread serverThread){
        	this.serverThread = serverThread;
        }
        
        public boolean isConnect(){
        	return clientSocket.isConnected();
        }

        public void run() {
//        	Log.d(TAG, "current Thread 2: " + Thread.currentThread().isInterrupted());
 
            while (isRun && !Thread.currentThread().isInterrupted()) {

                try {
                    final String msg = input.readLine();
                    if(msg != null){
                    	if(msg.startsWith("speed")){
                    		handleSpeedMessage(msg);
                    	}else if(msg.equals("receive_log")){
                    		commThreadList.add(this);
                    	}
                    	displayMessage(msg);
                    }

//                    updateConversationHandler.post(new updateUIThread(msg), );
                    
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
        public void sendMessage(String msg){
        	try {
				output.write(msg +"\n");
				output.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
    }




	public void displayMessage(final String msg) {

		mainAcActivity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				bugIndex++;
				// Log.d(TAG, "msg 1: " + msg + ", bugIndex = " + bugIndex);
				String[] split = msg.split("=");
				if (split.length > 0 && split[0].equalsIgnoreCase("speed")) {
					ServerThread.this.speed = split[1];
					Log.e(ServerThread.class.getName(), "SPEED: " + split[1]);
				}

				strDebugList.add(msg);

				
				if (bugIndex > MAX_LINES) {

					strDebugList.remove(0);

					tvDebug.setText("");

					for (String elem : strDebugList) {
						tvDebug.append(elem + "\n");

					}

					debugView.scrollTo(0, debugView.getBottom());

				} else {

					tvDebug.append(msg + "\n");

					debugView.scrollTo(0, debugView.getBottom());
				}

			}
		});

		sendMessageForClient(msg);
		
	}

	private void sendMessageForClient(final String msg) {
		Thread sendMessThrd = new Thread() {

			public void run() {
				if (commThreadList != null) {
					for (CommunicationThread con : commThreadList) {
						if (con.isConnect()) {
							con.sendMessage(msg);
						} else {
							commThreadList.remove(con);
						}
					}
				}
			}

		};
		sendMessThrd.setName("sendMessThrd_102");
		sendMessThrd.start();
	}

public void handleSpeedMessage(String msg) {
	String[] elemMsgs = msg.split(",");
	String strNotify = "Tốc độ:";
	if(mainAcActivity != null && elemMsgs != null && elemMsgs.length > 0){
		for(int i = 0; i < 2; i++){
			if(elemMsgs[i] != null){
				String[] elems = elemMsgs[i].trim().split("=");
				if(elems != null && elems.length > 1 && elems[0] != null && elems[1] != null){
					if("speed".equalsIgnoreCase(elems[0].trim())){
						strNotify += " TB = " + elems[1] + "MB";
					}
					
					if("currentSpeed".equalsIgnoreCase(elems[0].trim())){
						strNotify += " HT = " + elems[1] + "MB";
					}
				}
			}
		}
		MediaController mController = ((VpMainActivity)mainAcActivity).mController;
//		if(mController != null && mController.intProxySpeedDisplay == 0){
//			mController.setMessage2(strNotify);
//		}
	}
	
}
    
}
