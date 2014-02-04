package util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Utils {
	private final static Logger LOGGER = Logger
			.getLogger(Utils.class.getName());

	private Utils() {
		throw new AssertionError();
	}

	public static Date convertDateFrom(String dateString) {
		DateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		Date date = null;
		if (!dateString.isEmpty()) {
			try {
				date = dataFormat.parse(dateString);
			} catch (ParseException e) {
				LOGGER.log(Level.WARNING, "Date parsing error", e.getLocalizedMessage());
			}
		}
		return date;
	}

	public static String convertDateToString(Date date) {
		DateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		String res = dataFormat.format(date);
		return res;
	}
}
