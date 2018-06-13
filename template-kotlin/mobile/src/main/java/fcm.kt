package eu.livotov.labs.androidappskeleton

import android.content.Intent
import android.support.v4.app.JobIntentService
import android.text.TextUtils
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class AppFirebaseInstanceIDService : FirebaseInstanceIdService()
{
    override fun onTokenRefresh()
    {
        JobIntentService.enqueueWork(this, AppFirebaseToBackendDeliveryService::class.java, 1234, Intent())
    }
}

class AppFirebaseToBackendDeliveryService : JobIntentService()
{
    override fun onHandleWork(intent: Intent)
    {
        val lastDeliveredToken = AppSettings.lastDeliveredFcmToken
        val currentToken = FirebaseInstanceId.getInstance().token ?: ""

        if (!TextUtils.isEmpty(currentToken) && !lastDeliveredToken.equals(currentToken, ignoreCase = true))
        {
            try
            {
                sendTokenToBackend(currentToken)
                AppSettings.lastDeliveredFcmToken = currentToken
            }
            catch (e: Throwable)
            {
            }
        }
    }

    private fun sendTokenToBackend(token: String)
    {
    }
}

class AppFirebaseMessageService : FirebaseMessagingService()
{
    override fun onMessageReceived(remoteMessage: RemoteMessage?)
    {
    }
}