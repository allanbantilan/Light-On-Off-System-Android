package com.scoringapp.powersystem;

import android.os.Bundle;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ToggleButton;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.widget.ImageView;


public class MainActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private boolean isRelay1On = false;
    private boolean isRelay2On = false;
    private boolean isRelay3On = false;
    private boolean isRelay4On = false;

    private ImageView imageViewRelay1;
    private ImageView imageViewRelay2;
    private ImageView imageViewRelay3;
    private ImageView imageViewRelay4;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getPreferences(Context.MODE_PRIVATE);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("devices").child("device1");



        imageViewRelay1 = findViewById(R.id.imageViewRelay1);
        imageViewRelay2 = findViewById(R.id.imageViewRelay2);
        imageViewRelay3 = findViewById(R.id.imageViewRelay3);
        imageViewRelay4 = findViewById(R.id.imageViewRelay4);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Boolean relay1State = dataSnapshot.child("relay1").getValue(Boolean.class);
                    Boolean relay2State = dataSnapshot.child("relay2").getValue(Boolean.class);
                    Boolean relay3State = dataSnapshot.child("relay3").getValue(Boolean.class);
                    Boolean relay4State = dataSnapshot.child("relay4").getValue(Boolean.class);

                    isRelay1On = relay1State != null && relay1State;
                    isRelay2On = relay2State != null && relay2State;
                    isRelay3On = relay3State != null && relay3State;
                    isRelay4On = relay4State != null && relay4State;

                    updateToggleButtonState((ToggleButton) findViewById(R.id.relay1Button), isRelay1On);
                    updateToggleButtonState2((ToggleButton) findViewById(R.id.relay2Button), isRelay2On);
                    updateToggleButtonState((ToggleButton) findViewById(R.id.relay3Button), isRelay3On);
                    updateToggleButtonState2((ToggleButton) findViewById(R.id.relay4Button), isRelay4On);

                    updateImageViewState(imageViewRelay1, relay1State);
                    updateImageViewState(imageViewRelay2, relay2State);
                    updateImageViewState(imageViewRelay3, relay3State);
                    updateImageViewState(imageViewRelay4, relay4State);
                }
            }
            private void updateImageViewState(ImageView imageView, Boolean state) {
                if (state != null) {
                    imageView.setImageResource(state ? R.drawable.switch_off : R.drawable.switch_on);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors
            }
        });
    }


    public void toggleRelay1(View view) {
        isRelay1On = !isRelay1On;
        updateToggleButtonState((ToggleButton) view, isRelay1On);
        mDatabase.child("relay1").setValue(isRelay1On);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("relay1", isRelay1On);
        editor.apply();
    }

    public void toggleRelay2(View view) {
        isRelay2On = !isRelay2On;
        updateToggleButtonState2((ToggleButton) view, isRelay2On);
        mDatabase.child("relay2").setValue(isRelay2On);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("relay2", isRelay2On);
        editor.apply();
    }

    public void toggleRelay3(View view) {
        isRelay3On = !isRelay3On;
        updateToggleButtonState((ToggleButton) view, isRelay3On);
        mDatabase.child("relay3").setValue(isRelay3On);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("relay3", isRelay3On);
        editor.apply();
    }

    public void toggleRelay4(View view) {
        isRelay4On = !isRelay4On;
        updateToggleButtonState2((ToggleButton) view, isRelay4On);
        mDatabase.child("relay4").setValue(isRelay4On);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("relay4", isRelay4On);
        editor.apply();
    }

    private void updateToggleButtonState(ToggleButton button, boolean state) {
        button.setChecked(state);
        button.setText(state ? "Lights On" : "Lights Off");
    }

    private void updateToggleButtonState2(ToggleButton button, boolean state) {
        button.setChecked(state);
        button.setText(state ? "Fans On" : "Fans Off");
    }
}
