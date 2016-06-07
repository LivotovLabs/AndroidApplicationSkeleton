package eu.livotov.android.appskeleton.core;

import android.os.Bundle;

import eu.inloop.easygcm.EasyGcm;
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

        // Remove if you don't need the GCM support
        EasyGcm.init(this);
    }

    @Override
    public void onMessage(String sender, Bundle bundle)
    {

    }

    @Override
    public void sendRegistrationIdToBackend(String token)
    {

    }
}
