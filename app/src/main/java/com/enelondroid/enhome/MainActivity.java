package com.enelondroid.enhome;

import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import android.content.SharedPreferences;
import android.graphics.Color;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    boolean isOn = false;
    boolean isLightOn = false;
    ActionBar toolbar;
    private FirebaseDatabase database;
    private DatabaseReference roomLight;
    private DatabaseReference powerCable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        toolbar = getSupportActionBar();
        toolbar.hide();
        toolbar.setTitle("Devices");
        navigation.getMenu().findItem(R.id.devices).setChecked(true);

        final ImageButton roomLightBut = findViewById(R.id.turnOnBut);
        final ImageButton powerCableBut = findViewById(R.id.turnOnBut2);

        final SharedPreferences buttonState = getSharedPreferences("buttonState", 0);
        final SharedPreferences.Editor editor = buttonState.edit();


        isOn = buttonState.getBoolean("isOn",false);
        isLightOn = buttonState.getBoolean("isLightOn", false);

        if(isOn){
            roomLightBut.setColorFilter(Color.rgb( 44, 117, 255));
        }
        if(isLightOn) {
            powerCableBut.setColorFilter(Color.rgb( 44, 117, 255));
        }
        database = FirebaseDatabase.getInstance();
        roomLight = database.getReference("roomLight");
        powerCable = database.getReference("powerCable");

        roomLight.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (String.valueOf(dataSnapshot.getValue()).equals("1")) {
                    isOn = false;
                    roomLightBut.setColorFilter(null);
                } else {
                    isOn = true;
                    roomLightBut.setColorFilter(Color.rgb(44,117,255));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Synchronization error",
                        Toast.LENGTH_LONG).show();
            }
        });

        powerCable.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (String.valueOf(dataSnapshot.getValue()).equals("1")) {
                    isOn = false;
                    powerCableBut.setColorFilter(null);
                } else {
                    isOn = true;
                    powerCableBut.setColorFilter(Color.rgb(44,117,255));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Synchronization error",
                        Toast.LENGTH_LONG).show();
            }
        });

        roomLightBut.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(isOn) {
                    roomLight.setValue(1);
                    isOn = false;
                    roomLightBut.setColorFilter(null);
                    editor.putBoolean("isOn",false);
                    editor.apply();
                } else {
                    roomLight.setValue(2);
                    isOn = true;
                    roomLightBut.setColorFilter(Color.rgb( 44, 117, 255));
                    editor.putBoolean("isOn",true);
                    editor.apply();
                }
            }
        });

        powerCableBut.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(isLightOn) {
                    powerCable.setValue(1);
                    isLightOn = false;
                    powerCableBut.setColorFilter(null);
                    editor.putBoolean("isLightOn",false);
                    editor.apply();
                } else {
                    powerCable.setValue(2);
                    isLightOn = true;
                    powerCableBut.setColorFilter(Color.rgb( 44, 117, 255));
                    editor.putBoolean("isLightOn",true);
                    editor.apply();
                }
            }
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.devices:
                    toolbar.setTitle("Devices");
                    return true;
                case R.id.settings:
                    toolbar.setTitle("Settings");
                    Intent intentSettings = new Intent(MainActivity.this, SettingsActivity.class);
                    startActivity(intentSettings);
                    return true;
                case R.id.modes:
                    toolbar.setTitle("Modes");
                    Intent intentModes = new Intent(MainActivity.this, ModesActivity.class);
                    startActivity(intentModes);
                    return true;
            }
            return false;
        }
    };
}
