package com.example.proj_zesp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ViewProfileActivity extends AppCompatActivity {

    private TextView first_name, last_name, email, points_of_loyalty, date_of_birthday;
    private FirebaseFirestore db;
    private static String TAG = "Yuriy";
    DocumentSnapshot document; /// to jest źle

    private void showLoading(){  /////////////////////////////// zrobić

    }

    private void getDataAndSetTexts(String current_user){
        db = FirebaseFirestore.getInstance();
        Log.d(TAG, "Pobieranie danych");
        showLoading();
        DocumentReference docRef = db.collection("users").document(current_user);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        setTexts();
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    private void setTexts(){
        email.setText(document.getString("email"));
        first_name.setText(document.getString("first_name"));
        last_name.setText(document.getString("last_name"));

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = formatter.format(document.getLong("bday"));

        date_of_birthday.setText(dateString);

        points_of_loyalty.setText(document.get("points").toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        first_name = (TextView) findViewById(R.id.pt_first_name);
        last_name = (TextView) findViewById(R.id.pt_last_name);
        email = (TextView) findViewById(R.id.pt_email);
        points_of_loyalty = (TextView) findViewById(R.id.pt_points);
        date_of_birthday = (TextView) findViewById(R.id.pt_birthday);

        getDataAndSetTexts(FirebaseAuth.getInstance().getCurrentUser().getEmail());



    }

    @Override
    public void finish(){

        super.finish();
        overridePendingTransition(R.anim.slide_to_left, R.anim.slide_from_right);

    }
}