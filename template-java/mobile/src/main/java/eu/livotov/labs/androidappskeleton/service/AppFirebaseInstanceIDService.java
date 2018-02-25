package eu.livotov.labs.androidappskeleton.service;

import android.content.Intent;

import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * This class handles push token changes. Usually you don't need to add any code here.
 * You only need to acc actual backend delivery code into the AppFirebaseToBackendDeliveryService class.
 */
public class AppFirebaseInstanceIDService extends FirebaseInstanceIdService
{

    @Override
    public void onTokenRefresh()
    {
        startService(new Intent(this, AppFirebaseToBackendDeliveryService.class));
    }
}
