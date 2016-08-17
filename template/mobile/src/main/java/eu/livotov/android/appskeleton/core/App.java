package eu.livotov.android.appskeleton.core;

import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;

import eu.inloop.easygcm.GcmListener;
import eu.livotov.android.appskeleton.core.base.BaseApp;

/**
 * Created by dlivotov on 09/02/2016.
 */
public class App extends BaseApp implements GcmListener
{

    @Override
    public void onCreate()
    {
        super.onCreate();

        // Allow native VectorDrawable support for pre-lollipops
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        // Uncomment if you need the GCM support. Also, do not forget to generate your own google-services.json file and add some business code into the two methods below :)
        // EasyGcm.init(this);
    }

    /**
     * Incoming push message is received here in a background thread, locked by a WakeLock.
     * Process your message here without spawning any other addition threads.
     * Do not spawn any other threads from here.
     *
     * @param sender push sender ID
     * @param bundle push message payload.
     */
    @Override
    public void onMessage(String sender, Bundle bundle)
    {

    }

    /**
     * Send the supplied push token to your backend server here.
     * The method is invoked in a background thread, locked by a WakeLock, so feel free to access network from there.
     * Do not spawn any other threads from here.
     *
     * @param token new push token to be sent to your own backend server
     */
    @Override
    public void sendRegistrationIdToBackend(String token)
    {

    }

}
