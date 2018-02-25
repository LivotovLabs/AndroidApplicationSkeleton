import eu.livotov.labs.androidappskeleton.App
import eu.livotov.labs.androidappskeleton.R
import java.text.SimpleDateFormat
import java.util.*

internal val appDateAndTimeFormatter = SimpleDateFormat("dd.MM.yyyy HH:mm")
internal val appDateAndTimeWithSecondsFormatter = SimpleDateFormat("dd.MM.yyyy HH:mm:ss")
internal val appChatMessageDateTimeOnlyFormatter = SimpleDateFormat("HH:mm")
internal val appTimeOnlyFormatter = SimpleDateFormat("HH:mm")

fun Date.formatDateForEventFeeds(): String
{
    return if (isToday())
    {
        if (System.currentTimeMillis() - time < MILLIS_PER_MINUTE) App.self.getString(R.string.now) else appTimeOnlyFormatter.format(this)
    } else if (isYesterday())
    {
        App.self.getString(R.string.yesterday, appTimeOnlyFormatter.format(this))
    } else
    {
        appDateAndTimeFormatter.format(this)
    }
}

fun Date.formatAsCountdown(): String
{
    val difference = time - System.currentTimeMillis()

    return if (difference > 0)
    {
        val seconds = (difference / 1000).toInt() % 60
        val minutes = (difference / (1000 * 60) % 60).toInt()
        val hours = (difference / (1000 * 60 * 60) % 24).toInt()

        val builder = StringBuilder()

        if (hours > 0)
        {
            builder.append(App.self.getResources().getQuantityString(R.plurals.hours, hours, hours)).append(" ")
        }

        if (minutes > 0)
        {
            builder.append(App.self.getResources().getQuantityString(R.plurals.minutes, minutes, minutes)).append(" ")
        }

        if (seconds >= 0)
        {
            builder.append(App.self.getResources().getQuantityString(R.plurals.seconds, seconds, seconds))
        }

        builder.toString()
    } else
    {
        ""
    }
}