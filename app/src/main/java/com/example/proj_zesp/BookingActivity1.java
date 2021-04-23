package com.example.proj_zesp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

public class BookingActivity1 extends AppCompatActivity {

    private Calendar calendar;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;

    private Button check_availability,date_but,hour_but;
    private TextView date_text, hour_text;
    private Date current_data;

    private static String TAG = "Yuriy";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking1);
        Log.d(TAG,">>Boocking screen 1");


        check_availability = (Button) findViewById(R.id.check_av_but);
        date_but = (Button) findViewById(R.id.date_but);
        hour_but = (Button) findViewById(R.id.hour_but);
        date_text = (TextView) findViewById(R.id.date_text);
        hour_text = (TextView) findViewById(R.id.hour_text);

        long millis= System.currentTimeMillis();
        current_data = new Date(millis);

        date_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(BookingActivity1.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int myear, int mmonth, int mdayOfMonth) {
                        date_text.setText(mdayOfMonth + "/" + mmonth+1 + "/" + myear);
                        date_text.setVisibility(View.VISIBLE);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        hour_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();

                Log.d(TAG,"Hour_but clicked");

                timePickerDialog = new TimePickerDialog(BookingActivity1.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        hour_text.setText(hourOfDay + ":" + minute);
                        hour_text.setVisibility(View.VISIBLE);
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),true);
                timePickerDialog.show();
            }
        });



        check_availability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),BookingActivity2.class);
                //i.pu
            }
        });

    }
}