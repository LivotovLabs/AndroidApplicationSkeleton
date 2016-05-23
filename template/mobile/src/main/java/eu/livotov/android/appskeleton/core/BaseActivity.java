package eu.livotov.android.appskeleton.core;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import eu.livotov.android.appskeleton.core.App;
import eu.livotov.android.appskeleton.event.EventForceFinishActivity;
import eu.livotov.android.appskeleton.event.EventGenericError;
import eu.livotov.android.appskeleton.event.EventUITaskProgressUpdate;
import eu.livotov.android.appskeleton.event.EventUITaskStarted;
import eu.livotov.android.appskeleton.task.EventUITaskCompleted;

/**
 * Created by dlivotov on 09/02/2016.
 */
public class BaseActivity extends AppCompatActivity
{
    private List<String> runningTasks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        App.subscribeForSystemEvents(this);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        App.subscribe(this);
    }

    @Override
    protected void onPause()
    {
        App.unsubscribe(this);
        super.onPause();
    }

    @Override
    protected void onDestroy()
    {
        App.unsubscribeFromSystemEvents(this);
        super.onDestroy();
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
        App.postSystemEvent(new EventForceFinishActivity(getClass()));
    }

    public void finishAllInstancesButMe()
    {
        App.postSystemEvent(new EventForceFinishActivity(this));
    }
}
