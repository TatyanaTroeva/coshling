package com.coshling.fragments;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.coshling.adapter.MyReminderListAdapter;
import com.coshling.constants.ReminderInfo;
import com.coshling.db.Dao;
import com.coshling.main.MainTabActivity;
import com.coshling.main.R;
import com.coshling.main.ReminderDetailActivity;
import com.coshling.main.SlideMenuWithActivity;

public class MyReminderFragment extends BaseFragment implements OnClickListener {
	private Button 						menuButton_ = null;
	private ArrayList<ReminderInfo> 	reminderList = new ArrayList<ReminderInfo>();
	private ListView					remindersListView = null;
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	View view = inflater.inflate(R.layout.fragment_reminders, container, false);

    	initView(view);
    	setListener();
    	initData();
    	return view;
    }
    public void initView(View view) {
    	menuButton_ = (Button) view.findViewById(R.id.menuButton);
    	remindersListView = (ListView) view.findViewById(R.id.remindersListView);
    }
    public void setListener() {
    	menuButton_.setOnClickListener(this);
    }
    public void initData() {
    	Dao dao = new Dao(MainTabActivity.getInstance());
    	dao.open();
    	reminderList = dao.getReminderList();
    	dao.close();

    	MyReminderListAdapter myReminderListAdapter = new MyReminderListAdapter(MainTabActivity.getInstance(), reminderList, remindersListView); 
    	remindersListView.setAdapter(myReminderListAdapter);
    	myReminderListAdapter.notifyDataSetChanged();
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
   
}
