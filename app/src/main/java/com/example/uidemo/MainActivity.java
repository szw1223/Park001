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
                            Thread.sleep(30);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();
            scanPorts();
            Intent intent = new Intent(MainActivity.this, booking.class);
            startActivity(intent);
        }
    }

    private void scanPorts() {
        new ScanPorts(1400, 1500).start();
    }

    class ScanPorts extends Thread {
        private int minPort;
        private int maxPort;

        public ScanPorts(int minport, int maxport){
            this.maxPort = maxport;
            this.minPort = minport;
        }

        public void run() {
            System.out.println(name + "  " + pwd);
            for (int i = minPort; i < maxPort; i++) {
                try {
                    //创建客户端Socket，指定服务器的IP地址和端口
                    Socket socket = new Socket("10.140.42.143",5002);
                    //获取输出流，向服务器发送数据
                    OutputStream os = socket.getOutputStream();
                    PrintWriter pw = new PrintWriter(os);
                    pw.write("register 12 12");
                    pw.flush();
                    //关闭输出流
                    socket.shutdownOutput();
                    pw.close();
                    os.close();
//                  socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }



//    class ScanPorts extends Thread {
//        private int minPort;
//        private int maxPort;
//
//        public ScanPorts(int minport, int maxport){
//            this.maxPort = maxport;
//            this.minPort = minport;
//        }
//
//        public void run() {
//            System.out.println(name + "  " + pwd);
//            for (int i = minPort; i < maxPort; i++) {
//                try {
//                    //创建客户端Socket，指定服务器的IP地址和端口
//                    Socket socket = new Socket("10.140.42.143",5002);
//                    //获取输出流，向服务器发送数据
//                    OutputStream os = socket.getOutputStream();
//                    PrintWriter pw = new PrintWriter(os);
//                    pw.write("register 12 12");
//                    pw.flush();
//                    //关闭输出流
//                    socket.shutdownOutput();
//                    pw.close();
//                    os.close();
////                  socket.close();
//                } catch (IOException e) {
//                        e.printStackTrace();
//                }
//            }
//        }
//
//    }
}

