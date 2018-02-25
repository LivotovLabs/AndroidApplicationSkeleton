package eu.livotov.labs.androidappskeleton.util;

import java.util.Calendar;
import java.util.Date;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 03/08/2017
 */
public class DateUtil
{

    public static final long MILLIS_PER_SECOND = 1000;
    /**
     * Number of milliseconds in a standard minute.
     *
     * @since 2.1
     */
    public static final long MILLIS_PER_MINUTE = 60 * MILLIS_PER_SECOND;
    /**
     * Number of milliseconds in a standard hour.
     *
     * @since 2.1
     */
    public static final long MILLIS_PER_HOUR = 60 * MILLIS_PER_MINUTE;
    /**
     * Number of milliseconds in a standard day.
     *
     * @since 2.1
     */
    public static final long MILLIS_PER_DAY = 24 * MILLIS_PER_HOUR;

    public static boolean isYesterday(Date date)
    {
        java.util.Calendar cal1 = java.util.Calendar.getInstance();
        cal1.add(java.util.Calendar.DAY_OF_YEAR, -1);

        java.util.Calendar cal2 = java.util.Calendar.getInstance();
        cal2.setTime(date);

        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

    public static boolean isToday(Date date)
    {
        java.util.Calendar cal1 = java.util.Calendar.getInstance();
        java.util.Calendar cal2 = java.util.Calendar.getInstance();
        cal2.setTime(date);

        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

    public static boolean isSameDay(Date date1, Date date2)
    {
        final long julianDayNumber1 = date1.getTime() / MILLIS_PER_DAY;
        final long julianDayNumber2 = date2.getTime() / MILLIS_PER_DAY;
        return julianDayNumber1 == julianDayNumber2;
    }
}
