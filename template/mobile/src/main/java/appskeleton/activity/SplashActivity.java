package appskeleton.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by dlivotov on 23/06/2016.
 */

public class SplashActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, MainActivity.class);

        if (getIntent() != null && getIntent().getExtras() != null)
        {
            intent.putExtras(getIntent().getExtras());
        }

        startActivity(intent);
        finish();
    }

}
