package com.keplersegg.myself.activities

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
import com.keplersegg.myself.MySelfApplication
import com.keplersegg.myself.async.GetFacebookUser
import com.keplersegg.myself.interfaces.ILoginHost
import com.keplersegg.myself.interfaces.ISetUser
import com.keplersegg.myself.interfaces.ISyncTasksHost
import com.keplersegg.myself.async.LoginTask
import com.keplersegg.myself.async.SyncTasks
import com.keplersegg.myself.helper.AutoTasksManager
import com.keplersegg.myself.helper.TokenType
import com.keplersegg.myself.models.User
import com.keplersegg.myself.R
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

import java.util.Arrays


class LoginActivity : AuthActivity(), ISetUser, ILoginHost, ISyncTasksHost {

    override fun GetApplication(): MySelfApplication {
        return app
    }

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
                setToken(TokenType.Facebook, loginResult.accessToken.token)
            }

            override fun onCancel() {

            }

            override fun onError(e: FacebookException) {
                // here write code when get error
            }
        })

        txtContinueWithoutAccount.setOnClickListener {

            doAsync {

                truncateDB(true)
                app.clearSession()
                SyncTasks(this@LoginActivity).execute()
            }
        }

        btnLoginFacebook.setOnClickListener {
            fbLoginManager!!.logInWithReadPermissions(this@LoginActivity, Arrays.asList("email", "public_profile", "user_birthday"))
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
            setUser(app.user!!, TokenType.Google)

        } else {
            super.onActivityResult(requestCode, resultCode, data)
            fbCallbackManager!!.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun setUser(user: User?, tokenType: TokenType) {

        app.user = user

        if (user != null) {

            val token = if (tokenType == TokenType.Facebook)
                app.dataStore.getFacebookToken()
            else app.dataStore.getGoogleToken()

            LoginTask(this).Run(tokenType,
                    token,
                    app.user!!.Email,
                    app.user!!.FirstName,
                    app.user!!.LastName,
                    app.user!!.PictureUrl)
        } else {

            app.dataStore.setAccessToken(null)
            showErrorMessage("Cannot authenticate via Facebook")
        }
    }

    override fun onLoginSuccess() {

        SyncTasks(this).execute()
    }

    override fun onLoginError(message: String) {

        app.user = null
        showErrorMessage(message)
    }

    override fun setAccessToken(token: String) {

        setToken(TokenType.MySelf, token)
        SyncTasks(this).execute()
    }

    override fun onSyncTasksSuccess() {
        goToMain()
    }

    private fun goToMain() {
        AutoTasksManager().Run(applicationContext, Runnable { })
        NavigateToActivity("Main", true)
    }

    companion object {
        private val RC_SIGN_IN = 430
    }
}
