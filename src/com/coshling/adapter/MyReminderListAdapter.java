package com.coshling.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.coshling.connection.UserAPI;
import com.coshling.constants.Constants;
import com.coshling.constants.ReminderInfo;
import com.coshling.db.Dao;
import com.coshling.main.MainTabActivity;
import com.coshling.main.R;
import com.coshling.main.ReminderDetailActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
public class MyReminderListAdapter extends BaseAdapter {
	private LayoutInflater 					inflater = null;
	private ArrayList<ReminderInfo> 		list = null;
	private Activity 						activity = null;
	public DisplayImageOptions 				options;
	
	public MyReminderListAdapter(Activity context, ArrayList<ReminderInfo> list, ListView listView) {
		this.list = list;
		this.activity = context;
		options = new DisplayImageOptions.Builder().imageScaleType(ImageScaleType.EXACTLY)
				   .cacheOnDisc(true)
				   .cacheInMemory(true)
				   .bitmapConfig(Bitmap.Config.RGB_565)
				   .considerExifParams(true)
				   .displayer(new FadeInBitmapDisplayer(300))
				   .build();
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;
		final CellViewCache cache;
		if (view == null) {
			inflater = LayoutInflater.from(activity);
			view = inflater.inflate(R.layout.fragment_reminder_cell, parent, false);
			cache = new CellViewCache(view);
			view.setTag(cache);
		} else {
			cache = (CellViewCache) view.getTag();
		}
		final ReminderInfo info = list.get(position);
		TextView cardTitleTextView = cache.gettCardTitleTextView();
		TextView calendarTextView = cache.getCalendarTextView();
		TextView startDateTextView = cache.getStartDateTextView();
		TextView endDateTextView = cache.getEndDateTextView();
		Button closeButton = cache.getCloseButton();
		ImageView cardImageView = cache.getCardImageView();
		
		cardTitleTextView.setText(info.cardInfo.title);
		calendarTextView.setText(info.dealdate.split(" ")[0]);
		startDateTextView.setText(info.cardInfo.starttime);
		endDateTextView.setText(info.cardInfo.endtime);
		Constants.imageLoader.displayImage(UserAPI.COSHLING_BASE_URL + info.cardInfo.imageurl, cardImageView, options);
		closeButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Constants.DeleteCalendarEntry(activity, info.eventid);
				Dao dao = new Dao(MainTabActivity.getInstance());
				dao.open();
				dao.removeReminderInfo(info.cardInfo.id);
				list = dao.getReminderList();
				dao.close();
				notifyDataSetChanged();
			}
		});
		view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainTabActivity.getInstance(), ReminderDetailActivity.class);
				intent.putExtra("reminderinfo", info);
				MainTabActivity.getInstance().startActivity(intent);
			}
		});
		return view;
	}
	class CellViewCache {
		private View view;
		private TextView cardTitleTextView = null;
		private TextView calendarTextView = null;
		private TextView startDateTextView = null;
		private TextView endDateTextView = null;
		private Button closeButton = null;
		private ImageView cardImageView = null;
		
		public CellViewCache(View view) {
			this.view = view;
		}
		public TextView gettCardTitleTextView() {
			if (cardTitleTextView == null) {
				cardTitleTextView = (TextView) view.findViewById(R.id.cardTitleTextView);
			}
			return cardTitleTextView;
		}
		public TextView getCalendarTextView() {
			if (calendarTextView == null) {
				calendarTextView = (TextView) view.findViewById(R.id.calendarTextView);
			}
			return calendarTextView;
		}
		public TextView getStartDateTextView() {
			if (startDateTextView == null) {
				startDateTextView = (TextView) view.findViewById(R.id.startDateTextView);
			}
			return startDateTextView;
		}
		public TextView getEndDateTextView() {
			if (endDateTextView == null) {
				endDateTextView = (TextView) view.findViewById(R.id.endDateTextView);
			}
			return endDateTextView;
		}
		public Button getCloseButton() {
			if (closeButton == null) {
				closeButton = (Button) view.findViewById(R.id.closeButton);
			}
			return closeButton;
		}
		public ImageView getCardImageView() {
			if (cardImageView == null) {
				cardImageView = (ImageView) view.findViewById(R.id.cardImageView);
			}
			return cardImageView;
		}
	}
	
}

