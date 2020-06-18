package com.example.coromap;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(23.7660, 90.3586);
        LatLng dhanmondi = new LatLng(23.7461, 90.3742);
        LatLng sakhertek = new LatLng(23.7670, 90.3566);
        LatLng sakhertek1 = new LatLng(23.7670, 90.3566);
        LatLng sakhertek2 = new LatLng(23.7690, 90.3596);
        LatLng sakhertek3 = new LatLng(23.7672, 90.3569);
        mMap.addMarker(new MarkerOptions().position(sydney).title("In 500 meters"));
        mMap.addMarker(new MarkerOptions().position(dhanmondi).title("In 1 km"));
        mMap.addMarker(new MarkerOptions().position(sakhertek).title("Dear patient, stay home and safe."));
        mMap.addMarker(new MarkerOptions().position(sakhertek1).title("In 200 meters"));
        mMap.addMarker(new MarkerOptions().position(sakhertek2).title("In 500 meters"));
        mMap.addMarker(new MarkerOptions().position(sakhertek3).title("In 120 meters"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sakhertek1));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sakhertek1, 17.0f));
    }
}