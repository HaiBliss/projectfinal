package com.timnhatro1.asus;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.multidex.MultiDexApplication;

import io.branch.referral.Branch;

public class MyApplication extends MultiDexApplication {

    private static boolean activityVisible;
    private static boolean firstTimeRun = true;
    private static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences mPrefs = getSharedPreferences(Constant.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        firstTimeRun = mPrefs.getBoolean(Constant.KEY_FIRST_TIME_RUN,true);
//        firstTimeRun = true;
        if (firstTimeRun) {
            SharedPreferences.Editor prefsEditor = mPrefs.edit();
            prefsEditor.putBoolean(Constant.KEY_FIRST_TIME_RUN, false);
            prefsEditor.apply();
        }
        // Initialize the Branch object
        Branch.getAutoInstance(this);
        mContext = getApplicationContext();
    }
    public static Context getAppContext() {
        return mContext;
    }
    public static boolean isFirstTimeRun() {
        return firstTimeRun;
    }

    public static boolean isActivityVisible() {
        return activityVisible;
    }
    public static void setActivityVisible(boolean visible) {
        activityVisible = visible;
    }
    public static void onResume() {
        activityVisible = true;
    }
    public static void onPause() {
        activityVisible = false;
    }
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        MultiDex.install(this);
//    }

//    @Override
//    protected void attachBaseContext(Context base) {
//        super.attachBaseContext(base);
//        MultiDex.install(this);
//    }
}
