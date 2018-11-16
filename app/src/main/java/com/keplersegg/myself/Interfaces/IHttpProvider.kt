package com.keplersegg.myself.Interfaces

import android.net.ConnectivityManager
import com.keplersegg.myself.Interfaces.IErrorMessage

interface IHttpProvider: IErrorMessage {

    fun getAccessToken(): String?

    fun getDeviceId(): String?

    fun getConnectivityManager(): ConnectivityManager
}