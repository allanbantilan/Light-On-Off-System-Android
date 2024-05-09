package com.scoringapp.powersystem;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.AuthResult;
import android.content.Intent;


// ... (import statements)

public class admin_add_user extends AppCompatActivity {
    private EditText userEmailEditText;
    private Button sendVerificationButton;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_add_user);

        userEmailEditText = findViewById(R.id.userEmail);
        sendVerificationButton = findViewById(R.id.verifyButton);
        auth = FirebaseAuth.getInstance();

        sendVerificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = userEmailEditText.getText().toString();

                if (userEmail.isEmpty()) {
                    Toast.makeText(admin_add_user.this, "User email is required", Toast.LENGTH_SHORT).show();
                } else {
                    sendVerificationEmail(userEmail);
                }
            }
        });
    }

    private void sendVerificationEmail(String email) {
        // Create a new Firebase user with the given email and a temporary password
        auth.createUserWithEmailAndPassword(email, "temporary_password")
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();

                            // Send a verification email to the user
                            user.sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                // Verification email sent

                                                Toast.makeText(admin_add_user.this, "Verification email sent to " + user.getEmail(), Toast.LENGTH_SHORT).show();

                                                // Redirect to the login activity
                                                Intent intent = new Intent(admin_add_user.this, login_activity.class);
                                                startActivity(intent);
                                                finish(); // Close the current activity
                                            } else {
                                                // Error sending the verification email
                                                Toast.makeText(admin_add_user.this, "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            // Error creating a temporary user
                            Toast.makeText(admin_add_user.this, "Failed to create a user.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
