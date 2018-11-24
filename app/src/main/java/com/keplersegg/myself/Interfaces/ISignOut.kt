package com.keplersegg.myself.Interfaces

import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.keplersegg.myself.Activities.MasterActivity
import com.keplersegg.myself.Models.User

interface ISignOut {

    fun onSignOut()

    fun GetUser(): User?

    fun GetMasterActivity(): MasterActivity

    fun GetGoogleSignInClient(): GoogleSignInClient
}
