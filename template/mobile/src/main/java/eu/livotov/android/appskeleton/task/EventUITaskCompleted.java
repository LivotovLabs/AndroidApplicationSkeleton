package eu.livotov.android.appskeleton.task;

/**
 * Created by dlivotov on 09/02/2016.
 */
public class EventUITaskCompleted
{
    private final String id;

    public EventUITaskCompleted(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return id;
    }
}
