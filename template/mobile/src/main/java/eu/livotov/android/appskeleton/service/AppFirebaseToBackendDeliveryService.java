package eu.livotov.android.appskeleton.service;

import android.app.IntentService;
import android.content.Intent;
import android.text.TextUtils;

import com.google.firebase.iid.FirebaseInstanceId;

/**
 * This is semi-template class to handle new push token delivery to your backend service.
 * Simply implement the 3 methods at the bottom and you're done ! :)
 */
public class AppFirebaseToBackendDeliveryService extends IntentService
{

    public AppFirebaseToBackendDeliveryService()
    {
        super(AppFirebaseToBackendDeliveryService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        final String lastDeliveredToken = getLastDeliveredToken();
        final String currentToken = FirebaseInstanceId.getInstance().getToken();

        if (!TextUtils.isEmpty(currentToken))
        {
            if (lastDeliveredToken == null || !lastDeliveredToken.equalsIgnoreCase(currentToken))
            {
                try
                {
                    sendTokenToBackend(currentToken);
                    saveLastDeliveredToken(currentToken);
                }
                catch (Throwable e)
                {
                }
            }
        }
    }

    /**
     * Add here you code to retrieve the last saved (via the saveLastDeliveredToken method) token
     * from the settings. If no token was previously saved, return null.
     *
     * @return last saved token
     */
    private String getLastDeliveredToken()
    {
        return "123";
    }

    /**
     * Add here your own code to send supplied token to you backend service. You're safe to call
     * network from here, do not spawn any additional threads !
     *
     * @param token token to be sent to your backend
     */
    private void sendTokenToBackend(String token)
    {

    }

    /**
     * Add here your code to save the supplied token value somewhere in your settings
     *
     * @param token last backend delivered token to save
     */
    private void saveLastDeliveredToken(String token)
    {

    }
}
