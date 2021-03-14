package com.example.proj_zesp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;


import android.graphics.PorterDuff;
import android.os.Bundle;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        EditText email = (EditText) findViewById(R.id.email);
        email.setHintTextColor(getResources().getColor(R.color.black));
        EditText password = (EditText) findViewById(R.id.password);
        password.setHintTextColor(getResources().getColor(R.color.black));
    }
}