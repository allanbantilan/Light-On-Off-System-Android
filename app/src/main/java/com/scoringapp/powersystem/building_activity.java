package com.scoringapp.powersystem;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;

public class building_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buildings);


        RelativeLayout tofloor1 = findViewById(R.id.toFloor1);
//        RelativeLayout building2 = findViewById(R.id.building2);


        tofloor1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(building_activity.this, floor_activity.class);
                startActivity(intent);
            }
        });
    }
}