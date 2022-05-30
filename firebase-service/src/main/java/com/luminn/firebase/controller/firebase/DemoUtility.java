package com.luminn.firebase.controller.firebase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DemoUtility {

	private final static SimpleDateFormat FORMATE_1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
	private final static SimpleDateFormat FORMATE_2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	public static String currentDateFormate1() {
		return FORMATE_1.format(new Date());
	}

	public static String currentDateFormate2() {
		return FORMATE_2.format(new Date());
	}

	public static Date parseFormate1(String strDate) {
		try {
			return FORMATE_1.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

}
