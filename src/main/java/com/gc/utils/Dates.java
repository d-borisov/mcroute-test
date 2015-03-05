package com.gc.utils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Dates {

    private static final String DATE_TIME = "yyyy-MM-dd HH:mm:ss";
    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(DATE_TIME);

    private static final String DATE_TIME_TO_DB = "yyyy-MM-dd HH:mm:ss";
    private static final DateTimeFormatter FORMATTER_FROM_DB = DateTimeFormat.forPattern(DATE_TIME_TO_DB);

    public static DateTime now() {
        return DateTime.now();
    }

    public static String nowDateTime() {
        return now().toString(DATE_TIME);
    }

    public static String date(Date date) {
        return date != null ? DATE_FORMATTER.format(date) : "";
    }

    public static String dateTimeToDb(DateTime dateTime) {
        return dateTime.toString(DATE_TIME_TO_DB);
    }

    public static DateTime dateTimeFromDb(String string) {
        return DateTime.parse(string, FORMATTER_FROM_DB);
    }
}
