package com.example.travelapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;


public class PlaceFragment extends Fragment {

RecyclerView recyclerView;
reviewAdapter adapter;
String id, placename;
FirebaseAuth auth;
    String userId ;


    public PlaceFragment(String id, String name) {
        this.id = id;
        this.placename = name;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_place, container, false);
        recyclerView = view.findViewById(R.id.aboutplace);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        List<revModel> list = new ArrayList<>();
        adapter = new reviewAdapter(getContext(), list);
        recyclerView.setAdapter(adapter);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String dest = placename.toLowerCase();


        db.collection("reviews").whereEqualTo("Destination", dest)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<revModel> reviews = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String destination = document.getString("Destination");
                            String title = document.getString("title");
                            String Userid = document.getString("userid");
                            String reviewText = document.getString("review");
                            String with = document.getString("with");
                            String date = document.getString("date");
                            String email = document.getString("email");
                            double rating; // Default value if rating is not found
                            String image = "";
                            if (document.contains("rating") && document.get("rating") != null) {
                                rating = document.getDouble("rating");
                            } else {
                                rating = 0;
                            }
                            List<String> images = new ArrayList<>();
                            if (document.contains("photoes") && document.get("photoes") != null) {
                                List<Object> imageObjects = (List<Object>) document.get("photoes");
                                assert imageObjects != null;
                                for (Object imageObj : imageObjects) {
                                    if (imageObj instanceof String) {
                                        images.add((String) imageObj);
                                    }
                                }
                            }

                            auth = FirebaseAuth.getInstance();



                            db.collection("users").whereEqualTo("userid",Userid)
                                    .get()
                                    .addOnCompleteListener(userTask -> {
                                        if (userTask.isSuccessful()) {
                                            QuerySnapshot querySnapshot = userTask.getResult();
                                            if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                                DocumentSnapshot userDocument = querySnapshot.getDocuments().get(0);
                                                String userImage = userDocument.getString("profile_image");

                                                // Construct revModel object after fetching user_image
                                                revModel reviewModel = new revModel(userImage, email, Userid, destination, title, images, reviewText, with, date, rating);
                                                reviews.add(reviewModel);

                                                populateRecyclerView(reviews);

                                            }
                                        } else {
                                            Log.e("Error getting user documents:", String.valueOf(userTask.getException()));
                                        }
                                    });
                        }
                    } else {
                        Log.d("Error getting documents: ", String.valueOf(task.getException()));
                    }
                });

        return view;
    }

    private void populateRecyclerView(List<revModel> reviews) {
        // Create a new instance of your RecyclerView adapter
        reviewAdapter adapter = new reviewAdapter(getContext(), reviews);
        // Set the adapter to your RecyclerView
        recyclerView.setAdapter(adapter);
    }




}