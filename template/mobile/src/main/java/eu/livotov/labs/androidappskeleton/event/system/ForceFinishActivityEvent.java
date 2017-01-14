package eu.livotov.labs.androidappskeleton.event.system;

import eu.livotov.labs.androidappskeleton.activity.base.BaseActivity;

/**
 * Created by dlivotov on 09/02/2016.
 */
public class ForceFinishActivityEvent
{
    private BaseActivity instanceToKeep;
    private Class activityClassToFinishAllInstancesOf;
    private Object tag;

    public ForceFinishActivityEvent()
    {
    }

    public ForceFinishActivityEvent(Class classToFinishAllInstancesOf)
    {
        this.activityClassToFinishAllInstancesOf = classToFinishAllInstancesOf;
    }

    public ForceFinishActivityEvent keepInstanceOf(BaseActivity instanceToKeep)
    {
        this.instanceToKeep = instanceToKeep;
        return this;
    }

    public ForceFinishActivityEvent withTag(Object tag)
    {
        this.tag = tag;
        return this;
    }

    public Class getActivityClassToFinishAllInstancesOf()
    {
        return activityClassToFinishAllInstancesOf;
    }

    public boolean matches(BaseActivity activity)
    {
        if (instanceToKeep != null && instanceToKeep == activity)
        {
            return false;
        }
        else
        {
            if (tag != null)
            {
                return activity.getTag() != null && activity.getTag().equals(tag);
            }
            else
            {
                return activityClassToFinishAllInstancesOf == null || activityClassToFinishAllInstancesOf.equals(activity.getClass());
            }
        }
    }

}
