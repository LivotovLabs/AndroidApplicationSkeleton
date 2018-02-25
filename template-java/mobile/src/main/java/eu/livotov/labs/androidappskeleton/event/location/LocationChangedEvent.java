package eu.livotov.labs.androidappskeleton.event.location;

import android.location.Location;

/**
 * Created by dlivotov on 17/08/2016.
 */

public class LocationChangedEvent
{
    private final Location location;

    public LocationChangedEvent(Location location)
    {
        this.location = location;
    }

    public Location getLocation()
    {
        return location;
    }
}
