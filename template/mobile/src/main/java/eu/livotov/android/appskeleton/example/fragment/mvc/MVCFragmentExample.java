package eu.livotov.android.appskeleton.example.fragment.mvc;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import eu.livotov.android.appskeleton.fragment.base.BaseMvcFragment;

/**
 * Created by dlivotov on 07/06/2016.
 */

public class MVCFragmentExample extends BaseMvcFragment<ViewExample, ModelExample> implements ViewExample
{
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        setModelView(this);
    }

    @Nullable
    @Override
    public Class<ModelExample> getViewModelClass()
    {
        return null;
    }

    @Override
    public void showDataInUI(String sampleData)
    {

    }
}
