package com.example.travelapp;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.content.Intent;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class getInfo extends AppCompatActivity {
   EditText editName, editEmail, editPhone, editAddress, editDob, editBio;
   ImageView img;
    Uri imageUri;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Button saveButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_get_info);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        editName = findViewById(R.id.edit_name);
        editEmail = findViewById(R.id.edit_email);
        editPhone = findViewById(R.id.edit_phone);
        editAddress = findViewById(R.id.edit_address);
        editDob = findViewById(R.id.edit_dob);
        editBio = findViewById(R.id.edit_bio);
        img = findViewById(R.id.profile_image);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open gallery to select an image
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

        saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser != null) {
                    String userId = currentUser.getUid();
                    String username = currentUser.getEmail();
                    String newName = editName.getText().toString();
                    String newEmail = editEmail.getText().toString();
                    String newPhone = editPhone.getText().toString();
                    String newAddress = editAddress.getText().toString();
                    String newDob = editDob.getText().toString();
                    String newBio = editBio.getText().toString();

                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    Map<String, Object> review = new HashMap<>();
                    review.put("userid", userId);
                    review.put("email", username);
                    review.put("name", newName);
                    review.put("profile_image", imageUri.toString());
                    review.put("phone", newPhone);
                    review.put("address", newAddress);
                    review.put("dob", newDob);
                    review.put("bio", newBio);


                    db.collection("users")
                            .whereEqualTo("userid", userId)
                            .get()
                            .addOnSuccessListener(queryDocumentSnapshots -> {
                                if (!queryDocumentSnapshots.isEmpty()) {
                                    // User already exists, update the document
                                    DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                                    if(!newName.isEmpty()) {
                                        documentSnapshot.getReference().update("name", newName);
                                    }
                                    if(newName != null) {
                                        documentSnapshot.getReference().update("profile_image", imageUri.toString());
                                    }
                                    if(!newPhone.isEmpty()) {
                                        documentSnapshot.getReference().update("phone", newPhone);
                                    }
                                    if(!newAddress.isEmpty()) {
                                        documentSnapshot.getReference().update("address", newAddress);
                                    }
                                    if(!newDob.isEmpty()) {
                                        documentSnapshot.getReference().update("dob", newDob);
                                    }
                                    if(!newBio.isEmpty()) {
                                        documentSnapshot.getReference().update("bio", newBio);
                                    }
                                    Toast.makeText(getInfo.this, "Your Information updated successfully", Toast.LENGTH_LONG).show();
                                } else {
                                    // User doesn't exist, add a new document
                                    db.collection("users")
                                            .add(review)
                                            .addOnSuccessListener(documentReference -> {
                                                Log.d("DocumentSnapshot added with ID: ", documentReference.getId());
                                                Toast.makeText(getInfo.this, "Your Information saved successfully", Toast.LENGTH_LONG).show();
                                            })
                                            .addOnFailureListener(e -> Log.w("Error adding document", e));
                                }
                            })
                            .addOnFailureListener(e -> Log.w("Error querying document", e));

                }
                else{
                    Toast.makeText(getInfo.this, "please login ", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Get the image URI
             imageUri = data.getData();
            // Set the selected image to the ImageView
            img.setImageURI(imageUri);
        }
    }

    }
