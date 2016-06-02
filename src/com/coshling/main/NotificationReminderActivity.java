package com.coshling.main;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.coshling.connection.UserAPI;
import com.coshling.constants.Constants;
import com.coshling.constants.ReminderInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class NotificationReminderActivity extends Activity implements OnClickListener{

	private TextView titleTextView_ = null;
	private TextView descriptionTextView_ = null;
	private TextView subTitleTextView_ = null;
	private TextView subDescriptionTextView_ = null;
	private TextView nextDealDateTextView_ = null;
	private TextView startDateTextView = null;
	private TextView endDateTextView = null;
	private ImageView cardImageView_ = null;
	private Button getDealButton_ = null;
	private ReminderInfo reminderInfo = null;
	private DisplayImageOptions options;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_notification_reminder);
		
		initView();
		setListener();
		initData();
	}
	
	private void initView() {
		titleTextView_ = (TextView) findViewById(R.id.cardTitleTextView);
		descriptionTextView_ = (TextView) findViewById(R.id.cardDescriptionTextView);
		subTitleTextView_ = (TextView) findViewById(R.id.cardSubTitleTextView);
		subDescriptionTextView_ = (TextView) findViewById(R.id.cardSubDescriptionTextView);
		nextDealDateTextView_ = (TextView) findViewById(R.id.nextDealDateTextView);
		cardImageView_ = (ImageView) findViewById(R.id.cardImageView);
		getDealButton_ = (Button) findViewById(R.id.getDealButton);
		startDateTextView = (TextView) findViewById(R.id.startDateTextView);
		endDateTextView = (TextView) findViewById(R.id.endDateTextView);
	}	
	
	private void setListener() {
		getDealButton_.setOnClickListener(this);
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
		
		titleTextView_.setText(reminderInfo.cardInfo.title);
		descriptionTextView_.setText(reminderInfo.cardInfo.description);
		subTitleTextView_.setText(reminderInfo.cardInfo.subtitle);
		subDescriptionTextView_.setText(reminderInfo.cardInfo.subdescription);
		nextDealDateTextView_.setText(reminderInfo.cardInfo.nextdealdate);
		startDateTextView.setText(reminderInfo.cardInfo.starttime);
		endDateTextView.setText(reminderInfo.cardInfo.endtime);
		Constants.imageLoader.displayImage(UserAPI.COSHLING_BASE_URL + reminderInfo.cardInfo.imageurl, cardImageView_, options);
	}
	
	public void onClick(View v) {
		int viewId = v.getId();
		switch (viewId) {
		case R.id.getDealButton: {
			if (reminderInfo == null)
				return;
			String url = reminderInfo.cardInfo.website;
			if (url == null) {
				Toast.makeText(MainTabActivity.getInstance(), "Website address is invalid", Toast.LENGTH_LONG).show();
				return;
			}
			if (!url.contains("http"))
				url = "http://" + url;
			
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			startActivity(intent);
		}
		    break;
		}
	}
}
