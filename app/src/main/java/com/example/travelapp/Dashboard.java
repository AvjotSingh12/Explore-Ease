package com.example.travelapp;
import static androidx.core.content.ContextCompat.startActivity;

import android.Manifest;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.view.MenuItem;

import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;

import com.google.android.material.navigation.NavigationView;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import java.util.concurrent.ExecutionException;


public class Dashboard extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1 ;
    private RecyclerView recyclerView;
    private DrawerLayout drawerLayout;
    private EditText searchplace;
    private FirebaseAuth mAuth;
    private ImageView sidebar;
    private List<PlaceModel> placeList;
    private PlaceAdapter adapter;
    private static final String API_KEY = "AIzaSyDNOkgVyl9Wzj0TKT_O8pmUB4L8mwuiy_o";

    private EditText prompt;
    private List<String> generatedTextList;
    private ImageView img;
    private RecyclerView airecyclerview;
    private GeneratedTextAdapter adapterai;
    private TextView headertext, predict;
    String selectedPlaceId ;
    String selectedPlaceName;
    String username;
    private ImageView Profile_image;

    private ExecutorService executorService;
    private PlacesClient placesClient;
    CardView amritsar, taj, b,c,d,e,f;
    Button ai,p;



    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboardleisha);

predict = findViewById(R.id.textView);
p = findViewById(R.id.plan);

        p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(),TripPlann.class);
                startActivity(i);

            }
        });

