package com.example.travelapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;



public class Mainscreen extends AppCompatActivity {
    EditText username;
    EditText password;
    Button login;
    private FirebaseAuth mAuth;
    TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mainscreen);


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        txt = findViewById(R.id.signupText);
        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),Registeration.class);
                startActivity(i);
            }
        });
        if(user!= null){
            Toast.makeText(this,"Your are already signed in", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getApplicationContext(), Dashboard.class);
            i.putExtra("user", user.getEmail());
            startActivity(i);

        }
        else{
            username=findViewById(R.id.username);
            password=findViewById(R.id.password);


            login=findViewById(R.id.loginButton);

            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String user = username.getText().toString();
                    String pass = password.getText().toString();
                    if (TextUtils.isEmpty(user) || TextUtils.isEmpty(pass)) {
                        // Show an error message if email or password is empty
                        Toast.makeText(Mainscreen.this, "Email or password cannot be empty", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mAuth.signInWithEmailAndPassword(user, pass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        if (user != null) {
                                            Log.d("signInWithEmail:success", "User: " + user.getEmail() + " successfully signed in");
                                            Intent i = new Intent(getApplicationContext(), Dashboard.class);
                                            i.putExtra("user", user.getEmail());
                                            startActivity(i);
                                        } else {
                                            Log.e("signInWithEmail:success", "User object is null after sign-in");
                                            Toast.makeText(Mainscreen.this, "User object is null after sign-in", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        startActivity(new Intent(getApplicationContext(),Registeration.class));

                                        // If sign in fails, display a message to the user.
                                        Log.e("signInWithEmail:failure", "Error: " + task.getException().getMessage());
                                        Toast.makeText(Mainscreen.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            });


        }



    }
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Log.d("sign in successful","no error");

        }
    }
}