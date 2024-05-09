package com.scoringapp.powersystem;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Calendar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import android.widget.Button;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.DialogInterface;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import android.os.Handler;
import android.os.Looper;
import java.util.Date;
import android.util.Log;



public class edit_activity extends AppCompatActivity {

    private DatabaseReference mDatabase;

    private TextView textViewTimeOn;
    private TextView textViewDateOn;
    private TextView textViewTimeOff;
    private TextView textViewDateOff;
    private DatabaseReference relayStatusReference;
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_activity);

        // Initialize Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("devices/device1/schedule/Schedule 1");
        relayStatusReference = database.getReference("devices/device1");

        // Initialize TextViews
        textViewTimeOn = findViewById(R.id.textViewTimeOn);
        textViewDateOn = findViewById(R.id.textViewDateOn);
        textViewTimeOff = findViewById(R.id.textViewTimeOff);
        textViewDateOff = findViewById(R.id.textViewDateOff);

        Button updateButton = findViewById(R.id.updateButton);

        // Set OnClickListener for the Update Button
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show a Yes/No dialog when the update button is clicked
                showConfirmationDialog();
            }
        });

        // Fetch and populate data
        fetchAndPopulateData();

        // Check schedule and update relay status

        // Set OnClickListener for the TextViews
        textViewTimeOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(textViewTimeOn);
            }
        });

        textViewDateOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(textViewDateOn);
            }
        });

        textViewTimeOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(textViewTimeOff);
            }
        });

        textViewDateOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(textViewDateOff);
            }
        });

        // Fetch and populate data
        fetchAndPopulateData();


    }

    // Helper method to show TimePickerDialog
    private void showTimePickerDialog(final TextView textView) {
        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String formattedTime = String.format("%02d:%02d", hourOfDay, minute);
                        textView.setText(formattedTime);
                    }
                }, hourOfDay, minute, true);

        timePickerDialog.show();
    }

    // Helper method to show DatePickerDialog
    private void showDatePickerDialog(final TextView textView) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String formattedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
                        textView.setText(formattedDate);
                    }
                }, year, month, dayOfMonth);

        datePickerDialog.show();
    }

    // Helper method to fetch and populate existing data
    private void fetchAndPopulateData() {
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String timeOn = snapshot.child("timeOn").getValue(String.class);
                    String timeOff = snapshot.child("timeOff").getValue(String.class);
                    String dateOn = snapshot.child("dateOn").getValue(String.class);
                    String dateOff = snapshot.child("dateOff").getValue(String.class);

                    textViewTimeOn.setText(timeOn != null ? timeOn : "Not set");
                    textViewDateOn.setText(dateOn != null ? dateOn : "Not set");
                    textViewTimeOff.setText(timeOff != null ? timeOff : "Not set");
                    textViewDateOff.setText(dateOff != null ? dateOff : "Not set");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors
            }
        });
    }

    // Method to handle the update when the Update Button is clicked
    private void showConfirmationDialog() {
        // Get the updated values from the TextViews
        String updatedTimeOn = textViewTimeOn.getText().toString();
        String updatedDateOn = textViewDateOn.getText().toString();
        String updatedTimeOff = textViewTimeOff.getText().toString();
        String updatedDateOff = textViewDateOff.getText().toString();

        if (!isValidTime(updatedTimeOn) || !isValidDate(updatedDateOn) || !isValidTime(updatedTimeOff) || !isValidDate(updatedDateOff)) {
            Toast.makeText(this, "Please fill in valid integer values for all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show a Yes/No dialog only if all fields are filled
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Update");
        builder.setMessage("Do you want to update/add the schedule?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User clicked Yes, perform the update
                updateSchedule(updatedTimeOn, updatedDateOn, updatedTimeOff, updatedDateOff);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User clicked No, do nothing or provide feedback
                Toast.makeText(edit_activity.this, "Update/add canceled", Toast.LENGTH_SHORT).show();
            }
        });

        builder.show();
    }


    private void updateSchedule(String updatedTimeOn, String updatedDateOn, String updatedTimeOff, String updatedDateOff) {
        // Update the values in the Realtime Database
        mDatabase.child("timeOn").setValue(updatedTimeOn);
        mDatabase.child("dateOn").setValue(updatedDateOn);
        mDatabase.child("timeOff").setValue(updatedTimeOff);
        mDatabase.child("dateOff").setValue(updatedDateOff);

        // Inform the user that the update is successful (you can use Toast or another UI element)
        Toast.makeText(edit_activity.this, "Schedule updated successfully", Toast.LENGTH_SHORT).show();


    }

    // Helper method to check if a string is a valid integer
    // Helper method to check if a string is a valid time in "HH:mm" format
    private boolean isValidTime(String value) {
        try {
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            timeFormat.setLenient(false);
            timeFormat.parse(value);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    // Helper method to check if a string is a valid date in "yyyy-MM-dd" format
    private boolean isValidDate(String value) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setLenient(false);
            dateFormat.parse(value);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }



}





