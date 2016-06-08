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
import eu.livotov.android.appskeleton.event.system.UITaskCompletedEvent;
import eu.livotov.android.appskeleton.event.system.UITaskProgressUpdateEvent;
import eu.livotov.android.appskeleton.event.system.UITaskStartedEvent;
import eu.livotov.android.appskeleton.task.UITask;

/**
 * Created by dlivotov on 09/02/2016.
 */
public class BaseActivity extends AppCompatActivity
{
    private List<UITask> runningTasks = new ArrayList<>();
    private MaterialDialog blockingProgressDialog;
    private Object progressDialogSyncLock = new Object();

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

    /**
     * Shows modal dialog message
     *
     * @param title
     * @param message
     * @param callback
     */
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

    /**
     * Shows modal dialog question with yes/no buttons
     * @param message
     * @param callback
     */
    public void showQuestion(@StringRes final int message, final QuestionDialogCallback callback)
    {
        showQuestion(null, getString(message), callback);
    }

    /**
     * Shows modal dialog question with yes/no buttons
     * @param title
     * @param message
     * @param callback
     */
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

    /**
     * Shows modal dialog question with yes/no buttons
     * @param message
     * @param callback
     */
    public void showQuestion(final String message, final QuestionDialogCallback callback)
    {
        showQuestion(null, message, callback);
    }

    /**
     * Shows modal dialog question with yes/no buttons
     * @param title
     * @param message
     * @param callback
     */
    public void showQuestion(@StringRes final int title, @StringRes final int message, final QuestionDialogCallback callback)
    {
        showQuestion(getString(title), getString(message), callback);
    }

    /**
     * Shows modal dialog message
     * @param message
     */
    public void showMessage(@StringRes final int message)
    {
        showMessage(null, getString(message), null);
    }

    /**
     * Shows modal dialog message
     * @param message
     */
    public void showMessage(final String message)
    {
        showMessage(null, message, null);
    }

    /**
     * Shows modal dialog message
     * @param message
     * @param callback
     */
    public void showMessage(@StringRes final int message, final DialogCloseCallback callback)
    {
        showMessage(null, getString(message), callback);
    }

    /**
     * Shows modal dialog message
     * @param message
     * @param callback
     */
    public void showMessage(final String message, final DialogCloseCallback callback)
    {
        showMessage(null, message, callback);
    }

    /**
     * Shows modal dialog message
     * @param title
     * @param message
     * @param callback
     */
    public void showMessage(@StringRes final int title, @StringRes final int message, final DialogCloseCallback callback)
    {
        showMessage(getString(title), getString(message), callback);
    }

    @Subscribe
    public void onUITaskStarted(UITaskStartedEvent event)
    {
        runningTasks.add(event.getTask());
    }

    @Subscribe
    public void onUITaskFinished(UITaskCompletedEvent event)
    {
        runningTasks.remove(event.getTask());
    }

    @Subscribe
    public void onUITaskProgressUpdate(UITaskProgressUpdateEvent event)
    {
        if (runningTasks.contains(event.getTask()))
        {
            showBlockingProgressDialog(false, event.getTask().getTaskTitle(), event.getTask().getTaskDescription(), event.getTask().getTaskProgress());
        }
    }

    /**
     * Shows modal progress dialog or updates content on a existing one (if already showing)
     *
     * @param cancelable  whether the dialog can be cancelled by a user (back button) or not
     * @param title       optional dialog title
     * @param description optional dialog content
     * @param progress    current progress value from 0.0 to 1.0 or any negative number. Negative number switches the dialog into an indeterminate progress mode (only if dialog is not yet visible !)
     */
    public void showBlockingProgressDialog(final boolean cancelable, final String title, final String description, final float progress)
    {
        synchronized (progressDialogSyncLock)
        {
            if (blockingProgressDialog != null)
            {
                blockingProgressDialog.setTitle(TextUtils.isEmpty(title) ? "" : title);
                blockingProgressDialog.setContent(TextUtils.isEmpty(description) ? "" : description);
                blockingProgressDialog.setProgress(progress >= 0 ? (int) (100 * progress) : 0);
                blockingProgressDialog.setCancelable(cancelable);
            }
            else
            {
                MaterialDialog.Builder builder = new MaterialDialog.Builder(this);

                if (!TextUtils.isEmpty(title))
                {
                    builder.title(title);
                }

                if (!TextUtils.isEmpty(description))
                {
                    builder.content(description);
                }

                builder.progress(progress < 0, 100, true);
                builder.cancelable(cancelable);

                blockingProgressDialog = builder.build();
                blockingProgressDialog.show();
            }
        }
    }

    /**
     * Shows indeterminate modal progress dialog or updates content on a existing one (if already showing)
     *
     * @param cancelable  whether the dialog can be cancelled by a user (back button) or not
     * @param title       optional dialog title
     * @param description optional dialog content
     */
    public void showBlockingIndeterminateProgressDialog(final boolean cancelable, final String title, final String description)
    {
        showBlockingProgressDialog(cancelable, title, description, -1);
    }

    /**
     * Shows modal progress dialog or updates content on a existing one (if already showing)
     *
     * @param cancelable  whether the dialog can be cancelled by a user (back button) or not
     * @param title       optional dialog title
     * @param description optional dialog content
     * @param progress    current progress value from 0.0 to 1.0 or any negative number. Negative number switches the dialog into an indeterminate progress mode
     */
    public void showBlockingProgressDialog(final boolean cancelable, @StringRes final int title, @StringRes final int description, final float progress)
    {
        showBlockingProgressDialog(cancelable, getString(title), getString(description), progress);
    }

    /**
     * Updates modal progress dialog only if it is showing at the moment of call. Nothing happens otherwise.
     *
     * @param progress new progress value from 0.0 to 1.0
     */
    public void updateBlockingProgressDialog(float progress)
    {
        updateBlockingProgressDialog(null, progress);
    }

    /**
     * Updates modal progress dialog only if it is showing at the moment of call. Nothing happens otherwise.
     *
     * @param description new content text
     * @param progress    new progress value from 0.0 to 1.0
     */
    public void updateBlockingProgressDialog(final String description, float progress)
    {
        synchronized (progressDialogSyncLock)
        {
            if (blockingProgressDialog != null && blockingProgressDialog.isShowing())
            {
                blockingProgressDialog.setContent(TextUtils.isEmpty(description) ? "" : description);
                blockingProgressDialog.setProgress(progress >= 0 ? (int) (100 * progress) : 0);
            }
        }
    }

    /**
     * Updates modal progress dialog only if it is showing at the moment of call. Nothing happens otherwise.
     *
     * @param description new content text
     * @param progress    new progress value from 0.0 to 1.0
     */
    public void updateBlockingProgressDialog(@StringRes final int description, float progress)
    {
        updateBlockingProgressDialog(getString(description), progress);
    }

    /**
     * Hides modal progress dialog if it is currently running. If not - nothing will happen
     */
    public void hideBlockingProgressDialog()
    {
        synchronized (progressDialogSyncLock)
        {
            if (blockingProgressDialog != null && blockingProgressDialog.isShowing())
            {
                blockingProgressDialog.dismiss();
                blockingProgressDialog = null;
            }
        }
    }

    /**
     * Finishes all (even not visible ones) activities of this activity class, including this activity too.
     */
    public void finishAllInstances()
    {
        App.postSystemEvent(new ForceFinishActivityEvent(getClass()));
    }

    /**
     * Finishes all (even not visible ones) activities of this activity class, excluding this activity.
     */
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
