package net.netca.pdfconvert.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

	public static String getDate() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String s = simpleDateFormat.format(new Date());
		return s;
	}
}