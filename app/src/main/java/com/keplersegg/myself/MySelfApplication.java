package com.keplersegg.myself;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.keplersegg.myself.Models.User;

public class MySelfApplication extends Application {

    public User user;

    @Override
    public void onCreate() {

        super.onCreate();

        FacebookSdk.sdkInitialize(this);
    }
}
