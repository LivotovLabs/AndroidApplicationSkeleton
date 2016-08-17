package eu.livotov.android.appskeleton.util;

import android.Manifest;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import eu.livotov.android.appskeleton.core.App;
import eu.livotov.android.appskeleton.event.location.LocationChangedEvent;
import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.functions.Action1;

/**
 * Created by dlivotov on 17/08/2016.
 */

public class LocationUtil
{
    public static LocationRequestResult requestLocation()
    {
        if (!App.checkPermissions(Manifest.permission.ACCESS_COARSE_LOCATION))
        {
            return LocationRequestResult.NoPermission;
        }
        else if (!isLocationEnabled())
        {
            return LocationRequestResult.NoLocationEnabled;
        }
        else if (!areGoogleServicesPresent())
        {
            return LocationRequestResult.NoGoogleServices;
        }
        else
        {
            final ReactiveLocationProvider provider = new ReactiveLocationProvider(App.getContext());
            provider.getLastKnownLocation().subscribe(new Action1<Location>()
            {
                @Override
                public void call(Location location)
                {
                    App.postEvent(new LocationChangedEvent(location));
                }
            });
            return LocationRequestResult.Enqueued;
        }
    }

    public static boolean isLocationEnabled()
    {
        LocationManager lm = null;
        boolean gps_enabled = false;
        boolean network_enabled = false;

        if (lm == null)
        {
            lm = (LocationManager) App.getContext().getSystemService(Context.LOCATION_SERVICE);
        }
        try
        {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }
        catch (Exception ignored)
        {
        }

        try
        {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }
        catch (Exception ignored)
        {
        }

        return gps_enabled || network_enabled;
    }

    public static boolean areGoogleServicesPresent()
    {
        try
        {
            return GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(App.getContext()) == ConnectionResult.SUCCESS;
        }
        catch (Throwable err)
        {
            return false;
        }
    }

    public enum LocationRequestResult
    {
        Enqueued, NoPermission, NoGoogleServices, NoLocationEnabled;
    }
}
