package com.example.guswn.allthatlyrics.extension;

import android.app.Application;
import android.content.res.Resources;

public class App extends Application {
    private static App instance;
    private static Resources res;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        res = getResources();
    }

    public static App getInstance(){
        return instance;
    }
    public static Resources getResourses(){
        return res;
    }
}
