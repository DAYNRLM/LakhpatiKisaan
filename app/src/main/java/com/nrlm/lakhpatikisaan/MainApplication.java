package com.nrlm.lakhpatikisaan;

import android.app.Application;

import com.nrlm.lakhpatikisaan.utils.AppUtils;

public class MainApplication extends Application {
   public AppUtils appUtils;

    @Override
    public void onCreate() {
        super.onCreate();
        appUtils =AppUtils.getInstance();
    }
}
