import java.util.*

internal val MILLIS_PER_SECOND: Long = 1000
internal val MILLIS_PER_MINUTE = 60 * MILLIS_PER_SECOND
internal val MILLIS_PER_HOUR = 60 * MILLIS_PER_MINUTE
internal val MILLIS_PER_DAY = 24 * MILLIS_PER_HOUR

fun Date.isYesterday(): Boolean
{
    val cal1 = java.util.Calendar.getInstance()
    cal1.add(java.util.Calendar.DAY_OF_YEAR, -1)

    val cal2 = java.util.Calendar.getInstance()
    cal2.time = this

    return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
}

fun Date.isToday(): Boolean
{
    val cal1 = java.util.Calendar.getInstance()
    val cal2 = java.util.Calendar.getInstance()
    cal2.time = this

    return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
}

fun Date.sameDayAs(date: Date): Boolean
{
    val julianDayNumber1 = this.time / MILLIS_PER_DAY
    val julianDayNumber2 = date.time / MILLIS_PER_DAY
    return julianDayNumber1 == julianDayNumber2
}