package com.coshling.constants;

import java.io.Serializable;

public class ReminderInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	public int id = 0;
	public String dealdate = "";
	public CardInfo cardInfo = new CardInfo();
	public int eventid = 0;
}
