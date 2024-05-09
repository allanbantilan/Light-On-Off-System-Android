package com.scoringapp.powersystem;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.Timestamp;
import java.util.HashMap;
import java.util.Map;
import com.google.firebase.firestore.DocumentReference;


import android.content.Intent;

public class profile_activity extends AppCompatActivity {

    private EditText editTextNewPassword;
    private EditText editTextConfirmPassword;
    private EditText editTextName;
    private EditText editTextSurname;
    private EditText editemail;
    private Button updatePasswordButton;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        editemail = findViewById(R.id.email);
        editTextNewPassword = findViewById(R.id.editTextNewPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        editTextName = findViewById(R.id.name);
        editTextSurname = findViewById(R.id.surname);
        updatePasswordButton = findViewById(R.id.updatePasswordButton);

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // User is authenticated, fill the email field
            editemail.setText(user.getEmail());
        }
        updatePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPassword = editTextNewPassword.getText().toString();
                String confirmPassword = editTextConfirmPassword.getText().toString();

                if (newPassword.equals(confirmPassword)) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user != null) {
                        // Update the user's password
                        user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(Task<Void> task) {
                                if (task.isSuccessful()) {
                                    // Password updated successfully
                                    Toast.makeText(profile_activity.this, "Password updated successfully", Toast.LENGTH_SHORT).show();

                                    // Get the current timestamp
                                    Timestamp now = Timestamp.now();

                                    // Create a map to store user data in the desired order
                                    Map<String, Object> userData = new HashMap<>();
                                    userData.put("email", editemail.getText().toString());
                                    userData.put("name", editTextName.getText().toString());
                                    userData.put("surname", editTextSurname.getText().toString());
                                    userData.put("password", editTextConfirmPassword.getText().toString());
                                    userData.put("dateCreated", now);

                                    // Add the user data to Firestore in the "users" collection
                                    db.collection("users")
                                            .add(userData)
                                            .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                @Override
                                                public void onComplete(Task<DocumentReference> task) {
                                                    if (task.isSuccessful()) {
                                                        // User data added to Firestore successfully
                                                        Toast.makeText(profile_activity.this, "Saved Successfully", Toast.LENGTH_SHORT).show();

                                                        // Redirect to the dashboard
                                                        Intent intent = new Intent(profile_activity.this, dasboard_activity.class);
                                                        startActivity(intent);
                                                        finish(); // Finish the current activity
                                                    } else {
                                                        // User data storage in Firestore failed
                                                        Toast.makeText(profile_activity.this, "Error Occurred", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                } else {
                                    // Password update failed
                                    Toast.makeText(profile_activity.this, "Password update failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                } else {
                    // Passwords don't match
                    Toast.makeText(profile_activity.this, "Passwords don't match", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
