package eu.livotov.android.appskeleton.event.system;

import eu.livotov.android.appskeleton.activity.base.BaseActivity;

/**
 * Created by dlivotov on 09/02/2016.
 */
public class EventForceFinishActivity
{
    private BaseActivity instanceToKeep;
    private Class activityClassToFinishAllInstancesOf;

    public EventForceFinishActivity()
    {
    }

    public EventForceFinishActivity(Class classToFinishAllInstancesOf)
    {
        this.activityClassToFinishAllInstancesOf = classToFinishAllInstancesOf;
    }

    public EventForceFinishActivity keepInstanceOf(BaseActivity instanceToKeep)
    {
        this.instanceToKeep = instanceToKeep;
        return this;
    }

    public Class getActivityClassToFinishAllInstancesOf()
    {
        return activityClassToFinishAllInstancesOf;
    }

    public boolean matches(BaseActivity activity)
    {
        if (instanceToKeep !=null && instanceToKeep == activity)
        {
            return false;
        } else
        {
            return activityClassToFinishAllInstancesOf == null || activityClassToFinishAllInstancesOf.equals(activity.getClass());
        }
    }
}
