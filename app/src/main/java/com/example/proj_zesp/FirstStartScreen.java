package com.example.proj_zesp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirstStartScreen extends AppCompatActivity {

    EditText email, password;
    Button signin_btn, signup_btn;
    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference users;
    String email_s, password_s, first_name_s, last_name_s;


    private void Init(){
        EditText email = (EditText) findViewById(R.id.email);
        EditText password = (EditText) findViewById(R.id.password);


        password.setHintTextColor(getResources().getColor(R.color.black));
        email.setHintTextColor(getResources().getColor(R.color.black));


    }

    private void showRegScreen(){
        Intent SecAct = new Intent(this, SignUpScreenActivity.class);
        startActivity(SecAct);
        finish();
//        Bundle extras = getIntent().getExtras();
//        if(extras !=null) {
//            email_s = extras.getString("email");
//            password_s = extras.getString("password");
//            first_name_s = extras.getString("first_name");
//            last_name_s = extras.getString("last_name");
//            Toast toast = Toast.makeText(this, "email: "+ email_s,Toast.LENGTH_LONG);
//            toast.show();
//        }
        //Log.i("TAG",email_s);
//        Toast toast = Toast.makeText(this, "email: "+ email_s,Toast.LENGTH_LONG);
//        toast.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_start_screen);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        Init();

        Button signin_btn = (Button) findViewById(R.id.login_butto);
        Button signup_btn = (Button) findViewById(R.id.register_bu);

        auth = FirebaseAuth.getInstance();  // autoryzacja w bazie danych
        db = FirebaseDatabase.getInstance(); // podłączenia do bazy danych
        users = db.getReference("Users");

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRegScreen();
            }
        });

        signin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast toast = Toast.makeText(getApplicationContext(), "Logowanie...",Toast.LENGTH_LONG);
                        toast.show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast toast = Toast.makeText(getApplicationContext(), "Logowanie nie powiodło się",Toast.LENGTH_LONG);
                        toast.show();
                    }
                });
            }
        });

        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            email_s = extras.getString("email");
            password_s = extras.getString("password");
            first_name_s = extras.getString("first_name");
            last_name_s = extras.getString("last_name");
//            Toast toast = Toast.makeText(this, "email: "+ email_s,Toast.LENGTH_LONG);
//            toast.show();

            auth.createUserWithEmailAndPassword(email_s, password_s).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    User user = new User();
                    user.setEmail(email_s);
                    user.setPassword(password_s);
                    user.setFirst_name(first_name_s);
                    user.setLast_name(last_name_s);

                    users.child(user.getEmail())
                            .setValue(user)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast toast = Toast.makeText(getApplicationContext(), "Użytkownik został zarejestrowany",Toast.LENGTH_LONG);
                                    toast.show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast toast = Toast.makeText(getApplicationContext(), "Rejestracja nie powiodła się",Toast.LENGTH_LONG);
                            toast.show();
                        }
                    });
                }
            });
        }

        //signup_btn.setText(email_s);







    }
}