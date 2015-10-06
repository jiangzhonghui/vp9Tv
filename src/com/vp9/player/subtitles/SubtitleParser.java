package com.vp9.player.subtitles;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import android.view.View;

import com.vp9.player.controller.MediaController;

public class SubtitleParser {
	
	private boolean isParser;
	private MediaController mController;
	private int index;

	public SubtitleParser(MediaController mController, int index) {
		this.mController = mController;
		this.index = index;
	}

	public SubtitleEntityModel playSubtitle(final String urlSub) {
		isParser = true;
		final SubtitleEntityModel subtitleModel = new SubtitleEntityModel();

		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				
				try {
					
					URL url = new URL(urlSub);
					
					BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

					String inputLine;
					
					boolean isWork = false;
					
					boolean isEvents = false;
					
					while ((inputLine = in.readLine()) != null) {

						if (isWork) {
							
							String[] attrs = new String[10];
							
							int index = 0;
							
							int start = 0;
							
							
							for (int i = 0; i < inputLine.length(); i++) {
								 
								if (inputLine.charAt(i) == ',' && index < 9) {
									
									attrs[index] = inputLine.substring(start, i).trim();
									
									index++;
									
									start = i + 1;
									
									if(index == 9){
										break;
									}
								}
								
							}
							
							attrs[9] = inputLine.substring(start, inputLine.length()).trim();

							SubtitleEntity subtitleEntity = new SubtitleEntity(attrs[1], attrs[2], attrs[9]);
							subtitleModel.add(subtitleEntity);

						} else {

							if (inputLine.startsWith("[Events]")) {

								isEvents = true;

							} else if (isEvents) {

								isWork = true;

							}

						}

					}

					if(subtitleModel.size() > 0){
						isParser = true;
					}else{
						isParser = false;
						mController.setInvisibleForSub(index);
					}
					in.close();

				} catch (Exception e) {
					if(subtitleModel.size() > 0){
						isParser = true;
					}else{
						isParser = false;
						mController.setInvisibleForSub(index);
					}
					
					e.printStackTrace();

				}

			}

		});

		thread.setName("SubtitleParser_17");
		thread.start();

		return subtitleModel;
	}

	public boolean isParser() {
		return isParser;
	}

	public void setParser(boolean isParser) {
		this.isParser = isParser;
	}

}
