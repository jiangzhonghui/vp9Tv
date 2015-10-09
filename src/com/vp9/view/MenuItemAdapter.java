package com.vp9.view;

import java.util.ArrayList;

import org.apache.cordova.LOG;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.vp9.player.demandTivi.DemandTiviSchedule;
import com.vp9.player.model.VideoInfo;
import com.vp9.player.model.VideoResult;
import com.vp9.player.serveTime.ServerTimeInfo;
import com.vp9.tv.R;

public class MenuItemAdapter extends BaseAdapter {

	ArrayList<VideoInfo> videos;
	Activity context;
	int videoType;
	DemandTiviSchedule demandTiviSchedule;
	String serverTimeUrl;
	boolean mBusy = false;
	private LayoutInflater inflater;
	public int type;
	public ServerTimeInfo serverTimeInfo;

	public MenuItemAdapter(Activity context, int textViewResourceId, ArrayList<VideoInfo> _videos, int _videoType, DemandTiviSchedule _demandTiviSchedule, ServerTimeInfo serverTimeInfo, int type) {
		this.context = context;
		this.videos = _videos;
		this.videoType = _videoType;
		this.demandTiviSchedule = _demandTiviSchedule;
		this.serverTimeInfo = serverTimeInfo;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.type = type;
	}

	@Override
	public int getCount() {
		return videos.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
//		LOG.e(MenuItemAdapter.class.getSimpleName(), position + "");
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			// visual settings for the list item
			convertView = inflater.inflate(R.layout.custom_item_list, null);
			holder.textview = (MagicTextView) convertView.findViewById(R.id.custom_item_list);

			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		VideoInfo item = videos.get(position);
		
		
		if (item != null) {
			String textProgramName = "";
			if((item.getProgramName() != null) && (!item.getProgramName().equals(""))){
				textProgramName += " - " + item.getProgramName();
			}
			final String text = item.getVideoName() + textProgramName;
			final String id = String.valueOf(position);
			final int intPosition = position;

			final MagicTextView textItem = holder.textview;
			// Log.e(TAG, "position: " + position + " view != null: ");
			context.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					textItem.setText(text.trim());
					textItem.setTag(id);
					textItem.setTextSize(30);
//					textItem.setPadding(10, 10, 10, 10);
					textItem.setPadding(0, 5, 0, 5);
					// textItem.setShadowLayer(5.0f, 3, 3, Color.BLACK);
					textItem.setTextColor(Color.WHITE);
					textItem.setStroke(1, Color.BLACK);
					textItem.setVisibility(View.VISIBLE);
					textItem.setGravity(Gravity.LEFT);
					if (videoType >= 0 && videoType != 1) {
						if (demandTiviSchedule != null && demandTiviSchedule.getCurrentIndex() == intPosition) {
							textItem.setTextColor(Color.RED);
						} else {
							textItem.setTextColor(Color.WHITE);
						}
					} else {
//						if (videos.get(intPosition).getRecordUrl() != null && videos.get(intPosition).getRecordUrl() != "") {
//							textItem.setTextColor(Color.GREEN);
//						} else {
//							textItem.setTextColor(Color.WHITE);
//						}
						textItem.setTextColor(Color.WHITE);
//						if (serverTimeUrl != null) {
						if (serverTimeInfo != null) {
//							if(serverTimeInfo == null){
//								serverTimeInfo = new ServerTimeInfo(serverTimeUrl);
//							}
							
//							serverTimeInfo.updateTime();
							
							VideoResult videoResult = demandTiviSchedule.getVideoInfoByTime(serverTimeInfo.getSecondInDay());
							if (videoResult != null && videoResult.getVideoInfo() != null) {
								if (videoResult.getVideoInfo().getIndex() == intPosition) {
									textItem.setTextColor(Color.RED);
								}
							}
						}

					}
				}
			});

		}

		return convertView;

	}
	
//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) {
//		// TODO Auto-generated method stub
//
//		// setting the ID and text for every items in the list
//
//		LOG.e(MenuItemAdapter.class.getSimpleName(), position + "");
//		View view = convertView;
//		ViewHolder holder;
//		if (view == null) {
//			holder = new ViewHolder();
//			// visual settings for the list item
//			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//			view = inflater.inflate(R.layout.custom_item_list, null);
//			holder.textview = (MagicTextView) view.findViewById(R.id.custom_item_list);
//
//			view.setTag(holder);
//
//		} else {
//			holder = (ViewHolder) view.getTag();
//		}
//
//		VideoInfo item = videos.get(position);
//		if (item != null) {
//
//			final String text = item.getVideoName();
//			final String id = position + "";
//			final int intPosition = position;
//
//			final MagicTextView textItem = holder.textview;
//			// Log.e(TAG, "position: " + position + " view != null: ");
//			context.runOnUiThread(new Runnable() {
//
//				@Override
//				public void run() {
//					textItem.setText(text.trim());
//					textItem.setTag(id);
//					textItem.setTextSize(30);
////					textItem.setPadding(10, 10, 10, 10);
//					textItem.setPadding(0, 5, 0, 5);
//					// textItem.setShadowLayer(5.0f, 3, 3, Color.BLACK);
//					textItem.setTextColor(Color.WHITE);
//					textItem.setStroke(1, Color.BLACK);
//					textItem.setVisibility(View.VISIBLE);
//
//					if (videoType >= 0 && videoType != 1) {
//						if (demandTiviSchedule != null && demandTiviSchedule.getCurrentIndex() == intPosition) {
//							textItem.setTextColor(Color.RED);
//						} else {
//							textItem.setTextColor(Color.WHITE);
//						}
//					} else {
//						if (videos.get(intPosition).getRecordUrl() != null && videos.get(intPosition).getRecordUrl() != "") {
//							textItem.setTextColor(Color.GREEN);
//						} else {
//							textItem.setTextColor(Color.WHITE);
//						}
//						if (serverTimeUrl != null) {
//							ServerTimeInfo serverTimeInfo = new ServerTimeInfo(serverTimeUrl);
//							VideoResult videoResult = demandTiviSchedule.getVideoInfoByTime(serverTimeInfo.getSecondInDay());
//							if (videoResult != null && videoResult.getVideoInfo() != null) {
//								if (videoResult.getVideoInfo().getIndex() == intPosition) {
//									textItem.setTextColor(Color.RED);
//								}
//							}
//						}
//
//					}
//				}
//			});
//
//		}
//
//		return view;
//
//	}

	static class ViewHolder {
		public MagicTextView textview;

	}
}
