package com.example.travelapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PredictionAdapter extends RecyclerView.Adapter<PredictionAdapter.ViewHolder> {

    private Context context;
    private List<Package> packageList;

    public PredictionAdapter(Context context, List<Package> packageList) {
        this.context = context;
        this.packageList = packageList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.predict_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Package tripPackage = packageList.get(position);
        holder.packageNameTextView.setText(tripPackage.getPackageName());
        holder.hotelDetailsTextView.setText(tripPackage.getHotelDetails() != null ? tripPackage.getHotelDetails() : "N/A");
        holder.airlineTextView.setText(tripPackage.getAirline() != null ? tripPackage.getAirline() : "N/A");
        holder.itineraryTextView.setText(tripPackage.getItinerary());
        holder.placesCoveredTextView.setText(tripPackage.getPlacesCovered());
        holder.sightseeingPlacesCoveredTextView.setText(tripPackage.getSightseeingPlacesCovered());
        holder.startCityTextView.setText(tripPackage.getStartCity());
    }

    @Override
    public int getItemCount() {
        return packageList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView packageNameTextView;
        TextView hotelDetailsTextView;
        TextView airlineTextView;
        TextView itineraryTextView;
        TextView placesCoveredTextView;
        TextView sightseeingPlacesCoveredTextView;
        TextView startCityTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            packageNameTextView = itemView.findViewById(R.id.packageNameTextView);
            hotelDetailsTextView = itemView.findViewById(R.id.hotelDetailsTextView);
            airlineTextView = itemView.findViewById(R.id.airlineTextView);
            itineraryTextView = itemView.findViewById(R.id.itineraryTextView);
            placesCoveredTextView = itemView.findViewById(R.id.placesCoveredTextView);
            sightseeingPlacesCoveredTextView = itemView.findViewById(R.id.sightseeingPlacesCoveredTextView);
            startCityTextView = itemView.findViewById(R.id.startCityTextView);
        }
    }
}
