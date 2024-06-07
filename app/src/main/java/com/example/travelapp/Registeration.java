package com.example.travelapp;

import static com.android.volley.VolleyLog.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;



public class Registeration extends AppCompatActivity {
    EditText username, password, confirmpass;
    private FirebaseAuth mAuth;
    TextView txt;
    Button register;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_registeration);
        mAuth = FirebaseAuth.getInstance();
        username=findViewById(R.id.username);
        password=findViewById(R.id.password);
        confirmpass=findViewById(R.id.confirmPassword);
        register=findViewById(R.id.registerButton);
        txt = findViewById(R.id.loginTextView);

        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent (getApplicationContext(),Mainscreen.class);
                startActivity(i);
            }
        });



        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = username.getText().toString();
                String pass = password.getText().toString();
                String cpass = confirmpass.getText().toString();
                if (TextUtils.isEmpty(user) || TextUtils.isEmpty(pass)|| TextUtils.isEmpty(cpass)) {
                    // Show an error message if email or password is empty
                    Toast.makeText(Registeration.this, "Email or password cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!pass.equals(cpass)){
                    Toast.makeText(getApplicationContext(), "password do no match", Toast.LENGTH_SHORT).show();

                }

                    mAuth.createUserWithEmailAndPassword(user, pass)
                            .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        startActivity(new Intent(getApplicationContext(),Mainscreen.class));
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "createUserWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();

                                    } else {
                                        Toast.makeText(Registeration.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });

            }
        });




    }

}