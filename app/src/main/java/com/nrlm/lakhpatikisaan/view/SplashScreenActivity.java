package com.nrlm.lakhpatikisaan.view;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.nrlm.lakhpatikisaan.R;

import com.nrlm.lakhpatikisaan.utils.AppUtils;
import com.nrlm.lakhpatikisaan.utils.PreferenceFactory;
import com.nrlm.lakhpatikisaan.utils.PreferenceKeyManager;
import com.nrlm.lakhpatikisaan.view.auth.AuthActivity;
import com.nrlm.lakhpatikisaan.view.home.HomeActivity;
import com.nrlm.lakhpatikisaan.view.mpin.MpinActivity;

public class SplashScreenActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN_TIME_OUT=2000;

    String login_statusPref="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        setContentView(R.layout.activity_splash_screen);


        getLanguageCode();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                goToNextScreen();

            }
        }, SPLASH_SCREEN_TIME_OUT);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(AppUtils.getInstance().isDeviceRooted()){
            showAlertDialogAndExitApp("This device is rooted. You can't use this app.");
        }
    }

    public void showAlertDialogAndExitApp(String message) {

        AlertDialog alertDialog = new AlertDialog.Builder(SplashScreenActivity.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                });

        alertDialog.show();
    }

    private void setLocal() {
       /* if(appSharedPreferences.getLanguageCode().equalsIgnoreCase("")){
            appSharedPreferences.setLanguageCode("en");
        }
        appUtils.setLocale(appSharedPreferences.getLanguageCode(),getResources());*/
    }
    private void getLanguageCode() {
        String getLanguageCode = PreferenceFactory.getInstance().getSharedPrefrencesData(PreferenceKeyManager.getPrefLanguageCode(), SplashScreenActivity.this);
        if (getLanguageCode.equalsIgnoreCase("")) {
            getLanguageCode = "en";
        }

        AppUtils.getInstance().setLocale(getLanguageCode, getResources(), SplashScreenActivity.this);
    }
    private void goToNextScreen() {
        login_statusPref = PreferenceFactory.getInstance().getSharedPrefrencesData(PreferenceKeyManager.getPrefKeyLoginDone(), SplashScreenActivity.this);
        String loginStatus ="";
        loginStatus = PreferenceFactory.getInstance().getSharedPrefrencesData(PreferenceKeyManager.getPrefLoginSessionKey(),this);

        if(loginStatus.isEmpty()){
            Intent i = new Intent(SplashScreenActivity.this, AuthActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        }else {
           /* Intent i = new Intent(SplashScreenActivity.this, HomeActivity.class);
            startActivity(i);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();*/
            Intent intent = new Intent(SplashScreenActivity.this, MpinActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }
}