package com.example.abimanyu.waitingtrackv10;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class SplashActivity extends Activity {
    private static final int splashTime = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        TextView waitingTrack = (TextView) findViewById(R.id.wTrack);
        Typeface custom_fonts = Typeface.createFromAsset(getAssets(), "font/ArgonPERSONAL-Regular.otf");
        waitingTrack.setTypeface(custom_fonts);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences shpr = getSharedPreferences("LOGGEDIN_SHARED_PREF",MODE_PRIVATE);
                Intent i = null;

                if (shpr.getBoolean("status",false)){
                    i = new Intent(SplashActivity.this, MenuActivity.class);
                } else{
                    i = new Intent(SplashActivity.this, LoginActivity.class);
                }
                startActivity(i);
                finish();
            }
        }, splashTime);
    };
}



