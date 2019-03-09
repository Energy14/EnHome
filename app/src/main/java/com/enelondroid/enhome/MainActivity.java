package com.enelondroid.enhome;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.os.AsyncTask;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ToggleButton;

import com.jcraft.jsch.*;

public class MainActivity extends AppCompatActivity {

    boolean isOn = false;
    boolean isInside = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //final Button blinkButton = findViewById(R.id.blinkBut);
        //final Button offButton = findViewById(R.id.turnOffBut);
        final ImageButton onButton = findViewById(R.id.turnOnBut);

        final SharedPreferences buttonState = getSharedPreferences("buttonState", 0);
        final SharedPreferences.Editor editor = buttonState.edit();
        ToggleButton whereBut = (ToggleButton) findViewById(R.id.whereBut);

        isOn = buttonState.getBoolean("isOn",false);
        isInside = buttonState.getBoolean("isInside", true);
        if(isOn){
            onButton.setColorFilter(Color.rgb( 44, 117, 255));
        }
        if(!isInside){
            whereBut.setChecked(true);
        }


        /*blinkButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String[] params = {"cd Desktop && python blink.py", "192.168.8.111"};
                new AsyncTaskRunner().execute(params);
            }
        });
        offButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String[] params = {"cd Desktop && python turnOff.py", "192.168.8.111"};
                new AsyncTaskRunner().execute(params);
            }
        });*/
        onButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(isOn) {
                    if(isInside){
                        String[] params = {"cd Desktop && python turnOff.py", "192.168.8.111"};
                        new AsyncTaskRunner().execute(params);
                    } else {
                        String[] params = {"cd Desktop && python turnOff.py", "enelon.ddns.net"};
                        new AsyncTaskRunner().execute(params);
                    }
                    isOn = false;
                    onButton.setColorFilter(null);
                    editor.putBoolean("isOn",false);
                    editor.apply();
                } else {
                    if(isInside){
                        String[] params = {"cd Desktop && python turnOn.py", "192.168.8.111"};
                        new AsyncTaskRunner().execute(params);
                    } else {
                        String[] params = {"cd Desktop && python turnOn.py", "enelon.ddns.net"};
                        new AsyncTaskRunner().execute(params);
                    }
                    isOn = true;
                    onButton.setColorFilter(Color.rgb( 44, 117, 255));
                    editor.putBoolean("isOn",true);
                    editor.apply();
                }
            }
        });
        whereBut.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled - outside
                    isInside = false;
                    editor.putBoolean("isInside",false);
                    editor.apply();
                } else {
                    // The toggle is disabled - inside
                    isInside = true;
                    editor.putBoolean("isInside",true);
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



