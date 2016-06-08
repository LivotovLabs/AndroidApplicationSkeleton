package eu.livotov.android.appskeleton.task;

import eu.livotov.android.appskeleton.core.App;
import eu.livotov.android.appskeleton.event.system.UITaskCompletedEvent;
import eu.livotov.android.appskeleton.event.system.UITaskProgressUpdateEvent;
import eu.livotov.android.appskeleton.event.system.UITaskStartedEvent;
import eu.livotov.labs.android.robotools.os.RTLongTermUITask;

/**
 * Created by dlivotov on 09/02/2016.
 */
public abstract class UITask extends RTLongTermUITask
{
    protected float taskProgress;
    protected String taskTitle;
    protected String taskDescription;

    @Override
    public Object buildProgressEvent(ProgressUpdateType state)
    {
        switch (state)
        {
            case OperationProgressUpdate:
                return new UITaskProgressUpdateEvent(this);

            case OperationStart:
                return new UITaskStartedEvent(this);

            case OperationStopped:
                return new UITaskCompletedEvent(this);

            default:
                return null;
        }
    }

    @Override
    protected void publishEvent(Object event, boolean stickyDelivery)
    {
        if (stickyDelivery)
        {
            App.postStickyEvent(event);
        }
        else
        {
            App.postEvent(event);
        }
    }

    public float getTaskProgress()
    {
        return taskProgress;
    }

    public String getTaskTitle()
    {
        return taskTitle;
    }

    public String getTaskDescription()
    {
        return taskDescription;
    }
}
