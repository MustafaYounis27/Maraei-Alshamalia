package com.mustafa.maraeialshamalia.Ui;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.mustafa.maraeialshamalia.R;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback
{

    private GoogleMap mMap;
    private double lat, longt;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_maps );

        getMyLocation ();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        //get  restaurant name from  restaurant fragment
        //get lat and longt from  restaurant fragment

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission ( this,
                Manifest.permission.ACCESS_FINE_LOCATION )
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale ( this,
                    Manifest.permission.ACCESS_FINE_LOCATION )) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions ( this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        100 );

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }
    }

    private void getMyLocation()
    {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient ( this );

        if (ActivityCompat.checkSelfPermission ( this, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission ( this, Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        fusedLocationClient.getLastLocation ()
                .addOnSuccessListener ( this, new OnSuccessListener<Location> () {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null)
                        {
                            lat = location.getLatitude ();
                            longt = location.getLongitude ();

                            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager ()
                                    .findFragmentById ( R.id.map );
                            mapFragment.getMapAsync ( MapsActivity.this );
                            Toast.makeText ( MapsActivity.this, lat + "," + longt, Toast.LENGTH_SHORT ).show ();

                            SharedPreferences sharedPreferences = getSharedPreferences ( "myLocation", Context.MODE_PRIVATE );
                            sharedPreferences.edit ().putString ( "lat", lat+"" ).apply ();
                            sharedPreferences.edit ().putString ( "longt", longt+"" ).apply ();
                        }
                        else{
                            getMyLocation ();
                        }
                    }
                } );
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(lat, longt);
        Marker marker = mMap.addMarker(new MarkerOptions().position(sydney));
        marker.showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 17.0f));
    }

    public void next(View view)
    {
        onBackPressed ();
    }
}