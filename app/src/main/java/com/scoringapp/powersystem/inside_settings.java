package com.scoringapp.powersystem;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;

public class inside_settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inside_settings);


        RelativeLayout buildingLayout = findViewById(R.id.update_user);

        buildingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event here
                // Start a new activity when the RelativeLayout is clicked
                Intent intent = new Intent(inside_settings.this, profile_activity.class);
                startActivity(intent);
            }
        });


    }
}