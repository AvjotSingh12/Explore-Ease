package com.example.travelapp;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.reflect.TypeToken;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class predictionresult extends AppCompatActivity {


    private static final String API_KEY = "AIzaSyDNOkgVyl9Wzj0TKT_O8pmUB4L8mwuiy_o";

    private ExecutorService executorService;

    String budget, duration, destination;
    LinearLayout cont;
    ImageView img;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_predictionresult);


        budget = getIntent().getStringExtra("budget");
        duration = getIntent().getStringExtra("duration");
        destination = getIntent().getStringExtra("destination");
        img = findViewById(R.id.imageview);
        fethplace(destination);
       // recyclerView = findViewById(R.id.conversationRecyclerView);
      //  recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, recyclerV
      //  adapterai = new GeneratedTextAdapter(generatedTextList);
     //   recyclerView.setAdapter(adapterai);
        executorService = Executors.newSingleThreadExecutor();
        cont = findViewById(R.id.container);
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://10.0.2.2:8080/recommend?budget=" + budget + "&duration=" + duration + "&destination=" + destination)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Network error", Toast.LENGTH_SHORT).show());
                Log.e("NetworkError", "Network call failed", e);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String jsonResponse = response.body().string();
                Log.d("Response", jsonResponse);

                try {
                    Gson gson = new Gson();
                    JsonObject jsonObject = gson.fromJson(jsonResponse, JsonObject.class);
                    if (jsonObject.has("message")) {
                        String message = jsonObject.get("message").getAsString();
                        JsonArray jsonArray = JsonParser.parseString(message).getAsJsonArray();
                        Type packageListType = new TypeToken<List<Package>>() {
                        }.getType();
                        List<Package> tripPackageList = gson.fromJson(jsonArray, packageListType);

                        runOnUiThread(() -> {
                            if (tripPackageList != null) {
                                Package bestPackage = findBestPackage(tripPackageList);
                                if (bestPackage != null) {
                                    displayTripPlan(bestPackage);
                                    searchForAI(bestPackage);
                                } else {
                                    Log.e("EvaluationError", "No best package found");
                                }
                            } else {
                                Log.e("ParsingError", "Parsed tripPackageList is null");
                            }
                        });
                    }
                } catch (Exception e) {
                    Log.e("ParsingError", "Error parsing JSON response", e);
                    runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Error parsing data", Toast.LENGTH_SHORT).show());
                }
            }
        });

    }

    private void displayTripPlan(Package tripPackage) {
        // Create a parent layout for the entire trip plan
        TextView packageNameTextView = findViewById(R.id.packageNameTextView);
        TextView itineraryTextView = findViewById(R.id.itineraryTextView);
        TextView placesCoveredTextView = findViewById(R.id.placesCoveredTextView);
        TextView sightseeingPlacesTextView = findViewById(R.id.sightseeingPlacesTextView);
        TextView startCityTextView = findViewById(R.id.startCityTextView);
        TextView hotelDetailsTextView = findViewById(R.id.hotelDetailsTextView);
        TextView airlineTextView = findViewById(R.id.airlineTextView);

        // Set values to TextViews
        packageNameTextView.setText(tripPackage.getPackageName());
        itineraryTextView.setText("Itinerary: " + tripPackage.getItinerary());
        placesCoveredTextView.setText("Places Covered: " + tripPackage.getPlacesCovered());
        sightseeingPlacesTextView.setText("Sightseeing Places: " + tripPackage.getSightseeingPlacesCovered());
        startCityTextView.setText("Start City: " + tripPackage.getStartCity());
        hotelDetailsTextView.setText("Hotel Details: " + (tripPackage.getHotelDetails() != null ? tripPackage.getHotelDetails() : "Not available"));
        airlineTextView.setText("Price per person: " + (tripPackage.getPerpersonprice() != null ? tripPackage.getPerpersonprice() : "Not available"));


    }


    private void searchForAI(Package tripPackage) {
        GenerativeModel gm = new GenerativeModel("gemini-pro", "AIzaSyBqbeQ812_weTJi5x2yh_GdZdP7kiJLPA4");

        GenerativeModelFutures model = GenerativeModelFutures.from(gm);
          String g = "give a trip for"+duration+" days to"+destination;
         String p = "explain this trip for  "+duration+"days "+ tripPackage.getItinerary()+tripPackage.getSightseeingPlacesCovered()+tripPackage.getHotelDetails()+tripPackage.getAirline();
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
                   // textView.setTypeface(Typeface.DEFAULT_BOLD);
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


    private Package findBestPackage(List<Package> tripPackageList) {
        if (tripPackageList == null || tripPackageList.isEmpty()) {
            return null;
        }

        Package bestPackage = tripPackageList.get(0);
        double highestRating = bestPackage.getHotelRating();

        for (Package tripPackage : tripPackageList) {
            double currentRating = tripPackage.getHotelRating();
            if (currentRating > highestRating) {
                bestPackage = tripPackage;
                highestRating = currentRating;
            }
        }

        return bestPackage;
    }



    public void fethplace(String placeName) {
        String url = "https://maps.googleapis.com/maps/api/place/textsearch/json?" +
                "query=" + placeName + "&key=" + API_KEY;

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, null,
                response -> {
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
                                    return;
                                }
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> Log.e("Volley Error", "Error: " + error.getMessage()));

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

