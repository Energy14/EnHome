package com.enelondroid.enhome;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final Button button = findViewById(R.id.button);


        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                executeRemoteCommand();
                Toast.makeText(MainActivity.this,
                        "Button pressed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void executeRemoteCommand() {
        String user = "pi";
        String password = "raspberry";
        String host = "192.168.8.111";
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
            channelExec.setCommand("cd Desktop && python blink.py");
            channelExec.connect();
            session.disconnect();
        } catch (JSchException e) {
            e.printStackTrace();
        }
    }
}

