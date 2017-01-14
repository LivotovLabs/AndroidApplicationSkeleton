package appskeleton.event.system;

import appskeleton.R;
import appskeleton.core.App;

/**
 * Created by dlivotov on 09/02/2016.
 */
public class GenericErrorEvent
{
    private Throwable cause;

    public GenericErrorEvent(Throwable cause)
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
