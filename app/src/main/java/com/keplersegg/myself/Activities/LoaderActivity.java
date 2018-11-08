package com.keplersegg.myself.Activities;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.keplersegg.myself.Async.GetFacebookUser;
import com.keplersegg.myself.Async.ILoginHost;
import com.keplersegg.myself.Async.IRefreshTokenHost;
import com.keplersegg.myself.Async.ISetUser;
import com.keplersegg.myself.Async.LoginTask;
import com.keplersegg.myself.Async.RefreshTokenTask;
import com.keplersegg.myself.Helper.TokenType;
import com.keplersegg.myself.Models.User;
import com.keplersegg.myself.R;

import org.jetbrains.annotations.NotNull;

public class LoaderActivity extends MasterActivity implements ISetUser, ILoginHost, IRefreshTokenHost {

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loader);

        ActionBar ab = master.getActionBar();
        if (ab != null)
            ab.hide();

        Glide.with(this)
                .load(R.drawable.login_background)
                .apply(RequestOptions.centerCropTransform())
                .into((ImageView)findViewById(R.id.imgLoginBackground));
    }

    private void LoginCheck() {

        String accessToken = application.dataStore.getAccessToken();

        if (accessToken != null && !accessToken.isEmpty()) {

            new RefreshTokenTask(this).execute(accessToken, application.dataStore.getRegisterID());
        }
        else {
            LoginCheckSocial();
        }
    }

    private void LoginCheckSocial() {

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        if (account != null)
        {
            if (handleGPlusSignInResult(account)) {

                new LoginTask(this).Run(TokenType.Google, application.dataStore.getGoogleToken(), application.user.Email);
                return;
            }
        }

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

        if (isLoggedIn) {

            new GetFacebookUser().Run(this, accessToken);
            return;
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

        application.user = null;
        application.dataStore.setAccessToken(null);
        NavigateToActivity("Login", true);
    }

    @Override
    protected void onResume() {
        super.onResume();

        ProgressBar prgBarLoader = findViewById(R.id.prgBarLoader);

        FadeinView(prgBarLoader);
    }

    private void FadeinView(ProgressBar prgBarLoader) {

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.loader);


        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                LoginCheck();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        prgBarLoader.startAnimation(anim);
    }

    @Override
    public void setUser(User user) {

        application.user = user;

        if (user != null) {

            new LoginTask(this).Run(TokenType.Facebook, application.dataStore.getFacebookToken(), application.user.Email);
        }
        else {

            application.dataStore.setAccessToken(null);
            NavigateToActivity("Login", true);
        }
    }

    @Override
    public void setToken(TokenType tokenType, String token) {

        super.setToken(tokenType, token);
    }

    @Override
    public void onLoginSuccess() {

        NavigateToActivity("Main", true);
    }

    @Override
    public void onLoginError(@NonNull String message) {

        application.user = null;
        application.dataStore.setAccessToken(null);
        showErrorMessage(message);
        NavigateToActivity("Login", true);
    }

    @Override
    public void setAccessToken(@NotNull String token) {

        application.dataStore.setAccessToken(token);
    }

    @Override
    public void onRefreshSuccess() {
        NavigateToActivity("Main", true);
    }

    @Override
    public void onRefreshError(@NotNull String message) {
        LoginCheckSocial();
    }
}
