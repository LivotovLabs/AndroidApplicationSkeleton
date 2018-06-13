package eu.livotov.labs.androidappskeleton

import android.Manifest
import android.content.Context
import android.location.Location
import android.location.LocationManager
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability

class LocationHelper()
{
    companion object
    {
        private val PK = 180 / 3.1415926
        private val EARTH_RADIUS = 6371.0

        fun findDistanceInKm(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double = Math.acos(Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * (Math.cos(Math.toRadians(lon1)) * Math.cos(Math.toRadians(lon2)) + Math.sin(Math.toRadians(lon1)) * Math.sin(Math.toRadians(lon2)))) * EARTH_RADIUS
        fun findDistanceInKm(fromLocation: Location, toLocation: Location): Double = Math.abs(fromLocation.distanceTo(toLocation)).toDouble() / 1000

        fun isLocationServiceEnabled(ctx: Context = App.self): Boolean
        {
            var lm: LocationManager? = null
            var gps_enabled = false
            var network_enabled = false

            if (lm == null)
            {
                lm = ctx.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            }
            try
            {
                gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
            }
            catch (ignored: Exception)
            {
            }

            try
            {
                network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            }
            catch (ignored: Exception)
            {
            }

            return gps_enabled || network_enabled
        }

        fun isGoogleServicesPresent(ctx: Context = App.self): Boolean
        {
            return try
            {
                GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(ctx) == ConnectionResult.SUCCESS
            }
            catch (err: Throwable)
            {
                false
            }

        }

        fun getLocationServicesGlobalStatus(): LocationServicesStatus
        {
            return if (!App.hasPermissions(Manifest.permission.ACCESS_COARSE_LOCATION))
            {
                LocationServicesStatus.NoPermissions
            } else if (!isLocationServiceEnabled())
            {
                LocationServicesStatus.NotEnabled
            } else if (!isGoogleServicesPresent())
            {
                LocationServicesStatus.HasGeopositionButNoGoogleServices
            } else
            {
                LocationServicesStatus.FullyPresent
            }
        }
    }
}

enum class LocationServicesStatus
{
    FullyPresent, NoPermissions, HasGeopositionButNoGoogleServices, NotEnabled
}
