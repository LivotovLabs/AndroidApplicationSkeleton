package eu.livotov.labs.androidappskeleton.core;

import com.arellomobile.mvp.MvpFacade;

import eu.livotov.labs.androidappskeleton.core.base.BaseApp;
import eu.livotov.labs.androidappskeleton.util.AppSettings;

public class App extends BaseApp
{

    protected AppSettings settings;

    public static AppSettings getSettings()
    {
        return ((App) getInstance()).settings;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();

        MvpFacade.init();

        // Init settings object
        settings = new AppSettings(this);
    }
}
