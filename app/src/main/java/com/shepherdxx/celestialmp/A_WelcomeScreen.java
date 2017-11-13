package com.shepherdxx.celestialmp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;



public class A_WelcomeScreen
        extends AppCompatActivity
{

    private TextView textView;
    private ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);
        setView();
        animate();
    }

    private void setView(){
        textView    = findViewById(R.id.ws_tv);
        imageView   = findViewById(R.id.ws_iv);
        Button b    = findViewById(R.id.ws_but);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(A_WelcomeScreen.this,B_MainScreen.class));
                finish();
            }
        });
    }

    private void animate(){
        Animation ws_animation= AnimationUtils.loadAnimation(this,R.anim.welcome_screen_transition);
        textView.startAnimation(ws_animation);
        imageView.startAnimation(ws_animation);
        t.start();
    }

    private Thread t = new Thread(){
        @Override
        public void run() {
            try {
                sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                startActivity(new Intent(A_WelcomeScreen.this,B_MainScreen.class));
                finish();
            }
        }
    };


}