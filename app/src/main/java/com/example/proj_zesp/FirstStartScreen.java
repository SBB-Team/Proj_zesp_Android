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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

    private static String TAG = "Yuriy";

    private void showRegScreen(){
        Intent SecAct = new Intent(this, SignUpScreenActivity.class);
        startActivity(SecAct);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_start_screen);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        Log.d(TAG,"Start programu");

        Button signin_btn = (Button) findViewById(R.id.login_butto);
        Button signup_btn = (Button) findViewById(R.id.register_bu);

        EditText email = (EditText) findViewById(R.id.email);
        EditText password = (EditText) findViewById(R.id.password);

        password.setHintTextColor(getResources().getColor(R.color.black));
        email.setHintTextColor(getResources().getColor(R.color.black));



        auth = FirebaseAuth.getInstance();  // autoryzacja w bazie danych
        //db = FirebaseDatabase.getInstance(); // podłączenia do bazy danych
        //users = db.getReference("Users");


        if (auth.getCurrentUser() != null){
            Log.d(TAG, "Zalogowany email: " + auth.getCurrentUser().getEmail());
            Intent i = new Intent(getApplicationContext(), MainMenu.class);
            startActivity(i);
            finish();
        }





        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRegScreen();
                finish();
            }
        });



        signin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getText().toString().equals("") || email.getText() == null || password.getText().toString().equals("") || password.getText()==null){
                    Log.d(TAG, "Coś jest puste");
                    return;
                }
                auth.signInWithEmailAndPassword(email.getText().toString().trim(),password.getText().toString().trim())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Log.d(TAG,"Udało się zalogować");
                                Intent i = new Intent(getApplicationContext(), MainMenu.class);
                                startActivity(i);
                                finish();
                            }else{
                                Log.d(TAG,"Nie udało się zalogować, bo: "+task.getException().getMessage());

                                if (auth.getCurrentUser() != null) {
                                    Log.d(TAG, "Zalogowany Użytkownik: " + auth.getCurrentUser().getEmail());
                                }
                            }

                        }
                    }
                );
            }
        });

        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            email_s = extras.getString("email");
            password_s = extras.getString("password");
            first_name_s = extras.getString("first_name");
            last_name_s = extras.getString("last_name");
            Log.d(TAG, "Są extrasy");


            Log.d(TAG,"Rejestracja zaczyna się");
            auth.createUserWithEmailAndPassword(email_s.trim(),password_s.trim())
                    .addOnCompleteListener(FirstStartScreen.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Log.d(TAG,"Udało się zarejestrować");
                            }
                            if (!task.isSuccessful()) {
                                Log.d(TAG, "Nie udało się zarejestrować, bo: " + task.getException().getMessage());
                                Log.d(TAG, "Email: " + email_s);
                                Log.d(TAG, "Password: " + password_s);
                            }
                            if (task.isCanceled()){
                                Log.d(TAG, "Task is canceled");
                            }
                        }
                    });

            auth.signOut();
        }

    }
}