package com.example.proj_zesp;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static String TAG = "Yuriy";

    private MarkerOptions options = new MarkerOptions();
    LatLng Krakow = new LatLng(50.08, 19.99);
    LatLng GS1 =new LatLng(50.086876, 19.990734);
    LatLng GS2 =new LatLng(50.072092, 19.983849);
    LatLng GS3 =new LatLng(50.070595, 20.042382);

    private ArrayList<LatLng> GasStaionList;
    DocumentSnapshot document;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        GasStaionList = new ArrayList<LatLng>();
        GasStaionList.add(GS1);
        GasStaionList.add(GS2);
        GasStaionList.add(GS3);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        for (int i = 0; i < GasStaionList.size(); i++){
            mMap.addMarker(new MarkerOptions().position(GasStaionList.get(i)));
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setCompassEnabled(true);
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(50.082885, 19.989515), 12.0f));
        }

    }

    @Override
    public void finish(){

        super.finish();
        overridePendingTransition(R.anim.slide_to_left, R.anim.slide_from_right);

    }
}