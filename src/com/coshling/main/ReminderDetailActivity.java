package com.coshling.main;

import java.util.Calendar;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.coshling.connection.UserAPI;
import com.coshling.constants.Constants;
import com.coshling.constants.ReminderInfo;
import com.coshling.db.Dao;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.ptashek.widgets.datetimepicker.DateTimePicker;

public class ReminderDetailActivity extends Activity implements OnClickListener{

	private TextView cardTitleTextView_ = null;
	private TextView titleTextView_ = null;
	private TextView descriptionTextView_ = null;
	private TextView subTitleTextView_ = null;
	private TextView subDescriptionTextView_ = null;
	private TextView nextDealDateTextView_ = null;
	private TextView startDateTextView = null;
	private TextView endDateTextView = null;
	private TextView timeTextView_ = null;
	private TextView dateTextView_ = null;
	private ImageView cardImageView_ = null;
	private Button calendarButton_ = null;
	private Button setReminderButton_ = null;
	private Button backButton_ = null;
	private Button shareButton_ = null;
	private ReminderInfo reminderInfo = null;
	private DisplayImageOptions options;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_reminder_detail);
		
		initView();
		setListener();
		initData();
	}
	
	private void initView() {
		dateTextView_ = (TextView) findViewById(R.id.dateTextView);
		timeTextView_ = (TextView) findViewById(R.id.timeTextView);
		cardTitleTextView_ = (TextView) findViewById(R.id.cardTitleTextView);
		titleTextView_ = (TextView) findViewById(R.id.titleTextView);
		descriptionTextView_ = (TextView) findViewById(R.id.cardDescriptionTextView);
		subTitleTextView_ = (TextView) findViewById(R.id.cardSubTitleTextView);
		subDescriptionTextView_ = (TextView) findViewById(R.id.cardSubDescriptionTextView);
		nextDealDateTextView_ = (TextView) findViewById(R.id.nextDealDateTextView);
		cardImageView_ = (ImageView) findViewById(R.id.cardImageView);
		startDateTextView = (TextView) findViewById(R.id.startDateTextView);
		endDateTextView = (TextView) findViewById(R.id.endDateTextView);
		calendarButton_ = (Button) findViewById(R.id.calendarButton);
		setReminderButton_ = (Button) findViewById(R.id.setReminderButton);
		backButton_ = (Button) findViewById(R.id.backButton);
		shareButton_ = (Button) findViewById(R.id.shareButton);
	}	
	
	private void setListener() {
		calendarButton_.setOnClickListener(this);
		setReminderButton_.setOnClickListener(this);
		backButton_.setOnClickListener(this);
		shareButton_.setOnClickListener(this);
	}
	
	private void initData() {
		reminderInfo = (ReminderInfo) getIntent().getSerializableExtra("reminderinfo");
		if (reminderInfo == null)
			return;
		options = new DisplayImageOptions.Builder().imageScaleType(ImageScaleType.EXACTLY)
				   .cacheOnDisc(true)
				   .cacheInMemory(true)
				   .bitmapConfig(Bitmap.Config.RGB_565)
				   .considerExifParams(true)
				   .displayer(new FadeInBitmapDisplayer(300))
				   .build();
		
		cardTitleTextView_.setText(reminderInfo.cardInfo.title);
		titleTextView_.setText(reminderInfo.cardInfo.title);
		descriptionTextView_.setText(reminderInfo.cardInfo.description);
		subTitleTextView_.setText(reminderInfo.cardInfo.subtitle);
		subDescriptionTextView_.setText(reminderInfo.cardInfo.subdescription);
		nextDealDateTextView_.setText(reminderInfo.cardInfo.nextdealdate);
		startDateTextView.setText(reminderInfo.cardInfo.starttime);
		endDateTextView.setText(reminderInfo.cardInfo.endtime);
		String dealdate = reminderInfo.dealdate;
		dateTextView_.setText(dealdate.split(" ")[0]);
		timeTextView_.setText(dealdate.split(" ")[1]);
		Constants.imageLoader.displayImage(UserAPI.COSHLING_BASE_URL + reminderInfo.cardInfo.imageurl, cardImageView_, options);
	}
	
	public void onClick(View v) {
		int viewId = v.getId();
		switch (viewId) {
		case R.id.calendarButton: {
			showDateTimeDialog(dateTextView_, timeTextView_);
		}
		    break;
		case R.id.setReminderButton: {
			reminderInfo.dealdate = String.format("%s %s", dateTextView_.getText().toString(), timeTextView_.getText().toString());
			Dao dao = new Dao(ReminderDetailActivity.this);
			dao.open();
			ReminderInfo existReminder = dao.getReminderInfo(reminderInfo.cardInfo.id);
			if (existReminder != null) {
		        Constants.DeleteCalendarEntry(MainTabActivity.getInstance(), existReminder.eventid);
			}
			Uri newEvent = Constants.MakeNewCalendarEntry(this, 
														 reminderInfo.cardInfo.id, 
														 reminderInfo.cardInfo.title, 
														 reminderInfo.cardInfo.description, 
														 reminderInfo.cardInfo.nextdealdate + " " + reminderInfo.cardInfo.starttime, 
														 reminderInfo.cardInfo.nextdealdate + " " + reminderInfo.cardInfo.endtime);
			if (newEvent != null) {
				int eventID = Integer.parseInt(newEvent.getLastPathSegment());
				reminderInfo.eventid = eventID;
				Constants.setReminder(MainTabActivity.getInstance(), eventID, reminderInfo.dealdate, reminderInfo.cardInfo.nextdealdate + " " + reminderInfo.cardInfo.starttime);	
			}
			
			dao.addReminderInfo(reminderInfo);  
			dao.close();
			Toast.makeText(ReminderDetailActivity.this, "Success to update reminder!", Toast.LENGTH_LONG).show();
		}
		    break;
		case R.id.backButton: {
			ReminderDetailActivity.this.finish();
		}
		    break;
		case R.id.shareButton: {
			String msg = "";
			msg += reminderInfo.cardInfo.title;
			msg += "\n" + reminderInfo.cardInfo.description;
			msg += "\n" + reminderInfo.cardInfo.subtitle;
			msg += "\n" + reminderInfo.cardInfo.subdescription;
			msg += "\n" + reminderInfo.cardInfo.nextdealdate + " " + reminderInfo.cardInfo.starttime;
			msg += "\n" + reminderInfo.cardInfo.nextdealdate  + " " + reminderInfo.cardInfo.endtime;;
			Intent share = new Intent(Intent.ACTION_SEND);
			share.setType("image/*");
			share.putExtra(Intent.EXTRA_TEXT, msg);
			startActivity(Intent.createChooser(share, "Share"));
		}
		    break;
		}
	}
	private void showDateTimeDialog(final TextView dateTextView, final TextView timeTextView) {
		// Create the dialog
		final Dialog mDateTimeDialog = new Dialog(ReminderDetailActivity.this);
		// Inflate the root layout
		final RelativeLayout mDateTimeDialogView = (RelativeLayout) ReminderDetailActivity.this.getLayoutInflater().inflate(R.layout.date_time_dialog, null);
		// Grab widget instance
		final DateTimePicker mDateTimePicker = (DateTimePicker) mDateTimeDialogView.findViewById(R.id.DateTimePicker);
		// Check is system is set to use 24h time (this doesn't seem to work as expected though)
		final String timeS = android.provider.Settings.System.getString(ReminderDetailActivity.this.getContentResolver(), android.provider.Settings.System.TIME_12_24);
		final boolean is24h = true;
		
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
