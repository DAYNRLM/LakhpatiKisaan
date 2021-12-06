package com.nrlm.lakhpatikisaan.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.nrlm.lakhpatikisaan.R;

import com.nrlm.lakhpatikisaan.utils.PreferenceFactory;
import com.nrlm.lakhpatikisaan.utils.PreferenceKeyManager;
import com.nrlm.lakhpatikisaan.view.auth.AuthActivity;
import com.nrlm.lakhpatikisaan.view.home.HomeActivity;
import com.nrlm.lakhpatikisaan.view.mpin.MpinActivity;

public class SplashScreenActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN_TIME_OUT=4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                goToNextScreen();

            }
        }, SPLASH_SCREEN_TIME_OUT);
    }

    private void setLocal() {
       /* if(appSharedPreferences.getLanguageCode().equalsIgnoreCase("")){
            appSharedPreferences.setLanguageCode("en");
        }
        appUtils.setLocale(appSharedPreferences.getLanguageCode(),getResources());*/
    }

    private void goToNextScreen() {
        String loginStatus ="";
        loginStatus = PreferenceFactory.getInstance().getSharedPrefrencesData(PreferenceKeyManager.getPrefLoginSessionKey(),this);

        if(loginStatus.isEmpty()){
            Intent i = new Intent(SplashScreenActivity.this, AuthActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        }else {
            Intent i = new Intent(SplashScreenActivity.this, HomeActivity.class);
            startActivity(i);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
        }
    }
}