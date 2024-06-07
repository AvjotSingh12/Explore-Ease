package com.example.travelapp;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class GeocodingRequest {

    private static final String TAG = "GeocodingRequest";

    public interface GeocodingListener {
        void onSuccess(double latitude, double longitude);
        void onError(String message);
    }

    private final Context context;
    private final RequestQueue requestQueue;

    public GeocodingRequest(Context context) {
        this.context = context;
        requestQueue = Volley.newRequestQueue(context);
    }

    public void getLatLngFromPlaceName(String placeName, final GeocodingListener listener) {
        String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + placeName + "&key=AIzaSyDNOkgVyl9Wzj0TKT_O8pmUB4L8mwuiy_o";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray results = response.getJSONArray("results");
                            if (results.length() > 0) {
                                JSONObject result = results.getJSONObject(0);
                                JSONObject location = result.getJSONObject("geometry").getJSONObject("location");
                                double latitude = location.getDouble("lat");
                                double longitude = location.getDouble("lng");
                                listener.onSuccess(latitude, longitude);
                            } else {
                                listener.onError("No results found");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onError("Error parsing response");
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        listener.onError("Error getting response");
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }
}
