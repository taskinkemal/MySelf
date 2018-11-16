package com.keplersegg.myself

import android.app.Application

import com.facebook.FacebookSdk
import com.keplersegg.myself.Helper.DataStorage
import com.keplersegg.myself.Models.User

class MySelfApplication : Application() {

    var user: User? = null
    var dataStore: DataStorage? = null

    override fun onCreate() {

        super.onCreate()

        this.dataStore = DataStorage(this)
        FacebookSdk.sdkInitialize(this)
    }
}