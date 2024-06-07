package com.example.travelapp;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;
import java.util.List;



public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private PlacesClient placesClient;
    private ImageView photoImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        photoImageView = findViewById(R.id.photoImageView);

        // Initialize the map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyDNOkgVyl9Wzj0TKT_O8pmUB4L8mwuiy_o");
        }

        placesClient = Places.createClient(this);
        // Initialize the AutocompleteSupportFragment
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        if (autocompleteFragment != null) {
            // Specify the types of place data to return
            autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

            // Set up a PlaceSelectionListener to handle the selected place
            autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {

                @Override
                public void onPlaceSelected(@NonNull Place place) {
                    LatLng location = place.getLatLng();
                    Log.d("PlaceDebug", "Place: " + place.getName() + ", LatLng: " + location);

                    if (location != null) {
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
                        mMap.addMarker(new MarkerOptions().position(location).title(place.getName()));

                        // Retrieve photo metadata
                        List<Place.Field> fields = Arrays.asList(Place.Field.PHOTO_METADATAS);
                        FetchPlaceRequest request = FetchPlaceRequest.builder(place.getId(), fields).build();
                        placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
                            Place placeWithPhoto = response.getPlace();
                            List<PhotoMetadata> photoMetadataList = placeWithPhoto.getPhotoMetadatas();
                            if (photoMetadataList != null) {
                                for (PhotoMetadata photoMetadata : photoMetadataList) {
                                    // Load and display each photo
                                    loadAndDisplayPhoto(photoMetadata);
                                }
                            }
                        }).addOnFailureListener((exception) -> {
                            Log.e("PlacesAPI", "Place not found: " + exception.getMessage());
                        });
                    } else {
                        Log.d("error", "no lat long");
                    }
                }

                private void loadAndDisplayPhoto(PhotoMetadata photoMetadata) {
                    FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(photoMetadata)
                            .setMaxWidth(800) // Adjust according to your requirement
                            .setMaxHeight(700) // Adjust according to your requirement
                            .build();

                    placesClient.fetchPhoto(photoRequest).addOnSuccessListener((fetchPhotoResponse) -> {
                        Bitmap bitmap = fetchPhotoResponse.getBitmap();
                        photoImageView.setImageBitmap(bitmap);
                    }).addOnFailureListener((exception) -> {
                        Log.e("PlacesAPI", "Photo loading failed: " + exception.getMessage());
                    });
                }



                @Override
                public void onError(@NonNull Status status) {
                    // Handle errors
                }
            });
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }
    private PhotoMetadata getPhotoUrl(PhotoMetadata photoMetadata) {
        return photoMetadata;
}
}
