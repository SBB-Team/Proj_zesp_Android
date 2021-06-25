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
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpScreenActivity extends AppCompatActivity {

    // Defining instances - start
    private EditText email = null, password, password2, first_name, last_name;
    private TextView email_e, password_e, password2_e, fname_e, lname_e, privacy;
    private TextView birthday_picker = null;
    private Button signup_btn;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private long date_i = 0;
    private static String TAG = "Yuriy";
    // Defining instances - finish

    // Password validation method - start
    public static boolean isValidPassword(final String password) {
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
        setContentView(R.layout.sign_up_screen);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        Log.d(TAG, ">>Sign up screen");

        //Defining + settings elements - start
        EditText email = (EditText) findViewById(R.id.email);
        email.setHintTextColor(getResources().getColor(R.color.black));

        EditText password = (EditText) findViewById(R.id.password);
        password.setHintTextColor(getResources().getColor(R.color.black));

        EditText password2 = (EditText) findViewById(R.id.password2);
        password2.setHintTextColor(getResources().getColor(R.color.black));

        EditText first_name = (EditText) findViewById(R.id.first_name1);
        first_name.setHintTextColor(getResources().getColor(R.color.black));

        EditText last_name = (EditText) findViewById(R.id.last_name);
        last_name.setHintTextColor(getResources().getColor(R.color.black));

        TextView birthday_picker = (TextView) findViewById(R.id.bday_pick) ;
        birthday_picker.setHintTextColor(getResources().getColor(R.color.black));

        // Defining elemets #2 - start
        TextView email_e = (TextView) findViewById(R.id.email_e);
        TextView password_e = (TextView) findViewById(R.id.password_e);
        TextView password2_e = (TextView) findViewById(R.id.password2_e);
        TextView fname_e = (TextView) findViewById(R.id.fname_e);
        TextView lname_e = (TextView) findViewById(R.id.lname_e);
        TextView bday_e = (TextView) findViewById(R.id.bday_e);
        signup_btn = (Button) findViewById(R.id.change_but);
        TextView privacy = (TextView) findViewById(R.id.privacy);

        // Creating instances for db
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        // Defining elemets #2 - finish

        // Date picker - start
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
        // Date picker - finish

        // Date of virthday picker - start
        birthday_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int year = 2000, month = 0, day = 1;
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        SignUpScreenActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth,
                        listener, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });
        // Date of virthday picker - finish

        // Move to privacy and policy - start
        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.privacy-policy-template.com/live.php?token=vOgi8mnSxUKDspf51aTzRdFBPSfkxsL7"));
                startActivity(browserIntent);
            }
        });
        // Move to privacy and policy -  finish

        // Signing up - start
        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Registration fields validation - start
                if (email.getText().toString().equals("") || email.getText().toString() == null) {
                    email_e.setText("Email field is empty");
                    email_e.setVisibility(View.VISIBLE);
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches())
                {
                    email_e.setText("Email is invalid");
                    email_e.setVisibility(View.VISIBLE);
                }
                else if(password.getText().toString().equals("") || password.getText().toString() == null){
                    password_e.setText("Enter your password");
                    password_e.setVisibility(View.VISIBLE);
                    email_e.setVisibility(View.INVISIBLE);
                }
                else if(!isValidPassword(password.getText().toString().trim())){
                    password_e.setText("Try better password");
                    password_e.setVisibility(View.VISIBLE);
                }
                else if(password2.getText().toString().equals("") || password2.getText().toString() == null){
                    password2_e.setText("Repeat your password");
                    password2_e.setVisibility(View.VISIBLE);
                    password_e.setVisibility(View.INVISIBLE);
                }
                else if(!password2.getText().toString().equals(password.getText().toString())){
                    password2_e.setText("Repeat your password properly");
                    password2_e.setVisibility(View.VISIBLE);
                    password_e.setVisibility(View.INVISIBLE);
                }
                else if(first_name.getText().toString().equals("") || first_name.getText().toString() == null){
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
                }
                // Registration fields validation - finish

                // Registration - start
                else {
                    bday_e.setVisibility(View.INVISIBLE);


                    Log.d(TAG,"Sign up has begun");
                    auth.createUserWithEmailAndPassword(email.getText().toString().trim(),password.getText().toString().trim())
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                  @Override
                                  public void onSuccess(AuthResult authResult) {
                                      Map<String, Object> user = new HashMap<>();
                                      user.put("email", email.getText().toString().trim().toLowerCase());
                                      user.put("password", password.getText().toString().trim());
                                      user.put("first_name", first_name.getText().toString().trim());
                                      user.put("last_name", last_name.getText().toString().trim());
                                      user.put("points", 0);
                                      user.put("bday", date_i);
                                      Log.d(TAG, "Adding users data");

                                      db.collection("users").document(email.getText().toString().trim().toLowerCase())
                                              .set(user)
                                              .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                  @Override
                                                  public void onSuccess(Void aVoid) {
                                                      Log.d(TAG, "Data has been added");
                                                  }
                                              })
                                              .addOnFailureListener(new OnFailureListener() {
                                                  @Override
                                                  public void onFailure(@NonNull Exception e) {
                                                      Log.w(TAG, "Error during user`s data adding", e);
                                                  }
                                              }).addOnCanceledListener(new OnCanceledListener() {
                                                                           @Override
                                                                           public void onCanceled() {
                                                                               Log.d(TAG, "Canceling...");
                                                                           }
                                                                       }
                                      );
                                  }
                              }
                            );
                    // Registration - finish

                    // Logging out and Moving to FirstStartScreen activity
                    auth.signOut();
                    Intent i = new Intent(getApplicationContext(), FirstStartScreen.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_to_left, R.anim.slide_from_right);
                    finish();
                }
            }
        });
    }
    // Signing up - finish

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.slide_to_left, R.anim.slide_from_right);
    }
}