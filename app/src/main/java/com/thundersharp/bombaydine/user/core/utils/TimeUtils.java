package com.thundersharp.bombaydine.user.core.utils;

import android.text.format.DateFormat;

import androidx.annotation.NonNull;

import java.util.Calendar;
import java.util.Locale;

public class TimeUtils {

    public static String getTimeFromTimeStamp(long time){
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        String date = DateFormat.format("dd/MM/yyyy HH:MM", cal).toString();
        return date;
    }

    public static String getTimeFromTimeStamp(String timeStamp) throws NumberFormatException {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        long time;
        time = Long.parseLong(timeStamp);
        cal.setTimeInMillis(time);
        String date = DateFormat.format("dd/MM/yyyy HH:MM", cal).toString();
        return date;

    }

    public static String getDateFromTimeStamp(String timeStamp) throws NumberFormatException {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        long time;
        time = Long.parseLong(timeStamp);
        cal.setTimeInMillis(time);
        String date = DateFormat.format("dd-MM-yyyy", cal).toString();
        return date;

    }

    public static String getDateFromTimeStamp(long time){
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        String date = DateFormat.format("dd-MM-yyyy", cal).toString();
        return date;
    }

    public static long getTimeStampOfOriginDaysBeforeAfter(@NonNull int noOfDays){
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR); // current year
        int mMonth = c.get(Calendar.MONTH); // current month
        int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

        c.set(mYear,mMonth,mDay,00,00,00);
        c.set(Calendar.MILLISECOND,00);

        c.add(Calendar.DAY_OF_MONTH,noOfDays);
        return c.getTimeInMillis();
    }

    public static String getMonthName(int month){
        switch (month){
            case 0:
                return "January";
            case 1:
                return "February";

            case 2:
                return "March";

            case 3:
                return "April";
            case 4:
                return "May";

            case 5:
                return "June";

            case 6:
                return "July";

            case 7:
                return "August";

            case 8:
                return "September";

            case 9:
                return "October";

            case 10:
                return "November";

            case 11:
                return "December";

            default:
                return "Error";

        }
    }
}
