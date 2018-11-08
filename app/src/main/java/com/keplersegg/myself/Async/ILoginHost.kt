package com.keplersegg.myself.Async

import com.keplersegg.myself.Interfaces.IErrorMessage

interface ILoginHost : IHttpProvider, IErrorMessage {

    fun onLoginSuccess()

    fun onLoginError(message: String)

    fun setAccessToken(token: String)
}
