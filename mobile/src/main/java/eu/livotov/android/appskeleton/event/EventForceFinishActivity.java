package eu.livotov.android.appskeleton.event;

import eu.livotov.android.appskeleton.screen.BaseActivity;

/**
 * Created by dlivotov on 09/02/2016.
 */
public class EventForceFinishActivity
{
    private BaseActivity instanceToKeep;
    private Class activityClassToKillAll;

    public EventForceFinishActivity()
    {
    }

    public EventForceFinishActivity(Class activityClassToKillAll)
    {
        this.activityClassToKillAll = activityClassToKillAll;
    }

    public EventForceFinishActivity(BaseActivity instanceToKeep)
    {
        activityClassToKillAll = instanceToKeep.getClass();
        this.instanceToKeep = instanceToKeep;
    }

    public Class getActivityClassToKillAll()
    {
        return activityClassToKillAll;
    }

    public boolean matches(BaseActivity activity)
    {
        if (instanceToKeep !=null && instanceToKeep == activity)
        {
            return false;
        } else
        {
            return activityClassToKillAll == null || activityClassToKillAll.equals(activity.getClass());
        }
    }
}
