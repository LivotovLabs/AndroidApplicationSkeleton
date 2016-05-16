package eu.livotov.android.appskeleton;

import org.greenrobot.eventbus.EventBus;

import eu.livotov.android.appskeleton.util.AppSettings;
import eu.livotov.labs.android.robotools.app.RTApp;

/**
 * Created by dlivotov on 09/02/2016.
 */
public class App extends RTApp
{
    private static App INSTANCE;

    private AppSettings settings;

    @Override
    public void onCreate()
    {
        super.onCreate();
        init();
        INSTANCE = this;
    }

    private void init()
    {
        settings = new AppSettings(this);
    }

    public static App getContext()
    {
        return INSTANCE;
    }

    public static void postEvent(Object event)
    {
        EventBus.getDefault().post(event);
    }

    public static void postStickyEvent(Object event)
    {
        EventBus.getDefault().postSticky(event);
    }

    public static synchronized void subscribe(Object eventsListener)
    {
        if (!EventBus.getDefault().isRegistered(eventsListener))
        {
            EventBus.getDefault().register(eventsListener);
        }
    }

    public static synchronized void unsubscribe(Object eventListener)
    {
        if (EventBus.getDefault().isRegistered(eventListener))
        {
            EventBus.getDefault().unregister(eventListener);
        }
    }
}
