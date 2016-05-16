package eu.livotov.android.appskeleton.event;

import android.text.TextUtils;

import eu.livotov.android.appskeleton.screen.BaseActivity;

/**
 * Created by dlivotov on 09/02/2016.
 */
public class EventForceFinishActivity
{
    private BaseActivity exception;
    private Class activityClass;

    public EventForceFinishActivity()
    {
    }

    public EventForceFinishActivity(Class activityClass)
    {
        this.activityClass = activityClass;
    }

    public EventForceFinishActivity(BaseActivity instanceToKeep)
    {
        activityClass = instanceToKeep.getClass();
        exception = instanceToKeep;
    }

    public Class getActivityClass()
    {
        return activityClass;
    }

    public boolean matches(BaseActivity activity)
    {
        if (exception !=null && exception == activity)
        {
            return false;
        } else
        {
            return activityClass == null || activityClass.equals(activity.getClass());
        }
    }
}
