package com.message.gomessaging.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * DateUtils
 * Created by Akanksha on 24/4/16.
 */
public class DateUtils {

    private static List<SimpleDateFormat> knownPatterns = new ArrayList<>();

    private static void init() {
        knownPatterns.add(new SimpleDateFormat("EEEE MMM dd hh:mm:ss z yyyy", Locale.getDefault()));
    }

    public static Date getDateTime(String convertDate) {
        init();
        Date convertedDate = null;
        for (SimpleDateFormat pattern : knownPatterns) {
            try {
                convertedDate = pattern.parse(convertDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return convertedDate;
    }

    public static String getDisplayDatePattern(Date date, String pattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern, Locale.getDefault());
        return formatter.format(date);
    }

    public static String getDisplayDate(Date date) {
        long difference;
        Long mDate = System.currentTimeMillis();
        long delta = date.getTime();
        if (mDate > delta) {
            difference = mDate - delta;
            final long seconds = difference / 1000;
            final long minutes = seconds / 60;
            final long hours = minutes / 60;

            if (seconds < 0) {
                return "Not Yet";
            } else if (seconds < 60) {
                return "1 second ago";
            } else if (seconds < 120) {
                return "1 minute ago";
            } else if (seconds < 2700) // 45 * 60
            {
                return minutes + " minutes ago";
            } else if (seconds < 5400) // 90 * 60
            {
                return "1 hour ago";
            } else if (seconds < 86400) // 24 * 60 * 60
            {
                return hours + " hours ago";
            } else if (seconds < 172800) // 48 * 60 * 60
            {
                return "Yesterday";
            } else {
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MMM/yyyy", Locale.getDefault());
                return formatter.format(date);
            }
        }
        return null;
    }
}
