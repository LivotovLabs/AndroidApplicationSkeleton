package eu.livotov.android.appskeleton.activity.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import eu.livotov.android.appskeleton.R;
import eu.livotov.android.appskeleton.core.App;
import eu.livotov.android.appskeleton.event.system.ForceFinishActivityEvent;
import eu.livotov.android.appskeleton.event.system.GenericErrorEvent;
import eu.livotov.android.appskeleton.event.system.UITaskProgressUpdateEvent;
import eu.livotov.android.appskeleton.event.system.UITaskStartedEvent;
import eu.livotov.android.appskeleton.task.EventUITaskCompleted;

/**
 * Created by dlivotov on 09/02/2016.
 */
public class BaseActivity extends AppCompatActivity
{
    private List<String> runningTasks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        App.subscribeForSystemEvents(this);
    }

    @Override
    protected void onDestroy()
    {
        App.unsubscribeFromSystemEvents(this);
        super.onDestroy();
    }

    @Override
    protected void onPause()
    {
        App.unsubscribe(this);
        super.onPause();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        App.subscribe(this);
    }

    @Subscribe
    public void forceQuitEvent(ForceFinishActivityEvent data)
    {
        if (data.matches(this))
        {
            finish();
        }
    }

    @Subscribe
    public void onGenericError(GenericErrorEvent error)
    {
        showMessage(App.getContext().getString(R.string.generic_error_dialog_title), error.toString(), null);
    }

    public void showMessage(final String title, final String message, final DialogCloseCallback callback)
    {
        final MaterialDialog.Builder builder = new MaterialDialog.Builder(this);

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

        builder.positiveText(R.string.generic_dialog_btn_ok);

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

    public void showQuestion(@StringRes final int message, final QuestionDialogCallback callback)
    {
        showQuestion(null, getString(message), callback);
    }

    public void showQuestion(final String title, final String message, final QuestionDialogCallback callback)
    {
        final MaterialDialog.Builder builder = new MaterialDialog.Builder(this);

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

        builder.positiveText(R.string.generic_dialog_btn_yes);
        builder.negativeText(R.string.generic_dialog_btn_no);

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

    public void showQuestion(final String message, final QuestionDialogCallback callback)
    {
        showQuestion(null, message, callback);
    }

    public void showQuestion(@StringRes final int title, @StringRes final int message, final QuestionDialogCallback callback)
    {
        showQuestion(getString(title), getString(message), callback);
    }

    public void showMessage(@StringRes final int message)
    {
        showMessage(null, getString(message), null);
    }

    public void showMessage(final String message)
    {
        showMessage(null, message, null);
    }

    public void showMessage(@StringRes final int message, final DialogCloseCallback callback)
    {
        showMessage(null, getString(message), callback);
    }

    public void showMessage(final String message, final DialogCloseCallback callback)
    {
        showMessage(null, message, callback);
    }

    public void showMessage(@StringRes final int title, @StringRes final int message, final DialogCloseCallback callback)
    {
        showMessage(getString(title), getString(message), callback);
    }

    @Subscribe
    public void onUITaskStarted(UITaskStartedEvent task)
    {
        runningTasks.add(task.getId());
    }

    @Subscribe
    public void onUITaskFinished(EventUITaskCompleted task)
    {
        runningTasks.remove(task.getId());
    }

    @Subscribe
    public void onUITaskProgressUpdate(UITaskProgressUpdateEvent task)
    {
    }

    public void finishAllInstances()
    {
        App.postSystemEvent(new ForceFinishActivityEvent(getClass()));
    }

    public void finishAllInstancesButThis()
    {
        App.postSystemEvent(new ForceFinishActivityEvent(getClass()).keepInstanceOf(this));
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
