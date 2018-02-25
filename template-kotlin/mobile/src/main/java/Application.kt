package eu.livotov.labs.androidappskeleton

import android.app.Application
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build
import android.support.annotation.StringRes
import android.support.multidex.MultiDex
import android.support.v4.content.ContextCompat
import android.widget.Toast
import com.arellomobile.mvp.MvpFacade
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import org.greenrobot.eventbus.EventBus

class App : Application()
{
    override fun onCreate()
    {
        super.onCreate()
        self = this
        initApp()
    }

    override fun attachBaseContext(context: Context)
    {
        super.attachBaseContext(context)
        MultiDex.install(this)
    }

    private fun initApp()
    {
        EventBus.builder().eventInheritance(false).logNoSubscriberMessages(false).sendNoSubscriberEvent(false).throwSubscriberException(false).logSubscriberExceptions(true).installDefaultEventBus()
        MvpFacade.init()
    }

    companion object
    {
        lateinit var self: App
        val debuggable by lazy { (self.packageManager.getApplicationInfo(self.packageName, PackageManager.GET_META_DATA).flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0 }
        val versionCode by lazy { self.packageManager.getPackageInfo(self.packageName, 0).versionCode }
        val versionName by lazy { self.packageManager.getPackageInfo(self.packageName, 0).versionName }
        val packageId by lazy { self.packageManager.getPackageInfo(self.packageName, 0).packageName }
        val settings by lazy { Prefs() }

        fun toast(text: String) = Toast.makeText(self, text, Toast.LENGTH_SHORT)
        fun longToast(text: String) = Toast.makeText(self, text, Toast.LENGTH_LONG)
        fun toast(@StringRes textRes: Int) = Toast.makeText(self, textRes, Toast.LENGTH_SHORT)
        fun longToast(@StringRes textRes: Int) = Toast.makeText(self, textRes, Toast.LENGTH_LONG)

        fun subscribe(subscriber: Any) = if (!EventBus.getDefault().isRegistered(subscriber)) EventBus.getDefault().register(subscriber) else Unit
        fun unsubscribe(subscriber: Any) = if (EventBus.getDefault().isRegistered(subscriber)) EventBus.getDefault().unregister(subscriber) else Unit
        fun postEvent(event: Any) = EventBus.getDefault().post(event)
        fun postStickyEvent(event: Any) = EventBus.getDefault().postSticky(event)

        fun hasPermissions(vararg permissions: String): Boolean
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                for (permission in permissions)
                {
                    if (ContextCompat.checkSelfPermission(self, permission) != PackageManager.PERMISSION_GRANTED)
                    {
                        return false
                    }
                }
            }

            return true
        }
    }
}

@GlideModule
class GlideModule : AppGlideModule()
