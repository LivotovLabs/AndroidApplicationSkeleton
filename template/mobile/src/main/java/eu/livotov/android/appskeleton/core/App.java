package eu.livotov.android.appskeleton.core;

import android.support.v7.app.AppCompatDelegate;

import eu.livotov.android.appskeleton.core.base.BaseApp;

public class App extends BaseApp
{

    @Override
    public void onCreate()
    {
        super.onCreate();

        // Allow native VectorDrawable support for pre-lollipops
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

}
