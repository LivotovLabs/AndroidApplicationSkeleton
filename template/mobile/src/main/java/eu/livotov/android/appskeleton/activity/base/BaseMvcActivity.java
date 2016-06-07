package eu.livotov.android.appskeleton.activity.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import eu.inloop.viewmodel.AbstractViewModel;
import eu.inloop.viewmodel.IView;
import eu.inloop.viewmodel.IViewModelProvider;
import eu.inloop.viewmodel.ViewModelHelper;
import eu.inloop.viewmodel.ViewModelProvider;

/**
 * Created by dlivotov on 07/06/2016.
 * This is a copy of inloop's eu.inloop.viewmodel.base.ViewModelBaseActivity, just moved here
 * as we maintain our own base activity class.
 */

public abstract class BaseMvcActivity<T extends IView, R extends AbstractViewModel<T>> extends BaseActivity implements IViewModelProvider, IView
{
    private final ViewModelHelper<T, R> mViewModeHelper = new ViewModelHelper<>();
    private ViewModelProvider mViewModelProvider;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState)
    {
        //This code must be execute prior to super.onCreate()
        mViewModelProvider = ViewModelProvider.newInstance(this);
        super.onCreate(savedInstanceState);
        mViewModeHelper.onCreate(this, savedInstanceState, getViewModelClass(), getIntent().getExtras());
    }

    public abstract Class<R> getViewModelClass();

    @Override
    public void onDestroy()
    {
        mViewModeHelper.onDestroy(this);
        super.onDestroy();
    }

    /**
     * Call this after your view is ready - usually on the end of {@link android.app.Activity#onCreate(Bundle)}
     *
     * @param view view
     */
    @SuppressWarnings("unused")
    public void setModelView(@NonNull final T view)
    {
        mViewModeHelper.setView(view);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        mViewModeHelper.onStart();
    }

    @Override
    @Nullable
    public Object onRetainCustomNonConfigurationInstance()
    {
        return mViewModelProvider;
    }

    @Override
    public void onStop()
    {
        super.onStop();
        mViewModeHelper.onStop();

        if (isFinishing())
        {
            mViewModelProvider.removeAllViewModels();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull final Bundle outState)
    {
        super.onSaveInstanceState(outState);
        mViewModeHelper.onSaveInstanceState(outState);
    }

    /**
     * @see ViewModelHelper#getViewModel()
     */
    @SuppressWarnings("unused")
    @NonNull
    public R getViewModel()
    {
        return mViewModeHelper.getViewModel();
    }

    @Override
    public ViewModelProvider getViewModelProvider()
    {
        return mViewModelProvider;
    }
}
