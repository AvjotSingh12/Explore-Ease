package com.example.travelapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class profilePage extends AppCompatActivity {
Button edtprofile;
    private TextView profileName;
    private TextView profileEmail;
    private TextView profilePhone;
    private TextView profileAddress;
    private TextView profileDob;
    private TextView profileBioLabel;
    private TextView profileBio;
    private ImageView img;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_page);

        profileName = findViewById(R.id.profile_name);
        profileEmail = findViewById(R.id.profile_email);
        profilePhone = findViewById(R.id.profile_phone);
        profileAddress = findViewById(R.id.profile_address);
        profileDob = findViewById(R.id.profile_dob);
        profileBioLabel = findViewById(R.id.profile_bio_label);
        profileBio = findViewById(R.id.profile_bio);
        edtprofile =  findViewById(R.id.edit_profile_button);
        img = findViewById(R.id.profile_image);



        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
           String userid = user.getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users").whereEqualTo("userid",userid)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot != null && !querySnapshot.isEmpty()) {
                               DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                                // Populate TextViews with user data
                                profileName.setText(document.getString("name"));
                                profileEmail.setText(document.getString("email"));
                                profilePhone.setText(document.getString("phone number"));
                                profileAddress.setText(document.getString("address"));
                                profileDob.setText(document.getString("dob"));
                                profileBio.setText(document.getString("bio"));

                                String imageUriString = document.getString("profile_image");
                                if (imageUriString != null) {
                                    Glide.with(this).load(imageUriString).into(img);

                                } else {
                                    // If image URI is null, set a default image or hide the ImageView
                                    // For example:
                                    // img.setImageResource(R.drawable.default_profile_image);
                                    img.setVisibility(View.GONE); // Hide the ImageView
                                }
                            } else {
                                Log.d("profilePage", "No such document");
                            }
                        } else {
                            Log.d("profilePage", "Error getting documents: ", task.getException());
                        }
                    });


        }



        edtprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(profilePage.this, getInfo.class));
            }
        });


    }
}