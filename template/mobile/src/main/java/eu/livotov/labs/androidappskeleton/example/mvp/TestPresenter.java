package eu.livotov.labs.androidappskeleton.example.mvp;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.UUID;

/**
 * Created by dlivotov on 08/03/2017.
 */

@InjectViewState
public class TestPresenter extends MvpPresenter<TestPresenter.View>
{

    public void loadTestData()
    {
        getViewState().showTestData(UUID.randomUUID().toString());
    }

    @StateStrategyType(SkipStrategy.class)
    public interface View extends MvpView
    {
        void showTestData(String s);
    }
}
