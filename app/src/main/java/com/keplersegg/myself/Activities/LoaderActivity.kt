package com.keplersegg.myself.Activities

import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ProgressBar

import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.facebook.AccessToken
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.keplersegg.myself.Async.GetFacebookUser
import com.keplersegg.myself.Interfaces.ILoginHost
import com.keplersegg.myself.Interfaces.IRefreshTokenHost
import com.keplersegg.myself.Interfaces.ISetUser
import com.keplersegg.myself.Async.LoginTask
import com.keplersegg.myself.Async.RefreshTokenTask
import com.keplersegg.myself.Helper.TokenType
import com.keplersegg.myself.Models.User
import com.keplersegg.myself.R
import kotlinx.android.synthetic.main.activity_loader.*
import android.app.job.JobScheduler
import android.content.Context
import com.keplersegg.myself.Services.AutomatedTaskService
import android.app.job.JobInfo
import android.content.ComponentName

class LoaderActivity : MasterActivity(), ISetUser, ILoginHost, IRefreshTokenHost {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loader)

        val ab = master!!.actionBar
        ab?.hide()

        Glide.with(this)
                .load(R.drawable.login_background)
                .apply(RequestOptions.centerCropTransform())
                .into(imgLoginBackground)

        RegisterAutomatedTaskService()
    }

    private fun LoginCheck() {

        val accessToken = application!!.dataStore!!.getAccessToken()

        if (accessToken != null && !accessToken.isEmpty()) {

            RefreshTokenTask(this).execute(accessToken, application!!.dataStore!!.getRegisterID())
        } else {
            LoginCheckSocial()
        }
    }

    private fun LoginCheckSocial() {

        val account = GoogleSignIn.getLastSignedInAccount(this)

        if (account != null) {
            if (handleGoogleSignInResult(account)) {

                LoginTask(this).Run(TokenType.Google, application!!.dataStore!!.getGoogleToken(),
                        application!!.user!!.Email,
                        application!!.user!!.FirstName,
                        application!!.user!!.LastName,
                        application!!.user!!.PictureUrl)
                return
            }
        }

        val accessToken = AccessToken.getCurrentAccessToken()
        val isLoggedIn = accessToken != null && !accessToken.isExpired

        if (isLoggedIn) {

            GetFacebookUser().Run(this, accessToken)
            return
        }

        /*        String googleToken = application.dataStore.getGoogleToken();
        String facebookToken = application.dataStore.getFacebookToken();

        if (googleToken != null) {
            //TODO:
        }

        if (facebookToken != null) {
            //TODO:

            accessToken = new AccessToken(
                    facebookToken,
                    getString(R.string.facebook_app_id),
                    null,
                    null, null, null, null, null, null);

            new GetFacebookUser().Run(this, accessToken);
            return;
        }*/

        application!!.user = null
        application!!.dataStore!!.setAccessToken(null)
        NavigateToActivity("Login", true)
    }

    override fun onResume() {
        super.onResume()

        FadeinView(prgBarLoader)
    }

    private fun FadeinView(prgBarLoader: ProgressBar) {

        val anim = AnimationUtils.loadAnimation(this, R.anim.loader)


        anim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {

            }

            override fun onAnimationEnd(animation: Animation) {

                LoginCheck()
            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })

        prgBarLoader.startAnimation(anim)
    }

    override fun setUser(user: User?) {

        application!!.user = user

        if (user != null) {

            LoginTask(this).Run(TokenType.Facebook, application!!.dataStore!!.getFacebookToken(),
                    application!!.user!!.Email,
                    application!!.user!!.FirstName,
                    application!!.user!!.LastName,
                    application!!.user!!.PictureUrl)
        } else {

            application!!.dataStore!!.setAccessToken(null)
            NavigateToActivity("Login", true)
        }
    }

    override fun setToken(tokenType: TokenType, token: String?) {

        super.setToken(tokenType, token)
    }

    override fun onLoginSuccess() {

        NavigateToActivity("Main", true)
    }

    override fun onLoginError(message: String) {

        application!!.user = null
        application!!.dataStore!!.setAccessToken(null)
        showErrorMessage(message)
        NavigateToActivity("Login", true)
    }

    override fun setAccessToken(token: String) {

        application!!.dataStore!!.setAccessToken(token)
    }

    override fun onRefreshSuccess() {
        NavigateToActivity("Main", true)
    }

    override fun onRefreshError(message: String) {
        LoginCheckSocial()
    }

    private fun RegisterAutomatedTaskService() {

        val jobScheduler = applicationContext
                .getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler

        val componentName = ComponentName(this, AutomatedTaskService::class.java)

        val jobInfo = JobInfo.Builder(1, componentName)
                .setPeriodic(15 * 60 * 1000, 5000)
                .setPersisted(true)
                .build()

        jobScheduler.schedule(jobInfo)
    }


}
