package com.example.proj_zesp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirstStartScreen extends AppCompatActivity {
    TextView privacy;
    EditText email, password;
    Button signin_btn, signup_btn;
    FirebaseAuth auth;
    FirebaseFirestore db;
    DatabaseReference users;
    String email_s, password_s, first_name_s, last_name_s;
    private static String TAG = "Yuriy";

    private void showRegScreen(){
        Intent SecAct = new Intent(this, SignUpScreenActivity.class);
        startActivity(SecAct);
        overridePendingTransition(R.anim.slide_to_right, R.anim.slide_from_left);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_start_screen);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        Log.d(TAG,">>Log in screen");

        Button signin_btn = (Button) findViewById(R.id.login_butto);
        Button signup_btn = (Button) findViewById(R.id.change_but);
        EditText email = (EditText) findViewById(R.id.email);
        EditText password = (EditText) findViewById(R.id.password);
        TextView privacy = (TextView) findViewById(R.id.privacy);
        password.setHintTextColor(getResources().getColor(R.color.black));
        email.setHintTextColor(getResources().getColor(R.color.black));


        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null){
            Log.d(TAG, "Logged in using: " + auth.getCurrentUser().getEmail());
            Intent i = new Intent(getApplicationContext(), MainMenu.class);
            overridePendingTransition(R.anim.slide_to_right, R.anim.slide_from_left);
            startActivity(i);
            finish();
        }

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRegScreen();
            }
        });

        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.privacy-policy-template.com/live.php?token=vOgi8mnSxUKDspf51aTzRdFBPSfkxsL7"));
                startActivity(browserIntent);
                    }
                });

        signin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getText().toString().equals("") || email.getText() == null || password.getText().toString().equals("") || password.getText()==null){
                    Log.d(TAG, "something is missing");
                    return;
                }
                auth.signInWithEmailAndPassword(email.getText().toString().trim(),password.getText().toString().trim())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Log.d(TAG,"Authorization successful");
                                Intent i = new Intent(getApplicationContext(), MainMenu.class);
                                startActivity(i);
                                overridePendingTransition(R.anim.slide_to_right, R.anim.slide_from_left);
                                finish();
                            }else{
                                Log.d(TAG,"Authorization failed. Issue - "+task.getException().getMessage());
                                Toast toast = Toast.makeText(getApplicationContext(),
                                        "E-mail or password is incorrect or \n account does not exist", Toast.LENGTH_SHORT);
                                toast.show();
                                if (auth.getCurrentUser() != null) {
                                    Log.d(TAG, "Logged in using : " + auth.getCurrentUser().getEmail());
                                }
                            }
                        }
                    }
                );
            }
        });




    }
}