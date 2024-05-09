package com.scoringapp.powersystem;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.DatePicker;
import java.util.Calendar;

public class set_schedule extends Activity implements DatePickerDialog.OnDateSetListener {

    private AutoCompleteTextView daysOfWeekOn;
    private TimePicker timePickerOn;
    private AutoCompleteTextView daysOfWeekOff;
    private TimePicker timePickerOff;
    private Button addScheduleButton;
    private Button selectDateOnButton;
    private Button selectDateOffButton;
    private int yearOn, monthOn, dayOn, yearOff, monthOff, dayOff;
    private boolean isDateOn = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_schedule);

        // Initialize UI elements
        timePickerOn = findViewById(R.id.timePickerOn);
        timePickerOff = findViewById(R.id.timePickerOff);
        addScheduleButton = findViewById(R.id.addScheduleButton);
        selectDateOnButton = findViewById(R.id.selectDateOnButton);
        selectDateOffButton = findViewById(R.id.selectDateOffButton);

        // Create an ArrayAdapter for the days of the week
        ArrayAdapter<String> daysOfWeekAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line,
                getResources().getStringArray(R.array.days_of_week)
        );

        // Set the adapter for AutoCompleteTextViews
        // Add any AutoCompleteTextView setup as needed
        // ...

        selectDateOnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(true);
            }
        });

        selectDateOffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(false);
            }
        });

        addScheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Retrieve the selected data
                String selectedDaysOn = daysOfWeekOn.getText().toString();
                int hourOn = timePickerOn.getHour();
                int minuteOn = timePickerOn.getMinute();
                String selectedDaysOff = daysOfWeekOff.getText().toString();
                int hourOff = timePickerOff.getHour();
                int minuteOff = timePickerOff.getMinute();

                // Create an intent to send data back to ScheduleActivity
                Intent intent = new Intent();
                intent.putExtra("selectedDaysOn", selectedDaysOn);
                intent.putExtra("hourOn", hourOn);
                intent.putExtra("minuteOn", minuteOn);
                intent.putExtra("selectedDaysOff", selectedDaysOff);
                intent.putExtra("hourOff", hourOff);
                intent.putExtra("minuteOff", minuteOff);
                // Add selected dates
                intent.putExtra("yearOn", yearOn);
                intent.putExtra("monthOn", monthOn);
                intent.putExtra("dayOn", dayOn);
                intent.putExtra("yearOff", yearOff);
                intent.putExtra("monthOff", monthOff);
                intent.putExtra("dayOff", dayOff);

                // Set the result and finish this activity
                setResult(RESULT_OK, intent);
                finish(); // Return to ScheduleActivity
            }
        });
    }

    private void showDatePicker(final boolean isDateOn) {
        final Calendar c = Calendar.getInstance();
        int initialYear, initialMonth, initialDay;

        if (isDateOn) {
            initialYear = yearOn;
            initialMonth = monthOn;
            initialDay = dayOn;
        } else {
            initialYear = yearOff;
            initialMonth = monthOff;
            initialDay = dayOff;
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                set_schedule.this,
                this,
                initialYear,
                initialMonth,
                initialDay
        );

        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        if (isDateOn) {
            yearOn = year;
            monthOn = monthOfYear;
            dayOn = dayOfMonth;
            // Update the display with the selected date
            String selectedDateOn = yearOn + "-" + (monthOn + 1) + "-" + dayOn;
            // Set the selected date to the TextView or any other UI element
            // textViewDateOn.setText(selectedDateOn);
        } else {
            yearOff = year;
            monthOff = monthOfYear;
            dayOff = dayOfMonth;
            // Update the display with the selected date
            String selectedDateOff = yearOff + "-" + (monthOff + 1) + "-" + dayOff;
            // Set the selected date to the TextView or any other UI element
            // textViewDateOff.setText(selectedDateOff);
        }
    }
}
