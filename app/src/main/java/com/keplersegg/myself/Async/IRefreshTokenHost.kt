package com.keplersegg.myself.Async

import com.keplersegg.myself.Interfaces.IErrorMessage

interface IRefreshTokenHost : IHttpProvider, IErrorMessage {

    fun onRefreshSuccess()

    fun onRefreshError(message: String)

    fun setAccessToken(token: String)
}
