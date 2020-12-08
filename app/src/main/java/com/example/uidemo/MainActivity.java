package com.example.uidemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends Activity {

    public static final String NAME_PWD = "name_pwd";
    private static String name, pwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void register(View vas) {

        EditText nameEdt = findViewById(R.id.name);
        EditText pwdEdt = findViewById(R.id.pwd);
        final ProgressBar proBar = findViewById(R.id.pro_bar);
        name = nameEdt.getText().toString();
        pwd = pwdEdt.getText().toString();
        if (name.equals("") || pwd.equals("")) {
            Toast.makeText(this, "Name or password empty", Toast.LENGTH_SHORT).show();
        } else {
            proBar.setVisibility(View.VISIBLE);
            new Thread() {
                @Override
                public void run() {
                    for (int i = 0; i <= 100; i++) {
                        proBar.setProgress(i);
                        try {
                            Thread.sleep(20);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    new ScanPorts(5002).start();
                    Intent intent = new Intent(MainActivity.this, booking.class);
                    intent.putExtra(NAME_PWD, new String[]{name, pwd});
                    startActivity(intent);
                }
            }.start();
        }
    }

    class ScanPorts extends Thread {
        private int port;
        public ScanPorts(int port){
            this.port = port;
        }
        public void run() {
            System.out.println(name + "  " + pwd);
            try {
                //assign server address and port number
                Socket socket = new Socket("10.140.42.143",port);
                //send data
                OutputStream os = socket.getOutputStream();
                PrintWriter pw = new PrintWriter(os);
                pw.write("register " + name + " "+ pwd);
                pw.flush();
                //shut outputstream
                socket.shutdownOutput();
                pw.close();
                os.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

