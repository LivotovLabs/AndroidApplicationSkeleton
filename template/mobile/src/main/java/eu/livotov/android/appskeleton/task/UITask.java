package eu.livotov.android.appskeleton.task;

import java.util.UUID;

import eu.livotov.android.appskeleton.core.App;
import eu.livotov.android.appskeleton.event.system.EventUITaskProgressUpdate;
import eu.livotov.android.appskeleton.event.system.EventUITaskStarted;
import eu.livotov.labs.android.robotools.os.RTLongTermUITask;

/**
 * Created by dlivotov on 09/02/2016.
 */
public abstract class UITask extends RTLongTermUITask
{
    protected String id = UUID.randomUUID().toString();
    protected float currentProgress;

    public String getId()
    {
        return id;
    }

    @Override
    public Object buildProgressEvent(ProgressUpdateType state)
    {
        switch (state)
        {
            case OperationProgressUpdate:
                return new EventUITaskProgressUpdate(id, currentProgress);

            case OperationStart:
                return new EventUITaskStarted(id);

            case OperationStopped:
                return new EventUITaskCompleted(id);

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
}
