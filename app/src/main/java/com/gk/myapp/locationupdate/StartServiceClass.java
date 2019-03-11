package com.gk.myapp.locationupdate;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by Gk on 26-02-2017.
 */
public class StartServiceClass {
    @NonNull
    private static PendingIntent createLocationServiceIntent(@NonNull final Context context) {
        final Intent intent = new Intent(context, LocationUpdateService12.class);
        intent.setAction(LocationUpdateService12.ACTION_HANDLE_LOCATION);

        return PendingIntent.getService(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static void scheduleLocationUpdates(@NonNull final Context context,
                                               @NonNull final GoogleApiClient googleApiClient) {
        // Make sure you have permission, request if necessary
        if (googleApiClient.isConnected() &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                        // ACCESS_COARSE_LOCATION, depending on what you want
                        == PackageManager.PERMISSION_GRANTED) {

            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,
                    LocationRequest.create().setInterval(AlarmManager.INTERVAL_FIFTEEN_MINUTES),
                    createLocationServiceIntent(context));
        }
    }

    public static void unscheduleLocationUpdates(@NonNull final Context context,
                                                 @NonNull final GoogleApiClient googleApiClient) {
        if (googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi
                    .removeLocationUpdates(googleApiClient, createLocationServiceIntent(context));
        }
    }
}
