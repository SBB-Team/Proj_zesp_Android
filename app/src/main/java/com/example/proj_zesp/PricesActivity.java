package com.example.proj_zesp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;

public class PricesActivity extends AppCompatActivity {

    private static String TAG = "Yuriy";
    private TextView paliwo1_cena,paliwo2_cena,paliwo3_cena,paliwo4_cena;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private DocumentSnapshot document;
    private ProgressDialog dialog;

    private void getDataAndSetTexts(String current_user){
        db = FirebaseFirestore.getInstance();
        Log.d(TAG, "Getting data");
        DocumentReference docRef = db.collection("petrol_type").document("Prices");

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        setTexts();
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

        //Text setters - start
        paliwo1_cena.setText(document.getString("LPG"));
        paliwo2_cena.setText(document.getString("ON"));
        paliwo3_cena.setText(document.getString("PB95"));
        paliwo4_cena.setText(document.getString("PB98"));
        // Text setters - finish
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prices);

        paliwo1_cena = (TextView) findViewById(R.id.paliwo1_cena);
        paliwo2_cena = (TextView) findViewById(R.id.paliwo2_cena);
        paliwo3_cena = (TextView) findViewById(R.id.paliwo3_cena);
        paliwo4_cena = (TextView) findViewById(R.id.paliwo4_cena);

        getDataAndSetTexts(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        // Loading dialog - start
        dialog = new ProgressDialog(PricesActivity.this);
        dialog.setMessage("Loading prices...");
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