package eu.livotov.labs.androidappskeleton.activity;

import android.os.Bundle;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.mikepenz.materialize.MaterializeBuilder;
import eu.livotov.labs.androidappskeleton.R;
import eu.livotov.labs.androidappskeleton.activity.base.BaseActivity;
import eu.livotov.labs.androidappskeleton.example.mvp.TestPresenter;

/**
 * Created by dlivotov on 07/06/2016.
 */

public class MainActivity extends BaseActivity implements TestPresenter.View
{
    @InjectPresenter
    TestPresenter testPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_main);
        new MaterializeBuilder().withActivity(this).build();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        testPresenter.loadTestData();
    }

    @Override
    public void showTestData(String s)
    {
        showMessage("Hello From Template App !\n\n" + s);
    }
}
