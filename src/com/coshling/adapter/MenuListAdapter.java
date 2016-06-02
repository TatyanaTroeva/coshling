package com.coshling.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.coshling.main.R;
public class MenuListAdapter extends BaseAdapter {
	private LayoutInflater inflater = null;
	private String[] menulist = null;
	private Activity activity = null;
	public MenuListAdapter(Activity context, String[] menulist, ListView listView) {
		this.menulist = menulist;
		this.activity = context;
	}

	@Override
	public int getCount() {
		return menulist.length;
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
			view = inflater.inflate(R.layout.fragment_menu_cell, parent, false);
			cache = new CellViewCache(view);
			view.setTag(cache);
		} else {
			cache = (CellViewCache) view.getTag();
		}
		TextView titleTextView = cache.gettTitleTextView();
		titleTextView.setText(menulist[position]);
		
		return view;
	}
	class CellViewCache {
		private View view;
		private TextView titleTextView = null;
		
		public CellViewCache(View view) {
			this.view = view;
		}
		public TextView gettTitleTextView() {
			if (titleTextView == null) {
				titleTextView = (TextView) view.findViewById(R.id.titleTextView);
			}
			return titleTextView;
		}
	}
	
}

