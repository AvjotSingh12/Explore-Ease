package com.example.travelapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class ReviewActivity extends AppCompatActivity {
    EditText edt;
    Button btn ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        edt = findViewById(R.id.reviewlocation);


        btn = findViewById(R.id.searchButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String destination = edt.getText().toString();


                Intent i = new Intent(getApplicationContext(),takeReview.class);
                Bundle bundle = new Bundle();
                bundle.putString("destination", destination);

                i.putExtras(bundle);


                startActivity(i);

            }
        });



    }
}