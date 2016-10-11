package me.minitrabajo.view;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import me.minitrabajo.R;

//implements OnMapReadyCallback
public class MapActivity extends FragmentActivity implements OnMapReadyCallback , GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    //private MapView mapView;
    private Marker mMarker;

    //private Button btnMap, btnList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);

        // Add a marker in Sydney, Australia, and move the camera.
        LatLng sydney = new LatLng(-34, 151);
        mMarker = mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

    }

    @Override
    public boolean onMarkerClick(final Marker marker)
    {
        Log.w("MaarkerClick", "Map Marker Clicked");

        Toast.makeText(this,marker.getTitle(),Toast.LENGTH_LONG).show();

        if (marker.equals(marker))
        {
            //handle click here
        }

        return true;
    }

    public void onMapClick(View view)
    {
        Log.w("MapClick", "Map button Clicked -> Do Nothing");
    }

    public void onListClick(View view)
    {
        /*Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
        Log.w("ListClick", "Map button Clicked");*/
    }

}
