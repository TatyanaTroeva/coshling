package com.coshling.fragments;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.coshling.connection.UserAPI;
import com.coshling.constants.CardInfo;
import com.coshling.constants.Constants;
import com.coshling.constants.ReminderInfo;
import com.coshling.db.Dao;
import com.coshling.main.MainTabActivity;
import com.coshling.main.NotificationReminderActivity;
import com.coshling.main.R;
import com.coshling.main.SlideMenuWithActivity;
import com.coshling.utils.JSONParser;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.ptashek.widgets.datetimepicker.DateTimePicker;

public class CardsFragment extends BaseFragment implements OnClickListener {
	private Button 					menuButton_ = null;
	private ViewPager 				cardsViewPager_ = null;
	private ArrayList<CardInfo> 	cardslist = null;
	public DisplayImageOptions 		options;
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	View view = inflater.inflate(R.layout.fragment_cards, container, false);

    	initView(view);
    	setListener();
    	initData();
    	return view;
    }
    public void initView(View view) {
    	menuButton_ = (Button) view.findViewById(R.id.menuButton);
    	cardsViewPager_ = (ViewPager) view.findViewById(R.id.cardsViewPager);
    }
    public void setListener() {
    	menuButton_.setOnClickListener(this);
    	cardsViewPager_.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
    }
    public void initData() {
		options = new DisplayImageOptions.Builder().imageScaleType(ImageScaleType.EXACTLY)
				   .cacheOnDisc(true)
				   .cacheInMemory(true)
				   .bitmapConfig(Bitmap.Config.RGB_565)
				   .considerExifParams(true)
				   .displayer(new FadeInBitmapDisplayer(300))
				   .build();
		
    	AllCardsTask allCardsTask = new AllCardsTask();
    	allCardsTask.execute();
	}

    public void onClick(View view) {
    	switch (view.getId()) {
    	case R.id.menuButton: {
    		if (SlideMenuWithActivity.getSlideMenu().isOpen()) 
    			SlideMenuWithActivity.getSlideMenu().close(true);
    		else
    			SlideMenuWithActivity.getSlideMenu().open(false, true);
    	}
    	    break;
    	}
    }
    private class CardsViewAdapter extends PagerAdapter {
        private ArrayList<CardInfo> list = null;
        public CardsViewAdapter(ArrayList<CardInfo> list) {
        	this.list = list;
        }
        @Override
        public int getCount() {
          return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
             return view == ((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
	          Context context = MainTabActivity.getInstance();
	          LayoutInflater inflater = LayoutInflater.from(context);
			  View view = inflater.inflate(R.layout.fragment_cards_cell, null, false);
			  
			  final TextView dateTextView = (TextView) view.findViewById(R.id.dateTextView);
			  final TextView timeTextView = (TextView) view.findViewById(R.id.timeTextView);
			  TextView titleTextView = (TextView) view.findViewById(R.id.cardTitleTextView);
			  TextView startDateTextView = (TextView) view.findViewById(R.id.startDateTextView);
			  TextView endDateTextView = (TextView) view.findViewById(R.id.endDateTextView);
			  TextView descriptionTextView = (TextView) view.findViewById(R.id.cardDescriptionTextView);
			  TextView subTitleTextView = (TextView) view.findViewById(R.id.cardSubTitleTextView);
			  TextView subDescriptionTextView = (TextView) view.findViewById(R.id.cardSubDescriptionTextView);
			  TextView nextDealDateTextView = (TextView) view.findViewById(R.id.nextDealDateTextView);
			  ImageView cardImageView = (ImageView) view.findViewById(R.id.cardImageView);
			  Button calendarButton = (Button) view.findViewById(R.id.calendarButton);
			  Button setReminderButton = (Button) view.findViewById(R.id.setReminderButton);
			  
			  final CardInfo info = list.get(position);
			  Constants.imageLoader.displayImage(UserAPI.COSHLING_BASE_URL + info.imageurl, cardImageView, options);
			  titleTextView.setText(info.title);
			  descriptionTextView.setText(info.description);
			  subTitleTextView.setText(info.subtitle);
			  subDescriptionTextView.setText(info.subdescription);
			  nextDealDateTextView.setText(info.nextdealdate);
			  startDateTextView.setText(info.starttime);
			  endDateTextView.setText(info.endtime);
			  
			  Dao dao = new Dao(MainTabActivity.getInstance());
			  dao.open();
			  final ReminderInfo reminderInfo = dao.getReminderInfo(info.id);
			  if (reminderInfo != null) {
				  String dealdate = reminderInfo.dealdate;
				  dateTextView.setText(dealdate.split(" ")[0]);
				  timeTextView.setText(dealdate.split(" ")[1]);
			  } else {
				  Date date = new Date();
				  SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				  dateTextView.setText(format.format(date));
				  format = new SimpleDateFormat("HH:mm");
				  timeTextView.setText(format.format(date));  
			  }
			  dao.close();
			  
			  
			  calendarButton.setOnClickListener(new OnClickListener() {
				  @Override
		          public void onClick(View v) {
		        	  // TODO Auto-generated method stub
					  showDateTimeDialog(dateTextView, timeTextView);
		          }
			  });
			  setReminderButton.setOnClickListener(new OnClickListener() {
				  @Override
				  public void onClick(View v) {
					  // TODO Auto-generated method stub
					  ReminderInfo reminder1 = new ReminderInfo();
					  reminder1.dealdate = String.format("%s %s", dateTextView.getText().toString(), timeTextView.getText().toString());
					  reminder1.cardInfo = info;
					  Dao dao = new Dao(MainTabActivity.getInstance());
					  dao.open();
					  
					  ReminderInfo existReminder = dao.getReminderInfo(reminder1.cardInfo.id);
					  if (existReminder != null) {
				          Constants.DeleteCalendarEntry(MainTabActivity.getInstance(), existReminder.eventid);
					  }
					   
					  Uri newEvent = Constants.MakeNewCalendarEntry(MainTabActivity.getInstance(), 
																	  reminder1.cardInfo.id, 
																	  reminder1.cardInfo.title, 
																	  reminder1.cardInfo.title, 
																	  reminder1.cardInfo.nextdealdate + " " + reminder1.cardInfo.starttime, 
																	  reminder1.cardInfo.nextdealdate + " " + reminder1.cardInfo.endtime);
					  if (newEvent != null) {
						  int eventID = Integer.parseInt(newEvent.getLastPathSegment());
						  reminder1.eventid = eventID;
						  Constants.setReminder(MainTabActivity.getInstance(), eventID, reminder1.dealdate, reminder1.cardInfo.nextdealdate + " " + reminder1.cardInfo.starttime);	  
						  dao.addReminderInfo(reminder1);  
						  Toast.makeText(MainTabActivity.getInstance(), "Success to set reminder!", Toast.LENGTH_LONG).show();
					  } else {
						  Toast.makeText(MainTabActivity.getInstance(), "Failed to set reminder!", Toast.LENGTH_LONG).show();
					  }
					  dao.close();
					  
				  }
			  });
	          ((ViewPager) container).addView(view, 0);
	          return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
             ((ViewPager) container).removeView((View) object);
        }
    }
    private void checkReminder() {
    	Dao dao = new Dao(MainTabActivity.getInstance());
    	dao.open();
    	ArrayList<ReminderInfo> reminderlist = dao.getReminderList();
    	dao.close();
    	if (reminderlist != null && cardslist != null) {
    		for (int i = 0;i<reminderlist.size(); i++) {
    			for (int j=0;j<cardslist.size();j++) {
    				CardInfo cardinfo = cardslist.get(j);
    				ReminderInfo info = reminderlist.get(i);
        			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        			try {
        				Date currentDate = new Date();
        				Date reminderDate = format.parse(info.dealdate);	
        				//Date cardStartDate = format.parse(cardinfo.nextdealdate + " " + cardinfo.starttime);
        				Date cardEndDate = format.parse(cardinfo.nextdealdate + " " + cardinfo.endtime);
        				if (reminderDate.getTime() < currentDate.getTime() && currentDate.getTime() < cardEndDate.getTime()) {
        					Intent intent = new Intent(MainTabActivity.getInstance(), NotificationReminderActivity.class);
        					intent.putExtra("reminderinfo", info);
        					MainTabActivity.getInstance().startActivity(intent);
        					Dao dao1 = new Dao(MainTabActivity.getInstance());
        			    	dao1.open();
        			    	dao1.removeReminderInfo(info.cardInfo.id);
        			    	dao1.close();
        					return;
        				}
        				
        			} catch (Exception e){}
    			}
    		}
    	}

    }
    private class AllCardsTask extends AsyncTask<String, Void, String>
	{
		ProgressDialog mProgressDialog ;
		@Override
		protected void onPostExecute( String result )
		{
			mProgressDialog.cancel();
			if ( result != null && result.length() > 0)
			{
				cardslist = JSONParser.parseAllCards(result);
				CardsViewAdapter cardsViewAdapter = new CardsViewAdapter(cardslist);
				cardsViewPager_.setAdapter(cardsViewAdapter);
				cardsViewAdapter.notifyDataSetChanged();
				
				checkReminder();
			}
		}

		@Override
		protected void onPreExecute()
		{
			mProgressDialog = new ProgressDialog( MainTabActivity.getInstance() ) ;
			mProgressDialog.setMessage("please wait...");
			mProgressDialog.setCancelable(false);
			mProgressDialog.show() ;
		}

		@Override
		protected String doInBackground( String... params )
		{
			return UserAPI.getResponse(UserAPI.COSHLING_CARDS_URL) ;
		}
	}
    private void showDateTimeDialog(final TextView dateTextView, final TextView timeTextView) {
		// Create the dialog
		final Dialog mDateTimeDialog = new Dialog(MainTabActivity.getInstance());
		// Inflate the root layout
		final RelativeLayout mDateTimeDialogView = (RelativeLayout) MainTabActivity.getInstance().getLayoutInflater().inflate(R.layout.date_time_dialog, null);
		// Grab widget instance
		final DateTimePicker mDateTimePicker = (DateTimePicker) mDateTimeDialogView.findViewById(R.id.DateTimePicker);
		// Check is system is set to use 24h time (this doesn't seem to work as expected though)
		final String timeS = android.provider.Settings.System.getString(MainTabActivity.getInstance().getContentResolver(), android.provider.Settings.System.TIME_12_24);
		final boolean is24h = true;//!(timeS == null || timeS.equals("12"));

		// Update demo TextViews when the "OK" button is clicked 
		((Button) mDateTimeDialogView.findViewById(R.id.SetDateTime)).setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				mDateTimePicker.clearFocus();
				// TODO Auto-generated method stub
				dateTextView.setText(mDateTimePicker.get(Calendar.YEAR) + "-" + (mDateTimePicker.get(Calendar.MONTH)+1) + "-"
						+ mDateTimePicker.get(Calendar.DAY_OF_MONTH));
				if (mDateTimePicker.is24HourView()) {
					timeTextView.setText(mDateTimePicker.get(Calendar.HOUR_OF_DAY) + ":" + mDateTimePicker.get(Calendar.MINUTE));
				} else {
					timeTextView.setText(mDateTimePicker.get(Calendar.HOUR) + ":" + mDateTimePicker.get(Calendar.MINUTE) + " "
							+ (mDateTimePicker.get(Calendar.AM_PM) == Calendar.AM ? "AM" : "PM"));
				}
				mDateTimeDialog.dismiss();
			}
		});

		// Cancel the dialog when the "Cancel" button is clicked
		((Button) mDateTimeDialogView.findViewById(R.id.CancelDialog)).setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				mDateTimeDialog.cancel();
			}
		});

		// Reset Date and Time pickers when the "Reset" button is clicked
		((Button) mDateTimeDialogView.findViewById(R.id.ResetDateTime)).setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				mDateTimePicker.reset();
			}
		});
		
		// Setup TimePicker
		mDateTimePicker.setIs24HourView(is24h);
		// No title on the dialog window
		mDateTimeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// Set the dialog content view
		mDateTimeDialog.setContentView(mDateTimeDialogView);
		// Display the dialog
		mDateTimeDialog.show();
	}
}
