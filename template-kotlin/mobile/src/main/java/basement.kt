package eu.livotov.labs.androidappskeleton

import android.app.Fragment
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.MvpFragment


open class BaseActivity : MvpAppCompatActivity()
{
    private var blockingProgressDialog: MaterialDialog? = null
    private val progressDialogSyncLock = Any()

    protected fun <F : Fragment> setFragment(savedInstanceState: Bundle?, fragment: F): F
    {
        return setFragment(savedInstanceState, android.R.id.content, fragment)
    }

    protected fun <F : Fragment> setFragment(savedInstanceState: Bundle?, @IdRes slot: Int, fragment: F): F
    {
        return if (savedInstanceState == null)
        {
            fragmentManager.beginTransaction().replace(slot, fragment, fragment.javaClass.canonicalName).disallowAddToBackStack().commitAllowingStateLoss()
            fragment
        } else fragmentManager.findFragmentByTag(fragment.javaClass.canonicalName) as F
    }

    protected fun addFragment(fragment: BaseFragment)
    {
        fragmentManager.beginTransaction().replace(android.R.id.content, fragment).addToBackStack(null).commitAllowingStateLoss()
    }

    protected fun getFragment(): Fragment
    {
        return fragmentManager.findFragmentById(android.R.id.content)
    }

    protected fun <F : Fragment> getFragment(cls: Class<F>): F
    {
        return fragmentManager.findFragmentByTag(cls.canonicalName) as F
    }

    protected fun removeFragment()
    {
        try
        {
            val fragment = fragmentManager.findFragmentById(android.R.id.content)

            if (fragment != null)
            {
                fragmentManager.beginTransaction().remove(fragment).commitAllowingStateLoss()
            }
        }
        catch (ignored: Throwable)
        {
        }

    }

    fun back()
    {
        val mgr = fragmentManager

        if (mgr.backStackEntryCount > 0)
        {
            mgr.popBackStack()
        } else
        {
            finish()
        }
    }

    /**
     * Shows a toast for this activity.
     *
     * @param text      text to show as toast
     * @param longToast show a tost message with a longer lifetime
     */
    fun showToast(@StringRes text: Int, longToast: Boolean)
    {
        showToast(getString(text), longToast)
    }

    /**
     * Shows a toast for this activity.
     *
     * @param text      text to show as toast
     * @param longToast show a tost message with a longer lifetime
     */
    fun showToast(text: String, longToast: Boolean)
    {
        Toast.makeText(this, text, if (longToast) Toast.LENGTH_LONG else Toast.LENGTH_SHORT).show()
    }

    fun showSnackbar(target: View, longDuration: Boolean, text: String)
    {
        Snackbar.make(target, text, if (longDuration) Snackbar.LENGTH_LONG else Snackbar.LENGTH_SHORT).show()
    }

    fun showSnackbarWithAction(target: View, text: String, actionText: String, infinite: Boolean, actionListener: View.OnClickListener)
    {
        Snackbar.make(target, text, if (infinite) Snackbar.LENGTH_INDEFINITE else Snackbar.LENGTH_LONG).setAction(actionText, actionListener).show()
    }

    /**
     * Shows modal dialog message
     *
     * @param title
     * @param message
     * @param callback
     */
    fun showMessage(title: String?, message: String, callback: DialogCloseCallback?)
    {
        val builder = MaterialDialog.Builder(this)

        if (!TextUtils.isEmpty(title))
        {
            builder.title(title!!)
        }

        if (!TextUtils.isEmpty(message))
        {
            builder.content(message)
        } else
        {
            builder.content("")
        }

        builder.positiveText(R.string.generic_dialog_btn_ok)

        builder.onPositive { dialog, which ->
            if (callback != null)
            {
                callback!!.onDialogClosed()
            }
        }

        builder.cancelListener {
            if (callback != null)
            {
                callback!!.onDialogClosed()
            }
        }

        builder.show()
    }

