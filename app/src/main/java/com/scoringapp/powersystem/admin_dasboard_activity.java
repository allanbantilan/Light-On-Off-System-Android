package com.scoringapp.powersystem;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import com.google.firebase.auth.FirebaseUser;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class admin_dasboard_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);


        RelativeLayout buildingLayout = findViewById(R.id.building);
        RelativeLayout settings = findViewById(R.id.settings);
        RelativeLayout schedule = findViewById(R.id.schedule);
        RelativeLayout schedlist = findViewById(R.id.schedlist);
        buildingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event here
                // Start a new activity when the RelativeLayout is clicked
                Intent intent = new Intent(admin_dasboard_activity.this, building_activity.class);
                startActivity(intent);
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event here
                // Start a new activity when the RelativeLayout is clicked
                Intent intent = new Intent(admin_dasboard_activity.this, inside_settings.class);
                startActivity(intent);
            }
        });

        schedlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event here
                // Start a new activity when the RelativeLayout is clicked
                Intent intent = new Intent(admin_dasboard_activity.this, admin_sched_list_activity.class);
                startActivity(intent);
            }
        });

        schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an AlertDialog.Builder
                AlertDialog.Builder builder = new AlertDialog.Builder(admin_dasboard_activity.this);

                // Set the dialog title and message
                builder.setTitle("Logout Confirmation")
                        .setMessage("Are you sure you want to log out?");

                // Add the Yes button
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Sign out the current user
                        FirebaseAuth.getInstance().signOut();

                        // Start a new activity after logging out
                        Intent intent = new Intent(admin_dasboard_activity.this, login_activity.class);
                        startActivity(intent);

                        // Finish the current activity to prevent going back to it when the user presses back
                        finish();
                    }
                });

                // Add the No button
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked No, do nothing
                        dialog.dismiss(); // Close the dialog
                    }
                });

                // Create and show the AlertDialog
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Create an AlertDialog.Builder for back button press
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Set the dialog title and message
        builder.setTitle("Logout Confirmation")
                .setMessage("Are you sure you want to log out?");

        // Add the Yes button
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Sign out the current user
                FirebaseAuth.getInstance().signOut();

                // Start a new activity after logging out
                Intent intent = new Intent(admin_dasboard_activity.this, login_activity.class);
                startActivity(intent);

                // Finish the current activity to prevent going back to it when the user presses back
                finish();
            }
        });

        // Add the No button
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked No, do nothing
                dialog.dismiss(); // Close the dialog
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
