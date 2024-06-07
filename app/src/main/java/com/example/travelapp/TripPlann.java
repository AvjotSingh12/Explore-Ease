package com.example.travelapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class TripPlann extends AppCompatActivity {
    private EditText editTextBudget;
    private EditText editTextDuration;
    private EditText editTextDestination;
    private Button buttonSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_trip_plann);
        editTextBudget = findViewById(R.id.editTextBudget);
        editTextDuration = findViewById(R.id.editTextDuration);
        editTextDestination = findViewById(R.id.editTextDestination);
        buttonSubmit = findViewById(R.id.buttonSubmit);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String budget = editTextBudget.getText().toString();
                String duration = editTextDuration.getText().toString();
                String destination = editTextDestination.getText().toString();

                Intent intent = new Intent(TripPlann.this, predictionresult.class);
                intent.putExtra("budget", budget);
                intent.putExtra("duration", duration);
                intent.putExtra("destination", destination);
                startActivity(intent);
            }
        });

    }
}