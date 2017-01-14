package appskeleton.service;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * New incoming push messages will arrive into this service. Add your code to handle actial payload into the todo section below.
 */
public class AppFirebaseMessageService extends FirebaseMessagingService
{

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage)
    {
        try
        {
            //todo: handle your incoming message here via remoteMessage.getData() ...
        }
        catch (Throwable err)
        {
        }
    }
}
