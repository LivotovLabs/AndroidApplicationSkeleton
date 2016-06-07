package eu.livotov.android.appskeleton.event.system;

/**
 * Created by dlivotov on 09/02/2016.
 */
public class UITaskProgressUpdateEvent
{
    private final float progress;
    private final String id;

    public UITaskProgressUpdateEvent(String id, float currentProgress)
    {
        this.id = id;
        this.progress = currentProgress;
    }

    public float getProgress()
    {
        return progress;
    }

    public String getId()
    {
        return id;
    }
}
