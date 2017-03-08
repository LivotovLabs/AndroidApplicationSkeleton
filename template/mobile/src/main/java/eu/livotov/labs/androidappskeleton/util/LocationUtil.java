package eu.livotov.labs.androidappskeleton.util;

import android.Manifest;

import eu.livotov.labs.android.robotools.location.RTLocation;
import eu.livotov.labs.androidappskeleton.core.App;

/**
 * Created by dlivotov on 17/08/2016.
 */

public class LocationUtil
{
    public static LocationRequestResult checkLocationAvailability()
    {
        if (!App.checkPermissions(Manifest.permission.ACCESS_COARSE_LOCATION))
        {
            return LocationRequestResult.NoPermissions;
        }
        else if (!RTLocation.isLocationEnabled(App.getContext()))
        {
            return LocationRequestResult.NotEnabled;
        }
        else if (!RTLocation.areGoogleServicesPresent(App.getContext()))
        {
            return LocationRequestResult.NoGoogleServices;
        }
        else
        {
            return LocationRequestResult.Available;
        }
    }

    public enum LocationRequestResult
    {
        Available, NoPermissions, NoGoogleServices, NotEnabled
    }
}
