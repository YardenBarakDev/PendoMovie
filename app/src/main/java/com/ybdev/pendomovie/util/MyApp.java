package com.ybdev.pendomovie.util;

import android.app.Application;

import com.ybdev.pendomovie.retrofit.MyRetrofit;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MyRetrofit.getInstance(); //initiate retrofit
    }
}