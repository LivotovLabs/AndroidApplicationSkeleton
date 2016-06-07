package eu.livotov.android.appskeleton.event.system;

/**
 * Created by dlivotov on 09/02/2016.
 */
public class UITaskStartedEvent
{
    private final String id;

    public UITaskStartedEvent(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return id;
    }
}
