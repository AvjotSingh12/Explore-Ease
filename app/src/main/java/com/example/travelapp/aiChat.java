package com.example.travelapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class aiChat extends AppCompatActivity {
RecyclerView recyclerView;
    private ExecutorService executorService;
GeneratedTextAdapter adapterai;
private List<String> generatedTextList;
Button btn;
EditText edt;
    LinearLayout cont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_ai_chat);
        edt = findViewById(R.id.messageEditText);
        btn = findViewById(R.id.sendButton);
        cont = findViewById(R.id.container);



        executorService = Executors.newSingleThreadExecutor();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchForAI();
            }
        });


    }
    private void clearGeneratedContent() {
        generatedTextList.clear();
        adapterai.notifyDataSetChanged();
    }
    private void searchForAI() {
        GenerativeModel gm = new GenerativeModel("gemini-pro", "AIzaSyBqbeQ812_weTJi5x2yh_GdZdP7kiJLPA4");

        GenerativeModelFutures model = GenerativeModelFutures.from(gm);

        String p = edt.getText().toString();
                Content content = new Content.Builder().addText(p).build();

        ListenableFuture<GenerateContentResponse> response = model.generateContent(content);
        response.addListener(() -> {
            try {
                GenerateContentResponse result = response.get();
                String generatedText = result.getText();
                Log.d("airesponse", generatedText);

                runOnUiThread(() -> {
                    String v=  generatedText.replace("*", "");
                    TextView textView = new TextView(this);
                    textView.setText(v);

                    // Apply styling
                    textView.setTextSize(16);
                    textView.setTextColor(ContextCompat.getColor(this, R.color.black)); // Use a color resource for consistency
                    // textView.setTypeface(Typeface.DEFAULT_BOLD);
                    textView.setPadding(24, 24, 24, 24); // Add padding for better spacing

                    // Create layout parameters for the TextView

                    // Apply layout parameters to the TextView

                    // Add a background drawable to create a card-like appearance
                    textView.setBackground(ContextCompat.getDrawable(this, R.drawable.textview_background));

                    // Add the TextView to the container
                    cont.addView(textView);
                    //  generatedTextList.add(v);
                    // adapterai.notifyDataSetChanged();
                });

            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, executorService);


    }
}