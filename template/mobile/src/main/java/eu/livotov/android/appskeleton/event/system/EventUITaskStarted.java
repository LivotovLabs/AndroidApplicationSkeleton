package eu.livotov.android.appskeleton.event.system;

/**
 * Created by dlivotov on 09/02/2016.
 */
public class EventUITaskStarted
{
    private final String id;

    public EventUITaskStarted(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return id;
    }
}
