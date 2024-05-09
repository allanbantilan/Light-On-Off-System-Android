package com.scoringapp.powersystem;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import android.content.Context;
import android.content.SharedPreferences;


public class schedule_activity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private Handler handler = new Handler();
    private boolean scheduleDeleted = false;
    private boolean scheduleHasBeenSet = false;

   private int scheduleCounters = 0;
    private int scheduleCounter = 0; // Add this variable
    private static final int SCHEDULE_LIMIT = 2; // Set the schedule limit

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule);

        SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
        scheduleCounter = preferences.getInt("scheduleCounter", 0);

        // Initialize Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("devices").child("device1"); // Adjust this reference based on your database structure

        // Additional TextView elements
        TextView textViewTimeOn = findViewById(R.id.textViewTimeOn);
        TextView textViewDateOn = findViewById(R.id.textViewDateOn);
        TextView textViewTimeOff = findViewById(R.id.textViewTimeOff);
        TextView textViewDateOff = findViewById(R.id.textViewDateOff);

        // Set up a periodic task to check the schedule
        handler.postDelayed(scheduleChecker, 1000 * 60); // Check every minute (adjust as needed)

        // Set onClickListeners for "Not Set" TextViews
        setPickerClickListener(textViewTimeOn);
        setPickerClickListener(textViewDateOn);
        setPickerClickListener(textViewTimeOff);
        setPickerClickListener(textViewDateOff);


        // Set OnClickListener for the "Set Schedule" Button
        Button addScheduleButton = findViewById(R.id.back1);
        addScheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Fetch the existing schedule count from the database
                mDatabase.child("schedule").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        long existingSchedules = snapshot.getChildrenCount();

                        // The user can add a maximum of 2 schedules
                        if (existingSchedules < SCHEDULE_LIMIT) {
                            String timeOn = textViewTimeOn.getText().toString();
                            String dateOn = textViewDateOn.getText().toString();
                            String timeOff = textViewTimeOff.getText().toString();
                            String dateOff = textViewDateOff.getText().toString();

                            // Check if the schedule values are set
                            if (!timeOn.equals("Not Set") && !dateOn.equals("Not Set") &&
                                    !timeOff.equals("Not Set") && !dateOff.equals("Not Set")) {

                                // Create a unique schedule ID based on the current timestamp
                                String scheduleId = "Schedule " + (++scheduleCounters);


                                // Check if the counter exceeds the limit
                                if (scheduleCounters > SCHEDULE_LIMIT) {
                                    // Reset the counter to 1
                                    scheduleCounters = 1;
                                }
                                // Set the schedule in the Realtime Database under the unique ID
                                mDatabase.child("schedule").child(scheduleId).child("timeOn").setValue(timeOn);
                                mDatabase.child("schedule").child(scheduleId).child("dateOn").setValue(dateOn);
                                mDatabase.child("schedule").child(scheduleId).child("timeOff").setValue(timeOff);
                                mDatabase.child("schedule").child(scheduleId).child("dateOff").setValue(dateOff);

                                // Clear existing data
                                textViewTimeOn.setText("Not Set");
                                textViewDateOn.setText("Not Set");
                                textViewTimeOff.setText("Not Set");
                                textViewDateOff.setText("Not Set");

                                Toast.makeText(schedule_activity.this, "Schedule set successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(schedule_activity.this, "Please set all schedule values", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // Toast if the maximum schedule limit is reached
                            Toast.makeText(schedule_activity.this, "The Maximum for setting schedule is reached", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle errors
                        Log.e("ScheduleActivity", "Error retrieving existing schedules: " + error.getMessage());
                    }
                });
            }
        });



    }

    // Runnable to periodically check the schedule
    private Runnable scheduleChecker = new Runnable() {
        @Override
        public void run() {
            checkSchedule();
            handler.postDelayed(this, 1000 * 60); // Check every minute (adjust as needed)
        }
    };





    private void setPickerClickListener(TextView textView) {
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textView.getId() == R.id.textViewTimeOn || textView.getId() == R.id.textViewTimeOff) {
                    showTimePickerDialog(textView);
                } else if (textView.getId() == R.id.textViewDateOn || textView.getId() == R.id.textViewDateOff) {
                    showDatePickerDialog(textView);
                }
            }
        });
    }

    private void showTimePickerDialog(final TextView textViewToUpdate) {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
                        String selectedTime = String.format("%02d:%02d", hourOfDay, minute);
                        textViewToUpdate.setText(selectedTime);

                        // Save the selected schedule to the Realtime Database
                        saveScheduleToDatabase();
                    }
                }, hour, minute, false);

        timePickerDialog.show();
    }

    private void showDatePickerDialog(final TextView textViewToUpdate) {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String selectedDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        textViewToUpdate.setText(selectedDate);

                        // Save the selected schedule to the Realtime Database
                        saveScheduleToDatabase();
                    }
                }, year, month, day);

        datePickerDialog.show();
    }

    private void checkSchedule() {
        mDatabase.child("schedule").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot scheduleSnapshot : snapshot.getChildren()) {
                    String timeOn = scheduleSnapshot.child("timeOn").getValue(String.class);
                    String dateOn = scheduleSnapshot.child("dateOn").getValue(String.class);
                    String timeOff = scheduleSnapshot.child("timeOff").getValue(String.class);
                    String dateOff = scheduleSnapshot.child("dateOff").getValue(String.class);

                    relayControl(timeOn, dateOn, timeOff, dateOff);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors
                Log.e("ScheduleActivity", "Error retrieving schedule values: " + error.getMessage());
            }
        });
    }



    private void saveScheduleToDatabase() {
        // Implement saveScheduleToDatabase logic here if needed
        // ...
    }

    private void relayControl(String timeOn, String dateOn, String timeOff, String dateOff) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date currentTime = Calendar.getInstance().getTime();
            Date scheduleTimeOn = sdf.parse(dateOn + " " + timeOn);
            Date scheduleTimeOff = sdf.parse(dateOff + " " + timeOff);

            // Check if the schedule is in the past
            if (currentTime.after(scheduleTimeOff)) {
                // Schedule is in the past, delete the schedule node
                deletePastSchedules();
                scheduleDeleted = true;
                scheduleHasBeenSet = true;
                // Toast message for debugging or user feedback
                if (!scheduleHasBeenSet) {
                    Toast.makeText(schedule_activity.this, "Past schedules deleted", Toast.LENGTH_LONG).show();
                    // No need to reset scheduleHasBeenSet here
                }
            } else if (currentTime.before(scheduleTimeOn)) {
                // Schedule is in the future, set alarms or perform necessary actions
                // ...
//                scheduleHasBeenSet = true;
                // Toast message for debugging or user feedback
//                Toast.makeText(schedule_activity.this, "Schedule is set up", Toast.LENGTH_LONG).show();
            } else if (currentTime.equals(scheduleTimeOn) || (currentTime.after(scheduleTimeOn) && currentTime.before(scheduleTimeOff))) {
                // Turn on relays
                turnOnRelays();
                scheduleHasBeenSet = true;
                // Toast message for debugging or user feedback
                if (isCurrentTimeMatchingSchedule(currentTime, scheduleTimeOn)) {
                    Toast.makeText(schedule_activity.this, "Lights and fans turned On", Toast.LENGTH_LONG).show();
                }

                // Schedule is in progress, set a delay to turn off relays
                long delayOff = scheduleTimeOff.getTime() - currentTime.getTime();
                new Handler().postDelayed(() -> {
                    turnOffRelays();
                    // Schedule is in the past, delete past schedules
                    deletePastSchedules();
                    scheduleDeleted = true;
                    scheduleHasBeenSet = true;
                    scheduleHasBeenSet = false; // Reset the flag when the schedule is completed
                    // Toast message for debugging or user feedback
                    if (isCurrentTimeMatchingSchedule(Calendar.getInstance().getTime(), scheduleTimeOff)) {
                        Toast.makeText(schedule_activity.this, "Lights and fans turned Off", Toast.LENGTH_LONG).show();
                    }
                }, delayOff);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    // Helper method to check if the current time matches the schedule time
    private boolean isCurrentTimeMatchingSchedule(Date currentTime, Date scheduleTime) {
        return currentTime.equals(scheduleTime);
    }
    private void deletePastSchedules() {
        mDatabase.child("schedule").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> schedulesToDelete = new ArrayList<>();

                for (DataSnapshot scheduleSnapshot : snapshot.getChildren()) {
                    try {
                        String scheduleDate = scheduleSnapshot.child("dateOff").getValue(String.class);
                        String scheduleTime = scheduleSnapshot.child("timeOff").getValue(String.class);
                        Date scheduleDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(scheduleDate + " " + scheduleTime);

                        // Check if the schedule is in the past
                        if (Calendar.getInstance().getTime().after(scheduleDateTime)) {
                            // Schedule is in the past, collect its key for deletion
                            schedulesToDelete.add(scheduleSnapshot.getKey());
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                // Remove the collected schedules outside the loop
                for (String scheduleKey : schedulesToDelete) {
                    mDatabase.child("schedule").child(scheduleKey).removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors
                Log.e("ScheduleActivity", "Error deleting past schedules: " + error.getMessage());
            }
        });
    }

    private void turnOnRelays() {
        mDatabase.child("relay1").setValue(false);
        mDatabase.child("relay2").setValue(false);
        mDatabase.child("relay3").setValue(false);
        mDatabase.child("relay4").setValue(false);
    }

    private void turnOffRelays() {
        mDatabase.child("relay1").setValue(true);
        mDatabase.child("relay2").setValue(true);
        mDatabase.child("relay3").setValue(true);
        mDatabase.child("relay4").setValue(true);
    }
}
