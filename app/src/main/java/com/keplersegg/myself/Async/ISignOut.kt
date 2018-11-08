package com.keplersegg.myself.Async

import com.google.android.gms.common.api.GoogleApiClient
import com.keplersegg.myself.Models.User

interface ISignOut {

    fun onSignOut()

    fun GetUser(): User?

    fun GetGoogleApiClient(): GoogleApiClient
}
