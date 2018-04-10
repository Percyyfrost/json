package com.atc.vuyaninxele.sight3;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentController;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by vuyani.nxele on 2018/04/04.
 */

public class LocationLiveData extends LiveData<Location> implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener{
    private GoogleApiClient googleApiClient;

    public LocationLiveData(Context context) {
        googleApiClient =
                new GoogleApiClient.Builder(context, this, this)
                        .addApi(LocationServices.API)
                        .build();
    }

    @Override
    protected void onActive() {
        // Wait for the GoogleApiClient to be connected
        googleApiClient.connect();
    }

    @Override
    protected void onInactive() {
        if (googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    googleApiClient, this);
        }
        googleApiClient.disconnect();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onConnected(Bundle connectionHint) {
        // Try to immediately find a location

            LocationRequest locationRequest;
            locationRequest = new LocationRequest()
                    .setInterval(10 * 1000)
                    .setFastestInterval(15 * 1000)
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            @SuppressLint("MissingPermission") Location lastLocation = LocationServices.FusedLocationApi
                    .getLastLocation(googleApiClient);
            if (lastLocation != null) {
                setValue(lastLocation);
            }
            // Request updates if there’s someone observing
            if (hasActiveObservers()) {
                LocationServices.FusedLocationApi.requestLocationUpdates(
                        googleApiClient, locationRequest, this);
            }
    }
    @Override
    public void onLocationChanged(Location location) {
        // Deliver the location changes
        setValue(location);
    }
    @Override
    public void onConnectionSuspended(int cause) {
        // Cry softly, hope it comes back on its own
    }
    @Override
    public void onConnectionFailed(
            @NonNull ConnectionResult connectionResult) {
        // Consider exposing this state as described here:
        // https://d.android.com/topic/libraries/architecture/guide.html#addendum
    }

}