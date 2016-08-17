package eu.livotov.android.appskeleton.activity.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import eu.inloop.viewmodel.IViewModelProvider;
import eu.inloop.viewmodel.ViewModelProvider;

/**
 * All your activities must extend this activity in case your activity has no viewmodel but fragment(s) does.
 * The fragment viewmodels are using the {@link IViewModelProvider}
 * interface to get the {@link ViewModelProvider} from the current activity.
 */
public class BaseEmptyMvcActivity extends BaseActivity implements IViewModelProvider
{
    private ViewModelProvider mViewModelProvider;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState)
    {
        //This code must be execute prior to super.onCreate()
        mViewModelProvider = ViewModelProvider.newInstance(this);
        super.onCreate(savedInstanceState);
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
        if (isFinishing())
        {
            mViewModelProvider.removeAllViewModels();
        }
    }

    @Override
    public ViewModelProvider getViewModelProvider()
    {
        return mViewModelProvider;
    }

}
