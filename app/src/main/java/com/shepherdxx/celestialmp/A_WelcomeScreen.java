package com.shepherdxx.celestialmp;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import static java.lang.Thread.sleep;


public class A_WelcomeScreen
        extends AppCompatActivity
        implements Runnable
{

    private TextView textView;
    private ImageView imageView;

    public static final String ACTION_RESUME = "com.shepherdxx.celestialmp.RESUME";
    public static final String ACTION_START = "com.shepherdxx.celestialmp.START";

    private Handler myHandler=new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);
        if (MP_BG_Service.hasPlayer()) {
            skipIntro(true,ACTION_RESUME);
        }
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
                textView.clearAnimation();
                imageView.clearAnimation();
                skipIntro(true,ACTION_START);
            }
        });
    }

    long duration=0;
    private void animate(){
        Animation ws_animation= AnimationUtils.loadAnimation(this,R.anim.welcome_screen_transition);
        duration=ws_animation.getDuration();
        textView.startAnimation(ws_animation);
        imageView.startAnimation(ws_animation);
        skipIntro(false,ACTION_START);
    }

    void skipIntro(boolean skip,String action){
        this.action=action;
        if (skip) {
            duration=5;
            myHandler.removeCallbacks(this);}
        myHandler.postDelayed(this, duration);
    }

    @Override
    public void run() {
        try {
            sleep(duration);
            startActivity(
                    new Intent(A_WelcomeScreen.this, B_MainScreen.class)
                            .setAction(action));
            finish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private String action=ACTION_START;
}