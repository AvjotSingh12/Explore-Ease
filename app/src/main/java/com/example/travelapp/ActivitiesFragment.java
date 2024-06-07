package com.example.travelapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ActivitiesFragment extends Fragment {
    private RecyclerView recyclerView;
    private hotelsadapter adapter;
    private static final String API_KEY = "AIzaSyDNOkgVyl9Wzj0TKT_O8pmUB4L8mwuiy_o";

    private List<Hotel> hotelList;
    private String placeName;
    private TextView tx;



    public ActivitiesFragment() {
        // Required empty public constructor
    }
    public ActivitiesFragment(String placeName) {
        this.placeName = placeName;
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            placeName = getArguments().getString("placeName");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      //  return inflater.inflate(R.layout.fragment_activities, container, false);
        View view = inflater.inflate(R.layout.fragment_hotels, container, false);
        // tx = view.findViewById(R.id.txtv);
        // tx.setText(placeName);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.rvHotels);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize the list to hold hotel data
        hotelList = new ArrayList<>();




        // Initialize the adapter with an empty list
        adapter = new hotelsadapter(getContext(), hotelList);
        recyclerView.setAdapter(adapter);

        // Fetch hotels data
        fetchAvtivityData(placeName);

        return view;
    }
    private void fetchAvtivityData(String placeName) {
        GeocodingRequest geocodingRequest = new GeocodingRequest(getContext());
        geocodingRequest.getLatLngFromPlaceName(placeName, new GeocodingRequest.GeocodingListener() {
            @Override
            public void onSuccess(double latitude, double longitude) {
                // Use the latitude and longitude here

                // Construct the URL for nearby search with the obtained coordinates
                String apiKey = "AIzaSyDNOkgVyl9Wzj0TKT_O8pmUB4L8mwuiy_o";
                String url = "https://maps.googleapis.com/maps/api/place/textsearch/json?" +
                        "query=activies+to+do+in" + placeName + "&key=" + API_KEY;

                // Initialize Volley request queue
                RequestQueue requestQueue = Volley.newRequestQueue(requireContext());

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





}