    /**
     * Shows modal dialog question with yes/no buttons
     *
     * @param message
     * @param callback
     */
    fun showQuestion(@StringRes message: Int, callback: QuestionDialogCallback)
    {
        showQuestion(null, getString(message), callback)
    }

    /**
     * Shows modal dialog question with yes/no buttons
     *
     * @param title
     * @param message
     * @param callback
     */
    fun showQuestion(title: String?, message: String, callback: QuestionDialogCallback?)
    {
        val builder = MaterialDialog.Builder(this)

        if (!TextUtils.isEmpty(title))
        {
            builder.title(title!!)
        }

        if (!TextUtils.isEmpty(message))
        {
            builder.content(message)
        } else
        {
            builder.content("")
        }

        builder.positiveText(R.string.generic_dialog_btn_yes)
        builder.negativeText(R.string.generic_dialog_btn_no)

        builder.onPositive { dialog, which ->
            if (callback != null)
            {
                callback!!.onPositiveAnsfer()
            }
        }

        builder.onNegative { dialog, which ->
            if (callback != null)
            {
                callback!!.onNegativeAnswer()
            }
        }

        builder.show()
    }

    /**
     * Shows modal dialog question with yes/no buttons
     *
     * @param message
     * @param callback
     */
    fun showQuestion(message: String, callback: QuestionDialogCallback)
    {
        showQuestion(null, message, callback)
    }

    /**
     * Shows modal dialog question with yes/no buttons
     *
     * @param title
     * @param message
     * @param callback
     */
    fun showQuestion(@StringRes title: Int, @StringRes message: Int, callback: QuestionDialogCallback)
    {
        showQuestion(getString(title), getString(message), callback)
    }

    /**
     * Shows modal dialog message
     *
     * @param message
     */
    fun showMessage(@StringRes message: Int)
    {
        showMessage(null, getString(message), null)
    }

    /**
     * Shows modal dialog message
     *
     * @param message
     */
    fun showMessage(message: String)
    {
        showMessage(null, message, null)
    }

    /**
     * Shows modal dialog message
     *
     * @param message
     * @param callback
     */
    fun showMessage(@StringRes message: Int, callback: DialogCloseCallback)
    {
        showMessage(null, getString(message), callback)
    }

    /**
     * Shows modal dialog message
     *
     * @param message
     * @param callback
     */
    fun showMessage(message: String, callback: DialogCloseCallback)
    {
        showMessage(null, message, callback)
    }

    /**
     * Shows modal dialog message
     *
     * @param title
     * @param message
     * @param callback
     */
    fun showMessage(@StringRes title: Int, @StringRes message: Int, callback: DialogCloseCallback)
    {
        showMessage(getString(title), getString(message), callback)
    }

    /**
     * Shows indeterminate modal progress dialog or updates content on a existing one (if already showing)
     *
     * @param cancelable  whether the dialog can be cancelled by a user (back button) or not
     * @param title       optional dialog title
     * @param description optional dialog content
     */
    fun showBlockingIndeterminateProgressDialog(cancelable: Boolean, title: String, description: String)
    {
        showBlockingProgressDialog(cancelable, title, description, -1f)
    }

