package com.keplersegg.myself.Activities

import android.content.Intent
import android.os.Bundle
import android.view.Window

import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.keplersegg.myself.Async.GetFacebookUser
import com.keplersegg.myself.Interfaces.ILoginHost
import com.keplersegg.myself.Interfaces.ISetUser
import com.keplersegg.myself.Interfaces.ISyncTasksHost
import com.keplersegg.myself.Async.LoginTask
import com.keplersegg.myself.Async.SyncTasks
import com.keplersegg.myself.Helper.TokenType
import com.keplersegg.myself.Models.User
import com.keplersegg.myself.R
import kotlinx.android.synthetic.main.activity_login.*

import java.util.Arrays

class LoginActivity : AuthActivity(), ISetUser, ILoginHost, ISyncTasksHost {

    private var fbLoginManager: LoginManager? = null
    private var fbCallbackManager: CallbackManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.requestWindowFeature(Window.FEATURE_NO_TITLE)

        setContentView(R.layout.activity_login)

        Glide.with(this)
                .load(R.drawable.login_background)
                .apply(RequestOptions.centerCropTransform())
                .into(imgLoginBackground)

        fbLoginManager = com.facebook.login.LoginManager.getInstance()
        fbCallbackManager = CallbackManager.Factory.create()
        fbLoginManager!!.registerCallback(fbCallbackManager!!, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                // here write code When Login successfully

                GetFacebookUser().Run(this@LoginActivity, loginResult.accessToken)
            }

            override fun onCancel() {

            }

            override fun onError(e: FacebookException) {
                // here write code when get error
            }
        })

        txtContinueWithoutAccount.setOnClickListener {
            //application.dataStore.setAccessToken("194044032133214245110100013098164116206239065216108230151152227093182051129179034209198215059120005198162060090156001124114155022206198130022107007035033187205131099148147216228217003192152060");
            NavigateToActivity("Main", true)
        }

        btnLoginFacebook.setOnClickListener {
            fbLoginManager!!.logInWithReadPermissions(this@LoginActivity, Arrays.asList("email", "public_profile", "user_birthday"))

            /*
                setToken(TokenType.Facebook, "EAAKGheh3UEEBAFnKaQGbGO2bG6r0zfRZBuk8Seq44Bk7a1hRrkrjpsOmAfK4QvA0UVHtmaoden4rkb4VlmiOsKjQMpuLbkPjWorzBvBgI26k0xSOYNMyfnfX3L3UE6KrsiTtK95N6c7U05qZBw5y0OZChAC4TkGcLQMbgaoO9t210qMgmBqs5oLEu8nptenUGQMZBHzwvMbv0xnvfDqxn3xB2nJPcPwsTQhaV4d3HgZDZD");

                User user = new User();
                user.Email = "open_ydzjppo_user@tfbnw.net";
                user.FirstName = "Jacob";
                user.LastName = "Crafty";

                setUser(user);
                */
        }

        btnLoginGoogle.setOnClickListener {
            val signInIntent = mGoogleSigninClient!!.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == RC_SIGN_IN) {

            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleGoogleSignInResult(task.result!!)

        } else {
            super.onActivityResult(requestCode, resultCode, data)
            fbCallbackManager!!.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun setUser(user: User?) {

        application!!.user = user

        if (user != null) {

            LoginTask(this).Run(TokenType.Facebook, application!!.dataStore!!.getFacebookToken(), application!!.user!!.Email)
        } else {

            application!!.dataStore!!.setAccessToken(null)
            showErrorMessage("Cannot authenticate via Facebook")
        }
    }

    override fun setToken(tokenType: TokenType, token: String?) {

        super.setToken(tokenType, token)
    }

    override fun onLoginSuccess() {

        SyncTasks(this).execute()
    }

    override fun onLoginError(message: String) {

        application!!.user = null
        showErrorMessage(message)
    }

    override fun setAccessToken(token: String) {

        application!!.dataStore!!.setAccessToken(token)
        SyncTasks(this).execute()
    }

    override fun onSyncTasksSuccess() {

        NavigateToActivity("Main", true)
    }

    companion object {
        private val RC_SIGN_IN = 430
    }
}
