package com.example.dontforgetyourmoney;

import android.app.Application;
import android.content.Context;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class DontForgetYourMoneyApplication extends Application {
    private static DontForgetYourMoneyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this; // Application 인스턴스를 저장
    }

    public static DontForgetYourMoneyApplication getInstance() {
        return instance; // Application 인스턴스 반환
    }

    public static Context getAppContext() {
        return instance.getApplicationContext(); // Application Context 반환
    }
}
