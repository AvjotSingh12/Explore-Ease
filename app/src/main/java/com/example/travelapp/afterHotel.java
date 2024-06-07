package com.example.travelapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
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
import com.bumptech.glide.Glide;
import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.ListenableFuture;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class afterHotel extends AppCompatActivity {
TextView txt;
ImageView img;
String name ;
String image;
Button btn;
    LinearLayout cont;
    GeneratedTextAdapter adapterai;
    private List<String> generatedTextList;
    private ExecutorService executorService;
    private static final String API_KEY = "AIzaSyDNOkgVyl9Wzj0TKT_O8pmUB4L8mwuiy_o";
    private Padapter  adapter1;
    RecyclerView r2;
    private List<String> hotelList;
    Double lat, longg;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_hotel);
        txt = findViewById(R.id.textView);
        img = findViewById(R.id.imageView);
        cont = findViewById(R.id.container);
        executorService = Executors.newSingleThreadExecutor();

        btn = findViewById(R.id.button_directions);
        r2=findViewById(R.id.rvFood);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("name");
            image = extras.getString("photo");
            lat = extras.getDouble("lat");
            longg = extras.getDouble("long");

        }
        txt.setText(name);
        Glide.with(this).load(image).into(img);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        r2.setLayoutManager(layoutManager);
        hotelList = new ArrayList<>();
        adapter1 = new Padapter(getApplicationContext(), hotelList);
        r2.setAdapter(adapter1);
        fettch(name);
        searchForAI();


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMap(lat, longg, name);

            }
        });



    }
    private void openMap(double latitude, double longitude, String name) {
        Uri gmmIntentUri = Uri.parse("geo:" + latitude + "," + longitude + "?q=" + latitude + "," + longitude + "(" + name + ")");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(this.getPackageManager()) != null) {
            this.startActivity(mapIntent);
 }
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
                        "query=things in " + name + "&key=" + API_KEY;

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
                                        //String hotelName = hotelObject.getString("name");
                                        //Log.d("Hotel", "Name: " + hotelName); // Log hotel name


                                        // Extract photo reference if available

                                        if (hotelObject.has("photos")) {
                                            JSONArray photosArray = hotelObject.getJSONArray("photos");
                                            for (int j = 0; j < photosArray.length(); j++) {
                                                JSONObject photoObject = photosArray.getJSONObject(j);
                                                String photoReference = photoObject.getString("photo_reference");

                                                // Construct full URL for the photo

                                                // Add the photo URL to the list

                                                hotelList.add(photoReference);
                                            }
                                        }

                                        // You can extract more data such as address, rating, etc. as needed


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
    private void searchForAI() {
        GenerativeModel gm = new GenerativeModel("gemini-pro", "AIzaSyBqbeQ812_weTJi5x2yh_GdZdP7kiJLPA4");

        GenerativeModelFutures model = GenerativeModelFutures.from(gm);
      String p = "information and rating about"+name+"in short";
        Content content = new Content.Builder().addText(p).build();

        ListenableFuture<GenerateContentResponse> response = model.generateContent(content);
        response.addListener(() -> {
            try {
                GenerateContentResponse result = response.get();
                String generatedText = result.getText();
                Log.d("airesponse", generatedText);

                runOnUiThread(() -> {
                    String v=  generatedText.replace("*", "");
                    TextView textView = new TextView(this);
                    textView.setText(v);

                    // Apply styling
                    textView.setTextSize(16);
                    textView.setTextColor(ContextCompat.getColor(this, R.color.black)); // Use a color resource for consistency
                    textView.setTypeface(Typeface.DEFAULT_BOLD);
                    textView.setPadding(24, 24, 24, 24); // Add padding for better spacing

                    // Create layout parameters for the TextView

                    // Apply layout parameters to the TextView

                    // Add a background drawable to create a card-like appearance
                    textView.setBackground(ContextCompat.getDrawable(this, R.drawable.textview_background));

                    // Add the TextView to the container
                    cont.addView(textView);
                    //  generatedTextList.add(v);
                    // adapterai.notifyDataSetChanged();
                });

            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, executorService);


    }

}