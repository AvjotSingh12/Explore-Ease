package com.example.travelapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;



public class Padapter extends RecyclerView.Adapter<Padapter.ViewHolder> {

    private Context context;
    private List<String> hotelList;

    public Padapter(Context context, List<String> hotelList) {
        this.context = context;
        this.hotelList = hotelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.destinationphoto, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String hotel = hotelList.get(position);

        // Bind hotel data to views

        //holder.hotelLocation.setText("Location: " + hotel.getLatitude() + ", " + hotel.getLongitude());

        // Load photo if available
        if (!hotel.isEmpty()) {
            String photoUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" +
                    hotel + "&key=AIzaSyDNOkgVyl9Wzj0TKT_O8pmUB4L8mwuiy_o";
            Glide.with(context).load(photoUrl).into(holder.hotelPhoto);
        } else {
            // Handle case when photo is not available
            holder.hotelPhoto.setImageResource(R.drawable.dash);
        }
    }

    @Override
    public int getItemCount() {
        return hotelList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {


        ImageView hotelPhoto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // hotelLocation = itemView.findViewById(R.id.hotel_location);
            hotelPhoto = itemView.findViewById(R.id.destinationImageView);
        }
    }
}
