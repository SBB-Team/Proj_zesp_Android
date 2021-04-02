package com.example.proj_zesp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainMenu extends AppCompatActivity {

    private TextView view_profile_text,car_wash_text,location_text,prices_text;
   private  ImageView view_profile_logo,car_wash_booking_logo,location_logo,prices_logo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);



        TextView view_profile_text = (TextView) findViewById(R.id.view_profile_text);
        TextView car_wash_text = (TextView) findViewById(R.id.car_wash_text);
        TextView location_text = (TextView) findViewById(R.id.location_text);
        TextView prices_text = (TextView) findViewById(R.id.prices_text);

        ImageView view_profile_logo = (ImageView) findViewById(R.id.view_profile_logo);
        ImageView car_wash_booking_logo = (ImageView) findViewById(R.id.car_wash_booking_logo);
        ImageView location_logo = (ImageView) findViewById(R.id.location_logo);
        ImageView prices_logo = (ImageView) findViewById(R.id.prices_logo);



        //Jump to View Profile Activity

        view_profile_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ViewProfileActivity.class);
                startActivity(i);
            }
        });




    }



}