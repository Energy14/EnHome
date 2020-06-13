package com.enelondroid.enhome;

import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import android.content.SharedPreferences;
import android.graphics.Color;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    boolean isOn = false;
    boolean isLightOn = false;
    boolean isSecondLightOn;
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

        final ImageButton onButton = findViewById(R.id.turnOnBut);
        final ImageButton lightButton = findViewById(R.id.turnOnBut2);

        final SharedPreferences buttonState = getSharedPreferences("buttonState", 0);
        final SharedPreferences.Editor editor = buttonState.edit();


        isOn = buttonState.getBoolean("isOn",false);
        isLightOn = buttonState.getBoolean("isLightOn", false);
        isSecondLightOn = buttonState.getBoolean("isSecondLightOn", false);

        if(isOn){
            onButton.setColorFilter(Color.rgb( 44, 117, 255));
        }
        if(isLightOn) {
            lightButton.setColorFilter(Color.rgb( 44, 117, 255));
        }
        database = FirebaseDatabase.getInstance();
        roomLight = database.getReference("roomLight");
        powerCable = database.getReference("powerCable");


        onButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(isOn) {

                    isOn = false;
                    onButton.setColorFilter(null);
                    editor.putBoolean("isOn",false);
                    editor.apply();
                } else {

                    isOn = true;
                    onButton.setColorFilter(Color.rgb( 44, 117, 255));
                    editor.putBoolean("isOn",true);
                    editor.apply();
                }
            }
        });

        lightButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(isLightOn) {

                    isLightOn = false;
                    lightButton.setColorFilter(null);
                    editor.putBoolean("isLightOn",false);
                    editor.apply();
                } else {

                    isLightOn = true;
                    lightButton.setColorFilter(Color.rgb( 44, 117, 255));
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
