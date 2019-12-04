package com.example.knowyourgov;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class splash extends AppCompatActivity {
    private static final int SPLASH_TIME_OUT = 2500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                // This run method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(splash.this, MainActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                // close this activity
                finish();
            }

        }, SPLASH_TIME_OUT);
    }


    @Override
    public void onBackPressed() {
    }

}
