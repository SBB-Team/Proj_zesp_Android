package com.example.proj_zesp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BookingActivity1 extends AppCompatActivity {

    private Calendar calendar;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;

    private Button check_availability,date_but,hour_but;
    private TextView date_text, hour_text;
    private Spinner station_spinner;

    private Date current_date;
    private Date booking_date;

    private ArrayList<String> stations_str = new ArrayList<String>();
    private ArrayAdapter<String> adapter;

    private DocumentSnapshot document;

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    private static String TAG = "Yuriy";

    private String dateForBooking = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking1);
        Log.d(TAG,">>Booking screen 1");


        check_availability = (Button) findViewById(R.id.check_av_but);
        date_but = (Button) findViewById(R.id.date_but);
        hour_but = (Button) findViewById(R.id.hour_but);
        date_text = (TextView) findViewById(R.id.date_text);
        hour_text = (TextView) findViewById(R.id.hour_text);

        getStations();

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        station_spinner = (Spinner) findViewById(R.id.station_spinner);
        station_spinner.setAdapter(adapter);



        long millis= System.currentTimeMillis();
        current_date = new Date(millis);

        date_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);     // musi być +1
                int year = calendar.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(BookingActivity1.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int myear, int mmonth, int mdayOfMonth) {
                        date_text.setText(((mdayOfMonth<10) ? "0"+mdayOfMonth : mdayOfMonth)
                                + ":" + ((mmonth<10) ? "0"+(mmonth+1) : (mmonth+1)) + ":" + myear);
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

                timePickerDialog = new TimePickerDialog(BookingActivity1.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        hour_text.setText(((hourOfDay<10) ? "0"+hourOfDay : hourOfDay) + ":" +
                                ((minute<10) ? "0"+minute : minute));
                        hour_text.setVisibility(View.VISIBLE);
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),true);
                timePickerDialog.show();
            }
        });

        check_availability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_availability_meth();
            }
        });

    }

    private void check_availability_meth(){



        switch (station_spinner.getSelectedItem().toString()){
            case "station1":
                dateForBooking+="001_";
                break;
            case "station2":
                dateForBooking+="002_";
                break;
        }

        if(date_text != null && !hour_text.getText().equals("") && !date_text.getText().equals("- error -")){
//            dateForBooking += date_text.getText().subSequence(6,9) + ":"; //year
//            dateForBooking += date_text.getText().subSequence(3,4) + ":"; //month
//            dateForBooking += date_text.getText().subSequence(0,1);       //day
            dateForBooking += date_text.getText() + "_";
        }else{
            Toast toast = Toast.makeText(getApplicationContext(), "Choose date for your booking", Toast.LENGTH_LONG);
            toast.show();
        }

        Log.d(TAG,"Hour: _"+ hour_text.getText() +"_");
        if(hour_text != null && !hour_text.getText().equals("") && !hour_text.getText().equals("- error -")){
            dateForBooking += hour_text.getText();
        }else{
            Toast toast = Toast.makeText(getApplicationContext(), "Choose hour for your booking", Toast.LENGTH_LONG);
            toast.show();
        }

        Log.d(TAG,"Date: _" + date_text.getText() +"_");
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("booking").document(dateForBooking);


        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "Date already booked");
                        Toast toast = Toast.makeText(getApplicationContext(), "Choosen date is already booked", Toast.LENGTH_LONG);
                        toast.show();
                    } else {
                        Log.d(TAG, "Date is free");

                        Map<String, Object> choosedDate = new HashMap<>();
                        choosedDate.put("day", date_text.getText().subSequence(0,2));
                        choosedDate.put("hour", hour_text.getText().subSequence(0,2));
                        choosedDate.put("minute", hour_text.getText().subSequence(3,5));
                        choosedDate.put("month", date_text.getText().subSequence(3,5));
                        choosedDate.put("station_id", dateForBooking.subSequence(0,3));
                        choosedDate.put("year", date_text.getText().subSequence(6,10));




                        db.collection("booking").document(dateForBooking)
                                .set(choosedDate)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "Date was booked!");
                                        Toast toast = Toast.makeText(getApplicationContext(), "You have booked that date", Toast.LENGTH_LONG);
                                        toast.show();
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error during adding", e);
                                    }
                                }
                        );
                    }
                } else {
                    Log.d(TAG, "Failed with: ", task.getException());
                }
            }
        });
    }


    private ArrayList<String> getStations(){
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("stations").document("Stations_count_names");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    document = task.getResult();
                    if (document.exists()) {
                        //Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        if (document.getLong("count")>=1){
                            int count = 1;
                            do{
                                stations_str.add(document.getString(String.valueOf(count)));
                                adapter.add(document.getString(String.valueOf(count)));
                                count++;
                            }while(count-1 != document.getLong("count"));
                        }

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        return stations_str;
    }
}