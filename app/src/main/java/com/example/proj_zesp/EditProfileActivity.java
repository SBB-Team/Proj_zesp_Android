package com.example.proj_zesp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;

public class EditProfileActivity extends AppCompatActivity {

    // Defining instances - start
    private EditText password, password2, first_name, last_name;
    private TextView password_e, password2_e, fname_e, lname_e, privacy;
    private TextView birthday_picker = null;
    private TextView bday_e;
    private Button apply_but;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private long date_i = 0;
    private static String TAG = "Yuriy";
    private DocumentSnapshot document;
    private DocumentReference docRef;
    // Defining instances - finish

    // Password validation method - start
    private static boolean isValidPassword(final String password) {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }
    // Password validation method - finish

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        Log.d(TAG, ">>Edit profile screen");
        Log.d(TAG,"Current user: " + FirebaseAuth.getInstance().getCurrentUser().getEmail());

        //Defining + settings elements - start

        password = (EditText) findViewById(R.id.password);
        password.setHintTextColor(getResources().getColor(R.color.black));

        password2 = (EditText) findViewById(R.id.password2);
        password2.setHintTextColor(getResources().getColor(R.color.black));

        first_name = (EditText) findViewById(R.id.first_name1);
        first_name.setHintTextColor(getResources().getColor(R.color.black));

        last_name = (EditText) findViewById(R.id.last_name);
        last_name.setHintTextColor(getResources().getColor(R.color.black));

        TextView birthday_picker = (TextView) findViewById(R.id.bday_pick) ;
        birthday_picker.setHintTextColor(getResources().getColor(R.color.black));

        // Defining elemets #2 - start
        password_e = (TextView) findViewById(R.id.password_e);
        password2_e = (TextView) findViewById(R.id.password2_e);
        fname_e = (TextView) findViewById(R.id.fname_e);
        lname_e = (TextView) findViewById(R.id.lname_e);
        bday_e = (TextView) findViewById(R.id.bday_e);
        apply_but = (Button) findViewById(R.id.change_but);
        privacy = (TextView) findViewById(R.id.privacy);

        // Creating instances for db
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();





        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                year-=1900;
                Log.d(TAG, "Wybrana data: "+dayOfMonth+":"+month+":"+year);

                Date date = new Date(year,month,dayOfMonth);
                date_i = date.getTime();
                birthday_picker.setText(dayOfMonth+":"+month+":"+year);
            }
        };



        birthday_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int year = 2000, month = 0, day = 1;
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        EditProfileActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth,
                        listener, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });



        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.privacy-policy-template.com/live.php?token=vOgi8mnSxUKDspf51aTzRdFBPSfkxsL7"));
                startActivity(browserIntent);
            }
        });



        apply_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Apply but was clicked");


                if(first_name.getText().toString().equals("") || first_name.getText().toString() == null){
                    fname_e.setText("Enter your first name");
                    fname_e.setVisibility(View.VISIBLE);
                    password2_e.setVisibility(View.INVISIBLE);
                }
                else if(last_name.getText().toString().equals("") || last_name.getText().toString() == null){
                    lname_e.setText("Enter your last name");
                    lname_e.setVisibility(View.VISIBLE);
                    fname_e.setVisibility(View.INVISIBLE);
                }
                else if(date_i == 0 || date_i >= (new Date().getTime())){
                    lname_e.setVisibility(View.INVISIBLE);
                    bday_e.setText("Choose date of your birthday");
                    bday_e.setVisibility(View.VISIBLE);
                }else {
                    bday_e.setVisibility(View.INVISIBLE);

                    docRef = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                document = task.getResult();
                                if (document.exists()) {
                                    Log.d(TAG, "Profile data was changed");


                                    if (!password.getText().toString().equals("")){
                                        Log.d(TAG,"Password not empty");

                                        if(!isValidPassword(password.getText().toString().trim())){
                                            password_e.setText("Try better password");
                                            password_e.setVisibility(View.VISIBLE);
                                        }
                                        else if(password2.getText().toString().equals("") || password2.getText().toString() == null){
                                            password2_e.setText("Repeat your password");
                                            password2_e.setVisibility(View.VISIBLE);
                                            password_e.setVisibility(View.INVISIBLE);
                                        }
                                        else if(!password2.getText().toString().equals(password.getText().toString())) {
                                            password2_e.setText("Repeat your password properly");
                                            password2_e.setVisibility(View.VISIBLE);
                                            password_e.setVisibility(View.INVISIBLE);
                                        }else{

                                            updatePassword();
                                            updateProfileWithoutPassword();

                                            Intent i = new Intent(getApplicationContext(), ViewProfileActivity.class);
                                            startActivity(i);
                                            overridePendingTransition(R.anim.slide_to_left, R.anim.slide_from_right);
                                            finish();
                                        }
                                    }else{
                                        updateProfileWithoutPassword();

                                        Intent i = new Intent(getApplicationContext(), ViewProfileActivity.class);
                                        startActivity(i);
                                        overridePendingTransition(R.anim.slide_to_left, R.anim.slide_from_right);
                                        finish();
                                    }
                                } else {
                                    Log.d(TAG, "No such document");
                                }
                            } else {
                                Log.d(TAG, "get failed with ", task.getException());//
                            }
                        }
                    });


                }
            }
        });


        docRef = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    document = task.getResult();
                    if (document.exists()) {

                        //set text
                        first_name.setText(document.getString("first_name"));
                        last_name.setText(document.getString("last_name"));
                        password.setHint(R.string.hint_for_pass2);
                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                        String dateString = formatter.format(document.getLong("bday"));
                        birthday_picker.setText(dateString);
                        date_i = document.getLong("bday");

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());//
                }
            }
        });
    }


    private void updateProfileWithoutPassword(){

        Map<String, Object> user = new HashMap<>();
        user.put("first_name", first_name.getText().toString().trim());
        user.put("last_name", last_name.getText().toString().trim());
        user.put("bday", date_i);

        docRef.update(user);
    }

    private void updatePassword(){
        Log.d(TAG,"meth: updatePassword");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String newPassword = password2.getText().toString();

        user.updatePassword(newPassword)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User password updated.");
                        }
                    }
                });

        Map<String, Object> user1 = new HashMap<>();
        user1.put("password", password2.getText().toString().trim());

        docRef.update(user1);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent i = new Intent(getApplicationContext(), ViewProfileActivity.class);
        startActivity(i);
        // FIX ANIMATION
        overridePendingTransition(R.anim.slide_to_left, R.anim.slide_from_right);
        // FIX ANIMATION

        finish();

    }
}