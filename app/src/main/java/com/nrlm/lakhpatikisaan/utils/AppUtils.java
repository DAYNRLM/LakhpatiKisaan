package com.nrlm.lakhpatikisaan.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.nrlm.lakhpatikisaan.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.Random;

public class AppUtils {
    public static AppUtils utilsInstance;
    private TelephonyManager telephonyManager;

    public synchronized static AppUtils getInstance() {
        if (utilsInstance == null) {
            utilsInstance = new AppUtils();
        }
        return utilsInstance;
    }

    public void showLog(String logMsg, Class application) {
        if (AppConstant.wantToShow) {
            Log.d(application.getName(), logMsg);
        }
    }

    public String loadAssetData(Context context, String filName) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(filName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @NonNull
    public String getSha256(@NonNull String plain_text) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] hash = null;
        hash = digest.digest(plain_text.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(hash);
    }

    @NonNull
    private String bytesToHex(@NonNull byte[] hash) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1)
                hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public String getRandomOtp() {
        Random random = new Random();
        int otp = 1000 + random.nextInt(9000);
        return "" + otp;
    }

    public String removeComma(String string){
        return string.replaceAll("(,)*$", "");
    }

    public String getDeviceInfo() {
        String deviceInfo = "";
        try{
            deviceInfo = Build.MANUFACTURER + "-" + Build.DEVICE + "-" + Build.MODEL;
        }catch (Exception e){
            AppUtils.getInstance().showLog("Expection: "+e,AppDeviceInfoUtils.class);

        }

        if (deviceInfo.equalsIgnoreCase("")|| deviceInfo==null)
            return "123-dummy-123";

        //    appSharedPreferences.setDeviceInfo(deviceInfo);
        return deviceInfo;
    }

   /* public void setLocale(String localeName, Resources res) {
        Locale myLocale = new Locale(localeName);
        Locale.setDefault(myLocale);
        // Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.setLocale(myLocale);
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }*/


    @SuppressLint("HardwareIds")
    public String getIMEINo1(Context context) {
        String imeiNo1 = "";
        try {
            if (getSIMSlotCount(context) > 0) {
                if (ActivityCompat.checkSelfPermission(context,
                        Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                }

                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    imeiNo1 = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                    Build.getSerial();

                    AppUtils.getInstance().showLog("BUILD SERIAL "+ Build.getSerial(),AppDeviceInfoUtils.class);

                }else if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    imeiNo1 = telephonyManager.getDeviceId(0);

                }else if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
                    imeiNo1 ="dummy_123456789";
                }

            } else imeiNo1 = telephonyManager.getDeviceId();
        }catch (Exception e){
            AppUtils.getInstance().showLog("Expection: "+e,AppDeviceInfoUtils.class);
        }
        //appSharedPreferences.setImeiNumber(imeiNo1);
        AppUtils.getInstance().showLog("imeiiiiii: "+imeiNo1,AppDeviceInfoUtils.class);
        return imeiNo1;
    }


    private int getSIMSlotCount(Context context) {
        int getPhoneCount = 0;
        try {
            telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getPhoneCount = telephonyManager.getPhoneCount();
            }
        }catch (Exception e){
            AppUtils.getInstance().showLog("Expection: "+e,AppDeviceInfoUtils.class);
        }
        return getPhoneCount;
    }



    public String getAppVersion() {
        String appVersion = "";
        try {
            appVersion =  BuildConfig.VERSION_NAME;;
        }catch (Exception e){
            AppUtils.getInstance().showLog("Expection: "+e,AppDeviceInfoUtils.class);
        }
        return appVersion;
    }

    public String getAndroidApiVersion(){
        int version = 0;
        try {
            version =  Build.VERSION.SDK_INT;
        }catch (Exception e){
        }
        return String.valueOf(version);
    }




}
