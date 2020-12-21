package com.example.uidemo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import static com.example.uidemo.MainActivity.host;
import static com.example.uidemo.MainActivity.port;

public class resultActivity extends AppCompatActivity {
    public TextView positionTV, checkTV;
    public Button checkButton, arriveButton;
    public String result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        findViews();
    }
    private void findViews() {
        positionTV = findViewById(R.id.textView2);
        checkTV = findViewById(R.id.textView3);
        checkButton = findViewById(R.id.button2);
        arriveButton = findViewById(R.id.button);
        if (getIntent() != null) {
            result = getIntent().getStringExtra((booking.POSIT));
        }
        positionTV.setText(result);
        positionTV.setTextSize(15);
    }
    public void arrive(View vas) {
        new Thread() {
            @Override
            public void run() {
                new ScanPorts01(port).start();
            }
        }.start();
    }

    class ScanPorts01 extends Thread {
        private int port;
        public ScanPorts01(int port) {
            this.port = port;
        }
        public void run() {
            try {
                //assign server address and port number
                Socket socket = new Socket(host, port);
                //send data
                OutputStream os = socket.getOutputStream();
                PrintWriter pw = new PrintWriter(os);
                pw.write("arrive " + result);
                pw.flush();
                System.out.println("----->rec start");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        arriveButton.setText("arrived !");
                        arriveButton.setTextSize(10);
                    }
                });

                System.out.println("----->rec start");
                //shut outputstream
                pw.close();
                os.close();
                socket.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void check(View vas) {
        new Thread() {
            @Override
            public void run() {
                new ScanPorts02(port).start();
            }
        }.start();
    }
    class ScanPorts02 extends Thread {
        private int port;
        public ScanPorts02(int port) {
            this.port = port;
        }
        public void run() {
            try {
                //assign server address and port number
                Socket socket = new Socket(host, port);
                //send data
                OutputStream os = socket.getOutputStream();
                PrintWriter pw = new PrintWriter(os);
                pw.write("check " + result);
                pw.flush();
                //shut outputstream
                InputStream is = socket.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                final String msg;
                System.out.println("----->rec start");
                msg = br.readLine();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        checkTV.setText(msg);
                        checkTV.setTextSize(10);
                    }
                });

                System.out.println("----->rec ="+msg);
                br.close();
                isr.close();
                is.close();
                pw.close();
                os.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}