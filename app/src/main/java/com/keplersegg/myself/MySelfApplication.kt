package com.keplersegg.myself

import android.app.Application

import com.keplersegg.myself.helper.DataStorage
import com.keplersegg.myself.models.User

class MySelfApplication : Application() {

    var user: User? = null
    var dataStore: DataStorage? = null

    override fun onCreate() {

        super.onCreate()

        this.dataStore = DataStorage(this)
    }
}