import android.app.Activity
import eu.livotov.labs.androidappskeleton.App

fun Activity.postEvent(event: Any) = App.postEvent(event)
fun Activity.postStickyEvent(event: Any) = App.postStickyEvent(event)
