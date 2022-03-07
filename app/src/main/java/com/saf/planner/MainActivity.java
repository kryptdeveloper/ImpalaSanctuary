package com.saf.planner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.planner.R;

public class MainActivity extends AppCompatActivity {
    ImageView logo,bg;
    private static int SplashTimeout = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.events_activity_main);
        bg = findViewById(R.id.splash_screen);
        //logo = findViewById(R.id.logo);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(MainActivity.this,Main2Activity.class);
                startActivity(i);
                finish();
            }
        },SplashTimeout);
        Animation splashScreen = AnimationUtils.loadAnimation(this,R.anim.splash_anim);
        bg.startAnimation(splashScreen);
        //logo.startAnimation(splashScreen);
    }
}
