package com.keplersegg.myself.Activities

import android.annotation.SuppressLint
import android.os.Bundle

@SuppressLint("Registered")
open class AuthActivity : MasterActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        OnCreateInternal()
    }

    internal fun OnCreateInternal() {

        //RetrieveRegisterID();

        LoginCheck()
    }

    private fun LoginCheck() {

        //        String accessToken = app.GetAccessToken();
        //        User user = app.user;
        //
        //        if (user == null) {
        //            if (accessToken != null && !accessToken.isEmpty()) {
        //
        //                // accesstoken shared preferences'ta dolu ama user bilgisi boş: uygulama yeni açılmış olmalı.
        //                new MyGetUserTask().Execute();
        //            }
        //            else if (master instanceof LoaderActivity) // login durumda değilse ve uygulama açılmışsa direk redirect.
        //                NavigateToActivity("Login", true);
        //        }
        //        else if (master instanceof LoaderActivity) // kullanıcı login durumda ve uygulama yeni açılmışsa direk redirect.
        //            NavigateToActivity("Home", true);
    }
}
