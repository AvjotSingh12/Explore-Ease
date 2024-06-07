package com.example.travelapp;

import android.app.DatePickerDialog;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public  class takeReview extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1 ;
    String placeName;
    ImageView img;
    TextView txt;
    Button submit, photos;
    String userId;
    RatingBar rt;
    EditText date, review, titlerev, with;
   // List<PHOTO> photoes = new ArrayList<>();
    List<String> selectedImages = new ArrayList<>();
   // private static final int PICK_IMAGES_REQUEST = 1;

    private static final String API_KEY = "AIzaSyDNOkgVyl9Wzj0TKT_O8pmUB4L8mwuiy_o";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_review);


        img = findViewById(R.id.placephoto);

        submit = findViewById(R.id.btn_submit_review);
        photos = findViewById(R.id.btn_add_photos);


        rt = findViewById(R.id.ratingBar);
        date = findViewById(R.id.datePicker);
        review = findViewById(R.id.editText_review);
        titlerev = findViewById(R.id.editText_review_title);
        with = findViewById(R.id.withwhome);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();


        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });





        Bundle extras = getIntent().getExtras();
        assert extras != null;
        placeName = extras.getString("destination", "default");
       String dest =  placeName.toLowerCase();

        Log.d("place name: ", placeName);

        fethplace(placeName);
        photos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (currentUser != null) {
                    userId = currentUser.getUid();
                    String username = currentUser.getEmail();


                    double rating = (double) rt.getRating();
                    String rev = review.getText().toString();
                    String title = titlerev.getText().toString();
                    String withwhome = with.getText().toString();
                    String selectedDate = date.getText().toString();


                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    Map<String, Object> review = new HashMap<>();


                    review.put("userid", userId);
                    review.put("email", username);
                    review.put("Destination", dest);
                    review.put("rating", rating);
                    review.put("title", title);
                    review.put("review", rev);
                    review.put("with", withwhome);
                    review.put("date", selectedDate);
                    review.put("photoes", selectedImages);

                    db.collection("reviews")
                            .add(review).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d("DocumentSnapshot added with ID: ", documentReference.getId());
                                    Toast.makeText(takeReview.this, " Review added successfully", Toast.LENGTH_LONG).show();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("Error adding document", e);
                                }

                            });
                } else {
                    Toast.makeText(getApplicationContext(), "Please Log in to post a review", Toast.LENGTH_LONG).show();

                }

            }
        });



    }


    // Add this method to handle image selection from the gallery


    // Handle the result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            if (data != null) {
                // Check if multiple images are selected
                if (data.getClipData() != null) {
                    int count = data.getClipData().getItemCount();
                    for (int i = 0; i < count; i++) {
                        String imageUri = data.getClipData().getItemAt(i).getUri().toString();
                        selectedImages.add(imageUri);
                    }
                } else {
                    // Single image selected
                    String imageUri = data.getData().toString();
                    selectedImages.add(imageUri);
                }

                // Handle the selected images (e.g., display in ImageView, upload to server)

            }
        }
    }



    // Method to upload image to Firebase Storage

 


    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        date.setText(selectedDate);
                    }
                }, year, month, dayOfMonth);

        datePickerDialog.show();
    }

    public void fethplace(String placeName) {
        // Construct the URL for nearby search with the obtained coordinates
        String url = "https://maps.googleapis.com/maps/api/place/textsearch/json?" +
                "query=" + placeName + "&key=" + API_KEY;

        // Initialize Volley request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        // Make a GET request using Volley
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray results = response.getJSONArray("results");
                            for (int i = 0; i < results.length(); i++) {
                                JSONObject hotelObject = results.getJSONObject(i);

                                if (hotelObject.has("photos")) {
                                    JSONArray photosArray = hotelObject.getJSONArray("photos");
                                    if (photosArray.length() > 0) {
                                        JSONObject photoObject = photosArray.getJSONObject(0);
                                        String photoReference = photoObject.getString("photo_reference");
                                        loadImage(photoReference);
                                        return; // Load only the first photo found
                                    }
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley Error", "Error: " + error.getMessage());
            }
        });

        // Add the request to the RequestQueue
        requestQueue.add(jsonObjectRequest);
    }

    private void loadImage(String photoReference) {
        if (photoReference != null && !photoReference.isEmpty()) {
            // Load image with Glide
            String photoUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" +
                    photoReference + "&key=" + API_KEY;

            // Load image with Glide
            Glide.with(getApplicationContext()).load(photoUrl).into(img);
        }
    }

}