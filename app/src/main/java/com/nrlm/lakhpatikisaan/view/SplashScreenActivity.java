package com.nrlm.lakhpatikisaan.view;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.nrlm.lakhpatikisaan.R;

import com.nrlm.lakhpatikisaan.repository.LoginRepo;
import com.nrlm.lakhpatikisaan.utils.AppDateFactory;
import com.nrlm.lakhpatikisaan.utils.AppExecutor;
import com.nrlm.lakhpatikisaan.utils.AppUtils;
import com.nrlm.lakhpatikisaan.utils.PreferenceFactory;
import com.nrlm.lakhpatikisaan.utils.PreferenceKeyManager;
import com.nrlm.lakhpatikisaan.view.auth.AuthActivity;
import com.nrlm.lakhpatikisaan.view.mpin.MpinActivity;

import java.util.Date;
import java.util.concurrent.ExecutionException;

public class SplashScreenActivity extends AppCompatActivity {
    private static final int SPLASH_SCREEN_TIME_OUT=2000;
    String login_statusPref="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        if(AppUtils.isDeviceRooted()){
            showAlertDialogAndExitApp("This device is rooted. You can't use this app.");
        }
        else {
            getLanguageCode();
            try {
                if(DaycheckForLogouts()){
                    PreferenceFactory.getInstance().removeSharedPrefrencesData(PreferenceKeyManager.getPrefLoginSessionKey(), SplashScreenActivity.this);
                    LoginRepo.getInstance(AppExecutor.getInstance().threadExecutor(), SplashScreenActivity.this).deleteAllMaster();            }
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        new Handler().postDelayed(this::goToNextScreen, SPLASH_SCREEN_TIME_OUT);

    }

    private boolean DaycheckForLogouts() throws ExecutionException, InterruptedException {
        boolean performLogout=false;
        String serverDateTimeandDays=getDateFromDB();    //This need to be check in Database
        if (serverDateTimeandDays!=null){
            String [] serverDTAndDs=serverDateTimeandDays.split(",");
            String logoutDate= AppDateFactory.getInstance().getNextDate(serverDTAndDs[0],Integer.parseInt(serverDTAndDs[1]),"dd-MM-yyyy");
            Date convertedLogOutDate = AppDateFactory.getInstance().getDateFormate(logoutDate);
            //set today date in right formate
            String todayDate =AppDateFactory.getInstance().changeDateValue(AppDateFactory.getInstance().getTodayDate());
            Date convertedTodayDate = AppDateFactory.getInstance().getDateFormate(todayDate);
            AppUtils.getInstance().showLog("serverDateTime"+serverDateTimeandDays,SplashScreenActivity.class);

            if(convertedTodayDate.compareTo(convertedLogOutDate)>=0){
                performLogout=true;
            }
        } else {
            serverDateTimeandDays="null";
            AppUtils.getInstance().showLog("serverDateTime"+serverDateTimeandDays,SplashScreenActivity.class);
        }
        return performLogout;
    }

    private String getDateFromDB() throws ExecutionException, InterruptedException {
        String currentDate = LoginRepo.getInstance(AppExecutor.getInstance().threadExecutor(), SplashScreenActivity.this).getServerDateTime();
        String logoutDays = LoginRepo.getInstance(AppExecutor.getInstance().threadExecutor(), SplashScreenActivity.this).getLogoutDays();
        //  return "25-03-2022"+","+ "1";
        if(currentDate!=null && logoutDays!=null) {
            return currentDate + "," + logoutDays;
        }
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
      /*  if(AppUtils.isDeviceRooted()){
            showAlertDialogAndExitApp("This device is rooted. You can't use this app.");
        }*/
    }

    public void showAlertDialogAndExitApp(String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(SplashScreenActivity.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                (dialog, which) -> {
                    dialog.dismiss();
                   /* Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);*/
                    finish();
                });

        alertDialog.show();
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

        if(loginStatus!=null && loginStatus.isEmpty()){
            Intent i = new Intent(SplashScreenActivity.this, AuthActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        }else {
            Intent intent = new Intent(SplashScreenActivity.this, MpinActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }
}