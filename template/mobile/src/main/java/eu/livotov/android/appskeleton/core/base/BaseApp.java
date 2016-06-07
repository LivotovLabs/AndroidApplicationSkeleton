package eu.livotov.android.appskeleton.core.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import org.greenrobot.eventbus.EventBus;

import eu.livotov.android.appskeleton.R;
import eu.livotov.android.appskeleton.util.AppSettings;
import eu.livotov.labs.android.robotools.app.RTApp;

/**
 * Created by dlivotov on 09/02/2016.
 */
public class BaseApp extends RTApp
{
    protected AppSettings settings;
    protected EventBus systemEventBus;

    public static AppSettings getSettings()
    {
        return ((BaseApp) getInstance()).settings;
    }

    public static void postEvent(Object event)
    {
        EventBus.getDefault().post(event);
    }

    public static void postStickyEvent(Object event)
    {
        EventBus.getDefault().postSticky(event);
    }

    public static void postSystemEvent(Object event)
    {
        ((BaseApp) getInstance()).systemEventBus.post(event);
    }

    public static void postSystemStickyEvent(Object event)
    {
        ((BaseApp) getInstance()).systemEventBus.postSticky(event);
    }

    public static synchronized void subscribe(Object eventsListener)
    {
        final EventBus bus = EventBus.getDefault();
        if (!bus.isRegistered(eventsListener))
        {
            bus.register(eventsListener);
        }
    }

    public static synchronized void unsubscribe(Object eventListener)
    {
        final EventBus bus = EventBus.getDefault();
        if (bus.isRegistered(eventListener))
        {
            bus.unregister(eventListener);
        }
    }

    public static synchronized void subscribeForSystemEvents(Object eventsListener)
    {
        final EventBus bus = ((BaseApp) getInstance()).systemEventBus;
        if (!bus.isRegistered(eventsListener))
        {
            bus.register(eventsListener);
        }
    }

    public static synchronized void unsubscribeFromSystemEvents(Object eventListener)
    {
        final EventBus bus = ((BaseApp) getInstance()).systemEventBus;
        if (bus.isRegistered(eventListener))
        {
            bus.unregister(eventListener);
        }
    }

    public static void showQuestion(@StringRes final int message, final QuestionDialogCallback callback)
    {
        showQuestion(null, getContext().getString(message), callback);
    }

    public static void showQuestion(final String title, final String message, final QuestionDialogCallback callback)
    {
        final MaterialDialog.Builder builder = new MaterialDialog.Builder(getContext());

        if (!TextUtils.isEmpty(title))
        {
            builder.title(title);
        }

        if (!TextUtils.isEmpty(message))
        {
            builder.content(message);
        }
        else
        {
            builder.content("");
        }

        builder.positiveText(R.string.yes);
        builder.negativeText(R.string.no);

        builder.onPositive(new MaterialDialog.SingleButtonCallback()
        {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which)
            {
                if (callback != null)
                {
                    callback.onPositiveAnsfer();
                }
            }
        });

        builder.onNegative(new MaterialDialog.SingleButtonCallback()
        {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which)
            {
                if (callback != null)
                {
                    callback.onNegativeAnswer();
                }
            }
        });
    }

    public static void showQuestion(final String message, final QuestionDialogCallback callback)
    {
        showQuestion(null, message, callback);
    }

    public static void showQuestion(@StringRes final int title, @StringRes final int message, final QuestionDialogCallback callback)
    {
        showQuestion(getContext().getString(title), getContext().getString(message), callback);
    }

    public static void showMessage(@StringRes final int message)
    {
        showMessage(null, getContext().getString(message), null);
    }

    public static void showMessage(final String title, final String message, final DialogCloseCallback callback)
    {
        final MaterialDialog.Builder builder = new MaterialDialog.Builder(getContext());

        if (!TextUtils.isEmpty(title))
        {
            builder.title(title);
        }

        if (!TextUtils.isEmpty(message))
        {
            builder.content(message);
        }
        else
        {
            builder.content("");
        }

        builder.onPositive(new MaterialDialog.SingleButtonCallback()
        {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which)
            {
                if (callback != null)
                {
                    callback.onDialogClosed();
                }
            }
        });

        builder.show();
    }

    public static void showMessage(final String message)
    {
        showMessage(null, message, null);
    }

    public static void showMessage(@StringRes final int message, final DialogCloseCallback callback)
    {
        showMessage(null, getContext().getString(message), callback);
    }

    public static void showMessage(final String message, final DialogCloseCallback callback)
    {
        showMessage(null, message, callback);
    }

    public static void showMessage(@StringRes final int title, @StringRes final int message, final DialogCloseCallback callback)
    {
        showMessage(getContext().getString(title), getContext().getString(message), callback);
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        init();
    }

    private void init()
    {
        settings = new AppSettings(this);
        systemEventBus = EventBus.builder().eventInheritance(false).logNoSubscriberMessages(false).sendNoSubscriberEvent(false).throwSubscriberException(false).logSubscriberExceptions(true).build();
        EventBus.builder().eventInheritance(false).logNoSubscriberMessages(false).sendNoSubscriberEvent(false).throwSubscriberException(false).logSubscriberExceptions(true).installDefaultEventBus();
    }

    @Override
    protected void attachBaseContext(Context base)
    {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public interface DialogCloseCallback
    {
        void onDialogClosed();
    }

    public interface QuestionDialogCallback
    {
        void onPositiveAnsfer();

        void onNegativeAnswer();
    }
}