    /**
     * Shows modal progress dialog or updates content on a existing one (if already showing)
     *
     * @param cancelable  whether the dialog can be cancelled by a user (back button) or not
     * @param title       optional dialog title
     * @param description optional dialog content
     * @param progress    current progress value from 0.0 to 1.0 or any negative number. Negative number switches the dialog into an indeterminate progress mode (only if dialog is not yet visible !)
     */
    fun showBlockingProgressDialog(cancelable: Boolean, title: String, description: String, progress: Float)
    {
        val pd = blockingProgressDialog

        synchronized(progressDialogSyncLock) {
            if (pd != null)
            {
                pd.setTitle(if (TextUtils.isEmpty(title)) "" else title)
                pd.setContent(if (TextUtils.isEmpty(description)) "" else description)
                pd.setProgress(if (progress >= 0) (100 * progress).toInt() else 0)
                pd.setCancelable(cancelable)
            } else
            {
                val builder = MaterialDialog.Builder(this)

                if (!TextUtils.isEmpty(title))
                {
                    builder.title(title)
                }

                if (!TextUtils.isEmpty(description))
                {
                    builder.content(description)
                }

                builder.progress(progress < 0, 100, true)
                builder.cancelable(cancelable)

                val npd = builder.build()
                npd.show()
                blockingProgressDialog = npd;
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
    fun showBlockingProgressDialog(cancelable: Boolean, @StringRes title: Int, @StringRes description: Int, progress: Float)
    {
        showBlockingProgressDialog(cancelable, getString(title), getString(description), progress)
    }

    /**
     * Updates modal progress dialog only if it is showing at the moment of call. Nothing happens otherwise.
     *
     * @param progress new progress value from 0.0 to 1.0
     */
    fun updateBlockingProgressDialog(progress: Float)
    {
        updateBlockingProgressDialog(null, progress)
    }

    /**
     * Updates modal progress dialog only if it is showing at the moment of call. Nothing happens otherwise.
     *
     * @param description new content text
     * @param progress    new progress value from 0.0 to 1.0
     */
    fun updateBlockingProgressDialog(description: String?, progress: Float)
    {
        synchronized(progressDialogSyncLock) {
            val pd = blockingProgressDialog
            if (pd != null && pd.isShowing())
            {
                pd.setContent(if (TextUtils.isEmpty(description)) "" else description)
                pd.setProgress(if (progress >= 0) (100 * progress).toInt() else 0)
            }
        }
    }

    /**
     * Updates modal progress dialog only if it is showing at the moment of call. Nothing happens otherwise.
     *
     * @param description new content text
     * @param progress    new progress value from 0.0 to 1.0
     */
    fun updateBlockingProgressDialog(@StringRes description: Int, progress: Float)
    {
        updateBlockingProgressDialog(getString(description), progress)
    }

    /**
     * Hides modal progress dialog if it is currently running. If not - nothing will happen
     */
    fun hideBlockingProgressDialog()
    {
        synchronized(progressDialogSyncLock) {
            val pd = blockingProgressDialog
            if (pd != null && pd.isShowing())
            {
                pd.dismiss()
                blockingProgressDialog = null
            }
        }
    }

    interface DialogCloseCallback
    {

        fun onDialogClosed()
    }

    interface QuestionDialogCallback
    {

        fun onPositiveAnsfer()

        fun onNegativeAnswer()
    }
}

open class BaseFragment : MvpFragment()
{
    /**
     * Explicitly finishes the parent activity.
     */
    fun forceFinish()
    {
        activity.finish()
    }

    /**
     * Shows modal dialog question with yes/no buttons
     *
     * @param message
     * @param callback
     */
    fun showQuestion(@StringRes message: Int, callback: BaseActivity.QuestionDialogCallback)
    {
        showQuestion(null, getString(message), callback)
    }

    /**
     * Shows modal dialog question with yes/no buttons
     *
     * @param title
     * @param message
     * @param callback
     */
    fun showQuestion(title: String?, message: String, callback: BaseActivity.QuestionDialogCallback?)
    {
        val builder = MaterialDialog.Builder(activity)

        if (!TextUtils.isEmpty(title))
        {
            builder.title(title!!)
        }

        if (!TextUtils.isEmpty(message))
        {
            builder.content(message)
        } else
        {
            builder.content("")
        }

        builder.positiveText(R.string.generic_dialog_btn_yes)
        builder.negativeText(R.string.generic_dialog_btn_no)

        builder.onPositive { dialog, which ->
            if (callback != null)
            {
                callback!!.onPositiveAnsfer()
            }
        }

        builder.onNegative { dialog, which ->
            if (callback != null)
            {
                callback!!.onNegativeAnswer()
            }
        }

        builder.show()
    }

    /**
     * Shows a toast for this activity.
     *
     * @param text      text to show as toast
     * @param longToast show a tost message with a longer lifetime
     */
    fun showToast(@StringRes text: Int, longToast: Boolean)
    {
        showToast(getString(text), longToast)
    }

    /**
     * Shows a toast for this activity.
     *
     * @param text      text to show as toast
     * @param longToast show a tost message with a longer lifetime
     */
    fun showToast(text: String, longToast: Boolean)
    {
        if (longToast) App.longToast(text) else App.toast(text)
    }

    fun showSnackbar(target: View, longDuration: Boolean, text: String)
    {
        Snackbar.make(target, text, if (longDuration) Snackbar.LENGTH_LONG else Snackbar.LENGTH_SHORT).show()
    }

    fun showSnackbarWithAction(target: View, text: String, actionText: String, infinite: Boolean, actionListener: View.OnClickListener)
    {
        Snackbar.make(target, text, if (infinite) Snackbar.LENGTH_INDEFINITE else Snackbar.LENGTH_LONG).setAction(actionText, actionListener).show()
    }

    /**
     * Shows modal dialog question with yes/no buttons
     *
     * @param message
     * @param callback
     */
    fun showQuestion(message: String, callback: BaseActivity.QuestionDialogCallback)
    {
        showQuestion(null, message, callback)
    }

    /**
     * Shows modal dialog question with yes/no buttons
     *
     * @param title
     * @param message
     * @param callback
     */
    fun showQuestion(@StringRes title: Int, @StringRes message: Int, callback: BaseActivity.QuestionDialogCallback)
    {
        showQuestion(getString(title), getString(message), callback)
    }

    /**
     * Shows modal dialog message
     *
     * @param message
     */
    fun showMessage(@StringRes message: Int)
    {
        showMessage(null, getString(message), null)
    }

    /**
     * Shows modal dialog message
     *
     * @param title
     * @param message
     * @param callback
     */
    fun showMessage(title: String?, message: String, callback: BaseActivity.DialogCloseCallback?)
    {
        val builder = MaterialDialog.Builder(activity)

        if (!TextUtils.isEmpty(title))
        {
            builder.title(title!!)
        }

        if (!TextUtils.isEmpty(message))
        {
            builder.content(message)
        } else
        {
            builder.content("")
        }

        builder.positiveText(R.string.generic_dialog_btn_ok)

        builder.onPositive { dialog, which ->
            if (callback != null)
            {
                callback!!.onDialogClosed()
            }
        }

        builder.show()
    }

    /**
     * Shows modal dialog message
     *
     * @param message
     */
    fun showMessage(message: String)
    {
        showMessage(null, message, null)
    }

    /**
     * Shows modal dialog message
     *
     * @param message
     * @param callback
     */
    fun showMessage(@StringRes message: Int, callback: BaseActivity.DialogCloseCallback)
    {
        showMessage(null, getString(message), callback)
    }

    /**
     * Shows modal dialog message
     *
     * @param message
     * @param callback
     */
    fun showMessage(message: String, callback: BaseActivity.DialogCloseCallback?)
    {
        showMessage(null, message, callback)
    }

    /**
     * Shows modal dialog message
     *
     * @param title
     * @param message
     * @param callback
     */
    fun showMessage(@StringRes title: Int, @StringRes message: Int, callback: BaseActivity.DialogCloseCallback)
    {
        showMessage(getString(title), getString(message), callback)
    }

    protected fun showError(err: Throwable)
    {
        showError(err, null)
    }

    protected fun showError(err: Throwable, callback: BaseActivity.DialogCloseCallback?)
    {
        showMessage(err.message!!, callback)
    }

    protected fun showProgressDialog()
    {
        (activity as BaseActivity).showBlockingIndeterminateProgressDialog(false, getString(R.string.please_wait), "")
    }

    protected fun hideProgressDialog()
    {
        (activity as BaseActivity).hideBlockingProgressDialog()
    }
}