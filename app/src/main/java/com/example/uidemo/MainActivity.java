package com.example.uidemo;

import android.app.Activity;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void register(View vas){

        EditText nameEdt = findViewById(R.id.name);
        EditText pwdEdt = findViewById(R.id.pwd);
        final ProgressBar proBar = findViewById(R.id.pro_bar);

        String name = nameEdt.getText().toString();
        String pwd = pwdEdt.getText().toString();
        if(name.equals("") || pwd.equals("")) {

            Toast.makeText(this,"Name or password empty",Toast.LENGTH_SHORT).show();
        }else {

            proBar.setVisibility(View.VISIBLE);
            new Thread(){
                @Override
                public void run() {
                    for(int i = 0 ; i <= 100 ; i++){
                        proBar.setProgress(i);
                        try {
                            Thread.sleep(30);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();
            Intent intent = new Intent(MainActivity.this, booking.class);
            startActivity(intent);
        }
    }
}
