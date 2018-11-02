package com.keplersegg.myself.Activities;

import android.annotation.SuppressLint;
import android.os.Bundle;

@SuppressLint("Registered")
public class AuthActivity extends MasterActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        OnCreateInternal();
    }

    void OnCreateInternal() {

        //RetrieveRegisterID();

        LoginCheck();
    }

    private void LoginCheck() {

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
