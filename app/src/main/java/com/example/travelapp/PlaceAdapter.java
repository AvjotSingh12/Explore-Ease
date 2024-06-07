package com.example.travelapp;


import static androidx.core.content.ContextCompat.startActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;


import java.text.BreakIterator;
import java.util.List;




public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.PlaceHolder> {
public Context context;
public static List<PlaceModel> placelist;
public PlaceAdapter (Context context, List<PlaceModel> places){

    this.context = context;
    this.placelist = places;
}
    @NonNull
    @Override
    public PlaceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(context).inflate(R.layout.placehold, parent,false);
      return new PlaceHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull PlaceHolder holder, @SuppressLint("RecyclerView") int position) {

        PlaceModel place = placelist.get(position);
        holder.name.setText(place.getName());
        Glide.with(context).load(place.getImage()).into(holder.img);
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Intent i = new Intent(context,cardOnClick2.class);
                Bundle bundle = new Bundle();
                bundle.putString("image",place.getImage());
                bundle.putString("name",place.name);
                i.putExtras(bundle);
                startActivity(context,i,bundle);


            }
        });
    }




    @Override
    public int getItemCount() {
        return placelist.size();
    }

    public static class PlaceHolder extends RecyclerView.ViewHolder{
        public BreakIterator date;
        ImageView img
        ;
        TextView name;


    public PlaceHolder (@NonNull View itemview){
        super(itemview);
        img = itemview.findViewById(R.id.PlaceImage);
        name = itemview.findViewById(R.id.PlaceName);
    }
}


}
