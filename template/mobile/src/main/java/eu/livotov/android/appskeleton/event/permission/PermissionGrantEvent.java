package eu.livotov.android.appskeleton.event.permission;

import java.util.HashMap;
import java.util.Map;

import eu.livotov.android.appskeleton.core.App;

/**
 * Created by dlivotov on 10/07/2016.
 */

public class PermissionGrantEvent
{
    private Map<String, Boolean> permissions = new HashMap<>();

    public PermissionGrantEvent()
    {
    }

    public void addPermission(final String permission, final boolean isGranted)
    {
        permissions.put(permission, isGranted);
    }

    public boolean isGranted(final String permission)
    {
        if (hasPermission(permission))
        {
            return permissions.get(permission);
        }
        else
        {
            return App.checkPermissions(permission);
        }
    }

    public boolean hasPermission(final String permission)
    {
        return permissions.containsKey(permission);
    }

    public int getPermissionsCount()
    {
        return permissions.keySet().size();
    }
}
