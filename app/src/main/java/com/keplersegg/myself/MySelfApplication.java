package com.keplersegg.myself;

import android.app.Application;

import com.facebook.FacebookSdk;

public class MySelfApplication extends Application {

    public String dummyData;

    @Override
    public void onCreate() {

        super.onCreate();

        FacebookSdk.sdkInitialize(this);

        dummyData = "";
    }
}
