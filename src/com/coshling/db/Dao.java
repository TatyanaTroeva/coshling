package com.coshling.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.coshling.constants.ReminderInfo;

public class Dao {
	private static final String DATABASE_NAME = "coshling.db";
	private static final int DATABASE_VERSION = 1;
	private static final String REMINDER_DB_NAME = "reminderdb";
	private SQLiteDatabase db;
	private DatabaseHelper dbHelper;

	public Dao(Context context) {

		dbHelper = new DatabaseHelper(context, DATABASE_NAME, null,
				DATABASE_VERSION);
	}

	public void open() { 
		db = dbHelper.getWritableDatabase();
	}

	public void close() {
		db.close();
		dbHelper.close();
	}
    /**
     * 
     * @return reminder list saved in DB
     */
	public ArrayList<ReminderInfo> getReminderList() {
		
		ArrayList<ReminderInfo> reminderlist = new ArrayList<ReminderInfo>();
		Cursor u = null;
		u = db.query(REMINDER_DB_NAME, null, null, null, null,null, "id");
		
		int count = u.getCount();
		if (count == 0) {
			u.close();
			return reminderlist;
		}
		u.moveToFirst();
		for(int i=0;i<count;i++)
		{
			ReminderInfo reminerInfo = new ReminderInfo();
			reminerInfo.id = u.getInt(0);
			reminerInfo.dealdate = u.getString(1);
			reminerInfo.cardInfo.id = u.getString(2);
			reminerInfo.cardInfo.imageurl = u.getString(3);
			reminerInfo.cardInfo.nextdealdate = u.getString(4);
			reminerInfo.cardInfo.starttime = u.getString(5);
			reminerInfo.cardInfo.endtime = u.getString(6);
			reminerInfo.cardInfo.title = u.getString(7);
			reminerInfo.cardInfo.description = u.getString(8);
			reminerInfo.cardInfo.subtitle = u.getString(9);
			reminerInfo.cardInfo.subdescription = u.getString(10);
			reminerInfo.cardInfo.website = u.getString(11);
			reminerInfo.eventid = u.getInt(12);
			reminderlist.add(reminerInfo);
			u.moveToNext();
		}
		u.close();
		return reminderlist;
		
	}
	public ReminderInfo getReminderInfo(String card_id) {
		Cursor u = null;
		try {
			u = db.query(REMINDER_DB_NAME, null, "card_id=?", new String[]{ card_id }, null, null, null);	
		} catch (Exception e) {
			return null;
		}
		int count = u.getCount();
		if (count == 0) {
			u.close();
			return null;
		}
		ReminderInfo reminerInfo = new ReminderInfo();
		u.moveToFirst();
		for(int i=0;i<count;i++)
		{
			reminerInfo.id = u.getInt(0);
			reminerInfo.dealdate = u.getString(1);
			reminerInfo.cardInfo.id = u.getString(2);
			reminerInfo.cardInfo.imageurl = u.getString(3);
			reminerInfo.cardInfo.nextdealdate = u.getString(4);
			reminerInfo.cardInfo.starttime = u.getString(5);
			reminerInfo.cardInfo.endtime = u.getString(6);
			reminerInfo.cardInfo.title = u.getString(7);
			reminerInfo.cardInfo.description = u.getString(8);
			reminerInfo.cardInfo.subtitle = u.getString(9);
			reminerInfo.cardInfo.subdescription = u.getString(10);
			reminerInfo.cardInfo.website = u.getString(11);
			reminerInfo.eventid = u.getInt(12);
		}
		u.close();
		return reminerInfo;
		
	}
	public boolean existReminderInfo(ReminderInfo info) {
		Cursor u = null;
		try {
			u = db.query(REMINDER_DB_NAME, null, "card_id=?", new String[]{ info.cardInfo.id }, null, null, null);	
		} catch (Exception e) {
			return false;
		}
		int count = u.getCount();
		if (count == 0) {
			u.close();
			return false;
		}
		u.close();
		return true;
		
	}
	/**
	 * modify reminder info in DB
	 * @param info
	 */
	public void modifyReminderInfo(ReminderInfo info) {
		ContentValues values = new ContentValues();
		values.put("id",info.id);
		values.put("dealdate",info.dealdate);
		values.put("card_id",info.cardInfo.id);
		values.put("card_imageurl",info.cardInfo.imageurl);
		values.put("card_nextdealdate",info.cardInfo.nextdealdate);
		values.put("card_starttime",info.cardInfo.starttime);
		values.put("card_endtime",info.cardInfo.endtime);
		values.put("card_title",info.cardInfo.title);
		values.put("card_description",info.cardInfo.description);
		values.put("card_subtitle",info.cardInfo.subtitle);
		values.put("card_subdescription",info.cardInfo.subdescription);
		values.put("card_website",info.cardInfo.website);
		values.put("eventid",info.eventid);
		db.update(REMINDER_DB_NAME, values, "id=?", new String[] { info.id + "" });
		values = null;
	}
	/**
	 * removing any reminder with id from DB
	 * @param contactID
	 */
	public void removeReminderInfo(String card_id) {
		db.delete(REMINDER_DB_NAME, "card_id=" + "'" + card_id + "'", null);
	}
	/**
	 * adding new reminder in DB
	 * @param info
	 */
	public void addReminderInfo(ReminderInfo info) {
		ContentValues values = new ContentValues();
		values.put("dealdate",info.dealdate);
		values.put("card_id",info.cardInfo.id);
		values.put("card_imageurl",info.cardInfo.imageurl);
		values.put("card_nextdealdate",info.cardInfo.nextdealdate);
		values.put("card_starttime",info.cardInfo.starttime);
		values.put("card_endtime",info.cardInfo.endtime);
		values.put("card_title",info.cardInfo.title);
		values.put("card_description",info.cardInfo.description);
		values.put("card_subtitle",info.cardInfo.subtitle);
		values.put("card_subdescription",info.cardInfo.subdescription);
		values.put("card_website",info.cardInfo.website);
		values.put("eventid",info.eventid);
		db.insert(REMINDER_DB_NAME, null, values);
		values = null;
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			/**
			 * creating reminder table
			 */
			StringBuffer sb = new StringBuffer();
			sb.append("CREATE TABLE ");
			sb.append(REMINDER_DB_NAME);
			sb.append("(");
			sb.append("id INTEGER primary key autoincrement,");
			sb.append("dealdate VARCHAR(50),");
			sb.append("card_id VARCHAR(20),");
			sb.append("card_imageurl VARCHAR(50),");
			sb.append("card_nextdealdate VARCHAR(50),");
			sb.append("card_starttime VARCHAR(50),");
			sb.append("card_endtime VARCHAR(50),");
			sb.append("card_title VARCHAR(50),");
			sb.append("card_description VARCHAR(50),");
			sb.append("card_subtitle VARCHAR(50),");
			sb.append("card_subdescription VARCHAR(50),");
			sb.append("card_website VARCHAR(50),");
			sb.append("eventid INTEGER");
			sb.append(")");
			db.execSQL(sb.toString());
		}
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		}
	}

}
