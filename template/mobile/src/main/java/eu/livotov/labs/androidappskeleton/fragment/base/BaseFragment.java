package eu.livotov.labs.androidappskeleton.fragment.base;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.arellomobile.mvp.MvpFragment;
import eu.livotov.labs.androidappskeleton.R;
import eu.livotov.labs.androidappskeleton.activity.base.BaseActivity;
import eu.livotov.labs.androidappskeleton.core.App;
import eu.livotov.labs.androidappskeleton.event.stub.DummyEvent;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by dlivotov on 07/06/2016.
 */

public class BaseFragment extends MvpFragment
{
    private Unbinder unbinder;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
    }

    @Override
    public void onDestroyView()
    {
        if (unbinder != null)
        {
            unbinder.unbind();
        }

        super.onDestroyView();
    }

    /**
     * Pops backstack back or (if there are no more elements in backstack) finishes the parent activity.
     */
    public void finish()
    {
        final FragmentManager fragmentManager = getFragmentManager();

        if (fragmentManager.getBackStackEntryCount() > 0)
        {
            getFragmentManager().popBackStack();
        }
        else
        {
            getActivity().finish();
        }
    }

    /**
     * Explicitly finishes the parent activity.
     */
    public void forceFinish()
    {
        getActivity().finish();
    }

    @Subscribe
    public void onDummyEvent(DummyEvent event)
    {
        // NOP.
        // We need this dummy class in order to keep eventbys happy when you don't have any receiver on child fragments.
    }

    /**
     * Shows modal dialog question with yes/no buttons
     *
     * @param message
     * @param callback
     */
    public void showQuestion(@StringRes final int message, final BaseActivity.QuestionDialogCallback callback)
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
    public void showQuestion(final String title, final String message, final BaseActivity.QuestionDialogCallback callback)
    {
        final MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity());

        if (!TextUtils.isEmpty(title))
        {
            builder.title(title);
        }

        if (!TextUtils.isEmpty(message))
        {
            builder.content(message);
        } else
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

        builder.show();
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
        App.showToast(text, longToast);
    }

    public void showSnackbar(View target, boolean longDuration, String text)
    {
        Snackbar.make(target, text, longDuration ? Snackbar.LENGTH_LONG : Snackbar.LENGTH_SHORT).show();
    }

    public void showSnackbarWithAction(View target, String text, String actionText, boolean infinite, View.OnClickListener actionListener)
    {
        Snackbar.make(target, text, infinite ? Snackbar.LENGTH_INDEFINITE : Snackbar.LENGTH_LONG).setAction(actionText, actionListener).show();
    }

    /**
     * Shows modal dialog question with yes/no buttons
     *
     * @param message
     * @param callback
     */
    public void showQuestion(final String message, final BaseActivity.QuestionDialogCallback callback)
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
    public void showQuestion(@StringRes final int title, @StringRes final int message, final BaseActivity.QuestionDialogCallback callback)
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
     * @param title
     * @param message
     * @param callback
     */
    public void showMessage(final String title, final String message, final BaseActivity.DialogCloseCallback callback)
    {
        final MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity());

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
    public void showMessage(@StringRes final int message, final BaseActivity.DialogCloseCallback callback)
    {
        showMessage(null, getString(message), callback);
    }

    /**
     * Shows modal dialog message
     *
     * @param message
     * @param callback
     */
    public void showMessage(final String message, final BaseActivity.DialogCloseCallback callback)
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
    public void showMessage(@StringRes final int title, @StringRes final int message, final BaseActivity.DialogCloseCallback callback)
    {
        showMessage(getString(title), getString(message), callback);
    }
}
