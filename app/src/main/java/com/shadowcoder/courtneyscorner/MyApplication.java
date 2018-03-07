package com.shadowcoder.courtneyscorner;

import android.app.Application;

public class MyApplication extends Application {

    private static MyApplication singleton;

    @Override
    public void onCreate() {
        super.onCreate();

        singleton = this;
    }

    public static MyApplication getSingleton() {
        return singleton;
    }
}