predict.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        Intent i = new Intent(getApplicationContext(),TripPlann.class);
        startActivity(i);

    }
});
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            // Check if the bundle contains the desired key
            if (extras.containsKey("user")) {


                username = extras.getString("user");

            }
        } else {
            username = "user";
        }

        ai = findViewById(R.id.ai_button);
        ai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), aiChat.class);
                startActivity(i);
            }
        });

        amritsar = findViewById(R.id.amritsar_home);
        taj = findViewById(R.id.agra);
        amritsar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),cardOnClick2.class);
                Bundle bundle = new Bundle();
                bundle.putString("name","amritsar");
                i.putExtras(bundle);
                startActivity(i);
            }
        });
        taj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),cardOnClick2.class);
                Bundle bundle = new Bundle();
                bundle.putString("name","taj mehal");
                i.putExtras(bundle);
                startActivity(i);

            }
        });

        b=findViewById(R.id.udaipur);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),cardOnClick2.class);
                Bundle bundle = new Bundle();
                bundle.putString("name","udaipur lake pichola");
                i.putExtras(bundle);
                startActivity(i);

            }
        });
        c=findViewById(R.id.indiagate);
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),cardOnClick2.class);
                Bundle bundle = new Bundle();
                bundle.putString("name","india gate");
                i.putExtras(bundle);
                startActivity(i);


            }
        });
        d = findViewById(R.id.varanasi);
        d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),cardOnClick2.class);
                Bundle bundle = new Bundle();
                bundle.putString("name","varanasi ghat");
                i.putExtras(bundle);
                startActivity(i);

            }
        });
        e = findViewById(R.id.kolkata);
        e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(),cardOnClick2.class);
                Bundle bundle = new Bundle();
                bundle.putString("name"," kolkata victoria memorial hall");
                i.putExtras(bundle);
                startActivity(i);
            }
        });

        airecyclerview = findViewById(R.id.airecyclerView);
        airecyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


        generatedTextList = new ArrayList<>();
        adapterai = new GeneratedTextAdapter(generatedTextList);
        airecyclerview.setAdapter(adapterai);
        //prompt = findViewById(R.id.prompttext);
        img = findViewById(R.id.searchai);
        executorService = Executors.newCachedThreadPool();


        drawerLayout = findViewById(R.id.drawer_layout);
        sidebar = findViewById(R.id.sidebar);

        sidebar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        // Set up click listener for the sidebar ImageView
        sidebar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(findViewById(R.id.navigation_view))) {
                    drawerLayout.closeDrawer(findViewById(R.id.navigation_view));
                } else {
                    drawerLayout.openDrawer(findViewById(R.id.navigation_view));
                }
            }
        });


        searchplace = findViewById(R.id.searchforplace);
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyDNOkgVyl9Wzj0TKT_O8pmUB4L8mwuiy_o");
        }
        placesClient = Places.createClient(this);
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        if (autocompleteFragment != null) {
            // Specify the types of place data to return
            autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

            // Set up a PlaceSelectionListener to handle the selected place
            autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @SuppressLint("SuspiciousIndentation")
                @Override
                public void onPlaceSelected(@NonNull Place place) {
                    // Handle the selected place
                    selectedPlaceId = place.getId();
                    selectedPlaceName = place.getName();
                    Intent i = new Intent(Dashboard.this, cardOnClick2.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("id", selectedPlaceId);
                    bundle.putString("name", selectedPlaceName);
                    i.putExtras(bundle);
                    startActivity(i, bundle);


                }

                @Override
                public void onError(@NonNull Status status) {
                    // Handle errors
                    Log.e("PlaceDebug", "Error: " + status.getStatusMessage());
                }


            });
        }


        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String place = searchplace.getText().toString();
                Log.d("place 123", place);
                Intent i = new Intent(getApplicationContext(), cardOnclick.class);
                Bundle bundle = new Bundle();
                bundle.putString("name", place);
                bundle.putString("id", selectedPlaceId);
                i.putExtras(bundle);
                startActivity(i, bundle);
            }
        });


        searchplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear the text when clicked
                searchplace.setText("");
            }
        });

        searchplace.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // Clear the text when EditText gains focus
                    searchplace.setText("");
                }
            }
        });


        recyclerView = findViewById(R.id.placeList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        placeList = new ArrayList<>();
        adapter = new PlaceAdapter(this, placeList);
        recyclerView.setAdapter(adapter);

        // Fetch places data asynchronously
        fetchPlacesData();


        NavigationView navigationView = findViewById(R.id.navigation_view);
        View headerview = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            {

                if (id == R.id.nav_profile) {
                    // Navigate to Profile activity
                    startActivity(new Intent(Dashboard.this, profilePage.class));

                    return true;
                } else if (id == R.id.nav_write_review) {
                    // Navigate to Write Review activity
                    startActivity(new Intent(Dashboard.this, ReviewActivity.class));
                    return true;
                } else if (id == R.id.nav_view_reviews) {
                    // Navigate to View Reviews activity
                    startActivity(new Intent(Dashboard.this, myreviews.class));
                    return true;
                } else if (id == R.id.nav_login) {
                    // Navigate to Login activity
                    startActivity(new Intent(Dashboard.this, Mainscreen.class));
                    return true;
                } else if (id == R.id.nav_signup) {
                    // Navigate to Sign Up activity
                    startActivity(new Intent(Dashboard.this, Registeration.class));
                    return true;
                } else if (id == R.id.nav_logout) {
                    // Log out the user
                    FirebaseAuth.getInstance().signOut();
                    // Navigate to Login activity
                    startActivity(new Intent(Dashboard.this, Dashboard.class));
                    // Finish current activity to prevent going back to MainActivity after logging out
                    finish();
                    return true;
                } else
                    return false;
            }
        });
        // Check if the permission is not granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Request the permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }


        headertext = headerview.findViewById(R.id.user_name);
        headertext.setText(username);
        Profile_image = headerview.findViewById(R.id.user_photo);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String userid = null;
        if (user != null) {
             userid = user.getUid();
            // Proceed with accessing user data
        } else {
            // Handle the case where the user is not authenticated
            // For example, you can redirect them to the login screen
            startActivity(new Intent(Dashboard.this, Mainscreen.class));
            finish(); // Finish the current activity to prevent going back
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").whereEqualTo("userid",userid)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                            String imageUriString = document.getString("profile_image");
                            if (imageUriString != null) {
                                Glide.with(getApplicationContext()).load(imageUriString).into(Profile_image);

                            } else {
                                // If image URI is null, set a default image or hide the ImageView
                                // For example:
                                // img.setImageResource(R.drawable.default_profile_image);
                                Profile_image.setVisibility(View.GONE); // Hide the ImageView
                            }
                        }


                    }
                });
    }







        private void fetchPlacesData() {
        // Use Google Places API to fetch data about places in India
        String placesUrl = "https://maps.googleapis.com/maps/api/place/textsearch/json?" +
                "query=best+tourist+places+to+visit+in+India" +
                "&key=" + API_KEY;

        @SuppressLint("NotifyDataSetChanged") JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, placesUrl, null,
                response -> {
                    try {
                        JSONArray results = response.getJSONArray("results");
                        for (int i = 0; i < results.length(); i++) {
                            JSONObject placeObject = results.getJSONObject(i);
                            String name = placeObject.getString("name");
                            String photoUrl = ""; // Initialize with empty string

                            // Check if the 'photos' array exists and has elements
                            if (placeObject.has("photos")) {
                                JSONArray photosArray = placeObject.getJSONArray("photos");
                                for (int j = 0; j < photosArray.length(); j++) {
                                    JSONObject photoObject = photosArray.getJSONObject(j);
                                    String photoReference = photoObject.getString("photo_reference");

                                    photoUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + photoReference + "&key=" + API_KEY;

                                    // Add place to the list with photoUrl
                                    placeList.add(new PlaceModel(name, photoUrl));

                                    // Break after finding the first photo (optional)
                                    break;
                                }
                            }
                        }
                        // Notify adapter about the data change
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        Log.d("error", "An error occurred while parsing JSON response");
                    }
                },
                error -> {
                    // Handle error
                    Toast.makeText(Dashboard.this, "Error fetching places data", Toast.LENGTH_SHORT).show();
                });

        // Add the request to the RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void searchForAI() {
        GenerativeModel gm = new GenerativeModel("gemini-pro", "AIzaSyBqbeQ812_weTJi5x2yh_GdZdP7kiJLPA4");

        GenerativeModelFutures model = GenerativeModelFutures.from(gm);
        String promptText = prompt.getText().toString();

        if (promptText.isEmpty()) {
            clearGeneratedContent();
            return;
        }

        Content content = new Content.Builder().addText(promptText).build();

        ListenableFuture<GenerateContentResponse> response = model.generateContent(content);
        response.addListener(() -> {
            try {
                GenerateContentResponse result = response.get();
                String generatedText = result.getText();

                // Split the generated text into title and text based on the delimiter

                runOnUiThread(() -> {
                    // Create a new Place object with the extracted title and text

                    generatedTextList.add(generatedText);
                    adapter.notifyDataSetChanged();
                });

            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, executorService);


    }

    private void clearGeneratedContent() {
        generatedTextList.clear();
        adapter.notifyDataSetChanged();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }
}





