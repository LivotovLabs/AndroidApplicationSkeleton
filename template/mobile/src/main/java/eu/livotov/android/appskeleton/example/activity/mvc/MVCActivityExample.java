package eu.livotov.android.appskeleton.example.activity.mvc;

import android.os.Bundle;
import android.support.annotation.Nullable;

import eu.livotov.android.appskeleton.activity.base.BaseMvcActivity;
import eu.livotov.android.appskeleton.example.fragment.mvc.ModelExample;
import eu.livotov.android.appskeleton.example.fragment.mvc.ViewExample;

/**
 * Created by dlivotov on 07/06/2016.
 */

public class MVCActivityExample extends BaseMvcActivity<ViewExample, ModelExample> implements ViewExample
{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
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
