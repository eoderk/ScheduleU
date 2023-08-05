package com.evanknight.scheduleu.util;

import static com.evanknight.scheduleu.util.Constants.BAD_DATE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.logging.Logger;


public class EasyDate {
    private static Calendar c = Calendar.getInstance();
    private static final int MAX_TRACE_ELEMENTS = 20;
    public static final int YEAR = 0;
    public static final int MONTH = 1;
    public static final int DAY = 2;

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
    public static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z", Locale.US);
    public static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:DD:SS z", Locale.US);

    public static Date TODAY = today();

    private static Date today(){
        reset();
        return c.getTime();
    }

    public static long getDate(@NonNull String dateString) {
//        String dateString = year + "-" + month + "-" + dayOfMonth;
        c.getTimeInMillis();
        String exceptionMSG = "Exception";
        StackTraceElement[] stackTrace = new StackTraceElement[MAX_TRACE_ELEMENTS];
        try {
            c.setTime(Objects.requireNonNull(DATE_FORMAT.parse(dateString)));
        } catch (ParseException ex) {
            exceptionMSG = "Parse Date Exception: Invalid date string format";
            stackTrace = ex.getStackTrace();
        } catch (NullPointerException ex) {
            exceptionMSG = "Mull Pointer Exception: dateString not set to an instance of an Object";
            stackTrace = ex.getStackTrace();
        } finally {
            Logger.getLogger("EasyDate").warning(exceptionMSG);
            Logger.getLogger("EasyDate[Stack Trace]").warning(stackTrace.toString());
        }
        return c.getTimeInMillis();
    }

    public static String getFormat_ddMMMyyyy(int year, int month, int day){
        return day + "-" + month + "-" + year;
    }

    public static long getDate(int year, int month, int day){
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        long returnDate = c.getTimeInMillis();
        reset();
        return returnDate;
    }

    public static String getFormat_ddMMMyyyy(@Nullable String s){
        return "";
    }
    public static String getFormat_ddMMMyyyy(long d) {
        try {
            return DATE_FORMAT.format(d);
        } catch (Exception ex){
            Logger.getGlobal().severe("!!!!!!!!!!!!!!!!!! " + ex.getLocalizedMessage() + "!!!!!!!!!!!!!!!!!!!!!!");
            Logger.getLogger("EasyDate").severe(ex.getStackTrace().toString());
        }
        return null;
    }

    public static long todayInMilli() {
        reset();
        return TODAY.getTime();
    }

    public static long getDateInMilli(String dateInput) {
        if (!dateInput.isEmpty()) {
            try {
                Date date = EasyDate.DATE_FORMAT.parse(dateInput);
                if (todayInMilli() <= date.getTime()) {
                    return date.getTime();
                } else {
                    Logger.getLogger("EasyDate").warning("[getDateInMilli] Parse Error: String value set to a date in the past" + Utils.formattedTrace(Thread.currentThread().getStackTrace()) );
                }
            } catch (ParseException ex) {
                Logger.getLogger("EasyDate").warning("Parse Error: " + Utils.formattedTrace(ex.getStackTrace()));
            } catch (NullPointerException ex){
                Logger.getLogger("EasyDate").warning("Null Pointer: Date string provided not set to an instance of String\n" + Utils.formattedTrace(ex.getStackTrace()));
            }
        } else {
            Logger.getLogger("EasyDate").warning("[getDateInMilli] Parse Error: String value was empty");
        }
        return BAD_DATE;
    }

    public static long getDateInMilli(Date d){
        c.setTime(d);
        long milliTime = c.getTimeInMillis();
        reset();
        return milliTime;
    }

    public static long addMonths(long d, int numberOfMonths){
        c.setTimeInMillis(d);
        c.add(Calendar.MONTH, numberOfMonths);
        long returnDate = c.getTimeInMillis();
        reset();
        return returnDate;
    }

    public static int[] getDateAsYMD(long d){
        c.setTimeInMillis(d);
        Logger.getLogger("EasyDate").warning("Getting YMD from long="+ d + ": Y=" + c.get(Calendar.YEAR) +
                ", M=" + c.get(Calendar.MONTH) +
                ", D=" + c.get(Calendar.DAY_OF_MONTH));
        int[] date = {c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH) };
        reset();
        return date;
    }

    public static void reset(){
        if (c.getTime() != TODAY){
            //c.setTimeInMillis(System.currentTimeMillis());
            c = Calendar.getInstance(Locale.US);
            TODAY = c.getTime();
        }
    }
}
