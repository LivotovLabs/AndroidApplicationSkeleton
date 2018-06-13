package eu.livotov.labs.androidappskeleton

import android.content.SharedPreferences

object AppSettings
{
    val prefs: SharedPreferences = App.self.getSharedPreferences(App.packageId, 0)

    var testData: Int
        get() = prefs.getInt("test", 0)
        set(value) = prefs.edit().putInt("test", value).apply()

    var lastDeliveredFcmToken: String
        get() = prefs.getString("fcm", "")
        set(value) = prefs.edit().putString("fcm", value).apply()
}
