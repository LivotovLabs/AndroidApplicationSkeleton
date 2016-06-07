package eu.livotov.android.appskeleton.fragment.base;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import java.util.UUID;

import eu.inloop.viewmodel.AbstractViewModel;
import eu.inloop.viewmodel.BuildConfig;
import eu.inloop.viewmodel.IView;
import eu.inloop.viewmodel.IViewModelProvider;
import eu.inloop.viewmodel.ViewModelProvider;

/**
 * Created by dlivotov on 07/06/2016.
 * This is a clone of inloop's eu.inloop.viewmodel.base.ViewModelBaseFragment, just using
 * native android's Fragment class instead of one from support library.
 */

public abstract class BaseMvcFragment<T extends IView, R extends AbstractViewModel<T>> extends BaseFragment implements IView
{
    private final ViewModelHelper<T, R> mViewModeHelper = new ViewModelHelper<>();

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mViewModeHelper.onCreate(getActivity(), savedInstanceState, getViewModelClass(), getArguments());
    }

    @Nullable
    public abstract Class<R> getViewModelClass();

    @Override
    public void onStart()
    {
        super.onStart();
        mViewModeHelper.onStart();
    }

    @Override
    public void onSaveInstanceState(@NonNull final Bundle outState)
    {
        super.onSaveInstanceState(outState);
        mViewModeHelper.onSaveInstanceState(outState);
    }

    @Override
    public void onStop()
    {
        super.onStop();
        mViewModeHelper.onStop();
    }

    @Override
    public void onDestroyView()
    {
        mViewModeHelper.onDestroyView(this);
        super.onDestroyView();
    }

    @Override
    public void onDestroy()
    {
        mViewModeHelper.onDestroy(this);
        super.onDestroy();
    }

    /**
     * Call this after your view is ready - usually on the end of {@link Fragment#onViewCreated(View, Bundle)}
     *
     * @param view view
     */
    protected void setModelView(@NonNull final T view)
    {
        mViewModeHelper.setView(view);
    }

    /**
     * @see ViewModelHelper#getViewModel()
     */
    @NonNull
    @SuppressWarnings("unused")
    public R getViewModel()
    {
        return mViewModeHelper.getViewModel();
    }

    public class ViewModelHelper<T extends IView, R extends AbstractViewModel<T>>
    {

        @Nullable
        private String mScreenId;
        @Nullable
        private R mViewModel;
        private boolean mModelRemoved;
        private boolean mOnSaveInstanceCalled;

        /**
         * Call from {@link android.app.Activity#onCreate(android.os.Bundle)} or
         * {@link Fragment#onCreate(android.os.Bundle)}
         *
         * @param activity           parent activity
         * @param savedInstanceState savedInstance state from {@link Activity#onCreate(Bundle)} or
         *                           {@link Fragment#onCreate(Bundle)}
         * @param viewModelClass     the {@link Class} of your ViewModel
         * @param arguments          pass {@link Fragment#getArguments()}  or
         *                           {@link Activity#getIntent()}.{@link Intent#getExtras() getExtras()}
         */
        public void onCreate(@NonNull Activity activity, @Nullable Bundle savedInstanceState, @Nullable Class<? extends AbstractViewModel<T>> viewModelClass, @Nullable Bundle arguments)
        {
            // no viewmodel for this fragment
            if (viewModelClass == null)
            {
                mViewModel = null;
                return;
            }

            // screen (activity/fragment) created for first time, attach unique ID
            if (savedInstanceState == null)
            {
                mScreenId = UUID.randomUUID().toString();
            }
            else
            {
                mScreenId = savedInstanceState.getString("identifier");
                mOnSaveInstanceCalled = false;
            }

            // get model instance for this screen
            final ViewModelProvider.ViewModelWrapper<T> viewModelWrapper = getViewModelProvider(activity).getViewModelProvider().getViewModel(mScreenId, viewModelClass);
            //noinspection unchecked
            mViewModel = (R) viewModelWrapper.viewModel;

            if (viewModelWrapper.wasCreated)
            {
                // detect that the system has killed the app - saved instance is not null, but the model was recreated
                if (BuildConfig.DEBUG && savedInstanceState != null)
                {
                    Log.d("model", "Fragment recreated by system - restoring viewmodel");
                }
                mViewModel.onCreate(arguments, savedInstanceState);
            }
        }

        private IViewModelProvider getViewModelProvider(@NonNull Activity activity)
        {
            if (!(activity instanceof IViewModelProvider))
            {
                throw new IllegalStateException("Your activity must implement IViewModelProvider"); //NON-NLS
            }
            return ((IViewModelProvider) activity);
        }

        /**
         * Call from {@link Fragment#onViewCreated(android.view.View, android.os.Bundle)}
         * or {@link android.app.Activity#onCreate(android.os.Bundle)}
         *
         * @param view view
         */
        public void setView(@NonNull final T view)
        {
            if (mViewModel == null)
            {
                //no viewmodel for this fragment
                return;
            }
            mViewModel.onBindView(view);
        }

        /**
         * Use in case this model is associated with an {@link Fragment}
         * Call from {@link Fragment#onDestroyView()}. Use in case model is associated
         * with Fragment
         *
         * @param fragment fragment
         */
        public void onDestroyView(@NonNull Fragment fragment)
        {
            if (mViewModel == null)
            {
                //no viewmodel for this fragment
                return;
            }
            mViewModel.clearView();
            if (fragment.getActivity() != null && fragment.getActivity().isFinishing())
            {
                removeViewModel(fragment.getActivity());
            }
        }

        private void removeViewModel(@NonNull final Activity activity)
        {
            if (mViewModel != null && !mModelRemoved)
            {
                getViewModelProvider(activity).getViewModelProvider().remove(mScreenId);
                mViewModel.onDestroy();
                mModelRemoved = true;
            }
        }

        /**
         * Use in case this model is associated with an {@link Fragment}
         * Call from {@link Fragment#onDestroy()}
         *
         * @param fragment fragment
         */
        public void onDestroy(@NonNull final Fragment fragment)
        {
            if (mViewModel == null)
            {
                //no viewmodel for this fragment
                return;
            }
            if (fragment.getActivity().isFinishing())
            {
                removeViewModel(fragment.getActivity());
            }
            else if (fragment.isRemoving() && !mOnSaveInstanceCalled)
            {
                // The fragment can be still in backstack even if isRemoving() is true.
                // We check mOnSaveInstanceCalled - if this was not called then the fragment is totally removed.
                if (BuildConfig.DEBUG)
                {
                    Log.d("mode", "Removing viewmodel - fragment replaced"); //NON-NLS
                }
                removeViewModel(fragment.getActivity());
            }
        }

        /**
         * Use in case this model is associated with an {@link android.app.Activity}
         * Call from {@link android.app.Activity#onDestroy()}
         *
         * @param activity activity
         */
        public void onDestroy(@NonNull final Activity activity)
        {
            if (mViewModel == null)
            {
                //no viewmodel for this fragment
                return;
            }
            mViewModel.clearView();
            if (activity.isFinishing())
            {
                removeViewModel(activity);
            }
        }

        /**
         * Call from {@link android.app.Activity#onStop()} or {@link Fragment#onStop()}
         */
        public void onStop()
        {
            if (mViewModel == null)
            {
                //no viewmodel for this fragment
                return;
            }
            mViewModel.onStop();
        }

        /**
         * Call from {@link android.app.Activity#onStart()} ()} or {@link Fragment#onStart()} ()}
         */
        public void onStart()
        {
            if (mViewModel == null)
            {
                //no viewmodel for this fragment
                return;
            }
            mViewModel.onStart();
        }

        /**
         * Returns the current ViewModel instance associated with the Fragment or Activity.
         * Throws an {@link IllegalStateException} in case the ViewModel is null. This can happen
         * if you call this method too soon - before {@link Activity#onCreate(Bundle)} or {@link Fragment#onCreate(Bundle)}
         * or this {@link eu.inloop.viewmodel.ViewModelHelper} is not properly setup.
         *
         * @return {@link R}
         */
        @NonNull
        public R getViewModel()
        {
            if (null == mViewModel)
            {
                throw new IllegalStateException("ViewModel is not ready. Are you calling this method before Activity/Fragment onCreate?"); //NON-NLS
            }
            return mViewModel;
        }

        /**
         * Call from {@link android.app.Activity#onSaveInstanceState(android.os.Bundle)}
         * or {@link Fragment#onSaveInstanceState(android.os.Bundle)}.
         * This allows the model to save its state.
         *
         * @param bundle bundle
         */
        public void onSaveInstanceState(@NonNull Bundle bundle)
        {
            bundle.putString("identifier", mScreenId);
            if (mViewModel != null)
            {
                mViewModel.onSaveInstanceState(bundle);
                mOnSaveInstanceCalled = true;
            }
        }
    }

}
