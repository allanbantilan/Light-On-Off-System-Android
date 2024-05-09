package com.scoringapp.powersystem;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;

public class floor_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.floor);


        RelativeLayout floor1 = findViewById(R.id.floor1);
        RelativeLayout floor2 = findViewById(R.id.floor2);
        RelativeLayout floor3 = findViewById(R.id.floor3);


        floor1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event here
                // Start a new activity when the RelativeLayout is clicked
                Intent intent = new Intent(floor_activity.this, MainActivity.class);
                startActivity(intent);
            }
        });
//        floor2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Handle the click event here
//                // Start a new activity when the RelativeLayout is clicked
//                Intent intent = new Intent(floor_activity.this, inside_settings.class);
//                startActivity(intent);
//            }
//        });
//        floor3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Handle the click event here
//                // Start a new activity when the RelativeLayout is clicked
//                Intent intent = new Intent(floor_activity.this, schedule_activity.class);
//                startActivity(intent);
//            }
//        });
    }
}
