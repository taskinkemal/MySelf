package com.keplersegg.myself.Interfaces

interface IRefreshTokenHost : IHttpProvider, IErrorMessage {

    fun onRefreshSuccess()

    fun onRefreshError(message: String)

    fun setAccessToken(token: String)
}
