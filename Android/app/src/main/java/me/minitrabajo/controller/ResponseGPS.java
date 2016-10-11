package me.minitrabajo.controller;

import com.google.android.gms.common.ConnectionResult;

/**
 * Created by Scott on 05/10/2016.
 */
public interface ResponseGPS {

    //static final int REQUEST_CONNECTION_FAILURE_RESOLUTION = 0;
    //static final int REQUEST_PERMISSION_ACCESS_FINE_LOCATION = 1;

    static final int REQUEST_LOCATION = 2;
    static final  int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    void onGPSConnectionResolutionRequest(ConnectionResult connectionResult );
    void onGPSWarning(String string);
    //void onGPSPositionResult();
}
