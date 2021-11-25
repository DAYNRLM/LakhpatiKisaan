package com.nrlm.lakhpatikisaan.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import androidx.core.app.ActivityCompat;

import com.nrlm.lakhpatikisaan.BuildConfig;
import com.nrlm.lakhpatikisaan.MainApplication;

public class AppDeviceInfoUtils {
    Context context;

    private TelephonyManager telephonyManager;

    MainApplication mainApplication;

    public static AppDeviceInfoUtils deviceInfoutils = null;

    public static AppDeviceInfoUtils getInstance(Context context) {
        if (deviceInfoutils == null)
            deviceInfoutils = new AppDeviceInfoUtils(context);
        return deviceInfoutils;
    }
    public AppDeviceInfoUtils(Context context) {
        this.context = context;
    }


    @SuppressLint("MissingPermission")
    public String getIMEINo1() {
        String imeiNo1 = "";
        try {
            if (getSIMSlotCount() > 0) {
                if (ActivityCompat.checkSelfPermission(context,
                        Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                }

                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    imeiNo1 = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                    Build.getSerial();

                    mainApplication.appUtils.showLog("BUILD SERIAL ",AppDeviceInfoUtils.class);

                }else if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    imeiNo1 = telephonyManager.getDeviceId(0);

                }else if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
                    imeiNo1 =AppConstant.dummyImei;
                }

            } else imeiNo1 = telephonyManager.getDeviceId();
        }catch (Exception e){
            mainApplication.appUtils.showLog("Expection: "+e,AppDeviceInfoUtils.class);
        }
        mainApplication.appUtils.showLog("imeiiiiii: "+imeiNo1,AppDeviceInfoUtils.class);
        return imeiNo1;
    }


    private int getSIMSlotCount() {
        int getPhoneCount = 0;
        try {
            telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getPhoneCount = telephonyManager.getPhoneCount();
            }
        }catch (Exception e){
            mainApplication.appUtils.showLog("Expection: "+e,AppDeviceInfoUtils.class);
        }
        return getPhoneCount;
    }


    public String getDeviceInfo() {
        String deviceInfo = "";
        try{
            deviceInfo = Build.MANUFACTURER + "-" + Build.DEVICE + "-" + Build.MODEL;
        }catch (Exception e){
            mainApplication.appUtils.showLog("Expection: "+e,AppDeviceInfoUtils.class);

        }

        if (deviceInfo.equalsIgnoreCase("")|| deviceInfo==null)
            return "123-dummy-123";

        //    appSharedPreferences.setDeviceInfo(deviceInfo);
        return deviceInfo;
    }
    public String getAppVersion() {
        String appVersion = "";
        try {
            appVersion =  BuildConfig.VERSION_NAME;;
        }catch (Exception e){
            mainApplication.appUtils.showLog("Expection: "+e,AppDeviceInfoUtils.class);
        }
        return appVersion;
    }

    public String getApiVersion(){
        int version = 0;
        try {
            version =  Build.VERSION.SDK_INT;
        }catch (Exception e){
        }
        return String.valueOf(version);
    }


}
