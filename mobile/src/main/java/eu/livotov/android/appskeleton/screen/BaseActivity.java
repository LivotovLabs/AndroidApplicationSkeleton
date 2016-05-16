package eu.livotov.android.appskeleton.screen;

import android.os.Bundle;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import eu.livotov.android.appskeleton.App;
import eu.livotov.android.appskeleton.event.EventForceFinishActivity;
import eu.livotov.android.appskeleton.event.EventGenericError;
import eu.livotov.android.appskeleton.event.EventUITaskProgressUpdate;
import eu.livotov.android.appskeleton.event.EventUITaskStarted;
import eu.livotov.android.appskeleton.task.EventUITaskCompleted;
import eu.livotov.labs.android.robotools.app.RTActivity;

/**
 * Created by dlivotov on 09/02/2016.
 */
public class BaseActivity extends RTActivity
{
    private List<String> runningTasks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        App.subscribe(this);
    }

    @Override
    protected void onResume()
    {
        App.unsubscribe(this);
        super.onResume();
    }

    @Subscribe
    public void forceQuitEvent(EventForceFinishActivity data)
    {
        if (data.matches(this))
        {
            finish();
        }
    }

    @Subscribe
    public void onGenericError(EventGenericError error)
    {
    }

    @Subscribe
    public void onUITaskStarted(EventUITaskStarted task)
    {
        runningTasks.add(task.getId());
    }

    @Subscribe
    public void onUITaskFinished(EventUITaskCompleted task)
    {
        runningTasks.remove(task.getId());
    }

    @Subscribe
    public void onUITaskProgressUpdate(EventUITaskProgressUpdate task)
    {

    }

    public void finishAllInstances()
    {
        App.postEvent(new EventForceFinishActivity(getClass()));
    }

    public void finishAllInstancesButMe()
    {
        App.postEvent(new EventForceFinishActivity(this));
    }
}
