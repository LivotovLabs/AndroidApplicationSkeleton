package eu.livotov.android.appskeleton.event.system;

import eu.livotov.android.appskeleton.task.UITask;

/**
 * Created by dlivotov on 09/02/2016.
 */
public class UITaskCompletedEvent
{
    private final UITask task;

    public UITaskCompletedEvent(UITask task)
    {
        this.task = task;
    }

    public UITask getTask()
    {
        return task;
    }
}
