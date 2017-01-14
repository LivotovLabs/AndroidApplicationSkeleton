package appskeleton.activity.base;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.arellomobile.mvp.MvpAppCompatActivity;

import org.greenrobot.eventbus.Subscribe;

import appskeleton.R;
import appskeleton.core.App;
import appskeleton.event.permission.PermissionGrantEvent;
import appskeleton.event.system.ForceFinishActivityEvent;
import appskeleton.event.system.GenericErrorEvent;
import appskeleton.fragment.base.BaseFragment;
import butterknife.ButterKnife;

/**
 * Created by dlivotov on 09/02/2016.
 */
public class BaseActivity extends MvpAppCompatActivity
{
    public static final int PERMISSION_ACQUIRE_REQUEST_CODE = 9876;

    private MaterialDialog blockingProgressDialog;
    private Object progressDialogSyncLock = new Object();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        App.subscribe(this);
    }

    @Override
    protected void onDestroy()
    {
        App.unsubscribe(this);
        super.onDestroy();
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID)
    {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

    @Override
    public void setContentView(View view)
    {
        super.setContentView(view);
        ButterKnife.bind(this);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params)
    {
        super.setContentView(view, params);
        ButterKnife.bind(this);
    }

    /**
     * Finishes the current activity and also displays a short toast message at the same time.
     *
     * @param text text to show as toast
     */
    public void finishWithMessage(@StringRes int text)
    {
        finishWithMessage(getString(text));
    }

    /**
     * Finishes the current activity and also displays a short toast message at the same time.
     *
     * @param text text to show as toast
     */
    public void finishWithMessage(String text)
    {
        Toast.makeText(App.getContext(), text, Toast.LENGTH_SHORT).show();
        finish();
    }

    /**
     * Shows a toast for this activity.
     *
     * @param text      text to show as toast
     * @param longToast show a tost message with a longer lifetime
     */
    public void showToast(@StringRes int text, boolean longToast)
    {
        showToast(getString(text), longToast);
    }

    /**
     * Shows a toast for this activity.
     *
     * @param text      text to show as toast
     * @param longToast show a tost message with a longer lifetime
     */
    public void showToast(String text, boolean longToast)
    {
        Toast.makeText(this, text, longToast ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch (requestCode)
        {
            case PERMISSION_ACQUIRE_REQUEST_CODE:
            {
                if (permissions.length > 0 && grantResults.length > 0)
                {
                    PermissionGrantEvent event = new PermissionGrantEvent();

                    for (int i = 0; i < permissions.length; i++)
                    {
                        event.addPermission(permissions[i], grantResults[i] == PackageManager.PERMISSION_GRANTED);
                    }

                    App.postEvent(event);
                }
                break;
            }
        }
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

        builder.cancelListener(new DialogInterface.OnCancelListener()
        {
            @Override
            public void onCancel(DialogInterface dialogInterface)
            {
                if (callback != null)
                {
                    callback.onDialogClosed();
                }
            }
        });

        builder.show();
    }

    protected <F extends Fragment> F setFragment(Bundle savedInstanceState, F fragment)
    {
        if (savedInstanceState == null)
        {
            getFragmentManager().beginTransaction().replace(android.R.id.content, fragment, fragment.getClass().getCanonicalName()).disallowAddToBackStack().commitAllowingStateLoss();
            return fragment;
        }
        else
        {
            return (F) getFragmentManager().findFragmentByTag(fragment.getClass().getCanonicalName());
        }
    }

    protected void addFragment(BaseFragment fragment)
    {
        getFragmentManager().beginTransaction().replace(android.R.id.content, fragment).addToBackStack(null).commitAllowingStateLoss();
    }

    protected Fragment getFragment()
    {
        return getFragmentManager().findFragmentById(android.R.id.content);
    }

    protected <F extends Fragment> F getFragment(Class<F> cls)
    {
        return (F) getFragmentManager().findFragmentByTag(cls.getCanonicalName());
    }

    protected void removeFragment()
    {
        try
        {
            Fragment fragment = getFragmentManager().findFragmentById(android.R.id.content);

            if (fragment != null)
            {
                getFragmentManager().beginTransaction().remove(fragment).commitAllowingStateLoss();
            }
        }
        catch (Throwable ignored)
        {
        }
    }

    public void back()
    {
        final FragmentManager mgr = getFragmentManager();

        if (mgr.getBackStackEntryCount() > 0)
        {
            mgr.popBackStack();
        }
        else
        {
            finish();
        }
    }

    /**
     * Shows modal dialog question with yes/no buttons
     *
     * @param message
     * @param callback
     */
    public void showQuestion(@StringRes final int message, final QuestionDialogCallback callback)
    {
        showQuestion(null, getString(message), callback);
    }

    /**
     * Shows modal dialog question with yes/no buttons
     *
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

        builder.cancelListener(new DialogInterface.OnCancelListener()
        {
            @Override
            public void onCancel(DialogInterface dialogInterface)
            {
                if (callback != null)
                {
                    callback.onNegativeAnswer();
                }
            }
        });

        builder.show();
    }

    /**
     * Shows modal dialog question with yes/no buttons
     *
     * @param message
     * @param callback
     */
    public void showQuestion(final String message, final QuestionDialogCallback callback)
    {
        showQuestion(null, message, callback);
    }

    /**
     * Shows modal dialog question with yes/no buttons
     *
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
     *
     * @param message
     */
    public void showMessage(@StringRes final int message)
    {
        showMessage(null, getString(message), null);
    }

    /**
     * Shows modal dialog message
     *
     * @param message
     */
    public void showMessage(final String message)
    {
        showMessage(null, message, null);
    }

    /**
     * Shows modal dialog message
     *
     * @param message
     * @param callback
     */
    public void showMessage(@StringRes final int message, final DialogCloseCallback callback)
    {
        showMessage(null, getString(message), callback);
    }

    /**
     * Shows modal dialog message
     *
     * @param message
     * @param callback
     */
    public void showMessage(final String message, final DialogCloseCallback callback)
    {
        showMessage(null, message, callback);
    }

    /**
     * Shows modal dialog message
     *
     * @param title
     * @param message
     * @param callback
     */
    public void showMessage(@StringRes final int title, @StringRes final int message, final DialogCloseCallback callback)
    {
        showMessage(getString(title), getString(message), callback);
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
     * Provides an object to identify the group activities.
     *
     * @return any object as tag identifier
     */
    public Object getTag()
    {
        return this;
    }

    /**
     * Force finsih all (even not visible ones) activities of this activity class, including this (self) instance too.
     */
    public void finishAllInstances()
    {
        App.postEvent(new ForceFinishActivityEvent(getClass()));
    }

    /**
     * Force finish all (even not visible ones) activities of this activity class, excluding this (self) instance.
     */
    public void finishAllInstancesButThis()
    {
        App.postEvent(new ForceFinishActivityEvent(getClass()).keepInstanceOf(this));
    }

    public void finishAllInstancesByTag(final Object tag)
    {
        App.postEvent(new ForceFinishActivityEvent().withTag(tag));
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
