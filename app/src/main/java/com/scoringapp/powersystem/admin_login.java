package com.scoringapp.powersystem;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import android.content.Intent;

public class admin_login extends AppCompatActivity {

    private EditText adminEmailEditText;
    private EditText adminPasswordEditText;
    private Button adminLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_login);

        adminEmailEditText = findViewById(R.id.adminEmail);
        adminPasswordEditText = findViewById(R.id.amdinPassword);
        adminLoginButton = findViewById(R.id.adminLogin);

        adminLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String adminEmail = adminEmailEditText.getText().toString();
                String adminPassword = adminPasswordEditText.getText().toString();

                if (!adminEmail.isEmpty() && !adminPassword.isEmpty()) {
                    // Authenticate admin
                    checkAdminCredentials(adminEmail, adminPassword);
                } else {
                    // Show error message if fields are empty
                    Toast.makeText(admin_login.this, "Email and password are required", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void checkAdminCredentials(String adminEmail, String adminPassword) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Replace "YOUR_COLLECTION_NAME" with the actual name of your admins collection
        CollectionReference adminsCollection = db.collection("admins");

        adminsCollection.whereEqualTo("email", adminEmail)
                .whereEqualTo("pass", adminPassword)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (!task.getResult().isEmpty()) {
                            // Admin credentials are valid, redirect to admin_dashboard_activity
                            Toast.makeText(admin_login.this, "Admin login successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(admin_login.this, admin_dasboard_activity.class);
                            startActivity(intent);
                            finish(); // Optional: Close the current activity
                        } else {
                            // Admin credentials are invalid, show an error message
                            Toast.makeText(admin_login.this, "Invalid admin credentials", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Handle exceptions when querying the admins collection
                        Toast.makeText(admin_login.this, "Error checking admin credentials", Toast.LENGTH_SHORT).show();
                        Log.d("AdminLogin", "Result: " + task.getResult());

                    }
                });
    }
}
