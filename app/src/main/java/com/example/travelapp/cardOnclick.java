package com.example.travelapp;


import android.annotation.SuppressLint;
import android.os.Bundle;


import androidx.activity.EdgeToEdge;

import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;



public class  cardOnclick extends AppCompatActivity {
    String name,id ;
    RecyclerView r1,r2,r3;
    private hotelsadapter adapter;
    private static final String API_KEY = "AIzaSyDNOkgVyl9Wzj0TKT_O8pmUB4L8mwuiy_o";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_card_onclick);

        ViewPager viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        Bundle bundle = getIntent().getExtras();
        // Assuming 'this' is an Activity or a context
        assert bundle != null;
        name = bundle.getString("name");
        id = bundle.getString("id");





        // Set up ViewPager with custom adapter
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(),name,id);
        viewPager.setAdapter(adapter);

        // Connect TabLayout with ViewPager
        tabLayout.setupWithViewPager(viewPager);



    }
}