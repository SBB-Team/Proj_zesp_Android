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
    private FirebaseFirestore db;
    private MarkerOptions options = new MarkerOptions();
    LatLng Krakow = new LatLng(50.08, 19.99);
    LatLng GS1 =new LatLng(50.086876, 19.990734);
    LatLng GS2 =new LatLng(50.072092, 19.983849);
    LatLng GS3 =new LatLng(50.070595, 20.042382);

    private ArrayList<LatLng> GasStaionList;
    DocumentSnapshot document;

//blabla
//    private void getDataAndSetTexts(String coords){
//        db = FirebaseFirestore.getInstance();
//        Log.d(TAG, "Pobieranie danych");
//        DocumentReference docRef = db.collection("stations").document(coords);
//        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    document = task.getResult();
//                    if (document.exists()) {
//                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
//                        setCoords();
//                    } else {
//                        Log.d(TAG, "No such document");
//                    }
//                } else {
//                    Log.d(TAG, "get failed with ", task.getException());
//                }
//            }
//        });
//    }

//    private void setCoords(){
//        email.setText(document.getString("email"));
//        first_name.setText(document.getString("first_name"));
//        last_name.setText(document.getString("last_name"));
//
//        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
//        String dateString = formatter.format(document.getLong("bday"));
//
//        date_of_birthday.setText(dateString);
//
//        points_of_loyalty.setText(document.get("points").toString());
//    }


//    private void initMarkers(){
//        GasStaionList = new ArrayList<>();
//        GasStaionList.add(new MarkerOptions()
//                .position(new LatLng(50.086876, 19.990734))
//                .title("PB")
//                .snippet("Gas station №1")
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pb_logo))
//        );
//        GasStaionList.add(new MarkerOptions()
//                .position(new LatLng(50.072092, 19.983849))
//                .title("PB")
//                .snippet("Gas station №2")
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pb_logo))
//        );
//        GasStaionList.add(new MarkerOptions()
//                .position(new LatLng(50.070595, 20.042382))
//                .title("PB")
//                .snippet("Gas station №3")
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pb_logo))
//        );
//    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
//        initMarkers();
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

//        for (int i = 0; i < GasStaionList.size(); i++){
////            mMap.addMarker(m);
//            mMap.addMarker();
//            mMap.getUiSettings().setZoomControlsEnabled(true);
//            mMap.getUiSettings().setCompassEnabled(true);
//            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
//                    new LatLng(50.062885, 19.939515), 12.0f
//            ));
//        }

//        for (LatLng point : GasStaionList){
//
//            options.position(GasStaionList.get());
//            options.title("someTitle");
//            options.snippet("someDesc");
//            googleMap.addMarker(options);
//
//        }

        for (int i = 0; i < GasStaionList.size(); i++){
            mMap.addMarker(new MarkerOptions().position(GasStaionList.get(i)));
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setCompassEnabled(true);
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(50.062885, 19.939515), 12.0f));
        }

//        mMap.getUiSettings().setZoomControlsEnabled(true);
//        mMap.getUiSettings().setCompassEnabled(true);
//        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
//                new LatLng(50.062885, 19.939515), 12.0f));
    }

    @Override
    public void finish(){

        super.finish();
        overridePendingTransition(R.anim.slide_to_left, R.anim.slide_from_right);

    }
}