package com.example.travelapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.ktx.Firebase;

import java.util.ArrayList;

public class messageAdapter extends RecyclerView.Adapter {

   Context context;
   ArrayList<messageModel> msgAdapterArraylist;
   int ITEM_SEND = 1;
   int ITEM_RECIVE = 2;

    public messageAdapter(Context context, ArrayList<messageModel> msgAdapterArraylist) {
        this.context = context;
        this.msgAdapterArraylist = msgAdapterArraylist;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       if( viewType == ITEM_SEND){
           View view = LayoutInflater.from(context).inflate(R.layout.sender_layout, parent, false);
           return new senderViewHolder(view);
       }
       else{
           View view = LayoutInflater.from(context).inflate(R.layout.reciver_layout, parent, false);
           return new reciverViewHolder(view);
       }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
     messageModel msg  = msgAdapterArraylist.get(position);
     if(holder.getClass() == senderViewHolder.class){
         senderViewHolder viewHolder = (senderViewHolder) holder;
         viewHolder.msgtxt.setText(msg.getMessage());

     }
     else {
         reciverViewHolder viewHolder = (reciverViewHolder)  holder;
         viewHolder.msgtxt.setText(msg.getMessage());

     }


    }

    @Override
    public int getItemCount() {

        return msgAdapterArraylist.size();
    }


    @Override
    public int getItemViewType(int position) {
       messageModel messages = msgAdapterArraylist.get(position);
      if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messages.getSenderid())){
          return ITEM_SEND;
      }
      else{
          return ITEM_RECIVE;
      }
    }

    static class
    senderViewHolder extends RecyclerView.ViewHolder {
        TextView msgtxt;


        public senderViewHolder(@NonNull View itemView) {
            super(itemView);

            msgtxt = itemView.findViewById(R.id.sendermessage);


        }
    }

    static class reciverViewHolder extends RecyclerView.ViewHolder {
        TextView msgtxt;
        public reciverViewHolder(@NonNull View itemView) {
            super(itemView);
            msgtxt = itemView.findViewById(R.id.recivertextset);

        }
    }


}
