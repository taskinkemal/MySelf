package com.keplersegg.myself;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.keplersegg.myself.Helper.DataStorage;
import com.keplersegg.myself.Models.User;

public class MySelfApplication extends Application {

    public User user;
    public DataStorage dataStore = null;

    @Override
    public void onCreate() {

        super.onCreate();

        this.dataStore = new DataStorage(this);
        FacebookSdk.sdkInitialize(this);
    }
}
