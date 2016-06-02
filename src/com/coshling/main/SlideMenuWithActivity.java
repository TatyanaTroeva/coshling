package com.coshling.main;

import android.app.ActivityGroup;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.aretha.slidemenu.SlideMenu;
import com.aretha.slidemenu.SlideMenu.LayoutParams;
@SuppressWarnings("deprecation")
public class SlideMenuWithActivity extends ActivityGroup {
	private static SlideMenu mSlideMenu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_slidemenu);
	}
	public static SlideMenu getSlideMenu() {
		return mSlideMenu;
	}
	@Override
	public void onContentChanged() {
		super.onContentChanged();
		mSlideMenu = (SlideMenu) findViewById(R.id.slideMenu);
		

		final LocalActivityManager activityManager = getLocalActivityManager();
		View primary = activityManager.startActivity("MenuActivity",
				new Intent(this, MenuActivity.class)).getDecorView();
		mSlideMenu.addView(primary, new LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				LayoutParams.ROLE_PRIMARY_MENU));
	
//		View secondary = activityManager.startActivity("NotificationsActivity",
//				new Intent(this, NotificationsActivity.class)).getDecorView();
//		mSlideMenu.addView(secondary, new LayoutParams(
//				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
//				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
//				LayoutParams.ROLE_SECONDARY_MENU));
		
		View content = activityManager.startActivity("MainTabActivity",
				new Intent(this, MainTabActivity.class)).getDecorView();
		mSlideMenu.addView(content, new LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				LayoutParams.ROLE_CONTENT));

	}
	public void onBackPressed() {

		if (MainTabActivity.getInstance().mStacks.get(MainTabActivity.getInstance().mCurrentTab).size() == 1) {
			finish();
		} else {
			MainTabActivity.getInstance().onBackPressed();
		}

	}
	
}
