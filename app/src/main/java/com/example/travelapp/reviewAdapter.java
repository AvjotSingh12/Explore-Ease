package com.example.travelapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class reviewAdapter extends RecyclerView.Adapter<reviewAdapter.PlaceHolder> {
    public Context context;
    public List<revModel> list;
    View view;

    public reviewAdapter(Context context, List<revModel> list) {
        this.context = context;
        this.list = list;
    }

    public reviewAdapter() {

    }

    @NonNull
    @Override
    public reviewAdapter.PlaceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.placeabout, parent, false);
        return new PlaceHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceHolder holder, int position) {
        revModel dest = list.get(position);
        holder.destination.setText(dest.getDestination());
        holder.review.setText(dest.getReview());
        holder.title.setText(dest.getTitle());
        // holder.rating.setText(dest.getRating());
        holder.with.setText(dest.getWith());
        holder.date.setText(dest.getDate());
        List<String> imageUrls = dest.getImageUri();
        if(!imageUrls.isEmpty()){
            for (int i=0; i<imageUrls.size(); i++) {
                Log.d("Image url",imageUrls.get(i));

                Glide.with(context)
                        .load(imageUrls.get(i))
                        .into(holder.img);


            }
        }

        holder.username.setText(dest.getUsername());
        if (dest.getUser_image() != null) {
            Glide.with(context).load(dest.getUser_image()).into(holder.profilePicture);

        }

      //  holder.profilePicture.setImageURI(dest.user_image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Retrieve the user ID of the user who posted the review
                String reviewerUserId = dest.getReviewid(); // Assuming you have a method to get the user ID

                // Create a chat room ID using the IDs of the logged-in user and the reviewer
                String chatRoomId = reviewerUserId+"-"+dest.getUserid();

                // Navigate to the chat activity, passing the chat room ID
                Intent i = new Intent(context, chatroom.class);
                i.putExtra("reviewerId", reviewerUserId);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Add this line to set the new task flag
                context.startActivity(i);
            }
        });
    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class PlaceHolder extends RecyclerView.ViewHolder {
        TextView destination, review, title, rating, with, date,username;
        ImageView img,profilePicture;


        public PlaceHolder(@NonNull View itemView) {
            super(itemView);
            destination = itemView.findViewById(R.id.destination);
            review = itemView.findViewById(R.id.review);
            title = itemView.findViewById(R.id.title);
            rating = itemView.findViewById(R.id.ratings);
            with = itemView.findViewById(R.id.withwhome);
            date = itemView.findViewById(R.id.date);
            img = itemView.findViewById(R.id.image);
            View profileLayout = itemView.findViewById(R.id.profileLayout);
            profilePicture = profileLayout.findViewById(R.id.profilePicture);
            username = profileLayout.findViewById(R.id.username);

        }
    }
}
