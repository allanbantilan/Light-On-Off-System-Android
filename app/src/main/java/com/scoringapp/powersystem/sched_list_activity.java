package com.scoringapp.powersystem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.widget.Button;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import android.widget.Button;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.DialogInterface;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Locale;

public class sched_list_activity extends AppCompatActivity {

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sched_list3);

        // Initialize Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("devices/device1/schedule");

        // Fetch and populate data for "Schedule 2"
        fetchAndPopulateData("Schedule 1", R.id.TimeOn1, R.id.DateOn1, R.id.TimeOff1, R.id.DateOff1);

        // Fetch and populate data for "Schedule 3"
        fetchAndPopulateData("Schedule 2", R.id.TimeOn2, R.id.DateOn2, R.id.TimeOff2, R.id.DateOff2);


//        Button editbutton1 = findViewById(R.id.editbutton);
//        Button editbutton2 = findViewById(R.id.editbutton1);
//        Button deletebutton2 = findViewById(R.id.deletebutton);
//        Button deletebutton3 = findViewById(R.id.deletebutton1);

//        deletebutton2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Create an AlertDialog.Builder
//                AlertDialog.Builder builder = new AlertDialog.Builder(sched_list_activity.this);
//
//                // Set the dialog title and message
//                builder.setTitle("Confirmation")
//                        .setMessage("Do you want to delete Schedule 1?");
//
//                // Add the Yes button
//                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        // User clicked Yes, proceed with deletion
//                        deleteSchedule2();
//                    }
//                });
//
//                // Add the No button
//                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        // User clicked No, do nothing
//                        dialog.dismiss(); // Close the dialog
//                    }
//                });
//
//                // Create and show the AlertDialog
//                AlertDialog alertDialog = builder.create();
//                alertDialog.show();
//            }
//        });
//
//
//        deletebutton3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(sched_list_activity.this);
//
//                // Set the dialog title and message
//                builder.setTitle("Confirmation")
//                        .setMessage("Do you want to delete Schedule 2?");
//
//                // Add the Yes button
//                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        // User clicked Yes, proceed with deletion
//                        deleteSchedule3();
//                    }
//                });
//
//                // Add the No button
//                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        // User clicked No, do nothing
//                        dialog.dismiss(); // Close the dialog
//                    }
//                });
//
//                // Create and show the AlertDialog
//                AlertDialog alertDialog = builder.create();
//                alertDialog.show();
//            }
//        });
//
//        // Set OnClickListener for the "Edit" Button
//        editbutton1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Handle the click event here
//                // Start a new activity when the button is clicked
//                Intent intent = new Intent(sched_list_activity.this, edit_activity.class);
//                startActivity(intent);
//            }
//        });
//
//        editbutton2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Handle the click event here
//                // Start a new activity when the button is clicked
//                Intent intent = new Intent(sched_list_activity.this, edit_activity2.class);
//                startActivity(intent);
//            }
//        });


    }

    private void fetchAndPopulateData(String scheduleId, int timeOnId, int dateOnId, int timeOffId, int dateOffId) {
        DatabaseReference scheduleReference = mDatabase.child(scheduleId);

        scheduleReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Process and display data as before
                if (snapshot.exists()) {
                    String timeOn = snapshot.child("timeOn").getValue(String.class);
                    String dateOn = snapshot.child("dateOn").getValue(String.class);
                    String timeOff = snapshot.child("timeOff").getValue(String.class);
                    String dateOff = snapshot.child("dateOff").getValue(String.class);

                    // Assuming you have the corresponding TextViews in your layout
                    TextView timeOnTextView = findViewById(timeOnId);
                    TextView dateOnTextView = findViewById(dateOnId);
                    TextView timeOffTextView = findViewById(timeOffId);
                    TextView dateOffTextView = findViewById(dateOffId);

                    // Populate the TextViews with the retrieved data
                    timeOnTextView.setText(timeOn != null ? timeOn : "Not set");
                    dateOnTextView.setText(dateOn != null ? dateOn : "Not set");
                    timeOffTextView.setText(timeOff != null ? timeOff : "Not set");
                    dateOffTextView.setText(dateOff != null ? dateOff : "Not set");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors
                Log.e("ScheduleActivity", "Error retrieving schedule data: " + error.getMessage());
            }
        });

    }

    private void deleteSchedule2() {
        // Get a reference to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference scheduleRef = database.getReference("devices/device1/schedule/Schedule 1");

        // Remove the Schedule 2 node
        scheduleRef.removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Node removed successfully, show a toast
                        Toast.makeText(getApplicationContext(), "Schedule 1 deleted", Toast.LENGTH_SHORT).show();
                        // Refresh the activity
                        recreate();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to remove the node, show an error toast
                        Toast.makeText(getApplicationContext(), "Error deleting Schedule 1", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void deleteSchedule3() {
        // Get a reference to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference scheduleRef = database.getReference("devices/device1/schedule/Schedule 2");

        // Remove the Schedule 2 node
        scheduleRef.removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Node removed successfully, show a toast
                        Toast.makeText(getApplicationContext(), "Schedule 2 deleted", Toast.LENGTH_SHORT).show();
                        // Refresh the activity
                        recreate();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to remove the node, show an error toast
                        Toast.makeText(getApplicationContext(), "Error deleting Schedule 2", Toast.LENGTH_SHORT).show();
                    }
                });
    }


}