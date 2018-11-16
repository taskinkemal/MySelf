package com.keplersegg.myself.Interfaces

interface ILoginHost : IHttpProvider, IErrorMessage {

    fun onLoginSuccess()

    fun onLoginError(message: String)

    fun setAccessToken(token: String)
}
