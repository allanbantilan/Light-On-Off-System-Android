package com.scoringapp.powersystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class adminAuth extends AppCompatActivity {
    private EditText adminEmailEditText;
    private EditText adminPasswordEditText;
    private Button adminAuthButton;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_auth);

        adminEmailEditText = findViewById(R.id.userEmail);
        adminPasswordEditText = findViewById(R.id.adminPasswordEditText);
        adminAuthButton = findViewById(R.id.verifyButton);
        db = FirebaseFirestore.getInstance();

        adminAuthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String adminEmail = adminEmailEditText.getText().toString();
                final String adminPassword = adminPasswordEditText.getText().toString();

                if (adminEmail.isEmpty() || adminPassword.isEmpty()) {
                    Toast.makeText(adminAuth.this, "Admin email and password are required", Toast.LENGTH_SHORT).show();
                } else {
                    // Query Firestore to check admin credentials
                    db.collection("admins")
                            .whereEqualTo("email", adminEmail)
                            .get()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    QuerySnapshot querySnapshot = task.getResult();
                                    if (!querySnapshot.isEmpty()) {
                                        DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                                        String storedPassword = document.getString("pass");
                                        if (adminPassword.equals(storedPassword)) {
                                            // Admin authentication successful
                                            // Proceed to the next activity or perform additional actions
                                            // For example, navigate to the user management page
                                            Intent intent = new Intent(adminAuth.this, admin_add_user.class);
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(adminAuth.this, "Invalid admin password", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(adminAuth.this, "Admin not found", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(adminAuth.this, "Failed to query admin credentials", Toast.LENGTH_SHORT).show();
                                }
                            });

                }
            }
        });
    }
}
