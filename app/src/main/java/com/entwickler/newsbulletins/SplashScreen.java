package com.entwickler.newsbulletins;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {

    private ImageView splash_screen_image;
    private TextView splash_screen_title;
    Animation upper_animation, lower_animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);



        splash_screen_image=findViewById(R.id.splash_screen_image);
        splash_screen_title=findViewById(R.id.splash_screen_title);

        upper_animation = AnimationUtils.loadAnimation(this,R.anim.upper_animation);
        lower_animation = AnimationUtils.loadAnimation(this,R.anim.lower_animation);

        splash_screen_image.setAnimation(upper_animation);
        splash_screen_title.setAnimation(lower_animation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        },4000);

    }
}