package com.geewhiz.eis.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

	private static SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ (EEEE)");

	public static String getCurrentTime() {
		return getFormattedDate(Calendar.getInstance().getTime());
	}

	public static String getFormattedDate(Date date) {
		return DATE_FORMATTER.format(date);
	}

	public static Date getDateFromFormattedString(String date) {
		try {
			return DATE_FORMATTER.parse(date);
		} catch (ParseException e) {
			throw new IllegalArgumentException("Couldnt parse date [" + date + "]", e);
		}
	}

}
