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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpScreenActivity extends AppCompatActivity {

    EditText email, password, password2, first_name, last_name;
    TextView email_e, password_e, password2_e, fname_e, lname_e,privacy;
    FirebaseAuth auth;
    FirebaseFirestore db;
    DatabaseReference users;

    private static String TAG = "Yuriy";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_screen);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        //
        TextView email_e = (TextView) findViewById(R.id.email_e);
        TextView password_e = (TextView) findViewById(R.id.password_e);
        TextView password2_e = (TextView) findViewById(R.id.password2_e);
        TextView fname_e = (TextView) findViewById(R.id.fname_e);
        TextView lname_e = (TextView) findViewById(R.id.lname_e);

        TextView privacy = (TextView) findViewById(R.id.privacy);



        db = FirebaseFirestore.getInstance();




        EditText email = (EditText) findViewById(R.id.email);
        email.setHintTextColor(getResources().getColor(R.color.black));

        EditText password = (EditText) findViewById(R.id.password);
        password.setHintTextColor(getResources().getColor(R.color.black));

        EditText password2 = (EditText) findViewById(R.id.password2);
        password2.setHintTextColor(getResources().getColor(R.color.black));

        EditText first_name = (EditText) findViewById(R.id.first_name);
        first_name.setHintTextColor(getResources().getColor(R.color.black));

        EditText last_name = (EditText) findViewById(R.id.last_name);
        last_name.setHintTextColor(getResources().getColor(R.color.black));


        Button signup_btn = (Button) findViewById(R.id.register_bu);


        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.privacy-policy-template.com/live.php?token=vOgi8mnSxUKDspf51aTzRdFBPSfkxsL7"));
                startActivity(browserIntent);
            }
        });


        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (email.getText().toString().equals("") || email.getText().toString() == null){
                    email_e.setText("Enter your email");
                    email_e.setVisibility(View.VISIBLE);
                }
                else if(password.getText().toString().equals("") || password.getText().toString() == null){
                    password_e.setText("Enter your password");
                    password_e.setVisibility(View.VISIBLE);
                    email_e.setVisibility(View.INVISIBLE);
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
                else {
                    lname_e.setVisibility(View.INVISIBLE);

                    auth = FirebaseAuth.getInstance();
                    Log.d(TAG,"Rejestracja zaczyna się");
                    auth.createUserWithEmailAndPassword(email.getText().toString().trim(),password.getText().toString().trim())
//                            .addOnCompleteListener(SignUpScreenActivity.this, new OnCompleteListener<AuthResult>() {
//                                @Override
//                                public void onComplete(@NonNull Task<AuthResult> task) {
//                                    if (task.isSuccessful()){
//                                        Log.d(TAG,"Udało się zarejestrować");
//
//
//
//                                    }
//                                    if (!task.isSuccessful()) {
//                                        Log.d(TAG, "Nie udało się zarejestrować, bo: " + task.getException().getMessage());
//                                        Log.d(TAG, "Email: " + email.getText().toString().trim());
//                                        Log.d(TAG, "Password: " + password.getText().toString().trim());
//                                        return;
//                                    }else if (task.isCanceled()){
//                                        Log.d(TAG, "Task is canceled");
//                                        return;
//                                    }else{
//
//                                    }
//                                    Map<String, Object> user = new HashMap<>();
//                                    user.put("email", email.getText().toString().trim());
//                                    user.put("password", password.getText().toString().trim());
//                                    user.put("first_name", first_name.getText().toString().trim());
//                                    user.put("last_name", last_name.getText().toString().trim());
//
//                                    Log.d(TAG, "Dodawanie danych użytkownika");
//
//                                    db.collection("users").document("email.getText().toString().trim()")
//                                            .set(user)
//                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                @Override
//                                                public void onSuccess(Void aVoid) {
//                                                    Log.d(TAG, "Dane użytkownika zostały dodane!");
//                                                }
//                                            })
//                                            .addOnFailureListener(new OnFailureListener() {
//                                                @Override
//                                                public void onFailure(@NonNull Exception e) {
//                                                    Log.w(TAG, "Pomyłka przy dodaniu danych użytkownika", e);
//                                                }
//                                            });
//                                }
//                            });
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Log.d(TAG,"Dodawanie danych użytkownika1");
                            Map<String, Object> user = new HashMap<>();
                                    user.put("email", email.getText().toString().trim());
                                    user.put("password", password.getText().toString().trim());
                                    user.put("first_name", first_name.getText().toString().trim());
                                    user.put("last_name", last_name.getText().toString().trim());

                                    Log.d(TAG, "Dodawanie danych użytkownika2");

                                    db.collection("users").document("email.getText().toString().trim()")
                                            .set(user)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d(TAG, "Dane użytkownika zostały dodane!");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w(TAG, "Pomyłka przy dodaniu danych użytkownika", e);
                                                }
                                            });
                        }

                    });

                    auth.signOut();
                    Intent i = new Intent(getApplicationContext(), FirstStartScreen.class);
                    startActivity(i);

                    finish();

                }
            }
        });
    }
}