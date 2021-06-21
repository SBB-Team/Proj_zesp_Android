package com.example.proj_zesp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.QuerySnapshot;

public class MainMenu extends AppCompatActivity {

    // Defining instances - start
    private TextView view_profile_text,car_wash_text,location_text,prices_text;
    private  ImageView view_profile_logo,car_wash_booking_logo,location_logo,prices_logo;
    private ImageView logo;
    FirebaseAuth auth;
    // Defining instances - finish

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);


        // Finding id`s - start
        TextView view_profile_text = (TextView) findViewById(R.id.view_profile_text);
        TextView car_wash_text = (TextView) findViewById(R.id.car_wash_text);
        TextView location_text = (TextView) findViewById(R.id.location_text);
        TextView prices_text = (TextView) findViewById(R.id.prices_text);

        ImageView view_profile_logo = (ImageView) findViewById(R.id.view_profile_logo);
        ImageView car_wash_booking_logo = (ImageView) findViewById(R.id.car_wash_booking_logo);
        ImageView location_logo = (ImageView) findViewById(R.id.location_logo);
        ImageView prices_logo = (ImageView) findViewById(R.id.prices_logo);
        ImageView logo = (ImageView) findViewById(R.id.logo);
        // Finding id`s - finish

        // authorization instance
        auth = FirebaseAuth.getInstance();

        // Logging out from ViewProfile Activity - start
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Intent i = new Intent(getApplicationContext(), FirstStartScreen.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_to_left, R.anim.slide_from_right);
                finish();
            }
        });
        // Logging out from ViewProfile Activity - finish

        // Moving to View Profile Activity - start
        view_profile_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ViewProfileActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_to_right, R.anim.slide_from_left);
                finish();
            }
        });
        // Moving to View Profile Activity - finish

        // Moving to BookingActivity1 - start
        car_wash_booking_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),BookingActivity1.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_to_right, R.anim.slide_from_left);
            }
        });
        // Moving to BookingActivity1 - finish

        // Moving to MapsActivity - start
        location_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_to_right, R.anim.slide_from_left);
            }
        });
        // Moving to MapsActivity - finish

        // Moving to PricesActivity - start
        prices_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), PricesActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_to_right, R.anim.slide_from_left);
            }
        });
        // Moving to PricesActivity - finish
    }
}