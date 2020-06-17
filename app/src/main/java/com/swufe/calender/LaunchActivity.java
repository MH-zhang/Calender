package com.swufe.calender;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class LaunchActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_launch);
            if (getSupportActionBar() != null){
                getSupportActionBar().hide();
            }

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent main = new Intent(LaunchActivity.this,MainActivity.class);
                    startActivity(main);
                    finish();
                }
            },3000);
        }
}


