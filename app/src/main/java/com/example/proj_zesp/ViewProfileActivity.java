package com.example.proj_zesp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
    // Creating instances - start
    private TextView first_name, last_name, email, points_of_loyalty, date_of_birthday;
    private ImageView logo;
    private Button logout_btn, edit_prof_btn;
    private FirebaseFirestore db;
    private static String TAG = "Yuriy";
    private FirebaseAuth auth;
    private DocumentSnapshot document;
    private ProgressDialog dialog;
    // Creating instances - finish

    //  Getter from db to TextView`s - start
    private void getDataAndSetTexts(String current_user){
        db = FirebaseFirestore.getInstance();
        Log.d(TAG, "Getting data");
        DocumentReference docRef = db.collection("users").document(current_user);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        setTexts();
                        dialog.dismiss();
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());//
                }
            }
        });
    }
    //  Getter from db to TextView`s - finish

    // Setting text - start
    private void setTexts(){
        // Creating instances - start
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = formatter.format(document.getLong("bday"));
        // Creating instances - finish

        //Text setters - start
        email.setText(document.getString("email"));
        first_name.setText(document.getString("first_name"));
        last_name.setText(document.getString("last_name"));
        date_of_birthday.setText(dateString);
        points_of_loyalty.setText(document.get("points").toString());
        // Text setters - finish
    }
        // Setting text - finish

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // Loading dialog - start
        dialog = new ProgressDialog(ViewProfileActivity.this);
        dialog.setMessage("Loading...");
        dialog.show();
        dialog.setCancelable(false);
        // Loading dialog - finish

        //Handler (waiting for 2 seconds before dialog disappears) - start
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                dialog.dismiss();
            }
        }, 2000);
        //Handler (waiting for 2 seconds before dialog disappears) - finish

        //Creating instances - start
        auth = FirebaseAuth.getInstance();
        first_name = (TextView) findViewById(R.id.pt_first_name);
        last_name = (TextView) findViewById(R.id.pt_last_name);
        email = (TextView) findViewById(R.id.pt_email);
        points_of_loyalty = (TextView) findViewById(R.id.pt_points);
        date_of_birthday = (TextView) findViewById(R.id.pt_birthday);
        logo = (ImageView) findViewById(R.id.logo);
        logout_btn = (Button) findViewById(R.id.logout_btn);
        edit_prof_btn = (Button) findViewById(R.id.edit_prof_btn);
        Log.d(TAG,"Current user: " + FirebaseAuth.getInstance().getCurrentUser().getEmail());
        getDataAndSetTexts(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        //Creating instances - finish

        // LOGOUT ON CLICK METHOD - start
        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Log.d(TAG, "Log out");

                Intent i = new Intent(getApplicationContext(), FirstStartScreen.class);
                startActivity(i);
                finish();
                overridePendingTransition(R.anim.slide_to_left, R.anim.slide_from_right);

            }
        });
        // LOGOUT ON CLICK METHOD - finish


        // EDIT PROFILE ON CLICK METHOD - start
        edit_prof_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), EditProfileActivity.class);
                startActivity(i);
                finish();
                overridePendingTransition(R.anim.slide_to_left, R.anim.slide_from_right);
            }
        });
        // EDIT PROFILE ON CLICK METHOD - finish
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.slide_to_left, R.anim.slide_from_right);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent i = new Intent(getApplicationContext(), MainMenu.class);
        startActivity(i);
        // FIX ANIMATION
        overridePendingTransition(R.anim.slide_to_left, R.anim.slide_from_right);
        // FIX ANIMATION

        finish();

    }
}