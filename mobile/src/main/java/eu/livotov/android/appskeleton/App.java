package eu.livotov.android.appskeleton;

import android.content.Context;
import android.support.multidex.MultiDex;

import org.greenrobot.eventbus.EventBus;

import eu.livotov.android.appskeleton.util.AppSettings;
import eu.livotov.labs.android.robotools.app.RTApp;

/**
 * Created by dlivotov on 09/02/2016.
 */
public class App extends RTApp
{
    private AppSettings settings;
    private EventBus systemEventBus;

    @Override
    public void onCreate()
    {
        super.onCreate();
        init();
    }

    @Override
    protected void attachBaseContext(Context base)
    {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void init()
    {
        settings = new AppSettings(this);
        systemEventBus = EventBus.builder().eventInheritance(false).logNoSubscriberMessages(false).sendNoSubscriberEvent(false).throwSubscriberException(false).logSubscriberExceptions(true).build();
        EventBus.builder().eventInheritance(false).logNoSubscriberMessages(false).sendNoSubscriberEvent(false).throwSubscriberException(false).logSubscriberExceptions(true).installDefaultEventBus();
    }

    public static AppSettings getSettings()
    {
        return ((App) getInstance()).settings;
    }

    public static void postEvent(Object event)
    {
        EventBus.getDefault().post(event);
    }

    public static void postStickyEvent(Object event)
    {
        EventBus.getDefault().postSticky(event);
    }

    public static void postSystemEvent(Object event)
    {
        ((App) getInstance()).systemEventBus.post(event);
    }

    public static void postSystemStickyEvent(Object event)
    {
        ((App) getInstance()).systemEventBus.postSticky(event);
    }

    public static synchronized void subscribe(Object eventsListener)
    {
        final EventBus bus = EventBus.getDefault();
        if (!bus.isRegistered(eventsListener))
        {
            bus.register(eventsListener);
        }
    }

    public static synchronized void unsubscribe(Object eventListener)
    {
        final EventBus bus = EventBus.getDefault();
        if (bus.isRegistered(eventListener))
        {
            bus.unregister(eventListener);
        }
    }

    public static synchronized void subscribeForSystemEvents(Object eventsListener)
    {
        final EventBus bus = ((App) getInstance()).systemEventBus;
        if (!bus.isRegistered(eventsListener))
        {
            bus.register(eventsListener);
        }
    }

    public static synchronized void unsubscribeFromSystemEvents(Object eventListener)
    {
        final EventBus bus = ((App) getInstance()).systemEventBus;
        if (bus.isRegistered(eventListener))
        {
            bus.unregister(eventListener);
        }
    }
}