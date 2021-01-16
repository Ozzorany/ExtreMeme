package com.example.extrememe;

import android.app.Application;
import android.content.Context;

public class MemesApplication extends Application {
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
}
