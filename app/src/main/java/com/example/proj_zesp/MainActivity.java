package com.example.proj_zesp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;


import android.os.Bundle;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_screen);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);



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
    }
}