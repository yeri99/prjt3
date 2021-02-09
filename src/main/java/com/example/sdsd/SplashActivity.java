package com.example.sdsd;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

public class SplashActivity extends Activity {
    Thread splashThread;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        splashThread = new Thread(){
            @Override
            public void run() {
                try{
                    int waited = 0;
                    while(waited < 2000){
                        sleep(100);
                        waited += 100;
                    }
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    SplashActivity.this.finish();
                } catch (InterruptedException ignored) {
                }finally {
                    SplashActivity.this.finish();
                }
            }
        };
        splashThread.start();
    }
}
