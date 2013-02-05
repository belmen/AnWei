package com.belmen.anwei.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class DateParser {

	private static Map<String, SimpleDateFormat> formatMap =
			new HashMap<String, SimpleDateFormat>();
	
	public static Date parse(String str, String format) {
		if (str == null || "".equals(str)) {
			return new Date(System.currentTimeMillis());
		}
		SimpleDateFormat sdf = formatMap.get(format);
		if (null == sdf) {
			sdf = new SimpleDateFormat(format, Locale.US);
			sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
			formatMap.put(format, sdf);
		}
		try {
			synchronized (sdf) {
				// SimpleDateFormat is not thread safe
				return sdf.parse(str);
			}
		} catch (ParseException pe) {
			throw new IllegalArgumentException("Unexpected date format: " + str);
		}
	}
	
	public static String fixTime(String timestamp) {
		if (timestamp == null || "".equals(timestamp)) {
			return "";
		}

		try {
			long _timestamp = Long.parseLong(timestamp) * 1000;
			if (System.currentTimeMillis() - _timestamp < 1 * 60 * 1000) {
				return ((System.currentTimeMillis() - _timestamp) / 1000) + "秒前";
			} else if (System.currentTimeMillis() - _timestamp < 30 * 60 * 1000) {
				return ((System.currentTimeMillis() - _timestamp) / 1000 / 60)
						+ "分钟前";
			} else {
				Calendar now = Calendar.getInstance();
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(_timestamp);
				if (c.get(Calendar.YEAR) == now.get(Calendar.YEAR)
						&& c.get(Calendar.MONTH) == now.get(Calendar.MONTH)
						&& c.get(Calendar.DATE) == now.get(Calendar.DATE)) {
					SimpleDateFormat sdf = new SimpleDateFormat("今天 HH:mm");
					return sdf.format(c.getTime());
				}
				if (c.get(Calendar.YEAR) == now.get(Calendar.YEAR)
						&& c.get(Calendar.MONTH) == now.get(Calendar.MONTH)
						&& c.get(Calendar.DATE) == now.get(Calendar.DATE) - 1) {
					SimpleDateFormat sdf = new SimpleDateFormat("昨天 HH:mm");
					return sdf.format(c.getTime());
				} else if (c.get(Calendar.YEAR) == now.get(Calendar.YEAR)) {
					SimpleDateFormat sdf = new SimpleDateFormat("M月d日 HH:mm:ss");
					return sdf.format(c.getTime());
				} else {
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy年M月d日");
					return sdf.format(c.getTime());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
}
