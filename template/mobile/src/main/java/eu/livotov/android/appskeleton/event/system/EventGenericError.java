package eu.livotov.android.appskeleton.event.system;

import eu.livotov.android.appskeleton.R;
import eu.livotov.android.appskeleton.core.App;

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

    @Override
    public String toString()
    {
        if (cause != null)
        {
            return cause.getLocalizedMessage();
        }
        else
        {
            return App.getContext().getString(R.string.unexpected_error_generic_text);
        }
    }
}
