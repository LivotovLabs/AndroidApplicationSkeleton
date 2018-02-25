package eu.livotov.labs.androidappskeleton.core.base;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.multidex.MultiDex;
import android.support.v4.content.ContextCompat;

import org.greenrobot.eventbus.EventBus;

import eu.livotov.labs.android.robotools.app.RTApp;
import eu.livotov.labs.androidappskeleton.core.App;

/**
 * Created by dlivotov on 09/02/2016.
 */
public class BaseApp extends RTApp
{

    public static void postStickyEvent(Object event)
    {
        EventBus.getDefault().postSticky(event);
    }

    public static void subscribe(Object eventsListener)
    {
        final EventBus bus = EventBus.getDefault();
        if (!bus.isRegistered(eventsListener))
        {
            bus.register(eventsListener);
        }
    }

    public static void unsubscribe(Object eventListener)
    {
        final EventBus bus = EventBus.getDefault();
        if (bus.isRegistered(eventListener))
        {
            bus.unregister(eventListener);
        }
    }

    public static boolean checkPermissions(final String... permissions)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            for (String permission : permissions)
            {
                if (ContextCompat.checkSelfPermission(getContext(), permission) != PackageManager.PERMISSION_GRANTED)
                {
                    return false;
                }
            }
        }

        return true;
    }

    public static void postEvent(Object event)
    {
        EventBus.getDefault().post(event);
    }

    public static int getVersionCode()
    {
        try
        {
            return App.getContext().getPackageManager().getPackageInfo(App.getContext().getPackageName(), 0).versionCode;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            return 0;
        }
    }

    @Nullable
    public static String getVersionName()
    {
        try
        {
            return App.getContext().getPackageManager().getPackageInfo(App.getContext().getPackageName(), 0).versionName;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            return null;
        }
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        init();
    }

    private void init()
    {
        EventBus.builder().eventInheritance(false).logNoSubscriberMessages(false).sendNoSubscriberEvent(false).throwSubscriberException(false).logSubscriberExceptions(true).installDefaultEventBus();
    }

    @Override
    protected void attachBaseContext(Context base)
    {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
