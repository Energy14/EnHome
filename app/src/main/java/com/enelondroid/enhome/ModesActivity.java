package com.enelondroid.enhome;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class ModesActivity extends AppCompatActivity {

    ActionBar toolbar;
    String insideIp;
    String outsideIp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modes);

        toolbar = getSupportActionBar();
        toolbar.hide();
        toolbar.setTitle("Modes");

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        navigation.getMenu().findItem(R.id.modes).setChecked(true);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences buttonState = getSharedPreferences("buttonState", 0);
        final SharedPreferences.Editor editor = buttonState.edit();
        final ImageButton blackoutBut = findViewById(R.id.blackout_but);

        insideIp = settings.getString("inside_ip", "0");
        outsideIp = settings.getString("outside_ip", "5");

        blackoutBut.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String[] params = {"cd enhome && python blackout.py", insideIp};
                new ModesActivity.AsyncTaskRunner().execute(params);
                String[] params2 = {"cd enhome && python blackout.py", outsideIp};
                new ModesActivity.AsyncTaskRunner().execute(params2);
                editor.putBoolean("isOn",false);
                editor.apply();
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
                    Intent intentDevices = new Intent(ModesActivity.this, MainActivity.class);
                    startActivity(intentDevices);
                    return true;
                case R.id.settings:
                    toolbar.setTitle("Settings");
                    Intent intentSettings = new Intent(ModesActivity.this, SettingsActivity.class);
                    startActivity(intentSettings);
                    return true;
                case R.id.modes:
                    toolbar.setTitle("Modes");
                    return true;
            }
            return false;
        }
    };


    private class AsyncTaskRunner extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            String user = "pi";
            String password = "raspi";
            String host = params[1];
            String command = params[0];
            int port = 22;

            try {
                JSch jsch = new JSch();
                Session session = jsch.getSession(user, host, port);
                session.setPassword(password);
                session.setTimeout(1000);
                session.setConfig("StrictHostKeyChecking", "no");
                session.connect();
                // create the execution channel over the session
                ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
                // Set the command to execute on the channel and execute the command
                channelExec.setCommand(command);
                channelExec.connect();
                session.disconnect();
            } catch (JSchException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}