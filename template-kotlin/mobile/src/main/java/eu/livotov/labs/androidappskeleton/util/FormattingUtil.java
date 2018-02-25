package eu.livotov.labs.androidappskeleton.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import eu.livotov.labs.androidappskeleton.R;
import eu.livotov.labs.androidappskeleton.core.App;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 03/08/2017
 */
public class FormattingUtil
{

    private static SimpleDateFormat dateAndTimeFormatter = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    private static SimpleDateFormat dateAndTimeWithSecondsFormatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    private static SimpleDateFormat chatMessageDateTimeOnlyFormatter = new SimpleDateFormat("HH:mm");
    private static SimpleDateFormat timeOnlyFormatter = new SimpleDateFormat("HH:mm");

    public static String formatDateForEventFeeds(final Date date)
    {
        if (date != null)
        {
            if (DateUtil.isToday(date))
            {
                return (System.currentTimeMillis() - date.getTime()) < DateUtil.MILLIS_PER_MINUTE ? App.getContext().getString(R.string.now) : timeOnlyFormatter.format(date);
            }
            else if (DateUtil.isYesterday(date))
            {
                return App.getContext().getString(R.string.yesterday, timeOnlyFormatter.format(date));
            }
            else
            {
                return dateAndTimeFormatter.format(date);
            }
        }
        else
        {
            return "";
        }
    }

    public static String formatFutureDateAsCountdown(final long millis)
    {
        long difference = millis - System.currentTimeMillis();

        if (difference > 0)
        {
            final int seconds = (int) (difference / 1000) % 60;
            final int minutes = (int) ((difference / (1000 * 60)) % 60);
            final int hours = (int) ((difference / (1000 * 60 * 60)) % 24);

            StringBuilder builder = new StringBuilder();

            if (hours > 0)
            {
                builder.append(App.getContext().getResources().getQuantityString(R.plurals.hours, hours, hours)).append(" ");
            }

            if (minutes > 0)
            {
                builder.append(App.getContext().getResources().getQuantityString(R.plurals.minutes, minutes, minutes)).append(" ");
            }

            if (seconds >= 0)
            {
                builder.append(App.getContext().getResources().getQuantityString(R.plurals.seconds, seconds, seconds));
            }

            return builder.toString();
        }
        else
        {
            return "";
        }
    }
}
