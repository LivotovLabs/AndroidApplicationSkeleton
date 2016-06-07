package eu.livotov.android.appskeleton.activity.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import eu.livotov.android.appskeleton.R;
import eu.livotov.android.appskeleton.core.App;
import eu.livotov.android.appskeleton.event.system.ForceFinishActivityEvent;
import eu.livotov.android.appskeleton.event.system.GenericErrorEvent;
import eu.livotov.android.appskeleton.event.system.UITaskProgressUpdateEvent;
import eu.livotov.android.appskeleton.event.system.UITaskStartedEvent;
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
    protected void onDestroy()
    {
        App.unsubscribeFromSystemEvents(this);
        super.onDestroy();
    }

    @Override
    protected void onPause()
    {
        App.unsubscribe(this);
        super.onPause();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        App.subscribe(this);
    }

    @Subscribe
    public void forceQuitEvent(ForceFinishActivityEvent data)
    {
        if (data.matches(this))
        {
            finish();
        }
    }

    @Subscribe
    public void onGenericError(GenericErrorEvent error)
    {
        App.showMessage(App.getContext().getString(R.string.generic_error_dialog_title), error.toString(), null);
    }

    @Subscribe
    public void onUITaskStarted(UITaskStartedEvent task)
    {
        runningTasks.add(task.getId());
    }

    @Subscribe
    public void onUITaskFinished(EventUITaskCompleted task)
    {
        runningTasks.remove(task.getId());
    }

    @Subscribe
    public void onUITaskProgressUpdate(UITaskProgressUpdateEvent task)
    {
    }

    public void finishAllInstances()
    {
        App.postSystemEvent(new ForceFinishActivityEvent(getClass()));
    }

    public void finishAllInstancesButThis()
    {
        App.postSystemEvent(new ForceFinishActivityEvent(getClass()).keepInstanceOf(this));
    }

}
