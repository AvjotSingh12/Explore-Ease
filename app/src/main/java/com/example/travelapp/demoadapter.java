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

public class demoadapter extends RecyclerView.Adapter<demoadapter.PlaceViewHolder> {
    private Context context;
    private List<demoplace> placeList;

    public demoadapter(Context context, List<demoplace> placeList) {
        this.context = context;
        this.placeList = placeList;
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.placeplaceplace, parent, false);
        return new PlaceViewHolder(view);
    }






    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        demoplace place = placeList.get(position);
        holder.placeName.setText(place.getName());
        Glide.with(context).load(demoplace.getPhotoUrl()).into(holder.placeImage);
    }


    @Override
    public int getItemCount() {
        return placeList.size();
    }

    public static class PlaceViewHolder extends RecyclerView.ViewHolder {
        ImageView placeImage;
        TextView placeName;

        public PlaceViewHolder(@NonNull View itemView) {
            super(itemView);
            placeImage = itemView.findViewById(R.id.placeImage);
            placeName = itemView.findViewById(R.id.placeName);
        }
    }
}
