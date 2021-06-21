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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import static java.lang.Integer.parseInt;

public class BookingActivity1 extends AppCompatActivity {

    private Calendar calendar;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;

    private Button check_availability,date_but,hour_but;
    private TextView date_text, hour_text;
    private Spinner station_spinner;

    private ArrayList<String> stations_str = new ArrayList<String>();
    private ArrayAdapter<String> adapter;

    private DocumentSnapshot document;

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    private static String TAG = "Yuriy";

    private String dateForBooking = "";
    private int countOfDates = 0;
    private int passedDates = 0;

    private DateOfBooking dateOfBooking;


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

        dateOfBooking = new DateOfBooking();

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

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
                                + "." + ((mmonth<10) ? "0"+(mmonth+1) : (mmonth+1)) + "." + myear);
                        date_text.setVisibility(View.VISIBLE);

                        dateOfBooking.setYear(myear);
                        dateOfBooking.setMonth(mmonth+1);
                        dateOfBooking.setDayOfMonth(mdayOfMonth);
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
                        hour_text.setText(((hourOfDay<10) ? "0"+hourOfDay : hourOfDay) + ":" + ((minute<10) ? "0"+minute : minute));
                        hour_text.setVisibility(View.VISIBLE);

                        dateOfBooking.setHour(hourOfDay);
                        dateOfBooking.setMinute(minute);
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),true);
                timePickerDialog.show();
            }
        });

        check_availability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(
                        calendar.get(Calendar.YEAR) > dateOfBooking.getYear() ||
                        (calendar.get(Calendar.MONTH)+1) > dateOfBooking.getMonth() ||
                        calendar.get(Calendar.DAY_OF_MONTH) > dateOfBooking.getDayOfMonth() ||
                        calendar.get(Calendar.HOUR_OF_DAY) > dateOfBooking.getHour() ||
                        calendar.get(Calendar.MINUTE) > dateOfBooking.getMinute()
                ){
                    Toast toast = Toast.makeText(getApplicationContext(), "You cannot select a date from the past", Toast.LENGTH_LONG);
                    toast.show();
                }else{
                    Log.d(TAG,"FSDFDS");
                    check_availability_meth();
                }
            }
        });
    }

    private void check_availability_meth(){

        //station add text and set value
        dateForBooking+= "00" + (station_spinner.getSelectedItemPosition()+1) + "_";
        dateOfBooking.setStation_id("00" + (station_spinner.getSelectedItemPosition()+1));


        // year,month,day add text and exception
        if(date_text != null && !hour_text.getText().equals("") && !date_text.getText().equals("- error -")){
            dateForBooking += date_text.getText() + "_";
        }else{
            Toast toast = Toast.makeText(getApplicationContext(), "Choose date for your booking", Toast.LENGTH_LONG);
            toast.show();
        }

        // hour,minute add text and exception
        if(hour_text != null && !hour_text.getText().equals("") && !hour_text.getText().equals("- error -")){
            dateForBooking += hour_text.getText();
        }else{
            Toast toast = Toast.makeText(getApplicationContext(), "Choose hour for your booking", Toast.LENGTH_LONG);
            toast.show();
        }

        Log.d(TAG,"Date: _" + dateForBooking +"_");

        getBookingIn15Min(
                dateOfBooking.getYear()
                ,dateOfBooking.getMonth()
                ,dateOfBooking.getDayOfMonth()
                ,dateOfBooking.getHour()
                ,dateOfBooking.getMinute()
                ,dateOfBooking.getStation_id());
    }



    private void getBookingIn15Min(int year, int month, int day, int hour, int minute, String station_id){
        countOfDates = 0;

//------- Logs for showing picked date
//        Log.d(TAG, "Checking date --------------");
//        Log.d(TAG, "Year: " + dateOfBooking.getYear());
//        Log.d(TAG, "Month: " + dateOfBooking.getMonth());
//        Log.d(TAG, "Day: " + dateOfBooking.getDayOfMonth());
//        Log.d(TAG, "Hour: " + dateOfBooking.getHour());
//        Log.d(TAG, "Minute: " + dateOfBooking.getMinute());
//        Log.d(TAG, "Station id: " + dateOfBooking.getStation_id());
//        Log.d(TAG, "User: " + dateOfBooking.getUser());
//        Log.d(TAG, "End check ------------------");


        db.collection("booking")
                .whereEqualTo("year",dateOfBooking.getYear())
                .whereEqualTo("month",dateOfBooking.getMonth())
                .whereEqualTo("dayOfMonth",dateOfBooking.getDayOfMonth())
                .whereEqualTo("station_id",dateOfBooking.getStation_id())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isComplete()){
                            for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                                countOfDates++;

                                Log.d(TAG, "lower: " + (queryDocumentSnapshot.getDouble("hour")*60 +
                                        queryDocumentSnapshot.getDouble("minute") - 15) + "---");
                                Log.d(TAG, "upper: " + (queryDocumentSnapshot.getDouble("hour")*60 +
                                        queryDocumentSnapshot.getDouble("minute") + 15));
                                Log.d(TAG, "current: " + ((hour * 60) + minute));


                                if( queryDocumentSnapshot.getDouble("hour")*60 +
                                        queryDocumentSnapshot.getDouble("minute") - 15 <
                                        ((hour * 60) + minute)
                                &&  queryDocumentSnapshot.getDouble("hour")*60 +
                                        queryDocumentSnapshot.getDouble("minute") + 15 >
                                        ((hour * 60) + minute)
                                ){
                                    Log.d(TAG, "Date booked");
                                }else{
                                    passedDates++;
                                }
                            }

                            Log.d(TAG, "Dates count: " + (countOfDates+1));
                            if ( countOfDates == 0){
                                Log.d(TAG, "Date is free");
                                bookDate();
                            }else if (passedDates <= countOfDates){
                                Log.d(TAG, "Date is free");
                                bookDate();
                            }else{
                                Toast toast = Toast.makeText(getApplicationContext(), "Chosen date is already booked", Toast.LENGTH_LONG);
                                toast.show();
                                passedDates = 0;
                                countOfDates = 0;
                            }
                        }
                    }
                });
    }

    private void bookDate(){
        dateOfBooking = new DateOfBooking(
                date_text.getText().subSequence(6,10),
                date_text.getText().subSequence(3,5),
                date_text.getText().subSequence(0,2),
                hour_text.getText().subSequence(0,2),
                hour_text.getText().subSequence(3,5),
                dateForBooking.subSequence(0,3)
        );





        db.collection("booking").document(dateForBooking)
            .set(dateOfBooking)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast toast = Toast.makeText(getApplicationContext(), "Booking completed succesfully", Toast.LENGTH_LONG);
                toast.show();
                finish();
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

    private class DateOfBooking{
        private int year;
        private int month;
        private int dayOfMonth;
        private int hour;
        private int minute;
        private String station_id;
        private String user;

        public DateOfBooking(int year, int month, int dayOfMonth, int hour, int minute, String station_id) {
            this.year = year;
            this.month = month;
            this.dayOfMonth = dayOfMonth;
            this.hour = hour;
            this.minute = minute;
            this.station_id = station_id;
            this.user = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        }

        public DateOfBooking(String year, String month, String dayOfMonth, String hour, String minute, String station_id) {
            this.year = parseInt(year);
            this.month = parseInt(month);;
            this.dayOfMonth = parseInt(dayOfMonth);;
            this.hour = parseInt(hour);;
            this.minute = parseInt(minute);;
            this.station_id = station_id;
            this.user = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        }

        public DateOfBooking(CharSequence year, CharSequence month, CharSequence dayOfMonth, CharSequence hour, CharSequence minute, CharSequence station_id) {
            this.year = parseInt(year.toString());
            this.month = parseInt(month.toString());
            this.dayOfMonth = parseInt(dayOfMonth.toString());
            this.hour = parseInt(hour.toString());
            this.minute = parseInt(minute.toString());
            this.station_id = station_id.toString();
            this.user = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        }

        public DateOfBooking() {
            year = 0;
            month = 0;
            dayOfMonth = 0;
            hour = 0;
            minute = 0;
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public int getMonth() {
            return month;
        }

        public void setMonth(int month) {
            this.month = month;
        }

        public int getDayOfMonth() {
            return dayOfMonth;
        }

        public void setDayOfMonth(int dayOfMonth) {
            this.dayOfMonth = dayOfMonth;
        }

        public int getHour() {
            return hour;
        }

        public void setHour(int hour) {
            this.hour = hour;
        }

        public int getMinute() {
            return minute;
        }

        public void setMinute(int minute) {
            this.minute = minute;
        }

        public String getStation_id() {
            return station_id;
        }

        public void setStation_id(String station_id) {
            this.station_id = station_id;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }
    }
}