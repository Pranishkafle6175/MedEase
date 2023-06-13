package com.example.medease;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;


public class SplashScreen extends AppCompatActivity {

    ImageView background,logo;
    //TextView name;
    LottieAnimationView lottie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        background= findViewById(R.id.img);
        logo= findViewById(R.id.Logo);
        //name= findViewById(R.id.app_name);
        logo= findViewById(R.id.lottieId);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent= new Intent(getApplicationContext(),onboardscreen.class);
                startActivity(intent);
            }
        },5000);


    }
}