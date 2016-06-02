package com.coshling.main;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.coshling.adapter.MenuListAdapter;

public class MenuActivity extends FragmentActivity {

	private static MenuActivity 		menuActivity = null;
    private ListView 					menuListView_ = null;
    
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.fragment_menu);
		initView();
		setListener();
		initData();
	}
	public void initView() {
		menuListView_ = (ListView) findViewById(R.id.menuListView);
	}
	public void setListener() {
		menuListView_.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (SlideMenuWithActivity.getSlideMenu() != null) {
					SlideMenuWithActivity.getSlideMenu().close(true);
				}
				MainTabActivity.getInstance().onClickedMenuItem(position);
	        }
	    });
	}
	public void initData() {
		String[] menulist = {"All Cards", "My Reminders"};
		MenuListAdapter menuListAdapter = new MenuListAdapter(this, menulist, menuListView_);
		menuListView_.setAdapter(menuListAdapter);
		menuListAdapter.notifyDataSetChanged();
	}
	public static MenuActivity getInstance() {
		return menuActivity;
	}
}
