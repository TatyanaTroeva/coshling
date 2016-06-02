package com.coshling.constants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract.Events;
import android.provider.CalendarContract.Reminders;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.nostra13.universalimageloader.core.ImageLoader;

public class Constants {

	public final static int MSG_SUCCESS = 0;
	public final static int MSG_FAIL = 1;
	
	public static ProgressDialog mProgressDialog ;
	
	public static final String MENU_CARDS      		= "menu_cards";
	public static final String MENU_MYREMINDER   	= "menu_myreminder";

	public static ImageLoader imageLoader = null;

	public static boolean isEmailValid(String email) {
	    boolean isValid = false;

	    String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
	    CharSequence inputStr = email;

	    Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
	    Matcher matcher = pattern.matcher(inputStr);
	    if (matcher.matches()) {
	        isValid = true;
	    }
	    return isValid;
	}
	
	public static String getDeviceId(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getDeviceId();
	}
	
	public static void showProgressDialog (Context context) {
		mProgressDialog = new ProgressDialog( context ) ;
		mProgressDialog.setMessage("please wait...");
		mProgressDialog.setCancelable(false);
		mProgressDialog.show() ;
	}
	public static void hideProgressDialog() {
		if (mProgressDialog != null)
			mProgressDialog.cancel();
	}
	public static void setEmail(Context context, String val) {
		SharedPreferences.Editor editor = context.getSharedPreferences("com.coshling.main", 0).edit();
		editor.putString("email", val);
		editor.commit();
	}

	public static String getEmail(Context context) {
		SharedPreferences pref = context.getSharedPreferences("com.coshling.main",0);
		return pref.getString("email", "");
	}
	public static Uri MakeNewCalendarEntry(Activity activity, String calId, String title, String description, String starttime, String endtime) {
    
        ContentValues event = new ContentValues();
        TimeZone timeZone = TimeZone.getDefault();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        try {
        	Date startDate = format.parse(starttime);
            Date endDate = format.parse(endtime);
            
            Calendar startCal = Calendar.getInstance();
            startCal.setTime(startDate);
            Calendar endCal = Calendar.getInstance();
            endCal.setTime(endDate);

            event.put(Events.CALENDAR_ID, calId);
            event.put(Events.TITLE, title);
            event.put(Events.DESCRIPTION, description);
            event.put(Events.EVENT_LOCATION, "");

            event.put(Events.DTSTART, startCal.getTimeInMillis());
            event.put(Events.DTEND, endCal.getTimeInMillis());

            event.put(Events.ALL_DAY, 0); // 0 for false, 1 for true
            
            event.put(Events.HAS_ALARM, 1); // 0 for false, 1 for true
            event.put(Events.EVENT_TIMEZONE, timeZone.getID());
            //Uri eventsUri = Uri.parse(getCalendarUriBase(activity)+"events");
            Uri insertedUri = activity.getContentResolver().insert(Events.CONTENT_URI, event);
            return insertedUri;
        } catch (Exception e) {
        	Log.e("log", e.toString());
        	return null;
        }
    }
	private static String getCalendarUriBase(Activity activity) {
        String calendarUriBase = null;
        Uri calendars = Uri.parse("content://calendar/calendars");
        Cursor managedCursor = null;
        try {
            managedCursor = activity.managedQuery(calendars, null, null, null, null);
        } catch (Exception e) {
            // eat
        }

        if (managedCursor != null) {
            calendarUriBase = "content://calendar/";
        } else {
            calendars = Uri.parse("content://com.android.calendar/calendars");
            try {
                managedCursor = activity.managedQuery(calendars, null, null, null, null);
            } catch (Exception e) {
                // eat
            }

            if (managedCursor != null) {
                calendarUriBase = "content://com.android.calendar/";
            }

        }

        return calendarUriBase;
    }
	public static int UpdateCalendarEntry(Activity activity, int entryID, String calId, String title, String description, String starttime, String endtime) {
        int iNumRowsUpdated = 0;
        TimeZone timeZone = TimeZone.getDefault();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        try {
        	Date startDate = format.parse(starttime);
            Date endDate = format.parse(endtime);
            
            Calendar startCal = Calendar.getInstance();
            startCal.setTime(startDate);
            Calendar endCal = Calendar.getInstance();
            endCal.setTime(endDate);
            
            ContentValues event = new ContentValues();

            event.put(Events.CALENDAR_ID, calId);
            event.put(Events.TITLE, title);
            event.put(Events.DESCRIPTION, description);
            event.put(Events.EVENT_LOCATION, "");

            event.put(Events.DTSTART, startCal.getTimeInMillis());
            event.put(Events.DTEND, endCal.getTimeInMillis());

            event.put(Events.ALL_DAY, 0); // 0 for false, 1 for true
            
            event.put(Events.HAS_ALARM, 1); // 0 for false, 1 for true
            event.put(Events.EVENT_TIMEZONE, timeZone.getID());
            //Uri eventsUri = Uri.parse(getCalendarUriBase(activity)+"events");
            //Uri eventUri = ContentUris.withAppendedId(eventsUri, entryID);

            iNumRowsUpdated = activity.getContentResolver().update(Events.CONTENT_URI, event, null,
                    null);

            Log.i("log", "Updated " + iNumRowsUpdated + " calendar entry.");

            return iNumRowsUpdated;
        } catch (Exception e) {
        	return 0;
        }
        
    }

    public static int DeleteCalendarEntry(Activity activity, int entryID) {
        int iNumRowsDeleted = 0;

        Uri eventsUri = Uri.parse(getCalendarUriBase(activity)+"events");
        Uri eventUri = ContentUris.withAppendedId(eventsUri, entryID);
        iNumRowsDeleted = activity.getContentResolver().delete(eventUri, null, null);

        Log.i("log", "Deleted " + iNumRowsDeleted + " calendar entry.");

        return iNumRowsDeleted;
    }
    public static void setReminder(Activity activity, int entryID, String reminderTime, String startTime) {
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
    	int mins = 0;
    	try {
    		Date reminderDate = format.parse(reminderTime);
    		Date startDate = format.parse(startTime);
    		mins = (int)(startDate.getTime() - reminderDate.getTime()) / (1000 * 60);
    	} catch (Exception e){}
    	
    	ContentResolver cr = activity.getContentResolver();
    	ContentValues values = new ContentValues();
    	values.put(Reminders.MINUTES, mins);
    	values.put(Reminders.EVENT_ID, entryID);
    	values.put(Reminders.METHOD, Reminders.METHOD_ALERT);
    	Uri uri = cr.insert(Reminders.CONTENT_URI, values);
    }
    
}
