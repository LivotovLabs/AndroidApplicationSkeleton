package eu.livotov.labs.androidappskeleton.ui;

import android.os.Bundle;
import com.mikepenz.materialize.MaterializeBuilder;
import eu.livotov.labs.androidappskeleton.R;
import eu.livotov.labs.androidappskeleton.core.base.BaseActivity;

/**
 * Created by dlivotov on 07/06/2016.
 */

public class MainActivity extends BaseActivity
{

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
    }

}
