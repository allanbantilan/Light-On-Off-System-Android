package com.scoringapp.powersystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;



public class login_activity extends AppCompatActivity {
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button registerButton;
    private Button adminLogin;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        adminLogin = findViewById(R.id.adminLogin);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton2);
        registerButton = findViewById(R.id.registerButton);
        auth = FirebaseAuth.getInstance();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // When the "Register" button is clicked, open the admin authentication page
                Intent adminIntent = new Intent(login_activity.this, adminAuth.class);
                startActivity(adminIntent);
            }
        });

        adminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent to start the admin_login activity
                Intent intent = new Intent(login_activity.this, admin_login.class);
                startActivity(intent);
                // Optional: Close the current activity
                finish();
            }
        });


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(login_activity.this, "Email and password are required", Toast.LENGTH_SHORT).show();
                } else {
                    signIn(email, password);
                }
            }
        });
    }

    private void signIn(String email, String password) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            // Check if the signed-in user is in the "users" collection
                            checkIfUserExists(user.getEmail());
                        }
                    } else {
                        // Login failed, show an error message
                        Toast.makeText(login_activity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkIfUserExists(String userEmail) {
        // Assuming you have a reference to your Firestore database
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Replace "users" with the actual collection name where user data is stored
        CollectionReference usersCollection = db.collection("users");

        usersCollection.whereEqualTo("email", userEmail)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (!task.getResult().isEmpty()) {
                            // User exists in the "users" collection, navigate to the dashboard
                            Intent intent = new Intent(login_activity.this, dasboard_activity.class);
                            startActivity(intent);
                            finish(); // Optional: Close the login activity
                        } else {
                            // User does not exist in the "users" collection, redirect to profile activity
                            Intent intent = new Intent(login_activity.this, profile_activity.class);
                            startActivity(intent);
                            finish(); // Optional: Close the login activity
                        }
                    } else {
                        // Handle exceptions when querying the "users" collection
                        Toast.makeText(login_activity.this, "Error checking user existence.", Toast.LENGTH_SHORT).show();
                    }
                });
    }






}

