package com.enelondroid.enhome;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.os.AsyncTask;
import android.widget.ImageButton;
import android.widget.Toast;

import com.jcraft.jsch.*;

public class MainActivity extends AppCompatActivity {

    boolean isOn = false;
    boolean isLightOn = false;
    boolean isInside = true;
    boolean isSecondLightOn;
    ActionBar toolbar;
    String insideIp;
    String outsideIp;
    String timeout;
    boolean useOutsideIp;


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
        //final Button secButton = findViewById(R.id.secBut);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);

        final SharedPreferences buttonState = getSharedPreferences("buttonState", 0);
        final SharedPreferences.Editor editor = buttonState.edit();

        insideIp = settings.getString("inside_ip", "0");
        useOutsideIp = settings.getBoolean("use_outside_ip", false);
        outsideIp = settings.getString("outside_ip", "5");
        timeout = settings.getString("ssh_timeout", "1000");


        isOn = buttonState.getBoolean("isOn",false);
        isLightOn = buttonState.getBoolean("isLightOn", false);
        isSecondLightOn = buttonState.getBoolean("isSecondLightOn", false);
        isInside = buttonState.getBoolean("isInside", true);
        if(isOn){
            onButton.setColorFilter(Color.rgb( 44, 117, 255));
        }
        if(isLightOn) {
            lightButton.setColorFilter(Color.rgb( 44, 117, 255));
        }
        if (insideIp.equals("0")) {
            Toast.makeText(this, "Set up your inside IP in settings!",
                    Toast.LENGTH_LONG).show();
        }
        if (outsideIp.equals("5") && useOutsideIp) {
            Toast.makeText(this, "Set up your outside IP in settings!",
                    Toast.LENGTH_LONG).show();
        }

        onButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (insideIp.equals("0")) {
                    Toast.makeText(MainActivity.this, "Set up your inside IP in settings!",
                            Toast.LENGTH_LONG).show();
                } else if (outsideIp.equals("5") && useOutsideIp) {
                    Toast.makeText(MainActivity.this, "Set up your outside IP in settings!",
                            Toast.LENGTH_LONG).show();
                }
                if(isOn) {
                    if (!useOutsideIp) {
                        String[] params = {"cd enhome && python mqttOff2.py", insideIp, timeout};
                        new MainActivity.AsyncTaskRunner().execute(params);
                    } else {
                        String[] params = {"cd enhome && python mqttOff2.py", insideIp, timeout};
                        new MainActivity.AsyncTaskRunner().execute(params);
                        String[] params2 = {"cd enhome && python mqttOff2.py", outsideIp, timeout};
                        new MainActivity.AsyncTaskRunner().execute(params2);
                    }
                    isOn = false;
                    onButton.setColorFilter(null);
                    editor.putBoolean("isOn",false);
                    editor.apply();
                } else {
                    if (!useOutsideIp) {
                        String[] params = {"cd enhome && python mqttOn2.py", insideIp, timeout};
                        new MainActivity.AsyncTaskRunner().execute(params);
                    } else {
                        String[] params = {"cd enhome && python mqttOn2.py", insideIp, timeout};
                        new MainActivity.AsyncTaskRunner().execute(params);
                        String[] params2 = {"cd enhome && python mqttOn2.py", outsideIp, timeout};
                        new MainActivity.AsyncTaskRunner().execute(params2);
                    }
                    isOn = true;
                    onButton.setColorFilter(Color.rgb( 44, 117, 255));
                    editor.putBoolean("isOn",true);
                    editor.apply();
                }
            }
        });

        lightButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (insideIp.equals("0")) {
                    Toast.makeText(MainActivity.this, "Set up your inside IP in settings!",
                            Toast.LENGTH_LONG).show();
                } else if (outsideIp.equals("5") && useOutsideIp) {
                    Toast.makeText(MainActivity.this, "Set up your outside IP in settings!",
                            Toast.LENGTH_LONG).show();
                }
                if(isLightOn) {
                    if (!useOutsideIp) {
                        String[] params = {"cd enhome && python mqttOff.py", insideIp, timeout};
                        new MainActivity.AsyncTaskRunner().execute(params);
                    } else {
                        String[] params = {"cd enhome && python mqttOff.py", insideIp, timeout};
                        new MainActivity.AsyncTaskRunner().execute(params);
                        String[] params2 = {"cd enhome && python mqttOff.py", outsideIp, timeout};
                        new MainActivity.AsyncTaskRunner().execute(params2);
                    }
                    isLightOn = false;
                    lightButton.setColorFilter(null);
                    editor.putBoolean("isLightOn",false);
                    editor.apply();
                } else {
                    if (!useOutsideIp) {
                        String[] params = {"cd enhome && python mqttOn.py", insideIp, timeout};
                        new MainActivity.AsyncTaskRunner().execute(params);
                    } else {
                        String[] params = {"cd enhome && python mqttOn.py", insideIp, timeout};
                        new MainActivity.AsyncTaskRunner().execute(params);
                        String[] params2 = {"cd enhome && python mqttOn.py", outsideIp, timeout};
                        new MainActivity.AsyncTaskRunner().execute(params2);
                    }
                    isLightOn = true;
                    lightButton.setColorFilter(Color.rgb( 44, 117, 255));
                    editor.putBoolean("isLightOn",true);
                    editor.apply();
                }
            }
        });
    }


    private class AsyncTaskRunner extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            String user = "pi";
            String password = "raspi";
            String host = params[1];
            String command = params[0];
            int sshTimeout = Integer.parseInt(params[2]);
            int port = 22;

            try {
                JSch jsch = new JSch();
                Session session = jsch.getSession(user, host, port);
                session.setPassword(password);
                session.setTimeout(sshTimeout);
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



