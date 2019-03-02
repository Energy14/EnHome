package com.enelondroid.enhome;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.os.AsyncTask;

import com.jcraft.jsch.*;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final Button blinkButton = findViewById(R.id.blinkBut);
        final Button offButton = findViewById(R.id.turnOffBut);
        final Button onButton = findViewById(R.id.turnOnBut);


        blinkButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String[] params = {"cd Desktop && python blink.py"};
                new AsyncTaskRunner().execute(params);

                //executeRemoteCommand();
            }
        });
        offButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String[] params = {"cd Desktop && python turnOff.py"};
                new AsyncTaskRunner().execute(params);

                //executeRemoteCommand();
            }
        });
        onButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String[] params = {"cd Desktop && python turnOn.py"};
                new AsyncTaskRunner().execute(params);

                //executeRemoteCommand();
            }
        });
    }

    /*public void executeRemoteCommand() {

    }*/


    private class AsyncTaskRunner extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            String user = "pi";
            String password = "raspi";
            String host = "192.168.8.111";
            String command = params[0];

            int port = 22;

            try {
                JSch jsch = new JSch();
                Session session = jsch.getSession(user, host, port);
                session.setPassword(password);
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



