package com.example.proj_zesp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SignUpScreenActivity extends AppCompatActivity {

    EditText email, password, password2, first_name, last_name;
    TextView email_e, password_e, password2_e, fname_e, lname_e;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_screen);
        //
        TextView email_e = (TextView) findViewById(R.id.email_e);
        TextView password_e = (TextView) findViewById(R.id.password_e);
        TextView password2_e = (TextView) findViewById(R.id.password2_e);
        TextView fname_e = (TextView) findViewById(R.id.fname_e);
        TextView lname_e = (TextView) findViewById(R.id.lname_e);


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

                    Intent i = new Intent(getApplicationContext(), FirstStartScreen.class);
                    i.putExtra("email", email.getText().toString());
                    i.putExtra("password", email.getText().toString());
                    i.putExtra("first_name", email.getText().toString());
                    i.putExtra("last_name", email.getText().toString());

                    startActivity(i);

                    finish();
                    //onBackPressed();
                }
            }
        });

        //last_name.setPaintFlags(last_name.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


    }
}