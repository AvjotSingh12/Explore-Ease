package com.example.travelapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class myreviews extends AppCompatActivity {
    RecyclerView recyclerView;
    reviewAdapter adapter;
    String userId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_myreviews);
        recyclerView = findViewById(R.id.aboutplace);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        List<revModel> list = new ArrayList<>();
        adapter = new reviewAdapter(getApplicationContext(), list);
        recyclerView.setAdapter(adapter);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            userId = currentUser.getUid();
        }
        // Inside the onCreate() method
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").whereEqualTo("userid", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                            String user_image = document.getString("profile_image");
                            // Fetch reviews only after getting the user image
                            fetchReviews(user_image);
                        }
                    } else {
                        Log.e("Error fetching user image:", String.valueOf(task.getException()));
                    }
                });
    }

// Define a method to fetch reviews
        private void fetchReviews(String userImage) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("reviews").whereEqualTo("userid", userId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            List<revModel> reviews = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String destination = document.getString("Destination");
                                String title = document.getString("title");
                                String reviewText = document.getString("review");
                                String with = document.getString("with");
                                String date = document.getString("date");
                                String id = document.getString("userid");
                                String email = document.getString("email");

                                double rating = document.getDouble("rating") != null ? document.getDouble("rating") : 0;

                                List<String> images = new ArrayList<>();
                                if (document.contains("photoes") && document.get("photoes") != null) {
                                    List<Object> imageObjects = (List<Object>) document.get("photoes");
                                    if (imageObjects != null) {
                                        for (Object imageObj : imageObjects) {
                                            if (imageObj instanceof String) {
                                                images.add((String) imageObj);
                                            }
                                        }
                                    }
                                }

                                revModel reviewModel = new revModel(userImage, email, id, destination, title, images, reviewText, with, date, rating);
                                reviews.add(reviewModel);
                            }
                            // Populate RecyclerView with reviews
                            populateRecyclerView(reviews);
                        } else {
                            Log.e("Error fetching reviews:", String.valueOf(task.getException()));
                        }
                    });
        }

// Inside populateRecyclerView() method, reuse the existing adapter instance

        private void populateRecyclerView(List<revModel> reviews) {
        // Create a new instance of your RecyclerView adapter
        reviewAdapter adapter = new reviewAdapter(getApplicationContext(), reviews);
        // Set the adapter to your RecyclerView
        recyclerView.setAdapter(adapter);
    }
}