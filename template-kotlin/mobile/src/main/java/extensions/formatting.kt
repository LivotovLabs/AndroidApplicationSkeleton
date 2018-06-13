import eu.livotov.labs.androidappskeleton.App
import eu.livotov.labs.androidappskeleton.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.getOrSet

internal val appDateAndTimeFormatter = ThreadLocal<SimpleDateFormat>()
internal val appTimeOnlyFormatter = ThreadLocal<SimpleDateFormat>()

private fun appDateAndTimeFormatterBuilder() = SimpleDateFormat("dd.MM.yyyy HH:mm")
private fun appTimeOnlyFormatterBuilder() = SimpleDateFormat("HH:mm")

fun Date.formatDateForEventFeeds(): String
{
    return if (isToday())
    {
        if (System.currentTimeMillis() - time < MILLIS_PER_MINUTE) App.self.getString(R.string.now) else appTimeOnlyFormatter.getOrSet { appTimeOnlyFormatterBuilder() }.format(this)
    } else if (isYesterday())
    {
        App.self.getString(R.string.yesterday, appTimeOnlyFormatter.getOrSet { appTimeOnlyFormatterBuilder() }.format(this))
    } else
    {
        appDateAndTimeFormatter.getOrSet { appDateAndTimeFormatterBuilder() }.format(this)
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