package me.minitrabajo.controller;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Scott on 05/10/2016.
 */
public class GPS implements  GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {


    //private PendingResult<LocationSettingsResult> mLocationSettingRequestResult;
    private double currentLatitude;
    private double currentLongitude;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Context context;

    public ResponseGPS delegate = null;

    public GPS(Context context)
    {
        if (googleApiClient == null) {
            Log.w("onCreate", "Create the GoogleApiClient object");
            googleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        Log.w("onCreate", "Create the LocationRequest object");
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds

    }

    @Override
    public void onConnected(Bundle bundle)
    {
        Log.i("Location", "Location services connected.");

        try {
            Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

            if (location == null)
            {
                LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);

            } else {
                //If everything went fine lets get latitude and longitude
                this.currentLatitude = location.getLatitude();
                this.currentLongitude = location.getLongitude();
                Log.v("GPS:Position",String.valueOf(currentLatitude)+":"+ String.valueOf(currentLongitude));
            }
        }
        catch (SecurityException ex)
        {
            Log.i("GPS:onConnect", ex.getMessage());
            delegate.onGPSWarning("GPS failed to connect");
        }
    }

    @Override
    public void onConnectionSuspended(int i)
    {
        Log.v("GPS:onConnectionSuspend", "Location services suspended. Please reconnect.");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.v("GPS:onConnectionFailed", "Failed");
        if (connectionResult.hasResolution()) {
            // Start an Activity that tries to resolve the error
            delegate.onGPSConnectionResolutionRequest(connectionResult);
        } else {
                // If no resolution is available, display a dialog to the user with the error.
            Log.e("Error", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        this.currentLatitude = location.getLatitude();
        this.currentLongitude = location.getLongitude();
        Log.v("GPS:Location",String.valueOf(currentLatitude)+":"+ String.valueOf(currentLongitude));
    }

    public LatLng getLocation()
    {
        this.Resume();
        LatLng p = new LatLng(currentLatitude,currentLongitude);
        this.Pause();
        return p;
    }

    public void Resume() {
        googleApiClient.connect();
        Log.i("GPS:onResume()", "Called");
    }

    public void Pause()
    {
        Log.v("GPS:onPause()", "Called");
        if (googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
            googleApiClient.disconnect();
        }
    }


}
