package com.coshling.utils;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.coshling.constants.CardInfo;


public class JSONParser
{
	public static final ArrayList<CardInfo> parseAllCards(String res )
	{	
		ArrayList<CardInfo> cardslist = new ArrayList<CardInfo>();
		try {
			JSONObject json = new JSONObject(res);
			JSONArray list = json.getJSONArray("data");
			for (int i=0;i<list.length(); i++) {
				JSONObject item = list.getJSONObject(i);
				CardInfo info = new CardInfo();
				info.id = item.getString("id");
				info.title = item.getString("card_title");
				info.nextdealdate = item.getString("card_next_deal_date");
				info.starttime = item.getString("card_start_time");
				info.endtime = item.getString("card_end_time");
				info.description = item.getString("card_description");
				info.subtitle = item.getString("sub_title");
				info.subdescription = item.getString("sub_description");
				info.imageurl = item.getString("picture_url");
				info.website = item.getString("card_website");
				cardslist.add(info);
			}
			return cardslist;
		}
		catch( Exception e ) {

		}
		return cardslist;
	}
	
}
