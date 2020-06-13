package com.enelondroid.enhome;

import android.content.Intent;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class SettingsActivity extends AppCompatActivity {

    ActionBar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        toolbar = getSupportActionBar();
        toolbar.hide();
        toolbar.setTitle("Settings");
        setTheme(R.style.SettingsStyle);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        navigation.getMenu().findItem(R.id.settings).setChecked(true);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new settingsFragment()).commit();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.devices:
                    toolbar.setTitle("Devices");
                    Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.settings:
                    toolbar.setTitle("Settings");
                    return true;
                case R.id.modes:
                    toolbar.setTitle("Modes");
                    Intent intentModes = new Intent(SettingsActivity.this, ModesActivity.class);
                    startActivity(intentModes);
                    return true;
            }
            return false;
        }
    };

}
