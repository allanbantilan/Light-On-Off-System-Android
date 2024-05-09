package com.scoringapp.powersystem;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;

public class buildings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buildings);

        RelativeLayout buildingLayout = findViewById(R.id.building1);

        buildingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event here
                // Start a new activity when the RelativeLayout is clicked
                Intent intent = new Intent(buildings.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}