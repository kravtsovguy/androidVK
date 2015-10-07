package com.baseteam.test4;

import android.app.Application;

import com.vk.sdk.VKSdk;

/**
 * Created by matvey on 07.10.15.
 */
public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        VKSdk.initialize(this);


    }
}