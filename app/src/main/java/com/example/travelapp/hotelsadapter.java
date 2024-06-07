package com.example.travelapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;



public class hotelsadapter extends RecyclerView.Adapter<hotelsadapter.ViewHolder> {

    private Context context;
    private List<Hotel> hotelList;

    public hotelsadapter(Context context, List<Hotel> hotelList) {
        this.context = context;
        this.hotelList = hotelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.placehold, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Hotel hotel = hotelList.get(position);

        // Bind hotel data to views
        holder.hotelName.setText(hotel.getName());
        Intent i = new Intent(context, afterHotel.class);
        i.putExtra("name", hotel.getName());
        i.putExtra("lat",hotel.getLatitude());
        i.putExtra("long",hotel.getLongitude());
        //holder.hotelLocation.setText("Location: " + hotel.getLatitude() + ", " + hotel.getLongitude());
        String photoUrl = null;
        // Load photo if available
        if (!hotel.getPhotoReference().isEmpty()) {
             photoUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" +
                    hotel.getPhotoReference() + "&key=AIzaSyDNOkgVyl9Wzj0TKT_O8pmUB4L8mwuiy_o";
            Glide.with(context).load(photoUrl).into(holder.hotelPhoto);
            i.putExtra("photo", photoUrl);
        } else {
            // Handle case when photo is not available
            holder.hotelPhoto.setImageResource(R.drawable.dash);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return hotelList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView hotelName;

        ImageView hotelPhoto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            hotelName = itemView.findViewById(R.id.PlaceName);
           // hotelLocation = itemView.findViewById(R.id.hotel_location);
            hotelPhoto = itemView.findViewById(R.id.PlaceImage);
        }
    }
}
