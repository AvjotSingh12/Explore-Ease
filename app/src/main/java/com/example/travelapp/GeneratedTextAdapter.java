package com.example.travelapp;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class GeneratedTextAdapter extends RecyclerView.Adapter<GeneratedTextAdapter.GeneratedTextViewHolder> {

    private List<String> generatedTextList;

    public GeneratedTextAdapter(List<String> generatedTextList) {
        this.generatedTextList = generatedTextList;
    }

    @NonNull
    @Override
    public GeneratedTextViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_generated_text, parent, false);
        return new GeneratedTextViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GeneratedTextViewHolder holder, int position) {
        String  title = generatedTextList.get(position);
        holder.title.setText(title);

    }

    @Override
    public int getItemCount() {
        Log.d("Adapter" , generatedTextList.toString());
        return generatedTextList.size();
    }

    public static class GeneratedTextViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView desc;

        public GeneratedTextViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);

        }
    }
}
