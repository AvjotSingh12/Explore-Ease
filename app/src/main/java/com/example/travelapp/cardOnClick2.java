package com.example.travelapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class cardOnClick2 extends AppCompatActivity {
    String name,id ;
    TextView heading, h,f,a;
    RecyclerView r,r1,r2,r3,r4;
    reviewAdapter revadapter;
    FirebaseAuth auth;
    private hotelsadapter  adapter1, adapter2, adapter3;
    private Padapter adapter;
    private static final String API_KEY = "AIzaSyDNOkgVyl9Wzj0TKT_O8pmUB4L8mwuiy_o";
    private List<Hotel> hotelList, foodList, ActivityList;
    List<String> p;
    Button btn;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_card_on_click2);
        Bundle bundle = getIntent().getExtras();
        // Assuming 'this' is an Activity or a context
        assert bundle != null;
        name = bundle.getString("name");
        id = bundle.getString("id");

        btn = findViewById(R.id.btnAddReview);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),takeReview.class);
                Bundle bundle = new Bundle();
                bundle.putString("destination", name);

                i.putExtras(bundle);

                startActivity(i);
            }
        });





         heading = findViewById(R.id.headingTextView);
         heading.setText(name);

         h = findViewById(R.id.hotelText);
         f = findViewById(R.id.foodText);
         a = findViewById(R.id.activityText);

         h.setText("Hotels in "+ name);
         f.setText("Restaurants in "+name);
         a.setText("Things to do "+ name);





        r1 = findViewById(R.id.rvHotel);
        r2=findViewById(R.id.rvFood);
        r3= findViewById(R.id.rvActivity);
        r4=findViewById(R.id.reviews);
        r = findViewById(R.id.place);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager3 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager4 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);

        r.setLayoutManager(layoutManager);
        r1.setLayoutManager(layoutManager1);
        r2.setLayoutManager(layoutManager2);
        r3.setLayoutManager(layoutManager3);
        r4.setLayoutManager(layoutManager4);

        hotelList = new ArrayList<>();
        foodList = new ArrayList<>();
        ActivityList = new ArrayList<>();
        p= new ArrayList<>();
        List<revModel> list = new ArrayList<>();


        adapter1 = new hotelsadapter(getApplicationContext(), hotelList);
        adapter2 = new hotelsadapter(getApplicationContext(), foodList);
        adapter3 = new hotelsadapter(getApplicationContext(), ActivityList);
        revadapter = new reviewAdapter(getApplicationContext(), list);
        adapter= new Padapter(getApplicationContext(),p);

        r.setAdapter(adapter);
        r1.setAdapter(adapter1);
        r2.setAdapter(adapter2);
        r3.setAdapter(adapter3);
        r4.setAdapter(revadapter);

        fetchHotelsData(name);
        fetchrestaurentData(name);
        fetchAvtivityData(name);
        fettch(name);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String dest = name.toLowerCase();



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






    }
    private void populateRecyclerView(List<revModel> reviews) {
        // Create a new instance of your RecyclerView adapter
        reviewAdapter adapter = new reviewAdapter(getApplicationContext(), reviews);
        // Set the adapter to your RecyclerView
        r4.setAdapter(adapter);
    }
    private void fettch(String placeName) {
        GeocodingRequest geocodingRequest = new GeocodingRequest(getApplicationContext());
        geocodingRequest.getLatLngFromPlaceName(placeName, new GeocodingRequest.GeocodingListener() {
            @Override
            public void onSuccess(double latitude, double longitude) {
                // Use the latitude and longitude here

                // Construct the URL for nearby search with the obtained coordinates
                String apiKey = "AIzaSyDNOkgVyl9Wzj0TKT_O8pmUB4L8mwuiy_o";
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
                                        String hotelName = hotelObject.getString("name");
                                        Log.d("Hotel", "Name: " + hotelName); // Log hotel name


                                        // Extract photo reference if available

                                        if (hotelObject.has("photos")) {
                                            JSONArray photosArray = hotelObject.getJSONArray("photos");
                                            for (int j = 0; j < photosArray.length(); j++) {
                                                JSONObject photoObject = photosArray.getJSONObject(j);
                                                String photoReference = photoObject.getString("photo_reference");



                                                // Add the photo URL to the list
                                               // hotelList.add(); // Construct full URL for the photo

                                                // Add the photo URL to the list
                                                p.add(photoReference);
                                            }
                                        }

                                        // You can extract more data such as address, rating, etc. as needed


                                    }

                                    for(Hotel h: hotelList){
                                        Log.d("hotel", h.getName());
                                    }
                                    // Notify adapter of data change
                                    adapter.notifyDataSetChanged();
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

            @Override
            public void onError(String message) {
                // Handle error
                Log.e("Geocoding Error", message);
            }
        });
    }
    private void fetchHotelsData(String placeName) {
       GeocodingRequest geocodingRequest = new GeocodingRequest(getApplicationContext());
        geocodingRequest.getLatLngFromPlaceName(placeName, new GeocodingRequest.GeocodingListener() {
            @Override
            public void onSuccess(double latitude, double longitude) {
                // Use the latitude and longitude here

                // Construct the URL for nearby search with the obtained coordinates
                String apiKey = "AIzaSyDNOkgVyl9Wzj0TKT_O8pmUB4L8mwuiy_o";
                String url = "https://maps.googleapis.com/maps/api/place/textsearch/json?" +
                        "query=hotels+near" + placeName + "&key=" + API_KEY;

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
                                        String hotelName = hotelObject.getString("name");
                                        Log.d("Hotel", "Name: " + hotelName); // Log hotel name


                                        // Extract photo reference if available
                                        String photoReference = "";
                                        if (hotelObject.has("photos")) {
                                            JSONArray photosArray = hotelObject.getJSONArray("photos");
                                            if (photosArray.length() > 0) {
                                                JSONObject photoObject = photosArray.getJSONObject(0);
                                                photoReference = photoObject.getString("photo_reference");
                                            }
                                        }

                                        // You can extract more data such as address, rating, etc. as needed
                                        Hotel hotel = new Hotel(hotelName, latitude, longitude, photoReference);
                                        hotelList.add(hotel);

                                    }


                                    // Notify adapter of data change
                                    adapter1.notifyDataSetChanged();
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

            @Override
            public void onError(String message) {
                // Handle error
                Log.e("Geocoding Error", message);
            }
        });
    }
    private void fetchrestaurentData(String placeName) {
        GeocodingRequest geocodingRequest = new GeocodingRequest(getApplicationContext());
        geocodingRequest.getLatLngFromPlaceName(placeName, new GeocodingRequest.GeocodingListener() {
            @Override
            public void onSuccess(double latitude, double longitude) {
                // Use the latitude and longitude here

                // Construct the URL for nearby search with the obtained coordinates
                String apiKey = "AIzaSyDNOkgVyl9Wzj0TKT_O8pmUB4L8mwuiy_o";
                String url = "https://maps.googleapis.com/maps/api/place/textsearch/json?" +
                        "query=eating +places +near" + placeName + "&key=" + API_KEY;

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
                                        String hotelName = hotelObject.getString("name");
                                        Log.d("Hotel", "Name: " + hotelName); // Log hotel name


                                        // Extract photo reference if available
                                        String photoReference = "";
                                        if (hotelObject.has("photos")) {
                                            JSONArray photosArray = hotelObject.getJSONArray("photos");
                                            if (photosArray.length() > 0) {
                                                JSONObject photoObject = photosArray.getJSONObject(0);
                                                photoReference = photoObject.getString("photo_reference");
                                            }
                                        }

                                        // You can extract more data such as address, rating, etc. as needed
                                        Hotel hotel = new Hotel(hotelName, latitude, longitude, photoReference);
                                        foodList.add(hotel);

                                    }

                                    for(Hotel h: hotelList){
                                        Log.d("hotel", h.getName());
                                    }
                                    // Notify adapter of data change
                                    adapter2.notifyDataSetChanged();
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

            @Override
            public void onError(String message) {
                // Handle error
                Log.e("Geocoding Error", message);
            }
        });
    }
    private void fetchAvtivityData(String placeName) {
        GeocodingRequest geocodingRequest = new GeocodingRequest(getApplicationContext());
        geocodingRequest.getLatLngFromPlaceName(placeName, new GeocodingRequest.GeocodingListener() {
            @Override
            public void onSuccess(double latitude, double longitude) {
                // Use the latitude and longitude here

                // Construct the URL for nearby search with the obtained coordinates
                String apiKey = "AIzaSyDNOkgVyl9Wzj0TKT_O8pmUB4L8mwuiy_o";
                String url = "https://maps.googleapis.com/maps/api/place/textsearch/json?" +
                        "query=activies+to+do+in" + placeName + "&key=" + API_KEY;

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
                                        String hotelName = hotelObject.getString("name");
                                        Log.d("Hotel", "Name: " + hotelName); // Log hotel name


                                        // Extract photo reference if available
                                        String photoReference = "";
                                        if (hotelObject.has("photos")) {
                                            JSONArray photosArray = hotelObject.getJSONArray("photos");
                                            if (photosArray.length() > 0) {
                                                JSONObject photoObject = photosArray.getJSONObject(0);
                                                photoReference = photoObject.getString("photo_reference");
                                            }
                                        }

                                        // You can extract more data such as address, rating, etc. as needed
                                        Hotel hotel = new Hotel(hotelName, latitude, longitude, photoReference);
                                        ActivityList.add(hotel);

                                    }

                                    for(Hotel h: hotelList){
                                        Log.d("hotel", h.getName());
                                    }
                                    // Notify adapter of data change
                                    adapter3.notifyDataSetChanged();
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

            @Override
            public void onError(String message) {
                // Handle error
                Log.e("Geocoding Error", message);
            }
        });
    }
}