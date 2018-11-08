package com.keplersegg.myself.Helper

import android.content.Context
import android.content.SharedPreferences
import com.keplersegg.myself.MySelfApplication

class DataStorage(app: MySelfApplication) {

    private val preferences: SharedPreferences

    private val preferencesKey = "MyselfPreferences"
    private val accessToken = "accessToken"
    private val facebookToken = "facebookToken"
    private val googleToken = "googleToken"
    private val deviceRegistrationID = "deviceRegistrationID"

    init {

        preferences = app.getSharedPreferences(preferencesKey, Context.MODE_PRIVATE)
    }

    fun getAccessToken(): String? { return getValue(accessToken) }
    fun getFacebookToken(): String? { return getValue(facebookToken) }
    fun getGoogleToken(): String? { return getValue(googleToken) }
    fun getRegisterID(): String? { return getValue(deviceRegistrationID) }

    fun setAccessToken(token: String?) { setValue(accessToken, token) }
    fun setFacebookToken(token: String?) { setValue(facebookToken, token) }
    fun setGoogleToken(token: String?) { setValue(googleToken, token) }
    fun setRegisterID(registerID: String) { setValue(deviceRegistrationID, registerID) }

    private fun setValue(key: String, value: String?) {

        val editor = preferences.edit()

        editor.putString(key, value)

        editor.apply()
    }

    private fun getValue(key: String): String? {

        return if (preferences.contains(key))
            preferences.getString(key, null)
        else
            null
    }
}