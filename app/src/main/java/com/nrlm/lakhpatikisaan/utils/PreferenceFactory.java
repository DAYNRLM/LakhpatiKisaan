package com.nrlm.lakhpatikisaan.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.nrlm.lakhpatikisaan.BuildConfig;

public class PreferenceFactory {
    @Nullable
    public static PreferenceFactory projectPrefrences = null;

    @Nullable
    public static PreferenceFactory getInstance() {

        if (projectPrefrences == null)
            projectPrefrences = new PreferenceFactory();
        return projectPrefrences;
    }

    @Nullable
    public String getSharedPrefrencesData(String key, @NonNull Context context) {
        String value = "";
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
            value = sharedPreferences.getString(key, "");
            return value;

        } catch (ClassCastException cce) {
            AppUtils.getInstance().showLog(String.valueOf(cce), PreferenceFactory.class);
        }

        return null;
    }

    public int getSharedPrefrencesIntegerData(String key, @NonNull Context context) {
        int value = 0;
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
            value = sharedPreferences.getInt(key, 0);
            return value;

        } catch (ClassCastException cce) {
            AppUtils.getInstance().showLog(String.valueOf(cce), PreferenceFactory.class);
        }

        return 0;
    }

    public boolean saveSharedPrefrecesData(String key, String value, @NonNull Context context) {
        SharedPreferences spref = context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
        SharedPreferences.Editor sprefEditor = spref.edit();
        sprefEditor.putString(key, value);
        boolean isDataSaved = sprefEditor.commit();
        sprefEditor.clear();
        return isDataSaved;
    }

    public boolean saveSharedPrefrecesData(String key, int value, @NonNull Context context) {
        SharedPreferences spref = context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
        SharedPreferences.Editor sprefEditor = spref.edit();
        sprefEditor.putInt(key, value);
        boolean isDataSaved = sprefEditor.commit();
        sprefEditor.clear();
        return isDataSaved;
    }

    public void removeSharedPrefrencesData(String key, @NonNull Context context) {
        SharedPreferences preferences = context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(key);
        editor.commit();
    }
}
