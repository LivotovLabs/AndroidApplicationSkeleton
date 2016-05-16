package eu.livotov.android.appskeleton.event;

/**
 * Created by dlivotov on 09/02/2016.
 */
public class EventGenericError
{
    private Throwable cause;

    public EventGenericError(Throwable cause)
    {
        this.cause = cause;
    }

    public Throwable getCause()
    {
        return cause;
    }
}
