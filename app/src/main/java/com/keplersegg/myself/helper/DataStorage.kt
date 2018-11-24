package com.keplersegg.myself.helper

import android.content.Context
import android.content.SharedPreferences
import com.keplersegg.myself.MySelfApplication
import java.util.*

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
    fun getRegisterID(): String? {

        var registerID = getValue(deviceRegistrationID)

        if (registerID.isNullOrBlank()) {

            registerID = UUID.randomUUID().toString()
            setValue(deviceRegistrationID, registerID)
        }

        return registerID
    }

    fun setAccessToken(token: String?) { setValue(accessToken, token) }
    fun setFacebookToken(token: String?) { setValue(facebookToken, token) }
    fun setGoogleToken(token: String?) { setValue(googleToken, token) }

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